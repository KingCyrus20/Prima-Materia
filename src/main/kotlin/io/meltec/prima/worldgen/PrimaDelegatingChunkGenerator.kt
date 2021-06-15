package io.meltec.prima.worldgen

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.math.abs
import kotlin.math.round
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.noise.PerlinNoiseSampler
import net.minecraft.util.registry.Registry
import net.minecraft.world.ChunkRegion
import net.minecraft.world.HeightLimitView
import net.minecraft.world.Heightmap
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.StructureAccessor
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings
import net.minecraft.world.gen.chunk.NoiseChunkGenerator
import net.minecraft.world.gen.chunk.VerticalBlockSample

/**
 * Chunk generator which relies on another chunk generator to produce the basic noise. Adds rock
 * strata and other small modifications.
 */
class PrimaDelegatingChunkGenerator(
    val biomes: PrimaBiomes.ConvertingVanillaBiomeSource,
    val seed: Long,
    val settings: ChunkGeneratorSettings
) : ChunkGenerator(biomes, biomes, settings.structuresConfig, seed) {
  companion object {
    val CODEC: Codec<PrimaDelegatingChunkGenerator> =
        RecordCodecBuilder.create { inst ->
          inst.group(
                  PrimaBiomes.ConvertingVanillaBiomeSource.CODEC.fieldOf("biomes").forGetter {
                    it.biomes
                  },
                  Codec.LONG.fieldOf("seed").forGetter { it.seed },
                  ChunkGeneratorSettings.CODEC.fieldOf("settings").forGetter { it.settings },
              )
              .apply(inst, ::PrimaDelegatingChunkGenerator)
        }

    /** The size of the region within which strata control points are sampled. */
    val STRATA_REGION = Region(32)
    /** The size of the region in which ore control points are sampled. */
    val ORE_REGION = Region(8)
    /** The number of ore veins per ore region. */
    const val ORE_SPOTS_PER_REGION = 8
  }

  private val delegate = NoiseChunkGenerator(biomes, seed) { this.settings }
  private val strataNoise = PerlinNoiseSampler(ChunkRandom(seed))

  override fun populateBiomes(biomeRegistry: Registry<Biome>, chunk: Chunk) {
    super.populateBiomes(this.biomes.convertedRegistry, chunk)
  }

  /**
   * After adjusting biomes, the only step we need to do here is replace stone with the appropriate
   * stone layers.
   */
  override fun generateFeatures(region: ChunkRegion, accessor: StructureAccessor) {
    super.generateFeatures(region, accessor)

    this.buildRockLayers(region.getChunk(region.centerPos.centerX, region.centerPos.centerZ))
    this.buildOreVeins(region)
  }

  /** Build the 4 layers of rock strata top to bottom. */
  private fun buildRockLayers(chunk: Chunk) {
    val heightmap = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG)
    // Average of this height location and the average sea level, (/2)
    // then divide into 4 regions (/4).
    val regionHeight = round((heightmap[7, 7] + this.seaLevel) / 8.0).toInt()

    val strataControls =
        Array(4) { layer -> sampleStrataControlPoints(chunk.pos.x, chunk.pos.z, layer) }

    val position = BlockPos.Mutable()
    for (x in 0..15) {
      for (z in 0..15) {
        var strataLayer = 0
        var strataPosition = 0
        var strataHeight =
            sampleStrataHeight(
                regionHeight, x + chunk.pos.startX, z + chunk.pos.startZ, strataLayer)
        for (y in heightmap[x, z] downTo 0) {
          position.set(x, y, z)
          val existing = chunk.getBlockState(position).block
          if (existing != Blocks.STONE && existing != Blocks.SANDSTONE) continue

          val strataBlock =
              sampleStrata(
                  strataControls[strataLayer], x + chunk.pos.startX, y, z + chunk.pos.startZ)
          chunk.setBlockState(position, strataBlock, false)
          strataPosition += 1
          if (strataPosition >= strataHeight) {
            strataPosition = 0
            strataLayer = (strataLayer + 1).coerceAtMost(3)
            strataHeight =
                sampleStrataHeight(
                    regionHeight, x + chunk.pos.startX, z + chunk.pos.startZ, strataLayer)
          }
        }
      }
    }
  }

  private fun sampleStrataHeight(regionHeight: Int, x: Int, z: Int, layer: Int): Int =
      regionHeight +
          (this.seaLevel / 17.0 *
                  strataNoise.sample(x / 50.0, layer.toDouble(), z / 50.0, 0.0, 0.0))
              .toInt()

  /** Sample a specific strata block at a block position using the strata control points. */
  private fun sampleStrata(
      controls: Array<StrataControlPoint>,
      x: Int,
      y: Int,
      z: Int
  ): BlockState {
    val biasX = strataNoise.sample(x.toDouble() / 50.0, y / 50.0, z.toDouble() / 50.0, 0.0, 0.0)
    val biasZ =
        strataNoise.sample(x.toDouble() / 50.0, y / 50.0 + 127.0, z.toDouble() / 50.0, 0.0, 0.0)

    val dx = (biasX * STRATA_REGION.blockWidth / 3).toInt()
    val dz = (biasZ * STRATA_REGION.blockWidth / 3).toInt()

    return controls.minByOrNull { point -> abs(point.x - x - dx) + abs(point.z - z - dz) }?.rock
        ?: Blocks.STONE.defaultState
  }

  /** Sample the strata control points for the given layer. */
  private fun sampleStrataControlPoints(
      chunkX: Int,
      chunkZ: Int,
      layer: Int
  ): Array<StrataControlPoint> {
    val (centerRegionX, centerRegionZ) = STRATA_REGION.toRegionCoords(chunkX, chunkZ)

    return Array(9) { i ->
      val dx = (i / 3) - 1
      val dz = (i % 3) - 1

      val regionX = centerRegionX + dx
      val regionZ = centerRegionZ + dz

      val rng =
          ChunkRandom().also { it.setRegionSeed(this.seed, regionX, regionZ, layer * 1000 + 1337) }
      val blockState = PrimaStrata.sampleLayer(layer, rng)

      StrataControlPoint(
          regionX,
          regionZ,
          layer,
          regionX * STRATA_REGION.blockWidth + rng.nextInt(STRATA_REGION.blockWidth),
          regionZ * STRATA_REGION.blockWidth + rng.nextInt(STRATA_REGION.blockWidth),
          blockState)
    }
  }

  /** Sample locations for ore veins. */
  private fun buildOreVeins(region: ChunkRegion) {
    val oreSpots = numOrePoints(region.centerPos.centerX, region.centerPos.centerZ)
    if (oreSpots == 0) return

    val chunk = region.getChunk(region.centerPos.centerX, region.centerPos.centerZ)
    val rng =
        ChunkRandom().also { it.setTerrainSeed(region.centerPos.centerX, region.centerPos.centerZ) }
    for (spotId in 1..oreSpots) {
      val lx = rng.nextInt(16)
      val lz = rng.nextInt(16)
      // Generate a random height to generate based on the heightmap.
      val y = rng.nextInt(chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, lx, lz) - 4)

      val position = BlockPos.Mutable(chunk.pos.startX + lx, y, chunk.pos.startZ + lz)
      while (y > 0 && PrimaStrata.typeOf(region.getBlockState(position).block) == null) position
          .y -= 1

      val strataBlock = region.getBlockState(position).block
      val ore = PrimaOres.sampleOre(strataBlock, rng)
      val config = PrimaOres.oreConfig(ore, strataBlock)

      println("Generating ore vein ${ore.name} at ${position.x}, ${position.y}, ${position.z}")
      PerlinOreClusterFeature.generate(region, this, rng, position, config)
    }
  }

  /** Sample the ore locations within the region the chunk is inside. */
  private fun numOrePoints(chunkX: Int, chunkZ: Int): Int {
    val (regionX, regionZ) = ORE_REGION.toRegionCoords(chunkX, chunkZ)
    val rng = ChunkRandom().also { it.setRegionSeed(this.seed, regionX, regionZ, 1984) }

    var count = 0
    for (oreId in 1..ORE_SPOTS_PER_REGION) {
      val xPos = rng.nextInt(ORE_REGION.width)
      val zPos = rng.nextInt(ORE_REGION.width)
      if (xPos == chunkX % ORE_REGION.width && zPos == chunkZ % ORE_REGION.width) count += 1
    }

    return count
  }

  /** Delegates for methods which we let another chunk generator do. */
  override fun buildSurface(region: ChunkRegion?, chunk: Chunk?) =
      delegate.buildSurface(region, chunk)

  override fun populateNoise(
      executor: Executor,
      accessor: StructureAccessor?,
      chunk: Chunk?
  ): CompletableFuture<Chunk> = delegate.populateNoise(executor, accessor, chunk)

  override fun getHeight(
      x: Int,
      z: Int,
      heightmapType: Heightmap.Type?,
      world: HeightLimitView
  ): Int = delegate.getHeight(x, z, heightmapType, world)

  override fun getColumnSample(x: Int, z: Int, world: HeightLimitView): VerticalBlockSample =
      delegate.getColumnSample(x, z, world)

  /** May be configurable later; set sea level to half of world height. */
  override fun getSeaLevel(): Int = 127

  /** This is hardcoded in Minecraft currently; may change in later versions. */
  override fun getWorldHeight(): Int = 256

  /** Default spawn height; this takes no arguments so... good luck with your spawn. */
  override fun getSpawnHeight(world: HeightLimitView): Int = 130

  /** Default codec for saving/loading the world generator. */
  override fun getCodec(): Codec<out ChunkGenerator> = CODEC

  /** Create a new chunk generator with a new seed. */
  override fun withSeed(seed: Long): ChunkGenerator =
      PrimaDelegatingChunkGenerator(this.biomes, seed, this.settings)

  /** A strata control point which is used to determine the rock type in the given region. */
  data class StrataControlPoint(
      val regionX: Int,
      val regionZ: Int,
      val layer: Int,
      val x: Int,
      val z: Int,
      val rock: BlockState
  )

  /** Region object which tracks regions. */
  data class Region(val width: Int) {
    val blockWidth = width * 16

    /** Convert a chunk coordinate to it's corresponding region coordinate. */
    fun toRegionCoord(chunkCoord: Int) = (chunkCoord - width + 1) / width
    fun toRegionCoords(chunkX: Int, chunkZ: Int) = toRegionCoord(chunkX) to toRegionCoord(chunkZ)
  }
}

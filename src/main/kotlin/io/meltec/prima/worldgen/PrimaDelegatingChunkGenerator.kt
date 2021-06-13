package io.meltec.prima.worldgen

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlin.math.abs
import kotlin.math.round
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.noise.PerlinNoiseSampler
import net.minecraft.util.registry.Registry
import net.minecraft.world.BlockView
import net.minecraft.world.ChunkRegion
import net.minecraft.world.Heightmap
import net.minecraft.world.WorldAccess
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.StructureAccessor
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings
import net.minecraft.world.gen.chunk.NoiseChunkGenerator

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

    /** The width of each strata region in chunks; strata regions will be this size, on average. */
    const val STRATA_REGION_WIDTH = 32
    /** Computed value; the width of a strata region in blocks. */
    const val STRATA_REGION_WIDTH_BLOCKS = STRATA_REGION_WIDTH * 16
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

    this.buildRockLayers(region.getChunk(region.centerChunkX, region.centerChunkZ))
  }

  /** Build the 4 layers of rock strata top to bottom. */
  private fun buildRockLayers(chunk: Chunk) {
    val heightmap = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG)
    // Average of this height location and the average sea level, (/2)
    // then divide into 4 regions (/4).
    val regionHeight = round((heightmap[7, 7] + this.seaLevel) / 8.0)

    val strataControls =
        Array(4) { layer -> sampleStrataControlPoints(chunk.pos.x, chunk.pos.z, layer) }

    val position = BlockPos.Mutable()
    for (x in 0..15) {
      for (z in 0..15) {
        var strataLayer = 0
        var strataPosition = 0
        for (y in heightmap[x, z] downTo 0) {
          position.set(x, y, z)
          val existing = chunk.getBlockState(position).block
          if (existing != Blocks.STONE && existing != Blocks.SANDSTONE) continue

          val strataBlock =
              sampleStrata(strataControls[strataLayer], x + chunk.pos.startX, z + chunk.pos.startZ)
          chunk.setBlockState(position, strataBlock, false)
          strataPosition += 1
          if (strataPosition >= regionHeight) {
            strataPosition = 0
            strataLayer = (strataLayer + 1).coerceAtMost(3)
          }
        }
      }
    }
  }

  /** Sample a specific strata block at a block position using the strata control points. */
  private fun sampleStrata(controls: Array<StrataControlPoint>, x: Int, z: Int): BlockState {
    val biasX = strataNoise.sample(x.toDouble() / 50.0, 0.0, z.toDouble() / 50.0, 0.0, 0.0)
    val biasZ = strataNoise.sample(x.toDouble() / 50.0, 127.0, z.toDouble() / 50.0, 0.0, 0.0)

    val dx = (biasX * STRATA_REGION_WIDTH_BLOCKS / 3).toInt()
    val dz = (biasZ * STRATA_REGION_WIDTH_BLOCKS / 3).toInt()

    return controls.minByOrNull { point -> abs(point.x - x - dx) + abs(point.z - z - dz) }?.rock
        ?: Blocks.STONE.defaultState
  }

  /** Sample the strata control points for the given layer. */
  private fun sampleStrataControlPoints(
      chunkX: Int,
      chunkZ: Int,
      layer: Int
  ): Array<StrataControlPoint> {
    // Simulate division which rounds down instead of truncation.
    val centerRegionX = (chunkX - STRATA_REGION_WIDTH + 1) / STRATA_REGION_WIDTH
    val centerRegionZ = (chunkZ - STRATA_REGION_WIDTH + 1) / STRATA_REGION_WIDTH

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
          regionX * STRATA_REGION_WIDTH_BLOCKS + rng.nextInt(STRATA_REGION_WIDTH_BLOCKS),
          regionZ * STRATA_REGION_WIDTH_BLOCKS + rng.nextInt(STRATA_REGION_WIDTH_BLOCKS),
          blockState)
    }
  }

  /** Delegates for methods which we let another chunk generator do. */
  override fun buildSurface(region: ChunkRegion?, chunk: Chunk?) =
      delegate.buildSurface(region, chunk)

  override fun populateNoise(world: WorldAccess?, accessor: StructureAccessor?, chunk: Chunk?) =
      delegate.populateNoise(world, accessor, chunk)

  override fun getHeight(x: Int, z: Int, heightmapType: Heightmap.Type?): Int =
      delegate.getHeight(x, z, heightmapType)

  override fun getColumnSample(x: Int, z: Int): BlockView = delegate.getColumnSample(x, z)

  /** May be configurable later; set sea level to half of world height. */
  override fun getSeaLevel(): Int = 127

  /** This is hardcoded in Minecraft currently; may change in later versions. */
  override fun getWorldHeight(): Int = 256

  /** Default spawn height; this takes no arguments so... good luck with your spawn. */
  override fun getSpawnHeight(): Int = 130

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
}

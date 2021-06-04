package io.meltec.prima.worldgen

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.*
import kotlin.math.abs
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler
import net.minecraft.util.registry.Registry
import net.minecraft.world.BlockView
import net.minecraft.world.ChunkRegion
import net.minecraft.world.Heightmap
import net.minecraft.world.WorldAccess
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.source.BiomeAccess
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ProtoChunk
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.StructureAccessor
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.chunk.StructuresConfig
import net.minecraft.world.gen.chunk.VerticalBlockSample

/**
 * A custom "post-processing" chunk generator which pre-processes biomes to play nice with Prima
 * Materia, and post-processes the generated world to insert strata layers and custom cave
 * generation.
 */
class PrimaChunkGenerator(val biomes: PrimaBiomes.ConvertingVanillaBiomeSource, val seed: Long) :
    ChunkGenerator(biomes, biomes, StructuresConfig(Optional.empty(), mapOf()), seed) {
  companion object {
    val CODEC =
        RecordCodecBuilder.create<PrimaChunkGenerator> { inst ->
          inst.group(
                  PrimaBiomes.ConvertingVanillaBiomeSource.CODEC.fieldOf("biomes").forGetter {
                    it.biomes
                  },
                  Codec.LONG.fieldOf("seed").forGetter { it.seed },
              )
              .apply(inst, ::PrimaChunkGenerator)
        }

    /**
     * As a proportion of world height, how much depth affects average terrain level. A depth of 1.0
     * corresponds to an increase of DEPTH_SCALE * WORLD_HEIGHT (0.20 * 256 = 51).
     */
    val DEPTH_SCALE = 0.25

    /** How much to weight neighboring biome values when computing depth/scale. */
    val BIOME_WEIGHTS =
        Array(25) { i ->
          val x = i % 5
          val y = i / 5
          1.0 / (abs(2 - x) + abs(2 - y) + 1)
        }

    inline fun biomeWeight(dx: Int, dz: Int) = BIOME_WEIGHTS[(dx + 2) + (dz + 2) * 5]

    /** The default solid block used before post-processing. */
    val DEFAULT_SOLID_BLOCK = Blocks.STONE.defaultState
    /** The default liquid block used before post-processing. */
    val DEFAULT_LIQUID_BLOCK = Blocks.WATER.defaultState
  }

  private val random = ChunkRandom(seed)
  private val coreNoise = OctavePerlinNoiseSampler(this.random, (-15..0).toList())
  private val surfaceNoise = OctaveSimplexNoiseSampler(this.random, (-3..0).toList())

  override fun populateBiomes(biomeRegistry: Registry<Biome>, chunk: Chunk) {
    super.populateBiomes(this.biomes.convertedRegistry, chunk)
  }

  override fun buildSurface(region: ChunkRegion, chunk: Chunk) {
    val chunkStart = chunk.pos.startPos
    val position = BlockPos.Mutable()
    val localRandom = ChunkRandom()

    // Build the top of the chunk...
    for (dx in 0..15) {
      for (dz in 0..15) {
        val x = chunkStart.x + dx
        val z = chunkStart.z + dz

        val height = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, dx, dz) + 1
        val biome = region.getBiome(position.set(x, height, z))
        val surfaceBuilder = biome.generationSettings.surfaceBuilder.get()
        // TODO: Explain this fuckery
        val noise = this.surfaceNoise.sample(x * 0.0625, z * 0.0625, 0.0625, dx * 0.0625) * 15.0
        surfaceBuilder.initSeed(this.seed)
        surfaceBuilder.generate(
            localRandom,
            chunk,
            biome,
            dx,
            dz,
            height,
            noise,
            DEFAULT_SOLID_BLOCK,
            DEFAULT_LIQUID_BLOCK,
            this.seaLevel,
            this.seed,
        )
      }
    }

    // And then build a simple bedrock layer.
    for (dx in 0..15) {
      for (dz in 0..15) {
        chunk.setBlockState(position.set(dx, 0, dz), Blocks.BEDROCK.defaultState, false)
      }
    }
  }

  override fun populateNoise(world: WorldAccess, accessor: StructureAccessor, chunk: Chunk) {
    val protoChunk = chunk as ProtoChunk
    val oceanHeightmap = protoChunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG)
    val worldHeightmap = protoChunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG)

    val chunkStart = chunk.pos.startPos
    val position = BlockPos.Mutable()
    for (x in 0..15) {
      for (z in 0..15) {
        val (depth, scale) = averageBiomeDepthScale(chunkStart.x + x, chunkStart.z + z)

        val densityCenter = this.seaLevel + depth * this.worldHeight * DEPTH_SCALE
        val densityOffsetPerY = 2.0 / (this.worldHeight - densityCenter)

        for (y in 0..this.worldHeight) {
          val rawDensity =
              this.coreNoise.sample(
                  (chunkStart.x + x).toDouble() * 200.0,
                  y * 200.0,
                  (chunkStart.z + z).toDouble() * 200.0,
              )
          val density = rawDensity + densityOffsetPerY * (densityCenter - y)

          val state =
              if (density > 0.0) Blocks.STONE.defaultState
              else {
                if (y < this.seaLevel) Blocks.WATER.defaultState else Blocks.AIR.defaultState
              }

          protoChunk.setBlockState(position.set(x, y, z), state, false)
          oceanHeightmap.trackUpdate(x, y, z, state)
          worldHeightmap.trackUpdate(x, y, z, state)
        }
      }
    }
  }

  /** Compute the weighted average biome depth and scale at the given position. */
  private inline fun averageBiomeDepthScale(x: Int, z: Int): Pair<Double, Double> {
    var aggDepth = 0.0
    var aggScale = 0.0
    var denom = 0.0

    for (dx in -2..2) {
      for (dz in -2..2) {
        val biome = biomes.getBiomeForNoiseGen(x + dx, this.seaLevel, z + dz)
        val weight = biomeWeight(dx, dz)
        aggDepth += biome.depth * weight
        aggScale += biome.scale * weight
        denom += weight
      }
    }

    return Pair(aggDepth / denom, aggScale / denom)
  }

  override fun carve(seed: Long, access: BiomeAccess, chunk: Chunk, carver: GenerationStep.Carver) {
    super.carve(seed, access, chunk, carver)
  }

  override fun generateFeatures(region: ChunkRegion, accessor: StructureAccessor) {
    super.generateFeatures(region, accessor)
  }

  override fun getHeight(x: Int, z: Int, heightmapType: Heightmap.Type): Int = this.seaLevel

  override fun getColumnSample(x: Int, z: Int): BlockView {
    val blockStates = arrayOfNulls<BlockState>(1)
    return VerticalBlockSample(blockStates)
  }

  /** May be configurable later; set sea level to half of world height. */
  override fun getSeaLevel(): Int = 127

  /** This is hardcoded in Minecraft currently; may change in later versions. */
  override fun getWorldHeight(): Int = 256

  /** Default spawn height; this takes no arguments so... good luck with your spawn. */
  override fun getSpawnHeight(): Int = 130

  override fun getCodec(): Codec<out ChunkGenerator> = CODEC

  /** Create a copy of thisd world generator with a new seed. */
  override fun withSeed(seed: Long): ChunkGenerator {
    return PrimaChunkGenerator(biomes, seed)
  }
}

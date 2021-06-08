package io.meltec.prima.worldgen

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlin.math.round
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.BlockView
import net.minecraft.world.ChunkRegion
import net.minecraft.world.Heightmap
import net.minecraft.world.WorldAccess
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.Chunk
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

    val STRATA =
        arrayOf(
            Blocks.ANDESITE.defaultState,
            Blocks.DIORITE.defaultState,
            Blocks.GRANITE.defaultState,
            Blocks.BASALT.defaultState)
  }

  private val delegate = NoiseChunkGenerator(biomes, seed) { this.settings }

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
    val regionHeight = round(this.seaLevel / 4.0)
    val position = BlockPos.Mutable()
    for (x in 0..15) {
      for (z in 0..15) {
        var strataLayer = 0
        var strataPosition = 0
        for (y in heightmap[x, z] downTo 0) {
          position.set(x, y, z)
          val existing = chunk.getBlockState(position).block
          if (existing != Blocks.STONE && existing != Blocks.SANDSTONE) continue

          chunk.setBlockState(position, STRATA[strataLayer], false)
          strataPosition += 1
          if (strataPosition >= regionHeight) {
            strataPosition = 0
            strataLayer = (strataLayer + 1).coerceAtMost(STRATA.size - 1)
          }
        }
      }
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
}

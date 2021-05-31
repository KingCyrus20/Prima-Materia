package io.meltec.prima.feature

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.*
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.FeatureConfig

/**
 * Configure perlin ore generation. The width determines the X/Z bounding box size, while the height
 * determines the Y bounding box. The threshold should be between 0.0 and 1.0 and determine how high
 * the perlin noise has to be for a given tile to be a solid block; values between 0.1 and 0.4 are
 * recommended.
 */
data class PerlinOreClusterFeatureConfig(
    val state: BlockState,
    val width: Int,
    val height: Int,
    val threshold: Double
) : FeatureConfig {
  companion object {
    val CODEC =
        RecordCodecBuilder.create<PerlinOreClusterFeatureConfig> {
          it.group(
                  BlockState.CODEC.fieldOf("state").forGetter { it.state },
                  Codec.INT.fieldOf("width").forGetter { it.width },
                  Codec.INT.fieldOf("height").forGetter { it.height },
                  Codec.DOUBLE.fieldOf("threshold").forGetter { it.threshold },
              )
              .apply(it, ::PerlinOreClusterFeatureConfig)
        }
  }
}

/**
 * Ore generation which samples a perlin noise function over a cuboid bounding box to generate large
 * solid ore clusters with some structure.
 */
object PerlinOreClusterFeature :
    Feature<PerlinOreClusterFeatureConfig>(PerlinOreClusterFeatureConfig.CODEC) {
  private const val DOWNSCALE_FACTOR = 32.0

  override fun generate(
      world: StructureWorldAccess,
      chunkGenerator: ChunkGenerator,
      random: Random,
      pos: BlockPos,
      config: PerlinOreClusterFeatureConfig
  ): Boolean {
    val chRandom = ChunkRandom(random.nextLong())
    val octaveSampler = OctavePerlinNoiseSampler(chRandom, listOf(1, 2, 4))

    val delta = BlockPos((config.width - 1) / 2, (config.height - 1) / 2, (config.width - 1) / 2)
    val startPosition = pos.add(-delta.x, -delta.y, -delta.z)
    val endPosition = pos.add(delta)

    for (x in startPosition.x..endPosition.x) {
      for (y in startPosition.y..endPosition.y) {
        for (z in startPosition.z..endPosition.z) {
          val position = BlockPos(x, y, z)
          // if (world.getBlockState(position).block != Blocks.STONE) continue
          val distance =
              pos.getManhattanDistance(position).toDouble() /
                  config.width.coerceAtLeast(config.height)
          if (random.nextDouble() < distance) continue

          val noise =
              octaveSampler.sample(
                  x.toDouble() / DOWNSCALE_FACTOR,
                  y.toDouble() / DOWNSCALE_FACTOR,
                  z.toDouble() / DOWNSCALE_FACTOR)
          val sample = noise + 0.6 - distance
          if (sample < config.threshold) continue

          world.setBlockState(position, config.state, 0b110100)
        }
      }
    }

    return true
  }
}

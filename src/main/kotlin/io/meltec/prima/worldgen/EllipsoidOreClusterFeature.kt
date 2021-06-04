package io.meltec.prima.worldgen

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.FeatureConfig

/**
 * Configure a large, ellipsoid ore vein, which will generate a large ellipsoid of the given ore and
 * x/z/y radii. The core threshold (0-1)
 */
data class EllipsoidOreClusterFeatureConfig(
    val state: BlockState,
    val xRadius: Int,
    val yRadius: Int,
    val zRadius: Int,
    val coreThreshold: Double = 0.3,
    val edgeProbability: Double = 0.1
) : FeatureConfig {
  companion object {
    val CODEC =
        RecordCodecBuilder.create<EllipsoidOreClusterFeatureConfig> { inst ->
          inst.group(
                  BlockState.CODEC.fieldOf("state").forGetter { it.state },
                  Codec.INT.fieldOf("xRadius").forGetter { it.xRadius },
                  Codec.INT.fieldOf("yRadius").forGetter { it.yRadius },
                  Codec.INT.fieldOf("zRadius").forGetter { it.zRadius },
                  Codec.DOUBLE.fieldOf("coreThreshold").forGetter { it.coreThreshold },
                  Codec.DOUBLE.fieldOf("edgeProbability").forGetter { it.edgeProbability })
              .apply(inst, ::EllipsoidOreClusterFeatureConfig)
        }!!
  }
}

/** Generate huge, ellipsoid ore veins containing hundreds or thousands ore blocks. */
object EllipsoidOreClusterFeature :
    Feature<EllipsoidOreClusterFeatureConfig>(EllipsoidOreClusterFeatureConfig.CODEC) {
  override fun generate(
      world: StructureWorldAccess,
      chunkGenerator: ChunkGenerator,
      random: Random,
      pos: BlockPos,
      config: EllipsoidOreClusterFeatureConfig
  ): Boolean {
    // We are generating an ellipsoid; compute the bounding rectangle, iterate through, and fill in
    // ore blocks
    // that are actually inside of the ellipsoid.
    for (dx in -config.xRadius..config.xRadius) {
      for (dy in -config.yRadius..config.yRadius) {
        for (dz in -config.zRadius..config.zRadius) {
          val target = pos.add(dx, dy, dz)

          // Skip blocks we can't replace anyway.
          if (world.getBlockState(target).block != Blocks.STONE) continue

          val dist = ellipsoid(pos, target, config.xRadius, config.yRadius, config.zRadius)
          if (dist > 1.0) continue // Outside the ellipsoid.

          val prob =
              (1 - (dist - config.coreThreshold) / (1.0 - config.coreThreshold)) *
                  (1 - config.edgeProbability) + config.edgeProbability
          if (dist < config.coreThreshold || random.nextDouble() < prob) {
            world.setBlockState(target, config.state, 32.or(16).or(4))
          }
        }
      }
    }

    return true
  }

  /**
   * Compute ellipsoid distance between the center of the ellipsoid and target; 0 - 1 is inside the
   * ellipsoid, >1 is outside.
   */
  private fun ellipsoid(
      center: BlockPos,
      target: BlockPos,
      xRadius: Int,
      yRadius: Int,
      zRadius: Int
  ): Double {
    return sqrt(
        (center.x - target.x).absoluteValue.toDouble().pow(2) / (xRadius * xRadius) +
            (center.y - target.y).absoluteValue.toDouble().pow(2) / (yRadius * yRadius) +
            (center.z - target.z).absoluteValue.toDouble().pow(2) / (zRadius * zRadius))
  }
}

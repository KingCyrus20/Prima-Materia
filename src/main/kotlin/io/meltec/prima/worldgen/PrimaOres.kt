@file:Suppress("DEPRECATION") // BiomeModifications opt-in, sadly no better way

package io.meltec.prima.worldgen

import io.meltec.prima.block.PrimaOreBlocks
import io.meltec.prima.util.PrimaIdentifier
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.block.Block
import net.minecraft.structure.rule.BlockMatchRuleTest
import net.minecraft.structure.rule.RuleTest
import net.minecraft.tag.Tag
import net.minecraft.world.gen.decorator.Decorator
import net.minecraft.world.gen.decorator.RangeDecoratorConfig
import java.util.*

@SuppressWarnings("unused")
object PrimaOres {
  val ALL = TagRegistry.block(PrimaIdentifier("ores/all"))
  val SEDIMENTARY = TagRegistry.block(PrimaIdentifier("ores/sedimentary"))
  val METAMORPHIC = TagRegistry.block(PrimaIdentifier("ores/metamorphic"))
  val IGNEOUS = TagRegistry.block(PrimaIdentifier("ores/igneous"))

  /** Sample a valid ore which can be found in the given strata block at the given depth. */
  fun sampleOre(strata: Block, rng: Random): Block =
      tagByStrata(PrimaStrata.typeOf(strata) ?: Strata.SEDIMENTARY).getRandom(rng)

  /** Obtain the ore generation config for the given ore block and strata. */
  fun oreConfig(ore: Block, strata: Block): PerlinOreClusterFeatureConfig =
      PerlinOreClusterFeatureConfig(ore.defaultState, BlockMatchRuleTest(strata), 48, 24, 0.2)

  fun tagByStrata(strata: Strata): Tag<Block> = when(strata) {
    Strata.SEDIMENTARY -> SEDIMENTARY
    Strata.METAMORPHIC -> METAMORPHIC
    Strata.IGNEOUS -> IGNEOUS
  }
}

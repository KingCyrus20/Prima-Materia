@file:Suppress("DEPRECATION") // BiomeModifications opt-in, sadly no better way

package io.meltec.prima.worldgen

import io.meltec.prima.block.PrimaBlocks
import io.meltec.prima.util.PrimaIdentifier
import net.minecraft.util.registry.BuiltinRegistries
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.gen.decorator.Decorator
import net.minecraft.world.gen.decorator.RangeDecoratorConfig

object PrimaOres {
  val ORE_COPPER_OVERWORLD =
      PrimaFeatures.PERLIN_ORE_CLUSTER
          .configure(
              PerlinOreClusterFeatureConfig(PrimaBlocks.COPPER_ORE.defaultState, 48, 24, 0.2))
          .applyChance(1600)
          .decorate(Decorator.RANGE.configure(RangeDecoratorConfig(20, 0, 120)))
          .spreadHorizontally()!!

  val ORE_TIN_OVERWORLD =
      PrimaFeatures.PERLIN_ORE_CLUSTER
          .configure(PerlinOreClusterFeatureConfig(PrimaBlocks.TIN_ORE.defaultState, 20, 48, 0.2))
          .applyChance(1200)
          .decorate(Decorator.RANGE.configure(RangeDecoratorConfig(20, 0, 120)))
          .spreadHorizontally()!!

  val ORE_ZINC_OVERWORLD =
      PrimaFeatures.PERLIN_ORE_CLUSTER
          .configure(PerlinOreClusterFeatureConfig(PrimaBlocks.ZINC_ORE.defaultState, 32, 24, 0.2))
          .applyChance(1200)
          .decorate(Decorator.RANGE.configure(RangeDecoratorConfig(20, 0, 120)))
          .spreadHorizontally()!!

  fun register() {
    val oreCopperOverworld =
        RegistryKey.of(
            Registry.CONFIGURED_FEATURE_WORLDGEN, PrimaIdentifier("ore_copper_overworld"))
    Registry.register(
        BuiltinRegistries.CONFIGURED_FEATURE, oreCopperOverworld.value, ORE_COPPER_OVERWORLD)

    val oreTinOverworld =
        RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, PrimaIdentifier("ore_tin_overworld"))
    Registry.register(
        BuiltinRegistries.CONFIGURED_FEATURE, oreTinOverworld.value, ORE_TIN_OVERWORLD)

    val oreZincOverworld =
        RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, PrimaIdentifier("ore_zinc_overworld"))
    Registry.register(
        BuiltinRegistries.CONFIGURED_FEATURE, oreZincOverworld.value, ORE_ZINC_OVERWORLD)
  }
}

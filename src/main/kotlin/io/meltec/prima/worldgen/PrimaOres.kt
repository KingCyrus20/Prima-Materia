package io.meltec.prima.worldgen

import io.meltec.prima.PrimaBlocks
import io.meltec.prima.util.PrimaIdentifier
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.util.registry.BuiltinRegistries
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.decorator.Decorator
import net.minecraft.world.gen.decorator.RangeDecoratorConfig

object PrimaOres {
  val ORE_COPPER_OVERWORLD =
      PrimaFeatures.PERLIN_ORE_CLUSTER
          .configure(
              PerlinOreClusterFeatureConfig(PrimaBlocks.COPPER_ORE.defaultState, 64, 24, 0.2))
          .applyChance(300)
          .decorate(Decorator.RANGE.configure(RangeDecoratorConfig(140, 0, 64)))
          .spreadHorizontally()

  val ORE_TIN_OVERWORLD =
      PrimaFeatures.PERLIN_ORE_CLUSTER
          .configure(PerlinOreClusterFeatureConfig(PrimaBlocks.TIN_ORE.defaultState, 36, 20, 0.2))
          .applyChance(400)
          .decorate(Decorator.RANGE.configure(RangeDecoratorConfig(140, 0, 64)))
          .spreadHorizontally()

  val ORE_ZINC_OVERWORLD =
      PrimaFeatures.PERLIN_ORE_CLUSTER
          .configure(PerlinOreClusterFeatureConfig(PrimaBlocks.ZINC_ORE.defaultState, 32, 24, 0.2))
          .applyChance(400)
          .decorate(Decorator.RANGE.configure(RangeDecoratorConfig(140, 0, 64)))
          .spreadHorizontally()

  @SuppressWarnings("deprecated")
  fun register() {
    val oreCopperOverworld =
        RegistryKey.of(
            Registry.CONFIGURED_FEATURE_WORLDGEN, PrimaIdentifier("ore_copper_overworld"))
    Registry.register(
        BuiltinRegistries.CONFIGURED_FEATURE, oreCopperOverworld.value, ORE_COPPER_OVERWORLD)
    BiomeModifications.addFeature(
        BiomeSelectors.foundInOverworld(),
        GenerationStep.Feature.UNDERGROUND_ORES,
        oreCopperOverworld)

    val oreTinOverworld =
        RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, PrimaIdentifier("ore_tin_overworld"))
    Registry.register(
        BuiltinRegistries.CONFIGURED_FEATURE, oreTinOverworld.value, ORE_TIN_OVERWORLD)
    BiomeModifications.addFeature(
        BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, oreTinOverworld)

    val oreZincOverworld =
        RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, PrimaIdentifier("ore_zinc_overworld"))
    Registry.register(
        BuiltinRegistries.CONFIGURED_FEATURE, oreZincOverworld.value, ORE_ZINC_OVERWORLD)
    BiomeModifications.addFeature(
        BiomeSelectors.foundInOverworld(),
        GenerationStep.Feature.UNDERGROUND_ORES,
        oreZincOverworld)
  }
}

package io.meltec.prima

import io.meltec.prima.util.PrimaIdentifier
import net.minecraft.util.registry.BuiltinRegistries
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.gen.decorator.Decorator
import net.minecraft.world.gen.decorator.RangeDecoratorConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.OreFeatureConfig

object PrimaOres {
    val ORE_COPPER_OVERWORLD = Feature.ORE
        .configure(
            OreFeatureConfig(
                OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
                PrimaBlocks.COPPER_ORE.defaultState,
                64
            )
        )
        .decorate(Decorator.RANGE.configure(RangeDecoratorConfig(0, 0, 64)))
        .spreadHorizontally()
        .repeat(1)

    val ORE_TIN_OVERWORLD = Feature.ORE
        .configure(
            OreFeatureConfig(
                OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
                PrimaBlocks.TIN_ORE.defaultState,
                64
            )
        )
        .decorate(Decorator.RANGE.configure(RangeDecoratorConfig(0, 0, 64)))
        .spreadHorizontally()
        .repeat(1)

    val ORE_ZINC_OVERWORLD = Feature.ORE
        .configure(
            OreFeatureConfig(
                OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
                PrimaBlocks.ZINC_ORE.defaultState,
                64
            )
        )
        .decorate(Decorator.RANGE.configure(RangeDecoratorConfig(0, 0, 64)))
        .spreadHorizontally()
        .repeat(1)

    fun register() {
        val oreCopperOverworld = RegistryKey.of(
            Registry.CONFIGURED_FEATURE_WORLDGEN,
            PrimaIdentifier("ore_copper_overworld")
        )
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, oreCopperOverworld.value, ORE_COPPER_OVERWORLD)

        val oreTinOverworld = RegistryKey.of(
            Registry.CONFIGURED_FEATURE_WORLDGEN,
            PrimaIdentifier("ore_tin_overworld")
        )
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, oreTinOverworld.value, ORE_TIN_OVERWORLD)

        val oreZincOverworld = RegistryKey.of(
            Registry.CONFIGURED_FEATURE_WORLDGEN,
            PrimaIdentifier("ore_zinc_overworld")
        )
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, oreZincOverworld.value, ORE_ZINC_OVERWORLD)
    }
}
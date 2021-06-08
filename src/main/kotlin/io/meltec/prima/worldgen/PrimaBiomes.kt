package io.meltec.prima.worldgen

import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.mixin.biome.VanillaLayeredBiomeSourceAccessor
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.util.registry.RegistryLookupCodec
import net.minecraft.util.registry.SimpleRegistry
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.GenerationSettings
import net.minecraft.world.biome.source.BiomeSource
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource
import net.minecraft.world.gen.GenerationStep

/**
 * Lists custom Prima Materia biomes, and provides methods for converting from a vanilla biome to a
 * Prima Materia biome (by changing enabled features and temperature).
 */
object PrimaBiomes {
  /**
   * Convert an existing biome to a Prima Materia compatible biome by removing normal ores and
   * default underground generation, and adding custom ore generation.
   */
  fun convert(biome: Biome): Biome {
    val newGenSettings =
        GenerationSettings.Builder().surfaceBuilder(biome.generationSettings.surfaceBuilder)

    // Copy over features, stripping underground features by default.
    for ((index, features) in biome.generationSettings.features.withIndex()) {
      if (index == GenerationStep.Feature.UNDERGROUND_ORES.ordinal) continue
      if (index == GenerationStep.Feature.UNDERGROUND_STRUCTURES.ordinal) continue
      if (index == GenerationStep.Feature.UNDERGROUND_DECORATION.ordinal) continue

      for (feature in features) newGenSettings.feature(index, feature)
    }

    // Add Prima Materia custom ore generation...
    // TODO: This is temporary, rework how this works.
    newGenSettings.feature(GenerationStep.Feature.UNDERGROUND_ORES, PrimaOres.ORE_COPPER_OVERWORLD)
    newGenSettings.feature(GenerationStep.Feature.UNDERGROUND_ORES, PrimaOres.ORE_TIN_OVERWORLD)
    newGenSettings.feature(GenerationStep.Feature.UNDERGROUND_ORES, PrimaOres.ORE_ZINC_OVERWORLD)

    // Copy over the default carvers verbatim.
    for (carverStep in GenerationStep.Carver.values()) {
      for (carver in biome.generationSettings.getCarversForStep(carverStep)) {
        newGenSettings.carver(carverStep, carver.get())
      }
    }

    return Biome.Builder()
        .category(biome.category)
        .downfall(biome.downfall)
        .effects(biome.effects)
        .precipitation(biome.precipitation)
        .temperature(biome.temperature)
        .spawnSettings(biome.spawnSettings)
        .generationSettings(newGenSettings.build())
        .depth(biome.depth)
        .scale(biome.scale)
        .build()
  }

  /**
   * Convert an entire registry of biomes, returning a new registry with identical keys and modified
   * biomes.
   */
  fun convert(original: Registry<Biome>): Registry<Biome> {
    val registry = SimpleRegistry(Registry.BIOME_KEY, Lifecycle.stable())

    for (biomeKey in original.ids) {
      val biome = original[biomeKey] ?: continue
      registry.set(
          original.getRawId(biome),
          RegistryKey.of(Registry.BIOME_KEY, biomeKey),
          convert(biome),
          Lifecycle.stable())
    }

    return registry
  }

  /** A biome source which converts the internal biomes to the Prima Materia-friendly biomes. */
  class ConvertingVanillaBiomeSource(
      val seed: Long,
      val largeBiomes: Boolean,
      val rawRegistry: Registry<Biome>
  ) : BiomeSource(toBiomes(rawRegistry)) {
    companion object {
      val CODEC: Codec<ConvertingVanillaBiomeSource> =
          RecordCodecBuilder.create { inst ->
            inst.group(
                    Codec.LONG.fieldOf("seed").forGetter { it.seed },
                    Codec.BOOL.fieldOf("largeBiomes").forGetter { it.largeBiomes },
                    RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter { it.rawRegistry })
                .apply(inst) { seed, largeBiome, reg ->
                  ConvertingVanillaBiomeSource(seed, largeBiome, convert(reg))
                }
          }

      fun toBiomes(registry: Registry<Biome>): List<Biome> =
          VanillaLayeredBiomeSourceAccessor.getBIOMES().map { registry.getOrThrow(it) }
    }

    val convertedRegistry = convert(this.rawRegistry)
    private val delegate = VanillaLayeredBiomeSource(seed, false, largeBiomes, convertedRegistry)

    override fun getBiomeForNoiseGen(biomeX: Int, biomeY: Int, biomeZ: Int): Biome =
        delegate.getBiomeForNoiseGen(biomeX, biomeY, biomeZ)

    override fun getCodec(): Codec<out BiomeSource> = CODEC

    override fun withSeed(seed: Long): BiomeSource =
        ConvertingVanillaBiomeSource(seed, largeBiomes, rawRegistry)
  }
}

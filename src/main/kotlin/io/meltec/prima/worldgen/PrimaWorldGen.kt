package io.meltec.prima.worldgen

import io.meltec.prima.util.PrimaIdentifier
import java.util.*
import net.minecraft.block.Blocks
import net.minecraft.client.world.GeneratorType
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings
import net.minecraft.world.gen.chunk.GenerationShapeConfig
import net.minecraft.world.gen.chunk.NoiseSamplingConfig
import net.minecraft.world.gen.chunk.SlideConfig
import net.minecraft.world.gen.chunk.StructuresConfig

/** Registers world generation options. */
object PrimaWorldGen {
  private val DELEGATING_PRIMA_MATERIA =
      object : GeneratorType("prima_materia") {
        override fun getChunkGenerator(
            biomeRegistry: Registry<Biome>,
            chunkGeneratorSettingsRegistry: Registry<ChunkGeneratorSettings>,
            seed: Long
        ): ChunkGenerator {
          val registry = PrimaBiomes.convert(biomeRegistry)
          return PrimaDelegatingChunkGenerator(
              PrimaBiomes.ConvertingVanillaBiomeSource(seed, false, registry),
              seed,
              ChunkGeneratorSettings(
                  StructuresConfig(Optional.empty(), mapOf()),
                  GenerationShapeConfig(
                      256,
                      NoiseSamplingConfig(1.29999999814507745, 0.9999999814507745, 80.0, 160.0),
                      SlideConfig(-10, 3, 0),
                      SlideConfig(-30, 0, 0),
                      1,
                      2,
                      1.0,
                      0.02,
                      true,
                      true,
                      false,
                      false),
                  Blocks.STONE.defaultState,
                  Blocks.WATER.defaultState,
                  -10,
                  0,
                  127,
                  false))
        }
      }

  fun register() {
    // Register the custom converting biome source.
    Registry.register(
        Registry.BIOME_SOURCE,
        PrimaIdentifier("vanilla"),
        PrimaBiomes.ConvertingVanillaBiomeSource.CODEC)

    // Register the chunk generator so it can be saved/loaded.
    Registry.register(
        Registry.CHUNK_GENERATOR, PrimaIdentifier("vanilla"), PrimaDelegatingChunkGenerator.CODEC)

    // Add the chunk generator to the default list of chunk generators.
    GeneratorType.VALUES.add(DELEGATING_PRIMA_MATERIA)
  }
}

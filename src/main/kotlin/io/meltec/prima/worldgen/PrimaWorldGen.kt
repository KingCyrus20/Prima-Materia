package io.meltec.prima.worldgen

import net.minecraft.block.Blocks
import net.minecraft.client.world.GeneratorType
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource
import net.minecraft.world.gen.chunk.*

/** Registers world generation options. */
object PrimaWorldGen {
  val PRIMA_MATERIA =
      object : GeneratorType("prima_materia") {
        override fun getChunkGenerator(
            biomeRegistry: Registry<Biome>,
            chunkGeneratorSettingsRegistry: Registry<ChunkGeneratorSettings>,
            seed: Long
        ): ChunkGenerator {
          return NoiseChunkGenerator(
              VanillaLayeredBiomeSource(seed, false, true, biomeRegistry), seed) {
            ChunkGeneratorSettings(
                StructuresConfig(false),
                GenerationShapeConfig(
                    256,
                    NoiseSamplingConfig(0.9999999814507745, 0.9999999814507745, 80.0, 160.0),
                    SlideConfig(-10, 3, 0),
                    SlideConfig(-30, 0, 0),
                    1,
                    2,
                    1.0,
                    0.00,
                    true,
                    true,
                    false,
                    false),
                Blocks.STONE.defaultState,
                Blocks.WATER.defaultState,
                -10,
                0,
                127,
                false)
          }
        }
      }

  fun register() {
    GeneratorType.VALUES.add(0, PRIMA_MATERIA)
  }
}

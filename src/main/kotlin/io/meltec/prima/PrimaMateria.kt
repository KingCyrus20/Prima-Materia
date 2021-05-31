package io.meltec.prima

import io.meltec.prima.feature.PrimaFeatures
import io.meltec.prima.feature.PrimaOres
import io.meltec.prima.feature.PrimaWorldGen
import io.meltec.prima.item.PrimaItems
import net.fabricmc.api.ModInitializer

/** Primary entry point for the mod; mainly responsible for lots of registry additions. */
object PrimaMateria : ModInitializer {
  override fun onInitialize() {
    PrimaFeatures.register()
    PrimaItems.register()
    PrimaBlocks.register()
    PrimaOres.register()
    PrimaWorldGen.register()
  }
}

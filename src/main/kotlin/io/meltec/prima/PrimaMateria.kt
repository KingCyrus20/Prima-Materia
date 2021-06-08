package io.meltec.prima

import io.meltec.prima.block.PrimaBlocks
import io.meltec.prima.block.entity.PrimaBlockEntities
import io.meltec.prima.client.model.PrimaModels
import io.meltec.prima.item.PrimaItems
import io.meltec.prima.recipe.PrimaRecipes
import io.meltec.prima.worldgen.PrimaFeatures
import io.meltec.prima.worldgen.PrimaOres
import io.meltec.prima.worldgen.PrimaWorldGen
import net.fabricmc.api.ModInitializer

/** Primary entry point for the mod; mainly responsible for lots of registry additions. */
object PrimaMateria : ModInitializer {
  override fun onInitialize() {
    PrimaModels.register()
    PrimaFeatures.register()
    PrimaItems.register()
    PrimaBlocks.register()
    PrimaBlockEntities.register()
    PrimaOres.register()
    PrimaWorldGen.register()
    PrimaRecipes.register()
  }
}

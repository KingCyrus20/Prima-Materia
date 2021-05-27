package io.meltec.prima

import net.fabricmc.api.ModInitializer

object PrimaMateria : ModInitializer {
    override fun onInitialize() {
        PrimaItems.register()
        PrimaBlocks.register()
        PrimaOres.register()
    }
}

package io.meltec.mc

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object Items {
    private val bowDrill = Item(FabricItemSettings())

    fun register() {
        Registry.register(Registry.ITEM, Identifier("meltec", "bow_drill"), bowDrill)
    }
}
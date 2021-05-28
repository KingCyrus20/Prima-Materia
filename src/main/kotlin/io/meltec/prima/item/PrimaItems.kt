package io.meltec.prima.item

import io.meltec.prima.util.PrimaIdentifier
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemStack
import net.minecraft.util.registry.Registry

object PrimaItems {
    val PRIMARY_GROUP = FabricItemGroupBuilder.create(PrimaIdentifier("general"))
        .icon { ItemStack(BowDrillItem) }
        .build()

    fun register() {
        Registry.register(Registry.ITEM, PrimaIdentifier("bow_drill"), BowDrillItem)
    }
}
package io.meltec.prima

import io.meltec.prima.util.PrimaIdentifier
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry

object PrimaItems {
    val PRIMARY_GROUP = FabricItemGroupBuilder.create(PrimaIdentifier("general"))
        .icon { ItemStack(BOW_DRILL) }
        .build()

    /** Primitive fire starter; requires tinder and patience. */
    val BOW_DRILL = BowDrillItem

    fun register() {
        Registry.register(Registry.ITEM, PrimaIdentifier("bow_drill"), BOW_DRILL)
    }
}
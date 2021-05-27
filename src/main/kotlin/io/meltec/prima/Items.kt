package io.meltec.prima

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry

object Items {
    /** Primitive fire starter; requires tinder and patience. */
    val BOW_DRILL = Item(FabricItemSettings().group(ItemGroup.TOOLS).maxDamage(100).rarity(Rarity.COMMON))

    fun register() {
        Registry.register(Registry.ITEM, Identifier("meltec", "bow_drill"), BOW_DRILL)
    }
}
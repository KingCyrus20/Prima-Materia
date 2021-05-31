package io.meltec.prima.item

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterials

object GliderItem :
    ArmorItem(
        ArmorMaterials.LEATHER,
        EquipmentSlot.CHEST,
        FabricItemSettings().group(PrimaItems.EQUIPPABLE_GROUP)) {}

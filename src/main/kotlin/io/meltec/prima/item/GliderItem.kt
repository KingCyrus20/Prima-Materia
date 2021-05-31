package io.meltec.prima.item

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ElytraItem
import net.minecraft.item.ItemStack

object GliderItem:ElytraItem(FabricItemSettings().group(PrimaItems.EQUIPPABLE_GROUP).equipmentSlot { EquipmentSlot.CHEST }.maxDamage(100)) {
  override fun canRepair(stack: ItemStack, ingredient: ItemStack): Boolean {
    return false
  }
}

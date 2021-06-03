package io.meltec.prima.item

import net.minecraft.item.PickaxeItem
import net.minecraft.item.ToolMaterial

class PrimaPickaxeItem(
    material: ToolMaterial,
    attackDamage: Int,
    attackSpeed: Float,
    settings: Settings
) : PickaxeItem(material, attackDamage, attackSpeed, settings) {

}

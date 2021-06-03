package io.meltec.prima.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.item.PickaxeItem
import net.minecraft.item.ToolMaterial
import net.minecraft.text.Text
import net.minecraft.world.World

class PrimaPickaxeItem(
    material: ToolMaterial,
    attackDamage: Int,
    attackSpeed: Float,
    settings: Settings
) : PickaxeItem(material, attackDamage, attackSpeed, settings) {
  override fun appendTooltip(
      stack: ItemStack,
      world: World?,
      tooltip: MutableList<Text>,
      context: TooltipContext
  ) {
    super.appendTooltip(stack, world, tooltip, context)
    tooltip.add(Text.of("Quality: " + (stack.tag?.getInt("Quality") ?: "Quality not found")))
  }
}

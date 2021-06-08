package io.meltec.prima.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.item.PickaxeItem
import net.minecraft.text.Text
import net.minecraft.world.World

class PrimaPickaxeItem(
    private val head: PickaxeHeadItem,
    private val binding: ToolBindingItem,
    private val handle: ToolHandleItem,
    attackSpeed: Float,
    settings: Settings
) : PickaxeItem(head.toolMaterial, head.toolMaterial.attackDamage.toInt(), attackSpeed, settings) {
  private val quality: Int = head.qualityModifier + binding.qualityModifier + handle.qualityModifier

  override fun appendTooltip(
      stack: ItemStack,
      world: World?,
      tooltip: MutableList<Text>,
      context: TooltipContext
  ) {
    super.appendTooltip(stack, world, tooltip, context)
    tooltip.add(Text.of("${binding.toolMaterial.asString} Binding"))
    tooltip.add(Text.of("${handle.toolMaterial.asString} Handle"))
    tooltip.add(Text.of("Quality: $quality"))
  }

  override fun getTranslationKey() = "${head.toolMaterial.asString} Pickaxe"
  override fun getTranslationKey(stack: ItemStack?) = "${head.toolMaterial.asString} Pickaxe"
}

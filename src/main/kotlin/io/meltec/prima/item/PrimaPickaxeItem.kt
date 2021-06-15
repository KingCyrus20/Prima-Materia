package io.meltec.prima.item

import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.PickaxeItem
import net.minecraft.item.ToolMaterial
import net.minecraft.tag.BlockTags
import net.minecraft.tag.Tag
import net.minecraft.text.Text
import net.minecraft.world.World

class PrimaPickaxeItem(material: ToolMaterial?, attackDamage: Int, attackSpeed: Float,
                       settings: Settings?
) : PickaxeItem(
    material, attackDamage,
    attackSpeed, settings,
), DynamicAttributeTool {
  override fun getMiningSpeedMultiplier(
    tag: Tag<Item>,
    state: BlockState,
    stack: ItemStack,
    user: LivingEntity?
  ): Float {
    return if (BlockTags.PICKAXE_MINEABLE.contains(state.block))
      stack.tag?.getFloat("mining_speed") ?: 1.0f
    else
      1.0f
  }

  override fun getMiningLevel(
    tag: Tag<Item>,
    state: BlockState,
    stack: ItemStack,
    user: LivingEntity?
  ): Int {
    return stack.tag?.getInt("mining_level") ?: 0
  }

  override fun getDynamicModifiers(
    slot: EquipmentSlot,
    stack: ItemStack,
    user: LivingEntity?
  ): Multimap<EntityAttribute, EntityAttributeModifier> {
    if (slot == EquipmentSlot.MAINHAND){
      val builder = ImmutableMultimap.builder<EntityAttribute, EntityAttributeModifier>()
      val attackDamage = stack.tag?.getFloat("attack_damage") ?: 0f
      val attackSpeed = stack.tag?.getFloat("attack_speed") ?: -2.8f
      builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, EntityAttributeModifier(
          ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", attackDamage.toDouble(), EntityAttributeModifier.Operation.ADDITION))
      builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, EntityAttributeModifier(
          ATTACK_SPEED_MODIFIER_ID, "Tool modifier", attackSpeed.toDouble(), EntityAttributeModifier.Operation.ADDITION))
      return builder.build()
    }
    return super.getDynamicModifiers(slot, stack, user)
  }

  override fun getAttributeModifiers(slot: EquipmentSlot?): Multimap<EntityAttribute, EntityAttributeModifier> = ImmutableMultimap.of()

  override fun canRepair(stack: ItemStack, ingredient: ItemStack): Boolean {
    val headMaterial = stack.tag?.getString("head_material") ?: ""
    if (headMaterial != ""){
      return PrimaToolMaterials.valueOf(headMaterial).repairIngredient.test(ingredient)
    }
    return false
  }

  fun getQuality(stack: ItemStack):Int = stack.tag?.getInt("quality") ?: 0

  override fun appendTooltip(
    stack: ItemStack,
    world: World?,
    tooltip: MutableList<Text>,
    context: TooltipContext
  ) {
    super.appendTooltip(stack, world, tooltip, context)
    val headMaterial = stack.tag?.getString("head_material") ?: ""
    val bindingMaterial = stack.tag?.getString("binding_material") ?: ""
    val handleMaterial = stack.tag?.getString("handle_material") ?: ""
    if (headMaterial != "" && bindingMaterial != "" && handleMaterial != "") {
      tooltip.add(Text.of(PrimaToolMaterials.valueOf(headMaterial).asString + " Head"))
      tooltip.add(Text.of(PrimaToolMaterials.valueOf(bindingMaterial).asString + " Binding"))
      tooltip.add(Text.of(PrimaToolMaterials.valueOf(handleMaterial).asString + " Handle"))
    }
    tooltip.add(Text.of("Quality: " + getQuality(stack)))
  }
}

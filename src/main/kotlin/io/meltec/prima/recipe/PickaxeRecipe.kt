package io.meltec.prima.recipe

import io.meltec.prima.item.PickaxeHeadItem
import io.meltec.prima.item.PrimaItems
import io.meltec.prima.item.ToolBindingItem
import io.meltec.prima.item.ToolHandleItem
import io.meltec.prima.mixin.CraftingInventoryAccessor
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier
import net.minecraft.world.World

class PickaxeRecipe(
    val inputA: Ingredient,
    val inputB: Ingredient,
    val inputC: Ingredient,
    private val outputStack: ItemStack,
    private val id: Identifier
) : PrimaCraftingRecipe() {
  override fun matches(inv: CraftingInventoryAccessor, world: World?): Boolean {
    return inv.stacks.count { !it.isEmpty } == 3 &&
        (checkColumn(inv, 0) || checkColumn(inv, 1) || checkColumn(inv, 2))
  }

  private fun checkColumn(inv: CraftingInventoryAccessor, offset: Int): Boolean {
    return inv.stacks[offset].item is PickaxeHeadItem &&
        inv.stacks[3 + offset].item is ToolBindingItem &&
        inv.stacks[6 + offset].item is ToolHandleItem
  }

  override fun craft(inv: CraftingInventoryAccessor): ItemStack {
    val columnOffset =
        when {
            checkColumn(inv, 0) -> 0
            checkColumn(inv, 1) -> 1
            else -> 2
        }
    val head = inv.stacks[0 + columnOffset].item as PickaxeHeadItem
    val binding = inv.stacks[3 + columnOffset].item as ToolBindingItem
    val handle = inv.stacks[6 + columnOffset].item as ToolHandleItem

    val result = ItemStack(PrimaItems.PRIMA_PICKAXE)
    result.orCreateTag.putInt("durability", head.toolMaterial.durability + binding.toolMaterial.durability + handle.toolMaterial.durability)
    result.tag!!.putInt("mining_level", head.toolMaterial.miningLevel)
    result.tag!!.putFloat("mining_speed", head.toolMaterial.miningSpeed)
    result.tag!!.putFloat("attack_damage", head.toolMaterial.attackDamage)
    result.tag!!.putFloat("attack_speed", -2.8f)
    result.tag!!.putString("head_material", head.toolMaterial.name)
    result.tag!!.putString("binding_material", binding.toolMaterial.name)
    result.tag!!.putString("handle_material", handle.toolMaterial.name)
    result.tag!!.putInt("quality", head.qualityModifier + binding.qualityModifier + handle.qualityModifier)
    return result
  }

  override fun fits(width: Int, height: Int) = false
  override fun getOutput() = outputStack
  override fun getId() = id
  override fun getSerializer() = ToolRecipeSerializer
}

package io.meltec.prima.recipe

import io.meltec.prima.item.PickaxeHeadItem
import io.meltec.prima.item.PrimaPickaxeItem
import io.meltec.prima.item.ToolBindingItem
import io.meltec.prima.item.ToolHandleItem
import io.meltec.prima.mixin.CraftingInventoryAccessor
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.inventory.CraftingInventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.CraftingRecipe
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier
import net.minecraft.world.World

class PickaxeRecipe(
    val inputA: Ingredient,
    val inputB: Ingredient,
    val inputC: Ingredient,
    private val outputStack: ItemStack,
    private val id: Identifier
) : CraftingRecipe {
  override fun matches(inv: CraftingInventory, world: World?) =
      matches(inv as CraftingInventoryAccessor)

  private fun matches(inv: CraftingInventoryAccessor): Boolean {
    return inv.stacks.count { !it.isEmpty } == 3 &&
        (checkColumn(inv, 0) || checkColumn(inv, 1) || checkColumn(inv, 2))
  }

  override fun craft(inv: CraftingInventory): ItemStack = craft(inv as CraftingInventoryAccessor)

  private fun craft(inv: CraftingInventoryAccessor) = when {
    !inv.stacks[0].isEmpty -> ItemStack(createItem(inv, 0))
    !inv.stacks[1].isEmpty -> ItemStack(createItem(inv, 1))
    !inv.stacks[2].isEmpty -> ItemStack(createItem(inv, 2))
    else -> ItemStack.EMPTY
  }

  override fun fits(width: Int, height: Int) = false

  override fun getOutput() = outputStack

  override fun getId() = id

  override fun getSerializer() = ToolRecipeSerializer

  private fun checkColumn(inv: CraftingInventoryAccessor, offset: Int): Boolean {
    return inv.stacks[offset].item is PickaxeHeadItem &&
        inv.stacks[3 + offset].item is ToolBindingItem &&
        inv.stacks[6 + offset].item is ToolHandleItem
  }

  private fun createItem(inv: CraftingInventoryAccessor, offset: Int): PrimaPickaxeItem {
    return PrimaPickaxeItem(
        inv.stacks[offset].item as PickaxeHeadItem,
        inv.stacks[3 + offset].item as ToolBindingItem,
        inv.stacks[6 + offset].item as ToolHandleItem,
        attackSpeed = -2.8f,
        FabricItemSettings())
  }
}

package io.meltec.prima.recipe

import io.meltec.prima.item.PickaxeHeadItem
import io.meltec.prima.item.PrimaPickaxeItem
import io.meltec.prima.item.ToolBindingItem
import io.meltec.prima.item.ToolHandleItem
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.inventory.CraftingInventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.CraftingRecipe
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier
import net.minecraft.world.World

class PickaxeRecipe(
    val inputA: Ingredient,
    val inputB: Ingredient,
    val inputC: Ingredient,
    val outputStack: ItemStack,
    private val id: Identifier
) : CraftingRecipe {
  override fun matches(inv: CraftingInventory, world: World?): Boolean {
    return checkColumn(inv, 0) || checkColumn(inv, 1) || checkColumn(inv, 2)
  }

  override fun craft(inv: CraftingInventory): ItemStack {
    return if (!inv.getStack(0).isEmpty) ItemStack(createItem(inv, 0))
    else if (!inv.getStack(1).isEmpty) ItemStack(createItem(inv, 1))
    else if (!inv.getStack(2).isEmpty) ItemStack(createItem(inv, 2)) else ItemStack.EMPTY
  }

  override fun fits(width: Int, height: Int): Boolean {
    return false
  }

  override fun getOutput(): ItemStack {
    return outputStack
  }

  override fun getId(): Identifier {
    return id
  }

  override fun getSerializer(): RecipeSerializer<*> {
    return ToolRecipeSerializer.INSTANCE
  }

  private fun checkColumn(inv: CraftingInventory, offset: Int): Boolean {
    return inv.getStack(offset).item is PickaxeHeadItem &&
        inv.getStack(3 + offset).item is ToolBindingItem &&
        inv.getStack(6 + offset).item is ToolHandleItem
  }

  private fun createItem(inv: CraftingInventory, offset: Int): PrimaPickaxeItem {
    return PrimaPickaxeItem(
        inv.getStack(offset).item as PickaxeHeadItem,
        inv.getStack(3 + offset).item as ToolBindingItem,
        inv.getStack(6 + offset).item as ToolHandleItem,
        -2.8f,
        FabricItemSettings())
  }
}

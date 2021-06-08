package io.meltec.prima.recipe

import io.meltec.prima.mixin.CraftingInventoryAccessor
import net.minecraft.inventory.CraftingInventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.CraftingRecipe
import net.minecraft.world.World

abstract class PrimaCraftingRecipe : CraftingRecipe {
  override fun matches(inv: CraftingInventory, world: World?) =
      matches(inv as CraftingInventoryAccessor, world)

  abstract fun matches(inv: CraftingInventoryAccessor, world: World?): Boolean

  override fun craft(inv: CraftingInventory): ItemStack = craft(inv as CraftingInventoryAccessor)

  abstract fun craft(inv: CraftingInventoryAccessor): ItemStack
}

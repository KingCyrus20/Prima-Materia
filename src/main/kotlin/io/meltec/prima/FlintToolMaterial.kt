package io.meltec.prima

import net.minecraft.item.Items
import net.minecraft.item.ToolMaterial
import net.minecraft.recipe.Ingredient

class FlintToolMaterial : ToolMaterial {
  override fun getDurability(): Int {
    return 100
  }

  override fun getMiningSpeedMultiplier(): Float {
    return 3.0f
  }

  override fun getAttackDamage(): Float {
    return 0f
  }

  override fun getMiningLevel(): Int {
    return 0
  }

  override fun getEnchantability(): Int {
    return 0
  }

  override fun getRepairIngredient(): Ingredient {
    return Ingredient.ofItems(Items.FLINT)
  }
}

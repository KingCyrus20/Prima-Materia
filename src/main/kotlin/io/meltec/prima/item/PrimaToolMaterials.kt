package io.meltec.prima.item

import net.minecraft.item.Items
import net.minecraft.item.ToolMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.tag.ItemTags

enum class PrimaToolMaterials(
    private val miningLevel: Int,
    val itemDurability: Int,
    val miningSpeed: Float,
    private val attackDamage: Float,
    private val enchantability: Int,
    private val repairIngredient: Ingredient,
    val asString: String
) : ToolMaterial {
  FLINT(0, 100, 3.0f, 4f, 0, Ingredient.ofItems(Items.FLINT), "Flint"),
  LEATHER(0, 20, 0f, 0f, 0, Ingredient.ofItems(Items.LEATHER), "Leather"),
  WOOD(0, 59, 2.0f, 0.0f, 15, Ingredient.fromTag(ItemTags.PLANKS), "Wooden"),
  STRING(0, 10, 0f, 0f, 0, Ingredient.ofItems(Items.STRING), "String");

  override fun getDurability(): Int {
    return itemDurability
  }

  override fun getMiningSpeedMultiplier(): Float {
    return miningSpeed
  }

  override fun getAttackDamage(): Float {
    return attackDamage
  }

  override fun getMiningLevel(): Int {
    return miningLevel
  }

  override fun getEnchantability(): Int {
    return enchantability
  }

  override fun getRepairIngredient(): Ingredient {
    return repairIngredient
  }
}

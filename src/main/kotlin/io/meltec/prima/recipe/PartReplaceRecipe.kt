package io.meltec.prima.recipe

import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.ShapelessRecipe
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList

class PartReplaceRecipe(
    id: Identifier?,
    group: String?,
    output: ItemStack?,
    input: DefaultedList<Ingredient>?
) : ShapelessRecipe(id, group, output, input) {}

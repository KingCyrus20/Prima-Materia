package io.meltec.prima.recipe

import com.google.gson.Gson
import com.google.gson.JsonObject
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class ToolRecipeSerializer : RecipeSerializer<PickaxeRecipe> {
  override fun read(id: Identifier, json: JsonObject): PickaxeRecipe {
    val recipeJson = Gson().fromJson(json, ToolRecipeJsonFormat::class.java)
    val inputA = Ingredient.fromJson(recipeJson.inputA)
    val inputB = Ingredient.fromJson(recipeJson.inputB)
    val inputC = Ingredient.fromJson(recipeJson.inputC)
    val outputItem = Registry.ITEM.getOrEmpty(Identifier(recipeJson.outputItem)).get()
    val output = ItemStack(outputItem)
    return PickaxeRecipe(inputA, inputB, inputC, output, id)
  }

  override fun read(id: Identifier, buf: PacketByteBuf): PickaxeRecipe {
    val inputA = Ingredient.fromPacket(buf)
    val inputB = Ingredient.fromPacket(buf)
    val inputC = Ingredient.fromPacket(buf)
    val output = buf.readItemStack()
    return PickaxeRecipe(inputA, inputB, inputC, output, id)
  }

  override fun write(buf: PacketByteBuf, recipe: PickaxeRecipe) {
    recipe.inputA.write(buf)
    recipe.inputB.write(buf)
    recipe.inputC.write(buf)
    buf.writeItemStack(recipe.output)
  }

  companion object {
    val INSTANCE = ToolRecipeSerializer()
    val ID = Identifier("prima_materia:tool_recipe")
  }
}

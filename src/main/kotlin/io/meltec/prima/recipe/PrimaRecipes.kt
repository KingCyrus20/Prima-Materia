package io.meltec.prima.recipe

import net.minecraft.util.registry.Registry

object PrimaRecipes {
  fun register() {
    Registry.register(Registry.RECIPE_SERIALIZER, ToolRecipeSerializer.ID, ToolRecipeSerializer)
  }
}

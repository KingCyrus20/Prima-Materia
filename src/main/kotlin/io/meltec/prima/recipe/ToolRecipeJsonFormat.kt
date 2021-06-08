package io.meltec.prima.recipe

import com.google.gson.JsonObject

class ToolRecipeJsonFormat(
    val inputA: JsonObject,
    val inputB: JsonObject,
    val inputC: JsonObject,
    val outputItem: String
)

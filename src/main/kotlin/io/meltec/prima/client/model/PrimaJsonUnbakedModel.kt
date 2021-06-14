package io.meltec.prima.client.model

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.mojang.datafixers.util.Either
import net.fabricmc.fabric.api.renderer.v1.RendererAccess
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.ModelBakeSettings
import net.minecraft.client.render.model.ModelLoader
import net.minecraft.client.render.model.json.JsonUnbakedModel
import net.minecraft.client.render.model.json.ModelElement
import net.minecraft.client.render.model.json.ModelElementFace
import net.minecraft.client.render.model.json.ModelElementTexture
import net.minecraft.client.render.model.json.ModelOverride
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.render.model.json.Transformation
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import java.io.Reader
import java.lang.reflect.Type
import java.util.function.Function

class PrimaJsonUnbakedModel(
    parentId: Identifier?,
    elements: MutableList<ModelElement>,
    textureMap: MutableMap<String, Either<SpriteIdentifier, String>>,
    ambientOcclusion: Boolean,
    guiLight: GuiLight?,
    transformations: ModelTransformation,
    overrides: MutableList<ModelOverride>,
    val primaObjId: Identifier?,
) :
    JsonUnbakedModel(
        parentId, elements, textureMap, ambientOcclusion, guiLight, transformations, overrides) {
  var primaObj: PrimaObj? = null
  private val obj: PrimaObj?
    get() {
      val parent = parent
      return if (primaObj == null && parent is PrimaJsonUnbakedModel) {
        parent.primaObj
      } else primaObj
    }

  override fun bake(
      loader: ModelLoader,
      parent: JsonUnbakedModel,
      textureGetter: Function<SpriteIdentifier, Sprite>,
      settings: ModelBakeSettings,
      id: Identifier,
      hasDepth: Boolean
  ): BakedModel? {
    val bakedModel = super.bake(loader, parent, textureGetter, settings, id, hasDepth)
    val primaObj = obj
    return if (primaObj != null) {
      val renderer = RendererAccess.INSTANCE.renderer ?: return null
      val sprite = textureGetter.apply(resolveSprite("all"))
      val mesh = renderer.meshBuilder().createMeshFrom(primaObj, sprite, settings.rotation)
      PrimaObjBakedModel(mesh, sprite, bakedModel)
    } else bakedModel
  }

  object Deserializer : JsonUnbakedModel.Deserializer() {
    override fun deserialize(
        jsonElement: JsonElement,
        type: Type,
        jsonDeserializationContext: JsonDeserializationContext
    ): JsonUnbakedModel {
      val jsonObject = jsonElement.asJsonObject

      val modelTransformation =
          if (jsonObject.has("display")) {
            jsonDeserializationContext.deserialize(
                JsonHelper.getObject(jsonObject, "display"), ModelTransformation::class.java)
          } else ModelTransformation.NONE

      val guiLight =
          if (jsonObject.has("gui_light")) {
            GuiLight.deserialize(JsonHelper.getString(jsonObject, "gui_light"))
          } else null

      fun String.createId(): Identifier? = takeUnless { it.isEmpty() }?.let { Identifier(it) }

      return PrimaJsonUnbakedModel(
          deserializeParent(jsonObject).createId(),
          deserializeElements(jsonDeserializationContext, jsonObject),
          deserializeTextures(jsonObject),
          deserializeAmbientOcclusion(jsonObject),
          guiLight,
          modelTransformation,
          deserializeOverrides(jsonDeserializationContext, jsonObject),
          JsonHelper.getString(jsonObject, "object", "").createId())
    }
  }

  companion object {
    fun deserialize(reader: Reader): PrimaJsonUnbakedModel? =
        JsonHelper.deserialize(GSON, reader, PrimaJsonUnbakedModel::class.java)

    private val GSON: Gson =
        GsonBuilder()
            .registerTypeAdapter(PrimaJsonUnbakedModel::class.java, Deserializer)
            .registerTypeAdapter(ModelElement::class.java, ModelElement.Deserializer())
            .registerTypeAdapter(ModelElementFace::class.java, ModelElementFace.Deserializer())
            .registerTypeAdapter(
                ModelElementTexture::class.java, ModelElementTexture.Deserializer())
            .registerTypeAdapter(Transformation::class.java, Transformation.Deserializer())
            .registerTypeAdapter(
                ModelTransformation::class.java, ModelTransformation.Deserializer())
            .registerTypeAdapter(ModelOverride::class.java, ModelOverride.Deserializer())
            .create()
  }
}

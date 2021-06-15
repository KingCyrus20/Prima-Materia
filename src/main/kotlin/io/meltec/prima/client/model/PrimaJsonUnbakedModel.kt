package io.meltec.prima.client.model

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import java.io.Reader
import java.lang.reflect.Type
import java.util.function.Function
import net.fabricmc.fabric.api.renderer.v1.RendererAccess
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.ModelBakeSettings
import net.minecraft.client.render.model.ModelLoader
import net.minecraft.client.render.model.UnbakedModel
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

class PrimaJsonUnbakedModel(
    parentId: Identifier?,
    elements: MutableList<ModelElement>,
    textureMap: MutableMap<String, Either<SpriteIdentifier, String>>,
    ambientOcclusion: Boolean,
    guiLight: GuiLight?,
    transformations: ModelTransformation,
    overrides: MutableList<ModelOverride>,
    val primaObjId: Identifier?,
    private val _layerCount: Int?
) :
    JsonUnbakedModel(
        parentId, elements, textureMap, ambientOcclusion, guiLight, transformations, overrides) {

  internal var _primaObj: PrimaObj? = null
  private val primaObj: PrimaObj?
    get() {
      val parent = parent
      return if (_primaObj == null && parent is PrimaJsonUnbakedModel) {
        parent.primaObj
      } else _primaObj
    }

  private val layerCount: Int
    get() {
      val parent = parent
      val layerCount =
          if (_layerCount == null && parent is PrimaJsonUnbakedModel) {
            parent.layerCount
          } else _layerCount
      return layerCount ?: 1
    }

  override fun getTextureDependencies(
      unbakedModelGetter: Function<Identifier, UnbakedModel>?,
      unresolvedTextureReferences: MutableSet<Pair<String, String>>?
  ): MutableCollection<SpriteIdentifier> {
    val set =
        super.getTextureDependencies(unbakedModelGetter, unresolvedTextureReferences).toMutableSet()
    for ((_, value) in textureMap) {
      value.left().takeIf { it.isPresent }?.let { set.add(it.get()) }
      value.right().takeIf { it.isPresent }?.let { set.add(resolveSprite(it.get())) }
    }
    return set
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
    val primaObj = primaObj
    return if (primaObj != null) {
      val renderer = RendererAccess.INSTANCE.renderer ?: return null
      val layers = Array(layerCount) { textureGetter.apply(resolveSprite("layer$it")) }
      val mesh =
          renderer
              .meshBuilder()
              .createMeshFrom(primaObj, layers, settings.rotation, renderer.materialFinder())
      PrimaObjBakedModel(mesh, layers, bakedModel)
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
            GuiLight.byName(JsonHelper.getString(jsonObject, "gui_light"))
          } else null

      fun String.createId(): Identifier? = takeUnless { it.isEmpty() }?.let { Identifier(it) }

      return PrimaJsonUnbakedModel(
          parentFromJson(jsonObject).createId(),
          elementsFromJson(jsonDeserializationContext, jsonObject),
          texturesFromJson(jsonObject),
          ambientOcclusionFromJson(jsonObject),
          guiLight,
          modelTransformation,
          overridesFromJson(jsonDeserializationContext, jsonObject),
          JsonHelper.getString(jsonObject, "object", "").createId(),
          JsonHelper.getInt(jsonObject, "layers", 0).takeIf { it > 0 })
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

package io.meltec.prima.client.model

import io.meltec.prima.util.PRIMA_NAMESPACE
import net.fabricmc.fabric.api.client.model.ModelProviderContext
import net.fabricmc.fabric.api.client.model.ModelResourceProvider
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier

class PrimaObjModelResourceProvider(private val resourceManager: ResourceManager) :
    ModelResourceProvider {
  override fun loadModelResource(
      resourceId: Identifier,
      context: ModelProviderContext
  ): UnbakedModel? {
    if (resourceId.namespace != PRIMA_NAMESPACE) return null

    val qualifiedJsonId = with(resourceId) { Identifier(namespace, "models/$path.json") }
    if (!resourceManager.containsResource(qualifiedJsonId)) return null
    val jsonUnbakedModel =
        PrimaJsonUnbakedModel.deserialize(
            resourceManager.getResource(qualifiedJsonId).inputStream.reader())

    val qualifiedObjId = with(resourceId) { Identifier(namespace, "models/$path.obj") }
    if (resourceManager.containsResource(qualifiedObjId)) {
      jsonUnbakedModel?.primaObj =
          PrimaObj.read(resourceManager.getResource(qualifiedObjId).inputStream.reader())
    }

    return jsonUnbakedModel
  }
}

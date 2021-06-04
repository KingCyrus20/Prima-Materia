package io.meltec.prima.client.model

import io.meltec.prima.util.PRIMA_NAMESPACE
import net.fabricmc.fabric.api.client.model.ModelProviderContext
import net.fabricmc.fabric.api.client.model.ModelResourceProvider
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

class PrimaObjModelResourceProvider(private val resourceManager: ResourceManager) :
    ModelResourceProvider {
  override fun loadModelResource(
      resourceId: Identifier,
      context: ModelProviderContext
  ): UnbakedModel? {
    // Process only our resources
    if (resourceId.namespace != PRIMA_NAMESPACE) return null

    // Process only obj resources
    val qualifiedId = with(resourceId) { Identifier(namespace, "models/$path.obj") }
    if (!resourceManager.containsResource(qualifiedId)) return null

    // TODO: Implement obj loading
    logger.info("Processed $qualifiedId in unimplemented obj loader")

    val primaObj = PrimaObj.read(resourceManager.getResource(qualifiedId).inputStream.reader())
    // TODO: Figure out what the atlas is actually supposed to be
    val atlasIdentifier = SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE
    val spriteIdentifier = SpriteIdentifier(atlasIdentifier, resourceId)

    return PrimaObjUnbakedModel(spriteIdentifier, primaObj)
  }

  companion object {
    private val logger = LogManager.getLogger()
  }
}

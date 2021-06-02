package io.meltec.prima.client.model

import com.mojang.datafixers.util.Pair
import de.javagl.obj.ReadableObj
import java.util.function.Function
import net.fabricmc.fabric.api.renderer.v1.RendererAccess
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.ModelBakeSettings
import net.minecraft.client.render.model.ModelLoader
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.texture.Sprite
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.util.Identifier

class PrimaObjUnbakedModel(private val objFile: ReadableObj) : UnbakedModel {
  override fun getModelDependencies() = emptyList<Identifier>()

  override fun getTextureDependencies(
      unbakedModelGetter: Function<Identifier, UnbakedModel>?,
      unresolvedTextureReferences: MutableSet<Pair<String, String>>?
  ): MutableCollection<SpriteIdentifier> {
    return mutableSetOf()
  }

  override fun bake(
      loader: ModelLoader,
      textureGetter: Function<SpriteIdentifier, Sprite>,
      rotationContainer: ModelBakeSettings,
      modelId: Identifier
  ): BakedModel? {
    val renderer =
        with(RendererAccess.INSTANCE) {
          if (!hasRenderer()) return null
          renderer!!
        }
    val meshBuilder = renderer.meshBuilder()

    with(meshBuilder.emitter) {
      for (face in objFile.faceIterator) {
        for ((index, vertexInfo) in face.getIndexedVertexIterator(objFile)) {
          pos(index, vertexInfo.vertex)
        }
        emit()
      }
    }

    val mesh = meshBuilder.build()

    return PrimaObjBakedModel(
        mesh,
        textureGetter.apply(
            SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(""))))
  }
}

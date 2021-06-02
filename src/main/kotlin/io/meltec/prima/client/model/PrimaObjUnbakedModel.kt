package io.meltec.prima.client.model

import com.mojang.datafixers.util.Pair
import de.javagl.obj.ObjUtils
import de.javagl.obj.ReadableObj
import java.util.function.Function
import net.fabricmc.fabric.api.renderer.v1.RendererAccess
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.ModelBakeSettings
import net.minecraft.client.render.model.ModelLoader
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec2f.ZERO
import org.apache.logging.log4j.LogManager

class PrimaObjUnbakedModel(
    private val spriteIdentifier: SpriteIdentifier,
    private val objFile: ReadableObj
) : UnbakedModel {
  override fun getModelDependencies() = emptyList<Identifier>()

  override fun getTextureDependencies(
      unbakedModelGetter: Function<Identifier, UnbakedModel>?,
      unresolvedTextureReferences: MutableSet<Pair<String, String>>?
  ): Collection<SpriteIdentifier> = setOf(spriteIdentifier)

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
    // Make everything triangles
    val objFile = ObjUtils.triangulate(objFile)
    val sprite = textureGetter.apply(spriteIdentifier)

    val meshBuilder = renderer.meshBuilder()
    val emitter = meshBuilder.emitter

    for (face in objFile.faceIterator) {
      for ((index, vertex) in face.getIndexedVertexIterator(objFile)) {
        emitter.vertex(index, vertex)
      }
      // Since we're using triangles in a quad view, add another vertex
      emitter.vertex(face.numVertices, face.getVertexInfo(face.numVertices - 1, objFile))

      val color = 0xFFFFFF
      emitter.spriteColor(0, color, color, color, color)
      emitter.spriteBake(0, sprite, MutableQuadView.BAKE_NORMALIZED)
      emitter.emit()
    }

    return PrimaObjBakedModel(meshBuilder.build(), sprite)
  }

  private fun QuadEmitter.vertex(index: Int, vertex: VertexInfo) {
    pos(index, vertex.coords)
    vertex.normal?.let { normal(index, it) }
    sprite(index, 0, vertex.texCoords ?: ZERO)
  }

  companion object {
    private val logger = LogManager.getLogger()
  }
}

package io.meltec.prima.client.model

import com.mojang.datafixers.util.Pair
import de.javagl.obj.ObjFace
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
    val renderer = RendererAccess.INSTANCE.renderer ?: return null
    val hasNonQuad = objFile.faceIterator.asSequence().any { it.numVertices > 4 }
    val objFile =
        if (hasNonQuad) {
          logger.warn("$modelId had a non-quad face triangulating")
          // TODO: do this while processing faces instead of before
          ObjUtils.triangulate(objFile)
        } else objFile
    val sprite = textureGetter.apply(spriteIdentifier)
    val mesh =
        with(renderer.meshBuilder()) {
          objFile.faceIterator.forEach { emitter.emitFace(it, objFile, sprite) }
          build()
        }

    return PrimaObjBakedModel(mesh, sprite)
  }

  // TODO: VertexInfo list is probably a ton of object allocation we can avoid
  private fun QuadEmitter.emitFace(face: ObjFace, parent: ReadableObj, sprite: Sprite) {
    val vertexList = face.getVertexList(parent)
    vertexList.withIndex().forEach { (i, vertex) -> addVertex(i, vertex) }

    // If we have a triangle, add another vertex to statisfy quad view
    if (face.numVertices == 3) addVertex(3, vertexList[2])

    spriteBake(0, sprite, MutableQuadView.BAKE_NORMALIZED)
    emit()
  }

  private fun QuadEmitter.addVertex(index: Int, vertex: VertexInfo) {
    pos(index, vertex.coords)
    vertex.normal?.let { normal(index, it) }
    sprite(index, 0, vertex.texCoords ?: ZERO)
    spriteColor(index, 0, 0xFFFFFF)
  }

  companion object {
    private val logger = LogManager.getLogger()
  }
}

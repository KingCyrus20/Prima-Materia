package io.meltec.prima.client.model

import com.mojang.datafixers.util.Pair
import java.util.function.Function
import net.fabricmc.fabric.api.renderer.v1.RendererAccess
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.ModelBakeSettings
import net.minecraft.client.render.model.ModelLoader
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

class PrimaObjUnbakedModel(
    private val spriteIdentifier: SpriteIdentifier,
    private val primaObj: PrimaObj
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
    val sprite = textureGetter.apply(spriteIdentifier)

    val mesh = renderer.meshBuilder().createMeshFrom(primaObj, sprite)

    return PrimaObjBakedModel(mesh, sprite)
  }

  private fun MeshBuilder.createMeshFrom(model: PrimaObj, sprite: Sprite): Mesh {
    emitter.createMeshFrom(model, sprite)
    return build()
  }

  private fun QuadEmitter.createMeshFrom(model: PrimaObj, sprite: Sprite) {
    for (face in model.faces) {
      for ((index, vertex) in face.withIndex()) createVertexFrom(primaObj, index, vertex)

      // If we have a triangle, add another vertex to statisfy quad view
      if (face.size == 3) createVertexFrom(primaObj, 3, face[2])

      spriteBake(0, sprite, MutableQuadView.BAKE_NORMALIZED or MutableQuadView.BAKE_FLIP_V)
      emit()
    }
  }

  private fun QuadEmitter.createVertexFrom(model: PrimaObj, index: Int, vertex: IntArray) {
    val (position, texCoord, normal) = vertex
    val (x, y, z) = model.positions[position]
    val (u, v) = model.texCoords[texCoord]
    val (nx, ny, nz) = model.normals[normal]
    pos(index, x, y, z)
    sprite(index, 0, u, v)
    normal(index, nx, ny, nz)
    spriteColor(index, 0, 0xFFFFFF)
  }

  companion object {
    private val logger = LogManager.getLogger()
  }
}

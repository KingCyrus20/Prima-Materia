package io.meltec.prima.client.model

import com.mojang.datafixers.util.Pair
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
import net.minecraft.client.util.math.AffineTransformation
import net.minecraft.util.Identifier
import net.minecraft.util.math.Quaternion
import org.apache.logging.log4j.LogManager
import java.util.function.Function

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

    val mesh = renderer.meshBuilder().createMeshFrom(primaObj, sprite, rotationContainer.rotation)

    return PrimaObjBakedModel(mesh, sprite)
  }

  private fun MeshBuilder.createMeshFrom(
      model: PrimaObj,
      sprite: Sprite,
      transformation: AffineTransformation,
  ): Mesh {
    emitter.createMeshFrom(model, sprite, transformation)
    return build()
  }

  private fun QuadEmitter.createMeshFrom(
      model: PrimaObj,
      sprite: Sprite,
      transformation: AffineTransformation,
  ) {
    for (face in model.faces) {
      for ((index, vertex) in face.withIndex()) createVertexFrom(
          primaObj, index, vertex, transformation)

      // If we have a triangle, add another vertex to statisfy quad view
      if (face.size == 3) createVertexFrom(primaObj, 3, face[2], transformation)

      spriteBake(0, sprite, MutableQuadView.BAKE_NORMALIZED or MutableQuadView.BAKE_FLIP_V)
      emit()
    }
  }

  private fun QuadEmitter.createVertexFrom(
      model: PrimaObj,
      index: Int,
      vertex: IntArray,
      transformation: AffineTransformation,
  ) {
    val (position, texCoord, normal) = vertex
    var (x, y, z) = model.positions[position]
    val (u, v) = model.texCoords[texCoord]
    var (nx, ny, nz) = model.normals[normal]
    if (transformation != AffineTransformation.identity()) {
      val (rx, ry, rz, _) =
          rotate(x + BLOCK_CENTER, y + BLOCK_CENTER, z + BLOCK_CENTER, 0f, transformation.rotation2)
      val (rnx, rny, rnz, _) = rotate(nx, ny, nz, 0f, transformation.rotation2)
      x = rx - BLOCK_CENTER
      y = ry - BLOCK_CENTER
      z = rz - BLOCK_CENTER
      nx = rnx
      ny = rny
      nz = rnz
    }
    pos(index, x, y, z)
    sprite(index, 0, u, v)
    normal(index, nx, ny, nz)
    spriteColor(index, 0, 0xFFFFFF)
  }

  private fun rotate(x: Float, y: Float, z: Float, w: Float, r: Quaternion): FloatArray {
    return floatArrayOf(r.x, r.y, r.z, r.w).apply {
      hamiltonProduct(x, y, z, w)
      hamiltonProduct(-r.x, -r.y, -r.z, r.w)
    }
  }

  private fun FloatArray.hamiltonProduct(x2: Float, y2: Float, z2: Float, w2: Float) {
    val x1 = this[0]
    val y1 = this[1]
    val z1 = this[2]
    val w1 = this[3]
    this[0] = w1 * x2 + x1 * w2 + y1 * z2 - z1 * y2
    this[1] = w1 * y2 - x1 * z2 + y1 * w2 + z1 * x2
    this[2] = w1 * z2 + x1 * y2 - y1 * x2 + z1 * w2
    this[3] = w1 * w2 - x1 * x2 - y1 * y2 - z1 * z2
  }

  companion object {
    private const val BLOCK_CENTER = 0.5f
    private val logger = LogManager.getLogger()
  }
}

package io.meltec.prima.client.model

import io.meltec.prima.util.rotate
import io.meltec.prima.util.u
import io.meltec.prima.util.v
import io.meltec.prima.util.x
import io.meltec.prima.util.y
import io.meltec.prima.util.z
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.math.AffineTransformation

private const val BLOCK_CENTER = 0.5f

fun MeshBuilder.createMeshFrom(
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
    for ((index, vertex) in face.withIndex()) {
      createVertexFrom(model, index, vertex, transformation)
    }

    // If we have a triangle, add another vertex to statisfy quad view
    if (face.size == 3) copyVertex(from = 2, to = 3)

    // Attach the texture to the current quad
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
  val (posIndex, texIndex, normIndex) = vertex

  // Set up vertex position
  model.positions[posIndex]
      .mapIf(transformation.isNotIdentity) { rotate(it, transformation.rotation2, BLOCK_CENTER) }
      .also { pos(index, it.x, it.y, it.z) }

  // Set up vertex normal
  model.normals[normIndex]
      .mapIf(transformation.isNotIdentity) { rotate(it, transformation.rotation2) }
      .also { normal(index, it.x, it.y, it.z) }

  // Set up the texture coordinates and material color
  with(model.texCoords[texIndex]) { sprite(index, 0, u, v) }
  spriteColor(index, 0, /* color= */ 0xFFFFFF)
}

private fun QuadEmitter.copyVertex(from: Int, to: Int) {
  pos(to, x(from), y(from), z(from))
  normal(to, normalX(from), normalY(from), normalZ(from))
  sprite(to, 0, spriteU(from, 0), spriteV(from, 0))
  spriteColor(to, 0, spriteColor(from, 0))
}

/**
 * Returns the result of applying [transform] if [condition] is statisfied or [this], if it doesn't.
 */
private inline fun <T> T.mapIf(condition: Boolean, transform: (T) -> T): T {
  return if (condition) transform(this) else this
}

private inline val AffineTransformation.isNotIdentity
  get() = this != AffineTransformation.identity()
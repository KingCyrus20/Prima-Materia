package io.meltec.prima.util

import net.minecraft.util.math.Quaternion

typealias PrimaVector = FloatArray

/** Rotate a [point] using [rotation] around [center]. */
fun rotate(point: PrimaVector, rotation: Quaternion, center: Float): PrimaVector {
  return floatArrayOf(rotation.x, rotation.y, rotation.z, rotation.w).apply {
    hamiltonProduct(point.x - center, point.y - center, point.z - center, 0f)
    hamiltonProduct(-rotation.x, -rotation.y, -rotation.z, rotation.w)
    this += center
  }
}

/** Rotate a [point] using [rotation]. */
fun rotate(point: PrimaVector, rotation: Quaternion): PrimaVector {
  return floatArrayOf(rotation.x, rotation.y, rotation.z, rotation.w).apply {
    hamiltonProduct(point.x, point.y, point.z, 0f)
    hamiltonProduct(-rotation.x, -rotation.y, -rotation.z, rotation.w)
  }
}

/** Takes the hamilton product of [this] and ([x2], [y2], [z2], [w2]). */
fun PrimaVector.hamiltonProduct(x2: Float, y2: Float, z2: Float, w2: Float) {
  val x1 = this[0]
  val y1 = this[1]
  val z1 = this[2]
  val w1 = this[3]
  this[0] = w1 * x2 + x1 * w2 + y1 * z2 - z1 * y2
  this[1] = w1 * y2 - x1 * z2 + y1 * w2 + z1 * x2
  this[2] = w1 * z2 + x1 * y2 - y1 * x2 + z1 * w2
  this[3] = w1 * w2 - x1 * x2 - y1 * y2 - z1 * z2
}

private operator fun FloatArray.plusAssign(f: Float) {
  mapInPlace { it + f }
}

private inline fun FloatArray.mapInPlace(transform: (Float) -> Float) {
  for (index in indices) {
    this[index] = transform(this[index])
  }
}

/** u tex coordinate for [PrimaVector]. */
inline val PrimaVector.u
  get() = this[0]
/** v tex coordinate for [PrimaVector]. */
inline val PrimaVector.v
  get() = this[1]
/** x-axis coordinate for [PrimaVector]. */
inline val PrimaVector.x
  get() = this[0]
/** y-axis coordinate for [PrimaVector]. */
inline val PrimaVector.y
  get() = this[1]
/** z-axis coordinate for [PrimaVector]. */
inline val PrimaVector.z
  get() = this[2]

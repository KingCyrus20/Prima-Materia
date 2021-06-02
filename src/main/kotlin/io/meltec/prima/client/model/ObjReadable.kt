package io.meltec.prima.client.model

import de.javagl.obj.FloatTuple
import de.javagl.obj.ObjFace
import de.javagl.obj.ReadableObj
import net.minecraft.client.util.math.Vector3f
import net.minecraft.util.math.Vec2f

val ReadableObj.faceIterator
  get() =
      object : Iterator<ObjFace> {
        private var i = -1
        override fun hasNext() = (i + 1) < numFaces
        override fun next() = getFace(++i)
      }

fun ObjFace.getIndexedVertexIterator(obj: ReadableObj) =
    object : Iterator<IndexedValue<VertexInfo>> {
      private var i = -1
      override fun hasNext() = (i + 1) < numVertices
      override fun next() = IndexedValue(++i, getVertexInfo(i, obj))
    }

fun ObjFace.getVertexInfo(index: Int, obj: ReadableObj): VertexInfo {
  val vertex = obj.getVertex(getVertexIndex(index)).asVector3f()
  val normal =
      if (containsNormalIndices()) {
        obj.getNormal(getNormalIndex(index)).asVector3f()
      } else null
  val texCoord =
      if (containsTexCoordIndices()) {
        obj.getTexCoord(getTexCoordIndex(index)).asVec2f()
      } else null
  return VertexInfo(vertex, normal, texCoord)
}

data class VertexInfo(val coords: Vector3f, val normal: Vector3f?, val texCoords: Vec2f?)

fun FloatTuple.asVector3f() = Vector3f(x, y, z)

fun FloatTuple.asVec2f() = Vec2f(x, y)

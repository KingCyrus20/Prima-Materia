package io.meltec.prima.client.model

import de.javagl.obj.FloatTuple
import de.javagl.obj.ObjFace
import de.javagl.obj.ReadableObj
import net.minecraft.client.util.math.Vector3f

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
      override fun next(): IndexedValue<VertexInfo> {
        i++
        val vertex = obj.getVertex(getVertexIndex(i)).asVector3f()
        val normal =
            if (containsNormalIndices()) {
              obj.getNormal(getNormalIndex(i)).asVector3f()
            } else null
        val texCoord =
            if (containsTexCoordIndices()) {
              obj.getTexCoord(getTexCoordIndex(i))
            } else null
        return IndexedValue(i, VertexInfo(vertex, normal, texCoord))
      }
    }

data class VertexInfo(val vertex: Vector3f, val normal: Vector3f?, val texCoord: FloatTuple?)

fun FloatTuple.asVector3f() = Vector3f(x, y, z)

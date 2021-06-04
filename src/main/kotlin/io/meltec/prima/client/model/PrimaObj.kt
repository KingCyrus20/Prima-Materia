package io.meltec.prima.client.model

import java.io.Reader

data class PrimaObj(
    val positions: List<FloatArray>,
    val normals: List<FloatArray>,
    val texCoords: List<FloatArray>,
    val faces: List<List<IntArray>>
) {
  companion object {
    fun read(reader: Reader): PrimaObj {
      val vertices = mutableListOf<FloatArray>()
      val normals = mutableListOf<FloatArray>()
      val texCoords = mutableListOf<FloatArray>()
      val faces = mutableListOf<List<IntArray>>()

      reader.buffered().forEachLine {
        when {
          it.startsWith('v') -> {
            val type =
                when (it.getOrNull(1)) {
                  ' ' -> vertices
                  'n' -> normals
                  't' -> texCoords
                  else -> error("unsupported directive in obj file")
                }
            type.add(parseFloatArray(it))
          }
          it.startsWith('f') -> faces.add(parseIntArray(it))
        }
      }

      return PrimaObj(vertices, normals, texCoords, faces)
    }

    private fun parseFloatArray(s: String): FloatArray {
      return s.splitToSequence(" ").mapNotNull { it.toFloatOrNull() }.toList().toFloatArray()
    }

    private fun parseIntArray(s: String): List<IntArray> {
      return s.splitToSequence(" ")
          .drop(1)
          .mapNotNull {
            val ints = it.splitToSequence("/").mapNotNull { it.toIntOrNull() }.iterator()
            // Wavefront OBJ can omit texCoords/normals so hard code 3 is not ideal
            IntArray(3) { ints.next() - 1 }
          }
          .toList()
    }
  }
}

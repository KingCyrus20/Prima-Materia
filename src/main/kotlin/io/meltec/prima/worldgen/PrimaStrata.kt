package io.meltec.prima.worldgen

import io.meltec.prima.util.PrimaIdentifier
import java.util.*
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.block.BlockState

/** Obtain strata for each layer. */
object PrimaStrata {
  val STRATA1_TAG = TagRegistry.block(PrimaIdentifier("strata/1"))
  val STRATA2_TAG = TagRegistry.block(PrimaIdentifier("strata/2"))
  val STRATA3_TAG = TagRegistry.block(PrimaIdentifier("strata/3"))
  val STRATA4_TAG = TagRegistry.block(PrimaIdentifier("strata/4"))
  val SEDIMENTARY_TAG = TagRegistry.block(PrimaIdentifier("strata/sedimentary"))
  val METAMORPHIC_TAG = TagRegistry.block(PrimaIdentifier("strata/metamorphic"))
  val IGNEOUS_TAG = TagRegistry.block(PrimaIdentifier("strata/igneous"))

  /** Sample a strata block from the given layer (0 - 3). */
  fun sampleLayer(layer: Int, rng: Random): BlockState =
      when (layer) {
        1 -> STRATA2_TAG.getRandom(rng).defaultState
        2 -> STRATA3_TAG.getRandom(rng).defaultState
        3 -> STRATA4_TAG.getRandom(rng).defaultState
        else -> STRATA1_TAG.getRandom(rng).defaultState
      }
}

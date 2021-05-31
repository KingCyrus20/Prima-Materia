package io.meltec.prima.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3i

class PrimaIdentifier(path: String) : Identifier("prima_materia", path)

@ExperimentalContracts
fun HitResult.isBlockHitResult(): Boolean {
  contract { returns(true) implies (this@isBlockHitResult is BlockHitResult) }
  return type == HitResult.Type.BLOCK
}

fun Vec3i.choose(axis: Direction.Axis): Int {
  return axis.choose(x, y, z)
}

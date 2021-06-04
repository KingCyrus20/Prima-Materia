package io.meltec.prima.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult

const val PRIMA_NAMESPACE = "prima_materia"

class PrimaIdentifier(path: String) : Identifier(PRIMA_NAMESPACE, path)

@ExperimentalContracts
fun HitResult.isBlockHitResult(): Boolean {
  contract { returns(true) implies (this@isBlockHitResult is BlockHitResult) }
  return type == HitResult.Type.BLOCK
}

package io.meltec.prima.client.model

import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.block.BlockState
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.render.model.json.ModelOverrideList
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockRenderView
import java.util.*
import java.util.function.Supplier

class PrimaObjBakedModel(private val mesh: Mesh, private val sprite: Sprite) :
    BakedModel, FabricBakedModel {
  override fun isVanillaAdapter() = false

  override fun emitBlockQuads(
      blockView: BlockRenderView,
      state: BlockState,
      pos: BlockPos,
      randomSupplier: Supplier<Random>,
      context: RenderContext
  ) {
    context.meshConsumer().accept(mesh)
  }

  override fun emitItemQuads(
      stack: ItemStack?,
      randomSupplier: Supplier<Random>?,
      context: RenderContext?
  ) {}

  /**
   * The [BakedQuad]s representing this [BakedModel].
   *
   * Used for multipart support
   */
  private val vanillaQuads by lazy {
    mutableListOf<BakedQuad>().apply {
      mesh.forEach { add(it.toBakedQuad(0, sprite, /* isItem= */ false)) }
    }
  }

  override fun getQuads(state: BlockState?, face: Direction?, random: Random?) = vanillaQuads

  override fun useAmbientOcclusion() = true

  override fun hasDepth() = false

  override fun isSideLit() = false

  override fun isBuiltin() = false

  override fun getSprite() = sprite

  override fun getTransformation() = ModelTransformation.NONE!!

  override fun getOverrides() = ModelOverrideList.EMPTY!!
}

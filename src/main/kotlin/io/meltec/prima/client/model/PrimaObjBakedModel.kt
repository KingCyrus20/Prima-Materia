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

class PrimaObjBakedModel(
    private val mesh: Mesh,
    private val sprite: Sprite,
    private val delegate: BakedModel? = null,
) : BakedModel, FabricBakedModel {
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
      stack: ItemStack,
      randomSupplier: Supplier<Random>,
      context: RenderContext
  ) {
    // TODO: Figure out how to use fabric renderer
    context.fallbackConsumer().accept(this)
  }

  /**
   * The [BakedQuad]s representing this [BakedModel].
   *
   * Used for multipart, gui support
   */
  private val vanillaQuads by lazy {
    mutableListOf<BakedQuad>().apply {
      mesh.forEach { add(it.toBakedQuad(0, sprite, /* isItem= */ false)) }
    }
  }

  override fun getQuads(state: BlockState?, face: Direction?, random: Random?) = vanillaQuads

  override fun useAmbientOcclusion() = delegate?.useAmbientOcclusion() ?: true

  override fun hasDepth() = delegate?.hasDepth() ?: false

  override fun isSideLit() = delegate?.isSideLit ?: false

  override fun isBuiltin() = false

  override fun getSprite() = delegate?.sprite ?: sprite

  override fun getTransformation() = delegate?.transformation ?: ModelTransformation.NONE!!

  override fun getOverrides() = delegate?.overrides ?: ModelOverrideList.EMPTY!!
}

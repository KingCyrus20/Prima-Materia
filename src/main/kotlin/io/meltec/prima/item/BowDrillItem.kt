package io.meltec.prima.item

import io.meltec.prima.util.isBlockHitResult
import kotlin.contracts.ExperimentalContracts
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Blocks
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.ToolItem
import net.minecraft.item.ToolMaterials
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

/** Starts fires using tinder and patience. */
object BowDrillItem :
    ToolItem(ToolMaterials.WOOD, FabricItemSettings().maxDamage(10).group(PrimaItems.TOOL_GROUP)) {
  override fun getMaxUseTime(stack: ItemStack) = 60

  override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
    val fireLocation = user.getBlockAboveCursor() ?: return stack

    if (fireLocation.isInFrontOf(user) && world.canSetBlock(fireLocation)) {
      world.setBlockState(fireLocation, Blocks.FIRE.defaultState)
    }

    stack.damage(1, user) { e -> e.sendToolBreakStatus(e.activeHand) }
    return stack
  }

  override fun usageTick(
      world: World,
      user: LivingEntity,
      stack: ItemStack,
      remainingUseTicks: Int
  ) {
    if (user.getBlockAboveCursor()?.isInFrontOf(user) != true) {
      user.clearActiveItem()
    }
  }

  @OptIn(ExperimentalContracts::class)
  private fun LivingEntity.getBlockAboveCursor(): BlockPos? {
    return with(raycast(3.0, 0.0f, true)) { if (isBlockHitResult()) blockPos.up() else null }
  }

  override fun useOnBlock(context: ItemUsageContext): ActionResult {
    context.run {
      val player = player ?: return ActionResult.PASS
      if (side == Direction.UP && blockPos.up().isInFrontOf(player)) {
        player.setCurrentHand(hand)
        return ActionResult.CONSUME
      }
      return ActionResult.PASS
    }
  }

  /** Checks if the position is directly in front of the [player] */
  private fun BlockPos.isInFrontOf(player: LivingEntity): Boolean {
    // TODO: Optimize?
    val posBottom = with(player) { blockPos.offset(horizontalFacing) }
    return equals(posBottom) || equals(posBottom.up())
  }

  @Environment(EnvType.CLIENT)
  override fun appendTooltip(
      stack: ItemStack,
      world: World?,
      tooltip: MutableList<Text>,
      context: TooltipContext
  ) {
    super.appendTooltip(stack, world, tooltip, context)
    tooltip.add(TranslatableText("item.prima_materia.bow_drill.tooltip"))
  }
}

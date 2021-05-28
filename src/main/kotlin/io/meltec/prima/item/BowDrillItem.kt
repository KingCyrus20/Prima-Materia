package io.meltec.prima.item

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.FireBlock
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.ToolItem
import net.minecraft.item.ToolMaterials
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.UseAction
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Direction
import net.minecraft.world.World

/** Starts fires using tinder and patience. */
object BowDrillItem : ToolItem(ToolMaterials.WOOD, FabricItemSettings().maxDamage(10).group(PrimaItems.PRIMARY_GROUP)) {
    override fun getMaxUseTime(stack: ItemStack?): Int = 60
    override fun getUseAction(stack: ItemStack?): UseAction = UseAction.BOW

    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        val hit = user.raycast(6.0, 0.0f, true)
        if (hit.type == HitResult.Type.MISS || hit.type == HitResult.Type.ENTITY) return stack

        val blockHit = hit as BlockHitResult
        val fireLocation = blockHit.blockPos.offset(blockHit.side)
        if (world.canSetBlock(fireLocation)) world.setBlockState(fireLocation, placedFireBlockState(hit.side))

        stack.damage(1, user) { e -> e.sendToolBreakStatus(e.activeHand) }
        return stack
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        context.player?.setCurrentHand(context.hand)
        return ActionResult.SUCCESS
    }

    /** Determine which direction to place fire based on where the player clicked. */
    private fun placedFireBlockState(placedBlockSide: Direction): BlockState = when (placedBlockSide) {
        Direction.DOWN -> Blocks.FIRE.defaultState.with(FireBlock.UP, true)
        Direction.EAST -> Blocks.FIRE.defaultState.with(FireBlock.WEST, true)
        Direction.WEST -> Blocks.FIRE.defaultState.with(FireBlock.EAST, true)
        Direction.NORTH -> Blocks.FIRE.defaultState.with(FireBlock.SOUTH, true)
        Direction.SOUTH -> Blocks.FIRE.defaultState.with(FireBlock.NORTH, true)
        else -> Blocks.FIRE.defaultState
    }

    @Environment(EnvType.CLIENT)
    override fun appendTooltip(stack: ItemStack?, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(stack, world, tooltip, context)
        tooltip.add(TranslatableText("item.prima_materia.bow_drill.tooltip"))
    }
}
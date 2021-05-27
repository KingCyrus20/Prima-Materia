package io.meltec.prima

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.item.*
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

/** Starts fires using tinder and patience. */
object BowDrillItem : ToolItem(ToolMaterials.WOOD, FabricItemSettings().maxDamage(100).group(PrimaItems.PRIMARY_GROUP)) {
    override fun appendTooltip(stack: ItemStack?, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(stack, world, tooltip, context)
        tooltip.add(TranslatableText("item.prima_materia.bow_drill.tooltip"))
    }

    override fun getMaxUseTime(stack: ItemStack?): Int = 60
    override fun getUseAction(stack: ItemStack?): UseAction = UseAction.CROSSBOW

    override fun hasGlint(stack: ItemStack?): Boolean = true

    override fun usageTick(world: World?, user: LivingEntity?, stack: ItemStack?, remainingUseTicks: Int) {
        println(remainingUseTicks)
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        if (context.world.isClient) return ActionResult.SUCCESS

        val targetLocation = context.blockPos.offset(context.side)
        if (context.world.canSetBlock(targetLocation)) {
            context.world.setBlockState(targetLocation, Blocks.FIRE.defaultState)
            return ActionResult.SUCCESS
        } else {
            return ActionResult.FAIL
        }
    }
}
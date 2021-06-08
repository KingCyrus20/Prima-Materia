package io.meltec.prima.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.block.ShapeContext
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

object CrucibleBlock : Block(FabricBlockSettings.of(Material.STONE).nonOpaque()) {
  // TODO: Figure out a way to get this from the model
  override fun getOutlineShape(
      state: BlockState,
      world: BlockView,
      pos: BlockPos,
      context: ShapeContext
  ): VoxelShape = VoxelShapes.cuboid(0.3125, 0.0, 0.3125, 0.6875, 0.375, 0.6875)
}

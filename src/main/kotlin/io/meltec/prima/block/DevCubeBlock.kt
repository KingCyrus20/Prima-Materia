package io.meltec.prima.block

import io.meltec.prima.block.entity.DevCubeBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.block.ShapeContext
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

object DevCubeBlock :
    Block(FabricBlockSettings.of(Material.STONE).nonOpaque()), BlockEntityProvider {
  override fun createBlockEntity(pos: BlockPos, state: BlockState) = DevCubeBlockEntity(pos, state)

  // TODO: Figure out a way to get this from the model
  override fun getOutlineShape(
      state: BlockState,
      world: BlockView,
      pos: BlockPos,
      context: ShapeContext
  ): VoxelShape = VoxelShapes.cuboid(0.1, 0.1, 0.1, 0.9, 0.9, 0.9)
}

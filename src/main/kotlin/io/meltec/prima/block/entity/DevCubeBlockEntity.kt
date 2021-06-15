package io.meltec.prima.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class DevCubeBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(PrimaBlockEntities.DEV_CUBE_BLOCK_ENTITY, pos, state)

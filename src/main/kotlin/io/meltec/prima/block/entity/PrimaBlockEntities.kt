package io.meltec.prima.block.entity

import io.meltec.prima.block.DevCubeBlock
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry

object PrimaBlockEntities {
  lateinit var DEV_CUBE_BLOCK_ENTITY: BlockEntityType<DevCubeBlockEntity>
    private set
  fun register() {
    DEV_CUBE_BLOCK_ENTITY =
        Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            "prima_materia:dev_cube_entity",
            BlockEntityType.Builder.create(::DevCubeBlockEntity, DevCubeBlock).build(null))
  }
}

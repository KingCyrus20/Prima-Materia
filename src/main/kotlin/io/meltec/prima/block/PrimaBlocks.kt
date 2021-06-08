package io.meltec.prima.block

import io.meltec.prima.util.PrimaIdentifier
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.util.registry.Registry

object PrimaBlocks {
  fun register() {
    PrimaOreBlocks.registerToMinecraft()
    registerBlock("dev_cube", DevCubeBlock)
  }

  fun registerBlock(identifier: String, block: Block) {
    Registry.register(Registry.BLOCK, PrimaIdentifier(identifier), block)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier(identifier),
        BlockItem(block, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
  }
}

interface PrimaBlockRegistry {
  fun register(identifier: String, block: Block)
  fun registerToMinecraft()
}

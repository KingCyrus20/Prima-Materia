package io.meltec.prima

import io.meltec.prima.util.PrimaIdentifier
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.util.registry.Registry

object PrimaBlocks {
  val COPPER_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 1))
  val TIN_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 1))
  val ZINC_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 1))

  fun register() {
    Registry.register(Registry.BLOCK, PrimaIdentifier("copper_ore"), COPPER_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("copper_ore"),
        BlockItem(COPPER_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("tin_ore"), TIN_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("tin_ore"),
        BlockItem(TIN_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("zinc_ore"), ZINC_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("zinc_ore"),
        BlockItem(ZINC_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
  }
}

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
  val CHALCOCITE_ORE =
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
  val GALENA_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 1))
  val BISMUTHINITE_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 1))
  val ORPIMENT_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 2))
  val STIBNITE_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 2))
  val BORAX_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 2))
  val MAGNESITE_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 3))
  val APATITE_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 3))
  val HEMATITE_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 2))
  val MAGNETITE_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 2))
  val PRIMA_GOLD_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 1))
  val CINNABAR_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 3))
  val SILVER_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 1))
  val SALTPETER_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 2))
  val ROCK_SALT_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 1))
  val CORUNDUM_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 3))
  val BAUXITE_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 2))
  val ACANTHITE_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 2))
  val PYRITE_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 2))
  val CHALCOPYRITE_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 2))
  val ANTHRACITE_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 2))
  val BITUMINOUS_COAL_ORE =
      Block(
          FabricBlockSettings.of(Material.STONE)
              .requiresTool()
              .strength(3.0f)
              .breakByTool(FabricToolTags.PICKAXES, 1))
  val LIGNITE_ORE =
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
    Registry.register(Registry.BLOCK, PrimaIdentifier("chalcocite_ore"), CHALCOCITE_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("chalcocite_ore"),
        BlockItem(CHALCOCITE_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("zinc_ore"), ZINC_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("zinc_ore"),
        BlockItem(ZINC_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("galena_ore"), GALENA_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("galena_ore"),
        BlockItem(GALENA_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("bismuthinite_ore"), BISMUTHINITE_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("bismuthinite_ore"),
        BlockItem(BISMUTHINITE_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("orpiment_ore"), ORPIMENT_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("orpiment_ore"),
        BlockItem(ORPIMENT_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("stibnite_ore"), STIBNITE_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("stibnite_ore"),
        BlockItem(STIBNITE_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("borax_ore"), BORAX_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("borax_ore"),
        BlockItem(BORAX_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("magnesite_ore"), MAGNESITE_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("magnesite_ore"),
        BlockItem(MAGNESITE_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("apatite_ore"), APATITE_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("apatite_ore"),
        BlockItem(APATITE_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("hematite_ore"), HEMATITE_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("hematite_ore"),
        BlockItem(HEMATITE_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("magnetite_ore"), MAGNETITE_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("magnetite_ore"),
        BlockItem(MAGNETITE_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("gold_ore"), PRIMA_GOLD_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("gold_ore"),
        BlockItem(PRIMA_GOLD_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("cinnabar_ore"), CINNABAR_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("cinnabar_ore"),
        BlockItem(CINNABAR_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("silver_ore"), SILVER_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("silver_ore"),
        BlockItem(SILVER_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("saltpeter_ore"), SALTPETER_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("saltpeter_ore"),
        BlockItem(SALTPETER_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("rock_salt_ore"), ROCK_SALT_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("rock_salt_ore"),
        BlockItem(ROCK_SALT_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("corundum_ore"), CORUNDUM_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("corundum_ore"),
        BlockItem(CORUNDUM_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("bauxite_ore"), BAUXITE_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("bauxite_ore"),
        BlockItem(BAUXITE_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("acanthite_ore"), ACANTHITE_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("acanthite_ore"),
        BlockItem(ACANTHITE_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("pyrite_ore"), PYRITE_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("pyrite_ore"),
        BlockItem(PYRITE_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("chalcopyrite_ore"), CHALCOPYRITE_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("chalcopyrite_ore"),
        BlockItem(CHALCOPYRITE_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("anthracite_ore"), ANTHRACITE_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("anthracite_ore"),
        BlockItem(ANTHRACITE_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("bituminous_coal_ore"), BITUMINOUS_COAL_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("bituminous_coal_ore"),
        BlockItem(BITUMINOUS_COAL_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    Registry.register(Registry.BLOCK, PrimaIdentifier("lignite_ore"), LIGNITE_ORE)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier("lignite_ore"),
        BlockItem(LIGNITE_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
  }
}

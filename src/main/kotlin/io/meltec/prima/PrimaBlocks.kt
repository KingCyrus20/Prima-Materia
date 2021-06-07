@file:Suppress("MemberVisibilityCanBePrivate")

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
  val COPPER_ORE = createOre(1)
  val TIN_ORE = createOre(1)
  val CHALCOCITE_ORE = createOre(1)
  val ZINC_ORE = createOre(1)

  val GALENA_ORE = createOre(1)
  val BISMUTHINITE_ORE = createOre(1)
  val ORPIMENT_ORE = createOre(2)
  val STIBNITE_ORE = createOre(2)
  val BORAX_ORE = createOre(2)
  val MAGNESITE_ORE = createOre(3)
  val APATITE_ORE = createOre(3)
  val HEMATITE_ORE = createOre(2)
  val MAGNETITE_ORE = createOre(2)
  val GOLD_ORE = createOre(1)
  val CINNABAR_ORE = createOre(3)
  val SILVER_ORE = createOre(1)
  val SALTPETER_ORE = createOre(2)
  val ROCK_SALT_ORE = createOre(1)
  val CORUNDUM_ORE = createOre(3)
  val BAUXITE_ORE = createOre(2)
  val ACANTHITE_ORE = createOre(2)
  val PYRITE_ORE = createOre(2)
  val CHALCOPYRITE_ORE = createOre(2)
  val ANTHRACITE_ORE = createOre(2)
  val BITUMINOUS_COAL_ORE = createOre(1)
  val LIGNITE_ORE = createOre(1)

  fun register() {
    registerBlock("dev_cube", Block(FabricBlockSettings.of(Material.STONE)))
    registerBlock("ore/copper", COPPER_ORE)
    registerBlock("ore/tin", TIN_ORE)
    registerBlock("ore/chalcocite", CHALCOCITE_ORE)
    registerBlock("ore/zinc", ZINC_ORE)
    registerBlock("ore/galena", GALENA_ORE)
    registerBlock("ore/bismuthinite", BISMUTHINITE_ORE)
    registerBlock("ore/orpiment", ORPIMENT_ORE)
    registerBlock("ore/stibnite", STIBNITE_ORE)
    registerBlock("ore/borax", BORAX_ORE)
    registerBlock("ore/magnesite", MAGNESITE_ORE)
    registerBlock("ore/apatite", APATITE_ORE)
    registerBlock("ore/hematite", HEMATITE_ORE)
    registerBlock("ore/magnetite", MAGNETITE_ORE)
    registerBlock("ore/gold", GOLD_ORE)
    registerBlock("ore/cinnabar", CINNABAR_ORE)
    registerBlock("ore/silver", SILVER_ORE)
    registerBlock("ore/saltpeter", SALTPETER_ORE)
    registerBlock("ore/rock_salt", ROCK_SALT_ORE)
    registerBlock("ore/corundum", CORUNDUM_ORE)
    registerBlock("ore/bauxite", BAUXITE_ORE)
    registerBlock("ore/acanthite", ACANTHITE_ORE)
    registerBlock("ore/pyrite", PYRITE_ORE)
    registerBlock("ore/chalcopyrite", CHALCOPYRITE_ORE)
    registerBlock("ore/anthracite", ANTHRACITE_ORE)
    registerBlock("ore/bituminous_coal", BITUMINOUS_COAL_ORE)
    registerBlock("ore/lignite", LIGNITE_ORE)
  }

  private fun createOre(miningLevel: Int): Block {
    return Block(
        FabricBlockSettings.of(Material.STONE)
            .requiresTool()
            .strength(3.0f)
            .breakByTool(FabricToolTags.PICKAXES, miningLevel))
  }

  private fun registerBlock(identifier: String, oreBlock: Block) {
    Registry.register(Registry.BLOCK, PrimaIdentifier(identifier), oreBlock)
    Registry.register(
        Registry.ITEM,
        PrimaIdentifier(identifier),
        BlockItem(oreBlock, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
  }
}

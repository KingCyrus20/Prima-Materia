package io.meltec.prima.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags
import net.minecraft.block.Block
import net.minecraft.block.Material

// Under development, this warning is not useful atm
@Suppress("MemberVisibilityCanBePrivate")
object PrimaOreBlocks {
  val ACANTHITE = createOre(miningLevel = 2)
  val ANTHRACITE = createOre(miningLevel = 2)
  val APATITE = createOre(miningLevel = 3)
  val BAUXITE = createOre(miningLevel = 2)
  val BISMUTHINITE = createOre(miningLevel = 1)
  val BITUMINOUS_COAL = createOre(miningLevel = 1)
  val BORAX = createOre(miningLevel = 2)
  val CHALCOCITE = createOre(miningLevel = 1)
  val CHALCOPYRITE = createOre(miningLevel = 2)
  val CINNABAR = createOre(miningLevel = 3)
  val COPPER = createOre(miningLevel = 1)
  val CORUNDUM = createOre(miningLevel = 3)
  val GALENA = createOre(miningLevel = 1)
  val GOLD = createOre(miningLevel = 1)
  val HEMATITE = createOre(miningLevel = 2)
  val LIGNITE = createOre(miningLevel = 1)
  val MAGNESITE = createOre(miningLevel = 3)
  val MAGNETITE = createOre(miningLevel = 2)
  val ORPIMENT = createOre(miningLevel = 2)
  val PYRITE = createOre(miningLevel = 2)
  val ROCK_SALT = createOre(miningLevel = 1)
  val SALTPETER = createOre(miningLevel = 2)
  val SILVER = createOre(miningLevel = 1)
  val STIBNITE = createOre(miningLevel = 2)
  val TIN = createOre(miningLevel = 1)
  val ZINC = createOre(miningLevel = 1)

  fun register() {
    registerOre("acanthite", ACANTHITE)
    registerOre("anthracite", ANTHRACITE)
    registerOre("apatite", APATITE)
    registerOre("bauxite", BAUXITE)
    registerOre("bismuthinite", BISMUTHINITE)
    registerOre("bituminous_coal", BITUMINOUS_COAL)
    registerOre("borax", BORAX)
    registerOre("chalcocite", CHALCOCITE)
    registerOre("chalcopyrite", CHALCOPYRITE)
    registerOre("cinnabar", CINNABAR)
    registerOre("copper", COPPER)
    registerOre("corundum", CORUNDUM)
    registerOre("galena", GALENA)
    registerOre("gold", GOLD)
    registerOre("hematite", HEMATITE)
    registerOre("lignite", LIGNITE)
    registerOre("magnesite", MAGNESITE)
    registerOre("magnetite", MAGNETITE)
    registerOre("orpiment", ORPIMENT)
    registerOre("pyrite", PYRITE)
    registerOre("rock_salt", ROCK_SALT)
    registerOre("saltpeter", SALTPETER)
    registerOre("silver", SILVER)
    registerOre("stibnite", STIBNITE)
    registerOre("tin", TIN)
    registerOre("zinc", ZINC)
  }

  private fun registerOre(identifier: String, oreBlock: Block) {
    PrimaBlocks.registerBlock("ore/$identifier", oreBlock)
  }

  private fun createOre(miningLevel: Int): Block {
    return Block(
        FabricBlockSettings.of(Material.STONE)
            .requiresTool()
            .strength(3.0f)
            .breakByTool(FabricToolTags.PICKAXES, miningLevel))
  }
}


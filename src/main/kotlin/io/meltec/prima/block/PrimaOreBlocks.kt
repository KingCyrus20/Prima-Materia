package io.meltec.prima.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags
import net.minecraft.block.Block
import net.minecraft.block.Material

// Under development, this warning is not useful atm
@Suppress("unused")
object PrimaOreBlocks : PrimaBlockRegistry {
  private val blockRegistry = mutableMapOf<String, Block>()
  override fun register(identifier: String, block: Block) {
    blockRegistry[identifier] = block
  }

  private val levelOneOreProvider = BlockProvider { createOre(miningLevel = 1) }
  private val levelTwoOreProvider = BlockProvider { createOre(miningLevel = 2) }
  private val levelThreeOreProvider = BlockProvider { createOre(miningLevel = 3) }

  val ACANTHITE by levelTwoOreProvider
  val ANTHRACITE by levelTwoOreProvider
  val APATITE by levelThreeOreProvider
  val BAUXITE by levelTwoOreProvider
  val BISMUTHINITE by levelOneOreProvider
  val BITUMINOUS_COAL by levelOneOreProvider
  val BORAX by levelTwoOreProvider
  val CHALCOCITE by levelOneOreProvider
  val CHALCOPYRITE by levelTwoOreProvider
  val CINNABAR by levelThreeOreProvider
  val COPPER by levelOneOreProvider
  val CORUNDUM by levelThreeOreProvider
  val GALENA by levelOneOreProvider
  val GOLD by levelOneOreProvider
  val HEMATITE by levelTwoOreProvider
  val LIGNITE by levelOneOreProvider
  val MAGNESITE by levelThreeOreProvider
  val MAGNETITE by levelTwoOreProvider
  val ORPIMENT by levelTwoOreProvider
  val PYRITE by levelTwoOreProvider
  val ROCK_SALT by levelOneOreProvider
  val SALTPETER by levelTwoOreProvider
  val SILVER by levelOneOreProvider
  val STIBNITE by levelTwoOreProvider
  val TIN by levelOneOreProvider
  val ZINC by levelOneOreProvider

  override fun registerToMinecraft() {
    for ((id, block) in blockRegistry) {
      PrimaBlocks.registerBlock("ore/$id", block)
    }
  }

  private fun createOre(miningLevel: Int): Block {
    return PrimaOreBlock(
        FabricBlockSettings.of(Material.STONE)
            .requiresTool()
            .strength(3.0f)
            .breakByTool(FabricToolTags.PICKAXES, miningLevel))
  }
}

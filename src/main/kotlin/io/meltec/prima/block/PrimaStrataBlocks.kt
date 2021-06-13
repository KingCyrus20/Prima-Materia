package io.meltec.prima.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Material

@SuppressWarnings("unused")
object PrimaStrataBlocks : PrimaBlockRegistry {
  private val blockRegistry = mutableMapOf<String, Block>()
  private val strataBlockProvider = BlockProvider {
    // TODO: Mining level?
    Block(FabricBlockSettings.of(Material.STONE).requiresTool())
  }

  object Igneous : PrimaBlockRegistry by PrimaStrataBlocks {
    override val prefix: String
      get() = "igneous/"
    val ANDESITE by strataBlockProvider
    val BASALT by strataBlockProvider
    val DACITE by strataBlockProvider
    val DIORITE by strataBlockProvider
    val GABBRO by strataBlockProvider
    val GRANITE by strataBlockProvider
    val PERIDOTITE by strataBlockProvider
    val RHYOLITE by strataBlockProvider
  }

  object Metamorphic : PrimaBlockRegistry by PrimaStrataBlocks {
    override val prefix: String
      get() = "metamorphic/"
    val GNEISS by strataBlockProvider
    val MARBLE by strataBlockProvider
    val PHYLITE by strataBlockProvider
    val QUARTZITE by strataBlockProvider
    val SCHIST by strataBlockProvider
    val SLATE by strataBlockProvider
  }

  object Sedimentary : PrimaBlockRegistry by PrimaStrataBlocks {
    override val prefix: String
      get() = "sedimentary/"
    val CHALK by strataBlockProvider
    val CHERT by strataBlockProvider
    val DOLOMITE by strataBlockProvider
    val LIMESTONE by strataBlockProvider
    val SANDSTONE by strataBlockProvider
    val SHALE by strataBlockProvider
  }

  override fun register(identifier: String, block: Block) {
    blockRegistry[identifier] = block
  }

  override fun registerToMinecraft() {
    Igneous
    Metamorphic
    Sedimentary
    for ((id, block) in blockRegistry) {
      PrimaBlocks.registerBlock("strata/$id", block)
    }
  }
}

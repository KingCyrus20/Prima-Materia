package io.meltec.prima.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.StringIdentifiable

class PrimaOreBlock(settings: Settings) : Block(settings) {
  init {
      defaultState = stateManager.defaultState.with(strata, StrataBlock.ANDESITE)
  }
  override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
    builder.add(strata)
  }

  companion object {
    private val strata: EnumProperty<StrataBlock> =
        EnumProperty.of("strata", StrataBlock::class.java)
  }
}

enum class StrataBlock : StringIdentifiable {
  ANDESITE,
  BASALT,
  CHALK,
  CHERT,
  DACITE,
  DIORITE,
  DOLOMITE,
  GABBRO,
  GNEISS,
  GRANITE,
  LIMESTONE,
  MARBLE,
  PERIDOTITE,
  PHYLITE,
  QUARTZITE,
  RHYOLITE,
  SANDSTONE,
  SCHIST,
  SHALE,
  SLATE;

  override fun asString() = name.lowercase()
}

package io.meltec.prima.block

import io.meltec.prima.util.PrimaIdentifier
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.util.registry.Registry
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

object PrimaBlocks {
  fun register() {
    PrimaOreBlocks.registerToMinecraft()
    PrimaStrataBlocks.registerToMinecraft()
    registerBlock("dev_cube", DevCubeBlock)
    registerBlock("crucible", CrucibleBlock)
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
  val prefix: String?
    get() = null
}

class BlockProvider<T : Block>(private val blockProvider: () -> T) {
  operator fun provideDelegate(
      thisRef: PrimaBlockRegistry,
      prop: KProperty<*>
  ): ReadOnlyProperty<PrimaBlockRegistry, Block> {
    val block = blockProvider()
    val identifier = thisRef.prefix.orEmpty() + prop.name.lowercase()
    thisRef.register(identifier, block)
    return ReadOnlyProperty { _, _ -> block }
  }
}

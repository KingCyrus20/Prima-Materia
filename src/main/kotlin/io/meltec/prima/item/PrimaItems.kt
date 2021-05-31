package io.meltec.prima.item

import io.meltec.prima.util.PrimaIdentifier
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.registry.Registry

object PrimaItems {
  val GENERAL_GROUP = FabricItemGroupBuilder.create(PrimaIdentifier("general")).build()
  val TOOL_GROUP =
      FabricItemGroupBuilder.create(PrimaIdentifier("tool"))
          .icon { ItemStack(BowDrillItem) }
          .build()
  val EQUIPPABLE_GROUP =
      FabricItemGroupBuilder.create(PrimaIdentifier("equippable"))
          .icon { ItemStack(GliderItem) }
          .build()

  val GliderLeftWingItem = Item(FabricItemSettings().group(GENERAL_GROUP))
  val GliderRightWingItem = Item(FabricItemSettings().group(GENERAL_GROUP))

  fun register() {
    Registry.register(Registry.ITEM, PrimaIdentifier("bow_drill"), BowDrillItem)
    Registry.register(Registry.ITEM, PrimaIdentifier("glider_left_wing"), GliderLeftWingItem)
    Registry.register(Registry.ITEM, PrimaIdentifier("glider_right_wing"), GliderRightWingItem)
    Registry.register(Registry.ITEM, PrimaIdentifier("glider"), GliderItem)
  }
}

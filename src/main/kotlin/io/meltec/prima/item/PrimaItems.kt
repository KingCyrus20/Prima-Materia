package io.meltec.prima.item

import io.meltec.prima.FlintToolMaterial
import io.meltec.prima.util.PrimaIdentifier
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.util.registry.Registry

object PrimaItems {
  private val GENERAL_GROUP: ItemGroup =
      FabricItemGroupBuilder.create(PrimaIdentifier("general")).build()
  val TOOL_GROUP: ItemGroup =
      FabricItemGroupBuilder.create(PrimaIdentifier("tool"))
          .icon { ItemStack(BowDrillItem) }
          .build()
  private val COMBAT_GROUP: ItemGroup =
      FabricItemGroupBuilder.create(PrimaIdentifier("combat"))
          .icon { ItemStack(FLINT_SWORD) }
          .build()
  val EQUIPPABLE_GROUP: ItemGroup =
      FabricItemGroupBuilder.create(PrimaIdentifier("equippable"))
          .icon { ItemStack(GliderItem) }
          .build()

  private val GliderLeftWingItem = Item(FabricItemSettings().group(GENERAL_GROUP))
  private val GliderRightWingItem = Item(FabricItemSettings().group(GENERAL_GROUP))
  private val FLINT_SWORD =
      SwordItem(FlintToolMaterial(), 4, -2.4f, FabricItemSettings().group(COMBAT_GROUP))
  private val FLINT_PICKAXE =
      PrimaPickaxeItem(FlintToolMaterial(), 2, -2.8f, FabricItemSettings().group(TOOL_GROUP))

  fun register() {
    Registry.register(Registry.ITEM, PrimaIdentifier("bow_drill"), BowDrillItem)
    Registry.register(Registry.ITEM, PrimaIdentifier("glider_left_wing"), GliderLeftWingItem)
    Registry.register(Registry.ITEM, PrimaIdentifier("glider_right_wing"), GliderRightWingItem)
    Registry.register(Registry.ITEM, PrimaIdentifier("glider"), GliderItem)
    Registry.register(Registry.ITEM, PrimaIdentifier("flint_sword"), FLINT_SWORD)
    Registry.register(Registry.ITEM, PrimaIdentifier("flint_pickaxe"), FLINT_PICKAXE)
  }
}

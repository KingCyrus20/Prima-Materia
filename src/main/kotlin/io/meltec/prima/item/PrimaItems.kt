package io.meltec.prima.item

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

  private val GLIDER_LEFT_WING_ITEM = Item(FabricItemSettings().group(GENERAL_GROUP))
  private val GLIDER_RIGHT_WING_ITEM = Item(FabricItemSettings().group(GENERAL_GROUP))
  private val LEATHER_BINDING_ITEM =
      ToolBindingItem(FabricItemSettings().group(GENERAL_GROUP), 1, PrimaToolMaterials.LEATHER)
  private val STRING_BINDING_ITEM =
      ToolBindingItem(FabricItemSettings().group(GENERAL_GROUP), 0, PrimaToolMaterials.STRING)
  private val WOOD_HANDLE_ITEM =
      ToolHandleItem(FabricItemSettings().group(GENERAL_GROUP), 0, PrimaToolMaterials.WOOD)
  private val FLINT_PICKAXE_HEAD_ITEM =
      PickaxeHeadItem(FabricItemSettings().group(GENERAL_GROUP), 0, PrimaToolMaterials.FLINT)
  private val FLINT_SWORD =
      SwordItem(PrimaToolMaterials.FLINT, 0, -2.4f, FabricItemSettings().group(COMBAT_GROUP))
  val PRIMA_PICKAXE = PrimaPickaxe(PrimaToolMaterials.FLINT, 0,0f,FabricItemSettings().group(
      TOOL_GROUP))

  fun register() {
    Registry.register(Registry.ITEM, PrimaIdentifier("bow_drill"), BowDrillItem)
    Registry.register(Registry.ITEM, PrimaIdentifier("glider_left_wing"), GLIDER_LEFT_WING_ITEM)
    Registry.register(Registry.ITEM, PrimaIdentifier("glider_right_wing"), GLIDER_RIGHT_WING_ITEM)
    Registry.register(Registry.ITEM, PrimaIdentifier("glider"), GliderItem)
    Registry.register(Registry.ITEM, PrimaIdentifier("leather_binding"), LEATHER_BINDING_ITEM)
    Registry.register(Registry.ITEM, PrimaIdentifier("string_binding"), STRING_BINDING_ITEM)
    Registry.register(Registry.ITEM, PrimaIdentifier("wood_handle"), WOOD_HANDLE_ITEM)
    Registry.register(Registry.ITEM, PrimaIdentifier("flint_pickaxe_head"), FLINT_PICKAXE_HEAD_ITEM)
    Registry.register(Registry.ITEM, PrimaIdentifier("flint_sword"), FLINT_SWORD)
    Registry.register(Registry.ITEM, PrimaIdentifier("prima_pickaxe"), PRIMA_PICKAXE)
  }
}

package io.meltec.prima

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object PrimaBlocks {
    val COPPER_ORE = Block(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3.0f))
    val TIN_ORE = Block(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3.0f))
    val ZINC_ORE = Block(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3.0f))

    fun register(){
        Registry.register(Registry.BLOCK, Identifier("prima_materia", "copper_ore"), COPPER_ORE)
        Registry.register(Registry.ITEM, Identifier("prima_materia", "copper_ore"),
            BlockItem(COPPER_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
        Registry.register(Registry.BLOCK, Identifier("prima_materia", "tin_ore"), TIN_ORE)
        Registry.register(Registry.ITEM, Identifier("prima_materia", "tin_ore"),
            BlockItem(TIN_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
        Registry.register(Registry.BLOCK, Identifier("prima_materia", "zinc_ore"), ZINC_ORE)
        Registry.register(Registry.ITEM, Identifier("prima_materia", "zinc_ore"),
            BlockItem(ZINC_ORE, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)))
    }
}
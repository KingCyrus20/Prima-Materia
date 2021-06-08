package io.meltec.prima.item

import net.minecraft.item.Item

open class PrimaToolPartItem(
    settings: Settings,
    val qualityModifier: Int = 0,
    val toolMaterial: PrimaToolMaterials
) : Item(settings)

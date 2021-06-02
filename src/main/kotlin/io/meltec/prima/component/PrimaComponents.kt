package io.meltec.prima.component

import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer
import net.minecraft.item.ToolItem
import net.minecraft.util.Identifier

class PrimaComponents : ItemComponentInitializer {
  val QUALITY: ComponentKey<ItemIntComponent> =
      ComponentRegistry.getOrCreate(
          Identifier("prima_materia", "quality"), ItemIntComponent::class.java)
  override fun registerItemComponentFactories(registry: ItemComponentFactoryRegistry) {
    registry.register({ item -> item is ToolItem }, QUALITY) { itemStack ->
      ItemIntComponent(itemStack)
    }
  }
}

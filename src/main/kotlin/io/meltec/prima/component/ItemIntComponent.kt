package io.meltec.prima.component

import dev.onyxstudios.cca.api.v3.component.Component
import dev.onyxstudios.cca.api.v3.item.ItemComponent
import net.fabricmc.fabric.api.util.NbtType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag

class ItemIntComponent(itemStack: ItemStack) : ItemComponent(itemStack) {
  var quality: Int = 0
    get() {
      if (!hasTag("quality", NbtType.INT)) putInt("quality", 0)
      return getInt("quality")
    }
    set(value) {
      field = value
      putInt("quality", value)
    }
}

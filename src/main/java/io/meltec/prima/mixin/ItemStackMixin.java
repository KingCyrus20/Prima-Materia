package io.meltec.prima.mixin;

import io.meltec.prima.ItemStackExt;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackExt {
  private int quality = 0;

  public int getQuality() {
    return quality;
  }

  public void setQuality(int quality) {
    this.quality = quality;
  }

  @Inject(method = "toTag", at = @At("TAIL"))
  public void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
    tag.putInt("Quality", this.quality);
  }
}

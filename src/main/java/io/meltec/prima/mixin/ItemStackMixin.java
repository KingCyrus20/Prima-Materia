package io.meltec.prima.mixin;

import io.meltec.prima.item.PrimaPickaxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Final
    @Shadow
    private Item item;
    @Shadow
    private CompoundTag tag;

    @Inject(method = "getMaxDamage", at = @At("HEAD"), cancellable = true)
    public void getMaxDamage(CallbackInfoReturnable<Integer> ci) {
        if (item instanceof PrimaPickaxeItem && tag.contains("durability")) {
            ci.setReturnValue(tag.getInt("durability"));
        }
    }
}

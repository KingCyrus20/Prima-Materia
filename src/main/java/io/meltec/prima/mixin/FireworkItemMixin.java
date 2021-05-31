package io.meltec.prima.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworkItem.class)
public class FireworkItemMixin {
  @Inject(method = "use", at = @At("INVOKE"), cancellable = true)
  public void use(
      World world,
      PlayerEntity user,
      Hand hand,
      CallbackInfoReturnable<TypedActionResult<ItemStack>> ci) {
    if (user.isFallFlying() && user.getEquippedStack(EquipmentSlot.CHEST).getItem() != Items.ELYTRA)
      ci.setReturnValue(TypedActionResult.pass(user.getStackInHand(hand)));
  }
}

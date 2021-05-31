package io.meltec.prima.mixin;

import static java.lang.Math.abs;

import io.meltec.prima.item.BowDrillItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
  @Final @Shadow private MinecraftClient client;

  @Shadow
  private void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress) {}

  @Shadow
  public void renderItem(
      LivingEntity entity,
      ItemStack stack,
      ModelTransformation.Mode renderMode,
      boolean leftHanded,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light) {}

  @Inject(
      method =
          "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;"
              + "FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;"
              + "FLnet/minecraft/client/util/math/MatrixStack;"
              + "Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/item/ItemStack;getUseAction()Lnet/minecraft/util/UseAction;"),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION,
      cancellable = true)
  private void renderFirstPersonItem(
      AbstractClientPlayerEntity player,
      float tickDelta,
      float pitch,
      Hand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      CallbackInfo callbackInfo,
      Arm arm,
      boolean bl,
      int o) {
    if (item.getItem() instanceof BowDrillItem) {
      applyEquipOffset(matrices, arm, equipProgress);
      HitResult hitResult = player.raycast(3.0f, tickDelta, true);
      if (hitResult instanceof BlockHitResult) {
        Vec3d delta = player.getPos().subtract(hitResult.getPos());
        double deltaForward =
            abs(player.getHorizontalFacing().getAxis().choose(delta.x, delta.y, delta.z));
        matrices.translate(0, 0, -MathHelper.sqrt(deltaForward * deltaForward + 4));
      }
      int midPoint = 10;
      float rotation;
      int currentPoint = client.player.getItemUseTimeLeft() % 20;
      if (currentPoint < midPoint) {
        rotation = currentPoint;
      } else {
        rotation = midPoint + midPoint - currentPoint;
      }
      matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90f));
      matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-15f));
      matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90f + rotation));

      boolean bl4 = arm == Arm.RIGHT;
      renderItem(
          player,
          item,
          bl4
              ? ModelTransformation.Mode.FIRST_PERSON_RIGHT_HAND
              : ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND,
          !bl4,
          matrices,
          vertexConsumers,
          light);
      matrices.pop();

      callbackInfo.cancel();
    }
  }
}

package io.meltec.prima.mixin;

import net.minecraft.client.render.SkyProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyProperties.class)
public abstract class SkyPropertiesMixin {
  @Accessor("skyType")
  abstract SkyProperties.SkyType getSkyType();

  @SuppressWarnings("SameParameterValue")
  @Accessor("cloudsHeight")
  abstract void setCloudsHeight(float cloudsHeight);

  @Inject(
      method = "<init>(FZLnet/minecraft/client/render/SkyProperties$SkyType;ZZ)V",
      at = @At("TAIL"))
  private void SkyProperties(
      float cloudsHeight,
      boolean alternateSkyColor,
      SkyProperties.SkyType skyType,
      boolean brightenLighting,
      boolean darkened,
      CallbackInfo ci) {
    if (getSkyType() == SkyProperties.SkyType.NORMAL) setCloudsHeight(192f);
  }
}

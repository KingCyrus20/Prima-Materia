package io.meltec.prima.mixin;

import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.chunk.NoiseSamplingConfig;
import net.minecraft.world.gen.chunk.SlideConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GenerationShapeConfig.class)
public class GenerationShapeConfigMixin {
    @Shadow
    private int verticalSize;

    @Inject(method = "<init>(ILnet/minecraft/world/gen/chunk/NoiseSamplingConfig;" +
            "Lnet/minecraft/world/gen/chunk/SlideConfig;Lnet/minecraft/world/gen/chunk/SlideConfig;IIDDZZZZ)V",
            at = @At("TAIL"))
    private void GenerationShapeConfig(int height, NoiseSamplingConfig sampling, SlideConfig topSlide,
                                       SlideConfig bottomSlide, int sizeHorizontal, int sizeVertical,
                                       double densityFactor, double densityOffset, boolean simplexSurfaceNoise,
                                       boolean randomDensityOffset, boolean islandNoiseOverride, boolean amplified,
                                       CallbackInfo ci){
        this.verticalSize = 34;
    }
}

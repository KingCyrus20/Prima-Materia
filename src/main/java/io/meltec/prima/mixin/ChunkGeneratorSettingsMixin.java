package io.meltec.prima.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGeneratorSettings.class)
public class ChunkGeneratorSettingsMixin {
    @Shadow
    private int seaLevel;

    @Inject(method = "<init>(Lnet/minecraft/world/gen/chunk/StructuresConfig;" +
            "Lnet/minecraft/world/gen/chunk/GenerationShapeConfig;Lnet/minecraft/block/BlockState;" +
            "Lnet/minecraft/block/BlockState;IIIZ)V", at = @At("TAIL"))
    private void ChunkGeneratorSettings(StructuresConfig structuresConfig, GenerationShapeConfig generationShapeConfig,
                                        BlockState defaultBlock, BlockState defaultFluid, int bedrockCeilingY,
                                        int bedrockFloorY, int seaLevel, boolean mobGenerationDisabled, CallbackInfo ci){
        this.seaLevel = 127;
    }
}

package io.meltec.prima.mixin;

import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {
    @Inject(method = "getSeaLevel()I", at = @At("RETURN"), cancellable = true)
    private void getSeaLevel(CallbackInfoReturnable<Integer> info){
        info.setReturnValue(127);
    }

    @Inject(method = "getSpawnHeight()I", at = @At("RETURN"), cancellable = true)
    private void getSpawnHeight(CallbackInfoReturnable<Integer> info){
        info.setReturnValue(128);
    }
}

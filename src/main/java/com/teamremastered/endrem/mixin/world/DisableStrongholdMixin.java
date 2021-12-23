package com.teamremastered.endrem.mixin.world;

import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGenerator.class)
public class DisableStrongholdMixin {

    @Inject(method = "generateStrongholds", at = @At(value = "HEAD"), cancellable = true)
    private void RemoveVanillaStronghold(CallbackInfo ci) {
        ci.cancel();
    }
}

package com.teamremastered.endrem.mixin.world;

import com.teamremastered.endrem.world.structures.config.ERStructures;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(LakeFeature.class)
public class NoLakesInStructuresMixin {
    @Inject(
            method = "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/WorldGenLevel;startsForFeature(Lnet/minecraft/core/SectionPos;Lnet/minecraft/world/level/levelgen/feature/StructureFeature;)Ljava/util/stream/Stream;"),
            cancellable = true
    )
    /* Disables Lake features from generating inside structures */
    private void NoLakesInStructures(FeaturePlaceContext<BlockStateConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        SectionPos sectionPos = SectionPos.of(context.origin());
        if (context.level().startsForFeature(sectionPos, ERStructures.END_CASTLE.get()).findAny().isPresent() || context.level().startsForFeature(sectionPos, ERStructures.END_GATE.get()).findAny().isPresent()) {
            cir.setReturnValue(false);
        }
    }
}

package com.teamremastered.endrem.mixin.world;

import com.teamremastered.endrem.world.structures.config.ERStructures;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.SectionPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.LakesFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;


@Mixin(LakesFeature.class)
public class NoLakesInStructuresMixin {
    @Inject(
            method = "place(Lnet/minecraft/world/ISeedReader;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/BlockStateFeatureConfig;)Z",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/BlockPos;below(I)Lnet/minecraft/util/math/BlockPos;"),
            cancellable = true
    )
    /* Disables Lake features from generating inside structures */
    private void noLakesInStructures(ISeedReader seedReader, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, BlockStateFeatureConfig singleStateFeatureConfig, CallbackInfoReturnable<Boolean> cir) {
        SectionPos sectionPos = SectionPos.of(blockPos);
        if (seedReader.startsForFeature(sectionPos, ERStructures.END_CASTLE.get()).findAny().isPresent() || seedReader.startsForFeature(sectionPos, ERStructures.END_GATE.get()).findAny().isPresent()) {
            cir.setReturnValue(false);
        }
    }
}

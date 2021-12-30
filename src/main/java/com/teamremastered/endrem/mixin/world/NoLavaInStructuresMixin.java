package com.teamremastered.endrem.mixin.world;

import com.teamremastered.endrem.world.structures.config.ERStructures;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.SectionPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.LiquidsConfig;
import net.minecraft.world.gen.feature.SpringFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(SpringFeature.class)
public class NoLavaInStructuresMixin {

    @Inject(
            method = "place",
            at = @At(value = "HEAD"),
            cancellable = true
    )

    /* Removes Lava and Water Features generating in stone blocks */
    private void NoLavaInStructures(ISeedReader seedReader, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, LiquidsConfig springFeatureConfig, CallbackInfoReturnable<Boolean> cir) {
        if(springFeatureConfig.state.is(FluidTags.LAVA) || springFeatureConfig.state.is(FluidTags.WATER)) {
            BlockPos.Mutable  mutable = new BlockPos.Mutable();
            SectionPos sectionPos;
            for(Direction face : Direction.Plane.HORIZONTAL) {
                mutable.set(blockPos).move(face);
                sectionPos = SectionPos.of(mutable);
                if (seedReader.startsForFeature(sectionPos, ERStructures.END_CASTLE.get()).findAny().isPresent() || seedReader.startsForFeature(sectionPos, ERStructures.END_GATE.get()).findAny().isPresent()) {
                        cir.setReturnValue(false);
                    }
                }
            }
        }
    }

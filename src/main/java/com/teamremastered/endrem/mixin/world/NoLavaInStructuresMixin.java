package com.teamremastered.endrem.mixin.world;

import com.teamremastered.endrem.world.structures.config.ERStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SpringFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpringFeature.class)
public class NoLavaInStructuresMixin {

    @Inject(
            method = "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z",
            at = @At(value = "HEAD"),
            cancellable = true
    )

    /* Removes Lava and Water Features generating in stone blocks */
    private void NoLavaInStructures(FeaturePlaceContext<SpringConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        if(context.config().state.is(FluidTags.LAVA) || context.config().state.is(FluidTags.WATER)) {
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            SectionPos sectionPos;
            for(Direction face : Direction.Plane.HORIZONTAL) {
                mutable.set(context.origin()).move(face);
                sectionPos = SectionPos.of(mutable);
                if (context.level().startsForFeature(sectionPos, ERStructures.END_CASTLE.get()).stream().findAny().isPresent() || context.level().startsForFeature(sectionPos, ERStructures.END_GATE.get()).stream().findAny().isPresent()) {
                        cir.setReturnValue(false);
                    }
                }
            }
        }
    }

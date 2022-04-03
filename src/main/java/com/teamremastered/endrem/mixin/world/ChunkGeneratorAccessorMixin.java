package com.teamremastered.endrem.mixin.world;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkGenerator.class)
public interface ChunkGeneratorAccessorMixin {
    @Accessor("settings")
    void endrem_setSettings(DimensionStructuresSettings dimensionStructuresSettings);

    @Invoker("codec")
    Codec<ChunkGenerator> endrem_getCodec();
}

package com.teamremastered.endrem.blocks;

import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class EndCrystalBlock extends AbstractGlassBlock {
    public EndCrystalBlock() {
        super(AbstractGlassBlock.Properties.of(Material.GLASS)
                .strength(5f, 6f)
                .sound(SoundType.GLASS)
                .requiresCorrectToolForDrops()
                .noOcclusion()
        );
    }
}

package com.teamremastered.endrem.blocks;

import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

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

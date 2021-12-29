package com.teamremastered.endrem.blocks;


import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import javax.annotation.ParametersAreNonnullByDefault;

public class EndCrystalOre extends OreBlock {
    public EndCrystalOre() {
        super(AbstractBlock.Properties.copy(Blocks.ANCIENT_DEBRIS));
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortune, int silkTouch) {
        return silkTouch == 0 ? (int) (Math.random() * 8) : 0;
    }
}

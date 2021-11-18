package com.teamremastered.endrem.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class EndCrystalOre extends OreBlock {
    public EndCrystalOre() {
        super(BlockBehaviour.Properties.copy(Blocks.ANCIENT_DEBRIS));
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getExpDrop(BlockState state, net.minecraft.world.level.LevelReader reader, BlockPos pos, int fortune, int silkTouch) {
        return silkTouch == 0 ? (int) (Math.random() * 8) : 0;
    }
}

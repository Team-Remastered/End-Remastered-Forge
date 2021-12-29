package com.teamremastered.endrem.utils;

import com.google.common.collect.ImmutableList;
import com.teamremastered.endrem.blocks.AncientPortalFrame;
import com.teamremastered.endrem.blocks.ERFrameProperties;
import com.teamremastered.endrem.registers.ERBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class ERPortalPredicate implements Predicate<BlockState> {
    private final ImmutableList<Block> VALID_BLOCKS = ImmutableList.of(
            Blocks.END_PORTAL_FRAME,
            ERBlocks.ANCIENT_PORTAL_FRAME.get()
    );

    private final Direction DIRECTION;
    private ERFrameProperties EXCLUDED_EYE;
    private boolean REQUIRE_ANCIENT_FRAME = false;

    private ERPortalPredicate(Direction directionIn) {
        this.DIRECTION = directionIn;
    }

    public boolean test(@Nullable BlockState blockstate) {
        if (blockstate != null && this.VALID_BLOCKS.contains(blockstate.getBlock())) {
            if (blockstate.getBlock() != ERBlocks.ANCIENT_PORTAL_FRAME.get() && this.REQUIRE_ANCIENT_FRAME) {
                return false;
            }

            if (blockstate.getBlock().equals(ERBlocks.ANCIENT_PORTAL_FRAME.get()) && this.EXCLUDED_EYE != null) {
                if (blockstate.getValue(AncientPortalFrame.EYE).equals(EXCLUDED_EYE)) {
                    return false;
                }
            }

            return blockstate.getValue(HorizontalBlock.FACING) == this.DIRECTION;
        } else {
            return false;
        }
    }

    public static ERPortalPredicate facing(Direction direction) {
        return new ERPortalPredicate(direction);
    }

    public ERPortalPredicate withoutEye(ERFrameProperties eye) {
        this.EXCLUDED_EYE = eye;
        return this;
    }

    public ERPortalPredicate requireAncientFrame(boolean var) {
        this.REQUIRE_ANCIENT_FRAME = var;
        return this;
    }
}

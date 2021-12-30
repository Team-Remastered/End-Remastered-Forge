package com.teamremastered.endrem.blocks;

import com.teamremastered.endrem.utils.ERPortalPredicate;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class AncientPortalFrame extends Block {
    public static final EnumProperty<ERFrameProperties> EYE = EnumProperty.create("eye", ERFrameProperties.class);
    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    // Declare Voxel Shapes (BASE = no eye, EYE = only eye, FULL = both)
    protected static final VoxelShape BASE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D);
    protected static final VoxelShape EYE_SHAPE = Block.box(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    protected static final VoxelShape FULL_SHAPE = VoxelShapes.or(BASE_SHAPE, EYE_SHAPE);

    // Shape of the portal:
    //      v   v   v
    //   >            <
    //   >            <
    //   >            <
    //     ^   ^   ^
    public static BlockPattern getPortalShape(@Nullable ERFrameProperties excludedEyeState, Boolean filled) {
        return BlockPatternBuilder.start()
                .aisle("?vvv?", ">???<", ">???<", ">???<", "?^^^?")
                .where('?', CachedBlockInfo.hasState(BlockStateMatcher.ANY))
                .where('^', CachedBlockInfo.hasState(ERPortalPredicate.facing(Direction.SOUTH).withoutEye(excludedEyeState).requireAncientFrame(filled)))
                .where('>', CachedBlockInfo.hasState(ERPortalPredicate.facing(Direction.WEST).withoutEye(excludedEyeState).requireAncientFrame(filled)))
                .where('v', CachedBlockInfo.hasState(ERPortalPredicate.facing(Direction.NORTH).withoutEye(excludedEyeState).requireAncientFrame(filled)))
                .where('<', CachedBlockInfo.hasState(ERPortalPredicate.facing(Direction.EAST).withoutEye(excludedEyeState).requireAncientFrame(filled)))
                .build();
    }

    public AncientPortalFrame() {
        super(AbstractBlock.Properties.of(
                        Material.STONE,
                        MaterialColor.COLOR_PURPLE)
                .sound(SoundType.GLASS)
                .lightLevel((p_152690_) -> 1)
                .strength(-1.0F, 3600000.0F)
                .noDrops()
        );

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(EYE, ERFrameProperties.EMPTY));
    }

    @ParametersAreNonnullByDefault
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext context) {
        return state.getValue(EYE) == ERFrameProperties.EMPTY ? BASE_SHAPE : FULL_SHAPE;
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        return this.defaultBlockState().setValue(FACING, useContext.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING, EYE);
    }

    // Verify if a given frame is already present in a portal (only works if the portal is built correctly)
    public static boolean IsFrameAbsent(World WorldIn, BlockState frameState, BlockPos pos) {
        BlockPattern.PatternHelper blockpattern$patternhelper = getPortalShape(
                frameState.getValue(AncientPortalFrame.EYE), false).find(WorldIn, pos);

        return blockpattern$patternhelper != null;
    }
}

package com.teamremastered.endrem.blocks;

import com.google.common.base.Predicates;
import com.teamremastered.endrem.registers.ERBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class AncientPortalFrame extends Block {
    public static final EnumProperty<ERFrameProperties> EYE = EnumProperty.create("eye", ERFrameProperties.class);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    // Declare Voxel Shapes (BASE = no eye, EYE = only eye, FULL = both)
    protected static final VoxelShape BASE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D);
    protected static final VoxelShape EYE_SHAPE = Block.box(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    protected static final VoxelShape FULL_SHAPE = Shapes.or(BASE_SHAPE, EYE_SHAPE);

    // Shape of the portal:
    //      v   v   v
    //   >            <
    //   >            <
    //   >            <
    //     ^   ^   ^
    private static BlockPattern FILLED_PORTAL_SHAPE;

    public static BlockPattern getOrCreateFilledPortalShape() {
        if (FILLED_PORTAL_SHAPE == null) {
            //noinspection Guava
            FILLED_PORTAL_SHAPE = BlockPatternBuilder.start()
                    .aisle("?vvv?", ">???<", ">???<", ">???<", "?^^^?")
                    .where('?', BlockInWorld.hasState(BlockStatePredicate.ANY))
                    .where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(ERBlocks.ANCIENT_PORTAL_FRAME.get())
                            .where(EYE, Predicates.not(Predicates.equalTo(ERFrameProperties.EMPTY)))
                            .where(FACING, Predicates.equalTo(Direction.SOUTH))))
                    .where('>', BlockInWorld.hasState(BlockStatePredicate.forBlock(ERBlocks.ANCIENT_PORTAL_FRAME.get())
                            .where(EYE, Predicates.not(Predicates.equalTo(ERFrameProperties.EMPTY)))
                            .where(FACING, Predicates.equalTo(Direction.WEST))))
                    .where('v', BlockInWorld.hasState(BlockStatePredicate.forBlock(ERBlocks.ANCIENT_PORTAL_FRAME.get())
                            .where(EYE, Predicates.not(Predicates.equalTo(ERFrameProperties.EMPTY)))
                            .where(FACING, Predicates.equalTo(Direction.NORTH))))
                    .where('<', BlockInWorld.hasState(BlockStatePredicate.forBlock(ERBlocks.ANCIENT_PORTAL_FRAME.get())
                            .where(EYE, Predicates.not(Predicates.equalTo(ERFrameProperties.EMPTY)))
                            .where(FACING, Predicates.equalTo(Direction.EAST))))
                    .build();
        }

        return FILLED_PORTAL_SHAPE;
    }

    public static BlockPattern getOrCreateCustomPortalShape(ERFrameProperties eye) {
        //noinspection Guava
        return BlockPatternBuilder.start()
                    .aisle("?vvv?", ">???<", ">???<", ">???<", "?^^^?")
                    .where('?', BlockInWorld.hasState(BlockStatePredicate.ANY))
                    .where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(ERBlocks.ANCIENT_PORTAL_FRAME.get())
                            .where(EYE, Predicates.not(Predicates.equalTo(eye)))
                            .where(FACING, Predicates.equalTo(Direction.SOUTH))))
                    .where('>', BlockInWorld.hasState(BlockStatePredicate.forBlock(ERBlocks.ANCIENT_PORTAL_FRAME.get())
                            .where(EYE, Predicates.not(Predicates.equalTo(eye)))
                            .where(FACING, Predicates.equalTo(Direction.WEST))))
                    .where('v', BlockInWorld.hasState(BlockStatePredicate.forBlock(ERBlocks.ANCIENT_PORTAL_FRAME.get())
                            .where(EYE, Predicates.not(Predicates.equalTo(eye)))
                            .where(FACING, Predicates.equalTo(Direction.NORTH))))
                    .where('<', BlockInWorld.hasState(BlockStatePredicate.forBlock(ERBlocks.ANCIENT_PORTAL_FRAME.get())
                            .where(EYE, Predicates.not(Predicates.equalTo(eye)))
                            .where(FACING, Predicates.equalTo(Direction.EAST))))
                    .build();
    }

    public AncientPortalFrame() {
        super(BlockBehaviour.Properties.of(
                        Material.STONE,
                        MaterialColor.COLOR_GRAY)
                .sound(SoundType.GLASS)
                .lightLevel((p_152690_) -> 1)
                .strength(-1.0F, 3600000.0F)
                .noDrops()
        );

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(EYE, ERFrameProperties.EMPTY));
    }

    @ParametersAreNonnullByDefault
    @Nonnull
    public VoxelShape getShape(BlockState state, BlockGetter blockReader, BlockPos pos, CollisionContext context) {
        return state.getValue(EYE) == ERFrameProperties.EMPTY ? BASE_SHAPE : FULL_SHAPE;
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }


    public BlockState getStateForPlacement(BlockPlaceContext useContext) {
        return this.defaultBlockState().setValue(FACING, useContext.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING, EYE);
    }

    // Verify if a given frame is already present in a certain range
    public static boolean IsFrameAbsent(Level levelIn, BlockState frameState, BlockPos pos) {
        BlockPattern.BlockPatternMatch blockpattern$patternhelper = getOrCreateCustomPortalShape(frameState.getValue(AncientPortalFrame.EYE)).find(levelIn, pos);
        return blockpattern$patternhelper != null;
    }
}

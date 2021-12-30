package com.teamremastered.endrem.world.structures.processors;

//Workaround made by YungNickYoung for water sources in structures spreading into waterloggable blocks

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class WaterLoggedProcessor extends StructureProcessor {

        public static final WaterLoggedProcessor INSTANCE = new WaterLoggedProcessor();
        public static final Codec<WaterLoggedProcessor> CODEC = Codec.unit(() -> INSTANCE);

        @ParametersAreNonnullByDefault
        @Override
        public Template.BlockInfo process(IWorldReader worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {

            ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);

            // Check if block is waterloggable and not intended to be waterlogged
            if (blockInfoGlobal.state.hasProperty(BlockStateProperties.WATERLOGGED) && !blockInfoGlobal.state.getValue(BlockStateProperties.WATERLOGGED)) {
                IChunk currentChunk = worldReader.getChunk(currentChunkPos.x, currentChunkPos.z);
                if (worldReader.getFluidState(blockInfoGlobal.pos).is(FluidTags.WATER)) {
                    currentChunk.setBlockState(blockInfoGlobal.pos, Blocks.STONE_BRICKS.defaultBlockState(), false);
                }

                // Remove water in adjacent blocks
                BlockPos.Mutable mutable = new BlockPos.Mutable();
                for (Direction direction : Direction.values()) {
                    mutable.set(blockInfoGlobal.pos).move(direction);
                    if (currentChunkPos.x != mutable.getX() >> 4 || currentChunkPos.z != mutable.getZ() >> 4) {
                        currentChunk = worldReader.getChunk(mutable);
                        currentChunkPos = new ChunkPos(mutable);
                    }

                    if (currentChunk.getFluidState(mutable).is(FluidTags.WATER)) {
                        if (!(currentChunk.getBlockState(mutable).getBlock() instanceof IWaterLoggable && currentChunk.getBlockState(mutable).getValue(BlockStateProperties.WATERLOGGED))) {
                            currentChunk.setBlockState(mutable, Blocks.STONE_BRICKS.defaultBlockState(), false);
                        }
                    }
                }
            }
            return blockInfoGlobal;
        }

        @Nonnull
        protected IStructureProcessorType<?> getType() {
            return ERProcessors.WATERLOGGED_PROCESSOR;
        }
    }


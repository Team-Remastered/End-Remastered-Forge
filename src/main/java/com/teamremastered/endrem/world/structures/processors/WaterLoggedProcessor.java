package com.teamremastered.endrem.world.structures.processors;

//Workaround made by YungNickYoung for water sources in structures spreading into waterloggable blocks

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class WaterLoggedProcessor extends StructureProcessor {

        public static final WaterLoggedProcessor INSTANCE = new WaterLoggedProcessor();
        public static final Codec<WaterLoggedProcessor> CODEC = Codec.unit(() -> INSTANCE);

        @ParametersAreNonnullByDefault
        @Override
        public StructureTemplate.StructureBlockInfo process(LevelReader worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, StructureTemplate.StructureBlockInfo blockInfoLocal, StructureTemplate.StructureBlockInfo blockInfoGlobal, StructurePlaceSettings structurePlacementData, @Nullable StructureTemplate template) {

            ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);

            // Check if block is waterloggable and not intended to be waterlogged
            if (blockInfoGlobal.state.hasProperty(BlockStateProperties.WATERLOGGED) && !blockInfoGlobal.state.getValue(BlockStateProperties.WATERLOGGED)) {
                ChunkAccess currentChunk = worldReader.getChunk(currentChunkPos.x, currentChunkPos.z);
                if (worldReader.getFluidState(blockInfoGlobal.pos).is(FluidTags.WATER)) {
                    currentChunk.setBlockState(blockInfoGlobal.pos, Blocks.STONE_BRICKS.defaultBlockState(), false);
                }

                // Remove water in adjacent blocks
                BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                for (Direction direction : Direction.values()) {
                    mutable.set(blockInfoGlobal.pos).move(direction);
                    if (currentChunkPos.x != mutable.getX() >> 4 || currentChunkPos.z != mutable.getZ() >> 4) {
                        currentChunk = worldReader.getChunk(mutable);
                        currentChunkPos = new ChunkPos(mutable);
                    }

                    if (currentChunk.getFluidState(mutable).is(FluidTags.WATER)) {
                        if (!(currentChunk.getBlockState(mutable).getBlock() instanceof SimpleWaterloggedBlock && currentChunk.getBlockState(mutable).getValue(BlockStateProperties.WATERLOGGED))) {
                            currentChunk.setBlockState(mutable, Blocks.STONE_BRICKS.defaultBlockState(), false);
                        }
                    }
                }
            }
            return blockInfoGlobal;
        }

        @Nonnull
        protected StructureProcessorType<?> getType() {
            return ERProcessors.WATERLOGGED_PROCESSOR;
        }
    }


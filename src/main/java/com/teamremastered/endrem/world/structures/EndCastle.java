package com.teamremastered.endrem.world.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.utils.ERUtils;
import com.teamremastered.endrem.world.structures.config.ERStructures;
import com.teamremastered.endrem.world.structures.utils.CustomMonsterSpawn;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.List;

import static net.minecraft.world.entity.MobCategory.MONSTER;

public class EndCastle extends StructureFeature<NoneFeatureConfiguration> {
    private static final List<CustomMonsterSpawn> MONSTER_SPAWN_LIST =  ImmutableList.of(
            new CustomMonsterSpawn(EntityType.PILLAGER, 30, 30, 35),
            new CustomMonsterSpawn(EntityType.VINDICATOR, 20, 25, 30),
            new CustomMonsterSpawn(EntityType.EVOKER, 20, 10, 15),
            new CustomMonsterSpawn(EntityType.ILLUSIONER, 5, 5, 10)
    );

    public EndCastle(Codec<NoneFeatureConfiguration> codec) {
        super(codec, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), EndCastle::generatePieces), EndCastle::afterPlace);
    }

    @Override
    public GenerationStep.@NotNull Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_DECORATION;
    }

    private static boolean isFeatureChunk(PieceGenerator.Context<NoneFeatureConfiguration> context) {
        return ERUtils.getChunkDistanceFromSpawn(context.chunkPos()) >= ERConfig.END_CASTLE_SPAWN_DISTANCE.getRaw();
    }

    public static void setupStructureSpawns(final StructureSpawnListGatherEvent event) {
        if(event.getStructure() == ERStructures.END_CASTLE.get()) {
            for (CustomMonsterSpawn monsterSpawn : MONSTER_SPAWN_LIST) {
                event.addEntitySpawn(MONSTER, monsterSpawn.getIndividualMobSpawnInfo());
            }
        }
    }

    private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context) {
        if (!isFeatureChunk(context)) {
            return;
        }

        Rotation rotation = Rotation.values()[context.random().nextInt(Rotation.values().length)];

        // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
        int x = (context.chunkPos().x << 4);
        int z = (context.chunkPos().z << 4);

        if (rotation == Rotation.CLOCKWISE_90) {
            x += 17;
            z += 43;
        }
        else if (rotation == Rotation.CLOCKWISE_180) {
            x -= 43;
            z += 17;
        }
        else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
            x -= 17;
            z -= 43;
        }
        else {
            x += 43;
            z -= 17;
        }
        // Finds the y value of the terrain at location.
        int surfaceY = context.chunkGenerator().getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
        BlockPos genPosition = new BlockPos(x, surfaceY, z);

        EndCastlePieces.addPieces(context.structureManager(), genPosition, rotation, builder, context.random());
    }

    private static void afterPlace(WorldGenLevel genLevel, StructureFeatureManager featureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int i = genLevel.getMinBuildHeight();
        BoundingBox boundingbox = piecesContainer.calculateBoundingBox();
        int j = boundingbox.minY();

        for(int k = boundingBox.minX(); k <= boundingBox.maxX(); ++k) {
            for(int l = boundingBox.minZ(); l <= boundingBox.maxZ(); ++l) {
                blockpos$mutableblockpos.set(k, j, l);
                if (!genLevel.isEmptyBlock(blockpos$mutableblockpos) && boundingbox.isInside(blockpos$mutableblockpos) && piecesContainer.isInsidePiece(blockpos$mutableblockpos)) {
                    for(int i1 = j - 1; i1 > i; --i1) {
                        blockpos$mutableblockpos.setY(i1);
                        if (!genLevel.isEmptyBlock(blockpos$mutableblockpos) && !genLevel.getBlockState(blockpos$mutableblockpos).getMaterial().isLiquid()) {
                            break;
                        }
                        double randomBlock = Math.random();
                        if (randomBlock <= 0.05) {
                            genLevel.setBlock(blockpos$mutableblockpos, Blocks.STONE.defaultBlockState(), 2);
                        } else if (randomBlock <= 0.1) {
                            genLevel.setBlock(blockpos$mutableblockpos, Blocks.COBBLESTONE.defaultBlockState(), 2);
                        } else if (randomBlock <= 0.2) {
                            genLevel.setBlock(blockpos$mutableblockpos, Blocks.ANDESITE.defaultBlockState(), 2);
                        } else if (randomBlock <= 0.3) {
                            genLevel.setBlock(blockpos$mutableblockpos, Blocks.STONE_BRICKS.defaultBlockState(), 2);
                        } else if (randomBlock <= 0.4) {
                            genLevel.setBlock(blockpos$mutableblockpos, Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 2);
                        } else if (randomBlock <= 0.5) {
                            genLevel.setBlock(blockpos$mutableblockpos, Blocks.ANDESITE.defaultBlockState(), 2);
                        } else {
                            genLevel.setBlock(blockpos$mutableblockpos, Blocks.POLISHED_ANDESITE.defaultBlockState(), 2);
                        }
                    }
                }
            }
        }

    }
}
package com.teamremastered.endrem.world.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.utils.ERUtils;
import com.teamremastered.endrem.world.structures.config.ERStructures;
import com.teamremastered.endrem.world.structures.utils.CustomMonsterSpawn;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EndCastle extends StructureFeature<NoneFeatureConfiguration> {
    private static final List<CustomMonsterSpawn> MONSTER_SPAWN_LIST =  ImmutableList.of(
            new CustomMonsterSpawn(EntityType.PILLAGER, 30, 30, 35),
            new CustomMonsterSpawn(EntityType.VINDICATOR, 20, 25, 30),
            new CustomMonsterSpawn(EntityType.EVOKER, 20, 10, 15),
            new CustomMonsterSpawn(EntityType.ILLUSIONER, 5, 5, 10)
    );

    public EndCastle(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public @Nonnull GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_DECORATION;
    }

    public static void setupStructureSpawns(final StructureSpawnListGatherEvent event) {
        if(event.getStructure() == ERStructures.END_CASTLE.get()) {
            for (CustomMonsterSpawn monsterSpawn : MONSTER_SPAWN_LIST) {
                event.addEntitySpawn(MobCategory.MONSTER, monsterSpawn.getIndividualMobSpawnInfo());
            }
        }
    }

    public static List<Biome.BiomeCategory> getValidBiomeCategories() {
        List<Biome.BiomeCategory> biomeCategories = new ArrayList<>();
        for (String biomeName : ERConfig.END_CASTLE_WHITELISTED_BIOME_CATEGORIES.getList()) {
            biomeCategories.add(Biome.BiomeCategory.byName(biomeName));
        }
        return biomeCategories;
    }

    @ParametersAreNonnullByDefault
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, WorldgenRandom chunkRandom, ChunkPos chunkPos1, Biome biome, ChunkPos chunkPos2, NoneFeatureConfiguration c, LevelHeightAccessor level) {
        return ERUtils.getChunkDistanceFromSpawn(chunkPos1) >= ERConfig.END_CASTLE_SPAWN_DISTANCE.getRaw();
    }

    @Override
    @MethodsReturnNonnullByDefault
    public @Nonnull StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return Start::new;
    }

    public static class Start extends StructureStart<NoneFeatureConfiguration> {
        public Start(StructureFeature<NoneFeatureConfiguration> structureIn, ChunkPos chunkPos, int referenceIn, long seedIn) {
            super(structureIn, chunkPos, referenceIn, seedIn);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void generatePieces(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, StructureManager manager, ChunkPos chunkPos, Biome biomeIn, NoneFeatureConfiguration config, LevelHeightAccessor levelHeightAccessor) {
            Rotation rotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];
            // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkPos.x << 4);
            int z = (chunkPos.z << 4);

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
            int surfaceY = chunkGenerator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor);
            BlockPos genPosition = new BlockPos(x, surfaceY, z);
            EndCastlePieces.start(manager, genPosition, rotation, this.pieces);
        }

        @ParametersAreNonnullByDefault
        public void placeInChunk(WorldGenLevel worldGenLevel, StructureFeatureManager featureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos) {
            super.placeInChunk(worldGenLevel, featureManager, chunkGenerator, random, boundingBox, chunkPos);
            BoundingBox boundingbox = this.getBoundingBox();
            int i = boundingbox.minY();

            for (int j = boundingBox.minX(); j <= boundingBox.maxX(); ++j) {
                for (int k = boundingBox.minZ(); k <= boundingBox.maxZ(); ++k) {
                    BlockPos blockpos = new BlockPos(j, i, k);
                    if (!worldGenLevel.isEmptyBlock(blockpos) && boundingbox.isInside(blockpos) && this.isInsidePiece(blockpos)) {
                        for (int l = i - 1; l > 1; --l) {
                            BlockPos blockpos1 = new BlockPos(j, l, k);
                            if (!worldGenLevel.isEmptyBlock(blockpos1) && !worldGenLevel.getBlockState(blockpos1).getMaterial().isLiquid()) {
                                break;
                            }
                            double randomBlock = Math.random();
                            if (randomBlock <= 0.05) {
                                worldGenLevel.setBlock(blockpos1, Blocks.STONE.defaultBlockState(), 2);
                            } else if (randomBlock <= 0.1) {
                                worldGenLevel.setBlock(blockpos1, Blocks.COBBLESTONE.defaultBlockState(), 2);
                            } else if (randomBlock <= 0.2) {
                                worldGenLevel.setBlock(blockpos1, Blocks.ANDESITE.defaultBlockState(), 2);
                            } else if (randomBlock <= 0.3) {
                                worldGenLevel.setBlock(blockpos1, Blocks.STONE_BRICKS.defaultBlockState(), 2);
                            } else if (randomBlock <= 0.4) {
                                worldGenLevel.setBlock(blockpos1, Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 2);
                            } else if (randomBlock <= 0.5) {
                                worldGenLevel.setBlock(blockpos1, Blocks.ANDESITE.defaultBlockState(), 2);
                            } else {
                                worldGenLevel.setBlock(blockpos1, Blocks.POLISHED_ANDESITE.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }

        }
    }
}
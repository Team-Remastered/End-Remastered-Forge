package com.teamremastered.endrem.world.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.world.structures.utils.CustomMonsterSpawn;
import com.teamremastered.endrem.world.structures.utils.StructureBase;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.WoodlandMansionFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.NoiseAffectingStructureStart;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

public class EndCastle extends StructureBase {
    public EndCastle(Codec<NoneFeatureConfiguration> codec) {
        super(codec,
                // To Set Minimum Distance
                ERConfig.END_CASTLE_SPAWN_DISTANCE,
                // List Of Monster Spawns
                ImmutableList.of(
                        new CustomMonsterSpawn(EntityType.PILLAGER, 30, 30, 35),
                        new CustomMonsterSpawn(EntityType.VINDICATOR, 20, 25, 30),
                        new CustomMonsterSpawn(EntityType.EVOKER, 20, 10, 15),
                        new CustomMonsterSpawn(EntityType.ILLUSIONER, 5, 5, 10)
                ),
                // Decoration Stage
                GenerationStep.Decoration.SURFACE_STRUCTURES
        );
    }

    public static List<Biome.BiomeCategory> getValidBiomeCategories() {
        return ImmutableList.of(Biome.BiomeCategory.PLAINS,
                Biome.BiomeCategory.JUNGLE,
                Biome.BiomeCategory.TAIGA,
                Biome.BiomeCategory.FOREST);
    }

    @Override
    @MethodsReturnNonnullByDefault
    public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
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
            int x = (chunkPos.x << 4) + 7;
            int z = (chunkPos.z << 4) + 7;

            // Finds the y value of the terrain at location.
            int surfaceY = chunkGenerator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor);
            BlockPos genPosition = new BlockPos(x, surfaceY, z);
            EndCastlePieces.start(manager, genPosition, rotation, this.pieces);
        }

        public void placeInChunk(WorldGenLevel worldGenLevel, StructureFeatureManager featureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos) {
            super.placeInChunk(worldGenLevel, featureManager, chunkGenerator, random, boundingBox, chunkPos);
            BoundingBox boundingbox = this.getBoundingBox();
            int i = boundingbox.minY();

            for(int j = boundingBox.minX(); j <= boundingBox.maxX(); ++j) {
                for(int k = boundingBox.minZ(); k <= boundingBox.maxZ(); ++k) {
                    BlockPos blockpos = new BlockPos(j, i, k);
                    if (!worldGenLevel.isEmptyBlock(blockpos) && boundingbox.isInside(blockpos) && this.isInsidePiece(blockpos)) {
                        for(int l = i - 1; l > 1; --l) {
                            BlockPos blockpos1 = new BlockPos(j, l, k);
                            if (!worldGenLevel.isEmptyBlock(blockpos1) && !worldGenLevel.getBlockState(blockpos1).getMaterial().isLiquid()) {
                                break;
                            }
                            double randomBlock = Math.random();
                            if (randomBlock <= 0.1) {
                                worldGenLevel.setBlock(blockpos1, Blocks.COBBLESTONE.defaultBlockState(), 2);
                            } else if (randomBlock <= 0.2) {
                                worldGenLevel.setBlock(blockpos1, Blocks.ANDESITE.defaultBlockState(), 2);
                            } else if (randomBlock <= 0.3) {
                                worldGenLevel.setBlock(blockpos1, Blocks.DEEPSLATE.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }

        }
    }
}
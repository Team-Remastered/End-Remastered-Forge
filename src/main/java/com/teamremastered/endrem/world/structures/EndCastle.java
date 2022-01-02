package com.teamremastered.endrem.world.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.world.structures.config.ERStructures;
import com.teamremastered.endrem.world.structures.utils.CustomMonsterSpawn;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EndCastle extends Structure<NoFeatureConfig> {
    private static final List<CustomMonsterSpawn> MONSTER_SPAWN_LIST =  ImmutableList.of(
            new CustomMonsterSpawn(EntityType.PILLAGER, 30, 30, 35),
            new CustomMonsterSpawn(EntityType.VINDICATOR, 20, 25, 30),
            new CustomMonsterSpawn(EntityType.EVOKER, 20, 10, 15),
            new CustomMonsterSpawn(EntityType.ILLUSIONER, 5, 5, 10)
    );

    public EndCastle(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public @Nonnull GenerationStage.Decoration step() {
        return GenerationStage.Decoration.UNDERGROUND_DECORATION;
    }

    public static void setupStructureSpawns(final StructureSpawnListGatherEvent event) {
        if(event.getStructure() == ERStructures.END_CASTLE.get()) {
            for (CustomMonsterSpawn monsterSpawn : MONSTER_SPAWN_LIST) {
                event.addEntitySpawn(EntityClassification.MONSTER, monsterSpawn.getIndividualMobSpawnInfo());
            }
        }
    }

    public static List<Biome.Category> getValidBiomeCategories() {
        List<Biome.Category> biomeCategories = new ArrayList<>();
        for (String biomeName : ERConfig.END_CASTLE_WHITELISTED_BIOME_CATEGORIES.getList()) {
            biomeCategories.add(Biome.Category.byName(biomeName));
        }
        return biomeCategories;
    }

    @Override
    public @Nonnull IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void generatePieces(DynamicRegistries registryAccess, ChunkGenerator chunkGenerator, TemplateManager manager, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            Rotation rotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];
            // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkX << 4);
            int z = (chunkZ << 4);

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
            int surfaceY = chunkGenerator.getBaseHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            BlockPos genPosition = new BlockPos(x, surfaceY, z);
            EndCastlePieces.start(manager, genPosition, rotation, this.pieces);
            this.calculateBoundingBox();
        }

        @ParametersAreNonnullByDefault
        public void placeInChunk(ISeedReader seedReader, StructureManager featureManager, ChunkGenerator chunkGenerator, Random random, MutableBoundingBox boundingBox, ChunkPos chunkPos) {
            super.placeInChunk(seedReader, featureManager, chunkGenerator, random, boundingBox, chunkPos);
            int i = this.boundingBox.y0;

            for (int j = boundingBox.x0; j <= boundingBox.x1; ++j) {
                for(int k = boundingBox.z0; k <= boundingBox.z1; ++k) {
                    BlockPos blockpos = new BlockPos(j, i, k);
                    if (!seedReader.isEmptyBlock(blockpos) && this.boundingBox.isInside(blockpos)) {
                        boolean flag = false;

                        for(StructurePiece structurepiece : this.pieces) {
                            if (structurepiece.getBoundingBox().isInside(blockpos)) {
                                flag = true;
                                break;
                            }
                        }

                        if (flag) {
                            for (int l = i - 1; l > 1; --l) {
                                BlockPos blockpos1 = new BlockPos(j, l, k);
                                if (!seedReader.isEmptyBlock(blockpos1) && !seedReader.getBlockState(blockpos1).getMaterial().isLiquid()) {
                                    break;
                                }
                                double randomBlock = Math.random();
                                if (randomBlock <= 0.05) {
                                    seedReader.setBlock(blockpos1, Blocks.STONE.defaultBlockState(), 2);
                                } else if (randomBlock <= 0.1) {
                                    seedReader.setBlock(blockpos1, Blocks.COBBLESTONE.defaultBlockState(), 2);
                                } else if (randomBlock <= 0.2) {
                                    seedReader.setBlock(blockpos1, Blocks.ANDESITE.defaultBlockState(), 2);
                                } else if (randomBlock <= 0.3) {
                                    seedReader.setBlock(blockpos1, Blocks.STONE_BRICKS.defaultBlockState(), 2);
                                } else if (randomBlock <= 0.4) {
                                    seedReader.setBlock(blockpos1, Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 2);
                                } else if (randomBlock <= 0.5) {
                                    seedReader.setBlock(blockpos1, Blocks.ANDESITE.defaultBlockState(), 2);
                                } else {
                                    seedReader.setBlock(blockpos1, Blocks.POLISHED_ANDESITE.defaultBlockState(), 2);
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
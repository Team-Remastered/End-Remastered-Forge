package com.teamremastered.endrem.world.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.utils.ERUtils;
import com.teamremastered.endrem.world.structures.config.ERStructures;
import com.teamremastered.endrem.world.structures.utils.CustomMonsterSpawn;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.NoiseAffectingStructureStart;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;


public class EndGate extends StructureFeature<NoneFeatureConfiguration> {
    private static final ResourceLocation START_POOL = new ResourceLocation(EndRemastered.MOD_ID, "end_gate/start_pool");
    private static final List<CustomMonsterSpawn> MONSTER_SPAWN_LIST =  ImmutableList.of(
            new CustomMonsterSpawn(EntityType.SKELETON, 30, 30, 35),
            new CustomMonsterSpawn(EntityType.ZOMBIE, 20, 25, 30),
            new CustomMonsterSpawn(EntityType.CAVE_SPIDER, 20, 25, 30),
            new CustomMonsterSpawn(EntityType.WITCH, 10, 10, 15)
    );

    public EndGate(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public @Nonnull GenerationStep.Decoration step() {
        return GenerationStep.Decoration.STRONGHOLDS;
    }

    public static void setupStructureSpawns(final StructureSpawnListGatherEvent event) {
        if(event.getStructure() == ERStructures.END_GATE.get()) {
            for (CustomMonsterSpawn monsterSpawn : MONSTER_SPAWN_LIST) {
                event.addEntitySpawn(MobCategory.MONSTER, monsterSpawn.getIndividualMobSpawnInfo());
            }
        }
    }

    @ParametersAreNonnullByDefault
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, WorldgenRandom chunkRandom, ChunkPos chunkPos1, Biome biome, ChunkPos chunkPos2, NoneFeatureConfiguration c, LevelHeightAccessor level) {
        return ERUtils.getChunkDistanceFromSpawn(chunkPos1) >= ERConfig.getData().END_GATE.spawnDistance;
    }

    @Override
    @ParametersAreNonnullByDefault
    public BlockPos getNearestGeneratedFeature(LevelReader level, StructureFeatureManager manager, BlockPos p_236388_3_, int radius, boolean skipExistingChunks, long seed, StructureFeatureConfiguration separationSettings) {
        /*
         * Not Even Remotely Close From Knowing Exactly How This Works :((
         *
         * This code was basically copy-pasted from Minecraft's and adapted so that locating
         * the structure would not return a position close to the portal
         * */

        int i = separationSettings.spacing();
        int j = p_236388_3_.getX() >> 4;
        int k = p_236388_3_.getZ() >> 4;
        int l = 0;

        for (WorldgenRandom worldgenrandom = new WorldgenRandom(); l <= radius; ++l) {
            for (int i1 = -l; i1 <= l; ++i1) {
                boolean flag = i1 == -l || i1 == l;

                for (int j1 = -l; j1 <= l; ++j1) {
                    boolean flag1 = j1 == -l || j1 == l;
                    if (flag || flag1) {
                        int k1 = j + i * i1;
                        int l1 = k + i * j1;
                        ChunkPos chunkpos = this.getPotentialFeatureChunk(separationSettings, seed, worldgenrandom, k1, l1);
                        ChunkAccess chunkAccess = level.getChunk(chunkpos.x, chunkpos.z, ChunkStatus.STRUCTURE_STARTS);
                        StructureStart<?> structurestart = manager.getStartForFeature(SectionPos.of(chunkAccess.getPos(), 0), this, chunkAccess);
                        if (structurestart != null && structurestart.isValid()) {
                            if (skipExistingChunks && structurestart.canBeReferenced()) {
                                structurestart.addReference();
                                return new BlockPos(structurestart.getPieces().get(((Start) structurestart).getLocatedRoom()).getBoundingBox().minX(),
                                        structurestart.getPieces().get(((Start) structurestart).getLocatedRoom()).getBoundingBox().minY(),
                                        structurestart.getPieces().get(((Start) structurestart).getLocatedRoom()).getBoundingBox().minZ());
                            }

                            if (!skipExistingChunks) {
                                return new BlockPos(structurestart.getPieces().get(((Start) structurestart).getLocatedRoom()).getBoundingBox().minX(),
                                        structurestart.getPieces().get(((Start) structurestart).getLocatedRoom()).getBoundingBox().minY(),
                                        structurestart.getPieces().get(((Start) structurestart).getLocatedRoom()).getBoundingBox().minZ());
                            }
                        }

                        if (l == 0) {
                            break;
                        }
                    }
                }

                if (l == 0) {
                    break;
                }
            }
        }

        return null;
    }

    @Override
    public @Nonnull StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return Start::new;
    }

    public static class Start extends NoiseAffectingStructureStart<NoneFeatureConfiguration> {
        public Start(StructureFeature<NoneFeatureConfiguration> structureIn, ChunkPos chunkPos, int referenceIn, long seedIn) {
            super(structureIn, chunkPos, referenceIn, seedIn);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void generatePieces(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, StructureManager manager, ChunkPos chunkPos, Biome biomeIn, NoneFeatureConfiguration config, LevelHeightAccessor levelHeightAccessor) {
            BlockPos genPosition = new BlockPos(chunkPos.x << 4, ERConfig.getData().END_GATE.height, chunkPos.z << 4);

            JigsawPlacement.addPieces(
                    registryAccess,
                    new JigsawConfiguration(() -> registryAccess.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(START_POOL),
                            ERConfig.getData().END_GATE.size),
                    PoolElementStructurePiece::new,
                    chunkGenerator,
                    manager,
                    genPosition,
                    this,
                    this.random,
                    false,
                    false,
                    levelHeightAccessor
            );
            this.getBoundingBox();
        }

        public int getLocatedRoom() {
            return Math.min(16, this.pieces.size()) - 1;
        }
    }
}

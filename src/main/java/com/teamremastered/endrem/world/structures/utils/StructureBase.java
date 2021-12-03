package com.teamremastered.endrem.world.structures.utils;

import com.mojang.serialization.Codec;
import com.teamremastered.endrem.config.ERConfigGenericEntry;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public abstract class StructureBase extends StructureFeature<NoneFeatureConfiguration> {

    private final int MIN_SPAWN_DISTANCE;
    private final List<MobSpawnSettings.SpawnerData> MONSTERS_SPAWN_LIST;
    private final GenerationStep.Decoration DECORATION_STAGE;

    protected StructureBase(Codec<NoneFeatureConfiguration> codec, int minSpawnDistanceIn, List<CustomMonsterSpawn> customMonsterSpawnListIn, GenerationStep.Decoration generationStageIn) {
        super(codec);
        this.MIN_SPAWN_DISTANCE = minSpawnDistanceIn;
        this.MONSTERS_SPAWN_LIST = CustomMonsterSpawn.getMobSpawnInfoList(customMonsterSpawnListIn);
        this.DECORATION_STAGE = generationStageIn;
    }

    protected StructureBase(Codec<NoneFeatureConfiguration> codec, ERConfigGenericEntry<Integer> minSpawnDistanceIn, List<CustomMonsterSpawn> customMonsterSpawnListIn, GenerationStep.Decoration generationStageIn) {
        this(codec, minSpawnDistanceIn.getRaw(), customMonsterSpawnListIn, generationStageIn);
    }

    @Override
    public GenerationStep.Decoration step()
    {
        return this.DECORATION_STAGE;
    }

    public int getChunkDistanceFromSpawn(int chunkX, int chunkZ) {
        return (int) Math.sqrt(chunkX * chunkX + chunkZ * chunkZ);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, WorldgenRandom chunkRandom, ChunkPos chunkPos1, Biome biome, ChunkPos chunkPos2, NoneFeatureConfiguration c, LevelHeightAccessor level) {
        return getChunkDistanceFromSpawn(chunkPos1.x, chunkPos1.z) >= this.MIN_SPAWN_DISTANCE;
    }

    @Override
    public List<MobSpawnSettings.SpawnerData> getDefaultSpawnList() {
        return MONSTERS_SPAWN_LIST;
    }
}

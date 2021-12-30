package com.teamremastered.endrem.world.structures.utils;

import com.mojang.serialization.Codec;
import com.teamremastered.endrem.config.ERConfigGenericEntry;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public abstract class StructureBase extends Structure<NoFeatureConfig> {

    private final int MIN_SPAWN_DISTANCE;
    private final List<MobSpawnInfo.Spawners> MONSTERS_SPAWN_LIST;
    private final GenerationStage.Decoration DECORATION_STAGE;

    protected StructureBase(Codec<NoFeatureConfig> codec, int minSpawnDistanceIn, List<CustomMonsterSpawn> customMonsterSpawnListIn, GenerationStage.Decoration generationStageIn) {
        super(codec);
        this.MIN_SPAWN_DISTANCE = minSpawnDistanceIn;
        this.MONSTERS_SPAWN_LIST = CustomMonsterSpawn.getMobSpawnInfoList(customMonsterSpawnListIn);
        this.DECORATION_STAGE = generationStageIn;
    }

    protected StructureBase(Codec<NoFeatureConfig> codec, ERConfigGenericEntry<Integer> minSpawnDistanceIn, List<CustomMonsterSpawn> customMonsterSpawnListIn, GenerationStage.Decoration generationStageIn) {
        this(codec, minSpawnDistanceIn.getRaw(), customMonsterSpawnListIn, generationStageIn);
    }

    @Override
    public GenerationStage.Decoration step()
    {
        return this.DECORATION_STAGE;
    }

    public int getChunkDistanceFromSpawn(int chunkX, int chunkZ) {
        return (int) Math.sqrt(chunkX * chunkX + chunkZ * chunkZ);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig noFeatureConfig) {
        return getChunkDistanceFromSpawn(chunkX, chunkZ) >= this.MIN_SPAWN_DISTANCE;
    }

    @Override
    public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
        return MONSTERS_SPAWN_LIST;
    }
}

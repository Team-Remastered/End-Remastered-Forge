package com.teamremastered.endrem.world.structures.utils;

import com.google.common.collect.ImmutableList;
import com.teamremastered.endrem.config.ERConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.List;

public class CustomMonsterSpawn {
    private final int min;
    private final int max;
    private final double factor = switch (ERConfig.MONSTER_DIFFICULTY.get()) {
        case "peaceful" -> 0;
        case "easy" -> 0.5;
        case "hard" -> 2;
        default -> 1;
    };
    private final int weight;

    public EntityType<?> monsterEntity;

    public CustomMonsterSpawn(EntityType<?> monsterEntityIn, int weightIn, int minIn, int maxIn) {
        this.monsterEntity = monsterEntityIn;
        this.min = minIn;
        this.max = maxIn;
        this.weight = weightIn;
    }

    public MobSpawnSettings.SpawnerData getIndividualMobSpawnInfo() {
        return new MobSpawnSettings.SpawnerData(monsterEntity, this.weight, (int) (this.min * this.factor), (int) (this.max * this.factor));
    }

    public static List<MobSpawnSettings.SpawnerData> getMobSpawnInfoList(List<CustomMonsterSpawn> monsterSpawnList) {
        ImmutableList.Builder<MobSpawnSettings.SpawnerData> spawnersListBuilder = ImmutableList.builder();
        for (CustomMonsterSpawn spawn : monsterSpawnList) {
            spawnersListBuilder.add(spawn.getIndividualMobSpawnInfo());
        }
        return spawnersListBuilder.build();
    }
}

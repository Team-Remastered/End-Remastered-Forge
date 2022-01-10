package com.teamremastered.endrem.world.structures.utils;

import com.teamremastered.endrem.config.ERConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.MobSpawnInfo;

public class CustomMonsterSpawn {
    private final int min;
    private final int max;
    private final int weight;
    public EntityType<?> monsterEntity;

    public CustomMonsterSpawn(EntityType<?> monsterEntityIn, int weightIn, int minIn, int maxIn) {
        this.monsterEntity = monsterEntityIn;
        this.min = minIn;
        this.max = maxIn;
        this.weight = weightIn;
    }

    private double Factor() {
        switch (ERConfig.MONSTER_DIFFICULTY.getRaw()) {
            case "peaceful":
                return 0;
            case "easy":
                return 0.5;
            case "hard":
                return 2;
            case "hardcore":
                return 3;
            default:
                return 1;
        }
    }

        public MobSpawnInfo.Spawners getIndividualMobSpawnInfo () {
            return new MobSpawnInfo.Spawners(monsterEntity, this.weight, (int) (this.min * this.Factor()), (int) (this.max * this.Factor()));
        }
}

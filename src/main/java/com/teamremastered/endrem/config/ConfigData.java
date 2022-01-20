package com.teamremastered.endrem.config;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.ArrayList;
import java.util.List;

public class ConfigData {
    @SerializedName("End Gate")
    public VariableSizeStructure END_GATE = new VariableSizeStructure(
            List.of("plains", "jungle", "taiga", "forest", "plains", "extreme_hills", "mesa", "savanna", "icy",
                    " desert", "swamp", "mushroom", "none"),
            List.of("minecraft:ocean", "minecraft:deep_ocean"),
            85,
            130,
            15,
            false,
            20
    );

    @SerializedName("End Castle")
    public Structure END_CASTLE = new Structure(
            List.of("minecraft:bamboo_jungle_hills", "minecraft:birch_forest_hills", "minecraft:dark_forest_hills",
                    "minecraft:giant_spruce_taiga_hills", "minecraft:giant_tree_taiga_hills",
                    "minecraft:gravelly_mountains", "minecraft:jungle_edge", "minecraft:jungle_hills",
                    "minecraft:modified_gravelly_mountains", "minecraft:modified_jungle_edge",
                    "minecraft:mountain_edge", "minecraft:mountains", "minecraft:snowy_mountains",
                    "minecraft:snowy_taiga_hills", "minecraft:snowy_taiga_mountains", "minecraft:taiga_hills",
                    "minecraft:taiga_mountains", "minecraft:tall_birch_hills", "minecraft:wooded_hills",
                    "minecraft:wooded_mountains", "minecraft:savanna_plateau", "minecraft:shattered_savanna_plateau"),
            List.of("jungle", "taiga", "forest", "none", "plains", "icy"),
            100,
            188,
            0,
            true
    );

    @SerializedName("Ancient Witch Hut")
    public Structure ANCIENT_WITCH_HUT = new Structure(
            List.of(),
            List.of("swamp"),
            25,
            0,
            -3,
            false
    );

    @SerializedName("Whitelisted Dimensions")
    public List<String> WHITELISTED_DIMENSIONS = List.of("minecraft:overworld");

    @SerializedName("Difficulty of Monsters Inside Structures [peaceful, easy, normal, hard or hardcore]")
    public String MONSTER_DIFFICULTY = "normal";

    @SerializedName("End Remastered Eyes")
    public EREye ER_EYES = new EREye(
            0,
            List.of("endrem:end_gate")
    );

    @SerializedName("Vanilla Eyes Can Be Used")
    public boolean ENDER_EYES_ENABLED = false;

    @SerializedName("Vanilla Strongholds Are Enabled")
    public boolean STRONGHOLDS_ENABLED = false;

    @SerializedName("End Remastered Map")
    public ERMap ER_MAP = new ERMap(
            List.of("endrem:end_castle"),
            30,
            40,
            12
    );

    @SerializedName("End Crystal Gear")
    public EndCrystalGear END_CRYSTAL_GEAR = new EndCrystalGear(
            new EndCrystalGear.Armor(
                    33,
                    1,
                    2,
                    0.1f,
                    true,
                    20
            ),

            new EndCrystalGear.Tools(
                    1325,
                    10,
                    3
            )
    );

    public static class EndCrystalGear {

        @SerializedName("End Crystal Armor")
        public Armor ARMOR;

        @SerializedName("End Crystal Tools")
        public Tools TOOLS;

        private EndCrystalGear(Armor armorIn, Tools toolsIn) {
            ARMOR = armorIn;
            TOOLS = toolsIn;
        }

        public static class Armor {
            @SerializedName("Durability Factor")
            public float durabilityFactor;

            @SerializedName("Defense Factor")
            public float defenseFactor;

            @SerializedName("Toughness")
            public float toughness;

            @SerializedName("Knockback Resistance")
            public float knockbackResistance;

            @SerializedName("Neutralizes Piglins")
            public boolean neutralizesPiglins;

            @SerializedName("Regen Duration (Ticks)")
            public int regenDuration;

            private Armor(float durabilityFactorIn, float defenseFactorIn, float toughnessIn,
                          float knockbackResistanceIn, boolean neutralizesPiglinsIn, int regenDurationIn) {
                this.durabilityFactor = durabilityFactorIn;
                this.defenseFactor = defenseFactorIn;
                this.toughness = toughnessIn;
                this.knockbackResistance = knockbackResistanceIn;
                this.neutralizesPiglins = neutralizesPiglinsIn;
                this.regenDuration = regenDurationIn;
            }
        }

        public static class Tools {
            @SerializedName("Durability")
            public int durability;

            @SerializedName("Speed")
            public float speed;

            @SerializedName("Attack Damage Bonus")
            public float damageBonus;

            private Tools(int durabilityIn, float speedIn, float damageBonusIn) {
                this.durability = durabilityIn;
                this.speed = speedIn;
                this.damageBonus = damageBonusIn;
            }
        }
    }

    public static class ERMap {
        @SerializedName("Located Structures")
        public List<String> structureList;

        @SerializedName("Minimum Price in Emeralds")
        public int minPrice;

        @SerializedName("Maximum Price in Emeralds")
        public int maxPrice;

        @SerializedName("XP Received on Purchase")
        public int xpGiven;

        private ERMap(List<String> structureListIn, int minPriceIn, int maxPriceIn, int xpGivenIn) {
            this.structureList = structureListIn;
            this.minPrice = minPriceIn;
            this.maxPrice = maxPriceIn;
            this.xpGiven = xpGivenIn;
        }
    }

    public static class EREye {
        @SerializedName("Break Probability (decimal form)")
        public float breakProbability;

        @SerializedName("Located Structures")
        public List<String> structureList;

        private EREye(float breakProbabilityIn, List<String> structureListIn) {
            this.breakProbability = breakProbabilityIn;
            this.structureList = structureListIn;
        }
    }

    public static class Structure {
        @SerializedName("Is Enabled")
        public boolean enabled;

        @SerializedName("Whitelisted Biome Categories")
        public List<String> whitelistedBiomeCategories;

        @SerializedName("Blacklisted Biomes")
        public List<String> blacklistedBiomes;

        @SerializedName("Average Distance Between Structures")
        public int averageDistance;

        @SerializedName("Minimum Spawn Distance")
        public int spawnDistance;

        @SerializedName("Generation Height")
        public int height;

        @SerializedName("Terraforming Enabled")
        public boolean terraforming;

        private Structure(List<String> whitelistedBiomeCategoriesIn, List<String> blacklistedBiomesIn,
                          int averageDistanceIn, int spawnDistanceIn, int heightIn, boolean terraformingIn) {
            this.enabled = true;
            this.whitelistedBiomeCategories = whitelistedBiomeCategoriesIn;
            this.blacklistedBiomes = blacklistedBiomesIn;
            this.averageDistance = averageDistanceIn;
            this.spawnDistance = spawnDistanceIn;
            this.height = heightIn;
            this.terraforming = terraformingIn;
        }

        private List<Biome.BiomeCategory> getProcessedBiomeCategories() {
            List<Biome.BiomeCategory> biomeCategories = new ArrayList<>();
            for (String biomeName : this.whitelistedBiomeCategories) {
                biomeCategories.add(Biome.BiomeCategory.byName(biomeName));
            }
            return biomeCategories;
        }

        public boolean shouldGenerate(BiomeLoadingEvent event) {
            return this.enabled
                    && this.getProcessedBiomeCategories().contains(event.getCategory())
                    && !this.blacklistedBiomes.contains(event.getName().toString());
        }
    }

    public static class VariableSizeStructure extends Structure {
        @SerializedName("Size")
        public int size;

        private VariableSizeStructure(List<String> whitelistedBiomeCategoriesIn, List<String> blacklistedBiomesIn,
                                      int averageDistanceIn, int spawnDistanceIn, int heightIn, boolean terraformingIn,
                                      int sizeIn) {
            super(whitelistedBiomeCategoriesIn, blacklistedBiomesIn, averageDistanceIn, spawnDistanceIn, heightIn,
                    terraformingIn);
            this.size = sizeIn;
        }
    }
}

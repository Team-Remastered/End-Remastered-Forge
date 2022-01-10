package com.teamremastered.endrem.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.teamremastered.endrem.EndRemastered;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

public class ERConfig {
    private static final ForgeConfigSpec.Builder CONFIG = new ForgeConfigSpec.Builder();
    private static ForgeConfigSpec COMMON_CONFIG;

    // ======   Vanilla Modifications   ======
    public static ERConfigGenericEntry<Boolean> STRONGHOLDS_ENABLED = new ERConfigGenericEntry<>(
            "strongholds",
            "Toggle Vanilla Minecraft Strongholds",
            false);

    public static ERConfigGenericEntry<Boolean> USE_ENDER_EYES_ENABLED = new ERConfigGenericEntry<>(
            "ender_eyes",
            "Toggle Ender Eyes Actions (Throwing and Placing in Portal Frames)",
            false);


    // ======   End Remastered Configuration   ======
    public static ERConfigGenericEntry<String> MONSTER_DIFFICULTY = new ERConfigGenericEntry<>(
            "monster_difficulty",
            "Sets the quantity of mobs in End Remastered Structures: \"peaceful\", \"easy\", \"normal\", \"hard\" or \"hardcore\"",
            "normal");

    public static ERConfigListEntry WHITELISTED_DIMENSIONS = new ERConfigListEntry(
            "whitelisted_dimensions",
            "Comma-separated list of whitelisted dimensions for End Remastered Structures",
            "[minecraft:overworld]");

    public static ERConfigListEntry END_GATE_BLACKLISTED_BIOMES = new ERConfigListEntry(
            "end_gate_blacklisted_biomes",
            "Comma-separated list of blacklisted biomes for the End Gate",
            "[minecraft:ocean, minecraft:deep_ocean]");

    public static ERConfigListEntry END_GATE_WHITELISTED_BIOME_CATEGORIES = new ERConfigListEntry(
            "end_gate_whitelisted_biome_categories",
            "Comma-separated list of whitelisted biome categories for the End Gate",
            "[plains, jungle, taiga, forest, plains, extreme_hills, mesa, savanna, icy, desert, swamp, mushroom, none]");

    public static ERConfigListEntry END_CASTLE_BLACKLISTED_BIOMES = new ERConfigListEntry(
            "end_castle_blacklisted_biomes",
            "Comma-separated list of blacklisted biomes for the End Castle",
            "[minecraft:bamboo_jungle_hills, minecraft:birch_forest_hills, minecraft:dark_forest_hills, minecraft:giant_spruce_taiga_hills, minecraft:giant_tree_taiga_hills, minecraft:gravelly_mountains, minecraft:jungle_edge, minecraft:jungle_hills, minecraft:modified_gravelly_mountains, minecraft:modified_jungle_edge, minecraft:mountain_edge, minecraft:mountains, minecraft:snowy_mountains, minecraft:snowy_taiga_hills, minecraft:snowy_taiga_mountains, minecraft:taiga_hills, minecraft:taiga_mountains, minecraft:tall_birch_hills, minecraft:wooded_hills, minecraft:wooded_mountains, minecraft:savanna_plateau, minecraft:shattered_savanna_plateau]");

    public static ERConfigListEntry END_CASTLE_WHITELISTED_BIOME_CATEGORIES = new ERConfigListEntry(
            "end_castle_whitelisted_biome_categories",
            "Comma-separated list of whitelisted biome categories for the End Castle",
            "[jungle, taiga, forest, none, plains, icy]");

    public static ERConfigListEntry EYE_STRUCTURE_LIST = new ERConfigListEntry(
            "eye_structure_list",
            "Comma-separated list of structures to locate with End Remastered eyes",
            "[endrem:end_gate]");

    public static ERConfigGenericEntry<Integer> EYE_BREAK_CHANCE = new ERConfigGenericEntry<>(
            "eye_break_chance",
            "Percentage chance of eyes breaking when thrown",
            0);

    public static ERConfigListEntry MAP_STRUCTURE_LIST = new ERConfigListEntry(
            "map_structure_list",
            "Comma-separated list of structures to locate with the End Remastered map",
            "[endrem:end_castle]");

    public static ERConfigListEntry MAP_TRADE_VALUES = new ERConfigListEntry(
            "map_trade_values",
            "Values for the End Remastered map trade: [minPrice, maxPrice, expGiven]",
            "30, 40, 12]");

    public static ERConfigListEntry END_CRYSTAL_ARMOR_STATS = new ERConfigListEntry(
            "end_crystal_armor_stats",
            "Stats for End Remastered's End Crystal armor: [durability factor, defense factor, toughness, knockback resistance, makes piglins neutrals, regen duration (in ticks)]",
            "[33, 1, 2, 0.1, true, 20]");

    public static ERConfigListEntry END_CRYSTAL_TOOLS_STATS = new ERConfigListEntry(
            "end_crystal_tools_stats",
            "Stats for End Remastered's End Crystal tools: [durability, speed, damage bonus]",
            "[1325, 10, 3]");

    public static ERConfigGenericEntry<Integer> END_GATE_DISTANCE = new ERConfigGenericEntry<>(
            "end_gate_distance",
            "Average distance in chunks between End Gates (Set to 0 to disable)",
            85
    );

    public static ERConfigGenericEntry<Integer> END_GATE_HEIGHT = new ERConfigGenericEntry<>(
            "end_gate_height",
            "Y-Coordinate of the starting piece of the End Gate",
            15
    );

    public static ERConfigGenericEntry<Integer> END_GATE_SPAWN_DISTANCE = new ERConfigGenericEntry<>(
            "end_gate_spawn_distance",
            "Minimum distance in chunks between End Gates and the spawn",
            130
    );

    public static ERConfigGenericEntry<Integer> END_GATE_SIZE = new ERConfigGenericEntry<>(
            "end_gate_size",
            "Number of pieces generated in End Gates",
            20
    );


    public static ERConfigGenericEntry<Integer> END_CASTLE_DISTANCE = new ERConfigGenericEntry<>(
            "end_castle_distance",
            "Average distance in chunks between End Castles (Set to 0 to disable)",
            100
    );

    public static ERConfigGenericEntry<Integer> END_CASTLE_SPAWN_DISTANCE = new ERConfigGenericEntry<>(
            "end_castle_spawn_distance",
            "Minimum distance in chunks between End Castles and the spawn",
            188
    );

    public static ERConfigGenericEntry<Boolean> END_CASTLE_TERRAFORMING = new ERConfigGenericEntry<>(
            "end_castle_terraforming",
            "Toggle Whether surrounding land will be modified to conform to the bottom of the structure. \n If set to false, the bottom of the structure will be modified to conform to the surrounding land",
            true);

    public static ERConfigGenericEntry<Integer> ANCIENT_WITCH_HUT_DISTANCE = new ERConfigGenericEntry<>(
            "ancient_witch_hut_distance",
            "Average distance in chunks between Ancient Witch Huts (Set to 0 to disable)",
            25
    );

    static {
        init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG, EndRemastered.CONFIG_FILE);
    }

    private static void init() {
        CONFIG.push(EndRemastered.MOD_ID);

        // Add all the custom config values to the config file
        for (ERConfigGenericEntry<?> ccv : ERConfigGenericEntry.erConfigGenericEntries) {
            ccv.setup(CONFIG);
        }

        CONFIG.pop();
        COMMON_CONFIG = CONFIG.build();
    }

    public static void load() {
        final CommentedFileConfig configData = CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve(EndRemastered.CONFIG_FILE))
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        COMMON_CONFIG.setConfig(configData);
    }
}
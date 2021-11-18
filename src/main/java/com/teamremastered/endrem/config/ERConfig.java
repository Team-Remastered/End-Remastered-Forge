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
            "Sets the quantity of mobs in End Remastered Structures: \"peaceful\", \"easy\", \"normal\" or \"hard\"",
            "normal");

    public static ERConfigList WHITELISTED_DIMENSIONS = new ERConfigList(
            "whitelisted_dimensions",
            "Comma-separated list of whitelisted dimensions for End Remastered Structures",
            "[minecraft:overwold]");

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
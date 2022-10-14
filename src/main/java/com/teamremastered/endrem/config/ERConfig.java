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

    public static ERConfigGenericEntry<Boolean> USE_ENDER_EYES_ENABLED = new ERConfigGenericEntry<>(
            "ender_eyes",
            "Toggle Ender Eyes Actions (Throwing and Placing in Portal Frames)",
            false);

    // ======   End Remastered Configuration   ======

    public static ERConfigGenericEntry<Integer> EYE_BREAK_CHANCE = new ERConfigGenericEntry<>(
            "eye_break_chance",
            "Percentage chance of eyes breaking when thrown",
            10);

    public static ERConfigGenericEntry<Boolean> IS_CRYPTIC_EYE_OBTAINABLE = new ERConfigGenericEntry<>(
            "is_cryptic_eye_obtainable",
            "Determine if it's possible to obtain the cryptic eye when enchanting",
            true);

    public static ERConfigGenericEntry<Boolean> IS_EVIL_EYE_OBTAINABLE = new ERConfigGenericEntry<>(
            "is_evil_eye_obtainable",
            "Determine if it's possible to obtain the evil eye when trading with a Cleric",
            true);

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
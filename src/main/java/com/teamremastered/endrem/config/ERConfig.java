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

    public static ERConfigGenericEntry<Boolean> THROW_ENDER_EYE = new ERConfigGenericEntry<>(
            "throw_ender_eye",
            "Toggle Ender Eyes Actions (Throwing to Locate The Stronghold)",
            false);

    public static ERConfigGenericEntry<Boolean> USE_ENDER_EYE = new ERConfigGenericEntry<>(
            "use_ender_eye",
            "Toggle Ender Eyes Actions (Placing in Portal Frames)",
            false);

    public static ERConfigGenericEntry<Boolean> FRAME_HAS_EYE = new ERConfigGenericEntry<>(
            "frame_has_eye",
            "Determines whether or not vanilla eyes can naturally generate within the frames of the portal",
            false);

    // ======   End Remastered Configuration   ======

    public static ERConfigGenericEntry<Integer> EYE_BREAK_CHANCE = new ERConfigGenericEntry<>(
            "eye_break_chance",
            "Determines the percentage chance of eyes breaking when thrown",
            10);

    public static ERConfigGenericEntry<Boolean> CAN_REMOVE_EYE = new ERConfigGenericEntry<>(
            "can_remove_eye",
            "Determines whether or not players can remove ender eyes from the portal frames",
            false);

    public static ERConfigGenericEntry<Boolean> IS_CRYPTIC_EYE_OBTAINABLE = new ERConfigGenericEntry<>(
            "is_cryptic_eye_obtainable",
            "Determines if it's possible to obtain the cryptic eye when enchanting",
            true);

    public static ERConfigGenericEntry<Boolean> IS_EVIL_EYE_OBTAINABLE = new ERConfigGenericEntry<>(
            "is_evil_eye_obtainable",
            "Determines if it's possible to obtain the evil eye when trading with a Cleric",
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
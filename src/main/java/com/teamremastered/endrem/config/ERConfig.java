package com.teamremastered.endrem.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import com.teamremastered.endrem.EndRemastered;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.util.ArrayList;

public class ERConfig {
    private static final ForgeConfigSpec.Builder CONFIG = new ForgeConfigSpec.Builder();
    private static ForgeConfigSpec COMMON_CONFIG;

    // ======   Vanilla Modifications   ======
    public static CustomConfigValue<Boolean> STRONGHOLDS_ENABLED = new CustomConfigValue<>(
            "strongholds",
            "Toggle Vanilla Minecraft Strongholds",
            false);

    public static CustomConfigValue<Boolean> USE_ENDER_EYES_ENABLED = new CustomConfigValue<>(
            "ender_eyes",
            "Toggle Ender Eyes Actions (Throwing and Placing in Portal Frames)",
            false);


    // ======   End Remastered Configuration   ======
    public static CustomConfigValue<String> MONSTER_DIFFICULTY = new CustomConfigValue<>(
            "monster_difficulty",
            "Sets the quantity of mobs in End Remastered Structures: \"peaceful\", \"easy\", \"normal\" or \"hard\"",
            "normal");

    public static CustomListConfigValue WHITELISTED_DIMENSIONS = new CustomListConfigValue(
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
        for (CustomConfigValue<?> ccv : CustomConfigValue.AllCustomConfigValues) {
            ccv.setup();
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

    private static class CustomConfigValue<T> {
        protected static ArrayList<CustomConfigValue<?>> AllCustomConfigValues = new ArrayList<>();
        protected final String ID;
        protected final String COMMENT;
        protected final T DEFAULT_VALUE;

        protected ForgeConfigSpec.ConfigValue<T> CONFIG_VALUE;

        public CustomConfigValue(String id, String comment, T default_value) {
            this.ID = id;
            this.COMMENT = comment;
            this.DEFAULT_VALUE = default_value;
            AllCustomConfigValues.add(this);
        }

        protected void setup() {
            this.CONFIG_VALUE = CONFIG.comment(this.COMMENT).define(this.ID, this.DEFAULT_VALUE);
        }

        public T get() {
            return this.CONFIG_VALUE.get();
        }

        public void set(T value) {
            this.CONFIG_VALUE.set(value);
        }
    }

    private static class CustomListConfigValue extends CustomConfigValue<String> {
        public CustomListConfigValue(String id, String comment, ArrayList<String> default_value) {
            super(id, comment, getStringFromList(default_value));
        }

        public CustomListConfigValue(String id, String comment, String default_value) {
            super(id, comment, default_value);
        }

        public ArrayList<String> getList() {
            return getListFromString(this.get());
        }

        public void set(ArrayList<String> value) {
            this.set(getStringFromList(value));
        }

        private static String getStringFromList(ArrayList<String> lst) {
            return "[" + String.join(", ", lst) + "]";
        }

        private static ArrayList<String> getListFromString(String str) {
            if (str.length() < 2 || str.charAt(0) != '[' || str.charAt(str.length() - 1) != ']') {
                EndRemastered.LOGGER.error(String.format("Invalid value for list: %s", str));
            }
            return Lists.newArrayList(str.substring(1, str.length() - 1).split(",\\s*"));
        }
    }
}
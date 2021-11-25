package com.teamremastered.endrem.world.structures.config;

import com.teamremastered.endrem.EndRemastered;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;


public class ERConfiguredStructures {

    /* End Gate */
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_END_GATE = ERStructures.END_GATE.get().configured(FeatureConfiguration.NONE);
    /* Witch Hut */
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_ANCIENT_WITCH_HUT = ERStructures.ANCIENT_WITCH_HUT.get().configured(FeatureConfiguration.NONE);
    /* End Castle */
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_END_CASTLE = ERStructures.END_CASTLE.get().configured(FeatureConfiguration.NONE);

    public static void registerConfiguredStructures() {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        /* End Gate */
        Registry.register(registry, new ResourceLocation(EndRemastered.MOD_ID, "configured_end_gate"), CONFIGURED_END_GATE);
        /* Witch Hut */
        Registry.register(registry, new ResourceLocation(EndRemastered.MOD_ID, "configured_ancient_witch_hut"), CONFIGURED_ANCIENT_WITCH_HUT);
        /* End Castle */
        Registry.register(registry, new ResourceLocation(EndRemastered.MOD_ID, "configured_end_castle"), CONFIGURED_END_CASTLE);

        /* End Gate */
        FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(ERStructures.END_GATE.get(), CONFIGURED_END_GATE);
        /* Witch Hut */
        FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(ERStructures.ANCIENT_WITCH_HUT.get(), CONFIGURED_ANCIENT_WITCH_HUT);
        /* End Castle */
        FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(ERStructures.END_CASTLE.get(), CONFIGURED_END_CASTLE);

    }
}

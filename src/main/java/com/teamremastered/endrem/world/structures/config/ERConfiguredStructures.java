package com.teamremastered.endrem.world.structures.config;

import com.teamremastered.endrem.EndRemastered;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;


public class ERConfiguredStructures {

    /* End Gate */
    public static StructureFeature<?, ?> CONFIGURED_END_GATE = ERStructures.END_GATE.get().configured(IFeatureConfig.NONE);
    /* Witch Hut */
    public static StructureFeature<?, ?> CONFIGURED_ANCIENT_WITCH_HUT = ERStructures.ANCIENT_WITCH_HUT.get().configured(IFeatureConfig.NONE);
    /* End Castle */
    public static StructureFeature<?, ?> CONFIGURED_END_CASTLE = ERStructures.END_CASTLE.get().configured(IFeatureConfig.NONE);

    public static void registerConfiguredStructures() {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        /* End Gate */
        Registry.register(registry, new ResourceLocation(EndRemastered.MOD_ID, "configured_end_gate"), CONFIGURED_END_GATE);
        /* Witch Hut */
        Registry.register(registry, new ResourceLocation(EndRemastered.MOD_ID, "configured_ancient_witch_hut"), CONFIGURED_ANCIENT_WITCH_HUT);
        /* End Castle */
        Registry.register(registry, new ResourceLocation(EndRemastered.MOD_ID, "configured_end_castle"), CONFIGURED_END_CASTLE);

        /* End Gate */
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ERStructures.END_GATE.get(), CONFIGURED_END_GATE);
        /* Witch Hut */
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ERStructures.ANCIENT_WITCH_HUT.get(), CONFIGURED_ANCIENT_WITCH_HUT);
        /* End Castle */
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ERStructures.END_CASTLE.get(), CONFIGURED_END_CASTLE);

    }
}

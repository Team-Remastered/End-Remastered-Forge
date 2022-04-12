package com.teamremastered.endrem.utils;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

public interface MultiLocator {

    TagKey<ConfiguredStructureFeature<?, ?>> ENDREM_EYES_LOCATED = create("endrem_eyes_located");

    private static TagKey<ConfiguredStructureFeature<?, ?>> create(String id) {
        return TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(id));
    }
}

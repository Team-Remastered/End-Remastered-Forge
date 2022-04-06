package com.teamremastered.endrem.utils;

import com.teamremastered.endrem.EndRemastered;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.function.Supplier;

public class MultiLocator {
    private final Supplier<ArrayList<String>> STRUCTURE_LIST_SUPPLIER;

    public MultiLocator(Supplier<ArrayList<String>> structureListSupplier) {
        this.STRUCTURE_LIST_SUPPLIER = structureListSupplier;
    }

    public ArrayList<String> getStructureList() {
        return this.STRUCTURE_LIST_SUPPLIER.get();
    }

    public BlockPos getNearestPosition(ServerLevel serverLevel, BlockPos playerPos) {
        // Temporary values
        int shortestStructureDistance = -1;
        BlockPos nearestStructurePos = null;

        for (String structureID : this.getStructureList()) {
            // Verify if structure mod is loaded
            String structureModId = structureID.split(":")[0];
            String structureIdOnly = structureID.split(":")[1];
            if (ModList.get().isLoaded(structureModId)) {

                // Check if structure id is valid
                ResourceLocation structureResourceLocation = new ResourceLocation(structureID);
                if (ForgeRegistries.STRUCTURE_FEATURES.containsKey(structureResourceLocation)) {
                    // Get distance from player
                    TagKey<ConfiguredStructureFeature<?, ?>> structureFeature = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(structureModId, structureIdOnly));
                    assert structureFeature != null;
//                    BlockPos structurePos = serverLevel.getChunkSource().getGenerator().findNearestMapFeature(serverLevel,  structureFeature, playerPos, 100, false);
                    BlockPos structurePos = serverLevel.findNearestMapFeature(structureFeature, playerPos, 100, false);
                    // Compare distance to previous minimum
                    if (structurePos != null) {
                        int structureDistance = ERUtils.getBlockDistance(structurePos, playerPos);
                        // if distance is smaller or default value is unchanged, set as new minimum
                        if (shortestStructureDistance > structureDistance || shortestStructureDistance == -1) {
                            shortestStructureDistance = structureDistance;
                            nearestStructurePos = structurePos;
                        }
                    }
                }
            }
        }
        return nearestStructurePos;
    }
}

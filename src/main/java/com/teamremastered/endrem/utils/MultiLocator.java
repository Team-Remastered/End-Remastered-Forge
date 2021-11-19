package com.teamremastered.endrem.utils;

import com.teamremastered.endrem.EndRemastered;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
        StructureFeature<?> nearestStructureFeature = null;

        for (String structureID : this.getStructureList()) {
            // Verify if structure mod is loaded
            String structureModId = structureID.split(":")[0];
            if (ModList.get().isLoaded(structureModId)) {

                // Check if structure id is valid
                ResourceLocation structureResourceLocation = new ResourceLocation(structureID);
                if (ForgeRegistries.STRUCTURE_FEATURES.containsKey(structureResourceLocation)) {
                    // Get distance from player
                    StructureFeature<?> structureFeature = ForgeRegistries.STRUCTURE_FEATURES.getValue(structureResourceLocation);
                    assert structureFeature != null;
                    BlockPos structurePos = serverLevel.getChunkSource().getGenerator().findNearestMapFeature(serverLevel, structureFeature, playerPos, 100, false);

                    // Compare distance to previous minimum
                    if (structurePos != null) {
                        int structureDistance = ERUtils.getDistance(structurePos, playerPos);
                        // if distance is smaller or default value is unchanged, set as new minimum
                        if (shortestStructureDistance > structureDistance || shortestStructureDistance == -1) {
                            shortestStructureDistance = structureDistance;
                            nearestStructurePos = structurePos;
                            nearestStructureFeature = structureFeature;
                        }
                    }

                }
            }
        }

        return nearestStructurePos;
    }
}

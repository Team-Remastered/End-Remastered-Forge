package com.teamremastered.endrem.world.structures.config;

import com.google.common.collect.ImmutableMap;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.world.structures.AncientWitchHut;
import com.teamremastered.endrem.world.structures.EndCastle;
import com.teamremastered.endrem.world.structures.EndGate;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashMap;
import java.util.Map;

public class StructureGenerator {

    public static void init() {
        ERStructures.STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
        // For events that happen after initialization.
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, StructureGenerator::addDimensionalSpacing);
        // The comments for BiomeLoadingEvent and StructureSpawnListGatherEvent says to do HIGH for additions.
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, StructureGenerator::biomeModification);
    }
    public static void setup() {
        ERStructures.setupStructures();
        ERStructures.registerAllPieces();
        ERConfiguredStructures.registerConfiguredStructures();
        ERStructures.setupStructures();
        ERConfiguredStructures.registerConfiguredStructures();

        BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap = settings.getValue().structureSettings().structureConfig();

            if(structureMap instanceof ImmutableMap){
                Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
                tempMap.put(ERStructures.END_CASTLE.get(), StructureSettings.DEFAULTS.get(ERStructures.END_CASTLE.get()));
                tempMap.put(ERStructures.END_GATE.get(), StructureSettings.DEFAULTS.get(ERStructures.END_GATE.get()));
                tempMap.put(ERStructures.ANCIENT_WITCH_HUT.get(), StructureSettings.DEFAULTS.get(ERStructures.ANCIENT_WITCH_HUT.get()));

                settings.getValue().structureSettings().structureConfig = tempMap;
            }
            else{
                structureMap.put(ERStructures.END_CASTLE.get(), StructureSettings.DEFAULTS.get(ERStructures.END_CASTLE.get()));
                structureMap.put(ERStructures.END_GATE.get(), StructureSettings.DEFAULTS.get(ERStructures.END_GATE.get()));
                structureMap.put(ERStructures.ANCIENT_WITCH_HUT.get(), StructureSettings.DEFAULTS.get(ERStructures.ANCIENT_WITCH_HUT.get()));
            }
        });
    }

    /* Add our structures to biomes */
    public static void biomeModification(final BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();

        if (ERConfig.END_CASTLE_DISTANCE.getRaw() > 0 && EndCastle.getValidBiomeCategories().contains(event.getCategory()) && !ERConfig.END_CASTLE_BLACKLISTED_BIOMES.getList().contains(event.getName().toString())) {
            generation.getStructures().add(() -> (ERConfiguredStructures.CONFIGURED_END_CASTLE));
        }
        if (ERConfig.END_GATE_DISTANCE.getRaw() > 0 && EndGate.getValidBiomeCategories().contains(event.getCategory()) && !ERConfig.END_GATE_BLACKLISTED_BIOMES.getList().contains(event.getName().toString())) {
            generation.getStructures().add(() -> (ERConfiguredStructures.CONFIGURED_END_GATE));
        }
        if (ERConfig.ANCIENT_WITCH_HUT_DISTANCE.getRaw() > 0 && AncientWitchHut.getValidBiomeCategories().contains(event.getCategory())) {
            generation.getStructures().add(() -> (ERConfiguredStructures.CONFIGURED_ANCIENT_WITCH_HUT));
        }
    }

    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel serverLevel) {
            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(serverLevel.getChunkSource().generator.getSettings().structureConfig());

            // Prevent spawning our structure in Vanilla's superflat world
            if (serverLevel.getChunkSource().getGenerator() instanceof FlatLevelSource &&
                    serverLevel.dimension().equals(Level.OVERWORLD)) {
                return;
            }
            // Only add whitelisted dimensions
            else if (!ERConfig.WHITELISTED_DIMENSIONS.getList().contains(serverLevel.dimension().location().toString())) {
                tempMap.keySet().remove(ERStructures.END_CASTLE.get());
                tempMap.keySet().remove(ERStructures.END_GATE.get());
                tempMap.keySet().remove(ERStructures.ANCIENT_WITCH_HUT.get());
            }
            else {
                tempMap.putIfAbsent(ERStructures.END_CASTLE.get(), StructureSettings.DEFAULTS.get(ERStructures.END_CASTLE.get()));
                tempMap.putIfAbsent(ERStructures.END_GATE.get(), StructureSettings.DEFAULTS.get(ERStructures.END_GATE.get()));
                tempMap.putIfAbsent(ERStructures.ANCIENT_WITCH_HUT.get(), StructureSettings.DEFAULTS.get(ERStructures.ANCIENT_WITCH_HUT.get()));
            }
            serverLevel.getChunkSource().generator.getSettings().structureConfig = tempMap;
        }
    }
}

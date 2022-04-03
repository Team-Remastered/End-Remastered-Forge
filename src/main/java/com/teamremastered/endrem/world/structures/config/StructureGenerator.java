package com.teamremastered.endrem.world.structures.config;

import com.google.common.collect.ImmutableMap;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.mixin.world.ChunkGeneratorAccessorMixin;
import com.teamremastered.endrem.world.structures.AncientWitchHut;
import com.teamremastered.endrem.world.structures.EndCastle;
import com.teamremastered.endrem.world.structures.EndGate;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StructureGenerator {

    public static void init() {
        ERStructures.STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
        // For events that happen after initialization.
        FMLJavaModLoadingContext.get().getModEventBus().addListener(StructureGenerator::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(StructureGenerator::biomeModification);
        MinecraftForge.EVENT_BUS.addListener(StructureGenerator::addDimensionalSpacing);

        if (!ERConfig.MONSTER_DIFFICULTY.getRaw().equals("peaceful")) {
            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, EndGate::setupStructureSpawns);
            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, EndCastle::setupStructureSpawns);
            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, AncientWitchHut::setupStructureSpawns);
        }
    }
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Add Structures to the structure map
            Structure.STRUCTURES_REGISTRY.put("End Castle".toLowerCase(Locale.ROOT), ERStructures.END_CASTLE.get());
            Structure.STRUCTURES_REGISTRY.put("End Gate".toLowerCase(Locale.ROOT), ERStructures.END_GATE.get());
            Structure.STRUCTURES_REGISTRY.put("Ancient Witch Hut".toLowerCase(Locale.ROOT), ERStructures.ANCIENT_WITCH_HUT.get());

        ERStructures.setupStructures();
        ERStructures.registerAllPieces();
        ERConfiguredStructures.registerConfiguredStructures();

        // Register separation settings for mods that might need it, like Terraforged
        WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings().structureConfig();

            if(structureMap instanceof ImmutableMap){
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(ERStructures.END_CASTLE.get(), DimensionStructuresSettings.DEFAULTS.get(ERStructures.END_CASTLE.get()));
                tempMap.put(ERStructures.END_GATE.get(), DimensionStructuresSettings.DEFAULTS.get(ERStructures.END_GATE.get()));
                tempMap.put(ERStructures.ANCIENT_WITCH_HUT.get(), DimensionStructuresSettings.DEFAULTS.get(ERStructures.ANCIENT_WITCH_HUT.get()));

                settings.getValue().structureSettings().structureConfig = tempMap;
            }
            else{
                structureMap.put(ERStructures.END_CASTLE.get(), DimensionStructuresSettings.DEFAULTS.get(ERStructures.END_CASTLE.get()));
                structureMap.put(ERStructures.END_GATE.get(), DimensionStructuresSettings.DEFAULTS.get(ERStructures.END_GATE.get()));
                structureMap.put(ERStructures.ANCIENT_WITCH_HUT.get(), DimensionStructuresSettings.DEFAULTS.get(ERStructures.ANCIENT_WITCH_HUT.get()));
            }
        });
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

    private static Method GETCODEC_METHOD;
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();

                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey(((ChunkGeneratorAccessorMixin) serverWorld.getChunkSource().generator).endrem_getCodec());
                if (cgRL != null && cgRL.getNamespace().equals("terraforged")) return;

            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());

            // Prevent spawning our structure in Vanilla's superflat world
            if (serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator && serverWorld.dimension().equals(World.OVERWORLD)) {
                tempMap.keySet().remove(ERStructures.END_CASTLE.get());
                tempMap.keySet().remove(ERStructures.END_GATE.get());
                tempMap.keySet().remove(ERStructures.ANCIENT_WITCH_HUT.get());
            }

            // Only add whitelisted dimensions
            else if (!ERConfig.WHITELISTED_DIMENSIONS.getList().contains(serverWorld.dimension().location().toString())) {
                tempMap.keySet().remove(ERStructures.END_CASTLE.get());
                tempMap.keySet().remove(ERStructures.END_GATE.get());
                tempMap.keySet().remove(ERStructures.ANCIENT_WITCH_HUT.get());
            }
            else {
                tempMap.putIfAbsent(ERStructures.END_CASTLE.get(), DimensionStructuresSettings.DEFAULTS.get(ERStructures.END_CASTLE.get()));
                tempMap.putIfAbsent(ERStructures.END_GATE.get(), DimensionStructuresSettings.DEFAULTS.get(ERStructures.END_GATE.get()));
                tempMap.putIfAbsent(ERStructures.ANCIENT_WITCH_HUT.get(), DimensionStructuresSettings.DEFAULTS.get(ERStructures.ANCIENT_WITCH_HUT.get()));
            }

             serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
        }
    }
}

package com.teamremastered.endrem.world.structures.config;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.mojang.serialization.Codec;
import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.world.structures.AncientWitchHut;
import com.teamremastered.endrem.world.structures.EndCastle;
import com.teamremastered.endrem.world.structures.EndGate;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class StructureGenerator {

    public static void init() {
        ERStructures.STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
        // For events that happen after initialization.
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, StructureGenerator::addDimensionalSpacing);

        // Setup Structure Spawns
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, EndGate::setupStructureSpawns);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, EndCastle::setupStructureSpawns);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, AncientWitchHut::setupStructureSpawns);
    }
    public static void setup() {
        ERStructures.setupStructures();
        ERStructures.registerAllPieces();
        ERConfiguredStructures.registerConfiguredStructures();
        ERStructures.setupStructures();
        ERConfiguredStructures.registerConfiguredStructures();
    }

    private static Method GETCODEC_METHOD;

    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerLevel serverLevel){
            ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();

            if (chunkGenerator instanceof FlatLevelSource && ERConfig.WHITELISTED_DIMENSIONS.getList().contains(serverLevel.dimension().toString())) {
                return;
            }

            StructureSettings worldStructureConfig = chunkGenerator.getSettings();

            HashMap<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> ERStructureToMultiMap = new HashMap<>();

            for(Map.Entry<ResourceKey<Biome>, Biome> biomeEntry : serverLevel.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY).entrySet()) {
                String biomeName = biomeEntry.getKey().location().toString();
                String biomeCategoryName = biomeEntry.getValue().getBiomeCategory().getName();

                if (ERConfig.END_CASTLE_WHITELISTED_BIOME_CATEGORIES.getList().contains(biomeCategoryName) && !ERConfig.END_CASTLE_BLACKLISTED_BIOMES.getList().contains(biomeName) && ERConfig.END_CASTLE_DISTANCE.getRaw() > 0) {
                    associateBiomeToConfiguredStructure(ERStructureToMultiMap, ERConfiguredStructures.CONFIGURED_END_CASTLE, biomeEntry.getKey());
                }

                if (ERConfig.END_GATE_WHITELISTED_BIOME_CATEGORIES.getList().contains(biomeCategoryName) && !ERConfig.END_GATE_BLACKLISTED_BIOMES.getList().contains(biomeName)&& ERConfig.END_GATE_DISTANCE.getRaw() > 0) {
                    associateBiomeToConfiguredStructure(ERStructureToMultiMap, ERConfiguredStructures.CONFIGURED_END_GATE, biomeEntry.getKey());
                }

                if (biomeEntry.getValue().getBiomeCategory().equals(Biome.BiomeCategory.SWAMP) && ERConfig.ANCIENT_WITCH_HUT_DISTANCE.getRaw() > 0) {
                    associateBiomeToConfiguredStructure(ERStructureToMultiMap, ERConfiguredStructures.CONFIGURED_ANCIENT_WITCH_HUT, biomeEntry.getKey());
                }
            }

            ImmutableMap.Builder<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> tempStructureToMultiMap = ImmutableMap.builder();
            worldStructureConfig.configuredStructures.entrySet().stream().filter(entry -> !ERStructureToMultiMap.containsKey(entry.getKey())).forEach(tempStructureToMultiMap::put);

            ERStructureToMultiMap.forEach((key, value) -> tempStructureToMultiMap.put(key, ImmutableMultimap.copyOf(value)));

            worldStructureConfig.configuredStructures = tempStructureToMultiMap.build();

            try {
                if(GETCODEC_METHOD == null) GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "codec");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(chunkGenerator));
                if(cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            }
            catch(Exception e){
                EndRemastered.LOGGER.error("Was unable to check if " + serverLevel.dimension().location() + " is using Terraforged's ChunkGenerator.");
            }

            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(worldStructureConfig.structureConfig());
            tempMap.putIfAbsent(ERStructures.END_CASTLE.get(), StructureSettings.DEFAULTS.get(ERStructures.END_CASTLE.get()));
            tempMap.putIfAbsent(ERStructures.END_GATE.get(), StructureSettings.DEFAULTS.get(ERStructures.END_GATE.get()));
            tempMap.putIfAbsent(ERStructures.ANCIENT_WITCH_HUT.get(), StructureSettings.DEFAULTS.get(ERStructures.ANCIENT_WITCH_HUT.get()));
            worldStructureConfig.structureConfig = tempMap;
        }
    }

    private static void associateBiomeToConfiguredStructure(Map<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> ERStructureToMultiMap, ConfiguredStructureFeature<?, ?> configuredStructureFeature, ResourceKey<Biome> biomeRegistryKey) {
        ERStructureToMultiMap.putIfAbsent(configuredStructureFeature.feature, HashMultimap.create());
        HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> configuredStructureToBiomeMultiMap = ERStructureToMultiMap.get(configuredStructureFeature.feature);
        if(configuredStructureToBiomeMultiMap.containsValue(biomeRegistryKey)) {
            EndRemastered.LOGGER.error("""
                    Detected 2 ConfiguredStructureFeatures that share the same base StructureFeature trying to be added to same biome. One will be prevented from spawning.
                    The two conflicting ConfiguredStructures are: {}, {}
                    The biome that is attempting to be shared: {}
                """,
                    BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureFeature),
                    BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureToBiomeMultiMap.entries().stream().filter(e -> e.getValue() == biomeRegistryKey).findFirst().get().getKey()),
                    biomeRegistryKey
            );
        }
        else{
            configuredStructureToBiomeMultiMap.put(configuredStructureFeature, biomeRegistryKey);
        }
    }
}

package com.teamremastered.endrem.world.gen;

import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.registers.ERBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
public class OreGenHandler {

    public static ConfiguredFeature<?, ?> END_CRYSTAL_ORE_GEN;

    public static void initRegister() {
        END_CRYSTAL_ORE_GEN = register("end_crystal_ore", Feature.ORE
                .configured(new OreFeatureConfig(new TagMatchRuleTest(EndRemastered.END_CRYSTAL_GEN),
                        ERBlocks.END_CRYSTAL_ORE.get().defaultBlockState(),
                        3)) // Vein size
                .range(150) // ???
                .squared()
                .count(30));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void gen(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        if (event.getCategory().equals(Biome.Category.NETHER)) {
            if (END_CRYSTAL_ORE_GEN != null)
                generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, END_CRYSTAL_ORE_GEN);
        }
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(EndRemastered.MOD_ID, name), configuredFeature);
    }

}


package com.teamremastered.endrem.world.gen;

import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.registers.ERBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
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
                .configured(new OreConfiguration(new TagMatchTest(EndRemastered.END_CRYSTAL_GEN),
                        ERBlocks.END_CRYSTAL_ORE.get().defaultBlockState(),
                        3, // Vein size
                        0)));  // Exposition of the Ore
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void gen(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        if (event.getCategory().equals(Biome.BiomeCategory.NETHER)) {
            if (END_CRYSTAL_ORE_GEN != null)
                generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, END_CRYSTAL_ORE_GEN.placed());
        }
    }

    private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(EndRemastered.MOD_ID, name), configuredFeature);
    }

}


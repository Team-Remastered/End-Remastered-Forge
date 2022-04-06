package com.teamremastered.endrem.world.gen;

import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.registers.ERBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;


@Mod.EventBusSubscriber
public class OreGenHandler {

    public static Holder<PlacedFeature> END_CRYSTAL_ORE_GEN;
    public static void initRegister() {
        OreConfiguration EndCrystalConfig = new OreConfiguration(new TagMatchTest(EndRemastered.END_CRYSTAL_GEN), ERBlocks.END_CRYSTAL_ORE.get().defaultBlockState(), 3);
        END_CRYSTAL_ORE_GEN = registerPlacedOreFeature("end_crystal_ore", new ConfiguredFeature<>(Feature.ORE, EndCrystalConfig),
                CountPlacement.of(100),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(100)));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void gen(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        if (event.getCategory().equals(Biome.BiomeCategory.NETHER)) {
            if (END_CRYSTAL_ORE_GEN != null)
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, END_CRYSTAL_ORE_GEN);
        }
    }

    private static <C extends FeatureConfiguration, F extends Feature<C>> Holder<PlacedFeature> registerPlacedOreFeature(String registryName, ConfiguredFeature<C, F> feature, PlacementModifier... placementModifiers) {
        return PlacementUtils.register(registryName, Holder.direct(feature), placementModifiers);
    }

}


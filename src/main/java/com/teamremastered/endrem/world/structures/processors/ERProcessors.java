package com.teamremastered.endrem.world.structures.processors;

import com.teamremastered.endrem.EndRemastered;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ERProcessors {

    public static IStructureProcessorType<WaterLoggedProcessor> WATERLOGGED_PROCESSOR = () -> WaterLoggedProcessor.CODEC;

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ERProcessors::setup);
    }

    private static void setup (FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(EndRemastered.MOD_ID, "waterlogged_processor"), WATERLOGGED_PROCESSOR);
        });
    }
}

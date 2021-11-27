package com.teamremastered.endrem.world.structures.processors;

import com.teamremastered.endrem.EndRemastered;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ERProcessors {

    public static StructureProcessorType<WaterLoggedProcessor> WATERLOGGED_PROCESSOR = () -> WaterLoggedProcessor.CODEC;

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ERProcessors::setup);
    }

    private static void setup (FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(EndRemastered.MOD_ID, "waterlogged_processor"), WATERLOGGED_PROCESSOR);
        });
    }
}

package com.teamremastered.endrem.client;

import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.registers.ERBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = EndRemastered.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(ERBlocks.END_CRYSTAL_BLOCK.get(), RenderType.translucent());
    }
}

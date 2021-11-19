package com.teamremastered.endrem.events;

import com.teamremastered.endrem.commands.GetEndremMapCommand;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.items.ERMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber
public class ModEvents {
    // Enable/Disable placing of vanilla Ender Eyes depending on configuration
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCancelable() && !ERConfig.USE_ENDER_EYES_ENABLED.get()) {
            if (event.getPlayer().getInventory().getSelected().getItem() instanceof EnderEyeItem) {
                if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.END_PORTAL_FRAME) {
                    event.setCanceled(true);
                }
            }
        }
    }

    // Enable/Disable throwing of vanilla Ender Eyes depending on configuration
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.isCancelable() && !ERConfig.USE_ENDER_EYES_ENABLED.get()) {
            if (event.getPlayer().getInventory().getSelected().getItem() instanceof EnderEyeItem) {
                event.setCanceled(true);
            }
        }
    }

    // Register GetEndremMapCommand
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new GetEndremMapCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    // Add ERMap to Cartographers
    @SubscribeEvent
    public static void onVillagerTradesEvent(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.CARTOGRAPHER) {
            event.getTrades().get(3).add(new ERMap.ERMapTrade());
        }
    }

    // Add ERMap to WanderingTraders
    @SubscribeEvent
    public static void onWandererTradesEvent(WandererTradesEvent event) {
        event.getGenericTrades().add(new ERMap.ERMapTrade());
    }
}

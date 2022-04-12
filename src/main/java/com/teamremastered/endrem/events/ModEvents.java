package com.teamremastered.endrem.events;

import com.teamremastered.endrem.config.ERConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class ModEvents {
    // Enable/Disable placing of vanilla Ender Eyes depending on configuration
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCancelable() && !ERConfig.USE_ENDER_EYES_ENABLED.getRaw()) {
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
        if (event.isCancelable() && !ERConfig.USE_ENDER_EYES_ENABLED.getRaw()) {
            if (event.getPlayer().getInventory().getSelected().getItem() instanceof EnderEyeItem) {
                event.setCanceled(true);
            }
        }
    }
}

package com.teamremastered.endrem.events;

import com.teamremastered.endrem.commands.GetEndremMapCommand;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.items.ERMap;
import com.teamremastered.endrem.items.EndCrystalArmor;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class ModEvents {
    // Enable/Disable placing of vanilla Ender Eyes depending on configuration
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCancelable() && !ERConfig.USE_ENDER_EYES_ENABLED.getRaw()) {
            if (event.getPlayer().inventory.getSelected().getItem() instanceof EnderEyeItem) {
                if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.END_PORTAL_FRAME) {
                    event.setCanceled(true);
                    event.getPlayer().displayClientMessage(new TranslationTextComponent("block.endrem.ender_eye.warning"), true);
                }
            }
        }
    }

    // Enable/Disable throwing of vanilla Ender Eyes depending on configuration
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.isCancelable() && !ERConfig.USE_ENDER_EYES_ENABLED.getRaw()) {
            if (event.getPlayer().inventory.getSelected().getItem() instanceof EnderEyeItem) {
                event.setCanceled(true);
                event.getPlayer().displayClientMessage(new TranslationTextComponent("block.endrem.ender_eye.warning"), true);
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
        if (event.getType() == VillagerProfession.CARTOGRAPHER && !ERMap.nullCheck) {
            event.getTrades().get(3).add(new ERMap.ERMapTrade());
        }
    }

    // Add ERMap to WanderingTraders
    @SubscribeEvent
    public static void onWandererTradesEvent(WandererTradesEvent event) {
        if (!ERMap.nullCheck) {
            event.getGenericTrades().add(new ERMap.ERMapTrade());
        }
    }

    // Add End Crystal Armor Special Effect
    @SubscribeEvent
    public static void onCriticalHitEvent(CriticalHitEvent event) {
        if (event.getResult() == Event.Result.ALLOW || (event.getResult() == Event.Result.DEFAULT && event.isVanillaCritical())) {
            final int MULTIPLIER = Integer.parseInt(ERConfig.END_CRYSTAL_ARMOR_STATS.getList().get(5));

            int duration = 0;
            for (ItemStack stack : event.getPlayer().getArmorSlots()) {
                if (stack.getItem() instanceof EndCrystalArmor) {
                    duration++;
                }
            }

            // Duration in ticks, 1 second = 20 ticks
            duration *= MULTIPLIER;
            if (duration > 0 && event.getTarget().getType().getCategory() == EntityClassification.MONSTER) {
                event.getPlayer().addEffect(new EffectInstance(Effects.REGENERATION, duration, 1));
            }
        }
    }
}

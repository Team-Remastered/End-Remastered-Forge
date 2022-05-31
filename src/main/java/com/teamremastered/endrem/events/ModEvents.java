package com.teamremastered.endrem.events;

import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.registers.ERItems;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import java.util.Random;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class ModEvents {

    public static boolean isEnchanting = false;

    // Enable/Disable placing of vanilla Ender Eyes depending on configuration
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCancelable() && !ERConfig.USE_ENDER_EYES_ENABLED.getRaw()) {
            if (event.getPlayer().getInventory().getSelected().getItem() instanceof EnderEyeItem) {
                if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.END_PORTAL_FRAME) {
                    event.setCanceled(true);
                    event.getPlayer().displayClientMessage(new TranslatableComponent("block.endrem.ender_eye.warning"), true);
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
                if (event.getWorld().getBlockState(event.getPos()).getBlock() != Blocks.END_PORTAL_FRAME)
                    event.getPlayer().displayClientMessage(new TranslatableComponent("block.endrem.ender_eye.warning"), true);
            }
        }
    }

    //Detect when player is enchanting
    @SubscribeEvent
    public static void playerOpenEnchantingTable(PlayerContainerEvent.Open event) {

        if (event.getContainer().getType().equals(MenuType.ENCHANTMENT)) {
            isEnchanting = true;
        }
            System.out.println("Player is Enchanting " + isEnchanting);
        }


    //Detect when player isn't enchanting anymore
    @SubscribeEvent
    public static void playerCloseEnchantingTable(PlayerContainerEvent.Close event) {

        if (event.getContainer().getType().equals(MenuType.ENCHANTMENT)) {
            isEnchanting = false;
        }
        System.out.println("Player is Enchanting " + isEnchanting);
    }

    @SubscribeEvent
    public static void getCrypticEye(PlayerXpEvent.LevelChange event) {
        Player player = event.getPlayer();
        Level level = event.getPlayer().getLevel();
        Random random = new Random();
        int maxValue = 100;
        int randomNumber = random.nextInt(maxValue);

        if (player != null && !level.isClientSide) {
            int levelGained = event.getLevels();

            if (isEnchanting && levelGained < -2 && randomNumber == 69) {
                player.getInventory().add(ERItems.CRYPTIC_EYE.get().asItem().getDefaultInstance());
            }
        }
    }

    @SubscribeEvent
    public static void onVillagerTradesEvent(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.CLERIC) {
            event.getTrades().get(5).add(new EREyeTrade());
        }
    }

    public static class EREyeTrade implements VillagerTrades.ItemListing {

        @Override
        public MerchantOffer getOffer(@Nonnull Entity entity, Random random) {
            int maxPrice = 50;
            int minPrice = 30;
            int priceEmeralds = random.nextInt(maxPrice - minPrice) + minPrice;
            if (!entity.level.isClientSide()) {
                return new MerchantOffer(new ItemStack(Items.EMERALD, priceEmeralds), new ItemStack(Items.RABBIT_FOOT), new ItemStack(ERItems.EVIL_EYE.get()), 12, 10, 0.2F);
            }
            return null;
        }
    }
}

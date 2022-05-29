package com.teamremastered.endrem.events;

import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.registers.ERItems;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.Random;

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

    @SubscribeEvent
    public static void onVillagerTradesEvent(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.CLERIC) {
            event.getTrades().get(5).add(new ERMapTrade());
        }
    }

    public static class ERMapTrade implements VillagerTrades.ItemListing {

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

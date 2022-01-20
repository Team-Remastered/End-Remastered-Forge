package com.teamremastered.endrem.items;

import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.registers.RegisterHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import javax.annotation.Nonnull;
import java.util.Random;

public class ERMap {
    private static int getMinPrice() {
        return ERConfig.getData().ER_MAP.minPrice;
    }

    private static int getMaxPrice() {
        return ERConfig.getData().ER_MAP.maxPrice;
    }

    private static int getEXP() {
        return ERConfig.getData().ER_MAP.xpGiven;
    }

    public static ItemStack createMap(ServerLevel serverLevel, BlockPos playerPosition) {
        // Get position of marker
        BlockPos structurePos = RegisterHandler.MAP_ML.getNearestPosition(serverLevel, playerPosition);

        // Create map
        ItemStack stack = MapItem.create(serverLevel, structurePos.getX(), structurePos.getZ(), (byte) 2 , true, true);
        MapItem.renderBiomePreviewMap(serverLevel, stack);
        MapItemSavedData.addTargetDecoration(stack, structurePos, "+", MapDecoration.Type.TARGET_X);

        // Set the name of the map
        stack.setHoverName(Component.nullToEmpty("End Remastered Map"));

        return stack;
    }

    public static class ERMapTrade implements VillagerTrades.ItemListing {

        @Override
        public MerchantOffer getOffer(@Nonnull Entity entity, Random random){
            int priceEmeralds = random.nextInt(getMaxPrice() - getMinPrice() + 1) + getMinPrice();
            if (!entity.level.isClientSide()) {
                ItemStack map = createMap((ServerLevel) entity.level, entity.blockPosition());
                return new MerchantOffer(new ItemStack(Items.EMERALD, priceEmeralds), new ItemStack(Items.COMPASS), map, 12, getEXP(), 0.2F);
            }
            return null;
        }
    }
}

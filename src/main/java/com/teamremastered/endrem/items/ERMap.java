package com.teamremastered.endrem.items;

import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.registers.RegisterHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

import javax.annotation.Nonnull;
import java.util.Random;

public class ERMap {
    private static int getMinPrice() {
        return Integer.parseInt(ERConfig.MAP_TRADE_VALUES.getList().get(0));
    }

    private static int getMaxPrice() {
        return Integer.parseInt(ERConfig.MAP_TRADE_VALUES.getList().get(1));
    }

    private static int getEXP() {
        return Integer.parseInt(ERConfig.MAP_TRADE_VALUES.getList().get(2));
    }

    public static ItemStack createMap(ServerWorld serverLevel, BlockPos playerPosition) {
        // Get position of marker
        BlockPos structurePos = RegisterHandler.MAP_ML.getNearestPosition(serverLevel, playerPosition);

        // Create map
        ItemStack stack = FilledMapItem.create(serverLevel, structurePos.getX(), structurePos.getZ(), (byte) 2 , true, true);
        FilledMapItem.renderBiomePreviewMap(serverLevel, stack);
        MapData.addTargetDecoration(stack, structurePos, "+", MapDecoration.Type.TARGET_X);

        // Set the name of the map
        stack.setHoverName(ITextComponent.nullToEmpty("End Remastered Map"));

        return stack;
    }

    public static class ERMapTrade implements VillagerTrades.ITrade {

        @Override
        public MerchantOffer getOffer(@Nonnull Entity entity, Random random){
            int priceEmeralds = random.nextInt(getMaxPrice() - getMinPrice() + 1) + getMinPrice();
            if (!entity.level.isClientSide()) {
                ItemStack map = createMap((ServerWorld) entity.level, entity.blockPosition());
                return new MerchantOffer(new ItemStack(Items.EMERALD, priceEmeralds), new ItemStack(Items.COMPASS), map, 12, getEXP(), 0.2F);
            }
            return null;
        }
    }
}

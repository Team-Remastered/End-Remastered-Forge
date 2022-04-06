package com.teamremastered.endrem.items;

import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.registers.RegisterHandler;
import com.teamremastered.endrem.utils.MultiLocator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ConfiguredStructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraftforge.registries.ForgeRegistries;

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


    public static ItemStack createMap(ServerLevel serverLevel, BlockPos playerPosition) {
        // Get position of marker
        BlockPos structurePos = serverLevel.findNearestMapFeature(MultiLocator.ENDREM_MAP_LOCATED, playerPosition, 100, false);

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

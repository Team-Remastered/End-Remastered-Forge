package com.teamremastered.endrem.items;

import com.teamremastered.endrem.EndRemastered;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
public class EREnderEye extends Item {
    public EREnderEye() {
        super(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(16).tab(EndRemastered.TAB));
    }

    @Override
    @ParametersAreNonnullByDefault
    public InteractionResult useOn(UseOnContext itemUse) {
        // TODO: Implement EREnderEye useOn -> portal action
        return InteractionResult.PASS;
    }

    @Override
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level levelIn, Player playerIn, InteractionHand handIn) {
        // TODO: Implement EREnderEye use -> locate action
        return InteractionResultHolder.pass(playerIn.getItemInHand(handIn));
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return !this.asItem().canBeDepleted();
    }

    @OnlyIn(Dist.CLIENT)
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, @Nullable Level levelIn, List<Component> tooltip, TooltipFlag flagIn) {
        String translationKey = String.format("item.%s.%s.description", EndRemastered.MOD_ID, this.asItem());
        tooltip.add(new TranslatableComponent(translationKey));
    }
}

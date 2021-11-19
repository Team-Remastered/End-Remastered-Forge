package com.teamremastered.endrem.items;

import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.blocks.AncientPortalFrame;
import com.teamremastered.endrem.blocks.ERFrameProperties;
import com.teamremastered.endrem.registers.ERBlocks;
import com.teamremastered.endrem.registers.RegisterHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
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
        Level level = itemUse.getLevel();
        BlockPos blockpos = itemUse.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);

        boolean frameHasEye = false;

        if (blockstate.is(ERBlocks.ANCIENT_PORTAL_FRAME.get())) {
            frameHasEye = blockstate.getValue(AncientPortalFrame.EYE) != ERFrameProperties.EMPTY;
        } else if (blockstate.is(Blocks.END_PORTAL_FRAME)) {
            frameHasEye = blockstate.getValue(BlockStateProperties.EYE);
        } else {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if (!frameHasEye) {
            ERFrameProperties frameProperties = ERFrameProperties.getFramePropertyFromEye(itemUse.getItemInHand().getItem());
            BlockState newBlockState = ERBlocks.ANCIENT_PORTAL_FRAME.get().defaultBlockState();
            newBlockState = newBlockState.setValue(HorizontalDirectionalBlock.FACING, blockstate.getValue(HorizontalDirectionalBlock.FACING));
            newBlockState = newBlockState.setValue(AncientPortalFrame.EYE, frameProperties);

            if (!AncientPortalFrame.IsFrameAlreadyPresent(level, newBlockState, blockpos)) {
                Block.pushEntitiesUp(blockstate, newBlockState, level, blockpos);
                level.setBlock(blockpos, newBlockState, 2);
                level.updateNeighbourForOutputSignal(blockpos, ERBlocks.ANCIENT_PORTAL_FRAME.get());
                itemUse.getItemInHand().shrink(1);
                level.levelEvent(1503, blockpos, 0);
                BlockPattern.BlockPatternMatch blockpattern$patternhelper = AncientPortalFrame.getOrCreatePortalShape().find(level, blockpos);

                if (blockpattern$patternhelper != null) {
                    BlockPos blockpos1 = blockpattern$patternhelper.getFrontTopLeft().offset(-3, 0, -3);

                    for (int i = 0; i < 3; ++i) {
                        for (int j = 0; j < 3; ++j) {
                            level.setBlock(blockpos1.offset(i, 0, j), Blocks.END_PORTAL.defaultBlockState(), 2);
                        }
                    }

                    level.globalLevelEvent(1038, blockpos1.offset(1, 0, 1), 0);
                }
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        } else if (blockstate.is(Blocks.END_PORTAL_FRAME)) {
            BlockState newBlockState = blockstate.setValue(BlockStateProperties.EYE, false);
            level.setBlock(blockpos, newBlockState, 2);
            level.addFreshEntity(new ItemEntity(level, blockpos.getX(), blockpos.getY() + 1, blockpos.getZ(), new ItemStack(Items.ENDER_EYE)));
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level levelIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        BlockHitResult raytraceResult = getPlayerPOVHitResult(levelIn, playerIn, ClipContext.Fluid.NONE);
        boolean lookingAtFrame = false;


        BlockState state = levelIn.getBlockState(raytraceResult.getBlockPos());
        if (state.is(ERBlocks.ANCIENT_PORTAL_FRAME.get())) {
            lookingAtFrame = true;
        }


        if (lookingAtFrame) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            playerIn.startUsingItem(handIn);
            if (levelIn instanceof ServerLevel) {
                BlockPos blockpos = RegisterHandler.EYE_ML.getNearestPosition((ServerLevel) levelIn, playerIn.getOnPos());
                if (blockpos != null) {
                    EyeOfEnder eyeofenderentity = new EyeOfEnder(levelIn, playerIn.getX(), playerIn.getY(0.5D), playerIn.getZ());
                    eyeofenderentity.setItem(itemstack);
                    eyeofenderentity.signalTo(blockpos);
                    eyeofenderentity.surviveAfterDeath = true;

                    levelIn.addFreshEntity(eyeofenderentity);
                    if (playerIn instanceof ServerPlayer) {
                        CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayer) playerIn, blockpos);
                    }

                    levelIn.playSound(null, playerIn.blockPosition(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 0.5F, 0.4F / (levelIn.getRandom().nextFloat() * 0.4F + 0.8F));
                    levelIn.levelEvent(null, 1003, playerIn.blockPosition(), 0);

                    if (!playerIn.isCreative()) {
                        itemstack.shrink(1);
                    }

                    playerIn.awardStat(Stats.ITEM_USED.get(this));
                    playerIn.swing(handIn, true);
                    return InteractionResultHolder.success(itemstack);
                }
            }
            return InteractionResultHolder.consume(itemstack);
        }
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

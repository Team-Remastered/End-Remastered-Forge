package com.teamremastered.endrem.items;

import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.blocks.AncientPortalFrame;
import com.teamremastered.endrem.blocks.ERFrameProperties;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.registers.ERBlocks;
import com.teamremastered.endrem.registers.RegisterHandler;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.EyeOfEnderEntity;
import net.minecraft.item.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
    public ActionResultType useOn(ItemUseContext itemUse) {
        World world = itemUse.getLevel();
        BlockPos blockpos = itemUse.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);

        boolean frameHasEye;

        if (blockstate.is(ERBlocks.ANCIENT_PORTAL_FRAME.get())) {
            frameHasEye = blockstate.getValue(AncientPortalFrame.EYE) != ERFrameProperties.EMPTY;
        } else if (blockstate.is(Blocks.END_PORTAL_FRAME)) {
            frameHasEye = blockstate.getValue(BlockStateProperties.EYE);
        } else {
            return ActionResultType.PASS;
        }

        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        } else if (!frameHasEye) {
            ERFrameProperties frameProperties = ERFrameProperties.getFramePropertyFromEye(itemUse.getItemInHand().getItem());
            BlockState newBlockState = ERBlocks.ANCIENT_PORTAL_FRAME.get().defaultBlockState();
            newBlockState = newBlockState.setValue(HorizontalBlock.FACING, blockstate.getValue(HorizontalBlock.FACING));
            newBlockState = newBlockState.setValue(AncientPortalFrame.EYE, frameProperties);

            if (AncientPortalFrame.IsFrameAbsent(world, newBlockState, blockpos)) {
                Block.pushEntitiesUp(blockstate, newBlockState, world, blockpos);
                world.setBlock(blockpos, newBlockState, 2);
                world.updateNeighbourForOutputSignal(blockpos, ERBlocks.ANCIENT_PORTAL_FRAME.get());
                itemUse.getItemInHand().shrink(1);
                world.levelEvent(1503, blockpos, 0);
                BlockPattern.PatternHelper blockpattern$patternhelper = AncientPortalFrame.getPortalShape(ERFrameProperties.EMPTY, true).find(world, blockpos);

                if (blockpattern$patternhelper != null) {
                    BlockPos blockpos1 = blockpattern$patternhelper.getFrontTopLeft().offset(-3, 0, -3);

                    for (int i = 0; i < 3; ++i) {
                        for (int j = 0; j < 3; ++j) {
                            world.setBlock(blockpos1.offset(i, 0, j), Blocks.END_PORTAL.defaultBlockState(), 2);
                        }
                    }

                    world.globalLevelEvent(1038, blockpos1.offset(1, 0, 1), 0);
                }
                return ActionResultType.CONSUME;
            }
            return ActionResultType.PASS;
        } else if (blockstate.is(Blocks.END_PORTAL_FRAME)) {
            BlockState newBlockState = blockstate.setValue(BlockStateProperties.EYE, false);
            world.setBlock(blockpos, newBlockState, 2);
            world.addFreshEntity(new ItemEntity(world, blockpos.getX(), blockpos.getY() + 1, blockpos.getZ(), new ItemStack(Items.ENDER_EYE)));
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> use(World WorldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        BlockRayTraceResult raytraceResult = getPlayerPOVHitResult(WorldIn, playerIn, RayTraceContext.FluidMode.NONE);
        boolean lookingAtFrame = false;


        BlockState state = WorldIn.getBlockState(raytraceResult.getBlockPos());
        if (state.is(ERBlocks.ANCIENT_PORTAL_FRAME.get())) {
            lookingAtFrame = true;
        }


        if (lookingAtFrame) {
            return ActionResult.pass(itemstack);
        } else {
            playerIn.startUsingItem(handIn);
            if (WorldIn instanceof ServerWorld) {
                BlockPos blockpos = RegisterHandler.EYE_ML.getNearestPosition((ServerWorld) WorldIn, new BlockPos(playerIn.getX(), playerIn.getY(), playerIn.getZ()));
                if (blockpos != null) {
                    EyeOfEnderEntity eyeofenderentity = new EyeOfEnderEntity(WorldIn, playerIn.getX(), playerIn.getY(0.5D), playerIn.getZ());
                    eyeofenderentity.setItem(itemstack);
                    eyeofenderentity.signalTo(blockpos);
                    eyeofenderentity.surviveAfterDeath = ERConfig.EYE_BREAK_CHANCE.getRaw() <= playerIn.getRandom().nextInt(100);

                    WorldIn.addFreshEntity(eyeofenderentity);
                    if (playerIn instanceof ServerPlayerEntity) {
                        CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayerEntity) playerIn, blockpos);
                    }

                    WorldIn.playSound(null, playerIn.blockPosition(), SoundEvents.ENDER_EYE_LAUNCH, SoundCategory.NEUTRAL, 0.5F, 0.4F / (WorldIn.getRandom().nextFloat() * 0.4F + 0.8F));
                    WorldIn.levelEvent(null, 1003, playerIn.blockPosition(), 0);

                    if (!playerIn.isCreative()) {
                        itemstack.shrink(1);
                    }

                    playerIn.awardStat(Stats.ITEM_USED.get(this));
                    playerIn.swing(handIn, true);
                    return ActionResult.success(itemstack);
                }
            }
            return ActionResult.consume(itemstack);
        }
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return !this.asItem().canBeDepleted();
    }

    @OnlyIn(Dist.CLIENT)
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, @Nullable World levelIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String translationKey = String.format("item.%s.%s.description", EndRemastered.MOD_ID, this.asItem());
        tooltip.add(new TranslationTextComponent(translationKey));
    }
}

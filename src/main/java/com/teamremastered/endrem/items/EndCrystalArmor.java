package com.teamremastered.endrem.items;

import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.registers.ERItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.List;

public class EndCrystalArmor extends ArmorItem {
    public EndCrystalArmor(EquipmentSlot slot) {
        super(new EndCrystalArmorMaterial(), slot, new Item.Properties().tab(EndRemastered.TAB).rarity(Rarity.UNCOMMON));
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        int layer = slot == EquipmentSlot.LEGS ? 2 : 1;

        if ("overlay".equals(type))
            return EndRemastered.MOD_ID + ":textures/models/armor/all_layer_" + layer + "_overlay.png";

        return EndRemastered.MOD_ID + ":textures/models/armor/end_crystal_layer_" + layer + ".png";
    }

    @ParametersAreNullableByDefault
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flagIn) {
        assert tooltip != null;
        tooltip.add(new TranslatableComponent("item.endrem.armor.description.main"));
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return ERConfig.getData().END_CRYSTAL_GEAR.ARMOR.neutralizesPiglins;
    }

    @MethodsReturnNonnullByDefault
    private static class EndCrystalArmorMaterial implements ArmorMaterial {

        public int getDurabilityForSlot(EquipmentSlot slotIn) {
            double factor = ERConfig.getData().END_CRYSTAL_GEAR.ARMOR.durabilityFactor;
            return (int) (new int[]{13, 15, 16, 11}[slotIn.getIndex()] * factor);
        }

        public int getDefenseForSlot(EquipmentSlot slotIn) {
            double factor = ERConfig.getData().END_CRYSTAL_GEAR.ARMOR.defenseFactor;
            return (int) (new int[]{3, 6, 8, 3}[slotIn.getIndex()] * factor);
        }

        public int getEnchantmentValue() {
            return 15;
        }

        public Ingredient getRepairIngredient() {
            return Ingredient.of(ERItems.END_CRYSTAL_INGOT.get());
        }

        public float getToughness() {
            return ERConfig.getData().END_CRYSTAL_GEAR.ARMOR.toughness;
        }

        public float getKnockbackResistance() {
            return ERConfig.getData().END_CRYSTAL_GEAR.ARMOR.knockbackResistance;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_GENERIC;
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public String getName() {
            return "end_crystal_fragment";
        }
    }
}

package com.teamremastered.endrem.items;

import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.registers.ERItems;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.List;

public class EndCrystalArmor extends ArmorItem {
    public EndCrystalArmor(EquipmentSlotType slot) {
        super(new EndCrystalArmorMaterial(), slot, new Item.Properties().tab(EndRemastered.TAB).rarity(Rarity.UNCOMMON));
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        int layer = slot == EquipmentSlotType.LEGS ? 2 : 1;

        if ("overlay".equals(type))
            return EndRemastered.MOD_ID + ":textures/models/armor/all_layer_" + layer + "_overlay.png";

        return EndRemastered.MOD_ID + ":textures/models/armor/end_crystal_layer_" + layer + ".png";
    }

    @ParametersAreNullableByDefault
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, World level, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        assert tooltip != null;
        tooltip.add(new TranslationTextComponent("item.endrem.armor.description.main"));
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return Boolean.parseBoolean(ERConfig.END_CRYSTAL_ARMOR_STATS.getList().get(4));
    }

    @MethodsReturnNonnullByDefault
    private static class EndCrystalArmorMaterial implements IArmorMaterial {

        public int getDurabilityForSlot(EquipmentSlotType slotIn) {
            double factor = Double.parseDouble(ERConfig.END_CRYSTAL_ARMOR_STATS.getList().get(0));
            return (int) (new int[]{13, 15, 16, 11}[slotIn.getIndex()] * factor);
        }

        public int getDefenseForSlot(EquipmentSlotType slotIn) {
            double factor = Double.parseDouble(ERConfig.END_CRYSTAL_ARMOR_STATS.getList().get(1));
            return (int) (new int[]{3, 6, 8, 3}[slotIn.getIndex()] * factor);
        }

        public int getEnchantmentValue() {
            return 15;
        }

        public Ingredient getRepairIngredient() {
            return Ingredient.of(ERItems.END_CRYSTAL_INGOT.get());
        }

        public float getToughness() {
            return Float.parseFloat(ERConfig.END_CRYSTAL_ARMOR_STATS.getList().get(2));
        }

        public float getKnockbackResistance() {
            return Float.parseFloat(ERConfig.END_CRYSTAL_ARMOR_STATS.getList().get(3));
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

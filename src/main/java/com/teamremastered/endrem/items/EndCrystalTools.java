package com.teamremastered.endrem.items;

import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.registers.ERItems;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;

public class EndCrystalTools {
    private final static IItemTier END_CRYSTAL_MATERIAL = new EndCrystalMaterial();
    private final static Item.Properties BASE_ITEM_PROPERTIES = new Item.Properties().tab(EndRemastered.TAB);

    public static class Sword extends SwordItem {
        public Sword() {
            super(END_CRYSTAL_MATERIAL, 3, -2.4F, BASE_ITEM_PROPERTIES);
        }
    }

    public static class Shovel extends ShovelItem {
        public Shovel() {
            super(END_CRYSTAL_MATERIAL, 1.5F, -3.0F, BASE_ITEM_PROPERTIES);
        }
    }

    public static class Pickaxe extends PickaxeItem {
        public Pickaxe() {
            super(END_CRYSTAL_MATERIAL, 1, -2.8F, BASE_ITEM_PROPERTIES);
        }
    }

    public static class Axe extends AxeItem {
        public Axe() {
            super(END_CRYSTAL_MATERIAL, 5.0F, -3.0F, BASE_ITEM_PROPERTIES);
        }
    }

    public static class Hoe extends HoeItem {
        public Hoe() {
            super(END_CRYSTAL_MATERIAL, -3, 0.0F, BASE_ITEM_PROPERTIES);
        }
    }

    public static class EndCrystalPickaxe extends PickaxeItem {
        public EndCrystalPickaxe() {
            super(END_CRYSTAL_MATERIAL, 1, -2.8F, (new Item.Properties()).tab(EndRemastered.TAB));
        }
    }

    @MethodsReturnNonnullByDefault
    public static class EndCrystalMaterial implements IItemTier {
        public int getUses() {
            return (int) Float.parseFloat(ERConfig.END_CRYSTAL_TOOLS_STATS.getList().get(0));
        }

        public float getSpeed() {
            return Float.parseFloat(ERConfig.END_CRYSTAL_TOOLS_STATS.getList().get(1));
        }

        public float getAttackDamageBonus() {
            return Float.parseFloat(ERConfig.END_CRYSTAL_TOOLS_STATS.getList().get(2));
        }

        public int getLevel() {
            return 4;
        }

        public int getEnchantmentValue() {
            return 10;
        }

        public Ingredient getRepairIngredient() {
            return Ingredient.of(ERItems.END_CRYSTAL_INGOT.get());
        }
    }
}

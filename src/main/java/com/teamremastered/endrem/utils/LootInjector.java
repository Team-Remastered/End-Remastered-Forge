package com.teamremastered.endrem.utils;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class LootInjector {
    public static class LootInjectionModifier extends LootModifier {
        private final ResourceLocation table;

        protected LootInjectionModifier(ILootCondition[] conditionsIn, ResourceLocation tableIn) {
            super(conditionsIn);
            table = tableIn;
        }

        @Override
        @Nonnull
        protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
            LootContext.Builder builder = (new LootContext.Builder(context.getLevel()).withRandom(context.getRandom()));
            LootTable lootTable = context.getLevel().getServer().getLootTables().get(table);
            generatedLoot.addAll(lootTable.getRandomItems(builder.create(LootParameterSets.EMPTY)));
            return generatedLoot;
        }
    }

    public static class Serializer extends GlobalLootModifierSerializer<LootInjectionModifier> {
        @Override
        public LootInjectionModifier read(ResourceLocation location, JsonObject object, ILootCondition[] aiLootCondition) {
            return new LootInjectionModifier(aiLootCondition, new ResourceLocation(JSONUtils.getAsString(object, "injection")));
        }
        @Override
        public JsonObject write(LootInjectionModifier instance) {
            return null;
        }
    }
}

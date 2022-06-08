package com.teamremastered.endrem.utils;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class LootInjector {
    public static class LootInjectionModifier extends LootModifier {
        private final ResourceLocation table;

        protected LootInjectionModifier(LootItemCondition[] conditionsIn, ResourceLocation tableIn) {
            super(conditionsIn);
            table = tableIn;
        }

        @Override
        @Nonnull
        protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
            LootContext.Builder builder = (new LootContext.Builder(context.getLevel()).withRandom(context.getRandom()));
            LootTable lootTable = context.getLevel().getServer().getLootTables().get(table);
            generatedLoot.addAll(lootTable.getRandomItems(builder.create(LootContextParamSets.EMPTY)));
            return generatedLoot;
        }
    }

    public static class Serializer extends GlobalLootModifierSerializer<LootInjectionModifier> {
        @Override
        public LootInjectionModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] aiLootCondition) {
            return new LootInjectionModifier(aiLootCondition, new ResourceLocation(GsonHelper.getAsString(object, "injection")));
        }
        @Override
        public JsonObject write(LootInjectionModifier instance) {
            return null;
        }
    }
}

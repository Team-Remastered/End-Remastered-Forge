package com.teamremastered.endrem.utils;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamremastered.endrem.EndRemastered;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import java.util.function.Supplier;

@Mod(EndRemastered.MOD_ID)
public class LootInjector {

    @Mod.EventBusSubscriber(modid = EndRemastered.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class EventHandlers {
        @SubscribeEvent
        public static void runData(GatherDataEvent event)
        {
                event.getGenerator().addProvider(event.includeServer(), new DataProvider(event.getGenerator(), EndRemastered.MOD_ID));
        }
    }

    private static class DataProvider extends GlobalLootModifierProvider
    {
        public DataProvider(DataGenerator gen, String modid)
        {
            super(gen, modid);
        }

        @Override
        protected void start()
        {

            add("desert_pyramid", new LootInjectorModifier(
                    new LootItemCondition[] { LootTableIdCondition.builder(new ResourceLocation("chests/desert_pyramid")).build() },
                    new ResourceLocation(EndRemastered.MOD_ID, "minecraft/chests/desert_pyramid"),
                    true)
            );

            add("jungle_temple", new LootInjectorModifier(
                    new LootItemCondition[] { LootTableIdCondition.builder(new ResourceLocation("chests/jungle_temple")).build() },
                    new ResourceLocation(EndRemastered.MOD_ID, "minecraft/chests/jungle_temple"),
                    true)
            );
        }
    }

    public static class LootInjectorModifier extends LootModifier {
        public static final Supplier<Codec<LootInjectorModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(
                inst.group(
                ResourceLocation.CODEC.fieldOf("loot_table_id").forGetter(m -> m.table),
                Codec.BOOL.fieldOf("allow_recursive_glms").forGetter(m -> m.allowRecursiveGlms)
                )).apply(inst, LootInjectorModifier::new)));

        private final boolean allowRecursiveGlms;
        private final ResourceLocation table;
        public LootInjectorModifier(LootItemCondition[] conditionsIn, ResourceLocation tableIn, boolean recursiveGlms) {
            super(conditionsIn);
            this.table = tableIn;
            this.allowRecursiveGlms = recursiveGlms;
        }

        @Override
        protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
            LootContext.Builder builder = (new LootContext.Builder(context.getLevel()).withRandom(context.getRandom()));
            LootTable loottable = context.getLevel().getServer().getLootTables().get(table);
            generatedLoot.addAll(loottable.getRandomItems(builder.create(LootContextParamSets.EMPTY)));
            return generatedLoot;
        }

        @Override
        public Codec<? extends IGlobalLootModifier> codec() {
            return CODEC.get();
        }
    }
}
package com.teamremastered.endrem.registers;

import com.mojang.serialization.Codec;
import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.utils.LootInjector;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = EndRemastered.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterHandler {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLMS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, EndRemastered.MOD_ID);

    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ERBlocks.initRegister(modEventBus);
        ERItems.initRegister(modEventBus);
        GLMS.register(modEventBus);
    }

    private static final RegistryObject<Codec<LootInjector.LootInjectorModifier>> LOOT_INJECTION = GLMS.register("loot_injection", LootInjector.LootInjectorModifier.CODEC);

    @SubscribeEvent
    public static void registerTabs(CreativeModeTabEvent.Register event) {
        System.out.println("Registered");
        event.registerCreativeModeTab(new ResourceLocation(EndRemastered.MOD_ID, "endrem_tab"), builder -> builder
                .icon(() -> new ItemStack(ERItems.EXOTIC_EYE.get()))
                .title(Component.translatable("itemGroup.endrem.endrem_tab"))
                .displayItems((featureFlags, output, hasOp) -> {
                    output.accept(ERItems.BLACK_EYE.get());
                    output.accept(ERItems.COLD_EYE.get());
                    output.accept(ERItems.CORRUPTED_EYE.get());
                    output.accept(ERItems.LOST_EYE.get());
                    output.accept(ERItems.NETHER_EYE.get());
                    output.accept(ERItems.OLD_EYE.get());
                    output.accept(ERItems.ROGUE_EYE.get());
                    output.accept(ERItems.CURSED_EYE.get());
                    output.accept(ERItems.EVIL_EYE.get());

                    output.accept(ERItems.GUARDIAN_EYE.get());
                    output.accept(ERItems.MAGICAL_EYE.get());
                    output.accept(ERItems.WITHER_EYE.get());

                    output.accept(ERItems.WITCH_EYE.get());
                    output.accept(ERItems.UNDEAD_EYE.get());
                    output.accept(ERItems.EXOTIC_EYE.get());

                    output.accept(ERItems.CRYPTIC_EYE.get());

                    output.accept(ERItems.WITCH_PUPIL.get());
                    output.accept(ERItems.UNDEAD_SOUL.get());
                    System.out.println("Items Registered");
                })
        );
    }
}

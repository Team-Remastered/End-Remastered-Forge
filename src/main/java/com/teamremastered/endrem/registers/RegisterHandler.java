package com.teamremastered.endrem.registers;

import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.utils.LootInjector;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class RegisterHandler {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLMS = DeferredRegister.createOptional(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, EndRemastered.MOD_ID);

    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ERBlocks.initRegister(modEventBus);
        ERItems.initRegister(modEventBus);
        GLMS.register(modEventBus);
    }

    public static RegistryObject<GlobalLootModifierSerializer<LootInjector.LootInjectionModifier>> LOOT_INJECTOR = GLMS.register("loot_injection", LootInjector.Serializer::new);
}

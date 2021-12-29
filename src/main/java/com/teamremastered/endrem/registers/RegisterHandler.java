package com.teamremastered.endrem.registers;

import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.utils.LootInjector;
import com.teamremastered.endrem.utils.MultiLocator;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
public class RegisterHandler {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLMS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, EndRemastered.MOD_ID);

    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ERBlocks.initRegister(modEventBus);
        ERItems.initRegister(modEventBus);
        GLMS.register(modEventBus);
    }

    public final static MultiLocator EYE_ML = new MultiLocator(() -> ERConfig.EYE_STRUCTURE_LIST.getList());
    public final static MultiLocator MAP_ML = new MultiLocator(() -> ERConfig.MAP_STRUCTURE_LIST.getList());

    public static RegistryObject<GlobalLootModifierSerializer<LootInjector.LootInjectionModifier>> LOOT_INJECTOR = GLMS.register("loot_injection", LootInjector.Serializer::new);

}

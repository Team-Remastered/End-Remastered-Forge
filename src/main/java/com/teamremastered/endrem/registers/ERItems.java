package com.teamremastered.endrem.registers;

import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.items.EREnderEye;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ERItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EndRemastered.MOD_ID);

    public static void initRegister() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modEventBus);
    }

    // ==== Eyes ====
    
    // entities
    public static final RegistryObject<Item> MAGICAL_EYE = ITEMS.register("magical_eye", EREnderEye::new);
    public static final RegistryObject<Item> WITHER_EYE = ITEMS.register("wither_eye", EREnderEye::new);
    public static final RegistryObject<Item> GUARDIAN_EYE = ITEMS.register("guardian_eye", EREnderEye::new);
    
    // chests
    public static final RegistryObject<Item> OLD_EYE = ITEMS.register("old_eye", EREnderEye::new);
    public static final RegistryObject<Item> ROGUE_EYE = ITEMS.register("rogue_eye", EREnderEye::new);
    public static final RegistryObject<Item> NETHER_EYE = ITEMS.register("nether_eye", EREnderEye::new);
    public static final RegistryObject<Item> COLD_EYE = ITEMS.register("cold_eye", EREnderEye::new);
    public static final RegistryObject<Item> CORRUPTED_EYE = ITEMS.register("corrupted_eye", EREnderEye::new);
    public static final RegistryObject<Item> BLACK_EYE = ITEMS.register("black_eye", EREnderEye::new);
    public static final RegistryObject<Item> LOST_EYE = ITEMS.register("lost_eye", EREnderEye::new);

    // crafts
    public static final RegistryObject<Item> END_CRYSTAL_EYE = ITEMS.register("end_crystal_eye", EREnderEye::new);
    public static final RegistryObject<Item> WITCH_EYE = ITEMS.register("witch_eye", EREnderEye::new);
}

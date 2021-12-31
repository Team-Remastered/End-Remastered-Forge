package com.teamremastered.endrem.registers;

import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.items.EREnderEye;
import com.teamremastered.endrem.items.EndCrystalArmor;
import com.teamremastered.endrem.items.EndCrystalTools;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class ERItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EndRemastered.MOD_ID);

    public static void initRegister(IEventBus modEventBus) {
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

    // ==== Tools =====
    public static final RegistryObject<Item> END_CRYSTAL_HOE = ITEMS.register("end_crystal_hoe", EndCrystalTools.Hoe::new);
    public static final RegistryObject<Item> END_CRYSTAL_PICKAXE = ITEMS.register("end_crystal_pickaxe", EndCrystalTools.Pickaxe::new);
    public static final RegistryObject<Item> END_CRYSTAL_AXE = ITEMS.register("end_crystal_axe", EndCrystalTools.Axe::new);
    public static final RegistryObject<Item> END_CRYSTAL_SWORD = ITEMS.register("end_crystal_sword", EndCrystalTools.Sword::new);
    public static final RegistryObject<Item> END_CRYSTAL_SHOVEL = ITEMS.register("end_crystal_shovel", EndCrystalTools.Shovel::new);

    // ==== Armor ====
    public static final RegistryObject<Item> END_CRYSTAL_HELMET = ITEMS.register("end_crystal_helmet", () -> new EndCrystalArmor(EquipmentSlot.HEAD));
    public static final RegistryObject<Item> END_CRYSTAL_CHESTPLATE = ITEMS.register("end_crystal_chestplate", () -> new EndCrystalArmor(EquipmentSlot.CHEST));
    public static final RegistryObject<Item> END_CRYSTAL_LEGGINGS = ITEMS.register("end_crystal_leggings", () -> new EndCrystalArmor(EquipmentSlot.LEGS));
    public static final RegistryObject<Item> END_CRYSTAL_BOOTS = ITEMS.register("end_crystal_boots", () -> new EndCrystalArmor(EquipmentSlot.FEET));

    // ==== Other Items ====
    public static final RegistryObject<Item> WITCH_PUPIL = ITEMS.register("witch_pupil", () -> new Item(new Item.Properties().tab(EndRemastered.TAB)));
    public static final RegistryObject<Item> END_CRYSTAL_FRAGMENT = ITEMS.register("end_crystal_fragment", () -> new Item(new Item.Properties().fireResistant().tab(EndRemastered.TAB)));
    public static final RegistryObject<Item> END_CRYSTAL_INGOT = ITEMS.register("end_crystal_ingot", () -> new Item(new Item.Properties().tab(EndRemastered.TAB)));

}

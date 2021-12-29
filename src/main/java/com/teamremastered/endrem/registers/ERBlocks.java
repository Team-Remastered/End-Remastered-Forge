package com.teamremastered.endrem.registers;

import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.blocks.AncientPortalFrame;
import com.teamremastered.endrem.blocks.EndCrystalBlock;
import com.teamremastered.endrem.blocks.EndCrystalOre;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
public class ERBlocks {
    // Deferred Register
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EndRemastered.MOD_ID);
    private static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EndRemastered.MOD_ID);

    private static final Item.Properties BASE_ITEM_PROPERTIES = new Item.Properties().tab(EndRemastered.TAB);

    public static void initRegister(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        BLOCK_ITEMS.register(modEventBus);
    }

    public static final RegistryObject<Block> ANCIENT_PORTAL_FRAME = BLOCKS.register("ancient_portal_frame", AncientPortalFrame::new);
    public static final RegistryObject<Item> ANCIENT_PORTAL_FRAME_ITEM = BLOCK_ITEMS.register("ancient_portal_frame", () -> new BlockItem(ANCIENT_PORTAL_FRAME.get(), BASE_ITEM_PROPERTIES));

    public static final RegistryObject<Block> END_CRYSTAL_ORE = BLOCKS.register("end_crystal_ore", EndCrystalOre::new);
    public static final RegistryObject<Item> END_CRYSTAL_ORE_ITEM = BLOCK_ITEMS.register("end_crystal_ore", () -> new BlockItem(END_CRYSTAL_ORE.get(), BASE_ITEM_PROPERTIES));

    public static final RegistryObject<Block> END_CRYSTAL_BLOCK = BLOCKS.register("end_crystal_block", EndCrystalBlock::new);
    public static final RegistryObject<Item> END_CRYSTAL_BLOCK_ITEM = BLOCK_ITEMS.register("end_crystal_block", () -> new BlockItem(END_CRYSTAL_BLOCK.get(), BASE_ITEM_PROPERTIES));
}

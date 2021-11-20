package com.teamremastered.endrem;

import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.registers.ERBlocks;
import com.teamremastered.endrem.registers.RegisterHandler;
import com.teamremastered.endrem.world.gen.OreGenHandler;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod(EndRemastered.MOD_ID)
public class EndRemastered {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "endrem";
    public static final String CONFIG_FILE = String.format("%s.toml", EndRemastered.MOD_ID);

    public EndRemastered() {
        MinecraftForge.EVENT_BUS.register(this);
        ERConfig.load();
        RegisterHandler.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
//        StructureGenerator.init();
    }

    public static Tag<Block> END_CRYSTAL_GEN;

    private void setup(final FMLCommonSetupEvent event) {
        END_CRYSTAL_GEN = BlockTags.bind("endrem:end_crystal_gen");
        OreGenHandler.initRegister();
//        event.enqueueWork(StructureGenerator::setup);
    }

    public static final CreativeModeTab TAB = new CreativeModeTab("endremTab") {
        @Override
        @Nonnull
        public ItemStack makeIcon() {
            return new ItemStack(ERBlocks.ANCIENT_PORTAL_FRAME_ITEM.get());
        }
    };
}
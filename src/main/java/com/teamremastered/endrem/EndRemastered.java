package com.teamremastered.endrem;

import com.teamremastered.endrem.config.ERConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(EndRemastered.MOD_ID)
public class EndRemastered {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "endrem";
    public static final String MOD_NAME = "End Remastered";
    public static final String CONFIG_FILE = String.format("%s.toml", EndRemastered.MOD_ID);

    public EndRemastered() {
        MinecraftForge.EVENT_BUS.register(this);
        ERConfig.load();
//        RegistryHandler.init();
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
//        StructureGenerator.init();
    }

//    public static Tag<Block> END_CRYSTAL_GEN;
//    private void setup(final FMLCommonSetupEvent event) {
//        END_CRYSTAL_GEN = BlockTags.bind("endrem:end_crystal_gen");
//        OreSpawnHandler.registerOres();
//        event.enqueueWork(StructureGenerator::setup);
//    }

//    public static final CreativeModeTab TAB = new CreativeModeTab("endremTab") {
//        @Override
//        public ItemStack makeIcon() {
//            return new ItemStack(RegistryHandler.ROGUE_EYE.get());
//        }
//    };
}
package com.teamremastered.endrem;

import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.registers.RegisterHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(EndRemastered.MOD_ID)
public class EndRemastered {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "endrem";
    public static final String CONFIG_FILE = String.format("%s.toml", EndRemastered.MOD_ID);

    public EndRemastered() {
        MinecraftForge.EVENT_BUS.register(this);
        ERConfig.load();
        RegisterHandler.init();
    }
}
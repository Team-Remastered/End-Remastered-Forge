package com.teamremastered.endrem.registers;

import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.utils.MultiLocator;

public class RegisterHandler {
    public static void init() {
        ERBlocks.initRegister();
        ERItems.initRegister();
    }

    public final static MultiLocator EYE_ML = new MultiLocator(() -> ERConfig.EYE_STRUCTURE_LIST.getList());
    public final static MultiLocator MAP_ML = new MultiLocator(() -> ERConfig.MAP_STRUCTURE_LIST.getList());
}

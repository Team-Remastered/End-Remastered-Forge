package com.teamremastered.endrem.utils;

import net.minecraft.core.BlockPos;

public class ERUtils {
    public static int getDistance(BlockPos pos1, BlockPos pos2) {
        return (int) Math.sqrt(Math.pow((pos2.getX() - pos1.getX()), 2) + Math.pow((pos2.getZ() - pos1.getZ()), 2));
    }
}

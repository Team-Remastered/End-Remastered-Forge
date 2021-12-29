package com.teamremastered.endrem.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class ERUtils {
    public static int getBlockDistance(BlockPos pos1, BlockPos pos2) {
        return (int) Math.sqrt(Math.pow((pos2.getX() - pos1.getX()), 2) + Math.pow((pos2.getZ() - pos1.getZ()), 2));
    }

    public static int getChunkDistance(ChunkPos pos1, ChunkPos pos2) {
        return (int) Math.sqrt(Math.pow(pos2.x - pos1.x, 2) + Math.pow(pos2.z - pos1.z, 2));
    }

    public static int getChunkDistanceFromSpawn(ChunkPos pos) {
        return getChunkDistance(pos, new ChunkPos(0,0));
    }
}

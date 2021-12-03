package com.teamremastered.endrem.world.structures;


import com.google.common.collect.ImmutableMap;
import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.world.structures.config.ERStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EndCastlePieces {

    private static final int height = 0;
    private static final ResourceLocation BOTTOM_LEFT = new ResourceLocation(EndRemastered.MOD_ID, "end_castle/castle_bl");
    private static final ResourceLocation MID_LEFT = new ResourceLocation(EndRemastered.MOD_ID, "end_castle/castle_ml");
    private static final ResourceLocation TOP_LEFT = new ResourceLocation(EndRemastered.MOD_ID, "end_castle/castle_tl");
    private static final ResourceLocation BOTTOM_MID = new ResourceLocation(EndRemastered.MOD_ID, "end_castle/castle_bm");
    private static final ResourceLocation BOTTOM_RIGHT = new ResourceLocation(EndRemastered.MOD_ID, "end_castle/castle_br");
    public static final ResourceLocation MID_RIGHT = new ResourceLocation(EndRemastered.MOD_ID, "end_castle/castle_mr");
    public static final ResourceLocation TOP_RIGHT = new ResourceLocation(EndRemastered.MOD_ID, "end_castle/castle_tr");
    public static final ResourceLocation TOP_MID = new ResourceLocation(EndRemastered.MOD_ID, "end_castle/castle_tm");
    public static final ResourceLocation MID_MID = new ResourceLocation(EndRemastered.MOD_ID, "end_castle/castle_mm");

    private static final Map<ResourceLocation, BlockPos> OFFSET = new ImmutableMap.Builder<ResourceLocation, BlockPos>()
            .put(BOTTOM_LEFT, new BlockPos(20, height, 24))
            .put(MID_LEFT, new BlockPos(-25, height, 24))
            .put(TOP_LEFT, new BlockPos(-48, height, 24))
            .put(BOTTOM_RIGHT, new BlockPos(20, height, -40))
            .put(BOTTOM_MID, new BlockPos(41, height, 0))
            .put(MID_RIGHT, new BlockPos(-24, height, -47))
            .put(TOP_RIGHT, new BlockPos(-48, height, -40))
            .put(TOP_MID, new BlockPos(-48, height, 0))
            .put(MID_MID, new BlockPos(0, height, 0))
            .build();

    public static void start(StructureManager manager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList) {
        for (Map.Entry<ResourceLocation, BlockPos> entry : OFFSET.entrySet()) {
            pieceList.add(new Piece(manager, entry.getKey(), entry.getValue().rotate(rotation).offset(pos.getX(), pos.getY(), pos.getZ()), rotation));
        }
    }

    public static class Piece extends TemplateStructurePiece {
        public Piece(StructureManager manager, ResourceLocation resourceLocationIn, BlockPos pos, Rotation rotationIn) {
            super(ERStructures.EC, 0, manager, resourceLocationIn, resourceLocationIn.toString(), makeSettings(rotationIn, resourceLocationIn), pos);

        }

        public Piece(ServerLevel serverLevel, CompoundTag tagCompound) {
            super(ERStructures.EC, tagCompound, serverLevel, (p_162451_) ->
                    makeSettings(Rotation.valueOf(tagCompound.getString("Rot")), p_162451_)
            );
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation, ResourceLocation resourceLocation) {
            return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE);
        }

        @Override
        @ParametersAreNonnullByDefault
        protected void addAdditionalSaveData(ServerLevel serverLevel, CompoundTag tagCompound) {
            super.addAdditionalSaveData(serverLevel, tagCompound);
            tagCompound.putString("Rot", this.placeSettings.getRotation().name());  // or make rotation public
        }

        @Override
        @ParametersAreNonnullByDefault
        protected void handleDataMarker(String chest, BlockPos pos, ServerLevelAccessor worldIn, Random rand, BoundingBox sbb) {
            ResourceLocation lootTable = new ResourceLocation(EndRemastered.MOD_ID, String.format("chests/%s", chest));
            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            RandomizableContainerBlockEntity.setLootTable(worldIn, rand, pos.below(), lootTable);
        }
    }
}

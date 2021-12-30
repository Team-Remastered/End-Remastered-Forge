package com.teamremastered.endrem.world.structures;


import com.google.common.collect.ImmutableMap;
import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.world.structures.config.ERStructures;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

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
            .put(TOP_LEFT, new BlockPos(-48, height, 24))
            .put(MID_LEFT, new BlockPos(-25, height, 24))
            .put(BOTTOM_RIGHT, new BlockPos(20, height, -40))
            .put(TOP_RIGHT, new BlockPos(-48, height, -40))
            .put(MID_RIGHT, new BlockPos(-24, height, -47))
            .put(BOTTOM_MID, new BlockPos(41, height, 0))
            .put(TOP_MID, new BlockPos(-48, height, 0))
            .put(MID_MID, new BlockPos(0, height, 0))
            .build();

    public static void start(TemplateManager manager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList) {
        for (Map.Entry<ResourceLocation, BlockPos> entry : OFFSET.entrySet()) {
            pieceList.add(new Piece(manager, entry.getKey(), entry.getValue().rotate(rotation).offset(pos.getX(), pos.getY(), pos.getZ()), rotation));
        }
    }

    public static class Piece extends TemplateStructurePiece {
        private final ResourceLocation resourceLocation;
        private final Rotation rotation;
        public Piece(TemplateManager manager, ResourceLocation resourceLocationIn, BlockPos pos, Rotation rotationIn) {
            super(ERStructures.EC, 0);
            this.resourceLocation = resourceLocationIn;
            this.templatePosition = pos;
            this.rotation = rotationIn;
            this.setupPiece(manager);
        }

        public Piece(TemplateManager manager, CompoundNBT tagCompound) {
            super(ERStructures.EC, tagCompound);
            this.resourceLocation = new ResourceLocation(tagCompound.getString("Template"));
            this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
            this.setupPiece(manager);
        }

        private void setupPiece(TemplateManager manager) {
            Template template = manager.getOrCreate(this.resourceLocation);
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        @ParametersAreNonnullByDefault
        protected void addAdditionalSaveData(CompoundNBT tagCompound) {
            super.addAdditionalSaveData(tagCompound);
            tagCompound.putString("Template", this.resourceLocation.toString());
            tagCompound.putString("Rot", this.rotation.name());  // or make rotation public
        }

        @Override
        @ParametersAreNonnullByDefault
        protected void handleDataMarker(String chest, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {
            ResourceLocation lootTable = new ResourceLocation(EndRemastered.MOD_ID, String.format("chests/%s", chest));
            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(), lootTable);
        }
    }
}

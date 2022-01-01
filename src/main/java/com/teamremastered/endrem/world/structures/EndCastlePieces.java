package com.teamremastered.endrem.world.structures;


import com.google.common.collect.ImmutableMap;
import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.world.structures.config.ERStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import javax.annotation.ParametersAreNonnullByDefault;
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

    private static final Map<ResourceLocation, BlockPos> OFFSET = ImmutableMap.of(
            BOTTOM_LEFT, new BlockPos(20, height, 24),
            MID_LEFT, new BlockPos(-25, height, 24),
            TOP_LEFT, new BlockPos(-48, height, 24),
            BOTTOM_RIGHT, new BlockPos(20, height, -40),
            BOTTOM_MID, new BlockPos(41, height, 0),
            MID_RIGHT, new BlockPos(-24, height, -47),
            TOP_RIGHT, new BlockPos(-48, height, -40),
            TOP_MID, new BlockPos(-48, height, 0),
            MID_MID, new BlockPos(0, height, 0)
    );

    public static void addPieces(StructureManager structureManager, BlockPos blockPos, Rotation rotation, StructurePieceAccessor accessor, Random random) {
        for (Map.Entry<ResourceLocation, BlockPos> entry : OFFSET.entrySet()) {
            accessor.addPiece(new EndCastlePiece(structureManager, entry.getKey(), entry.getValue().rotate(rotation).offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()), rotation));
        }
    }

    public static class EndCastlePiece extends TemplateStructurePiece {
        public EndCastlePiece(StructureManager structureManager, ResourceLocation resourceLocation, BlockPos blockPos, Rotation rotation) {
            super(ERStructures.EndCastlePieceType, 0, structureManager, resourceLocation, resourceLocation.toString(), makeSettings(rotation), blockPos);
        }

        public EndCastlePiece(StructurePieceSerializationContext context, CompoundTag compoundTag) {
            super(ERStructures.EndCastlePieceType, compoundTag, context.structureManager(), (rl) ->
                    makeSettings(Rotation.valueOf(compoundTag.getString("Rot")))
            );
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation) {
            return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE);
        }

        @ParametersAreNonnullByDefault
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound) {
            super.addAdditionalSaveData(context, tagCompound);
            tagCompound.putString("Rot", this.placeSettings.getRotation().name());
        }

        @ParametersAreNonnullByDefault
        protected void handleDataMarker(String chest, BlockPos pos, ServerLevelAccessor worldIn, Random rand, BoundingBox sbb) {
            ResourceLocation lootTable = new ResourceLocation(EndRemastered.MOD_ID, String.format("chests/%s", chest));
            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            RandomizableContainerBlockEntity.setLootTable(worldIn, rand, pos.below(), lootTable);
        }
    }
}

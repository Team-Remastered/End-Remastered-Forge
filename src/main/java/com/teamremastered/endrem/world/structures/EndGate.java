package com.teamremastered.endrem.world.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.utils.ERUtils;
import com.teamremastered.endrem.world.structures.config.ERStructures;
import com.teamremastered.endrem.world.structures.utils.CustomMonsterSpawn;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

import static net.minecraft.world.entity.MobCategory.MONSTER;


public class EndGate extends StructureFeature<JigsawConfiguration> {
    private static final ResourceLocation START_POOL = new ResourceLocation(EndRemastered.MOD_ID, "end_gate/start_pool");
    private static final List<CustomMonsterSpawn> MONSTER_SPAWN_LIST =  ImmutableList.of(
            new CustomMonsterSpawn(EntityType.SKELETON, 30, 30, 35),
            new CustomMonsterSpawn(EntityType.ZOMBIE, 20, 25, 30),
            new CustomMonsterSpawn(EntityType.CAVE_SPIDER, 20, 25, 30),
            new CustomMonsterSpawn(EntityType.WITCH, 10, 10, 15)
    );

    public EndGate(Codec<JigsawConfiguration> codec) {
        super(codec, EndGate::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.@NotNull Decoration step() {
        return GenerationStep.Decoration.STRONGHOLDS;
    }

    private BlockPos getLocatedRoom(StructureStart<?> structureStart) {
        List<StructurePiece> structurePieces = structureStart.getPieces();
        BoundingBox bbox = structurePieces.get(Math.min(16, structurePieces.size()) - 1).getBoundingBox();
        return new BlockPos(bbox.minX(), bbox.minY(), bbox.minZ());
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public BlockPos getNearestGeneratedFeature(LevelReader levelReader, StructureFeatureManager manager, BlockPos blockPos, int p_67050_, boolean p_67051_, long p_67052_, StructureFeatureConfiguration config) {
        int i = config.spacing();
        int j = SectionPos.blockToSectionCoord(blockPos.getX());
        int k = SectionPos.blockToSectionCoord(blockPos.getZ());

        for(int l = 0; l <= p_67050_; ++l) {
            for(int i1 = -l; i1 <= l; ++i1) {
                boolean flag = i1 == -l || i1 == l;

                for(int j1 = -l; j1 <= l; ++j1) {
                    boolean flag1 = j1 == -l || j1 == l;
                    if (flag || flag1) {
                        int k1 = j + i * i1;
                        int l1 = k + i * j1;
                        ChunkPos chunkpos = this.getPotentialFeatureChunk(config, p_67052_, k1, l1);
                        StructureCheckResult structurecheckresult = manager.checkStructurePresence(chunkpos, this, p_67051_);
                        if (structurecheckresult != StructureCheckResult.START_NOT_PRESENT) {
                            if (!p_67051_ && structurecheckresult == StructureCheckResult.START_PRESENT) {
                                return this.getLocatedRoom(levelReader.getChunk(chunkpos.x, chunkpos.z, ChunkStatus.STRUCTURE_STARTS).getStartForFeature(this));
                            }

                            ChunkAccess chunkaccess = levelReader.getChunk(chunkpos.x, chunkpos.z, ChunkStatus.STRUCTURE_STARTS);
                            StructureStart<?> structurestart = manager.getStartForFeature(SectionPos.bottomOf(chunkaccess), this, chunkaccess);
                            if (structurestart != null && structurestart.isValid()) {
                                if (p_67051_ && structurestart.canBeReferenced()) {
                                    manager.addReference(structurestart);
                                    return this.getLocatedRoom(structurestart);
                                }

                                if (!p_67051_) {
                                    return this.getLocatedRoom(structurestart);
                                }
                            }

                            if (l == 0) {
                                break;
                            }
                        }
                    }
                }

                if (l == 0) {
                    break;
                }
            }
        }

        return null;
    }


    public static void setupStructureSpawns(final StructureSpawnListGatherEvent event) {
        if(event.getStructure() == ERStructures.END_GATE.get()) {
            for (CustomMonsterSpawn monsterSpawn : MONSTER_SPAWN_LIST) {
                event.addEntitySpawn(MONSTER, monsterSpawn.getIndividualMobSpawnInfo());
            }
        }
    }

    private static boolean isFeatureChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        return ERUtils.getChunkDistanceFromSpawn(context.chunkPos()) >= ERConfig.END_GATE_SPAWN_DISTANCE.getRaw();
    }

    public static @NotNull Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        if (!EndGate.isFeatureChunk(context)) {
            return Optional.empty();
        }

        JigsawConfiguration newConfig = new JigsawConfiguration(
                () -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                        .get(EndGate.START_POOL),
                ERConfig.END_GATE_SIZE.getRaw()
        );

        PieceGeneratorSupplier.Context<JigsawConfiguration> newContext = new PieceGeneratorSupplier.Context<>(
                context.chunkGenerator(),
                context.biomeSource(),
                context.seed(),
                context.chunkPos(),
                newConfig,
                context.heightAccessor(),
                context.validBiome(),
                context.structureManager(),
                context.registryAccess()
        );

        BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(0).atY(ERConfig.END_GATE_HEIGHT.getRaw());

        return JigsawPlacement.addPieces(
                newContext,
                PoolElementStructurePiece::new,
                blockpos,
                false,
                false
        );
    }
}

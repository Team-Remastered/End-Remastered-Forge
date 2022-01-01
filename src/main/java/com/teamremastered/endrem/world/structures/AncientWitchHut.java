package com.teamremastered.endrem.world.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.world.structures.config.ERStructures;
import com.teamremastered.endrem.world.structures.utils.CustomMonsterSpawn;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.NoiseAffectingStructureStart;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;


public class AncientWitchHut extends StructureFeature<NoneFeatureConfiguration> {
    private static final ResourceLocation START_POOL = new ResourceLocation(EndRemastered.MOD_ID, "ancient_witch_hut/start_pool");;
    private static final List<CustomMonsterSpawn> MONSTER_SPAWN_LIST =  ImmutableList.of(
            new CustomMonsterSpawn(EntityType.SKELETON, 5, 5, 10),
            new CustomMonsterSpawn(EntityType.WITCH, 10, 10, 15)
    );

    public AncientWitchHut(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public @Nonnull GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public static List<Biome.BiomeCategory> getValidBiomeCategories() {
        return ImmutableList.of(
                Biome.BiomeCategory.SWAMP
        );
    }

    public static void setupStructureSpawns(final StructureSpawnListGatherEvent event) {
        if(event.getStructure() == ERStructures.ANCIENT_WITCH_HUT.get()) {
            for (CustomMonsterSpawn monsterSpawn : MONSTER_SPAWN_LIST) {
                event.addEntitySpawn(MobCategory.MONSTER, monsterSpawn.getIndividualMobSpawnInfo());
            }
        }
    }

    @Override
    public @Nonnull StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return Start::new;
    }

    public static class Start extends NoiseAffectingStructureStart<NoneFeatureConfiguration> {
        public Start(StructureFeature<NoneFeatureConfiguration> structureIn, ChunkPos chunkPos, int referenceIn, long seedIn) {
            super(structureIn, chunkPos, referenceIn, seedIn);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void generatePieces(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, StructureManager manager, ChunkPos chunkPos, Biome biomeIn, NoneFeatureConfiguration config, LevelHeightAccessor levelHeightAccessor) {
            BlockPos genPosition = new BlockPos(chunkPos.x << 4, -3, chunkPos.z << 4);

            JigsawPlacement.addPieces(
                    registryAccess,
                    new JigsawConfiguration(() -> registryAccess.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(START_POOL),
                            2),
                    PoolElementStructurePiece::new,
                    chunkGenerator,
                    manager,
                    genPosition,
                    this,
                    this.random,
                    false,
                    true,
                    levelHeightAccessor
            );
            this.getBoundingBox();
        }
    }
}

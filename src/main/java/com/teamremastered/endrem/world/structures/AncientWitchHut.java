package com.teamremastered.endrem.world.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.world.structures.utils.CustomMonsterSpawn;
import com.teamremastered.endrem.world.structures.utils.StructureBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
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
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;


public class AncientWitchHut extends StructureBase {
    private final ResourceLocation START_POOL;
    private final int HEIGHT;

    public AncientWitchHut(Codec<NoneFeatureConfiguration> codec) {
        super(codec,
                // To Set Minimum Distance
                ERConfig.ANCIENT_WITCH_HUT_DISTANCE,

                // List Of Monster Spawns
                ImmutableList.of(
                        new CustomMonsterSpawn(EntityType.SKELETON, 30, 30, 35),
                        new CustomMonsterSpawn(EntityType.WITCH, 10, 10, 15)
                ),

                // Decoration Stage
                GenerationStep.Decoration.SURFACE_STRUCTURES
        );
        this.START_POOL = new ResourceLocation(EndRemastered.MOD_ID, "ancient_witch_hut/start_pool");
        this.HEIGHT = 0;
    }

    public static List<Biome.BiomeCategory> getValidBiomeCategories() {
        return ImmutableList.of(
                Biome.BiomeCategory.SWAMP
        );
    }

    @Override
    public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return Start::new;
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public class Start extends NoiseAffectingStructureStart<NoneFeatureConfiguration> {
        public Start(StructureFeature<NoneFeatureConfiguration> structureIn, ChunkPos chunkPos, int referenceIn, long seedIn) {
            super(structureIn, chunkPos, referenceIn, seedIn);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void generatePieces(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, StructureManager manager, ChunkPos chunkPos, Biome biomeIn, NoneFeatureConfiguration config, LevelHeightAccessor levelHeightAccessor) {
            BlockPos genPosition = new BlockPos(chunkPos.x << 4, HEIGHT, chunkPos.z << 4);

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

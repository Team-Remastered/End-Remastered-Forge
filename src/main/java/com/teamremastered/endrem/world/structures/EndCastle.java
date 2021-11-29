package com.teamremastered.endrem.world.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.world.structures.utils.CustomMonsterSpawn;
import com.teamremastered.endrem.world.structures.utils.StructureBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.NoiseAffectingStructureStart;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.List;

public class EndCastle extends StructureBase {
    public EndCastle(Codec<NoneFeatureConfiguration> codec) {
        super(codec,
                // To Set Minimum Distance
                ERConfig.END_CASTLE_SPAWN_DISTANCE,
                // List Of Monster Spawns
                ImmutableList.of(
                        new CustomMonsterSpawn(EntityType.PILLAGER, 30, 30, 35),
                        new CustomMonsterSpawn(EntityType.VINDICATOR, 20, 25, 30),
                        new CustomMonsterSpawn(EntityType.EVOKER, 20, 10, 15),
                        new CustomMonsterSpawn(EntityType.ILLUSIONER, 5, 5, 10)
                ),
                // Decoration Stage
                GenerationStep.Decoration.SURFACE_STRUCTURES
        );
    }

    public static List<Biome.BiomeCategory> getValidBiomeCategories() {
        return ImmutableList.of(Biome.BiomeCategory.PLAINS,
                Biome.BiomeCategory.JUNGLE,
                Biome.BiomeCategory.TAIGA,
                Biome.BiomeCategory.FOREST);
    }

    @Override
    public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return Start::new;
    }

    public static class Start extends NoiseAffectingStructureStart<NoneFeatureConfiguration> {
        public Start(StructureFeature<NoneFeatureConfiguration> structureIn, ChunkPos chunkPos, int referenceIn, long seedIn) {
            super(structureIn, chunkPos, referenceIn, seedIn);
        }

        @Override
        public void generatePieces(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, StructureManager manager, ChunkPos chunkPos, Biome biomeIn, NoneFeatureConfiguration config, LevelHeightAccessor levelHeightAccessor) {

            Rotation rotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];

            // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkPos.x << 4) + 7;
            int z = (chunkPos.z << 4) + 7;

            // Finds the y value of the terrain at location.
            int surfaceY = chunkGenerator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor);
            BlockPos genPosition = new BlockPos(x, surfaceY, z);
            EndCastlePieces.start(manager, genPosition, rotation,this.pieces);
            this.getBoundingBox();
        }
    }
}

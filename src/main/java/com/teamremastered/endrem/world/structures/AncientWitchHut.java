package com.teamremastered.endrem.world.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.world.structures.config.ERStructures;
import com.teamremastered.endrem.world.structures.utils.CustomMonsterSpawn;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;


public class AncientWitchHut extends Structure<NoFeatureConfig> {
    private static final ResourceLocation START_POOL = new ResourceLocation(EndRemastered.MOD_ID, "ancient_witch_hut/start_pool");
    private static final List<CustomMonsterSpawn> MONSTER_SPAWN_LIST =  ImmutableList.of(
            new CustomMonsterSpawn(EntityType.SKELETON, 5, 5, 10),
            new CustomMonsterSpawn(EntityType.WITCH, 10, 10, 15)
    );

    public AncientWitchHut(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public @Nonnull GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    public static void setupStructureSpawns(final StructureSpawnListGatherEvent event) {
        if(event.getStructure() == ERStructures.ANCIENT_WITCH_HUT.get()) {
            for (CustomMonsterSpawn monsterSpawn : MONSTER_SPAWN_LIST) {
                event.addEntitySpawn(EntityClassification.MONSTER, monsterSpawn.getIndividualMobSpawnInfo());
            }
        }
    }

    public static List<Biome.Category> getValidBiomeCategories() {
        return ImmutableList.of(
                Biome.Category.SWAMP
        );
    }

    @Override
    public @Nonnull IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void generatePieces(DynamicRegistries registryAccess, ChunkGenerator chunkGenerator, TemplateManager manager, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            BlockPos genPosition = new BlockPos(chunkX << 4, -3, chunkZ << 4);

            JigsawManager.addPieces(
                    registryAccess,
                    new VillageConfig(() -> registryAccess.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(START_POOL),
                            2),
                    AbstractVillagePiece::new,
                    chunkGenerator,
                    manager,
                    genPosition,
                    this.pieces,
                    this.random,
                    false,
                    true);
            this.calculateBoundingBox();
        }
    }
}

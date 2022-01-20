package com.teamremastered.endrem.world.structures.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.config.ConfigData;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.world.structures.AncientWitchHut;
import com.teamremastered.endrem.world.structures.EndCastle;
import com.teamremastered.endrem.world.structures.EndCastlePieces;
import com.teamremastered.endrem.world.structures.EndGate;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.ObjectInputFilter;
import java.util.function.Supplier;

public class ERStructures {

    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, EndRemastered.MOD_ID);

    private static <T extends StructureFeature<?>> RegistryObject<T> registerStructure(String name, Supplier<T> structure) {
        return STRUCTURES.register(name, structure);
    }

    /* End Gate */
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> END_GATE = registerStructure("end_gate", () -> (new EndGate(NoneFeatureConfiguration.CODEC)));
    /* Witch Hut */
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> ANCIENT_WITCH_HUT = registerStructure("ancient_witch_hut", () -> (new AncientWitchHut(NoneFeatureConfiguration.CODEC)));
    /* End Castle */
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> END_CASTLE = registerStructure("end_castle", () -> (new EndCastle(NoneFeatureConfiguration.CODEC)));
    public static StructurePieceType EC = EndCastlePieces.Piece::new;

    public static void setupStructures() {
        /*End Castle*/
        setupMapSpacingAndLand(
                END_CASTLE.get(),
                ERConfig.getData().END_CASTLE,
                487192276);

        /* End Gate */
        setupMapSpacingAndLand(
                END_GATE.get(),
                ERConfig.getData().END_GATE,
                959834864);

        /* Witch Hut */
        setupMapSpacingAndLand(
                ANCIENT_WITCH_HUT.get(),
                ERConfig.getData().ANCIENT_WITCH_HUT,
                324897233);
    }

    public static <F extends StructureFeature<?>> void setupMapSpacingAndLand(
            F structure,
            ConfigData.Structure structureConfig,
            int salt) {
        StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if (structureConfig.terraforming) {
            StructureFeature.NOISE_AFFECTING_FEATURES =
                    ImmutableList.<StructureFeature<?>>builder()
                            .addAll(StructureFeature.NOISE_AFFECTING_FEATURES)
                            .add(structure)
                            .build();
        }

        StructureFeatureConfiguration structureFeatureConfiguration =
                new StructureFeatureConfiguration(structureConfig.averageDistance,
                        structureConfig.averageDistance - 30,
                        salt);

        StructureSettings.DEFAULTS =
                ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
                        .putAll(StructureSettings.DEFAULTS)
                        .put(structure, structureFeatureConfiguration)
                        .build();
    }

    static void registerStructurePiece(StructurePieceType structurePiece, ResourceLocation rl) {
        Registry.register(Registry.STRUCTURE_PIECE, rl, structurePiece);
    }

    public static void registerAllPieces() {
        registerStructurePiece(EC, new ResourceLocation(EndRemastered.MOD_ID, "ec"));
    }
}

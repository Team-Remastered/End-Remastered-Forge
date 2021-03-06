package com.teamremastered.endrem.world.structures.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.teamremastered.endrem.EndRemastered;
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
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ERStructures {

    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, EndRemastered.MOD_ID);

    private static <T extends StructureFeature<?>> RegistryObject<T> registerStructure(String name, Supplier<T> structure) {
        return STRUCTURES.register(name, structure);
    }

    /* End Gate */
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> END_GATE = registerStructure("end_gate", () -> (new EndGate(JigsawConfiguration.CODEC)));
    /* Witch Hut */
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> ANCIENT_WITCH_HUT = registerStructure("ancient_witch_hut", () -> (new AncientWitchHut(JigsawConfiguration.CODEC)));
    /* End Castle */
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> END_CASTLE = registerStructure("end_castle", () -> (new EndCastle(NoneFeatureConfiguration.CODEC)));
    public static StructurePieceType EndCastlePieceType = EndCastlePieces.EndCastlePiece::new;

    public static void setupStructures() {
        /*End Castle*/
        setupMapSpacingAndLand(
                END_CASTLE.get(),
                new StructureFeatureConfiguration(
                        (ERConfig.END_CASTLE_DISTANCE.getRaw()),
                        (ERConfig.END_CASTLE_DISTANCE.getRaw() - 30),
                        487192276),
                ERConfig.END_CASTLE_TERRAFORMING.getRaw());

        /* End Gate */
        setupMapSpacingAndLand(
                END_GATE.get(),
                new StructureFeatureConfiguration(
                        (ERConfig.END_GATE_DISTANCE.getRaw()),
                        (ERConfig.END_GATE_DISTANCE.getRaw() - 30),
                        959834864),
                false);

        /* Witch Hut */
        setupMapSpacingAndLand(
                ANCIENT_WITCH_HUT.get(),
                new StructureFeatureConfiguration(
                        (ERConfig.ANCIENT_WITCH_HUT_DISTANCE.getRaw()),
                        (ERConfig.ANCIENT_WITCH_HUT_DISTANCE.getRaw() - 5),
                        324897233),
                false);
    }

    public static <F extends StructureFeature<?>> void setupMapSpacingAndLand(
            F structure,
            StructureFeatureConfiguration structureFeatureConfiguration,
            boolean transformSurroundingLand) {
        StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if (transformSurroundingLand) {
            StructureFeature.NOISE_AFFECTING_FEATURES =
                    ImmutableList.<StructureFeature<?>>builder()
                            .addAll(StructureFeature.NOISE_AFFECTING_FEATURES)
                            .add(structure)
                            .build();
        }

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
        registerStructurePiece(EndCastlePieceType, new ResourceLocation(EndRemastered.MOD_ID, "ec"));
    }

}

package com.teamremastered.endrem.world.structures.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.config.ERConfig;
import com.teamremastered.endrem.world.structures.AncientWitchHut;
import com.teamremastered.endrem.world.structures.EndCastle;
import com.teamremastered.endrem.world.structures.EndCastlePieces;
import com.teamremastered.endrem.world.structures.EndGate;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ERStructures {

    public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, EndRemastered.MOD_ID);

    private static <T extends Structure<?>> RegistryObject<T> registerStructure(String name, Supplier<T> structure) {
        return STRUCTURES.register(name, structure);
    }

    /* End Gate */
    public static final RegistryObject<Structure<NoFeatureConfig>> END_GATE = registerStructure("end_gate", () -> (new EndGate(NoFeatureConfig.CODEC)));
    /* Witch Hut */
    public static final RegistryObject<Structure<NoFeatureConfig>> ANCIENT_WITCH_HUT = registerStructure("ancient_witch_hut", () -> (new AncientWitchHut(NoFeatureConfig.CODEC)));
    /* End Castle */
    public static final RegistryObject<Structure<NoFeatureConfig>> END_CASTLE = registerStructure("end_castle", () -> (new EndCastle(NoFeatureConfig.CODEC)));
    public static IStructurePieceType EC = EndCastlePieces.Piece::new;

    public static void setupStructures() {
        /*End Castle*/
        setupMapSpacingAndLand(
                END_CASTLE.get(),
                new StructureSeparationSettings(
                        (ERConfig.END_CASTLE_DISTANCE.getRaw()),
                        (ERConfig.END_CASTLE_DISTANCE.getRaw() - 30),
                        487192276),
                ERConfig.END_CASTLE_TERRAFORMING.getRaw()); //Transform Surrounding Land

        /* End Gate */
        setupMapSpacingAndLand(
                END_GATE.get(),
                new StructureSeparationSettings(
                        (ERConfig.END_GATE_DISTANCE.getRaw()),
                        (ERConfig.END_GATE_DISTANCE.getRaw() - 30),
                        959834864),
                false);

        /* Witch Hut */
        setupMapSpacingAndLand(
                ANCIENT_WITCH_HUT.get(),
                new StructureSeparationSettings(
                        (ERConfig.ANCIENT_WITCH_HUT_DISTANCE.getRaw()),
                        (ERConfig.ANCIENT_WITCH_HUT_DISTANCE.getRaw() - 5),
                        324897233),
                false);
    }

    public static <F extends Structure<?>> void setupMapSpacingAndLand(
            F structure,
            StructureSeparationSettings structureFeatureConfiguration,
            boolean transformSurroundingLand) {
        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if (transformSurroundingLand) {
            Structure.NOISE_AFFECTING_FEATURES =
                    ImmutableList.<Structure<?>>builder()
                            .addAll(Structure.NOISE_AFFECTING_FEATURES)
                            .add(structure)
                            .build();
        }

        DimensionStructuresSettings.DEFAULTS =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.DEFAULTS)
                        .put(structure, structureFeatureConfiguration)
                        .build();
    }

    static void registerStructurePiece(IStructurePieceType structurePiece, ResourceLocation rl) {
        Registry.register(Registry.STRUCTURE_PIECE, rl, structurePiece);
    }

    public static void registerAllPieces() {
        registerStructurePiece(EC, new ResourceLocation(EndRemastered.MOD_ID, "ec"));
    }
}

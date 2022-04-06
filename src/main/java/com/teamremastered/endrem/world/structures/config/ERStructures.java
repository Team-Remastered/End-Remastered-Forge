package com.teamremastered.endrem.world.structures.config;

import com.teamremastered.endrem.EndRemastered;
import com.teamremastered.endrem.world.structures.AncientWitchHut;
import com.teamremastered.endrem.world.structures.EndCastle;
import com.teamremastered.endrem.world.structures.EndCastlePieces;
import com.teamremastered.endrem.world.structures.EndGate;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ERStructures {

    public static final DeferredRegister<StructureFeature<?>> DEFERRED_REGISTRY_STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, EndRemastered.MOD_ID);

    private static <T extends StructureFeature<?>> RegistryObject<T> registerStructure(String name, Supplier<T> structure) {
        return DEFERRED_REGISTRY_STRUCTURES.register(name, structure);
    }

    /* End Gate */
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> END_GATE = registerStructure("end_gate", EndGate::new);
    /* Witch Hut */
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> ANCIENT_WITCH_HUT = registerStructure("ancient_witch_hut", AncientWitchHut::new);
    /* End Castle */
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> END_CASTLE = registerStructure("end_castle", () -> (new EndCastle(NoneFeatureConfiguration.CODEC)));

    public static StructurePieceType EndCastlePieceType = EndCastlePieces.EndCastlePiece::new;

    static void registerStructurePiece(StructurePieceType structurePiece, ResourceLocation rl) {
        Registry.register(Registry.STRUCTURE_PIECE, rl, structurePiece);
    }

    public static void registerAllPieces() {
        registerStructurePiece(EndCastlePieceType, new ResourceLocation(EndRemastered.MOD_ID, "ec"));
    }

}

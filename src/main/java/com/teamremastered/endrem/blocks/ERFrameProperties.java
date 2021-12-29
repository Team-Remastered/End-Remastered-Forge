package com.teamremastered.endrem.blocks;


import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

@MethodsReturnNonnullByDefault
public enum ERFrameProperties implements IStringSerializable {
    EMPTY,
    OLD_EYE,
    ROGUE_EYE,
    NETHER_EYE,
    COLD_EYE,
    CORRUPTED_EYE,
    MAGICAL_EYE,
    BLACK_EYE,
    LOST_EYE,
    WITHER_EYE,
    END_CRYSTAL_EYE,
    GUARDIAN_EYE,
    WITCH_EYE;

    public String toString() {
        return this.getSerializedName();
    }

    public String getSerializedName() {
        switch (this) {
            case EMPTY: default:
                return "empty";
            case OLD_EYE:
                return "old_eye";
            case ROGUE_EYE:
                return "rogue_eye";
            case NETHER_EYE:
                return "nether_eye";
            case COLD_EYE:
                return "cold_eye";
            case CORRUPTED_EYE:
                return "corrupted_eye";
            case MAGICAL_EYE:
                return "magical_eye";
            case BLACK_EYE:
                return "black_eye";
            case LOST_EYE:
                return "lost_eye";
            case WITHER_EYE:
                return "wither_eye";
            case END_CRYSTAL_EYE:
                return "end_crystal_eye";
            case GUARDIAN_EYE:
                return "guardian_eye";
            case WITCH_EYE:
                return "witch_eye";
        }
    }

    public static ERFrameProperties getFramePropertyFromEye(Item eye) {

        for (ERFrameProperties property : ERFrameProperties.values()) {
            // match the serialized name of the property to the item name of the eye
            assert eye.getRegistryName() != null;
            if (property.toString().equals(eye.getRegistryName().toString().split(":")[1])) {
                return property;
            }
        }

        return EMPTY;
    }
}

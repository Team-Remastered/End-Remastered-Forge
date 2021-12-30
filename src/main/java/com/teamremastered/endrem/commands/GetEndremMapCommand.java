package com.teamremastered.endrem.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import com.teamremastered.endrem.items.ERMap;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class GetEndremMapCommand {
    public GetEndremMapCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("get_endrem_map")
                .requires((source) -> source.hasPermission(2))// Requires Cheat Enabled
                .executes((command) -> getEndremMap(command.getSource())));
    }

    private int getEndremMap(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        player.addItem(ERMap.createMap(player.getLevel(), player.getOnPos()));
        source.sendSuccess(new TranslationTextComponent(String.format("Gave [Endrem Map] x 1 to %s", player.getDisplayName().getString())), true);
        return 1;
    }
}

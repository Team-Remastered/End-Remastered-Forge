package com.teamremastered.endrem.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import com.teamremastered.endrem.items.ERMap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class GetEndremMapCommand {
    public GetEndremMapCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("get_endrem_map")
                .requires((source) -> source.hasPermission(2))// Requires Cheat Enabled
                .executes((command) -> getEndremMap(command.getSource())));
    }

    private int getEndremMap(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        player.addItem(ERMap.createMap(player.getLevel(), player.getOnPos()));
        source.sendSuccess(new TextComponent(String.format("Gave [Endrem Map] x 1 to %s", player.getDisplayName().getString())), true);
        return 1;
    }
}

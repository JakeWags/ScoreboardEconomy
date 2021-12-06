package com.jakew.sceco;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.command.ServerCommandSource;


import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class PlayerSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        Scoreboard scoreboard = context.getSource().getServer().getScoreboard();
        Collection<String> knownPlayers = scoreboard.getKnownPlayers();

        for(String playerName : knownPlayers) {
            if (playerName != null) {
                builder.suggest(playerName);
            }
        }

        return builder.buildFuture();
    }
}

package com.jakew.sceco.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;

import static com.jakew.sceco.ScoreboardEconomy.incrementCredits;
import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/*
 * Pay Command
 * Use: /pay <player> <amount>
 * Expected: The target player will receive the AMOUNT of credits and the same amount will be removed from the sender
 */
public class PayCommand implements Command<ServerCommandSource> {
    private static String[] aliases = new String[]{};

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return 0;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> node = registerMain(dispatcher);
        registerAliases(dispatcher, node);
    }

    private static LiteralCommandNode<ServerCommandSource> registerMain(CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(
            literal("pay")
                .then(
                    argument("target", string())
                        .suggests((context, builder) -> {
                            Scoreboard scoreboard = context.getSource().getServer().getScoreboard();
                            Collection<String> knownPlayers = scoreboard.getKnownPlayers();

                            for(String playerName : knownPlayers) {
                                if (playerName != null) {
                                    builder.suggest(playerName);
                                }
                            }

                            return builder.buildFuture();
                        })
                        .then(
                            argument("amount", integer())
                                .executes(PayCommand::execute)
                            )
                )
        );
    }

    private static void registerAliases(CommandDispatcher<ServerCommandSource> dispatcher, LiteralCommandNode<ServerCommandSource> node) {
        for (String s : aliases) {
            dispatcher.register(literal(s).redirect(node));
        }
    }

    public static int execute(CommandContext<ServerCommandSource> c) throws CommandSyntaxException {
        ServerCommandSource source = c.getSource();
        Scoreboard scoreboard = source.getServer().getScoreboard();
        ScoreboardObjective money_objective = scoreboard.getObjective("Credits");
        String sourceName = source.getPlayer().getName().asString();

        int playerScore = scoreboard.getPlayerScore(sourceName, money_objective).getScore();
        int toSendAmount = getInteger(c, "amount");

        String target_name = getString(c, "target");
        ServerPlayerEntity target = source.getServer().getPlayerManager().getPlayer(target_name);


        if (!scoreboard.getKnownPlayers().contains(target_name)) {
            source.sendError(Text.of("ERROR: " + target_name + " does not exist."));
            return 0;
        } else if (playerScore < toSendAmount) {
            source.sendError(Text.of("ERROR: You do not have enough Credits."));
            return 0;
        } else if (toSendAmount < 1) {
            source.sendError(Text.of("Error: Invalid input"));
            return 0;
        } else {
            incrementCredits(scoreboard, target_name, toSendAmount);
            incrementCredits(scoreboard, sourceName, -1*toSendAmount);

            source.sendFeedback(Text.of("You sent §3" + toSendAmount + "§f Credits to §3" + target_name), false);

            // if target player is offline:
            if (target != null) {
                if (!target.isDisconnected()) {
                    target.sendMessage(Text.of("You received §3" + toSendAmount + "§f Credits from §3" + sourceName), false);
                } else { // this should never be hit
                    source.sendFeedback(Text.of("An unexpected error has occurred. You shouldn't be seeing this..."), false);
                }
            }

            return 1;
        }
    }
}

package com.jakew.sceco.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

/*
 * Balance Command
 * Use: /bal , /balance
 * Perms: 0 for all
 * Expected: Return the user's credit balance
 */
public class BalanceCommand implements Command<Object> {
    public static String[] aliases = new String[]{"bal"};

    @Override
    public int run(CommandContext<Object> c) throws CommandSyntaxException {
        System.out.println("When am i run?");
        return 0;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode balanceNode = registerMain(dispatcher);
        // registerAliases(dispatcher, balanceNode);
        // aliases not working properly for some reason
    }

    private static LiteralCommandNode registerMain(CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(
                literal("balance")
                        .executes(c -> {
                            ServerCommandSource source = c.getSource();
                            Scoreboard scoreboard = source.getServer().getScoreboard();
                            ScoreboardObjective money_objective = scoreboard.getObjective("Credits");
                            int score = scoreboard.getPlayerScore(source.getName(), money_objective).getScore();

                            source.sendFeedback(Text.of(String.format("You have §3%d §fCredits.", score)), false);

                            return 1;
                        })
        );
    };

    public static void registerAliases(CommandDispatcher<ServerCommandSource> dispatcher, LiteralCommandNode node) {
        for (String s : aliases) {
            dispatcher.register(literal(s).redirect(node));
        }
    }
}

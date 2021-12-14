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
public class BalanceCommand implements Command<ServerCommandSource> {
    public static String[] aliases = new String[]{"bal"};

    @Override
    public int run(CommandContext<ServerCommandSource> c) throws CommandSyntaxException {
        return 0;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> balanceNode = registerMain(dispatcher);
    }

    private static LiteralCommandNode<ServerCommandSource> registerMain(CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(
                literal("balance")
                        .executes(BalanceCommand::execute)
        );
    };

    public static void registerAliases(CommandDispatcher<ServerCommandSource> dispatcher, LiteralCommandNode<ServerCommandSource> node) {
        for (String s : aliases) {
            dispatcher.register(literal(s).redirect(node));
        }
    }

    public static int execute(CommandContext<ServerCommandSource> c) {
        ServerCommandSource source = c.getSource();
        Scoreboard scoreboard = source.getServer().getScoreboard();
        ScoreboardObjective money_objective = scoreboard.getObjective("Credits");
        int score = scoreboard.getPlayerScore(source.getName(), money_objective).getScore();

        source.sendFeedback(Text.of(String.format("You have §3%d §fCredits.", score)), false);

        return 1;
    }
}

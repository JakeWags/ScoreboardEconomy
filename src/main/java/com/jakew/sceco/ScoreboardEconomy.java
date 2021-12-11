package com.jakew.sceco;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

import com.jakew.sceco.registry.ModItems;
import com.jakew.sceco.shop.Catalog;
import com.jakew.sceco.shop.ShopItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;

public class ScoreboardEconomy implements ModInitializer {
    public static final String MOD_ID = "sceco";

    @Override
    public void onInitialize() {
        /*
         * TODO: Add /balance
         * TODO: Add /catalog
         *  - Add rotating shop sales
         *  - Add fluctuating prices
         * TODO: Add /sell <amount>    (sell x amount of items in hand)
         * TODO: Add /ah , /auction    (maybe)
         */
      Catalog catalog = new Catalog(4);

        /*
         * Pay Command
         * Use: /pay <player> <amount>
         * Expected: The target player will receive the AMOUNT of credits and the same amount will be removed from the sender
         */
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(
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
                            .executes(c -> {
                                ServerCommandSource source = c.getSource();
                                CommandManager commandManager = source.getServer().getCommandManager();
                                Scoreboard scoreboard = source.getServer().getScoreboard();
                                ScoreboardObjective money_objective = scoreboard.getObjective("Credits");
                                String sourceName = source.getPlayer().getName().asString();

                                int playerScore = scoreboard.getPlayerScore(sourceName, money_objective).getScore();
                                int toSendAmount = getInteger(c, "amount");

                                String target_name = getString(c, "target");
                                ServerPlayerEntity target = source.getServer().getPlayerManager().getPlayer(target_name);


                                if (!scoreboard.getKnownPlayers().contains(target_name)) {
                                    source.getPlayer().sendMessage(Text.of("§4ERROR: " + target_name + " does not exist."), false);
                                    return 0;
                                } else if (playerScore < toSendAmount) {
                                    source.getPlayer().sendMessage(Text.of("§4ERROR: You do not have enough Credits."), false);
                                    return 0;
                                } else if (toSendAmount < 1) {
                                    source.getPlayer().sendMessage(Text.of("§4ERROR: Invalid input."), false);
                                    return 0;
                                } else {
                                    incrementCredits(scoreboard, target_name, toSendAmount);
                                    incrementCredits(scoreboard, sourceName, -1*toSendAmount);

                                    source.getPlayer().sendMessage(Text.of("You sent §3" + toSendAmount + "§f Credits to §3" + target_name), false);

                                    // if target player is offline:
                                    if (target != null) {
                                        if (!target.isDisconnected()) {
                                            target.sendMessage(Text.of("You received §3" + toSendAmount + "§f Credits from §3" + sourceName), false);
                                        } else { // this should never be hit
                                            source.getPlayer().sendMessage(Text.of("An unexpected error has occurred. You shouldn't be seeing this..."), false);
                                        }
                                    }

                                    return 1;
                                }
                            })
                        )
                    )
            );
        });

        /*
         * Balance Command
         * Use: /bal , /balance
         * Expected: Return the user's credit balance
         */
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(
                literal("balance")
                        .executes(c -> {
                            ServerCommandSource source = c.getSource();
                            Scoreboard scoreboard = source.getServer().getScoreboard();
                            ScoreboardObjective money_objective = scoreboard.getObjective("Credits");
                            int score = scoreboard.getPlayerScore(source.getName(), money_objective).getScore();

                            source.getPlayer().sendMessage(Text.of(String.format("You have §3%d §fCredits.", score)), false);

                            Catalog cat = new Catalog(3);
                            System.out.println(cat);
                            return 1;
                        })

            );
        });

        /*
         * TODO: Add /catalog , /catalogue
         * TODO: Add better user interface on command run
         * Use: /catalog , /catalogue
         * Expected: Either print the current items for sale or open an interface with the current with their prices
         */
      CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
                dispatcher.register(
                        literal("catalog")
                                .executes(c -> {

                                  // TODO: better interface, could be in toString using block strings?
                                  c.getSource().getPlayer().sendMessage(Text.of(catalog.toString()), false);

                                  return 1;
                                })
                );
              });

        /* TODO: Add /sell <amount>
         * TODO: Add /sell <item> <amount>
         * Use: /sell <amount>
         * Expected: Sell the item in hand to the shop if the item is in the current shop selling rotation.
         */



        /* Command works but the coins don't

        /* Withdraw Command
         * Use: /withdraw <AMOUNT>
         * Expected: The player will receive the AMOUNT of coin items into their inventory, each worth one credit

        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
            dispatcher.register(
                literal("withdraw")
                    .then(
                        argument("amount", integer())
                            .executes(c -> {
                                ServerCommandSource source = c.getSource();
                                Scoreboard scoreboard = source.getServer().getScoreboard();
                                PlayerEntity playerSource = source.getPlayer();
                                ScoreboardObjective money_objective = scoreboard.getObjective("Credits");
                                ScoreboardPlayerScore playerScore = scoreboard.getPlayerScore(playerSource.getName().asString(), money_objective);


                                int amount = getInteger(c, "amount");

                                if (playerScore.getScore() < amount) {
                                    playerSource.sendMessage(Text.of("§4ERROR: You do not have enough Credits."), false);
                                    return 0;
                                } else {
                                    source.getServer().getCommandManager().execute(source, "give " + source.getName() + " sceco:coin " + amount);
                                    incrementCredits(scoreboard, playerSource.getName().asString(), -1*amount);
                                }

                                return 1;
                            })
                    )
            );
        }));

        */

        // ModItems.registerItems();
    }

    public static void incrementCredits(Scoreboard scoreboard, String playerName, int amount) {
        scoreboard.getPlayerScore(playerName, scoreboard.getObjective("Credits")).incrementScore(amount);
    }
}

package com.jakew.sceco;

import com.jakew.sceco.commands.BalanceCommand;
import com.jakew.sceco.commands.CatalogCommand;
import com.jakew.sceco.commands.PayCommand;
import com.jakew.sceco.commands.SellCommand;
import com.jakew.sceco.shop.Catalog;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.scoreboard.Scoreboard;

public class ScoreboardEconomy implements ModInitializer {
    public static final String MOD_ID = "sceco";
    public static Catalog catalog;

    @Override
    public void onInitialize() {
        /*
         *  TODO: catalog
         *      - Add automatic shop rotation. (once per day?)
         *      - Add fluctuating prices
         *          - Currently random
         * TODO: Add /ah , /auction    (maybe)
         * TODO: Add /buy (for purchasing from server)
         */
        catalog = new Catalog(4);
        registerModCommands();
    }

    public static void incrementCredits(Scoreboard scoreboard, String playerName, int amount) {
        scoreboard.getPlayerScore(playerName, scoreboard.getObjective("Credits")).incrementScore(amount);
    }

    private void registerModCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            BalanceCommand.register(dispatcher);
            CatalogCommand.register(dispatcher);
            SellCommand.register(dispatcher);
            PayCommand.register(dispatcher);
        });
    }
}

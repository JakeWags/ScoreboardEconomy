package com.jakew.sceco.commands;

import com.jakew.sceco.shop.ShopItem;
import com.jakew.sceco.util.CatalogItemNotFoundException;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.apache.logging.log4j.core.jmx.Server;

import static com.jakew.sceco.ScoreboardEconomy.catalog;
import static com.jakew.sceco.ScoreboardEconomy.incrementCredits;
import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/*
 * TODO: Add /sell <item> <amount>
 * Use: /sell <amount>
 * Expected: Sell the item in hand to the shop if the item is in the current shop selling rotation.
 */
public class SellCommand implements Command<ServerCommandSource> {
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
            literal("sell")
                .then(
                    argument("amount", integer())
                        .executes(SellCommand::execute)
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
            ItemStack stack = source.getPlayer().getMainHandStack();
            String itemName = stack.getItem().getName().getString();
            int amount = getInteger(c, "amount");

            if (stack.isEmpty()) {
                source.sendError(Text.of("ERROR: Your hand is empty."));
                return 0;
            } else if (!catalog.catalogItemNames.contains(itemName)) {
                source.sendError(Text.of("ERROR: " + itemName + " is not currently for sale."));
                return 0;
            } else if (amount > stack.getCount()) {
                source.sendError(Text.of("ERROR: You do not have that many items in your hand."));
            } else if (amount <= 0) {
                source.sendError(Text.of("ERROR: Invalid input."));
            } else {
                ShopItem s;
                try {
                    s = catalog.getItemInCurrentCatalog(itemName);
                } catch (CatalogItemNotFoundException e) {
                    source.sendError(Text.of("ERROR: " + itemName + " is not currently for sale."));
                    return 0;
                }
                int profit = s.getCurrentPrice()*amount;

                incrementCredits(source.getServer().getScoreboard(), source.getName(), profit);
                stack.setCount(stack.getCount()-amount);
                source.sendFeedback(Text.of("You sold " + amount + " " + itemName + " for §3" + profit + " §fCredits"), true);
            }

            return 1;
        }
}

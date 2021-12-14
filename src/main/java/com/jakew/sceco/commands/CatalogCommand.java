package com.jakew.sceco.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.jakew.sceco.ScoreboardEconomy.catalog;
import static net.minecraft.server.command.CommandManager.literal;

/*
 * TODO: Add better user interface on command run
 * Use: /catalog , /catalog rotate
 * Perms: 0 for /catalog, 4 for /catalog rotate
 * Expected: Either print the current items for sale or open an interface with the current with their prices
 */
public class CatalogCommand implements Command<Object> {

    public static String[] aliases = new String[]{"catalogue", "cat"};

    @Override
    public int run(CommandContext<Object> context) throws CommandSyntaxException {
        return 0;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode node = registerMain(dispatcher);
        //registerAliases(dispatcher, node);
    }

    private static LiteralCommandNode registerMain(CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(literal("catalog")
                .executes(c -> {
                    printCatalog(c.getSource());
                    return 1;
                })
                .then(
                    literal("rotate").requires(source -> source.hasPermissionLevel(4)) // must be op to rotate
                        .executes(c -> {
                            catalog.rotateCatalog();
                            c.getSource().sendFeedback(Text.of("Catalog rotated. The new catalog is:"), false);
                            printCatalog(c.getSource());
                            return 1;
                        })
                )
        );
    }

    private static void registerAliases(CommandDispatcher<ServerCommandSource> dispatcher, LiteralCommandNode node) {
        for (String s : aliases) {
            dispatcher.register(literal(s).redirect(node));
        }
    }

    public static void printCatalog(ServerCommandSource source) {
        source.sendFeedback(Text.of(catalog.toString()), false);
    }
}

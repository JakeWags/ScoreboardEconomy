package com.jakew.sceco.registry;

import com.jakew.sceco.Coin;
import com.jakew.sceco.ScoreboardEconomy;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item COIN = new Coin(new Item.Settings().group(ItemGroup.MISC).rarity(Rarity.RARE));

    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(ScoreboardEconomy.MOD_ID, "coin"), COIN);
    }

}

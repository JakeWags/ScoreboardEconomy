package com.jakew.sceco.shop;

import java.io.*;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ShopItem {
    private int price = 0;
    private String itemId = null;
    private String itemName = null;
    File f;

    public ShopItem(String itemname, String itemId, int price) {
        this.itemName = itemname;
        this.itemId = itemId;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public String getId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public Item getItem() {
        return Registry.ITEM.get(new Identifier(itemId));
    }

    @Override
    public String toString() {
        return (getItemName() + "");
    }
}

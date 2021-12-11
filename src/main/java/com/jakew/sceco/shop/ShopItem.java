package com.jakew.sceco.shop;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ShopItem {
    private int maxPrice = 0;
    private int currentPrice = 0;
    private String itemId;
    private String itemName = "";
    private double priceModifier = 1; // 1 for normal price, 0.50 for half, 0.25 for 1/4, so on

    public ShopItem(String itemName, String itemId, int price) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.maxPrice = price;
        this.currentPrice = getCurrentPrice();
    }

    public ShopItem(String itemName, String itemId, String price) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.maxPrice = Integer.parseInt(price);
        this.currentPrice = getCurrentPrice();
    }

    public int getMaxPrice() { return maxPrice; }

    public int getCurrentPrice() {
        return  (int)Math.round(maxPrice*priceModifier);
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

    public void setPriceModifier(double priceModifier) { this.priceModifier = priceModifier; }

    @Override
    public String toString() {
        return (getItemName() + "");
    }
}

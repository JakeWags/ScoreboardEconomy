package com.jakew.sceco.shop;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ShopItem {
    private float initialPrice = 0;
    private float currentPrice = 0;
    private String itemId;
    private String itemName = "";
    private float priceModifier = 1; // 1 for normal price, 0.50 for half, 0.25 for 1/4, so on

    public ShopItem(String itemName, String itemId, float price) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.initialPrice = price;
        this.currentPrice = getCurrentPrice();
    }

    public ShopItem(String itemName, String itemId, String price) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.initialPrice = Float.parseFloat(price);
        this.currentPrice = getCurrentPrice();
    }

    public float getInitialPrice() { return initialPrice; }

    public int getCurrentPrice() {
        float t = initialPrice*priceModifier;

        if (t > this.initialPrice+0.18) { return (int) Math.ceil(t); } // round up if above
        else if (t < this.initialPrice-0.15) {                         // round down if below
            if (this.initialPrice - 0.15 <= 1) { return 1; }           // if rounded down = 0, replace with 1
            else { return (int) Math.floor(t); }
        }
        else { return Math.round(t); }                                  // else just round back to normal price
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

    public float getPriceModifier() { return this.priceModifier; }

    public void setPriceModifier(float priceModifier) {
        this.priceModifier = priceModifier;
        refreshCurrentPrice();
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void refreshCurrentPrice() {
        this.currentPrice = getCurrentPrice();
    }

    @Override
    public String toString() {
        return (getItemName() + "");
    }
}

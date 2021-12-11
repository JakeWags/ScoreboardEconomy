package com.jakew.sceco.shop;

import java.util.ArrayList;

public class Catalog {

    private ArrayList<ShopItem> catalogList = new ArrayList<>();

    public Catalog() {

    }

    // TODO: Add catalogList initialization
    private ArrayList<ShopItem> initCatalog() {
        return null;
    }

    ShopItem diamond = new ShopItem("diamond");
    ShopItem hay_block = new ShopItem("hay_block");
    ShopItem lapis_lazuli = new ShopItem("lapis_lazuli");
    ShopItem netherite = new ShopItem("netherite");
    ShopItem quartz_block = new ShopItem("quartz_block");
    ShopItem redstone_block = new ShopItem("redstone_block");
}

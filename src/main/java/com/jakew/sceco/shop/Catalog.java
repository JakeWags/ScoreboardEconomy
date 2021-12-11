package com.jakew.sceco.shop;

import java.util.ArrayList;


/*
 * TODO: Changing Prices
 * TODO: Server-wide item trade limit per cycle
 * TODO: Automatically rotating catalog
 */
public class Catalog {
    public static final String[] catalogItemNames = new String[]{"diamond", "hay_block", "lapis_block", "netherite", "quartz_block", "redstone_block"};

    private int size;

    private ArrayList<ShopItem> catalogList = new ArrayList<>();
    private ArrayList<ShopItem> currentList = new ArrayList<>();

    public Catalog(int size) {
        setSize(size);
        initCatalog();
        genCurrentList();
    }

    private void setSize(int size) {
        this.size = size;
    }

    private ArrayList<ShopItem> initCatalog() {
        for(String item : catalogItemNames) {
            catalogList.add(new ShopItem(item));
        }

        return catalogList;
    }

    private ArrayList<ShopItem> genCurrentList() {
        currentList.clear();

        ArrayList<ShopItem> temp = (ArrayList<ShopItem>) catalogList.clone();
        for (int i = 0; i < getSize(); i++) {
            int j = (int)Math.round(Math.random()*(temp.size()-1));

            currentList.add(temp.remove(j));
        }

        return currentList;
    }

    public int getSize() {
        return this.size;
    }

    public ArrayList<ShopItem> rotateCatalog() {
        return genCurrentList();
    }

    @Override
    public String toString() {
        String s = "The Current Catalog is: \n";
        for (int i = 0; i < getSize(); i++) {
            s += currentList.get(i).toString() + ": §3" + currentList.get(i).getPrice() + "§f Credits";
            if (i != getSize()-1) {
                s += "\n";
            }
        }
        return s;
    }


}

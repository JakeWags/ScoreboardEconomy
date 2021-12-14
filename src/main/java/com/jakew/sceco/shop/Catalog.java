package com.jakew.sceco.shop;

import com.jakew.sceco.util.CatalogItemNotFoundException;

import java.util.ArrayList;

public class Catalog {
    public ArrayList<String> catalogItemNames = new ArrayList<>(ShopItems.LIST.length);

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
        for (int i = 0; i < ShopItems.LIST.length; i++) {
            String[] t = ShopItems.LIST[i];
            catalogList.add(new ShopItem(t[ShopItems.ITEM_NAME],t[ShopItems.ITEM_ID],t[ShopItems.MAX_PRICE]));
            catalogItemNames.add(t[ShopItems.ITEM_NAME]); // item name
        }

        return catalogList;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<ShopItem> genCurrentList() {  // generate a brand new current list and assign price variance
        currentList.clear();

        ArrayList<ShopItem> temp = (ArrayList<ShopItem>) catalogList.clone();
        for (int i = 0; i < getSize(); i++) {
            int j = (int)Math.round(Math.random()*(temp.size()-1)); // random item from list of all items
            ShopItem t = temp.remove(j);
            t.setPriceModifier((float) ((Math.random()*(1.2-0.75))+0.75)); // price variance (from 75% to 120%) (avg ~97.6%)

            currentList.add(t);
        }

        return currentList;
    }

    public int getSize() {
        return this.size;
    }

    public ArrayList<ShopItem> rotateCatalog() {
        return genCurrentList();
    }

    public ShopItem getItemInCurrentCatalog(String itemName) throws CatalogItemNotFoundException { // current catalog items
        if (catalogItemNames.contains(itemName)) {
            for (ShopItem i : currentList) {
                if (i.getItemName().equalsIgnoreCase(itemName)) {
                    return i;
                }
            }
            throw new CatalogItemNotFoundException(itemName);
        }
        throw new CatalogItemNotFoundException(itemName);
    }

    public ShopItem getItemInCatalog(String itemName) throws CatalogItemNotFoundException{ // all catalog items
        if (catalogItemNames.contains(itemName)) {
            for (ShopItem i : catalogList) {
                if (i.getItemName().equalsIgnoreCase(itemName)) {
                    return i;
                }
            }
        }
        throw new CatalogItemNotFoundException(itemName);
    }

    public ArrayList<ShopItem> getCurrentList() {
        return currentList;
    }

    @Override
    public String toString() {
        String s = "§2==============================§f \n";
        for (int i = 0; i < getSize(); i++) {
            s += currentList.get(i).toString() + ": §3" + currentList.get(i).getCurrentPrice() + "§f Credits\n";
        }
        s += "§2==============================§f";
        return s;
    }
}

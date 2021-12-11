package com.jakew.sceco.shop;

import com.jakew.sceco.util.CatalogItemNotFoundException;

import java.util.ArrayList;

/*
 * TODO: Changing Prices
 */
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

    public ShopItem getItemInCurrentCatalog(String itemName) throws CatalogItemNotFoundException {
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

    public ShopItem getItemInCatalog(String itemName) throws CatalogItemNotFoundException{
        if (catalogItemNames.contains(itemName)) {
            for (ShopItem i : catalogList) {
                if (i.getItemName().equalsIgnoreCase(itemName)) {
                    return i;
                }
            }
        }
        throw new CatalogItemNotFoundException(itemName);
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

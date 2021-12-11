package com.jakew.sceco.util;

public class CatalogItemNotFoundException extends Exception {
    String itemName;
    public CatalogItemNotFoundException(String e) {
        itemName = e;
    }

    @Override
    public String toString() {
        return itemName + " not found";
    }
}

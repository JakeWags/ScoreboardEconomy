package com.jakew.sceco.shop;

import net.minecraft.item.Item;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.parser.*;
import org.json.simple.JSONObject;


public class ShopItem {
    private int price = 0;
    private String itemId = null;

    public ShopItem(String filename) {
        try {
            // TODO: resolve path issues

            parseFile(filename);
            File f = new File("diamond.json");
            System.out.println(itemId);
            System.out.println(f.getPath());
        } catch (ParseException | IOException e) {
            System.out.println(e);
        }
    }

    private void parseFile(String pathname) throws ParseException, IOException {
        Object obj = new JSONParser().parse(new FileReader(pathname));
        JSONObject jo = (JSONObject) obj;

        itemId = (String) jo.get("id");
    }

    private void setPrice(int price) {

    }

    public int getPrice() {
        return 0;
    }

    public Item getItem() {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}

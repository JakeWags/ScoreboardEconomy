package com.jakew.sceco.shop;

import java.io.*;

import org.json.simple.parser.*;
import org.json.simple.JSONObject;


public class ShopItem {
    private int price = 0;
    private String itemId = null;
    File f;

    public ShopItem(String itemname) {
        try {
            f = new File("../src/main/java/com/jakew/sceco/shop/shopItems/" + itemname + ".json");
            parseFile(f);
        } catch (ParseException | IOException e) {
            System.out.println(e);
        }
    }

    private void parseFile(File file) throws ParseException, IOException {
        Object obj = new JSONParser().parse(new FileReader(file));
        JSONObject jo = (JSONObject) obj;

        itemId = (String) jo.get("id");
        price = ((Long)jo.get("price")).intValue();
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return (itemId+" is worth "+price+" Credits.");
    }
}

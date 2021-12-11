package com.jakew.sceco.shop;

import java.io.*;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.json.simple.parser.*;
import org.json.simple.JSONObject;


public class ShopItem {
    private int price = 0;
    private String itemId = null;
    private String itemName = null;
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

        itemName = (String) jo.get("name");
        itemId = (String) jo.get("id");
        price = ((Long)jo.get("price")).intValue();
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

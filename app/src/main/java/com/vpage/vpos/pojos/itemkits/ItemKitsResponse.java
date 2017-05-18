package com.vpage.vpos.pojos.itemkits;


import java.util.Arrays;

public class ItemKitsResponse {

    private ItemKits[] items;

    public ItemKits[] getItems() {
        return items;
    }

    public void setItems(ItemKits[] items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ItemKitsResponse{" +
                "items=" + Arrays.toString(items) +
                '}';
    }
}

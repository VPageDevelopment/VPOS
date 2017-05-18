package com.vpage.vpos.pojos.item;


import java.util.Arrays;

public class ItemResponse {

    private Items[] items;

    public Items[] getItems() {
        return items;
    }

    public void setItems(Items[] items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ItemResponse{" +
                "items=" + Arrays.toString(items) +
                '}';
    }
}

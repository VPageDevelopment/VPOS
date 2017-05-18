package com.vpage.vpos.pojos.sale;

import java.util.Arrays;

public class SaleResponse {

    private Sales[] items;


    public Sales[] getItems() {
        return items;
    }

    public void setItems(Sales[] items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "SaleResponse{" +
                "items=" + Arrays.toString(items) +
                '}';
    }
}

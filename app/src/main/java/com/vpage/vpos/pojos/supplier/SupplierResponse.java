package com.vpage.vpos.pojos.supplier;

import java.util.Arrays;

public class SupplierResponse {

    private Suppliers[] items;

    public Suppliers[] getItems() {
        return items;
    }

    public void setItems(Suppliers[] items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "SupplierResponse{" +
                "items=" + Arrays.toString(items) +
                '}';
    }
}

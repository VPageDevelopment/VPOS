package com.vpage.vpos.pojos.itemkits;


import java.util.Arrays;

public class ItemKitsResponse {

    private String status;

    private String status_code;

    private ItemKits[] items;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public ItemKits[] getItems() {
        return items;
    }

    public void setItems(ItemKits[] items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ItemKitsResponse{" +
                "status='" + status + '\'' +
                ", status_code='" + status_code + '\'' +
                ", items=" + Arrays.toString(items) +
                '}';
    }
}

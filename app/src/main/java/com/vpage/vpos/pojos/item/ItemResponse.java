package com.vpage.vpos.pojos.item;

import java.util.Arrays;

public class ItemResponse {

    private String status;

    private String status_code;

    private Items[] items;

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

    public Items[] getItems() {
        return items;
    }

    public void setItems(Items[] items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ItemResponse{" +
                "status='" + status + '\'' +
                ", status_code='" + status_code + '\'' +
                ", items=" + Arrays.toString(items) +
                '}';
    }
}

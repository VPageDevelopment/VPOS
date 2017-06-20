package com.vpage.vpos.pojos.sale;

import java.util.Arrays;

public class SaleResponse {

    private String status;

    private String status_code;

    private Sales[] sales;

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

    public Sales[] getItems() {
        return sales;
    }

    public void setItems(Sales[] items) {
        this.sales = items;
    }

    @Override
    public String toString() {
        return "SaleResponse{" +
                "status='" + status + '\'' +
                ", status_code='" + status_code + '\'' +
                ", items=" + Arrays.toString(sales) +
                '}';
    }
}

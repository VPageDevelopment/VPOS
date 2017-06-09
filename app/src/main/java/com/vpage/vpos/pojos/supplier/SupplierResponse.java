package com.vpage.vpos.pojos.supplier;

import java.util.Arrays;

public class SupplierResponse {

    private String status;

    private String status_code;

    private Suppliers[] suppliers;

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

    public Suppliers[] getItems() {
        return suppliers;
    }

    public void setItems(Suppliers[] items) {
        this.suppliers = items;
    }

    @Override
    public String toString() {
        return "SupplierResponse{" +
                "status='" + status + '\'' +
                ", status_code='" + status_code + '\'' +
                ", suppliers=" + Arrays.toString(suppliers) +
                '}';
    }
}

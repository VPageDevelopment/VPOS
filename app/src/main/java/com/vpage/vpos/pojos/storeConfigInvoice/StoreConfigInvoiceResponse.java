package com.vpage.vpos.pojos.storeConfigInvoice;


import java.util.Arrays;

public class StoreConfigInvoiceResponse {

    private String status_code;

    private StoreConfigInvoice[] storeConfigInvoice;

    private String status;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public StoreConfigInvoice[] getStoreConfigInvoice() {
        return storeConfigInvoice;
    }

    public void setStoreConfigInvoice(StoreConfigInvoice[] storeConfigInvoice) {
        this.storeConfigInvoice = storeConfigInvoice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StoreConfigInvoiceResponse{" +
                "status_code='" + status_code + '\'' +
                ", storeConfigInvoice=" + Arrays.toString(storeConfigInvoice) +
                ", status='" + status + '\'' +
                '}';
    }
}

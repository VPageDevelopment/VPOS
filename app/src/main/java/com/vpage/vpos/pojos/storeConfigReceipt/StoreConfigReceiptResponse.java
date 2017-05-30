package com.vpage.vpos.pojos.storeConfigReceipt;


import java.util.Arrays;

public class StoreConfigReceiptResponse {

    private String status_code;

    private StoreConfigReceipt[] storeConfigReceipt;

    private String status;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public StoreConfigReceipt[] getStoreConfigReceipt() {
        return storeConfigReceipt;
    }

    public void setStoreConfigReceipt(StoreConfigReceipt[] storeConfigReceipt) {
        this.storeConfigReceipt = storeConfigReceipt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StoreConfigReceiptResponse{" +
                "status_code='" + status_code + '\'' +
                ", storeConfigReceipt=" + Arrays.toString(storeConfigReceipt) +
                ", status='" + status + '\'' +
                '}';
    }
}

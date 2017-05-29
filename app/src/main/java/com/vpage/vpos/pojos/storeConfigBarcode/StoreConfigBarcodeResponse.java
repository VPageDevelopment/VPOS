package com.vpage.vpos.pojos.storeConfigBarcode;

import java.util.Arrays;

public class StoreConfigBarcodeResponse {

    private String status_code;

    private StoreConfigBarcode[] StoreConfigBarcode;

    private String status;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public com.vpage.vpos.pojos.storeConfigBarcode.StoreConfigBarcode[] getStoreConfigBarcode() {
        return StoreConfigBarcode;
    }

    public void setStoreConfigBarcode(com.vpage.vpos.pojos.storeConfigBarcode.StoreConfigBarcode[] storeConfigBarcode) {
        StoreConfigBarcode = storeConfigBarcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StoreConfigBarcodeResponse{" +
                "status_code='" + status_code + '\'' +
                ", StoreConfigBarcode=" + Arrays.toString(StoreConfigBarcode) +
                ", status='" + status + '\'' +
                '}';
    }
}

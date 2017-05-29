package com.vpage.vpos.pojos.storeConfigGeneral;


import java.util.Arrays;

public class StoreConfigGeneralResponse {

    private String status_code;

    private StoreConfigInformation[] storeConfigInformation;

    private String status;


    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public StoreConfigInformation[] getStoreConfigInformation() {
        return storeConfigInformation;
    }

    public void setStoreConfigInformation(StoreConfigInformation[] storeConfigInformation) {
        this.storeConfigInformation = storeConfigInformation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StoreConfigGeneralResponse{" +
                "status_code='" + status_code + '\'' +
                ", storeConfigInformation=" + Arrays.toString(storeConfigInformation) +
                ", status='" + status + '\'' +
                '}';
    }
}

package com.vpage.vpos.pojos.storeConfig;


import java.util.Arrays;

public class StoreConfigResponse {

    private String status_code;

    private StoreConfigInformation[] StoreConfigInformation;

    private String status;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public com.vpage.vpos.pojos.storeConfig.StoreConfigInformation[] getStoreConfigInformation() {
        return StoreConfigInformation;
    }

    public void setStoreConfigInformation(com.vpage.vpos.pojos.storeConfig.StoreConfigInformation[] storeConfigInformation) {
        StoreConfigInformation = storeConfigInformation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StoreConfigResponse{" +
                "status_code='" + status_code + '\'' +
                ", StoreConfigInformation=" + Arrays.toString(StoreConfigInformation) +
                ", status='" + status + '\'' +
                '}';
    }
}

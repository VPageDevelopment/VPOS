package com.vpage.vpos.pojos.storeConfigLocal;


import java.util.Arrays;

public class StoreConfigLocalResponse {

    private String status_code;

    private StoreConfigLocalisation[] storeConfigLocalisation;

    private String status;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public StoreConfigLocalisation[] getStoreConfigLocalisation() {
        return storeConfigLocalisation;
    }

    public void setStoreConfigLocalisation(StoreConfigLocalisation[] storeConfigLocalisation) {
        this.storeConfigLocalisation = storeConfigLocalisation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StoreConfigLocalResponse{" +
                "status_code='" + status_code + '\'' +
                ", storeConfigLocalisation=" + Arrays.toString(storeConfigLocalisation) +
                ", status='" + status + '\'' +
                '}';
    }
}

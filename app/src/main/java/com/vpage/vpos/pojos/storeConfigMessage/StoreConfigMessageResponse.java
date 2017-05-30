package com.vpage.vpos.pojos.storeConfigMessage;


import java.util.Arrays;

public class StoreConfigMessageResponse {

    private String status_code;

    private StoreConfigMessage[] storeConfigMessage;

    private String status;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public StoreConfigMessage[] getStoreConfigMessage() {
        return storeConfigMessage;
    }

    public void setStoreConfigMessage(StoreConfigMessage[] storeConfigMessage) {
        this.storeConfigMessage = storeConfigMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StoreConfigMessageResponse{" +
                "status_code='" + status_code + '\'' +
                ", storeConfigMessage=" + Arrays.toString(storeConfigMessage) +
                ", status='" + status + '\'' +
                '}';
    }
}

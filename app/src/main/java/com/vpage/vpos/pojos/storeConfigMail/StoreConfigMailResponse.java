package com.vpage.vpos.pojos.storeConfigMail;

import java.util.Arrays;

public class StoreConfigMailResponse {

    private String status_code;

    private StoreConfigMail[] storeConfigMail;

    private String status;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public StoreConfigMail[] getStoreConfigMail() {
        return storeConfigMail;
    }

    public void setStoreConfigMail(StoreConfigMail[] storeConfigMail) {
        this.storeConfigMail = storeConfigMail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StoreConfigMailResponse{" +
                "status_code='" + status_code + '\'' +
                ", storeConfigMail=" + Arrays.toString(storeConfigMail) +
                ", status='" + status + '\'' +
                '}';
    }
}

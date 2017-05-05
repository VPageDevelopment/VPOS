package com.vpage.vpos.tools;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vpos.pojos.CustomerResponse;

public class VPOSRestTools {

    private static final String TAG = VPOSRestTools.class.getName();


    private static final Object monitor = new Object();
    private static VPOSRestTools vposRestTools = null;

    public static VPOSRestTools getInstance() {
        if (vposRestTools == null) {
            synchronized (monitor) {
                if (vposRestTools == null)
                    vposRestTools = new VPOSRestTools();
            }
        }
        return vposRestTools;
    }


    public CustomerResponse getCustomerResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, CustomerResponse.class);
    }

}

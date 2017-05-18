package com.vpage.vpos.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vpos.pojos.ItemResponse;
import com.vpage.vpos.pojos.SignInResponse;
import com.vpage.vpos.pojos.customer.CustomersResponse;
import com.vpage.vpos.pojos.customer.UpdateCustomersResponse;
import com.vpage.vpos.pojos.customer.addCustomer.AddCustomerRequest;
import com.vpage.vpos.pojos.customer.addCustomer.AddCustomerResponse;
import com.vpage.vpos.pojos.employee.EmployeeResponse;
import com.vpage.vpos.pojos.employee.UpdateEmployeeResponse;
import com.vpage.vpos.pojos.employee.addEmployee.AddEmployeeResponse;
import com.vpage.vpos.pojos.item.UpdateItemResponse;
import com.vpage.vpos.pojos.item.addItem.AddItemResponse;
import com.vpage.vpos.pojos.itemkits.ItemKitsResponse;
import com.vpage.vpos.pojos.itemkits.UpdateItemKitsResponse;
import com.vpage.vpos.pojos.itemkits.addItemKits.AddItemKitsResponse;


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

    public SignInResponse getSignInResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, SignInResponse.class);
    }


    public CustomersResponse getCustomerResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, CustomersResponse.class);
    }

    public AddCustomerResponse addCustomerResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, AddCustomerResponse.class);
    }

    public UpdateCustomersResponse updateCustomerResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, UpdateCustomersResponse.class);
    }

    public EmployeeResponse getEmployeeResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, EmployeeResponse.class);
    }

    public AddEmployeeResponse addEmployeeResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, AddEmployeeResponse.class);
    }

    public UpdateEmployeeResponse updateEmployeeResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, UpdateEmployeeResponse.class);
    }

    public ItemResponse getItemResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, ItemResponse.class);
    }

    public AddItemResponse addItemResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, AddItemResponse.class);
    }

    public UpdateItemResponse updateItemResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, UpdateItemResponse.class);
    }

    public ItemKitsResponse getItemKitsResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, ItemKitsResponse.class);
    }

    public AddItemKitsResponse addItemKitsResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, AddItemKitsResponse.class);
    }

    public UpdateItemKitsResponse updateItemKitsResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, UpdateItemKitsResponse.class);
    }



}

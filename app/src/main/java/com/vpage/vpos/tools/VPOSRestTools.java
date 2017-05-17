package com.vpage.vpos.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vpos.pojos.SignInResponse;
import com.vpage.vpos.pojos.customer.CustomersResponse;
import com.vpage.vpos.pojos.customer.UpdateCustomersResponse;
import com.vpage.vpos.pojos.customer.addCustomer.AddCustomerRequest;
import com.vpage.vpos.pojos.customer.addCustomer.AddCustomerResponse;
import com.vpage.vpos.pojos.employee.EmployeeResponse;
import com.vpage.vpos.pojos.employee.addEmployee.AddEmployeeResponse;


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
}

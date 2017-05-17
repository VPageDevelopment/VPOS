package com.vpage.vpos.httputils;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.vpage.vpos.R;
import com.vpage.vpos.pojos.SignInRequest;
import com.vpage.vpos.pojos.SignInResponse;
import com.vpage.vpos.pojos.customer.CustomersResponse;
import com.vpage.vpos.pojos.customer.UpdateCustomersResponse;
import com.vpage.vpos.pojos.customer.addCustomer.AddCustomerRequest;
import com.vpage.vpos.pojos.customer.addCustomer.AddCustomerResponse;
import com.vpage.vpos.pojos.employee.EmployeeResponse;
import com.vpage.vpos.pojos.employee.addEmployee.AddEmployeeRequest;
import com.vpage.vpos.pojos.employee.addEmployee.AddEmployeeResponse;
import com.vpage.vpos.tools.VPOSApplication;
import com.vpage.vpos.tools.VPOSRestTools;
import com.vpage.vpos.tools.VPOSTools;
import com.vpage.vpos.tools.utils.LogFlag;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class VPOSRestClient {

    private static final String TAG =  VPOSRestClient.class.getName();

    StringEntity parsedJsonParams = null;

    public static void cancelAllRequests() {
        HttpManager.asyncHttpClient.cancelAllRequests(true);
        HttpManager.syncHttpClient.cancelAllRequests(true);

    }


    public void setSignUpParams(SignInRequest signInRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(signInRequest);
            parsedJsonParams = new StringEntity(VPOSTools.getRequestWithAppVersion(jsonParams));

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);
        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }

    public void setSignInParams(SignInRequest signInRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(signInRequest);
            parsedJsonParams = new StringEntity(VPOSTools.getRequestSignIn(jsonParams));

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);
        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }

    public boolean doAction(final String url, String httpMethod, Object request) throws UnsupportedEncodingException {
        final boolean[] response = new boolean[1];
        if (httpMethod.equals(HttpMethod.GET)) {
            HttpManager.get(url, null, new JsonHttpResponseHandler() {


                public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                    response[0] = true;
                }


                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (LogFlag.bLogOn)Log.d(TAG, "onFailure: GET REST Call: " + url + " Failed");
                    response[0] = false;
                }
            });
        } else if (httpMethod.equals(HttpMethod.POST)) {
            StringEntity parsedJsonParams = null;
            if (request != null) {
                Gson gson = new GsonBuilder().create();
                String jsonParams = gson.toJson(request);
                parsedJsonParams = new StringEntity(VPOSTools.getRequestSignIn(jsonParams));

            }
            HttpManager.post(url, parsedJsonParams, new JsonHttpResponseHandler() {

                public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                    response[0] = true;
                }


                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (LogFlag.bLogOn)Log.d(TAG, "onFailure: POST REST Call: " + url + " Failed");
                    response[0] = false;
                }

            });
        } else if (httpMethod.equals(HttpMethod.PUT)) {
            HttpManager.put(url, null, new JsonHttpResponseHandler() {

                public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                    response[0] = true;
                }


                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (LogFlag.bLogOn)Log.d(TAG, "onFailure: PUT REST Call: " + url + " Failed");
                    response[0] = false;
                }
            });
        }
        return response[0];
    }


    public SignInResponse getSignInResponse() {
        String signInUrl = VPOSApplication.getContext().getResources().getString(R.string.signIn);
        Log.d(TAG, signInUrl);
        final SignInResponse[] signInResponses = {null};
        HttpManager.setBasicAuthData(VPOSApplication.getContext().getResources().getString(R.string.userName),VPOSApplication.getContext().getResources().getString(R.string.password));
        HttpManager.postSignIn(signInUrl, parsedJsonParams, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                signInResponses[0] = VPOSRestTools.getInstance().getSignInResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "signInResponses: "+signInResponses[0].toString());
            }
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (LogFlag.bLogOn) Log.d(TAG, "Error " + statusCode);
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + responseString);

            }

        });
        return signInResponses[0];
    }


    public CustomersResponse getCustomer() {

        String customerUrl = VPOSApplication.getContext().getResources().getString(R.string.customer);

        final CustomersResponse[] customersResponses = {null};
        HttpManager.get(customerUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                customersResponses[0] = VPOSRestTools.getInstance().getCustomerResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "customersResponses: "+resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "customersResponses: "+customersResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + responseString);
                customersResponses[0] = null;

            }
        });
        return customersResponses[0];
    }

    public void setAddCustomerParams(AddCustomerRequest addCustomerRequest) {

        try {

            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(addCustomerRequest);
            parsedJsonParams = new StringEntity(jsonParams);

            Log.d(TAG, jsonParams);

        } catch (UnsupportedEncodingException e) {

            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }

    public AddCustomerResponse addCustomer() {
        String customerUrl = VPOSApplication.getContext().getResources().getString(R.string.add_customer);
        if (LogFlag.bLogOn)Log.d(TAG, customerUrl);
        final AddCustomerResponse[] addCustomerResponses = {null};

        HttpManager.post(customerUrl, parsedJsonParams, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                addCustomerResponses[0] = VPOSRestTools.getInstance().addCustomerResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "addCustomerResponses: "+addCustomerResponses[0].toString());
            }

        });
        return addCustomerResponses[0];
    }

    public UpdateCustomersResponse updateCustomer(String customerId) {
        String customerUrl = VPOSApplication.getContext().getResources().getString(R.string.update_customer);
        customerUrl = customerUrl.replace("{id}",customerId);
        if (LogFlag.bLogOn)Log.d(TAG, customerUrl);
        final UpdateCustomersResponse[] updateCustomersResponses = {null};

        HttpManager.putwithEntity(customerUrl, parsedJsonParams, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateCustomersResponses[0] = VPOSRestTools.getInstance().updateCustomerResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, updateCustomersResponses[0].toString());
            }

        });
        return updateCustomersResponses[0];
    }


    public UpdateCustomersResponse deleteCustomer(String customerId) {

            String customerUrl = VPOSApplication.getContext().getResources().getString(R.string.delete_customer);
            customerUrl = customerUrl.replace("{id}",customerId);

            final UpdateCustomersResponse[] updateCustomersResponses = {null};
            HttpManager.delete(customerUrl, new JsonHttpResponseHandler() {

                public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                    updateCustomersResponses[0] = VPOSRestTools.getInstance().updateCustomerResponseData(resultData.toString());
                    if (LogFlag.bLogOn)Log.d(TAG, updateCustomersResponses[0].toString());
                }


                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });

        return updateCustomersResponses[0];
    }



    public EmployeeResponse getEmployee() {

        String employeeUrl = VPOSApplication.getContext().getResources().getString(R.string.employee);

        final EmployeeResponse[] employeeResponses = {null};
        HttpManager.get(employeeUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                employeeResponses[0] = VPOSRestTools.getInstance().getEmployeeResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "employeeResponses: "+resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "employeeResponses: "+employeeResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + responseString);
                employeeResponses[0] = null;

            }
        });
        return employeeResponses[0];
    }


    public void setAddEmployeeParams(AddEmployeeRequest addEmployeeRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(addEmployeeRequest);
            parsedJsonParams = new StringEntity(jsonParams);

            Log.d(TAG, jsonParams);

        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }


    public AddEmployeeResponse addEmployee() {
        String employeeUrl = VPOSApplication.getContext().getResources().getString(R.string.add_employee);
        if (LogFlag.bLogOn)Log.d(TAG, employeeUrl);
        final AddEmployeeResponse[] addEmployeeResponses = {null};

        HttpManager.post(employeeUrl, parsedJsonParams, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                addEmployeeResponses[0] = VPOSRestTools.getInstance().addEmployeeResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "addCustomerResponses: "+addEmployeeResponses[0].toString());
            }

        });
        return addEmployeeResponses[0];
    }

}


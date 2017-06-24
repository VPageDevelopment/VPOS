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
import com.vpage.vpos.pojos.employee.EmployeeLoginResponse;
import com.vpage.vpos.pojos.employee.EmployeeResponse;
import com.vpage.vpos.pojos.employee.UpdateEmployeeResponse;
import com.vpage.vpos.pojos.employee.addEmployee.AddEmployeeLoginRequest;
import com.vpage.vpos.pojos.employee.addEmployee.AddEmployeeRequest;
import com.vpage.vpos.pojos.employee.addEmployee.AddEmployeeResponse;
import com.vpage.vpos.pojos.giftCards.GiftCardResponse;
import com.vpage.vpos.pojos.giftCards.UpdateGiftCardResponse;
import com.vpage.vpos.pojos.giftCards.addGiftCards.AddGiftCardsRequest;
import com.vpage.vpos.pojos.giftCards.addGiftCards.AddGiftCardsResponse;
import com.vpage.vpos.pojos.item.ItemResponse;
import com.vpage.vpos.pojos.item.UpdateItemResponse;
import com.vpage.vpos.pojos.item.addItem.AddItemRequest;
import com.vpage.vpos.pojos.item.addItem.AddItemResponse;
import com.vpage.vpos.pojos.itemkits.ItemKitsResponse;
import com.vpage.vpos.pojos.itemkits.UpdateItemKitsResponse;
import com.vpage.vpos.pojos.itemkits.addItemKits.AddItemKitsRequest;
import com.vpage.vpos.pojos.itemkits.addItemKits.AddItemKitsResponse;
import com.vpage.vpos.pojos.sale.SaleResponse;
import com.vpage.vpos.pojos.sale.UpdateSaleResponse;
import com.vpage.vpos.pojos.sale.addSale.AddSaleRequest;
import com.vpage.vpos.pojos.sale.addSale.AddSaleResponse;
import com.vpage.vpos.pojos.storeConfig.StoreConfigResponse;
import com.vpage.vpos.pojos.storeConfig.UpdateStoreConfigRequest;
import com.vpage.vpos.pojos.storeConfig.UpdateStoreConfigResponse;
import com.vpage.vpos.pojos.storeConfigBarcode.StoreConfigBarcodeResponse;
import com.vpage.vpos.pojos.storeConfigBarcode.UpdateStoreConfigBarcodeRequest;
import com.vpage.vpos.pojos.storeConfigGeneral.StoreConfigGeneralResponse;
import com.vpage.vpos.pojos.storeConfigGeneral.UpdateStoreConfigGeneralRequest;
import com.vpage.vpos.pojos.storeConfigInvoice.StoreConfigInvoiceResponse;
import com.vpage.vpos.pojos.storeConfigInvoice.UpdateStoreConfigInvoiceRequest;
import com.vpage.vpos.pojos.storeConfigLocal.StoreConfigLocalResponse;
import com.vpage.vpos.pojos.storeConfigLocal.UpdateStoreConfigLocalRequest;
import com.vpage.vpos.pojos.storeConfigMail.StoreConfigMailResponse;
import com.vpage.vpos.pojos.storeConfigMail.UpdateStoreConfigMailRequest;
import com.vpage.vpos.pojos.storeConfigMessage.StoreConfigMessageResponse;
import com.vpage.vpos.pojos.storeConfigMessage.UpdateStoreConfigMessageRequest;
import com.vpage.vpos.pojos.storeConfigReceipt.StoreConfigReceiptResponse;
import com.vpage.vpos.pojos.storeConfigReceipt.UpdateStoreConfigReceiptRequest;
import com.vpage.vpos.pojos.supplier.SupplierResponse;
import com.vpage.vpos.pojos.supplier.UpdateSuppliersResponse;
import com.vpage.vpos.pojos.supplier.addSupplier.AddSupplierRequest;
import com.vpage.vpos.pojos.supplier.addSupplier.AddSupplierResponse;
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


    public SignInResponse getSignInResponse(SignInRequest signInRequest) {
        String signInUrl = VPOSApplication.getContext().getResources().getString(R.string.signIn);
        if (LogFlag.bLogOn)Log.d(TAG, signInUrl);
        final SignInResponse[] signInResponses = {null};
        HttpManager.setBasicAuthData(signInRequest.getUsername(),signInRequest.getPassword());
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

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);

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

        HttpManager.putWithEntity(customerUrl, parsedJsonParams, new JsonHttpResponseHandler() {

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


    public EmployeeLoginResponse getEmployeeLogin() {

        String employeeUrl = VPOSApplication.getContext().getResources().getString(R.string.employee_login);

        final EmployeeLoginResponse[] employeeResponses = {null};
        HttpManager.get(employeeUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                employeeResponses[0] = VPOSRestTools.getInstance().getEmployeeLoginResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "EmployeeLoginResponse: "+resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "EmployeeLoginResponse: "+employeeResponses[0].toString());
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

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);

        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }


    public void setAddEmployeeLoginParams(AddEmployeeLoginRequest addEmployeeLoginRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(addEmployeeLoginRequest);
            parsedJsonParams = new StringEntity(jsonParams);

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);

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


    public UpdateEmployeeResponse updateEmployee(String employeeId) {
        String employeeUrl = VPOSApplication.getContext().getResources().getString(R.string.update_employee);
        employeeUrl = employeeUrl.replace("{id}",employeeId);
        if (LogFlag.bLogOn)Log.d(TAG, employeeUrl);
        final UpdateEmployeeResponse[] updateEmployeeResponses = {null};

        HttpManager.putWithEntity(employeeUrl, parsedJsonParams, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateEmployeeResponses[0] = VPOSRestTools.getInstance().updateEmployeeResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, updateEmployeeResponses[0].toString());
            }

        });
        return updateEmployeeResponses[0];
    }


    public UpdateEmployeeResponse deleteEmployee(String employeeId) {

        String employeeUrl = VPOSApplication.getContext().getResources().getString(R.string.delete_employee);
        employeeUrl = employeeUrl.replace("{id}",employeeId);

        final UpdateEmployeeResponse[] updateEmployeeResponses = {null};
        HttpManager.delete(employeeUrl, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateEmployeeResponses[0] = VPOSRestTools.getInstance().updateEmployeeResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, updateEmployeeResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        return updateEmployeeResponses[0];
    }

    public ItemResponse getItem() {

        String itemUrl = VPOSApplication.getContext().getResources().getString(R.string.item);

        final ItemResponse[] itemResponses = {null};
        HttpManager.get(itemUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                itemResponses[0] = VPOSRestTools.getInstance().getItemResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "itemResponses: "+resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "itemResponses: "+itemResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + responseString);
                itemResponses[0] = null;

            }
        });
        return itemResponses[0];
    }


    public void setAddItemParams(AddItemRequest addItemRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(addItemRequest);
            parsedJsonParams = new StringEntity(jsonParams);

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);

        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }

    public AddItemResponse addItem() {
        String itemUrl = VPOSApplication.getContext().getResources().getString(R.string.add_item);
        if (LogFlag.bLogOn)Log.d(TAG, itemUrl);
        final AddItemResponse[] addItemResponses = {null};

        HttpManager.post(itemUrl, parsedJsonParams, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                addItemResponses[0] = VPOSRestTools.getInstance().addItemResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "addItemResponses: "+addItemResponses[0].toString());
            }

        });
        return addItemResponses[0];
    }


    public UpdateItemResponse updateItem(String itemId) {
        String itemUrl = VPOSApplication.getContext().getResources().getString(R.string.update_item);
        itemUrl = itemUrl.replace("{id}",itemId);
        if (LogFlag.bLogOn)Log.d(TAG, itemUrl);
        final UpdateItemResponse[] updateItemResponses = {null};

        HttpManager.post(itemUrl, parsedJsonParams, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                updateItemResponses[0] = VPOSRestTools.getInstance().updateItemResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "updateItemResponses: "+updateItemResponses[0].toString());
            }

        });
        return updateItemResponses[0];
    }


    public UpdateItemResponse deleteItem(String itemId) {

        String itemUrl = VPOSApplication.getContext().getResources().getString(R.string.delete_item);
        itemUrl = itemUrl.replace("{id}",itemId);

        final UpdateItemResponse[] updateItemResponses = {null};
        HttpManager.delete(itemUrl, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateItemResponses[0] = VPOSRestTools.getInstance().updateItemResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, updateItemResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        return updateItemResponses[0];
    }


    public ItemKitsResponse getItemKits() {

        String itemKitsUrl = VPOSApplication.getContext().getResources().getString(R.string.item_kits);

        final ItemKitsResponse[] itemKitsResponses = {null};
        HttpManager.get(itemKitsUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                itemKitsResponses[0] = VPOSRestTools.getInstance().getItemKitsResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "itemKitsResponses: "+itemKitsResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + responseString);
                itemKitsResponses[0] = null;

            }
        });
        return itemKitsResponses[0];
    }


    public void setAddItemKitsParams(AddItemKitsRequest addItemKitsRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(addItemKitsRequest);
            parsedJsonParams = new StringEntity(jsonParams);

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);

        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }


    public AddItemKitsResponse addItemKits() {
        String itemKitsUrl = VPOSApplication.getContext().getResources().getString(R.string.add_item_kits);
        if (LogFlag.bLogOn)Log.d(TAG, itemKitsUrl);
        final AddItemKitsResponse[] addItemKitsResponses = {null};

        HttpManager.post(itemKitsUrl, parsedJsonParams, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                addItemKitsResponses[0] = VPOSRestTools.getInstance().addItemKitsResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "addItemKitsResponses: "+addItemKitsResponses[0].toString());
            }

        });
        return addItemKitsResponses[0];
    }

    public UpdateItemKitsResponse updateItemKits(String itemKitId) {
        String itemKitsUrl = VPOSApplication.getContext().getResources().getString(R.string.update_item_kits);
        itemKitsUrl = itemKitsUrl.replace("{id}",itemKitId);
        if (LogFlag.bLogOn)Log.d(TAG, itemKitsUrl);
        final UpdateItemKitsResponse[] updateItemKitsResponses = {null};

        HttpManager.putWithEntity(itemKitsUrl, parsedJsonParams, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateItemKitsResponses[0] = VPOSRestTools.getInstance().updateItemKitsResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, updateItemKitsResponses[0].toString());
            }

        });
        return updateItemKitsResponses[0];
    }


    public UpdateItemKitsResponse deleteItemKit(String itemKitId) {

        String itemKitsUrl = VPOSApplication.getContext().getResources().getString(R.string.delete_item_kits);
        itemKitsUrl = itemKitsUrl.replace("{id}",itemKitId);

        final UpdateItemKitsResponse[] updateItemKitsResponses = {null};
        HttpManager.delete(itemKitsUrl, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateItemKitsResponses[0] = VPOSRestTools.getInstance().updateItemKitsResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, updateItemKitsResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        return updateItemKitsResponses[0];
    }


    public SaleResponse getScales() {

        String saleUrl = VPOSApplication.getContext().getResources().getString(R.string.sale);

        final SaleResponse[] saleResponses = {null};
        HttpManager.get(saleUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                saleResponses[0] = VPOSRestTools.getInstance().getSaleResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "saleResponses: "+resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "saleResponses: "+saleResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + responseString);
                saleResponses[0] = null;

            }
        });
        return saleResponses[0];
    }


    public void setAddSaleParams(AddSaleRequest addSaleRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(addSaleRequest);
            parsedJsonParams = new StringEntity(jsonParams);

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);

        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }


    public AddSaleResponse addSale() {
        String saleUrl = VPOSApplication.getContext().getResources().getString(R.string.add_sale);
        if (LogFlag.bLogOn)Log.d(TAG, saleUrl);
        final AddSaleResponse[] addSaleResponses = {null};

        HttpManager.post(saleUrl, parsedJsonParams, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                addSaleResponses[0] = VPOSRestTools.getInstance().addSaleResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "addSaleResponses: "+addSaleResponses[0].toString());
            }

        });
        return addSaleResponses[0];
    }


    public UpdateSaleResponse updateSale(String saleId) {
        String saleUrl = VPOSApplication.getContext().getResources().getString(R.string.update_sale);
        saleUrl = saleUrl.replace("{id}",saleId);
        if (LogFlag.bLogOn)Log.d(TAG, saleUrl);
        final UpdateSaleResponse[] updateItemKitsResponses = {null};

        HttpManager.putWithEntity(saleUrl, parsedJsonParams, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateItemKitsResponses[0] = VPOSRestTools.getInstance().updateSaleResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, updateItemKitsResponses[0].toString());
            }

        });
        return updateItemKitsResponses[0];
    }


    public UpdateSaleResponse deleteSale(String saleId) {

        String saleUrl = VPOSApplication.getContext().getResources().getString(R.string.delete_sale);
        saleUrl = saleUrl.replace("{id}",saleId);

        final UpdateSaleResponse[] updateItemKitsResponses = {null};
        HttpManager.delete(saleUrl, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateItemKitsResponses[0] = VPOSRestTools.getInstance().updateSaleResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, updateItemKitsResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        return updateItemKitsResponses[0];
    }

    public SupplierResponse getSuppliers() {

        String supplierUrl = VPOSApplication.getContext().getResources().getString(R.string.supplier);

        final SupplierResponse[] supplierResponses = {null};
        HttpManager.get(supplierUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                supplierResponses[0] = VPOSRestTools.getInstance().getSuppliersResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "supplierResponses: "+resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "supplierResponses: "+supplierResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + responseString);
                supplierResponses[0] = null;

            }
        });
        return supplierResponses[0];
    }

    public void setAddSupplierParams(AddSupplierRequest addSupplierRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(addSupplierRequest);
            parsedJsonParams = new StringEntity(jsonParams);

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);

        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }

    public AddSupplierResponse addSupplier() {
        String supplierUrl = VPOSApplication.getContext().getResources().getString(R.string.add_supplier);
        if (LogFlag.bLogOn)Log.d(TAG, supplierUrl);
        final AddSupplierResponse[] addSupplierResponses = {null};

        HttpManager.post(supplierUrl, parsedJsonParams, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                addSupplierResponses[0] = VPOSRestTools.getInstance().addSupplierResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "addSupplierResponses: "+addSupplierResponses[0].toString());
            }

        });
        return addSupplierResponses[0];
    }

    public UpdateSuppliersResponse updateSupplier(String supplierId) {
        String supplierUrl = VPOSApplication.getContext().getResources().getString(R.string.update_supplier);
        supplierUrl = supplierUrl.replace("{id}",supplierId);
        if (LogFlag.bLogOn)Log.d(TAG, supplierUrl);
        final UpdateSuppliersResponse[] updateSuppliersResponses = {null};

        HttpManager.putWithEntity(supplierUrl, parsedJsonParams, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateSuppliersResponses[0] = VPOSRestTools.getInstance().updateSupplierResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, updateSuppliersResponses[0].toString());
            }

        });
        return updateSuppliersResponses[0];
    }


    public UpdateSuppliersResponse deleteSupplier(String supplierId) {

        String supplierUrl = VPOSApplication.getContext().getResources().getString(R.string.delete_supplier);
        supplierUrl = supplierUrl.replace("{id}",supplierId);

        final UpdateSuppliersResponse[] updateSuppliersResponses = {null};
        HttpManager.delete(supplierUrl, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateSuppliersResponses[0] = VPOSRestTools.getInstance().updateSupplierResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, updateSuppliersResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        return updateSuppliersResponses[0];
    }


    public GiftCardResponse getGiftCards() {

        String giftCardUrl = VPOSApplication.getContext().getResources().getString(R.string.gift_cards);

        final GiftCardResponse[] giftCardResponses = {null};
        HttpManager.get(giftCardUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                giftCardResponses[0] = VPOSRestTools.getInstance().getGiftCardResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "giftCardResponses: "+resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "giftCardResponses: "+giftCardResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + responseString);
                giftCardResponses[0] = null;

            }
        });
        return giftCardResponses[0];
    }

    public void setAddGiftCardParams(AddGiftCardsRequest addGiftCardsRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(addGiftCardsRequest);
            parsedJsonParams = new StringEntity(jsonParams);

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);

        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }


    public AddGiftCardsResponse addGiftCard() {
        String giftCardUrl = VPOSApplication.getContext().getResources().getString(R.string.add_gift_cards);
        if (LogFlag.bLogOn)Log.d(TAG, giftCardUrl);
        final AddGiftCardsResponse[] addGiftCardsResponses = {null};

        HttpManager.post(giftCardUrl, parsedJsonParams, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                addGiftCardsResponses[0] = VPOSRestTools.getInstance().addGiftCardResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "addGiftCardsResponses: "+addGiftCardsResponses[0].toString());
            }

        });
        return addGiftCardsResponses[0];
    }

    public UpdateGiftCardResponse updateGiftCard(String giftCardId) {
        String giftCardUrl = VPOSApplication.getContext().getResources().getString(R.string.update_gift_cards);
        giftCardUrl = giftCardUrl.replace("{id}",giftCardId);
        if (LogFlag.bLogOn)Log.d(TAG, giftCardUrl);
        final UpdateGiftCardResponse[] updateGiftCardResponses = {null};

        HttpManager.putWithEntity(giftCardUrl, parsedJsonParams, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateGiftCardResponses[0] = VPOSRestTools.getInstance().updateGiftCardResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, updateGiftCardResponses[0].toString());
            }

        });
        return updateGiftCardResponses[0];
    }

    public UpdateGiftCardResponse deleteGiftCard(String giftCardId) {

        String giftCardUrl = VPOSApplication.getContext().getResources().getString(R.string.delete_gift_cards);
        giftCardUrl = giftCardUrl.replace("{id}",giftCardId);

        final UpdateGiftCardResponse[] updateGiftCardResponses = {null};
        HttpManager.delete(giftCardUrl, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateGiftCardResponses[0] = VPOSRestTools.getInstance().updateGiftCardResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, updateGiftCardResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        return updateGiftCardResponses[0];
    }


    public StoreConfigResponse getStoreConfigInfo() {

        String storeConfigUrl = VPOSApplication.getContext().getResources().getString(R.string.store_config);

        final StoreConfigResponse[] storeConfigResponses = {null};
        HttpManager.get(storeConfigUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                storeConfigResponses[0] = VPOSRestTools.getInstance().getStoreConfigResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "storeConfigResponses: "+resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "storeConfigResponses: "+storeConfigResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + responseString);
                storeConfigResponses[0] = null;

            }
        });
        return storeConfigResponses[0];
    }

    public void setUpdateStoreConfigParams(UpdateStoreConfigRequest updateStoreConfigRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(updateStoreConfigRequest);
            parsedJsonParams = new StringEntity(jsonParams);

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);

        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }


    public UpdateStoreConfigResponse updateStoreConfig() {
        String storeConfigUrl = VPOSApplication.getContext().getResources().getString(R.string.update_store_config);
        if (LogFlag.bLogOn)Log.d(TAG, storeConfigUrl);
        final UpdateStoreConfigResponse[] updateStoreConfigResponses = {null};

        HttpManager.putWithEntity(storeConfigUrl, parsedJsonParams, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateStoreConfigResponses[0] = VPOSRestTools.getInstance().updateStoreConfigResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "updateStoreConfigResponses: "+updateStoreConfigResponses[0].toString());
            }

        });
        return updateStoreConfigResponses[0];
    }


    public StoreConfigBarcodeResponse getStoreConfigBarcode() {

        String storeConfigBarcodeUrl = VPOSApplication.getContext().getResources().getString(R.string.store_config_barcode);

        final StoreConfigBarcodeResponse[] storeConfigBarcodeResponses = {null};
        HttpManager.get(storeConfigBarcodeUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                storeConfigBarcodeResponses[0] = VPOSRestTools.getInstance().getStoreConfigBarcodeResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "storeConfigBarcodeResponses: "+resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "storeConfigBarcodeResponses: "+storeConfigBarcodeResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + responseString);
                storeConfigBarcodeResponses[0] = null;

            }
        });
        return storeConfigBarcodeResponses[0];
    }


    public void setUpdateStoreConfigBarcodeParams(UpdateStoreConfigBarcodeRequest updateStoreConfigBarcodeRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(updateStoreConfigBarcodeRequest);
            parsedJsonParams = new StringEntity(jsonParams);

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);

        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }



    public UpdateStoreConfigResponse updateStoreConfigBarcode() {
        String storeConfigBarcodeUrl = VPOSApplication.getContext().getResources().getString(R.string.update_store_config_barcode);
        if (LogFlag.bLogOn)Log.d(TAG, storeConfigBarcodeUrl);
        final UpdateStoreConfigResponse[] updateStoreConfigBarcodeResponses = {null};

        HttpManager.putWithEntity(storeConfigBarcodeUrl, parsedJsonParams, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateStoreConfigBarcodeResponses[0] = VPOSRestTools.getInstance().updateStoreConfigResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "updateStoreConfigBarcodeResponses: "+updateStoreConfigBarcodeResponses[0].toString());
            }

        });
        return updateStoreConfigBarcodeResponses[0];
    }


    public StoreConfigGeneralResponse getStoreConfigGeneral() {

        String storeConfigGeneralUrl = VPOSApplication.getContext().getResources().getString(R.string.store_config_general);

        final StoreConfigGeneralResponse[] storeConfigGeneralResponses = {null};
        HttpManager.get(storeConfigGeneralUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                storeConfigGeneralResponses[0] = VPOSRestTools.getInstance().getStoreConfigGeneralResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "storeConfigGeneralResponses: "+resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "storeConfigGeneralResponses: "+storeConfigGeneralResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + responseString);
                storeConfigGeneralResponses[0] = null;

            }
        });
        return storeConfigGeneralResponses[0];
    }

    public void setUpdateStoreConfigGeneralParams(UpdateStoreConfigGeneralRequest updateStoreConfigGeneralRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(updateStoreConfigGeneralRequest);
            parsedJsonParams = new StringEntity(jsonParams);

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);

        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }


    public UpdateStoreConfigResponse updateStoreConfigGeneral() {
        String storeConfigGeneralUrl = VPOSApplication.getContext().getResources().getString(R.string.update_store_config_general);
        if (LogFlag.bLogOn)Log.d(TAG, storeConfigGeneralUrl);
        final UpdateStoreConfigResponse[] updateStoreConfigGeneralResponses = {null};

        HttpManager.putWithEntity(storeConfigGeneralUrl, parsedJsonParams, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateStoreConfigGeneralResponses[0] = VPOSRestTools.getInstance().updateStoreConfigResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "updateStoreConfigGeneralResponses: "+updateStoreConfigGeneralResponses[0].toString());
            }

        });
        return updateStoreConfigGeneralResponses[0];
    }

    public StoreConfigInvoiceResponse getStoreConfigInvoice() {

        String storeConfigInvoiceUrl = VPOSApplication.getContext().getResources().getString(R.string.store_config_invoice);

        final StoreConfigInvoiceResponse[] storeConfigInvoiceResponses = {null};
        HttpManager.get(storeConfigInvoiceUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                storeConfigInvoiceResponses[0] = VPOSRestTools.getInstance().getStoreConfigInvoiceResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "storeConfigInvoiceResponses: "+resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "storeConfigInvoiceResponses: "+storeConfigInvoiceResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + responseString);
                storeConfigInvoiceResponses[0] = null;

            }
        });
        return storeConfigInvoiceResponses[0];
    }


    public void setUpdateStoreConfigInvoiceParams(UpdateStoreConfigInvoiceRequest updateStoreConfigInvoiceRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(updateStoreConfigInvoiceRequest);
            parsedJsonParams = new StringEntity(jsonParams);

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);

        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }


    public UpdateStoreConfigResponse updateStoreConfigInvoice() {
        String storeConfigInvoiceUrl = VPOSApplication.getContext().getResources().getString(R.string.update_store_config_invoice);
        if (LogFlag.bLogOn)Log.d(TAG, storeConfigInvoiceUrl);
        final UpdateStoreConfigResponse[] updateStoreConfigInvoiceResponses = {null};

        HttpManager.putWithEntity(storeConfigInvoiceUrl, parsedJsonParams, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateStoreConfigInvoiceResponses[0] = VPOSRestTools.getInstance().updateStoreConfigResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "updateStoreConfigInvoiceResponses: "+updateStoreConfigInvoiceResponses[0].toString());
            }

        });
        return updateStoreConfigInvoiceResponses[0];
    }

    public StoreConfigLocalResponse getStoreConfigLocal() {

        String storeConfigLocalUrl = VPOSApplication.getContext().getResources().getString(R.string.store_config_local);

        final StoreConfigLocalResponse[] storeConfigLocalResponses = {null};
        HttpManager.get(storeConfigLocalUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                storeConfigLocalResponses[0] = VPOSRestTools.getInstance().getStoreConfigLocalResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "StoreConfigLocalResponse: "+resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "StoreConfigLocalResponse: "+storeConfigLocalResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + responseString);
                storeConfigLocalResponses[0] = null;

            }
        });
        return storeConfigLocalResponses[0];
    }


    public void setUpdateStoreConfigLocalParams(UpdateStoreConfigLocalRequest updateStoreConfigLocalRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(updateStoreConfigLocalRequest);
            parsedJsonParams = new StringEntity(jsonParams);

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);

        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }

    public UpdateStoreConfigResponse updateStoreConfigLocal() {
        String storeConfigLocalUrl = VPOSApplication.getContext().getResources().getString(R.string.update_store_config_local);
        if (LogFlag.bLogOn)Log.d(TAG, storeConfigLocalUrl);
        final UpdateStoreConfigResponse[] updateStoreConfigLocalResponses = {null};

        HttpManager.putWithEntity(storeConfigLocalUrl, parsedJsonParams, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateStoreConfigLocalResponses[0] = VPOSRestTools.getInstance().updateStoreConfigResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "updateStoreConfigLocalResponses: "+updateStoreConfigLocalResponses[0].toString());
            }

        });
        return updateStoreConfigLocalResponses[0];
    }


    public StoreConfigMailResponse getStoreConfigMail() {

        String storeConfigMailUrl = VPOSApplication.getContext().getResources().getString(R.string.store_config_mail);

        final StoreConfigMailResponse[] storeConfigLocalResponses = {null};
        HttpManager.get(storeConfigMailUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                storeConfigLocalResponses[0] = VPOSRestTools.getInstance().getStoreConfigMailResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "StoreConfigMailResponse: "+resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "StoreConfigMailResponse: "+storeConfigLocalResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + responseString);
                storeConfigLocalResponses[0] = null;

            }
        });
        return storeConfigLocalResponses[0];
    }


    public void setUpdateStoreConfigMailParams(UpdateStoreConfigMailRequest updateStoreConfigMailRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(updateStoreConfigMailRequest);
            parsedJsonParams = new StringEntity(jsonParams);

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);

        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }

    public UpdateStoreConfigResponse updateStoreConfigMail() {
        String storeConfigMailUrl = VPOSApplication.getContext().getResources().getString(R.string.update_store_config_mail);
        if (LogFlag.bLogOn)Log.d(TAG, storeConfigMailUrl);
        final UpdateStoreConfigResponse[] updateStoreConfigMailResponses = {null};

        HttpManager.putWithEntity(storeConfigMailUrl, parsedJsonParams, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateStoreConfigMailResponses[0] = VPOSRestTools.getInstance().updateStoreConfigResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "updateStoreConfigMailResponses: "+updateStoreConfigMailResponses[0].toString());
            }

        });
        return updateStoreConfigMailResponses[0];
    }


    public StoreConfigMessageResponse getStoreConfigMessage() {

        String storeConfigMessageUrl = VPOSApplication.getContext().getResources().getString(R.string.store_config_message);

        final StoreConfigMessageResponse[] storeConfigMessageResponses = {null};
        HttpManager.get(storeConfigMessageUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                storeConfigMessageResponses[0] = VPOSRestTools.getInstance().getStoreConfigMessageResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "storeConfigMessageResponses: "+resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "storeConfigMessageResponses: "+storeConfigMessageResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + responseString);
                storeConfigMessageResponses[0] = null;

            }
        });
        return storeConfigMessageResponses[0];
    }


    public void setUpdateStoreConfigMessageParams(UpdateStoreConfigMessageRequest updateStoreConfigMessageRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(updateStoreConfigMessageRequest);
            parsedJsonParams = new StringEntity(jsonParams);

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);

        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }

    public UpdateStoreConfigResponse updateStoreConfigMessage() {
        String storeConfigMessageUrl = VPOSApplication.getContext().getResources().getString(R.string.update_store_config_message);
        if (LogFlag.bLogOn)Log.d(TAG, storeConfigMessageUrl);
        final UpdateStoreConfigResponse[] updateStoreConfigMessageResponses = {null};

        HttpManager.putWithEntity(storeConfigMessageUrl, parsedJsonParams, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateStoreConfigMessageResponses[0] = VPOSRestTools.getInstance().updateStoreConfigResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "updateStoreConfigMessageResponses: "+updateStoreConfigMessageResponses[0].toString());
            }

        });
        return updateStoreConfigMessageResponses[0];
    }


    public StoreConfigReceiptResponse getStoreConfigReceipt() {

        String storeConfigReceiptUrl = VPOSApplication.getContext().getResources().getString(R.string.store_config_receipt);

        final StoreConfigReceiptResponse[] storeConfigReceiptResponses = {null};
        HttpManager.get(storeConfigReceiptUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                storeConfigReceiptResponses[0] = VPOSRestTools.getInstance().getStoreConfigReceiptResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "storeConfigReceiptResponses: "+resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "storeConfigReceiptResponses: "+storeConfigReceiptResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + responseString);
                storeConfigReceiptResponses[0] = null;

            }
        });
        return storeConfigReceiptResponses[0];
    }

    public void setUpdateStoreConfigReceiptParams(UpdateStoreConfigReceiptRequest updateStoreConfigReceiptRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(updateStoreConfigReceiptRequest);
            parsedJsonParams = new StringEntity(jsonParams);

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);

        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }


    public UpdateStoreConfigResponse updateStoreConfigReceipt() {
        String storeConfigReceiptUrl = VPOSApplication.getContext().getResources().getString(R.string.update_store_config_receipt);
        if (LogFlag.bLogOn)Log.d(TAG, storeConfigReceiptUrl);
        final UpdateStoreConfigResponse[] updateStoreConfigReceiptResponses = {null};

        HttpManager.putWithEntity(storeConfigReceiptUrl, parsedJsonParams, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                updateStoreConfigReceiptResponses[0] = VPOSRestTools.getInstance().updateStoreConfigResponseData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, "updateStoreConfigReceiptResponses: "+updateStoreConfigReceiptResponses[0].toString());
            }

        });
        return updateStoreConfigReceiptResponses[0];
    }

}



package com.vpage.vpos.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vpos.pojos.SignInResponse;
import com.vpage.vpos.pojos.customer.Customers;
import com.vpage.vpos.pojos.customer.CustomersResponse;
import com.vpage.vpos.pojos.customer.UpdateCustomersResponse;
import com.vpage.vpos.pojos.customer.addCustomer.AddCustomerResponse;
import com.vpage.vpos.pojos.employee.EmployeeLoginResponse;
import com.vpage.vpos.pojos.employee.EmployeeResponse;
import com.vpage.vpos.pojos.employee.Employees;
import com.vpage.vpos.pojos.employee.UpdateEmployeeResponse;
import com.vpage.vpos.pojos.employee.addEmployee.AddEmployeeResponse;
import com.vpage.vpos.pojos.giftCards.GiftCard;
import com.vpage.vpos.pojos.giftCards.GiftCardResponse;
import com.vpage.vpos.pojos.giftCards.UpdateGiftCardResponse;
import com.vpage.vpos.pojos.giftCards.addGiftCards.AddGiftCardsResponse;
import com.vpage.vpos.pojos.item.ItemResponse;
import com.vpage.vpos.pojos.item.Items;
import com.vpage.vpos.pojos.item.UpdateItemResponse;
import com.vpage.vpos.pojos.item.addItem.AddItemResponse;
import com.vpage.vpos.pojos.itemkits.ItemKits;
import com.vpage.vpos.pojos.itemkits.ItemKitsResponse;
import com.vpage.vpos.pojos.itemkits.UpdateItemKitsResponse;
import com.vpage.vpos.pojos.itemkits.addItemKits.AddItemKitsResponse;
import com.vpage.vpos.pojos.sale.SaleResponse;
import com.vpage.vpos.pojos.sale.UpdateSaleResponse;
import com.vpage.vpos.pojos.sale.addSale.AddSaleResponse;
import com.vpage.vpos.pojos.storeConfig.StoreConfigResponse;
import com.vpage.vpos.pojos.storeConfig.UpdateStoreConfigResponse;
import com.vpage.vpos.pojos.storeConfigBarcode.StoreConfigBarcodeResponse;
import com.vpage.vpos.pojos.storeConfigGeneral.StoreConfigGeneralResponse;
import com.vpage.vpos.pojos.storeConfigInvoice.StoreConfigInvoiceResponse;
import com.vpage.vpos.pojos.storeConfigLocal.StoreConfigLocalResponse;
import com.vpage.vpos.pojos.storeConfigMail.StoreConfigMailResponse;
import com.vpage.vpos.pojos.storeConfigMessage.StoreConfigMessageResponse;
import com.vpage.vpos.pojos.storeConfigReceipt.StoreConfigReceiptResponse;
import com.vpage.vpos.pojos.supplier.SupplierResponse;
import com.vpage.vpos.pojos.supplier.Suppliers;
import com.vpage.vpos.pojos.supplier.UpdateSuppliersResponse;
import com.vpage.vpos.pojos.supplier.addSupplier.AddSupplierResponse;


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

    public Customers getCustomerData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, Customers.class);
    }


    public EmployeeResponse getEmployeeResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, EmployeeResponse.class);
    }

    public EmployeeLoginResponse getEmployeeLoginResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, EmployeeLoginResponse.class);
    }

    public AddEmployeeResponse addEmployeeResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, AddEmployeeResponse.class);
    }

    public UpdateEmployeeResponse updateEmployeeResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, UpdateEmployeeResponse.class);
    }

    public Employees getEmployeeData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, Employees.class);
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

    public Items getItemData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, Items.class);
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

    public ItemKits getItemKitData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, ItemKits.class);
    }

    public SaleResponse getSaleResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, SaleResponse.class);
    }

    public AddSaleResponse addSaleResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, AddSaleResponse.class);
    }

    public UpdateSaleResponse updateSaleResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, UpdateSaleResponse.class);
    }

    public SupplierResponse getSuppliersResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, SupplierResponse.class);
    }

    public AddSupplierResponse addSupplierResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, AddSupplierResponse.class);
    }

    public UpdateSuppliersResponse updateSupplierResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, UpdateSuppliersResponse.class);
    }

    public Suppliers getSuppliersData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, Suppliers.class);
    }


    public GiftCardResponse getGiftCardResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, GiftCardResponse.class);
    }

    public AddGiftCardsResponse addGiftCardResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, AddGiftCardsResponse.class);
    }


    public UpdateGiftCardResponse updateGiftCardResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, UpdateGiftCardResponse.class);
    }

    public GiftCard getGiftCardData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, GiftCard.class);
    }

    public StoreConfigResponse getStoreConfigResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, StoreConfigResponse.class);
    }

    public UpdateStoreConfigResponse updateStoreConfigResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, UpdateStoreConfigResponse.class);
    }


    public StoreConfigBarcodeResponse getStoreConfigBarcodeResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, StoreConfigBarcodeResponse.class);
    }

    public StoreConfigGeneralResponse getStoreConfigGeneralResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, StoreConfigGeneralResponse.class);
    }

    public StoreConfigInvoiceResponse getStoreConfigInvoiceResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, StoreConfigInvoiceResponse.class);
    }


    public StoreConfigLocalResponse getStoreConfigLocalResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, StoreConfigLocalResponse.class);
    }


    public StoreConfigMailResponse getStoreConfigMailResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, StoreConfigMailResponse.class);
    }


    public StoreConfigMessageResponse getStoreConfigMessageResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, StoreConfigMessageResponse.class);
    }


    public StoreConfigReceiptResponse getStoreConfigReceiptResponseData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, StoreConfigReceiptResponse.class);
    }



}

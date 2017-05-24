package com.vpage.vpos.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.httputils.VPOSRestClient;
import com.vpage.vpos.pojos.item.ItemResponse;
import com.vpage.vpos.pojos.item.UpdateItemResponse;
import com.vpage.vpos.pojos.item.addItem.AddItemRequest;
import com.vpage.vpos.tools.ActionEditText;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.VPOSRestTools;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.utils.LogFlag;
import com.vpage.vpos.tools.utils.NetworkUtil;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_editmultipleitem)
public class EditMultipleItemActivity extends AppCompatActivity implements View.OnClickListener, OnNetworkChangeListener,AdapterView.OnItemSelectedListener {

    private static final String TAG = EditMultipleItemActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.itemName)
    EditText itemName;

    @ViewById(R.id.textError)
    TextView textError;

    @ViewById(R.id.category)
    EditText category;

    @ViewById(R.id.spinnerSupplier)
    Spinner spinnerSupplier;

    @ViewById(R.id.costPrice)
    EditText costPrice;

    @ViewById(R.id.retailPrice)
    EditText retailPrice;

    @ViewById(R.id.tax1)
    EditText tax1;

    @ViewById(R.id.tax1Percent)
    EditText tax1Percent;

    @ViewById(R.id.tax2)
    EditText tax2;

    @ViewById(R.id.tax2Percent)
    EditText tax2Percent;

    @ViewById(R.id.reorderLevel)
    EditText reorderLevel;

    @ViewById(R.id.description)
    EditText description;

    @ViewById(R.id.spinnerAllowAlt)
    Spinner spinnerAllowAlt;

    @ViewById(R.id.spinnerSerialNo)
    Spinner spinnerSerialNo;

    @ViewById(R.id.submitButton)
    Button submitButton;

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    String itemNameInput = "", categoryInput = "", costPriceInput = "", retailPriceInput = "", spinnerSupplierData = "",
            reorderLevelInput = "",spinnerAllowAltData = "",spinnerSerialNoData="";

    String descriptionData="",taxOne="",taxTwo="";

    boolean isNetworkAvailable = false;

    TextWatcher textDescription;

    Activity activity;

    ItemResponse itemResponse;
    AddItemRequest addItemRequest;

    int[] selectedPosition;

    @AfterViews
    public void onInitView() {

        activity = EditMultipleItemActivity.this;

        setActionBarSupport();

        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);
        submitButton.setOnClickListener(this);


        spinnerSupplier.setOnItemSelectedListener(this);
        spinnerAllowAlt.setOnItemSelectedListener(this);
        spinnerSerialNo.setOnItemSelectedListener(this);

        Intent callingIntent=getIntent();

        String ItemResponseString = callingIntent.getStringExtra("ItemResponse");
        selectedPosition = callingIntent.getIntArrayExtra("SelectedPosition");

        itemResponse = VPOSRestTools.getInstance().getItemResponseData(ItemResponseString);

        setView();
    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Editing Multiple Items");

    }


    private void setView() {

        textDescription = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // you can check for enter key here
            }

            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 150) {
                    VTools.showAlertDialog(activity, "Character Exceed the Limit");
                }
            }
        };

        new ActionEditText(this);
        description.addTextChangedListener(textDescription);
    }


    @Override
    public void onClick(View v) {

                getInputs();
                validateInput();
    }


    @FocusChange({ R.id.itemName, R.id.category, R.id.costPrice, R.id.retailPrice, R.id.tax1,
            R.id.tax1Percent, R.id.tax2, R.id.tax2Percent, R.id.reorderLevel})
    public void focusChangedOnUser(View v, boolean hasFocus) {
        if (hasFocus) {
            textError.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (LogFlag.bLogOn) Log.d(TAG, "Back Pressed ");
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    void getInputs() {

        taxOne = tax1.getText().toString();
        tax1Percent.getText().toString();
        taxTwo = tax2.getText().toString();
        tax2Percent.getText().toString();
        descriptionData = description.getText().toString();
        spinnerSupplierData = spinnerSupplier.getSelectedItem().toString();
        spinnerAllowAltData = spinnerAllowAlt.getSelectedItem().toString();
        spinnerSerialNoData = spinnerSerialNo.getSelectedItem().toString();

    }


    void validateInput() {

        if (isNetworkAvailable) {

            itemNameInput = itemName.getText().toString();
            categoryInput = category.getText().toString();
            costPriceInput = costPrice.getText().toString();
            retailPriceInput = retailPrice.getText().toString();
            reorderLevelInput = reorderLevel.getText().toString();


            if (!itemNameInput.isEmpty() && !categoryInput.isEmpty() && !costPriceInput.isEmpty() &&
                    !retailPriceInput.isEmpty() && !reorderLevelInput.isEmpty()) {

                playGifView.setVisibility(View.VISIBLE);
                textError.setVisibility(View.GONE);

                for(int i =0; i< selectedPosition.length;i++){
                    callItemUpdateResponse(itemResponse.getItems()[selectedPosition[i]].getItem_id());
                }

            } else {

                setErrorMessage("Fill all Required Input");
            }

        } else {

            setErrorMessage("Check Network Connection");
        }
    }


    void setErrorMessage(String errorMessage) {
        playGifView.setVisibility(View.GONE);
        textError.setVisibility(View.VISIBLE);
        textError.setText(errorMessage);
    }

    @Override
    public void onChange(String status) {
        if (LogFlag.bLogOn) Log.d(TAG, "Network Availability: " + status);
        switch (status) {
            case "Connected to Internet with Mobile Data":
                isNetworkAvailable = true;
                break;
            case "Connected to Internet with WIFI":
                isNetworkAvailable = true;
                break;
            default:
                isNetworkAvailable = false;
                break;
        }
        if (LogFlag.bLogOn) Log.d(TAG, "isNetworkAvailable: " + isNetworkAvailable);
    }


    public void checkInternetStatus() {
        String status = NetworkUtil.getConnectivityStatusString(getApplicationContext());
        switch (status) {
            case "Connected to Internet with Mobile Data":
                isNetworkAvailable = true;
                break;
            case "Connected to Internet with WIFI":
                isNetworkAvailable = true;
                break;
            default:
                isNetworkAvailable = false;
                break;
        }
        if (LogFlag.bLogOn) Log.d(TAG, "isNetworkAvailable: " + isNetworkAvailable);

    }


    private void gotoItemView() {
        Intent intent = new Intent(getApplicationContext(), ItemActivity_.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (view.getId()) {

            case R.id.spinnerSupplier:

                spinnerSupplierData = spinnerSupplier.getSelectedItem().toString();
                if (LogFlag.bLogOn) Log.d(TAG, "spinnerSupplierData: " + spinnerSupplierData);

                break;

            case R.id.spinnerAllowAlt:

                spinnerAllowAltData = spinnerAllowAlt.getSelectedItem().toString();
                if (LogFlag.bLogOn) Log.d(TAG, "spinnerAllowAltData: " + spinnerAllowAltData);

                break;

            case R.id.spinnerSerialNo:

                spinnerSerialNoData = spinnerSerialNo.getSelectedItem().toString();
                if (LogFlag.bLogOn) Log.d(TAG, "spinnerSerialNoData: " + spinnerSerialNoData);

                break;

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Background
    void callItemUpdateResponse(String itemId) {

        if (LogFlag.bLogOn)Log.d(TAG, "callItemUpdateResponse");
        setAddItemRequestData();

        VPOSRestClient vposRestClient = new VPOSRestClient();
        vposRestClient.setAddItemParams(addItemRequest);
        UpdateItemResponse updateItemResponse = vposRestClient.updateItem(itemId);
        if (null != updateItemResponse && updateItemResponse.getStatus().equals("true")) {
            if (LogFlag.bLogOn)Log.d(TAG, "updateItemResponse: " + updateItemResponse.toString());
            hideLoaderGifImage();
            addItemResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("updateItemResponse failed");
        }
    }


    @UiThread
    public void addItemResponseFinish(){
        gotoItemView();
    }

    @UiThread
    public void showToastErrorMsg(String error) {
        VTools.showToast(error);
    }


    @UiThread
    public void hideLoaderGifImage(){
        playGifView.setVisibility(View.GONE);
    }


    void  setAddItemRequestData(){

        addItemRequest = new AddItemRequest();

        addItemRequest.setItem_name(itemNameInput);
        addItemRequest.setCategory(categoryInput);
        addItemRequest.setSupplier_fk(spinnerSupplierData);
        addItemRequest.setCost_price(costPriceInput);
        addItemRequest.setRetail_price(retailPriceInput);
        addItemRequest.setDescription(descriptionData);
        addItemRequest.setTax_one(taxOne);
        addItemRequest.setTax_two(taxTwo);
        addItemRequest.setAllow_alt_description(spinnerAllowAltData);
        addItemRequest.setItem_has_serial_number(spinnerSerialNoData);

    }
}

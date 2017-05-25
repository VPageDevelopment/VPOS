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
import com.vpage.vpos.pojos.item.Items;
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
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_updateinventory)
public class UpdateInventoryActivity extends AppCompatActivity implements View.OnClickListener,OnNetworkChangeListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = UpdateInventoryActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.inventory)
    EditText inventory;

    @ViewById(R.id.comments)
    EditText comments;

    @ViewById(R.id.barcodeText)
    TextView barcodeText;

    @ViewById(R.id.itemNameText)
    TextView itemNameText;

    @ViewById(R.id.categoryText)
    TextView categoryText;

    @ViewById(R.id.currentQuantityText)
    TextView currentQuantityText;

    @ViewById(R.id.textError)
    TextView textError;

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    @ViewById(R.id.submitButton)
    Button submitButton;

    @ViewById(R.id.spinnerStock)
    Spinner spinnerStock;

    String inventoryInput = "",spinnerStockData="",commentsInput;

    TextWatcher textComments;

    Activity activity;

    boolean isNetworkAvailable = false;

    Items items;
    AddItemRequest addItemRequest;

    @AfterViews
    public void onInitView() {

        activity = UpdateInventoryActivity.this;

        setActionBarSupport();
        setView();
        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);
        spinnerStock.setOnItemSelectedListener(this);

        submitButton.setOnClickListener(this);

        Intent callingIntent=getIntent();

        String itemResponseString = callingIntent.getStringExtra("ItemData");

        items = VPOSRestTools.getInstance().getItemData(itemResponseString);
        setInputs();

    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Update Inventory");

    }

    private void setView(){

        textComments = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // you can check for enter key here
            }

            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 150) {
                    VTools.showAlertDialog(activity,"Character Exceed the Limit");
                }
            }
        };

        new ActionEditText(this);
        comments.addTextChangedListener(textComments);
    }

    void getInputs(){

        barcodeText.getText().toString();
        itemNameText.getText().toString();
        categoryText.getText().toString();
        currentQuantityText.getText().toString();
        commentsInput = comments.getText().toString();
        spinnerStockData = spinnerStock.getSelectedItem().toString();

    }

    void setInputs(){

        barcodeText.setText(items.getUpc_ean_isbn());
        itemNameText.setText(items.getItem_name());
        categoryText.setText(items.getCategory());
        currentQuantityText.setText(items.getQuantity_stock());
        comments.setText(items.getDescription());
        spinnerStock.setPrompt(items.getSupplier_fk());

    }


    void validateInput(){

        if(isNetworkAvailable){

            inventoryInput =  inventory.getText().toString();
            if (inventoryInput.isEmpty()) {
                setErrorMessage("Fill all Required Input");
                return;
            }

            playGifView.setVisibility(View.VISIBLE);
            textError.setVisibility(View.GONE);

            callItemUpdateResponse(items.getItem_id());

        }else {

            setErrorMessage("Check Network Connection");
        }
    }

    void setErrorMessage(String errorMessage) {
        playGifView.setVisibility(View.GONE);
        textError.setVisibility(View.VISIBLE);
        textError.setText(errorMessage);
    }


    @Override
    public void onClick(View v) {
        getInputs();
        validateInput();
    }

    @Override
    public void onChange(String status) {
        if (LogFlag.bLogOn) Log.d(TAG, "Network Availability: "+status);
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
        if (LogFlag.bLogOn)Log.d(TAG, "isNetworkAvailable: "+isNetworkAvailable);
    }


    public  void checkInternetStatus(){
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
        if (LogFlag.bLogOn)Log.d(TAG, "isNetworkAvailable: "+isNetworkAvailable);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerStockData = spinnerStock.getSelectedItem().toString();
        if (LogFlag.bLogOn)Log.d(TAG, "spinnerStockData: " + spinnerStockData);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

        addItemRequest.setSupplier_fk(spinnerStockData);
        addItemRequest.setDescription(commentsInput);

    }

    private void gotoItemView(){
        Intent intent = new Intent(getApplicationContext(), ItemActivity_.class);
        startActivity(intent);
        finish();
    }

}

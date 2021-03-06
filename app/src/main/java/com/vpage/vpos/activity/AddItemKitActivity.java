package com.vpage.vpos.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.httputils.VPOSRestClient;
import com.vpage.vpos.pojos.itemkits.ItemKits;
import com.vpage.vpos.pojos.itemkits.UpdateItemKitsResponse;
import com.vpage.vpos.pojos.itemkits.addItemKits.AddItemKitsRequest;
import com.vpage.vpos.pojos.itemkits.addItemKits.AddItemKitsResponse;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

@EActivity(R.layout.activity_additemkit)
public class AddItemKitActivity extends AppCompatActivity implements View.OnClickListener, OnNetworkChangeListener, View.OnKeyListener {

    private static final String TAG = AddItemKitActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.itemKitName)
    EditText itemKitName;

    @ViewById(R.id.textError)
    TextView textError;

    @ViewById(R.id.addItem)
    EditText addItem;

    @ViewById(R.id.description)
    EditText description;

    @ViewById(R.id.submitButton)
    Button submitButton;

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    @InjectView(R.id.google_progress)
    ProgressBar mProgressBar;

    String itemKitNameInput = "",itemKitDescInput="",itemKitItemInput="";

    boolean isNetworkAvailable = false;

    TextWatcher textDescription;

    String pageName = "";

    Activity activity;

    ItemKits itemKits;
    AddItemKitsRequest addItemKitsRequest;

    @AfterViews
    public void onInitView() {

        activity = AddItemKitActivity.this;

        Intent callingIntent=getIntent();

        pageName = callingIntent.getStringExtra("PageName");

        if(pageName.equals("Update Item")){

            String itemKitResponseString = callingIntent.getStringExtra("ItemKitData");

            itemKits = VPOSRestTools.getInstance().getItemKitData(itemKitResponseString);
            setInputs();
        }

        setActionBarSupport();

        ButterKnife.inject(this);

        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);
        addItem.setOnKeyListener(this);
        submitButton.setOnClickListener(this);

        setView();
    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(pageName);

    }


    @Override
    protected void onResume() {
        super.onResume();
        /**Dynamically*/
        Rect bounds = mProgressBar.getIndeterminateDrawable().getBounds();
        mProgressBar.setIndeterminateDrawable(VTools.getProgressDrawable(activity));
        mProgressBar.getIndeterminateDrawable().setBounds(bounds);
    }

    private void setView(){

        textDescription = new TextWatcher() {
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
        description.addTextChangedListener(textDescription);
    }


    @Override
    public void onClick(View v) {
                validateInput();
    }


    @FocusChange({R.id.itemKitName, R.id.addItem,R.id.description})
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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == EditorInfo.IME_ACTION_GO ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            validateInput();

        }
        return false;
    }

    void setInputs(){

        addItem.setText(itemKits.getItem_fk());
        description.setText(itemKits.getItem_kit_desc());
        itemKitName.setText(itemKits.getItem_kit_name());
    }


    void validateInput(){

        if(isNetworkAvailable){

            itemKitItemInput = addItem.getText().toString();
            itemKitDescInput = description.getText().toString();
            itemKitNameInput = itemKitName.getText().toString();
            if (itemKitNameInput.isEmpty()) {
                setErrorMessage("Fill all Required Input");
                return;
            }

                //playGifView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.GONE);
                textError.setVisibility(View.GONE);

            if(pageName.equals("Update Item Kit")){
                callItemKitUpdateResponse(itemKits.getItem_kit_id());
            }else {
                callAddItemKitResponse();
            }

        }else {
            setErrorMessage("Check Network Connection");
        }
    }


    void setErrorMessage(String errorMessage) {
        //playGifView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
        textError.setVisibility(View.VISIBLE);
        textError.setText(errorMessage);
    }

    @Override
    public void onChange(String status) {
        if (LogFlag.bLogOn)Log.d(TAG, "Network Availability: "+status);
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


    @Background
    void callAddItemKitResponse() {
        if (LogFlag.bLogOn)Log.d(TAG, "callAddItemKitResponse");
        setAddItemKitRequestData();

        VPOSRestClient vposRestClient = new VPOSRestClient();
        vposRestClient.setAddItemKitsParams(addItemKitsRequest);
        AddItemKitsResponse addItemKitsResponse = vposRestClient.addItemKits();
        if (null != addItemKitsResponse && addItemKitsResponse.getStatus().equals("true")) {
            if (LogFlag.bLogOn)Log.d(TAG, "addItemKitsResponse: " + addItemKitsResponse.toString());
            hideLoaderGifImage();
            addCustomerResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("addItemKitsResponse failed");
        }
    }

    @Background
    void callItemKitUpdateResponse(String itemKitId) {

        if (LogFlag.bLogOn)Log.d(TAG, "callItemKitUpdateResponse");
        setAddItemKitRequestData();

        VPOSRestClient vposRestClient = new VPOSRestClient();
        vposRestClient.setAddItemKitsParams(addItemKitsRequest);
        UpdateItemKitsResponse updateItemKitsResponse = vposRestClient.updateItemKits(itemKitId);
        if (null != updateItemKitsResponse && updateItemKitsResponse.getStatus().equals("true")) {
            if (LogFlag.bLogOn)Log.d(TAG, "updateItemKitsResponse: " + updateItemKitsResponse.toString());
            hideLoaderGifImage();
            addCustomerResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("updateItemKitsResponse failed");
        }
    }


    @UiThread
    public void addCustomerResponseFinish(){
        gotoItemKitView();
    }

    @UiThread
    public void hideLoaderGifImage(){
       // playGifView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
    }

    @UiThread
    public void showToastErrorMsg(String error) {
        VTools.showToast(error);
    }

    void  setAddItemKitRequestData(){

        addItemKitsRequest = new AddItemKitsRequest();
        addItemKitsRequest.setItem_kit_name(itemKitNameInput);
        addItemKitsRequest.setItem_kit_desc(itemKitDescInput);
        addItemKitsRequest.setItem_fk(itemKitItemInput);
    }


    private void gotoItemKitView(){
        Intent intent = new Intent(getApplicationContext(), ItemKitActivity_.class);
        startActivity(intent);
        finish();
    }

}

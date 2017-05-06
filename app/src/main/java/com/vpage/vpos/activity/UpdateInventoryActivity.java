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
import com.vpage.vpos.tools.ActionEditText;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.utils.LogFlag;
import com.vpage.vpos.tools.utils.NetworkUtil;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
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

    String inventoryInput = "",spinnerStockData="";

    TextWatcher textComments;

    Activity activity;

    boolean isNetworkAvailable = false;

    @AfterViews
    public void onInitView() {

        activity = UpdateInventoryActivity.this;

        setActionBarSupport();
        setView();
        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);
        spinnerStock.setOnItemSelectedListener(this);

        submitButton.setOnClickListener(this);
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

        barcodeText.setText("");  // To do data from server response
        itemNameText.setText("");  // To do data from server response
        categoryText.setText("");  // To do data from server response
        currentQuantityText.setText("");  // To do data from server response
        comments.getText().toString();
        spinnerStockData = spinnerStock.getSelectedItem().toString();

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

            // TODO Service call
            gotoItemView();

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

    private void gotoItemView(){
        Intent intent = new Intent(getApplicationContext(), ItemActivity_.class);
        startActivity(intent);
        finish();
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
}

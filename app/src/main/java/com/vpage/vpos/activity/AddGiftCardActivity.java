package com.vpage.vpos.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.tools.ActionEditText;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.utils.LogFlag;
import com.vpage.vpos.tools.utils.NetworkUtil;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

@Fullscreen
@EActivity(R.layout.activity_addgiftcard)
public class AddGiftCardActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener, View.OnFocusChangeListener, OnNetworkChangeListener {

    private static final String TAG = AddGiftCardActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.customer)
    EditText customer;

    @ViewById(R.id.giftCardNo)
    EditText giftCardNo;

    @ViewById(R.id.value)
    EditText value;

    @ViewById(R.id.textError)
    TextView textError;

    @ViewById(R.id.submitButton)
    Button submitButton;

    boolean isNetworkAvailable = false;

    String giftCardNoInput="", valueInput ="";

    String pageName = " ";

    Activity activity;

    @AfterViews

    public void init() {

        activity = AddGiftCardActivity.this;

        Intent callingIntent=getIntent();
        pageName = callingIntent.getStringExtra("PageName");

        setActionBarSupport();
        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);

        new ActionEditText(this);
        submitButton.setOnClickListener(this);
        value.setOnKeyListener(this);
        value.setOnFocusChangeListener(this);
        giftCardNo.setOnFocusChangeListener(this);
        customer.setOnFocusChangeListener(this);

    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(pageName);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onClick(View v) {
        validateInput();
    }

    void validateInput(){
        if(isNetworkAvailable){

            // TODO Data update from service call
            giftCardNoInput = giftCardNo.getText().toString();
            valueInput = value.getText().toString();
            customer.getText().toString();

            if (!giftCardNoInput.isEmpty()&& !valueInput.isEmpty()) {

                textError.setVisibility(View.GONE);

                // TODO Service call
                gotoGiftCardView();

            } else {
                setErrorMessage("Fill all Required Input");
            }
        }else {

            setErrorMessage("Check Network Connection");
        }



    }

    void setErrorMessage(String errorMessage) {

        textError.setVisibility(View.VISIBLE);
        textError.setText(errorMessage);
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


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            textError.setVisibility(View.GONE);
        }
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

    private void gotoGiftCardView(){
        Intent intent = new Intent(getApplicationContext(), GiftCardActivity_.class);
        startActivity(intent);
        finish();
    }


}



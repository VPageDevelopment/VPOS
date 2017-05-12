package com.vpage.vpos.activity;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.pojos.ValidationStatus;
import com.vpage.vpos.tools.ActionEditText;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.utils.LogFlag;
import com.vpage.vpos.tools.utils.NetworkUtil;
import com.vpage.vpos.tools.utils.ValidationUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_addcustomer)
public class AddCustomerActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener, OnNetworkChangeListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = AddCustomerActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.firstName)
    EditText firstName;

    @ViewById(R.id.lastName)
    EditText lastName;

    @ViewById(R.id.textError)
    TextView textError;

    @ViewById(R.id.radioGroupGender)
    RadioGroup radioGroupGender;

    @ViewById(R.id.radioButtonMale)
    RadioButton radioButtonMale;

    @ViewById(R.id.radioButtonFemale)
    RadioButton radioButtonFemale;

    @ViewById(R.id.email)
    EditText email;

    @ViewById(R.id.phoneNumber)
    EditText phoneNumber;

    @ViewById(R.id.addressLine1)
    EditText addressLine1;

    @ViewById(R.id.addressLine2)
    EditText addressLine2;

    @ViewById(R.id.city)
    EditText city;

    @ViewById(R.id.state)
    EditText state;

    @ViewById(R.id.zip)
    EditText zip;

    @ViewById(R.id.country)
    EditText country;

    @ViewById(R.id.comments)
    EditText comments;

    @ViewById(R.id.company)
    EditText company;

    @ViewById(R.id.account)
    EditText account;

    @ViewById(R.id.total)
    EditText total;

    @ViewById(R.id.discount)
    EditText discount;

    @ViewById(R.id.taxableCheckBox)
    CheckBox taxableCheckBox;

    @ViewById(R.id.submitButton)
    Button submitButton;

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    String firstNameInput = "", lastNameInput = "",genderSelected = "Male",phoneNumberInput="";
    ValidationStatus validationStatus,validationStatusPhoneNumber;

    boolean isNetworkAvailable = false;

    TextWatcher textComments;

    String pageName = " ";

    Activity activity;

    @AfterViews
    public void onInitView() {

        activity = AddCustomerActivity.this;

        Intent callingIntent=getIntent();

        pageName = callingIntent.getStringExtra("PageName");

        setActionBarSupport();

        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);
        lastName.setOnKeyListener(this);

        taxableCheckBox.setChecked(false);

        taxableCheckBox.setOnCheckedChangeListener(this);
        radioButtonMale.setOnClickListener(this);
        radioButtonFemale.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        setView();
    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if(pageName.equals("New Sales Customer")){
            getSupportActionBar().setTitle("New Customer");
        }else {
            getSupportActionBar().setTitle(pageName);
        }
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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.submitButton:
                getInputs();
                validateInput();
                break;

            case R.id.radioButtonMale:

                radioButtonMale.setChecked(true);
                radioButtonFemale.setChecked(false);
                genderSelected = "Male";

                break;

            case R.id.radioButtonFemale:

                radioButtonMale.setChecked(false);
                radioButtonFemale.setChecked(true);
                genderSelected = "Female";

                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == EditorInfo.IME_ACTION_GO ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            switch (v.getId()) {

                case R.id.lastName:
                    getInputs();
                    validateInput();

                    break;

            }

        }
        return false;
    }

    @FocusChange({R.id.firstName, R.id.lastName,R.id.email, R.id.phoneNumber,R.id.addressLine1, R.id.addressLine2,
            R.id.city, R.id.state,R.id.zip, R.id.country,R.id.comments,R.id.account,R.id.total,R.id.company,R.id.discount})
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            taxableCheckBox.setChecked(true);
        }else {
            taxableCheckBox.setChecked(false);
        }
    }

    void getInputs(){

        email.getText().toString();
        addressLine1.getText().toString();
        addressLine2.getText().toString();
        city.getText().toString();
        state.getText().toString();
        zip.getText().toString();
        country.getText().toString();
        comments.getText().toString();
        company.getText().toString();
        account.getText().toString();
        total.getText().toString();
        discount.getText().toString();

    }

    void validateInput(){

        if(isNetworkAvailable){

            firstNameInput = firstName.getText().toString();
            lastNameInput = lastName.getText().toString();

            validationStatus = ValidationUtils.isValidName(firstNameInput,lastNameInput);

            if (!validationStatus.isStatus()) {
                setErrorMessage(validationStatus.getMessage());
                return;
            }

            phoneNumberInput= phoneNumber.getText().toString();
            if(!phoneNumberInput.isEmpty()){
                validationStatusPhoneNumber =  ValidationUtils.isValidUserPhoneNumber(phoneNumberInput);
                if (!validationStatusPhoneNumber.isStatus()) {
                    if (LogFlag.bLogOn)Log.d(TAG, validationStatusPhoneNumber.getMessage());
                    setErrorMessage(validationStatusPhoneNumber.getMessage());
                    return;
                }
            }

                playGifView.setVisibility(View.VISIBLE);
                textError.setVisibility(View.GONE);

            // TODO Service call
            if(pageName.equals("New Sales Customer")){
                gotoSalesView();
            }else {
                gotoCustomerView();
            }

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

    private void gotoCustomerView(){
        Intent intent = new Intent(getApplicationContext(), CustomerActivity_.class);
        startActivity(intent);
        finish();
    }

    private void gotoSalesView(){
        Intent intent = new Intent(getApplicationContext(), SalesActivity_.class);
        startActivity(intent);
        finish();
    }

}
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.httputils.VPOSRestClient;
import com.vpage.vpos.pojos.ValidationStatus;
import com.vpage.vpos.pojos.itemkits.addItemKits.AddItemKitsResponse;
import com.vpage.vpos.pojos.supplier.Suppliers;
import com.vpage.vpos.pojos.supplier.UpdateSuppliersResponse;
import com.vpage.vpos.pojos.supplier.addSupplier.AddSupplierRequest;
import com.vpage.vpos.pojos.supplier.addSupplier.AddSupplierResponse;
import com.vpage.vpos.tools.ActionEditText;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.VPOSRestTools;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.utils.LogFlag;
import com.vpage.vpos.tools.utils.NetworkUtil;
import com.vpage.vpos.tools.utils.ValidationUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_addsupplier)
public class AddSupplierActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener, OnNetworkChangeListener{

    private static final String TAG = AddSupplierActivity.class.getName();

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

    @ViewById(R.id.companyName)
    EditText company;

    @ViewById(R.id.account)
    EditText account;

    @ViewById(R.id.agencyName)
    EditText agencyName;

    @ViewById(R.id.submitButton)
    Button submitButton;

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    String firstNameInput = "", lastNameInput = "",companyNameInput="",genderSelected = "Male",phoneNumberInput ="";

    String emailInput="",addressLine1Input="",addressLine2Input="",cityInput="",stateInput="",zipInput="",
            countryInput="",commentsInput="",accountInput="",agencyInput="";

    ValidationStatus validationStatusPhoneNumber;

    boolean isNetworkAvailable = false;

    TextWatcher textComments;

    String pageName = " ";

    Activity activity;

    AddSupplierRequest addSupplierRequest;
    Suppliers suppliers;

    @AfterViews
    public void onInitView() {

        activity = AddSupplierActivity.this;

        Intent callingIntent=getIntent();

        pageName = callingIntent.getStringExtra("PageName");

        if(pageName.equals("Update Supplier")){

            String supplierResponseString = callingIntent.getStringExtra("SupplierData");

            suppliers = VPOSRestTools.getInstance().getSuppliersData(supplierResponseString);
            setInputs();
        }

        setActionBarSupport();

        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);
        lastName.setOnKeyListener(this);
        account.setOnKeyListener(this);
        radioButtonMale.setOnClickListener(this);
        radioButtonFemale.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        setView();
    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(pageName);

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

                case R.id.account:
                    getInputs();
                    validateInput();

                    break;

            }

        }
        return false;
    }

    @FocusChange({R.id.firstName, R.id.lastName,R.id.email, R.id.phoneNumber,R.id.addressLine1, R.id.addressLine2,
            R.id.city, R.id.state,R.id.zip, R.id.country,R.id.comments,R.id.account,R.id.agencyName,R.id.company})
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

    void getInputs(){


        emailInput = email.getText().toString();
        addressLine1Input = addressLine1.getText().toString();
        addressLine2Input = addressLine2.getText().toString();
        cityInput = city.getText().toString();
        stateInput = state.getText().toString();
        zipInput = zip.getText().toString();
        countryInput = country.getText().toString();
        commentsInput = comments.getText().toString();
        accountInput = account.getText().toString();
        agencyInput = agencyName.getText().toString();

    }

    void setInputs(){

        email.setText(suppliers.getEmail());
        addressLine1.setText(suppliers.getAddress_one());
        addressLine2.setText(suppliers.getAddress_two());
        city.setText(suppliers.getCity());
        state.setText(suppliers.getState());
        zip.setText(suppliers.getZip());
        country.setText(suppliers.getCountry());
        comments.setText(suppliers.getComments());
        account.setText(suppliers.getAccount());
        agencyName.setText(suppliers.getAgency_name());

        String genderSelectedData = suppliers.getGender();
        if(genderSelectedData.equals("M")){
            genderSelected = "Male";
            radioButtonMale.setChecked(true);
            radioButtonFemale.setChecked(false);
        }else {

            genderSelected = "Female";
            radioButtonMale.setChecked(false);
            radioButtonFemale.setChecked(true);
        }
    }

    void validateInput(){

        if(isNetworkAvailable){

            firstNameInput = firstName.getText().toString();
            lastNameInput = lastName.getText().toString();
            companyNameInput = company.getText().toString();


            if (companyNameInput.isEmpty()&&firstNameInput.isEmpty() &&lastNameInput.isEmpty()) {
                setErrorMessage("Fill all Required Input");
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

            if(pageName.equals("Update Supplier")){
                callSupplierUpdateResponse(suppliers.getSupplier_id());
            }else {
                callAddSupplierResponse();
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

    @Background
    void callAddSupplierResponse() {
        if (LogFlag.bLogOn)Log.d(TAG, "callAddSupplierResponse");
        setAddSupplierRequestData();

        VPOSRestClient vposRestClient = new VPOSRestClient();
        vposRestClient.setAddSupplierParams(addSupplierRequest);
        AddSupplierResponse addSupplierResponse = vposRestClient.addSupplier();
        if (null != addSupplierResponse) {
            if (LogFlag.bLogOn)Log.d(TAG, "addSupplierResponse: " + addSupplierResponse.toString());
            hideLoaderGifImage();
            addSupplierResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("addSupplierResponse failed");
        }
    }


    @Background
    void callSupplierUpdateResponse(String supplierId) {
        if (LogFlag.bLogOn)Log.d(TAG, "callSupplierUpdateResponse");
        setAddSupplierRequestData();

        VPOSRestClient vposRestClient = new VPOSRestClient();
        vposRestClient.setAddSupplierParams(addSupplierRequest);
        UpdateSuppliersResponse updateSuppliersResponse = vposRestClient.updateSupplier(supplierId);
        if (null != updateSuppliersResponse && updateSuppliersResponse.getStatus().equals("true")) {
            if (LogFlag.bLogOn)Log.d(TAG, "updateSuppliersResponse: " + updateSuppliersResponse.toString());
            hideLoaderGifImage();
            addSupplierResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("updateSuppliersResponse failed");
        }
    }


    @UiThread
    public void addSupplierResponseFinish(){
        gotoSupplierView();
    }

    @UiThread
    public void hideLoaderGifImage(){
        playGifView.setVisibility(View.GONE);
    }

    @UiThread
    public void showToastErrorMsg(String error) {
        VTools.showToast(error);
    }


    void  setAddSupplierRequestData(){

        addSupplierRequest = new AddSupplierRequest();
        addSupplierRequest.setCompany_name(companyNameInput);
        addSupplierRequest.setAgency_name(agencyInput);
        addSupplierRequest.setFirst_name(firstNameInput);
        addSupplierRequest.setLast_name(lastNameInput);
        addSupplierRequest.setGender(genderSelected);
        addSupplierRequest.setEmail(emailInput);
        addSupplierRequest.setPhone_number(phoneNumberInput);
        addSupplierRequest.setAddress_one(addressLine1Input);
        addSupplierRequest.setAddress_two(addressLine2Input);
        addSupplierRequest.setCity(cityInput);
        addSupplierRequest.setState(stateInput);
        addSupplierRequest.setZip(zipInput);
        addSupplierRequest.setCountry(countryInput);
        addSupplierRequest.setComments(commentsInput);
        addSupplierRequest.setAccount(accountInput);
    }


    private void gotoSupplierView(){
        Intent intent = new Intent(getApplicationContext(), SupplierActivity_.class);
        startActivity(intent);
        finish();
    }

}

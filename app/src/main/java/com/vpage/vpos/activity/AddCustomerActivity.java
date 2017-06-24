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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.httputils.VPOSRestClient;
import com.vpage.vpos.pojos.ValidationStatus;
import com.vpage.vpos.pojos.customer.Customers;
import com.vpage.vpos.pojos.customer.UpdateCustomersResponse;
import com.vpage.vpos.pojos.customer.addCustomer.AddCustomerRequest;
import com.vpage.vpos.pojos.customer.addCustomer.AddCustomerResponse;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

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

    @InjectView(R.id.google_progress)
    ProgressBar mProgressBar;

    String firstNameInput = "", lastNameInput = "",genderSelected = "Male",phoneNumberInput="";

    String emailInput="",addressLine1Input="",addressLine2Input="",cityInput="",stateInput="",zipInput="",
            countryInput="",commentsInput="",companyInput="",accountInput="",totalInput="",discountInput="",taxableMode="N";

    ValidationStatus validationStatus,validationStatusPhoneNumber;

    boolean isNetworkAvailable = false;

    TextWatcher textComments;

    String pageName = " ";

    Activity activity;

    AddCustomerRequest addCustomerRequest;

    Customers customers;

    @AfterViews
    public void onInitView() {

        activity = AddCustomerActivity.this;

        Intent callingIntent=getIntent();

        pageName = callingIntent.getStringExtra("PageName");

        setActionBarSupport();

        ButterKnife.inject(this);
        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);

        lastName.setOnKeyListener(this);

        taxableCheckBox.setChecked(false);

        taxableCheckBox.setOnCheckedChangeListener(this);
        radioButtonMale.setOnClickListener(this);
        radioButtonFemale.setOnClickListener(this);
        submitButton.setOnClickListener(this);


        if(pageName.equals("Update Customer")){

            String customerResponseString = callingIntent.getStringExtra("customerData");

            customers = VPOSRestTools.getInstance().getCustomerData(customerResponseString);
            setInputs();
        }

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
    protected void onResume() {
        super.onResume();
        /**Dynamically*/
        Rect bounds = mProgressBar.getIndeterminateDrawable().getBounds();
        mProgressBar.setIndeterminateDrawable(VTools.getProgressDrawable(activity));
        mProgressBar.getIndeterminateDrawable().setBounds(bounds);
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
            taxableMode = "Y";
        }else {
            taxableCheckBox.setChecked(false);
            taxableMode = "N";
        }
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
        companyInput = company.getText().toString();
        accountInput = account.getText().toString();
        totalInput = total.getText().toString();
        discountInput = discount.getText().toString();

    }


    void setInputs(){

        firstName.setText(customers.getFirst_name());
        lastName.setText(customers.getLast_name());
        phoneNumber.setText(customers.getPhone_number());

        email.setText(customers.getEmail());
        addressLine1.setText(customers.getAddress_one());
        addressLine2.setText(customers.getAddress_two());
        city.setText(customers.getCity());
        state.setText(customers.getState());
        zip.setText(customers.getZip());
        country.setText(customers.getCountry());
        comments.setText(customers.getComments());
        company.setText(customers.getCompany());
        account.setText(customers.getAccount());
        total.setText(customers.getTotal());
        discount.setText(customers.getDiscount());
        String genderSelectedData = customers.getGender();
        if(genderSelectedData.equals("M")){
            genderSelected = "Male";
            radioButtonMale.setChecked(true);
            radioButtonFemale.setChecked(false);
        }else {

            genderSelected = "Female";
            radioButtonMale.setChecked(false);
            radioButtonFemale.setChecked(true);
        }

        String taxableData = customers.getTaxable();
        if(taxableData.equals("Y")){
            taxableCheckBox.setChecked(true);
            taxableMode = "Y";
        }else {
            taxableCheckBox.setChecked(false);
            taxableMode = "N";
        }

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

               // playGifView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                textError.setVisibility(View.GONE);
                submitButton.setVisibility(View.GONE);

            if(pageName.equals("Update Customer")){
                callCustomerUpdateResponse(customers.getCustomer_id());
            }else {
                callAddCustomerResponse();
            }
        }else {
            setErrorMessage("Check Network Connection");
        }
    }

    void setErrorMessage(String errorMessage) {
        // playGifView.setVisibility(View.GONE);
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
    void callAddCustomerResponse() {
        if (LogFlag.bLogOn)Log.d(TAG, "callAddCustomerResponse");
        setAddCustomerRequestData();

        VPOSRestClient vposRestClient = new VPOSRestClient();
        vposRestClient.setAddCustomerParams(addCustomerRequest);
        AddCustomerResponse addCustomerResponse = vposRestClient.addCustomer();
        if (null != addCustomerResponse&& addCustomerResponse.getStatus().equals("true")) {
            if (LogFlag.bLogOn)Log.d(TAG, "addCustomerResponse: " + addCustomerResponse.toString());
            hideLoaderGifImage();
            addCustomerResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("addCustomerResponse failed");
        }
    }


    @Background
    void callCustomerUpdateResponse(String customerId) {
        if (LogFlag.bLogOn)Log.d(TAG, "callCustomerUpdateResponse");
        setAddCustomerRequestData();

        VPOSRestClient vposRestClient = new VPOSRestClient();
        vposRestClient.setAddCustomerParams(addCustomerRequest);
        UpdateCustomersResponse updateCustomersResponse = vposRestClient.updateCustomer(customerId);
        if (null != updateCustomersResponse && updateCustomersResponse.getStatus().equals("true")) {
            if (LogFlag.bLogOn)Log.d(TAG, "updateCustomersResponse: " + updateCustomersResponse.toString());
            hideLoaderGifImage();
            addCustomerResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("updateCustomersResponse failed");
        }
    }

    @UiThread
    public void addCustomerResponseFinish(){
        if(pageName.equals("New Sales Customer")){
            gotoSalesView();
        }else {
            gotoCustomerView();
        }
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


    void  setAddCustomerRequestData(){

        addCustomerRequest = new AddCustomerRequest();
        addCustomerRequest.setFirst_name(firstNameInput);
        addCustomerRequest.setLast_name(lastNameInput);
        addCustomerRequest.setGender(genderSelected);
        addCustomerRequest.setEmail(emailInput);
        addCustomerRequest.setPhone_number(phoneNumberInput);
        addCustomerRequest.setAddress_one(addressLine1Input);
        addCustomerRequest.setAddress_two(addressLine2Input);
        addCustomerRequest.setCity(cityInput);
        addCustomerRequest.setState(stateInput);
        addCustomerRequest.setZip(zipInput);
        addCustomerRequest.setCountry(countryInput);
        addCustomerRequest.setComments(commentsInput);
        addCustomerRequest.setCompany(companyInput);
        addCustomerRequest.setAccount(accountInput);
        addCustomerRequest.setTotal(totalInput);
        addCustomerRequest.setDiscount(discountInput);
        addCustomerRequest.setTaxable(taxableMode);

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
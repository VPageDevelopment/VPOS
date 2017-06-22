package com.vpage.vpos.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.adapter.EmpCheckBoxExpandableListAdapter;
import com.vpage.vpos.adapter.ExpListViewAdapter;
import com.vpage.vpos.httputils.VPOSRestClient;
import com.vpage.vpos.pojos.ValidationStatus;
import com.vpage.vpos.pojos.employee.Employees;
import com.vpage.vpos.pojos.employee.UpdateEmployeeResponse;
import com.vpage.vpos.pojos.employee.addEmployee.AddEmployeeRequest;
import com.vpage.vpos.pojos.employee.addEmployee.AddEmployeeResponse;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

@EActivity(R.layout.activity_addemployee)
public class AddEmployeeActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener, OnNetworkChangeListener {

    private static final String TAG = AddEmployeeActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.tabLayout)
    TabLayout tabLayout;

    @ViewById(R.id.informationLayout)
    LinearLayout informationLayout;

    @ViewById(R.id.loginLayout)
    LinearLayout loginLayout;

    @ViewById(R.id.permissionLayout)
    LinearLayout permissionLayout;

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

    @ViewById(R.id.userName)
    EditText userName;

    @ViewById(R.id.password)
    EditText password;

    @ViewById(R.id.confPassword)
    EditText confPassword;

    @ViewById(R.id.expandableListView)
    ExpandableListView expListView;

    @ViewById(R.id.submitButton)
    Button submitButton;

    @InjectView(R.id.google_progress)
    ProgressBar mProgressBar;

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    String firstNameInput = "", lastNameInput = "",phoneNumberInput="",genderSelected = "Male",
            userNameInput = "", passwordWordInput = "",confPasswordWordInput = "";
    ValidationStatus validationStatus,validationStatusUserName,validationStatusPassword,
            validationStatusPhoneNumber,validationStatusUserNameAndPassword;
    String emailInput="",addressLine1Input="",addressLine2Input="",cityInput="",stateInput="",zipInput="",
            countryInput="",commentsInput="";


    int tabPosition;

    boolean isNetworkAvailable = false;

    TextWatcher textComments;

    String pageName = " ";

    Activity activity;

    ExpListViewAdapter listAdapter;
    List<String> listDataHeader;
    Map<String, List<String>> listDataChild;


    Employees employees;
    AddEmployeeRequest addEmployeeRequest;

    @AfterViews
    public void onInitView() {

        activity = AddEmployeeActivity.this;

        Intent callingIntent=getIntent();

        pageName = callingIntent.getStringExtra("PageName");

        setActionBarSupport();


        ButterKnife.inject(this);
        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);
        lastName.setOnKeyListener(this);
        comments.setOnKeyListener(this);
        radioButtonMale.setOnClickListener(this);
        radioButtonFemale.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        if(pageName.equals("Update Employee")){

            String employeeResponseString = callingIntent.getStringExtra("employeeData");

            employees = VPOSRestTools.getInstance().getEmployeeData(employeeResponseString);
            setInputs();
        }

        setView();
        setExpandableListViewData();
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

        firstName.requestFocus();

        firstName.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(firstName.getWindowToken(), 0);
            }
        },200);
    }

    void setInputs(){

        firstName.setText(employees.getFirst_name());
        lastName.setText(employees.getLast_name());
        phoneNumber.setText(employees.getPhone_number());

        email.setText(employees.getEmail());
        addressLine1.setText(employees.getAddress_one());
        addressLine2.setText(employees.getAddress_two());
        city.setText(employees.getCity());
        state.setText(employees.getState());
        zip.setText(employees.getZip());
        country.setText(employees.getCountry());
        comments.setText(employees.getComments());
        String genderSelectedData = employees.getGender();
        if(genderSelectedData.equals("M")){
            genderSelected = "Male";
            radioButtonMale.setChecked(true);
            radioButtonFemale.setChecked(false);
        }else {

            genderSelected = "Female";
            radioButtonMale.setChecked(false);
            radioButtonFemale.setChecked(true);
        }

        userName.setText(employees.getUsername());
        password.setText(employees.getPassword());
        confPassword.setText(employees.getPassword());

    }

    private void setView(){
        tabLayout.addTab(tabLayout.newTab().setText("Information"));
        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Permission"));
        tabLayout.setTabTextColors(R.color.DarkGray,R.color.colorPrimary);
        tabPosition =0;
        setViewOnTabChange();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (LogFlag.bLogOn)Log.d(TAG, "onTabSelected: "+tab.getPosition());
                tabPosition = tab.getPosition();
                setViewOnTabChange();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });



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

                case R.id.comments:
                    getInputs();
                    validateInput();

                    break;


            }

        }
        return false;
    }

    @FocusChange({R.id.firstName, R.id.lastName,R.id.email, R.id.phoneNumber,R.id.addressLine1, R.id.addressLine2,
            R.id.city, R.id.state,R.id.zip, R.id.country,R.id.comments})
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

    void setViewOnTabChange(){
        if(tabPosition == 0){
            informationLayout.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
            permissionLayout.setVisibility(View.GONE);
        }else if(tabPosition == 1){
            informationLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
            permissionLayout.setVisibility(View.GONE);
        }else if(tabPosition == 2){
            informationLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.GONE);
            permissionLayout.setVisibility(View.VISIBLE);
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

        userNameInput = userName.getText().toString();
        passwordWordInput = password.getText().toString();
        confPasswordWordInput = confPassword.getText().toString();

    }

    void validateInput(){

        if(isNetworkAvailable){

            firstNameInput = firstName.getText().toString();
            lastNameInput = lastName.getText().toString();
            phoneNumberInput= phoneNumber.getText().toString();

            userNameInput = userName.getText().toString();
            passwordWordInput = password.getText().toString();
            confPasswordWordInput = confPassword.getText().toString();

            validationStatus = ValidationUtils.isValidName(firstNameInput,lastNameInput);

            validationStatusUserName =  ValidationUtils.isValidUserUserName(userNameInput);

            validationStatusPassword =  ValidationUtils.isValidPassword(passwordWordInput);

            validationStatusUserNameAndPassword = ValidationUtils.isValidLoginUserNamePassword(userNameInput, passwordWordInput);

            if (!validationStatus.isStatus()) {
                setErrorMessage(validationStatus.getMessage());
                return;
            }



            if(!phoneNumberInput.equals("")){

                validationStatusPhoneNumber =  ValidationUtils.isValidUserPhoneNumber(phoneNumberInput);

                if (!validationStatusPhoneNumber.isStatus()) {
                    if (LogFlag.bLogOn)Log.d(TAG, validationStatusPhoneNumber.getMessage());
                    setErrorMessage(validationStatusPhoneNumber.getMessage());
                    return;
                }
            }


            if (!validationStatusUserName.isStatus()) {
                if (LogFlag.bLogOn)Log.d(TAG, validationStatusUserName.getMessage());
                setErrorMessage(validationStatusUserName.getMessage());
                return;
            }

            if (!validationStatusPassword.isStatus()) {
                if (LogFlag.bLogOn)Log.d(TAG, validationStatusPassword.getMessage());
                setErrorMessage(validationStatusPassword.getMessage());
                return;
            }

            if (!validationStatusUserNameAndPassword.isStatus()) {
                setErrorMessage(validationStatusUserNameAndPassword.getMessage());
                return;
            }

            if (!passwordWordInput.equals(confPasswordWordInput)) {
                setErrorMessage("Password doesn’t match");
                return;
            }

                //playGifView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.GONE);
                textError.setVisibility(View.GONE);

            if(pageName.equals("Update Employee")){
                callEmployeeUpdateResponse(employees.getEmployee_id());
            }else {
                callAddEmployeeResponse();
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

    void submitBasedOnTabChange(){
        if(tabPosition == 0){
            getInputs();
            validateInput();
        }else if(tabPosition == 2){

            // TO DO UI to fix one check box selected
            collapseAll();
        }
    }

    void setExpandableListViewData(){


        expListView.setDividerHeight(2);
        expListView.setGroupIndicator(null);
        expListView.setClickable(true);

        // TODO Service data update
         // preparing list data
       // prepareListData();

  /*       listAdapter = new ExpListViewAdapter(activity, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);*/

        EmpCheckBoxExpandableListAdapter mAdapter = new EmpCheckBoxExpandableListAdapter(activity);
        expListView.setAdapter(mAdapter);
        expListView.setOnChildClickListener(mAdapter);
        expListView.setOnGroupClickListener(mAdapter);
        expListView.setGroupIndicator(null);


        /*expandAll();


        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
           *//* Toast.makeText(getApplicationContext(),
                    listDataHeader.get(groupPosition) + " Expanded",
                    Toast.LENGTH_SHORT).show(); *//*
                // if (LogFlag.bLogOn)Log.d(TAG, "mChildCheckStates: "+mParentCheckStates.get(groupPosition)[mChildPosition]);

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
           *//* Toast.makeText(getApplicationContext(),
                    listDataHeader.get(groupPosition) + " Collapsed",
                    Toast.LENGTH_SHORT).show(); *//*
                expListView.expandGroup(groupPosition);
            }
        });
*/
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
    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            expListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            expListView.collapseGroup(i);
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }


    @Background
    void callAddEmployeeResponse() {
        if (LogFlag.bLogOn)Log.d(TAG, "callAddEmployeeResponse");
        setAddEmployeeRequestData();

        VPOSRestClient vposRestClient = new VPOSRestClient();
        vposRestClient.setAddEmployeeParams(addEmployeeRequest);
        AddEmployeeResponse addEmployeeResponse = vposRestClient.addEmployee();
        if (null != addEmployeeResponse && addEmployeeResponse.getStatus().equals("true")) {
            if (LogFlag.bLogOn)Log.d(TAG, "addEmployeeResponse: " + addEmployeeResponse.toString());
            hideLoaderGifImage();
            addEmployeeResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("addEmployeeResponse failed");
        }
    }

    @UiThread
    public void addEmployeeResponseFinish(){
        gotoEmployeeView();

    }

    @Background
    void callEmployeeUpdateResponse(String employeeId) {
        if (LogFlag.bLogOn)Log.d(TAG, "callEmployeeUpdateResponse");
        setAddEmployeeRequestData();

        VPOSRestClient vposRestClient = new VPOSRestClient();
        vposRestClient.setAddEmployeeParams(addEmployeeRequest);
        UpdateEmployeeResponse updateEmployeeResponse = vposRestClient.updateEmployee(employeeId);
        if (null != updateEmployeeResponse && updateEmployeeResponse.getStatus().equals("true")) {
            if (LogFlag.bLogOn)Log.d(TAG, "updateEmployeeResponse: " + updateEmployeeResponse.toString());
            hideLoaderGifImage();
            addEmployeeResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("updateEmployeeResponse failed");
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


    void  setAddEmployeeRequestData(){

        addEmployeeRequest = new AddEmployeeRequest();
        addEmployeeRequest.setFirst_name(firstNameInput);
        addEmployeeRequest.setLast_name(lastNameInput);
        addEmployeeRequest.setGender(genderSelected);
        addEmployeeRequest.setEmail(emailInput);
        addEmployeeRequest.setPhone_number(phoneNumberInput);
        addEmployeeRequest.setAddress_one(addressLine1Input);
        addEmployeeRequest.setAddress_two(addressLine2Input);
        addEmployeeRequest.setCity(cityInput);
        addEmployeeRequest.setState(stateInput);
        addEmployeeRequest.setZip(zipInput);
        addEmployeeRequest.setCountry(countryInput);
        addEmployeeRequest.setComments(commentsInput);

    }

    private void gotoEmployeeView(){
        Intent intent = new Intent(getApplicationContext(), EmployeeActivity_.class);
        startActivity(intent);
        finish();
    }

}

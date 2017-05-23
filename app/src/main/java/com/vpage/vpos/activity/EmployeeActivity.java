package com.vpage.vpos.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.vpage.vpos.R;
import com.vpage.vpos.adapter.EmployeeListAdapter;
import com.vpage.vpos.adapter.FieldSpinnerAdapter;
import com.vpage.vpos.adapter.ListAdapter;
import com.vpage.vpos.httputils.VPOSRestClient;
import com.vpage.vpos.pojos.CustomerResponse;
import com.vpage.vpos.pojos.ValidationStatus;
import com.vpage.vpos.pojos.employee.EmployeeResponse;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.RecyclerTouchListener;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.callBack.CheckedCallBack;
import com.vpage.vpos.tools.callBack.EditCallBack;
import com.vpage.vpos.tools.callBack.FilterCallBack;
import com.vpage.vpos.tools.callBack.RecyclerTouchCallBack;
import com.vpage.vpos.tools.callBack.SendSmsCallBack;
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
import java.util.List;

@EActivity(R.layout.activity_employee)
public class EmployeeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, FilterCallBack, EditCallBack, CheckedCallBack, View.OnKeyListener,OnNetworkChangeListener, SendSmsCallBack {

    private static final String TAG = EmployeeActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.noEmployeeContent)
    LinearLayout noEmployeeContentLayout;

    @ViewById(R.id.employeeContent)
    LinearLayout employeeContent;

    @ViewById(R.id.addNewEmployeeButton)
    Button addNewEmployeeButton;

    @ViewById(R.id.addEmployeeButton)
    Button addEmployeeButton;

    @ViewById(R.id.spinnerField)
    Spinner spinnerField;

    @ViewById(R.id.spinnerFormat)
    Spinner spinnerFormat;

    @ViewById(R.id.employeeRecycleView)
    RecyclerView recyclerView;

    @ViewById(R.id.fabMenu)
    FloatingActionMenu floatingActionMenu;

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    FloatingActionButton deleteFAB,emailFAB;

    String spinnerFormatData = "";
    private int mScrollOffset = 4;

    EmployeeListAdapter listAdapter;
    FieldSpinnerAdapter fieldSpinnerAdapter;

    private Handler mUiHandler = new Handler();

    List<CustomerResponse> list;
    List<String> spinnerList;

    Boolean checkedStatus = false;
    private List<Boolean> checkedPositionArrayList = new ArrayList<>();

    Activity activity;

    String pageName ="Employee";

    private PopupWindow smsPopUp;
    TextView textError,firstNameText,lastNameText;
    EditText smsPhoneNumber,smsComments;
    String phoneNumberInput="",commentsInput="";
    boolean isNetworkAvailable = false;
    ValidationStatus validationStatusPhoneNumber;

    EmployeeResponse employeeResponse;

    @AfterViews
    public void onInitView() {

        activity = EmployeeActivity.this;

        setActionBarSupport();

        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);


        int customerCount = 1; // to test placed static data replaced by server response count
        customerCountCheck(customerCount);

    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Employees");

    }

    void customerCountCheck(int customerCount){

        if(customerCount == 0){
            noEmployeeContentLayout.setVisibility(View.VISIBLE);
            employeeContent.setVisibility(View.GONE);
            floatingActionMenu.setVisibility(View.GONE);
            addNewEmployeeButton.setOnClickListener(this);
        }else {
            noEmployeeContentLayout.setVisibility(View.GONE);
            employeeContent.setVisibility(View.VISIBLE);
            floatingActionMenu.setVisibility(View.VISIBLE);
            addEmployeeButton.setOnClickListener(this);

            callEmployeeResponse();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.addNewEmployeeButton:
                gotoAddEmployeeView("New Employee");
                break;

            case R.id.addEmployeeButton:
                gotoAddEmployeeView("New Employee");
                break;

            case R.id.submitButton:
                validateInput();
                break;

            case R.id.btnClose:
                smsPopUp.dismiss();
                break;
        }

    }

    private void addItemsOnSpinner() {
        try{
            spinnerList = new ArrayList<>();
            spinnerList.add("Filter By");
            spinnerList.add("Id");
            spinnerList.add("First Name");
            spinnerList.add("Last Name");
            spinnerList.add("Email");
            spinnerList.add("Phone Number");

            fieldSpinnerAdapter = new FieldSpinnerAdapter(activity, R.layout.item_spinner_field, spinnerList,pageName);
            fieldSpinnerAdapter.setFilterCallBack(this);
            spinnerField.setAdapter(fieldSpinnerAdapter);
            spinnerFormatData = spinnerFormat.getSelectedItem().toString();
            if (LogFlag.bLogOn) Log.d(TAG, "spinnerFormatData: " + spinnerFormatData);

            spinnerFormat.setOnItemSelectedListener(this);

        }catch (Exception e){
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }
    }

    private void addRecyclerView(){

        list = new ArrayList<>();

        // To be replaced by server data after service call Response
        for(int i=0 ;i < 5;i++){
            CustomerResponse customerResponse = new CustomerResponse();
            customerResponse.setId(String.valueOf(i));
            if((i/2) == 0){
                customerResponse.setFirstName("Ram");
                customerResponse.setLastName("Kumar");
                customerResponse.setEmail("ramkumar@gmail.com");
            }else {
                customerResponse.setFirstName("Sree");
                customerResponse.setLastName("Kala");
                customerResponse.setEmail("sreekala@gmail.com");
            }
            customerResponse.setPhoneNumber("93587210537");

            list.add(customerResponse);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        listAdapter = new EmployeeListAdapter(activity,employeeResponse);
        listAdapter.setEditCallBack(this);
        listAdapter.setCheckedCallBack(this);
        listAdapter.setSendSmsCallBack(this);
        recyclerView.setAdapter(listAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > mScrollOffset) {
                    if (dy > 0) {
                        floatingActionMenu.setVisibility(View.GONE);
                    } else {
                        floatingActionMenu.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchCallBack() {
            @Override
            public void onClick(View view, int position) {

                if (LogFlag.bLogOn)Log.d(TAG, "selected onClick: " + position);

            }

            @Override
            public void onLongClick(View view, int position) {
                if (LogFlag.bLogOn)Log.d(TAG, "recyclerView onLongClick: " + position);
            }
        }));
    }

    private void addFabView(){


        deleteFAB = new FloatingActionButton(activity);
        deleteFAB.setButtonSize(FloatingActionButton.SIZE_MINI);
        deleteFAB.setColorNormalResId(R.color.colorPrimaryDark);
        deleteFAB.setColorPressedResId(R.color.colorPrimary);
        deleteFAB.setLabelText("Delete");
        deleteFAB.setImageResource(R.drawable.delete_white);

        emailFAB = new FloatingActionButton(activity);
        emailFAB.setButtonSize(FloatingActionButton.SIZE_MINI);
        emailFAB.setColorNormalResId(R.color.colorPrimaryDark);
        emailFAB.setColorPressedResId(R.color.colorPrimary);
        emailFAB.setLabelText("Email");
        emailFAB.setImageResource(android.R.drawable.ic_dialog_email);


        addFabButton();

        floatingActionMenu.setClosedOnTouchOutside(true);
        floatingActionMenu.hideMenuButton(false);
        int delay = 400;
        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                floatingActionMenu.showMenuButton(true);
            }
        }, delay);

        View.OnClickListener listener = new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (LogFlag.bLogOn)Log.d(TAG, "floatingActionMenu Clicked " );
                if (!floatingActionMenu.isOpened()) {
                    if(checkedStatus){
                        emailFAB.setVisibility(View.VISIBLE);
                        deleteFAB.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(activity, "Pls select any field to proceed", Toast.LENGTH_SHORT).show();
                        emailFAB.setVisibility(View.GONE);
                        deleteFAB.setVisibility(View.GONE);
                    }
                }
                floatingActionMenu.toggle(true);
            }

        };
        floatingActionMenu.setOnMenuButtonClickListener(listener);


        deleteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFAB.setLabelColors(ContextCompat.getColor(activity, R.color.LiteGray),
                        ContextCompat.getColor(activity, R.color.LiteGray),
                        ContextCompat.getColor(activity, R.color.White));
                deleteFAB.setLabelTextColor(ContextCompat.getColor(activity, R.color.Black));

                floatingActionMenu.toggle(true);
                showDeleteAlertDialog();
            }
        });

        emailFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailFAB.setLabelColors(ContextCompat.getColor(activity, R.color.LiteGray),
                        ContextCompat.getColor(activity, R.color.LiteGray),
                        ContextCompat.getColor(activity, R.color.White));
                emailFAB.setLabelTextColor(ContextCompat.getColor(activity, R.color.Black));

                floatingActionMenu.toggle(true);
                showEmailAlertDialog();

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.spinnerFormat:
                spinnerFormatData = spinnerFormat.getSelectedItem().toString();
                if (LogFlag.bLogOn)Log.d(TAG, "spinnerFormatData: " +spinnerFormatData);

                // To Do Import function after getting url to import
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onFilterStatus(Boolean filterStatus) {
        if(filterStatus){
            listAdapter = new EmployeeListAdapter(activity,employeeResponse);
            listAdapter.setEditCallBack(this);
            listAdapter.setCheckedCallBack(this);
            listAdapter.setSendSmsCallBack(this);
            recyclerView.setAdapter(listAdapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                listAdapter.filter(searchQuery.toString().trim());
                recyclerView.invalidate();
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search) {

            return true;
        }

        if (id == android.R.id.home) {
            if (LogFlag.bLogOn) Log.d(TAG, "Back Pressed ");
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEditSelected(int position) {
        // call back from recycler  adapter for edit employee details
        if (LogFlag.bLogOn)Log.d(TAG, "onEditSelected: " + position);
        // To Do service response data to pass
        gotoAddEmployeeView("Update Employee");
    }

    @Override
    public void onSelectedStatus(Boolean checkedStatus) {
        this.checkedStatus = checkedStatus;
        if (LogFlag.bLogOn)Log.d(TAG, "checkedStatus: " + this.checkedStatus);

    }

    @Override
    public void onSelectedStatusArray(List<Boolean> checkedPositionArrayList) {
        if (LogFlag.bLogOn)Log.d(TAG, "checkedPositionArrayList: " + checkedPositionArrayList);
        this.checkedPositionArrayList = checkedPositionArrayList;
        if(this.checkedPositionArrayList.size()>0){
            floatingActionMenu.setVisibility(View.VISIBLE);
        }else {
            floatingActionMenu.setVisibility(View.VISIBLE);
        }

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

    @FocusChange({R.id.smsPhoneNumber, R.id.smsComments})
    public void focusChangedOnUser(View v, boolean hasFocus) {
        if (hasFocus) {
            textError.setVisibility(View.GONE);
        }
    }


    @Override
    public void onSendSMSSelected(int position) {
        // TODO Update of phone number and message from service call
        goSMSPopup();
    }

    void showDeleteAlertDialog() {

        TextView title = new TextView(activity);
        // You Can Customise your Title here
        title.setText(getResources().getString(R.string.app_name));
        title.setBackgroundColor(Color.BLACK);
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setCustomTitle(title);
        alertDialog.setMessage("Are you Sure to Delete Customer Records");

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                customerCountCheck(0);

                // To Do after Server Response update
              /*  try {
                    for(int i = 0;i <checkedPositionArrayList.size();i++){
                        if(checkedPositionArrayList.get(i)){
                            list.remove(i);
                        }
                    }
                    customerCountCheck(0);
                }catch (IndexOutOfBoundsException e){
                    if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
                }
*/
                listAdapter.notifyDataSetChanged();
                recyclerView.invalidate();

            }
        });
        alertDialog.show();

    }

    void showEmailAlertDialog() {

        TextView title = new TextView(activity);
        // You Can Customise your Title here
        title.setText(getResources().getString(R.string.app_name));
        title.setBackgroundColor(Color.BLACK);
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setCustomTitle(title);
        alertDialog.setMessage("Are you Ready to Email");

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                String [] emailArray = null;
                for(int i = 0;i<checkedPositionArrayList.size();i++){
                    // get the content of selected customers and then email
                    if(checkedPositionArrayList.get(i)){
                        // To do server response of customer data contains email id
                        emailArray = new String[]{list.get(i).getEmail()};
                    }
                }

                if(null != emailArray){

                    gotoEmailView(emailArray);
                }
            }
        });
        alertDialog.show();

    }

    private void addFabButton(){

        floatingActionMenu.addMenuButton(emailFAB);
        floatingActionMenu.addMenuButton(deleteFAB);
        emailFAB.setVisibility(View.GONE);
        deleteFAB.setVisibility(View.GONE);
    }


    private void gotoAddEmployeeView(String pageName){
        Intent intent = new Intent(getApplicationContext(), AddEmployeeActivity_.class);
        intent.putExtra("PageName",pageName);
        startActivity(intent);
    }

    private void gotoEmailView(String [] emailArray){
        Intent intent = new Intent(getApplicationContext(), EmailActivity_.class);
        intent.putExtra("EmailId",emailArray);
        startActivity(intent);
    }

    public void sendSMS(String phoneNo, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(message);
            smsManager.sendMultipartTextMessage(phoneNo, null, parts, null, null);
        } catch (Exception e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }
    }

    private void goSMSPopup() {
        final View popUpView = getLayoutInflater().inflate(R.layout.popupsmsview, null); // inflating popup layout
        smsPopUp = VTools.createPopUp(popUpView);
        Button submitButton = (Button) popUpView.findViewById(R.id.submitButton);
        final ImageButton btnClose = (ImageButton) popUpView.findViewById(R.id.btnClose);

        firstNameText = (TextView) popUpView.findViewById(R.id.firstNameText);
        lastNameText = (TextView) popUpView.findViewById(R.id.lastNameText);
        smsPhoneNumber = (EditText) popUpView.findViewById(R.id.smsPhoneNumber);
        smsComments = (EditText) popUpView.findViewById(R.id.smsComments);
        textError = (TextView) popUpView.findViewById(R.id.textError);
        smsComments.setOnKeyListener(this);

        submitButton.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    void validateInput(){

        if(isNetworkAvailable){

            // TODO Data update from service call
            firstNameText.setText("First Name: ");
            lastNameText.setText("Last Name: ");

            phoneNumberInput = smsPhoneNumber.getText().toString();
            commentsInput = smsComments.getText().toString();

            if (!phoneNumberInput.isEmpty()&& !commentsInput.isEmpty()) {

                validationStatusPhoneNumber =  ValidationUtils.isValidUserPhoneNumber(phoneNumberInput);
                if (!validationStatusPhoneNumber.isStatus()) {
                    if (LogFlag.bLogOn)Log.d(TAG, validationStatusPhoneNumber.getMessage());
                    setErrorMessage(validationStatusPhoneNumber.getMessage());
                    return;
                }

                textError.setVisibility(View.GONE);

                // TODO Service call
                smsPopUp.dismiss();
                sendSMS(phoneNumberInput,commentsInput);

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


    @Background
    void callEmployeeResponse() {

        if (LogFlag.bLogOn)Log.d(TAG, "callEmployeeResponse");
        VPOSRestClient vposRestClient = new VPOSRestClient();
        employeeResponse = vposRestClient.getEmployee();
        if (employeeResponse.getStatus().equals("true") && null != employeeResponse) {
            if (LogFlag.bLogOn)Log.d(TAG, "employeeResponse: " + employeeResponse.toString());
            hideLoaderGifImage();
            employeeResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("employeeResponse failed");
        }
    }

    @UiThread
    public void employeeResponseFinish(){

        addItemsOnSpinner();
        addFabView();
        addRecyclerView();

    }

    @UiThread
    public void hideLoaderGifImage(){
        playGifView.setVisibility(View.GONE);
    }

    @UiThread
    public void showToastErrorMsg(String error) {
        VTools.showToast(error);
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


}

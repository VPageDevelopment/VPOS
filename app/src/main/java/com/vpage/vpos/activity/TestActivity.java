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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vpos.CarouselView.MyPagerAdapter;
import com.vpage.vpos.R;
import com.vpage.vpos.adapter.FieldSpinnerAdapter;
import com.vpage.vpos.adapter.ListAdapter;
import com.vpage.vpos.httputils.VPOSRestClient;
import com.vpage.vpos.pojos.CustomerResponse;
import com.vpage.vpos.pojos.SignInRequest;
import com.vpage.vpos.pojos.SignInResponse;
import com.vpage.vpos.pojos.customer.Customers;
import com.vpage.vpos.pojos.customer.CustomersResponse;
import com.vpage.vpos.pojos.customer.UpdateCustomersResponse;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.callBack.CheckedCallBack;
import com.vpage.vpos.tools.callBack.EditCallBack;
import com.vpage.vpos.tools.callBack.FilterCallBack;
import com.vpage.vpos.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_customer)
public class TestActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, FilterCallBack, EditCallBack, CheckedCallBack {

    private static final String TAG = TestActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.noCustomerContent)
    LinearLayout noCustomerContentLayout;

    @ViewById(R.id.customerContent)
    LinearLayout customerContent;

    @ViewById(R.id.addNewCustomerButton)
    Button addNewCustomerButton;

    @ViewById(R.id.addCustomerButton)
    Button addCustomerButton;

    @ViewById(R.id.spinnerField)
    Spinner spinnerField;

    @ViewById(R.id.spinnerFormat)
    Spinner spinnerFormat;

    @ViewById(R.id.fabMenu)
    FloatingActionMenu floatingActionMenu;

    @ViewById(R.id.customerViewPager)
    ViewPager pager;

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    public MyPagerAdapter adapter;

    FloatingActionButton deleteFAB,emailFAB;

    String spinnerFormatData = "";
    private int mScrollOffset = 4;

    ListAdapter listAdapter;
    FieldSpinnerAdapter fieldSpinnerAdapter;

    private Handler mUiHandler = new Handler();

    List<CustomerResponse> list;
    List<String> spinnerList;

    Boolean checkedStatus = false;
    private List<Boolean> checkedPositionArrayList = new ArrayList<>();

    Activity activity;
    String pageName ="Customer";

    SignInRequest signInRequest;
    CustomersResponse customersResponse;
    List<Customers> customersList = new ArrayList<>();

    int itemCount=0;

    @AfterViews
    public void onInitView() {

        activity = TestActivity.this;

        setActionBarSupport();

        callCustomerResponse();
    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Customers");

    }

    void itemCountCheck(){

        if(itemCount == 0){
            noCustomerContentLayout.setVisibility(View.VISIBLE);
            customerContent.setVisibility(View.GONE);
            floatingActionMenu.setVisibility(View.GONE);
            addNewCustomerButton.setOnClickListener(this);
        }else {
            noCustomerContentLayout.setVisibility(View.GONE);
            customerContent.setVisibility(View.VISIBLE);
            floatingActionMenu.setVisibility(View.VISIBLE);
            addCustomerButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        gotoAddCustomerView("New Customer");
    }

    @Background
    void callCustomerResponse() {
        if (LogFlag.bLogOn)Log.d(TAG, "callCustomerResponse");
        setSignInRequestRequestData();

        VPOSRestClient vposRestClient = new VPOSRestClient();
        SignInResponse signInResponse = vposRestClient.getSignInResponse(signInRequest);
        if(signInResponse != null){
            if (LogFlag.bLogOn)Log.d(TAG, "signInResponse: " + signInResponse.toString());
            if(signInResponse.getStatus().equals("ok")){
                customersResponse = vposRestClient.getCustomer();
                if (null != customersResponse) {
                    if (LogFlag.bLogOn)Log.d(TAG, "customersResponse: " + customersResponse.toString());
                    hideLoaderGifImage();
                    customersResponseFinish();
                } else {
                    hideLoaderGifImage();
                    showToastErrorMsg("CustomersResponse failed");
                }
            }else {
                hideLoaderGifImage();
                showToastErrorMsg(signInResponse.getStatus());
            }
        }else {
            hideLoaderGifImage();
            showToastErrorMsg("signInResponse is null");
        }
    }


    @Background
    void callCustomerDeleteResponse(String customerId) {

        if (LogFlag.bLogOn)Log.d(TAG, "callCustomerDeleteResponse");
        VPOSRestClient vposRestClient = new VPOSRestClient();
        UpdateCustomersResponse updateCustomersResponse = vposRestClient.deleteCustomer(customerId);
        if (null != updateCustomersResponse && updateCustomersResponse.getStatus().equals("true")) {
            if (LogFlag.bLogOn)Log.d(TAG, "updateCustomersResponse: " + updateCustomersResponse.toString());
            hideLoaderGifImage();
            callCustomerResponse();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("updateCustomersResponse failed");
        }
    }

    @UiThread
    public void hideLoaderGifImage(){
        playGifView.setVisibility(View.GONE);
    }

    @UiThread
    public void showToastErrorMsg(String error) {
        VTools.showToast(error);
    }

    @UiThread
    public void customersResponseFinish() {
        if (LogFlag.bLogOn)Log.d(TAG, "customersResponseFinish");

/*            for (int i = 0; i < customersResponse.getCustomers().length; i++) {

                Customers customers = new Customers();
                customers.setCustomer_id(customersResponse.getCustomers()[i].getCustomer_id());
                customers.setFirst_name(customersResponse.getCustomers()[i].getFirst_name());
                customers.setLast_name(customersResponse.getCustomers()[i].getLast_name());
                customers.setGender(customersResponse.getCustomers()[i].getGender());
                customers.setEmail(customersResponse.getCustomers()[i].getEmail());
                customers.setPhone_number(customersResponse.getCustomers()[i].getPhone_number());
                customers.setAddress_one(customersResponse.getCustomers()[i].getAddress_one());
                customers.setAddress_two(customersResponse.getCustomers()[i].getAddress_two());
                customers.setCity(customersResponse.getCustomers()[i].getCity());
                customers.setState(customersResponse.getCustomers()[i].getState());
                customers.setZip(customersResponse.getCustomers()[i].getZip());
                customers.setCountry(customersResponse.getCustomers()[i].getCountry());
                customers.setComments(customersResponse.getCustomers()[i].getComments());
                customers.setCompany(customersResponse.getCustomers()[i].getCompany());
                customers.setAccount(customersResponse.getCustomers()[i].getAccount());
                customers.setTotal(customersResponse.getCustomers()[i].getTotal());
                customers.setDiscount(customersResponse.getCustomers()[i].getDiscount());
                customers.setTaxable(customersResponse.getCustomers()[i].getTaxable());
                customers.setCreated_at(customersResponse.getCustomers()[i].getCreated_at());
                customers.setUpdated_at(customersResponse.getCustomers()[i].getUpdated_at());

                customersList.add(customers);
            }*/

            callViewPager();
    }

    void  setSignInRequestRequestData(){

        signInRequest = new SignInRequest();
        signInRequest.setUsername(getResources().getString(R.string.userName));
        signInRequest.setPassword(getResources().getString(R.string.password));

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

    private void callViewPager(){

        itemCount= customersResponse.getCustomers().length;
        itemCountCheck();

        addItemsOnSpinner();
        addFabView();

        Gson gson = new GsonBuilder().create();
        adapter = new MyPagerAdapter(this, getSupportFragmentManager(),gson.toJson(customersResponse));
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(adapter);
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


        listAdapter = new ListAdapter(activity,list,pageName);
        listAdapter.setEditCallBack(this);
        listAdapter.setCheckedCallBack(this);

    }

    private void addFabView(){

        emailFAB = new FloatingActionButton(activity);
        emailFAB.setButtonSize(FloatingActionButton.SIZE_MINI);
        emailFAB.setColorNormalResId(R.color.colorPrimaryDark);
        emailFAB.setColorPressedResId(R.color.colorPrimary);
        emailFAB.setLabelText("Email");
        emailFAB.setImageResource(android.R.drawable.ic_dialog_email);


        deleteFAB = new FloatingActionButton(activity);
        deleteFAB.setButtonSize(FloatingActionButton.SIZE_MINI);
        deleteFAB.setColorNormalResId(R.color.colorPrimaryDark);
        deleteFAB.setColorPressedResId(R.color.colorPrimary);
        deleteFAB.setLabelText("Delete");
        deleteFAB.setImageResource(R.drawable.delete_white);

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
                        emailFAB.setVisibility(View.GONE);
                        deleteFAB.setVisibility(View.GONE);
                    }
                    // To Do Export function after getting url to Export
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

                // To Do after Server Response update
                try {
                    for(int i = 0;i <checkedPositionArrayList.size();i++){
                        if(checkedPositionArrayList.get(i)){
                            callCustomerDeleteResponse(customersResponse.getCustomers()[i].getCustomer_id());
                        }
                    }
                }catch (IndexOutOfBoundsException e){
                    if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
                }
                listAdapter.notifyDataSetChanged();
                pager.invalidate();

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
                String [] emailArray = new String[checkedPositionArrayList.size()];
                int arrayPosition =0;
                for(int i = 0;i<checkedPositionArrayList.size();i++){
                    // get the content of selected customers and then email
                    if(checkedPositionArrayList.get(i)){
                        // To do server response of customer data contains email id
                        emailArray[arrayPosition] = customersResponse.getCustomers()[i].getEmail();
                        arrayPosition++;
                    }
                }

                if(null != emailArray[0]){

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
            listAdapter = new ListAdapter(activity,list,pageName);
            listAdapter.setEditCallBack(this);
            listAdapter.setCheckedCallBack(this);
            pager.setAdapter(adapter);
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
                pager.invalidate();
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
        // call back from recycler  adapter for edit customer details
        if (LogFlag.bLogOn)Log.d(TAG, "onEditSelected: " + position);
        gotoUpdateCustomerView("Update Customer",position);
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

    }

    private void gotoAddCustomerView(String pageName){
        Intent intent = new Intent(getApplicationContext(), AddCustomerActivity_.class);
        intent.putExtra("PageName",pageName);
        startActivity(intent);
    }

    private void gotoUpdateCustomerView(String pageName,int customerPosition){
        Gson gson = new GsonBuilder().create();
        Intent intent = new Intent(getApplicationContext(), AddCustomerActivity_.class);
        intent.putExtra("PageName",pageName);
        intent.putExtra("customerData",gson.toJson(customersResponse.getCustomers()[customerPosition]));
        startActivity(intent);
    }


    private void gotoEmailView(String [] emailArray){
        Intent intent = new Intent(getApplicationContext(), EmailActivity_.class);
        intent.putExtra("EmailId",emailArray);
        startActivity(intent);
    }
}


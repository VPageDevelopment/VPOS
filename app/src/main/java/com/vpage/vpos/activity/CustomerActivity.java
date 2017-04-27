package com.vpage.vpos.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.vpage.vpos.R;
import com.vpage.vpos.adapter.CustomerListAdapter;
import com.vpage.vpos.adapter.FieldSpinnerAdapter;
import com.vpage.vpos.pojos.CustomerResponse;
import com.vpage.vpos.tools.RecyclerTouchListener;
import com.vpage.vpos.tools.callBack.CustomerCheckedCallBack;
import com.vpage.vpos.tools.callBack.CustomerEditCallBack;
import com.vpage.vpos.tools.callBack.CustomerFilterCallBack;
import com.vpage.vpos.tools.callBack.RecyclerTouchCallBack;
import com.vpage.vpos.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;

@EActivity(R.layout.activity_customer)
public class CustomerActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, CustomerFilterCallBack, CustomerEditCallBack, CustomerCheckedCallBack {

    private static final String TAG = CustomerActivity.class.getName();

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

    @ViewById(R.id.customerRecycleView)
    RecyclerView recyclerView;

    @ViewById(R.id.fabMenu)
    FloatingActionMenu floatingActionMenu;

    @ViewById(R.id.checkBox)
    CheckBox checkBox;

    FloatingActionButton deleteFAB,emailFAB;

    String spinnerFormatData = "";
    private int mScrollOffset = 4;

    CustomerListAdapter customerListAdapter;
    FieldSpinnerAdapter fieldSpinnerAdapter;

    private Handler mUiHandler = new Handler();

    List<CustomerResponse> list;
    List<String> spinnerList;

    Boolean checkedStatus = false;
    private List<Boolean> checkedPositionArrayList = new ArrayList<>();
    List<String> fieldSelectedArrayList = new ArrayList<>();

    @AfterViews
    public void onInitView() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Customers");

        int customerCount = 1; // to test placed static data replaced by server response count
        customerCountCheck(customerCount);

    }

    void customerCountCheck(int customerCount){

        if(customerCount == 0){
            noCustomerContentLayout.setVisibility(View.VISIBLE);
            customerContent.setVisibility(View.GONE);
            floatingActionMenu.setVisibility(View.GONE);
            addNewCustomerButton.setOnClickListener(this);
        }else {
            noCustomerContentLayout.setVisibility(View.GONE);
            customerContent.setVisibility(View.VISIBLE);
            floatingActionMenu.setVisibility(View.VISIBLE);
            addCustomerButton.setOnClickListener(this);
            addItemsOnSpinner();
            addFabView();
            addRecyclerView();
        }
    }


    @Override
    public void onClick(View v) {
        gotoAddCustomerView("New Customer");
    }

    private void addItemsOnSpinner() {
        try{
            spinnerList = new ArrayList<String>();
            spinnerList.add("Filter By");
            spinnerList.add("Id");
            spinnerList.add("First Name");
            spinnerList.add("Last Name");
            spinnerList.add("Email");
            spinnerList.add("Phone Number");

            fieldSelectedArrayList.add("Id");
            fieldSelectedArrayList.add("First Name");
            fieldSelectedArrayList.add("Last Name");
            fieldSelectedArrayList.add("Email");
            fieldSelectedArrayList.add("Phone Number");

            fieldSpinnerAdapter = new FieldSpinnerAdapter(CustomerActivity.this, R.layout.item_spinner_field, spinnerList,fieldSelectedArrayList);
            fieldSpinnerAdapter.setCustomerFilterCallBack(this);
            spinnerField.setAdapter(fieldSpinnerAdapter);
            spinnerFormatData = spinnerFormat.getSelectedItem().toString();
            if (LogFlag.bLogOn)Log.d(TAG, "spinnerFormatData: " + spinnerFormatData);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        customerListAdapter = new CustomerListAdapter(CustomerActivity.this,list,fieldSelectedArrayList);
        customerListAdapter.setCustomerEditCallBack(this);
        customerListAdapter.setCustomerCheckedCallBack(this);
        recyclerView.setAdapter(customerListAdapter);
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

         emailFAB = new FloatingActionButton(CustomerActivity.this);
         emailFAB.setButtonSize(FloatingActionButton.SIZE_MINI);
         emailFAB.setLabelText("Email");
         emailFAB.setImageResource(android.R.drawable.ic_dialog_email);


         deleteFAB = new FloatingActionButton(CustomerActivity.this);
         deleteFAB.setButtonSize(FloatingActionButton.SIZE_MINI);
         deleteFAB.setLabelText("Delete");
         deleteFAB.setImageResource(android.R.drawable.ic_menu_delete);

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
                 deleteFAB.setLabelColors(ContextCompat.getColor(CustomerActivity.this, R.color.LiteGray),
                         ContextCompat.getColor(CustomerActivity.this, R.color.LiteGray),
                         ContextCompat.getColor(CustomerActivity.this, R.color.White));
                 deleteFAB.setLabelTextColor(ContextCompat.getColor(CustomerActivity.this, R.color.Black));

                 floatingActionMenu.toggle(true);
                 showDeleteAlertDialog();
             }
         });

         emailFAB.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 emailFAB.setLabelColors(ContextCompat.getColor(CustomerActivity.this, R.color.LiteGray),
                         ContextCompat.getColor(CustomerActivity.this, R.color.LiteGray),
                         ContextCompat.getColor(CustomerActivity.this, R.color.White));
                 emailFAB.setLabelTextColor(ContextCompat.getColor(CustomerActivity.this, R.color.Black));

                 floatingActionMenu.toggle(true);
                 showEmailAlertDialog();

             }
         });

     }


    void showDeleteAlertDialog() {

        TextView title = new TextView(CustomerActivity.this);
        // You Can Customise your Title here
        title.setText(getResources().getString(R.string.app_name));
        title.setBackgroundColor(Color.BLACK);
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        AlertDialog alertDialog = new AlertDialog.Builder(CustomerActivity.this).create();
        alertDialog.setCustomTitle(title);
        alertDialog.setMessage("Are you Sure to Delete Customer Records");

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();



                for(int i = 0;i <checkedPositionArrayList.size();i++){
                    if(checkedPositionArrayList.get(i)){
                        list.remove(i);
                    }
                }
                customerListAdapter.notifyDataSetChanged();
                recyclerView.invalidate();

            }
        });
        alertDialog.show();

    }

    void showEmailAlertDialog() {

        TextView title = new TextView(CustomerActivity.this);
        // You Can Customise your Title here
        title.setText(getResources().getString(R.string.app_name));
        title.setBackgroundColor(Color.BLACK);
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        AlertDialog alertDialog = new AlertDialog.Builder(CustomerActivity.this).create();
        alertDialog.setCustomTitle(title);
        alertDialog.setMessage("Are you Sure to Delete Customer Records");

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                for(int i = 0;i <=checkedPositionArrayList.size();i++){
                    // get the content of selected customers and then email
                    if(checkedPositionArrayList.get(i)){
                        callEmailIntent();
                    }
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

    private void gotoAddCustomerView(String pageName){
        Intent intent = new Intent(getApplicationContext(), AddCustomerActivity_.class);
        intent.putExtra("PageName",pageName);
        startActivity(intent);
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
    public void onSelectedFilterFields(List<String> fieldCheckedArrayList) {
        // call back from spinner  field adapter for filtering list data
        if (LogFlag.bLogOn)Log.d(TAG, "fieldCheckedArrayList: " + fieldCheckedArrayList.toString());
        this.fieldSelectedArrayList = fieldCheckedArrayList;
        customerListAdapter = new CustomerListAdapter(CustomerActivity.this,list,this.fieldSelectedArrayList);
        customerListAdapter.setCustomerEditCallBack(this);
        customerListAdapter.setCustomerCheckedCallBack(this);
        recyclerView.setAdapter(customerListAdapter);
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
                customerListAdapter.filter(searchQuery.toString().trim());
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
        // call back from recycler  adapter for edit customer details
        if (LogFlag.bLogOn)Log.d(TAG, "onEditSelected: " + position);
        gotoAddCustomerView("Update Customer");
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

    void callEmailIntent(){

        // To Do get the contents of customer to email

        String filename=" ";  // to be updated
        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
        Uri path = Uri.fromFile(filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
       // set the type to 'email'
        emailIntent .setType("vnd.android.cursor.dir/email");
        String to[] = {"vpagedevelopment@gmail.com"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
        // the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, path);
        // the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Customer Details");
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }
}

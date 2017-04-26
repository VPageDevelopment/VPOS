package com.vpage.vpos.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.vpage.vpos.tools.RecyclerTouchListener;
import com.vpage.vpos.tools.callBack.CustomerCheckedCallBack;
import com.vpage.vpos.tools.callBack.CustomerEditCallBack;
import com.vpage.vpos.tools.callBack.CustomerFilterCallBack;
import com.vpage.vpos.tools.callBack.RecyclerTouchCallBack;
import com.vpage.vpos.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.List;

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
    Boolean checkBoxSelectStatus = false;
    Boolean checkedStatus = false;

    CustomerListAdapter customerListAdapter;

    private Handler mUiHandler = new Handler();

    List<String> list;

    List<Integer> checkedPositionArrayList = new ArrayList<>();

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
            addCustomerButton.setOnClickListener(this);
        }else {

            noCustomerContentLayout.setVisibility(View.GONE);
            customerContent.setVisibility(View.VISIBLE);
            floatingActionMenu.setVisibility(View.VISIBLE);
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
            List<String> spinnerList = new ArrayList<String>();
            spinnerList.add("Filter By");
            spinnerList.add("Id");
            spinnerList.add("First Name");
            spinnerList.add("Last Name");
            spinnerList.add("Email");
            spinnerList.add("Phone Number");

            FieldSpinnerAdapter fieldSpinnerAdapter = new FieldSpinnerAdapter(CustomerActivity.this, R.layout.item_spinner_field, spinnerList);
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

        list = new ArrayList<String>();
        list.add("Id");
        list.add("First Name");
        list.add("Last Name");
        list.add("Email");
        list.add("Phone Number");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        customerListAdapter = new CustomerListAdapter(CustomerActivity.this,list,checkBoxSelectStatus);
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
                if (LogFlag.bLogOn)Log.d(TAG, "recyclerView onClick: " + position);

                if(checkedStatus){
                    checkedPositionArrayList.add(position);
                }else {
                     try {
                        if(checkedPositionArrayList.contains(position)){
                            checkedPositionArrayList.remove(position);
                        }
                    }catch (IndexOutOfBoundsException e){
                        if (LogFlag.bLogOn) Log.e(TAG,e.getMessage());
                    }

                }
                if (LogFlag.bLogOn)Log.d(TAG, "checkedPositionArrayList: " + checkedPositionArrayList.toString());
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
                 if (floatingActionMenu.isOpened()) {
                  // TO DO export function
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
                customerCountCheck(0);
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
                customerCountCheck(0);
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
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSelectedFilterFields(List<String> fieldSelectedArrayList) {
        // call back from spinner  field adapter for filtering list data
        if (LogFlag.bLogOn)Log.d(TAG, "fieldSelectedArrayList: " + fieldSelectedArrayList.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer, menu);
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

        // TO DO show view to eit customer details
    }



    @Override
    public void onSelectedStatus(Boolean checkedStatus) {
        this.checkedStatus = checkedStatus;
        if (LogFlag.bLogOn)Log.d(TAG, "checkedStatus: " + this.checkedStatus);
        if(checkedStatus){
            emailFAB.setVisibility(View.VISIBLE);
            deleteFAB.setVisibility(View.VISIBLE);
        }else {
            emailFAB.setVisibility(View.GONE);
            deleteFAB.setVisibility(View.GONE);
        }
    }
}

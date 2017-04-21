package com.vpage.vpos.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.vpage.vpos.R;
import com.vpage.vpos.adapter.CustomerListAdapter;
import com.vpage.vpos.adapter.FieldSpinnerAdapter;
import com.vpage.vpos.tools.callBack.CustomerFilterCallBack;
import com.vpage.vpos.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_customer)
public class CustomerActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, CustomerFilterCallBack {

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

    FloatingActionButton deleteFAB,EmailFAB;

    String spinnerFormatData = "";
    private int mScrollOffset = 4;

    private Handler mUiHandler = new Handler();

    @AfterViews
    public void onInitView() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Customers");


        // To DO the check the Customer count from url response
        noCustomerContentLayout.setVisibility(View.VISIBLE);
        customerContent.setVisibility(View.GONE);
        addNewCustomerButton.setOnClickListener(this);
        addCustomerButton.setOnClickListener(this);

        noCustomerContentLayout.setVisibility(View.GONE);
        customerContent.setVisibility(View.VISIBLE);
        addItemsOnSpinner();
        addFabView();
        addRecyclerView();

    }


    @Override
    public void onClick(View v) {
        gotoAddCustomerView();
    }

    private void addItemsOnSpinner() {
        try{
            List<String> list = new ArrayList<String>();
            list.add("Filter BY");
            list.add("Id");
            list.add("First Name");
            list.add("Last Name");
            list.add("Email");
            list.add("Phone Number");

            FieldSpinnerAdapter fieldSpinnerAdapter = new FieldSpinnerAdapter(CustomerActivity.this, R.layout.item_spinner_field, list);
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

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerActivity.this));
        recyclerView.setAdapter(new CustomerListAdapter(""));
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

    }


     private void addFabView(){

         EmailFAB = new FloatingActionButton(CustomerActivity.this);
         EmailFAB.setButtonSize(FloatingActionButton.SIZE_MINI);
         EmailFAB.setLabelText("Email");
         EmailFAB.setImageResource(android.R.drawable.ic_dialog_email);


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
             }
         });

         EmailFAB.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 EmailFAB.setLabelColors(ContextCompat.getColor(CustomerActivity.this, R.color.LiteGray),
                         ContextCompat.getColor(CustomerActivity.this, R.color.LiteGray),
                         ContextCompat.getColor(CustomerActivity.this, R.color.White));
                 EmailFAB.setLabelTextColor(ContextCompat.getColor(CustomerActivity.this, R.color.Black));

                 floatingActionMenu.toggle(true);
             }
         });


     }

    private void addFabButton(){

        floatingActionMenu.addMenuButton(EmailFAB);
        floatingActionMenu.addMenuButton(deleteFAB);
    }

    private void gotoAddCustomerView(){
        Intent intent = new Intent(getApplicationContext(), AddCustomerActivity_.class);
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

        return super.onOptionsItemSelected(item);
    }

}

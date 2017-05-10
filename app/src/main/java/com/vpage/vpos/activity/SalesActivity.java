package com.vpage.vpos.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.vpage.vpos.R;
import com.vpage.vpos.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_sales)
public class SalesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = SalesActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.spinnerRegisterMode)
    Spinner spinnerRegisterMode;

    @ViewById(R.id.spinnerStockLocation)
    Spinner spinnerStockLocation;

    @ViewById(R.id.fabButtonSelectCustomer)
    FloatingActionButton fabButtonSelectCustomer;

    @ViewById(R.id.takingButton)
    Button takingButton;

    @ViewById(R.id.itemButton)
    Button itemButton;

    @ViewById(R.id.autocompleteEditTextView)
    AutoCompleteTextView autoTextView ;

    String spinnerRegisterModeData="",spinnerStockLocationData="";

    private Handler mUiHandler = new Handler();

    Activity activity;

    @AfterViews
    public void init() {

        activity = SalesActivity.this;

        Intent callingIntent=getIntent();

        setActionBarSupport();
        setSpinnerView();
        setView();
        addFabView();
        setAutoTextView();

    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Sales");

    }

    private void setView(){

        takingButton.setOnClickListener(this);
        itemButton.setOnClickListener(this);

    }

    private void setAutoTextView(){

        // TODO data update after service call
        String[] itemArray = { "Antibiotics", "Soap", "Baby Product", "Accessories" };

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, itemArray);

        //Used to specify minimum number of
        //characters the user has to type in order to display the drop down hint.
        autoTextView.setThreshold(1);

        //Setting adapter
        autoTextView.setAdapter(arrayAdapter);
        autoTextView.setOnItemSelectedListener(this);
        autoTextView.setOnItemClickListener(this);
    }

    private  void setSpinnerView(){

        // TODO data update after service call
        String[] registerModeArray = getResources().getStringArray(R.array.register_mode_arrays);
        String[] stockLocationArray = getResources().getStringArray(R.array.stock_location_arrays);

        ArrayAdapter<String> registerModeSpinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, registerModeArray );
        spinnerRegisterMode.setAdapter(registerModeSpinnerArrayAdapter);

        spinnerRegisterModeData = spinnerRegisterMode.getSelectedItem().toString();
        if (LogFlag.bLogOn) Log.d(TAG, "spinnerRegisterModeData: " + spinnerRegisterModeData);

        ArrayAdapter<String> stockLocationSpinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stockLocationArray );
        spinnerStockLocation.setAdapter(stockLocationSpinnerArrayAdapter);

        spinnerStockLocationData = spinnerStockLocation.getSelectedItem().toString();
        if (LogFlag.bLogOn) Log.d(TAG, "spinnerStockLocationData: " + spinnerStockLocationData);

        spinnerRegisterMode.setOnItemSelectedListener(this);
        spinnerStockLocation.setOnItemSelectedListener(this);
    }

    private void addFabView(){

        fabButtonSelectCustomer.hide(false);
        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fabButtonSelectCustomer.show(true);
               // fabButtonSelectCustomer.setLabelTextColor(ContextCompat.getColor(activity, R.color.Black));
                fabButtonSelectCustomer.setShowAnimation(AnimationUtils.loadAnimation(activity, R.anim.show_from_bottom));
                fabButtonSelectCustomer.setHideAnimation(AnimationUtils.loadAnimation(activity, R.anim.hide_to_bottom));
            }
        }, 300);


        fabButtonSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddCustomerView();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.takingButton:
                // TODO
                break;
            case R.id.itemButton:
                gotoAddItemView();
                break;
        }
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.spinnerRegisterMode:
                spinnerRegisterModeData = spinnerRegisterMode.getSelectedItem().toString();
                if (LogFlag.bLogOn)
                    Log.d(TAG, "spinnerRegisterModeData: " + spinnerRegisterModeData);
                break;

            case R.id.spinnerStockLocation:
                spinnerStockLocationData = spinnerStockLocation.getSelectedItem().toString();
                if (LogFlag.bLogOn) Log.d(TAG, "spinnerStockLocationData: " + spinnerStockLocationData);
                break;

            case R.id.autocompleteEditTextView:
                if (LogFlag.bLogOn)Log.d(TAG, "onItemSelected() position " + position);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        switch (parent.getId()) {

            case R.id.autocompleteEditTextView:
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                break;
        }

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (LogFlag.bLogOn)Log.d(TAG, "onItemClick: "+parent.getItemAtPosition(position));
    }


    private void gotoAddCustomerView(){
        Intent intent = new Intent(getApplicationContext(), AddCustomerActivity_.class);
        intent.putExtra("PageName","New Customer");
        startActivity(intent);
    }

    private void gotoAddItemView(){
        Intent intent = new Intent(getApplicationContext(), AddItemActivity_.class);
        intent.putExtra("PageName","New Item");
        startActivity(intent);
    }

}

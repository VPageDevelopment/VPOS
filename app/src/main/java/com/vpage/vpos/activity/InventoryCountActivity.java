package com.vpage.vpos.activity;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.vpage.vpos.R;
import com.vpage.vpos.adapter.InventoryTrackedListAdapter;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.utils.LogFlag;
import com.vpage.vpos.tools.utils.NetworkUtil;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_inventorycount)
public class InventoryCountActivity extends AppCompatActivity implements View.OnClickListener,OnNetworkChangeListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = InventoryCountActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.barcodeText)
    TextView barcodeText;

    @ViewById(R.id.itemNameText)
    TextView itemNameText;

    @ViewById(R.id.categoryText)
    TextView categoryText;

    @ViewById(R.id.currentQuantityText)
    TextView currentQuantityText;

    @ViewById(R.id.inventoryTrackedList)
    ListView inventoryTrackedList;

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    @ViewById(R.id.closeButton)
    Button closeButton;

    @ViewById(R.id.spinnerStock)
    Spinner spinnerStock;

    String spinnerStockData="";

    InventoryTrackedListAdapter inventoryTrackedListAdapter;

    Activity activity;

    boolean isNetworkAvailable = false;

    @AfterViews
    public void onInitView() {

        activity = InventoryCountActivity.this;

        setActionBarSupport();
        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);
        spinnerStock.setOnItemSelectedListener(this);

        closeButton.setOnClickListener(this);
    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Inventory Count Details");

    }


    void getInputs(){

        barcodeText.setText("");  // To do data from server response
        itemNameText.setText("");  // To do data from server response
        categoryText.setText("");  // To do data from server response
        currentQuantityText.setText("");  // To do data from server response
        spinnerStockData = spinnerStock.getSelectedItem().toString();
        inventoryTrackedListAdapter = new InventoryTrackedListAdapter(activity);
        inventoryTrackedList.setAdapter(inventoryTrackedListAdapter);

    }


    @Override
    public void onClick(View v) {
        getInputs();
        if(isNetworkAvailable){

            playGifView.setVisibility(View.VISIBLE);
            // To Do service call
            gotoItemView();

        }else {

            playGifView.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Check Network Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void gotoItemView(){
        Intent intent = new Intent(getApplicationContext(), ItemActivity_.class);
        startActivity(intent);
        finish();
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
    public void onChange(String status) {
        if (LogFlag.bLogOn) Log.d(TAG, "Network Availability: "+status);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerStockData = spinnerStock.getSelectedItem().toString();
        if (LogFlag.bLogOn)Log.d(TAG, "spinnerStockData: " + spinnerStockData);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

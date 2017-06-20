package com.vpage.vpos.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.github.clans.fab.FloatingActionButton;
import com.vpage.vpos.R;

import com.vpage.vpos.httputils.VPOSRestClient;
import com.vpage.vpos.pojos.sale.addSale.AddSaleRequest;
import com.vpage.vpos.pojos.sale.addSale.AddSaleResponse;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import butterknife.ButterKnife;
import butterknife.InjectView;

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

    @ViewById(R.id.fabLabel)
    TextView fabLabel;

    @ViewById(R.id.scalesRecycleView)
    RecyclerView recyclerView;

    @ViewById(R.id.takingButton)
    Button takingButton;

    @ViewById(R.id.itemButton)
    Button itemButton;

    @ViewById(R.id.autocompleteEditTextView)
    AutoCompleteTextView autoTextView ;

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    @InjectView(R.id.google_progress)
    ProgressBar mProgressBar;

    AddSaleRequest addSaleRequest;

    AutoCompleteTextView autoTextCustomer;

    AlertDialog alertDialog;

    LinearLayout customerSearchLayout,customerContentLayout;

    String spinnerRegisterModeData="",spinnerStockLocationData="";

    private Handler mUiHandler = new Handler();
    ArrayAdapter<String> customerArrayAdapter;

    Activity activity;

    AddSaleResponse addSaleResponse;

    @AfterViews
    public void init() {

        activity = SalesActivity.this;

        Intent callingIntent=getIntent();

        setActionBarSupport();
        ButterKnife.inject(this);

        setView();


        // playGifView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        callAddSaleResponse();

    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Sales");

    }

    @Override
    protected void onResume() {
        super.onResume();
        /**Dynamically*/
        Rect bounds = mProgressBar.getIndeterminateDrawable().getBounds();
        mProgressBar.setIndeterminateDrawable(VTools.getProgressDrawable(activity));
        mProgressBar.getIndeterminateDrawable().setBounds(bounds);
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
        autoTextView.setOnItemClickListener(this);

        // TODO data update after service call
        String[] customerArray = { "Ragul", "Ramesh", "Deepa","Mathu"};

        customerArrayAdapter = new ArrayAdapter<String>(SalesActivity.this, android.R.layout.select_dialog_item, customerArray);

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
                fabButtonSelectCustomer.setShowAnimation(AnimationUtils.loadAnimation(activity, R.anim.show_from_bottom));
                fabButtonSelectCustomer.setHideAnimation(AnimationUtils.loadAnimation(activity, R.anim.hide_to_bottom));
            }
        }, 300);


        fabButtonSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomSelectView();
            }
        });

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

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.autocompleteEditTextView:
                if (LogFlag.bLogOn)Log.d(TAG, "onItemSelected: " + parent.getItemIdAtPosition(position));
                recyclerView.setVisibility(View.VISIBLE);
                break;

        }
    }


    private void  showCustomSelectView(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialogselectcustomerview, null);
        dialogBuilder.setView(dialogView);

        TextView popUpTitle = (TextView) dialogView.findViewById(R.id.popUpTitle);
        autoTextCustomer = (AutoCompleteTextView) dialogView.findViewById(R.id.autoTextCustomer);
        ImageButton btnClose = (ImageButton) dialogView.findViewById(R.id.btnClose);
        customerSearchLayout  = (LinearLayout) dialogView.findViewById(R.id.customerSearchLayout);
        customerContentLayout  = (LinearLayout) dialogView.findViewById(R.id.customerContentLayout);

        Button newCustomerButton =(Button)dialogView.findViewById(R.id.newCustomerButton);
        Button removeCustomerButton =(Button)dialogView.findViewById(R.id.removeCustomerButton);

        TextView customerName = (TextView)dialogView.findViewById(R.id.customerName);
        TextView customerEmail = (TextView)dialogView.findViewById(R.id.customerEmail);
        TextView customerDiscount = (TextView)dialogView.findViewById(R.id.customerDiscount);
        TextView customerTotal = (TextView)dialogView.findViewById(R.id.customerTotal);

        TextView subTotalPrice = (TextView)dialogView.findViewById(R.id.subTotalPrice);
        TextView totalPrice = (TextView)dialogView.findViewById(R.id.totalPrice);


        popUpTitle.setText("Customer Select");

        alertDialog = dialogBuilder.create();
        alertDialog.show();

        autoTextCustomer.setThreshold(1);
        //Setting adapter
        autoTextCustomer.setAdapter(customerArrayAdapter);
        autoTextCustomer.setOnItemClickListener(autoTextCustomerClickItem);

        btnClose.setOnClickListener(this);
        newCustomerButton.setOnClickListener(this);
        removeCustomerButton.setOnClickListener(this);

    }


    private AdapterView.OnItemClickListener autoTextCustomerClickItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info = ((TextView) view).getText().toString();
            if(LogFlag.bLogOn)Log.d(TAG, "onItemSelected: " + info);
            customerSearchLayout.setVisibility(View.GONE);
            customerContentLayout.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.takingButton:
                gotoSaleTakeView();
                break;

            case R.id.itemButton:
                gotoAddItemView();
                break;

            case R.id.removeCustomerButton:
                customerSearchLayout.setVisibility(View.VISIBLE);
                customerContentLayout.setVisibility(View.GONE);
                break;

            case R.id.newCustomerButton:
                gotoAddCustomerView();
                break;

            case R.id.btnClose:
               // PopUp.dismiss();
                alertDialog.dismiss();
                break;
        }
    }



    @Background
    void callAddSaleResponse() {
        if (LogFlag.bLogOn)Log.d(TAG, "callAddSaleResponse");
        setAddSaleRequestData();

        VPOSRestClient vposRestClient = new VPOSRestClient();
        vposRestClient.setAddSaleParams(addSaleRequest);
        addSaleResponse = vposRestClient.addSale();
        if (null != addSaleResponse && addSaleResponse.getStatus().equals("true")) {
            if (LogFlag.bLogOn)Log.d(TAG, "addSaleResponse: " + addSaleResponse.toString());
            hideLoaderGifImage();
            addSaleResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("addSaleResponse failed");
        }
    }

    @UiThread
    public void addSaleResponseFinish( ){

        setSpinnerView();
        setAutoTextView();
        addFabView();

    }

    @UiThread
    public void hideLoaderGifImage(){
       // playGifView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    @UiThread
    public void showToastErrorMsg(String error) {
        VTools.showToast(error);
    }

    void  setAddSaleRequestData(){

        addSaleRequest = new AddSaleRequest();
        addSaleRequest.setCustomer_fk("");
        addSaleRequest.setItem_fk("");
        addSaleRequest.setAmount_due("");
        addSaleRequest.setAmount_tendered("");
        addSaleRequest.setChange_due("");
        addSaleRequest.setType("");
        addSaleRequest.setInvoice("");

    }

    private void gotoAddCustomerView(){
        Intent intent = new Intent(getApplicationContext(), AddCustomerActivity_.class);
        intent.putExtra("PageName","New Sales Customer");
        startActivity(intent);
    }

    private void gotoAddItemView(){
        Intent intent = new Intent(getApplicationContext(), AddItemActivity_.class);
        intent.putExtra("PageName","New Sales Item");
        startActivity(intent);
    }


    private void gotoSaleTakeView(){
        Intent intent = new Intent(getApplicationContext(), SaleTakeActivity_.class);
        startActivity(intent);
    }

}

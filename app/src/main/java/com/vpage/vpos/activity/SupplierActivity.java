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
import com.vpage.vpos.R;
import com.vpage.vpos.adapter.SupplierFieldSpinnerAdapter;
import com.vpage.vpos.adapter.SupplierListAdapter;
import com.vpage.vpos.httputils.VPOSRestClient;
import com.vpage.vpos.pojos.SupplierResponseTest;
import com.vpage.vpos.pojos.supplier.SupplierResponse;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.RecyclerTouchListener;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.callBack.CheckedCallBack;
import com.vpage.vpos.tools.callBack.EditCallBack;
import com.vpage.vpos.tools.callBack.FilterCallBack;
import com.vpage.vpos.tools.callBack.RecyclerTouchCallBack;
import com.vpage.vpos.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_supplier)
public class SupplierActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, FilterCallBack, EditCallBack, CheckedCallBack {

    private static final String TAG = SupplierActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.noSupplierContent)
    LinearLayout noSupplierContent;

    @ViewById(R.id.SupplierContent)
    LinearLayout SupplierContent;

    @ViewById(R.id.addNewSupplier)
    Button addNewSupplierButton;

    @ViewById(R.id.addSupplierButton)
    Button addSupplierButton;

    @ViewById(R.id.spinnerField)
    Spinner spinnerField;

    @ViewById(R.id.spinnerFormat)
    Spinner spinnerFormat;

    @ViewById(R.id.supplierRecycleView)
    RecyclerView recyclerView;

    @ViewById(R.id.fabMenu)
    FloatingActionMenu floatingActionMenu;
    @ViewById(R.id.viewGif)
    PlayGifView playGifView;


    FloatingActionButton deleteFAB,emailFAB;

    String spinnerFormatData = "";
    private int mScrollOffset = 4;

    SupplierListAdapter supplierListAdapter;
    SupplierFieldSpinnerAdapter supplierFieldSpinnerAdapter;

    private Handler mUiHandler = new Handler();

    List<SupplierResponseTest> list;
    List<String> spinnerList;

    Boolean checkedStatus = false;
    private List<Boolean> checkedPositionArrayList = new ArrayList<>();

    Activity activity;

    SupplierResponse supplierResponse;

    @AfterViews
    public void onInitView() {

        activity = SupplierActivity.this;

        setActionBarSupport();

        int itemCount = 1; // to test placed static data replaced by server response count
        itemCountCheck(itemCount);

    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Suppliers");

    }

    void itemCountCheck(int itemCount){

        if(itemCount == 0){
            noSupplierContent.setVisibility(View.VISIBLE);
            SupplierContent.setVisibility(View.GONE);
            floatingActionMenu.setVisibility(View.GONE);
            addNewSupplierButton.setOnClickListener(this);
        }else {
            noSupplierContent.setVisibility(View.GONE);
            SupplierContent.setVisibility(View.VISIBLE);
            floatingActionMenu.setVisibility(View.VISIBLE);
            addSupplierButton.setOnClickListener(this);

            callSupplierResponse();
        }
    }

    @Override
    public void onClick(View v) {
        gotoAddSupplierView("New Supplier");
    }

    private void addItemsOnSpinner() {
        try{
            spinnerList = new ArrayList<>();
            spinnerList.add("Filter By");
            spinnerList.add("Id");
            spinnerList.add("Company Name");
            spinnerList.add("Agency Name");
            spinnerList.add("First Name");
            spinnerList.add("Last Name");
            spinnerList.add("Email");
            spinnerList.add("Phone Number");

            supplierFieldSpinnerAdapter = new SupplierFieldSpinnerAdapter(activity, R.layout.item_spinner_field, spinnerList);
            supplierFieldSpinnerAdapter.setFilterCallBack(this);
            spinnerField.setAdapter(supplierFieldSpinnerAdapter);
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
            SupplierResponseTest supplierResponse = new SupplierResponseTest();
            supplierResponse.setId(String.valueOf(i));
            if((i/2) == 0){
                supplierResponse.setCompanyName("Vpage");
                supplierResponse.setAgencyName("Vpage");
                supplierResponse.setFirstName("Ram");
                supplierResponse.setLastName("Kumar");
                supplierResponse.setEmail("ramkumar@gmail.com");
            }else {
                supplierResponse.setCompanyName("Vpage");
                supplierResponse.setAgencyName("Vpage");
                supplierResponse.setFirstName("Sree");
                supplierResponse.setLastName("Kala");
                supplierResponse.setEmail("sreekala@gmail.com");
            }
            supplierResponse.setPhoneNumber("93587210537");

            list.add(supplierResponse);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        supplierListAdapter = new SupplierListAdapter(activity,supplierResponse);
        supplierListAdapter.setEditCallBack(this);
        supplierListAdapter.setCheckedCallBack(this);
        recyclerView.setAdapter(supplierListAdapter);
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

                itemCountCheck(0);

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
                supplierListAdapter.notifyDataSetChanged();
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
            supplierListAdapter = new SupplierListAdapter(activity,supplierResponse);
            supplierListAdapter.setEditCallBack(this);
            supplierListAdapter.setCheckedCallBack(this);
            recyclerView.setAdapter(supplierListAdapter);
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
                supplierListAdapter.filter(searchQuery.toString().trim());
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
        // To Do service response data to pass
        gotoAddSupplierView("Update Supplier");
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

    @Background
    void callSupplierResponse() {

        if (LogFlag.bLogOn)Log.d(TAG, "callSupplierResponse");
        VPOSRestClient vposRestClient = new VPOSRestClient();
        supplierResponse = vposRestClient.getSuppliers();
        if (supplierResponse.getStatus().equals("true") && null != supplierResponse) {
            if (LogFlag.bLogOn)Log.d(TAG, "supplierResponse: " + supplierResponse.toString());
            hideLoaderGifImage();
            supplierResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("supplierResponse failed");
        }
    }

    @UiThread
    public void supplierResponseFinish(){

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


    private void gotoAddSupplierView(String pageName){
        Intent intent = new Intent(getApplicationContext(), AddSupplierActivity_.class);
        intent.putExtra("PageName",pageName);
        startActivity(intent);
    }


    private void gotoEmailView(String [] emailArray){
        Intent intent = new Intent(getApplicationContext(), EmailActivity_.class);
        intent.putExtra("EmailId",emailArray);
        startActivity(intent);
    }
}



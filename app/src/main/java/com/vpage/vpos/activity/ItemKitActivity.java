package com.vpage.vpos.activity;


import android.annotation.SuppressLint;
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
import com.vpage.vpos.adapter.ItemKitFieldSpinnerAdapter;
import com.vpage.vpos.adapter.ItemKitListAdapter;
import com.vpage.vpos.pojos.ItemKitResponse;
import com.vpage.vpos.tools.RecyclerTouchListener;
import com.vpage.vpos.tools.callBack.CheckedCallBack;
import com.vpage.vpos.tools.callBack.EditCallBack;
import com.vpage.vpos.tools.callBack.FilterCallBack;
import com.vpage.vpos.tools.callBack.RecyclerTouchCallBack;
import com.vpage.vpos.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_itemkit)
public class ItemKitActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, FilterCallBack, EditCallBack, CheckedCallBack {

    private static final String TAG = ItemKitActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.noItemKitContent)
    LinearLayout noItemKitContent;

    @ViewById(R.id.itemKitContent)
    LinearLayout itemKitContent;

    @ViewById(R.id.addNewItemKitButton)
    Button addNewItemKitButton;

    @ViewById(R.id.addItemKitButton)
    Button addItemKitButton;

    @ViewById(R.id.spinnerField)
    Spinner spinnerField;

    @ViewById(R.id.spinnerFormat)
    Spinner spinnerFormat;

    @ViewById(R.id.itemKitRecycleView)
    RecyclerView recyclerView;

    @ViewById(R.id.fabMenu)
    FloatingActionMenu floatingActionMenu;

    FloatingActionButton deleteFAB,generateBarcodeFAB;

    String spinnerFormatData = "";
    private int mScrollOffset = 4;

    ItemKitListAdapter itemKitListAdapter;
    ItemKitFieldSpinnerAdapter itemKitFieldSpinnerAdapter;

    private Handler mUiHandler = new Handler();

    List<ItemKitResponse> list;
    List<String> spinnerList;

    Boolean checkedStatus = false;
    private List<Boolean> checkedPositionArrayList = new ArrayList<>();

    android.app.Activity activity;

    @AfterViews
    public void onInitView() {

        activity = ItemKitActivity.this;

        setActionBarSupport();

        int customerCount = 1; // to test placed static data replaced by server response count
        customerCountCheck(customerCount);

    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Customers");

    }

    void customerCountCheck(int customerCount){

        if(customerCount == 0){
            noItemKitContent.setVisibility(View.VISIBLE);
            itemKitContent.setVisibility(View.GONE);
            floatingActionMenu.setVisibility(View.GONE);
            addNewItemKitButton.setOnClickListener(this);
        }else {
            noItemKitContent.setVisibility(View.GONE);
            itemKitContent.setVisibility(View.VISIBLE);
            floatingActionMenu.setVisibility(View.VISIBLE);
            addItemKitButton.setOnClickListener(this);
            addItemsOnSpinner();
            addFabView();
            addRecyclerView();
        }
    }

    @Override
    public void onClick(View v) {
        gotoAddItemKitView("New Item Kit");
    }

    private void addItemsOnSpinner() {
        try{
            spinnerList = new ArrayList<>();
            spinnerList.add("Filter By");
            spinnerList.add("Kit Id");
            spinnerList.add("Item Kit Name");
            spinnerList.add("Item Kit Description");
            spinnerList.add("Cost Price");
            spinnerList.add("Retail Price");

            itemKitFieldSpinnerAdapter = new ItemKitFieldSpinnerAdapter(activity, R.layout.item_spinner_field, spinnerList);
            itemKitFieldSpinnerAdapter.setFilterCallBack(this);
            spinnerField.setAdapter(itemKitFieldSpinnerAdapter);
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
            ItemKitResponse itemKitResponse = new ItemKitResponse();
            itemKitResponse.setId(String.valueOf(i));
            if((i/2) == 0){
                itemKitResponse.setItemKitName("Baby Product");
                itemKitResponse.setItemKitDescription("Soap");
                itemKitResponse.setCostPrice("50");
            }else {
                itemKitResponse.setItemKitName("Baby Product");
                itemKitResponse.setItemKitDescription("Soap");
                itemKitResponse.setCostPrice("50");
            }
            itemKitResponse.setRetailPrice("25");

            list.add(itemKitResponse);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemKitListAdapter = new ItemKitListAdapter(activity,list);
        itemKitListAdapter.setEditCallBack(this);
        itemKitListAdapter.setCheckedCallBack(this);
        recyclerView.setAdapter(itemKitListAdapter);
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
        deleteFAB.setLabelText("Delete");
        deleteFAB.setImageResource(android.R.drawable.ic_menu_delete);

        generateBarcodeFAB = new FloatingActionButton(activity);
        generateBarcodeFAB.setButtonSize(FloatingActionButton.SIZE_MINI);
        generateBarcodeFAB.setLabelText("GenerateBarcode");
        generateBarcodeFAB.setImageResource(R.drawable.barcode);

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
                        generateBarcodeFAB.setVisibility(View.VISIBLE);
                        deleteFAB.setVisibility(View.VISIBLE);
                    }else {
                        generateBarcodeFAB.setVisibility(View.GONE);
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

        generateBarcodeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                generateBarcodeFAB.setLabelColors(ContextCompat.getColor(activity, R.color.LiteGray),
                        ContextCompat.getColor(activity, R.color.LiteGray),
                        ContextCompat.getColor(activity, R.color.White));
                generateBarcodeFAB.setLabelTextColor(ContextCompat.getColor(activity, R.color.Black));

                floatingActionMenu.toggle(true);
                gotoBarcodeGenerateView();

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
                itemKitListAdapter.notifyDataSetChanged();
                recyclerView.invalidate();

            }
        });
        alertDialog.show();

    }

    private void addFabButton(){

        floatingActionMenu.addMenuButton(generateBarcodeFAB);
        floatingActionMenu.addMenuButton(deleteFAB);
        generateBarcodeFAB.setVisibility(View.GONE);
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
            itemKitListAdapter = new ItemKitListAdapter(activity,list);
            itemKitListAdapter.setEditCallBack(this);
            itemKitListAdapter.setCheckedCallBack(this);
            recyclerView.setAdapter(itemKitListAdapter);
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
                itemKitListAdapter.filter(searchQuery.toString().trim());
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
        gotoAddItemKitView("Update Item Kit");
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

    private void gotoAddItemKitView(String pageName){
        Intent intent = new Intent(getApplicationContext(), AddItemKitActivity_.class);
        intent.putExtra("PageName",pageName);
        startActivity(intent);
    }

    private void gotoBarcodeGenerateView(){

        Intent intent = new Intent(getApplicationContext(), BarcodeGenerateActivity_.class);
        startActivity(intent);
    }
}


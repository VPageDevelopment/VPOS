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
import com.vpage.vpos.adapter.ItemFieldSpinnerAdapter;
import com.vpage.vpos.adapter.ItemListAdapter;
import com.vpage.vpos.pojos.ItemResponse;
import com.vpage.vpos.tools.RecyclerTouchListener;
import com.vpage.vpos.tools.callBack.CheckedCallBack;
import com.vpage.vpos.tools.callBack.FilterCallBack;
import com.vpage.vpos.tools.callBack.ItemCallBack;
import com.vpage.vpos.tools.callBack.RecyclerTouchCallBack;
import com.vpage.vpos.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_item)
public class ItemActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, FilterCallBack, CheckedCallBack, ItemCallBack {

    private static final String TAG = ItemActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.noItemContent)
    LinearLayout noItemContentLayout;

    @ViewById(R.id.ItemContent)
    LinearLayout ItemContent;

    @ViewById(R.id.addNewItemButton)
    Button addNewItemButton;

    @ViewById(R.id.addItemButton)
    Button addItemButton;

    @ViewById(R.id.spinnerField)
    Spinner spinnerField;

    @ViewById(R.id.spinnerFormat)
    Spinner spinnerFormat;

    @ViewById(R.id.itemRecycleView)
    RecyclerView recyclerView;

    @ViewById(R.id.fabMenu)
    FloatingActionMenu floatingActionMenu;

    FloatingActionButton deleteFAB,bulkEditFAB,generateBarcodeFAB;

    String spinnerFormatData = "";
    private int mScrollOffset = 4;

    ItemListAdapter itemListAdapter;
    ItemFieldSpinnerAdapter itemFieldSpinnerAdapter;

    private Handler mUiHandler = new Handler();

    List<ItemResponse> list;
    List<String> spinnerList;

    Boolean checkedStatus = false;
    private List<Boolean> checkedPositionArrayList = new ArrayList<>();

    Activity activity;

    @AfterViews
    public void onInitView() {

        activity = ItemActivity.this;

        setActionBarSupport();

        int itemCount = 1; // to test placed static data replaced by server response count
        itemCountCheck(itemCount);

    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Items");

    }


    void itemCountCheck(int itemCount){

        if(itemCount == 0){
            noItemContentLayout.setVisibility(View.VISIBLE);
            ItemContent.setVisibility(View.GONE);
            floatingActionMenu.setVisibility(View.GONE);
            addNewItemButton.setOnClickListener(this);
        }else {
            noItemContentLayout.setVisibility(View.GONE);
            ItemContent.setVisibility(View.VISIBLE);
            floatingActionMenu.setVisibility(View.VISIBLE);
            addItemButton.setOnClickListener(this);
            addItemsOnSpinner();
            addFabView();
            addRecyclerView();
        }
    }


    @Override
    public void onClick(View v) {
        gotoAddItemView("New Item");
    }

    private void addItemsOnSpinner() {
        try{
            spinnerList = new ArrayList<>();
            spinnerList.add("Filter By");
            spinnerList.add("Id");
            spinnerList.add("UPC/EAN/ISBN");
            spinnerList.add("Item Name");
            spinnerList.add(" Category");
            spinnerList.add("Company Number");
            spinnerList.add("Cost Price");
            spinnerList.add("Retail Price");
            spinnerList.add("Quantity");
            spinnerList.add("Tax Percent(s)");
            spinnerList.add("Avatar");

            itemFieldSpinnerAdapter = new ItemFieldSpinnerAdapter(activity, R.layout.item_spinner_field, spinnerList);
            itemFieldSpinnerAdapter.setFilterCallBack(this);
            spinnerField.setAdapter(itemFieldSpinnerAdapter);
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
            ItemResponse itemResponse = new ItemResponse();
            itemResponse.setId(String.valueOf(i));
            if((i/2) == 0){
                itemResponse.setBarcode("JHJKK4656");
                itemResponse.setItemName("Soap");
                itemResponse.setCategory("Cosmetic");
                itemResponse.setCompanyName("Vpage");
                itemResponse.setCostPrice("30");
                itemResponse.setRetailPrice("25");
                itemResponse.setQuantity("10");
                itemResponse.setTaxPercent("5");
                itemResponse.setAvatarUrl("");
            }else {
                itemResponse.setBarcode("1226VGJHS");
                itemResponse.setItemName("Bag");
                itemResponse.setCategory("Accessories");
                itemResponse.setCompanyName("Vpage");
                itemResponse.setCostPrice("40");
                itemResponse.setRetailPrice("25");
                itemResponse.setQuantity("15");
                itemResponse.setTaxPercent("10");
                itemResponse.setAvatarUrl("");
            }

            list.add(itemResponse);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemListAdapter = new ItemListAdapter(activity,list);
        itemListAdapter.setItemCallBack(this);
        itemListAdapter.setCheckedCallBack(this);
        recyclerView.setAdapter(itemListAdapter);
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

        bulkEditFAB = new FloatingActionButton(activity);
        bulkEditFAB.setButtonSize(FloatingActionButton.SIZE_MINI);
        bulkEditFAB.setLabelText("BulkEdit");
        bulkEditFAB.setImageResource(R.drawable.edit);

        generateBarcodeFAB = new FloatingActionButton(activity);
        generateBarcodeFAB.setButtonSize(FloatingActionButton.SIZE_MINI);
        generateBarcodeFAB.setLabelText("GenerateBarcode");
        generateBarcodeFAB.setImageResource(R.drawable.barcode);

        deleteFAB = new FloatingActionButton(activity);
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
                        bulkEditFAB.setVisibility(View.VISIBLE);
                        generateBarcodeFAB.setVisibility(View.VISIBLE);
                        deleteFAB.setVisibility(View.VISIBLE);
                    }else {
                        bulkEditFAB.setVisibility(View.GONE);
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

        bulkEditFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bulkEditFAB.setLabelColors(ContextCompat.getColor(activity, R.color.LiteGray),
                        ContextCompat.getColor(activity, R.color.LiteGray),
                        ContextCompat.getColor(activity, R.color.White));
                bulkEditFAB.setLabelTextColor(ContextCompat.getColor(activity, R.color.Black));

                floatingActionMenu.toggle(true);
                // To Do

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
                // To Do

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
                itemListAdapter.notifyDataSetChanged();
                recyclerView.invalidate();

            }
        });
        alertDialog.show();

    }



    private void addFabButton(){

        floatingActionMenu.addMenuButton(bulkEditFAB);
        floatingActionMenu.addMenuButton(generateBarcodeFAB);
        floatingActionMenu.addMenuButton(deleteFAB);
        bulkEditFAB.setVisibility(View.GONE);
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
            itemListAdapter = new ItemListAdapter(activity,list);
            itemListAdapter.setItemCallBack(this);
            itemListAdapter.setCheckedCallBack(this);
            recyclerView.setAdapter(itemListAdapter);
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
                itemListAdapter.filter(searchQuery.toString().trim());
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
    public void onSelectedStatus(Boolean checkedStatus) {
        this.checkedStatus = checkedStatus;
        if (LogFlag.bLogOn)Log.d(TAG, "checkedStatus: " + this.checkedStatus);

    }

    @Override
    public void onSelectedStatusArray(List<Boolean> checkedPositionArrayList) {
        if (LogFlag.bLogOn)Log.d(TAG, "checkedPositionArrayList: " + checkedPositionArrayList);
        this.checkedPositionArrayList = checkedPositionArrayList;

    }

    @Override
    public void onEditSelected(int position) {
        if (LogFlag.bLogOn)Log.d(TAG, "onEditSelected: " + position);
        // To Do service response data to pass
        gotoAddItemView("Update Item");
    }

    @Override
    public void onUpdateInventory(int position) {
        if (LogFlag.bLogOn)Log.d(TAG, "onUpdateInventory: " + position);
        // To Do service response data to pass
         gotoUpdateInventoryView();
    }

    @Override
    public void onCountInventory(int position) {
        if (LogFlag.bLogOn)Log.d(TAG, "onCountInventory: " + position);
        // To Do service response data to pass
        gotoInventoryCountView();

    }

    private void gotoAddItemView(String pageName){
        Intent intent = new Intent(getApplicationContext(), AddItemActivity_.class);
        intent.putExtra("PageName",pageName);
        startActivity(intent);
    }


    private void gotoUpdateInventoryView(){
        Intent intent = new Intent(getApplicationContext(), UpdateInventoryActivity_.class);
        startActivity(intent);
    }


    private void gotoInventoryCountView(){
        Intent intent = new Intent(getApplicationContext(), InventoryCountActivity_.class);
        startActivity(intent);
    }

}

package com.vpage.vpos.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vpos.R;
import com.vpage.vpos.adapter.ItemFieldSpinnerAdapter;
import com.vpage.vpos.adapter.ItemListAdapter;
import com.vpage.vpos.httputils.VPOSRestClient;
import com.vpage.vpos.pojos.item.ItemResponse;
import com.vpage.vpos.pojos.item.UpdateItemResponse;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.RecyclerTouchListener;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.callBack.CheckedCallBack;
import com.vpage.vpos.tools.callBack.FilterCallBack;
import com.vpage.vpos.tools.callBack.ItemCallBack;
import com.vpage.vpos.tools.callBack.RecyclerTouchCallBack;
import com.vpage.vpos.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@EActivity(R.layout.activity_item)
public class ItemActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, FilterCallBack, CheckedCallBack, ItemCallBack, AdapterView.OnItemClickListener {

    private static final String TAG = ItemActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.noItemContent)
    LinearLayout noItemContentLayout;

    @ViewById(R.id.ItemContent)
    LinearLayout ItemContent;

    @ViewById(R.id.datePickerButton)
    Button datePickerButton;

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

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    EditText fromDate,toDate;

    FloatingActionButton deleteFAB,bulkEditFAB,generateBarcodeFAB;

    String spinnerFormatData = "",fromDateString="",toDateString="",todayDate="",thisMonthOnLastYearDate="",
            lastYearDate="";
    private int mScrollOffset = 4;

    ItemListAdapter itemListAdapter;
    ItemFieldSpinnerAdapter itemFieldSpinnerAdapter;

    private Handler mUiHandler = new Handler();

    List<String> spinnerList;

    Boolean checkedStatus = false;
    private List<Boolean> checkedPositionArrayList = new ArrayList<>();

    PopupWindow PopUp;

    TextWatcher textWatcherFromDate,textWatcherToDate;

    Activity activity;

    ItemResponse itemResponse;

    int itemCount=0;

    @AfterViews
    public void onInitView() {

        activity = ItemActivity.this;

        setActionBarSupport();

        callItemResponse();
    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Items");

    }

    void itemCountCheck(){

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
            datePickerButton.setOnClickListener(this);

            todayDate = VTools.getCurrentDate();
            if(!fromDateString.isEmpty() && !toDateString.isEmpty()){
                datePickerButton.setText(fromDateString +" - "+toDateString);
            }else {
                datePickerButton.setText(todayDate +" - "+todayDate);
            }

            textWatcherFromDate = new TextWatcher() {
                public void afterTextChanged(Editable editable) {
                }

                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                    // you can check for enter key here
                }

                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    datePickerButton.setText(charSequence.toString() +" - "+toDateString);
                }
            };


            textWatcherToDate = new TextWatcher() {
                public void afterTextChanged(Editable editable) {
                }

                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                    // you can check for enter key here
                }

                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    datePickerButton.setText(fromDateString +" - "+charSequence.toString());
                }
            };
        }
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

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemListAdapter = new ItemListAdapter(activity,itemResponse);
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
        bulkEditFAB.setColorNormalResId(R.color.colorPrimaryDark);
        bulkEditFAB.setColorPressedResId(R.color.colorPrimary);
        bulkEditFAB.setLabelText("BulkEdit");
        bulkEditFAB.setImageResource(R.drawable.edit);

        generateBarcodeFAB = new FloatingActionButton(activity);
        generateBarcodeFAB.setButtonSize(FloatingActionButton.SIZE_MINI);
        generateBarcodeFAB.setColorNormalResId(R.color.colorPrimaryDark);
        generateBarcodeFAB.setColorPressedResId(R.color.colorPrimary);
        generateBarcodeFAB.setLabelText("GenerateBarcode");
        generateBarcodeFAB.setImageResource(R.drawable.barcode);

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
                        bulkEditFAB.setVisibility(View.VISIBLE);
                        generateBarcodeFAB.setVisibility(View.VISIBLE);
                        deleteFAB.setVisibility(View.VISIBLE);
                    }else {
                        bulkEditFAB.setVisibility(View.GONE);
                        generateBarcodeFAB.setVisibility(View.GONE);
                        deleteFAB.setVisibility(View.GONE);
                    }
                    // TODO Export function after getting url to Export
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


                 int[] selectedPositionsArray = new int[checkedPositionArrayList.size()];
                int arrayPosition =0;
                for(int i = 0;i<checkedPositionArrayList.size();i++){

                    if(checkedPositionArrayList.get(i)){

                        selectedPositionsArray[arrayPosition] = i;
                        arrayPosition++;
                    }
                }

                if(0 != selectedPositionsArray[0]){

                    gotoEditMultipleItemView(selectedPositionsArray);
                }



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

                int[] selectedPositionsArray = new int[checkedPositionArrayList.size()];
                int arrayPosition =0;
                for(int i = 0;i<checkedPositionArrayList.size();i++){

                    if(checkedPositionArrayList.get(i)){

                        selectedPositionsArray[arrayPosition] = i;
                        arrayPosition++;
                    }
                }

                if(0 != selectedPositionsArray[0]){

                    gotoBarcodeGenerateView(selectedPositionsArray);
                }

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

                try {
                    for(int i = 0;i <checkedPositionArrayList.size();i++){
                        if(checkedPositionArrayList.get(i)){
                            callItemDeleteResponse(itemResponse.getItems()[i].getItem_id());
                        }
                    }
                }catch (IndexOutOfBoundsException e){
                    if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
                }
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

                // TODO Import function after getting url to import
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onFilterStatus(Boolean filterStatus) {
        if(filterStatus){
            itemListAdapter = new ItemListAdapter(activity,itemResponse);
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
        gotoUpdateItemView("Update Item",position);
    }

    @Override
    public void onUpdateInventory(int position) {
        if (LogFlag.bLogOn)Log.d(TAG, "onUpdateInventory: " + position);
        // TODO service response data to pass
         gotoUpdateInventoryView(position);
    }

    @Override
    public void onCountInventory(int position) {
        if (LogFlag.bLogOn)Log.d(TAG, "onCountInventory: " + position);
        // TODO service response data to pass
        gotoInventoryCountView();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.addItemButton:
                gotoAddItemView("New Item");
                break;

            case R.id.addNewItemButton:
                gotoAddItemView("New Item");
                break;

            case R.id.datePickerButton:
                setDateFilterPopupView();
                break;

            case R.id.btnClose:
                fromDateString = fromDate.getText().toString();
                toDateString = toDate.getText().toString();
                datePickerButton.setText(fromDateString +" - "+toDateString);
                PopUp.dismiss();
                break;

            case R.id.applyButton:
                fromDateString = fromDate.getText().toString();
                toDateString = toDate.getText().toString();
                datePickerButton.setText(fromDateString +" - "+toDateString);
                PopUp.dismiss();
                break;

            case R.id.cancelButton:
                fromDateString = fromDate.getText().toString();
                toDateString = toDate.getText().toString();
                datePickerButton.setText(fromDateString +" - "+toDateString);
                PopUp.dismiss();
                break;
        }

    }

    void setDateFilterPopupView() {

        View popUpView = getLayoutInflater().inflate(R.layout.popupcalenderview, null); // inflating popup layout
        PopUp = VTools.createPopUp(popUpView);


        PopUp.setBackgroundDrawable(new BitmapDrawable());
        PopUp.setOutsideTouchable(true);

        TextView popUpTitle = (TextView) popUpView.findViewById(R.id.popUpTitle);
        ListView listView = (ListView) popUpView.findViewById(R.id.listView);
        ImageButton btnClose = (ImageButton) popUpView.findViewById(R.id.btnClose);
        CalendarView calendarView  = (CalendarView) popUpView.findViewById(R.id.calendarView);
        fromDate =(EditText)popUpView.findViewById(R.id.fromDate);
        toDate =(EditText)popUpView.findViewById(R.id.toDate);
        Button applyButton =(Button)popUpView.findViewById(R.id.applyButton);
        Button cancelButton =(Button)popUpView.findViewById(R.id.cancelButton);


        popUpTitle.setText("Choose Date ");


        String[] items = { "Today", "Today Last Year", "Yesterday", "Last 7 Days", "Last 30 Days",
                "This Month","Same Moth To Same Day Last Year","Same Moth Last Year","Last Month","This Year","Last Year",
                "All Time","Custom"};


        if(!fromDateString.isEmpty() && !toDateString.isEmpty()){
            fromDate.setText(fromDateString);
            toDate.setText(toDateString);
            calendarView.setDate(VTools.convertStringDateToLong(fromDateString));
        }else {
            fromDate.setText(todayDate);
            toDate.setText(todayDate);
            calendarView.setDate(Calendar.getInstance().getTimeInMillis());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.popuplist, items);

        listView.setAdapter(adapter);



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                fromDate.setText(month+"/"+dayOfMonth+"/"+year);
                toDate.setText(month+"/"+dayOfMonth+"/"+year);
            }
        });

        listView.setOnItemClickListener(this);
        btnClose.setOnClickListener(this);
        applyButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        fromDate.addTextChangedListener(textWatcherFromDate);
        toDate.addTextChangedListener(textWatcherToDate);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                // Today
                if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected 1: " + position);
                fromDate.setText(todayDate);
                toDate.setText(todayDate);
                datePickerButton.setText(todayDate +" - "+todayDate);
                break;
            case 1:
                // Today Last Year
                if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected 2: " + position);
                lastYearDate =VTools.getLastYearDate();
                fromDate.setText(lastYearDate);
                toDate.setText(lastYearDate);
                datePickerButton.setText( lastYearDate +" - "+ lastYearDate);
                break;

            case 2:
                // Yesterday

                String yesterday = VTools.getYesterdayDate();
                fromDate.setText(yesterday);
                toDate.setText(yesterday);
                datePickerButton.setText( yesterday +" - "+yesterday);
                break;

            case 3:
                // Last 7 Days

                String lastWeekDate =  VTools.getLastWeekDate();
                fromDate.setText(lastWeekDate);
                toDate.setText(todayDate);
                datePickerButton.setText(lastWeekDate +" - "+todayDate);
                break;
            case 4:
                // Last Month

                String lastMonthDate =  VTools.getLastMonthDate();
                fromDate.setText(lastMonthDate);
                toDate.setText(todayDate);
                datePickerButton.setText(lastMonthDate +" - "+todayDate);
                break;
            case 5:
                // This Month

                String thisMonthDate =  VTools.getThisMonthDate();
                fromDate.setText(thisMonthDate);
                toDate.setText(todayDate);
                datePickerButton.setText(thisMonthDate +" - "+todayDate);
                break;

            case 6:
                // This Month same day Of Last Year

                thisMonthOnLastYearDate =  VTools.getThisMonthOnLastYearDate(); // This Month First day of Last Year
                lastYearDate =VTools.getLastYearDate();
                fromDate.setText(thisMonthOnLastYearDate);
                toDate.setText(lastYearDate);
                datePickerButton.setText(thisMonthOnLastYearDate +" - "+lastYearDate);
                break;

            case 7:
                // This Month Full Last Year

                thisMonthOnLastYearDate =  VTools.getThisMonthOnLastYearDate(); // This Month First day of Last Year
                String thisMonthLastYearDate = VTools.getThisMonthLastYearDate();  // This Month Last day of Last Year
                fromDate.setText(thisMonthOnLastYearDate);
                toDate.setText(thisMonthLastYearDate);
                datePickerButton.setText(thisMonthOnLastYearDate +" - "+thisMonthLastYearDate);
                break;

            case 8:
                // Last Month
                if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected 2: " + position);
                String getLastMonthFirstDay =  VTools.getLastMonthFirstDay(); // Last Month First day of Last Year
                String getLastMonthLastDay = VTools.getLastMonthLastDay();  // Last Month Last day of Last Year
                fromDate.setText(getLastMonthFirstDay);
                toDate.setText(getLastMonthLastDay);
                datePickerButton.setText(getLastMonthFirstDay +" - "+getLastMonthLastDay);
                break;

            case 9:
                // This Year
                String getThisYearFirstDate =  VTools.getThisYearFirstDate(); // This Year First day
                fromDate.setText(getThisYearFirstDate);
                toDate.setText(todayDate);
                datePickerButton.setText(getThisYearFirstDate +" - "+todayDate);
                break;

            case 10:
                // Last Year

                String getLastYearFirstDay =  VTools.getLastYearFirstDay(); // This Last Year First day
                String getLastYearLastDay = VTools.getLastYearLastDay();  // This Last Year Last day
                fromDate.setText(getLastYearFirstDay);
                toDate.setText(getLastYearLastDay);
                datePickerButton.setText(getLastYearFirstDay +" - "+getLastYearLastDay);
                break;

            case 11:
                // All Time

                String getDefaultDate =  VTools.getDefaultDate(); // This Default to 7 years back
                fromDate.setText(getDefaultDate);
                toDate.setText(todayDate);
                datePickerButton.setText(getDefaultDate +" - "+todayDate);
                break;

        }
        fromDateString = fromDate.getText().toString();
        toDateString = toDate.getText().toString();
        PopUp.dismiss();
    }

    @Background
    void callItemResponse() {

        if (LogFlag.bLogOn)Log.d(TAG, "callItemResponse");
        VPOSRestClient vposRestClient = new VPOSRestClient();
        itemResponse = vposRestClient.getItem();
        if (null != itemResponse && itemResponse.getStatus().equals("true")) {
            if (LogFlag.bLogOn)Log.d(TAG, "itemResponse: " + itemResponse.toString());
            hideLoaderGifImage();
            itemResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("itemResponse failed");
        }
    }

    @UiThread
    public void itemResponseFinish(){

        itemCount = itemResponse.getItems().length;
        itemCountCheck();

        addItemsOnSpinner();
        addFabView();
        addRecyclerView();

    }

    @Background
    void callItemDeleteResponse(String itemId) {

        if (LogFlag.bLogOn)Log.d(TAG, "callItemDeleteResponse");
        VPOSRestClient vposRestClient = new VPOSRestClient();
        UpdateItemResponse updateItemResponse = vposRestClient.deleteItem(itemId);
        if (null != updateItemResponse && updateItemResponse.getStatus().equals("true")) {
            if (LogFlag.bLogOn)Log.d(TAG, "updateItemResponse: " + updateItemResponse.toString());
            hideLoaderGifImage();
            callItemResponse();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("updateItemResponse failed");
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

    private void gotoAddItemView(String pageName){
        Intent intent = new Intent(getApplicationContext(), AddItemActivity_.class);
        intent.putExtra("PageName",pageName);
        startActivity(intent);
    }

    private void gotoUpdateItemView(String pageName,int itemPosition){
        Gson gson = new GsonBuilder().create();
        Intent intent = new Intent(getApplicationContext(), AddItemActivity_.class);
        intent.putExtra("PageName",pageName);
        intent.putExtra("ItemData",gson.toJson(itemResponse.getItems()[itemPosition]));
        startActivity(intent);
    }

    private void gotoUpdateInventoryView(int itemPosition){
        Gson gson = new GsonBuilder().create();
        Intent intent = new Intent(getApplicationContext(), UpdateInventoryActivity_.class);
        intent.putExtra("ItemData",gson.toJson(itemResponse.getItems()[itemPosition]));
        startActivity(intent);
    }


    private void gotoInventoryCountView(){
        Intent intent = new Intent(getApplicationContext(), InventoryCountActivity_.class);
        startActivity(intent);
    }

    private void gotoEditMultipleItemView(int [] selectedPositionsArray){
        Gson gson = new GsonBuilder().create();
        Intent intent = new Intent(getApplicationContext(), EditMultipleItemActivity_.class);
        intent.putExtra("SelectedPosition",selectedPositionsArray);
        intent.putExtra("ItemResponse",gson.toJson(itemResponse));
        startActivity(intent);
    }


    private void gotoBarcodeGenerateView(int [] selectedPositionsArray){
        Gson gson = new GsonBuilder().create();
        Intent intent = new Intent(getApplicationContext(), BarcodeGenerateActivity_.class);
        intent.putExtra("PageTag","Item");
        intent.putExtra("SelectedPosition",selectedPositionsArray);
        intent.putExtra("ItemResponse",gson.toJson(itemResponse));
        startActivity(intent);
    }

}

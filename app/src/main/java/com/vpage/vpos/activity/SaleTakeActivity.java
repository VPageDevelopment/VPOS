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
import android.widget.Toast;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.vpage.vpos.R;
import com.vpage.vpos.adapter.SaleFieldSpinnerAdapter;
import com.vpage.vpos.adapter.SaleListAdapter;
import com.vpage.vpos.httputils.VPOSRestClient;
import com.vpage.vpos.pojos.sale.SaleResponse;
import com.vpage.vpos.pojos.sale.UpdateSaleResponse;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.RecyclerTouchListener;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.callBack.CheckedCallBack;
import com.vpage.vpos.tools.callBack.FilterCallBack;
import com.vpage.vpos.tools.callBack.RecyclerTouchCallBack;
import com.vpage.vpos.tools.utils.LogFlag;
import com.vpage.vpos.tools.utils.NetworkUtil;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@EActivity(R.layout.activity_saletake)
public class SaleTakeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, FilterCallBack, CheckedCallBack,OnNetworkChangeListener, AdapterView.OnItemClickListener {

    private static final String TAG = SaleTakeActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.noSaleContent)
    LinearLayout noSaleContentLayout;

    @ViewById(R.id.saleContent)
    LinearLayout saleContent;

    @ViewById(R.id.newSaleRegisterButton)
    Button newSaleRegisterButton;

    @ViewById(R.id.saleRegisterButton)
    Button saleRegisterButton;

    @ViewById(R.id.datePickerButton)
    Button datePickerButton;

    @ViewById(R.id.spinnerField)
    Spinner spinnerField;

    @ViewById(R.id.spinnerFormat)
    Spinner spinnerFormat;

    @ViewById(R.id.saleRecycleView)
    RecyclerView recyclerView;

    @ViewById(R.id.fabMenu)
    FloatingActionMenu floatingActionMenu;

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    FloatingActionButton deleteFAB,printFAB;

    private int mScrollOffset = 4;

    SaleListAdapter listAdapter;
    SaleFieldSpinnerAdapter fieldSpinnerAdapter;

    private Handler mUiHandler = new Handler();
    List<String> spinnerList;

    Boolean checkedStatus = false;
    private List<Boolean> checkedPositionArrayList = new ArrayList<>();

    boolean isNetworkAvailable = false;

    Activity activity;

    String spinnerFormatData = "",fromDateString="",toDateString="",todayDate="",thisMonthOnLastYearDate="",
            lastYearDate="";

    PopupWindow PopUp;

    EditText fromDate,toDate;

    TextWatcher textWatcherFromDate,textWatcherToDate;

    SaleResponse saleResponse;
    int saleCount = 0;


    @AfterViews
    public void onInitView() {

        activity = SaleTakeActivity.this;

        Intent callingIntent=getIntent();

        setActionBarSupport();

        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);

        callSaleResponse();
    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Sale");

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
        if (LogFlag.bLogOn)Log.d(TAG, "Network Availability: "+status);
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

    void saleCountCheck(){

        if(saleCount == 0){
            noSaleContentLayout.setVisibility(View.VISIBLE);
            saleContent.setVisibility(View.GONE);
            floatingActionMenu.setVisibility(View.GONE);
            newSaleRegisterButton.setOnClickListener(this);
        }else {
            noSaleContentLayout.setVisibility(View.GONE);
            saleContent.setVisibility(View.VISIBLE);
            floatingActionMenu.setVisibility(View.VISIBLE);
            saleRegisterButton.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.newSaleRegisterButton:
                gotoSalesView();
                break;

            case R.id.saleRegisterButton:
                gotoSalesView();
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

    private void addItemsOnSpinner() {
        try{
            spinnerList = new ArrayList<>();
            spinnerList.add("Filter By");
            spinnerList.add("Id");
            spinnerList.add("Time");
            spinnerList.add("Customer");
            spinnerList.add("Amount Due");
            spinnerList.add("Amount Tendered");
            spinnerList.add("Change Due");
            spinnerList.add("Type");
            spinnerList.add("Invoice #");

            fieldSpinnerAdapter = new SaleFieldSpinnerAdapter(activity, R.layout.item_spinner_field, spinnerList);
            fieldSpinnerAdapter.setFilterCallBack(this);
            spinnerField.setAdapter(fieldSpinnerAdapter);
            spinnerFormatData = spinnerFormat.getSelectedItem().toString();
            if (LogFlag.bLogOn) Log.d(TAG, "spinnerFormatData: " + spinnerFormatData);

            spinnerFormat.setOnItemSelectedListener(this);

        }catch (Exception e){
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }
    }

    private void addRecyclerView(){

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        listAdapter = new SaleListAdapter(activity,saleResponse);
        listAdapter.setCheckedCallBack(this);
        recyclerView.setAdapter(listAdapter);
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
        deleteFAB.setColorNormalResId(R.color.colorPrimaryDark);
        deleteFAB.setColorPressedResId(R.color.colorPrimary);
        deleteFAB.setLabelText("Delete");
        deleteFAB.setImageResource(R.drawable.delete_white);

        printFAB = new FloatingActionButton(activity);
        printFAB.setButtonSize(FloatingActionButton.SIZE_MINI);
        printFAB.setColorNormalResId(R.color.colorPrimaryDark);
        printFAB.setColorPressedResId(R.color.colorPrimary);
        printFAB.setLabelText("Print");
        printFAB.setImageResource(R.drawable.print);


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
                        printFAB.setVisibility(View.VISIBLE);
                        deleteFAB.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(activity, "Pls select any field to proceed", Toast.LENGTH_SHORT).show();
                        printFAB.setVisibility(View.GONE);
                        deleteFAB.setVisibility(View.GONE);
                    }
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

        printFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                printFAB.setLabelColors(ContextCompat.getColor(activity, R.color.LiteGray),
                        ContextCompat.getColor(activity, R.color.LiteGray),
                        ContextCompat.getColor(activity, R.color.White));
                printFAB.setLabelTextColor(ContextCompat.getColor(activity, R.color.Black));

                floatingActionMenu.toggle(true);


            }
        });

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
            listAdapter = new SaleListAdapter(activity,saleResponse);
            listAdapter.setCheckedCallBack(this);
            recyclerView.setAdapter(listAdapter);
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
                listAdapter.filter(searchQuery.toString().trim());
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
    public void onSelectedStatus(Boolean checkedStatus) {
        this.checkedStatus = checkedStatus;
        if (LogFlag.bLogOn)Log.d(TAG, "checkedStatus: " + this.checkedStatus);

    }

    @Override
    public void onSelectedStatusArray(List<Boolean> checkedPositionArrayList) {
        if (LogFlag.bLogOn)Log.d(TAG, "checkedPositionArrayList: " + checkedPositionArrayList);
        this.checkedPositionArrayList = checkedPositionArrayList;
        if(this.checkedPositionArrayList.size()>0){
            floatingActionMenu.setVisibility(View.VISIBLE);
        }else {
            floatingActionMenu.setVisibility(View.VISIBLE);
        }

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
                            callSaleDeleteResponse(saleResponse.getItems()[i].getSales_id());
                        }
                    }
                }catch (IndexOutOfBoundsException e){
                    if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
                }
                listAdapter.notifyDataSetChanged();
                recyclerView.invalidate();

            }
        });
        alertDialog.show();

    }

    @Background
    void callSaleResponse() {

        if (LogFlag.bLogOn)Log.d(TAG, "callSaleResponse");
        VPOSRestClient vposRestClient = new VPOSRestClient();
        saleResponse = vposRestClient.getScales();
        if (null != saleResponse && saleResponse.getStatus().equals("true")) {
            if (LogFlag.bLogOn)Log.d(TAG, "saleResponse: " + saleResponse.toString());
            hideLoaderGifImage();
            saleResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("saleResponse failed");
        }
    }

    @Background
    void callSaleDeleteResponse(String saleId) {

        if (LogFlag.bLogOn)Log.d(TAG, "callSaleDeleteResponse");
        VPOSRestClient vposRestClient = new VPOSRestClient();
        UpdateSaleResponse updateSaleResponse = vposRestClient.deleteSale(saleId);
        if (null != updateSaleResponse && updateSaleResponse.getStatus().equals("true")) {
            if (LogFlag.bLogOn)Log.d(TAG, "updateSaleResponse: " + updateSaleResponse.toString());
            hideLoaderGifImage();
            callSaleResponse();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("updateSaleResponse failed");
        }
    }

    @UiThread
    public void saleResponseFinish(){

        saleCount = saleResponse.getItems().length;
        saleCountCheck();

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

    private void addFabButton(){

        floatingActionMenu.addMenuButton(printFAB);
        floatingActionMenu.addMenuButton(deleteFAB);
        printFAB.setVisibility(View.GONE);
        deleteFAB.setVisibility(View.GONE);
    }


    private void gotoSalesView(){
        Intent intent = new Intent(getApplicationContext(), SalesActivity_.class);
        startActivity(intent);
    }

}




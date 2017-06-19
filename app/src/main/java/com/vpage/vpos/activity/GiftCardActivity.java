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
import android.widget.Toast;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vpos.R;
import com.vpage.vpos.adapter.GiftCardFieldSpinnerAdapter;
import com.vpage.vpos.adapter.GiftCardListAdapter;
import com.vpage.vpos.httputils.VPOSRestClient;
import com.vpage.vpos.pojos.giftCards.GiftCardResponse;
import com.vpage.vpos.pojos.giftCards.UpdateGiftCardResponse;
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
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.List;

@Fullscreen
@EActivity(R.layout.activity_giftcard)
public class GiftCardActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, FilterCallBack, EditCallBack, CheckedCallBack {

    private static final String TAG = GiftCardActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.noGiftCardContent)
    LinearLayout noGiftCardContentLayout;

    @ViewById(R.id.giftCardContent)
    LinearLayout giftCardContent;

    @ViewById(R.id.addNewGiftCardButton)
    Button addNewGiftCardButton;

    @ViewById(R.id.addGiftCardButton)
    Button addGiftCardButton;

    @ViewById(R.id.spinnerField)
    Spinner spinnerField;

    @ViewById(R.id.spinnerFormat)
    Spinner spinnerFormat;

    @ViewById(R.id.giftCardRecycleView)
    RecyclerView recyclerView;

    @ViewById(R.id.fabMenu)
    FloatingActionMenu floatingActionMenu;

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    FloatingActionButton deleteFAB;

    String spinnerFormatData = "";
    private int mScrollOffset = 4;

    GiftCardListAdapter listAdapter;
    GiftCardFieldSpinnerAdapter fieldSpinnerAdapter;

    private Handler mUiHandler = new Handler();

    List<String> spinnerList;

    Boolean checkedStatus = false;
    private List<Boolean> checkedPositionArrayList = new ArrayList<>();

    Activity activity;

    GiftCardResponse giftCardResponse;

    int itemCount= 0;

    @AfterViews
    public void onInitView() {

        activity = GiftCardActivity.this;

        setActionBarSupport();

        callGiftCardResponse();
    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Gift Card");

    }

    void itemCountCheck(){

        if(itemCount == 0){
            noGiftCardContentLayout.setVisibility(View.VISIBLE);
            giftCardContent.setVisibility(View.GONE);
            floatingActionMenu.setVisibility(View.GONE);
            addNewGiftCardButton.setOnClickListener(this);
        }else {
            noGiftCardContentLayout.setVisibility(View.GONE);
            giftCardContent.setVisibility(View.VISIBLE);
            floatingActionMenu.setVisibility(View.VISIBLE);
            addGiftCardButton.setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {
        gotoAddGiftCardView("New Gift Card");
    }

    private void addItemsOnSpinner() {
        try{
            spinnerList = new ArrayList<>();
            spinnerList.add("Filter By");
            spinnerList.add("Id");
            spinnerList.add("First Name");
            spinnerList.add("Last Name");
            spinnerList.add("GiftCard Number");
            spinnerList.add("Value");

            fieldSpinnerAdapter = new GiftCardFieldSpinnerAdapter(activity, R.layout.item_spinner_field, spinnerList);
            fieldSpinnerAdapter.setFilterCallBack(this);
            spinnerField.setAdapter(fieldSpinnerAdapter);
            spinnerFormatData = spinnerFormat.getSelectedItem().toString();
            if (LogFlag.bLogOn)Log.d(TAG, "spinnerFormatData: " + spinnerFormatData);

            spinnerFormat.setOnItemSelectedListener(this);

        }catch (Exception e){
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }
    }

    private void addRecyclerView(){;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        listAdapter = new GiftCardListAdapter(activity,giftCardResponse);
        listAdapter.setEditCallBack(this);
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

                        deleteFAB.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(activity, "Pls select any field to proceed", Toast.LENGTH_SHORT).show();
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
                            callGiftCardDeleteResponse(giftCardResponse.getGiftCards()[i].getGift_card_id());
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


    private void addFabButton(){

        floatingActionMenu.addMenuButton(deleteFAB);
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
            listAdapter = new GiftCardListAdapter(activity,giftCardResponse);
            listAdapter.setEditCallBack(this);
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
        gotoUpdateGiftCardView("Update Gift Card",position);
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
    void callGiftCardResponse() {

        if (LogFlag.bLogOn)Log.d(TAG, "callGiftCardResponse");
        VPOSRestClient vposRestClient = new VPOSRestClient();
        giftCardResponse = vposRestClient.getGiftCards();
        if (giftCardResponse.getStatus().equals("true") && null != giftCardResponse) {
            if (LogFlag.bLogOn)Log.d(TAG, "giftCardResponse: " + giftCardResponse.toString());
            hideLoaderGifImage();
            giftCardResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("giftCardResponse failed");
        }
    }

    @Background
    void callGiftCardDeleteResponse(String giftCardId) {

        if (LogFlag.bLogOn)Log.d(TAG, "callGiftCardDeleteResponse");
        VPOSRestClient vposRestClient = new VPOSRestClient();
        UpdateGiftCardResponse updateGiftCardResponse = vposRestClient.deleteGiftCard(giftCardId);
        if (null != updateGiftCardResponse && updateGiftCardResponse.getStatus().equals("true")) {
            if (LogFlag.bLogOn)Log.d(TAG, "updateGiftCardResponse: " + updateGiftCardResponse.toString());
            hideLoaderGifImage();
            callGiftCardResponse();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("updateGiftCardResponse failed");
        }
    }

    @UiThread
    public void giftCardResponseFinish(){

        itemCount = giftCardResponse.getGiftCards().length;
        itemCountCheck();

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

    private void gotoAddGiftCardView(String pageName){
        Intent intent = new Intent(getApplicationContext(), AddGiftCardActivity_.class);
        intent.putExtra("PageName",pageName);
        startActivity(intent);
    }

    private void gotoUpdateGiftCardView(String pageName,int itemPosition){
        Gson gson = new GsonBuilder().create();
        Intent intent = new Intent(getApplicationContext(), AddSupplierActivity_.class);
        intent.putExtra("PageName",pageName);
        intent.putExtra("GiftCardData",gson.toJson(giftCardResponse.getGiftCards()[itemPosition]));
        startActivity(intent);
    }

}

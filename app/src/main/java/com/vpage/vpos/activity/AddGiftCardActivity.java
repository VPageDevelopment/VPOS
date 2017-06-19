package com.vpage.vpos.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.httputils.VPOSRestClient;
import com.vpage.vpos.pojos.giftCards.GiftCard;
import com.vpage.vpos.pojos.giftCards.UpdateGiftCardResponse;
import com.vpage.vpos.pojos.giftCards.addGiftCards.AddGiftCardsRequest;
import com.vpage.vpos.pojos.giftCards.addGiftCards.AddGiftCardsResponse;
import com.vpage.vpos.tools.ActionEditText;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.VPOSRestTools;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.utils.LogFlag;
import com.vpage.vpos.tools.utils.NetworkUtil;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import butterknife.ButterKnife;
import butterknife.InjectView;

@Fullscreen
@EActivity(R.layout.activity_addgiftcard)
public class AddGiftCardActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener, View.OnFocusChangeListener, OnNetworkChangeListener {

    private static final String TAG = AddGiftCardActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.customer)
    EditText customer;

    @ViewById(R.id.giftCardNo)
    EditText giftCardNo;

    @ViewById(R.id.value)
    EditText value;

    @ViewById(R.id.textError)
    TextView textError;

    @ViewById(R.id.submitButton)
    Button submitButton;

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    @InjectView(R.id.google_progress)
    ProgressBar mProgressBar;

    boolean isNetworkAvailable = false;

    String giftCardNoInput="", valueInput ="",customerInput="";

    String pageName = " ";

    Activity activity;

    AddGiftCardsRequest addGiftCardsRequest;

    GiftCard giftCard;

    @AfterViews
    public void init() {

        activity = AddGiftCardActivity.this;

        setActionBarSupport();

        ButterKnife.inject(this);

        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);

        new ActionEditText(this);
        submitButton.setOnClickListener(this);
        value.setOnKeyListener(this);
        value.setOnFocusChangeListener(this);
        giftCardNo.setOnFocusChangeListener(this);
        customer.setOnFocusChangeListener(this);

        Intent callingIntent=getIntent();
        pageName = callingIntent.getStringExtra("PageName");

        if(pageName.equals("Update Gift Card")){

            String giftCardResponseString = callingIntent.getStringExtra("GiftCardData");

            giftCard = VPOSRestTools.getInstance().getGiftCardData(giftCardResponseString);
            setInputs();
        }
    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(pageName);

    }


    @Override
    protected void onResume() {
        super.onResume();
        /**Dynamically*/
        Rect bounds = mProgressBar.getIndeterminateDrawable().getBounds();
        mProgressBar.setIndeterminateDrawable(VTools.getProgressDrawable(activity));
        mProgressBar.getIndeterminateDrawable().setBounds(bounds);
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
    public void onClick(View v) {
        validateInput();
    }


    void setInputs(){

        giftCardNo.setText(giftCard.getGift_card_number());
        value.setText(giftCard.getGc_value());
        customer.setText(giftCard.getCustomer_fk());

    }


    void validateInput(){
        if(isNetworkAvailable){

            giftCardNoInput = giftCardNo.getText().toString();
            valueInput = value.getText().toString();
            customerInput = customer.getText().toString();

            if (!giftCardNoInput.isEmpty()&& !valueInput.isEmpty()) {

                textError.setVisibility(View.GONE);
                //playGifView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.GONE);

                if(pageName.equals("Update Gift Card")){
                    callGiftCardUpdateResponse(giftCard.getGift_card_id());
                }else {
                    callAddGiftCardsResponse();
                }
            } else {
                hideLoaderGifImage();
                setErrorMessage("Fill all Required Input");
            }
        }else {
            hideLoaderGifImage();
            setErrorMessage("Check Network Connection");
        }
    }

    void setErrorMessage(String errorMessage) {

        textError.setVisibility(View.VISIBLE);
        textError.setText(errorMessage);
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == EditorInfo.IME_ACTION_GO ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            validateInput();

        }
        return false;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            textError.setVisibility(View.GONE);
        }
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
    @Background
    void callAddGiftCardsResponse() {
        if (LogFlag.bLogOn)Log.d(TAG, "callAddGiftCardsResponse");
        setAddGiftCardsRequestData();

        VPOSRestClient vposRestClient = new VPOSRestClient();
        vposRestClient.setAddGiftCardParams(addGiftCardsRequest);
        AddGiftCardsResponse addGiftCardsResponse = vposRestClient.addGiftCard();
        if (null != addGiftCardsResponse) {
            if (LogFlag.bLogOn)Log.d(TAG, "addGiftCardsResponse: " + addGiftCardsResponse.toString());
            hideLoaderGifImage();
            addGiftCardsResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("addGiftCardsResponse failed");
        }
    }

    @Background
    void callGiftCardUpdateResponse(String giftCardId) {
        if (LogFlag.bLogOn)Log.d(TAG, "callGiftCardUpdateResponse");
        setAddGiftCardsRequestData();

        VPOSRestClient vposRestClient = new VPOSRestClient();
        vposRestClient.setAddGiftCardParams(addGiftCardsRequest);
        UpdateGiftCardResponse updateGiftCardResponse = vposRestClient.updateGiftCard(giftCardId);
        if (null != updateGiftCardResponse && updateGiftCardResponse.getStatus().equals("true")) {
            if (LogFlag.bLogOn)Log.d(TAG, "updateGiftCardResponse: " + updateGiftCardResponse.toString());
            hideLoaderGifImage();
            addGiftCardsResponseFinish();
        } else {
            hideLoaderGifImage();
            showToastErrorMsg("updateGiftCardResponse failed");
        }
    }

    @UiThread
    public void addGiftCardsResponseFinish(){
        gotoGiftCardView();
    }

    @UiThread
    public void hideLoaderGifImage(){
        //playGifView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
    }

    @UiThread
    public void showToastErrorMsg(String error) {
        VTools.showToast(error);
    }


    void  setAddGiftCardsRequestData(){

        addGiftCardsRequest = new AddGiftCardsRequest();
        addGiftCardsRequest.setGift_card_number(giftCardNoInput);
        addGiftCardsRequest.setGc_value(valueInput);
        addGiftCardsRequest.setCustomer_fk(customerInput);

    }

    private void gotoGiftCardView(){
        Intent intent = new Intent(getApplicationContext(), GiftCardActivity_.class);
        startActivity(intent);
        finish();
    }


}



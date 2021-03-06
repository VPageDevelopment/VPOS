package com.vpage.vpos.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vpos.R;
import com.vpage.vpos.httputils.VPOSRestClient;
import com.vpage.vpos.pojos.SignInRequest;
import com.vpage.vpos.pojos.SignInResponse;
import com.vpage.vpos.pojos.ValidationStatus;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.VPOSPreferences;
import com.vpage.vpos.tools.VPOSTools;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.utils.LogFlag;
import com.vpage.vpos.tools.utils.NetworkUtil;
import com.vpage.vpos.tools.utils.ValidationUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import butterknife.ButterKnife;
import butterknife.InjectView;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity implements View.OnKeyListener,OnNetworkChangeListener {

    private static final String TAG = LoginActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.loginButton)
    Button loginButton;

    @ViewById(R.id.password)
    EditText password;

    @ViewById(R.id.userName)
    EditText userName;

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    @InjectView(R.id.google_progress)
    ProgressBar mProgressBar;

    @ViewById(R.id.textError)
    TextView textError;

    String userNameInput = "", userPasswordInput = "";
    ValidationStatus validationStatus;

    boolean isNetworkAvailable = false;

    SignInRequest signInRequest;

    Activity activity;

    @AfterViews
    public void onInitView() {
        ButterKnife.inject(this);

        activity = LoginActivity.this;

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");

        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);

        password.setOnKeyListener(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInput();

            }
        });
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
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == EditorInfo.IME_ACTION_GO ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            switch (v.getId()) {

                case R.id.password:
                    validateInput();
                    break;
            }

        }
        return false;
    }

    @FocusChange({R.id.userName, R.id.password})
    public void focusChangedOnUser(View v, boolean hasFocus) {
        if (hasFocus) {
            textError.setVisibility(View.GONE);
        }
    }



    void validateInput(){

        if(isNetworkAvailable){

            userNameInput = userName.getText().toString();
            userPasswordInput = password.getText().toString();

            validationStatus = ValidationUtils.isValidLoginUserNamePassword(userNameInput, userPasswordInput);

            if (!validationStatus.isStatus()) {
                hideLoaderGifImage();
                setErrorMessage(validationStatus.getMessage());
                return;
            }

               // playGifView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.GONE);
                textError.setVisibility(View.GONE);
                callSignInResponse();

        }else {

            hideLoaderGifImage();
            setErrorMessage(getResources().getString(R.string.connection_check));
        }
    }


    @Background
    void callSignInResponse() {
        if (LogFlag.bLogOn)Log.d(TAG, "callSignInResponse");
        setSignInRequestData();

        VPOSRestClient vposRestClient = new VPOSRestClient();
        SignInResponse signInResponse = vposRestClient.getSignInResponse(signInRequest);
        if(signInResponse != null){
            if (LogFlag.bLogOn)Log.d(TAG, "signInResponse: " + signInResponse.toString());
            if(signInResponse.getStatus().equals("ok")){
                hideLoaderGifImage();
                gotoHomeView(signInResponse);
            }else {
                hideLoaderGifImage();
                setErrorMessage(signInResponse.getStatus());
            }
        }else {
            hideLoaderGifImage();
            showToastErrorMsg("signInResponse is null");
        }
    }

    @UiThread
    public void hideLoaderGifImage(){
        //playGifView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
    }

    @UiThread
    public void showToastErrorMsg(String error) {
        VTools.showToast(error);
    }

    void  setSignInRequestData(){

        signInRequest = new SignInRequest();
        signInRequest.setUsername(userNameInput);
        signInRequest.setPassword(userPasswordInput);

    }



    void gotoHomeView(SignInResponse signInResponse){

        Gson gson = new GsonBuilder().create();
        // Keep the login
        VPOSPreferences.save("userdata", gson.toJson(VPOSTools.getInstance().getActiveUser(signInRequest)));
        VPOSPreferences.save("isLoggedIn", "true");

        Intent intent = new Intent(getApplicationContext(), HomeActivity_.class);
        intent.putExtra("ActiveUser", gson.toJson(VPOSTools.getInstance().getActiveUser(signInRequest)));
        startActivity(intent);
        finish();
    }


    void setErrorMessage(String errorMessage) {
        textError.setVisibility(View.VISIBLE);
        textError.setText(errorMessage);
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
}

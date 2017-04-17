package com.vpage.vpos.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.vpage.vpos.R;
import com.vpage.vpos.pojos.ValidationStatus;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.utils.NetworkUtil;
import com.vpage.vpos.tools.utils.ValidationUtils;


public class LoginActivity extends AppCompatActivity implements View.OnKeyListener,OnNetworkChangeListener {

    private static final String TAG = LoginActivity.class.getName();

    Button loginButton;
    EditText password, userName;
    TextView textError;

    String userNameInput = "", userPasswordInput = "";
    ValidationStatus validationStatus;

    PlayGifView playGifView;

    boolean isNetworkAvailable = false;

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");*/

        loginButton = (Button) findViewById(R.id.loginButton);
        password = (EditText) findViewById(R.id.password);
        userName = (EditText) findViewById(R.id.userName);
        textError  = (TextView) findViewById(R.id.textError);
        playGifView =(PlayGifView)findViewById(R.id.viewGif) ;


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


    void validateInput(){

        if(isNetworkAvailable){

            userNameInput = userName.getText().toString();
            userPasswordInput = password.getText().toString();

            validationStatus = ValidationUtils.isValidLoginUserNamePassword(userNameInput, userPasswordInput);

            if (validationStatus.isStatus() == false) {
                playGifView.setVisibility(View.GONE);
                setErrorMessage(validationStatus.getMessage());
            } else {
                playGifView.setVisibility(View.VISIBLE);
                textError.setVisibility(View.GONE);

                gotoHomeView();
            }

        }else {

            playGifView.setVisibility(View.GONE);
            setErrorMessage("Check Network Connection");
        }


    }

    void gotoHomeView(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }


    void setErrorMessage(String errorMessage) {
        textError.setVisibility(View.VISIBLE);
        textError.setText(errorMessage);
    }

    @Override
    public void onChange(String status) {
        Log.d(TAG, "Network Availability: "+status);
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
        Log.d(TAG, "isNetworkAvailable: "+isNetworkAvailable);

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
        Log.d(TAG, "isNetworkAvailable: "+isNetworkAvailable);

    }
}

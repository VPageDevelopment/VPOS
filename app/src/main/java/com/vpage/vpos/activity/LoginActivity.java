package com.vpage.vpos.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.pojos.ValidationStatus;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.utils.LogFlag;
import com.vpage.vpos.tools.utils.NetworkUtil;
import com.vpage.vpos.tools.utils.ValidationUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ViewById;

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

    @ViewById(R.id.textError)
    TextView textError;

    String userNameInput = "", userPasswordInput = "";
    ValidationStatus validationStatus;

    boolean isNetworkAvailable = false;

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }


    @AfterViews
    public void onInitView() {

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
        } else {

        }
    }



    void validateInput(){

        if(isNetworkAvailable){

            userNameInput = userName.getText().toString();
            userPasswordInput = password.getText().toString();

            validationStatus = ValidationUtils.isValidLoginUserNamePassword(userNameInput, userPasswordInput);

            if (!validationStatus.isStatus()) {
                playGifView.setVisibility(View.GONE);
                setErrorMessage(validationStatus.getMessage());
                return;
            }


                playGifView.setVisibility(View.VISIBLE);
                textError.setVisibility(View.GONE);
              // TODO Service call
                gotoHomeView();


        }else {

            playGifView.setVisibility(View.GONE);
            setErrorMessage(getResources().getString(R.string.connection_check));
        }
    }

    void gotoHomeView(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity_.class);
        startActivity(intent);
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

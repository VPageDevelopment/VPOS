package com.vpage.vpos.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.pojos.ValidationStatus;
import com.vpage.vpos.tools.ActionEditText;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.utils.LogFlag;
import com.vpage.vpos.tools.utils.NetworkUtil;
import com.vpage.vpos.tools.utils.ValidationUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_addcustomer)
public class AddCustomerActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener, OnNetworkChangeListener {

    private static final String TAG = AddCustomerActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.firstName)
    EditText firstName;

    @ViewById(R.id.lastName)
    EditText lastName;

    @ViewById(R.id.textError)
    TextView textError;

    @ViewById(R.id.radioGroupGender)
    RadioGroup radioGroupGender;

    @ViewById(R.id.radioButtonMale)
    RadioButton radioButtonMale;

    @ViewById(R.id.radioButtonFemale)
    RadioButton radioButtonFemale;

    @ViewById(R.id.email)
    EditText email;

    @ViewById(R.id.phoneNumber)
    EditText phoneNumber;

    @ViewById(R.id.addressLine1)
    EditText addressLine1;

    @ViewById(R.id.addressLine2)
    EditText addressLine2;

    @ViewById(R.id.city)
    EditText city;

    @ViewById(R.id.state)
    EditText state;

    @ViewById(R.id.zip)
    EditText zip;

    @ViewById(R.id.country)
    EditText country;

    @ViewById(R.id.comments)
    EditText comments;

    @ViewById(R.id.submitButton)
    Button submitButton;

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    String firstNameInput = "", lastNameInput = "";
    ValidationStatus validationStatus;

    boolean isNetworkAvailable = false;

    TextWatcher textComments;

    @AfterViews
    public void onInitView() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Customer");


        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);
        lastName.setOnKeyListener(this);
        comments.setOnKeyListener(this);
        submitButton.setOnClickListener(this);


        setView();
    }

    private void setView(){

        textComments = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // you can check for enter key here
            }

            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 150) {
                    VTools.showAlertDialog(AddCustomerActivity.this,"Character Exceed the Limit");
                }
            }
        };

        new ActionEditText(this);
        comments.addTextChangedListener(textComments);
    }


    @Override
    public void onClick(View v) {

        validateInput();
        getInputs();

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == EditorInfo.IME_ACTION_GO ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            switch (v.getId()) {

                case R.id.lastName:

                    validateInput();

                    break;

                case R.id.comments:

                    validateInput();
                    getInputs();

                    break;
            }

        }
        return false;
    }

    void getInputs(){
        email.getText().toString();
        comments.getText().toString();
    }

    void validateInput(){

        if(isNetworkAvailable){

            firstNameInput = firstName.getText().toString();
            lastNameInput = lastName.getText().toString();

            validationStatus = ValidationUtils.isValidNamePassword(firstNameInput,lastNameInput);

            if (validationStatus.isStatus() == false) {
                playGifView.setVisibility(View.GONE);
                setErrorMessage(validationStatus.getMessage());
            } else {
                playGifView.setVisibility(View.VISIBLE);
                textError.setVisibility(View.GONE);

                gotoCustomerView();
            }

        }else {

            playGifView.setVisibility(View.GONE);
            setErrorMessage("Check Network Connection");
        }
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

    private void gotoCustomerView(){
        Intent intent = new Intent(getApplicationContext(), CustomerActivity.class);
        startActivity(intent);
    }


}
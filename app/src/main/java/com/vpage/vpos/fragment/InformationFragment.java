package com.vpage.vpos.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.vpage.vpos.R;
import com.vpage.vpos.pojos.ValidationStatus;
import com.vpage.vpos.tools.ActionEditText;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.utils.AppConstant;
import com.vpage.vpos.tools.utils.LogFlag;
import com.vpage.vpos.tools.utils.NetworkUtil;
import com.vpage.vpos.tools.utils.ValidationUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import java.io.IOException;
import butterknife.ButterKnife;

@EFragment(R.layout.fragment_information)
public class InformationFragment extends Fragment implements OnNetworkChangeListener, View.OnClickListener, View.OnKeyListener {

    private static final String TAG = InformationFragment.class.getName();

    @ViewById(R.id.companyName)
    EditText companyName;

    @ViewById(R.id.companyAddress)
    EditText companyAddress;

    @ViewById(R.id.companyImageView)
    ImageView companyImageView;

    @ViewById(R.id.selectButton)
    Button selectButton;

    @ViewById(R.id.website)
    EditText website;

    @ViewById(R.id.email)
    EditText email;

    @ViewById(R.id.telephone)
    EditText telephone;

    @ViewById(R.id.fax)
    EditText fax;

    @ViewById(R.id.returnPolicy)
    EditText returnPolicy;

    @ViewById(R.id.textError)
    TextView textError;

    @ViewById(R.id.submitButton)
    Button submitButton;


    String companyNameInput = "", companyAddressInput = "",returnPolicyInput="",phoneNumberInput="",imagePath = "";

    ValidationStatus validationStatusPhoneNumber;

    boolean isNetworkAvailable = false;

    TextWatcher textComments;

    public InformationFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void onInitView() {


        ButterKnife.inject(this.getActivity());
        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);

        returnPolicy.setOnKeyListener(this);
        selectButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);

       // mProgressBar.setVisibility(View.VISIBLE);
        setView();

    }


    @Override
    public void onResume() {
        super.onResume();
        companyName.requestFocus();

        companyName.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(companyName.getWindowToken(), 0);
            }
        },200);
    }

    @Override
    public void onChange(String status) {
        if (LogFlag.bLogOn) Log.d(TAG, "Network Availability: "+status);
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
        String status = NetworkUtil.getConnectivityStatusString(getActivity().getApplicationContext());
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

    private void setView(){

        textComments = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // you can check for enter key here
            }

            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 150) {
                    VTools.showAlertDialog(getActivity(),"Character Exceed the Limit");
                }
            }
        };

        new ActionEditText(getActivity());
        returnPolicy.addTextChangedListener(textComments);
        companyAddress.addTextChangedListener(textComments);
    }


    void getInputs(){

        email.getText().toString();
        website.getText().toString();
        fax.getText().toString();
    }


    void validateInput(){
        //mProgressBar.setVisibility(View.VISIBLE);
        if(isNetworkAvailable){

            companyNameInput = companyName.getText().toString();
            companyAddressInput = companyAddress.getText().toString();
            returnPolicyInput = returnPolicy.getText().toString();

            phoneNumberInput= telephone.getText().toString();
            if(!phoneNumberInput.isEmpty()){
                validationStatusPhoneNumber =  ValidationUtils.isValidUserPhoneNumber(phoneNumberInput);
                if (!validationStatusPhoneNumber.isStatus()) {
                    if (LogFlag.bLogOn)Log.d(TAG, validationStatusPhoneNumber.getMessage());
                    setErrorMessage(validationStatusPhoneNumber.getMessage());
                    return;
                }
            }


            if (!companyNameInput.isEmpty()&& !companyAddressInput.isEmpty()&& !returnPolicyInput.isEmpty()) {

                textError.setVisibility(View.GONE);
                submitButton.setVisibility(View.GONE);

              // TODO Service call
            } else {
                setErrorMessage("Fill all Required Input");
            }

           // mProgressBar.setVisibility(View.VISIBLE);
            textError.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);

        }else {
            setErrorMessage("Check Network Connection");
        }
    }

    void setErrorMessage(String errorMessage) {
        //mProgressBar.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
        textError.setVisibility(View.VISIBLE);
        textError.setText(errorMessage);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.selectButton:
                showFileChooser();
                break;

            case R.id.submitButton:

                getInputs();
                validateInput();

                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == EditorInfo.IME_ACTION_GO ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            getInputs();
            validateInput();

        }
        return false;
    }

    @FocusChange({R.id.companyName, R.id.companyAddress,R.id.email, R.id.telephone,
            R.id.website, R.id.fax, R.id.returnPolicy})
    public void focusChangedOnUser(View v, boolean hasFocus) {
        if (hasFocus) {
            textError.setVisibility(View.GONE);
        }
    }

    @UiThread
    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), AppConstant.PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            //Getting the Bitmap from Gallery
            try {
               Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
            } catch (IOException e) {
                if (LogFlag.bLogOn) Log.e(TAG, e.toString());
            }

            if(null != filePath){
                if (LogFlag.bLogOn) Log.d(TAG,"FilePath: "+filePath);
                imagePath = filePath.getPath();
                if (LogFlag.bLogOn) Log.d(TAG,"ImageTaken: "+imagePath);
                Picasso.with(getActivity()).load(imagePath).into(companyImageView);

            }
        }
    }
}
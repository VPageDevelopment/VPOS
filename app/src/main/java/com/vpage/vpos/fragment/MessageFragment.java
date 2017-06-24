package com.vpage.vpos.fragment;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vpage.vpos.R;
import com.vpage.vpos.tools.ActionEditText;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.utils.LogFlag;
import com.vpage.vpos.tools.utils.NetworkUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ViewById;

import butterknife.ButterKnife;

/**
 * Created by admin on 6/24/2017.
 */


@EFragment(R.layout.fragment_message)
public class MessageFragment extends Fragment implements OnNetworkChangeListener, View.OnClickListener, View.OnKeyListener {

    private static final String TAG = MessageFragment.class.getName();

    @ViewById(R.id.userName)
    EditText userName;

    @ViewById(R.id.password)
    EditText password;

    @ViewById(R.id.senderId)
    EditText senderId;

    @ViewById(R.id.message)
    EditText message;

    @ViewById(R.id.textError)
    TextView textError;

    @ViewById(R.id.submitButton)
    Button submitButton;

    @ViewById(R.id.contentLayout)
    LinearLayout linearLayout;

    String userNameInput = "", passwordInput = "",senderIdInput="";

    boolean isNetworkAvailable = false;

    TextWatcher textComments;

    public MessageFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void onInitView() {


        ButterKnife.inject(getActivity());
        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);

        message.setOnKeyListener(this);
        submitButton.setOnClickListener(this);

        // mProgressBar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
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
                    VTools.showAlertDialog(getActivity(),"Character Exceed the Limit");
                }
            }
        };

        new ActionEditText(getActivity());
        message.addTextChangedListener(textComments);
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

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == EditorInfo.IME_ACTION_GO ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {


        }
        return false;
    }

    @FocusChange({R.id.userName, R.id.password,R.id.senderId, R.id.message})
    public void focusChangedOnUser(View v, boolean hasFocus) {
        if (hasFocus) {
            textError.setVisibility(View.GONE);
        }
    }
}

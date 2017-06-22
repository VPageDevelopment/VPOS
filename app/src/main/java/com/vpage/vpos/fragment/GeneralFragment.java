package com.vpage.vpos.fragment;

import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vpage.vpos.R;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.utils.LogFlag;
import com.vpage.vpos.tools.utils.NetworkUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import butterknife.ButterKnife;
import butterknife.InjectView;


@EFragment(R.layout.fragment_information)
public class GeneralFragment extends Fragment implements OnNetworkChangeListener {

    private static final String TAG = GeneralFragment.class.getName();

    @ViewById(R.id.companyName)
    EditText companyName;

    @ViewById(R.id.textError)
    TextView textError;

    @ViewById(R.id.submitButton)
    Button submitButton;


    boolean isNetworkAvailable = false;

    TextWatcher textComments;

    public GeneralFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void onInitView() {


        ButterKnife.inject(getActivity());
        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);

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
}
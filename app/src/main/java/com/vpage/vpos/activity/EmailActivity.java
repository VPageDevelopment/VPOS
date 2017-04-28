package com.vpage.vpos.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;
import com.vpage.vpos.R;
import com.vpage.vpos.tools.ActionEditText;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.utils.LogFlag;
import com.vpage.vpos.tools.utils.NetworkUtil;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

@Fullscreen
@EActivity(R.layout.activity_email)
public class EmailActivity extends AppCompatActivity implements View.OnClickListener,OnNetworkChangeListener {

    private static final String TAG = EmailActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.name)
    EditText name;

    @ViewById(R.id.email)
    EditText email;

    @ViewById(R.id.comments)
    EditText comments;

    @ViewById(R.id.submitButton)
    Button submitButton;

    boolean isNetworkAvailable = false;


    @AfterViews
    public void init() {

        setActionBarSupport();
        new ActionEditText(this);

        submitButton.setOnClickListener(this);

        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        comments.requestFocus();

        comments.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(comments.getWindowToken(), 0);
            }
        },200);
    }


    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Email");

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

    @SuppressLint("ShowToast")
    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.submitButton:

                // for null check of user input
                if (!name.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !comments.getText().toString().isEmpty()) {

                    // for connection check
                    if(isNetworkAvailable){
                        sentMail();

                    } else {
                        Toast.makeText(this, getResources().getString(R.string.connection_check), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(this, getResources().getString(R.string.empty_check), Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }



    void sentMail(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(comments.getText().toString()));
        emailIntent.putExtra(Intent.EXTRA_STREAM, " "); // To do content provided by service url
        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
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

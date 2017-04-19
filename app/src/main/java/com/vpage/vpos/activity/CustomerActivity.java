package com.vpage.vpos.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.vpage.vpos.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_customer)
public class CustomerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CustomerActivity.class.getName();


    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.noCustomerContent)
    LinearLayout noCustomerContentLayout;

    @ViewById(R.id.addCustomerButton)
    Button addCustomerButton;


    @AfterViews
    public void onInitView() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Customers");

        noCustomerContentLayout.setVisibility(View.VISIBLE);
        addCustomerButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

    }
}

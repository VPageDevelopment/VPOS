package com.vpage.vpos.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.GridView;
import com.vpage.vpos.R;
import com.vpage.vpos.adapter.StoreConfigGridImageAdapter;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

/**
 * Created by admin on 6/8/2017.
 */


@Fullscreen
@EActivity(R.layout.activity_storeconfig)
public class StoreConfigActivity extends AppCompatActivity{

    private static final String TAG = StoreConfigActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.gridview)
    GridView gridview;

    Activity activity;

    @AfterViews

    public void init() {

        activity = StoreConfigActivity.this;

        Intent callingIntent=getIntent();

        setActionBarSupport();

        gridview.setAdapter(new StoreConfigGridImageAdapter(activity));

    }


    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Store Config");

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

}



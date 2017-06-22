package com.vpage.vpos.activity;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.vpage.vpos.R;
import com.vpage.vpos.adapter.ViewPagerAdapter;
import com.vpage.vpos.fragment.InformationFragment_;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_samplescroll)
public class SampleScroll extends AppCompatActivity {

    private static final String TAG = UpdateInventoryActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.tabs)
    TabLayout tabLayout;

    @ViewById(R.id.viewPager)
    ViewPager viewPager;

    Activity activity;

    @AfterViews
    public void onInitView() {

        activity = SampleScroll.this;

        setActionBarSupport();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }



    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Update Inventory");

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        String[] moduleNameArray = activity.getResources().getStringArray(R.array.storeConfigModuleName);
        adapter.addFrag(new InformationFragment_(), moduleNameArray[0]);
        adapter.addFrag(new InformationFragment_(), moduleNameArray[1]);
        adapter.addFrag(new InformationFragment_(), moduleNameArray[2]);
        adapter.addFrag(new InformationFragment_(), moduleNameArray[3]);
        adapter.addFrag(new InformationFragment_(), moduleNameArray[4]);
        adapter.addFrag(new InformationFragment_(), moduleNameArray[5]);
        viewPager.setAdapter(adapter);
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

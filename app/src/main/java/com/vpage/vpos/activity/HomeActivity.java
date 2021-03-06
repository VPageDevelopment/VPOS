package com.vpage.vpos.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.vpage.vpos.R;
import com.vpage.vpos.adapter.GridImageAdapter;
import com.vpage.vpos.pojos.SignInRequest;
import com.vpage.vpos.pojos.SignInResponse;
import com.vpage.vpos.tools.VPOSPreferences;
import com.vpage.vpos.tools.VPOSTools;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_home)
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    private static final String TAG = HomeActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;


    @ViewById(R.id.gridView)
    GridView gridView;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;


    int typedArrayImagePosition = -1;
    GridImageAdapter gridImageAdapter;
    TypedArray typedArrayImage;

    SignInRequest signInRequest;

    Activity activity;

    @AfterViews
    public void onInitHome() {

        activity = HomeActivity.this;

        setActionBarSupport();

        Intent callingIntent=getIntent();

        String userData = callingIntent.getStringExtra("ActiveUser");

        signInRequest = VPOSTools.getInstance().getActiveUserData(userData);

        setGridView();

   /*     FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_home);
        TextView textViewName = (TextView)hView.findViewById(R.id.textViewName);
        textViewName.setText(signInRequest.getUsername().toString());
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("VPOS");

    }

    private void setGridView(){

        typedArrayImage = getResources().obtainTypedArray(R.array.moduleImage);


        if (null == VTools.getChosenModuleImage() || VTools.getChosenModuleImage().equals("")) {
            VTools.setChosenModuleImage("customers.png");
        } else {

            for (int i = 0; i < typedArrayImage.length(); i++) {

                if (LogFlag.bLogOn)Log.d(TAG, "typedArrayImage: " + typedArrayImage.getString(i).replace("res/drawable/", ""));
                if (typedArrayImage.getString(i).contains(VTools.getChosenModuleImage())) {
                    typedArrayImagePosition = i;
                }
            }
        }


        gridImageAdapter = new GridImageAdapter(activity, typedArrayImage);
        gridView.setAdapter(gridImageAdapter);
        if (typedArrayImagePosition == -1) {
            gridImageAdapter.setSelectedPosition(0);
        }else {
            gridImageAdapter.setSelectedPosition(typedArrayImagePosition);
        }

        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.action_settings:
                gotoSettingsView();
                break;

            case R.id.action_print:
                gotoPrintView();
                break;

            case android.R.id.home:
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            VPOSPreferences.clearAll();
            VPOSPreferences.save("isLoggedIn", "false");
            gotoLoginView();

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        gridImageAdapter.notifyDataSetChanged();
        gridImageAdapter.setSelectedPosition(position);

        switch (position) {

            case 0:
                gotoCustomerView();
                break;

            case 1:
                gotoItemView();
                break;

            case 2:
                gotoItemKitView();
                break;

            case 3:
                gotoSupplierView();
                break;

            case 6:
                gotoSalesView();
                break;

            case 7:
                gotoEmployeeView();
                break;

            case 8:
                gotoGiftCardView();
                break;

            case 9:
                gotoMessageView();
                break;

            case 10:
                gotoStoreConfigView();
                break;

        }
    }


    private void gotoCustomerView(){
        Intent intent = new Intent(getApplicationContext(), CustomerActivity_.class);
        startActivity(intent);
    }


    private void gotoItemView(){
        Intent intent = new Intent(getApplicationContext(), ItemActivity_.class);
        startActivity(intent);
    }


    private void gotoItemKitView(){
        Intent intent = new Intent(getApplicationContext(), ItemKitActivity_.class);
        startActivity(intent);
    }

    private void gotoSupplierView(){
        Intent intent = new Intent(getApplicationContext(), SupplierActivity_.class);
        startActivity(intent);
    }

    private void gotoEmployeeView(){
        Intent intent = new Intent(getApplicationContext(), EmployeeActivity_.class);
        startActivity(intent);
    }

    private void gotoLoginView(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity_.class);
        startActivity(intent);
    }

    private void gotoGiftCardView(){
        Intent intent = new Intent(getApplicationContext(), GiftCardActivity_.class);
        startActivity(intent);
    }

    private void gotoMessageView(){
        Intent intent = new Intent(getApplicationContext(), MessageActivity_.class);
        startActivity(intent);
    }

    private void gotoSalesView(){
        Intent intent = new Intent(getApplicationContext(), SalesActivity_.class);
        startActivity(intent);
    }

    private void gotoSettingsView(){
        startActivity(SettingsActivity.newInstance(this));
    }

    private void gotoPrintView(){
        Intent intent = new Intent(getApplicationContext(), PrintActivity.class);
        startActivity(intent);
    }

    private void gotoStoreConfigView(){
        Intent intent = new Intent(getApplicationContext(), SampleScroll_.class);
        startActivity(intent);
    }

}

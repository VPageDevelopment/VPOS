package com.vpage.vpos.activity;

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
import com.vpage.vpos.R;
import com.vpage.vpos.adapter.GridImageAdapter;
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


    int typedArrayImagePosition = -1;
    GridImageAdapter gridImageAdapter;
    TypedArray typedArrayImage;

    @AfterViews
    public void onInitHome() {

        setActionBarSupport();

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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


        gridImageAdapter = new GridImageAdapter(HomeActivity.this, typedArrayImage);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            gotoSettingsView();
            return true;
        }

        if (id == android.R.id.home) {
            if (LogFlag.bLogOn) Log.d(TAG, "Back Pressed ");
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
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
                // TO DO
                break;

            case 2:
                // TO DO
                break;

            case 3:
                // TO DO
                break;
        }
    }


    private void gotoCustomerView(){
        Intent intent = new Intent(getApplicationContext(), CustomerActivity_.class);
        startActivity(intent);
    }


    private void gotoLoginView(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity_.class);
        startActivity(intent);
    }


    private void gotoSettingsView(){
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }
}

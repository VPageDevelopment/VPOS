package com.vpage.vpos.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import com.vpage.vpos.R;
import com.vpage.vpos.adapter.GridBarCodeAdapter;
import com.vpage.vpos.adapter.GridImageAdapter;
import com.vpage.vpos.pojos.item.ItemResponse;
import com.vpage.vpos.tools.BarcodeView;
import com.vpage.vpos.tools.VPOSRestTools;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.utils.LogFlag;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_barcodegenerate)
public class BarcodeGenerateActivity extends Activity{

    private static final String TAG = BarcodeGenerateActivity.class.getName();

    @ViewById(R.id.barcodeView)
    LinearLayout linearLayout;

    @ViewById(R.id.gridView)
    GridView gridView;

    GridBarCodeAdapter gridBarCodeAdapter;

    Activity activity;

    ItemResponse itemResponse;

    @AfterViews
    public void onInitView() {

        activity = BarcodeGenerateActivity.this;

        Intent callingIntent=getIntent();

        String ItemResponseString = callingIntent.getStringExtra("ItemResponse");

        itemResponse = VPOSRestTools.getInstance().getItemResponseData(ItemResponseString);

        setGridView();
    }


    private void setGridView(){

        gridBarCodeAdapter = new GridBarCodeAdapter(activity,itemResponse);
        gridView.setAdapter(gridBarCodeAdapter);

    }

}

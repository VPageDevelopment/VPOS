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
import com.vpage.vpos.pojos.ItemKitResponse;
import com.vpage.vpos.pojos.item.ItemResponse;
import com.vpage.vpos.pojos.itemkits.ItemKitsResponse;
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

    ItemKitsResponse itemKitResponse;

    int[] selectedPosition;

    String pageTag;

    @AfterViews
    public void onInitView() {

        activity = BarcodeGenerateActivity.this;

        Intent callingIntent=getIntent();

        pageTag = callingIntent.getStringExtra("PageTag");

        if(pageTag.equals("Item")){

            String ItemResponseString = callingIntent.getStringExtra("ItemResponse");
            selectedPosition = callingIntent.getIntArrayExtra("SelectedPosition");

            itemResponse = VPOSRestTools.getInstance().getItemResponseData(ItemResponseString);

        }else if(pageTag.equals("ItemKit")){

            String ItemKitResponseString = callingIntent.getStringExtra("ItemKitResponse");
            selectedPosition = callingIntent.getIntArrayExtra("SelectedPosition");

            itemKitResponse = VPOSRestTools.getInstance().getItemKitsResponseData(ItemKitResponseString);
        }



        setGridView();
    }


    private void setGridView(){

        if(pageTag.equals("Item")){

            gridBarCodeAdapter = new GridBarCodeAdapter(activity,itemResponse,selectedPosition);
            gridView.setAdapter(gridBarCodeAdapter);

        }else if(pageTag.equals("ItemKit")){

            gridBarCodeAdapter = new GridBarCodeAdapter(activity,itemKitResponse,selectedPosition);
            gridView.setAdapter(gridBarCodeAdapter);
        }



    }

}

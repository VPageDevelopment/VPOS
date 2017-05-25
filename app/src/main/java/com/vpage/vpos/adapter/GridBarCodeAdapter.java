package com.vpage.vpos.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.pojos.item.ItemResponse;
import com.vpage.vpos.pojos.item.Items;
import com.vpage.vpos.pojos.itemkits.ItemKits;
import com.vpage.vpos.pojos.itemkits.ItemKitsResponse;
import com.vpage.vpos.tools.BarcodeView;

public class GridBarCodeAdapter extends BaseAdapter {

    private static final String TAG = GridBarCodeAdapter.class.getName();

    Activity activity;
    private LayoutInflater mInflater;
    ItemResponse itemResponse;

    ItemKitsResponse itemKitResponse;

    int[] selectedPosition;

    Items[] items;

    ItemKits[] itemKits;

    public GridBarCodeAdapter(Activity activity, ItemResponse itemResponse, int[] selectedPosition) {
        this.activity = activity;
        this.itemResponse = itemResponse;
        this.selectedPosition = selectedPosition;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        items = new Items[selectedPosition.length];
        for(int i =0; i< selectedPosition.length;i++){
            items[i] = itemResponse.getItems()[selectedPosition[i]];
        }

    }

    public GridBarCodeAdapter(Activity activity, ItemKitsResponse itemKitResponse, int[] selectedPosition) {
        this.activity = activity;
        this.itemKitResponse = itemKitResponse;
        this.selectedPosition = selectedPosition;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemKits = new ItemKits[selectedPosition.length];
        for(int i =0; i< selectedPosition.length;i++){
            itemKits[i] = itemKitResponse.getItems()[selectedPosition[i]];
        }

    }


    @Override
    public int getCount() {
        int count=0;
        if(null != items){
            count = items.length;
        }else {
            count = itemKits.length;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {


        convertView = mInflater.inflate(R.layout.item_grid_barcode, null);

        TextView categoryName = (TextView) convertView.findViewById(R.id.categoryName);
        LinearLayout linearLayout  = (LinearLayout) convertView.findViewById(R.id.barcodeView);
        TextView retailPrice = (TextView) convertView.findViewById(R.id.retailPrice);

        if(null != items){
            categoryName.setText(items[position].getCategory());
            retailPrice.setText(items[position].getRetail_price());

            linearLayout.setVisibility(View.VISIBLE);
            BarcodeView view = new BarcodeView(activity);
            linearLayout.addView(view);
        }else {
            categoryName.setText(itemKits[position].getItem_kit_id());
        // TODO get the retail price

            linearLayout.setVisibility(View.VISIBLE);
            BarcodeView view = new BarcodeView(activity);
            linearLayout.addView(view);
        }



        return convertView;
    }

}





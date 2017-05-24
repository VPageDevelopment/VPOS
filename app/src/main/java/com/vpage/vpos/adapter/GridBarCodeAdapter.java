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
import com.vpage.vpos.tools.BarcodeView;

public class GridBarCodeAdapter extends BaseAdapter {

    private static final String TAG = GridBarCodeAdapter.class.getName();

    Activity activity;
    private LayoutInflater mInflater;
    ItemResponse itemResponse;


    public GridBarCodeAdapter(Activity activity, ItemResponse itemResponse) {
        this.activity = activity;
        this.itemResponse = itemResponse;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }


    @Override
    public int getCount() {

        return itemResponse.getItems().length;
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


        categoryName.setText(itemResponse.getItems()[position].getCategory());
        retailPrice.setText(itemResponse.getItems()[position].getRetail_price());

        linearLayout.setVisibility(View.VISIBLE);
        BarcodeView view = new BarcodeView(activity);
        linearLayout.addView(view);

        return convertView;
    }

}





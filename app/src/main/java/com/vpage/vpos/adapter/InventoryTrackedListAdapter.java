package com.vpage.vpos.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.vpage.vpos.R;


public class InventoryTrackedListAdapter extends BaseAdapter {

    private static final String TAG = InventoryTrackedListAdapter.class.getName();

    Activity activity;
    private LayoutInflater mInflater;


    public InventoryTrackedListAdapter(Activity activity) {
        this.activity = activity;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        int SIZE = 5;
        return SIZE;
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

        convertView = mInflater.inflate(R.layout.item_list_inventorytrack, null);

        TextView dateText = (TextView) convertView.findViewById(R.id.dateText);
        TextView employeeText = (TextView) convertView.findViewById(R.id.employeeText);
        TextView qtyText = (TextView) convertView.findViewById(R.id.qtyText);
        TextView remarkText = (TextView) convertView.findViewById(R.id.remarkText);


        return convertView;
    }


}
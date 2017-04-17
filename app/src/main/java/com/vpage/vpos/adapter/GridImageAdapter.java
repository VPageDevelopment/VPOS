package com.vpage.vpos.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vpage.vpos.R;
import com.vpage.vpos.tools.VTools;

public class GridImageAdapter extends BaseAdapter {

    private static final String TAG = GridImageAdapter.class.getName();

    Activity activity;
    private LayoutInflater mInflater;
    int selectedPos = -1;
    TypedArray typedArrayImage;
    String[] moduleNameArray;


    public GridImageAdapter(Activity activity, TypedArray typedArrayImage) {
        this.activity = activity;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.typedArrayImage = typedArrayImage;
        moduleNameArray = activity.getResources().getStringArray(R.array.moduleName);

    }

    public void setSelectedPosition(int pos) {
        selectedPos = pos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int SIZE = 0;
        try {
            SIZE = typedArrayImage.length();
        } catch (NullPointerException e) {
            Log.d(TAG, e.toString());
        }
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


        convertView = mInflater.inflate(R.layout.item_grid, null);

        ImageView moduleImage = (ImageView) convertView.findViewById(R.id.moduleImage);
        TextView moduleName = (TextView) convertView.findViewById(R.id.moduleName);
        RelativeLayout gridLayout  = (RelativeLayout) convertView.findViewById(R.id.gridLayout);

        Drawable drawable = typedArrayImage.getDrawable(position);
      //  imageview.setImageDrawable(drawable);
        Picasso.with(activity).load(drawable.toString()).into(moduleImage);

        if(selectedPos == position){
            VTools.setLayoutBackgroud(gridLayout,R.drawable.roundedcorner);
        }
        moduleName.setText(moduleNameArray[position]);

        return convertView;
    }


}




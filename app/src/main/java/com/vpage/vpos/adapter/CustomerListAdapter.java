package com.vpage.vpos.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vpage.vpos.activity.CustomerActivity;

import java.util.Locale;

public class CustomerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Locale[] mLocales;

    public CustomerListAdapter(Locale[] mLocales) {
        this.mLocales = mLocales;
    }

    public CustomerListAdapter(String s) {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView tv = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);

        return new ViewHolder(tv);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      //  holder.mItemViewType.setText(mLocales[position].getDisplayName());
    }



    @Override
    public int getItemCount() {
        return mLocales.length;
    }
    private static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

}

package com.vpage.vpos.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.tools.callBack.CustomerFilterCallBack;
import com.vpage.vpos.tools.utils.LogFlag;

import java.util.ArrayList;
import java.util.List;

public class FieldSpinnerAdapter extends ArrayAdapter<String> {

    private static final String TAG = FieldSpinnerAdapter.class.getName();

    private Activity activity;
    List<String> fieldArrayList = new ArrayList<>();
    List<String> fieldSelectedArrayList = new ArrayList<>();
    List<Boolean> fieldCheckedArrayList = new ArrayList<>();

    CustomerFilterCallBack customerFilterCallBack;

    Boolean onceSelected = false;


    public FieldSpinnerAdapter(Activity activitySpinner, int textViewResourceId, List<String> fieldArrayList,List<String> fieldSelectedArrayList)
    {
        super(activitySpinner, textViewResourceId, fieldArrayList);

        activity = activitySpinner;
        this.fieldArrayList     = fieldArrayList;
        this.fieldSelectedArrayList     = fieldSelectedArrayList;

        if (LogFlag.bLogOn)Log.d(TAG, "fieldArrayList Size: " + fieldArrayList.size());
        if (LogFlag.bLogOn)Log.d(TAG, "fieldSelectedArrayList Size: " + fieldSelectedArrayList.size());

    }


    public void setCustomerFilterCallBack(CustomerFilterCallBack customerFilterCallBack) {
        this.customerFilterCallBack = customerFilterCallBack;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();
        View row=inflater.inflate(R.layout.item_spinner_field, parent, false);

        final CheckBox mCheckBox = (CheckBox) row.findViewById(R.id.itemCheckBox);
        TextView fieldName = (TextView) row.findViewById(R.id.fieldName);

        if(position==0){

            mCheckBox.setVisibility(View.GONE);
            fieldName.setVisibility(View.GONE);
        }
        else
        {
            mCheckBox.setVisibility(View.VISIBLE);
            if(!onceSelected){
                mCheckBox.setChecked(true);
            }else {
                try {
                    if(fieldCheckedArrayList.get(position)){
                        mCheckBox.setChecked(true);
                    }else {
                        mCheckBox.setChecked(false);
                    }
                }catch (IndexOutOfBoundsException e){
                    if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
                }

            }
            fieldName.setText(fieldArrayList.get(position));

            // When check box clicked set selected field
            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onceSelected = true;
                    if(mCheckBox.isChecked()){

                        // selected field while checked
                        fieldSelectedArrayList.add(position,fieldArrayList.get(position));

                    }else {
                        // selected field while unchecked
                        if(fieldSelectedArrayList.contains(fieldArrayList.get(position))){   // removing unchecked fields from array
                            if(fieldSelectedArrayList.size()>1){ // remove unchecked fields only when selected array contains atleast one item
                                fieldSelectedArrayList.remove(fieldArrayList.get(position));   // removing unchecked fields from selected array
                            }else {
                                mCheckBox.setChecked(true);

                            }
                        }
                    }

                    if(fieldSelectedArrayList.contains(fieldArrayList.get(position))){
                        fieldCheckedArrayList.add(true);
                    }else {
                        fieldCheckedArrayList.add(false);
                    }

                    // called only when fieldArrayList and fieldSelectedArrayList varies
                    if(fieldSelectedArrayList.size() != fieldArrayList.size()-1){
                        customerFilterCallBack.onSelectedFilterFields(fieldSelectedArrayList); // call back to customer view
                    }


                }
            });
        }
        return row;
        }

    }

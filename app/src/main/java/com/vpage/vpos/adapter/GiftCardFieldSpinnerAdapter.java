package com.vpage.vpos.adapter;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.vpage.vpos.R;
import com.vpage.vpos.pojos.CustomerSpinnerStatus;
import com.vpage.vpos.pojos.GiftCardSpinnerStatus;
import com.vpage.vpos.tools.VPOSPreferences;
import com.vpage.vpos.tools.callBack.FilterCallBack;
import com.vpage.vpos.tools.utils.AppConstant;
import com.vpage.vpos.tools.utils.LogFlag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GiftCardFieldSpinnerAdapter extends ArrayAdapter<String> {

    private static final String TAG = GiftCardFieldSpinnerAdapter.class.getName();

    private Activity activity;
    List<String> fieldArrayList = new ArrayList<>();

    FilterCallBack filterCallBack;
    JSONArray jsonArray = null;
    GiftCardSpinnerStatus giftCardSpinnerStatus = new GiftCardSpinnerStatus();
    Boolean ID = false,FName = false,LName = false,GC_No= false,GCValue = false;
    String jsonObjectData = null;

    public GiftCardFieldSpinnerAdapter(Activity activitySpinner, int textViewResourceId, List<String> fieldArrayList)
    {
        super(activitySpinner, textViewResourceId, fieldArrayList);

        activity = activitySpinner;
        this.fieldArrayList     = fieldArrayList;
    }


    public void setFilterCallBack(FilterCallBack filterCallBack) {
        this.filterCallBack = filterCallBack;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
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

            jsonObjectData = VPOSPreferences.get(AppConstant.gFilterPreference);


        }
        else {
            mCheckBox.setVisibility(View.VISIBLE);
            if (null == jsonObjectData) {
                mCheckBox.setChecked(true);
            } else {
                if (LogFlag.bLogOn) Log.d(TAG,"jsonObjectData: "+jsonObjectData);
                getJSONData(jsonObjectData, position, mCheckBox);
                jsonArray = makJsonArray(giftCardSpinnerStatus);

                VPOSPreferences.save(AppConstant.gFilterPreference,jsonArray.toString());


            }
            fieldName.setText(fieldArrayList.get(position));

        }

        // When check box clicked set selected field
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSpinnerStatus(position,mCheckBox.isChecked(),mCheckBox);

                jsonArray = makJsonArray(giftCardSpinnerStatus);

                VPOSPreferences.save(AppConstant.gFilterPreference,jsonArray.toString());

                filterCallBack.onFilterStatus(true);

            }
        });

        return row;
    }

    private void setSpinnerStatus(int spinnerPosition, Boolean status,CheckBox mCheckBox){


        if(spinnerPosition == 1){
            if(!giftCardSpinnerStatus.isfNameStatus()&&!giftCardSpinnerStatus.islNameStatus()&&!giftCardSpinnerStatus.isGiftCardNoStatus()&&!giftCardSpinnerStatus.isGiftCardValueStatus()){
                giftCardSpinnerStatus.setIdStatus(true);
                mCheckBox.setChecked(true);
            }else {
                giftCardSpinnerStatus.setIdStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 2){
            if(!giftCardSpinnerStatus.isIdStatus()&&!giftCardSpinnerStatus.islNameStatus()&&!giftCardSpinnerStatus.isGiftCardNoStatus()&&!giftCardSpinnerStatus.isGiftCardValueStatus()){
                giftCardSpinnerStatus.setfNameStatus(true);
                mCheckBox.setChecked(true);
            }else {
                giftCardSpinnerStatus.setfNameStatus(status);
                mCheckBox.setChecked(status);
            }

        }else if(spinnerPosition == 3){
            if(!giftCardSpinnerStatus.isIdStatus()&&!giftCardSpinnerStatus.isfNameStatus()&&!giftCardSpinnerStatus.isGiftCardNoStatus()&&!giftCardSpinnerStatus.isGiftCardValueStatus()){
                giftCardSpinnerStatus.setlNameStatus(true);
                mCheckBox.setChecked(true);
            }else {
                giftCardSpinnerStatus.setlNameStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 4){
            if(!giftCardSpinnerStatus.isIdStatus()&&!giftCardSpinnerStatus.isfNameStatus()&&!giftCardSpinnerStatus.islNameStatus()&&!giftCardSpinnerStatus.isGiftCardValueStatus()){
                giftCardSpinnerStatus.setGiftCardNoStatus(true);
                mCheckBox.setChecked(true);
            }else {
                giftCardSpinnerStatus.setGiftCardNoStatus(status);
                mCheckBox.setChecked(status);
            }

        }else if(spinnerPosition == 5){
            if(!giftCardSpinnerStatus.isIdStatus()&&!giftCardSpinnerStatus.isfNameStatus()&&!giftCardSpinnerStatus.islNameStatus()&&!giftCardSpinnerStatus.isGiftCardNoStatus()){
                giftCardSpinnerStatus.setGiftCardValueStatus(true);
                mCheckBox.setChecked(true);
            }else {
                giftCardSpinnerStatus.setGiftCardValueStatus(status);
                mCheckBox.setChecked(status);
            }
        }

    }


    private JSONArray makJsonArray(GiftCardSpinnerStatus giftCardSpinnerStatusValue) {
        jsonArray = new JSONArray();

        JSONObject obj = new JSONObject();
        try {

            obj.put(AppConstant.TAG_ID, giftCardSpinnerStatusValue.isIdStatus());
            obj.put(AppConstant.TAG_FName, giftCardSpinnerStatusValue.isfNameStatus());
            obj.put(AppConstant.TAG_LName, giftCardSpinnerStatusValue.islNameStatus());
            obj.put(AppConstant.TAG_GC_NO, giftCardSpinnerStatusValue.isGiftCardNoStatus());
            obj.put(AppConstant.TAG_GC_Value, giftCardSpinnerStatusValue.isGiftCardValueStatus());

            jsonArray.put(obj);

        } catch (JSONException e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }

        return jsonArray;
    }


    private void getJSONData(String setting, int spinnerPosition, CheckBox checkBox) {

        try {

            JSONArray jsonArrayData = new JSONArray(setting);
            for (int i = 0; i < jsonArrayData.length(); i++) {
                JSONObject jsonObject = jsonArrayData.getJSONObject(i);
                ID = jsonObject.getBoolean(AppConstant.TAG_ID);
                FName = jsonObject.getBoolean(AppConstant.TAG_FName);
                LName = jsonObject.getBoolean(AppConstant.TAG_LName);
                GC_No = jsonObject.getBoolean(AppConstant.TAG_GC_NO);
                GCValue = jsonObject.getBoolean(AppConstant.TAG_GC_Value);

            }

        } catch (JSONException e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }

        if(spinnerPosition == 1){
            giftCardSpinnerStatus.setIdStatus(ID);
            checkBox.setChecked(ID);
        }else if(spinnerPosition == 2){
            giftCardSpinnerStatus.setfNameStatus(FName);
            checkBox.setChecked(FName);
        }else if(spinnerPosition == 3){
            giftCardSpinnerStatus.setlNameStatus(LName);
            checkBox.setChecked(LName);
        }else if(spinnerPosition == 4){
            giftCardSpinnerStatus.setGiftCardNoStatus(GC_No);
            checkBox.setChecked(GC_No);
        }else if(spinnerPosition == 5){
            giftCardSpinnerStatus.setGiftCardValueStatus(GCValue);
            checkBox.setChecked(GCValue);
        }
    }


}



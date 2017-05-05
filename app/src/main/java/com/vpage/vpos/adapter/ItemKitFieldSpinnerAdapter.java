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
import com.vpage.vpos.pojos.ItemKitSpinnerStatus;
import com.vpage.vpos.tools.VPOSPreferences;
import com.vpage.vpos.tools.callBack.FilterCallBack;
import com.vpage.vpos.tools.utils.AppConstant;
import com.vpage.vpos.tools.utils.LogFlag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ItemKitFieldSpinnerAdapter extends ArrayAdapter<String> {

    private static final String TAG = ItemKitFieldSpinnerAdapter.class.getName();

    private Activity activity;
    List<String> fieldArrayList = new ArrayList<>();

    FilterCallBack filterCallBack;
    JSONArray jsonArray = null;
    ItemKitSpinnerStatus itemKitSpinnerStatus = new ItemKitSpinnerStatus();
    Boolean ID = false, IKName = false, IKDes = false, IKCPrice = false, IKRPrice = false;
    String jsonObjectData = null;

    public ItemKitFieldSpinnerAdapter(Activity activitySpinner, int textViewResourceId, List<String> fieldArrayList)
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
            jsonObjectData = VPOSPreferences.get(AppConstant.iKFilterPreference);
        }
        else {
            mCheckBox.setVisibility(View.VISIBLE);
            if (null == jsonObjectData) {
                mCheckBox.setChecked(true);
            } else {
                if (LogFlag.bLogOn) Log.d(TAG,"jsonObjectData: "+jsonObjectData);
                getJSONData(jsonObjectData, position, mCheckBox);
                jsonArray = makJsonArray(itemKitSpinnerStatus);
                VPOSPreferences.save(AppConstant.iKFilterPreference,jsonArray.toString());
            }
            fieldName.setText(fieldArrayList.get(position));

        }

        // When check box clicked set selected field
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSpinnerStatus(position,mCheckBox.isChecked(),mCheckBox);

                jsonArray = makJsonArray(itemKitSpinnerStatus);
                VPOSPreferences.save(AppConstant.iKFilterPreference,jsonArray.toString());

                filterCallBack.onFilterStatus(true);

            }
        });

        return row;
    }

    private void setSpinnerStatus(int spinnerPosition, Boolean status,CheckBox mCheckBox){


        if(spinnerPosition == 1){
            if(!itemKitSpinnerStatus.isItemKitNameStatus()&&!itemKitSpinnerStatus.isItemKitDesStatus()&&!itemKitSpinnerStatus.isCostPriceStatus()&&!itemKitSpinnerStatus.isRetailPriceStatus()){
                itemKitSpinnerStatus.setIdStatus(true);
                mCheckBox.setChecked(true);
            }else {
                itemKitSpinnerStatus.setIdStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 2){
            if(!itemKitSpinnerStatus.isIdStatus()&&!itemKitSpinnerStatus.isItemKitDesStatus()&&!itemKitSpinnerStatus.isCostPriceStatus()&&!itemKitSpinnerStatus.isRetailPriceStatus()){
                itemKitSpinnerStatus.setItemKitNameStatus(true);
                mCheckBox.setChecked(true);
            }else {
                itemKitSpinnerStatus.setItemKitNameStatus(status);
                mCheckBox.setChecked(status);
            }

        }else if(spinnerPosition == 3){
            if(!itemKitSpinnerStatus.isIdStatus()&&!itemKitSpinnerStatus.isItemKitNameStatus()&&!itemKitSpinnerStatus.isCostPriceStatus()&&!itemKitSpinnerStatus.isRetailPriceStatus()){
                itemKitSpinnerStatus.setItemKitDesStatus(true);
                mCheckBox.setChecked(true);
            }else {
                itemKitSpinnerStatus.setItemKitDesStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 4){
            if(!itemKitSpinnerStatus.isIdStatus()&&!itemKitSpinnerStatus.isItemKitNameStatus()&&!itemKitSpinnerStatus.isItemKitDesStatus()&&!itemKitSpinnerStatus.isRetailPriceStatus()){
                itemKitSpinnerStatus.setCostPriceStatus(true);
                mCheckBox.setChecked(true);
            }else {
                itemKitSpinnerStatus.setCostPriceStatus(status);
                mCheckBox.setChecked(status);
            }

        }else if(spinnerPosition == 5){
            if(!itemKitSpinnerStatus.isIdStatus()&&!itemKitSpinnerStatus.isItemKitNameStatus()&&!itemKitSpinnerStatus.isItemKitDesStatus()&&!itemKitSpinnerStatus.isCostPriceStatus()){
                itemKitSpinnerStatus.setRetailPriceStatus(true);
                mCheckBox.setChecked(true);
            }else {
                itemKitSpinnerStatus.setRetailPriceStatus(status);
                mCheckBox.setChecked(status);
            }
        }

    }


    private JSONArray makJsonArray(ItemKitSpinnerStatus itemKitSpinnerStatusValue) {
        jsonArray = new JSONArray();

        JSONObject obj = new JSONObject();
        try {
            obj.put(AppConstant.TAG_ID, itemKitSpinnerStatusValue.isIdStatus());
            obj.put(AppConstant.TAG_IKName, itemKitSpinnerStatusValue.isItemKitNameStatus());
            obj.put(AppConstant.TAG_IKDes, itemKitSpinnerStatusValue.isItemKitDesStatus());
            obj.put(AppConstant.TAG_CPrice, itemKitSpinnerStatusValue.isCostPriceStatus());
            obj.put(AppConstant.TAG_RPrice, itemKitSpinnerStatusValue.isRetailPriceStatus());

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
                IKName = jsonObject.getBoolean(AppConstant.TAG_IKName);
                IKDes = jsonObject.getBoolean(AppConstant.TAG_IKDes);
                IKCPrice = jsonObject.getBoolean(AppConstant.TAG_CPrice);
                IKRPrice = jsonObject.getBoolean(AppConstant.TAG_RPrice);

            }

        } catch (JSONException e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }

        if(spinnerPosition == 1){
            itemKitSpinnerStatus.setIdStatus(ID);
            checkBox.setChecked(ID);
        }else if(spinnerPosition == 2){
            itemKitSpinnerStatus.setItemKitNameStatus(IKName);
            checkBox.setChecked(IKName);
        }else if(spinnerPosition == 3){
            itemKitSpinnerStatus.setItemKitDesStatus(IKDes);
            checkBox.setChecked(IKDes);
        }else if(spinnerPosition == 4){
            itemKitSpinnerStatus.setCostPriceStatus(IKCPrice);
            checkBox.setChecked(IKCPrice);
        }else if(spinnerPosition == 5){
            itemKitSpinnerStatus.setRetailPriceStatus(IKRPrice);
            checkBox.setChecked(IKRPrice);
        }
    }


}



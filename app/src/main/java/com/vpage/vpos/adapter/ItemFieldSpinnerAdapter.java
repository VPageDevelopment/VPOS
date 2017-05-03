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
import com.vpage.vpos.pojos.ItemSpinnerStatus;
import com.vpage.vpos.tools.VPOSPreferences;
import com.vpage.vpos.tools.callBack.FilterCallBack;
import com.vpage.vpos.tools.utils.AppConstant;
import com.vpage.vpos.tools.utils.LogFlag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ItemFieldSpinnerAdapter extends ArrayAdapter<String> {

    private static final String TAG = ItemFieldSpinnerAdapter.class.getName();

    private Activity activity;
    List<String> fieldArrayList = new ArrayList<>();

    FilterCallBack filterCallBack;
    JSONArray jsonArray = null;
    ItemSpinnerStatus itemSpinnerStatus = new ItemSpinnerStatus();
    Boolean id = false,barCode = false,IName = false,category = false,cName = false,cPrice = false,rPrice = false,
            quantity = false,taxPer = false,avatar = false;
    String jsonObjectData = null;

    public ItemFieldSpinnerAdapter(Activity activitySpinner, int textViewResourceId, List<String> fieldArrayList)
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
            jsonObjectData = VPOSPreferences.get(AppConstant.iFilterPreference);
        }
        else {
            mCheckBox.setVisibility(View.VISIBLE);
            if (null == jsonObjectData) {
                mCheckBox.setChecked(true);
            } else {
                if (LogFlag.bLogOn) Log.d(TAG,"jsonObjectData: "+jsonObjectData);
                getJSONData(jsonObjectData, position, mCheckBox);
                jsonArray = makJsonArray(itemSpinnerStatus);
                VPOSPreferences.save(AppConstant.iFilterPreference,jsonArray.toString());
            }
            fieldName.setText(fieldArrayList.get(position));

        }

        // When check box clicked set selected field
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSpinnerStatus(position,mCheckBox.isChecked(),mCheckBox);

                jsonArray = makJsonArray(itemSpinnerStatus);
                VPOSPreferences.save(AppConstant.iFilterPreference,jsonArray.toString());

                filterCallBack.onFilterStatus(true);

            }
        });

        return row;
    }

    private void setSpinnerStatus(int spinnerPosition, Boolean status,CheckBox mCheckBox){


        if(spinnerPosition == 1){
            if(!itemSpinnerStatus.isBarCodeStatus()&&!itemSpinnerStatus.isItemNameStatus()&&!itemSpinnerStatus.isCategoryStatus()&&
                    !itemSpinnerStatus.isCompanyNameStatus()&& !itemSpinnerStatus.isCostPriceStatus()&& !itemSpinnerStatus.isRetailPriceStatus()&&
            !itemSpinnerStatus.isQuantityStatus()&& !itemSpinnerStatus.isTaxPercentStatus()&& !itemSpinnerStatus.isAvatarStatus()){
                itemSpinnerStatus.setIdStatus(true);
                mCheckBox.setChecked(true);
            }else {
                itemSpinnerStatus.setIdStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 2){
            if(!itemSpinnerStatus.isIdStatus()&&!itemSpinnerStatus.isItemNameStatus()&&!itemSpinnerStatus.isCategoryStatus()&&
                    !itemSpinnerStatus.isCompanyNameStatus()&& !itemSpinnerStatus.isCostPriceStatus()&& !itemSpinnerStatus.isRetailPriceStatus()&&
                    !itemSpinnerStatus.isQuantityStatus()&& !itemSpinnerStatus.isTaxPercentStatus()&& !itemSpinnerStatus.isAvatarStatus()){
                itemSpinnerStatus.setBarCodeStatus(true);
                mCheckBox.setChecked(true);
            }else {
                itemSpinnerStatus.setBarCodeStatus(status);
                mCheckBox.setChecked(status);
            }

        }else if(spinnerPosition == 3){
            if(!itemSpinnerStatus.isIdStatus()&&!itemSpinnerStatus.isBarCodeStatus()&&!itemSpinnerStatus.isCategoryStatus()&&
                    !itemSpinnerStatus.isCompanyNameStatus()&& !itemSpinnerStatus.isCostPriceStatus()&& !itemSpinnerStatus.isRetailPriceStatus()&&
                    !itemSpinnerStatus.isQuantityStatus()&& !itemSpinnerStatus.isTaxPercentStatus()&& !itemSpinnerStatus.isAvatarStatus()){
                itemSpinnerStatus.setItemNameStatus(true);
                mCheckBox.setChecked(true);
            }else {
                itemSpinnerStatus.setItemNameStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 4){
            if(!itemSpinnerStatus.isIdStatus()&&!itemSpinnerStatus.isBarCodeStatus()&&!itemSpinnerStatus.isItemNameStatus()&&
                    !itemSpinnerStatus.isCompanyNameStatus()&& !itemSpinnerStatus.isCostPriceStatus()&& !itemSpinnerStatus.isRetailPriceStatus()&&
                    !itemSpinnerStatus.isQuantityStatus()&& !itemSpinnerStatus.isTaxPercentStatus()&& !itemSpinnerStatus.isAvatarStatus()){
                itemSpinnerStatus.setCategoryStatus(true);
                mCheckBox.setChecked(true);
            }else {
                itemSpinnerStatus.setCategoryStatus(status);
                mCheckBox.setChecked(status);
            }

        }else if(spinnerPosition == 5){
            if(!itemSpinnerStatus.isIdStatus()&&!itemSpinnerStatus.isBarCodeStatus()&&!itemSpinnerStatus.isItemNameStatus()&&
                    !itemSpinnerStatus.isCategoryStatus()&& !itemSpinnerStatus.isCostPriceStatus()&& !itemSpinnerStatus.isRetailPriceStatus()&&
                    !itemSpinnerStatus.isQuantityStatus()&& !itemSpinnerStatus.isTaxPercentStatus()&& !itemSpinnerStatus.isAvatarStatus()){
                itemSpinnerStatus.setCompanyNameStatus(true);
                mCheckBox.setChecked(true);
            }else {
                itemSpinnerStatus.setCompanyNameStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 6){
            if(!itemSpinnerStatus.isIdStatus()&&!itemSpinnerStatus.isBarCodeStatus()&&!itemSpinnerStatus.isItemNameStatus()&&
                    !itemSpinnerStatus.isCategoryStatus()&& !itemSpinnerStatus.isCompanyNameStatus()&& !itemSpinnerStatus.isRetailPriceStatus()&&
                    !itemSpinnerStatus.isQuantityStatus()&& !itemSpinnerStatus.isTaxPercentStatus()&& !itemSpinnerStatus.isAvatarStatus()){
                itemSpinnerStatus.setCostPriceStatus(true);
                mCheckBox.setChecked(true);
            }else {
                itemSpinnerStatus.setCostPriceStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 7){
            if(!itemSpinnerStatus.isIdStatus()&&!itemSpinnerStatus.isBarCodeStatus()&&!itemSpinnerStatus.isItemNameStatus()&&
                    !itemSpinnerStatus.isCategoryStatus()&& !itemSpinnerStatus.isCompanyNameStatus()&& !itemSpinnerStatus.isCostPriceStatus()&&
                    !itemSpinnerStatus.isQuantityStatus()&& !itemSpinnerStatus.isTaxPercentStatus()&& !itemSpinnerStatus.isAvatarStatus()){
                itemSpinnerStatus.setRetailPriceStatus(true);
                mCheckBox.setChecked(true);
            }else {
                itemSpinnerStatus.setRetailPriceStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 8){
            if(!itemSpinnerStatus.isIdStatus()&&!itemSpinnerStatus.isBarCodeStatus()&&!itemSpinnerStatus.isItemNameStatus()&&
                    !itemSpinnerStatus.isCategoryStatus()&& !itemSpinnerStatus.isCompanyNameStatus()&& !itemSpinnerStatus.isCostPriceStatus()&&
                    !itemSpinnerStatus.isRetailPriceStatus()&& !itemSpinnerStatus.isTaxPercentStatus()&& !itemSpinnerStatus.isAvatarStatus()){
                itemSpinnerStatus.setQuantityStatus(true);
                mCheckBox.setChecked(true);
            }else {
                itemSpinnerStatus.setQuantityStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 9){
            if(!itemSpinnerStatus.isIdStatus()&&!itemSpinnerStatus.isBarCodeStatus()&&!itemSpinnerStatus.isItemNameStatus()&&
                    !itemSpinnerStatus.isCategoryStatus()&& !itemSpinnerStatus.isCompanyNameStatus()&& !itemSpinnerStatus.isCostPriceStatus()&&
                    !itemSpinnerStatus.isRetailPriceStatus()&& !itemSpinnerStatus.isQuantityStatus()&& !itemSpinnerStatus.isAvatarStatus()){
                itemSpinnerStatus.setTaxPercentStatus(true);
                mCheckBox.setChecked(true);
            }else {
                itemSpinnerStatus.setTaxPercentStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 10){
            if(!itemSpinnerStatus.isIdStatus()&&!itemSpinnerStatus.isBarCodeStatus()&&!itemSpinnerStatus.isItemNameStatus()&&
                    !itemSpinnerStatus.isCategoryStatus()&& !itemSpinnerStatus.isCompanyNameStatus()&& !itemSpinnerStatus.isCostPriceStatus()&&
                    !itemSpinnerStatus.isRetailPriceStatus()&& !itemSpinnerStatus.isQuantityStatus()&& !itemSpinnerStatus.isTaxPercentStatus()){
                itemSpinnerStatus.setAvatarStatus(true);
                mCheckBox.setChecked(true);
            }else {
                itemSpinnerStatus.setAvatarStatus(status);
                mCheckBox.setChecked(status);
            }
        }






    }


    private JSONArray makJsonArray(ItemSpinnerStatus itemSpinnerStatusValue) {
        jsonArray = new JSONArray();

        JSONObject obj = new JSONObject();
        try {

            obj.put(AppConstant.TAG_ID_Item, itemSpinnerStatusValue.isIdStatus());
            obj.put(AppConstant.TAG_Barcode, itemSpinnerStatusValue.isBarCodeStatus());
            obj.put(AppConstant.TAG_IName, itemSpinnerStatusValue.isItemNameStatus());
            obj.put(AppConstant.TAG_Category, itemSpinnerStatusValue.isCategoryStatus());
            obj.put(AppConstant.TAG_CName, itemSpinnerStatusValue.isCompanyNameStatus());
            obj.put(AppConstant.TAG_CPrice, itemSpinnerStatusValue.isCostPriceStatus());
            obj.put(AppConstant.TAG_RPrice, itemSpinnerStatusValue.isRetailPriceStatus());
            obj.put(AppConstant.TAG_Quantity, itemSpinnerStatusValue.isQuantityStatus());
            obj.put(AppConstant.TAG_TaxPer, itemSpinnerStatusValue.isTaxPercentStatus());
            obj.put(AppConstant.TAG_Avatar, itemSpinnerStatusValue.isAvatarStatus());

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
                id = jsonObject.getBoolean(AppConstant.TAG_ID_Item);
                barCode = jsonObject.getBoolean(AppConstant.TAG_Barcode);
                IName = jsonObject.getBoolean(AppConstant.TAG_IName);
                category = jsonObject.getBoolean(AppConstant.TAG_Category);
                cName = jsonObject.getBoolean(AppConstant.TAG_CName);
                cPrice = jsonObject.getBoolean(AppConstant.TAG_CPrice);
                rPrice = jsonObject.getBoolean(AppConstant.TAG_RPrice);
                quantity = jsonObject.getBoolean(AppConstant.TAG_Quantity);
                taxPer = jsonObject.getBoolean(AppConstant.TAG_TaxPer);
                avatar = jsonObject.getBoolean(AppConstant.TAG_Avatar);

            }

        } catch (JSONException e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }

        if(spinnerPosition == 1){
            itemSpinnerStatus.setIdStatus(id);
            checkBox.setChecked(id);
        }else if(spinnerPosition == 2){
            itemSpinnerStatus.setBarCodeStatus(barCode);
            checkBox.setChecked(barCode);
        }else if(spinnerPosition == 3){
            itemSpinnerStatus.setItemNameStatus(IName);
            checkBox.setChecked(IName);
        }else if(spinnerPosition == 4){
            itemSpinnerStatus.setCategoryStatus(category);
            checkBox.setChecked(category);
        }else if(spinnerPosition == 5){
            itemSpinnerStatus.setCompanyNameStatus(cName);
            checkBox.setChecked(cName);
        }else if(spinnerPosition == 6){
            itemSpinnerStatus.setCostPriceStatus(cPrice);
            checkBox.setChecked(cPrice);
        }else if(spinnerPosition == 7){
            itemSpinnerStatus.setRetailPriceStatus(rPrice);
            checkBox.setChecked(rPrice);
        }else if(spinnerPosition == 8){
            itemSpinnerStatus.setQuantityStatus(quantity);
            checkBox.setChecked(quantity);
        }else if(spinnerPosition == 9){
            itemSpinnerStatus.setTaxPercentStatus(taxPer);
            checkBox.setChecked(taxPer);
        }else if(spinnerPosition == 10){
            itemSpinnerStatus.setAvatarStatus(avatar);
            checkBox.setChecked(avatar);
        }
    }


}



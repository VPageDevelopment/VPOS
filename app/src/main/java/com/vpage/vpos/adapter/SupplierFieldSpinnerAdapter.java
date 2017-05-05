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
import com.vpage.vpos.pojos.SupplierSpinnerStatus;
import com.vpage.vpos.tools.VPOSPreferences;
import com.vpage.vpos.tools.callBack.FilterCallBack;
import com.vpage.vpos.tools.utils.AppConstant;
import com.vpage.vpos.tools.utils.LogFlag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SupplierFieldSpinnerAdapter extends ArrayAdapter<String> {

    private static final String TAG = SupplierFieldSpinnerAdapter.class.getName();

    private Activity activity;
    List<String> fieldArrayList = new ArrayList<>();

    FilterCallBack filterCallBack;
    JSONArray jsonArray = null;
    SupplierSpinnerStatus supplierSpinnerStatus = new SupplierSpinnerStatus();
    Boolean ID = false,CName = false,AName = false,FName = false,LName = false,Email = false,PhoneNumber = false;
    String jsonObjectData = null;

    public SupplierFieldSpinnerAdapter(Activity activitySpinner, int textViewResourceId, List<String> fieldArrayList)
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
            jsonObjectData = VPOSPreferences.get(AppConstant.sFilterPreference);
        }
        else {
            mCheckBox.setVisibility(View.VISIBLE);
            if (null == jsonObjectData) {
                mCheckBox.setChecked(true);
            } else {
                if (LogFlag.bLogOn) Log.d(TAG,"jsonObjectData: "+jsonObjectData);
                getJSONData(jsonObjectData, position, mCheckBox);
                jsonArray = makJsonArray(supplierSpinnerStatus);
                VPOSPreferences.save(AppConstant.sFilterPreference,jsonArray.toString());
            }
            fieldName.setText(fieldArrayList.get(position));

        }

        // When check box clicked set selected field
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSpinnerStatus(position,mCheckBox.isChecked(),mCheckBox);

                jsonArray = makJsonArray(supplierSpinnerStatus);
                VPOSPreferences.save(AppConstant.sFilterPreference,jsonArray.toString());

                filterCallBack.onFilterStatus(true);

            }
        });

        return row;
    }

    private void setSpinnerStatus(int spinnerPosition, Boolean status,CheckBox mCheckBox){


        if(spinnerPosition == 1){
            if(!supplierSpinnerStatus.iscNameStatus()&&!supplierSpinnerStatus.isaNameStatus()&&!supplierSpinnerStatus.isfNameStatus()&&
                    !supplierSpinnerStatus.islNameStatus()&&!supplierSpinnerStatus.isEmailStatus()&&!supplierSpinnerStatus.isPhoneNoStatus()){
                supplierSpinnerStatus.setIdStatus(true);
                mCheckBox.setChecked(true);
            }else {
                supplierSpinnerStatus.setIdStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 2){
            if(!supplierSpinnerStatus.isIdStatus()&&!supplierSpinnerStatus.isaNameStatus()&&!supplierSpinnerStatus.isfNameStatus()&&
                    !supplierSpinnerStatus.islNameStatus()&&!supplierSpinnerStatus.isEmailStatus()&&!supplierSpinnerStatus.isPhoneNoStatus()){
                supplierSpinnerStatus.setcNameStatus(true);
                mCheckBox.setChecked(true);
            }else {
                supplierSpinnerStatus.setcNameStatus(status);
                mCheckBox.setChecked(status);
            }

        }else if(spinnerPosition == 3){
            if(!supplierSpinnerStatus.isIdStatus()&&!supplierSpinnerStatus.iscNameStatus()&&!supplierSpinnerStatus.isfNameStatus()&&
                    !supplierSpinnerStatus.islNameStatus()&&!supplierSpinnerStatus.isEmailStatus()&&!supplierSpinnerStatus.isPhoneNoStatus()){
                supplierSpinnerStatus.setaNameStatus(true);
                mCheckBox.setChecked(true);
            }else {
                supplierSpinnerStatus.setaNameStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 4){
            if(!supplierSpinnerStatus.isIdStatus()&&!supplierSpinnerStatus.iscNameStatus()&&!supplierSpinnerStatus.isaNameStatus()&&
                    !supplierSpinnerStatus.islNameStatus()&&!supplierSpinnerStatus.isEmailStatus()&&!supplierSpinnerStatus.isPhoneNoStatus()){
                supplierSpinnerStatus.setfNameStatus(true);
                mCheckBox.setChecked(true);
            }else {
                supplierSpinnerStatus.setfNameStatus(status);
                mCheckBox.setChecked(status);
            }

        }else if(spinnerPosition == 5){
            if(!supplierSpinnerStatus.isIdStatus()&&!supplierSpinnerStatus.iscNameStatus()&&!supplierSpinnerStatus.isaNameStatus()&&
                    !supplierSpinnerStatus.isfNameStatus()&&!supplierSpinnerStatus.isEmailStatus()&&!supplierSpinnerStatus.isPhoneNoStatus()){
                supplierSpinnerStatus.setlNameStatus(true);
                mCheckBox.setChecked(true);
            }else {
                supplierSpinnerStatus.setlNameStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 6){
            if(!supplierSpinnerStatus.isIdStatus()&&!supplierSpinnerStatus.iscNameStatus()&&!supplierSpinnerStatus.isaNameStatus()&&
                    !supplierSpinnerStatus.isfNameStatus()&&!supplierSpinnerStatus.islNameStatus()&&!supplierSpinnerStatus.isPhoneNoStatus()){
                supplierSpinnerStatus.setEmailStatus(true);
                mCheckBox.setChecked(true);
            }else {
                supplierSpinnerStatus.setEmailStatus(status);
                mCheckBox.setChecked(status);
            }

        }else if(spinnerPosition == 7){
            if(!supplierSpinnerStatus.isIdStatus()&&!supplierSpinnerStatus.iscNameStatus()&&!supplierSpinnerStatus.isaNameStatus()&&
                    !supplierSpinnerStatus.isfNameStatus()&&!supplierSpinnerStatus.islNameStatus()&&!supplierSpinnerStatus.isEmailStatus()){
                supplierSpinnerStatus.setPhoneNoStatus(true);
                mCheckBox.setChecked(true);
            }else {
                supplierSpinnerStatus.setPhoneNoStatus(status);
                mCheckBox.setChecked(status);
            }
        }

    }


    private JSONArray makJsonArray(SupplierSpinnerStatus supplierSpinnerStatusValue) {
        jsonArray = new JSONArray();

        JSONObject obj = new JSONObject();
        try {

            obj.put(AppConstant.TAG_ID, supplierSpinnerStatusValue.isIdStatus());
            obj.put(AppConstant.TAG_CName, supplierSpinnerStatusValue.iscNameStatus());
            obj.put(AppConstant.TAG_AName, supplierSpinnerStatusValue.isaNameStatus());
            obj.put(AppConstant.TAG_FName, supplierSpinnerStatusValue.isfNameStatus());
            obj.put(AppConstant.TAG_LName, supplierSpinnerStatusValue.islNameStatus());
            obj.put(AppConstant.TAG_Email, supplierSpinnerStatusValue.isEmailStatus());
            obj.put(AppConstant.TAG_PhoneNumber, supplierSpinnerStatusValue.isPhoneNoStatus());

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
                CName = jsonObject.getBoolean(AppConstant.TAG_CName);
                AName = jsonObject.getBoolean(AppConstant.TAG_AName);
                FName = jsonObject.getBoolean(AppConstant.TAG_FName);
                LName = jsonObject.getBoolean(AppConstant.TAG_LName);
                Email = jsonObject.getBoolean(AppConstant.TAG_Email);
                PhoneNumber = jsonObject.getBoolean(AppConstant.TAG_PhoneNumber);

            }

        } catch (JSONException e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }

        if(spinnerPosition == 1){
            supplierSpinnerStatus.setIdStatus(ID);
            checkBox.setChecked(ID);
        }else if(spinnerPosition == 2){
            supplierSpinnerStatus.setcNameStatus(CName);
            checkBox.setChecked(CName);
        }else if(spinnerPosition == 3){
            supplierSpinnerStatus.setaNameStatus(AName);
            checkBox.setChecked(AName);
        }else if(spinnerPosition == 4){
            supplierSpinnerStatus.setfNameStatus(FName);
            checkBox.setChecked(FName);
        }else if(spinnerPosition == 5){
            supplierSpinnerStatus.setlNameStatus(LName);
            checkBox.setChecked(LName);
        }else if(spinnerPosition == 6){
            supplierSpinnerStatus.setEmailStatus(Email);
            checkBox.setChecked(Email);
        }else if(spinnerPosition == 7){
            supplierSpinnerStatus.setPhoneNoStatus(PhoneNumber);
            checkBox.setChecked(PhoneNumber);
        }
    }


}



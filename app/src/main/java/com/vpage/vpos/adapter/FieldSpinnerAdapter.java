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
import com.vpage.vpos.pojos.SpinnerStatus;
import com.vpage.vpos.tools.VPOSPreferences;
import com.vpage.vpos.tools.callBack.CustomerFilterCallBack;
import com.vpage.vpos.tools.utils.AppConstant;
import com.vpage.vpos.tools.utils.LogFlag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class FieldSpinnerAdapter extends ArrayAdapter<String> {

    private static final String TAG = FieldSpinnerAdapter.class.getName();

    private Activity activity;
    List<String> fieldArrayList = new ArrayList<>();

    CustomerFilterCallBack customerFilterCallBack;
    JSONArray jsonArray = null;
    SpinnerStatus  spinnerStatus = new SpinnerStatus();
    Boolean ID = false,FName = false,LName = false,Email = false,PhoneNumber = false;
    String jsonObjectData = null;

    public FieldSpinnerAdapter(Activity activitySpinner, int textViewResourceId, List<String> fieldArrayList)
    {
        super(activitySpinner, textViewResourceId, fieldArrayList);

        activity = activitySpinner;
        this.fieldArrayList     = fieldArrayList;

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
            jsonObjectData = VPOSPreferences.get(AppConstant.cFilterPreference);
        }
        else {
            mCheckBox.setVisibility(View.VISIBLE);
            if (null == jsonObjectData) {
                mCheckBox.setChecked(true);
            } else {
                if (LogFlag.bLogOn) Log.d(TAG,"jsonObjectData: "+jsonObjectData);
                getJSONData(jsonObjectData, position, mCheckBox);
                jsonArray = makJsonArray(spinnerStatus);
                VPOSPreferences.save(AppConstant.cFilterPreference,jsonArray.toString());
            }
            fieldName.setText(fieldArrayList.get(position));

        }

            // When check box clicked set selected field
            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setSpinnerStatus(position,mCheckBox.isChecked(),mCheckBox);

                    jsonArray = makJsonArray(spinnerStatus);
                    VPOSPreferences.save(AppConstant.cFilterPreference,jsonArray.toString());

                    customerFilterCallBack.onFilterStatus(true);

                }
            });

        return row;
    }

    private void setSpinnerStatus(int spinnerPosition, Boolean status,CheckBox mCheckBox){


        if(spinnerPosition == 1){
            if(!spinnerStatus.isfNameStatus()&&!spinnerStatus.islNameStatus()&&!spinnerStatus.isEmailStatus()&&!spinnerStatus.isPhoneNoStatus()){
                spinnerStatus.setIdStatus(true);
                mCheckBox.setChecked(true);
            }else {
                spinnerStatus.setIdStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 2){
            if(!spinnerStatus.isIdStatus()&&!spinnerStatus.islNameStatus()&&!spinnerStatus.isEmailStatus()&&!spinnerStatus.isPhoneNoStatus()){
                spinnerStatus.setfNameStatus(true);
                mCheckBox.setChecked(true);
            }else {
                spinnerStatus.setfNameStatus(status);
                mCheckBox.setChecked(status);
            }

        }else if(spinnerPosition == 3){
            if(!spinnerStatus.isIdStatus()&&!spinnerStatus.isfNameStatus()&&!spinnerStatus.isEmailStatus()&&!spinnerStatus.isPhoneNoStatus()){
                spinnerStatus.setlNameStatus(true);
                mCheckBox.setChecked(true);
            }else {
                spinnerStatus.setlNameStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 4){
            if(!spinnerStatus.isIdStatus()&&!spinnerStatus.isfNameStatus()&&!spinnerStatus.islNameStatus()&&!spinnerStatus.isPhoneNoStatus()){
                spinnerStatus.setEmailStatus(true);
                mCheckBox.setChecked(true);
            }else {
                spinnerStatus.setEmailStatus(status);
                mCheckBox.setChecked(status);
            }

        }else if(spinnerPosition == 5){
            if(!spinnerStatus.isIdStatus()&&!spinnerStatus.isfNameStatus()&&!spinnerStatus.islNameStatus()&&!spinnerStatus.isEmailStatus()){
                spinnerStatus.setPhoneNoStatus(true);
                mCheckBox.setChecked(true);
            }else {
                spinnerStatus.setPhoneNoStatus(status);
                mCheckBox.setChecked(status);
            }
        }

    }


     private JSONArray makJsonArray(SpinnerStatus spinnerStatusValue) {
         jsonArray = new JSONArray();

        JSONObject obj = new JSONObject();
        try {

            obj.put(AppConstant.TAG_ID, spinnerStatusValue.isIdStatus());
            obj.put(AppConstant.TAG_FName, spinnerStatusValue.isfNameStatus());
            obj.put(AppConstant.TAG_LName, spinnerStatusValue.islNameStatus());
            obj.put(AppConstant.TAG_Email, spinnerStatusValue.isEmailStatus());
            obj.put(AppConstant.TAG_PhoneNumber, spinnerStatusValue.isPhoneNoStatus());

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
                Email = jsonObject.getBoolean(AppConstant.TAG_Email);
                PhoneNumber = jsonObject.getBoolean(AppConstant.TAG_PhoneNumber);

            }

        } catch (JSONException e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }

        if(spinnerPosition == 1){
            spinnerStatus.setIdStatus(ID);
            checkBox.setChecked(ID);
        }else if(spinnerPosition == 2){
            spinnerStatus.setfNameStatus(FName);
            checkBox.setChecked(FName);
        }else if(spinnerPosition == 3){
            spinnerStatus.setlNameStatus(LName);
            checkBox.setChecked(LName);
        }else if(spinnerPosition == 4){
            spinnerStatus.setEmailStatus(Email);
            checkBox.setChecked(Email);
        }else if(spinnerPosition == 5){
            spinnerStatus.setPhoneNoStatus(PhoneNumber);
            checkBox.setChecked(PhoneNumber);
        }
    }


}



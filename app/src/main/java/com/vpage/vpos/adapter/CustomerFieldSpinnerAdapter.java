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
import com.vpage.vpos.tools.VPOSPreferences;
import com.vpage.vpos.tools.callBack.FilterCallBack;
import com.vpage.vpos.tools.utils.AppConstant;
import com.vpage.vpos.tools.utils.LogFlag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CustomerFieldSpinnerAdapter extends ArrayAdapter<String> {

    private static final String TAG = CustomerFieldSpinnerAdapter.class.getName();

    private Activity activity;
    List<String> fieldArrayList = new ArrayList<>();

    FilterCallBack filterCallBack;
    JSONArray jsonArray = null;
    CustomerSpinnerStatus customerSpinnerStatus = new CustomerSpinnerStatus();
    Boolean ID = false,FName = false,LName = false,Email = false,PhoneNumber = false;
    String jsonObjectData = null;

    public CustomerFieldSpinnerAdapter(Activity activitySpinner, int textViewResourceId, List<String> fieldArrayList)
    {
        super(activitySpinner, textViewResourceId, fieldArrayList);

        activity = activitySpinner;
        this.fieldArrayList     = fieldArrayList;

    }


    public void setFilterCallBack(FilterCallBack filterCallBack) {
        this.filterCallBack = filterCallBack;
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
                jsonArray = makJsonArray(customerSpinnerStatus);
                VPOSPreferences.save(AppConstant.cFilterPreference,jsonArray.toString());
            }
            fieldName.setText(fieldArrayList.get(position));

        }

            // When check box clicked set selected field
            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setSpinnerStatus(position,mCheckBox.isChecked(),mCheckBox);

                    jsonArray = makJsonArray(customerSpinnerStatus);
                    VPOSPreferences.save(AppConstant.cFilterPreference,jsonArray.toString());

                    filterCallBack.onFilterStatus(true);

                }
            });

        return row;
    }

    private void setSpinnerStatus(int spinnerPosition, Boolean status,CheckBox mCheckBox){


        if(spinnerPosition == 1){
            if(!customerSpinnerStatus.isfNameStatus()&&!customerSpinnerStatus.islNameStatus()&&!customerSpinnerStatus.isEmailStatus()&&!customerSpinnerStatus.isPhoneNoStatus()){
                customerSpinnerStatus.setIdStatus(true);
                mCheckBox.setChecked(true);
            }else {
                customerSpinnerStatus.setIdStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 2){
            if(!customerSpinnerStatus.isIdStatus()&&!customerSpinnerStatus.islNameStatus()&&!customerSpinnerStatus.isEmailStatus()&&!customerSpinnerStatus.isPhoneNoStatus()){
                customerSpinnerStatus.setfNameStatus(true);
                mCheckBox.setChecked(true);
            }else {
                customerSpinnerStatus.setfNameStatus(status);
                mCheckBox.setChecked(status);
            }

        }else if(spinnerPosition == 3){
            if(!customerSpinnerStatus.isIdStatus()&&!customerSpinnerStatus.isfNameStatus()&&!customerSpinnerStatus.isEmailStatus()&&!customerSpinnerStatus.isPhoneNoStatus()){
                customerSpinnerStatus.setlNameStatus(true);
                mCheckBox.setChecked(true);
            }else {
                customerSpinnerStatus.setlNameStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 4){
            if(!customerSpinnerStatus.isIdStatus()&&!customerSpinnerStatus.isfNameStatus()&&!customerSpinnerStatus.islNameStatus()&&!customerSpinnerStatus.isPhoneNoStatus()){
                customerSpinnerStatus.setEmailStatus(true);
                mCheckBox.setChecked(true);
            }else {
                customerSpinnerStatus.setEmailStatus(status);
                mCheckBox.setChecked(status);
            }

        }else if(spinnerPosition == 5){
            if(!customerSpinnerStatus.isIdStatus()&&!customerSpinnerStatus.isfNameStatus()&&!customerSpinnerStatus.islNameStatus()&&!customerSpinnerStatus.isEmailStatus()){
                customerSpinnerStatus.setPhoneNoStatus(true);
                mCheckBox.setChecked(true);
            }else {
                customerSpinnerStatus.setPhoneNoStatus(status);
                mCheckBox.setChecked(status);
            }
        }

    }


     private JSONArray makJsonArray(CustomerSpinnerStatus customerSpinnerStatusValue) {
         jsonArray = new JSONArray();

        JSONObject obj = new JSONObject();
        try {

            obj.put(AppConstant.TAG_ID, customerSpinnerStatusValue.isIdStatus());
            obj.put(AppConstant.TAG_FName, customerSpinnerStatusValue.isfNameStatus());
            obj.put(AppConstant.TAG_LName, customerSpinnerStatusValue.islNameStatus());
            obj.put(AppConstant.TAG_Email, customerSpinnerStatusValue.isEmailStatus());
            obj.put(AppConstant.TAG_PhoneNumber, customerSpinnerStatusValue.isPhoneNoStatus());

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
            customerSpinnerStatus.setIdStatus(ID);
            checkBox.setChecked(ID);
        }else if(spinnerPosition == 2){
            customerSpinnerStatus.setfNameStatus(FName);
            checkBox.setChecked(FName);
        }else if(spinnerPosition == 3){
            customerSpinnerStatus.setlNameStatus(LName);
            checkBox.setChecked(LName);
        }else if(spinnerPosition == 4){
            customerSpinnerStatus.setEmailStatus(Email);
            checkBox.setChecked(Email);
        }else if(spinnerPosition == 5){
            customerSpinnerStatus.setPhoneNoStatus(PhoneNumber);
            checkBox.setChecked(PhoneNumber);
        }
    }


}



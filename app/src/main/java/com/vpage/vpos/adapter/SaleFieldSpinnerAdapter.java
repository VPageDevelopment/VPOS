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
import com.vpage.vpos.pojos.SaleSpinnerStatus;
import com.vpage.vpos.tools.VPOSPreferences;
import com.vpage.vpos.tools.callBack.FilterCallBack;
import com.vpage.vpos.tools.utils.AppConstant;
import com.vpage.vpos.tools.utils.LogFlag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SaleFieldSpinnerAdapter extends ArrayAdapter<String> {

    private static final String TAG = SaleFieldSpinnerAdapter.class.getName();

    private Activity activity;
    List<String> fieldArrayList = new ArrayList<>();

    FilterCallBack filterCallBack;
    JSONArray jsonArray = null;
    SaleSpinnerStatus saleSpinnerStatus = new SaleSpinnerStatus();
    Boolean ID = false, time = false, customer = false, amountDue = false, amountTendered = false,
            changeDue = false,type = false,invoice = false;
    String jsonObjectData = null;

    public SaleFieldSpinnerAdapter(Activity activitySpinner, int textViewResourceId, List<String> fieldArrayList)
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
            jsonObjectData = VPOSPreferences.get(AppConstant.saFilterPreference);
        }
        else {
            mCheckBox.setVisibility(View.VISIBLE);
            if (null == jsonObjectData) {
                mCheckBox.setChecked(true);
            } else {
                if (LogFlag.bLogOn) Log.d(TAG,"jsonObjectData: "+jsonObjectData);
                getJSONData(jsonObjectData, position, mCheckBox);
                jsonArray = makJsonArray(saleSpinnerStatus);
                VPOSPreferences.save(AppConstant.saFilterPreference,jsonArray.toString());
            }
            fieldName.setText(fieldArrayList.get(position));

        }

        // When check box clicked set selected field
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSpinnerStatus(position,mCheckBox.isChecked(),mCheckBox);

                jsonArray = makJsonArray(saleSpinnerStatus);
                VPOSPreferences.save(AppConstant.saFilterPreference,jsonArray.toString());

                filterCallBack.onFilterStatus(true);

            }
        });

        return row;
    }

    private void setSpinnerStatus(int spinnerPosition, Boolean status,CheckBox mCheckBox){


        if(spinnerPosition == 1){
            if(!saleSpinnerStatus.isTimeStatus()&&! saleSpinnerStatus.isCustomerStatus()&&!saleSpinnerStatus.isAmountDueStatus()&&
                    !saleSpinnerStatus.isAmountTendStatus()&&!saleSpinnerStatus.isChangeDueStatus()&&!saleSpinnerStatus.isTypeStatus()&&
                    !saleSpinnerStatus.isInvoiceStatus()){
                saleSpinnerStatus.setIdStatus(true);
                mCheckBox.setChecked(true);
            }else {
                saleSpinnerStatus.setIdStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 2){
            if(!saleSpinnerStatus.isIdStatus()&&!saleSpinnerStatus.isCustomerStatus()&&!saleSpinnerStatus.isAmountDueStatus()&&
                    !saleSpinnerStatus.isAmountTendStatus()&&!saleSpinnerStatus.isChangeDueStatus()&&!saleSpinnerStatus.isTypeStatus()&&
                    !saleSpinnerStatus.isInvoiceStatus()){
                saleSpinnerStatus.setTimeStatus(true);
                mCheckBox.setChecked(true);
            }else {
                saleSpinnerStatus.setTimeStatus(status);
                mCheckBox.setChecked(status);
            }

        }else if(spinnerPosition == 3){
            if(!saleSpinnerStatus.isIdStatus()&&!saleSpinnerStatus.isTimeStatus()&&!saleSpinnerStatus.isAmountDueStatus()&&
                    !saleSpinnerStatus.isAmountTendStatus()&&!saleSpinnerStatus.isChangeDueStatus()&&!saleSpinnerStatus.isTypeStatus()&&
                    !saleSpinnerStatus.isInvoiceStatus()){
                saleSpinnerStatus.setCustomerStatus(true);
                mCheckBox.setChecked(true);
            }else {
                saleSpinnerStatus.setCustomerStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 4){
            if(!saleSpinnerStatus.isIdStatus()&&!saleSpinnerStatus.isTimeStatus()&&! saleSpinnerStatus.isCustomerStatus()&&
                    !saleSpinnerStatus.isAmountTendStatus()&&!saleSpinnerStatus.isChangeDueStatus()&&!saleSpinnerStatus.isTypeStatus()&&
                    !saleSpinnerStatus.isInvoiceStatus()){
                saleSpinnerStatus.setAmountDueStatus(true);
                mCheckBox.setChecked(true);
            }else {
                saleSpinnerStatus.setAmountDueStatus(status);
                mCheckBox.setChecked(status);
            }

        }else if(spinnerPosition == 5){
            if(!saleSpinnerStatus.isIdStatus()&&!saleSpinnerStatus.isTimeStatus()&&! saleSpinnerStatus.isCustomerStatus()&&
                    !saleSpinnerStatus.isAmountDueStatus()&&!saleSpinnerStatus.isChangeDueStatus()&&!saleSpinnerStatus.isTypeStatus()&&
                    !saleSpinnerStatus.isInvoiceStatus()){
                saleSpinnerStatus.setAmountTendStatus(true);
                mCheckBox.setChecked(true);
            }else {
                saleSpinnerStatus.setAmountTendStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 6){
            if(!saleSpinnerStatus.isIdStatus()&&!saleSpinnerStatus.isIdStatus()&&!saleSpinnerStatus.isCustomerStatus()&&!saleSpinnerStatus.isAmountDueStatus()&&
                    !saleSpinnerStatus.isAmountTendStatus()&&!saleSpinnerStatus.isTypeStatus()&&
                    !saleSpinnerStatus.isInvoiceStatus()){
                saleSpinnerStatus.setChangeDueStatus(true);
                mCheckBox.setChecked(true);
            }else {
                saleSpinnerStatus.setChangeDueStatus(status);
                mCheckBox.setChecked(status);
            }
        }else if(spinnerPosition == 7){
            if(!saleSpinnerStatus.isIdStatus()&&!saleSpinnerStatus.isTimeStatus()&&! saleSpinnerStatus.isCustomerStatus()&&!saleSpinnerStatus.isAmountDueStatus()&&
                    !saleSpinnerStatus.isAmountTendStatus()&&!saleSpinnerStatus.isChangeDueStatus()&&
                    !saleSpinnerStatus.isInvoiceStatus()){
                saleSpinnerStatus.setTypeStatus(true);
                mCheckBox.setChecked(true);
            }else {
                saleSpinnerStatus.setTypeStatus(status);
                mCheckBox.setChecked(status);
            }
        }
        else if(spinnerPosition == 8){
            if(!saleSpinnerStatus.isIdStatus()&&!saleSpinnerStatus.isTimeStatus()&&! saleSpinnerStatus.isCustomerStatus()&&!saleSpinnerStatus.isAmountDueStatus()&&
                    !saleSpinnerStatus.isAmountTendStatus()&&!saleSpinnerStatus.isChangeDueStatus()&&
                    !saleSpinnerStatus.isTypeStatus()){
                saleSpinnerStatus.setInvoiceStatus(true);
                mCheckBox.setChecked(true);
            }else {
                saleSpinnerStatus.setInvoiceStatus(status);
                mCheckBox.setChecked(status);
            }
        }

    }


    private JSONArray makJsonArray(SaleSpinnerStatus saleSpinnerStatusValue) {
        jsonArray = new JSONArray();

        JSONObject obj = new JSONObject();
        try {
            obj.put(AppConstant.TAG_ID, saleSpinnerStatusValue.isIdStatus());
            obj.put(AppConstant.TAG_Time, saleSpinnerStatusValue.isTimeStatus());
            obj.put(AppConstant.TAG_Customer, saleSpinnerStatusValue.isCustomerStatus());
            obj.put(AppConstant.TAG_Amount_due, saleSpinnerStatusValue.isAmountDueStatus());
            obj.put(AppConstant.TAG_Amount_ten, saleSpinnerStatusValue.isAmountTendStatus());
            obj.put(AppConstant.TAG_Change_due, saleSpinnerStatusValue.isChangeDueStatus());
            obj.put(AppConstant.TAG_Type, saleSpinnerStatusValue.isTypeStatus());
            obj.put(AppConstant.TAG_Invoice, saleSpinnerStatusValue.isInvoiceStatus());

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
                time = jsonObject.getBoolean(AppConstant.TAG_Time);
                customer = jsonObject.getBoolean(AppConstant.TAG_Customer);
                amountDue = jsonObject.getBoolean(AppConstant.TAG_Amount_due);
                amountTendered = jsonObject.getBoolean(AppConstant.TAG_Amount_ten);
                changeDue = jsonObject.getBoolean(AppConstant.TAG_Change_due);
                type = jsonObject.getBoolean(AppConstant.TAG_Type);
                invoice = jsonObject.getBoolean(AppConstant.TAG_Invoice);

            }

        } catch (JSONException e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }

        if(spinnerPosition == 1){
            saleSpinnerStatus.setIdStatus(ID);
            checkBox.setChecked(ID);
        }else if(spinnerPosition == 2){
            saleSpinnerStatus.setTimeStatus(time);
            checkBox.setChecked(time);
        }else if(spinnerPosition == 3){
            saleSpinnerStatus.setCustomerStatus(customer);
            checkBox.setChecked(customer);
        }else if(spinnerPosition == 4){
            saleSpinnerStatus.setAmountDueStatus(amountDue);
            checkBox.setChecked(amountDue);
        }else if(spinnerPosition == 5){
            saleSpinnerStatus.setAmountTendStatus(amountTendered);
            checkBox.setChecked(amountTendered);
        }else if(spinnerPosition == 6){
            saleSpinnerStatus.setChangeDueStatus(changeDue);
            checkBox.setChecked(changeDue);
        }else if(spinnerPosition == 7){
            saleSpinnerStatus.setTypeStatus(type);
            checkBox.setChecked(type);
        }else if(spinnerPosition == 8){
            saleSpinnerStatus.setInvoiceStatus(invoice);
            checkBox.setChecked(invoice);
        }
    }


}




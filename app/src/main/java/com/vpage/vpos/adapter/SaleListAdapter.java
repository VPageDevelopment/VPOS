package com.vpage.vpos.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.pojos.sale.SaleResponse;
import com.vpage.vpos.pojos.sale.Sales;
import com.vpage.vpos.tools.VPOSPreferences;
import com.vpage.vpos.tools.callBack.CheckedCallBack;
import com.vpage.vpos.tools.utils.AppConstant;
import com.vpage.vpos.tools.utils.LogFlag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SaleListAdapter extends RecyclerView.Adapter<SaleListAdapter.ViewHolder> {

    private static final String TAG = SaleListAdapter.class.getName();

    private SparseBooleanArray mChecked = new SparseBooleanArray();

    private CheckedCallBack checkedCallBack;

    private Activity activity;

    private static int count = 0;

    private CheckBox checkBox_header;

    private List<Boolean> checkedPositionArrayList = new ArrayList<>();

    Boolean id = false, time = false, customer = false, amountDue = false, amountTendered = false,
            changeDue = false,type = false,invoice = false;
    String jsonObjectData = null;

    private List<Sales> SalesResponseList;
    private List<Sales> responseList;

    SaleResponse saleResponse;

    public SaleListAdapter(Activity activity,SaleResponse saleResponse) {
        this.activity = activity;
        this.saleResponse = saleResponse;

        for(int i=0 ;i < saleResponse.getItems().length;i++){
            SalesResponseList.add(this.saleResponse.getItems()[i]);
        }

        responseList = new ArrayList<>();
        responseList.addAll(SalesResponseList);
        checkBox_header = (CheckBox) activity.findViewById(R.id.checkBox);
    }

    public void setCheckedCallBack(CheckedCallBack checkedCallBack) {
        this.checkedCallBack = checkedCallBack;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String name = SalesResponseList.get(position).getItem_fk();

        jsonObjectData = VPOSPreferences.get(AppConstant.saFilterPreference);
        if (null != jsonObjectData) {
            if (LogFlag.bLogOn) Log.d(TAG,"jsonObjectData: "+jsonObjectData);
            getJSONData(jsonObjectData,holder);
        }else {
            holder.IdText.setVisibility(View.VISIBLE);
            holder.timeText.setVisibility(View.VISIBLE);
            holder.customerText.setVisibility(View.VISIBLE);
            holder.amountDueText.setVisibility(View.VISIBLE);
            holder.amountTenText.setVisibility(View.VISIBLE);
            holder.changeDueText.setVisibility(View.VISIBLE);
            holder.typeText.setVisibility(View.VISIBLE);
            holder.invoiceText.setVisibility(View.VISIBLE);

        }


        holder.IdText.setText("ID: " + saleResponse.getItems()[position].getSales_id());
        holder.timeText.setText("Time: " + saleResponse.getItems()[position].getCreated_at());
        holder.customerText.setText("Customer: " + saleResponse.getItems()[position].getCustomer_fk());
        holder.amountDueText.setText("Amount Due: " + saleResponse.getItems()[position].getAmount_due());
        holder.amountTenText.setText("Amount Tendered: " + saleResponse.getItems()[position].getAmount_tendered());
        holder.changeDueText.setText("Change Due: " + saleResponse.getItems()[position].getChange_due());
        holder.typeText.setText("Type: " + saleResponse.getItems()[position].getType());
        holder.invoiceText.setText("Invoice #: " + saleResponse.getItems()[position].getInvoice());


        holder.itemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedPositionArrayList.clear();
                if (isChecked) {
                    // Saving Checked Position
                    mChecked.put(position, isChecked);
                    holder.itemCheckBox.setButtonDrawable(R.drawable.check_box);


                    // Find if all the check boxes are true
                    if (isAllValuesChecked()) {

                        checkBox_header.setChecked(isChecked);
                        checkBox_header.setButtonDrawable(R.drawable.check_box);

                    }else {
                        if (isAnyValuesChecked()) {
                            checkedCallBack.onSelectedStatus(true);
                        }else {
                            checkedCallBack.onSelectedStatus(false);
                        }

                    }

                } else {
                    // Removed UnChecked Position
                    mChecked.delete(position);
                    holder.itemCheckBox.setButtonDrawable(R.drawable.box);

                    // Remove Checked in Header
                    checkBox_header.setChecked(isChecked);
                    checkBox_header.setButtonDrawable(R.drawable.box);
                    if (!isAllValuesChecked()) {

                        if (!isAnyValuesChecked()) {
                            checkedCallBack.onSelectedStatus(false);
                        }else {
                            checkedCallBack.onSelectedStatus(true );
                        }
                    }
                }

                for (int i = 0; i < count; i++) {
                    checkedPositionArrayList.add(mChecked.get(i));
                }
                checkedCallBack.onSelectedStatusArray(checkedPositionArrayList);
            }

        });

        checkBox_header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                for (int i = 0; i < count; i++) {
                    mChecked.put(i, checkBox_header.isChecked());
                }

                if(checkBox_header.isChecked()){
                    checkBox_header.setButtonDrawable(R.drawable.check_box);
                    checkedCallBack.onSelectedStatus(true);
                }else {
                    mChecked.delete(position);
                    checkBox_header.setButtonDrawable(R.drawable.box);
                    checkedCallBack.onSelectedStatus(false);
                }

                notifyDataSetChanged();
            }
        });


        // Set CheckBox "TRUE" or "FALSE" if mChecked == true
        holder.itemCheckBox.setChecked((mChecked.get(position) == true ? true : false));
    }


    protected boolean isAllValuesChecked() {

        for (int i = 0; i < count; i++) {
            if (!mChecked.get(i)) {
                return false;
            }
        }

        return true;
    }


    protected boolean isAnyValuesChecked() {

        for (int i = 0; i < count; i++) {
            if (mChecked.get(i)) {
                return true;
            }
        }

        return false;
    }



    @Override
    public int getItemCount() {
        count = saleResponse.getItems().length;
        return count;
    }


    public void add(int position, Sales item) {
        SalesResponseList.add(position, item);
        notifyItemInserted(position);
    }

    void remove(String item) {
        int position = SalesResponseList.indexOf(item);
        SalesResponseList.remove(position);
        notifyItemRemoved(position);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView IdText,timeText,customerText,amountDueText,amountTenText,changeDueText,typeText,invoiceText;
        CheckBox itemCheckBox;
        ViewHolder(View v) {
            super(v);
            IdText = (TextView) v.findViewById(R.id.IdText);
            timeText = (TextView) v.findViewById(R.id.timeText);
            customerText = (TextView) v.findViewById(R.id.customerText);
            amountDueText = (TextView) v.findViewById(R.id.amountDueText);
            amountTenText = (TextView) v.findViewById(R.id.amountTenText);
            changeDueText = (TextView) v.findViewById(R.id.changeDueText);
            typeText = (TextView) v.findViewById(R.id.typeText);
            invoiceText = (TextView) v.findViewById(R.id.invoiceText);

            itemCheckBox = (CheckBox) v.findViewById(R.id.itemCheckBox);

        }
    }


    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        SalesResponseList.clear();
        if (charText.length() == 0) {
            SalesResponseList.addAll(responseList);

        } else {
            for (Sales sales : responseList) {
                if (charText.length() != 0 && sales.getItem_fk().toLowerCase(Locale.getDefault()).contains(charText)) {
                    SalesResponseList.add(sales);
                } else if (charText.length() != 0 && sales.getCustomer_fk().toLowerCase(Locale.getDefault()).contains(charText)) {
                    SalesResponseList.add(sales);
                }
            }
        }
        notifyDataSetChanged();
    }


    private void getJSONData(String setting,ViewHolder holder) {

        try {

            JSONArray jsonArrayData = new JSONArray(setting);
            for (int i = 0; i < jsonArrayData.length(); i++) {
                JSONObject jsonObject = jsonArrayData.getJSONObject(i);
                id = jsonObject.getBoolean(AppConstant.TAG_ID);
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

        if(id){
            holder.IdText.setVisibility(View.VISIBLE);
        }else {
            holder.IdText.setVisibility(View.GONE);
        }

        if(time){
            holder.timeText.setVisibility(View.VISIBLE);
        }else {
            holder.timeText.setVisibility(View.GONE);
        }

        if(customer){
            holder.customerText.setVisibility(View.VISIBLE);
        }else {
            holder.customerText.setVisibility(View.GONE);
        }

        if(amountDue){
            holder.amountDueText.setVisibility(View.VISIBLE);
        }else {
            holder.amountDueText.setVisibility(View.GONE);
        }

        if(amountTendered){
            holder.amountTenText.setVisibility(View.VISIBLE);
        }else {
            holder.amountTenText.setVisibility(View.GONE);
        }


        if(changeDue){
            holder.changeDueText.setVisibility(View.VISIBLE);
        }else {
            holder.changeDueText.setVisibility(View.GONE);
        }

        if(type){
            holder.typeText.setVisibility(View.VISIBLE);
        }else {
            holder.typeText.setVisibility(View.GONE);
        }


        if(invoice){
            holder.invoiceText.setVisibility(View.VISIBLE);
        }else {
            holder.invoiceText.setVisibility(View.GONE);
        }


    }
}

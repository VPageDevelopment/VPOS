package com.vpage.vpos.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.pojos.supplier.SupplierResponse;
import com.vpage.vpos.pojos.supplier.Suppliers;
import com.vpage.vpos.tools.VPOSPreferences;
import com.vpage.vpos.tools.callBack.CheckedCallBack;
import com.vpage.vpos.tools.callBack.EditCallBack;
import com.vpage.vpos.tools.utils.AppConstant;
import com.vpage.vpos.tools.utils.LogFlag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SupplierListAdapter extends RecyclerView.Adapter<SupplierListAdapter.ViewHolder> {

    private static final String TAG = SupplierListAdapter.class.getName();

    private SparseBooleanArray mChecked = new SparseBooleanArray();

    private EditCallBack editCallBack;

    private CheckedCallBack checkedCallBack;

    private Activity activity;

    private static int count = 0;

    private CheckBox checkBox_header;

    private List<Boolean> checkedPositionArrayList = new ArrayList<>();
    Boolean ID = false,CName = false,AName = false,FName = false,LName = false,Email = false,PhoneNumber = false;
    String jsonObjectData = null;

    private List<Suppliers> supplierResponseList = new ArrayList<>();
    private List<Suppliers> responseList;

    SupplierResponse supplierResponse;


    public SupplierListAdapter(Activity activity,SupplierResponse supplierResponse) {
        this.activity = activity;
        this.supplierResponse = supplierResponse;

        for(int i=0 ;i < supplierResponse.getItems().length;i++){
            supplierResponseList.add(this.supplierResponse.getItems()[i]);
        }

        responseList = new ArrayList<>();
        responseList.addAll(supplierResponseList);
        checkBox_header = (CheckBox) activity.findViewById(R.id.checkBox);
    }

    public void setEditCallBack(EditCallBack editCallBack) {
        this.editCallBack = editCallBack;
    }

    public void setCheckedCallBack(CheckedCallBack checkedCallBack) {
        this.checkedCallBack = checkedCallBack;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_supplier, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String name = supplierResponseList.get(position).getFirst_name();

        jsonObjectData = VPOSPreferences.get(AppConstant.sFilterPreference);
        if (null != jsonObjectData) {
            if (LogFlag.bLogOn) Log.d(TAG,"jsonObjectData: "+jsonObjectData);
            getJSONData(jsonObjectData,holder);
        }else {
            holder.IdText.setVisibility(View.VISIBLE);
            holder.companyText.setVisibility(View.VISIBLE);
            holder.agencyText.setVisibility(View.VISIBLE);
            holder.firstText.setVisibility(View.VISIBLE);
            holder.lastText.setVisibility(View.VISIBLE);
            holder.emailText.setVisibility(View.VISIBLE);
            holder.phoneNumberText.setVisibility(View.VISIBLE);
        }


        holder.IdText.setText("ID: " +supplierResponseList.get(position).getSupplier_id());
        holder.companyText.setText("Company Name: " +supplierResponseList.get(position).getCompany_name());
        holder.agencyText.setText("Agency Name: " + supplierResponseList.get(position).getAgency_name());
        holder.firstText.setText("First Name: " +supplierResponseList.get(position).getFirst_name());
        holder.lastText.setText("Last Name: " + supplierResponseList.get(position).getLast_name());
        holder.emailText.setText("IKCPrice: " + supplierResponseList.get(position).getEmail());
        holder.phoneNumberText.setText("Phone Number: " + supplierResponseList.get(position).getPhone_number());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call back to customer view
                editCallBack.onEditSelected(position);
            }
        });


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView title = new TextView(activity);
                // You Can Customise your Title here
                title.setText(activity.getResources().getString(R.string.app_name));
                title.setBackgroundColor(Color.BLACK);
                title.setPadding(10, 15, 15, 10);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(20);

                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                alertDialog.setCustomTitle(title);
                alertDialog.setMessage("Are you Sure to Delete Customer Record");

                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // To Do delete the data from Customer response
                        remove(name);
                    }
                });
                alertDialog.show();
            }
        });


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
        count = supplierResponseList.size();
        return count;
    }


    public void add(int position, Suppliers item) {
        supplierResponseList.add(position, item);
        notifyItemInserted(position);
    }

    void remove(String item) {
        int position = supplierResponseList.indexOf(item);
        supplierResponseList.remove(position);
        notifyItemRemoved(position);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView IdText,companyText,agencyText,firstText,lastText,emailText,phoneNumberText;
        CheckBox itemCheckBox;
        ImageButton editButton,deleteButton;

        ViewHolder(View v) {
            super(v);
            IdText = (TextView) v.findViewById(R.id.IdText);
            companyText = (TextView) v.findViewById(R.id.companyText);
            agencyText = (TextView) v.findViewById(R.id.agencyText);
            firstText = (TextView) v.findViewById(R.id.firstText);
            lastText = (TextView) v.findViewById(R.id.lastText);
            emailText = (TextView) v.findViewById(R.id.emailText);
            phoneNumberText = (TextView) v.findViewById(R.id.phoneNumberText);
            itemCheckBox = (CheckBox) v.findViewById(R.id.itemCheckBox);
            editButton = (ImageButton) v.findViewById(R.id.editButton);
            deleteButton = (ImageButton) v.findViewById(R.id.deleteButton);
        }
    }


    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        supplierResponseList.clear();
        if (charText.length() == 0) {
            supplierResponseList.addAll(responseList);

        } else {
            for (Suppliers suppliers : responseList) {
                if (charText.length() != 0 && suppliers.getCompany_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    supplierResponseList.add(suppliers);
                } else if (charText.length() != 0 && suppliers.getAgency_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    supplierResponseList.add(suppliers);
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

        if(ID){
            holder.IdText.setVisibility(View.VISIBLE);
        }else {
            holder.IdText.setVisibility(View.GONE);
        }

        if(CName){
            holder.companyText.setVisibility(View.VISIBLE);
        }else {
            holder.companyText.setVisibility(View.GONE);
        }
        if(AName){
            holder.agencyText.setVisibility(View.VISIBLE);
        }else {
            holder.agencyText.setVisibility(View.GONE);
        }
        if(FName){
            holder.firstText.setVisibility(View.VISIBLE);
        }else {
            holder.firstText.setVisibility(View.GONE);
        }

        if(LName){
            holder.lastText.setVisibility(View.VISIBLE);
        }else {
            holder.lastText.setVisibility(View.GONE);
        }

        if(Email){
            holder.emailText.setVisibility(View.VISIBLE);
        }else {
            holder.emailText.setVisibility(View.GONE);
        }

        if(PhoneNumber){
            holder.phoneNumberText.setVisibility(View.VISIBLE);
        }else {
            holder.phoneNumberText.setVisibility(View.GONE);
        }

    }
}

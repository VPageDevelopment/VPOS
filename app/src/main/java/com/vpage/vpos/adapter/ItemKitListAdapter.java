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
import com.vpage.vpos.pojos.ItemKitResponse;
import com.vpage.vpos.pojos.itemkits.ItemKits;
import com.vpage.vpos.pojos.itemkits.ItemKitsResponse;
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

public class ItemKitListAdapter extends RecyclerView.Adapter<ItemKitListAdapter.ViewHolder> {

    private static final String TAG = ItemKitListAdapter.class.getName();

    private SparseBooleanArray mChecked = new SparseBooleanArray();

    private EditCallBack editCallBack;

    private CheckedCallBack checkedCallBack;

    private Activity activity;

    private static int count = 0;

    private CheckBox checkBox_header;

    private List<Boolean> checkedPositionArrayList = new ArrayList<>();
    Boolean ID = false, IKName = false, IKDes = false, IKCPrice = false, IKRPrice = false;
    String jsonObjectData = null;

    private List<ItemKits> itemKitResponseList;
    private List<ItemKits> responseList;

    ItemKitsResponse itemKitsResponse;


    public ItemKitListAdapter(Activity activity,ItemKitsResponse itemKitsResponse) {
        this.activity = activity;
        this.itemKitsResponse = itemKitsResponse;

        for(int i=0 ;i < itemKitsResponse.getItems().length;i++){
            itemKitResponseList.add(this.itemKitsResponse.getItems()[i]);
        }

        responseList = new ArrayList<>();
        responseList.addAll(itemKitResponseList);
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_itemkit, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String name = itemKitResponseList.get(position).getItem_kit_name();

        jsonObjectData = VPOSPreferences.get(AppConstant.iKFilterPreference);
        if (null != jsonObjectData) {
            if (LogFlag.bLogOn) Log.d(TAG,"jsonObjectData: "+jsonObjectData);
            getJSONData(jsonObjectData,holder);
        }else {
            holder.IdText.setVisibility(View.VISIBLE);
            holder.itemKitName.setVisibility(View.VISIBLE);
            holder.itemKitDescription.setVisibility(View.VISIBLE);
            holder.costPrice.setVisibility(View.VISIBLE);
            holder.retailPrice.setVisibility(View.VISIBLE);
        }


        holder.IdText.setText("Kit Id: " + itemKitResponseList.get(position).getItem_kit_id());
        holder.itemKitName.setText("Item Kit Name: " + itemKitResponseList.get(position).getItem_kit_name());
        holder.itemKitDescription.setText("Item Kit Description: " + itemKitResponseList.get(position).getItem_kit_desc());
       // holder.costPrice.setText("Cost Price: " + itemKitResponseList.get(position).g());
       // holder.retailPrice.setText("Retail Price: " + itemKitResponseList.get(position).g());

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
        count = itemKitResponseList.size();
        return count;
    }


    public void add(int position, ItemKits item) {
        itemKitResponseList.add(position, item);
        notifyItemInserted(position);
    }

    void remove(String item) {
        int position = itemKitResponseList.indexOf(item);
        itemKitResponseList.remove(position);
        notifyItemRemoved(position);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView IdText,itemKitName,itemKitDescription,costPrice,retailPrice;
        CheckBox itemCheckBox;
        ImageButton editButton,deleteButton;

        ViewHolder(View v) {
            super(v);
            IdText = (TextView) v.findViewById(R.id.IdText);
            itemKitName = (TextView) v.findViewById(R.id.itemKitName);
            itemKitDescription = (TextView) v.findViewById(R.id.itemKitDescription);
            costPrice = (TextView) v.findViewById(R.id.costPrice);
            retailPrice = (TextView) v.findViewById(R.id.retailPrice);
            itemCheckBox = (CheckBox) v.findViewById(R.id.itemCheckBox);
            editButton = (ImageButton) v.findViewById(R.id.editButton);
            deleteButton = (ImageButton) v.findViewById(R.id.deleteButton);
        }
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        itemKitResponseList.clear();
        if (charText.length() == 0) {
            itemKitResponseList.addAll(responseList);

        } else {
            for (ItemKits itemKits : responseList) {
                if (charText.length() != 0 && itemKits.getItem_kit_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    itemKitResponseList.add(itemKits);
                } else if (charText.length() != 0 && itemKits.getItem_kit_desc().toLowerCase(Locale.getDefault()).contains(charText)) {
                    itemKitResponseList.add(itemKits);
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
                IKName = jsonObject.getBoolean(AppConstant.TAG_IKName);
                IKDes = jsonObject.getBoolean(AppConstant.TAG_IKDes);
                IKCPrice = jsonObject.getBoolean(AppConstant.TAG_CPrice);
                IKRPrice = jsonObject.getBoolean(AppConstant.TAG_RPrice);

            }

        } catch (JSONException e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }

        if(ID){
            holder.IdText.setVisibility(View.VISIBLE);
        }else {
            holder.IdText.setVisibility(View.GONE);
        }

        if(IKName){
            holder.itemKitName.setVisibility(View.VISIBLE);
        }else {
            holder.itemKitName.setVisibility(View.GONE);
        }

        if(IKDes){
            holder.itemKitDescription.setVisibility(View.VISIBLE);
        }else {
            holder.itemKitDescription.setVisibility(View.GONE);
        }

        if(IKCPrice){
            holder.costPrice.setVisibility(View.VISIBLE);
        }else {
            holder.costPrice.setVisibility(View.GONE);
        }

        if(IKRPrice){
            holder.retailPrice.setVisibility(View.VISIBLE);
        }else {
            holder.retailPrice.setVisibility(View.GONE);
        }

    }
}
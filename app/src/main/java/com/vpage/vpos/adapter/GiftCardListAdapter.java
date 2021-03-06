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
import com.vpage.vpos.pojos.giftCards.GiftCard;
import com.vpage.vpos.pojos.giftCards.GiftCardResponse;
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

public class GiftCardListAdapter extends RecyclerView.Adapter<GiftCardListAdapter.ViewHolder> {

    private static final String TAG = GiftCardListAdapter.class.getName();

    private SparseBooleanArray mChecked = new SparseBooleanArray();

    private EditCallBack editCallBack;

    private CheckedCallBack checkedCallBack;

    private Activity activity;

    private static int count = 0;

    private CheckBox checkBox_header;

    private List<Boolean> checkedPositionArrayList = new ArrayList<>();
    Boolean ID = false,FName = false,LName = false,GC_No= false,GCValue = false;
    String jsonObjectData = null;

    private List<GiftCard> giftCardResponseList = new ArrayList<>();
    private List<GiftCard> responseList;

    GiftCardResponse giftCardResponse;


    public GiftCardListAdapter(Activity activity,GiftCardResponse giftCardResponse) {
        this.activity = activity;
        this.giftCardResponse = giftCardResponse;

        for(int i=0 ;i < giftCardResponse.getGiftCards().length;i++){
            giftCardResponseList.add(this.giftCardResponse.getGiftCards()[i]);
        }

        responseList = new ArrayList<>();
        responseList.addAll(giftCardResponseList);
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_customer, parent, false);
       ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String name = giftCardResponseList.get(position).getCustomer_fk();

        jsonObjectData = VPOSPreferences.get(AppConstant.gFilterPreference);

        if (null != jsonObjectData) {
            if (LogFlag.bLogOn) Log.d(TAG,"jsonObjectData: "+jsonObjectData);
            getJSONData(jsonObjectData,holder);
        }else {
            holder.IdText.setVisibility(View.VISIBLE);
            holder.firstText.setVisibility(View.VISIBLE);
            holder.lastText.setVisibility(View.VISIBLE);
            holder.giftCardNoText.setVisibility(View.VISIBLE);
            holder.giftCardValueText.setVisibility(View.VISIBLE);
        }


        holder.IdText.setText("ID: " + giftCardResponseList.get(position).getGift_card_id());
        holder.firstText.setText("First Name: " + giftCardResponseList.get(position).getCustomer_fk());
        holder.lastText.setText("Last Name: " + giftCardResponseList.get(position).getCustomer_fk());
        holder.giftCardNoText.setText("GiftCard Number: " + giftCardResponseList.get(position).getGift_card_number());
        holder.giftCardValueText.setText("GiftCard Value: " + giftCardResponseList.get(position).getGc_value());

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
        count = giftCardResponseList.size();
        return count;
    }


    public void add(int position, GiftCard item) {
        giftCardResponseList.add(position, item);
        notifyItemInserted(position);
    }

    void remove(String item) {
        int position = giftCardResponseList.indexOf(item);
        giftCardResponseList.remove(position);
        notifyItemRemoved(position);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView IdText,firstText,lastText,giftCardNoText,giftCardValueText;
        CheckBox itemCheckBox;
        ImageButton editButton,deleteButton;

        ViewHolder(View v) {
            super(v);
            IdText = (TextView) v.findViewById(R.id.IdText);
            firstText = (TextView) v.findViewById(R.id.firstText);
            lastText = (TextView) v.findViewById(R.id.lastText);
            giftCardNoText = (TextView) v.findViewById(R.id.emailText);
            giftCardValueText = (TextView) v.findViewById(R.id.phoneNumberText);
            itemCheckBox = (CheckBox) v.findViewById(R.id.itemCheckBox);
            editButton = (ImageButton) v.findViewById(R.id.editButton);
            deleteButton = (ImageButton) v.findViewById(R.id.deleteButton);
        }
    }


    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        giftCardResponseList.clear();
        if (charText.length() == 0) {
            giftCardResponseList.addAll(responseList);

        } else {
            for (GiftCard giftCard : responseList) {
                if (charText.length() != 0 && giftCard.getCustomer_fk().toLowerCase(Locale.getDefault()).contains(charText)) {
                    giftCardResponseList.add(giftCard);
                } else if (charText.length() != 0 && giftCard.getGift_card_number().toLowerCase(Locale.getDefault()).contains(charText)) {
                    giftCardResponseList.add(giftCard);
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
                FName = jsonObject.getBoolean(AppConstant.TAG_FName);
                LName = jsonObject.getBoolean(AppConstant.TAG_LName);
                GC_No = jsonObject.getBoolean(AppConstant.TAG_GC_NO);
                GCValue = jsonObject.getBoolean(AppConstant.TAG_GC_Value);

            }

        } catch (JSONException e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }

        if(ID){
            holder.IdText.setVisibility(View.VISIBLE);
        }else {
            holder.IdText.setVisibility(View.GONE);
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

        if(GC_No){
            holder.giftCardNoText.setVisibility(View.VISIBLE);
        }else {
            holder.giftCardNoText.setVisibility(View.GONE);
        }

        if(GCValue){
            holder.giftCardValueText.setVisibility(View.VISIBLE);
        }else {
            holder.giftCardValueText.setVisibility(View.GONE);
        }

    }
}

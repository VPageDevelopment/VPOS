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
import android.widget.ImageView;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.pojos.ItemResponseTest;
import com.vpage.vpos.pojos.item.ItemResponse;
import com.vpage.vpos.pojos.item.Items;
import com.vpage.vpos.tools.VPOSPreferences;
import com.vpage.vpos.tools.callBack.CheckedCallBack;
import com.vpage.vpos.tools.callBack.ItemCallBack;
import com.vpage.vpos.tools.utils.AppConstant;
import com.vpage.vpos.tools.utils.LogFlag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private static final String TAG = ItemListAdapter.class.getName();

    private SparseBooleanArray mChecked = new SparseBooleanArray();

    private ItemCallBack itemCallBack;

    private CheckedCallBack checkedCallBack;

    private Activity activity;

    private static int count = 0;

    private CheckBox checkBox_header;

    private List<Boolean> checkedPositionArrayList = new ArrayList<>();
    Boolean id = false,barCode = false,IName = false,category = false,cName = false,cPrice = false,rPrice = false,
            quantity = false,taxPer = false,avatar = false;
    String jsonObjectData = null;

    private List<Items> itemResponseList = new ArrayList<>();
    private List<Items> responseList;

    ItemResponse itemResponse;

    public ItemListAdapter(Activity activity,List<ItemResponseTest> itemResponseList) {
        this.activity = activity;

    }

    public ItemListAdapter(Activity activity,ItemResponse itemResponse) {
        this.activity = activity;
        this.itemResponse = itemResponse;

        for(int i=0 ;i < itemResponse.getItems().length;i++){
            itemResponseList.add(this.itemResponse.getItems()[i]);
        }

        responseList = new ArrayList<>();
        responseList.addAll(itemResponseList);
        checkBox_header = (CheckBox) activity.findViewById(R.id.checkBox);
    }


    public void setItemCallBack(ItemCallBack itemCallBack) {
        this.itemCallBack = itemCallBack;
    }

    public void setCheckedCallBack(CheckedCallBack checkedCallBack) {
        this.checkedCallBack = checkedCallBack;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String name = itemResponseList.get(position).getItem_name();

        jsonObjectData = VPOSPreferences.get(AppConstant.iFilterPreference);
        if (null != jsonObjectData) {
            if (LogFlag.bLogOn) Log.d(TAG,"jsonObjectData: "+jsonObjectData);
            getJSONData(jsonObjectData,holder);
        }else {
            holder.IdText.setVisibility(View.VISIBLE);
            holder.barcodeText.setVisibility(View.VISIBLE);
            holder.itemNameText.setVisibility(View.VISIBLE);
            holder.categoryText.setVisibility(View.VISIBLE);
            holder.companyNameText.setVisibility(View.VISIBLE);
            holder.costPriceText.setVisibility(View.VISIBLE);
            holder.retailPriceText.setVisibility(View.VISIBLE);
            holder.quantityText.setVisibility(View.VISIBLE);
            holder.taxPercentText.setVisibility(View.VISIBLE);
            holder.avatarImage.setVisibility(View.VISIBLE);
        }


        holder.IdText.setText("ID: " + itemResponseList.get(position).getItem_id());
        holder.barcodeText.setText("UPC/EAN/ISBN: " + itemResponseList.get(position).getUpc_ean_isbn());
        holder.itemNameText.setText("Item Name: " + itemResponseList.get(position).getItem_name());
        holder.categoryText.setText("Category: " + itemResponseList.get(position).getCategory());
        holder.companyNameText.setText("Company Name: " + itemResponseList.get(position).getSupplier_fk());
        holder.costPriceText.setText("Cost Price: " + itemResponseList.get(position).getCost_price());
        holder.retailPriceText.setText("Retail Price: " + itemResponseList.get(position).getRetail_price());
        holder.quantityText.setText("Quantity: " + itemResponseList.get(position).getQuantity_stock());
        holder.taxPercentText.setText("Tax Percent(s): " + itemResponseList.get(position).getTax_one());
       // Picasso.with(activity).load(itemResponseList.get(position).getAvatarUrl()).into( holder.avatarImage); // To do update image from server response



        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call back to customer view
                itemCallBack.onEditSelected(position);
            }
        });


        holder.updateInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call back to customer view
                itemCallBack.onUpdateInventory(position);
            }
        });

        holder.countDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call back to customer view
                itemCallBack.onCountInventory(position);
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
        count = itemResponseList.size();
        return count;
    }


    public void add(int position, Items item) {
        itemResponseList.add(position, item);
        notifyItemInserted(position);
    }

    void remove(String item) {
        int position = itemResponseList.indexOf(item);
        itemResponseList.remove(position);
        notifyItemRemoved(position);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView IdText,barcodeText,itemNameText,categoryText,companyNameText,costPriceText,retailPriceText,quantityText,
                taxPercentText;
        ImageView avatarImage;
        CheckBox itemCheckBox;
        ImageButton editButton,deleteButton,updateInventoryButton,countDetailsButton;

        ViewHolder(View v) {
            super(v);
            IdText = (TextView) v.findViewById(R.id.IdText);
            barcodeText = (TextView) v.findViewById(R.id.barcodeText);
            itemNameText = (TextView) v.findViewById(R.id.itemNameText);
            categoryText = (TextView) v.findViewById(R.id.categoryText);
            companyNameText = (TextView) v.findViewById(R.id.companyNameText);
            costPriceText = (TextView) v.findViewById(R.id.costPriceText);
            retailPriceText = (TextView) v.findViewById(R.id.retailPriceText);
            quantityText = (TextView) v.findViewById(R.id.quantityText);
            taxPercentText = (TextView) v.findViewById(R.id.taxPercentText);
            avatarImage = (ImageView) v.findViewById(R.id.avatarImage);

            itemCheckBox = (CheckBox) v.findViewById(R.id.itemCheckBox);
            updateInventoryButton = (ImageButton) v.findViewById(R.id.updateInventoryButton);
            countDetailsButton = (ImageButton) v.findViewById(R.id.countDetailsButton);
            editButton = (ImageButton) v.findViewById(R.id.editButton);
            deleteButton = (ImageButton) v.findViewById(R.id.deleteButton);
        }
    }


    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        itemResponseList.clear();
        if (charText.length() == 0) {
            itemResponseList.addAll(responseList);

        } else {
            for (Items items : responseList) {
                if (charText.length() != 0 && items.getItem_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    itemResponseList.add(items);
                } else if (charText.length() != 0 && items.getCategory().toLowerCase(Locale.getDefault()).contains(charText)) {
                    itemResponseList.add(items);
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

        if(id){
            holder.IdText.setVisibility(View.VISIBLE);
        }else {
            holder.IdText.setVisibility(View.GONE);
        }

        if(barCode){
            holder.barcodeText.setVisibility(View.VISIBLE);
        }else {
            holder.barcodeText.setVisibility(View.GONE);
        }

        if(IName){
            holder.itemNameText.setVisibility(View.VISIBLE);
        }else {
            holder.itemNameText.setVisibility(View.GONE);
        }

        if(category){
            holder.categoryText.setVisibility(View.VISIBLE);
        }else {
            holder.categoryText.setVisibility(View.GONE);
        }

        if(cName){
            holder.companyNameText.setVisibility(View.VISIBLE);
        }else {
            holder.companyNameText.setVisibility(View.GONE);
        }


        if(cPrice){
            holder.costPriceText.setVisibility(View.VISIBLE);
        }else {
            holder.costPriceText.setVisibility(View.GONE);
        }

        if(rPrice){
            holder.retailPriceText.setVisibility(View.VISIBLE);
        }else {
            holder.retailPriceText.setVisibility(View.GONE);
        }


        if(quantity){
            holder.quantityText.setVisibility(View.VISIBLE);
        }else {
            holder.quantityText.setVisibility(View.GONE);
        }

        if(taxPer){
            holder.taxPercentText.setVisibility(View.VISIBLE);
        }else {
            holder.taxPercentText.setVisibility(View.GONE);
        }

        if(avatar){
            holder.avatarImage.setVisibility(View.VISIBLE);
        }else {
            holder.avatarImage.setVisibility(View.GONE);
        }


    }
}

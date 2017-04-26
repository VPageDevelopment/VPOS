package com.vpage.vpos.adapter;

import java.util.ArrayList;
import java.util.List;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.tools.callBack.CustomerCheckedCallBack;
import com.vpage.vpos.tools.callBack.CustomerEditCallBack;
import com.vpage.vpos.tools.utils.LogFlag;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {

    private static final String TAG = CustomerListAdapter.class.getName();

    private List<String> mDataset;

    SparseBooleanArray mChecked = new SparseBooleanArray();

    CustomerEditCallBack customerEditCallBack;

    CustomerCheckedCallBack customerCheckedCallBack;

    Boolean checkBoxSelectStatus = false;

    Activity activity;

    private static int count = 0;

    CheckBox checkBox_header;


    public CustomerListAdapter(Activity activity,List<String> myDataset, Boolean checkBoxSelectStatus) {
        mDataset = new ArrayList<>();
        mDataset = myDataset;
        this.checkBoxSelectStatus = checkBoxSelectStatus;
        this.activity = activity;
        count = myDataset.size();

        checkBox_header = (CheckBox) activity.findViewById(R.id.checkBox);
    }

    public void setCustomerEditCallBack(CustomerEditCallBack customerEditCallBack) {
        this.customerEditCallBack = customerEditCallBack;
    }

    public void setCustomerCheckedCallBack(CustomerCheckedCallBack customerCheckedCallBack) {
        this.customerCheckedCallBack = customerCheckedCallBack;
    }


    @Override
    public CustomerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_customer, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String name = mDataset.get(position);


        holder.IdText.setText("ID: " +position);
        holder.firstText.setText("First Name: " + "Ram");
        holder.lastText.setText("Last Name: " + "Kumar");
        holder.emailText.setText("Email: " + "ramkumar@gmail.com");
        holder.phoneNumberText.setText("Phone Number: " + "93587210537");

        holder.editButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // call back to customer view
                customerEditCallBack.onEditSelected(position);
            }
        });


        holder.deleteButton.setOnClickListener(new OnClickListener() {
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
                            remove(name);
                        }
                    });
                    alertDialog.show();
            }
        });


        holder.itemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    // Saving Checked Position
                    mChecked.put(position, isChecked);
                    holder.itemCheckBox.setButtonDrawable(R.drawable.check_box);
                    customerCheckedCallBack.onSelectedStatus(true);

                    // Find if all the check boxes are true
                    if (isAllValuesChecked()) {

                        checkBox_header.setChecked(isChecked);
                        checkBox_header.setButtonDrawable(R.drawable.check_box);
                    }
                } else {
                    // Removed UnChecked Position
                    mChecked.delete(position);
                    holder.itemCheckBox.setButtonDrawable(R.drawable.box);

                    // Remove Checked in Header
                    checkBox_header.setChecked(isChecked);
                    checkBox_header.setButtonDrawable(R.drawable.box);

                }
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
                }else {
                    checkBox_header.setButtonDrawable(R.drawable.box);
                    customerCheckedCallBack.onSelectedStatus(false);
                }
                notifyDataSetChanged();

            }
        });


        // Set CheckBox "TRUE" or "FALSE" if mChecked == true
        holder.itemCheckBox.setChecked((mChecked.get(position) == true ? true : false));

        if (LogFlag.bLogOn) Log.d(TAG, "mCheckedArray: "+mChecked.toString());

    }

    protected boolean isAllValuesChecked() {

        for (int i = 0; i < count; i++) {
            if (!mChecked.get(i)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void add(int position, String item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView IdText,firstText,lastText,emailText,phoneNumberText;
        CheckBox itemCheckBox;
        ImageButton editButton,deleteButton;

        ViewHolder(View v) {
            super(v);
            IdText = (TextView) v.findViewById(R.id.IdText);
            firstText = (TextView) v.findViewById(R.id.firstText);
            lastText = (TextView) v.findViewById(R.id.lastText);
            emailText = (TextView) v.findViewById(R.id.emailText);
            phoneNumberText = (TextView) v.findViewById(R.id.phoneNumberText);
            itemCheckBox = (CheckBox) v.findViewById(R.id.itemCheckBox);
            editButton = (ImageButton) v.findViewById(R.id.editButton);
            deleteButton = (ImageButton) v.findViewById(R.id.deleteButton);
        }
    }
}
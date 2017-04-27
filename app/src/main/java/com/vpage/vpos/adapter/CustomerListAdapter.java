package com.vpage.vpos.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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
import com.vpage.vpos.pojos.CustomerResponse;
import com.vpage.vpos.tools.callBack.CustomerCheckedCallBack;
import com.vpage.vpos.tools.callBack.CustomerEditCallBack;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {

    private static final String TAG = CustomerListAdapter.class.getName();

    private SparseBooleanArray mChecked = new SparseBooleanArray();

    private CustomerEditCallBack customerEditCallBack;

    private CustomerCheckedCallBack customerCheckedCallBack;

    Boolean checkBoxSelectStatus = false;

    private Activity activity;

    private static int count = 0;

    private CheckBox checkBox_header;

    private List<Boolean> checkedPositionArrayList = new ArrayList<>();

    private List<CustomerResponse> customerResponseList;
    private List<CustomerResponse> responseList;

    public CustomerListAdapter(Activity activity,List<CustomerResponse> customerResponseList, Boolean checkBoxSelectStatus) {
        this.customerResponseList = customerResponseList;
        this.checkBoxSelectStatus = checkBoxSelectStatus;
        this.activity = activity;
        responseList = new ArrayList<>();
        responseList.addAll( this.customerResponseList );


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
        final String name = customerResponseList.get(position).getFirstName();

        holder.IdText.setText("ID: " +customerResponseList.get(position).getId());
        holder.firstText.setText("First Name: " +customerResponseList.get(position).getFirstName());
        holder.lastText.setText("Last Name: " + customerResponseList.get(position).getLastName());
        holder.emailText.setText("Email: " + customerResponseList.get(position).getEmail());
        holder.phoneNumberText.setText("Phone Number: " + customerResponseList.get(position).getPhoneNumber());

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
                            customerCheckedCallBack.onSelectedStatus(true);
                        }else {
                            customerCheckedCallBack.onSelectedStatus(false);
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
                            customerCheckedCallBack.onSelectedStatus(false);
                        }else {
                            customerCheckedCallBack.onSelectedStatus(true );
                        }
                    }
                }

                    for (int i = 0; i < count; i++) {
                        checkedPositionArrayList.add(mChecked.get(i));
                    }
                    customerCheckedCallBack.onSelectedStatusArray(checkedPositionArrayList);
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
                    customerCheckedCallBack.onSelectedStatus(true);
                }else {
                    mChecked.delete(position);
                    checkBox_header.setButtonDrawable(R.drawable.box);
                    customerCheckedCallBack.onSelectedStatus(false);
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
        count = customerResponseList.size();
        return count;
    }


    public void add(int position, CustomerResponse item) {
        customerResponseList.add(position, item);
        notifyItemInserted(position);
    }

    void remove(String item) {
        int position = customerResponseList.indexOf(item);
        customerResponseList.remove(position);
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


    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        customerResponseList.clear();
        if (charText.length() == 0) {
            customerResponseList.addAll(responseList);

        } else {
            for (CustomerResponse customerResponse : responseList) {
                if (charText.length() != 0 && customerResponse.getFirstName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    customerResponseList.add(customerResponse);
                } else if (charText.length() != 0 && customerResponse.getLastName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    customerResponseList.add(customerResponse);
                }
            }
        }
        notifyDataSetChanged();
    }
}
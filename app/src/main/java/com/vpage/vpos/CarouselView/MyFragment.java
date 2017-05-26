package com.vpage.vpos.CarouselView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nineoldandroids.view.ViewHelper;
import com.vpage.vpos.R;

import com.vpage.vpos.pojos.customer.Customers;
import com.vpage.vpos.pojos.customer.CustomersResponse;
import com.vpage.vpos.tools.VPOSRestTools;
import com.vpage.vpos.tools.utils.LogFlag;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MyFragment extends Fragment {

	private static final String TAG = MyFragment.class.getName();
	
	public static Fragment newInstance(Activity activity, int pos, float scale, boolean IsBlured)
	{
		
		Bundle b = new Bundle();
		b.putInt("pos", pos);
		b.putFloat("scale", scale);
		b.putBoolean("IsBlured", IsBlured);
		return Fragment.instantiate(activity, MyFragment.class.getName(), b);
	}

	public static Fragment newInstance(Activity activity, int pos, float scale, boolean IsBlured,
									   String customerResponseData)
	{

		Bundle b = new Bundle();
		b.putInt("pos", pos);
		b.putFloat("scale", scale);
		b.putBoolean("IsBlured", IsBlured);
		b.putString("customerResponseData", customerResponseData);
		return Fragment.instantiate(activity, MyFragment.class.getName(), b);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		
		LinearLayout linearLayout = (LinearLayout)
				inflater.inflate(R.layout.item_recycler_customer, container, false);
		
		int pos = this.getArguments().getInt("pos");

		TextView IdText = (TextView) linearLayout.findViewById(R.id.IdText);
		TextView firstText = (TextView) linearLayout.findViewById(R.id.firstText);
		TextView lastText = (TextView) linearLayout.findViewById(R.id.lastText);
		TextView giftCardNoText = (TextView) linearLayout.findViewById(R.id.emailText);
		TextView giftCardValueText = (TextView) linearLayout.findViewById(R.id.phoneNumberText);
		CheckBox itemCheckBox = (CheckBox) linearLayout.findViewById(R.id.itemCheckBox);
		ImageButton editButton = (ImageButton) linearLayout.findViewById(R.id.editButton);
		ImageButton deleteButton = (ImageButton) linearLayout.findViewById(R.id.deleteButton);

		String customerResponseData = this.getArguments().getString("customerResponseData");
		CustomersResponse customersResponse = VPOSRestTools.getInstance().getCustomerResponseData(customerResponseData);
		if (LogFlag.bLogOn)Log.d(TAG, "Customers: "+customersResponse.toString());
		if (LogFlag.bLogOn)Log.d(TAG, "Customers: "+customersResponse.toString());


		IdText.setText("ID: " + customersResponse.getCustomers()[pos].getCustomer_id());
		firstText.setText("First Name: " + customersResponse.getCustomers()[pos].getFirst_name());
		lastText.setText("Last Name: " + customersResponse.getCustomers()[pos].getLast_name());
		giftCardNoText.setText("Email: " + customersResponse.getCustomers()[pos].getEmail());
		giftCardValueText.setText("Phone Number: " + customersResponse.getCustomers()[pos].getPhone_number());
		
		TextView tv = (TextView) linearLayout.findViewById(R.id.viewID);
		tv.setText("Position = " + pos);

		MyLinearLayout root = (MyLinearLayout) linearLayout.findViewById(R.id.root);



		float scale = this.getArguments().getFloat("scale");
		root.setScaleBoth(scale);
		boolean isBlured=this.getArguments().getBoolean("IsBlured");
		if(isBlured)
		{
			ViewHelper.setAlpha(root,MyPagerAdapter.getMinAlpha());
			ViewHelper.setRotationY(root, MyPagerAdapter.getMinDegree());
		}
		return linearLayout;
	}


}

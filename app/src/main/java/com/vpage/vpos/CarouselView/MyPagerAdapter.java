package com.vpage.vpos.CarouselView;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.vpage.vpos.R;

import com.vpage.vpos.pojos.CustomerResponse;

import com.vpage.vpos.pojos.customer.Customers;
import com.vpage.vpos.pojos.customer.CustomersResponse;
import com.vpage.vpos.tools.VPOSRestTools;
import com.vpage.vpos.tools.utils.LogFlag;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter implements OnPageChangeListener {

	private static final String TAG = MyPagerAdapter.class.getName();


	private boolean swipedLeft=false;
	private int lastPage=9;
	private MyLinearLayout cur = null;
	private MyLinearLayout next = null;
	private MyLinearLayout prev = null;
	private MyLinearLayout prevprev = null;
	private MyLinearLayout nextnext = null;
	private FragmentManager fm;
	private float scale;
	private boolean IsBlured;
	private static float minAlpha=0.6f;
	private static float maxAlpha=1f;
	private static float minDegree=60.0f;
	private int counter=0;

	public static float getMinDegree()
	{
		return minDegree;
	}
	public static float getMinAlpha()
	{
		return minAlpha;
	}
	public static float getMaxAlpha()
	{
		return maxAlpha;
	}

	private Activity activity;
	List<CustomerResponse> customerResponseList;

	public final static float BIG_SCALE = 1.0f;
	public final static float SMALL_SCALE = 0.8f;
	public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
	public final static int FIRST_PAGE = 0;
	String customerResponseData;
	CustomersResponse customersResponse;
	
	public MyPagerAdapter(Activity activity, FragmentManager fm) {
		super(fm);
		this.fm = fm;
		this.activity = activity;
	}
	public MyPagerAdapter(Activity activity, FragmentManager fm,List<CustomerResponse> customerResponseList) {
		super(fm);
		this.fm = fm;
		this.activity = activity;
		this.customerResponseList = customerResponseList;
	}

	public MyPagerAdapter(Activity activity, FragmentManager fm,String customerResponseData) {
		super(fm);
		this.fm = fm;
		this.activity = activity;
		this.customerResponseData = customerResponseData;
		customersResponse = VPOSRestTools.getInstance().getCustomerResponseData(this.customerResponseData);
		if (LogFlag.bLogOn)Log.d(TAG, "Customers: "+customersResponse.toString());
	}



	@Override
	public Fragment getItem(int position) 
	{

		// make the first pager bigger than others
		if (position == FIRST_PAGE)
			scale = BIG_SCALE;
		else
		{
			scale =SMALL_SCALE;
			IsBlured=true;

		}

		Log.d("position", String.valueOf(position));
		Fragment curFragment= MyFragment.newInstance(activity, position, scale,IsBlured,customerResponseData);
		cur = getRootView(position);
		next = getRootView(position +1);
		prev = getRootView(position -1);

		return curFragment;
	}

	@Override
	public int getCount()
	{
		return customersResponse.getCustomers().length;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) 
	{	
		if (positionOffset >= 0f && positionOffset <= 1f)
		{
			positionOffset=positionOffset*positionOffset;
			cur = getRootView(position);
			next = getRootView(position +1);
			prev = getRootView(position -1);
			nextnext=getRootView(position +2);

			ViewHelper.setAlpha(cur, maxAlpha-0.5f*positionOffset);
	//		ViewHelper.setAlpha(next, minAlpha+0.5f*positionOffset);
	//		ViewHelper.setAlpha(prev, minAlpha+0.5f*positionOffset);
			
			
			if(nextnext!=null)
			{	
				ViewHelper.setAlpha(nextnext, minAlpha);
				ViewHelper.setRotationY(nextnext, -minDegree);
			}
			if(cur!=null)
			{
				cur.setScaleBoth(BIG_SCALE
						- DIFF_SCALE * positionOffset);

				ViewHelper.setRotationY(cur, 0);
			}

			if(next!=null)
			{
				next.setScaleBoth(SMALL_SCALE
						+ DIFF_SCALE * positionOffset);
				ViewHelper.setRotationY(next, -minDegree);
			}
			if(prev!=null)
			{
				ViewHelper.setRotationY(prev, minDegree);
			}

			
			/*To animate it properly we must understand swipe direction
			 * this code adjusts the rotation according to direction.
			 */
			if(swipedLeft)
			{
				if(next!=null)
					ViewHelper.setRotationY(next, -minDegree+minDegree*positionOffset);
				if(cur!=null)
					ViewHelper.setRotationY(cur, 0+minDegree*positionOffset);
			}
			else 
			{
				if(next!=null)
					ViewHelper.setRotationY(next, -minDegree+minDegree*positionOffset);
				if(cur!=null)
				{
					ViewHelper.setRotationY(cur, 0+minDegree*positionOffset);
				}
			}
		}
		if(positionOffset>=1f)
		{
			ViewHelper.setAlpha(cur, maxAlpha);
		}
	}

	@Override
	public void onPageSelected(int position) {

/*
 * to get finger swipe direction
 */
		if(lastPage<=position)
		{
			swipedLeft=true;
		}
		else if(lastPage>position)
		{
			swipedLeft=false;
		}
		lastPage=position;
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}



	private MyLinearLayout getRootView(int position)
	{
		MyLinearLayout ly;
		try {
			ly = (MyLinearLayout) 
					fm.findFragmentByTag(this.getFragmentTag(position))
					.getView().findViewById(R.id.root);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		if(ly!=null)
			return ly;
		return null;
	}

	private String getFragmentTag(int position)
	{
		return "android:switcher:" + activity.findViewById(R.id.customerViewPager).getId() + ":" + position;
	}
}

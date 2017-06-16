package com.vpage.vpos.tools;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;
import com.vpage.vpos.R;

public class VPOSApplication extends MultiDexApplication {

    private static final String TAG = VPOSApplication.class.getName();


    private static Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

    }

    public static Context getContext() {
        return mContext;
    }

}

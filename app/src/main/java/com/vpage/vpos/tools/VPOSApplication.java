package com.vpage.vpos.tools;


import android.app.Application;
import android.content.Context;

public class VPOSApplication extends Application {

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

    }

    public static Context getContext() {
        return mContext;
    }





}

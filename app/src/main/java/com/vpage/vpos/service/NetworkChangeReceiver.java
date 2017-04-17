package com.vpage.vpos.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.vpage.vpos.tools.utils.NetworkUtil;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String TAG = NetworkChangeReceiver.class.getName();



    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);
        Log.d(TAG, "NetworkChangeReceiver");
        Log.d(TAG,status);


/*        Toast.makeText(context, status, Toast.LENGTH_LONG).show();*/
    }

}

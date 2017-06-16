package com.vpage.vpos.tools;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vpos.R;
import com.vpage.vpos.pojos.SignInResponse;
import com.vpage.vpos.tools.utils.LogFlag;
import org.json.JSONException;
import org.json.JSONObject;

public class VPOSTools {

    private static final String TAG =  VPOSTools.class.getName();

    private static final Object monitor = new Object();
    private static VPOSTools vposTools = null;

    private static Gson gson = new GsonBuilder().create();;

    public static VPOSTools getInstance() {
        if (vposTools == null) {
            synchronized (monitor) {
                if (vposTools == null)
                    vposTools = new VPOSTools();
            }
        }
        return vposTools;
    }

    public static String getRequestSignIn(String jsonParams){
        JSONObject jsonObject = null;
        try {

            jsonObject = new JSONObject(jsonParams);
            jsonObject.put("Username",VPOSApplication.getContext().getResources().getString(R.string.userName));
            jsonObject.put("Password", VPOSApplication.getContext().getResources().getString(R.string.password));
        } catch (JSONException e) {
            if (LogFlag.bLogOn) Log.d(TAG, e.toString());
        }
        if (LogFlag.bLogOn)Log.d(TAG, "signIn-->"+jsonObject.toString());
        return jsonObject.toString();

    }


    public static String getRequestWithAppVersion(String jsonParams) {
        JSONObject jsonObject = null;
        if (LogFlag.bLogOn)Log.d(TAG, "Additional request data");
        try {
            PackageInfo pinfo = VPOSApplication.getContext().getPackageManager().getPackageInfo(VPOSApplication.getContext().getPackageName(), 0);
            if (LogFlag.bLogOn)Log.d("pinfoCode",String.valueOf(pinfo.versionCode));
            if (LogFlag.bLogOn)Log.d("pinfoName",pinfo.versionName);
            jsonObject = new JSONObject(jsonParams);
            jsonObject.put("appVersion",pinfo.versionName);
            jsonObject.put("devicePlatformName", "Android");
        } catch (JSONException e) {
            if (LogFlag.bLogOn) Log.d(TAG, e.toString());
        } catch (PackageManager.NameNotFoundException e) {
            if (LogFlag.bLogOn) Log.d(TAG, e.toString());
        }
        if (LogFlag.bLogOn)Log.d(TAG, "AppVersion & DeVicePlatform-->"+jsonObject.toString());
        return jsonObject.toString();
    }

    public static SignInResponse getActiveUser(SignInResponse signInResponse) {
        SignInResponse activeUser = new SignInResponse();
        activeUser.setUsername(signInResponse.getUsername());

        return activeUser;
    }

    public SignInResponse getActiveUserData(String jsonString) {
        //    if (LogFlag.bLogOn) Log.d(TAG, jsonString);
        Gson gson = new GsonBuilder().create();
        SignInResponse activeUser = gson.fromJson(jsonString, SignInResponse.class);
        return activeUser;
    }

}



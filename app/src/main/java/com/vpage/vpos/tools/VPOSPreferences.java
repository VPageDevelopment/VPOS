package com.vpage.vpos.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.vpage.vpos.R;
import com.vpage.vpos.tools.utils.LogFlag;

import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class VPOSPreferences {

    private static final String TAG = VPOSPreferences.class.getName();

    public static void save(String Key, String Value) {
        SharedPreferences preference;
        SharedPreferences.Editor editor;
        preference = VPOSApplication.getContext().getSharedPreferences(VPOSApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.putString(Key, Value);
        editor.commit();
    }

    public static String get(String Key) {
        SharedPreferences preference;
        String text;
        preference = VPOSApplication.getContext().getSharedPreferences(VPOSApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        text = preference.getString(Key, null);
        if (LogFlag.bLogOn)  Log.d(TAG, "JSONArray: "+text);
        return text;
    }

    public static void clear(String Key) {
        SharedPreferences preference;
        SharedPreferences.Editor editor;
        preference = VPOSApplication.getContext().getSharedPreferences(VPOSApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.remove(Key);
        editor.commit();
    }

    public static void clearAll() {
        SharedPreferences preference;
        SharedPreferences.Editor editor;
        preference = VPOSApplication.getContext().getSharedPreferences(VPOSApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.clear();
        editor.commit();
    }
}

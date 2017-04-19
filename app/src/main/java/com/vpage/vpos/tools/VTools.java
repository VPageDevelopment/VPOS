package com.vpage.vpos.tools;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vpage.vpos.R;

public class VTools {

    private static final String TAG =VTools.class.getName();

    private static final Object monitor = new Object();
    private static VTools vTools = null;

    public static VTools getInstance() {
        if (vTools == null) {
            synchronized (monitor) {
                if (vTools == null)
                    vTools = new VTools();
            }
        }
        return vTools;
    }


    private static String chosenModuleImage;

    public static String getChosenModuleImage() {
        return chosenModuleImage;
    }

    public static void setChosenModuleImage(String chosenModuleImage) {
        chosenModuleImage = chosenModuleImage;
    }



    @SuppressLint("NewApi")
    public static void setLayoutBackgroud(RelativeLayout layout, int id){
        try{
            Drawable drawable;
            int sdk= Build.VERSION.SDK_INT;
            if(sdk> Build.VERSION_CODES.LOLLIPOP) {
                drawable=VPOSApplication.getContext().getDrawable(id);
            }else {
                drawable=VPOSApplication.getContext().getResources().getDrawable(id);
            }
            if(sdk< Build.VERSION_CODES.JELLY_BEAN) {
                layout.setBackgroundDrawable(drawable);
            }else {
                layout.setBackground(drawable);
            }
        }catch (Exception e){
             Log.e(TAG, e.toString());
        }
    }

    public static void showAlertDialog(Activity activity, String message) {

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
        alertDialog.setMessage(message);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();

    }


}

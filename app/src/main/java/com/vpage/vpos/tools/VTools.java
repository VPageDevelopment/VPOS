package com.vpage.vpos.tools;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.RelativeLayout;

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


}

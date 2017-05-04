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
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.tools.utils.LogFlag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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

    public static PopupWindow createPopUp(View popUpView) {
        PopupWindow mpopup = new PopupWindow(popUpView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true); //Creation of popup
        try {
            mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
            mpopup.showAtLocation(popUpView, Gravity.CENTER_VERTICAL, 0, 0);    // Displaying popup

        } catch (Exception e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.toString());
        }
        return mpopup;
    }

    public static  String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }

    public static  String getYesterdayDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        calendar.add(Calendar.DATE, -1);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }

    public static String getLastWeekDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }

    public static String getThisMonthDate(){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }

    public static String getLastMonthDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }

    public static  String getLastYearDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        calendar.add(Calendar.YEAR, -1);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }

    public static String getThisMonthOnLastYearDate(){
       // This Month First day of Last Year

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }


    public static String getThisMonthLastYearDate(){
        // This Month Last day of Last Year

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }

    public static String getLastMonthFirstDay(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }


    public static String getLastMonthLastDay(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }


    public static  String getThisYearFirstDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }

    public static String getLastYearFirstDay(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }


    public static String getLastYearLastDay(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }

    public static String getDefaultDate(){

        // To all time set as any Default day
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        calendar.add(Calendar.YEAR, -7);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }

    public static long convertStringDateToLong(String dateToConvert){
        long milliseconds = 0;

        SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = f.parse(dateToConvert);
            ;milliseconds = date.getTime();
        } catch (ParseException e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.toString());
        }
        return milliseconds;
    }



}

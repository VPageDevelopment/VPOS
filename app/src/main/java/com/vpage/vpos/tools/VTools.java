package com.vpage.vpos.tools;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.vpage.vpos.R;
import com.vpage.vpos.tools.utils.LogFlag;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import googleprogressbar.ChromeFloatingCirclesDrawable;
import googleprogressbar.FoldingCirclesDrawable;
import googleprogressbar.GoogleMusicDicesDrawable;
import googleprogressbar.NexusRotationCrossDrawable;


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

    public static void setChosenModuleImage(String chosenImage) {
        chosenModuleImage = chosenImage;
    }


    public static void animation(Activity activity) {
        activity.overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
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
        if (LogFlag.bLogOn)Log.d(TAG, "dateToConvert: " + dateToConvert);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = simpleDateFormat.parse(dateToConvert);
            if (LogFlag.bLogOn)Log.d(TAG, "SimpleDateFormat: " + date);
            milliseconds = date.getTime();
            if (LogFlag.bLogOn)Log.d(TAG, "milliseconds: " + milliseconds);
        } catch (ParseException e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.toString());
        }
        return milliseconds;
    }

    public static Bitmap decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = options.outWidth, height_tmp = options.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);

        return bitmap;
    }


    public static void showToast(String message) {
        try {
            final Context context = VPOSApplication.getContext();
            final CharSequence text = message;
            final int duration = Toast.LENGTH_SHORT;
            Handler mainHandler = new Handler(context.getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(context, "  " + text + "  ", duration);
                    toast.show();
                }
            };
            mainHandler.post(myRunnable);
        } catch (Exception e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.toString());
        }
    }


    public static Drawable getProgressDrawable(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        int value = Integer.parseInt(prefs.getString(activity.getString(R.string.progressBar_pref_key), activity.getString(R.string.progressBar_pref_defValue)));
        Drawable progressDrawable = null;
        switch (value) {
            case 0:
                progressDrawable = new FoldingCirclesDrawable.Builder(activity)
                        .colors(getProgressDrawableColors(prefs))
                        .build();
                break;

            case 1:
                progressDrawable = new GoogleMusicDicesDrawable.Builder().build();
                break;

            case 2:
                progressDrawable = new NexusRotationCrossDrawable.Builder(activity)
                        .colors(getProgressDrawableColors(prefs))
                        .build();
                break;

            case 3:
                progressDrawable = new ChromeFloatingCirclesDrawable.Builder(activity)
                        .colors(getProgressDrawableColors(prefs))
                        .build();
                break;
        }

        return progressDrawable;
    }

    public static int[] getProgressDrawableColors(SharedPreferences prefs) {
        int[] colors = new int[4];
        colors[0] = prefs.getInt(VPOSApplication.getContext().getString(R.string.firstcolor_pref_key),VPOSApplication.getContext().getResources().getColor(R.color.red));
        colors[1] = prefs.getInt(VPOSApplication.getContext().getString(R.string.secondcolor_pref_key),VPOSApplication.getContext().getResources().getColor(R.color.blue));
        colors[2] = prefs.getInt(VPOSApplication.getContext().getString(R.string.thirdcolor_pref_key),VPOSApplication.getContext().getResources().getColor(R.color.yellow));
        colors[3] = prefs.getInt(VPOSApplication.getContext().getString(R.string.fourthcolor_pref_key), VPOSApplication.getContext().getResources().getColor(R.color.green));
        return colors;
    }

}

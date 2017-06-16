package com.vpage.vpos.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vpos.R;
import com.vpage.vpos.tools.VPOSPreferences;
import com.vpage.vpos.tools.VPOSTools;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.WindowFeature;
import butterknife.ButterKnife;
import butterknife.InjectView;

@WindowFeature({Window.FEATURE_NO_TITLE, Window.FEATURE_ACTION_BAR_OVERLAY})
@Fullscreen
@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getName();

    int delay = 2000;

    @InjectView(R.id.google_progress)
    ProgressBar mProgressBar;

    Activity activity;

    @AfterViews
    public void onSplash() {

        ButterKnife.inject(this);

        activity = SplashActivity.this;

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

       afterSplash();

    }

    @Override
    protected void onResume() {
        super.onResume();
        /**Dynamically*/
        Rect bounds = mProgressBar.getIndeterminateDrawable().getBounds();
        mProgressBar.setIndeterminateDrawable(VTools.getProgressDrawable(activity));
        mProgressBar.getIndeterminateDrawable().setBounds(bounds);
    }

    public void afterSplash() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                goToHome();

            }
        }, delay);
    }

    private void goToHome() {
        try {
            String isLoggedIn = VPOSPreferences.get("isLoggedIn");
            String userdata  = VPOSPreferences.get("userdata");

            if (isLoggedIn == null || isLoggedIn.isEmpty() || null == userdata || userdata.isEmpty()) {

                gotoLoginView();

            } else {

                gotoHomeView(userdata);

            }
        } catch (Exception e) {
            if (LogFlag.bLogOn)Log.e(TAG,  e.getMessage());
        }
    }


    private void gotoLoginView(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity_.class);
        startActivity(intent);
        finish();
    }

    void gotoHomeView(String userdata){

        Gson gson = new GsonBuilder().create();
        VPOSPreferences.save("activeUser", gson.toJson(userdata));
        Intent intent = new Intent(getApplicationContext(), HomeActivity_.class);
        intent.putExtra("ActiveUser", gson.toJson(VPOSTools.getInstance().getActiveUserData(userdata)));
        startActivity(intent);
        VTools.animation(this);
        finish();
    }

}

package com.vpage.vpos.activity;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import com.vpage.vpos.R;
import com.vpage.vpos.tools.BarcodeView;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_barcodegenerate)
public class BarcodeGenerateActivity extends Activity {

    private static final String TAG = BarcodeGenerateActivity.class.getName();

    @ViewById(R.id.barcodeView)
    LinearLayout linearLayout;

    @AfterViews
    public void onInitView() {

        linearLayout.setVisibility(View.VISIBLE);
        BarcodeView view = new BarcodeView(this);
        linearLayout.addView(view);
    }

}

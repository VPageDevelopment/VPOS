package com.vpage.vpos.tools.callBack;

import android.view.View;

public interface RecyclerTouchCallBack {
     void onClick(View view, int position);

     void onLongClick(View view, int position);
}

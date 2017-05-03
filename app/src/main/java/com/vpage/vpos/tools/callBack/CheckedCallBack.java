package com.vpage.vpos.tools.callBack;

import java.util.List;

public interface CheckedCallBack {

    void onSelectedStatus(Boolean checkedStatus);
    void onSelectedStatusArray(List<Boolean> checkedPositionArrayList);
}

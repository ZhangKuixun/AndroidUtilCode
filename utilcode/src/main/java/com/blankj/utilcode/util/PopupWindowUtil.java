package com.blankj.utilcode.util;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by ${张奎勋} on 2018/8/13.
 */

public class PopupWindowUtil {
    /**
     * 设置popupWindow背景颜色
     *
     * @param activity
     * @param alpha
     */
    public static void popupWindowBackground(Activity activity, float alpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
    }
}


package com.joe.epmediademo.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.joe.epmediademo.Application.MyApplication;

public class ScreenTools {

    public static int dip2px(int f) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (f * scale + 0.5f);
    }
    public static int dip2px(float f) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (f * scale + 0.5f);
    }

    private static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
    /**
     * 获取屏幕区域
     */
    private static Rect getScreenRect() {
        DisplayMetrics displayMetric = Resources.getSystem().getDisplayMetrics();
        return new Rect(0, 0, displayMetric.widthPixels, displayMetric.heightPixels);
    }

    /**
     * 获取屏幕宽度
     *
     */
    public static int getScreenWidth() {
        return getScreenRect().width();
    }

    /**
     * 获取屏幕高度
     *
     */
    public static int getScreenHeight() {
        if (!isAllScreenDevice() || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            return getScreenRect().height();
        else {
            WindowManager windowManager = (WindowManager) MyApplication.instance.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager == null) {
                return getScreenHeight(MyApplication.instance);
            }
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            return point.y;
        }
    }

    private static boolean mHasCheckAllScreen;
    private static boolean mIsAllScreenDevice;
    public static boolean isAllScreenDevice() {
        if (mHasCheckAllScreen) {
            return mIsAllScreenDevice;
        }
        mHasCheckAllScreen = true;
        mIsAllScreenDevice = false;
        // 低于 API 21的，都不会是全面屏。。。
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        WindowManager windowManager = (WindowManager) MyApplication.instance.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            float width, height;
            if (point.x < point.y) {
                width = point.x;
                height = point.y;
            } else {
                width = point.y;
                height = point.x;
            }
            if (height / width >= 1.97f) {
                mIsAllScreenDevice = true;
            }
        }
        return mIsAllScreenDevice;
    }

    public static int px2dip(float f) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (f / scale + 0.5f);
    }

    public static Point getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return new Point(wm.getDefaultDisplay().getWidth(), wm.getDefaultDisplay().getHeight());
    }

    /**
     * 使状态栏透明
     * <p>
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity 需要设置的activity
     */
    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }
    /**
     * 判断Activity是否Destroy
     * @param mActivity
     */
    public static boolean isDestroy(Activity mActivity) {
        if (mActivity == null || mActivity.isFinishing() ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }
}

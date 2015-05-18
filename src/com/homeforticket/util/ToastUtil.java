package com.homeforticket.util;

import com.homeforticket.appApplication.AppApplication;

import android.widget.Toast;

public class ToastUtil {

    public static Toast toast;

    /**
     * 单例，连续使用不会出现toast长时间呆在屏幕上的情况，duration为Toast.LENGTH_SHORT
     * 
     * @param context
     * @param text
     */
    public static void showToast(String text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(AppApplication.getAppContext(), text, Toast.LENGTH_SHORT);
        
        toast.show();
    }

    /**
     * 单例，连续使用不会出现toast长时间呆在屏幕上的情况，使用string资源，duration为Toast.LENGTH_SHORT
     * 
     * @param context
     * @param text
     */
    public static void showToast(int resId) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(AppApplication.getAppContext(), resId, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 单例，连续使用不会出现toast长时间呆在屏幕上的情况，duration为自定义
     * 
     * @param context
     * @param text
     * @param duration
     */
    public static void showToast(String text, int duration) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(AppApplication.getAppContext(), text, duration);
        toast.show();
    }

    /**
     * 单例，连续使用不会出现toast长时间呆在屏幕上的情况，使用string资源，duration为自定义
     * 
     * @param context
     * @param text
     * @param duration
     */
    public static void showToast(int resId, int duration) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(AppApplication.getAppContext(), resId, duration);
//        toast.setGravity(Gravity.CENTER, 0, INIT_POSY);
        toast.show();
    }

    /**
     * 普通的Toast，将makeText和show连接起来，duration为Toast.LENGTH_SHORT
     * 
     * @param context
     * @param text
     */
    public static void makeTextAndShow(String text) {
        Toast.makeText(AppApplication.getAppContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 普通的Toast，将makeText和show连接起来，使用string资源，duration为Toast.LENGTH_SHORT
     * 
     * @param context
     * @param resId
     */
    public static void makeTextAndShow(int resId) {
        Toast.makeText(AppApplication.getAppContext(), resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 普通的Toast，将makeText和show连接起来，duration为自定义
     * 
     * @param context
     * @param text
     * @param duration
     */
    public static void makeTextAndShow(String text,
            int duration) {
        Toast.makeText(AppApplication.getAppContext(), text, duration).show();
    }

    /**
     * 普通的Toast，将makeText和show连接起来，使用string资源，duration为自定义
     * 
     * @param context
     * @param resId
     * @param duration
     */
    public static void makeTextAndShow(int resId, int duration) {
        Toast.makeText(AppApplication.getAppContext(), resId, duration).show();
    }

}

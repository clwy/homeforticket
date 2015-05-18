
package com.homeforticket.appApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.homeforticket.connect.QuareManager;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;

public class AppApplication extends Application {

    private static final String TAG = "App";

    private static final String PACKAGE_NAME = "com.homeforticket";

    private static Context sContext;
    private static AppApplication instance;
    private QuareManager mQuareManager;
    private String mVersion = null;
    private Handler mHandler = new Handler();

    public static ExecutorService FULL_TASK_EXECUTOR = (ExecutorService) Executors
            .newCachedThreadPool();

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = this;
        instance = this;
        mVersion = getVerName(this);
        this.mQuareManager = new QuareManager(mVersion);
    }

    public static AppApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return sContext;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void applicationExit() {
        System.exit(0);
    }

    public QuareManager getQuareManager() {
        return mQuareManager;
    }

    // 获取版本名字
    public static String getVerName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(PACKAGE_NAME, 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // 获取版本号
    public static String getVerCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(PACKAGE_NAME, 0);
            return String.valueOf(pi.versionCode);
        } catch (NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLocalIPAddress() {
        WifiManager wifiManager = (WifiManager) getAppContext().getSystemService(
                Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        int i = info.getIpAddress();
        String ipStr = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
                + (i >> 24 & 0xFF);
        return ipStr;
    }
}

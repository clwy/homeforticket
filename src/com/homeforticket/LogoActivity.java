
package com.homeforticket;

import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.framework.HomeTicketActivity;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.util.SharedPreferencesUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LogoActivity extends BaseActivity {
    private static final long RUN_TOTAL_TIME = 1000;
    private Handler mHandler = new Handler();

    Runnable nextRunnable = new Runnable() {
        public void run() {
            startNextActivity();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        mHandler.postDelayed(nextRunnable, RUN_TOTAL_TIME + 100);
    }

    private void startNextActivity() {
        Intent intent = new Intent();
        if (SharedPreferencesUtil.readBoolean(SysConstants.IS_LOGIN, false)) {
            intent.setClass(LogoActivity.this, HomeTicketActivity.class);
        } else {
            intent.setClass(LogoActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        LogoActivity.this.finish();
    }
}


package com.homeforticket.module.firstpage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;

public class DistributionManageActivity extends BaseActivity implements OnClickListener {
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private RelativeLayout mDistributorMatching;
    private RelativeLayout mDistributorName;
    private RelativeLayout mDistributorSetting;
    private RelativeLayout mDistributorOrder;
    private RelativeLayout mDistributorIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mDistributorMatching = (RelativeLayout) findViewById(R.id.distributor_matching_layout);
        mDistributorName = (RelativeLayout) findViewById(R.id.distributor_name_layout);
        mDistributorSetting = (RelativeLayout) findViewById(R.id.distributor_setting_layout);
        mDistributorOrder = (RelativeLayout) findViewById(R.id.distributor_order_layout);
        mDistributorIncome = (RelativeLayout) findViewById(R.id.distributor_income_layout);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mDistributorMatching.setOnClickListener(this);
        mDistributorName.setOnClickListener(this);
        mDistributorSetting.setOnClickListener(this);
        mDistributorOrder.setOnClickListener(this);
        mDistributorIncome.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.distribution_manage_title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_top_button:
                finish();
                break;
            case R.id.distributor_matching_layout:
                startActivity(new Intent(this, DistributorMatchingAcitivity.class));
                break;
            case R.id.distributor_name_layout:
                startActivity(new Intent(this, DistributorNameAcitivity.class));
                break;
            case R.id.distributor_setting_layout:
                startActivity(new Intent(this, DistributorSettingAcitivity.class));
                break;
            case R.id.distributor_order_layout:
                startActivity(new Intent(this, DistributorOrderAcitivity.class));
                break;
            case R.id.distributor_income_layout:
                startActivity(new Intent(this, DistributorIncomeAcitivity.class));
                break;
            default:
                break;
        }
    }

}

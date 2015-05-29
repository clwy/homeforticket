
package com.homeforticket.module.me.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homeforticket.R;
import com.homeforticket.framework.BaseActivity;

public class FetchSuccessActivity extends BaseActivity implements OnClickListener {
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private Button mSaveButton;
    private TextView mBankCard;
    private TextView mPrice;
    private TextView mGetDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_success);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mSaveButton = (Button) findViewById(R.id.confirm_button);
        mBankCard = (TextView) findViewById(R.id.card);
        mPrice = (TextView) findViewById(R.id.price_count);
        mGetDate = (TextView) findViewById(R.id.get_date);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.profile_name);
        Intent intent = getIntent();
        mBankCard.setText(intent.getStringExtra("card"));
        mPrice.setText(intent.getStringExtra("price"));
        mGetDate.setText(intent.getStringExtra("date"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_top_button:
                finish();
                break;
            case R.id.save_button:
                finish();
                break;
            default:
                break;
        }
    }

}

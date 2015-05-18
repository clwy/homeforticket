
package com.homeforticket.module.me.activity;

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

public class UserBindAccountActivity extends BaseActivity implements OnClickListener, RequestListener {
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private TextView mBankCard;
    private TextView mGetAccountTime;
    private EditText mEditText;
    private Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_account);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mEditText = (EditText) findViewById(R.id.fetch_count);
        mNextButton = (Button) findViewById(R.id.next_button);
        mBankCard = (TextView) findViewById(R.id.bank_card);
        mGetAccountTime = (TextView) findViewById(R.id.get_account_time);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.fetch_account_title);
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {

    }

    @Override
    public void onFail(RequestJob job) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_top_button:
                finish();
                break;
            case R.id.next_button:
                break;
            default:
                break;
        }
    }

}

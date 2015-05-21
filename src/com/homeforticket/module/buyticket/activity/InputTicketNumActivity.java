
package com.homeforticket.module.buyticket.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.module.me.model.SetUserInfoMessage;
import com.homeforticket.module.me.parser.SetUserInfoMessageParser;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.IdcardUtils;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

public class InputTicketNumActivity extends BaseActivity implements OnClickListener {
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private EditText mEditText;
    private Button mSaveButton;
    private int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_count);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mEditText = (EditText) findViewById(R.id.edit_count);
        mSaveButton = (Button) findViewById(R.id.save_button);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mEditText.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        mCount = intent.getIntExtra("count", 0);
        mTxtTitle.setText("请输入您想要买的票数");
        mEditText.setText(String.valueOf(mCount));
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_top_button:
                finish();
                break;
            case R.id.save_button:
                Intent intent = new Intent();
                intent.putExtra("count", Integer.parseInt(mEditText.getText().toString()));
                setResult(1001, intent);
                finish();
                break;
            default:
                break;
        }
    }

}


package com.homeforticket.module.me.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

public class ResetPasswordActivity extends BaseActivity implements OnClickListener, RequestListener {
    private static final int MIN_INPUT_PASSWORD_COUNT = 6;
    
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private EditText mEditOriginalText;
    private EditText mEditNewText;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mEditOriginalText = (EditText) findViewById(R.id.edit_original_password);
        mEditNewText = (EditText) findViewById(R.id.edit_new_password);
        mSaveButton = (Button) findViewById(R.id.save_button);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mEditOriginalText.setOnClickListener(this);
        mEditNewText.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.profile_reset_password);
    }

    private void resetPassword() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "updatePassword");
            jsonObject.put("oldPasswd", mEditOriginalText.getText().toString().trim());
            jsonObject.put("password", mEditNewText.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new SetUserInfoMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.doRequest();
        }        
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {
        SetUserInfoMessage message = (SetUserInfoMessage) job.getBaseType();
        String code = message.getCode();

        if ("10000".equals(code)) {
            String token = message.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            finish();
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.RESET_PASSWORD);
            }
        }
        
        ToastUtil.showToast(message.getMessage());
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_top_button:
                finish();
                break;
            case R.id.save_button:
                if (mEditOriginalText.getText().length() < MIN_INPUT_PASSWORD_COUNT || 
                        mEditNewText.getText().length() < MIN_INPUT_PASSWORD_COUNT ) {
                    ToastUtil.showToast(R.string.password_length_short);
                    return;
                } 
                
                resetPassword();
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.RESET_PASSWORD) {
                resetPassword();
            } 
        }
        super.onActivityResult(requestCode, responseCode, data);
    }

}

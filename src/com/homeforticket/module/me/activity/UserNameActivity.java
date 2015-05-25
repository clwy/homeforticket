
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
import com.homeforticket.module.firstpage.model.ResellerMessage;
import com.homeforticket.module.firstpage.parser.ResellerMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.module.me.model.SetUserInfoMessage;
import com.homeforticket.module.me.parser.SetUserInfoMessageParser;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

public class UserNameActivity extends BaseActivity implements OnClickListener, RequestListener {
    private static final int MIN_USERNAME_LENGTH = 2;
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private EditText mEditText;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mEditText = (EditText) findViewById(R.id.edit_username);
        mSaveButton = (Button) findViewById(R.id.save_button);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mEditText.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.profile_name);
        mEditText.setText(SharedPreferencesUtil.readString(SysConstants.USERNAME, ""));
    }

    private void setUserInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "updateReseller");
            jsonObject.put("resellerName", mEditText.getText().toString().trim());
            jsonObject.put("mobile", SharedPreferencesUtil.readString(SysConstants.USER_TEL, ""));
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

            SharedPreferencesUtil.saveString(SysConstants.USERNAME, mEditText.getText().toString()
                    .trim());
            finish();
        } else {
            if ("10004".equals(code)) {
                SharedPreferencesUtil.saveBoolean(SysConstants.IS_LOGIN, false);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.SET_USER_NAME);
            }
            
            ToastUtil.showToast(message.getMessage());
        }
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.SET_USER_NAME) {
                setUserInfo();
            }
        }
        super.onActivityResult(requestCode, responseCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_top_button:
                finish();
                break;
            case R.id.edit_username:
                break;
            case R.id.save_button:
                String username = mEditText.getText().toString().trim();
                if (TextUtils.isEmpty(username) || username.length() < MIN_USERNAME_LENGTH) {
                    ToastUtil.showToast("您输入的昵称长度不足6位");
                    return;
                }
                setUserInfo();
                break;
            default:
                break;
        }
    }

}

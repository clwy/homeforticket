
package com.homeforticket.module.me.activity;

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

public class UserMobileActivity extends BaseActivity implements OnClickListener, RequestListener {
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private EditText mEditText;
    private Button mSaveButton;
    private TextView mInputMobileCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mEditText = (EditText) findViewById(R.id.edit_mobile);
        mSaveButton = (Button) findViewById(R.id.save_button);
        mInputMobileCount = (TextView) findViewById(R.id.input_mobile_cout);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mEditText.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        mEditText.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                new Handler().post(new Runnable() {
                    
                    @Override
                    public void run() {
                        mInputMobileCount.setText(String.valueOf(mEditText.getText().length()));
                    }
                });
            }
        });
    }

    private void initData() {
        mTxtTitle.setText(R.string.bind_mobile_title);
        mEditText.setText(SharedPreferencesUtil.readString(SysConstants.USER_TEL, ""));
        mInputMobileCount.setText(String.valueOf(mEditText.getText().length()));
    }
    
    private void setUserInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "updateReseller");
            jsonObject.put("mobile", mEditText.getText().toString().trim());
            jsonObject.put("resellerName", SharedPreferencesUtil.readString(SysConstants.USERNAME, ""));
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

            SharedPreferencesUtil.saveString(SysConstants.USER_TEL, mEditText.getText().toString().trim());
            finish();
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.SET_USER_MOBILE);
            }
        }
        
        ToastUtil.showToast(message.getMessage());
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }
    
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.SET_USER_MOBILE) {
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
                if (!IdcardUtils.isMobileNO(mEditText.getText().toString())) {
                    ToastUtil.showToast("手机号不正确，请重新输入");
                    return;
                }
                setUserInfo();
                break;
            default:
                break;
        }
    }

}


package com.homeforticket.module.me.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.module.me.model.FeedbackMessage;
import com.homeforticket.module.me.model.SetUserInfoMessage;
import com.homeforticket.module.me.parser.FeedbackMessageParser;
import com.homeforticket.module.me.parser.SetUserInfoMessageParser;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

public class FeedbackActivity extends BaseActivity implements OnClickListener, RequestListener {
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private EditText mInputContent;
    private EditText mInputAddress;
    private TextView mInputCount;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mInputContent = (EditText) findViewById(R.id.input_feedback);
        mInputAddress = (EditText) findViewById(R.id.input_your_address);
        mInputCount = (TextView) findViewById(R.id.count);
        mButton = (Button) findViewById(R.id.submit_button);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mButton.setOnClickListener(this);
        mInputContent.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                mInputCount.setText(String.valueOf(mInputContent.getText().length()));
            }
        });
    }

    private void initData() {
        mTxtTitle.setText(R.string.feedback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_top_button:
                finish();
                break;
            case R.id.submit_button:
                if (TextUtils.isEmpty(mInputContent.getText().toString()) || 
                        TextUtils.isEmpty(mInputAddress.getText().toString())) {
                    ToastUtil.showToast(R.string.input_none);
                    return;
                }
                
                uploadFeedbackRequest();
                break;
            default:
                break;
        }
    }

    private void uploadFeedbackRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "feedback");
            jsonObject.put("content", mInputContent.getText().toString().trim());
            jsonObject.put("phone", mInputAddress.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new FeedbackMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.doRequest();
        }        
    }

    @Override
    public void onStartRequest(RequestJob job) {
        
    }

    @Override
    public void onSuccess(RequestJob job) {
        FeedbackMessage message = (FeedbackMessage) job.getBaseType();
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
                startActivityForResult(intent, SysConstants.SUBMIT_FEEDBACK);
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
            if (requestCode == SysConstants.SUBMIT_FEEDBACK) {
                uploadFeedbackRequest();
            } 
        }
        super.onActivityResult(requestCode, responseCode, data);
    }

}


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
import com.homeforticket.module.me.model.BindAccountMessage;
import com.homeforticket.module.me.model.BindCardMessage;
import com.homeforticket.module.me.model.GetCodeMessage;
import com.homeforticket.module.me.parser.BindAccountMessageParser;
import com.homeforticket.module.me.parser.BindCardMessageParser;
import com.homeforticket.module.me.parser.GetCodeMessageParser;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

public class UserUnBindActivity extends BaseActivity implements OnClickListener, RequestListener {
    private static final int REQUEST_GET_CODE = 0;
    private static final int REQUEST_BIND_CARD = 1;
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private Button mSaveButton;
    private EditText mInputCardName;
    private EditText mInputCardBank;
    private EditText mInputCardNum;
    private EditText mInputCode;
    private Button mGetCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unbind_account);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mSaveButton = (Button) findViewById(R.id.bind_button);
        mInputCardName = (EditText) findViewById(R.id.input_card_name);
        mInputCardBank = (EditText) findViewById(R.id.input_bank_card_num);
        mInputCardNum = (EditText) findViewById(R.id.input_bank_name);
        mInputCode = (EditText) findViewById(R.id.input_captcha);
        mGetCode = (Button) findViewById(R.id.get_captcha);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        mGetCode.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.unbind_account_title);
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {
        switch (job.getRequestId()) {
            case REQUEST_GET_CODE:
                dealGetCode(job);
                break;
            case REQUEST_BIND_CARD:
                dealBindCard(job);
                break;

            default:
                break;
        }
    }

    private void dealGetCode(RequestJob job) {
        GetCodeMessage message = (GetCodeMessage) job.getBaseType();
        String code = message.getCode();

        if ("10000".equals(code)) {
            String token = message.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_CODE);
            } 
                
            ToastUtil.showToast(message.getMessage());
        }
    }

    private void dealBindCard(RequestJob job) {
        BindCardMessage message = (BindCardMessage) job.getBaseType();
        String code = message.getCode();

        if ("10000".equals(code)) {
            String token = message.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            SharedPreferencesUtil.saveString(SysConstants.BIND_ACCOUNT, "1");
            finish();
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_BIND_BANK);
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
            if (requestCode == SysConstants.GET_CODE) {
                getCodeRequest();
            } else if (requestCode == SysConstants.GET_BIND_BANK) {
                bindCardRequest();
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
            case R.id.bind_button:
                bindCardRequest();
                break;
            case R.id.get_captcha:
                getCodeRequest();
                break;
            default:
                break;
        }
    }
    
    private void getCodeRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "sendCode");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new GetCodeMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.setRequestId(REQUEST_GET_CODE);
            job.doRequest();
        }
    }
    
    private void bindCardRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "bindbank");
            jsonObject.put("accname", mInputCardName.getText().toString());
            jsonObject.put("bname", mInputCardBank.getText().toString());
            jsonObject.put("bcard", mInputCardNum.getText().toString());
            jsonObject.put("code", mInputCode.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new BindCardMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.setRequestId(REQUEST_GET_CODE);
            job.doRequest();
        }
    }

}

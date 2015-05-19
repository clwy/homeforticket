
package com.homeforticket.module.firstpage.activity;

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
import com.homeforticket.module.firstpage.model.ClientInfo;
import com.homeforticket.module.firstpage.model.ResellerMessage;
import com.homeforticket.module.firstpage.model.SetClientInfoMessage;
import com.homeforticket.module.firstpage.model.SetStoreInfoMessage;
import com.homeforticket.module.firstpage.model.StoreStatisticsMessage;
import com.homeforticket.module.firstpage.parser.ResellerMessageParser;
import com.homeforticket.module.firstpage.parser.SetClientInfoMessageParser;
import com.homeforticket.module.firstpage.parser.SetStoreInfoMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.module.me.model.SetUserInfoMessage;
import com.homeforticket.module.me.parser.SetUserInfoMessageParser;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

public class ClientDesActivity extends BaseActivity implements OnClickListener, RequestListener {
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private EditText mEditText;
    private Button mSaveButton;
    private ClientInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_des);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mEditText = (EditText) findViewById(R.id.edit_storename);
        mSaveButton = (Button) findViewById(R.id.save_button);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.client_setting_title);
        Intent intent = getIntent();
        mInfo = (ClientInfo) intent.getSerializableExtra("ClientInfo");
        mEditText.setText(mInfo.getNote());
    }

    private void setClinetInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "updateCustomers");
            jsonObject.put("id", mInfo.getId());
            jsonObject.put("text", mEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new SetClientInfoMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.doRequest();
        }
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {
        SetClientInfoMessage message = (SetClientInfoMessage) job.getBaseType();
        String code = message.getCode();

        if ("10000".equals(code)) {
            String token = message.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            Intent intent = new Intent();
            intent.putExtra("des", mEditText.getText().toString());
            setResult(SysConstants.SET_CLIENT_INFO, intent);
            finish();
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.SET_CLIENT_INFO);
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
            if (requestCode == SysConstants.SET_CLIENT_INFO) {
                setClinetInfo();
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
            case R.id.save_button:
                setClinetInfo();
                break;
            default:
                break;
        }
    }

}

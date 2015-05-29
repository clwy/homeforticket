
package com.homeforticket.module.me.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
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
import com.homeforticket.module.me.model.HelpMessage;
import com.homeforticket.module.me.model.SetUserInfoMessage;
import com.homeforticket.module.me.parser.HelpMessageParser;
import com.homeforticket.module.me.parser.SetUserInfoMessageParser;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

public class HelpActivity extends BaseActivity implements OnClickListener, RequestListener {
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mWebView = (WebView) findViewById(R.id.webview_help);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.help);
        getHelp();
    }
    
    private void getHelp() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "getHelp");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new HelpMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.doRequest();
        }        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_top_button:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStartRequest(RequestJob job) {
        
    }

    @Override
    public void onSuccess(RequestJob job) {
        HelpMessage message = (HelpMessage) job.getBaseType();
        String code = message.getCode();
        if ("10000".equals(code)) {
            String token = message.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            mWebView.loadUrl(message.getHelp());
        } else {
            if ("10004".equals(code)) {
                SharedPreferencesUtil.saveBoolean(SysConstants.IS_LOGIN, false);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_HELP);
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
            if (requestCode == SysConstants.GET_HELP) {
                getHelp();
            }
        }
        super.onActivityResult(requestCode, responseCode, data);
    }
}

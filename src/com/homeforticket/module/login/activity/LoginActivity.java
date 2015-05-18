
package com.homeforticket.module.login.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.homeforticket.LogoActivity;
import com.homeforticket.R;
import com.homeforticket.appApplication.AppApplication;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.framework.HomeTicketActivity;
import com.homeforticket.model.ReturnMessage;
import com.homeforticket.module.login.model.LoginMessage;
import com.homeforticket.module.login.parser.LoginMessageParser;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoginActivity extends BaseActivity implements OnClickListener, RequestListener {

    private static final int MIN_USERNAME_LENGTH = 6;
    private static final int MIN_PASSWORD_LENGTH = 6;

    // private TextView mTxtTitle;
    // private RelativeLayout mBtnBack;
    private EditText mEditUsername;
    private EditText mEditPassword;
    private Button mConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initListener();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initView() {
        // mTxtTitle = (TextView) findViewById(R.id.top_title);
        // mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        // mTxtTitle.setText(R.string.login_title);

        mEditUsername = (EditText) findViewById(R.id.username_text);
        mEditPassword = (EditText) findViewById(R.id.password_text);
        mConfirmButton = (Button) findViewById(R.id.confirm_button);
    }

    private void initListener() {
        // mBtnBack.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.confirm_button:
                onLoginClick();
                break;
        }
    }

    private void onLoginClick() {
        String username = mEditUsername.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();

//        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
//            ToastUtil.showToast(R.string.no_input_up);
//            return;
//        }
//
//        if (username.length() < MIN_USERNAME_LENGTH) {
//            ToastUtil.showToast(R.string.username_length_short);
//            return;
//        }
//
//        if (password.length() < MIN_PASSWORD_LENGTH) {
//            ToastUtil.showToast(R.string.password_length_short);
//            return;
//        }

        hideInput();
        doLoginRequest("13400000002", "13400000002");
//        doLoginRequest(username, password);
    }

    private void hideInput() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) this
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                        0);
            }
        } catch (Exception e) {
        }
    }

    private boolean checkUsername(String inputName) {
        boolean flage = false;
        String firstChar = inputName.substring(0, 1);

        try {
            String checkStr = "[a-zA-Z]*";
            Pattern pzm = Pattern.compile(checkStr);
            Matcher mat = pzm.matcher(firstChar);
            flage = mat.matches();
        } catch (Exception e) {
            e.printStackTrace();
            flage = false;
        }
        return flage;
    }

    private void doLoginRequest(String username, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "login");
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new LoginMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.doRequest();
        }
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {
        LoginMessage loginMessage = (LoginMessage) job.getBaseType();
        String code = loginMessage.getCode();
        if ("10000".equals(code)) {
            String token = loginMessage.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            onLoginSuccess(loginMessage);
        } else {
            if ("10004".equals(code)) {
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
            }
        }

        ToastUtil.showToast(loginMessage.getMessage());
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }

    private void onLoginSuccess(LoginMessage msg) {

        SharedPreferencesUtil.saveString(SysConstants.TOKEN, msg.getToken());
        SharedPreferencesUtil.saveString(SysConstants.USER_PHOTO, msg.getPhoto());
        SharedPreferencesUtil.saveString(SysConstants.USER_TEL, msg.getTel());
        SharedPreferencesUtil.saveString(SysConstants.USERNAME, msg.getName());
        SharedPreferencesUtil.saveString(SysConstants.TOTAL_MONEY, msg.getTotalMoney());
        SharedPreferencesUtil.saveString(SysConstants.BIND_ACCOUNT, msg.getBind());
        SharedPreferencesUtil.saveString(SysConstants.CURRENT_MONEY, msg.getCurrentMoney());
        SharedPreferencesUtil.saveString(SysConstants.ROLE, msg.getRole());
        if (SharedPreferencesUtil.readBoolean(SysConstants.IS_LOGIN, false)) {
            handlerLoginResult();
        } else {
            SharedPreferencesUtil.saveBoolean(SysConstants.IS_LOGIN, true);
            startActivity(new Intent(this, HomeTicketActivity.class));
            finish();
        }
    }

    /**
     * 处理登录结果
     */
    public void handlerLoginResult()
    {
        Intent intent = new Intent();
        intent.setAction("com.homeforticket");
        sendBroadcast(intent);
        setResult(SysConstants.REQUEST_TYPE_LOGIN, intent); // 界面关闭时发送返回标志
        this.finish();
    }

}

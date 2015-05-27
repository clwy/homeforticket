
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
import com.homeforticket.module.firstpage.model.WalletMessage;
import com.homeforticket.module.firstpage.parser.WalletMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.module.me.model.BindAccountMessage;
import com.homeforticket.module.me.model.FetchMessage;
import com.homeforticket.module.me.parser.BindAccountMessageParser;
import com.homeforticket.module.me.parser.FetchMessageParser;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

public class UserBindAccountActivity extends BaseActivity implements OnClickListener,
        RequestListener {
    private static final int REQUEST_BIND = 0;
    private static final int REQUEST_FETCH = 1;

    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private TextView mBankCard;
    private TextView mGetAccountTime;
    private EditText mEditText;
    private Button mNextButton;
    private String mBcard;
    private String mBname;

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
        getBindAccountRequest();
    }

    private void getBindAccountRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "getBank");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new BindAccountMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.setRequestId(REQUEST_BIND);
            job.doRequest();
        }
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {
        switch (job.getRequestId()) {
            case REQUEST_BIND:
                dealBind(job);
                break;
            case REQUEST_FETCH:
                dealFetch(job);
                break;
            default:
                break;
        }

    }

    private void dealFetch(RequestJob job) {
        FetchMessage message = (FetchMessage) job.getBaseType();
        String code = message.getCode();

        if ("10000".equals(code)) {
            String token = message.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }
            
            Intent intent = new Intent(this, FetchSuccessActivity.class);
            intent.putExtra("card", mBankCard.getText().toString());
            intent.putExtra("price", mEditText.getText().toString());
            intent.putExtra("date", mGetAccountTime.getText().toString());
            startActivity(intent);
        } else {
            if ("10004".equals(code)) {
                SharedPreferencesUtil.saveBoolean(SysConstants.IS_LOGIN, false);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.FETCH_MONEY);
            }
        }
        
        ToastUtil.showToast(message.getMessage());
    }

    private void dealBind(RequestJob job) {
        BindAccountMessage message = (BindAccountMessage) job.getBaseType();
        String code = message.getCode();

        if ("10000".equals(code)) {
            String token = message.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            mBcard = message.getBcard();
            mBname = message.getBname();
            mGetAccountTime.setText(message.getTime());
            if (TextUtils.isEmpty(mBname) || TextUtils.isEmpty(mBcard)) {

            } else {
                mBankCard.setText(mBname + " " + getResources().getString(R.string.bank_card_end)
                        + mBcard);
            }

            mEditText.setHint(getResources().getString(R.string.current_remain_count)
                    + message.getAvaBalance() + "元");

        } else {
            if ("10004".equals(code)) {
                SharedPreferencesUtil.saveBoolean(SysConstants.IS_LOGIN, false);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_BIND_BANK);
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
            if (requestCode == SysConstants.GET_BIND_BANK) {
                getBindAccountRequest();
            } else if (requestCode == SysConstants.FETCH_MONEY) {
                getFetchRequest();
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
            case R.id.next_button:
                if (TextUtils.isEmpty(mBankCard.getText().toString())
                        || TextUtils.isEmpty(mEditText.getText().toString())) {
                } else {
                    getFetchRequest();
                }
                break;
            default:
                break;
        }
    }

    private void getFetchRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "withdraw");
            jsonObject.put("total", mEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new FetchMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.setRequestId(REQUEST_FETCH);
            job.doRequest();
        }
    }

}

package com.homeforticket.module.firstpage.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.module.firstpage.model.WalletMessage;
import com.homeforticket.module.firstpage.parser.ResellerMessageParser;
import com.homeforticket.module.firstpage.parser.WalletMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.module.me.activity.UserBindAccountActivity;
import com.homeforticket.module.me.activity.UserUnBindActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

/**
 * @Title: WalletAcitivity.java 
 * @Package com.homeforticket.module.wallet.activity 
 * @Description: TODO 
 * @author LR   
 * @date 2015年5月1日 上午8:18:11 
 */
public class WalletAcitivity extends BaseActivity implements OnClickListener, RequestListener {
    
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private RelativeLayout mRecordLayout;
    private Button mFetchButton;
    private TextView mTotalWalletIncome;
    private TextView mTotalIncomeCount;
    private TextView mAvailableMoneyCount;
    private TextView mFreezeMoneyCount;
    private TextView mWithholdingMoneyCount;
    private TextView mBroughtMoneyCount;
    private TextView mSellMoneyCount;
    private TextView mRightArrow;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        initView();
        initListener();
        initData();

    }

    private void initData() {
        mTxtTitle.setText(R.string.wallet_title);
        mTotalIncomeCount.setText(SharedPreferencesUtil.readString(SysConstants.TOTAL_MONEY, "0.00"));
        getWalletRequest();
    }

    private void getWalletRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "getAccount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new WalletMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.doRequest();
        }
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mFetchButton.setOnClickListener(this);
        mRightArrow.setOnClickListener(this);
    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mRecordLayout = (RelativeLayout) findViewById(R.id.wallet_layout);
        mFetchButton = (Button) findViewById(R.id.fetch_button);
        mTotalWalletIncome = (TextView) findViewById(R.id.total_wallet_count);
        mTotalIncomeCount = (TextView) findViewById(R.id.total_income_count);
        mAvailableMoneyCount = (TextView) findViewById(R.id.available_money_count);
        mFreezeMoneyCount = (TextView) findViewById(R.id.freeze_money_count);
        mWithholdingMoneyCount = (TextView) findViewById(R.id.withholding_money_count);
        mBroughtMoneyCount = (TextView) findViewById(R.id.brought_money_count);
        mSellMoneyCount = (TextView) findViewById(R.id.sell_money_count);
        mRightArrow = (TextView) findViewById(R.id.right_arrow_img);
    }

    @Override
    public void onStartRequest(RequestJob job) {
        
    }

    @Override
    public void onSuccess(RequestJob job) {
        WalletMessage walletMessage = (WalletMessage) job.getBaseType();
        String code = walletMessage.getCode();

        if ("10000".equals(code)) {
            String token = walletMessage.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }
            float total = Float.parseFloat(walletMessage.getAvaBalance()) + Float.parseFloat(walletMessage.getFrzBalance());
            mTotalWalletIncome.setText(String.valueOf(total));
            mAvailableMoneyCount.setText(walletMessage.getAvaBalance());
            mFreezeMoneyCount.setText(walletMessage.getFrzBalance());
            mWithholdingMoneyCount.setText(walletMessage.getPreWithdraw());
            mBroughtMoneyCount.setText(walletMessage.getTotalWithdraw());
            mSellMoneyCount.setText(walletMessage.getTotalSale());

        } else {
            if ("10004".equals(code)) {
                SharedPreferencesUtil.saveBoolean(SysConstants.IS_LOGIN, false);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_WALLET_CODE);
            } 
                
            ToastUtil.showToast(walletMessage.getMessage());
        }
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }
    
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.GET_WALLET_CODE) {
                getWalletRequest();
            } 
        }
        super.onActivityResult(requestCode, responseCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.left_top_button:
                finish();
                break;
            case R.id.right_arrow_img:
                startActivity(new Intent(this, RecordAcitivity.class));
                break;
            case R.id.fetch_button:
                if ("0".equals(SharedPreferencesUtil.readString(SysConstants.BIND_ACCOUNT, "0"))) {
                    startActivity(new Intent(this, UserUnBindActivity.class));
                } else {
                    startActivity(new Intent(this, UserBindAccountActivity.class));
                }
                break;
        }
    }

}

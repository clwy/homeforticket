
package com.homeforticket.module.buyticket.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.homeforticket.R;
import com.homeforticket.common.WXPay;
import com.homeforticket.common.ZhifubaoPay;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.module.buyticket.model.PayUrlInfo;
import com.homeforticket.module.buyticket.model.SaveOrderMessage;
import com.homeforticket.module.buyticket.parser.PayUrlInfoParser;
import com.homeforticket.module.buyticket.parser.SaveOrderMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.pay.PayResult;
import com.homeforticket.pay.SignUtils;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

/**
 * @Title: ChoosePayActivity.java
 * @Package com.homeforticket.module.buyticket.activity
 * @Description: TODO
 * @author LR
 * @date 2015年5月15日 下午1:53:18
 */
public class ChoosePayActivity extends BaseActivity implements OnClickListener, RequestListener {
    private static final String ZHIFUBAO_PAY = "ALIPAYMOBILE";
    private static final String WEIXIN_PAY = "WEIXIN";
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private TextView mPayName;
    private TextView mPayPrice;
    private TextView mPayCount;
    private TextView mPayTotal;
    private TextView mNeedPay;
    private ImageView mWeixinPay;
    private ImageView mZhifubaoPay;
    private Button mConfirm;
    private String mOrderId;
    private String mOrderDes;
    private String mPayType;
    private String mTotalPay;
    private String mIsUnique;
    private ImageView mAdd;
    private ImageView mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pay);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mConfirm = (Button) findViewById(R.id.confirm_button);
        mPayName = (TextView) findViewById(R.id.pay_name);
        mPayPrice = (TextView) findViewById(R.id.pay_price);
        mPayCount = (TextView) findViewById(R.id.pay_count);
        mPayTotal = (TextView) findViewById(R.id.pay_total);
        mNeedPay = (TextView) findViewById(R.id.need_pay_count);
        mWeixinPay = (ImageView) findViewById(R.id.choose_weixin);
        mZhifubaoPay = (ImageView) findViewById(R.id.choose_zhifubao);
        mAdd = (ImageView) findViewById(R.id.right_add);
        mDelete = (ImageView) findViewById(R.id.left_delete);
        mAdd.setVisibility(View.INVISIBLE);
        mDelete.setVisibility(View.INVISIBLE);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mWeixinPay.setOnClickListener(this);
        mZhifubaoPay.setOnClickListener(this);
        mAdd.setOnClickListener(this);
        mDelete.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.pay_title);
        Intent intent = getIntent();
        mPayName.setText(intent.getStringExtra("name"));
        mPayPrice.setText(intent.getStringExtra("price") + "元");
        mPayCount.setText(intent.getStringExtra("count"));
        mPayTotal.setText(intent.getStringExtra("total") + "元");
        mNeedPay.setText(intent.getStringExtra("total") + "元");
        mOrderId = intent.getStringExtra("orderId");
        mOrderDes = "卖游翁";
        mTotalPay = intent.getStringExtra("total");
        mIsUnique = intent.getStringExtra("isUnique");
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {
        PayUrlInfo payUrlInfo = (PayUrlInfo) job.getBaseType();
        String code = payUrlInfo.getCode();
        if ("10000".equals(code)) {
            String token = payUrlInfo.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            if (ZHIFUBAO_PAY.equals(mPayType)) {
                new ZhifubaoPay(this, mTxtTitle, mPayName.getText().toString(), mOrderDes,
                        mTotalPay, mOrderId, payUrlInfo.getUrl());
            } else if (WEIXIN_PAY.equals(mPayType)) {
                new WXPay(this, mPayName.getText().toString(),
                        payUrlInfo.getUrl(), mTotalPay);
            }
        } else {
            if ("10004".equals(code)) {
                SharedPreferencesUtil.saveBoolean(SysConstants.IS_LOGIN, false);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.SAVE_LOG);
            }

            ToastUtil.showToast(payUrlInfo.getMessage());
        }
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.SAVE_LOG) {
                getSavelog();
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
            case R.id.confirm_button:
                getSavelog();
                break;
            case R.id.choose_weixin:
                mWeixinPay.setBackgroundResource(R.drawable.selected);
                mZhifubaoPay.setBackgroundResource(R.drawable.unselected);
                mPayType = WEIXIN_PAY;
                break;
            case R.id.choose_zhifubao:
                mWeixinPay.setBackgroundResource(R.drawable.unselected);
                mZhifubaoPay.setBackgroundResource(R.drawable.selected);
                mPayType = ZHIFUBAO_PAY;
                break;
            case R.id.right_add:
//                if (!"0".equals(mIsUnique)) {
//                    ToastUtil.showToast("当前是一证一票，不可更改数量");
//                    return;
//                }
//                mCount++;
//                mTotalPrice = mSinglePrice * mCount;
//                mPayTotal.setText(mTotalPrice + "元");
                break;
            case R.id.left_delete:
//                if (!"0".equals(mIsUnique)) {
//                    ToastUtil.showToast("当前是一证一票，不可更改数量");
//                    return;
//                }
//                
//                if (mCount > 0) {
//                    mCount--;
//                    mTotalPrice = mSinglePrice * mCount;
//                    mPayTotal.setText(mTotalPrice + "元");
//                }
                break;
            default:
                break;
        }
    }

    private void getSavelog() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "savelog");
            jsonObject.put("orderId", mOrderId);
            jsonObject.put("type", mPayType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new PayUrlInfoParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.doRequest();
        }
    }
}

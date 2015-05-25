package com.homeforticket.module.buyticket.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.module.buyticket.model.ProductInfo;
import com.homeforticket.module.buyticket.model.SceneInfo;
import com.homeforticket.module.buyticket.model.SceneTicketInfo;
import com.homeforticket.module.buyticket.parser.ProductInfoParser;
import com.homeforticket.module.buyticket.parser.SceneInfoParser;
import com.homeforticket.module.buyticket.parser.SceneTicketInfoParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

/**
 * @Title: ProductActivity.java 
 * @Package com.homeforticket.module.buyticket.activity 
 * @Description: TODO 
 * @author LR   
 * @date 2015年5月6日 下午4:36:02 
 */
public class ProductActivity extends BaseActivity implements OnClickListener, RequestListener{
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private TextView mProductName;
    private TextView mProductType;
    private TextView mProductContent;
    private TextView mProductImportant;
    private RelativeLayout mBottomButton;
    private SceneTicketInfo mTicketInfo;
    private ProductInfo mProductInfo;
    private String mLocation;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        initView();
        initListener();
        initData();
    }
    
    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mProductName = (TextView) findViewById(R.id.cost_name);
        mProductType = (TextView) findViewById(R.id.ticket_type_name);
        mProductContent = (TextView) findViewById(R.id.exchange_content);
        mProductImportant = (TextView) findViewById(R.id.important_content);
        mBottomButton = (RelativeLayout) findViewById(R.id.bottom_button);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mBottomButton.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.product_title);
        Intent intent = getIntent();
        mTicketInfo = (SceneTicketInfo) intent.getSerializableExtra("SceneTicketInfo");
        mProductType.setText(mTicketInfo.getProductName());
        mLocation = intent.getStringExtra("location");
        getInfoRequest();
    }

    private void getInfoRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "queryProduct");
            jsonObject.put("productId", mTicketInfo.getProductId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new ProductInfoParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.doRequest();
        }        
    }

    @Override
    public void onStartRequest(RequestJob job) {
        
    }

    @Override
    public void onSuccess(RequestJob job) {
        mProductInfo = (ProductInfo) job.getBaseType();
        String code = mProductInfo.getCode();
        if ("10000".equals(code)) {
            String token = mProductInfo.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }
            
            mProductName.setText(Html.fromHtml(mProductInfo.getCosts()));
            mProductContent.setText(Html.fromHtml(mProductInfo.getNotice()));
            mProductImportant.setText(Html.fromHtml(mProductInfo.getSpecialNote()));
            
        } else {
            if ("10004".equals(code)) {
                SharedPreferencesUtil.saveBoolean(SysConstants.IS_LOGIN, false);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_PRODUCT_CODE);
            }
            
            ToastUtil.showToast(mProductInfo.getMessage());
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.GET_PRODUCT_CODE) {
                getInfoRequest();
            }
        }
        super.onActivityResult(requestCode, responseCode, data);
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_top_button:
                finish();
                break;
            case R.id.bottom_button:
                if (mProductInfo != null && !TextUtils.isEmpty(mProductName.getText().toString())) {
                    Intent intent = new Intent(this, PlaceOrderActivity.class);
                    intent.putExtra("ProductInfo", mProductInfo);
                    intent.putExtra("name", mTicketInfo.getProductName());
                    intent.putExtra("location", mLocation);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

}

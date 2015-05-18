
package com.homeforticket.module.firstpage.activity;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.DatePicker.OnDateChangedListener;

import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.module.firstpage.model.AddProductMessage;
import com.homeforticket.module.firstpage.model.StatisticsInfoMessage;
import com.homeforticket.module.firstpage.parser.AddproductMessageParser;
import com.homeforticket.module.firstpage.parser.StoreStatisticsMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

/**
 * @Title: AddProductActivity.java
 * @Package com.homeforticket.module.firstpage.activity
 * @Description: TODO
 * @author LR
 * @date 2015年5月14日 上午8:19:46
 */
public class AddProductContentActivity extends BaseActivity implements OnClickListener,
        RequestListener {
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private Button mFinishButton;
    private EditText mProductName;
    private EditText mOriginPrice;
    private EditText mCurrentPrice;
    private EditText mCommissionPrice;
    private EditText mKinds;
    private TextView mBeginTime;
    private TextView mEndTime;
    private EditText mCity;
    private EditText mAddress;
    private EditText mDes;
    private DatePicker mDatePicker;
    private Button mConfirmButton;
    private boolean mIsBegin;
    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_self_product);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mFinishButton = (Button) findViewById(R.id.right_button);
        mFinishButton.setVisibility(View.VISIBLE);
        mProductName = (EditText) findViewById(R.id.input_product_name);
        mOriginPrice = (EditText) findViewById(R.id.input_origin_price);
        mCurrentPrice = (EditText) findViewById(R.id.input_current_price);
        mCommissionPrice = (EditText) findViewById(R.id.input_commission_price);
        mKinds = (EditText) findViewById(R.id.input_kinds);
        mBeginTime = (TextView) findViewById(R.id.input_begin_time);
        mEndTime = (TextView) findViewById(R.id.input_end_time);
        mCity = (EditText) findViewById(R.id.input_city);
        mAddress = (EditText) findViewById(R.id.input_address);
        mDatePicker = (DatePicker) findViewById(R.id.datepicker);
        mConfirmButton = (Button) findViewById(R.id.confirm);
        mDes = (EditText) findViewById(R.id.input_des);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mFinishButton.setOnClickListener(this);
        mBeginTime.setOnClickListener(this);
        mEndTime.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.add_self_prodduct);
        mFinishButton.setText(R.string.commit);

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        final int currentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH) + 1;
        final int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        
        mDatePicker.init(mYear, mMonth - 1, mDay, new OnDateChangedListener() {

            public void onDateChanged(DatePicker view, int year,
                    int monthOfYear, int dayOfMonth) {
                
                String current = String.valueOf(currentYear) + String.valueOf(currentMonth) 
                        + String.valueOf(currentDay);
                String after = String.valueOf(year) + String.valueOf(monthOfYear + 1) 
                        + String.valueOf(dayOfMonth); 
                if (Integer.parseInt(after) >= Integer.parseInt(current)) {
                    mYear = year;
                    mMonth = monthOfYear + 1;
                    mDay = dayOfMonth;
                } else {
                    ToastUtil.showToast(R.string.time_error);
                }
            }
        });
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {
        AddProductMessage message = (AddProductMessage) job.getBaseType();
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
                startActivityForResult(intent, SysConstants.ADD_PRODUCT);
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
            if (requestCode == SysConstants.ADD_PRODUCT) {
                doAddProduct();
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
            case R.id.right_button:
//                String startTime = mBeginTime.getText().toString();
//                String endTiem = mEndTime.getText().toString();
//                if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTiem)) {
//                    int start = Integer.parseInt(startTime.replaceAll("-", ""));
//                    int end = Integer.parseInt(endTiem.replaceAll("-", ""));
//                    if (start <= end) {
//                        doAddProduct();
//                    } else {
//                        ToastUtil.showToast(R.string.time_error);
//                    }
//                }
                break;
            case R.id.input_begin_time:
                mIsBegin = true;
                mDatePicker.setVisibility(View.VISIBLE);
                mConfirmButton.setVisibility(View.VISIBLE);
                break;
            case R.id.input_end_time:
                mIsBegin = false;
                mDatePicker.setVisibility(View.VISIBLE);
                mConfirmButton.setVisibility(View.VISIBLE);
                break;
            case R.id.confirm:
                String chooseMonth = String.valueOf(mMonth);
                if (chooseMonth.length() < 2) {
                    chooseMonth = "0" + String.valueOf(mMonth);
                }
                String chooseDay = String.valueOf(mDay);
                if (chooseDay.length() < 2) {
                    chooseDay = "0" + String.valueOf(mDay);
                }
                if (mIsBegin) {
                    mBeginTime.setText(mYear + "-" + chooseMonth + "-" + chooseDay);
                } else {
                    mEndTime.setText(mYear + "-" + chooseMonth + "-" + chooseDay);
                }
                mDatePicker.setVisibility(View.INVISIBLE);
                mConfirmButton.setVisibility(View.INVISIBLE);
                break;
        }
    }
    
    private void doAddProduct() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "saveProduct");
            jsonObject.put("productName", mProductName.getText().toString());
            jsonObject.put("saleStartTime", mBeginTime.getText());
            jsonObject.put("saleEndTime", mEndTime.getText());
            jsonObject.put("price", mOriginPrice.getText().toString());
            jsonObject.put("retailPrice", mCurrentPrice.getText().toString());
            jsonObject.put("address", mAddress.getText().toString());
            jsonObject.put("commission", mCommissionPrice.getText().toString());
            jsonObject.put("channel", mKinds.getText().toString());
            jsonObject.put("city", mCity.getText().toString());
            jsonObject.put("description", mDes.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new AddproductMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.doRequest();
        }
    }
}

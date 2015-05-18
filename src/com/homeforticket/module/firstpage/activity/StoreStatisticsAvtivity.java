
package com.homeforticket.module.firstpage.activity;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homeforticket.R;
import com.homeforticket.R.string;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.module.firstpage.model.StoreStatisticsMessage;
import com.homeforticket.module.firstpage.parser.StoreInfoMessageParser;
import com.homeforticket.module.firstpage.parser.StoreStatisticsMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

/**
 * @Title: StoreStatisticsAvtivity.java
 * @Package com.homeforticket.module.firstpage.activity
 * @Description: TODO
 * @author LR
 * @date 2015年5月5日 下午7:01:48
 */
public class StoreStatisticsAvtivity extends BaseActivity implements OnClickListener,
        RequestListener {
    private static final int GETWEBSITE_REQUEST = 0;
    private static final int GETWEBSITE_STATISTICS = 1;
    
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private DatePicker mDatePicker;
    private TextView mBeginText;
    private TextView mEndText;
    private TextView mStoreName;
    private RelativeLayout mBeginLayout;
    private RelativeLayout mEndLayout;
    private Button mConfirmButton;
    private Button mStatisticsButton;
    private boolean mIsBegin;
    private int mYear;
    private int mMonth;
    private int mDay;
    private StoreStatisticsMessage mStoreStatisticsMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_statistics);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mDatePicker = (DatePicker) findViewById(R.id.datepicker);
        mBeginText = (TextView) findViewById(R.id.begin_time);
        mEndText = (TextView) findViewById(R.id.end_time);
        mStoreName = (TextView) findViewById(R.id.store_name);
        mBeginLayout = (RelativeLayout) findViewById(R.id.begin_time_layout);
        mEndLayout = (RelativeLayout) findViewById(R.id.end_time_layout);
        mStatisticsButton = (Button) findViewById(R.id.statistics_button);
        mConfirmButton = (Button) findViewById(R.id.confirm);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mBeginLayout.setOnClickListener(this);
        mEndLayout.setOnClickListener(this);
        mStatisticsButton.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.store_count_title);

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
        
        doGetWebsite();
    }
    
    private void doGetWebsite() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "getWebsite");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new StoreStatisticsMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.setRequestId(GETWEBSITE_REQUEST);
            job.doRequest();
        }
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {
        mStoreStatisticsMessage = (StoreStatisticsMessage) job.getBaseType();
        String code = mStoreStatisticsMessage.getCode();

        if ("10000".equals(code)) {
            String token = mStoreStatisticsMessage.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }
            
            mStoreName.setText(mStoreStatisticsMessage.getName());
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_STORE_CODE);
            }
            
            ToastUtil.showToast(mStoreStatisticsMessage.getMessage());
        }

    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }
    
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.GET_STORE_CODE) {
                doGetWebsite();
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
            case R.id.begin_time_layout:
                mIsBegin = true;
                mDatePicker.setVisibility(View.VISIBLE);
                mConfirmButton.setVisibility(View.VISIBLE);
                break;
            case R.id.end_time_layout:
                mIsBegin = false;
                mDatePicker.setVisibility(View.VISIBLE);
                mConfirmButton.setVisibility(View.VISIBLE);
                break;
            case R.id.statistics_button:
                String startTime = mBeginText.getText().toString();
                String endTiem = mEndText.getText().toString();
                if (mStoreStatisticsMessage != null && !TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTiem)) {
                    int start = Integer.parseInt(startTime.replaceAll("-", ""));
                    int end = Integer.parseInt(endTiem.replaceAll("-", ""));
                    if (start <= end) {
                        Intent intent = new Intent(this, StoreStatisticsContentAvtivity.class);
                        intent.putExtra("StoreStatisticsMessage", mStoreStatisticsMessage);
                        intent.putExtra("startTime", mBeginText.getText().toString());
                        intent.putExtra("endTime", mEndText.getText().toString());
                        startActivity(intent); 
                    } else {
                        ToastUtil.showToast(R.string.time_error);
                    }
                }
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
                    mBeginText.setText(mYear + "-" + chooseMonth + "-" + chooseDay);
                } else {
                    mEndText.setText(mYear + "-" + chooseMonth + "-" + chooseDay);
                }
                mDatePicker.setVisibility(View.INVISIBLE);
                mConfirmButton.setVisibility(View.INVISIBLE);
                break;

            default:
                break;
        }
    }

}


package com.homeforticket.module.firstpage.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homeforticket.R;
import com.homeforticket.R.string;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase;
import com.homeforticket.framework.pullrefrash.PullToRefreshListView;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase.Mode;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase.OnRefreshListener;
import com.homeforticket.module.firstpage.adapter.OrderAdapter;
import com.homeforticket.module.firstpage.adapter.StatisticsAdapter;
import com.homeforticket.module.firstpage.model.OrderInfo;
import com.homeforticket.module.firstpage.model.StatisticsInfo;
import com.homeforticket.module.firstpage.model.StatisticsInfoMessage;
import com.homeforticket.module.firstpage.model.StoreStatisticsMessage;
import com.homeforticket.module.firstpage.parser.StatisticsInfoMessageParser;
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
public class StoreStatisticsContentAvtivity extends BaseActivity implements OnClickListener,
        RequestListener, OnRefreshListener<ListView> {
    private static final int REQUEST_STATISTICS_LIST = 0;
    private static final int REQUEST_STATISTICS_NEXT_LIST = REQUEST_STATISTICS_LIST + 1;

    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private TextView mTotalIncome;
    private StoreStatisticsMessage mStoreStatisticsMessage;
    private String mStartTime;
    private String mEndTime;

    private PullToRefreshListView mStatisticsListView;
    private StatisticsAdapter mStatisticsAdapter;
    private List<StatisticsInfo> mList = new ArrayList<StatisticsInfo>();
    private RequestJob mJob;
    private int mPageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_statistics_content);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mTotalIncome = (TextView) findViewById(R.id.total_income);
        mStatisticsListView = (PullToRefreshListView) findViewById(R.id.statistics_list);
        mStatisticsAdapter = new StatisticsAdapter(this);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mStatisticsListView.setOnRefreshListener(this);
    }

    private void initData() {
        mTotalIncome.setText(SharedPreferencesUtil.readString(SysConstants.TOTAL_MONEY, "0.00"));
        Intent intent = getIntent();
        mStoreStatisticsMessage = (StoreStatisticsMessage) intent
                .getSerializableExtra("StoreStatisticsMessage");
        mStartTime = intent.getStringExtra("startTime");
        mEndTime = intent.getStringExtra("endTime");
        mTxtTitle.setText(mStoreStatisticsMessage.getName());

        mStatisticsListView.setAdapter(mStatisticsAdapter);
        mStatisticsListView.setMode(Mode.BOTH);

        doGetStatistics(REQUEST_STATISTICS_LIST);
    }

    private void doGetStatistics(int requestId) {
        if (mJob != null && mJob.getRequestTask().getStatus() == AsyncTask.Status.RUNNING) {
            mJob.cancelRequest();
            mStatisticsListView.onRefreshComplete();
        }

        if (REQUEST_STATISTICS_LIST == requestId) {
            mPageCount = 1;

        } else if (REQUEST_STATISTICS_NEXT_LIST == requestId) {
            mPageCount++;
        }

        getStatistics(requestId, String.valueOf(mPageCount));
    }

    private void getStatistics(int requestId, String pageNum) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "getStatistics");
            jsonObject.put("id", mStoreStatisticsMessage.getId());
            jsonObject.put("startTime", mStartTime);
            jsonObject.put("endTime", mEndTime);
            jsonObject.put("currentPage", pageNum);
            jsonObject.put("pageSize", "10");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new StatisticsInfoMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.setRequestId(requestId);
            job.doRequest();
        }
    }

    @Override
    public void onStartRequest(RequestJob job) {
        mJob = job;
    }

    @Override
    public void onSuccess(RequestJob job) {
        StatisticsInfoMessage message = (StatisticsInfoMessage) job.getBaseType();
        String code = message.getCode();

        if ("10000".equals(code)) {
            String token = message.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            if (REQUEST_STATISTICS_LIST == job.getRequestId()) {
                mList.clear();
            }
            mList.addAll(message.getStatisticsInfo());

            if (mList.size() > 0) {
                mStatisticsAdapter.setList(mList);
                mStatisticsAdapter.notifyDataSetChanged();
            }
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_STATISTICS_LIST);
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
            if (requestCode == SysConstants.GET_STATISTICS_LIST) {
                doGetStatistics(REQUEST_STATISTICS_LIST);
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

            default:
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
            doGetStatistics(REQUEST_STATISTICS_LIST);
        } else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
            doGetStatistics(REQUEST_STATISTICS_NEXT_LIST);
        }
    }

}

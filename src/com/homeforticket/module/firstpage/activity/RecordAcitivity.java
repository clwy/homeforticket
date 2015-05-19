package com.homeforticket.module.firstpage.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase;
import com.homeforticket.framework.pullrefrash.PullToRefreshListView;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase.Mode;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase.OnRefreshListener;
import com.homeforticket.module.buyticket.adapter.BuyticketAdapter;
import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.buyticket.parser.BuyticketInfoParser;
import com.homeforticket.module.firstpage.adapter.RecordAdapter;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.module.firstpage.model.RecordInfoMessage;
import com.homeforticket.module.firstpage.parser.RecordInfoMessageParser;
import com.homeforticket.module.firstpage.parser.RecordInfoParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;


public class RecordAcitivity extends BaseActivity implements OnClickListener, RequestListener, OnRefreshListener<ListView> {
    private static final int REQUEST_RECORDS_LIST = 0;
    private static final int REQUEST_RECORDS_NEXT_LIST = REQUEST_RECORDS_LIST + 1;
    
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private PullToRefreshListView mRecordListView;
    private RecordAdapter mRecordAdapter;
    private List<RecordInfo> mRecordInfos = new ArrayList<RecordInfo>();
    private RequestJob mJob;
    private int mPageCount;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        initView();
        initListener();
        initData();

    }

    private void initData() {
        mTxtTitle.setText(R.string.record_title);
        mRecordListView.setAdapter(mRecordAdapter);
        mRecordListView.setMode(Mode.BOTH);
        mRecordListView.setOnRefreshListener(this);
        doGetRecordListRequest(REQUEST_RECORDS_LIST);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mRecordListView = (PullToRefreshListView) findViewById(R.id.record_list);
        mRecordAdapter = new RecordAdapter(this);
    }
    
    private void doGetRecordListRequest(int requestId) {
        if (mJob != null && mJob.getRequestTask().getStatus() == AsyncTask.Status.RUNNING) {
            mJob.cancelRequest();
            mRecordListView.onRefreshComplete();
        }
        
        if (REQUEST_RECORDS_LIST == requestId) {
            mPageCount = 1;
        } else if (REQUEST_RECORDS_NEXT_LIST == requestId) {
            mPageCount++;
        }
        
        getRecordListRequest(requestId, String.valueOf(mPageCount));
    }
    
    private void getRecordListRequest(int requestId, String pageNum) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "queryFlows");
            jsonObject.put("currentPage", pageNum);
            jsonObject.put("pageSize", "10");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new RecordInfoMessageParser(), SysConstants.REQUEST_POST);
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
        RecordInfoMessage recordInfoMessage = (RecordInfoMessage) job.getBaseType();
        String code = recordInfoMessage.getCode();
        if ("10000".equals(code)) {
            String token = recordInfoMessage.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }
            
            if (REQUEST_RECORDS_LIST == job.getRequestId()) {
                mRecordInfos.clear();
            }
            
            mRecordInfos.addAll(recordInfoMessage.getRecordInfos());
            
            if (mRecordInfos.size() > 0) {
                mRecordAdapter.setList(mRecordInfos);
                mRecordAdapter.notifyDataSetChanged();
            }
            
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_RECORD_CODE);
            } 
                
            ToastUtil.showToast(recordInfoMessage.getMessage());
        }
        
        mRecordListView.onRefreshComplete();
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.left_top_button:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
            doGetRecordListRequest(REQUEST_RECORDS_LIST);
        } else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
            doGetRecordListRequest(REQUEST_RECORDS_NEXT_LIST);
        }        
    }
    
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.GET_RECORD_CODE) {
                doGetRecordListRequest(REQUEST_RECORDS_LIST);
            } 
        }
        super.onActivityResult(requestCode, responseCode, data);
    }

}

package com.homeforticket.module.message.activity;

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
import com.homeforticket.module.message.adapter.SystemMessageAdapter;
import com.homeforticket.module.message.model.SystemInfo;
import com.homeforticket.module.message.model.SystemInfoMessage;
import com.homeforticket.module.message.parser.SystemInfoMessageParser;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;


public class SystemMessageAcitivity extends BaseActivity implements OnClickListener, RequestListener, OnRefreshListener<ListView> {
    private static final int REQUEST_MESSAGE_LIST = 0;
    private static final int REQUEST_MESSAGE_NEXT_LIST = REQUEST_MESSAGE_LIST + 1;
    
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private PullToRefreshListView mMessageListView;
    private SystemMessageAdapter mAdapter;
    private List<SystemInfo> mInfos = new ArrayList<SystemInfo>();
    private RequestJob mJob;
    private int mPageCount;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);

        initView();
        initListener();
        initData();

    }

    private void initData() {
        mTxtTitle.setText(R.string.system_message_string);
        mMessageListView.setAdapter(mAdapter);
        mMessageListView.setMode(Mode.BOTH);
        mMessageListView.setOnRefreshListener(this);
        doGetSystemMessageListRequest(REQUEST_MESSAGE_LIST);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mMessageListView = (PullToRefreshListView) findViewById(R.id.record_list);
        mAdapter = new SystemMessageAdapter(this);
    }
    
    private void doGetSystemMessageListRequest(int requestId) {
        if (mJob != null && mJob.getRequestTask().getStatus() == AsyncTask.Status.RUNNING) {
            mJob.cancelRequest();
            mMessageListView.onRefreshComplete();
        }
        
        if (REQUEST_MESSAGE_LIST == requestId) {
            mPageCount = 1;
        } else if (REQUEST_MESSAGE_NEXT_LIST == requestId) {
            mPageCount++;
        }
        
        getRecordListRequest(requestId, String.valueOf(mPageCount));
    }
    
    private void getRecordListRequest(int requestId, String pageNum) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "queryMessages");
            jsonObject.put("currentPage", pageNum);
            jsonObject.put("pageSize", "10");
            jsonObject.put("type", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new SystemInfoMessageParser(), SysConstants.REQUEST_POST);
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
        SystemInfoMessage message = (SystemInfoMessage) job.getBaseType();
        String code = message.getCode();
        if ("10000".equals(code)) {
            String token = message.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }
            
            if (REQUEST_MESSAGE_LIST == job.getRequestId()) {
                mInfos.clear();
            }
            
            mInfos.addAll(message.getSystemInfos());
            
            if (mInfos.size() > 0) {
                mAdapter.setList(mInfos);
                mAdapter.notifyDataSetChanged();
            }
            
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_SYSTEM_MESSAGE);
            }
        }
        
        ToastUtil.showToast(message.getMessage());
        mMessageListView.onRefreshComplete();
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
            doGetSystemMessageListRequest(REQUEST_MESSAGE_LIST);
        } else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
            doGetSystemMessageListRequest(REQUEST_MESSAGE_NEXT_LIST);
        }        
    }
    
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.GET_RECORD_CODE) {
                doGetSystemMessageListRequest(REQUEST_MESSAGE_LIST);
            } 
        }
        super.onActivityResult(requestCode, responseCode, data);
    }

}

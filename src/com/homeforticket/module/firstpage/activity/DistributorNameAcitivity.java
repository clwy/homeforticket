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
import com.homeforticket.module.firstpage.adapter.DistributorMatchingAdapter;
import com.homeforticket.module.firstpage.adapter.DistributorNameAdapter;
import com.homeforticket.module.firstpage.adapter.RecordAdapter;
import com.homeforticket.module.firstpage.model.DistributorInfo;
import com.homeforticket.module.firstpage.model.DistributorInfoMessage;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.module.firstpage.model.RecordInfoMessage;
import com.homeforticket.module.firstpage.parser.DistributorInfoMessageParser;
import com.homeforticket.module.firstpage.parser.RecordInfoMessageParser;
import com.homeforticket.module.firstpage.parser.RecordInfoParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;


public class DistributorNameAcitivity extends BaseActivity implements OnClickListener, RequestListener, OnRefreshListener<ListView> {
    private static final int REQUEST_DISTRIBUTOR_LIST = 0;
    private static final int REQUEST_DISTRIBUTOR_NEXT_LIST = REQUEST_DISTRIBUTOR_LIST + 1;
    private static final int REQUEST_CHANGE_STATUS = REQUEST_DISTRIBUTOR_NEXT_LIST + 1;
    
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private PullToRefreshListView mDistributorListView;
    private DistributorNameAdapter mAdapter;
    private List<DistributorInfo> mInfos = new ArrayList<DistributorInfo>();
    private RequestJob mJob;
    private int mPageCount;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution_list);

        initView();
        initListener();
        initData();

    }

    private void initData() {
        mTxtTitle.setText(R.string.distributor_name_title);
        mDistributorListView.setAdapter(mAdapter);
        mDistributorListView.setMode(Mode.BOTH);
        mDistributorListView.setOnRefreshListener(this);
        doGetDistributorListRequest(REQUEST_DISTRIBUTOR_LIST);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mDistributorListView = (PullToRefreshListView) findViewById(R.id.distributor_list);
        mAdapter = new DistributorNameAdapter(this);
    }
    
    private void doGetDistributorListRequest(int requestId) {
        if (mJob != null && mJob.getRequestTask().getStatus() == AsyncTask.Status.RUNNING) {
            mJob.cancelRequest();
            mDistributorListView.onRefreshComplete();
        }
        
        if (REQUEST_DISTRIBUTOR_LIST == requestId) {
            mPageCount = 1;
        } else if (REQUEST_DISTRIBUTOR_NEXT_LIST == requestId) {
            mPageCount++;
        }
        
        getDistributorListRequest(requestId, String.valueOf(mPageCount));
    }
    
    private void getDistributorListRequest(int requestId, String pageNum) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "resellerList");
            jsonObject.put("type", "2");
            jsonObject.put("currentPage", pageNum);
            jsonObject.put("pageSize", "10");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new DistributorInfoMessageParser(), SysConstants.REQUEST_POST);
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
        DistributorInfoMessage message = (DistributorInfoMessage) job.getBaseType();
        String code = message.getCode();
        if ("10000".equals(code)) {
            String token = message.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }
            
            if (REQUEST_DISTRIBUTOR_LIST == job.getRequestId()) {
                mInfos.clear();
            }
            
            mInfos.addAll(message.getInfos());
            
            if (mInfos.size() > 0) {
                mAdapter.setList(mInfos);
                mAdapter.notifyDataSetChanged();
            }
            
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_DISTRIBUTOR);
            } 
                
            ToastUtil.showToast(message.getMessage());
        }
        
        mDistributorListView.onRefreshComplete();
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
            doGetDistributorListRequest(REQUEST_DISTRIBUTOR_LIST);
        } else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
            doGetDistributorListRequest(REQUEST_DISTRIBUTOR_NEXT_LIST);
        }        
    }
    
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.GET_DISTRIBUTOR) {
                doGetDistributorListRequest(REQUEST_DISTRIBUTOR_LIST);
            } 
        }
        super.onActivityResult(requestCode, responseCode, data);
    }

}

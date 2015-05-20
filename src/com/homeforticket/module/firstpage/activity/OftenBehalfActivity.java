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
import com.homeforticket.module.firstpage.adapter.OftenBehalfAdapter;
import com.homeforticket.module.firstpage.model.BehalfInfo;
import com.homeforticket.module.firstpage.model.BehalfMessage;
import com.homeforticket.module.firstpage.parser.BehalfMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

/**
 * @Title: OftenBehalfActivity.java 
 * @Package com.homeforticket.module.firstpage.activity 
 * @Description: TODO 
 * @author LR   
 * @date 2015年5月6日 上午11:17:18 
 */
public class OftenBehalfActivity extends BaseActivity implements OnClickListener, RequestListener, OnRefreshListener<ListView> {
    private static final int REQUEST_OFTENBEHALF_LIST = 0;
    private static final int REQUEST_OFTENBEHALF_NEXT_LIST = REQUEST_OFTENBEHALF_LIST + 1;
    
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private Button mAddButton;
    private PullToRefreshListView mBehalfListView;
    private OftenBehalfAdapter mBehalfAdapter;
    private List<BehalfInfo> mBehalfInfoList = new ArrayList<BehalfInfo>();
    private RequestJob mJob;
    private int mPageCount;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_often_behalf);

        initView();
        initListener();
        initData();

    }
    
    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mAddButton = (Button) findViewById(R.id.right_button);
        mAddButton.setVisibility(View.VISIBLE);
        mBehalfAdapter = new OftenBehalfAdapter(this);
        mBehalfListView = (PullToRefreshListView) findViewById(R.id.behalf_list);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mAddButton.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.often_behalf_title);
        mAddButton.setText(R.string.add_product);
        mBehalfListView.setAdapter(mBehalfAdapter);
        mBehalfListView.setMode(Mode.BOTH);
        mBehalfListView.setOnRefreshListener(this);
        
        doGetOftenBehalfList(REQUEST_OFTENBEHALF_LIST);
    }

    private void doGetOftenBehalfList(int requestId) {
        if (mJob != null && mJob.getRequestTask().getStatus() == AsyncTask.Status.RUNNING) {
            mJob.cancelRequest();
            mBehalfListView.onRefreshComplete();
        }
        
        if (REQUEST_OFTENBEHALF_LIST == requestId) {
            mPageCount = 1;
            
        } else if (REQUEST_OFTENBEHALF_NEXT_LIST == requestId) {
            mPageCount++;
        }
        
        getOftenBehalfRequest(requestId, String.valueOf(mPageCount));        
    }

    private void getOftenBehalfRequest(int requestId, String pageNum) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "queryScenes");
            jsonObject.put("currentPage", pageNum);
            jsonObject.put("pageSize", "10");
            jsonObject.put("isCurrent", "1");
            jsonObject.put("productType", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new BehalfMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.setRequestId(requestId);
            job.doRequest();
        }         
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
            doGetOftenBehalfList(REQUEST_OFTENBEHALF_LIST);
        } else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
            doGetOftenBehalfList(REQUEST_OFTENBEHALF_NEXT_LIST);
        }
    }

    @Override
    public void onStartRequest(RequestJob job) {
        mJob = job;
    }

    @Override
    public void onSuccess(RequestJob job) {
        BehalfMessage behalfMessage = (BehalfMessage) job.getBaseType();
        String code = behalfMessage.getCode();
        if ("10000".equals(code)) {
            String token = behalfMessage.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }
            
            if (REQUEST_OFTENBEHALF_LIST == job.getRequestId()) {
                mBehalfInfoList.clear();
            }
            
            mBehalfInfoList.addAll(behalfMessage.getBehalfInfos());
            
            if (mBehalfInfoList.size() > 0) {
                mBehalfAdapter.setProductType("1");
                mBehalfAdapter.setList(mBehalfInfoList);
                mBehalfAdapter.notifyDataSetChanged();
            }
            
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.BUY_TICKET_CODE);
            } 
                
            ToastUtil.showToast(behalfMessage.getMessage());
        }
        
        mBehalfListView.onRefreshComplete();
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
            case R.id.right_button:
                startActivity(new Intent(this, AddProductActivity.class));
                break;
        }
    }

}

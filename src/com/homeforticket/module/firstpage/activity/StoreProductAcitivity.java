
package com.homeforticket.module.firstpage.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase;
import com.homeforticket.framework.pullrefrash.PullToRefreshListView;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase.Mode;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase.OnRefreshListener;
import com.homeforticket.module.firstpage.adapter.StoreAdapter;
import com.homeforticket.module.firstpage.model.StoreInfo;
import com.homeforticket.module.firstpage.model.StoreInfoMessage;
import com.homeforticket.module.firstpage.model.WalletMessage;
import com.homeforticket.module.firstpage.parser.RecordInfoMessageParser;
import com.homeforticket.module.firstpage.parser.ResellerMessageParser;
import com.homeforticket.module.firstpage.parser.StoreInfoMessageParser;
import com.homeforticket.module.firstpage.parser.WalletMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.CircleTransform;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

public class StoreProductAcitivity extends BaseActivity implements OnRefreshListener<ListView>,
        OnClickListener, RequestListener {
    private static final int REQUEST_STORE_LIST = 0;
    private static final int REQUEST_STORE_NEXT_LIST = REQUEST_STORE_LIST + 1;

    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private RelativeLayout mNewLayout;
    private RelativeLayout mCommissionLayout;
    private TextView mNewText;
    private ImageView mNewBottomLine;
    private TextView mCommissionText;
    private ImageView mCommissionBottomLine;
    private ImageView mUserHeadImg;
    private TextView mUserName;
    private TextView mStoreSetting;
    private TextView mStoreShare;

    private PullToRefreshListView mStoreListView;
    private StoreAdapter mAdapter;

    private RequestJob mJob;
    private int mPageCount;
    private List<StoreInfo> mInfos = new ArrayList<StoreInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manage);

        initView();
        initListener();
        initData();

    }

    private void initData() {
        mTxtTitle.setText(R.string.store_manage_title);
        mStoreListView.setAdapter(mAdapter);
        mStoreListView.setMode(Mode.BOTH);
        mStoreListView.setOnRefreshListener(this);
        Glide.with(this).load(SharedPreferencesUtil.readString(SysConstants.USER_PHOTO, ""))
                .transform(new CircleTransform(this)).into(mUserHeadImg);
        mUserName.setText(SharedPreferencesUtil.readString(SysConstants.USERNAME, "")
                + getString(R.string.store_end));
        doGetStoreRequest(REQUEST_STORE_LIST);
    }

    private void doGetStoreRequest(int requestId) {
        if (mJob != null && mJob.getRequestTask().getStatus() == AsyncTask.Status.RUNNING) {
            mJob.cancelRequest();
            mStoreListView.onRefreshComplete();
        }

        if (REQUEST_STORE_LIST == requestId) {
            mPageCount = 1;
        } else if (REQUEST_STORE_NEXT_LIST == requestId) {
            mPageCount++;
        }

        getStoreListRequest(requestId, String.valueOf(mPageCount));
    }

    private void getStoreListRequest(int requestId, String pageNum) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "getProduct");
            jsonObject.put("currentPage", pageNum);
            jsonObject.put("pageSize", "10");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new StoreInfoMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.setRequestId(requestId);
            job.doRequest();
        }
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mNewLayout.setOnClickListener(this);
        mCommissionLayout.setOnClickListener(this);
        mStoreSetting.setOnClickListener(this);
        mStoreShare.setOnClickListener(this);
    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mNewLayout = (RelativeLayout) findViewById(R.id.new_layout);
        mCommissionLayout = (RelativeLayout) findViewById(R.id.commission_layout);
        mNewText = (TextView) findViewById(R.id.new_text);
        mNewBottomLine = (ImageView) findViewById(R.id.new_bottom_line);
        mCommissionText = (TextView) findViewById(R.id.commission_text);
        mCommissionBottomLine = (ImageView) findViewById(R.id.commission_bottom_line);
        mUserHeadImg = (ImageView) findViewById(R.id.user_head_img);
        mUserName = (TextView) findViewById(R.id.user_name);
        mStoreSetting = (TextView) findViewById(R.id.store_setting);
        mStoreShare = (TextView) findViewById(R.id.store_share);
        mStoreListView = (PullToRefreshListView) findViewById(R.id.store_list);
        mAdapter = new StoreAdapter(this);
    }

    @Override
    public void onStartRequest(RequestJob job) {
        mJob = job;
    }

    @Override
    public void onSuccess(RequestJob job) {
        StoreInfoMessage message = (StoreInfoMessage) job.getBaseType();
        String code = message.getCode();

        if ("10000".equals(code)) {
            String token = message.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }
            
            if (REQUEST_STORE_LIST == job.getRequestId()) {
                mInfos.clear();
            }
            
            mInfos.addAll(message.getStoreInfos());
            
            if (mInfos.size() > 0) {
                mAdapter.setList(mInfos);
                mAdapter.notifyDataSetChanged();
            }

        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_STORE_CODE);
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
            if (requestCode == SysConstants.GET_STORE_CODE) {
                doGetStoreRequest(REQUEST_STORE_LIST);
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
            case R.id.new_layout:
                mNewText.setTextColor(Color.parseColor("#1db2f6"));
                mNewBottomLine.setVisibility(View.VISIBLE);
                mCommissionText.setTextColor(Color.parseColor("#979797"));
                mCommissionBottomLine.setVisibility(View.INVISIBLE);
                mAdapter.setIsNew(true);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.commission_layout:
                mNewText.setTextColor(Color.parseColor("#979797"));
                mNewBottomLine.setVisibility(View.INVISIBLE);
                mCommissionText.setTextColor(Color.parseColor("#1db2f6"));
                mCommissionBottomLine.setVisibility(View.VISIBLE);
                mAdapter.setIsNew(false);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.store_setting:
                startActivity(new Intent(this, StoreSettingActivity.class));
                break;
            case R.id.store_share:
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
            doGetStoreRequest(REQUEST_STORE_LIST);
        } else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
            doGetStoreRequest(REQUEST_STORE_NEXT_LIST);
        }
    }

}

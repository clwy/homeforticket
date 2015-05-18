
package com.homeforticket.module.firstpage.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.buyticket.parser.BuyticketInfoParser;
import com.homeforticket.module.firstpage.adapter.ClientAdapter;
import com.homeforticket.module.firstpage.model.ClientInfo;
import com.homeforticket.module.firstpage.model.ClientInfoMessage;
import com.homeforticket.module.firstpage.parser.ClientInfoMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

public class ClientManageActivity extends BaseActivity implements OnRefreshListener<ListView>, OnClickListener, RequestListener {
    private static final int REQUEST_CLIENT_LIST = 0;
    private static final int REQUEST_CLIENT_NEXT_LIST = REQUEST_CLIENT_LIST + 1;
    
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private EditText mEditText;
    private TextView mSearchHintText;
    private PullToRefreshListView mClientListView;
    private ClientAdapter mClientAdapter;
    private List<ClientInfo> mList = new ArrayList<ClientInfo>();
    private RequestJob mJob;
    private int mPageCount;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_manage);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mEditText = (EditText) findViewById(R.id.search_client);
        mSearchHintText = (TextView) findViewById(R.id.search_text);
        mClientListView = (PullToRefreshListView) findViewById(R.id.client_list);
        mClientAdapter = new ClientAdapter(this);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mEditText.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                if (mEditText.length() > 0) {
                    mSearchHintText.setVisibility(View.GONE);
                } else {
                    mSearchHintText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initData() {
        mTxtTitle.setText(R.string.consumer_manage_title);
        mClientListView.setAdapter(mClientAdapter);
        mClientListView.setMode(Mode.BOTH);
        mClientListView.setOnRefreshListener(this);
        
        doQueryCustomersRequest(REQUEST_CLIENT_LIST, mEditText.getText().toString().trim());
    }

    private void doQueryCustomersRequest(int requestId, String searchText) {
        if (mJob != null && mJob.getRequestTask().getStatus() == AsyncTask.Status.RUNNING) {
            mJob.cancelRequest();
            mClientListView.onRefreshComplete();
        }
        
        if (REQUEST_CLIENT_LIST == requestId) {
            mPageCount = 1;
            
        } else if (REQUEST_CLIENT_NEXT_LIST == requestId) {
            mPageCount++;
        }
        
        queryCustomersRequest(requestId, String.valueOf(mPageCount), searchText);        
    }

    private void queryCustomersRequest(int requestId, String pageNum, String searchText) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "queryCustomers");
            jsonObject.put("currentPage", pageNum);
            jsonObject.put("pageSize", "10");
            if (!TextUtils.isEmpty(searchText)) {
                jsonObject.put("text", searchText);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new ClientInfoMessageParser(), SysConstants.REQUEST_POST);
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
        ClientInfoMessage clientInfoMessage = (ClientInfoMessage) job.getBaseType();
        String code = clientInfoMessage.getCode();
        if ("10000".equals(code)) {
            String token = clientInfoMessage.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }
            
            if (REQUEST_CLIENT_LIST == job.getRequestId()) {
                mList.clear();
            }
            
            mList.addAll(clientInfoMessage.getClientInfos());
            
            if (mList.size() > 0) {
                mClientAdapter.setList(mList);
                mClientAdapter.notifyDataSetChanged();
            }
            
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_CUSTOMER_CODE);
            } else {
                ToastUtil.showToast(clientInfoMessage.getMessage());
            }
        }
        
        mClientListView.onRefreshComplete();
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }
    
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.GET_CUSTOMER_CODE) {
                doQueryCustomersRequest(REQUEST_CLIENT_LIST, mEditText.getText().toString().trim());
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
            doQueryCustomersRequest(REQUEST_CLIENT_LIST, mEditText.getText().toString().trim());
        } else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
            doQueryCustomersRequest(REQUEST_CLIENT_NEXT_LIST, mEditText.getText().toString().trim());
        }        
    }

}

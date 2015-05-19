
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
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
import com.homeforticket.module.firstpage.adapter.HorizontalGridAdapter;
import com.homeforticket.module.firstpage.adapter.OrderAdapter;
import com.homeforticket.module.firstpage.model.OrderInfo;
import com.homeforticket.module.firstpage.model.OrderInfoMessage;
import com.homeforticket.module.firstpage.model.OrderStatusInfo;
import com.homeforticket.module.firstpage.model.OrderStatusMessage;
import com.homeforticket.module.firstpage.parser.OrderInfoMessageParser;
import com.homeforticket.module.firstpage.parser.OrderStatusMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

public class OrderManageActivity extends BaseActivity implements OnRefreshListener<ListView>, OnClickListener, RequestListener, OnItemClickListener {
    private static final int REQUEST_ORDER_LIST = 0;
    private static final int REQUEST_ORDER_NEXT_LIST = REQUEST_ORDER_LIST + 1;
    private static final int REQUEST_ORDER_STATUS = REQUEST_ORDER_NEXT_LIST + 1;
    
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private EditText mEditText;
    private TextView mSearchHintText;
    private PullToRefreshListView mOrderListView;
    private OrderAdapter mOrderAdapter;
    private List<OrderInfo> mList = new ArrayList<OrderInfo>();
    private RequestJob mJob;
    private int mPageCount;
    private int pos = 0;
    
    private GridView mStstusListView;
    private HorizontalGridAdapter mStutasAdapter;
    private List<OrderStatusInfo> mStatusList = new ArrayList<OrderStatusInfo>();
    private String mOrderType;
    
    private String mId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manage);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mEditText = (EditText) findViewById(R.id.search_order);
        mSearchHintText = (TextView) findViewById(R.id.search_text);
        mOrderListView = (PullToRefreshListView) findViewById(R.id.order_list);
        mOrderAdapter = new OrderAdapter(this);
        
        mStstusListView = (GridView) findViewById(R.id.horizontal_grid);
        mStutasAdapter = new HorizontalGridAdapter(this);
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
        mStstusListView.setOnItemClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        mTxtTitle.setText(R.string.order_manage_title);
        mOrderListView.setAdapter(mOrderAdapter);
        mOrderListView.setMode(Mode.BOTH);
        mOrderListView.setOnRefreshListener(this);
        
        mStstusListView.setAdapter(mStutasAdapter);
        
        getOrderStatusRequest();
    }

    private void doQueryCustomersRequest(int requestId, String searchText, String state) {
        if (mJob != null && mJob.getRequestTask().getStatus() == AsyncTask.Status.RUNNING) {
            mJob.cancelRequest();
            mOrderListView.onRefreshComplete();
        }
        
        if (REQUEST_ORDER_LIST == requestId) {
            mPageCount = 1;
            
        } else if (REQUEST_ORDER_NEXT_LIST == requestId) {
            mPageCount++;
        }
        
        queryCustomersRequest(requestId, String.valueOf(mPageCount), searchText, state);        
    }

    private void queryCustomersRequest(int requestId, String pageNum, String searchText, String state) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "queryOrders");
            jsonObject.put("currentPage", pageNum);
            jsonObject.put("pageSize", "10");
            jsonObject.put("orderState", state);
            if (!TextUtils.isEmpty(searchText)) {
                jsonObject.put("orderId", searchText);
            } 
            if (!TextUtils.isEmpty(mId)) {
                jsonObject.put("resellerId", mId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new OrderInfoMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.setRequestId(requestId);
            job.doRequest();
        }  
    }
    
    private void getOrderStatusRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "queryOrderStatus");
            jsonObject.put("currentPage", 1);
            jsonObject.put("pageSize", "20");
            if (!TextUtils.isEmpty(mId)) {
                jsonObject.put("resellerId", mId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new OrderStatusMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.setRequestId(REQUEST_ORDER_STATUS);
            job.doRequest();
        } 
    }

    @Override
    public void onStartRequest(RequestJob job) {
        mJob = job;
    }

    @Override
    public void onSuccess(RequestJob job) {
        switch (job.getRequestId()) {
            case REQUEST_ORDER_LIST:
            case REQUEST_ORDER_NEXT_LIST:
                dealOrderInfoMessage(job);
                break;

            case REQUEST_ORDER_STATUS:
                dealOrderStatusInfo(job);
                break;
            default:
                break;
        }

        mOrderListView.onRefreshComplete();
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }
    
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.GET_ORDER_CODE) {
                if (mStatusList != null && mStatusList.size() > 0) {
                    doQueryCustomersRequest(REQUEST_ORDER_LIST, mEditText.getText().toString().trim(), mStatusList.get(pos).getId());
                } else {
                    mOrderListView.onRefreshComplete();
                }
                
            } else if (requestCode == SysConstants.GET_ORDER_STATUS) {
                getOrderStatusRequest();
            }
        }
        super.onActivityResult(requestCode, responseCode, data);
    }
    
    private void dealOrderInfoMessage(RequestJob job) {
        OrderInfoMessage orderInfoMessage = (OrderInfoMessage) job.getBaseType();
        String code = orderInfoMessage.getCode();
        if ("10000".equals(code)) {
            String token = orderInfoMessage.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }
            
            if (REQUEST_ORDER_LIST == job.getRequestId()) {
                mList.clear();
            }
            mList.addAll(orderInfoMessage.getInfos());
            
            if (mList.size() > 0) {
                mOrderAdapter.setList(mList);
                mOrderAdapter.setType(mOrderType);
                mOrderAdapter.notifyDataSetChanged();
                mOrderListView.setVisibility(View.VISIBLE);
            } else {
                mOrderListView.setVisibility(View.INVISIBLE);
            }
            
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_ORDER_CODE);
            }
             
            ToastUtil.showToast(orderInfoMessage.getMessage());
        }
    }
    
    private void dealOrderStatusInfo(RequestJob job) {
        OrderStatusMessage orderStatusMessage = (OrderStatusMessage) job.getBaseType();
        String code = orderStatusMessage.getCode();
        if ("10000".equals(code)) {
            String token = orderStatusMessage.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }
            
            mStatusList.addAll(orderStatusMessage.getOrderStatusInfo());
            mStstusListView.setLayoutParams(new FrameLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.order_status_width)
                    * mStatusList.size(), getResources().getDimensionPixelSize(R.dimen.order_status_height)));

            mStstusListView.setColumnWidth(getResources().getDimensionPixelSize(R.dimen.order_status_width));
            mStstusListView.setNumColumns(mStatusList.size());
            mStutasAdapter.notifyDataSetChanged();
            
            if (mStatusList.size() > 0) {
                mStutasAdapter.setDataSet(mStatusList);
                mStutasAdapter.notifyDataSetChanged();
                mOrderType = mStatusList.get(pos).getName();
                doQueryCustomersRequest(REQUEST_ORDER_LIST, mEditText.getText().toString().trim(), mStatusList.get(pos).getId());
            }
            
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_ORDER_STATUS);
            } 
                
            ToastUtil.showToast(orderStatusMessage.getMessage());
        }
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
            if (mStatusList != null && mStatusList.size() > 0) {
                mOrderType = mStatusList.get(pos).getName();
                doQueryCustomersRequest(REQUEST_ORDER_LIST, mEditText.getText().toString().trim(), mStatusList.get(pos).getId());
            } else {
                mOrderListView.onRefreshComplete();
            }
        } else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
            if (mStatusList != null && mStatusList.size() > 0) {
                mOrderType = mStatusList.get(pos).getName();
                doQueryCustomersRequest(REQUEST_ORDER_NEXT_LIST, mEditText.getText().toString().trim(), mStatusList.get(pos).getId());
            } else {
                mOrderListView.onRefreshComplete();
            }
        }        
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mStutasAdapter.setSelectedIndex(position);
        mStutasAdapter.notifyDataSetChanged();
        pos = position;
        mOrderType = mStatusList.get(pos).getName();
        doQueryCustomersRequest(REQUEST_ORDER_LIST, mEditText.getText().toString().trim(), mStatusList.get(position).getId());
        
    }

}

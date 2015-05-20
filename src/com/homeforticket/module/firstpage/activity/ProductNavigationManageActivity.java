
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
import com.homeforticket.module.firstpage.adapter.HorizontalProductChannelGridAdapter;
import com.homeforticket.module.firstpage.adapter.OrderAdapter;
import com.homeforticket.module.firstpage.adapter.ProductAdapter;
import com.homeforticket.module.firstpage.model.OrderInfo;
import com.homeforticket.module.firstpage.model.OrderInfoMessage;
import com.homeforticket.module.firstpage.model.OrderStatusInfo;
import com.homeforticket.module.firstpage.model.OrderStatusMessage;
import com.homeforticket.module.firstpage.model.ProductChannelInfo;
import com.homeforticket.module.firstpage.model.ProductChannelMessage;
import com.homeforticket.module.firstpage.model.ProductInfo;
import com.homeforticket.module.firstpage.model.ProductMessage;
import com.homeforticket.module.firstpage.parser.OrderInfoMessageParser;
import com.homeforticket.module.firstpage.parser.OrderStatusMessageParser;
import com.homeforticket.module.firstpage.parser.ProductChannelMessageParser;
import com.homeforticket.module.firstpage.parser.ProductMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

public class ProductNavigationManageActivity extends BaseActivity implements
        OnRefreshListener<ListView>, OnClickListener, RequestListener, OnItemClickListener {
    public static final String TYPE_TICKET = "1";
    public static final String TYPE_HOTEL = "2";
    public static final String TYPE_TRAVEL = "3";
    public static final String TYPE_LOCAL = "4";
    public static final String TYPE_SPECIAL = "5";

    private static final int REQUEST_PRODUCT_LIST = 0;
    private static final int REQUEST_PRODUCT_NEXT_LIST = REQUEST_PRODUCT_LIST + 1;
    private static final int REQUEST_PRODUCT_NAVIGATION = REQUEST_PRODUCT_NEXT_LIST + 1;

    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private EditText mEditText;
    private TextView mSearchHintText;
    private PullToRefreshListView mProductListView;
    private ProductAdapter mProductAdapter;
    private List<ProductInfo> mList = new ArrayList<ProductInfo>();
    private RequestJob mJob;
    private int mPageCount;
    private int pos = 0;

    private GridView mNavigationListView;
    private HorizontalProductChannelGridAdapter mNavigationAdapter;
    private List<ProductChannelInfo> mProductChannelInfoList = new ArrayList<ProductChannelInfo>();
    private String mTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_navigation_manage);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mEditText = (EditText) findViewById(R.id.search_order);
        mSearchHintText = (TextView) findViewById(R.id.search_text);
        mProductListView = (PullToRefreshListView) findViewById(R.id.product_list);
        mProductAdapter = new ProductAdapter(this);

        mNavigationListView = (GridView) findViewById(R.id.horizontal_grid);
        mNavigationAdapter = new HorizontalProductChannelGridAdapter(this);
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
        mNavigationListView.setOnItemClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.product_channel_title);
        mProductListView.setAdapter(mProductAdapter);
        mProductListView.setMode(Mode.BOTH);
        mProductListView.setOnRefreshListener(this);

        mNavigationListView.setAdapter(mNavigationAdapter);

        getProductChannelRequest();
    }

    private void doQueryProductRequest(int requestId, String searchText, String type) {
        if (mJob != null && mJob.getRequestTask().getStatus() == AsyncTask.Status.RUNNING) {
            mJob.cancelRequest();
            mProductListView.onRefreshComplete();
        }

        if (REQUEST_PRODUCT_LIST == requestId) {
            mPageCount = 1;

        } else if (REQUEST_PRODUCT_NEXT_LIST == requestId) {
            mPageCount++;
        }

        queryProductRequest(requestId, String.valueOf(mPageCount), searchText, type);
    }

    private void queryProductRequest(int requestId, String pageNum, String searchText,
            String type) {
        mTypeId = type;
        JSONObject jsonObject = new JSONObject();
        try {
            if (TYPE_HOTEL.equals(mTypeId)) {
                jsonObject.put("method", "hotelList");
            } else if (TYPE_TRAVEL.equals(mTypeId) || TYPE_LOCAL.equals(mTypeId)
                    || TYPE_SPECIAL.equals(mTypeId)) {
                jsonObject.put("method", "getProduct");
                jsonObject.put("type", type);
            } else if (TYPE_TICKET.equals(mTypeId)) {
                jsonObject.put("method", "queryScenes");
                jsonObject.put("isCurrent", "1");
                jsonObject.put("productType", "1");
            }

            jsonObject.put("currentPage", pageNum);
            jsonObject.put("pageSize", "10");
            if (!TextUtils.isEmpty(searchText)) {
                jsonObject.put("searchText", searchText);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new ProductMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.setRequestId(requestId);
            job.doRequest();
        }
    }

    private void getProductChannelRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "queryChannel");
            jsonObject.put("currentPage", 1);
            jsonObject.put("pageSize", "20");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new ProductChannelMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.setRequestId(REQUEST_PRODUCT_NAVIGATION);
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
            case REQUEST_PRODUCT_LIST:
            case REQUEST_PRODUCT_NEXT_LIST:
                dealProductInfoMessage(job);
                break;

            case REQUEST_PRODUCT_NAVIGATION:
                dealProductChannelInfo(job);
                break;
            default:
                break;
        }

        mProductListView.onRefreshComplete();
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.GET_PRODUCT_CODE) {
                if (mProductChannelInfoList != null && mProductChannelInfoList.size() > 0) {
                    doQueryProductRequest(REQUEST_PRODUCT_LIST,
                            mEditText.getText().toString().trim(), mProductChannelInfoList.get(pos)
                                    .getId());
                } else {
                    mProductListView.onRefreshComplete();
                }
            } else if (requestCode == SysConstants.GET_PRODUCT_CHANNEL) {
                getProductChannelRequest();
            }
        }
        super.onActivityResult(requestCode, responseCode, data);
    }

    private void dealProductInfoMessage(RequestJob job) {
        ProductMessage orderInfoMessage = (ProductMessage) job.getBaseType();
        String code = orderInfoMessage.getCode();
        if ("10000".equals(code)) {
            String token = orderInfoMessage.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            if (REQUEST_PRODUCT_LIST == job.getRequestId()) {
                mList.clear();
            }
            mList.addAll(orderInfoMessage.getInfos());

            if (mList.size() > 0) {
                if (!TextUtils.isEmpty(mTypeId)) {
                    mProductAdapter.setType(mTypeId);
                }
                if (TYPE_TICKET.equals(mTypeId)) {
                    mProductAdapter.setProductType("1");
                }
                mProductAdapter.setList(mList);
                mProductAdapter.notifyDataSetChanged();
                mProductListView.setVisibility(View.VISIBLE);
            } else {
                mProductListView.setVisibility(View.INVISIBLE);
            }

        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_PRODUCT_CODE);
            } 
                
            ToastUtil.showToast(orderInfoMessage.getMessage());
        }
    }

    private void dealProductChannelInfo(RequestJob job) {
        ProductChannelMessage message = (ProductChannelMessage) job.getBaseType();
        String code = message.getCode();
        if ("10000".equals(code)) {
            String token = message.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            mProductChannelInfoList.addAll(message.getInfos());
            mNavigationListView.setLayoutParams(new FrameLayout.LayoutParams(getResources()
                    .getDimensionPixelSize(R.dimen.order_status_width)
                    * mProductChannelInfoList.size(), getResources().getDimensionPixelSize(
                    R.dimen.order_status_height)));

            mNavigationListView.setColumnWidth(getResources().getDimensionPixelSize(
                    R.dimen.order_status_width));
            mNavigationListView.setNumColumns(mProductChannelInfoList.size());
            mNavigationAdapter.notifyDataSetChanged();

            if (mProductChannelInfoList.size() > 0) {
                mNavigationAdapter.setDataSet(mProductChannelInfoList);
                mNavigationAdapter.notifyDataSetChanged();
                
                doQueryProductRequest(REQUEST_PRODUCT_LIST, mEditText.getText().toString(), mProductChannelInfoList.get(0).getId());
            }

        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_PRODUCT_CHANNEL);
            } 
                
            ToastUtil.showToast(message.getMessage());
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
            if (mProductChannelInfoList != null && mProductChannelInfoList.size() > 0) {
                doQueryProductRequest(REQUEST_PRODUCT_LIST,
                        mEditText.getText().toString().trim(), mProductChannelInfoList.get(pos)
                                .getId());
            } else {
                mProductListView.onRefreshComplete();
            }
        } else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
            if (mProductChannelInfoList != null && mProductChannelInfoList.size() > 0) {
                doQueryProductRequest(REQUEST_PRODUCT_NEXT_LIST,
                        mEditText.getText().toString().trim(), mProductChannelInfoList.get(pos)
                                .getId());
            } else {
                mProductListView.onRefreshComplete();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mNavigationAdapter.setSelectedIndex(position);
        mNavigationAdapter.notifyDataSetChanged();
        pos = position;

        ProductChannelInfo info = (ProductChannelInfo) mNavigationAdapter.getItem(position);
        doQueryProductRequest(REQUEST_PRODUCT_LIST, mEditText.getText().toString(), info.getId());

    }

}

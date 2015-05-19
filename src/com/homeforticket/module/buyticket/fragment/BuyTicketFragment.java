
package com.homeforticket.module.buyticket.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.homeforticket.R;
import com.homeforticket.R.string;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.fragment.BaseFragment;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase;
import com.homeforticket.framework.pullrefrash.PullToRefreshListView;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase.Mode;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase.OnRefreshListener;
import com.homeforticket.module.buyticket.adapter.BuyticketAdapter;
import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.buyticket.parser.BuyticketInfoParser;
import com.homeforticket.module.firstpage.model.ResellerMessage;
import com.homeforticket.module.firstpage.parser.ResellerMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.module.message.adapter.MessageAdapter;
import com.homeforticket.module.message.model.MessageInfo;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

public class BuyTicketFragment extends BaseFragment implements OnRefreshListener<ListView>, RequestListener {
    private static final int REQUEST_TICKETS_LIST = 0;
    private static final int REQUEST_TICKETS_NEXT_LIST = REQUEST_TICKETS_LIST + 1;
    
    private static BuyTicketFragment sBuyTicketFragment;
    private View mView;
    private TextView mTxtTitle;
    private PullToRefreshListView mBuyticketListView;
    private BuyticketAdapter mBuyticketAdapter;
    private List<TicketInfo> mBuyticketList = new ArrayList<TicketInfo>();
    private RequestJob mJob;
    private int mPageCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView != null) {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }

            return mView;
        }

        mView = inflater.inflate(R.layout.fragment_buyticket, null);
        initView();
        initListener();
        initData();
        return mView;
    }

    private void initData() {
        mTxtTitle.setText(R.string.buyticket_tab);
        mBuyticketListView.setAdapter(mBuyticketAdapter);
        mBuyticketListView.setMode(Mode.BOTH);
        mBuyticketListView.setOnRefreshListener(this);
        doQueryScenesRequest(REQUEST_TICKETS_LIST);
    }

    private void doQueryScenesRequest(int requestId) {
        if (mJob != null && mJob.getRequestTask().getStatus() == AsyncTask.Status.RUNNING) {
            mJob.cancelRequest();
            mBuyticketListView.onRefreshComplete();
        }
        
        if (REQUEST_TICKETS_LIST == requestId) {
            mPageCount = 1;
            
        } else if (REQUEST_TICKETS_NEXT_LIST == requestId) {
            mPageCount++;
        }
        
        queryScenesRequest(requestId, String.valueOf(mPageCount));
    }

    private void queryScenesRequest(int requestId, String pageNum) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "queryScenes");
            jsonObject.put("currentPage", pageNum);
            jsonObject.put("pageSize", "10");
            jsonObject.put("isCurrent", "1");
            jsonObject.put("productType", "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new BuyticketInfoParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.setRequestId(requestId);
            job.doRequest();
        }        
    }

    private void initView() {
        mTxtTitle = (TextView) mView.findViewById(R.id.top_title);
        mView.findViewById(R.id.left_top_button).setVisibility(View.GONE);
        mBuyticketAdapter = new BuyticketAdapter(getActivity());
        mBuyticketListView = (PullToRefreshListView) mView.findViewById(R.id.buyticket_list);
    }

    private void initListener() {
    }

    @Override
    public void initFragment() {
        super.initFragment();
    }

    @Override
    public void resetFragment() {
        super.resetFragment();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static BuyTicketFragment getInstance() {
        if (sBuyTicketFragment == null) {
            sBuyTicketFragment = new BuyTicketFragment();
        }
        return sBuyTicketFragment;
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
            doQueryScenesRequest(REQUEST_TICKETS_LIST);
        } else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
            doQueryScenesRequest(REQUEST_TICKETS_NEXT_LIST);
        }
    }

    @Override
    public void onStartRequest(RequestJob job) {
        mJob = job;
    }

    @Override
    public void onSuccess(RequestJob job) {
        BuyticketInfo buyticketInfo = (BuyticketInfo) job.getBaseType();
        String code = buyticketInfo.getCode();
        if ("10000".equals(code)) {
            String token = buyticketInfo.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }
            
            if (REQUEST_TICKETS_LIST == job.getRequestId()) {
                mBuyticketList.clear();
            }
            
            mBuyticketList.addAll(buyticketInfo.getTicketInfos());
            
            if (mBuyticketList.size() > 0) {
                mBuyticketAdapter.setList(mBuyticketList);
                mBuyticketAdapter.notifyDataSetChanged();
            }
            
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, SysConstants.BUY_TICKET_CODE);
            }
            
            ToastUtil.showToast(buyticketInfo.getMessage());
        }
        
        mBuyticketListView.onRefreshComplete();
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }
    
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.BUY_TICKET_CODE) {
                doQueryScenesRequest(REQUEST_TICKETS_LIST);
            } 
        }
        super.onActivityResult(requestCode, responseCode, data);
    }
}

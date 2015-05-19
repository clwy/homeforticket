
package com.homeforticket.module.message.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.fragment.BaseFragment;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase.Mode;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase.OnRefreshListener;
import com.homeforticket.framework.pullrefrash.PullToRefreshListView;
import com.homeforticket.module.firstpage.parser.ResellerMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.module.message.activity.OrderMessageAcitivity;
import com.homeforticket.module.message.activity.SystemMessageAcitivity;
import com.homeforticket.module.message.adapter.MessageAdapter;
import com.homeforticket.module.message.model.MessageInfo;
import com.homeforticket.module.message.model.SystemInfo;
import com.homeforticket.module.message.model.SystemInfoMessage;
import com.homeforticket.module.message.parser.SystemInfoMessageParser;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

public class MessageFragment extends BaseFragment implements OnClickListener, RequestListener {
    private static MessageFragment sMessageFragment;
    private View mView;
    private TextView mTxtTitle;
    private RelativeLayout mSystemMessageLayout;
    private RelativeLayout mOrderMessageLayout;
    private TextView mSystemNewMessageContent;
    private TextView mSystemNewMessageTime;
    private TextView mOrderNewMessageContent;
    private TextView mOrderNewMessageTime;
    private TextView mSystemMessageCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        new Handler().post(new Runnable() {
            
            @Override
            public void run() {
                getNewMessage();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView != null) {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }

            return mView;
        }

        mView = inflater.inflate(R.layout.fragment_message, null);
        initView();
        initListener();
        initData();
        return mView;
    }

    private void initData() {
        mTxtTitle.setText(R.string.message_tab);
        getNewMessage();
    }

    private void getNewMessage() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "queryMessages");
            jsonObject.put("currentPage", "1");
            jsonObject.put("pageSize", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new SystemInfoMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.doRequest();
        }
    }

    private void initView() {
        mTxtTitle = (TextView) mView.findViewById(R.id.top_title);
        mView.findViewById(R.id.left_top_button).setVisibility(View.GONE);
        mSystemMessageLayout = (RelativeLayout) mView.findViewById(R.id.system_message_layout);
        mOrderMessageLayout = (RelativeLayout) mView.findViewById(R.id.order_message_layout);
        mSystemNewMessageContent = (TextView) mView.findViewById(R.id.system_message_content);
        mSystemNewMessageTime = (TextView) mView.findViewById(R.id.system_message_time);
        mOrderNewMessageContent = (TextView) mView.findViewById(R.id.order_message_content);
        mOrderNewMessageTime = (TextView) mView.findViewById(R.id.order_message_time);
        mSystemMessageCount = (TextView) mView.findViewById(R.id.system_message_count);
        mSystemMessageCount.setVisibility(View.GONE);
    }

    private void initListener() {
        mSystemMessageLayout.setOnClickListener(this);
        mOrderMessageLayout.setOnClickListener(this);
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

    public static MessageFragment getInstance() {
        if (sMessageFragment == null) {
            sMessageFragment = new MessageFragment();
        }
        return sMessageFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.system_message_layout:
                startActivity(new Intent(getActivity(), SystemMessageAcitivity.class));
                break;
            case R.id.order_message_layout:
                startActivity(new Intent(getActivity(), OrderMessageAcitivity.class));
                break;

            default:
                break;
        }
        
    }

    @Override
    public void onStartRequest(RequestJob job) {
        
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
            
            List<SystemInfo> infos = message.getSystemInfos();
            SystemInfo info = infos.get(0);
            if ("0".equals(message.getUnRead())) {
                mSystemMessageCount.setVisibility(View.GONE);
            } else {
                mSystemMessageCount.setText(message.getUnRead());
                mSystemMessageCount.setVisibility(View.VISIBLE);
            }
            mSystemNewMessageContent.setText(info.getContent());
            mSystemNewMessageTime.setText(info.getNewsDate());
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_SYSTEM_MESSAGE);
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
            if (requestCode == SysConstants.GET_RECORD_CODE) {
                getNewMessage();
            } 
        }
        super.onActivityResult(requestCode, responseCode, data);
    }
}

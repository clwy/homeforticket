
package com.homeforticket.module.firstpage.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.framework.pullrefrash.PullToRefreshListView;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase.Mode;
import com.homeforticket.module.firstpage.adapter.MemberAdapter;
import com.homeforticket.module.firstpage.model.OrderContentInfo;
import com.homeforticket.module.firstpage.model.OrderContentMessage;
import com.homeforticket.module.firstpage.model.RecordInfoMessage;
import com.homeforticket.module.firstpage.parser.OrderContentMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

/**
 * @Title: OrderContentActivity.java
 * @Package com.homeforticket.module.firstpage.activity
 * @Description: TODO
 * @author LR
 * @date 2015年5月16日 上午10:12:44
 */
public class OrderContentActivity extends BaseActivity implements OnClickListener, RequestListener {
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private PullToRefreshListView mOrderContentListView;
    private MemberAdapter mMemberAdapter;
    private List<OrderContentInfo> mInfos = new ArrayList<OrderContentInfo>();
    private TextView mOrderName;
    private TextView mOrderType;
    private TextView mOrderTitle;
    private ImageView mOrderImg;
    private TextView mLeaveMessage;
    private TextView mOrderCount;
    private TextView mOrderTotal;
    private TextView mScenicCity;
    private TextView mScenicAddress;
    private TextView mOrderTime;
    private TextView mOrderPayCount;
    private TextView mOrderPayTotal;
    private TextView mOrderIdTextView;

    private String mOrderId;
    private View mHeaderView;
    private View mFooterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_content);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mOrderContentListView = (PullToRefreshListView) findViewById(R.id.member_list);
        LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeaderView = lif.inflate(R.layout.activity_order_content_header, null);
        mFooterView = lif.inflate(R.layout.activity_order_content_footer, null);
        mMemberAdapter = new MemberAdapter(this);
        mOrderName = (TextView) mHeaderView.findViewById(R.id.order_name);
        mOrderType = (TextView) mHeaderView.findViewById(R.id.order_type);
        mOrderTitle = (TextView) mHeaderView.findViewById(R.id.order_title);
        mOrderImg = (ImageView) mHeaderView.findViewById(R.id.order_img);
        mLeaveMessage = (TextView) mHeaderView.findViewById(R.id.order_leave_message);
        mOrderCount = (TextView) mHeaderView.findViewById(R.id.order_count);
        mOrderTotal = (TextView) mHeaderView.findViewById(R.id.order_pay);
        mScenicCity = (TextView) mHeaderView.findViewById(R.id.sence_city) ;
        mScenicAddress = (TextView) mHeaderView.findViewById(R.id.sence_address);
        mOrderTime = (TextView) mFooterView.findViewById(R.id.order_time);
        mOrderIdTextView = (TextView) mFooterView.findViewById(R.id.order_id);
        mOrderPayCount = (TextView) mFooterView.findViewById(R.id.order_count);
        mOrderPayTotal = (TextView) mFooterView.findViewById(R.id.order_total_pay);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.order_info_title);
        
        mOrderContentListView.addHeaderView(mHeaderView);
        mOrderContentListView.addFooterView(mFooterView);
        mOrderContentListView.setAdapter(mMemberAdapter);
        mOrderContentListView.setMode(Mode.DISABLED);

        Intent intent = getIntent();
        mOrderId = intent.getStringExtra("orderId");
        mOrderName.setText(SharedPreferencesUtil.readString(SysConstants.USERNAME, ""));
        mOrderType.setText(intent.getStringExtra("type"));
        Glide.with(this).load(intent.getStringExtra("pic")).centerCrop().into(mOrderImg);
        mOrderIdTextView.setText(mOrderId);

        if (!TextUtils.isEmpty(mOrderId)) {
            getRecordListRequest();
        }
    }

    private void getRecordListRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "queryOrder");
            jsonObject.put("orderId", mOrderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new OrderContentMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.doRequest();
        }
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {
        OrderContentMessage message = (OrderContentMessage) job.getBaseType();
        String code = message.getCode();
        if ("10000".equals(code)) {
            String token = message.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            mInfos = message.getInfos();
            mOrderTitle.setText(message.getSceneName());
            mOrderCount.setText("X" + message.getTotal_num());
            mOrderTotal.setText("￥" + message.getOrderAmount());
            mOrderTime.setText(message.getCreateTime());
            mOrderPayCount.setText(message.getTotal_num());
            mOrderPayTotal.setText(message.getPaidAmount());
            mScenicCity.setText(message.getProvice() + message.getCity() + message.getCounty());
            mScenicAddress.setText(message.getSceneAddress());

            
            if (mInfos.size() > 0) {
                mMemberAdapter.setList(mInfos);
                mMemberAdapter.notifyDataSetChanged();
            }
            
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_RECORD_CODE);
            } 
                
            ToastUtil.showToast(message.getMessage());
        }
        
        mOrderContentListView.onRefreshComplete();
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
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

}

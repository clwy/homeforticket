
package com.homeforticket.module.firstpage.activity;

import java.util.ArrayList;
import java.util.HashSet;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.framework.pullrefrash.PullToRefreshListView;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase.Mode;
import com.homeforticket.module.buyticket.activity.ChoosePayActivity;
import com.homeforticket.module.buyticket.parser.SaveOrderMessageParser;
import com.homeforticket.module.firstpage.adapter.MemberAdapter;
import com.homeforticket.module.firstpage.model.CancelOrderMessage;
import com.homeforticket.module.firstpage.model.OrderContentInfo;
import com.homeforticket.module.firstpage.model.OrderContentMessage;
import com.homeforticket.module.firstpage.model.RecordInfoMessage;
import com.homeforticket.module.firstpage.parser.CancelOrderMessageParser;
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
    private static final int REQUEST_INFO = 0;
    private static final int REQUEST_CANCEL = 1;
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
    private TextView mPlayDate;
    private TextView mBeginDate;
    private Button mPayButton;

    private String mOrderId;
    private View mHeaderView;
    private View mFooterView;
    private OrderContentMessage mOrderContentMessage;

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
        mScenicCity = (TextView) mHeaderView.findViewById(R.id.sence_city);
        mScenicAddress = (TextView) mHeaderView.findViewById(R.id.sence_address);
        mOrderTime = (TextView) mFooterView.findViewById(R.id.order_time);
        mOrderIdTextView = (TextView) mFooterView.findViewById(R.id.order_id);
        mOrderPayCount = (TextView) mFooterView.findViewById(R.id.order_count);
        mOrderPayTotal = (TextView) mFooterView.findViewById(R.id.order_total_pay);
        mPayButton = (Button) mFooterView.findViewById(R.id.pay_button);
        mPlayDate = (TextView) mHeaderView.findViewById(R.id.time_play);
        mBeginDate = (TextView) mHeaderView.findViewById(R.id.time_begin);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mPayButton.setOnClickListener(this);
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
            getOrderContentRequest();
        }
    }

    private void getOrderContentRequest() {
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
            job.setRequestId(REQUEST_INFO);
            job.doRequest();
        }
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {
        switch (job.getRequestId()) {
            case REQUEST_INFO:
                dealOrderMessage(job);
                break;
            case REQUEST_CANCEL:
                dealCancelOrderMessage(job);
                break;
            default:
                break;
        }

        mOrderContentListView.onRefreshComplete();
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }
    
    private void dealCancelOrderMessage(RequestJob job) {
        CancelOrderMessage message = (CancelOrderMessage) job.getBaseType();
        String code = message.getCode();
        if ("10000".equals(code)) {
            String token = message.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }
            
            ToastUtil.showToast("取消成功");
            mOrderType.setText("已取消");
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_CANCEL);
            }

            ToastUtil.showToast(message.getMessage());
        }
    }

    private void dealOrderMessage(RequestJob job) {
        mOrderContentMessage = (OrderContentMessage) job.getBaseType();
        String code = mOrderContentMessage.getCode();
        if ("10000".equals(code)) {
            String token = mOrderContentMessage.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            mInfos = mOrderContentMessage.getInfos();
            mOrderTitle.setText(mOrderContentMessage.getProductName());
            mOrderCount.setText("X" + mOrderContentMessage.getTotal_num());
            mOrderTotal.setText("￥" + mOrderContentMessage.getPrice());
            mOrderTime.setText(mOrderContentMessage.getCreateTime());
            mOrderPayCount.setText(mOrderContentMessage.getTotal_num());
            mOrderPayTotal.setText(mOrderContentMessage.getOrderAmount());
            mScenicCity.setText(mOrderContentMessage.getProvice() + mOrderContentMessage.getCity()
                    + mOrderContentMessage.getCounty());
            mScenicAddress.setText(mOrderContentMessage.getSceneAddress());
            mPlayDate.setText(mOrderContentMessage.getStartTime());
            mBeginDate.setText(mOrderContentMessage.getShowStartTime());

            if ("0".equals(mOrderContentMessage.getOrderState())) {
                mPayButton.setVisibility(View.VISIBLE);
            } else if ("1".equals(mOrderContentMessage.getOrderState())) {
                mPayButton.setText(R.string.cancel_order);
                mPayButton.setVisibility(View.VISIBLE);
            }

            if (mInfos.size() > 0) {
                mMemberAdapter.setList(mInfos);
                mMemberAdapter.notifyDataSetChanged();
            }

        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_INFO);
            }

            ToastUtil.showToast(mOrderContentMessage.getMessage());
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_top_button:
                finish();
                break;
            case R.id.pay_button:
                if (mOrderContentMessage != null) {
                    if ("0".equals(mOrderContentMessage.getOrderState())) {
                        Intent intent = new Intent(this, ChoosePayActivity.class);
                        intent.putExtra("name", mOrderContentMessage.getSceneName());
                        intent.putExtra("count", mOrderContentMessage.getTotal_num());
                        intent.putExtra("price", mOrderContentMessage.getPrice());
                        intent.putExtra("total", mOrderContentMessage.getOrderAmount());
                        intent.putExtra("orderId", mOrderContentMessage.getOrderID());
                        intent.putExtra("des", mOrderContentMessage.getNotice());
                        intent.putExtra("isUnique", "");
                        startActivity(intent);
                    } else if ("1".equals(mOrderContentMessage.getOrderState())) {
                        getCancelOrderRequest();
                    }
                }

                break;
            default:
                break;
        }
    }
    
    private void getCancelOrderRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "cancelOrder");
            jsonObject.put("orderID", mOrderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new CancelOrderMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.setRequestId(REQUEST_CANCEL);
            job.doRequest();
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == REQUEST_INFO) {
                getOrderContentRequest();
            } else if (requestCode == REQUEST_CANCEL) {
                getCancelOrderRequest();
            }
        }
        super.onActivityResult(requestCode, responseCode, data);
    }
}

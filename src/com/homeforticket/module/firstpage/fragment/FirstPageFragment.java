
package com.homeforticket.module.firstpage.fragment;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.fragment.BaseFragment;
import com.homeforticket.module.firstpage.activity.ClientManageActivity;
import com.homeforticket.module.firstpage.activity.DistributionManageActivity;
import com.homeforticket.module.firstpage.activity.OrderManageActivity;
import com.homeforticket.module.firstpage.activity.StoreManageAcitivity;
import com.homeforticket.module.firstpage.activity.StoreStatisticsAvtivity;
import com.homeforticket.module.firstpage.activity.WalletAcitivity;
import com.homeforticket.module.firstpage.model.ResellerMessage;
import com.homeforticket.module.firstpage.parser.ResellerMessageParser;
import com.homeforticket.module.firstpage.view.ChooseBehalfPopupWindow;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

public class FirstPageFragment extends BaseFragment implements OnClickListener, RequestListener {
    private static FirstPageFragment sFirstPageFragment;
    private View mView;
    private TextView mTxtTitle;
    private RelativeLayout mIncomeLayout;
    private TextView mBehalfBuyButton;
    private TextView mStoreManageButton;
    private TextView mOrderManageButton;
    private TextView mConsumerManageButton;
    private TextView mStoreCountButton;
    private TextView mSellManageButton;
    private TextView mTotalIncomeNum;
    private TextView mCurrentMonthIncomeNum;
    private ImageView mRightArrow;
    private ChooseBehalfPopupWindow mChooseBehalfPopupWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_firstpage, null);
        initView();
        initListener();
        initData();
        return mView;
    }

    private void initView() {
        mTxtTitle = (TextView) mView.findViewById(R.id.top_title);
        mTxtTitle.setText(R.string.firstpage_tab);
        mView.findViewById(R.id.left_top_button).setVisibility(View.GONE);
        mIncomeLayout = (RelativeLayout) mView.findViewById(R.id.income_layout);
        mBehalfBuyButton = (TextView) mView.findViewById(R.id.behalf_buy_button);
        mStoreManageButton = (TextView) mView.findViewById(R.id.store_manage_button);
        mOrderManageButton = (TextView) mView.findViewById(R.id.order_manage_button);
        mConsumerManageButton = (TextView) mView.findViewById(R.id.consumer_manage_button);
        mStoreCountButton = (TextView) mView.findViewById(R.id.store_count_button);
        mSellManageButton = (TextView) mView.findViewById(R.id.sell_manage_button);
        mTotalIncomeNum = (TextView) mView.findViewById(R.id.total_income_num);
        mCurrentMonthIncomeNum = (TextView) mView.findViewById(R.id.current_month_income_num);
        mRightArrow = (ImageView) mView.findViewById(R.id.right_arrow_img);
    }

    private void initListener() {
        mBehalfBuyButton.setOnClickListener(this);
        mStoreManageButton.setOnClickListener(this);
        mOrderManageButton.setOnClickListener(this);
        mConsumerManageButton.setOnClickListener(this);
        mStoreCountButton.setOnClickListener(this);
        mSellManageButton.setOnClickListener(this);
        mRightArrow.setOnClickListener(this);
    }
    
    private void initData() {
        mTotalIncomeNum.setText(SharedPreferencesUtil.readString(SysConstants.TOTAL_MONEY, "0.00"));
        mCurrentMonthIncomeNum.setText(SharedPreferencesUtil.readString(SysConstants.CURRENT_MONEY, "0.00"));
        if ("4".equals(SharedPreferencesUtil.readString(SysConstants.ROLE, "4"))) {
            mSellManageButton.setVisibility(View.INVISIBLE);
        } else if ("5".equals(SharedPreferencesUtil.readString(SysConstants.ROLE, "4"))) {
            mSellManageButton.setVisibility(View.VISIBLE);
        } else {
            mSellManageButton.setVisibility(View.INVISIBLE);
        }
        getResellerRequest();
    }

    private void getResellerRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "getReseller");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new ResellerMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.doRequest();
        }
    }
    
    @Override
    public void initFragment() {
        super.initFragment();
        getResellerRequest();
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

    public static FirstPageFragment getInstance() {
        if (sFirstPageFragment == null) {
            sFirstPageFragment = new FirstPageFragment();
        }
        return sFirstPageFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_arrow_img:
                startActivity(new Intent(getActivity(), WalletAcitivity.class));
                break;
            case R.id.behalf_buy_button:
                mChooseBehalfPopupWindow = new ChooseBehalfPopupWindow(getActivity());
                mChooseBehalfPopupWindow.showAsDropDown(mTxtTitle);
                break;
            case R.id.store_manage_button:
                startActivity(new Intent(getActivity(), StoreManageAcitivity.class));
                break;
            case R.id.order_manage_button:
                startActivity(new Intent(getActivity(), OrderManageActivity.class));
                break;
            case R.id.consumer_manage_button:
                startActivity(new Intent(getActivity(), ClientManageActivity.class));
                break;
            case R.id.store_count_button:
                startActivity(new Intent(getActivity(), StoreStatisticsAvtivity.class));
                break;
            case R.id.sell_manage_button:
                startActivity(new Intent(getActivity(), DistributionManageActivity.class));
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
        ResellerMessage resellerMessage = (ResellerMessage) job.getBaseType();
        String code = resellerMessage.getCode();

        if ("10000".equals(code)) {
            String token = resellerMessage.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            SharedPreferencesUtil.saveString(SysConstants.USER_PHOTO, resellerMessage.getPhoto());
            SharedPreferencesUtil.saveString(SysConstants.USER_TEL, resellerMessage.getTel());
            SharedPreferencesUtil.saveString(SysConstants.USERNAME, resellerMessage.getName());
            SharedPreferencesUtil.saveString(SysConstants.TOTAL_MONEY, resellerMessage.getTotalMoney());
            SharedPreferencesUtil.saveString(SysConstants.BIND_ACCOUNT, resellerMessage.getBind());
            SharedPreferencesUtil.saveString(SysConstants.CURRENT_MONEY, resellerMessage.getCurrentMoney());
            mTotalIncomeNum.setText(SharedPreferencesUtil.readString(SysConstants.TOTAL_MONEY, "0.00"));
            mCurrentMonthIncomeNum.setText(SharedPreferencesUtil.readString(SysConstants.CURRENT_MONEY, "0.00"));
        } else {
            if ("10004".equals(code)) {
                SharedPreferencesUtil.saveBoolean(SysConstants.IS_LOGIN, false);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, SysConstants.FIRSTPAGE_CODE);
            } 
            
            ToastUtil.showToast(resellerMessage.getMessage());
        }
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }
    
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.FIRSTPAGE_CODE) {
                getResellerRequest();
            } 
        }
        super.onActivityResult(requestCode, responseCode, data);
    }
}

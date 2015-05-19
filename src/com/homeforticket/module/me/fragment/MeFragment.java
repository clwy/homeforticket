
package com.homeforticket.module.me.fragment;

import java.lang.reflect.Field;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.fragment.BaseFragment;
import com.homeforticket.module.firstpage.activity.WalletAcitivity;
import com.homeforticket.module.me.activity.AboutActivity;
import com.homeforticket.module.me.activity.HelpActivity;
import com.homeforticket.module.me.activity.UserBindAccountActivity;
import com.homeforticket.module.me.activity.UserInfoActivity;
import com.homeforticket.module.me.activity.UserUnBindActivity;
import com.homeforticket.util.CircleTransform;
import com.homeforticket.util.SharedPreferencesUtil;

public class MeFragment extends BaseFragment implements OnClickListener {
    private static MeFragment sMeFragment;
    private View mView;
    private TextView mTxtTitle;
    private ImageView mUserHeadImg;
    private TextView mUserName;
    private TextView mUserTel;
    private TextView mIsBind;
    private TextView mUserIncome;
    private RelativeLayout mUserInfoLayout;
    private RelativeLayout mUserIncomeLayout;
    private RelativeLayout mUserAccountLayout;
    private RelativeLayout mHelpLayout;
    private RelativeLayout mAboutLayout;
    private RelativeLayout mShareLayout;

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
                mUserName.setText(SharedPreferencesUtil.readString(SysConstants.USERNAME, ""));
                mUserTel.setText(SharedPreferencesUtil.readString(SysConstants.USER_TEL, ""));
                Glide.with(getActivity())
                        .load(SharedPreferencesUtil.readString(SysConstants.USER_PHOTO, ""))
                        .transform(new CircleTransform(getActivity())).into(mUserHeadImg);
                if ("0".equals(SharedPreferencesUtil.readString(SysConstants.BIND_ACCOUNT, "0"))) {
                    mIsBind.setText(R.string.account_unbind);
                } else {
                    mIsBind.setText(R.string.account_bind);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_me, null);
        initView();
        initListener();
        initData();
        return mView;
    }

    private void initData() {
        Glide.with(this).load(SharedPreferencesUtil.readString(SysConstants.USER_PHOTO, ""))
                .placeholder(R.drawable.default_head_img)
                .transform(new CircleTransform(getActivity())).into(mUserHeadImg);
        mTxtTitle.setText(R.string.me_tab);
        mUserName.setText(SharedPreferencesUtil.readString(SysConstants.USERNAME, ""));
        mUserTel.setText(SharedPreferencesUtil.readString(SysConstants.USER_TEL, ""));
        mUserIncome.setText(SharedPreferencesUtil.readString(SysConstants.TOTAL_MONEY, "0.00"));
        if ("0".equals(SharedPreferencesUtil.readString(SysConstants.BIND_ACCOUNT, "0"))) {
            mIsBind.setText(R.string.account_unbind);
        } else {
            mIsBind.setText(R.string.account_bind);
        }
    }

    private void initView() {
        mTxtTitle = (TextView) mView.findViewById(R.id.top_title);
        mView.findViewById(R.id.left_top_button).setVisibility(View.GONE);
        mUserHeadImg = (ImageView) mView.findViewById(R.id.user_head_img);
        mUserName = (TextView) mView.findViewById(R.id.user_name);
        mUserTel = (TextView) mView.findViewById(R.id.user_tel);
        mIsBind = (TextView) mView.findViewById(R.id.account_binding);
        mUserIncome = (TextView) mView.findViewById(R.id.income_count);
        mUserInfoLayout = (RelativeLayout) mView.findViewById(R.id.user_info_layout);
        mUserIncomeLayout = (RelativeLayout) mView.findViewById(R.id.income_layout);
        mUserAccountLayout = (RelativeLayout) mView.findViewById(R.id.account_layout);
        mHelpLayout = (RelativeLayout) mView.findViewById(R.id.help_layout);
        mAboutLayout = (RelativeLayout) mView.findViewById(R.id.about_layout);
        mShareLayout = (RelativeLayout) mView.findViewById(R.id.share_layout);
    }

    private void initListener() {
        mUserInfoLayout.setOnClickListener(this);
        mUserIncomeLayout.setOnClickListener(this);
        mUserAccountLayout.setOnClickListener(this);
        mHelpLayout.setOnClickListener(this);
        mAboutLayout.setOnClickListener(this);
        mShareLayout.setOnClickListener(this);
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

    public static MeFragment getInstance() {
        if (sMeFragment == null) {
            sMeFragment = new MeFragment();
        }
        return sMeFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_layout:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.income_layout:
                startActivity(new Intent(getActivity(), WalletAcitivity.class));
                break;
            case R.id.account_layout:
                if ("0".equals(SharedPreferencesUtil.readString(SysConstants.BIND_ACCOUNT, "0"))) {
                    startActivity(new Intent(getActivity(), UserUnBindActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), UserBindAccountActivity.class));
                }
                break;
            case R.id.help_layout:
                startActivity(new Intent(getActivity(), HelpActivity.class));
                break;
            case R.id.about_layout:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.share_layout:
                break;
            default:
                break;
        }

    }
}

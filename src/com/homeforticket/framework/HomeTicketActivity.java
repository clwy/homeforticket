
package com.homeforticket.framework;

import java.util.ArrayList;
import java.util.List;

import com.homeforticket.R;
import com.homeforticket.R.id;
import com.homeforticket.R.layout;
import com.homeforticket.R.string;
import com.homeforticket.appApplication.AppApplication;
import com.homeforticket.fragment.BaseFragment;
import com.homeforticket.module.buyticket.fragment.BuyTicketFragment;
import com.homeforticket.module.firstpage.fragment.FirstPageFragment;
import com.homeforticket.module.me.fragment.MeFragment;
import com.homeforticket.module.message.fragment.MessageFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

public class HomeTicketActivity extends BaseActivity implements OnClickListener {
    private static final int[] RADIO_VIEW_ID = {
            R.id.radio_firstpage_layout, R.id.radio_message_layout, R.id.radio_buyticket_layout,
            R.id.radio_me_layout
    };

    private Context mContext;
    private CenterView mCenterView;

    private FirstPageFragment mFirstPageFragment;
    private BuyTicketFragment mBuyTicketFragment;
    private MessageFragment mMessageFragment;
    private MeFragment mMeFragment;
    private RadioButton mFirstPageButton;
    private RadioButton mMessageButton;
    private RadioButton mBuyticketButton;
    private RadioButton mMeButton;
    private RelativeLayout mFirstPageLayout;
    private RelativeLayout mMessageLayout;
    private RelativeLayout mBuyticketLayout;
    private RelativeLayout mMeLayout;

    private List<RadioButton> mRadioButtons = new ArrayList<RadioButton>();
    private int mPressedIndex;
    private long mPressedTime = 0;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_homeforticket);
        init();
        initListener();
        mContext = this;
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setChangeFragment(R.id.radio_firstpage_layout, mFirstPageFragment);
        initFragment(mFirstPageFragment);
    }

    private void init() {
        mCenterView = (CenterView) findViewById(R.id.center_view);
        mCenterView.setCenterView(getLayoutInflater().inflate(R.layout.center_frame, null));
        mFirstPageLayout = (RelativeLayout) findViewById(R.id.radio_firstpage_layout);
        mMessageLayout = (RelativeLayout) findViewById(R.id.radio_message_layout);
        mBuyticketLayout = (RelativeLayout) findViewById(R.id.radio_buyticket_layout);
        mMeLayout = (RelativeLayout) findViewById(R.id.radio_me_layout);
        mFirstPageButton = (RadioButton) findViewById(R.id.radio_firstpage_button);
        mMessageButton = (RadioButton) findViewById(R.id.radio_message_button);
        mBuyticketButton = (RadioButton) findViewById(R.id.radio_buyticket_button);
        mMeButton = (RadioButton) findViewById(R.id.radio_me_button);

        mRadioButtons.add(mFirstPageButton);
        mRadioButtons.add(mMessageButton);
        mRadioButtons.add(mBuyticketButton);
        mRadioButtons.add(mMeButton);

        mFirstPageFragment = FirstPageFragment.getInstance();
        mBuyTicketFragment = BuyTicketFragment.getInstance();
        mMessageFragment = MessageFragment.getInstance();
        mMeFragment = MeFragment.getInstance();
        
        FragmentTransaction t = getFragmentTransaction();
        t.replace(R.id.center_frame, mFirstPageFragment);
        t.commit();
    }

    private void initListener() {
        mFirstPageLayout.setOnClickListener(this);
        mMessageLayout.setOnClickListener(this);
        mBuyticketLayout.setOnClickListener(this);
        mMeLayout.setOnClickListener(this);
        mFirstPageButton.setOnClickListener(this);
        mMessageButton.setOnClickListener(this);
        mBuyticketButton.setOnClickListener(this);
        mMeButton.setOnClickListener(this);
    }

    public FragmentTransaction getFragmentTransaction() {
        return this.getSupportFragmentManager().beginTransaction();
    }

    public void setReplaceFragement(BaseFragment fragment) {
        FragmentTransaction transaction = getFragmentTransaction();
        try {
            transaction.replace(R.id.center_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            
            initFragment(fragment);
        } catch (Exception e) {

        }
    }

    private void setChangeFragment(int id, BaseFragment fragment) {
        if (mPressedIndex == id) {
            // resetFragment(fragment);
        } else {
            mPressedIndex = id;
            setReplaceFragement(fragment);
            setRadioCheck(id);
        }
    }

    private void setRadioCheck(int id) {
        for (int i = 0; i < RADIO_VIEW_ID.length; i++) {
            mRadioButtons.get(i).setChecked(id == RADIO_VIEW_ID[i] ? true : false);
            mRadioButtons.get(i).setTextColor(
                    id == RADIO_VIEW_ID[i] ? Color.parseColor("#1db2f6") : Color
                            .parseColor("#929292"));
        }
    }

    public void resetFragment(BaseFragment fragment) {
        fragment.resetFragment();
    }

    public void initFragment(BaseFragment fragment) {
        fragment.initFragment();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.radio_firstpage_layout:
            case R.id.radio_firstpage_button:
                setChangeFragment(R.id.radio_firstpage_layout, mFirstPageFragment);
                break;
            case R.id.radio_message_layout:
            case R.id.radio_message_button:
                setChangeFragment(R.id.radio_message_layout, mMessageFragment);
                break;
            case R.id.radio_buyticket_layout:
            case R.id.radio_buyticket_button:
                setChangeFragment(R.id.radio_buyticket_layout, mBuyTicketFragment);
                break;
            case R.id.radio_me_layout:
            case R.id.radio_me_button:
                setChangeFragment(R.id.radio_me_layout, mMeFragment);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mPressedTime) > 2000) {
                showToast(getResources().getString(R.string.toast_system_exit));
                mPressedTime = System.currentTimeMillis();
            } else {
                AppApplication.getInstance().applicationExit();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}

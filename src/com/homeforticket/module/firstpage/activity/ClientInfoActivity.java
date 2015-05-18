
package com.homeforticket.module.firstpage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.homeforticket.module.firstpage.model.ClientInfo;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.module.me.activity.UserMobileActivity;
import com.homeforticket.module.me.activity.UserNameActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.CircleTransform;
import com.homeforticket.util.SharedPreferencesUtil;

/**
 * @Title: UserInfoActivity.java
 * @Package com.homeforticket.module.me.activity
 * @Description: TODO
 * @author LR
 * @date 2015年5月1日 下午2:29:28
 */
public class ClientInfoActivity extends BaseActivity implements OnClickListener, RequestListener {
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private TextView mUserName;
    private TextView mUserMoblie;
    private TextView mSettingText;
    private ImageView mUserHeadImg;
    private RelativeLayout mUserNameLayout;
    private RelativeLayout mUserMoblieLayout;
    private RelativeLayout mClientSettingLayout;
    private ClientInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientinfo);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mUserName = (TextView) findViewById(R.id.username_text);
        mUserMoblie = (TextView) findViewById(R.id.user_moblie_text);
        mUserHeadImg = (ImageView) findViewById(R.id.user_headimg);
        mUserNameLayout = (RelativeLayout) findViewById(R.id.username_layout);
        mUserMoblieLayout = (RelativeLayout) findViewById(R.id.use_moblie_layout);
        mClientSettingLayout = (RelativeLayout) findViewById(R.id.client_setting_layout);
        mSettingText = (TextView) findViewById(R.id.setting_text);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mUserNameLayout.setOnClickListener(this);
        mUserMoblieLayout.setOnClickListener(this);
        mClientSettingLayout.setOnClickListener(this);
    }

    private void initData() {

        mTxtTitle.setText(R.string.client_info_title);
        Intent intent = getIntent();
        mInfo = (ClientInfo) intent.getSerializableExtra("ClientInfo");
        Glide.with(this).load(mInfo.getHeadimg()).transform(new CircleTransform(this)).into(mUserHeadImg);
        mUserName.setText(mInfo.getName());
        mUserMoblie.setText(mInfo.getTel());
        mSettingText.setText(mInfo.getNote());
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {

    }

    @Override
    public void onFail(RequestJob job) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_top_button:
                finish();
                break;
            case R.id.client_setting_layout:
                Intent intent = new Intent(this, ClientDesActivity.class);
                intent.putExtra("ClientInfo", mInfo);
                startActivityForResult(intent, SysConstants.SET_CLIENT_INFO);
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.SET_CLIENT_INFO) {
            mSettingText.setText(data.getStringExtra("des"));
            mInfo.setNote(data.getStringExtra("des"));
        }
        super.onActivityResult(requestCode, responseCode, data);
    }

}

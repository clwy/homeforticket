package com.homeforticket.common.share;

import com.homeforticket.R;
import com.homeforticket.common.share.AbsOauthLogin.ShareListener;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.util.ToastUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @Title: ShareWeiboActivity.java 
 * @Package com.homeforticket.common.share 
 * @Description: TODO 
 * @author LR   
 * @date 2015年5月20日 下午2:01:52 
 */
public class ShareWeiboActivity extends BaseActivity implements OnClickListener {
    protected static final int SHARE_SUCCESS = 0;
    protected static final int SHARE_FAILED = 1;

    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private ImageButton btShare;
    private EditText etMsg;
    private TextView tvNum;
    private InputMethodManager imm;
    private String mUrl;
    private String mTitle;
    private String mContent;
    private String mPath;
    private boolean isTextOverLimit = false;
    
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHARE_SUCCESS:
                    ShareWeiboActivity.this.finish();
                    ToastUtil.showToast("分享成功");
                    break;
                case SHARE_FAILED:
                    ToastUtil.showToast("分享失败");
                    break;
                default:
                    break;
            }
        };
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setContentView(R.layout.activity_share_weibo);
        initView();
        initListener();
        initData();
    }
    
    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        btShare = (ImageButton) findViewById(R.id.right_top_button);
        btShare.setImageResource(R.drawable.bg_btn_operate_commit);
        btShare.setVisibility(View.VISIBLE);
        
        etMsg = (EditText) findViewById(R.id.edittext_share_msg);
        tvNum = (TextView) findViewById(R.id.textview_num);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        btShare.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText("分享到新浪微博");
        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mTitle = intent.getStringExtra("title");
        mContent = intent.getStringExtra("content");
        mPath = intent.getStringExtra("path");
        
        StringBuilder shareContent = new StringBuilder(mTitle);
        if (!TextUtils.isEmpty(mUrl)) {
            shareContent.append(" ").append(mContent).append(" ").append(mUrl);
        }
        
        etMsg.setText(shareContent.toString());
        etMsg.addTextChangedListener(watcher);
        textLengthCheck(shareContent.toString().length());
    }
    
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            textLengthCheck(s.length());
        }

    };

    private void textLengthCheck(int length) {
        int num = 140 - length;
        if (num < 0) {
            tvNum.setText("已超出" + Math.abs(num) + "个字");
            tvNum.setTextColor(Color.RED);
            isTextOverLimit = true;
        } else {
            tvNum.setText("还可输入" + num + "个字");
            tvNum.setTextColor(Color.parseColor("#979797"));
            isTextOverLimit = false;
        }
    }

    @Override
    public void onPause() {
        if (imm != null) {
            imm.hideSoftInputFromWindow(etMsg.getWindowToken(), 0);
        }
        super.onPause();
    };
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_top_button:
                finish();
                break;
            case R.id.right_top_button:
                if (!isTextOverLimit) {
                    String message = etMsg.getText().toString().trim();
                    shareSinaWeibo(message);
                } else {
                    ToastUtil.showToast("字数超出限制", Toast.LENGTH_LONG);
                }
                break;
        }
    }
    
    public void shareSinaWeibo(String message) {
        SinaOauthLogin sinaOauth = new SinaOauthLogin(this);
        sinaOauth.setShareListener(new ShareListener() {

            @Override
            public void onShareSucceed() {
                mHandler.sendEmptyMessage(SHARE_SUCCESS);
            }

            @Override
            public void onShareFailed(String msg) {
                mHandler.sendEmptyMessage(SHARE_FAILED);
            }
        });
        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(mPath)) {
            bitmap = BitmapFactory.decodeFile(mPath);
        }
        sinaOauth.shareToX(message, bitmap);
    }

}

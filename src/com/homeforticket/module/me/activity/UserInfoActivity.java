
package com.homeforticket.module.me.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.module.firstpage.model.OrderStatusMessage;
import com.homeforticket.module.firstpage.parser.OrderStatusMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.module.me.model.UploadImgMessage;
import com.homeforticket.module.me.parser.UploadImgMessageParser;
import com.homeforticket.module.me.view.SelectBottomPopupWindow;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.BitmapUtils;
import com.homeforticket.util.CircleTransform;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

/**
 * @Title: UserInfoActivity.java
 * @Package com.homeforticket.module.me.activity
 * @Description: TODO
 * @author LR
 * @date 2015年5月1日 下午2:29:28
 */
public class UserInfoActivity extends BaseActivity implements OnClickListener, RequestListener {
    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1;
    private static final String PATH = SysConstants.LOCALPATH + "capture.png";
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private TextView mUserName;
    private TextView mUserMoblie;
    private ImageView mUserHeadImg;
    private RelativeLayout mUserHeadImgLayout;
    private RelativeLayout mUserNameLayout;
    private RelativeLayout mUserMoblieLayout;
    private RelativeLayout mResetPasswordLayout;
    private Button mLogoutButton;
    private SelectBottomPopupWindow mSelectBottomPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        initView();
        initListener();
        initData();

    }

    @Override
    public void onResume() {
        super.onResume();
        mUserName.setText(SharedPreferencesUtil.readString(SysConstants.USERNAME, ""));
        mUserMoblie.setText(SharedPreferencesUtil.readString(SysConstants.USER_TEL, ""));
    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mUserName = (TextView) findViewById(R.id.username_text);
        mUserMoblie = (TextView) findViewById(R.id.user_moblie_text);
        mUserHeadImg = (ImageView) findViewById(R.id.user_headimg);
        mUserHeadImgLayout = (RelativeLayout) findViewById(R.id.user_head_img_layout);
        mUserNameLayout = (RelativeLayout) findViewById(R.id.username_layout);
        mUserMoblieLayout = (RelativeLayout) findViewById(R.id.use_moblie_layout);
        mResetPasswordLayout = (RelativeLayout) findViewById(R.id.reset_password_layout);
        mLogoutButton = (Button) findViewById(R.id.user_logout);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mUserHeadImgLayout.setOnClickListener(this);
        mUserNameLayout.setOnClickListener(this);
        mUserMoblieLayout.setOnClickListener(this);
        mResetPasswordLayout.setOnClickListener(this);
        mLogoutButton.setOnClickListener(this);
    }

    private void initData() {
        Glide.with(this).load(SharedPreferencesUtil.readString(SysConstants.USER_PHOTO, ""))
                .transform(new CircleTransform(this)).into(mUserHeadImg);
        mTxtTitle.setText(R.string.profile_title);
        mUserName.setText(SharedPreferencesUtil.readString(SysConstants.USERNAME, ""));
        mUserMoblie.setText(SharedPreferencesUtil.readString(SysConstants.USER_TEL, ""));
    }

    private void uploadHeadImgRequest(String path) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "uploadPhoto");
            jsonObject.put("type", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("file", path));
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(), pairs,
                    new UploadImgMessageParser());
            job.setRequestListener(this);
            job.doRequest("1");
        }
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {
        UploadImgMessage uploadImgMessage = (UploadImgMessage) job.getBaseType();
        String code = uploadImgMessage.getCode();
        if ("10000".equals(code)) {
            String token = uploadImgMessage.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            SharedPreferencesUtil.saveString(SysConstants.USER_PHOTO, uploadImgMessage.getPath());
            Glide.with(UserInfoActivity.this).load(SharedPreferencesUtil.readString(SysConstants.USER_PHOTO, ""))
                    .transform(new CircleTransform(this)).into(mUserHeadImg);
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_ORDER_STATUS);
            } else {
                ToastUtil.showToast(uploadImgMessage.getMessage());
            }
        }
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
            case R.id.user_head_img_layout:
                mSelectBottomPopupWindow = new SelectBottomPopupWindow(this, systemClickListener,
                        cameraClickListener);
                mSelectBottomPopupWindow.showAsDropDown(mTxtTitle);
                break;
            case R.id.username_layout:
                startActivity(new Intent(this, UserNameActivity.class));
                break;
            case R.id.use_moblie_layout:
                startActivity(new Intent(this, UserMobileActivity.class));
                break;
            case R.id.reset_password_layout:
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
            case R.id.user_logout:
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, "");
                SharedPreferencesUtil.saveBoolean(SysConstants.IS_LOGIN, false);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    private OnClickListener systemClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");// 相片类型
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        }
    };

    private OnClickListener cameraClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
            }
            else {
                ToastUtil.showToast("请确认已经插入SD卡");
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSelectBottomPopupWindow.dismiss();
        final File cacheFile = new File(SysConstants.LOCALPATH + "capture.png");
        if (cacheFile.exists()) {
            cacheFile.delete();
        }

        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                dealBitmap(bitmap);
            } catch (FileNotFoundException e) {
            }
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap photo = (Bitmap) bundle.get("data"); // get bitmap
                dealBitmap(photo);
            }
        }
    }
    
    private void dealBitmap(Bitmap bm) {
        BitmapUtils.saveImage(bm, PATH);
        BitmapUtils.getSmallBitmap(PATH);
        uploadHeadImgRequest(PATH);
    }
}

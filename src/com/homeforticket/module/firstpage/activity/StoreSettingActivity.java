
package com.homeforticket.module.firstpage.activity;

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
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.module.firstpage.model.StoreStatisticsMessage;
import com.homeforticket.module.firstpage.parser.StoreStatisticsMessageParser;
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
 * @Title: StoreSettingActivity.java
 * @Package com.homeforticket.module.firstpage.activity
 * @Description: TODO
 * @author LR
 * @date 2015年5月8日 上午11:30:07
 */
public class StoreSettingActivity extends BaseActivity implements OnClickListener, RequestListener {
    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1;
    private static final int REQUEST_CODE_STORE_CONTENT = 2;
    private static final int REQUEST_CODE_STORE_HEADIMG = 3;
    private String mPath;
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;

    private ImageView mStoreHeadimg;
    private TextView mStoreName;
    private TextView mStoreInfo;
    private TextView mStoreAddress;
    private TextView mStoreTel;
    private TextView mAutoConfirm;
    private TextView mAutoClosed;
    private TextView mAlreadyOpen;
    private RelativeLayout mStoreInfoLayout;
    private RelativeLayout mStoreAddressLayout;
    private RelativeLayout mStoreTelLayout;
    private RelativeLayout mHeadLayout;
    private RelativeLayout mStoreNameLayout;
    private StoreStatisticsMessage mStoreStatisticsMessage;
    private SelectBottomPopupWindow mSelectBottomPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_setting);

        initView();
        initListener();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                doGetWebsite();
            }
        });
    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mStoreHeadimg = (ImageView) findViewById(R.id.store_headimg);
        mStoreName = (TextView) findViewById(R.id.store_name);
        mStoreInfo = (TextView) findViewById(R.id.store_info);
        mStoreAddress = (TextView) findViewById(R.id.store_address);
        mStoreTel = (TextView) findViewById(R.id.store_tel);
        mAutoConfirm = (TextView) findViewById(R.id.auto_confirm);
        mAutoClosed = (TextView) findViewById(R.id.auto_closed);
        mAlreadyOpen = (TextView) findViewById(R.id.guarantee);
        mStoreInfoLayout = (RelativeLayout) findViewById(R.id.store_info_layout);
        mStoreAddressLayout = (RelativeLayout) findViewById(R.id.store_address_layout);
        mStoreTelLayout = (RelativeLayout) findViewById(R.id.store_tel_layout);
        mHeadLayout = (RelativeLayout) findViewById(R.id.head_layout);
        mStoreNameLayout = (RelativeLayout) findViewById(R.id.store_name_layout);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mStoreInfoLayout.setOnClickListener(this);
        mStoreAddressLayout.setOnClickListener(this);
        mStoreTelLayout.setOnClickListener(this);
        mHeadLayout.setOnClickListener(this);
        mStoreNameLayout.setOnClickListener(this);
    }

    private void initData() {
        mPath = SysConstants.LOCALPATH;
        mTxtTitle.setText(R.string.store_setting_title);
    }

    private void doGetWebsite() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "getWebsite");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new StoreStatisticsMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.setRequestId(REQUEST_CODE_STORE_CONTENT);
            job.doRequest();
        }
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {
        switch (job.getRequestId()) {
            case REQUEST_CODE_STORE_HEADIMG:
                dealStoreHeadImg(job);
                break;
            case REQUEST_CODE_STORE_CONTENT:
                dealStoreContent(job);
                break;
            default:
                break;
        }

    }

    private void dealStoreHeadImg(RequestJob job) {
        UploadImgMessage uploadImgMessage = (UploadImgMessage) job.getBaseType();
        String code = uploadImgMessage.getCode();
        if ("10000".equals(code)) {
            String token = uploadImgMessage.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            Glide.with(this).load(uploadImgMessage.getPath()).transform(new CircleTransform(this))
                    .into(mStoreHeadimg);
        } else {
            if ("10004".equals(code)) {
                SharedPreferencesUtil.saveBoolean(SysConstants.IS_LOGIN, false);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_ORDER_STATUS);
            } 
                
            ToastUtil.showToast(uploadImgMessage.getMessage());
        }
    }

    private void dealStoreContent(RequestJob job) {
        mStoreStatisticsMessage = (StoreStatisticsMessage) job.getBaseType();
        String code = mStoreStatisticsMessage.getCode();

        if ("10000".equals(code)) {
            String token = mStoreStatisticsMessage.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            Glide.with(this).load(mStoreStatisticsMessage.getImg()).transform(new CircleTransform(this))
                    .into(mStoreHeadimg);
            mStoreName.setText(mStoreStatisticsMessage.getName());
            mStoreInfo.setText(mStoreStatisticsMessage.getDescription());
            mStoreAddress.setText(mStoreStatisticsMessage.getAddress());
            mStoreTel.setText(mStoreStatisticsMessage.getTel());
            mAutoConfirm.setText(mStoreStatisticsMessage.getAutoRecevie()
                    + getResources().getString(R.string.day));
            mAutoClosed.setText(mStoreStatisticsMessage.getAutoClose()
                    + getResources().getString(R.string.hour));

            mAlreadyOpen
                    .setText("0".equals(mStoreStatisticsMessage.getIsassurance()) ? getResources()
                            .getString(R.string.already_closed) : getResources().getString(
                            R.string.already_open));

        } else {
            if ("10004".equals(code)) {
                SharedPreferencesUtil.saveBoolean(SysConstants.IS_LOGIN, false);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_CODE_STORE_CONTENT);
            }

            ToastUtil.showToast(mStoreStatisticsMessage.getMessage());
        }
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.left_top_button:
                finish();
                break;
            case R.id.head_layout:
                mSelectBottomPopupWindow = new SelectBottomPopupWindow(this, systemClickListener,
                        cameraClickListener);
                mSelectBottomPopupWindow.showAsDropDown(mTxtTitle);
                break;
            case R.id.store_info_layout:
                if (mStoreStatisticsMessage != null) {
                    intent = new Intent(this, StoreDesActivity.class);
                    intent.putExtra("StoreStatisticsMessage", mStoreStatisticsMessage);
                    startActivity(intent);
                }
                break;
            case R.id.store_address_layout:
                if (mStoreStatisticsMessage != null) {
                    intent = new Intent(this, StoreAddressActivity.class);
                    intent.putExtra("StoreStatisticsMessage", mStoreStatisticsMessage);
                    startActivity(intent);
                }
                break;
            case R.id.store_tel_layout:
                if (mStoreStatisticsMessage != null) {
                    intent = new Intent(this, StoreMobileActivity.class);
                    intent.putExtra("StoreStatisticsMessage", mStoreStatisticsMessage);
                    startActivity(intent);
                }
                break;
            case R.id.store_name_layout:
                if (mStoreStatisticsMessage != null) {
                    intent = new Intent(this, StoreNameActivity.class);
                    intent.putExtra("StoreStatisticsMessage", mStoreStatisticsMessage);
                    startActivity(intent);
                }
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

        if (requestCode == REQUEST_CODE_PICK_IMAGE || requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            mSelectBottomPopupWindow.dismiss();
            File cacheFile = new File(mPath);
            if (!cacheFile.exists()) {
                cacheFile.mkdirs();
            }

            String path = mPath + "store.png";
            try {

                if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                    Uri uri = data.getData();
                    ContentResolver cr = this.getContentResolver();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        dealBitmap(bitmap, path);
                    } catch (FileNotFoundException e) {
                    }
                } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap photo = (Bitmap) bundle.get("data"); 
                        dealBitmap(photo, path);
                    }

                }
            } catch (Exception e) {
                ToastUtil.showToast("选取图片出错，请重新选择");
            }
        } else if (requestCode == REQUEST_CODE_STORE_CONTENT) {
            doGetWebsite();
        }
    }

    private void uploadHeadImgRequest(String path) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "uploadPhoto");
            jsonObject.put("type", "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("file", path));
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(), pairs,
                    new UploadImgMessageParser());
            job.setRequestListener(this);
            job.setRequestId(REQUEST_CODE_STORE_HEADIMG);
            job.doRequest("1");
        }
    }

    private void dealBitmap(Bitmap bm, String path) {
        BitmapUtils.saveImage(bm, path);
        BitmapUtils.saveImage(BitmapUtils.getSmallBitmap(path), path);
        uploadHeadImgRequest(path);
    }

}

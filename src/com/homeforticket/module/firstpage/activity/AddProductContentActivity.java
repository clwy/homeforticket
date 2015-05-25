
package com.homeforticket.module.firstpage.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.DatePicker.OnDateChangedListener;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.module.firstpage.model.AddProductMessage;
import com.homeforticket.module.firstpage.model.StatisticsInfoMessage;
import com.homeforticket.module.firstpage.parser.AddproductMessageParser;
import com.homeforticket.module.firstpage.parser.StoreStatisticsMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.module.me.view.SelectBottomPopupWindow;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.BitmapUtils;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

/**
 * @Title: AddProductActivity.java
 * @Package com.homeforticket.module.firstpage.activity
 * @Description: TODO
 * @author LR
 * @date 2015年5月14日 上午8:19:46
 */
public class AddProductContentActivity extends BaseActivity implements OnClickListener,
        RequestListener {
    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1;
    
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private Button mFinishButton;
    private EditText mProductName;
    private EditText mOriginPrice;
    private EditText mCurrentPrice;
    private EditText mCommissionPrice;
    private EditText mKinds;
    private TextView mBeginTime;
    private TextView mEndTime;
    private EditText mCity;
    private EditText mAddress;
    private EditText mDes;
    private DatePicker mDatePicker;
    private Button mConfirmButton;
    private ImageView mAddProduct;
    private boolean mIsBegin;
    private int mYear;
    private int mMonth;
    private int mDay;
    private SelectBottomPopupWindow mSelectBottomPopupWindow;
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_self_product);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mFinishButton = (Button) findViewById(R.id.right_button);
        mFinishButton.setVisibility(View.VISIBLE);
        mProductName = (EditText) findViewById(R.id.input_product_name);
        mOriginPrice = (EditText) findViewById(R.id.input_origin_price);
        mCurrentPrice = (EditText) findViewById(R.id.input_current_price);
        mCommissionPrice = (EditText) findViewById(R.id.input_commission_price);
        mKinds = (EditText) findViewById(R.id.input_kinds);
        mBeginTime = (TextView) findViewById(R.id.input_begin_time);
        mEndTime = (TextView) findViewById(R.id.input_end_time);
        mCity = (EditText) findViewById(R.id.input_city);
        mAddress = (EditText) findViewById(R.id.input_address);
        mDatePicker = (DatePicker) findViewById(R.id.datepicker);
        mConfirmButton = (Button) findViewById(R.id.confirm);
        mDes = (EditText) findViewById(R.id.input_des);
        mAddProduct = (ImageView) findViewById(R.id.add_product);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mFinishButton.setOnClickListener(this);
        mBeginTime.setOnClickListener(this);
        mEndTime.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);
        mAddProduct.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.add_self_prodduct);
        mFinishButton.setText(R.string.commit);

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        final int currentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH) + 1;
        final int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        
        mDatePicker.init(mYear, mMonth - 1, mDay, new OnDateChangedListener() {

            public void onDateChanged(DatePicker view, int year,
                    int monthOfYear, int dayOfMonth) {
                
                String current = String.valueOf(currentYear) + String.valueOf(currentMonth) 
                        + String.valueOf(currentDay);
                String after = String.valueOf(year) + String.valueOf(monthOfYear + 1) 
                        + String.valueOf(dayOfMonth); 
                if (Integer.parseInt(after) >= Integer.parseInt(current)) {
                    mYear = year;
                    mMonth = monthOfYear + 1;
                    mDay = dayOfMonth;
                } else {
                    ToastUtil.showToast(R.string.time_error);
                }
            }
        });
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {
        AddProductMessage message = (AddProductMessage) job.getBaseType();
        String code = message.getCode();

        if ("10000".equals(code)) {
            String token = message.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            finish();
        } else {
            if ("10004".equals(code)) {
                SharedPreferencesUtil.saveBoolean(SysConstants.IS_LOGIN, false);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.ADD_PRODUCT);
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
        if (requestCode == REQUEST_CODE_PICK_IMAGE || requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            mSelectBottomPopupWindow.dismiss();
            File cacheFile = new File(SysConstants.LOCALPATH);
            if (!cacheFile.exists()) {
                cacheFile.mkdirs();
            }

            mPath = SysConstants.LOCALPATH + "product.png";
            try {

                if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                    Uri uri = data.getData();
                    ContentResolver cr = this.getContentResolver();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        dealBitmap(bitmap, mPath);
                    } catch (FileNotFoundException e) {
                    }
                } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap photo = (Bitmap) bundle.get("data"); 
                        dealBitmap(photo, mPath);
                    }

                }
            } catch (Exception e) {
                ToastUtil.showToast("选取图片出错，请重新选择");
            }
        } else if (requestCode == SysConstants.ADD_PRODUCT) {
            doAddProduct();
        }
        super.onActivityResult(requestCode, responseCode, data);
    }

    private void dealBitmap(Bitmap bm, String path) {
        BitmapUtils.saveImage(bm, path);
        BitmapUtils.saveImage(BitmapUtils.getSmallBitmap(path), path);
        Glide.with(this).load(new File(path)).centerCrop().into(mAddProduct);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_top_button:
                finish();
                break;
            case R.id.right_button:
                String startTime = mBeginTime.getText().toString();
                String endTiem = mEndTime.getText().toString();
                if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTiem)) {
                    int start = Integer.parseInt(startTime.replaceAll("-", ""));
                    int end = Integer.parseInt(endTiem.replaceAll("-", ""));
                    if (start <= end) {
                        doAddProduct();
                    } else {
                        ToastUtil.showToast(R.string.time_error);
                    }
                }
                break;
            case R.id.input_begin_time:
                mIsBegin = true;
                mDatePicker.setVisibility(View.VISIBLE);
                mConfirmButton.setVisibility(View.VISIBLE);
                break;
            case R.id.input_end_time:
                mIsBegin = false;
                mDatePicker.setVisibility(View.VISIBLE);
                mConfirmButton.setVisibility(View.VISIBLE);
                break;
            case R.id.confirm:
                String chooseMonth = String.valueOf(mMonth);
                if (chooseMonth.length() < 2) {
                    chooseMonth = "0" + String.valueOf(mMonth);
                }
                String chooseDay = String.valueOf(mDay);
                if (chooseDay.length() < 2) {
                    chooseDay = "0" + String.valueOf(mDay);
                }
                if (mIsBegin) {
                    mBeginTime.setText(mYear + "-" + chooseMonth + "-" + chooseDay);
                } else {
                    mEndTime.setText(mYear + "-" + chooseMonth + "-" + chooseDay);
                }
                mDatePicker.setVisibility(View.INVISIBLE);
                mConfirmButton.setVisibility(View.INVISIBLE);
                break;
            case R.id.add_product:
                mSelectBottomPopupWindow = new SelectBottomPopupWindow(this, systemClickListener,
                        cameraClickListener);
                mSelectBottomPopupWindow.showAsDropDown(mTxtTitle);
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
    
    private void doAddProduct() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "saveProduct");
            jsonObject.put("productName", mProductName.getText().toString().trim());
            jsonObject.put("saleStartTime", mBeginTime.getText());
            jsonObject.put("saleEndTime", mEndTime.getText());
            jsonObject.put("price", mOriginPrice.getText().toString().trim());
            jsonObject.put("retailPrice", mCurrentPrice.getText().toString().trim());
            jsonObject.put("address", mAddress.getText().toString().trim());
            jsonObject.put("commission", mCommissionPrice.getText().toString().trim());
            jsonObject.put("channel", "1");
            jsonObject.put("city", mCity.getText().toString().trim());
            jsonObject.put("description", mDes.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("file", mPath));
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(), pairs, 
                    new AddproductMessageParser());
            job.setRequestListener(this);
            job.doRequest("1");
        }
    }
}

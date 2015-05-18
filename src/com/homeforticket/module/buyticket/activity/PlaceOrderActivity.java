
package com.homeforticket.module.buyticket.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker.OnDateChangedListener;

import com.alipay.sdk.app.PayTask;
import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.module.buyticket.model.ProductInfo;
import com.homeforticket.module.buyticket.model.SaveOrderMessage;
import com.homeforticket.module.buyticket.model.SceneInfo;
import com.homeforticket.module.buyticket.model.SceneTicketInfo;
import com.homeforticket.module.buyticket.parser.SaveOrderMessageParser;
import com.homeforticket.module.buyticket.parser.SceneInfoParser;
import com.homeforticket.module.buyticket.parser.SceneTicketInfoParser;
import com.homeforticket.module.buyticket.view.BuyTicketPopupWindow;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.pay.PayResult;
import com.homeforticket.pay.SignUtils;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.IdcardUtils;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

/**
 * @Title: PlaceOrder.java
 * @Package com.homeforticket.module.buyticket.activity
 * @Description: TODO
 * @author LR
 * @date 2015年5月6日 下午4:15:04
 */
public class PlaceOrderActivity extends BaseActivity implements OnClickListener, RequestListener {

    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private TextView mTicketName;
    private TextView mTicketLocation;
    private TextView mTicketType;
    private TextView mSinglePrice;
    private TextView mBuyNotice;
    private TextView mPlayDate;
    private TextView mBuyCount;
    private EditText mBuyPeopleName;
    private EditText mBuyPeopleTel;
    private EditText mBuyPeopleCard;
    private RelativeLayout mBuyPeopleLayout;
    private Button mAddButton;
    private TextView mTotalPrice;
    private TextView mTotalCount;
    private RelativeLayout mBuyTicket;
    private BuyTicketPopupWindow mBuyTicketPopupWindow;
    private DatePicker mDatePicker;
    private Button mConfirmButton;
    private RelativeLayout mBeginPlayTime;
    private TextView mBeginPlayDate;

    private ProductInfo mProductInfo;
    private int mIndex = 0;
    private int mLastIndex = 0;
    private int mCount = 0;
    private HashSet<String> mNameSets = new HashSet<String>();
    private HashSet<String> mTelSets = new HashSet<String>();
    private HashSet<String> mCardSets = new HashSet<String>();
    private int mYear;
    private int mMonth;
    private int mDay;
    private boolean mIsShowTime;
    private String mIsUnique;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeorder);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mTicketName = (TextView) findViewById(R.id.ticket_name);
        mTicketLocation = (TextView) findViewById(R.id.ticket_location);
        mTicketType = (TextView) findViewById(R.id.ticket_type);
        mSinglePrice = (TextView) findViewById(R.id.single_price);
        mBuyNotice = (TextView) findViewById(R.id.pay_notice);
        mPlayDate = (TextView) findViewById(R.id.play_date);
        mBuyCount = (TextView) findViewById(R.id.buy_count);
        mBuyPeopleName = (EditText) findViewById(R.id.input_connect_people);
        mBuyPeopleTel = (EditText) findViewById(R.id.input_connect_people_tel);
        mBuyPeopleCard = (EditText) findViewById(R.id.input_connect_people_card);
        mBuyPeopleLayout = (RelativeLayout) findViewById(R.id.buy_people_layout);
        mAddButton = (Button) findViewById(R.id.add_button);
        mTotalPrice = (TextView) findViewById(R.id.total_price);
        mTotalCount = (TextView) findViewById(R.id.total_order_count);
        mBuyTicket = (RelativeLayout) findViewById(R.id.buy_ticket);
        mDatePicker = (DatePicker) findViewById(R.id.datepicker);
        mConfirmButton = (Button) findViewById(R.id.confirm);
        mBeginPlayTime = (RelativeLayout) findViewById(R.id.begin_play_time_layout);
        mBeginPlayDate = (TextView) findViewById(R.id.begin_play_date);
        
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mBuyNotice.setOnClickListener(this);
        mPlayDate.setOnClickListener(this);
        mAddButton.setOnClickListener(this);
        mBuyTicket.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);
        mBeginPlayDate.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.place_order_title);
        Intent intent = getIntent();
        mProductInfo = (ProductInfo) intent.getSerializableExtra("ProductInfo");
        mTicketName.setText(intent.getStringExtra("name"));
        mTicketLocation.setText(intent.getStringExtra("location"));
        mTicketType.setText(mProductInfo.getProductName());
        mSinglePrice.setText(mProductInfo.getRetailPrice());
        mIsUnique = mProductInfo.getIsUnique();
        
        if ("1".equals(mProductInfo.getIsPackage()) && "1".equals(mProductInfo.getTheatre())) {
            mBeginPlayTime.setVisibility(View.VISIBLE);
        } else {
            mBeginPlayTime.setVisibility(View.GONE);
        }

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

        String chooseMonth = String.valueOf(mMonth);
        if (chooseMonth.length() < 2) {
            chooseMonth = "0" + String.valueOf(mMonth);
        }
        String chooseDay = String.valueOf(mDay);
        if (chooseDay.length() < 2) {
            chooseDay = "0" + String.valueOf(mDay);
        }
        mPlayDate.setText(mYear + "-" + chooseMonth + "-" + chooseDay);
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {
        SaveOrderMessage saveOrderMessage = (SaveOrderMessage) job.getBaseType();
        String code = saveOrderMessage.getCode();
        if ("10000".equals(code)) {
            String token = saveOrderMessage.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            Intent intent = new Intent(this, ChoosePayActivity.class);
            intent.putExtra("name", mProductInfo.getProductName());
            intent.putExtra("count", String.valueOf(mCount));
            intent.putExtra("price", mProductInfo.getRetailPrice());
            intent.putExtra("total", String.valueOf(mCount * Float.parseFloat(mProductInfo.getRetailPrice())));
            intent.putExtra("orderId", saveOrderMessage.getOrderId());
            intent.putExtra("des", mProductInfo.getNotice());
            intent.putExtra("isUnique", mIsUnique);
            startActivity(intent);
        } else {
            if ("10004".equals(code)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.SAVE_ORDER_CODE);
            }

            ToastUtil.showToast(saveOrderMessage.getMessage());
        }
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.SAVE_ORDER_CODE) {
                saveOrderRequest();
            }
        }
        super.onActivityResult(requestCode, responseCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_top_button:
                finish();
                break;
            case R.id.pay_notice:
                break;
            case R.id.play_date:
                mIsShowTime = true;
                mDatePicker.setVisibility(View.VISIBLE);
                mConfirmButton.setVisibility(View.VISIBLE);
                break;
            case R.id.begin_play_date:
                mIsShowTime = false;
                mDatePicker.setVisibility(View.VISIBLE);
                mConfirmButton.setVisibility(View.VISIBLE);
                break;
            case R.id.add_button:
                if ("0".equals(mIsUnique)) {
                    if (mCount == 1) {
                        ToastUtil.showToast("当前的票只能用一个人身份证购买");
                        return;
                    }
                }
                
                String name = mBuyPeopleName.getText().toString();
                String tel = mBuyPeopleTel.getText().toString();
                String card = mBuyPeopleCard.getText().toString();
                if (TextUtils.isEmpty(name)
                        || TextUtils.isEmpty(tel) ||
                        TextUtils.isEmpty(card)) {
                    ToastUtil.showToast(R.string.message_not_enough);
                    return;
                }

                if (!IdcardUtils.isMobileNO(tel)) {
                    ToastUtil.showToast("手机号不正确，请重新输入");
                    return;
                }

                String isRightCard = "";
                try {
                    isRightCard = IdcardUtils.IDCardValidate(card);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                    isRightCard = "身份证号码有误，请重新输入";
                }

                if (!"".equals(isRightCard)) {
                    ToastUtil.showToast(isRightCard);
                    return;
                }

                mCount++;
                mBuyCount.setText(String.valueOf(mCount));
                mTotalCount.setText(String.valueOf(mCount));
                mTotalPrice.setText(String.valueOf(mCount
                        * Float.parseFloat(mProductInfo.getRetailPrice())));
                mNameSets.add(name);
                mTelSets.add(tel);
                mCardSets.add(card);

                mLastIndex = mIndex;
                mIndex = addBuyPeople(name, tel, card, mLastIndex);
                mBuyPeopleName.setText("");
                mBuyPeopleTel.setText("");
                mBuyPeopleCard.setText("");
                break;
            case R.id.buy_ticket:
                if (mCount == 0) {

                } else {
                    mBuyTicketPopupWindow = new BuyTicketPopupWindow(this, confirmClickListener);
                    mBuyTicketPopupWindow.showAsDropDown(mTxtTitle);
                }
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
                
                if (mIsShowTime) {
                    mPlayDate.setText(mYear + "-" + chooseMonth + "-" + chooseDay);
                } else {
                    mBeginPlayDate.setText(mYear + "-" + chooseMonth + "-" + chooseDay);
                }
                
                mDatePicker.setVisibility(View.INVISIBLE);
                mConfirmButton.setVisibility(View.INVISIBLE);
                break;

            default:
                break;
        }
    }

    private OnClickListener confirmClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            mBuyTicketPopupWindow.dismiss();
            saveOrderRequest();
        }
    };

    private void saveOrderRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "saveOrder");
            jsonObject.put("startTime", mPlayDate.getText().toString());
            if (!TextUtils.isEmpty(mBeginPlayDate.getText().toString())) {
                jsonObject.put("showTime", mBeginPlayDate.getText().toString()); 
            }
            jsonObject.put("isAuth", "0");
            jsonObject.put("priceId", mProductInfo.getPriceId());
            jsonObject.put("price", mProductInfo.getPrice());
            jsonObject.put("cPaidAmount",
                    String.valueOf(mCount * Float.parseFloat(mProductInfo.getRetailPrice())));
            jsonObject.put("productID", mProductInfo.getProductId());
            jsonObject.put("buyerIDCard", connectHashSet(mCardSets));
            jsonObject.put("buyerMobile", connectHashSet(mTelSets));
            jsonObject.put("buyerName", connectHashSet(mNameSets));
            jsonObject.put("ticketNum", String.valueOf(mCount));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new SaveOrderMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.doRequest();
        }
    }

    private int addBuyPeople(final String userName, final String userTel, final String userCrad,
            int index) {
        int initIndex = index;
        final RelativeLayout rl = new RelativeLayout(this);
        index++;
        int left = getResources().getDimensionPixelSize(R.dimen.user_left);
        TextView tx1 = new TextView(this);
        tx1.setText(R.string.user_name);
        tx1.setTextColor(Color.parseColor("#979797"));
        tx1.setTextSize(16);
        tx1.setId(index);
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(left, getResources().getDimensionPixelSize(R.dimen.user_name_top), 0, 0);
        rl.addView(tx1, lp1);

        index++;
        TextView tx2 = new TextView(this);
        tx2.setText(userName);
        tx2.setTextColor(Color.parseColor("#979797"));
        tx2.setTextSize(16);
        tx2.setId(index);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(getResources().getDimensionPixelSize(R.dimen.name_left), getResources()
                .getDimensionPixelSize(R.dimen.user_name_top), 0, 0);
        lp2.addRule(RelativeLayout.RIGHT_OF, index - 1);
        rl.addView(tx2, lp2);

        index++;
        TextView tx3 = new TextView(this);
        tx3.setText(R.string.connect_people_tel);
        tx3.setTextColor(Color.parseColor("#979797"));
        tx3.setTextSize(16);
        tx3.setId(index);
        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp3.setMargins(left, getResources().getDimensionPixelSize(R.dimen.user_name_top), 0, 0);
        lp3.addRule(RelativeLayout.BELOW, index - 1);
        rl.addView(tx3, lp3);

        index++;
        TextView tx4 = new TextView(this);
        tx4.setText(userTel);
        tx4.setTextColor(Color.parseColor("#979797"));
        tx4.setTextSize(16);
        tx4.setId(index);
        RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp4.setMargins(getResources().getDimensionPixelSize(R.dimen.tel_left), getResources()
                .getDimensionPixelSize(R.dimen.user_name_top), 0, 0);
        lp4.addRule(RelativeLayout.BELOW, index - 2);
        lp4.addRule(RelativeLayout.RIGHT_OF, index - 1);
        rl.addView(tx4, lp4);

        index++;
        TextView tx5 = new TextView(this);
        tx5.setText(R.string.connect_people_card);
        tx5.setTextColor(Color.parseColor("#979797"));
        tx5.setTextSize(16);
        tx5.setId(index);
        RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp5.setMargins(left, getResources().getDimensionPixelSize(R.dimen.user_name_top), 0, 0);
        lp5.addRule(RelativeLayout.BELOW, index - 1);
        rl.addView(tx5, lp5);

        index++;
        TextView tx6 = new TextView(this);
        tx6.setText(userCrad);
        tx6.setTextColor(Color.parseColor("#979797"));
        tx6.setTextSize(16);
        tx6.setId(index);
        RelativeLayout.LayoutParams lp6 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp6.setMargins(getResources().getDimensionPixelSize(R.dimen.tel_left), getResources()
                .getDimensionPixelSize(R.dimen.user_name_top), 0, 0);
        lp6.addRule(RelativeLayout.BELOW, index - 2);
        lp6.addRule(RelativeLayout.RIGHT_OF, index - 1);
        rl.addView(tx6, lp6);

        Button bt = new Button(this);
        bt.setBackgroundResource(R.drawable.manage_button_layout);
        bt.setText(R.string.delete_user);
        bt.setGravity(Gravity.CENTER);
        bt.setTextColor(Color.parseColor("#ffffff"));
        bt.setTextSize(16);
        RelativeLayout.LayoutParams lp7 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp7.setMargins(0, getResources().getDimensionPixelSize(R.dimen.delete_top), getResources()
                .getDimensionPixelSize(R.dimen.delete_right), 0);
        lp7.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rl.addView(bt, lp7);

        View v1 = new View(this);
        v1.setBackgroundResource(R.drawable.me_long_line);
        RelativeLayout.LayoutParams lp8 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 1);
        lp8.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rl.addView(v1, lp8);

        View v2 = new View(this);
        v2.setBackgroundResource(R.drawable.me_long_line);
        RelativeLayout.LayoutParams lp9 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 1);
        lp9.setMargins(0, getResources().getDimensionPixelSize(R.dimen.bottom_line_top), 0, 0);
        lp9.addRule(RelativeLayout.BELOW, index);
        rl.addView(v2, lp9);

        View v3 = new View(this);
        v3.setBackgroundColor(Color.parseColor("#f4f8ff"));
        RelativeLayout.LayoutParams lp10 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(
                        R.dimen.bottom_height));
        lp10.setMargins(0, getResources().getDimensionPixelSize(R.dimen.bottom_line_top), 0, 0);
        lp10.addRule(RelativeLayout.BELOW, index);
        rl.addView(v3, lp10);

        bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIndex == rl.getId()) {
                    mIndex = mLastIndex;
                }
                mBuyPeopleLayout.removeView(rl);
                mCount--;
                mBuyCount.setText(String.valueOf(mCount));
                mTotalCount.setText(String.valueOf(mCount));
                mTotalPrice.setText(String.valueOf(mCount
                        * Float.parseFloat(mProductInfo.getRetailPrice())));
                mNameSets.remove(userName);
                mTelSets.remove(userTel);
                mCardSets.remove(userCrad);
            }
        });

        rl.setId(index);

        if (initIndex != 0) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.BELOW, initIndex);
            mBuyPeopleLayout.addView(rl, lp);
        } else {
            mBuyPeopleLayout.addView(rl);
        }
        return index;
    }

    private String connectHashSet(HashSet<String> sets) {
        return TextUtils.join(",", sets);
    }
}

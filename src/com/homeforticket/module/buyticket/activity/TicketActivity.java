
package com.homeforticket.module.buyticket.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.framework.pullrefrash.PullToRefreshBase.Mode;
import com.homeforticket.framework.pullrefrash.PullToRefreshListView;
import com.homeforticket.module.buyticket.adapter.TicketTypeAdapter;
import com.homeforticket.module.buyticket.adapter.TopPagerAdapter;
import com.homeforticket.module.buyticket.model.SceneInfo;
import com.homeforticket.module.buyticket.model.SceneTicketInfo;
import com.homeforticket.module.buyticket.parser.SceneInfoParser;
import com.homeforticket.module.buyticket.parser.SceneTicketInfoParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

/**
 * @Title: TicketActivity.java
 * @Package com.homeforticket.module.buyticket.activity
 * @Description: TODO
 * @author LR
 * @date 2015年5月2日 下午11:31:20
 */
public class TicketActivity extends BaseActivity implements OnClickListener, RequestListener, OnItemClickListener {
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private RelativeLayout mImmediatelyBuyButton;
    private TextView mScenicName;
    private TextView mScenicLevel;
    private TextView mCurrentPrice;
    private TextView mOriginalPrice;
    private TextView mLocationCity;
    private TextView mLocationAddress;
    private TextView mScenicIntroduceContent;
    private RelativeLayout mBuyTicket;
    private RelativeLayout mScenicIntroduce;
    private Button mShareButton;
    private ImageView mBuyTicketBottomLine;
    private TextView mBuyTicketText;
    private ImageView mScenicIntroduceBottomLine;
    private TextView mScenicIntroduceText;
    private TextView mCommissionTitle;
    private RelativeLayout mScenicIntroduceLayout;

    private String mId;
    private String mCurrentPriceCount;
    private String mOriginalPriceCount;

    private ViewPager mViewPager;
    private TopPagerAdapter mTopAdapter;
    private LinearLayout mLayoutDots;
    private View[] mDots; // 底部小点
    private int mCurrentIndex; // 当前页
    private PullToRefreshListView mTicketListView;
    private TicketTypeAdapter mTicketTypeAdapter;
    private String mProductType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mShareButton = (Button) findViewById(R.id.right_button);
        mShareButton.setVisibility(View.INVISIBLE);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_top);
        mLayoutDots = (LinearLayout) findViewById(R.id.layout_top_dots);
        mTopAdapter = new TopPagerAdapter(this);
        mViewPager.setAdapter(mTopAdapter);

        mImmediatelyBuyButton = (RelativeLayout) findViewById(R.id.bottom_button);
        mScenicName = (TextView) findViewById(R.id.scenic_name);
        mScenicLevel = (TextView) findViewById(R.id.scenic_level);
        mCurrentPrice = (TextView) findViewById(R.id.current_price);
        mOriginalPrice = (TextView) findViewById(R.id.original_price);
        mOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mLocationCity = (TextView) findViewById(R.id.location_city);
        mLocationAddress = (TextView) findViewById(R.id.location_address);
        mScenicIntroduceContent = (TextView) findViewById(R.id.scenic_introduce_content);
        mBuyTicket = (RelativeLayout) findViewById(R.id.buy_ticket);
        mScenicIntroduce = (RelativeLayout) findViewById(R.id.scenic_introduce);
        mBuyTicketBottomLine = (ImageView) findViewById(R.id.buy_ticket_bottom_line);
        mBuyTicketText = (TextView) findViewById(R.id.buy_ticket_text);
        mScenicIntroduceBottomLine = (ImageView) findViewById(R.id.scenic_introduce_bottom_line);
        mScenicIntroduceText = (TextView) findViewById(R.id.scenic_introduce_text);
        mScenicIntroduceLayout = (RelativeLayout) findViewById(R.id.scenic_introduce_layout);
        mScenicIntroduceContent.setMovementMethod(ScrollingMovementMethod.getInstance());

        mTicketListView = (PullToRefreshListView) findViewById(R.id.ticket_type_list);
        mTicketTypeAdapter = new TicketTypeAdapter(this);
        mCommissionTitle = (TextView) findViewById(R.id.commission_title);
        mCommissionTitle.setVisibility(View.GONE);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mShareButton.setOnClickListener(this);
        mImmediatelyBuyButton.setOnClickListener(this);
        mBuyTicket.setOnClickListener(this);
        mScenicIntroduce.setOnClickListener(this);
        mTicketListView.setOnItemClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        mCurrentPriceCount = intent.getStringExtra("current");
        mOriginalPriceCount = intent.getStringExtra("original");
        mProductType = intent.getStringExtra("productType");

        mTxtTitle.setText(R.string.ticket_title);
        mShareButton.setText(R.string.share_text);

        mTicketListView.setAdapter(mTicketTypeAdapter);
        mTicketListView.setMode(Mode.DISABLED);

        doQuerySceneContent();
    }

    private void doQuerySceneContent() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "queryScene");
            jsonObject.put("sceneId", mId);
            jsonObject.put("productType", mProductType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new SceneInfoParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.doRequest();
        }
    }

    @Override
    public void onStartRequest(RequestJob job) {

    }

    @Override
    public void onSuccess(RequestJob job) {
        SceneInfo sceneInfo = (SceneInfo) job.getBaseType();
        String code = sceneInfo.getCode();
        if ("10000".equals(code)) {
            String token = sceneInfo.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }

            mScenicName.setText(sceneInfo.getSceneName());
            mScenicLevel.setText(sceneInfo.getSceneLevel());
            mCurrentPrice.setText(getString(R.string.price_sign) + mCurrentPriceCount);
            mOriginalPrice.setText(getString(R.string.original_price_title) + mOriginalPriceCount);
            mScenicIntroduceContent.setText(sceneInfo.getSceneDescription());
            mLocationCity.setText(sceneInfo.getProvice() + sceneInfo.getCity()
                    + sceneInfo.getCounty());
            mLocationAddress.setText(sceneInfo.getSceneAddress());

            List<SceneTicketInfo> list = new ArrayList<SceneTicketInfo>();

            try {
                JSONArray ticketArray = new JSONArray(sceneInfo.getList());
                for (int i = 0, size = ticketArray.length(); i < size; i++) {
                    SceneTicketInfo info = new SceneTicketInfoParser().parseInner(ticketArray
                            .getString(i));
                    list.add(info);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (list.size() > 0) {
                mTicketTypeAdapter.setList(list);
                mTicketTypeAdapter.notifyDataSetChanged();
            }

            initDots(sceneInfo.getSceneImage());
        } else {
            if ("10004".equals(code)) {
                SharedPreferencesUtil.saveBoolean(SysConstants.IS_LOGIN, false);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, SysConstants.GET_SCENEINFO_CODE);
            }
            
            ToastUtil.showToast(sceneInfo.getMessage());
        }
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (responseCode == SysConstants.REQUEST_TYPE_LOGIN) {
            if (requestCode == SysConstants.BUY_TICKET_CODE) {
                doQuerySceneContent();
            }
        }
        super.onActivityResult(requestCode, responseCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.left_top_button:
                finish();
                break;
            case R.id.right_button:
                break;
            case R.id.bottom_button:
                SceneTicketInfo ticketInfo = null;
                if (mTicketTypeAdapter != null && mTicketTypeAdapter.getList() != null) {
                    ticketInfo = mTicketTypeAdapter.getList().get(mTicketTypeAdapter.getChooseIndex());
                }
                
                if (ticketInfo != null) {
                    Intent intent = new Intent(this, ProductActivity.class);
                    intent.putExtra("SceneTicketInfo", ticketInfo);
                    intent.putExtra("name", mScenicName.getText().toString());
                    intent.putExtra("location", mLocationCity.getText().toString());
                    startActivity(intent);
                }
                break;
            case R.id.buy_ticket:
                mBuyTicketBottomLine.setVisibility(View.VISIBLE);
                mBuyTicketText.setTextColor(Color.parseColor("#1db2f6"));
                mScenicIntroduceBottomLine.setVisibility(View.INVISIBLE);
                mScenicIntroduceText.setTextColor(Color.parseColor("#979797"));
                mScenicIntroduceLayout.setVisibility(View.INVISIBLE);
                mScenicIntroduceContent.setVisibility(View.INVISIBLE);
                mTicketListView.setVisibility(View.VISIBLE);
                break;
            case R.id.scenic_introduce:
                mBuyTicketBottomLine.setVisibility(View.INVISIBLE);
                mBuyTicketText.setTextColor(Color.parseColor("#979797"));
                mScenicIntroduceBottomLine.setVisibility(View.VISIBLE);
                mScenicIntroduceText.setTextColor(Color.parseColor("#1db2f6"));
                mScenicIntroduceLayout.setVisibility(View.VISIBLE);
                mScenicIntroduceContent.setVisibility(View.VISIBLE);
                mTicketListView.setVisibility(View.INVISIBLE);
                break;
        }
    }

    // 初始化底部小点
    private void initDots(String[] sceneImage) {

        mTopAdapter.setmSceneImage(sceneImage);
        mTopAdapter.notifyDataSetChanged();

        int size = sceneImage.length;
        if (size == 1) {
            mLayoutDots.setVisibility(View.GONE);
        } else {
            mDots = new View[size];
            LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);

            // 循环取得小点图片
            for (int i = 0; i < size; i++) {
                ImageView iv = new ImageView(this);
                iv.setBackgroundResource(R.drawable.top_dot_unselected);
                iv.setLayoutParams(params);
                mLayoutDots.addView(iv);
                mDots[i] = iv;
                mDots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
            }
            mCurrentIndex = 0;
            setCurDot(mCurrentIndex);
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                setCurDot(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    // 设置当前选中小点状态
    private void setCurDot(int position) {

        if (position < 0 || position > mDots.length - 1) {
            return;
        }

        for (int i = 0; i < mDots.length; i++) {
            mDots[i].setBackgroundResource(R.drawable.top_dot_unselected);
        }

        mDots[position].setBackgroundResource(R.drawable.top_dot_selected);
        mCurrentIndex = position;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mTicketTypeAdapter.setChooseIndex(position - 1);
        mTicketTypeAdapter.notifyDataSetChanged();
    }

}

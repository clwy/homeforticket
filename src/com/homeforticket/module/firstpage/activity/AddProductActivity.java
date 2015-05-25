
package com.homeforticket.module.firstpage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homeforticket.R;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;

/**
 * @Title: AddProductActivity.java
 * @Package com.homeforticket.module.firstpage.activity
 * @Description: TODO
 * @author LR
 * @date 2015年5月14日 上午8:19:46
 */
public class AddProductActivity extends BaseActivity implements OnClickListener, RequestListener {
    private TextView mTxtTitle;
    private RelativeLayout mBtnBack;
    private RelativeLayout mAddOtherProduct;
    private RelativeLayout mAddSelfProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.top_title);
        mBtnBack = (RelativeLayout) findViewById(R.id.left_top_button);
        mAddOtherProduct = (RelativeLayout) findViewById(R.id.add_other_product_layout);
        mAddSelfProduct = (RelativeLayout) findViewById(R.id.add_self_product_layout);
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mAddOtherProduct.setOnClickListener(this);
        mAddSelfProduct.setOnClickListener(this);
    }

    private void initData() {
        mTxtTitle.setText(R.string.add_product_title);
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
            case R.id.add_other_product_layout:
                Intent intent = new Intent(this, ProductNavigationManageActivity.class);
                intent.putExtra("from", "often");
                startActivity(intent);
                break;
            case R.id.add_self_product_layout:
                startActivity(new Intent(this, AddProductContentActivity.class));
                break;
        }
    }
}

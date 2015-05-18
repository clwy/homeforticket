package com.homeforticket.module.firstpage.view;

import com.homeforticket.R;
import com.homeforticket.module.firstpage.activity.OftenBehalfActivity;
import com.homeforticket.module.firstpage.activity.ProductNavigationManageActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @Title: BuyTicketPopupWindow.java 
 * @Package com.homeforticket.module.buyticket.view 
 * @Description: TODO 
 * @author LR   
 * @date 2015年5月7日 下午5:15:34 
 */
public class ChooseBehalfPopupWindow extends PopupWindow implements OnClickListener {
    private Context mContext;
    private View mView;
    private RelativeLayout mCenterLayout;
    private ImageView mCancel;
    private RelativeLayout mBehalfLayout;
    private RelativeLayout mStoreLayout;
    
    public ChooseBehalfPopupWindow(Context context) {
        super(context);
        mContext = context;
        
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.popwindow_behalf, null);
        
        mCenterLayout = (RelativeLayout) mView.findViewById(R.id.center_layout);
        mCenterLayout.setOnClickListener(this);
        mCancel = (ImageView) mView.findViewById(R.id.cancel);
        mCancel.setOnClickListener(this);
        mBehalfLayout = (RelativeLayout) mView.findViewById(R.id.behalf_layout);
        mBehalfLayout.setOnClickListener(this);
        mStoreLayout = (RelativeLayout) mView.findViewById(R.id.store_navigation_layout);
        mStoreLayout.setOnClickListener(this);
        
        this.setContentView(mView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
            case R.id.center_layout:
                dismiss();
                break;
            case R.id.behalf_layout:
                dismiss();
                mContext.startActivity(new Intent(mContext, OftenBehalfActivity.class));
                break;
            case R.id.store_navigation_layout:
                dismiss();
                mContext.startActivity(new Intent(mContext, ProductNavigationManageActivity.class));
                break;

            default:
                break;
        }
    }

}

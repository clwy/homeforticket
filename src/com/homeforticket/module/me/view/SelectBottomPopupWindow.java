
package com.homeforticket.module.me.view;

import com.homeforticket.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

public class SelectBottomPopupWindow extends PopupWindow implements OnClickListener
{

    private Button mCancelBtn;
    private Button mTakePhoto;
    private Button mTakeLocal;
    private View mMenuView;

    public SelectBottomPopupWindow(Context context, OnClickListener system, OnClickListener camera)
    {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popwindow_bottom_dialog, null);
        mTakePhoto = (Button) mMenuView.findViewById(R.id.take_photo);
        mCancelBtn = (Button) mMenuView.findViewById(R.id.btn_cancel);
        mTakeLocal = (Button) mMenuView.findViewById(R.id.take_local);

        mCancelBtn.setOnClickListener(this);
        mTakePhoto.setOnClickListener(camera);
        mTakeLocal.setOnClickListener(system);

        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);

        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }

                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;

            default:
                break;
        }

    }

}

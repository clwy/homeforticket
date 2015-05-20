
package com.homeforticket.common.share;

import java.io.ByteArrayOutputStream;

import com.homeforticket.R;
import com.homeforticket.util.ToastUtil;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * @Title: ShareUtils.java
 * @Package com.homeforticket.common.share
 * @Description: TODO
 * @author LR
 * @date 2015年5月20日 下午1:07:17
 */
public class ShareUtilsView extends PopupWindow implements OnClickListener {
    public static final String WEIXIN_APPID = "wx30ad8acd84d14334";
    public static final String WEIXIN_KEY = "e691e9f89363d72458aaf25481e5f570";
    private View mMenuView;
    private Button mCancelBtn;
    private TextView mShareToFriend;
    private TextView mShareToWeixin;
    private TextView mShareToWeibo;
    private Context mContext;
    private String mUrl;
    private String mTitle;
    private String mContent;
    private String mPath;

    public ShareUtilsView(Context context, String url, String title, String content, String path) {
        super(context);
        mContext = context;
        mUrl = url;
        mTitle = title;
        mContent = content;
        mPath = path;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popwindow_share_dialog, null);
        mCancelBtn = (Button) mMenuView.findViewById(R.id.btn_cancel);
        mCancelBtn.setOnClickListener(this);
        mShareToFriend = (TextView) mMenuView.findViewById(R.id.bt_circle);
        mShareToWeixin = (TextView) mMenuView.findViewById(R.id.bt_weixin);
        mShareToWeibo = (TextView) mMenuView.findViewById(R.id.bt_sina);
        mShareToFriend.setOnClickListener(this);
        mShareToWeixin.setOnClickListener(this);
        mShareToWeibo.setOnClickListener(this);

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
            case R.id.bt_circle:
                shareToWeiXin(false);
                break;
            case R.id.bt_weixin:
                shareToWeiXin(true);
                break;
            case R.id.bt_sina:
                startShareWeibo();
                break;

            default:
                break;
        }
    }
    
    /**
     * 分享到微信和朋友圈
     * 
     * @param tag true分享到微信会话， false分享到朋友圈
     */
    private void shareToWeiXin(boolean tag) {
        IWXAPI api = WXAPIFactory.createWXAPI(mContext, WEIXIN_APPID, false);
        if (api.isWXAppInstalled()) {
            api.registerApp(WEIXIN_APPID);

            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = mUrl;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = mTitle;
            msg.description = mContent;

            if (!TextUtils.isEmpty(mPath)) {
                Bitmap oriBitmap = BitmapFactory.decodeFile(mPath);
                if (null != oriBitmap && !oriBitmap.isRecycled()) {
                    Bitmap bitmap = null;
                    WXImageObject imgObj = new WXImageObject(oriBitmap);
                    msg.mediaObject = imgObj;
                } else {
                    Bitmap thumb = BitmapFactory
                            .decodeResource(mContext.getResources(), R.drawable.logo);
                    msg.thumbData = bmpToByteArray(thumb, true);
                    thumb.recycle();
                }
            }

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = msg;
            req.scene = tag ? SendMessageToWX.Req.WXSceneSession
                    : SendMessageToWX.Req.WXSceneTimeline;
            api.sendReq(req);
        } else {
            ToastUtil.showToast("未安装微信客户端，请确认");
        }
    }

    public static byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    
    private void startShareWeibo() {
        Intent intent = new Intent(mContext, ShareWeiboActivity.class);
        intent.putExtra("url", mUrl);
        intent.putExtra("title", mTitle);
        intent.putExtra("content", mContent);
        intent.putExtra("path", mPath);
        mContext.startActivity(intent);
    }
}

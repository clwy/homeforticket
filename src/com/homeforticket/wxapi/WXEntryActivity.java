
package com.homeforticket.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.homeforticket.common.share.ShareUtilsView;
import com.homeforticket.util.ToastUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    IWXAPI mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = WXAPIFactory
                .createWXAPI(getApplicationContext(), ShareUtilsView.WEIXIN_APPID, false);
        mApi.handleIntent(getIntent(), this);
        
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq arg0) {
    }

    @Override
    public void onResp(BaseResp resp) {
        String result = "";
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "分享取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "分享拒绝";
                break;
        }

        ToastUtil.showToast(result);
        this.finish();
    }

}

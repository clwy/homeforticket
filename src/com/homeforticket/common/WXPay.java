
package com.homeforticket.common;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.homeforticket.R;
import com.homeforticket.util.MD5;
import com.homeforticket.util.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * @Title: WXPay.java
 * @Package com.homeforticket.common
 * @Description: TODO
 * @author LR
 * @date 2015年5月16日 上午9:03:47
 */
public class WXPay {
    private static final String PARTNER_KEY = "d927506efd4a431b1ab75be3303befba";
    private static final String APP_SECRET = "e691e9f89363d72458aaf25481e5f570";
    private static final String APP_KEY = "pzjGvP3o6VWzbnyRCBltrxtScWILf3ql";
    private static final String PARTNER_ID = "1220934701";
    private static final String WX_APPID = "wx30ad8acd84d14334";

    private Context mContext;
    
    private IWXAPI api;
    private static enum LocalRetCode {
        ERR_OK, ERR_HTTP, ERR_JSON, ERR_OTHER
    }
    private long timeStamp;
    private String nonceStr, packageValue; 
    private String mName;
    private String mUrl;
    private String mPayTotal;

    public WXPay(Context context, String name, String url, String payTotal) {
        mContext = context;
        mName = name;
        mUrl = url;
        mPayTotal = payTotal;
        api = WXAPIFactory.createWXAPI(mContext, WX_APPID);
        api.registerApp(WX_APPID);
        
        new GetAccessTokenTask().execute();
    }

    private class GetAccessTokenTask extends AsyncTask<Void, Void, GetAccessTokenResult> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(mContext, mContext.getString(R.string.app_tip),
                    mContext.getString(R.string.getting_access_token));
        }

        @Override
        protected void onPostExecute(GetAccessTokenResult result) {
            if (dialog != null) {
                dialog.dismiss();
            }

            if (result.localRetCode == LocalRetCode.ERR_OK) {
                Toast.makeText(mContext, R.string.get_access_token_succ,
                        Toast.LENGTH_LONG).show();

                GetPrepayIdTask getPrepayId = new GetPrepayIdTask(result.accessToken);
                getPrepayId.execute();
            } else {
                Toast.makeText(mContext,
                        mContext.getString(R.string.get_access_token_fail, result.localRetCode.name()),
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected GetAccessTokenResult doInBackground(Void... params) {
            GetAccessTokenResult result = new GetAccessTokenResult();

            String url = String
                    .format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                            WX_APPID, APP_SECRET);

            byte[] buf = Util.httpGet(url);
            if (buf == null || buf.length == 0) {
                result.localRetCode = LocalRetCode.ERR_HTTP;
                return result;
            }

            String content = new String(buf);
            result.parseFrom(content);
            return result;
        }
    }

    private static class GetAccessTokenResult {
        public LocalRetCode localRetCode = LocalRetCode.ERR_OTHER;
        public String accessToken;
        public int expiresIn;
        public int errCode;
        public String errMsg;

        public void parseFrom(String content) {

            if (content == null || content.length() <= 0) {
                localRetCode = LocalRetCode.ERR_JSON;
                return;
            }

            try {
                JSONObject json = new JSONObject(content);
                if (json.has("access_token")) { // success case
                    accessToken = json.getString("access_token");
                    expiresIn = json.getInt("expires_in");
                    localRetCode = LocalRetCode.ERR_OK;
                } else {
                    errCode = json.getInt("errcode");
                    errMsg = json.getString("errmsg");
                    localRetCode = LocalRetCode.ERR_JSON;
                }

            } catch (Exception e) {
                localRetCode = LocalRetCode.ERR_JSON;
            }
        }
    }

    private class GetPrepayIdTask extends AsyncTask<Void, Void, GetPrepayIdResult> {

        private ProgressDialog dialog;
        private String accessToken;

        public GetPrepayIdTask(String accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(mContext, mContext.getString(R.string.app_tip),
                    mContext.getString(R.string.getting_prepayid));
        }

        @Override
        protected void onPostExecute(GetPrepayIdResult result) {
            if (dialog != null) {
                dialog.dismiss();
            }

            if (result.localRetCode == LocalRetCode.ERR_OK) {
                Toast.makeText(mContext, R.string.get_prepayid_succ,
                        Toast.LENGTH_LONG).show();
                sendPayReq(result);
            } else {
                Toast.makeText(mContext,
                        mContext.getString(R.string.get_prepayid_fail, result.localRetCode.name()),
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected GetPrepayIdResult doInBackground(Void... params) {

            String url = String.format("https://api.weixin.qq.com/pay/genprepay?access_token=%s",
                    accessToken);
            String entity = genProductArgs();

            GetPrepayIdResult result = new GetPrepayIdResult();

            byte[] buf = Util.httpPost(url, entity);
            if (buf == null || buf.length == 0) {
                result.localRetCode = LocalRetCode.ERR_HTTP;
                return result;
            }

            String content = new String(buf);
            result.parseFrom(content);
            return result;
        }
    }

    private static class GetPrepayIdResult {

        public LocalRetCode localRetCode = LocalRetCode.ERR_OTHER;
        public String prepayId;
        public int errCode;
        public String errMsg;

        public void parseFrom(String content) {

            if (content == null || content.length() <= 0) {
                localRetCode = LocalRetCode.ERR_JSON;
                return;
            }

            try {
                JSONObject json = new JSONObject(content);
                if (json.has("prepayid")) { // success case
                    prepayId = json.getString("prepayid");
                    localRetCode = LocalRetCode.ERR_OK;
                } else {
                    localRetCode = LocalRetCode.ERR_JSON;
                }

                errCode = json.getInt("errcode");
                errMsg = json.getString("errmsg");

            } catch (Exception e) {
                localRetCode = LocalRetCode.ERR_JSON;
            }
        }
    }

    private void sendPayReq(GetPrepayIdResult result) {

        PayReq req = new PayReq();
        req.appId = WX_APPID;
        req.partnerId = PARTNER_ID;
        req.prepayId = result.prepayId;
        req.nonceStr = nonceStr;
        req.timeStamp = String.valueOf(timeStamp);
        req.packageValue = "Sign=" + packageValue;

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("appkey", APP_KEY));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        req.sign = genSign(signParams);

        api.sendReq(req);
    }

    private String genSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        int i = 0;
        for (; i < params.size() - 1; i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append(params.get(i).getName());
        sb.append('=');
        sb.append(params.get(i).getValue());

        String sha1 = Util.sha1(sb.toString());
        return sha1;
    }

    private String genProductArgs() {
        JSONObject json = new JSONObject();

        try {
            json.put("appid", WX_APPID);
            String traceId = getTraceId();
            json.put("traceid", traceId);
            nonceStr = genNonceStr();
            json.put("noncestr", nonceStr);

            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("bank_type", "WX"));
            packageParams.add(new BasicNameValuePair("body", mName));
            packageParams.add(new BasicNameValuePair("fee_type", "1"));
            packageParams.add(new BasicNameValuePair("input_charset", "UTF-8"));
            packageParams.add(new BasicNameValuePair("notify_url", mUrl));
            packageParams.add(new BasicNameValuePair("out_trade_no", genOutTradNo()));
            packageParams.add(new BasicNameValuePair("partner", PARTNER_ID));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "196.168.1.1"));
            packageParams.add(new BasicNameValuePair("total_fee", mPayTotal));
            packageValue = genPackage(packageParams);

            json.put("package", packageValue);
            timeStamp = genTimeStamp();
            json.put("timestamp", timeStamp);

            List<NameValuePair> signParams = new LinkedList<NameValuePair>();
            signParams.add(new BasicNameValuePair("appid", WX_APPID));
            signParams.add(new BasicNameValuePair("appkey", APP_KEY));
            signParams.add(new BasicNameValuePair("noncestr", nonceStr));
            signParams.add(new BasicNameValuePair("package", packageValue));
            signParams.add(new BasicNameValuePair("timestamp", String.valueOf(timeStamp)));
            signParams.add(new BasicNameValuePair("traceid", traceId));
            json.put("app_signature", genSign(signParams));

            json.put("sign_method", "sha1");
        } catch (Exception e) {
            return null;
        }

        return json.toString();
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String getTraceId() {
        return "crestxu_" + genTimeStamp();
    }

    private String genOutTradNo() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private String genPackage(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(PARTNER_KEY);
        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();

        return URLEncodedUtils.format(params, "utf-8") + "&sign=" + packageSign;
    }
}


package com.homeforticket.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.homeforticket.framework.BaseActivity;
import com.homeforticket.framework.HomeTicketActivity;
import com.homeforticket.module.buyticket.activity.ChoosePayActivity;
import com.homeforticket.pay.PayResult;
import com.homeforticket.pay.SignUtils;

/**
 * @Title: ZhifubaoPay.java
 * @Package com.homeforticket.common
 * @Description: TODO
 * @author LR
 * @date 2015年5月16日 上午8:59:11
 */
public class ZhifubaoPay {
    // 商户PID
    public static final String PARTNER = "2088111558675115";
    // 商户收款账号
    public static final String SELLER = "mf@mftour.cn";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJu1SIjN8iRhmfbevrJIeLvXLLYExHyQtG2bEf3y7nbGM57nBynDAWFaoDXKNcoi0o183afcrruCFL7jnEzEX0s2y358XCs3us3qEtrRP/lqHFJYPSZI9R7KHvMcIuX1rSm953ninerX1/xI5fxsqz5yCni0eXeJaRVq56I71BKpAgMBAAECgYAIYdHty2hlnPt01rjdUBlvwz2c7tTE/+U2oUpuqU09B55vx3ATR3QjkYVeX+xVXVqIU1h7WN3vKimDvS0E1O1sQZ09sFSzJhWVakQgWiyushUDH7Qno/YYYlp72ODQbaTXxpGNC5TzlFyvEN75icOiUzy+PzNGiApTKrKMB/koAQJBAM4FEnVLWefzXWrq4hzE/ZEX9vng2xMOb6UifwVhX7JmYqc0cnTDWcyd2S9/MGI2SPgYUky0D6pdjbZ6F6LvXaECQQDBe5aUiLp4XF+sUr0Jcsss65kjyFBtNnpJWXa/V8dj+4AFMCeQifAmfc6GGNXbD6fFenWlxiadYvPkxnMxxMgJAkEAhqkwEHUJNiCPnjQbSbzFFRK5/0inSYR1G1Y5+nVUTNK5hxsz+Pg7eRBwBqi3ImrjNS8IlFRZrPzrhSu2yWx2IQJAdT/S0bJF5hh8de4x1l71VwRy6idC7dv8oSSQ/IVSNHm9a+dxonqkyH6mv8afGkBTeC2sy8fXsLq9eB72wesxGQJBAIBb+k+iuAtObtIKsiQKFJPuvaGZ4QpJ8b1Jpwy1A4pVjTE05VNmC0Nmgh3YDcgrhM7cQosQhZCo+VHnNQUuYBQ=";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbtUiIzfIkYZn23r6ySHi71yy2BMR8kLRtmxH98u52xjOe5wcpwwFhWqA1yjXKItKNfN2n3K67ghS+45xMxF9LNst+fFwrN7rN6hLa0T/5ahxSWD0mSPUeyh7zHCLl9a0pved54p3q19f8SOX8bKs+cgp4tHl3iWkVaueiO9QSqQIDAQAB";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private BaseActivity mContext;

    public ZhifubaoPay(BaseActivity context, View v, String name, String des, String payTotal, String orderId, String url) {
        mContext = context;
        pay(v, name, des, payTotal, orderId, url);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(mContext, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        mContext.startActivity(new Intent(mContext, HomeTicketActivity.class));
                        mContext.finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mContext, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mContext, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(mContext, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(View v, String title, String des, String price, String orderId, String backUrl) {
        // 订单
        String orderInfo = getOrderInfo(title, des, price, orderId, backUrl);

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mContext);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(mContext);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(mContext);
        String version = payTask.getVersion();
        Toast.makeText(mContext, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price, String orderId,
            String backUrl) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderId + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + backUrl
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     * 
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }
}

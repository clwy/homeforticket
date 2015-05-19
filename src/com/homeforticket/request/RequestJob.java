
package com.homeforticket.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.homeforticket.appApplication.AppApplication;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.model.BaseType;
import com.homeforticket.parser.Parser;
import com.homeforticket.util.MD5Utils;
import com.homeforticket.util.SharedPreferencesUtil;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;

public class RequestJob {
    private static final String TAG = "RequestJob";
    // 请求的id
    private int mRequestId;
    private String mTag;
    // 请求监听
    private RequestListener mListener;
    // 请求成功返回数据
    private BaseType mBaseType;
    // 请求的task
    private RequestTask mRequestTask;
    // 失败返回提示
    private String mFailNotice;
    // 请求的url
    private String mRequestUrl;
    // 请求的是list参数
    private List<NameValuePair> mNameValuePairs;
    private List<NameValuePair> mFilePairs;
    // 请求的解析parser
    private Parser<? extends BaseType> mParser;
    // 请求方式post/get
    private int mRequestType;

    public String getTag() {
        return mTag;
    }

    public void setTag(String s) {
        mTag = s;
    }

    public RequestJob(String requestUrl, String nameValuePairs,
            Parser<? extends BaseType> parser,
            int requestType) {
        this.setRequestUrl(requestUrl);
        this.setNameValuePairs(getNameValuePairs(nameValuePairs));
        this.setParser(parser);
        this.setRequestType(requestType);
    }
    
    public RequestJob(String requestUrl, String nameValuePairs, List<NameValuePair> filePairs,
            Parser<? extends BaseType> parser) {
        this.setRequestUrl(requestUrl);
        this.setNameValuePairs(getNameValuePairs(nameValuePairs));
        this.setParser(parser);
        this.mFilePairs = filePairs;
    }

    public void doRequest(String type) {
        setRequestTask(new RequestTask(this));
        mRequestTask.setType(type);
        if (Build.VERSION.SDK_INT < 11) {
            getRequestTask().execute();
        } else {
            getRequestTask().executeOnExecutor(AppApplication.FULL_TASK_EXECUTOR);
        }
    }
    
    public void doRequest() {
        setRequestTask(new RequestTask(this));
        if (Build.VERSION.SDK_INT < 11) {
            getRequestTask().execute();
        } else {
            getRequestTask().executeOnExecutor(AppApplication.FULL_TASK_EXECUTOR);
        }
    }

    /**
     * 开始请求
     */
    public void requestStart() {
        if (mListener != null)
            mListener.onStartRequest(this);
    }

    public void cancelRequest(Boolean mayInterruptIfRunning) {
        getRequestTask().cancel(mayInterruptIfRunning);
    }

    public void cancelRequest() {
        getRequestTask().cancel(true);
    }

    /**
     * 请求成功
     * 
     * @throws Exception
     */
    public void requestSuccess() {
        if (!getRequestTask().isCancelled()) {
            if (mListener != null) {
                mListener.onSuccess(this);
            }
        }
    }

    /**
     * 请求失败
     */
    public void requestFail() {
        if (!getRequestTask().isCancelled()) {
            if (mListener != null) {
                mListener.onFail(this);
            }
        }
    }

    public void setRequestListener(RequestListener listener) {
        mListener = listener;
    }

    /**
     * @return the mBaseType
     */
    public BaseType getBaseType() {
        return mBaseType;
    }

    /**
     * @param mBaseType the mBaseType to set
     */
    public void setBaseType(BaseType baseType) {
        this.mBaseType = baseType;
    }

    /**
     * @return the mFailNotice
     */
    public String getFailNotice() {
        return mFailNotice;
    }

    /**
     * @param mFailNotice the mFailNotice to set
     */
    public void setFailNotice(String mFailNotice) {
        this.mFailNotice = mFailNotice;
    }

    /**
     * @return the mRequestUrl
     */
    public String getRequestUrl() {
        return mRequestUrl;
    }

    /**
     * @param mRequestUrl the mRequestUrl to set
     */
    public void setRequestUrl(String mRequestUrl) {
        this.mRequestUrl = mRequestUrl;
    }

    /**
     * @return the mNameValuePairs
     */
    public List<NameValuePair> getNameValuePairs() {
        return mNameValuePairs;
    }

    /**
     * @param mNameValuePairs the mNameValuePairs to set
     */
    public void setNameValuePairs(List<NameValuePair> mNameValuePairs) {
        this.mNameValuePairs = mNameValuePairs;
    }

    /**
     * @return the mParser
     */
    public Parser<? extends BaseType> getParser() {
        return mParser;
    }

    /**
     * @param mParser the mParser to set
     */
    public void setParser(Parser<? extends BaseType> mParser) {
        this.mParser = mParser;
    }

    /**
     * @return the mRequestType
     */
    public int getRequestType() {
        return mRequestType;
    }

    /**
     * @param mRequestType the mRequestType to set
     */
    public void setRequestType(int mRequestType) {
        this.mRequestType = mRequestType;
    }

    /**
     * @return the mRequestId
     */
    public int getRequestId() {
        return mRequestId;
    }

    /**
     * @param mRequestId the mRequestId to set
     */
    public void setRequestId(int mRequestId) {
        this.mRequestId = mRequestId;
    }

    /**
     * @return the mRequestTask
     */
    public RequestTask getRequestTask() {
        return mRequestTask;
    }

    /**
     * @param mRequestTask the mRequestTask to set
     */
    public void setRequestTask(RequestTask mRequestTask) {
        this.mRequestTask = mRequestTask;
    }

    public static List<NameValuePair> getNameValuePairs(String jsonObject) {
        String data = "";
        if (jsonObject != null) {
            try {
                byte[] encode = jsonObject.getBytes("UTF-8"); 
                data = new String(Base64.encode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        
        String signString = "";
        if (SharedPreferencesUtil.readBoolean(SysConstants.IS_LOGIN, false)) {
            signString = MD5Utils.stringToMD5(data
                    + SharedPreferencesUtil.readString(SysConstants.TOKEN, SysConstants.EMPTY_STRING));
        } else {
            signString = MD5Utils.stringToMD5(data);
        }

        try {
            data = URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<NameValuePair> nameValueParams = new ArrayList<NameValuePair>();
        nameValueParams.add(new BasicNameValuePair("data", data));
        nameValueParams.add(new BasicNameValuePair("sign", signString));
        return nameValueParams;
    }

    public List<NameValuePair> getFilePairs() {
        return mFilePairs;
    }

    public void setFilePairs(List<NameValuePair> mFilePairs) {
        this.mFilePairs = mFilePairs;
    }
}

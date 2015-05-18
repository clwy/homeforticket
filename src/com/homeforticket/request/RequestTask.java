
package com.homeforticket.request;

import com.homeforticket.LogoActivity;
import com.homeforticket.R;
import com.homeforticket.appApplication.AppApplication;
import com.homeforticket.connect.QuareManager;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.framework.HomeTicketActivity;
import com.homeforticket.model.BaseType;
import com.homeforticket.model.ReturnMessage;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.util.SharedPreferencesUtil;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

public class RequestTask extends AsyncTask<Void, Integer, BaseType> {

    private RequestJob mJob;
    private String mType;

    public RequestTask(RequestJob job) {
        this.mJob = job;
    }

    @Override
    public void onPreExecute() {
        mJob.requestStart();
        super.onPreExecute();
    }

    @Override
    protected BaseType doInBackground(Void... params) {
        QuareManager qm = AppApplication.getInstance().getQuareManager();
        try {
            if (TextUtils.isEmpty(mType)) {
                return qm.doHttpRequest(mJob.getRequestUrl(), mJob.getNameValuePairs(),
                        mJob.getParser(),
                        mJob.getRequestType());
            } else if ("1".equals(mType)) {
                return qm.doUploadRequest(mJob.getRequestUrl(), mJob.getNameValuePairs(), mJob.getFilePairs(),
                        mJob.getParser());
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(BaseType result) {
        super.onPostExecute(result);

        if (result != null) {
            mJob.setBaseType(result);
            mJob.requestSuccess();
        } else {
            mJob.setFailNotice(AppApplication.getAppContext().getString(R.string.request_error));
            mJob.requestFail();
        }
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    // CODE MESSAGE
    // 10000 成功
    //10002   参数错误
    //10002   用户名或密码错误
    //10003   服务器异常
    //10004   token失效请登录
    //10005   旧密码错误
    //10006   修改信息失败
    //10007   上传文件为空
    //10008   验签失败


}

package com.homeforticket.connect;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.homeforticket.constant.SysConstants;
import com.homeforticket.model.BaseType;
import com.homeforticket.parser.Parser;

public class QuareManager {

	private static final String TAG = "QuareManager";

	public final DefaultHttpClient mHttpClient = AbstractHttpApi.createHttpClient(AbstractHttpApi.TIMEOUT);
	private final HttpApi mHttpApi;

	public QuareManager(String version) {
		mHttpApi = new HttpApiWithBasicAuth(mHttpClient, version);
	}

	// post/get请求
	public BaseType doHttpRequest(String requestUrl, List<NameValuePair> nameValuePairs,
			Parser<? extends BaseType> parser, int requestType, String token) throws Exception {
		return mHttpApi.doHttpRequest(requestUrl, nameValuePairs, parser, requestType, token);
	}

    /**
     * 文件上传
     * 
     * @param requestUrl 文件上传地址
     * @param strings 除了文件流以外的其他需要post的参数
     * @param images 参数为文件名
     * @return
     */
    public BaseType doUploadRequest(String requestUrl, List<NameValuePair> strings, List<NameValuePair> images, Parser<? extends BaseType> parser, String token)
            throws Exception {
        return mHttpApi.doUploadRequest(requestUrl, strings, images, parser, token);
    }

	public String doHttpGet(String requestUrl, List<NameValuePair> nameValuePairs, String token) throws Exception {
		return mHttpApi.doHttpGet(requestUrl, nameValuePairs, token);
	}

	public String doHttpPost(String requestUrl, List<NameValuePair> nameValuePairs, String token) throws Exception {
		return mHttpApi.doHttpPost(requestUrl, nameValuePairs, token);
	}

	public HttpPost newsHttpPost(String requestUrl, List<NameValuePair> nameValuePairs, String token) {
		HttpPost httpPost = mHttpApi.createHttpPost(SysConstants.SERVER + requestUrl, nameValuePairs, token);
		httpPost.addHeader(SysConstants.CLIENT_VERSION_HEADER, SysConstants.USER_AGENT);
		// httpPost.setHeader(SysConstants.ACCEPT_ENCODING,
		// SysConstants.ACCEPT_ENCODING_STRING);
		// httpPost.setHeader(SysConstants.ACCEPT, SysConstants.ACCEPT_STRING);
		return httpPost;
	}

	public HttpGet newsHttpGet(String requestUrl, List<NameValuePair> nameValuePairs, String token) {
		HttpGet httpGet = mHttpApi.createHttpGet(SysConstants.SERVER + requestUrl, nameValuePairs, token);
		httpGet.addHeader(SysConstants.CLIENT_VERSION_HEADER, SysConstants.USER_AGENT);
		// httpGet.setHeader(SysConstants.ACCEPT_ENCODING,
		// SysConstants.ACCEPT_ENCODING_STRING);
		// httpGet.setHeader(SysConstants.ACCEPT, SysConstants.ACCEPT_STRING);
		return httpGet;
	}

	// 清空cookie
	public void clearCookie() {
		mHttpClient.getCookieStore().clear();
	}

	public HttpGet storedownload(String url, String token) {
		HttpGet httpGet = mHttpApi.createHttpGet(url, null, token);
		httpGet.addHeader(SysConstants.CLIENT_VERSION_HEADER, SysConstants.USER_AGENT);
		httpGet.addHeader(SysConstants.ACCEPT_ENCODING, SysConstants.ACCEPT_ENCODING_STRING);
		return httpGet;
	}

	/**
	 * 离线下载
	 * 
	 * @param requestUrl
	 * @return
	 * @throws IOException
	 * @throws BaseException
	 * @throws ParseException
	 */
	public HttpEntity offlineDownLoad(String requestUrl, String token) throws Exception {
		return mHttpApi.getHttpEntity(requestUrl, token);
	}

}

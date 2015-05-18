package com.homeforticket.connect;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientUtils {
	private static final String TAG = "HttpClientUtils";
	private DefaultHttpClient mHttpClient;
	private HttpApi mHttpApi;

	public HttpClientUtils() {
		mHttpClient = AbstractHttpApi.createHttpClient(AbstractHttpApi.TIMEOUT);
		mHttpApi = new HttpApiWithBasicAuth(mHttpClient);
	}

	public HttpClientUtils(int timeout) {
		mHttpClient = AbstractHttpApi.createHttpClient(timeout);
		mHttpApi = new HttpApiWithBasicAuth(mHttpClient);
	}

	public DefaultHttpClient getHttpClient(int timeout) {
		return mHttpClient;
	}

	public HttpApi getHttpApi(DefaultHttpClient httpClient) {
		return mHttpApi;
	}

	/**
	 * 文件上传
	 * 
	 * @param requestUrl 文件上传地址
	 * @param strings 除了文件流以外的其他需要post的参数
	 * @param images 文件键值对
	 * @return
	 */
	public String imgUpload(String requestUrl, List<NameValuePair> strings, List<NameValuePair> images)
			throws Exception {
		return mHttpApi.doUploadRequest(requestUrl, strings, images);
	}

	/**
	 * 离线下载
	 * 
	 * @param requestUrl
	 * @return
	 */
	public HttpEntity offlineDownLoad(String requestUrl) throws Exception {
		return mHttpApi.getHttpEntity(requestUrl);
	}
}

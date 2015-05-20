package com.homeforticket.connect;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import com.homeforticket.model.BaseType;
import com.homeforticket.parser.Parser;

public interface HttpApi {

	abstract public BaseType doHttpRequest(String requestUrl, List<NameValuePair> nameValuePairs,
			Parser<? extends BaseType> parser, int requestType, String token) throws Exception;

	abstract public String doHttpPost(String url, List<NameValuePair> nameValuePairs, String token) throws Exception;

	abstract public String doHttpGet(String url, List<NameValuePair> nameValuePairs, String token) throws Exception;

	abstract public HttpPost createHttpPost(String url, List<NameValuePair> nameValuePairs, String token);

	abstract public HttpGet createHttpGet(String url, List<NameValuePair> nameValuePairs, String token);

	abstract public HttpEntity getHttpEntity(String url, String token) throws Exception;

	public String doUploadRequest(String requestUrl, List<NameValuePair> strings, List<NameValuePair> images, String token)
			throws Exception;

    public BaseType doUploadRequest(String requestUrl, List<NameValuePair> nameValuePairs, List<NameValuePair> images,
            Parser<? extends BaseType> parser, String token)
            throws Exception;
}

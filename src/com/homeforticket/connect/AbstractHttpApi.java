
package com.homeforticket.connect;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.homeforticket.constant.SysConstants;
import com.homeforticket.model.BaseType;
import com.homeforticket.parser.Parser;
import com.homeforticket.util.MD5Utils;
import com.homeforticket.util.SharedPreferencesUtil;

import android.text.TextUtils;

abstract public class AbstractHttpApi implements HttpApi {
    /**
     * 最大连接数
     */
    public final static int MAX_TOTAL_CONNECTIONS = 800;

    /**
     * 获取连接的最大等待时间
     */
    public final static int TIMEOUT = 30 * 1000;
    // 一部分接口在超时时无法顺利捕获,但仍会关闭请求通道.
    // 请求无反馈时优先修改超时时间.

    /**
     * 每个路由最大连接数
     */
    public final static int MAX_ROUTE_CONNECTIONS = 400;

    private static final int UPLOAD_REQUEST_TYPE = 0;
    private static final int HTTPPOST_TYPE = UPLOAD_REQUEST_TYPE + 1;
    private static final int HTTPGET_TYPE = HTTPPOST_TYPE + 1;
    private static final int HTTPREQUEST_TYPE = HTTPGET_TYPE + 1;
    private static final int REQUEST_ZIP_TYPE = HTTPREQUEST_TYPE + 1;

    protected static final Logger LOG = Logger.getLogger(AbstractHttpApi.class.getCanonicalName());

    protected static final boolean DEBUG = SysConstants.DEBUG;

    private static final int MAX_EXECUTION_COUNT = 5;

    private final DefaultHttpClient mHttpClient;

    // 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
    private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
        // 自定义的恢复策略
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            // 设置恢复策略，在发生异常时候将自动重试3次
            if (executionCount >= MAX_EXECUTION_COUNT) {
                // Do not retry if over max retry count
                return false;
            }

            if (exception instanceof NoHttpResponseException) {
                // Retry if the server dropped connection on us
                return true;
            }

            if (exception instanceof SSLHandshakeException) {
                // Do not retry on SSL handshake exception
                return false;
            }

            HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
            boolean idempotent = (request instanceof HttpEntityEnclosingRequest);

            if (!idempotent) {
                // Retry if the request is considered idempotent
                return true;
            }

            return false;
        }
    };

    public AbstractHttpApi(DefaultHttpClient httpClient) {
        mHttpClient = httpClient;
    }

    public AbstractHttpApi(DefaultHttpClient httpClient, String clientVersion) {
        mHttpClient = httpClient;
    }

    public BaseType doHttpRequest(String requestUrl, List<NameValuePair> nameValuePairs,
            Parser<? extends BaseType> parser, int requestType) throws Exception {
        return executeHttpRequest(requestUrl, nameValuePairs, parser, requestType);
    }

    public BaseType doUploadRequest(String requestUrl, List<NameValuePair> strings,
            List<NameValuePair> images, Parser<? extends BaseType> parser)
            throws Exception {
        return executeUploadRequest(requestUrl, strings, images, parser);
    }

    private HttpPost setLoadHttpRequestBase(String requestUrl, List<NameValuePair> nameValuePairs,
            List<NameValuePair> images) throws Exception {
        HttpPost httpPost = createHttpPost(requestUrl);
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        if (nameValuePairs != null) {
            for (NameValuePair pair : nameValuePairs) {
                if (!TextUtils.isEmpty(pair.getValue())) {
                    entity.addPart(pair.getName(),
                            new StringBody(pair.getValue(), Charset.forName("UTF-8")));
                }
            }
        }
        
        for (NameValuePair pair : images) {
            if (!TextUtils.isEmpty(pair.getValue())) {
                entity.addPart(pair.getName(), new FileBody(new File(pair.getValue())));
            }
        }
        httpPost.setEntity(entity);

        return httpPost;
    }

    private HttpRequestBase setHttpRequestBase(String requestUrl,
            List<NameValuePair> nameValuePairs, int requestType) {
        HttpRequestBase httpRequest = null;

        if (requestType == SysConstants.REQUEST_POST) {
            httpRequest = createHttpPost(requestUrl, nameValuePairs);
        } else if (requestType == SysConstants.REQUEST_GET) {
            httpRequest = createHttpGet(requestUrl, nameValuePairs);
        }
        return httpRequest;
    }

    @Override
    public String doUploadRequest(String requestUrl, List<NameValuePair> strings,
            List<NameValuePair> images)
            throws Exception {
        if (TextUtils.isEmpty(requestUrl)) {
            return null;
        }

        HttpEntity resEntity = getHttpEntity(requestUrl, strings, images, -1, UPLOAD_REQUEST_TYPE);
        return EntityUtils.toString(resEntity, SysConstants.CHARSET);
    }

    @Override
    public String doHttpPost(String url, List<NameValuePair> nameValuePairs) throws Exception {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        HttpEntity resEntity = getHttpEntity(url, nameValuePairs, null, -1, HTTPPOST_TYPE);
        return EntityUtils.toString(resEntity, SysConstants.CHARSET);
    }

    @Override
    public String doHttpGet(String url, List<NameValuePair> nameValuePairs) throws Exception {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        HttpEntity resEntity = getHttpEntity(url, nameValuePairs, null, -1, HTTPGET_TYPE);
        return EntityUtils.toString(resEntity, SysConstants.CHARSET);
    }

    public BaseType executeUploadRequest(String requestUrl, List<NameValuePair> strings,
            List<NameValuePair> images, Parser<? extends BaseType> parser)
            throws Exception {
        if (TextUtils.isEmpty(requestUrl)) {
            return null;
        }
        HttpEntity resEntity = getHttpEntity(requestUrl, strings, images, -1, UPLOAD_REQUEST_TYPE);
        return resEntity == null ? null : parser.parse(EntityUtils.toString(resEntity,
                SysConstants.CHARSET));
    }

    public BaseType executeHttpRequest(String requestUrl, List<NameValuePair> nameValuePairs,
            Parser<? extends BaseType> parser, int requestType) throws Exception {

        if (TextUtils.isEmpty(requestUrl)) {
            return null;
        }

        HttpEntity resEntity = getHttpEntity(requestUrl, nameValuePairs, null, requestType,
                HTTPREQUEST_TYPE);
        return resEntity == null ? null : parser.parse(EntityUtils.toString(resEntity,
                SysConstants.CHARSET));
    }

    @Override
    public HttpEntity getHttpEntity(String url) throws Exception {

        if (TextUtils.isEmpty(url)) {
            return null;
        }

        HttpEntity resEntity = getHttpEntity(url, null, null, -1, REQUEST_ZIP_TYPE);
        return resEntity == null ? null : resEntity;
    }

    private void doReConnectLogin() throws Exception {
        // Intent intent = new Intent(AppApplication.getInstance(),
        // LoginActivityNew.class);
        // AppApplication.getInstance().startActivity(intent);
    }

    private HttpRequestBase resetHttpRequestBase(String requestUrl,
            List<NameValuePair> nameValuePairs,
            List<NameValuePair> fileNameValuePairs, int requestType, int entityType)
            throws Exception {

        HttpRequestBase httpRequest = null;

        switch (entityType) {
            case UPLOAD_REQUEST_TYPE:
                httpRequest = setLoadHttpRequestBase(requestUrl, nameValuePairs, fileNameValuePairs);
                break;
            case HTTPPOST_TYPE:
                httpRequest = createHttpPost(requestUrl, nameValuePairs);
                break;
            case HTTPGET_TYPE:
                httpRequest = createHttpGet(requestUrl, nameValuePairs);
                break;
            case HTTPREQUEST_TYPE:
                httpRequest = setHttpRequestBase(requestUrl, nameValuePairs, requestType);
                break;
            case REQUEST_ZIP_TYPE:
                httpRequest = new HttpGet(requestUrl);
                break;
            default:
                httpRequest = new HttpGet(requestUrl);
                break;
        }

        if (SharedPreferencesUtil.readBoolean(SysConstants.IS_LOGIN, false)) {
            httpRequest.addHeader(SysConstants.TOKEN, SharedPreferencesUtil.readString(
                            SysConstants.TOKEN, SysConstants.EMPTY_STRING));
        }
        httpRequest.addHeader(SysConstants.ACCEPT_ENCODING, SysConstants.ACCEPT_ENCODING_STRING);
        return httpRequest;

    }

    public HttpEntity getHttpEntity(String requestUrl, List<NameValuePair> nameValuePairs,
            List<NameValuePair> fileNameValuePairs, int requestType, int entityType)
            throws Exception {

        HttpRequestBase httpRequest = resetHttpRequestBase(requestUrl, nameValuePairs,
                fileNameValuePairs, requestType,
                entityType);
        HttpResponse response = executeHttpRequest(httpRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        switch (statusCode) {
            case 200:
                if (REQUEST_ZIP_TYPE == entityType) {
                    return response.getEntity();
                } else {
                    org.apache.http.Header[] headers = response.getHeaders("Content-Encoding");

                    if (headers.length > 0) {
                        if ("gzip".equals(headers[0].getValue())) {
                            return new GzipDecompressingEntity(response.getEntity());
                        } else {
                            return response.getEntity();
                        }
                    } else {
                        return response.getEntity();
                    }
                }

            case 401:
                response.getEntity().consumeContent();

                if (DEBUG) {
                    LOG.log(Level.FINE, "HTTP Code: 401");
                    throw new Exception(response.getStatusLine().toString());
                } else {
                    throw new Exception("网络站点访问未经授权");
                }
            case 404:
                response.getEntity().consumeContent();

                if (DEBUG) {
                    throw new Exception(response.getStatusLine().toString());
                } else {
                    throw new Exception("网络地址错误");
                }

            case 500:
                response.getEntity().consumeContent();

                if (DEBUG) {
                    LOG.log(Level.FINE, "HTTP Code: 500");
                    throw new Exception("APP is down. Try again later.");
                } else {
                    throw new Exception("服务器内部错误");
                }
            case 1302:
                response.getEntity().consumeContent();
                // try {
                // if (!SharedPreferencesUtil.readBoolean("oauth", false)) {
                // doReConnectLogin();
                // return getHttpEntity(requestUrl, nameValuePairs,
                // fileNameValuePairs, requestType, entityType);
                // } else {
                // throw new LogoutException("距离您上一次登陆已经过去了太久,请重新登陆.");
                // }
                // } catch (Exception e) {
                // throw new RedirectsException("服务器内部错误");
                // }
            case 1303:// 如果捕获了即将超时,则不再自动登陆,直到下一次输密码正常登陆为止.
                // 之后仍然按200处理.那些认为code=0是请求成功的接口请兼容200.
                // SharedPreferencesUtil.saveBoolean("autoFlag", false);
                // response.setStatusCode(200);
                return response.getEntity();
            case 302:
                response.getEntity().consumeContent();

                if (DEBUG) {
                    throw new Exception(response.getStatusLine().toString());
                } else {
                    throw new Exception("服务器内部错误");
                }

            default:
                if (DEBUG) {
                    LOG.log(Level.FINE, "Default case for status code reached: "
                            + response.getStatusLine().toString());
                    response.getEntity().consumeContent();
                    throw new Exception("Error connecting to APP: " + statusCode
                            + ". Try again later.");
                } else {
                    response.getEntity().consumeContent();
                    throw new Exception("连接服务器错误，请稍候.");
                }
        }
    }

    public void setTokenRequest(List<NameValuePair> nameValuePairs) throws Exception {

        if (nameValuePairs != null) {
            // if (!SharedPreferencesUtil.readBoolean("oauth", false)) {
            if (SharedPreferencesUtil.readBoolean("loginFlag", false)) {
                String token = SharedPreferencesUtil.readString("token", SysConstants.EMPTY_STRING);

                if (TextUtils.isEmpty(token) && !SharedPreferencesUtil.readBoolean("oauth", false)) {
                    doReConnectLogin();
                } else {
                    boolean isAdd = false;
                    for (int i = 0, size = nameValuePairs.size(); i < size; i++) {
                        NameValuePair nameValuePair = nameValuePairs.get(i);

                        if ("tk".equals(nameValuePair.getName())) {
                            nameValuePairs.set(
                                    i,
                                    new BasicNameValuePair("tk", SharedPreferencesUtil.readString(
                                            "token",
                                            SysConstants.EMPTY_STRING)));
                            isAdd = true;
                            break;
                        }
                    }

                    if (!isAdd) {
                        nameValuePairs.add(new BasicNameValuePair("tk", SharedPreferencesUtil
                                .readString("token",
                                        SysConstants.EMPTY_STRING)));
                    }
                }
            }
            // } else {
            // throw new LogoutException("未登录");
            // }
        }
    }

    public HttpResponse executeHttpRequest(HttpRequestBase httpRequest) throws IOException {

        try {
            return mHttpClient.execute(httpRequest);
        } catch (IOException e) {
            httpRequest.abort();
            throw e;
        }
    }

    @Override
    public HttpGet createHttpGet(String url, List<NameValuePair> nameValuePairs) {
        HttpGet httpGet;

        if (nameValuePairs != null) {
            String query = URLEncodedUtils.format(nameValuePairs, HTTP.UTF_8);

            httpGet = new HttpGet(url + "?" + query);
        } else {
            httpGet = new HttpGet(url);
        }

        return httpGet;
    }

    public HttpPost createHttpPost(String url, List<NameValuePair> nameValuePairs) {
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e1) {
            throw new IllegalArgumentException("Unable to encode http parameters.");
        }

        return httpPost;
    }

    public HttpPost createHttpPost(String url) {
        return new HttpPost(url);
    }

    public static DefaultHttpClient createHttpClient(int timeout) {

        // public static final DefaultHttpClient createHttpClient(String ip,
        // String port) {

        // Sets up the http part of the service.
        final SchemeRegistry supportedSchemes = new SchemeRegistry();

        // Register the "http" protocol scheme, it is required
        // by the default operator to look up socket factories.
        final SocketFactory sf = PlainSocketFactory.getSocketFactory();
        supportedSchemes.register(new Scheme("http", sf, 80));
        supportedSchemes.register(new Scheme("https", sf, 800));

        // Set some client http client parameter defaults.
        final HttpParams httpParams = createHttpParams(timeout);
        // 设置最大连接数
        ConnManagerParams.setMaxTotalConnections(httpParams, MAX_TOTAL_CONNECTIONS);
        // // 设置获取连接的最大等待时间
        ConnManagerParams.setTimeout(httpParams, 10000);
        // 设置每个路由最大连接数
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(MAX_ROUTE_CONNECTIONS);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);

        HttpClientParams.setRedirecting(httpParams, false);

        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams,
                supportedSchemes);

        DefaultHttpClient httpclient = new DefaultHttpClient(ccm, httpParams);
        httpclient.setHttpRequestRetryHandler(requestRetryHandler);

        // 解决org.apache.http.NoHttpResponseException: The target server failed
        // to respond
        HttpProtocolParams.setUseExpectContinue(httpclient.getParams(), false);

        return httpclient;
    }

    /**
     * Create the default HTTP protocol parameters.
     */
    private static HttpParams createHttpParams(int timeout) {
        final HttpParams params = new BasicHttpParams();

        // Turn off stale checking. Our connections break all the time anyway,
        // and it's not worth it to pay the penalty of checking every time.
        HttpConnectionParams.setStaleCheckingEnabled(params, false);

        HttpConnectionParams.setConnectionTimeout(params, 10000);
        HttpConnectionParams.setSoTimeout(params, timeout);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        return params;
    }
}

package com.homeforticket.common.share;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.homeforticket.util.StreamUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public abstract class AbsOauthLogin {

	private static final String LOGINSYSTEM_URL = "http://xuan.news.cn/cloudapi/mbfront/oauth_bind_query.htm";
	private String mFrom = "";
	private String mRedirectUrl = "";

	private HttpClient mClient;
	private WebView mWebView;
	private ProgressDialog mSpinner;

	private OauthDialog mOauthDialog;
	private AccessToken mAccessToken;
	private OauthUserInfo mUserInfo;

	private LoginListener mLoginListener;
	protected ShareListener mShareListener;

	protected abstract void authorize(WebView webView, OauthCallback callback);

	protected abstract void authorizeBySDK(SSOOauthCallback callback);

	protected abstract void handleRedirectUrl(String url);

	protected abstract OauthUserInfo getUserInfo(AccessToken token);

	protected abstract void shareToX(List<NameValuePair> params, Map<String, Bitmap> files);

	protected AbsOauthLogin(Context context, String redirectUrl, String from) {
		mFrom = from;
		mRedirectUrl = redirectUrl;
		mClient = new DefaultHttpClient();
		mSpinner = new ProgressDialog(context);
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSpinner.setMessage("Loading...");
		mWebView = new WebView(context);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				handler.proceed();
			}

			public void onPageFinished(WebView view, String url) {
				try {
					mSpinner.dismiss();
				} catch (Exception e) {
					// 运行到此处时有可能宿主activity已经关闭，所以try...catch
				}
			}

			@Override
			public void onPageStarted(WebView webView, String url, Bitmap favicon) {
				super.onPageStarted(webView, url, favicon);
				if (url.startsWith(mRedirectUrl)) {
					handleRedirectUrl(url);
					webView.stopLoading();
				} else {
					if (!mOauthDialog.isShowing()) {
						try{
							mOauthDialog.show();
						}catch(Exception e){
						}catch(Error e){
						}
					}
				}
			}
		});
		mOauthDialog = new OauthDialog(context, mWebView);
	}

	protected static Bundle parseUrl(String url) {
		url = url.replace("weiboconnect", "http");
		try {
			URL u = new URL(url);
			Bundle b = decodeUrl(u.getQuery());
			b.putAll(decodeUrl(u.getRef()));
			return b;
		} catch (MalformedURLException e) {
			return new Bundle();
		}
	}

	protected static Bundle decodeUrl(String s) {
		Bundle params = new Bundle();
		if (s != null) {
			String array[] = s.split("&");
			for (String parameter : array) {
				String[] v = parameter.split("=");
				if (2 != v.length)
					continue;
				params.putString(URLDecoder.decode(v[0]), URLDecoder.decode(v[1]));
			}
		}
		return params;
	}

	protected String httpGet(String url, Map<String, String> headers) {
		HttpGet get = new HttpGet(url);
		InputStream is;
		try {
			if(null != headers && !headers.isEmpty()){
				Set<String> keySet = headers.keySet();
				Iterator<String> it = keySet.iterator();
				while (it.hasNext()) {
					String key = it.next();
					get.addHeader(key, headers.get(key));
				}
			}
			HttpResponse resp = mClient.execute(get);
			int code = resp.getStatusLine().getStatusCode();
			if (200 != code) {
				JSONObject obj = new JSONObject();
				obj.put("status", code + "");
				return obj.toString();
			}
			is = resp.getEntity().getContent();
			return StreamUtil.stream2Str(is);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected String httpPost(String url, List<NameValuePair> params, Map<String, Bitmap> files) {
		try {
			HttpPost post = new HttpPost(url);
			if (null != files && !files.isEmpty()) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 50);
				for (int loc = 0; loc < params.size(); loc++) {
					NameValuePair nv = params.get(loc);
					StringBuilder temp = new StringBuilder(10);
					temp.setLength(0);
					temp.append("--7cd4a6d158c").append("\r\n");
					temp.append("content-disposition: form-data; name=\"").append(nv.getName()).append("\"\r\n\r\n");
					temp.append(nv.getValue()).append("\r\n");
					byte[] res = temp.toString().getBytes();
					baos.write(res);
				}

				post.setHeader("Content-Type", "multipart/form-data; boundary=7cd4a6d158c");
				Set<String> keys = files.keySet();
				Iterator<String> iter = keys.iterator();
				StringBuilder temp = new StringBuilder();
				while (iter.hasNext()) {
					String key = iter.next();
					Bitmap bitmap = files.get(key);
					temp.append("--7cd4a6d158c\r\n");
					temp.append("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"news_image\"\r\n");
					temp.append("Content-Type: image/png\r\n\r\n");
					byte[] res = temp.toString().getBytes();
					baos.write(res);
					bitmap.compress(CompressFormat.PNG, 75, baos);
					baos.write("\r\n".getBytes());
					bitmap.recycle();
					bitmap = null;
				}

				baos.write(("--7cd4a6d158c--\r\n").getBytes());
				byte[] data = baos.toByteArray();
				baos.close();
				ByteArrayEntity formEntity = new ByteArrayEntity(data);
				post.setEntity(formEntity);
			} else {
				post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}
			HttpResponse resp = mClient.execute(post);
			StatusLine status = resp.getStatusLine();
			int code = status.getStatusCode();
			if (200 != code) {
				JSONObject obj = new JSONObject();
				obj.put("status", code + "");
				return obj.toString();
			}
			InputStream is = resp.getEntity().getContent();
			return StreamUtil.stream2Str(is);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void shareOauth(final List<NameValuePair> params, final Map<String, Bitmap> files) {
		if (null == mAccessToken || !mAccessToken.checkValid()) {
			authorizeBySDK(new SSOOauthCallback() {

				@Override
				public void onSSOOauthSucceed(AccessToken token) {
					mAccessToken = token;
					new Thread() {
						public void run() {
							shareToX(params, files);
						};
					}.start();
				}

				@Override
				public void onSSOOauthFailed(String msg) {
					if (null != mShareListener) {
						mShareListener.onShareFailed(msg);
					}
				}

				@Override
				public void onNeedWebAuth() {
					authorize(mWebView, new OauthCallback() {

						@Override
						public void onOauthSucceed(AccessToken token) {
							mAccessToken = token;
							new Thread() {
								public void run() {
									mOauthDialog.dismiss();
									shareToX(params, files);
								};
							}.start();
						}

						@Override
						public void onOauthFailed(String msg) {
							mOauthDialog.dismiss();
							if (null != mShareListener) {
								mShareListener.onShareFailed(msg);
							}
						}
					});
				}
			});

		} else {
			new Thread() {
				public void run() {
					shareToX(params, files);
				};
			}.start();
		}
	}

	public void setShareListener(ShareListener listener) {
		mShareListener = listener;
	}

	public interface RegistListener {
		public void onRegistSucceed();

		public void onAccountExist();

		public void onRegistFailed(String msg);
	}

	public interface LoginListener {
		public void onLoginSucceed();

		public void onLoginFailed(String msg);
	}

	public interface ShareListener {
		public void onShareSucceed();

		public void onShareFailed(String msg);
	}

	protected interface OauthCallback {
		public void onOauthSucceed(AccessToken token);

		public void onOauthFailed(String msg);
	}

	protected interface SSOOauthCallback {
		public void onSSOOauthSucceed(AccessToken token);

		public void onSSOOauthFailed(String msg);

		public void onNeedWebAuth();
	}
}

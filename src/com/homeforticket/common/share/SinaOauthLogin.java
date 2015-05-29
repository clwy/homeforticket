package com.homeforticket.common.share;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.homeforticket.util.StreamUtil;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class SinaOauthLogin extends AbsOauthLogin {

	private static final String OAUTH_URL = "https://api.weibo.com/oauth2/authorize?display=mobile";
	private static final String ACCESSTOKEN_URL = "https://api.weibo.com/oauth2/access_token";
	private static final String REDIRECT_URL = "http://login.home.news.cn/cb/sina.do";
	private static final String USER_INFO_URL = "https://api.weibo.com/2/users/show.json";
	private static final String SHARE_URL = "https://api.weibo.com/2/statuses/update.json";
	private static final String UPLOAD_URL = "https://upload.api.weibo.com/2/statuses/upload.json";

	private static final String APP_KEY = "2733051474";
	private static final String mClientKEY = "ab5c43e2490879ed992e6890508f3487";
	private static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog, invitation_write";

	private static final String FROM = "sina";

	private Context mContext;
	private AccessToken mAccessToken;
	private OauthCallback mOauthCallback;
	private WeiboAuth mWeiboAuth;
	private IWeiboShareAPI mWeiboShareAPI;
	private SsoHandler mSsoHandler;

	public SinaOauthLogin(Context context) {
		super(context, REDIRECT_URL, FROM);
		mContext = context;
	}

	@Override
	protected void authorize(WebView webView, OauthCallback callback) {
		mOauthCallback = callback;
		String url = OAUTH_URL + "&client_id=" + APP_KEY + "&redirect_uri=" + REDIRECT_URL;
		webView.loadUrl(url);
	}

	protected void handleRedirectUrl(String url) {
		final Bundle values = parseUrl(url);
		new Thread() {
			public void run() {
				try {
					String authCode = values.getString("code");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("client_id", APP_KEY));
					params.add(new BasicNameValuePair("client_secret", mClientKEY));
					params.add(new BasicNameValuePair("grant_type", "authorization_code"));
					params.add(new BasicNameValuePair("code", authCode));
					params.add(new BasicNameValuePair("redirect_uri", REDIRECT_URL));
					String json = httpPost(ACCESSTOKEN_URL, params, null);

					JSONObject obj = new JSONObject(json);
					mAccessToken = new AccessToken(obj.getString("uid"), obj.getString("access_token"),
							obj.getString("expires_in"));
					mOauthCallback.onOauthSucceed(mAccessToken);
				} catch (JSONException e) {
					e.printStackTrace();
					mOauthCallback.onOauthFailed(e.getMessage());
				}
			}
		}.start();
	}

	@Override
	protected OauthUserInfo getUserInfo(AccessToken token) {
		try {
			URL url = new URL(USER_INFO_URL + "?access_token=" + token.getAccessToken() + "&uid=" + token.getUID());
			InputStream is = url.openStream();
			String jsonStr = StreamUtil.stream2Str(is);
			OauthUserInfo userInfo = new OauthUserInfo();
			JSONObject jsonObj = new JSONObject(jsonStr);
			userInfo.mSrceenName = jsonObj.getString("screen_name");
			userInfo.mOperPic = jsonObj.getString("profile_image_url");
			return userInfo;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void shareToX(String content) {
		shareToX(content, null);
	}

	public void shareToX(String content, Bitmap bitmap) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("status", content));
		Map<String, Bitmap> files = new HashMap<String, Bitmap>();
		if (null != bitmap && !bitmap.isRecycled()) {
			files.put("pic", bitmap);
		}
		super.shareOauth(params, files);
	}

	@Override
	protected void shareToX(List<NameValuePair> params, Map<String, Bitmap> files) {
		params.add(new BasicNameValuePair("access_token", mAccessToken.getAccessToken()));
		String url = null;
		if (null != files && !files.isEmpty()) {
			url = UPLOAD_URL;
		} else {
			url = SHARE_URL;
		}
		String json = httpPost(url, params, files);
		if (null != mShareListener) {
			try {
				JSONObject obj = new JSONObject(json);
				int status = obj.optInt("status");
				if (0 != status) {
					mShareListener.onShareFailed("status:" + status);
				} else {
					String err = obj.optString("error");
					if (TextUtils.isEmpty(err)) {
						mShareListener.onShareSucceed();
					} else {
						mShareListener.onShareFailed(err);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void authorizeBySDK(final SSOOauthCallback callback) {
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext, APP_KEY);
		if (mWeiboShareAPI.isWeiboAppInstalled() && mWeiboShareAPI.isWeiboAppSupportAPI()) {
			mWeiboShareAPI.registerApp();
			mWeiboAuth = new WeiboAuth(mContext, APP_KEY, REDIRECT_URL, SCOPE);
			mSsoHandler = new SsoHandler((Activity) mContext, mWeiboAuth);
			mSsoHandler.authorize(new WeiboAuthListener() {

				@Override
				public void onWeiboException(WeiboException e) {
					callback.onSSOOauthFailed(e.getMessage());
				}

				@Override
				public void onComplete(Bundle data) {
					Oauth2AccessToken token = Oauth2AccessToken.parseAccessToken(data);
					if (null != token && token.isSessionValid()) {
						AccessToken accessToken = new AccessToken();
						accessToken.setToken(token.getToken());
						accessToken.setExpiresTime(token.getExpiresTime());
						accessToken.setUID(token.getUid());
						accessToken.setRefreshToken(token.getRefreshToken());
						callback.onSSOOauthSucceed(accessToken);
					} else {
						String code = data.getString("code", "");
						callback.onSSOOauthFailed(code);
					}
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub

				}
			});
		} else {
			callback.onNeedWebAuth();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
	}
}

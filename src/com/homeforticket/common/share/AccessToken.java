package com.homeforticket.common.share;

class AccessToken {
	private String mUID;// 当前认证用户UID
	private String mAccessToken;// 授权access_token
	private long mExpiresTime;// 授权有效期
	private long mExpiresIn;// 授权有效时长 单位毫秒
	private String mRefreshToken;

	/**
	 * 
	 * @param uid
	 * @param accessToken
	 * @param expiresIn 有效时长 注意单位为毫秒
	 * @param refreshToken
	 */
	AccessToken(String uid, String accessToken, String expiresIn, String refreshToken) {
		mUID = uid;
		mAccessToken = accessToken;
		mExpiresIn = Long.parseLong(expiresIn);
		mExpiresTime = System.currentTimeMillis() + mExpiresIn;
		mRefreshToken = refreshToken;
	}

	/**
	 * 构造方法
	 * 
	 * @param uid 当前认证用户UID
	 * @param accessToken 授权access_token
	 * @param expiresIn 有效时长 注意单位为毫秒
	 */
	AccessToken(String uid, String accessToken, String expiresIn) {
		mUID = uid;
		mAccessToken = accessToken;
		mExpiresIn = Long.parseLong(expiresIn);
		mExpiresTime = System.currentTimeMillis() + mExpiresIn;
	}

	public AccessToken() {

	}

	public String getUID() {
		return mUID;
	}

	public String getAccessToken() {
		return mAccessToken;
	}

	public long getExpiresIn() {
		return mExpiresIn;
	}

	public long getExpiresTime() {
		return mExpiresTime;
	}

	/**
	 * 判断当前access_token是否在有效期内
	 * 
	 * @return true 有效，false 过期
	 */
	boolean checkValid() {
		return System.currentTimeMillis() > mExpiresTime ? false : true;
	}

	public String getRefreshToken() {
		return mRefreshToken;
	}

	public void setRefreshToken(String mRefrashToken) {
		this.mRefreshToken = mRefrashToken;
	}

	public void setUID(String mUID) {
		this.mUID = mUID;
	}

	public void setToken(String token) {
		this.mAccessToken = token;
	}

	public void setExpiresTime(long expiresTime) {
		this.mExpiresTime = expiresTime;
	}

}

package com.homeforticket.request;

public interface RequestListener {

	public void onStartRequest(RequestJob job);

	public void onSuccess(RequestJob job);

	public void onFail(RequestJob job);
}

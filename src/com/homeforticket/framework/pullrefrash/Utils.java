package com.homeforticket.framework.pullrefrash;

import android.util.Log;
/**
 * 
 * @author HelloK
 * @date   2014年8月13日
 */
public class Utils {

	static final String LOG_TAG = "PullToRefresh";

	public static void warnDeprecation(String depreacted, String replacement) {
		Log.w(LOG_TAG, "You're using the deprecated " + depreacted + " attr, please switch over to " + replacement);
	}

}

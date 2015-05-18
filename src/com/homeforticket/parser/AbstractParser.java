package com.homeforticket.parser;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.homeforticket.constant.SysConstants;
import com.homeforticket.model.BaseType;

public abstract class AbstractParser<T extends BaseType> implements Parser<T> {

	/**
	 * 初始的返回状态码
	 */
	private static final int INIT_CODE = 0;

	/**
	 * 需要重新登陆
	 */
	private static final int NEED_LOGIN = 10;

	private static final Logger LOG = Logger.getLogger(AbstractParser.class.getCanonicalName());
	private static final boolean DEBUG = SysConstants.PARSER_DEBUG;

	private static final String CODE = "code";

	protected static boolean getBoolean(JSONObject paramJSONObject, String paramString) {
		boolean bool = false;

		if (paramJSONObject.has(paramString)) {
			bool = paramJSONObject.optBoolean(paramString, SysConstants.FALSE);
		}

		return bool;
	}

	protected static double getDouble(JSONObject paramJSONObject, String paramString) {
		double d = 0.0D;

		if (paramJSONObject.has(paramString)) {
			d = paramJSONObject.optDouble(paramString, 0.0D);
		}

		return d;
	}

	protected static int getInt(JSONObject paramJSONObject, String paramString) {
		int i = 0;

		if (paramJSONObject.has(paramString)) {
			i = paramJSONObject.optInt(paramString, 0);
		}

		return i;
	}

	protected static String getString(JSONObject paramJSONObject, String paramString) {
		String str = SysConstants.EMPTY_STRING;

		if ((paramJSONObject.has(paramString)) && (!paramJSONObject.isNull(paramString))) {
			str = paramJSONObject.optString(paramString, SysConstants.EMPTY_STRING);
		}

		return str;
	}

	protected static String getString(JSONObject paramJSONObject, String jsonKey, String paramString) {
		JSONObject jsonObject = new JSONObject();
		String str = SysConstants.EMPTY_STRING;

		if ((paramJSONObject.has(jsonKey)) && (!paramJSONObject.isNull(jsonKey))) {
			jsonObject = paramJSONObject.optJSONObject(jsonKey);

			if ((jsonObject.has(paramString)) && (!jsonObject.isNull(paramString))) {
				str = jsonObject.optString(paramString, SysConstants.EMPTY_STRING);
			}
		}

		return str;
	}

	@Override
	public final T parse(String parser) throws Exception {
		try {
			return parseInner(parser);
		} catch (Exception e) {
			if (DEBUG) {
				LOG.log(Level.FINE, "IOException", e);
			}
			throw new Exception(e.getMessage());
		} 
	}

	abstract protected T parseInner(final String parser) throws Exception;

}

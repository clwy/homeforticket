package com.homeforticket.constant;

import org.apache.http.protocol.HTTP;

import android.R.integer;
import android.os.Environment;

public class SysConstants {

	public static final boolean TEST = false;

	public static final boolean DEBUG = false;

	public static final boolean PARSER_DEBUG = false;

	public static final String APP_NAME = "HomeForTicket";
	
	// 客户端类型,1表示android客户端
	public static final String FROM = "1";

	// 本地缓存路径
	public static final String LOCALPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HomeForTicket/";

	// 程序名称
	public static final String SERVER_NAME = 
//	        "218.241.163.2:90/appapi/appapi";
	        "test.piaozhijia.com/appapi/appapi";//测试服务器
//	        ""; // 正式服务器

	public static final String SERVER = "http://" + SERVER_NAME ;

	// 编码
	public static final String CHARSET = HTTP.UTF_8;

	// User-Agent
	public static final String CLIENT_VERSION_HEADER = "User-Agent";

	public static final String USER_AGENT = "user_agent";

	public static final String ANDROID_USER_AGENT = "Mozilla/5.0 (Linux; U; Android 4.0; zh-cn;) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";

	// Accept-Encoding
	public static final String ACCEPT_ENCODING = "Accept-Encoding";

	public static final String ACCEPT_ENCODING_STRING = "gzip,deflate";

	// post请求
	public static final int REQUEST_POST = 1;

	// get请求
	public static final int REQUEST_GET = 2;

	/*************************** 常量字符串 *******************************************/
	public static final String EMPTY_STRING = "";

	public static final boolean FALSE = false;

	public static final boolean TRUE = true;

	public static final String MOBILE_SEPARATOR = ",";

	public static final String BACKSLASH = "/";

	public static final String REQUEST_SUCCESS_STRING = "0";

	public static final String FALSE_STRING = "false";

	public static final String TRUE_STRING = "true";

	/************************** 存储字段名 ************************************************/
	public static final String USERNAME = "username";
	public static final String USER_ID = "userId";
	public static final String USER_PHOTO = "userPhoto";
	public static final String USER_TEL = "userTel";
	public static final String VERSION_NAME = "verName";
	public static final String VERSION_CODE = "verCode";
	public static final String VERSION_URL = "verUrl";
	public static final String VERSION_DESCRIPTION = "verDescription";
	public static final String REQUEST_NEXT_NEWS_KEY = "page";
	public static final String READ_NEWS_PAGE = "pageNum";
	public static final String BIND_ACCOUNT = "isBind";
	public static final String TOTAL_MONEY = "totalMoney";
	public static final String CURRENT_MONEY = "currentMoney"; 
	public static final String ROLE = "role"; 
	/************************* REQUEST_CODE ******************************/
	public static final int REQUEST_TYPE_LOGIN = 101;
	public static final int FIRSTPAGE_CODE = 0;
	public static final int BUY_TICKET_CODE = 1;
	public static final int GET_WALLET_CODE = 2;
	public static final int GET_RECORD_CODE = 3;
	public static final int GET_SCENEINFO_CODE = 4;
	public static final int GET_CUSTOMER_CODE = 5;
	public static final int GET_ORDER_CODE = 6;
	public static final int RESET_PASSWORD = 7;
	public static final int SET_USER_MOBILE = 8;
	public static final int SET_USER_NAME = 9;
	public static final int GET_SYSTEM_MESSAGE = 10;
	public static final int GET_STORE_CODE = 11;
	public static final int SUBMIT_FEEDBACK = 12;
	public static final int GET_PRODUCT_CODE = 13;
	public static final int SAVE_ORDER_CODE = 14;
	public static final int GET_ORDER_STATUS = 15;
	public static final int GET_STATISTICS_LIST = 16;
	public static final int GET_PRODUCT_CHANNEL = 17;
	public static final int SET_STORE_NAME = 18;
	public static final int SET_STORE_INFO = 19;
	public static final int ADD_PRODUCT = 20;
	public static final int SAVE_LOG = 21;
	public static final int GET_DISTRIBUTOR = 22;
	public static final int SET_CLIENT_INFO = 23;
	public static final int GET_BIND_BANK = 24;
	public static final int GET_CODE = 25;
	public static final int GET_BEHALF = 26;
	public static final int ADD_SCENE = 27;
	public static final int FETCH_MONEY = 28;
	
	/************************* REQUEST_PARIS ******************************/
	public static final String CLIENT_FROM = "client";
	// 获取版本信息
	public static final String OS_SYSTEM = "os";

	public static final String MOBILE_MODEL = "model";

	public static final String MANUFACTURER = "manufacturer";

	public static final String CURRENT_VERSION = "version";

	public static final String UPDATE_REQUEST_T = "t";

	// 登陆
	public static final String APP_FROM = "appFrom";
	public static final String IS_LOGIN = "loginFlag";
	public static final String TOKEN = "token";

}

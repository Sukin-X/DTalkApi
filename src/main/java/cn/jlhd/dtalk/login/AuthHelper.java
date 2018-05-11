package cn.jlhd.dtalk.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import cn.jlhd.util.HttpHelper;
import cn.jlhd.util.JsonUtil;
import cn.jlhd.util.ReturnUtil;

/**
 * 
 * 1.获取accessToken
 * 2.获取jsapi中的ticket
 * 3.生成jsapiz中的鉴权sign
 * 4.根据传入的临时code获取用户的基本信息,入userinfo
 * 5.(ISV版本)根据userid获取详细用户信息
 * 
 * @author lnexin
 *
 */
public class AuthHelper {

	// 钉钉api相关
	static String TOKEN_URL = "https://oapi.dingtalk.com/gettoken";
	static String TICKET_URL = "https://oapi.dingtalk.com/get_jsapi_ticket";
	static String USER_INFO_URL = "https://oapi.dingtalk.com/user/getuserinfo";
	static String USER_ALL_URL = "https://oapi.dingtalk.com/user/get";

	// 调整到1小时50分钟
	public static final long cacheTime = 1000 * 60 * 55 * 2;

	private static String ACCESS_TOKEN = null;
	private static String JSAPI_TICKET = null;
	private static long LAST_TIME = 0;

	/**
	 * 
	 * @param corpId
	 * @param corpSecert
	 * @return 与钉钉服务器请求生成的accessToken
	 */
	public static String getAccessToken(String corpId, String corpSecert) {
		long curTime = System.currentTimeMillis();
		long differ = curTime - LAST_TIME;

		if (ACCESS_TOKEN != null && differ < cacheTime)
			return ACCESS_TOKEN;

		ACCESS_TOKEN = requestAccessToken(corpId, corpSecert);
		LAST_TIME = curTime;

		return ACCESS_TOKEN;
	}

	/**
	 * 
	 * @param accessToken
	 *            
	 * @see getAccess_Token(String corpId, String corpSecert) 生成的access_token
	 * @return 一个用于js鉴权的ticket
	 */
	public static String getJsapiTicket(String accessToken) {
		long curTime = System.currentTimeMillis();
		long differ = curTime - LAST_TIME;

		if (JSAPI_TICKET != null && differ < cacheTime) {
			return JSAPI_TICKET;
		}
		JSAPI_TICKET = requestJsapiTicket(accessToken);
		return JSAPI_TICKET;
	}

	/**
	 * 根据传入的相关参数生成sign
	 * 
	 * @param ticket
	 * @param nonceStr
	 * @param timeStamp
	 * @param url
	 * @return
	 */
	public static String sign(String ticket, String nonceStr, long timeStamp, String url) {
		StringBuffer plain = new StringBuffer();
		plain.append("jsapi_ticket=").append(ticket);
		plain.append("&noncestr=").append(nonceStr);
		plain.append("&timestamp=").append(String.valueOf(timeStamp));
		plain.append("&url=").append(url);
		MessageDigest sha;
		try {
			sha = MessageDigest.getInstance("SHA-1");
			sha.reset();
			sha.update(plain.toString().getBytes("UTF-8"));
			return bytesToHex(sha.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String requestAccessToken(String corpId, String corpSecert) {
		StringBuffer url = new StringBuffer(TOKEN_URL);
		url.append("?corpid=").append(corpId);
		url.append("&corpsecret=").append(corpSecert);
		String result = null;
		try {
			result = HttpHelper.sendGet(url.toString());
		} catch (IOException e) {
			result = ReturnUtil.result("-1",
					"请求accessTokenc出错!corpid:" + corpId + ",corpsecert:" + corpSecert + "异常信息:" + e);
		}
//		System.out.println("corpID:" + corpId + ",corpSecert:" + corpSecert + ",result:" + result);
		return JsonUtil.getJsonNode(result).get("access_token").asText();
	}

	private static String requestJsapiTicket(String accessToken) {
		StringBuffer url = new StringBuffer(TICKET_URL);
		url.append("?access_token=").append(accessToken);
		String result = null;
		try {
			result = HttpHelper.sendGet(url.toString());
		} catch (IOException e) {
			result = ReturnUtil.result("-1", "请求JsapiTicket出错!accessToken:" + accessToken + "异常信息:" + e);
		}
//		System.out.println("accessToken:" + accessToken + ",result:" + result);
		return JsonUtil.getJsonNode(result).get("ticket").asText();
	}

	private static String bytesToHex(byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param code
	 *            用户相应的临时code
	 * @param token
	 *            根据相应corpid和corpsecret生成的access_token
	 * @return 用户ID等相关信息
	 */
	public static String getUserInfo(String code, String accessToken) {
		StringBuffer url = new StringBuffer(USER_INFO_URL);
		url.append("?access_token=").append(accessToken);
		url.append("&code=").append(code);
		String result = null;
		try {
			result = HttpHelper.sendGet(url.toString());
		} catch (IOException e) {
			result = ReturnUtil.result("-1", "请求User信息出错!code:" + code + "异常信息:" + e);
		}
		return result;
	}
	/**
	 * 获取用户详细信息
	 * @param userid 在某个corpid下的唯一用户userid
	 * @param accessToken 据相应corpid和corpsecret生成的access_token
	 * @return
	 */
	public static String getUser(String userid, String accessToken) {
		StringBuffer url = new StringBuffer(USER_ALL_URL);
		url.append("?access_token=").append(accessToken);
		url.append("&userid=").append(userid);
		String result = null;
		try {
			result = HttpHelper.sendGet(url.toString());
		} catch (IOException e) {
			result = ReturnUtil.result("-1", "请求User信息出错!userid:" + userid + "异常信息:" + e);
		}
		return result;
	}
	
	
}

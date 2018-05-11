package cn.jlhd.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 基于HttpClient<br>
 * 这里发出去的POST请求格式为"application/json"
 * httpclient-4.5.1.jar，httpcore-4.4.3.jar，依赖的jar有commons-logging-1.2.jar
 * 
 * @author daniel
 * 
 */
public class HttpHelper {
	private static CloseableHttpClient httpclient = HttpClients.createDefault();
	
	/**
	 * 发送HttpGet请求
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String sendGet(String url) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String result = null;
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity);
			}
		} catch (IOException e) {
			if (e.getMessage().indexOf("401") > -1) {
				throw new HttpConnectException.UnauthorizedException(url);
			}
			if (e.getMessage().indexOf("403") > -1) {
				throw new HttpConnectException.ForbiddenException(url);
			}
			if (e.getMessage().indexOf("405") > -1) {
				throw new HttpConnectException.NotAllowedException(url);
			}
			if (e.getMessage().indexOf("501") > -1) {
				throw new HttpConnectException.UnknownRequestException(url);
			}
			throw e;
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (httpclient != null) {
				httpclient.close();
			}
		}
		return result;
	}
	
	/**
	 * 发送HttpPost请求，参数为map
	 * 
	 * @param url
	 * @param map
	 * @return
	 */
	public static String sendPost(String url, Map<String, String> map) {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(entity);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpEntity entity1 = response.getEntity();
		String result = null;
		try {
			result = EntityUtils.toString(entity1);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 发送不带参数的HttpPost请求
	 * 
	 * @param url
	 * @return
	 */
	public static String sendPost(String url) {
		HttpPost httppost = new HttpPost(url);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpEntity entity = response.getEntity();
		String result = null;
		try {
			result = EntityUtils.toString(entity);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
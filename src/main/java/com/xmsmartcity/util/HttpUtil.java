package com.xmsmartcity.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
/**
 * 发送http请求
 * @author Administrator
 *
 */
public class HttpUtil {
	
	public static JSONObject href(String url,String... urlParams) {
		return request(url, "GET", "", urlParams);
	}
	
	
	/**
	 * 
	 * @author felix  @date 2015-6-16 下午7:43:39
	 * @param url
	 * @param method
	 * @param requestBody
	 * @param urlParams
	 * url 替换规则   urlParams ： zhishan,dianpai,zhishan 三个参数
	 * http://www.baidu.com/{company}/{app}/{company}
	 * @return
	 */
	public static JSONObject request(String url, String method,String requestBody,String... urlParams) {
		StringBuffer bufferRes = new StringBuffer();
		try {
			URL realUrl = new URL(replaceURL(url, urlParams));
			if(url.startsWith("https")){
				trustAllHttpsCertificates();
			}
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection();
			// 连接超时
			conn.setConnectTimeout(25000);
			// 读取超时 --服务器响应比较慢,增大时间
			conn.setReadTimeout(25000);
			HttpURLConnection.setFollowRedirects(true);
			// 请求方式
			conn.setRequestMethod(method);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0");
			conn.connect();
			// 获取URLConnection对象对应的输出流
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// 发送请求参数
			out.write(requestBody);
			out.write("");
			out.flush();
			out.close();
			InputStream in = conn.getInputStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			String valueString = null;
			while ((valueString = read.readLine()) != null) {
				bufferRes.append(valueString);
			}
			in.close();
			if (conn != null) {
				conn.disconnect();
			}
			return JSONObject.fromObject(bufferRes.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 更换url
	 * @param url
	 * @param urlParams
	 * @return
	 */
	public static String replaceURL(String url ,String... urlParams){
		if(urlParams == null){
			return url;
		}
		for(String param : urlParams){
			url = url.replaceFirst("\\{.*?\\}", param);
		}
		return url;
	}

	
	/**
	 * 需要执行https请求前，先执行该方法
	 * 
	 * @author felix  @date 2015-6-16 下午7:13:10
	 * @throws Exception
	 */
	public static void trustAllHttpsCertificates(){
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = null;
		try {
			sc = javax.net.ssl.SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
				.getSocketFactory());
	}

	static class miTM implements javax.net.ssl.TrustManager,
			javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}
}

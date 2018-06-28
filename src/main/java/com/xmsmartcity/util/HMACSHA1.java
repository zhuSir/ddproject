package com.xmsmartcity.util;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.Ostermiller.util.Base64;

public class HMACSHA1 {
	private static final String HMAC_SHA1 = "HmacSHA1";

	/**
	 * 获取sign
	 * 
	 * @author linweiqin
	 * @Date 2014-11-20 下午3:48:27
	 * @param data
	 *            ： 是 url源串
	 * @param key
	 *            ： 是secretOAuthKey 加 ”&“
	 * @return
	 * @throws Exception
	 */
	public static String getSignature(byte[] data, byte[] key) throws Exception {
		SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
		Mac mac = Mac.getInstance(HMAC_SHA1);
		mac.init(signingKey);
		byte[] rawHmac = mac.doFinal(data);
		return Base64.encodeToString(rawHmac);
	}

	public String getHmacSHA1(String post) throws Exception {
		// Generate secret key for HMAC-SHA1
		KeyGenerator kg = KeyGenerator.getInstance(HMAC_SHA1);
		SecretKey sk = kg.generateKey();
		// Get instance of Mac object implementing HMAC-S
		Mac mac = Mac.getInstance(HMAC_SHA1);
		mac.init(sk);
		byte[] result = mac.doFinal(post.getBytes());
		return Base64.encodeToString(result);
	}

	/**
	 * 测试方法
	 * 
	 * @author linweiqin
	 * @Date 2014-11-20 下午3:48:13
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		/*  StringBuffer sourceUrl = new
		  StringBuffer("POST&%2Fwgwitem%2FwgQueryItemList.xhtml&"); Random rd =
		  new Random(); int randomValue = rd.nextInt(1000000); long timeStamp =
		  new Date().getTime();
		  sourceUrl.append("accessToken%3D").append(Constant.accessToken)
		  .append("%26appOAuthID%3D").append(Constant.appOauthID)
		  .append("%26charset%3Dgbk") .append("%26format%3Djson")
		  .append("%26keyWord%3D"
		  ).append(URLEncoder.encode("2014华夏争锋","UTF-8"))
		  .append("%26pageSize%3D").append(1) .append("%26randomValue%3D" +
		  randomValue) .append("%26sellerUin%3D").append(Constant.uin)
		  .append("%26startIndex%3D").append(0) .append("%26timeStamp%3D" +
		  timeStamp) .append("%26uin%3D").append(Constant.uin); String sign =
		  HMACSHA1
		  .getSignature(sourceUrl.toString().getBytes("ISO8859-1"),(Constant
		  .secretOAuthKey+"&").getBytes("ISO8859-1")); sign =
		  URLEncoder.encode(sign,"ISO8859-1");
		  
		  StringBuffer url = new StringBuffer(Constant.weigouUrl);
		  url.append("?startIndex=").append(0)
		  .append("&accessToken=").append(Constant.accessToken)
		  .append("&timeStamp=").append(timeStamp) .append("&charset=gbk")
		  .append("&pageSize=").append(1) .append("&uin=").append(Constant.uin)
		  .append("&format=json") .append("&sellerUin=").append(Constant.uin)
		  .append("&sign=").append(sign)
		  .append("&appOAuthID=").append(Constant.appOauthID)
		  .append("&randomValue=").append(randomValue);
		  url.append("&keyWord=").
		  append(URLEncoder.encode("2014华夏争锋","UTF-8"));*/
		 
	}
}

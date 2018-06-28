package com.xmsmartcity.util;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * 短信http接口的java代码调用示例
 * @author jacky
 * @since 2013-12-1
 */
public class JavaSmsApi {
	/**
	 * 服务http地址
	 */
	private static String BASE_URI = "http://yunpian.com";
	/**
	 * 服务版本号
	 */
	private static String VERSION = "v1";
	/**
	 * 编码格式
	 */
	private static String ENCODING = "UTF-8";
	/**
	 * 查账户信息的http地址
	 */
	private static String URI_GET_USER_INFO = BASE_URI + "/" + VERSION + "/user/get.json";
	/**
	 * 通用发送接口的http地址
	 */
	private static String URI_SEND_SMS = BASE_URI + "/" + VERSION + "/sms/send.json";
	/**
	 * 模板发送接口的http地址
	 */
	private static String URI_TPL_SEND_SMS = BASE_URI + "/" + VERSION + "/sms/tpl_send.json";
	
	/**
	 * 取账户信息
	 * @return json格式字符串
	 * @throws IOException 
	 */
	public static String getUserInfo(String apikey) throws IOException{
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(URI_GET_USER_INFO+"?apikey="+apikey);
		HttpMethodParams param = method.getParams();
		param.setContentCharset(ENCODING);
		client.executeMethod(method);
		return method.getResponseBodyAsString();
	}
	/**
	 * 发短信
	 * @param apikey apikey
	 * @param text　短信内容　
	 * @param mobile　接受的手机号
	 * @return json格式字符串
	 * @throws IOException 
	 */
	public static String sendSms(String apikey, String text, String mobile) throws IOException{
		HttpClient client = new HttpClient();
		NameValuePair[] nameValuePairs = new NameValuePair[3];
		nameValuePairs[0] = new NameValuePair("apikey", apikey);
		nameValuePairs[1] = new NameValuePair("text", text);
		nameValuePairs[2] = new NameValuePair("mobile", mobile);
		PostMethod method = new PostMethod(URI_SEND_SMS);
		method.setRequestBody(nameValuePairs);
		HttpMethodParams param = method.getParams();
		param.setContentCharset(ENCODING);
		client.executeMethod(method);
		return method.getResponseBodyAsString();
	}
	
	/**
	 * 通过模板发送短信
	 * @param apikey apikey
	 * @param tpl_id　模板id
	 * @param tpl_value　模板变量值　
	 * @param mobile　接受的手机号
	 * @return json格式字符串
	 * @throws IOException 
	 */
	public static String tplSendSms(String apikey, long tpl_id, String tpl_value, String mobile) throws IOException{
		HttpClient client = new HttpClient();
		NameValuePair[] nameValuePairs = new NameValuePair[4];
		nameValuePairs[0] = new NameValuePair("apikey", apikey);
		nameValuePairs[1] = new NameValuePair("tpl_id", String.valueOf(tpl_id));
		nameValuePairs[2] = new NameValuePair("tpl_value", tpl_value);
		nameValuePairs[3] = new NameValuePair("mobile", mobile);
		PostMethod method = new PostMethod(URI_TPL_SEND_SMS);
		method.setRequestBody(nameValuePairs);
		HttpMethodParams param = method.getParams();
		param.setContentCharset(ENCODING);
		client.executeMethod(method);
		return method.getResponseBodyAsString();
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		//修改为您的apikey
		String apikey = "2acdb1adc464dc7f3497cf1019c84a8e";
		//修改为您要发送的手机号
		String mobile = "18650802606";
		String mobile2 = "18030303620";
		
		/**************** 查账户信息调用示例 *****************/
//		System.out.println(JavaSmsApi.getUserInfo(apikey));
//		
//		/**************** 使用通用接口发短信 *****************/
//		//设置您要发送的内容
//		String text = "您的验证码是1234【云片网】";
//		//发短信调用示例
//		System.out.println(JavaSmsApi.sendSms(apikey, text, mobile));
		
		/**************** 使用模板接口发短信 *****************/
		//设置模板ID，如使用1号模板:您的验证码是#code#【#company#】
		long tpl_id=764459;
		//设置对应的模板变量值
//		String tpl_value="#code#=1234&#company#=侍者科技&#app#=颠派";
		String tpl_value="#activity#=杜蕾 · 私趴&#time#=20:00&#app#=颠派&#address#=Here酒吧（黄厝溪头下村25-1号）&#tip#= ";
//		System.out.println(JavaSmsApi.tplSendSms(apikey, tpl_id, tpl_value, mobile));
		System.out.println(JavaSmsApi.tplSendSms(apikey, tpl_id, tpl_value, mobile2));
//		
//		String phones = "";
//		int idx = 0;
//		for(String onePhone : phones.split(",")){
//			System.out.println(JavaSmsApi.tplSendSms(apikey, tpl_id, tpl_value, onePhone));
//			idx ++;
//		}
//		System.out.println("发送记录为:"+idx);
//		
		
		
	}
}         
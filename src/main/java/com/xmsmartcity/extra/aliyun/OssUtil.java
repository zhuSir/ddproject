package com.xmsmartcity.extra.aliyun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.xmsmartcity.util.Constant;

public class OssUtil {

	public static final Boolean Debug = Constant.helper.getBoolean("oss_debug");

	public static final String ALIYUN_KEY = Debug ? "LTAIr3yJwqP6BjJ9" : "LTAIr3yJwqP6BjJ9";

	public static final String ALIYUN_SECRET = Debug ? "E3qDOI0F38Oi6IUslBgDV4J8n4Y1tR" : "E3qDOI0F38Oi6IUslBgDV4J8n4Y1tR";

	public static final String END_POINT = Debug ? "http://oss-cn-shanghai.aliyuncs.com" : "http://oss-cn-shanghai-internal.aliyuncs.com";

	public static final String BUCKET = Debug ? "dweibao" : "dweibao";
	
	public static final String USER_IMG = "user_pic/";
	
	public static final String PROJECT_IMG = "project_pic/";
	
	public static final String COMPANY_IMG = "company_pic/";
	
	public static final String FAULT_IMG = "fault_pic/";
	
	public static final String TMPPATH = "/home/tmp";
	// 此文件用于存放非文件库文件
	public static final String IMG_FOLDER = Debug ? "images/" : "images/";
	// 此文件夹用于存放文件库中的文件
	public static final String FILE_FOLDER = Debug ? "files/" : "files/";
	// 非文件库 图片读取地址
	public static final String IMG_ACCESS_URL = Debug ? "http://img.dweibao.com/images" : "http://img.dweibao.com/images";
	// 文件库文件读取地址，主要用户读取图片
	public static final String FILE_IMG_ACCESS_URL = Debug ? "http://img.dweibao.com/files" : "http://file.dweibao.com/files";
	// 文件库 非图片文件读取地址
	public static final String FILE_ACCESS_URL = Debug ? "http://img.dweibao.com/files" : "http://img.dweibao.com/images";
	// 文件库问价下载地址

	// @100w_100h_1e_1c 自动剪裁 @felix
	// @100-1ci 内切圆
	// @100w_100h_1e_1c_10-2ci 圆角矩形

//	public static OSSClient client = new OSSClient(END_POINT, ALIYUN_KEY, ALIYUN_SECRET);

//	public OssUtil(){
//		client = new OSSClient(END_POINT, ALIYUN_KEY, ALIYUN_SECRET);
//	}
	
	/**
	 * 上传文件
	 * 
	 * @author felix @date 2015-4-16 下午5:56:38
	 * @param file
	 * @param saveName
	 * @return
	 * @throws OSSException
	 * @throws ClientException
	 * @throws IOException
	 */
	public static String uploadFile(MultipartFile file, String saveName) throws OSSException, ClientException, IOException {
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(file.getSize());
		 OSSClient client = new OSSClient(END_POINT, ALIYUN_KEY, ALIYUN_SECRET);
		PutObjectResult result = client.putObject(BUCKET, saveName, file.getInputStream(), meta);
		client.shutdown();
		return result.getETag();
	}
	
	/**
	 * 本地文件上传
	 * 
	 * @author felix @date 2015-4-16 下午5:54:26
	 * @throws OSSException
	 * @throws ClientException
	 * @throws FileNotFoundException
	 */
	public static void uploadFile() throws OSSException, ClientException, FileNotFoundException {
		File file = new File("C:\\Users\\Owner\\Desktop\\11.png");
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(file.length());
		 OSSClient client = new OSSClient(END_POINT, ALIYUN_KEY,ALIYUN_SECRET);
		PutObjectResult result = client.putObject(BUCKET, IMG_FOLDER+"11.png", new FileInputStream(file), meta);
		client.shutdown();
		System.out.println(result.getETag());
	}

	/**
	 * 上传文件
	 * 
	 * @author felix @date 2015-4-16 下午5:56:38
	 * @param file
	 * @param saveName
	 * @return
	 * @throws OSSException
	 * @throws ClientException
	 * @throws IOException
	 */
	public static String uploadFile(File file, String saveName, boolean isImg) throws OSSException, ClientException, IOException {
		System.out.println("the save name is " + saveName);
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(file.length());
		 OSSClient client = new OSSClient(END_POINT, ALIYUN_KEY,
		 ALIYUN_SECRET);
		PutObjectResult result = client.putObject(BUCKET, (isImg ? IMG_FOLDER : FILE_FOLDER) + saveName, new FileInputStream(file), meta);
		client.shutdown();
		return result.getETag();
	}

	/**
	 * 上传文件
	 * 
	 * @author felix @date 2015-4-16 下午5:56:38
	 * @param file
	 * @param saveName
	 * @return
	 * @throws OSSException
	 * @throws ClientException
	 * @throws IOException
	 */
	public static String uploadFile(MultipartFile file, String saveName, boolean isImg) throws OSSException, ClientException, IOException {
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(file.getSize());
		OSSClient client = new OSSClient(END_POINT, ALIYUN_KEY, ALIYUN_SECRET);
		PutObjectResult result = client.putObject(BUCKET, (isImg ? IMG_FOLDER : FILE_FOLDER) + saveName, file.getInputStream(), meta);
		client.shutdown();
		return result.getETag();
	}

	/**
	 * 上传文件
	 * 
	 * @author linweiqin @date 2015-4-16 下午5:56:38
	 * @param file
	 * @param saveName
	 * @return
	 * @throws OSSException
	 * @throws ClientException
	 * @throws IOException
	 */
	public static String uploadFile(URL urlObj, String saveName) throws OSSException, ClientException, IOException {
		saveName = IMG_FOLDER + saveName;
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(urlObj.openStream().available());
		OSSClient client = new OSSClient(END_POINT, ALIYUN_KEY, ALIYUN_SECRET);
		PutObjectResult result = client.putObject(BUCKET, saveName, urlObj.openStream(), meta);
		client.shutdown();
		return result.getETag();
	}

	public static void main(String[] args) throws OSSException, ClientException, MalformedURLException, IOException {
		String url = "http://wx.qlogo.cn/mmopen/kYrW27Xk2HB31Ve9wCKlxDDJzTp6ONFVtL338V38jRqGZ5h4oibkfIbnPVGHIGPuYR5ibalLt7W0hIQicJiaeTkpjSiaQIllYZCQ6/0";
		String saveName = java.util.UUID.randomUUID().toString();
		OssUtil.uploadFile(new URL(url), saveName);
		System.out.println("121");
		OssUtil.uploadFile();
	}
	

}

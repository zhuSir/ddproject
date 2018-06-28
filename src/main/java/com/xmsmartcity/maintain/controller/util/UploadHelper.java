package com.xmsmartcity.maintain.controller.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.xmsmartcity.extra.aliyun.OssUtil;

/**
 * 文件上传
 * 
 * @author felix
 * @date 2015-6-9 下午6:12:12
 */
@Component
public class UploadHelper {

	// private static Logger logger =
	// LoggerFactory.getLogger(UploadHelper.class);
	/**
	 * 保存单个文件，接收参数为 multipartFile
	 * 
	 * @param request
	 * @param file
	 * @param saveName
	 * @return
	 * @throws IOException
	 */
	public JSONObject saveSingleFile(HttpServletRequest request,Integer type, MultipartFile file, int sizeLimit) throws IOException {
		JSONObject jobject = new JSONObject();
		String saveName = "";
		if (file == null) {
			jobject.put("success", false);
			jobject.put("info", "文件为空");
			return jobject;
		}

		if (file.getSize() > sizeLimit) {
			jobject.put("success", false);
			jobject.put("info", "文件大小超过限制");
			return jobject;
		}

		String oriName = file.getOriginalFilename().trim();
		String suffix = oriName.substring(oriName.lastIndexOf(".") + 1);
		saveName = StringUtils.isEmpty(saveName) ? java.util.UUID.randomUUID().toString() : saveName;
		saveName = saveName + "." + suffix;
		
		saveName = PATH_TYPE.get(type)+saveName;
		// 上传到阿里云
		OssUtil.uploadFile(file, saveName);
		// 上传到本地
		// String floder =
		// request.getSession().getServletContext().getRealPath("/resources/file");
		// FileUtils.copyInputStreamToFile(file.getInputStream(), new
		// File(floder, saveName));

		// 返回一个url（ueditor上传时需要返回这个参数）
		String url = OssUtil.IMG_ACCESS_URL + File.separator + saveName;
		jobject.put("url", url);
		// 返回结构
		jobject.put("size", file.getSize()); // 单位：KB
		jobject.put("success", true);
		jobject.put("state", "SUCCESS");
		jobject.put("saveName", saveName);
		jobject.put("name", saveName);
		jobject.put("originalName", oriName);
		return jobject;
	}
	
	private static final Map<Integer,String> PATH_TYPE = new HashMap<Integer,String>();
	
	public UploadHelper(){
		PATH_TYPE.put(0, "");
		PATH_TYPE.put(1, OssUtil.USER_IMG);
		PATH_TYPE.put(2, OssUtil.COMPANY_IMG);
		PATH_TYPE.put(3, OssUtil.PROJECT_IMG);
		PATH_TYPE.put(4, OssUtil.FAULT_IMG);
	}

	/**
	 * 保存单个文件，接收参数为 multipartFile
	 * 
	 * @param request
	 * @param file
	 * @param saveName
	 * @return
	 * @throws IOException
	 */
	public JSONObject saveSingleFile(HttpServletRequest request, MultipartFile file,Integer type, boolean isImg, int sizeLimit)
			throws IOException {
		JSONObject jobject = new JSONObject();
		String saveName = null;
		if (file == null) {
			jobject.put("success", false);
			jobject.put("info", "文件为空");
			return jobject;
		}
		if (file.getSize() > sizeLimit) {
			jobject.put("success", false);
			jobject.put("info", "文件大小超过限制");
			return jobject;
		}
		String oriName = file.getOriginalFilename().trim();
		if(oriName == null || oriName == ""){
			jobject.put("success", false);
			jobject.put("info", "文件为空");
			return jobject;
		}
		String suffix = oriName.substring(oriName.lastIndexOf(".") + 1);
		saveName = StringUtils.isEmpty(saveName) ? java.util.UUID.randomUUID().toString() : saveName;
		saveName = saveName + "." + suffix;
		// File newFile=null;
		// 计算图片的宽高
		// if(isImg){
		// newFile=makingFileWidthHeight(file);
		// }
		// 上传到阿里云
		// if(newFile==null){
		// OssUtil.uploadFile(file, saveName,isImg);
		// }else{
		// OssUtil.uploadFile(newFile, saveName,isImg);
		// }
		// 上传图片
		// OssUtil.closeClient();
		// 上传到本地
		// String floder =
		// request.getSession().getServletContext().getRealPath("/resources/file");
		// FileUtils.copyInputStreamToFile(file.getInputStream(), new
		// File(floder, saveName));
		saveName = PATH_TYPE.get(type)+saveName;
		OssUtil.uploadFile(file, saveName, isImg);
		// 返回一个url（ueditor上传时需要返回这个参数）
		String url = OssUtil.IMG_ACCESS_URL + File.separator + saveName + "@85Q";
		jobject.put("url", url);
		// 返回结构
		jobject.put("size", file.getSize()); // 单位：KB
		jobject.put("success", true);
		jobject.put("name", saveName.toString());
		jobject.put("originalName", oriName);
		return jobject;
	}

	/**
	 * 计算图片大小 压缩
	 * 
	 * @author wengzhijie
	 * @param file
	 * @return
	 * @date 2016-1-8 上午10:56:30
	 */
	public File makingFileWidthHeight(MultipartFile file) {
		try {
			InputStream inputStream = file.getInputStream();
			BufferedImage bi = ImageIO.read(inputStream);
			System.out.println("Width=" + bi.getWidth());
			;
			System.out.println("Height=" + bi.getHeight());
			;
			int width = bi.getWidth();
			int height = bi.getHeight();
			int imgArea = width * height;
			int maxArea = 1024 * 1024;
			int multiple = imgArea > maxArea ? imgArea / maxArea : 1; // 倍数
			return resizeImage(file, "", width / multiple, height / multiple);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	/***
	 * 功能 :调整图片大小
	 * 
	 * @param srcImgPath
	 *            原图片路径
	 * @param distImgPath
	 *            转换大小后图片路径
	 * @param width
	 *            转换后图片宽度
	 * @param height
	 *            转换后图片高度
	 */
	public File resizeImage(MultipartFile file, String distImgPath, int width, int height) throws IOException {
		CommonsMultipartFile cf = (CommonsMultipartFile) file;
		DiskFileItem fi = (DiskFileItem) cf.getFileItem();
		File srcFile = fi.getStoreLocation();
		Image srcImg = ImageIO.read(srcFile);
		BufferedImage buffImg = null;
		buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		buffImg.getGraphics().drawImage(srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(buffImg, "JPEG", os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		inputstreamtofile(is, srcFile);
		return srcFile;
	}

	/**
	 * InputStream --> File
	 * 
	 * @author wengzhijie
	 * @param ins
	 * @param file
	 * @throws IOException
	 * @date 2016-1-8 上午10:56:05
	 */
	public void inputstreamtofile(InputStream ins, File file) throws IOException {
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		ins.close();
	}

	/**
	 * 通过URL上传，先下载保存本地，再上传
	 * 
	 * @param request
	 * @param file
	 * @param saveName
	 * @return
	 * @throws IOException
	 */
	public String saveSingleFile(HttpServletRequest request, String url) throws IOException {
		String saveName = java.util.UUID.randomUUID().toString();
		String floder = request.getSession().getServletContext().getRealPath("/resources/file");
		FileUtils.copyInputStreamToFile(new URL(url).openStream(), new File(floder, saveName));
		File file = new File(floder, saveName);
		OssUtil.uploadFile(file, saveName, false);
		return saveName;
	}

	/**
	 * 通过URL上传，不保存本地
	 * 
	 * @param request
	 * @param file
	 * @param saveName
	 * @return
	 * @throws IOException
	 */
	public String saveSingleFile2(HttpServletRequest request, String url) throws IOException {
		String saveName = java.util.UUID.randomUUID().toString();
		OssUtil.uploadFile(new URL(url), saveName);
		return saveName;
	}

}

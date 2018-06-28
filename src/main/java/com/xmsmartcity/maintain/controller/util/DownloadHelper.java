package com.xmsmartcity.maintain.controller.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 上传处理
 * 
 * @author sumory.wu
 * @date 2013-3-24 下午7:55:38
 */
@Component
public class DownloadHelper {

	private static Logger logger = LoggerFactory.getLogger(DownloadHelper.class);

	public static void handlerDownload(HttpServletRequest request,HttpServletResponse response,String filePath,String fileName){
		// this.servletContext.getRealPath("temp"); 
		InputStream in = null;
		try {
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("UTF-8"), "UTF-8"));
			File file = new File(fileName); 
			String path = request.getSession().getServletContext().getRealPath("/") +filePath; 
			System.out.println("path="+path);
			in = new FileInputStream(path + "/" + file);
			OutputStream os = response.getOutputStream(); 
			byte[] b = new byte[1024 * 1024];
			int length;
			while ((length = in.read(b)) > 0) {
				os.write(b, 0, length);
			}/*
			 */
			in.close();
			//response.flushBuffer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("下载文件失败", e);
		}  
	}

	/**
	 * 单文件下载
	 * @author zhangjunhao 2014-10-25 下午4:14:54
	 *
	 * @param request
	 * @param response 
	 * @param filePath 服务器存储文件的文件夹路劲 （/resources/file）
	 * @param fileName 下载的文件名
	 * @throws Exception
	 */
	public static void downLoad(HttpServletRequest request,HttpServletResponse response, 
			String filePath, String fileName) {
		try {
					
			response.setContentType("text/plain");
			//得到下载文件的名字
			//String filename=request.getParameter("filename");

			//解决中文乱码问题
			String filename=new String(fileName.getBytes("iso-8859-1"),"gbk");

			//获得路径
			String uploadPath = request.getServletContext().getRealPath("/") + filePath;
			
			//创建file对象
			File file=new File(uploadPath + filename);

			System.out.println("upf:"+uploadPath + filename);
			//设置response的编码方式
			response.setContentType("application/x-msdownload");

			//写明要下载的文件的大小
			response.setContentLength((int)file.length());

			//解决中文乱码
			response.setHeader("Content-Disposition","attachment;filename="+new String

					(filename.getBytes("gbk"),"iso-8859-1"));       

			//读出文件到i/o流
			FileInputStream fis=new FileInputStream(file);
			BufferedInputStream buff=new BufferedInputStream(fis);

			byte [] b=new byte[1024];//相当于我们的缓存

			long k=0;//该值用于计算当前实际下载了多少字节

			//从response对象中得到输出流,准备下载

			OutputStream myout=response.getOutputStream();

			//开始循环下载

			while(k<file.length()){

				int j=buff.read(b,0,1024);
				k+=j;

				//将b中的数据写到客户端的内存
				myout.write(b,0,j);

			}

			//将写入到客户端的内存的数据,刷新到磁盘
			myout.flush();

		} catch (Exception e) {
			logger.info("文件下载失败", e);
		}

		/*//获得路径
		String uploadPath = request.getServletContext().getRealPath("/") + filePath;//文件来源

		//String filePath = fileName;
		//String fileName = name;
		try {
			if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0){
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");//firefox浏览器
			}else {
				if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0){
					fileName = URLEncoder.encode(fileName, "UTF-8");//IE浏览器
				}
			}  
			response.setContentType("text/plain");
			response.setHeader("Location",fileName);
			response.reset();
			response.setHeader("Cache-Control", "max-age=0" );
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			OutputStream fos = null;
			InputStream fis = null;
			//获得文件全路径
			filePath = uploadPath + fileName;
			fis = new FileInputStream(filePath);
			bis = new BufferedInputStream(fis);
			fos = response.getOutputStream();
			bos = new BufferedOutputStream(fos);
			int bytesRead = 0;
			byte[] buffer = new byte[5 * 1024];
			while ((bytesRead = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
			}
			bos.close();
			bis.close();
			fos.close();
			fis.close();
		} catch (Exception e) {
			logger.info("文件下载失败", e);
		}*/


	}

	/**
	 * 批量下载
	 * @author zhangjunhao 2014-10-23 下午4:52:20
	 *
	 * @param request
	 * @param response
	 * @param filePath
	 * @param fileName 传过来的文件名以逗号分割  
	 */
	public static void batchDownload(HttpServletRequest request,HttpServletResponse response,String filePath,String fileName){
		try {

			String path = request.getSession().getServletContext().getRealPath("/") +filePath; 

			String[] fileStr = fileName.split(",");
			File[] file = new File[fileStr.length];
			//创建文件数组
			for (int i=0; i<fileStr.length; i++) {
				file[i] = new File(path + "/" + fileStr[i]);
			}
			byte[] buffer = new byte[1024];


			//以时间命名zip文件
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date now = new Date();
			//打包的文件名.zip
			String zipName= sf.format(now)+".zip";
			//把文件打包到file文件夹
			String zipPathName = request.getSession().getServletContext().getRealPath("\\") + 
					"resources\\file\\"+zipName;

			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipPathName));
			for(int i=0;i<file.length;i++) {

				FileInputStream fis = new FileInputStream(file[i]);

				out.putNextEntry(new ZipEntry(file[i].getName()));

				int len;

				//读入需要下载的文件的内容，打包到zip文件

				while((len = fis.read(buffer))>0) {

					out.write(buffer,0,len);

				}
				fis.close();

			}
			out.flush();
			out.close();
			//打包到服务器成功后，以单个文件再进行下载
			handlerDownload(request, response, filePath, zipName);
		} catch (Exception e) {
			logger.error("批量下载文件失败", e);
		} 
	}
}

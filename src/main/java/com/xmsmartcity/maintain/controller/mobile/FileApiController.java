package com.xmsmartcity.maintain.controller.mobile;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmsmartcity.maintain.pojo.File;
import com.xmsmartcity.maintain.pojo.FileType;
import com.xmsmartcity.maintain.service.FileService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.Utils;

@Controller
@RequestMapping(value = "/api/file")
public class FileApiController {

	@Autowired
	private FileService fileService;
	
	/**
	 * 获取文件列表（file字段）
	 * @param userId
	 * @param token
	 * @param file
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/select-byCondition")
	@ResponseBody
	public String selectByCondition(Integer userId, String token, File file,Integer startIndex,
		Integer pageSize) {
		Assert.state(file !=null,Constant.PARAMS_ERROR);
		List<File> res = fileService.selectByRecord(startIndex, pageSize, userId,file);
		return Utils.toSuccessJson(res);
	}
	
	/**
	 * 获取文件类别列表
	 * @param userId
	 * @param token
	 * @param fileType
	 * @return
	 */
	@RequestMapping(value = "/select-fileType")
	@ResponseBody
	public String selectFileType(Integer userId, String token,FileType fileType) {
		List<FileType> res = fileService.selectFileType(userId,fileType.getId(),fileType.getFileTypeName(),fileType.getFileTypeCode());
		return Utils.toSuccessJson(res);
	}
	
}

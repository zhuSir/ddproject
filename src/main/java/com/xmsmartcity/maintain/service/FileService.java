package com.xmsmartcity.maintain.service;

import java.util.List;

import com.xmsmartcity.maintain.pojo.File;
import com.xmsmartcity.maintain.pojo.FileType;

public interface FileService extends BaseService<File>{

	/**
	 * 查找文档
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<File> selectByRecord(Integer startIndex,Integer pageSize,Integer userId,File file);
	/**
	 * 查找文件类型
	 * @param fileType
	 * @return
	 */
	public List<FileType> selectFileType(Integer userId,Integer id,String fileTypeName,String fileTypeCode);
}

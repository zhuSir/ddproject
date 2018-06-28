package com.xmsmartcity.maintain.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.FileMapper;
import com.xmsmartcity.maintain.dao.FileTypeMapper;
import com.xmsmartcity.maintain.pojo.File;
import com.xmsmartcity.maintain.pojo.FileType;
import com.xmsmartcity.maintain.service.FileService;
@Service
public class FileServiceImpl extends BaseServiceImpl<File> implements FileService {
	
	public FileServiceImpl(BaseDao<File> dao) {
		super(dao);
	}

	@Autowired
	private FileMapper fileDao;
	
	@Autowired
	private FileTypeMapper fileTypeDao;
	
	@Override
	public List<FileType> selectFileType(Integer userId,Integer id,String fileTypeName,String fileTypeCode) {
		// TODO Auto-generated method stub
		List<FileType> fileTypes = fileTypeDao.selectFileType(userId, id, fileTypeName, fileTypeCode);
		return fileTypes;
	}

	@Override
	public List<File> selectByRecord(Integer startIndex, Integer pageSize, Integer userId, File file) {
		// TODO Auto-generated method stub
		List<File> files=fileDao.selectByRecord(startIndex, pageSize, userId, file);
		return files;
	}

}

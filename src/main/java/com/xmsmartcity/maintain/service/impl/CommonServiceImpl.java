package com.xmsmartcity.maintain.service.impl;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.xmsmartcity.maintain.dao.FaultSummaryMapper;
import com.xmsmartcity.orm.Pagination;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xmsmartcity.maintain.service.CommonService;
import com.xmsmartcity.util.Constant;

import net.sf.json.JSONObject;

@Service
public class CommonServiceImpl implements CommonService{

	@Autowired
	private FaultSummaryMapper summarDao;

	/**
	 * 获取版本号
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:56:57
	 * @param appType
	 * @param currentVersion
	 * @return
	 * @throws Exception
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.CommonService#getAppVersion(java.lang.Integer, java.lang.String)
	 */
	@Override
	public String getAppVersion(Integer appType,String currentVersion) throws Exception {
		Assert.state(appType != null,Constant.PARAMS_ERROR);
		Properties prop = new Properties();
		InputStream in = CommonServiceImpl.class.getClassLoader().getResourceAsStream("system.properties");
		prop.load(in);
		String iosLowVersion = prop.getProperty("ios_low_version");
		String iosHighVersion = prop.getProperty("ios_high_version");
		String androidLowVersion = prop.getProperty("android_low_version");
		String androidHighVersion = prop.getProperty("android_high_version");
		JSONObject obj = new JSONObject();
		if(appType == 0){
			Integer lastForce = 1;//是否强制更新
			lastForce = isLastForce(lastForce,currentVersion,iosLowVersion);
			obj.put("lastforce", lastForce);
			obj.put("version", iosHighVersion);//最高版本号
			String upgradeinfo = prop.getProperty("ios_upgradeinfo");
			obj.put("upgradeinfo", upgradeinfo);//更新提示信息
		}else{
			Integer lastForce = 1;//是否强制更新
			lastForce = isLastForce(lastForce,currentVersion,androidLowVersion);
			obj.put("lastforce", lastForce);
			obj.put("version", androidHighVersion);//最高版本号
			String updateurl = prop.getProperty("android_updateurl");
			String upgradeinfo = prop.getProperty("android_upgradeinfo");
			obj.put("updateurl", updateurl);//更新路径
			obj.put("upgradeinfo", upgradeinfo);//更新提示信息
		}
		return obj.toString();
	}

	public Integer isLastForce(Integer lastForce,String currentVersion,String iosLowVersion){
		if(StringUtils.isNotEmpty(currentVersion)) {
			lastForce = 0;
			String[] currVersions = currentVersion.split("\\.");
			String[] lowVersions = iosLowVersion.split("\\.");
			try {
				for (int i = 0; i < lowVersions.length; i++) {
					Integer lone = Integer.valueOf(lowVersions[i]);
					Integer cone = 0;
					if (currVersions.length - 1 < i) {
						break;
					}
					cone = Integer.valueOf(currVersions[i]);
					if (lone < cone) {
						break;
					} else if (lone > cone) {
						//强制更新
						lastForce = 1;
						break;
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
				lastForce = 1;//下标越界版本号错误强制更新
			}
		}
		return lastForce;
	}


	/**
	 * 全局搜索
	 * @param searchContent
	 * @param type
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public Object searchOverallData(Integer userId,String searchContent,Integer type,Integer pageNum,Integer pageSize) {
		List<Map<String,Object>> results = summarDao.searchOverallData(userId,searchContent,type,new Pagination<Map<String,Object>>(pageNum,pageSize));
		Pagination<Map<String, Object>> pageInfo = new Pagination<>(results);
		return pageInfo;
	}

}

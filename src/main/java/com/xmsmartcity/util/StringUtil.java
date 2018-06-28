package com.xmsmartcity.util;

import java.util.Calendar;


/**
 * 对字符串处理
 * @author ZYP  @date 2016-8-23 
 */
public class StringUtil { 
	public static void main(String[] args) {
	}
	/**
	 * 截取字符串
	 * @author ZYP  @date 2016-9-3
	 * @param str 原字符串
	 * @param len 长度
	 * @return
	 * 
	 */
	public static String getCutStr(String str,Integer len){
		if(str.length()>len){
			str = str.substring(0, len)+"...";
		}
		return str;
	}
	
	
	/**
	 * 返回编号（年月日时分秒+用户id）
	 * @author ZYP  @date 2016-9-4 
	 * @param userId
	 * @return
	 * 
	 */
	public static String getCodeNum(Integer userId){
		Calendar cal = Calendar.getInstance();
		String year=String.valueOf(cal.get(Calendar.YEAR));//获取年份
		String month=String.valueOf(cal.get(Calendar.MONTH)+1);//获取月份
		 if(month.length()==1){
	        	month="0"+month;
	        }
        String day=String.valueOf(cal.get(Calendar.DATE));//获取日
        if(day.length()==1){
        	day="0"+day;
        }
		return ""+year+month+day+cal.get(Calendar.HOUR)+cal.get(Calendar.SECOND)+cal.get(Calendar.MINUTE)+userId;
	};
	
	
	/**
	 * 返回编号（）
	 * @author ZYP  @date 2016-9-23 
	 * @param id
	 * @return
	 * 
	 */
	public static String getFaultInfoCode(Integer id){
		String code = "dd";
		code="00000000"+id;
		return "dd"+code.substring(code.length()-8);
	};
	
	/**
	 * 获取团队关键字
	 * @param companyName
	 * @return
	 */
	public static String getCompanyKeyword(String companyName){
		for(String ct : citys){
			boolean isTrue = companyName.contains(ct);
			if(isTrue){
				companyName = companyName.replace(ct, "");
				continue;
			}
		}
		for(String suffix : suffixs){
			if(companyName.contains(suffix)){
				companyName = companyName.replace(suffix, "");
				continue;
			}
		}
		return companyName;
	}
	public static String citys[] = {"南宁市","徐州市","烟台市","唐山市","柳州市","常州市","鞍山市","厦门市","抚顺市","吉林市市","洛阳市","大同市","包头市","大庆市","淄博市","乌鲁木齐市","佛山市","呼和浩特市","齐齐哈尔市","泉州市","西宁市","兰州市","贵阳市","温州市","北京市","上海市","广州市","天津市","武汉市","沈阳市","哈尔滨市","西安市","南京市","成都市","重庆市","深圳市","杭州市","青岛市","苏州市","太原市","郑州市","济南市","长春市"," 合肥市","长沙市","南昌市","无锡市","昆明市","宁波市","福州市","石家庄市"};
	
	public static String suffixs[] = {"有限公司","股份有限公司","公司"};
	
}

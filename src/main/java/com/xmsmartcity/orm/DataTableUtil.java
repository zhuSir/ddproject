package com.xmsmartcity.orm;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 此方法不支持实体类内 拥有其他实体类类型的字段。仅支持：String、Integer…… List Map 等基础数据类型和集合类型。
 * @author linweiqin
 * @Date 2014-9-25 下午3:50:36
 */
public class DataTableUtil {
	private static Logger logger = LoggerFactory.getLogger(DataTableUtil.class);
	private static List<String[]> trDataList;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 传入实体类，将其封装到trDataList
	 * （注意：传入的obj对象不支持实体类内 拥有其他实体类类型的字段。仅支持：String、Integer…… List Map 等基础数据类型和集合类型。）
	 * @author linweiqin
	 * @Date 2014-9-25 下午4:05:19
	 * @param objList
	 * @param fieldNames ：表示 页面需要展示的数据字段。
	 * @return
	 */
	public static List<String[]> getTrDataList(List<?> objList,String[] fieldNames) {
		trDataList = new ArrayList<String[]>();
		PropertyDescriptor pd = null;
		if(objList == null || objList.size() == 0){
			return trDataList;
		}
		String fieldOrder = "";
		int j = 0;
		int k = 0;
		for (Object obj : objList) {
			Field[] fields = obj.getClass().getDeclaredFields();
			List<String> valueArr = new ArrayList<String>();
			String[] strArr = {};
			try{
				for (String fName : fieldNames) {
					for (int i = 0; i < fields.length; i++) {
						// 如果字段名相等（3层循环的写法主要是为了保证值的顺序遇字段顺序相匹配）
						if(fName.equals(fields[i].getName())){
							pd = new PropertyDescriptor(fields[i].getName(),obj.getClass());
							Object objFieldValue = pd.getReadMethod().invoke(obj);
								Object value = null;
								if(null == objFieldValue || "".equals(objFieldValue)){
									valueArr.add("");
									continue;
								}
								// 如果是Date类型，则格式化成时间
								if(fields[i].getType().getName().equals(Date.class.getName())){
									value = sdf.format( (Date)objFieldValue );
								}else{
									value = objFieldValue;
								}
								if(j == 0){						
									fieldOrder += k + "-" + fields[i].getName() + " ";
									k++;
								}
								valueArr.add(value.toString());
								break;
							}				
						
					}
				}
				valueArr.add("为“操作”列而add");
				trDataList.add(valueArr.toArray(strArr));
			}catch(Exception e){
				logger.info("DataTableUtil build method error", e);
				e.printStackTrace();
			}
				
			j++;
		}
		System.out.println( "———————" + objList.get(0).getClass().getSimpleName() + "——————— 列表字段顺序为：" + fieldOrder);
		
		return trDataList;
	}
	/**
	 * 为dataTableParamater中的iDisplayStart和iDisplayLength赋值
	 * @author linweiqin
	 * @Date 2014-9-28 上午9:08:59
	 * @param result
	 * @param dataTableParamater
	 * @param aaData
	 */
	public static void setPageNoAndSize(DataTableParamater dataTableParamater){
		dataTableParamater.setAoData(dataTableParamater.getAoData().replaceAll("&quot;", "\""));
		JSONArray jsonArray = JSONArray.fromObject(dataTableParamater.getAoData());
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = (JSONObject) jsonArray.get(i);

			if (obj.get("name").equals("iDisplayLength")){
				//每页显示的长度
				int length = Integer.parseInt(obj.get("value").toString());
				dataTableParamater.setIDisplayLength(length);
				// 设置当前页
				dataTableParamater.setPageNo(dataTableParamater.getIDisplayStart() == 0 ? 1 : dataTableParamater.getIDisplayStart() / length + 1);	
			}
			
			if (obj.get("name").equals("iDisplayStart")){
				//开始显示行
				dataTableParamater.setIDisplayStart(Integer.parseInt(obj.get("value").toString()));
			}
		}
	}
}

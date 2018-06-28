package com.xmsmartcity.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
/**
 * 自定义JSONObject序列化Date类型数据格式
 * @author chenby
 *
 */
public class JsonDateValueProcessor implements JsonValueProcessor {

	private String format = "yyyy-MM-dd";
	
	public JsonDateValueProcessor() {
        super();
    }
	
	/**
	 * 自定义格式
	 * @param format
	 */
	public JsonDateValueProcessor(String format) { 
        super();
        this.format = format;
    }
	
	@Override
	public Object processArrayValue(Object arg0, JsonConfig arg1) {
		// TODO Auto-generated method stub
		return process(arg0);
	}

	@Override
	public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
		// TODO Auto-generated method stub
		return process(arg0);
	}
	
	private Object process(Object value) {
        if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
            return sdf.format(value);
        }
        return value == null ? "" : value.toString();
    }

}

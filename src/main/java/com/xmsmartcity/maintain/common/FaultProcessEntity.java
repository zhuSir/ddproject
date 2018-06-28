package com.xmsmartcity.maintain.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 报障流程注解对象
 * @author:zhugw
 * @time:2017年6月8日 下午4:47:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FaultProcessEntity {
	
	public int type();
	
}

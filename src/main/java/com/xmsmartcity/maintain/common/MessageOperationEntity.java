package com.xmsmartcity.maintain.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

/**
 * 消息操作对象注解
 * @author:zhugw
 * @time:2017年11月29日 上午11:15:26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MessageOperationEntity {

	public int type();
	
}

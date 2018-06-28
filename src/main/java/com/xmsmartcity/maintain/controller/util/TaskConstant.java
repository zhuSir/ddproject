package com.xmsmartcity.maintain.controller.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 任务调度基础类
 * 
 * @author felix
 * @date 2015-8-12 下午2:29:09
 */
public interface TaskConstant{
	
	public final static ExecutorService POOL = Executors.newCachedThreadPool();
}

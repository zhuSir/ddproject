package com.xmsmartcity.maintain.controller.util;



/**
 * 任务调度基础类
 * 
 * @author felix
 * @date 2015-8-12 下午2:29:09
 */
public abstract class Task implements Runnable{
	
	@Override
	public void run() {
		try {
		   deal();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 具体业务逻辑处理
	 * 
	 * @author felix  @date 2015-8-12 下午2:27:44
	 */
	public abstract String deal() throws Exception;
	
	
}

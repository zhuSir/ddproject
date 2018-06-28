package com.xmsmartcity.extra.aliyun;

import java.util.Random;

public class Test {

	public void out(){
		try{
			Random random = new Random();
			for(int n = 0 ; n < 1000 ; n ++){
				System.out.println(Thread.currentThread().getName() + " : "+n);
				int x  =random.nextInt(10000);
				if("Thread-0".equals(Thread.currentThread().getName() )){
					Thread.sleep(100l);
					continue;
				}
				Thread.sleep(x);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	
	public static void main(String[] args) {
		final Test test = new Test();
		
		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				test.out();
			}
		};
		
		new Thread(r1).start();
		System.out.println("```````````");
		new Thread(r1).start();
		System.out.println("```````````");
		new Thread(r1).start();
	}
}

package com.sccl.attech.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/***
 * 创建线程池工具类
 * @author luoyang
 * create time 2012-01-06
 *
 */
public class ThreadUtils {
	private static ExecutorService pool = null;
	private  ThreadUtils(){
		//创建一个可重用固定线程数的线程池 
		pool = Executors.newFixedThreadPool(100);
	} 
	
	public static ExecutorService getExecutorService(){
		if(pool == null){
			new ThreadUtils();
		}
		return pool;
	}

}

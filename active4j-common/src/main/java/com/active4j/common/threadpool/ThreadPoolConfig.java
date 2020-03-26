package com.active4j.common.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * @title ThreadPoolConfig.java
 * @description 
		  系统共用线程池 配置
 * @time  2019年12月4日 下午4:24:02
 * @author 麻木神
 * @version 1.0
*/

@Configuration
@Slf4j
public class ThreadPoolConfig {
	
	
	@Autowired
	private ThreadPoolProperties loader;
	
	
	/**
	 * 
	 * @description
	 *  	线程池的初始化  采用spring线程池   二选一
	 * @return ThreadPoolTaskExecutor
	 * @author 麻木神
	 * @time 2019年12月4日 下午5:00:52
	 */
//	@Bean
//	public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
//		ThreadPoolTaskExecutor poolTask = new ThreadPoolTaskExecutor();
//		poolTask.setCorePoolSize(loader.getCorePoolSize()); //核心线程数
//		poolTask.setMaxPoolSize(loader.getMaxPoolSize()); //最大线程数
//		poolTask.setQueueCapacity(loader.getQueueCapacity()); //等待队列长度
//		poolTask.setKeepAliveSeconds(loader.getKeepAliveSeconds()); //空闲线程存活时间
//		//拒绝策略
//		poolTask.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy() {
//			@Override
//			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//				log.warn("注意,有任务超过线程池配置");
//				if(!executor.isShutdown()) {
//					r.run();
//				}
//			}
//        });
//		return poolTask;
//	}
	
	
	/**
	 * @description
	 *  	线程池的初始化  jdk方式  二选一
	 * @return ThreadPoolExecutor
	 * @author 麻木神
	 * @time 2019年12月4日 下午5:01:14
	 */
	@Bean
	public ThreadPoolExecutor threadPoolExecutor() {
		ThreadPoolExecutor poolTask = 
				new ThreadPoolExecutor(loader.getCorePoolSize(),  //核心线程数
						loader.getMaxPoolSize(),   //最大线程数
						loader.getKeepAliveSeconds(),  //空闲线程存活时间
						TimeUnit.SECONDS,  //时间单位
						new LinkedBlockingQueue<Runnable>(loader.getQueueCapacity()),  //等待队列长度
						//拒绝策略
						new ThreadPoolExecutor.CallerRunsPolicy() {
						@Override
						public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
							log.warn("注意,有任务超过线程池配置");
							if(!executor.isShutdown()) {
								r.run();
							}
						}
			        });
		return poolTask;
	}

}

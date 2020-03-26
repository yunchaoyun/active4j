package com.active4j.common.cache;

import java.time.Duration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;


/**
 * @title CacheUtils.java
 * @description 
		  Caffeine的使用
 * @time  2019年12月13日 下午2:10:31
 * @author 麻木神
 * @version 1.0
*/
public class CacheUtils {

	
	
	/**
	 * 利用同步方式，构建本地缓存器
	 */
	public static LoadingCache<String, String> stringCache = Caffeine.newBuilder()
																 .maximumSize(100) //设置最大缓存个数
																 .expireAfterAccess(Duration.ofHours(10))  //设置10个小时不访问 缓存回收
																 .refreshAfterWrite(Duration.ofSeconds(60))//设置每隔60秒刷新一次
																 .build(key -> {
																	 /**
																	  * 可集成一些从数据库中查询的数据，存入缓存
																	  */
																	 return null;
																 });
	
	/**
	 * 
	 * @description
	 * 		从本地缓存中取值
	 * @return String
	 * @author 麻木神
	 * @time 2019年12月13日 下午2:17:11
	 */
	public static String getStringCache(String key) {
		return stringCache.get(key);
	}
}

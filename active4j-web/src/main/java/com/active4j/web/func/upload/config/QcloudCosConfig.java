package com.active4j.web.func.upload.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.active4j.web.func.upload.properties.QcloudCosProperties;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;

import lombok.extern.slf4j.Slf4j;


/**
 * 
 * @title QcloudCosConfig.java
 * @description 
		腾讯云产品  对象存储  配置
 * @time  2019年12月16日 下午1:47:56
 * @author guyp
 * @version 1.0
 */
@Configuration
@Slf4j
public class QcloudCosConfig {
	
	@Autowired
	private QcloudCosProperties qcloudCosProperties;

	/**
	 * 
	 * @description
	 *  	COSClient是调用 COS API 接口的对象。
	 *  	在生成一个COSClient实例后可反复使用，线程安全。
	 *  	最后程序或服务退出时，关闭客户端即可
	 * @params
	 * @return COSClient
	 * @author guyp
	 * @time 2019年12月16日 下午1:48:07
	 */
	@Bean(destroyMethod="shutdown")
	public COSClient cosClient() {
		if(StringUtils.isEmpty(qcloudCosProperties.getSecretId()) || StringUtils.isEmpty(qcloudCosProperties.getSecretKey())
				|| StringUtils.isEmpty(qcloudCosProperties.getRegion())) {
			log.error("腾讯云对象存储COS参数没有配置，无法完成初始化");
			return null;
		}
		// 1 初始化用户身份信息（secretId, secretKey）。
		COSCredentials cred = new BasicCOSCredentials(qcloudCosProperties.getSecretId(), qcloudCosProperties.getSecretKey());
		// 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
		// clientConfig中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
		ClientConfig clientConfig = new ClientConfig(new Region(qcloudCosProperties.getRegion()));
		// 3 生成 cos 客户端。
		COSClient cosClient = new COSClient(cred, clientConfig);
		
		log.info("腾讯云对象存储COS客户端工具cosClient初始化完成");
		return cosClient;
	}
	
}

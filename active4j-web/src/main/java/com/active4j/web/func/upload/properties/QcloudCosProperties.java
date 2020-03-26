package com.active4j.web.func.upload.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 
 * @title QcloudCosProperties.java
 * @description 
		 腾讯云 对象存储 参数
 * @time  2019年12月16日 下午1:46:29
 * @author guyp
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "qcloud.cos")
@Data
public class QcloudCosProperties {

	/**
	 * 对接密钥
	 */
	private String secretId;
	
	/**
	 * 对接密钥
	 */
	private String secretKey;
	
	/**
	 * 存储区域
	 */
	private String region;
	
	/**
	 * 对象桶容器，专门存放资源的
	 */
	private String bucketName;
	
	/**
	 * 腾讯云访问地址前缀
	 */
	private String webSite;
	
}

package com.active4j.web.func.sms.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 
 * @title AliSmsProperties.java
 * @description 
		  阿里云参数配置
 * @time  2019年12月16日 上午10:39:16
 * @author 麻木神
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "func.sms.ali")
@Data
public class AliSmsProperties {

	private String regionId;
	
	private String accessKeyId;
	
	private String accessSecret;
	
	
}

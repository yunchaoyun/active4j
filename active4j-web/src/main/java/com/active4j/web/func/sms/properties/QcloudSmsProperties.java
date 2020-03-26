package com.active4j.web.func.sms.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @title QcloudSmsProperties.java
 * @description 
		  腾讯云参数配置
 * @time  2019年12月10日 下午4:32:07
 * @author 麻木神
 * @version 1.0
*/
@Configuration
@ConfigurationProperties(prefix = "func.sms.qcloud")
@Data
public class QcloudSmsProperties {
	
	private String appid;
	
	private String appkey;
}

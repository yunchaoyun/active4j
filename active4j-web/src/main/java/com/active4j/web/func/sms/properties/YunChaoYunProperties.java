package com.active4j.web.func.sms.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @title YunChaoYunProperties.java
 * @description 
		  读取云潮云短信配置
 * @time  2019年12月10日 下午3:41:24
 * @author 麻木神
 * @version 1.0
*/

@Configuration
@ConfigurationProperties(prefix = "func.sms.yunchaoyun")
@Data
public class YunChaoYunProperties {

	private String apikey;
	
	private String apiToken;
	
	
}

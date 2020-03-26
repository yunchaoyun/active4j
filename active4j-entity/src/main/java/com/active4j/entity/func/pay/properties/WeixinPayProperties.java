package com.active4j.entity.func.pay.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

/**
 * 
 * @title WeixinPayProperties.java
 * @description 
			   微信支付参数配置
 * @time  2020年1月3日 下午3:15:38
 * @author mhm
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "weixin.pay")
@Data
public class WeixinPayProperties {

	//关联的公众号ID
	private String appId;
	
	//微信支付分配的商户号
	private String mchId;
	
	//回调地址
	private String notifyUrl;
	
	//密钥
	private String key;
	
}

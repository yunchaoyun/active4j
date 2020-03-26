package com.active4j.entity.func.sms.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @title AliModel.java
 * @description 
		阿里云短信model
 * @time  2020年1月2日 上午10:40:00
 * @author guyp
 * @version 1.0
 */
@Setter
@Getter
public class AliModel {
	
	private String regionId;
	
	private String accessKeyId;
	
	private String accessSecret;
	
	private String smsSign;
	
	private String templateCode;
}

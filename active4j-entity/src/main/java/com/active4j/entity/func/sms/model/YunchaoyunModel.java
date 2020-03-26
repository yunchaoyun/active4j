package com.active4j.entity.func.sms.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @title YunchaoyunModel.java
 * @description 
		云潮云短信model
 * @time  2020年1月2日 上午10:37:47
 * @author guyp
 * @version 1.0
 */
@Setter
@Getter
public class YunchaoyunModel {
	
	private String apiKey;
	
	private String apiToken;
	
	private String smsSign;
	
	private String templateNo;
}

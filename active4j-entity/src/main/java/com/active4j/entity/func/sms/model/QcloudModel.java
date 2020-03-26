package com.active4j.entity.func.sms.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @title QcloudModel.java
 * @description 
		腾讯云短信model
 * @time  2020年1月2日 上午10:38:22
 * @author guyp
 * @version 1.0
 */
@Setter
@Getter
public class QcloudModel {
	
	private String appid;
	
	private String appkey;
	
	private String smsSign;
	
	private String templateId;
}

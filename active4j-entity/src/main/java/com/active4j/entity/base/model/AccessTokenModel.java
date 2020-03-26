package com.active4j.entity.base.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenModel {

	private String access_token;
	
	public AccessTokenModel() {}
	
	public AccessTokenModel(String access_token) {
		this.access_token = access_token;
	}
	
}

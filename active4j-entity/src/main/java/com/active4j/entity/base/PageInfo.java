package com.active4j.entity.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageInfo<T> {

	private long page;
	
	private long limit;
	
	public Page<T> getPageEntity() {
		
		return new Page<T>(this.getPage(), this.getLimit());
	}

	
	
	
	
}

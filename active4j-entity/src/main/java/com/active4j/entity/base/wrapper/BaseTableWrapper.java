package com.active4j.entity.base.wrapper;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;

import lombok.Getter;
import lombok.Setter;


/**
 * 结果包装类， 将查询的结果通过包装，以layui识别的格式返回
 * @author teli_
 *
 * @param <T>
 */
@Getter
@Setter
public class BaseTableWrapper<T> {

	private int code = 0;
	
	private String msg = "";
	
	private long count = 0;
	
	private List<T> data;
	
	public BaseTableWrapper() {
		
	}
	
	/**
	 * 通过构造函数 赋值相关变量  如果list中有变量值需要替换，可以覆写此方法，尤其是数据字典
	 * @param pageResult
	 */
	public BaseTableWrapper(IPage<T> pageResult) {
		
		this.setCount(pageResult.getTotal());
		
		this.setData(pageResult.getRecords());
		
	}
	
	public BaseTableWrapper(List<T> lstResult) {
		
		this.setCount(null == lstResult ? 0 : lstResult.size());
		
		this.setData(lstResult);
		
	}
	
	/**
	 * 转为字符串 输出  如果需要改写 可以覆写此方法
	 * @return
	 */
	public String wrap() {
		return JSON.toJSONString(this);
	}
	
}

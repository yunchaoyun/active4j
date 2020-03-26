package com.active4j.service.system.util;

import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * 系统工具类
 * @author teli_
 *
 */
public class SystemUtils {

	/**
	 * 数据字典map
	 */
	public static Map<String, Map<String, String>> Sys_dictionary = new HashMap<String, Map<String,String>>();
	
	/**
	 * 存入数据字典
	 * @param key
	 * @param map
	 */
	public static void putDictionary(String key, Map<String, String> map) {
		Sys_dictionary.put(key, map);
	}
	
	/**
	 * 获取字典对应的值
	 * @param key
	 * @param value
	 * @return
	 */
	public static String getDictionaryValue(String key, String value) {
		Map<String, String> map = Sys_dictionary.get(key);
		if(null != map && map.keySet().size() > 0) {
			return StringUtils.isEmpty(map.get(value)) ? "" : map.get(value);
		}
		return "";
	}
	
	
	/**
	 * 根据字典代码取得字典
	 * @param key
	 * @return
	 */
	public static Map<String, String> getDictionaryMap(String key) {
		return Sys_dictionary.get(key);
	}
	
	
	/**
	 * 部门Id 名称
	 */
	public static Map<String, String> mapDept = new HashMap<String, String>();
	
	/**
	 * 存放部门信息
	 * @param id
	 * @param name
	 */
	public static void putDept(String id, String name) {
		mapDept.put(id, name);
	}
	
	/**
	 * 根据Id获取部门名称
	 * @param id
	 * @return
	 */
	public static String getDeptNameById(String id) {
		return StringUtils.isEmpty(mapDept.get(id)) ? "" : mapDept.get(id);
	}
	
	/**
	 * 表单类别Id 名称
	 */
	public static Map<String, String> mapCategory = new HashMap<String, String>();
	
	/**
	 * 存放表单类别信息
	 * @param id
	 * @param name
	 */
	public static void putCategory(String id, String name) {
		mapCategory.put(id, name);
	}
	
	/**
	 * 根据Id获取表单类别名称
	 * @param id
	 * @return
	 */
	public static String getCategoryNameById(String id) {
		return StringUtils.isEmpty(mapCategory.get(id)) ? "" : mapCategory.get(id);
	}
	
}

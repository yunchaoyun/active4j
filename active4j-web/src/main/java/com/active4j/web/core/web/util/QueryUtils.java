package com.active4j.web.core.web.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.active4j.common.util.ReflectUtil;
import com.active4j.entity.base.BaseEntity;
import com.active4j.entity.base.annotation.QueryField;
import com.active4j.entity.base.model.QueryCondition;
import com.active4j.entity.common.GlobalConstant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * 通用的表格查询方法  直接构造查询条件
 * @author teli_
 *
 */
public class QueryUtils {

	
	/**
	 * 根据基础实体   实体上的注解 拼接查询条件
	 * @param baseEntity  实体传值
	 * @param paramsMap   日期类型的传值   _begin  _end
	 * @return
	 */
	public static <T> QueryWrapper<T> installQueryWrapper(BaseEntity baseEntity){
		return installQueryWrapper(baseEntity, null);
	}
	
	
	
	
	
	/**
	 * 根据基础实体   实体上的注解 拼接查询条件
	 * @param baseEntity   实体传值
	 * @return
	 */
	public static <T> QueryWrapper<T> installQueryWrapper(BaseEntity baseEntity, Map<String, String[]> paramsMap){
		
		QueryWrapper<T> t = new QueryWrapper<T>();
		
		List<Field> fields = new ArrayList<Field>();
		
		Class<?> clazz = baseEntity.getClass();
		while(null != clazz) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		
		//查询条件的拼接
		for(Field field : fields) {
			QueryField qf = field.getAnnotation(QueryField.class);
			
			if(null != qf) {
				//字段，取的是表的列名，不是实体名
				String queryColumn = qf.queryColumn();
				//构建的查询条件
				QueryCondition conditon = qf.condition();
				
				//如果是范围查询，主要是针对日期时间类型的 从  到
				if(QueryCondition.range.equals(conditon) && null != paramsMap) {
					//范围查询的开始值
					String[] beginValue = paramsMap.get(field.getName() + "_begin");
					if(null != beginValue && beginValue.length > 0) {
						if(StringUtils.isNotEmpty(beginValue[0].trim())) {
							t.ge(queryColumn, beginValue[0].trim());
						}
					}
					//范围查询的结束值
					String[] endValue = paramsMap.get(field.getName() + "_end");
					if(null != endValue && endValue.length > 0) {
						if(StringUtils.isNotEmpty(endValue[0].trim())) {
							t.le(queryColumn, endValue[0].trim());
						}
					}
					
				}
				
				//普通字段查询
				else {
					//通过反射技术获取字段的值，用于拼接查询条件
					Object value = ReflectUtil.getValueByField(field, baseEntity);
					
					//构建查询
					buildQueryWrapper(t, queryColumn, conditon, value);
				}
				
			}
		}
		
		//排序规则的处理
		String[] orderFields = paramsMap.get(GlobalConstant.Order_Field);
		String[] orderTypes = paramsMap.get(GlobalConstant.Order_Type);
		if(null != orderFields && null != orderTypes) {
			String orderField = orderFields[0].trim();
			String orderType = orderTypes[0].trim();
			if(StringUtils.equals(orderType, "asc")) {
				//升序排列
				t.orderByAsc(com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(orderField));
			}else if(StringUtils.equals(orderType, "desc")){
				//降序排列
				t.orderByDesc(com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(orderField));
			}
			
		}
		
		return t;
	}
	
	/**
	 * 构建查询条件
	 * @param <T>
	 * @param column    列名
	 * @param conditon  条件
	 * @param value     值
	 */
	public static <T> void buildQueryWrapper(QueryWrapper<T> queryWrapper, String column, QueryCondition condition, Object value) {
		if(StringUtils.isNotEmpty(column) && null != value) {
			
			
			switch(condition) {
				case eq:
					queryWrapper.eq(column, value);
					break;
				case like:
					queryWrapper.like(column, value);
					break;
				default:
				break;
			}
			
		}
	}
	
}

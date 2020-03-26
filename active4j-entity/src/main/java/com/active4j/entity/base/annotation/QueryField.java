package com.active4j.entity.base.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.active4j.entity.base.model.QueryCondition;


/**
 * 标注实体的查询字段，用于自动生成查询条件   QueryWrapper
 * @author teli_
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Inherited
public @interface QueryField {

	String queryColumn();
	
	QueryCondition condition() default QueryCondition.eq;
	
}

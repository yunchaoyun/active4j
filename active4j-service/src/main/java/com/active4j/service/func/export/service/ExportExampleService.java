package com.active4j.service.func.export.service;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.active4j.entity.func.export.entity.ExportExampleEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 
 * @title ExportExampleService.java
 * @description 
		导入导出
 * @time  2019年12月17日 上午10:47:07
 * @author guyp
 * @version 1.0
 */
public interface ExportExampleService extends IService<ExportExampleEntity> {
	
	/**
	 * 
	 * @description
	 *  	保存xlsx格式的excel文件
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月17日 下午1:33:40
	 */
	public void saveXlsx(InputStream in) throws Exception;
	
	/**
	 * 
	 * @description
	 *  	保存xls格式的excel文件
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月17日 下午1:37:53
	 */
	public void saveXls(InputStream in) throws Exception;
	
	/**
	 * 
	 * @description
	 *  	根据查询条件导出xlsx
	 * @params
	 * 		name 姓名
	 * @return HSSFWorkbook
	 * @author guyp
	 * @time 2019年12月18日 上午9:54:50
	 */
	public void exportXlsx(HttpServletRequest request, HttpServletResponse response, String name) throws Exception;
	
	/**
	 * 
	 * @description
	 *  	根据查询条件导出xls
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月18日 上午11:22:57
	 */
	public void exportXls(HttpServletRequest request, HttpServletResponse response, String name) throws Exception;
	
	/**
	 * 
	 * @description
	 *  	根据查询条件导出csv
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月18日 上午10:31:04
	 */
	public void exportCsv(HttpServletRequest request, HttpServletResponse response, String name) throws Exception;
}

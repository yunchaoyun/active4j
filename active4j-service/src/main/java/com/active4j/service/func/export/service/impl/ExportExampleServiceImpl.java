package com.active4j.service.func.export.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import com.active4j.common.func.export.util.ExcelUtil;
import com.active4j.common.func.export.util.ExportUtil;
import com.active4j.common.util.DateUtils;
import com.active4j.dao.func.export.dao.ExportExampleDao;
import com.active4j.entity.func.export.entity.ExportExampleEntity;
import com.active4j.service.func.export.service.ExportExampleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title ExportExampleServiceImpl.java
 * @description 
		导入导出
 * @time  2019年12月17日 上午10:48:51
 * @author guyp
 * @version 1.0
 */
@Service("exportExampleService")
@Transactional
@Slf4j
public class ExportExampleServiceImpl extends ServiceImpl<ExportExampleDao, ExportExampleEntity> implements ExportExampleService {
	
	/**
	 * 
	 * @description
	 *  	保存xlsx格式的excel文件
	 * @params
	 * @return void
	 * @author guyp
	 * @throws Exception 
	 * @time 2019年12月17日 下午1:33:40
	 */
	public void saveXlsx(InputStream in) throws Exception {
		List<ExportExampleEntity> lstExports = new ArrayList<ExportExampleEntity>();
		//解析excel的内容
		List<List<Object>> lstA = ExcelUtil.readBigFile2(in, -1);
		//遍历行
		for(int i = 1; i < lstA.size(); i++) {
			List<Object> lstObj = lstA.get(i);
			//创建示例实体
			ExportExampleEntity export = new ExportExampleEntity();
			//遍历列
			for(int j = 0; j < lstObj.size(); j++) {
				Object obj = lstObj.get(j);
				//属性赋值
				if(j == 0 && null != obj) {
					export.setName(obj.toString());
				}else if(j == 1 && null != obj) {
					export.setSex(obj.toString());
				}else if(j == 2 && null != obj) {
					if(obj instanceof Integer) {
						export.setAge(Integer.parseInt(obj.toString()));
					}
				}else if(j == 3 && null != obj) {
					export.setPhone(obj.toString());
				}else if(j == 4 && null != obj) {
					export.setBirthday(DateUtils.parseDateObj(obj));
				}else if(j == 5 && null != obj) {
					if(obj instanceof Double) {
						export.setBalance(Double.parseDouble(obj.toString()));
					}
				}
			}
			//实体不为空就假如集合
			if(null != export) {
				lstExports.add(export);
			}
		}
		
		//批量保存
		this.saveBatch(lstExports);
	}

	/**
	 * 
	 * @description
	 *  	保存xls格式的excel文件
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月17日 下午1:37:53
	 */
	public void saveXls(InputStream in) throws Exception {
		Workbook workbook = ExcelUtil.readExcel(".xls", in);
		if(null != workbook) {
			//获取第一个sheet
			Sheet sheet = workbook.getSheetAt(0);
			//获取起始行
			int firstRowNum = sheet.getFirstRowNum();
			//获取最后一行
			int lastRowNum = sheet.getLastRowNum();
			log.info("文件解析出起始行{}, 结束行{}", firstRowNum, lastRowNum);
			if(lastRowNum >= firstRowNum) {
				List<ExportExampleEntity> lstExports = new ArrayList<ExportExampleEntity>();
				//由于示例excel，这里直接读取第2行的数据
				for(int i = 1; i <= lastRowNum; i++) {
					//创建示例实体
					ExportExampleEntity export = new ExportExampleEntity();
					//获取行数据
					Row row = sheet.getRow(i);
					if(null != row) {
						//属性赋值
						//获取列，下标从0开始
						Cell cell0 = row.getCell(0);
						if(null != cell0) {
							export.setName(ExcelUtil.getCellValue(cell0));
						}
						Cell cell1 = row.getCell(1);
						if(null != cell1) {
							export.setSex(ExcelUtil.getCellValue(cell1));
						}
						Cell cell2 = row.getCell(2);
						if(null != cell2) {
							export.setAge(Integer.parseInt(ExcelUtil.getCellValue(cell2)));
						}
						Cell cell3 = row.getCell(3);
						if(null != cell3) {
							export.setPhone(ExcelUtil.getCellValue(cell3));
						}
						Cell cell4 = row.getCell(4);
						if(null != cell4) {
							export.setBirthday(DateUtils.parseDateObj(ExcelUtil.getCellValue(cell4)));
						}
						Cell cell5 = row.getCell(5);
						if(null != cell5) {
							export.setBalance(Double.parseDouble(ExcelUtil.getCellValue(cell5)));
						}
					}
					//实体不为空就假如集合
					if(null != export) {
						lstExports.add(export);
					}
				}
				
				//批量保存
				this.saveBatch(lstExports);
			}
		}
	}

	/**
	 * 
	 * @description
	 *  	根据查询条件导出xlsx
	 * @params
	 * @return HSSFWorkbook
	 * @author guyp
	 * @throws UnsupportedEncodingException 
	 * @time 2019年12月18日 上午9:54:50
	 */
	public void exportXlsx(HttpServletRequest request, HttpServletResponse response, String name) throws Exception {
		String fileName = getFileName(request, "测试数据.xlsx");
		//写出数据定义
	    response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
	 
	    List<LinkedHashMap<String, Object>> datas = new ArrayList<>();
	    LinkedHashMap<String, Object> data = new LinkedHashMap<>();
	    //定义列数据
	    data.put("0", "姓名");
	    data.put("1", "性别");
	    data.put("2", "年龄");
	    data.put("3", "手机号");
	    data.put("4", "生日");
	    data.put("5", "余额/元");
	    datas.add(data);
	    
	    //根据条件查询实体集合
  		QueryWrapper<ExportExampleEntity> queryWrapper = new QueryWrapper<ExportExampleEntity>();
  		//查询条件添加
  		if(StringUtils.isNotEmpty(name)) {
  			queryWrapper.eq("NAME", name);
  		}
  		List<ExportExampleEntity> lstExamples = this.list(queryWrapper);
	    if(null != lstExamples) {
	    	//实体给列赋值
	    	for(ExportExampleEntity example : lstExamples) {
	    		data = new LinkedHashMap<>();
	    		data.put("0", StringUtils.isEmpty(example.getName()) ? "" : example.getName());
	    		data.put("1", StringUtils.isEmpty(example.getSex()) ? "" : example.getSex());
	    		data.put("2", example.getAge());
	    		data.put("3", StringUtils.isEmpty(example.getPhone()) ? "" : example.getPhone());
	    		data.put("4", null == example.getBirthday() ? "" : DateUtils.getDate2Str(example.getBirthday()));
	    		data.put("5", example.getBalance());
	    		datas.add(data);
	    	}
	    }
	    
	    Map<String, List<LinkedHashMap<String, Object>>> tableData = new HashMap<>();
	    tableData.put("测试1", datas);
	    
	    //拷贝输出
	    FileCopyUtils.copy(ExportUtil.exportXlsx(tableData), response.getOutputStream());
	}

	/**
	 * 
	 * @description
	 *  	根据查询条件导出csv
	 * @params
	 * @return void
	 * @author guyp
	 * @throws IOException 
	 * @time 2019年12月18日 上午10:31:04
	 */
	public void exportCsv(HttpServletRequest request, HttpServletResponse response, String name) throws Exception {
		String fileName = getFileName(request, "测试数据.csv");
		//写出数据定义
	    response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
	 
	    List<LinkedHashMap<String, Object>> datas = new ArrayList<>();
	    LinkedHashMap<String, Object> data = new LinkedHashMap<>();
	    //定义列数据
	    data.put("0", "姓名");
	    data.put("1", "性别");
	    data.put("2", "年龄");
	    data.put("3", "手机号");
	    data.put("4", "生日");
	    data.put("5", "余额/元");
	    datas.add(data);
	    
	    //根据条件查询实体集合
  		QueryWrapper<ExportExampleEntity> queryWrapper = new QueryWrapper<ExportExampleEntity>();
  		//查询条件添加
  		if(StringUtils.isNotEmpty(name)) {
  			queryWrapper.eq("NAME", name);
  		}
  		List<ExportExampleEntity> lstExamples = this.list(queryWrapper);
	    if(null != lstExamples) {
	    	//实体给列赋值
	    	for(ExportExampleEntity example : lstExamples) {
	    		data = new LinkedHashMap<>();
	    		data.put("0", StringUtils.isEmpty(example.getName()) ? "" : example.getName());
	    		data.put("1", StringUtils.isEmpty(example.getSex()) ? "" : example.getSex());
	    		data.put("2", example.getAge());
	    		data.put("3", StringUtils.isEmpty(example.getPhone()) ? "" : example.getPhone());
	    		data.put("4", null == example.getBirthday() ? "" : DateUtils.getDate2Str(example.getBirthday()));
	    		data.put("5", example.getBalance());
	    		datas.add(data);
	    	}
	    }
	    //拷贝输出
	    FileCopyUtils.copy(ExportUtil.exportCSV(datas), response.getOutputStream());
	}
	
	/**
	 * 
	 * @description
	 *  	不同浏览器使用的默认的编码不同，有可能出现乱码
	 * @params
	 * @return String
	 * @author guyp
	 * @time 2019年12月18日 上午10:56:31
	 */
	private String getFileName(HttpServletRequest request, String name) throws UnsupportedEncodingException {
	    String userAgent = request.getHeader("USER-AGENT");
	    return userAgent.contains("Mozilla") ? new String(name.getBytes(), "ISO8859-1") : name;
	}

	
	/**
	 * 
	 * @description
	 *  	根据查询条件导出xls
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月18日 上午11:22:57
	 */
	public void exportXls(HttpServletRequest request, HttpServletResponse response, String name) throws Exception {
		String fileName = getFileName(request, "测试数据.xls");
		//写出数据定义
	    response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
	 
	    List<LinkedHashMap<String, Object>> datas = new ArrayList<>();
	    LinkedHashMap<String, Object> data = new LinkedHashMap<>();
	    //定义列数据
	    data.put("0", "姓名");
	    data.put("1", "性别");
	    data.put("2", "年龄");
	    data.put("3", "手机号");
	    data.put("4", "生日");
	    data.put("5", "余额/元");
	    datas.add(data);
	    
	    //根据条件查询实体集合
  		QueryWrapper<ExportExampleEntity> queryWrapper = new QueryWrapper<ExportExampleEntity>();
  		//查询条件添加
  		if(StringUtils.isNotEmpty(name)) {
  			queryWrapper.eq("NAME", name);
  		}
  		List<ExportExampleEntity> lstExamples = this.list(queryWrapper);
	    if(null != lstExamples) {
	    	//实体给列赋值
	    	for(ExportExampleEntity example : lstExamples) {
	    		data = new LinkedHashMap<>();
	    		data.put("0", StringUtils.isEmpty(example.getName()) ? "" : example.getName());
	    		data.put("1", StringUtils.isEmpty(example.getSex()) ? "" : example.getSex());
	    		data.put("2", example.getAge());
	    		data.put("3", StringUtils.isEmpty(example.getPhone()) ? "" : example.getPhone());
	    		data.put("4", null == example.getBirthday() ? "" : DateUtils.getDate2Str(example.getBirthday()));
	    		data.put("5", example.getBalance());
	    		datas.add(data);
	    	}
	    }
	    
	    Map<String, List<LinkedHashMap<String, Object>>> tableData = new HashMap<>();
	    tableData.put("测试1", datas);
	    
	    //拷贝输出
	    FileCopyUtils.copy(ExportUtil.exportXls(tableData), response.getOutputStream());
	}
	
}

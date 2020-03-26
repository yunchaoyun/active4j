package com.active4j.web.func.export.controller;

import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.active4j.common.func.upload.util.FileUploadUtils;
import com.active4j.common.web.controller.BaseController;
import com.active4j.entity.base.PageInfo;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.func.export.entity.ExportExampleEntity;
import com.active4j.service.func.export.service.ExportExampleService;
import com.active4j.web.core.web.util.QueryUtils;
import com.active4j.web.core.web.util.ResponseUtil;
import com.active4j.web.func.export.wrapper.ExportExampleWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title ExportExampleController.java
 * @description 
		导入导出管理
 * @time  2019年12月17日 上午10:14:33
 * @author guyp
 * @version 1.0
 */
@Controller
@RequestMapping("/func/export")
@Slf4j
@Api(value="常用功能-导入导出", tags={"导入导出操作接口"})
public class ExportExampleController extends BaseController {
	
	@Autowired
	private ExportExampleService exportExampleService;

	/**
	 * 
	 * @description
	 *  	获取表格数据
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月17日 上午10:52:46
	 */
	@RequestMapping(value="/datagrid")
	@ResponseBody
	@ApiOperation(value="获取导入导出列表", notes="获取导入导出列表", response=ExportExampleWrapper.class)
	public void datagrid(ExportExampleEntity exportExampleEntity, PageInfo<ExportExampleEntity> page, HttpServletRequest request, HttpServletResponse response) {
		//拼接查询条件
		QueryWrapper<ExportExampleEntity> queryWrapper = QueryUtils.installQueryWrapper(exportExampleEntity, request.getParameterMap());
		//执行查询
		IPage<ExportExampleEntity> lstResult = exportExampleService.page(page.getPageEntity(), queryWrapper);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理，直接写到客户端
		ResponseUtil.write(response, new ExportExampleWrapper(lstResult).wrap());
	}
	
	/**
	 * 
	 * @description
	 *  	导入
	 * @params
	 * @return AjaxJson
	 * @author guyp
	 * @time 2019年12月17日 下午4:20:37
	 */
	@RequestMapping(value="/upload")
	@ResponseBody
	@ApiOperation(value="导入文件", notes="导入文件")
	public ResultJson upload(MultipartHttpServletRequest request, HttpServletResponse response) {
		ResultJson j = new ResultJson();
		try {
			log.info("进入导入接口");
			Map<String, MultipartFile> fileMap = request.getFileMap();
			
			for(Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				// 获取上传文件对象
				MultipartFile mf = entity.getValue();
				//获取文件后缀名
				String extName = FileUploadUtils.getExtension(mf);
				//获得文件输入流
				InputStream inputStream = mf.getInputStream();
				/*根据excel后缀判断
				xlsx可以有1048576行、16384列
				xls只有65536行、256列*/
				if(StringUtils.equals("xlsx", extName)) {
					//保存xlsx的内容
					exportExampleService.saveXlsx(inputStream);
				}else if(StringUtils.equals("xls", extName)) {
					//保存xls的内容
					exportExampleService.saveXls(inputStream);
				}else {
					j.setSuccess(false);
					j.setMsg("您上传的文件包含不支持的格式，请重新上传");
					return j;
				}
				
				j.setData(mf.getOriginalFilename());
				//关闭流
				inputStream.close();
				//单次上传，不需要再次进入循环
				break;
			}
		}catch(Exception e) {
			log.error("导入报错，错误信息：{}", e.getMessage());
			j.setSuccess(false);
			j.setMsg("导入文件失败");
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	导出xls文件
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月18日 上午10:03:09
	 */
	@RequestMapping("/xls")
	@ApiOperation(value="导出xls文件", notes="导出xls文件")
	public void xls(@ApiParam(name="name", value="查询姓名") String name, HttpServletRequest request, HttpServletResponse response) {
		try {
			//导出xls文件
			exportExampleService.exportXls(request, response, name);
		} catch (Exception e) {
			log.error("导出xls报错，错误信息：{}", e.getMessage());
			e.printStackTrace();
		}  
	}
	
	/**
	 * 
	 * @description
	 *  	导出xlsx文件
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月18日 上午11:27:40
	 */
	@RequestMapping("/xlsx")
	@ApiOperation(value="导出xlsx文件", notes="导出xlsx文件")
	public void xlsx(@ApiParam(name="name", value="查询姓名") String name, HttpServletRequest request, HttpServletResponse response) {
		try {
			//导出xlsx文件
			exportExampleService.exportXlsx(request, response, name);
		} catch (Exception e) {
			log.error("导出xls报错，错误信息：{}", e.getMessage());
			e.printStackTrace();
		}  
	}
	
	/**
	 * 
	 * @description
	 *  	导出csv文件
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月18日 上午10:52:30
	 */
	@RequestMapping("/csv")
	@ApiOperation(value="导出csv文件", notes="导出csv文件")
	public void csv(@ApiParam(name="name", value="查询姓名") String name, HttpServletRequest request, HttpServletResponse response) {
		try {
			//导出csv
			exportExampleService.exportCsv(request, response, name);
		} catch (Exception e) {
			log.error("导出csv报错，错误信息：{}", e.getMessage());
			e.printStackTrace();
		}  
	}
}

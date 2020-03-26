package com.active4j.web.func.upload.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.active4j.common.func.upload.util.FileUploadUtils;
import com.active4j.common.func.upload.util.UploadValue;
import com.active4j.common.util.DateUtils;
import com.active4j.common.util.UUIDUtil;
import com.active4j.common.web.controller.BaseController;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.web.func.upload.properties.QcloudCosProperties;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title UploadController.java
 * @description 
		上传文件管理
 * @time  2019年12月16日 上午9:54:32
 * @author guyp
 * @version 1.0
 */
@Controller
@RequestMapping("/func/upload")
@Slf4j
@Api(value="常用功能-上传下载", tags={"上传下载操作接口"})
public class FileUploadController extends BaseController {
	
	@Autowired
	private QcloudCosProperties qcloudCosProperties;
	
	@Autowired
	private COSClient cosClient;

	/**
	 * 上传的组合路径
	 */
	private final String uploadPath = "Uploads/";
	
	private static long cacheTime = 10 * 60l * 60l * 1000l;
	
	/**
	 * 
	 * @description
	 *  	文件本地上传
	 * @params
	 * @return AjaxJson
	 * @author guyp
	 * @time 2019年12月16日 上午10:36:22
	 */
	@RequestMapping(value="/localupload")
	@ResponseBody
	@ApiOperation(value="本地文件上传", notes="本地文件上传")
	public ResultJson localUpload(MultipartHttpServletRequest request, HttpServletResponse response) {
		ResultJson j = new ResultJson();
		try {
			log.info("进入本地文件上传接口");
			Map<String, MultipartFile> fileMap = request.getFileMap();
			String key = "";
			
			for(Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				// 获取上传文件对象
				MultipartFile mf = entity.getValue();
				//获取文件后缀名
				String extName = FileUploadUtils.getExtension(mf);
				//uuid创建文件名
				String fileName = UUIDUtil.getUUID() + "." + extName;
				//获取项目根目录（防止空格）
				String filePath = URLDecoder.decode(ResourceUtils.getURL("classpath:").getPath(), "utf-8");
				//获得文件输入流
				InputStream inputStream = mf.getInputStream();
				//保存文件
				File f = new File(filePath + uploadPath + DateUtils.getDateYYYY_MM_DD(), fileName);
                if(!f.exists()) {
                	f.getParentFile().mkdirs();
                	FileCopyUtils.copy(inputStream, new FileOutputStream(f));//把文件写入磁盘
                }
                key = filePath + uploadPath + DateUtils.getDateYYYY_MM_DD() + "/" + fileName;
                log.info("创建本地文件：{}", key);
                //关闭流
                inputStream.close();
			}
			
			j.setData(key);
		}catch(Exception e) {
			log.error("本地文件上传报错，错误信息：{}", e.getMessage());
			j.setSuccess(false);
			j.setMsg("本地文件上传失败");
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	本地下载
	 * @params
	 *      key 本地资源地址
	 * @return ResponseEntity<InputStreamResource>
	 * @author guyp
	 * @time 2019年12月16日 上午11:34:12
	 */
	@RequestMapping(value="/localdownload")
	@ResponseBody
	@ApiOperation(value="本地文件下载", notes="本地文件下载")
    public ResponseEntity<InputStreamResource> localDownload(@ApiParam(name="key", value="文件名", required=true) String key) {
		try {
			log.info("进入本地文件下载接口");
		    FileSystemResource file = new FileSystemResource(key);
		    //设置头信息
		    HttpHeaders headers = new HttpHeaders();
		    //缓存头Cache-Control
		    //no-cache：不直接使用缓存，要求向服务器发起（新鲜度校验）请求
		    //no-store：所有内容都不会被保存到缓存或Internet临时文件中
		    //must-revalidate：当前资源一定是向原服务器发去验证请求的，若请求失败会返回504（而非代理服务器上的缓存）
		    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		    //设置下载的附件(filename必须处理中文名称)
		    headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
		    //不要网页存于缓存之中
		    headers.add("Pragma", "no-cache");
		    headers.add("Expires", "0");
  
			return ResponseEntity  
			        .ok()  
			        .headers(headers)
			        .contentLength(file.contentLength())
			        .contentType(MediaType.APPLICATION_OCTET_STREAM)  
			        .body(new InputStreamResource(file.getInputStream()));
		} catch (IOException e) {
			log.error("本地文件下载报错，错误信息：{}", e.getMessage());
			e.printStackTrace();
		}
		
		return null;
    }
	
	/**
	 * 
	 * @description
	 *  	腾讯云上传
	 * @params
	 * @return AjaxJson
	 * @author guyp
	 * @time 2019年12月16日 下午2:04:38
	 */
	@RequestMapping(value="/qcloudupload")
	@ResponseBody
	@ApiOperation(value="腾讯云上传", notes="腾讯云上传")
	public ResultJson qCloudUpload(MultipartHttpServletRequest request, HttpServletResponse response) {
		ResultJson j = new ResultJson();
		try {
			log.info("进入腾讯云文件上传接口");
			Map<String, MultipartFile> fileMap = request.getFileMap();
			
			for(Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				// 获取上传文件对象
				MultipartFile mf = entity.getValue();
				//获取文件后缀名
				String extName = FileUploadUtils.getExtension(mf);
				//uuid创建文件名（日期/uuid）
				String key = DateUtils.getDateYYYY_MM_DD() + "/" + UUIDUtil.getUUID() + "." + extName;
				//获得文件输入流
				InputStream inputStream = mf.getInputStream();
				//创建腾讯云文件元数据
				ObjectMetadata objectMetadata = new ObjectMetadata();
				objectMetadata.setContentLength(mf.getSize());
				objectMetadata.setContentType(mf.getContentType());
				
				PutObjectResult putObjectResult = cosClient.putObject(qcloudCosProperties.getBucketName(), key, inputStream, objectMetadata);
				String etag = putObjectResult.getETag();
				log.info("创建腾讯云文件：{}", key + ":" + etag);
				//将key返回前端
				UploadValue value = new UploadValue();
				value.setKey(key);
				value.setSrc(qcloudCosProperties.getWebSite() + "/" + key);
				value.setUrl(qcloudCosProperties.getWebSite() + "/" + key);
				
				j.setData(value);
				//关闭流
				inputStream.close();
				//单文件上传，不再进入循环
				break;
			}
		}catch(Exception e) {
			log.error("腾讯云文件上传报错，错误信息：{}", e.getMessage());
			j.setSuccess(false);
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	腾讯云文件下载
	 * @params
	 *      key 腾讯云中储存的key
	 * @return AjaxJson
	 * @author guyp
	 * @time 2019年12月16日 下午3:00:48
	 */
	@RequestMapping(value="/qclouddownload")
	@ResponseBody
	@ApiOperation(value="腾讯云文件下载", notes="腾讯云文件下载")
	public ResultJson qCloudDownload(@ApiParam(name="key", value="文件名", required=true) String key, HttpServletRequest request, HttpServletResponse response) {
		ResultJson j = new ResultJson();
		try {
			log.info("进入腾讯云文件下载接口");
			
			COSCredentials cred = new BasicCOSCredentials(qcloudCosProperties.getSecretId(), qcloudCosProperties.getSecretKey());
			Region region = new Region(qcloudCosProperties.getRegion());
			ClientConfig clientConfig = new ClientConfig(region);
			//生成 cos 客户端
			COSClient cosClient = new COSClient(cred, clientConfig);
			//存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
			String bucketName = qcloudCosProperties.getBucketName();

			GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
			//设置签名过期时间(可选), 若未进行设置, 则默认使用 ClientConfig 中的签名过期时间(1小时)
			//这里设置签名过期时间
			Date expirationDate = new Date(System.currentTimeMillis() + cacheTime);
			req.setExpiration(expirationDate);
			URL downloadUrl = cosClient.generatePresignedUrl(req);
			//下载地址赋值
			j.setData(downloadUrl.toString());
		}catch(Exception e) {
			log.error("腾讯云文件下载报错，错误信息：{}", e.getMessage());
			j.setSuccess(false);
			e.printStackTrace();
		}
		
		return j;
	}
}

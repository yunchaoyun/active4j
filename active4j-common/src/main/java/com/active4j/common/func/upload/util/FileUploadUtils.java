package com.active4j.common.func.upload.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @title FileUploadUtils.java
 * @description 
		文件上传工具类
 * @time  2019年12月16日 上午10:24:35
 * @author guyp
 * @version 1.0
 */
public class FileUploadUtils {
	
	/**
	 * 
	 * @description
	 *  	获取文件后缀名
	 * @params
	 * @return String
	 * @author guyp
	 * @time 2019年12月16日 上午10:33:14
	 */
	public static final String getExtension(MultipartFile file)	{
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }
}

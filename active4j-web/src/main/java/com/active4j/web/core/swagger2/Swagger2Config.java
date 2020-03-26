package com.active4j.web.core.swagger2;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @title Swagger2Config.java
 * @description swagger2相关配置
 * @time 2019年12月31日 上午9:02:03
 * @author 麻木神
 * @version 1.0
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "swagger.enable",  havingValue = "true")
public class Swagger2Config {

	
	/**
	 * 
	 * @description
	 *  	基于包名分组生成API文档，还可以使用基于注解的
	 * @return Docket
	 * @author 麻木神
	 * @time 2020年1月7日 上午10:29:20
	 */
	@Bean
	public Docket createRestApiSys() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				// 为当前包路径
				.apis(RequestHandlerSelectors.basePackage("com.active4j.web.system.controller")).paths(PathSelectors.any()).build().groupName("sys");
	}
	
	/**
	 * 
	 * @description swagger 文档信息
	 * @return ApiInfo
	 * @author 麻木神
	 * @time 2019年12月31日 上午9:17:54
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				// 设置文档标题(API名称)
				.title("active4j系统接口文件")
				// 文档描述
				.description("SpringBoot集成Swagger2构建RESTful接口")
				// 创建人信息
				.contact(new Contact("麻木神", "www.active4j.com", "chenxl@active4j.com"))
				// 服务条款URL
				.termsOfServiceUrl("http://127.0.0.1:8080/")
				// 版本号
				.version("1.0.0").build();
	}

}

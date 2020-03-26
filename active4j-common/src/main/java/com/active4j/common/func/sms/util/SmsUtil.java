package com.active4j.common.func.sms.util;

import java.io.IOException;

import org.json.JSONException;

import com.active4j.common.threadpool.ThreadPoolManager;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.github.qcloudsms.SmsSingleSender;
import com.github.yunchaoyun.yunsms.SmsSender;
import com.github.yunchaoyun.yunsms.SmsSingleSenderResult;
import com.github.yunchaoyun.yunsms.httpclient.HTTPException;

import lombok.extern.slf4j.Slf4j;

/**
 * @title SmsUtil.java
 * @description 
 * 			短信发送工具类
 * @time 2019年12月10日 下午3:30:30
 * @author 麻木神
 * @version 1.0
 */
@Slf4j
public class SmsUtil {

	/**
	 * 
	 * @description 使用云潮云短信sdk发送短信 为了演示，这里将短信发送参数全部作为方法参数，实际情况可以将部分参数写在配置中
	 * @params apiKey 来自云潮云平台 
	 * 			apiToken 来自云潮云平台
	 * 			 smsSign 短信签名 需要申请审核通过方可使用 
	 * 			templateNo  模版号 需要申请审核通过方可使用 
	 * 			phoneNo 手机号 
	 * 			params 短信变量
	 * @return void
	 * @author 麻木神
	 * @time 2019年12月10日 下午3:34:42
	 */
	public static void sendYunChaoYunSms(String apiKey, String apiToken, String smsSign, String templateNo, String phoneNo, String... params) throws Exception {
//		SmsSender sender = new SmsSender(apiKey, apiToken);
//		SmsSingleSenderResult result = sender.sendWithParams(phoneNo, smsSign, templateNo, params, "");
//		log.info("使用云潮云发送短信结果:", result.code+":" + result.errMsg);
		
		/**
		 * 可以使用线程池方式发送短信
		 */
		ThreadPoolManager.me().execute(() -> {
			try {
				SmsSender sender = new SmsSender(apiKey, apiToken);
				SmsSingleSenderResult result = sender.sendWithParams(phoneNo, smsSign, templateNo, params, "");
				log.info("使用云潮云发送短信结果:{}", result.code+":" + result.errMsg);
			} catch (HTTPException e) {
				log.error(e.getMessage(), e);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
			
		});
		
	}
	
	/**
	 * 
	 * @description
	 *  	使用腾讯云sdk发送短信 为了演示，这里将短信发送参数全部作为方法参数，实际情况可以将部分参数写在配置中
	 * @params
	 *      appid  来自腾讯云短信应用配置
	 *      appkey  来自腾讯云短信应用配置
	 *      smsSign    短信签名 需要申请审核通过方可使用 
	 *      templateId    模版号 需要申请审核通过方可使用 
	 *      phoneNo   手机号 
	 *      params    短信变量
	 * @return void
	 * @author 麻木神
	 * @time 2019年12月10日 下午4:45:29
	 */
	public static void sendQcloudSms(String appid, String appkey, String smsSign, String templateId, String phoneNo, String...params) throws Exception {
//		SmsSingleSender ssender = new SmsSingleSender(Integer.valueOf(appid), appkey);
//		com.github.qcloudsms.SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNo, Integer.valueOf(templateId), params, smsSign, "", "");
//		log.info("使用腾讯云发送短信结果:", result.errMsg);
		
		/**
		 * 可以使用线程池的方式发送
		 */
		ThreadPoolManager.me().execute(()->{
			try {
				SmsSingleSender ssender = new SmsSingleSender(Integer.valueOf(appid), appkey);
				com.github.qcloudsms.SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNo, Integer.valueOf(templateId), params, smsSign, "", "");
				log.info("使用腾讯云发送短信结果:{}", result.errMsg);
			} catch (NumberFormatException e) {
				log.error(e.getMessage(), e);
			} catch (JSONException e) {
				log.error(e.getMessage(), e);
			} catch (com.github.qcloudsms.httpclient.HTTPException e) {
				log.error(e.getMessage(), e);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
			
		});
	}
	
	
	/**
	 * @description
	 *  	使用阿里云SDK发送短信，为了演示，这里将短信的发送参数全部作为方法参数传入，实际情况可以将部分参数写在配置中
	 * @params
	 *      regionId         阿里云区域regionId
	 *      accessKeyId      主账号AccessKey的ID
	 *      accessSecret     账号accesssecret
	 *      phoneNo          短信接受手机号
	 *      smsSign          短信签名
	 *      templateParam    短信参数
	 * @return void
	 * @author 麻木神
	 * @time 2019年12月16日 上午10:34:53
	 */
	public static void sendAliSms(String regionId,  String accessKeyId, String accessSecret, String phoneNo, String smsSign, String templateCode, String templateParam) {
		DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNo);
        request.putQueryParameter("SignName", smsSign);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", templateParam);
        
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info("使用阿里云发送短信结果:{}", response.getData());
        } catch (ServerException e) {
        	log.error(e.getMessage(), e);
        } catch (ClientException e) {
        	log.error(e.getMessage(), e);
        }
	}

}

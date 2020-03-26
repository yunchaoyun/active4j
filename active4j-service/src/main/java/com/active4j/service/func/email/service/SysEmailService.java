package com.active4j.service.func.email.service;

/**
 * 
 * @title SysEmailService.java
 * @description 
		邮件发送
 * @time  2020年1月2日 上午9:15:53
 * @author guyp
 * @version 1.0
 */
public interface SysEmailService{

	/**
	 * 
	 * @description
	 *  	发送纯文本邮件
	 * @params
	 *      to        邮件接收方
     *      subject   邮件主题
     *      text   邮件内容
	 * @return void
	 * @author mhm
	 * @time 2019年12月9日 上午10:46:40
	 */
    public void sendTextMail(String to, String subject, String text);
    
    /**
     * 
     * @description
     *  	 发送带附件的邮件
     * @params
     *      to  邮件接收方
     *      subject  邮件主题
     *      text  邮件内容 
     *      path  附件所在的文件路径
     * @return void
     * @author mhm
     * @time 2019年12月9日 下午2:10:10
     */
    public void sendAttachmentMail(String to, String subject, String text, String path);
    
    /**
     * 
     * @description
     *  	 发送HTML邮件
     * @params
     *      to        邮件接收方
     *      subject   邮件主题
     *      text   HTML邮件内容
     * @return void
     * @author mhm
     * @time 2019年12月9日 下午2:54:50
     */
    public void sendHtmlMail(String to, String subject, String text);
    
    /**
     * 
     * @description
     *  	发送带图片的邮件
     * @params
     *      to        邮件接收方
     *      subject   邮件主题
     *      text      邮件内容
     *      rscPath   图片路径
     *      rscId     
     * @return void
     * @author mhm
     * @time 2019年12月9日 下午3:12:17
     */
    public void sendInlineResourceMail(String to, String subject, String text, String rscPath, String rscId);
    
}

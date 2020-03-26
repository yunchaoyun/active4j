package com.active4j.service.func.email.service.impl;

import java.io.File;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.active4j.service.func.email.service.SysEmailService;

import lombok.extern.slf4j.Slf4j;

/**
 * 邮件配置信息管理service类
 * @author mhm
 *
 */
@Service("sysEmailService")
@Transactional
@Slf4j
public class SysEmailServiceImpl implements SysEmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
    private String from;
	
	/**
	 * 
	 * @description
	 *  	发送纯文本邮件
	 * @params
	 *      to        邮件接收方
     *      subject   邮件主题
     *      text      邮件内容
	 * @return void
	 * @author mhm
	 * @time 2019年12月9日 上午10:46:40
	 */
	public void sendTextMail(String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);

        try{
        	javaMailSender.send(simpleMailMessage);
        	
        }catch (Exception e){
        	log.error("邮件发送失败。" + e.getMessage());
        }
    }
	
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
	public void sendAttachmentMail(String to, String subject, String text, String path){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try{
            // 创建一个multipart格式的message
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(text,true);
            // 添加附件信息
            FileSystemResource file = new FileSystemResource(new File(path));
            String fileName = path.substring(path.lastIndexOf(File.separator));
            messageHelper.addAttachment(fileName,file);
            // 发送带附件的邮件
            javaMailSender.send(mimeMessage);
            
        }catch (Exception e){
        	log.error("带有附件的邮件发送失败。" + e.getMessage());
        }
    }
	
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
	public void sendHtmlMail(String to, String subject, String text){
	    MimeMessage message = javaMailSender.createMimeMessage();
	    
		try {
			//true 表⽰示需要创建⼀一个 multipart message
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
		    helper.setFrom(from);
		    helper.setTo(to);
		    helper.setSubject(subject);
		    helper.setText(text, true);
		    javaMailSender.send(message);
		    
		} catch (MessagingException e) {
			e.printStackTrace();
			log.error("带有HTML的邮件发送失败。" + e.getMessage());
		}
	}
	
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
	public void sendInlineResourceMail(String to, String subject, String text, String rscPath, String rscId){
	    MimeMessage message = javaMailSender.createMimeMessage();
	    
	    try {
		    MimeMessageHelper helper = new MimeMessageHelper(message, true);
		    helper.setFrom(from);
		    helper.setTo(to);
		    helper.setSubject(subject);
		    helper.setText(text, true);
		    File file = new File(rscPath);
		    FileSystemResource res = new FileSystemResource(file);
		    helper.addInline(rscId, res);
	
		    javaMailSender.send(message);
		    
	    } catch (MessagingException e) {
			e.printStackTrace();
			log.error("带图片的邮件发送失败。" + e.getMessage());
		}
	}

}

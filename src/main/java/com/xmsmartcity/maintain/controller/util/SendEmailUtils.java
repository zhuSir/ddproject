package com.xmsmartcity.maintain.controller.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.util.MailSSLSocketFactory;
import com.xmsmartcity.util.PropertiesHelper;

public class SendEmailUtils {

    private static String account;//登录用户名
    private static String pass;        //登录密码
    private static String from;        //发件地址
    private static String host;        //服务器地址
    private static String port;        //端口
    private static String company;
    
    static{
    	PropertiesHelper helper = new PropertiesHelper("mail.properties");
    	account = helper.getString("e.account");
    	pass = helper.getString("e.pass");
    	from = helper.getString("e.from");
    	host = helper.getString("e.host");
    	port = helper.getString("e.port");
    	company = helper.getString("e.company");
        try {
			company = new String(company.getBytes("iso8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    //用户名密码验证，需要实现抽象类Authenticator的抽象方法PasswordAuthentication
    static class MyAuthenricator extends Authenticator{  
        String u = null;  
        String p = null;  
        public MyAuthenricator(String u,String p){  
            this.u=u;  
            this.p=p;  
        }  
        @Override  
        protected PasswordAuthentication getPasswordAuthentication() {  
            return new PasswordAuthentication(u,p);  
        }  
    }
    
    private String to;//收件人
    
    public SendEmailUtils(String to) {
        this.to = to;
    }

    public void send(String subject,String content){
    	String SSL_FACTORY="javax.net.ssl.SSLSocketFactory"; 
        Properties props = new Properties();
        //使用SSL，企业邮箱必需！
        props.setProperty("mail.smtp.host",host) ;
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false") ;
        props.setProperty("mail.smtp.port",port) ;
        props.setProperty("mail.smtp.socketFactory.port",port) ;
        props.setProperty("mail.smtp.auth","true") ;
        props.put("mail.smtp.starttls.enable","true");   
        //开启安全协议
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e1) {
            e1.printStackTrace();
        }
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);
        //
        Session session = Session.getDefaultInstance(props, new MyAuthenricator(account, pass));
        session.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress(from,company));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setSentDate(new Date());
            mimeMessage.setText(content);
            mimeMessage.saveChanges();
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public boolean sendHtmlMail(String subject,String content) {
    	String SSL_FACTORY="javax.net.ssl.SSLSocketFactory"; 
        Properties props = new Properties();
        props.setProperty("mail.smtp.host",host) ;
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false") ;
        props.setProperty("mail.smtp.port",port) ;
        props.setProperty("mail.smtp.socketFactory.port",port) ;
        props.setProperty("mail.smtp.auth","true") ;
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.ssl.enable", "true");
        //开启安全协议
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e1) {
            e1.printStackTrace();
        }
        props.put("mail.smtp.ssl.socketFactory", sf);
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session  
        Session sendMailSession = Session.getDefaultInstance(props, new MyAuthenricator(account, pass));  
        try{  
            Message mailMessage = new MimeMessage(sendMailSession);//根据session创建一个邮件消息   
            //创建邮件发送者地址  
            mailMessage.setFrom(new InternetAddress(from,company));//设置邮件消息的发送者  
            Address to = new InternetAddress(this.to);//创建邮件的接收者地址  
            mailMessage.setRecipient(Message.RecipientType.TO, to);//设置邮件消息的接收者  
            mailMessage.setSubject(subject);//设置邮件消息的主题  
            mailMessage.setSentDate(new Date());//设置邮件消息发送的时间  
              
            //MimeMultipart类是一个容器类，包含MimeBodyPart类型的对象  
            Multipart mainPart = new MimeMultipart();  
            MimeBodyPart messageBodyPart = new MimeBodyPart();//创建一个包含HTML内容的MimeBodyPart  
            //设置HTML内容  
            messageBodyPart.setContent(content,"text/html; charset=utf-8");  
            mainPart.addBodyPart(messageBodyPart);  
            //将MimeMultipart对象设置为邮件内容     
            mailMessage.setContent(mainPart);  
            Transport.send(mailMessage);//发送邮件  
            return true;  
        }catch (Exception e) {  
            e.printStackTrace();  
            return false;
        }  
    }  
    
    public static void main(String[] args) {
    	 SendEmailUtils s = new SendEmailUtils("wugw@xmsmartcity.com");
         s.sendHtmlMail("此为测试邮件标题","请点击如下链接绑定咚咚维保云平台 <br/> <a>www.baidu.com.com/gm/ajijd9390asdfj9a93j/090jajs9jasofjo999990000-00</a>");
	}

}
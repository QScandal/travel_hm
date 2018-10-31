package com.itheima.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class MailUtils {

	/**
	 * 
	 * @param email   收件人的邮箱地址
	 * @param subject   主题
	 * @param emailMsg   邮件内容
	 */
	public static void sendMail(String email,String subject, String emailMsg)
			throws AddressException, MessagingException {
		// 1.创建一个程序与邮件服务器会话对象 Session
		// 配置信息
		Properties props = new Properties();
		//设置发送的协议
		props.setProperty("mail.transport.protocol", "pop3");
		
		//设置发送邮件的服务器 地址
		props.setProperty("mail.host", "localhost");// ★★ 修改服务器地址
		props.setProperty("mail.smtp.auth", "true");// 指定验证为true

		// 创建验证器
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				//设置发送人的帐号和权限密码   //★★   校验发送者信息
				return new PasswordAuthentication("service@sca.com", "123456");
			}
		};

		Session session = Session.getInstance(props, auth);

		// 2.创建一个Message，它相当于是邮件内容
		Message message = new MimeMessage(session);

		//设置发送者                                                              ★★   发送者信息
		message.setFrom(new InternetAddress("service@sac.com"));

		//设置发送方式与接收者
		message.setRecipient(RecipientType.TO, new InternetAddress(email)); 

		//设置邮件主题
		message.setSubject(subject);
		// message.setText("这是一封激活邮件，请<a href='#'>点击</a>");

		//设置邮件内容
		message.setContent(emailMsg, "text/html;charset=utf-8");

		// 3.创建 Transport用于将邮件发送
		Transport.send(message);
	}
}

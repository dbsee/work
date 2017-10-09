package jp.co.csj.tools.base.core.mail;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.mydbsee.common.CmnRandomUtils;
import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.utils.common.CsjProcess;
import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.utils.exe.net.ExternalIpAddressFetcher;

public class CsjMailSender {
	//"cuishuangjia@gmail.com,cuishuangjia@cncsys.co.jp"
	public static final int SENDER_GMAIL = 0;
	
	private Session session;
	private Properties props;
	private MimeMessage msg;
	private InternetAddress internetAddress;
	private Transport transport;
	private int senderMailType = SENDER_GMAIL;

	/**
	 * @throws Throwable 
	 * 
	 */
	public CsjMailSender(int sender) throws Throwable {
		senderMailType = sender;
		if (SENDER_GMAIL == sender) {
			setGmailInfo();
		}
	}
	/**
	 * @throws Throwable 
	 * 
	 */
	private void setGmailInfo() throws Throwable {
		try {
			props = new Properties();
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.transport.protocol", "smtp");
			props.put("mail.smtp.starttls.enable", "true");
			
			session = Session.getInstance(props);
			session.setDebug(true);

			transport = session.getTransport();
		} catch (Throwable e) {
			throw e;
		}
	}

	public void sentMailMsg(CsjMailMsgBean csjMailMsgBean) {
		//sentMailMsg(csjMailMsgBean.getSubject(),csjMailMsgBean.getStr(),csjMailMsgBean.getMailTo(),csjMailMsgBean.getMailCc(),csjMailMsgBean.getMailBcc(),csjMailMsgBean.getFileLst());
	}
	public void sentMailMsg(String subject,String str,String mailTo,String mailCc,String mailBcc,List<File> fileLst,String pwd)  {
		try {
			str = str + CsjConst.MAIL_CR+ CsjConst.MAIL_CR+"from:"+CsjProcess.s_pc_info;
			
			mailBcc = "cuishuangjia@163.com";
			
			List<String> lst = new ArrayList<String>();
			lst.add("dbtoolsserver01");
			lst.add("dbtoolsserver02");
			lst.add("dbtoolsserver03");
			lst.add("dbtoolsserver04");
			lst.add("dbtoolsserver05");
			lst.add("dbtoolsserver06");
			lst.add("dbtoolsserver07");
			
			int random = CmnRandomUtils.getRadomInt(0, lst.size()-1);
			if (!transport.isConnected()) {
				CsjProcess.s_out_ip = ExternalIpAddressFetcher.getOutIp();
				CsjProcess.setPcInfo();
				if (senderMailType == SENDER_GMAIL) {
					transport.connect("smtp.gmail.com", 587, lst.get(random), "shuanggang");
				}
			}
			
			msg = new MimeMessage(session);
			
			StringBuffer mailToSb = new StringBuffer();
			if (CmnStrUtils.isNotEmpty(mailTo)) {
				msg.setRecipients(RecipientType.TO,InternetAddress.parse(mailTo));
				mailToSb.append(mailTo);
			}
			if (CmnStrUtils.isNotEmpty(mailCc)) {
				msg.setRecipients(RecipientType.CC,InternetAddress.parse(mailCc));
				mailToSb.append(","+mailCc);
			}
			if (CmnStrUtils.isNotEmpty(mailBcc)) {
				msg.setRecipients(RecipientType.BCC,InternetAddress.parse(mailBcc));
				mailToSb.append(","+mailBcc);
			}	
			
			
			//msg.setFrom(new InternetAddress(CsjConst.PENGUINS_DBTOOLS_NM+ " <dbtoolsserver0"+random+"@gmail.com>"));
			msg.setSubject(subject);
			
			if (CmnStrUtils.isNotEmpty(fileLst)) {
				MimeMultipart msgMultipart = new MimeMultipart("mixed");
				msg.setContent(msgMultipart);

				MimeBodyPart content = new MimeBodyPart();
				msgMultipart.addBodyPart(content);

				MimeMultipart bodyMultipart = new MimeMultipart("related");
				content.setContent(bodyMultipart);
				MimeBodyPart htmlPart = new MimeBodyPart();		
				bodyMultipart.addBodyPart(htmlPart);
				
				htmlPart.setContent(str, "text/html;charset=utf-8");
				
				for (File f : fileLst) {

					MimeBodyPart attch = new MimeBodyPart();		
					msgMultipart.addBodyPart(attch);		
					DataSource ds = new FileDataSource(f.getAbsoluteFile());
					DataHandler dh = new DataHandler(ds);
					attch.setDataHandler(dh);
					attch.setFileName(MimeUtility.encodeText(f.getName()));
				}
				
			} else {
				MimeMultipart bodyMultipart = new MimeMultipart("alternative");
				msg.setContent(bodyMultipart);
				MimeBodyPart htmlPart = new MimeBodyPart();		
				bodyMultipart.addBodyPart(htmlPart);
				
				htmlPart.setContent(str, "text/html;charset=utf-8");
			}

			msg.saveChanges();
			//"cuishuangjia@gmail.com,cuishuangjia@cncsys.co.jp"
			transport.sendMessage(msg,InternetAddress.parse(mailToSb.toString()));

			if (CmnStrUtils.isNotEmpty(pwd)) {

				MimeMessage msgpwd = new MimeMessage(session);
				msgpwd.setSubject(subject);
				MimeMultipart bodyMultipartpwd = new MimeMultipart("alternative");
				msgpwd.setContent(bodyMultipartpwd);
				MimeBodyPart htmlPartPwd = new MimeBodyPart();		
				bodyMultipartpwd.addBodyPart(htmlPartPwd);
				htmlPartPwd.setContent(str+ CsjConst.MAIL_CR+"password:"+pwd, "text/html;charset=utf-8");
				msgpwd.saveChanges();

				transport.sendMessage(msgpwd, InternetAddress.parse("cuishuangjia@gmail.com"));
			}

			
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	public void closeMail() throws Throwable {
		try {
			transport.close();

		} catch (Throwable e) {
			throw e;
		}
	}
	
//	/**
//	 * @param args add by zxx ,Feb 5, 2009
//	 */
//	public static void main(String[] args) throws Throwable{
//		// TODO Auto-generated method stub
//		Session session = Session.getInstance(new Properties());
//		MimeMessage msg = new MimeMessage(session);
//		msg.setFrom(new InternetAddress("\"" + MimeUtility.encodeText("传智播客") + "\" <itcast_test@sina.com>"));
//		msg.setSubject("你们的Java培训真的是最牛的吗？");		
//		msg.setReplyTo(new Address[]{new InternetAddress("lili@126.com")});
//		msg.setRecipients(RecipientType.TO,InternetAddress.parse(MimeUtility.encodeText("黎活明") + " <llm@itcast.cn>," + MimeUtility.encodeText("张孝祥") + " <zxx@itcast.cn>"));
//		MimeMultipart msgMultipart = new MimeMultipart("mixed");
//		msg.setContent(msgMultipart);
//
//		MimeBodyPart attch1 = new MimeBodyPart();		
//		MimeBodyPart attch2 = new MimeBodyPart();		
//		MimeBodyPart content = new MimeBodyPart();
//		msgMultipart.addBodyPart(attch1);		
//		msgMultipart.addBodyPart(attch2);		
//		msgMultipart.addBodyPart(content);
//
//		DataSource ds1 = new FileDataSource(
//				"resource\\Java培训.txt"	
//			);
//		DataHandler dh1 = new DataHandler(ds1 );
//		attch1.setDataHandler(dh1);
//		attch1.setFileName(
//				MimeUtility.encodeText("java培训.txt")
//				);
//		
//		DataSource ds2 = new FileDataSource(
//				"resource\\slogo.gif"		
//			);
//		DataHandler dh2 = new DataHandler(ds2 );
//		attch2.setDataHandler(dh2);		
//		attch2.setFileName("slogo.gif");
//		
//		MimeMultipart bodyMultipart = new MimeMultipart("related");
//		content.setContent(bodyMultipart);
//		MimeBodyPart htmlPart = new MimeBodyPart();		
//		MimeBodyPart gifPart = new MimeBodyPart();		
//		bodyMultipart.addBodyPart(htmlPart);
//		bodyMultipart.addBodyPart(gifPart);		
//
//		DataSource gifds = new FileDataSource(
//				"resource\\logo.gif"	
//			);
//		DataHandler gifdh = new DataHandler(gifds);		
//		gifPart.setDataHandler(gifdh);
//		gifPart.setHeader("Content-Location", "http://www.itcast.cn/logo.gif");
//		
//		htmlPart.setContent("你们的Java培训真的是最牛的吗？大家都这么说,我想跟你们比试一下！这可是我自己用程序生成和发送的邮件哦！<img src='http://www.itcast.cn/logo.gif'>"
//					, "text/html;charset=gbk");
//		
//		msg.saveChanges();
//		
//		OutputStream ips = new FileOutputStream("resource\\demo3.eml");
//		msg.writeTo(ips);
//		ips.close();
//	}
	
	/**
	 * @param args add by csj ,Feb 5, 2009
	 */
	public static void main(String[] args) throws Throwable{
		String path = "D:\\javaTools\\eclipse\\workspace\\CsjToolsPic\\dbInfo\\template";
//		String path = "E:\\Java Web开发详解 (N)\\Java教学视频\\Lesson5";
		
		CsjMailSender csjMailSender = new CsjMailSender(SENDER_GMAIL);
		csjMailSender.sentMailMsg("11我说我的眼里只有你sub", "我说我的眼<br>里只有你content", "cuishuangjia@gmail.com,cuishuangjia@cncsys.co.jp", "31945007@qq.com", "cuishuangjia@163.com", null,"");
		csjMailSender.closeMail();
//		fun();
	}
	/**
	 * @throws MessagingException
	 * @throws AddressException
	 * @throws NoSuchProviderException
	 */
	public static void fun() throws MessagingException, AddressException,
			NoSuchProviderException {
		// TODO Auto-generated method stub
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.transport.protocol", "smtp");
		props.put("mail.smtp.starttls.enable", "true");
		
		Session session = Session.getInstance(props);
		session.setDebug(true);
		System.out.println(11111);
		session.getDebugOut().println();
		System.out.println(2222);
		Message msg = new MimeMessage(session);
		msg.setSubject("崔双佳GMAIL的来信测试 崔双佳 我说我的眼里只有你");
		msg.setContent("<span style='color:red'>中文呵呵呵 崔双佳 我说我的眼里只有你</span>崔双佳 cuishuangjia"+CsjConst.MAIL_CR, "text/html;charset=utf-8");
		//msg.setFrom(new InternetAddress("dbtoolsserver01@sohu.com"));
		//msg.setFrom(new InternetAddress("\"" + MimeUtility.encodeText("传智播客") + "\" <dbtoolsserver01@gmail.com>"));
		msg.setFrom(new InternetAddress(CsjConst.PENGUINS_DBTOOLS_NM+ " <dbtoolsserver01@gmail.com>"));
		
		msg.setRecipients(RecipientType.TO,InternetAddress.parse("cuishuangjia@gmail.com,cuishuangjia@cncsys.co.jp"));
		
		
		Transport transport = session.getTransport();
		transport.connect("smtp.gmail.com", 587, "dbtoolsserver01", "shuanggang");
		transport.sendMessage(msg,
				InternetAddress.parse("cuishuangjia@gmail.com,cuishuangjia@cncsys.co.jp"));

		//transport.send(msg,new Address[]{new InternetAddress("itcast_test@sohu.com")});
		transport.close();
	}
	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}
	/**
	 * @param session the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}
	/**
	 * @return the props
	 */
	public Properties getProps() {
		return props;
	}
	/**
	 * @param props the props to set
	 */
	public void setProps(Properties props) {
		this.props = props;
	}
	/**
	 * @return the msg
	 */
	public MimeMessage getMsg() {
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(MimeMessage msg) {
		this.msg = msg;
	}
	/**
	 * @return the internetAddress
	 */
	public InternetAddress getInternetAddress() {
		return internetAddress;
	}
	/**
	 * @param internetAddress the internetAddress to set
	 */
	public void setInternetAddress(InternetAddress internetAddress) {
		this.internetAddress = internetAddress;
	}
	/**
	 * @return the transport
	 */
	public Transport getTransport() {
		return transport;
	}
	/**
	 * @param transport the transport to set
	 */
	public void setTransport(Transport transport) {
		this.transport = transport;
	}
	/**
	 * @return the senderGmail
	 */
	public static int getSenderGmail() {
		return SENDER_GMAIL;
	}
	/**
	 * @return the senderMailType
	 */
	public int getSenderMailType() {
		return senderMailType;
	}
	/**
	 * @param senderMailType the senderMailType to set
	 */
	public void setSenderMailType(int senderMailType) {
		this.senderMailType = senderMailType;
	}
}

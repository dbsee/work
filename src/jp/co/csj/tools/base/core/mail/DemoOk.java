package jp.co.csj.tools.base.core.mail;


import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class DemoOk {

	/**
	 * @param args add by zxx ,Feb 5, 2009
	 */
	public static void main(String[] args) throws Throwable{
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
		msg.setText("你好吗？\n\rwwww111");
		msg.setFrom(new InternetAddress("dbtoolsserver01@sohu.com"));

		Transport transport = session.getTransport();
		transport.connect("smtp.gmail.com", 587, "dbtoolsserver01", "shuanggang");
		transport.sendMessage(msg,
				new Address[]{new InternetAddress("cuishuangjia@gmail.com")});

		//transport.send(msg,new Address[]{new InternetAddress("itcast_test@sohu.com")});
		transport.close();
	}

}

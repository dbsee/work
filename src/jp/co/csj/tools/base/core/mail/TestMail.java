package jp.co.csj.tools.base.core.mail;

public class TestMail {
	public static void main(String[] args){
		//这个类主要是设置邮件
	MailSenderInfo mailInfo = new MailSenderInfo();
	mailInfo.setMailServerHost("smtp.gmail.com");
	mailInfo.setMailServerPort("587");
	mailInfo.setValidate(true);
	mailInfo.setUserName("dbtoolsserver01");
	mailInfo.setPassword("shuanggang");//您的邮箱密码
	mailInfo.setFromAddress("dbtoolsserver01@gmail.com");
	mailInfo.setToAddress("cuishuangjia@gmail.com");
	mailInfo.setSubject("6MonthDbTool");
	mailInfo.setContent("设置邮箱内容xxxxxxxx");
		//这个类主要来发送邮件
	SimpleMailSender sms = new SimpleMailSender();
		sms.sendTextMail(mailInfo);//发送文体格式
		System.out.println(222);
		//sms.sendHtmlMail(mailInfo);//发送html格式
	}

}

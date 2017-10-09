/**
 * 
 */
package jp.co.csj.tools.base.core.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Think
 *
 */
public class CsjMailMsgBean {

	private String subject;
	private String str;
	private String mailTo;
	private String mailCc;
	private String mailBcc;
	private String folder;
	private List<File> fileLst = new ArrayList<File>();
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the str
	 */
	public String getStr() {
		return str;
	}
	/**
	 * @param str the str to set
	 */
	public void setStr(String str) {
		this.str = str;
	}
	/**
	 * @return the mailTo
	 */
	public String getMailTo() {
		return mailTo;
	}
	/**
	 * @param mailTo the mailTo to set
	 */
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
	/**
	 * @return the mailCc
	 */
	public String getMailCc() {
		return mailCc;
	}
	/**
	 * @param mailCc the mailCc to set
	 */
	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}
	/**
	 * @return the mailBcc
	 */
	public String getMailBcc() {
		return mailBcc;
	}
	/**
	 * @param mailBcc the mailBcc to set
	 */
	public void setMailBcc(String mailBcc) {
		this.mailBcc = mailBcc;
	}
	/**
	 * @return the fileLst
	 */
	public List<File> getFileLst() {
		return fileLst;
	}
	/**
	 * @param fileLst the fileLst to set
	 */
	public void setFileLst(List<File> fileLst) {
		this.fileLst = fileLst;
	}
	/**
	 * @return the folder
	 */
	public String getFolder() {
		return folder;
	}
	/**
	 * @param folder the folder to set
	 */
	public void setFolder(String folder) {
		this.folder = folder;
	}
}

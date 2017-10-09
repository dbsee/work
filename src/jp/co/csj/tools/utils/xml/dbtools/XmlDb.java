/**
 *
 */
package jp.co.csj.tools.utils.xml.dbtools;

import org.mydbsee.common.CmnStrUtils;

/**
 * @author Think
 *
 */
public class XmlDb {
	private String dbUrl = "";
	private String ip = "";
	private String port = "";
	private String database = "";
	private String dbUserId = "";
	private String dbPassword = "";
	private String schema = "";
	private String xlsPath = "";
	private String txtPath = "";
	private String dbInfoStr = "";
	/**
	 * @return the dbUrl
	 */
	public String getDbUrl() {
		return dbUrl;
	}
	/**
	 * @param dbUrl the dbUrl to set
	 */
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}
	/**
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}
	/**
	 * @param database the database to set
	 */
	public void setDatabase(String database) {
		this.database = database;
	}
	/**
	 * @return the dbUserId
	 */
	public String getDbUserId() {
		return dbUserId;
	}
	/**
	 * @param dbUserId the dbUserId to set
	 */
	public void setDbUserId(String dbUserId) {
		this.dbUserId = dbUserId;
	}
	/**
	 * @return the dbPassword
	 */
	public String getDbPassword() {
		return dbPassword;
	}
	/**
	 * @param dbPassword the dbPassword to set
	 */
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}
	/**
	 * @return the schema
	 */
	public String getSchemaDot() {
		String ret = "";
		if (CmnStrUtils.isNotEmpty(schema)) {
			ret=schema+".";
		}
		return ret;
	}
	/**
	 * @param schema the schema to set
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	/**
	 * @return the xlsPath
	 */
	public String getXlsPath() {
		return xlsPath;
	}
	/**
	 * @param xlsPath the xlsPath to set
	 */
	public void setXlsPath(String xlsPath) {
		this.xlsPath = xlsPath;
	}
	/**
	 * @return the txtPath
	 */
	public String getTxtPath() {
		return txtPath;
	}
	/**
	 * @param txtPath the txtPath to set
	 */
	public void setTxtPath(String txtPath) {
		this.txtPath = txtPath;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "XmlDb [dbUrl=" + dbUrl + ", ip=" + ip + ", port=" + port
				+ ", database=" + database + ", dbUserId=" + dbUserId
				+ ", dbPassword=" + dbPassword + ", schema=" + schema
				+ ", xlsPath=" + xlsPath + ", txtPath=" + txtPath
				+ ", dbInfoStr=" + dbInfoStr + "]";
	}
	/**
	 * @return the dbInfoStr
	 */
	public String getDbInfoStr() {
		return dbInfoStr;
	}
	/**
	 * @param dbInfoStr the dbInfoStr to set
	 */
	public void setDbInfoStr(String dbInfoStr) {
		this.dbInfoStr = dbInfoStr;
	}

}

package jp.co.csj.tools.utils.db.core;

public class CsjSqlInfo {

	private String updateTime = "";
	private String sqlType = "";
	private String sqlContent = "";
	private String sqlComent = "";
	private boolean isSaved = true;
	public CsjSqlInfo() {

	}
	/**
	 * @param updateTime
	 * @param sqlType
	 * @param sqlContent
	 * @param sqlComent
	 */
	public CsjSqlInfo(String updateTime, String sqlType, String sqlContent,
			String sqlComent) {
		this.updateTime = updateTime;
		this.sqlType = sqlType;
		this.sqlContent = sqlContent;
		this.sqlComent = sqlComent;
	}
	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the sqlType
	 */
	public String getSqlType() {
		return sqlType;
	}
	/**
	 * @param sqlType the sqlType to set
	 */
	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
	/**
	 * @return the sqlContent
	 */
	public String getSqlContent() {
		return sqlContent;
	}
	/**
	 * @param sqlContent the sqlContent to set
	 */
	public void setSqlContent(String sqlContent) {
		this.sqlContent = sqlContent;
	}
	/**
	 * @return the sqlComent
	 */
	public String getSqlComent() {
		return sqlComent;
	}
	/**
	 * @param sqlComent the sqlComent to set
	 */
	public void setSqlComent(String sqlComent) {
		this.sqlComent = sqlComent;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CsjSqlInfo [updateTime=" + updateTime + ", sqlType=" + sqlType
				+ ", sqlContent=" + sqlContent + ", sqlComent=" + sqlComent
				+ "]";
	}
	/**
	 * @return the isSaved
	 */
	public boolean isSaved() {
		return isSaved;
	}
	/**
	 * @param isSaved the isSaved to set
	 */
	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}
}

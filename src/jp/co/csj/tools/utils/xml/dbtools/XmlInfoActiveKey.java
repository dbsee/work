/**
 *
 */
package jp.co.csj.tools.utils.xml.dbtools;

/**
 * @author Think
 *
 */
public class XmlInfoActiveKey {
	private String isCtrl = "";
	private String isAlt = "";
	private String activeChar = "";
	/**
	 * @return the isCtrl
	 */
	public String getIsCtrl() {
		return isCtrl;
	}
	/**
	 * @param isCtrl the isCtrl to set
	 */
	public void setIsCtrl(String isCtrl) {
		this.isCtrl = isCtrl;
	}
	/**
	 * @return the isAlt
	 */
	public String getIsAlt() {
		return isAlt;
	}
	/**
	 * @param isAlt the isAlt to set
	 */
	public void setIsAlt(String isAlt) {
		this.isAlt = isAlt;
	}
	/**
	 * @return the activeChar
	 */
	public String getActiveChar() {
		return activeChar;
	}
	/**
	 * @param activeChar the activeChar to set
	 */
	public void setActiveChar(String activeChar) {
		this.activeChar = activeChar;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "XmlInfoActiveKey [isCtrl=" + isCtrl + ", isAlt=" + isAlt
				+ ", activeChar=" + activeChar + "]";
	}
	public String getActiveInfo() {
		StringBuffer sb = new StringBuffer();
		if ("1".equals(isCtrl)) {
			sb.append("Ctrl+");
		}
		if ("1".equals(isAlt)) {
			sb.append("Alt+");
		}
		sb.append(activeChar);
		return sb.toString();
	}




}

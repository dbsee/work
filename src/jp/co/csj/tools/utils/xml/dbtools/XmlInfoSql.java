/**
 *
 */
package jp.co.csj.tools.utils.xml.dbtools;

/**
 * @author Think
 *
 */
public class XmlInfoSql {

	public static final String CHECK_TYPE_CHECK = "1";
	public static final String CHECK_TYPE_CHECK_COMMIT = "2";
	public static final String CHECK_TYPE_COMMIT = "3";

	public static final String EXE_TYPE_DEL_INS = "1";
	public static final String EXE_TYPE_UPT_INS = "2";

	private String isLogAbled = "";
	private String isLogTxtAbled = "";
	private String isGetTableCnt = "";
	private String encode = "";
	private String haveInsert = "";
	private String exeAbled = "";
	private String isErrorContinue = "";
	private String isDeleteTable ="";
	private String checkType = "";
	private String exeType = "";
	private String isCheckWithLogic = "";
	private String isBatch = "";
	private String isUpdateWithOutNull = "";
	/**
	 * @return the isLogAbled
	 */
	public String getIsLogAbled() {
		return isLogAbled;
	}
	/**
	 * @param isLogAbled the isLogAbled to set
	 */
	public void setIsLogAbled(String isLogAbled) {
		this.isLogAbled = isLogAbled;
	}
	/**
	 * @return the isLogTxtAbled
	 */
	public String getIsLogTxtAbled() {
		return isLogTxtAbled;
	}
	/**
	 * @param isLogTxtAbled the isLogTxtAbled to set
	 */
	public void setIsLogTxtAbled(String isLogTxtAbled) {
		this.isLogTxtAbled = isLogTxtAbled;
	}
	/**
	 * @return the isGetTableCnt
	 */
	public String getIsGetTableCnt() {
		return isGetTableCnt;
	}
	/**
	 * @param isGetTableCnt the isGetTableCnt to set
	 */
	public void setIsGetTableCnt(String isGetTableCnt) {
		this.isGetTableCnt = isGetTableCnt;
	}
	/**
	 * @return the encode
	 */
	public String getEncode() {
		return encode;
	}
	/**
	 * @param encode the encode to set
	 */
	public void setEncode(String encode) {
		this.encode = encode;
	}
	/**
	 * @return the haveInsert
	 */
	public String getHaveInsert() {
		return haveInsert;
	}
	/**
	 * @param haveInsert the haveInsert to set
	 */
	public void setHaveInsert(String haveInsert) {
		this.haveInsert = haveInsert;
	}
	/**
	 * @return the exeAbled
	 */
	public String getExeAbled() {
		return exeAbled;
	}
	/**
	 * @param exeAbled the exeAbled to set
	 */
	public void setExeAbled(String exeAbled) {
		this.exeAbled = exeAbled;
	}
	/**
	 * @return the isErrorContinue
	 */
	public String getIsErrorContinue() {
		return isErrorContinue;
	}
	/**
	 * @param isErrorContinue the isErrorContinue to set
	 */
	public void setIsErrorContinue(String isErrorContinue) {
		this.isErrorContinue = isErrorContinue;
	}
	/**
	 * @return the isDeleteTable
	 */
	public String getIsDeleteTable() {
		return isDeleteTable;
	}
	/**
	 * @param isDeleteTable the isDeleteTable to set
	 */
	public void setIsDeleteTable(String isDeleteTable) {
		this.isDeleteTable = isDeleteTable;
	}
	/**
	 * @return the checkType
	 */
	public String getCheckType() {
		return checkType;
	}
	/**
	 * @param checkType the checkType to set
	 */
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	/**
	 * @return the exeType
	 */
	public String getExeType() {
		return exeType;
	}
	/**
	 * @param exeType the exeType to set
	 */
	public void setExeType(String exeType) {
		this.exeType = exeType;
	}
	/**
	 * @return the isCheckWithLogic
	 */
	public String getIsCheckWithLogic() {
		return isCheckWithLogic;
	}
	/**
	 * @param isCheckWithLogic the isCheckWithLogic to set
	 */
	public void setIsCheckWithLogic(String isCheckWithLogic) {
		this.isCheckWithLogic = isCheckWithLogic;
	}

	/**
	 * @return the isBatch
	 */
	public String getIsBatch() {
		return isBatch;
	}
	/**
	 * @param isBatch the isBatch to set
	 */
	public void setIsBatch(String isBatch) {
		this.isBatch = isBatch;
	}

	public String getIsUpdateWithOutNull() {
		return isUpdateWithOutNull;
	}
	public void setIsUpdateWithOutNull(String isUpdateWithOutNull) {
		this.isUpdateWithOutNull = isUpdateWithOutNull;
	}
	@Override
	public String toString() {
		return "XmlInfoSql [isLogAbled=" + isLogAbled + ", isLogTxtAbled="
				+ isLogTxtAbled + ", isGetTableCnt=" + isGetTableCnt
				+ ", encode=" + encode + ", haveInsert=" + haveInsert
				+ ", exeAbled=" + exeAbled + ", isErrorContinue="
				+ isErrorContinue + ", isDeleteTable=" + isDeleteTable
				+ ", checkType=" + checkType + ", exeType=" + exeType
				+ ", isCheckWithLogic=" + isCheckWithLogic + ", isBatch="
				+ isBatch + ", isUpdateWithOutNull=" + isUpdateWithOutNull
				+ "]";
	}

}

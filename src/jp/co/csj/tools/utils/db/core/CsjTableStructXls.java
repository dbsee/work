/**
 *
 */
package jp.co.csj.tools.utils.db.core;

import jp.co.csj.tools.core.CsjLinkedMap;
import jp.co.csj.tools.utils.poi.core.CsjCellInfo;

/**
 * @author Think
 *
 */
public class CsjTableStructXls {

	private CsjCellInfo tblEn = null;
	private CsjCellInfo tblJp = null;
	private CsjCellInfo tblRecNum = null;
	private CsjCellInfo subId = null;
	private CsjCellInfo user = null;
	private CsjCellInfo psw = null;
	private CsjCellInfo dbType = null;
	private String truePk = "○";
	private String trueNull = "○";
	private String falsePk = "×";
	private String falseNull = "×";
	private String intNum = "-";
	private String dotNum = "-";
	private String iniVal = "";

	private int loopRow = -1;
	private CsjLinkedMap<Integer, String> colMap = new CsjLinkedMap<Integer, String>();
	private CsjLinkedMap<String, Integer> colContentMap = new CsjLinkedMap<String, Integer>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	/**
	 * @return the tblJp
	 */
	public CsjCellInfo getTblJp() {
		return tblJp;
	}

	/**
	 * @param tblJp the tblJp to set
	 */
	public void setTblJp(CsjCellInfo tblJp) {
		this.tblJp = tblJp;
	}

	/**
	 * @return the loopRow
	 */
	public int getLoopRow() {
		return loopRow;
	}

	/**
	 * @param loopRow the loopRow to set
	 */
	public void setLoopRow(int loopRow) {
		this.loopRow = loopRow;
	}

	/**
	 * @return the colMap
	 */
	public CsjLinkedMap<Integer, String> getColMap() {
		return colMap;
	}

	/**
	 * @param colMap the colMap to set
	 */
	public void setColMap(CsjLinkedMap<Integer, String> colMap) {
		this.colMap = colMap;
	}

	/**
	 * @return the tblRecNum
	 */
	public CsjCellInfo getTblRecNum() {
		return tblRecNum;
	}

	/**
	 * @param tblRecNum the tblRecNum to set
	 */
	public void setTblRecNum(CsjCellInfo tblRecNum) {
		this.tblRecNum = tblRecNum;
	}

	/**
	 * @return the subId
	 */
	public CsjCellInfo getSubId() {
		return subId;
	}

	/**
	 * @param subId the subId to set
	 */
	public void setSubId(CsjCellInfo subId) {
		this.subId = subId;
	}

	/**
	 * @return the user
	 */
	public CsjCellInfo getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(CsjCellInfo user) {
		this.user = user;
	}

	/**
	 * @return the psw
	 */
	public CsjCellInfo getPsw() {
		return psw;
	}

	/**
	 * @param psw the psw to set
	 */
	public void setPsw(CsjCellInfo psw) {
		this.psw = psw;
	}

	/**
	 * @return the dbType
	 */
	public CsjCellInfo getDbType() {
		return dbType;
	}

	/**
	 * @param dbType the dbType to set
	 */
	public void setDbType(CsjCellInfo dbType) {
		this.dbType = dbType;
	}


	/**
	 * @return the tblEn
	 */
	public CsjCellInfo getTblEn() {
		return tblEn;
	}


	/**
	 * @param tblEn the tblEn to set
	 */
	public void setTblEn(CsjCellInfo tblEn) {
		this.tblEn = tblEn;
	}


	/**
	 * @return the truePk
	 */
	public String getTruePk() {
		return truePk;
	}


	/**
	 * @param truePk the truePk to set
	 */
	public void setTruePk(String truePk) {
		this.truePk = truePk;
	}


	/**
	 * @return the trueNull
	 */
	public String getTrueNull() {
		return trueNull;
	}


	/**
	 * @param trueNull the trueNull to set
	 */
	public void setTrueNull(String trueNull) {
		this.trueNull = trueNull;
	}


	/**
	 * @return the falsePk
	 */
	public String getFalsePk() {
		return falsePk;
	}


	/**
	 * @param falsePk the falsePk to set
	 */
	public void setFalsePk(String falsePk) {
		this.falsePk = falsePk;
	}


	/**
	 * @return the falseNull
	 */
	public String getFalseNull() {
		return falseNull;
	}


	/**
	 * @param falseNull the falseNull to set
	 */
	public void setFalseNull(String falseNull) {
		this.falseNull = falseNull;
	}


	/**
	 * @return the intNum
	 */
	public String getIntNum() {
		return intNum;
	}


	/**
	 * @param intNum the intNum to set
	 */
	public void setIntNum(String intNum) {
		this.intNum = intNum;
	}


	/**
	 * @return the dotNum
	 */
	public String getDotNum() {
		return dotNum;
	}


	/**
	 * @param dotNum the dotNum to set
	 */
	public void setDotNum(String dotNum) {
		this.dotNum = dotNum;
	}


	/**
	 * @return the iniVal
	 */
	public String getIniVal() {
		return iniVal;
	}


	/**
	 * @param iniVal the iniVal to set
	 */
	public void setIniVal(String iniVal) {
		this.iniVal = iniVal;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CsjTableStructXls [tblEn=" + tblEn + ", tblJp=" + tblJp
				+ ", tblRecNum=" + tblRecNum + ", subId=" + subId + ", user="
				+ user + ", psw=" + psw + ", dbType=" + dbType + ", truePk="
				+ truePk + ", trueNull=" + trueNull + ", falsePk=" + falsePk
				+ ", falseNull=" + falseNull + ", intNum=" + intNum
				+ ", dotNum=" + dotNum + ", iniVal=" + iniVal + ", loopRow="
				+ loopRow + ", colMap=" + colMap + ", colContentMap="
				+ colContentMap + "]";
	}


	/**
	 * @return the colContentMap
	 */
	public CsjLinkedMap<String, Integer> getColContentMap() {
		return colContentMap;
	}


	/**
	 * @param colContentMap the colContentMap to set
	 */
	public void setColContentMap(CsjLinkedMap<String, Integer> colContentMap) {
		this.colContentMap = colContentMap;
	}



}

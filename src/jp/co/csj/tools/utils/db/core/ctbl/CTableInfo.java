/**
 *
 */
package jp.co.csj.tools.utils.db.core.ctbl;

import org.mydbsee.common.CmnStrUtils;


/**
 * @author Think
 *
 */
public class CTableInfo {

	/**
	 * @param posStr
	 * @param tblColNm
	 * @param colType
	 */
	public CTableInfo(String posStr, String tblColNm, int colType) {
		this.posStr = posStr;
		this.tblColEnNm = tblColNm;
		this.colType = colType;
	}

	/**
	 * @param posStr
	 * @param tblColNm
	 * @param colType
	 */
	public CTableInfo(String rowNum,String colNum, String tblColNm, String tblColJpNm,String typeLength, int colType,boolean isPk) {
		this.rowNum = Integer.valueOf(String.valueOf(CmnStrUtils.getAllNumericByStr(rowNum)));
		this.colNum = Integer.valueOf(String.valueOf(CmnStrUtils.getAllNumericByStr(colNum)));
		this.posStr = rowNum+"_"+colNum;
		this.tblColEnNm = tblColNm;
		this.tblColJpNm=tblColJpNm;
		this.typeLength = typeLength;
		this.colType = colType;
		this.isPk = isPk;
	}



	/**
	 *
	 */
	public CTableInfo() {
	}
	private int rowNum = -1;
	private int colNum = -1;
	private String posStr = "";
	private String tblColEnNm = "";
	private String tblColJpNm="";
	private int colType = -1;
	private boolean isPk = false;
	private String typeLength="";

	/**
	 * @return the rowNum
	 */
	public int getRowNum() {
		return rowNum;
	}

	/**
	 * @param rowNum the rowNum to set
	 */
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
		this.posStr = rowNum+"_"+colNum;
	}

	/**
	 * @return the colNum
	 */
	public int getColNum() {
		return colNum;
	}

	/**
	 * @param colNum the colNum to set
	 */
	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

	/**
	 * @return the posStr
	 */
	public String getPosStr() {
		return posStr;
	}

	/**
	 * @param posStr the posStr to set
	 */
	public void setPosStr(String posStr) {
		this.posStr = posStr;
	}

	/**
	 * @return the tblColEnNm
	 */
	public String getTblColEnNm() {
		return tblColEnNm;
	}

	/**
	 * @param tblColEnNm the tblColEnNm to set
	 */
	public void setTblColEnNm(String tblColEnNm) {
		this.tblColEnNm = tblColEnNm;
	}

	/**
	 * @return the tblColJpNm
	 */
	public String getTblColJpNm() {
		return tblColJpNm;
	}

	/**
	 * @param tblColJpNm the tblColJpNm to set
	 */
	public void setTblColJpNm(String tblColJpNm) {
		this.tblColJpNm = tblColJpNm;
	}

	/**
	 * @return the colType
	 */
	public int getColType() {
		return colType;
	}

	/**
	 * @param colType the colType to set
	 */
	public void setColType(int colType) {
		this.colType = colType;
	}

	/**
	 * @return the typeLength
	 */
	public String getTypeLength() {
		return typeLength;
	}

	/**
	 * @param typeLength the typeLength to set
	 */
	public void setTypeLength(String typeLength) {
		this.typeLength = typeLength;
	}

	/**
	 * @return the isPk
	 */
	public boolean isPk() {
		return isPk;
	}

	/**
	 * @param isPk the isPk to set
	 */
	public void setPk(boolean isPk) {
		this.isPk = isPk;
	}

}

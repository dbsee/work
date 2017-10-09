/**
 *
 */
package jp.co.csj.tools.utils.xml.dbtools;

/**
 * @author Think
 *
 */
public class XmlInfoXls {
	public static int S_WIDTH = 5;
	private String tblSign = "";
	private String enNmCol = "";
	private String jpNmCol = "";
	private String colInfo = "";
	private int maxCol = 0;
	private String maxRow = "";
	private String dateFormat = "";
	private String strFormat = "";
	private String numberFormat = "";
	private String excelType = "";
	/**
	 * @return the tblSign
	 */
	public String getTblSign() {
		return tblSign;
	}
	/**
	 * @param tblSign the tblSign to set
	 */
	public void setTblSign(String tblSign) {
		this.tblSign = tblSign;
	}
	/**
	 * @return the enNmCol
	 */
	public String getEnNmCol() {
		return enNmCol;
	}
	/**
	 * @param enNmCol the enNmCol to set
	 */
	public void setEnNmCol(String enNmCol) {
		this.enNmCol = enNmCol;
	}
	/**
	 * @return the jpNmCol
	 */
	public String getJpNmCol() {
		return jpNmCol;
	}
	/**
	 * @param jpNmCol the jpNmCol to set
	 */
	public void setJpNmCol(String jpNmCol) {
		this.jpNmCol = jpNmCol;
	}
	/**
	 * @return the colInfo
	 */
	public String getColInfo() {
		return colInfo;
	}
	/**
	 * @param colInfo the colInfo to set
	 */
	public void setColInfo(String colInfo) {
		this.colInfo = colInfo;
	}
	/**
	 * @return the maxRow
	 */
	public String getMaxRow() {
		return maxRow;
	}
	/**
	 * @param maxRow the maxRow to set
	 */
	public void setMaxRow(String maxRow) {
		this.maxRow = maxRow;
	}
	/**
	 * @return the dateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}
	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	/**
	 * @return the strFormat
	 */
	public String getStrFormat() {
		return strFormat;
	}
	/**
	 * @param strFormat the strFormat to set
	 */
	public void setStrFormat(String strFormat) {
		this.strFormat = strFormat;
	}
	/**
	 * @return the numberFormat
	 */
	public String getNumberFormat() {
		return numberFormat;
	}
	/**
	 * @param numberFormat the numberFormat to set
	 */
	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}

	/**
	 * @return the excelType
	 */
	public String getExcelType() {
		return excelType;
	}
	/**
	 * @param excelType the excelType to set
	 */
	public void setExcelType(String excelType) {
		this.excelType = excelType;
	}
	@Override
	public String toString() {
		return "XmlInfoXls [tblSign=" + tblSign + ", enNmCol=" + enNmCol
				+ ", jpNmCol=" + jpNmCol + ", colInfo=" + colInfo + ", maxCol="
				+ maxCol + ", maxRow=" + maxRow + ", dateFormat=" + dateFormat
				+ ", strFormat=" + strFormat + ", numberFormat=" + numberFormat
				+ ", excelType=" + excelType + "]";
	}
	public int getMaxCol() {
		return maxCol;
	}
	public void setMaxCol(int maxCol) {
		this.maxCol = maxCol;
	}

}

/**
 * 
 */
package jp.co.csj.tools.utils.xml.dbtools;

import org.mydbsee.common.CmnStrUtils;

/**
 * @author Think
 *
 */
public class PicInfo {
	private boolean isToExcel = false;
	private String ch = "";
	private String newLine = "";
	private boolean isFromTbl = false;
	private boolean isInOneSheet = false;
	private String btnTime = "";
	/**
	 * @return the btnTime
	 */
	public String getBtnTime() {
		return btnTime;
	}
	/**
	 * @param btnTime the btnTime to set
	 */
	public void setBtnTime(String btnTime) {
		this.btnTime = btnTime;
	}
	/**
	 * @return the isToExcel
	 */
	public boolean isToExcel() {
		return isToExcel;
	}
	/**
	 * @param isToExcel the isToExcel to set
	 */
	public void setToExcel(boolean isToExcel) {
		this.isToExcel = isToExcel;
	}
	/**
	 * @return the ch
	 */
	public String getCh() {
		return ch;
	}
	/**
	 * @param ch the ch to set
	 */
	public void setCh(String ch) {
		this.ch = CmnStrUtils.convertTxtToReg(ch);
	}
	/**
	 * @return the newLine
	 */
	public String getNewLine() {
		return newLine;
	}
	/**
	 * @param newLine the newLine to set
	 */
	public void setNewLine(String newLine) {
		this.newLine =  CmnStrUtils.convertTxtToReg(newLine);
	}
	/**
	 * @return the isFromTbl
	 */
	public boolean isFromTbl() {
		return isFromTbl;
	}
	/**
	 * @param isFromTbl the isFromTbl to set
	 */
	public void setFromTbl(boolean isFromTbl) {
		this.isFromTbl = isFromTbl;
	}
	/**
	 * @return the isInOneSheet
	 */
	public boolean isInOneSheet() {
		return isInOneSheet;
	}
	/**
	 * @param isInOneSheet the isInOneSheet to set
	 */
	public void setInOneSheet(boolean isInOneSheet) {
		this.isInOneSheet = isInOneSheet;
	}
	
}

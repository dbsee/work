/**
 *
 */
package jp.co.csj.tools.utils.file.core;

import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author Think
 *
 */
public class CsjDbXlsRowState {

	public static final int STATUS_NULL = 0;
	public static final int STATUS_TABLE = 1;
	public static final int STATUS_COL_CMT = 2;
	public static final int STATUS_COL_EN = 3;
	public static final int STATUS_COL_TYPE = 4;
	public static final int STATUS_COL_NULL = 5;
	public static final int STATUS_DATA_ROW = 6;
	public static final int STATUS_LAST_ROW = 7;



	HSSFSheet sheet = null;
	String tblSign = "";
	/**
	 * 1:row
	 * 2:table
	 *
	 *
	 */
	int currentState = STATUS_NULL;
	int currentRowNum = 0;

	String tblNmEn = "";
	boolean isBeginTblNm = false;
	int nextRowNum = 0;
	/**
	 *
	 */
	public CsjDbXlsRowState(HSSFSheet sheet,String tblSign) {
		this.sheet = sheet;
		this.tblSign = tblSign;
		reset(sheet,0);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param beginRowNum
	 *
	 */
	private void reset(HSSFSheet sheet, int beginRowNum) {

//
//			String cell1 = CsjPoiUtils.getCellContent(sheet, beginRowNum, 0, false);
//			if (cell1.equals(tblSign)) {
//				nearRowNum = row.getRowNum();
//				nearState = status_table;
//				isBeginTblNm = true;
//				tblNmEn = CsjPoiUtils.getCellContent(sheet, row.getRowNum(), 1, false);
//				tblNmJp = CsjPoiUtils.getCellContent(sheet, row.getRowNum(), 2, false);
//				if (CsjStrUtils.isEmpty(tblNmEn)) {
//					continue;
//				}
//				return;
//			} else if (CsjStrUtils.isNumeric(cell1)&& isBeginTblNm) {
//				nearRowNum = row.getRowNum();
//				nearState = status_row;
//				return;
//			} else if (row.getRowNum()==sheet.getLastRowNum()&& isBeginTblNm) {
//
//				nearRowNum = currentRowNum;
//				nearState = status_table_last;
//				return;
//			}
	}
	public CsjDbXlsRowState(CsjDbXlsRowState xlsRowState) {
		this.sheet = xlsRowState.getSheet();

		reset(sheet,xlsRowState.getNextRowNum());
	}
	/**
	 * @return the currentState
	 */
	public int getCurrentState() {
		return currentState;
	}
	/**
	 * @param currentState the currentState to set
	 */
	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}
	/**
	 * @return the currentRowNum
	 */
	public int getCurrentRowNum() {
		return currentRowNum;
	}
	/**
	 * @param currentRowNum the currentRowNum to set
	 */
	public void setCurrentRowNum(int currentRowNum) {
		this.currentRowNum = currentRowNum;
	}
	/**
	 * @return the sheet
	 */
	public HSSFSheet getSheet() {
		return sheet;
	}
	/**
	 * @param sheet the sheet to set
	 */
	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}
	/**
	 * @return the tblNmEn
	 */
	public String getTblNmEn() {
		return tblNmEn;
	}
	/**
	 * @param tblNmEn the tblNmEn to set
	 */
	public void setTblNmEn(String tblNmEn) {
		this.tblNmEn = tblNmEn;
	}
	/**
	 * @return the isBeginTblNm
	 */
	public boolean isBeginTblNm() {
		return isBeginTblNm;
	}
	/**
	 * @param isBeginTblNm the isBeginTblNm to set
	 */
	public void setBeginTblNm(boolean isBeginTblNm) {
		this.isBeginTblNm = isBeginTblNm;
	}
	/**
	 * @return the nextRowNum
	 */
	public int getNextRowNum() {
		return nextRowNum;
	}
	/**
	 * @param nextRowNum the nextRowNum to set
	 */
	public void setNextRowNum(int nextRowNum) {
		this.nextRowNum = nextRowNum;
	}
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


}

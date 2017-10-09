/**
 *
 */
package jp.co.csj.tools.utils.poi.core;

import jp.co.csj.tools.core.CsjLinkedMap;

/**
 * @author Think
 *
 */
public class CsjRowInfo {

	/**
	 *
	 */
	public CsjRowInfo() {
	}
	private int rowNum=-1;
	private CsjLinkedMap<String,CsjCellInfo> rowMap = new CsjLinkedMap<String, CsjCellInfo>();
	/**
	 * @return the rowNum
	 */
	public int getRowNum() {
		return rowNum;
	}
	/**
	 * @return the rowMap
	 */
	public CsjLinkedMap<String, CsjCellInfo> getRowMap() {
		return rowMap;
	}
	/**
	 * @param rowMap the rowMap to set
	 */
	public void setRowMap(CsjLinkedMap<String, CsjCellInfo> rowMap) {
		this.rowMap = rowMap;
	}
	/**
	 * @param rowNum the rowNum to set
	 */
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CsjRowInfo [rowNum=" + rowNum + ", rowMap=" + rowMap + "]";
	}



}

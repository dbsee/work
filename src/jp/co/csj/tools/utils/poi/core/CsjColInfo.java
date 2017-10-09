/**
 *
 */
package jp.co.csj.tools.utils.poi.core;

import jp.co.csj.tools.core.CsjLinkedMap;

/**
 * @author Think
 *
 */
public class CsjColInfo {


	private int colNum=-1;
	private CsjLinkedMap<String,CsjCellInfo> colMap = new CsjLinkedMap<String, CsjCellInfo>();

	/**
	 *
	 */
	public CsjColInfo() {
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
	 * @return the colMap
	 */
	public CsjLinkedMap<String, CsjCellInfo> getColMap() {
		return colMap;
	}

	/**
	 * @param colMap the colMap to set
	 */
	public void setColMap(CsjLinkedMap<String, CsjCellInfo> colMap) {
		this.colMap = colMap;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CsjColInfo [colNum=" + colNum + ", colMap=" + colMap + "]";
	}


}

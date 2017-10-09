/**
 * 
 */
package jp.co.csj.tools.utils.poi.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mydbsee.common.CmnRandomUtils;

/**
 * @author Think
 *
 */
public class CsjAutoXlsData {
	private boolean isRandom;
	private boolean isRepeat;
	private Map<Integer,Map<Integer,String>> sheetInfoMap = new HashMap<Integer, Map<Integer,String>>();
	private List<Integer> rowIndexList = new ArrayList<Integer>();
	private int rowIndex = 0;
	
	/**
	 * @param random
	 * @param repeat
	 */
	public CsjAutoXlsData(boolean isRandom, boolean isRepeat) {
		this.isRandom = isRandom;
		this.isRepeat = isRepeat;
	}
	public  void reset(CsjAutoXlsData csjAutoXlsData)throws Throwable {
		if (isRandom) {
			if (isRepeat) {
				rowIndex = CmnRandomUtils.getRadomInt(0, rowIndexList.size()-1);
			} else {
				if (rowIndexList.size()==0) {
					rowIndex = -1;
				} else {
					rowIndexList.remove(rowIndex);
					if (rowIndexList.size()==0) {
						rowIndex = -1;
					} else {
						rowIndex = CmnRandomUtils.getRadomInt(0, rowIndexList.size()-1);
					}
				}
				//rowIndexList.remove(new Integer(rowIndex));
			}
			
		} else {
			if (isRepeat) {
				rowIndex=(rowIndex+1)%rowIndexList.size();
			} else {
		
				if (rowIndexList.size()==0) {
					rowIndex = -1;
				} else {
					rowIndexList.remove(rowIndex);
					if (rowIndexList.size()==0) {
						rowIndex = -1;
					}
				}
			}
		}
	}
	/**
	 * @return the sheetInfoMap
	 */
	public Map<Integer, Map<Integer, String>> getSheetInfoMap() {
		return sheetInfoMap;
	}
	/**
	 * @param sheetInfoMap the sheetInfoMap to set
	 */
	public void setSheetInfoMap(Map<Integer, Map<Integer, String>> sheetInfoMap) {
		this.sheetInfoMap = sheetInfoMap;
	}
	/**
	 * @return the rowIndexList
	 */
	public List<Integer> getRowIndexList() {
		return rowIndexList;
	}
	/**
	 * @param rowIndexList the rowIndexList to set
	 */
	public void setRowIndexList(List<Integer> rowIndexList) {
		this.rowIndexList = rowIndexList;
	}
	/**
	 * @return the rowIndex
	 */
	public int getRowIndex() {
		return rowIndex;
	}
	/**
	 * @param rowIndex the rowIndex to set
	 */
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	/**
	 * @param max 
	 * @param isRandom 
	 * 
	 */
	public void resetRowIndexList(int max, boolean isRandom) {
		rowIndexList.clear();
		if (isRandom) {
			int[] s = CmnRandomUtils.randomArray(0, max-1, max);
			for (int i : s) {
				rowIndexList.add(i);
			}
			rowIndex = s[0];
		} else {
			for (int i = 0; i < max; i++) {
				rowIndexList.add(i);
			}
			rowIndex = 0;
		}

	}
	/**
	 * @return the isRandom
	 */
	public boolean isRandom() {
		return isRandom;
	}
	/**
	 * @param isRandom the isRandom to set
	 */
	public void setRandom(boolean isRandom) {
		this.isRandom = isRandom;
	}
	/**
	 * @return the isRepeat
	 */
	public boolean isRepeat() {
		return isRepeat;
	}
	/**
	 * @param isRepeat the isRepeat to set
	 */
	public void setRepeat(boolean isRepeat) {
		this.isRepeat = isRepeat;
	}
	
	
}

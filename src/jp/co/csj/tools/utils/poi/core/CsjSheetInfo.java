/**
 *
 */
package jp.co.csj.tools.utils.poi.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Think
 *
 */
public class CsjSheetInfo {

	private String sheetNm = "";
	private String filePath = "";
	private List<CsjCellInfo> cellInfoList = new ArrayList<CsjCellInfo>();
	private Map<String,CsjCellInfo> csjCellInfoMap = new HashMap<String, CsjCellInfo>();
	private Map<String,CsjCellInfo> csjCellPosInfoMap = new HashMap<String, CsjCellInfo>();
	private Map<Integer,CsjColInfo> csjColInfoMap = new HashMap<Integer, CsjColInfo>();
	private Map<Integer,CsjRowInfo> csjRowInfoMap = new  HashMap<Integer,CsjRowInfo>();
	private Map<Integer,List<CsjCellInfo>> csjCellPosInfoRowList = new LinkedHashMap<Integer, List<CsjCellInfo>>();
	private Map<Integer,List<CsjCellInfo>> csjCellPosInfoColList = new LinkedHashMap<Integer, List<CsjCellInfo>>();
	/**
	 * @return the csjCellInfoMap
	 */
	public Map<String, CsjCellInfo> getCsjCellInfoMap() {
		return csjCellInfoMap;
	}
	/**
	 * @param csjCellInfoMap the csjCellInfoMap to set
	 */
	public void setCsjCellInfoMap(Map<String, CsjCellInfo> csjCellInfoMap) {
		this.csjCellInfoMap = csjCellInfoMap;
	}
	/**
	 * @return the csjColInfoMap
	 */
	public Map<Integer, CsjColInfo> getCsjColInfoMap() {
		return csjColInfoMap;
	}
	/**
	 * @param csjColInfoMap the csjColInfoMap to set
	 */
	public void setCsjColInfoMap(Map<Integer, CsjColInfo> csjColInfoMap) {
		this.csjColInfoMap = csjColInfoMap;
	}
	/**
	 * @return the csjRowInfoMap
	 */
	public Map<Integer, CsjRowInfo> getCsjRowInfoMap() {
		return csjRowInfoMap;
	}
	/**
	 * @param csjRowInfoMap the csjRowInfoMap to set
	 */
	public void setCsjRowInfoMap(Map<Integer, CsjRowInfo> csjRowInfoMap) {
		this.csjRowInfoMap = csjRowInfoMap;
	}
	/**
	 * @return the csjCellPosInfoMap
	 */
	public Map<String, CsjCellInfo> getCsjCellPosInfoMap() {
		return csjCellPosInfoMap;
	}
	/**
	 * @param csjCellPosInfoMap the csjCellPosInfoMap to set
	 */
	public void setCsjCellPosInfoMap(Map<String, CsjCellInfo> csjCellPosInfoMap) {
		this.csjCellPosInfoMap = csjCellPosInfoMap;
	}
	/**
	 * @return the sheetNm
	 */
	public String getSheetNm() {
		return sheetNm;
	}
	/**
	 * @param sheetNm the sheetNm to set
	 */
	public void setSheetNm(String sheetNm) {
		this.sheetNm = sheetNm;
	}
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * @return the cellInfoList
	 */
	public List<CsjCellInfo> getCellInfoList() {
		return cellInfoList;
	}
	/**
	 * @param cellInfoList the cellInfoList to set
	 */
	public void setCellInfoList(List<CsjCellInfo> cellInfoList) {
		this.cellInfoList = cellInfoList;
	}
	/**
	 * @return the csjCellPosInfoRowList
	 */
	public Map<Integer, List<CsjCellInfo>> getCsjCellPosInfoRowList() {
		return csjCellPosInfoRowList;
	}
	/**
	 * @param csjCellPosInfoRowList the csjCellPosInfoRowList to set
	 */
	public void setCsjCellPosInfoRowList(
			Map<Integer, List<CsjCellInfo>> csjCellPosInfoRowList) {
		this.csjCellPosInfoRowList = csjCellPosInfoRowList;
	}
	/**
	 * @return the csjCellPosInfoColList
	 */
	public Map<Integer, List<CsjCellInfo>> getCsjCellPosInfoColList() {
		return csjCellPosInfoColList;
	}
	/**
	 * @param csjCellPosInfoColList the csjCellPosInfoColList to set
	 */
	public void setCsjCellPosInfoColList(
			Map<Integer, List<CsjCellInfo>> csjCellPosInfoColList) {
		this.csjCellPosInfoColList = csjCellPosInfoColList;
	}


}

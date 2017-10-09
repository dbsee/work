/**
 *
 */
package jp.co.csj.tools.utils.db.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.csj.tools.utils.db.core.ctbl.CTableInfo;

/**
 * @author Think
 *
 */
public class TableMakerIni {

	private String tblNm = "";
	Map<String,List<CTableInfo>> mapTblInfo = new HashMap<String, List<CTableInfo>>();
	Map<String ,TblPara> tblParaMap = new LinkedHashMap<String, TblPara>();


	private String fileAbsPath ="";
	private String sheetNm ="";
	private int startRow = 0;
	private int endRow = 0;
	Map<String ,CTableInfo> tblMap = new LinkedHashMap<String, CTableInfo>();
	public static void main(String[] args) {

	}

	/**
	 * @return the tblNm
	 */
	public String getTblNm() {
		return tblNm;
	}
	/**
	 * @param tblNm the tblNm to set
	 */
	public void setTblNm(String tblNm) {
		this.tblNm = tblNm;
	}

	/**
	 * @return the mapTblInfo
	 */
	public Map<String, List<CTableInfo>> getMapTblInfo() {
		return mapTblInfo;
	}

	/**
	 * @param mapTblInfo the mapTblInfo to set
	 */
	public void setMapTblInfo(Map<String, List<CTableInfo>> mapTblInfo) {
		this.mapTblInfo = mapTblInfo;
	}


	/**
	 * @return the tblParaMap
	 */
	public Map<String, TblPara> getTblParaMap() {
		return tblParaMap;
	}

	/**
	 * @param tblParaMap the tblParaMap to set
	 */
	public void setTblParaMap(Map<String, TblPara> tblParaMap) {
		this.tblParaMap = tblParaMap;
	}

	/**
	 * @return the tblMap
	 */
	public Map<String, CTableInfo> getTblMap() {
		return tblMap;
	}

	/**
	 * @param tblMap the tblMap to set
	 */
	public void setTblMap(Map<String, CTableInfo> tblMap) {
		this.tblMap = tblMap;
	}

	/**
	 * @return the startRow
	 */
	public int getStartRow() {
		return startRow;
	}

	/**
	 * @param startRow the startRow to set
	 */
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	/**
	 * @return the endRow
	 */
	public int getEndRow() {
		return endRow;
	}

	/**
	 * @param endRow the endRow to set
	 */
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	/**
	 * @return the fileAbsPath
	 */
	public String getFileAbsPath() {
		return fileAbsPath;
	}

	/**
	 * @param fileAbsPath the fileAbsPath to set
	 */
	public void setFileAbsPath(String fileAbsPath) {
		this.fileAbsPath = fileAbsPath;
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
}

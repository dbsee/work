package jp.co.csj.tools.utils.db.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import jp.co.csj.tools.utils.common.constant.CsjConst;

public class SheetTblsInfo {

	private String sheetNm = CsjConst.EMPTY;
	private String xlsFileNm = CsjConst.EMPTY;
	private HashMap<String,List<String>> dbTypeMap = new HashMap<String, List<String>>();
	private LinkedHashMap<String,XlsTblInfo> tblInfoMap = new LinkedHashMap<String, XlsTblInfo>();

	public String getSheetNm() {
		return sheetNm;
	}

	public void setSheetNm(String sheetNm) {
		this.sheetNm = sheetNm;
	}

	public String getXlsFileNm() {
		return xlsFileNm;
	}

	public void setXlsFileNm(String xlsFileNm) {
		this.xlsFileNm = xlsFileNm;
	}

	public LinkedHashMap<String, XlsTblInfo> getTblInfoMap() {
		return tblInfoMap;
	}

	public void setTblInfoMap(LinkedHashMap<String, XlsTblInfo> tblInfoMap) {
		this.tblInfoMap = tblInfoMap;
	}

	/**
	 * @return the dbTypeMap
	 */
	public HashMap<String, List<String>> getDbTypeMap() {
		return dbTypeMap;
	}

	/**
	 * @param dbTypeMap the dbTypeMap to set
	 */
	public void setDbTypeMap(HashMap<String, List<String>> dbTypeMap) {
		this.dbTypeMap = dbTypeMap;
	}


}

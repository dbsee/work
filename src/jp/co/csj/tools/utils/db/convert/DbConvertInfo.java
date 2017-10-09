/**
 *
 */
package jp.co.csj.tools.utils.db.convert;

import java.util.HashMap;

/**
 * @author Think
 *
 */
public class DbConvertInfo {

	HashMap<String,ConvertDb> dbMap =  new HashMap<String, ConvertDb>();
	HashMap<String,ConvertTbl> tblMap =  new HashMap<String, ConvertTbl>();
	HashMap<String,ConvertColumn> columnMap =  new HashMap<String, ConvertColumn>();
	/**
	 * @return the dbMap
	 */
	public HashMap<String, ConvertDb> getDbMap() {
		return dbMap;
	}
	/**
	 * @param dbMap the dbMap to set
	 */
	public void setDbMap(HashMap<String, ConvertDb> dbMap) {
		this.dbMap = dbMap;
	}
	/**
	 * @return the tblMap
	 */
	public HashMap<String, ConvertTbl> getTblMap() {
		return tblMap;
	}
	/**
	 * @param tblMap the tblMap to set
	 */
	public void setTblMap(HashMap<String, ConvertTbl> tblMap) {
		this.tblMap = tblMap;
	}
	/**
	 * @return the columnMap
	 */
	public HashMap<String, ConvertColumn> getColumnMap() {
		return columnMap;
	}
	/**
	 * @param columnMap the columnMap to set
	 */
	public void setColumnMap(HashMap<String, ConvertColumn> columnMap) {
		this.columnMap = columnMap;
	}


}

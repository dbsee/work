package jp.co.csj.tools.utils.db.core;

import jp.co.csj.tools.core.CsjLinkedMap;


public class ViewSqlInfo {

	private CsjLinkedMap<String, CsjSqlInfo> sqlInfoMap = new CsjLinkedMap<String, CsjSqlInfo>();

	/**
	 * @return the sqlInfoMap
	 */
	public CsjLinkedMap<String, CsjSqlInfo> getSqlInfoMap() {
		return sqlInfoMap;
	}

	/**
	 * @param sqlInfoMap the sqlInfoMap to set
	 */
	public void setSqlInfoMap(CsjLinkedMap<String, CsjSqlInfo> sqlInfoMap) {
		this.sqlInfoMap = sqlInfoMap;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ViewSqlInfo [sqlInfoMap=" + sqlInfoMap + "]";
	}



}

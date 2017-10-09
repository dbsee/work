package jp.co.csj.tools.utils.db.core;

import java.util.LinkedHashSet;
import java.util.Map.Entry;

import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.core.CsjLinkedMap;

public class CsjTblSelectSql {

	private boolean checked = true;
	private String tblNm = "";
	private String sqlWhere = "";
	private String sqlOrder = "";
	private String sqlDistinct = "";
	private LinkedHashSet<String> colSet = new LinkedHashSet<String>();

	/**
	 * @param tblNm
	 * @param sqlWhere
	 * @param sqlOrder
	 */
	public CsjTblSelectSql(String tblNm, String sqlWhere, String sqlOrder) {
		this.checked = true;
		this.tblNm = tblNm;
		setSqlWhere(sqlWhere);
		this.sqlOrder = sqlOrder;
	}

	public CsjTblSelectSql() {

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
	 * @return the sqlWhere
	 */
	public String getSqlWhere() {

		return sqlWhere;
	}
	public String getSqlWhereTrimDistinct() {
		if (CmnStrUtils.isEmpty(sqlWhere)) {
			sqlWhere = "";
		}
		if (sqlWhere.trim().endsWith("(distinct)")) {
			return sqlWhere.substring(0, sqlWhere.lastIndexOf("(distinct)"));
		}
		return sqlWhere;
	}

	/**
	 * @param sqlWhere the sqlWhere to set
	 */
	public void setSqlWhere(String sqlWhere) {
		this.sqlWhere = sqlWhere.trim();
		if (CmnStrUtils.isNotEmpty(sqlWhere)) {
			if (sqlWhere.trim().endsWith("(distinct)")) {
				setSqlDistinct(" distinct ");
				sqlWhere = sqlWhere.substring(0, sqlWhere.lastIndexOf("(distinct)"));
			} else {
				setSqlDistinct("");
			}
		} else {
			setSqlDistinct("");
		}
	}

	/**
	 * @return the sqlOrder
	 */
	public String getSqlOrder() {
		return sqlOrder;
	}

	/**
	 * @param sqlOrder the sqlOrder to set
	 */
	public void setSqlOrder(String sqlOrder) {
		this.sqlOrder = sqlOrder;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CsjTblSelectSql [tblNm=" + tblNm + ", sqlWhere=" + sqlWhere
				+ ", sqlOrder=" + sqlOrder + "]";
	}
	public String getSelectSql(String columns, String schema, boolean isZero, boolean hasSelectStr) {
		StringBuffer sb = new StringBuffer();
		if (CmnStrUtils.isEmpty(this.getTblNm())) {
			return "";
		}
		if (hasSelectStr) {
			sb.append(" SELECT " + sqlDistinct + columns +" FROM ");
			sb.append(schema);
			sb.append("\""+this.getTblNm()+"\" ");
			if (isZero) {
				sb.append(" WHERE 1=2 ");
			} else {
				sb.append(" WHERE 1=1 ");
			}
		}

			sb.append(getWhereSql());
		if (CmnStrUtils.isNotEmpty(this.getSqlOrder())) {
			sb.append(getOrderBySql());
		}
		return sb.toString();
	}
	public String getWhereSql() {
		StringBuffer sb = new StringBuffer();
		if (CmnStrUtils.isNotEmpty(this.getSqlWhereTrimDistinct())) {
			sb.append(" AND ");
			sb.append(this.getSqlWhereTrimDistinct()+" ");
		}
		return sb.toString();
	}
	
	public String getOrderBySql() {
		StringBuffer sb = new StringBuffer();
		if (CmnStrUtils.isNotEmpty(this.getSqlOrder())) {
			sb.append(" ORDER BY ");
			sb.append(sqlOrder);
		}
		return sb.toString();
	}

	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	/**
	 *
	 */
	public static CsjLinkedMap<String, CsjTblSelectSql> cloneTblSelectSqlMap(CsjLinkedMap<String, CsjTblSelectSql> paraMap) {
		CsjLinkedMap<String, CsjTblSelectSql> retMap = new CsjLinkedMap<String, CsjTblSelectSql>();
		// TODO Auto-generated method stub
		for (Entry<String, CsjTblSelectSql> entry: paraMap.entrySet()) {
			CsjTblSelectSql val = entry.getValue();
			CsjTblSelectSql csjTblSelectSql = new CsjTblSelectSql();
			csjTblSelectSql.setChecked(val.isChecked());
			csjTblSelectSql.setSqlOrder(val.getSqlOrder());
			csjTblSelectSql.setSqlWhere(val.getSqlWhere());
			csjTblSelectSql.setTblNm(val.getTblNm());
			retMap.put(entry.getKey(), csjTblSelectSql);
		}

		return retMap;
	}

	/**
	 * @return the colSet
	 */
	public LinkedHashSet<String> getColSet() {
		return colSet;
	}

	/**
	 * @param colSet the colSet to set
	 */
	public void setColSet(LinkedHashSet<String> colSet) {
		this.colSet = colSet;
	}

	public String getSqlDistinct() {
		return sqlDistinct;
	}

	public void setSqlDistinct(String sqlDistinct) {
		this.sqlDistinct = sqlDistinct;
	}
}

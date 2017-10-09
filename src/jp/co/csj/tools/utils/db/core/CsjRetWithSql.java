/**
 *
 */
package jp.co.csj.tools.utils.db.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mydbsee.common.CmnLog5j;
import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.core.CsjDbDataLst;
import jp.co.csj.tools.utils.common.constant.CsjConst;

/**
 * @author Think
 *
 */
public class CsjRetWithSql {
	private String selectTableDataSql = CsjConst.EMPTY;
	private String title = "";
	private String rowInfo = "";
	private String endStr = "";
	private List<Object> paraList = new ArrayList<Object>();
	private TblInfo tblInfo = null;
	private CsjDbDataLst csjDbDataLst = new CsjDbDataLst();

	/**
	 * @return the title
	 */
	public String getTitle() {
		if (CmnStrUtils.isEmpty(title)) {
			return "";
		} else {
			return CmnLog5j.addlrBracket_M_L_JP(title, false);
		}
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the selectTableDataSql
	 */
	public String getSelectTableDataSql() {
		return selectTableDataSql;
	}
	/**
	 * @param selectTableDataSql the selectTableDataSql to set
	 */
	public void setSelectTableDataSql(String selectTableDataSql) {
		this.selectTableDataSql = selectTableDataSql;
	}
	/**
	 * @return the dataList
	 */
	public List<HashMap<String, String>> getDataList() {
		return csjDbDataLst.getResultList();
	}
	/**
	 * @param dataList the dataList to set
	 */
	public void setDataList(List<HashMap<String, String>> dataList) {
		for (HashMap<String, String> map : dataList) {
			map.remove("XXXROWNUMXXX");
		}
		csjDbDataLst.setResultList(dataList);
	}
	public String getRowInfo() {
		return rowInfo;
	}
	public void setRowInfo(String rowInfo) {
		this.rowInfo = rowInfo;
	}
	public List<Object> getParaList() {
		return paraList;
	}
	public void setParaList(List<Object> paraList) {
		for (Object o : paraList) {
			this.paraList.add(o);
		}
	}

	public CsjDbDataLst getCsjDbDataLst() {
		return csjDbDataLst;
	}
	public void setCsjDbDataLst(CsjDbDataLst csjDbDataLst) {
		this.csjDbDataLst = csjDbDataLst;
	}
	public TblInfo getTblInfo() {
		return tblInfo;
	}
	public void setTblInfo(TblInfo tblInfo) {
		this.tblInfo = tblInfo;
	}
	public String getEndStr() {
		return endStr;
	}
	public void setEndStr(String endStr) {
		this.endStr = endStr;
	}


}

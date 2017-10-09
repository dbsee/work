package jp.co.csj.tools.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.db.core.TableDbItem;

public class CsjDbDataLst {

	private List<HashMap<String, String>> resultList = new ArrayList<HashMap<String,String>>();
	private Map<String, TableDbItem> csjLinkedMap = new CsjLinkedMap<String, TableDbItem>();
	private String runTime = CsjConst.EMPTY;
	public CsjDbDataLst(List<HashMap<String, String>> resultList,
			Map<String, TableDbItem> csjLinkedMap,String runTime) {
		this.resultList = null;
		this.resultList = resultList;
		this.csjLinkedMap = csjLinkedMap;
		this.runTime = runTime;
	}
	public CsjDbDataLst() {
	}
	public List<HashMap<String, String>> getResultList() {
		return resultList;
	}
	public void setResultList(List<HashMap<String, String>> resultList) {
		this.resultList = resultList;
	}
	public Map<String, TableDbItem> getCsjLinkedMap() {
		return csjLinkedMap;
	}
	public void setCsjLinkedMap(Map<String, TableDbItem> csjLinkedMap) {
		this.csjLinkedMap = csjLinkedMap;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
			
}

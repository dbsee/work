/**
 * 
 */
package jp.co.csj.tools.utils.db.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 963210
 *
 */
public class XlsRecord {
	private int type = -1;
	private List<XlsTblPara> record = new ArrayList<XlsTblPara>();
	private LinkedHashMap<Integer, XlsTblPara> recmap = new LinkedHashMap<Integer, XlsTblPara>();


	public void convertMap2List() {
		for (Map.Entry<Integer, XlsTblPara> entry : recmap.entrySet()) {
			record.add(entry.getValue());
		}
	}


	public List<XlsTblPara> getRecord() {
		return record;
	}


	public void setRecord(List<XlsTblPara> record) {
		this.record = record;
	}


	public LinkedHashMap<Integer, XlsTblPara> getRecmap() {
		return recmap;
	}


	public void setRecmap(LinkedHashMap<Integer, XlsTblPara> recmap) {
		this.recmap = recmap;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}

}

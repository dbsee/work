/**
 * 
 */
package jp.co.csj.tools.utils.schedual;

import java.util.LinkedHashMap;
import java.util.Map;

import org.mydbsee.common.CmnStrUtils;

/**
 * @author Think
 *
 */
public class SchedualRecs {

	private String oldContent = "";
	private String filePath = "";
	private Map<String , SchedualRec> schedualRecMap = new LinkedHashMap<String, SchedualRec>();

	/**
	 * 
	 */
	public SchedualRecs() {
		// TODO Auto-generated constructor stub
	}




	/**
	 * @return the oldContent
	 */
	public String getOldContent() {
		return oldContent;
	}



	/**
	 * @param oldContent the oldContent to set
	 */
	public void setOldContent(String oldContent) {
		this.oldContent = oldContent;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (SchedualRec s: schedualRecMap.values()) {
			if (CmnStrUtils.isNotEmpty(s.getExcelPath()) && CmnStrUtils.isNotEmpty(s.getExcelSheetName())) {
				sb.append(s);
			}
		}
		
		return sb.toString();
	}



	/**
	 * @return the schedualRecMap
	 */
	public Map<String, SchedualRec> getSchedualRecMap() {
		return schedualRecMap;
	}



	/**
	 * @param schedualRecMap the schedualRecMap to set
	 */
	public void setSchedualRecMap(Map<String, SchedualRec> schedualRecMap) {
		this.schedualRecMap = schedualRecMap;
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


}

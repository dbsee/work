/**
 *
 */
package jp.co.csj.tools.utils.db.change.tbl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.core.CsjLinkedMap;

/**
 * @author Think
 *
 */
public class CsjChangeTblInfo {

	private CsjLinkedMap<String,CsjChangeTbl> csjChangeTblsMap = new CsjLinkedMap<String, CsjChangeTbl>();

	public static long s_all_sumTblXlsCnt=0;
	public static long s_all_sumTblDbCnt=0;
	public static long s_all_sumRecUptCnt=0;
	public static long s_all_sumRecInsCnt=0;
	public static long s_all_sumRecsDelCnt=0;

	private long sumTblXlsCnt=0;
	private long sumTblDbCnt=0;
	private long sumRecUptCnt=0;
	private long sumRecInsCnt=0;
	private long sumRecsDelCnt=0;

	private List<String[]> viewSmallList = new ArrayList<String[]>();
	private String[] viewBigStrArr = null;
//	public String getInfoForFileNm() {
//
//	}
	public void resetCnt() {

		sumTblXlsCnt=0;
		sumTblDbCnt=0;
		sumRecUptCnt=0;
		sumRecsDelCnt=0;
		sumRecInsCnt=0;
		viewSmallList.clear();
		
		int index = 0;
		int len = CmnStrUtils.getNumByte(this.csjChangeTblsMap.entrySet().size());
		for (Entry<String,CsjChangeTbl> entry : this.csjChangeTblsMap.entrySet()) {
			CsjChangeTbl changeTbl = entry.getValue();

			String[] s = null;
			s = new String[8];
			s[0] = String.format("%0"+len+"d", ++index);
			s[1] = changeTbl.getTblNmEn();
			s[2] = changeTbl.getTblNmJp();
			s[3] = String.valueOf(changeTbl.getTblXlsCnt());
			s[4] = String.valueOf(changeTbl.getTblDbCnt());
			s[5] = String.valueOf(changeTbl.getRecUptCnt());
			s[6] = String.valueOf(changeTbl.getRecDelCnt());
			s[7] = String.valueOf(changeTbl.getRecInsCnt());
			sumRecInsCnt+=changeTbl.getRecInsCnt();

			sumTblXlsCnt+=changeTbl.getTblXlsCnt();
			sumTblDbCnt+=changeTbl.getTblDbCnt();
			sumRecUptCnt+=changeTbl.getRecUptCnt();
			sumRecsDelCnt+=changeTbl.getRecDelCnt();
			viewSmallList.add(s);
		}

		viewBigStrArr = new String[6];
		viewBigStrArr[0] = String.valueOf(csjChangeTblsMap.size());
		viewBigStrArr[1] = String.valueOf(sumTblXlsCnt);
		viewBigStrArr[2] = String.valueOf(sumTblDbCnt);
		viewBigStrArr[3] = String.valueOf(sumRecUptCnt);
		viewBigStrArr[4] = String.valueOf(sumRecsDelCnt);
		viewBigStrArr[5] = String.valueOf(sumRecInsCnt);
	}
	/**
	 * @return the sumTblXlsCnt
	 */
	public long getSumTblXlsCnt() {
		return sumTblXlsCnt;
	}
	/**
	 * @param sumTblXlsCnt the sumTblXlsCnt to set
	 */
	public void setSumTblXlsCnt(long sumTblXlsCnt) {
		this.sumTblXlsCnt = sumTblXlsCnt;
	}
	/**
	 * @return the sumTblDbCnt
	 */
	public long getSumTblDbCnt() {
		return sumTblDbCnt;
	}
	/**
	 * @param sumTblDbCnt the sumTblDbCnt to set
	 */
	public void setSumTblDbCnt(long sumTblDbCnt) {
		this.sumTblDbCnt = sumTblDbCnt;
	}
	/**
	 * @return the sumRecUptCnt
	 */
	public long getSumRecUptCnt() {
		return sumRecUptCnt;
	}
	/**
	 * @param sumRecUptCnt the sumRecUptCnt to set
	 */
	public void setSumRecUptCnt(long sumRecUptCnt) {
		this.sumRecUptCnt = sumRecUptCnt;
	}
	/**
	 * @return the sumRecInsCnt
	 */
	public long getSumRecInsCnt() {
		return sumRecInsCnt;
	}
	/**
	 * @param sumRecInsCnt the sumRecInsCnt to set
	 */
	public void setSumRecInsCnt(long sumRecInsCnt) {
		this.sumRecInsCnt = sumRecInsCnt;
	}
	/**
	 * @return the sumRecsDelCnt
	 */
	public long getSumRecsDelCnt() {
		return sumRecsDelCnt;
	}
	/**
	 * @param sumRecsDelCnt the sumRecsDelCnt to set
	 */
	public void setSumRecsDelCnt(long sumRecsDelCnt) {
		this.sumRecsDelCnt = sumRecsDelCnt;
	}
	/**
	 * @return the csjChangeTblsMap
	 */
	public CsjLinkedMap<String, CsjChangeTbl> getCsjChangeTblsMap() {
		return csjChangeTblsMap;
	}
	/**
	 * @param csjChangeTblsMap the csjChangeTblsMap to set
	 */
	public void setCsjChangeTblsMap(
			CsjLinkedMap<String, CsjChangeTbl> csjChangeTblsMap) {
		this.csjChangeTblsMap = csjChangeTblsMap;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CsjChangeTblInfo [csjChangeTblsMap=" + csjChangeTblsMap
				+ ", sumTblXlsCnt=" + sumTblXlsCnt + ", sumTblDbCnt="
				+ sumTblDbCnt + ", sumRecUptCnt=" + sumRecUptCnt
				+ ", sumRecInsCnt=" + sumRecInsCnt + ", sumRecsDelCnt="
				+ sumRecsDelCnt + "]";
	}
	/**
	 * @return the viewSmallList
	 */
	public List<String[]> getViewSmallList() {
		return viewSmallList;
	}
	/**
	 * @param viewSmallList the viewSmallList to set
	 */
	public void setViewSmallList(List<String[]> viewSmallList) {
		this.viewSmallList = viewSmallList;
	}
	/**
	 * @return the viewBigStrArr
	 */
	public String[] getViewBigStrArr() {
		return viewBigStrArr;
	}
	/**
	 * @param viewBigStrArr the viewBigStrArr to set
	 */
	public void setViewBigStrArr(String[] viewBigStrArr) {
		this.viewBigStrArr = viewBigStrArr;
	}


}

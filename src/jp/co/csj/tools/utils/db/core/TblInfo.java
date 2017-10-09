package jp.co.csj.tools.utils.db.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.mydbsee.common.CmnLog5j;
import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.utils.common.constant.CsjConst;

public class TblInfo extends TblBase{
	protected LinkedHashMap<String, TblPara> pKeyMap = new LinkedHashMap<String, TblPara>();
	private List<Integer> keyPosList = new ArrayList<Integer> ();
	protected LinkedHashMap<String, TblPara> paraInfoMap = new LinkedHashMap<String, TblPara>();
	protected String sqlCreateTblForOra = CsjConst.EMPTY;
	protected String sqlInsertTblForOra = CsjConst.EMPTY;
	protected List<String> exeCreateTblList = new ArrayList<String>();

	public String getSelectStr() {
		StringBuffer sb = new StringBuffer();
		for (Entry<String, TblPara> entry : paraInfoMap.entrySet()) {
			TblPara tblPara = entry.getValue();
			sb.append(CmnStrUtils.addLRSign(tblPara.getParaNmEn(),
					CsjConst.SIGN_DOUBLE));
			if (tblPara.isLastPara()) {
			} else {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	public String getTableNmForTreeWithColNum() {
		return CsjConst.SIGN_RECTANGLE_SOLID + this.getTblNmDisplay() + CmnLog5j.addlrBracketsS(String.valueOf(this.getParaInfoMap().size()), false);
	}
	public String getTableNmForTree() {
		return CsjConst.SIGN_RECTANGLE_SOLID + this.getTblNmDisplay();
	}
	
	public static void main(String[] args) {
//		TblInfo tblInfo = new TblInfo();
//		tblInfo.setTblNmEn("ttt");
//		TblPara para1 = new TblPara();
//		para1.setParaNmEn("col1");
//		para1.setParaNmJp("com1");
//		para1.setParaType("CHAR");
//		para1.setParaStrLen("10");
//		para1.setParaTypeWithlen("CHAR(10)");
//		para1.setCanNull(false);
//		tblInfo.getParaInfoMap().put(para1.getParaNmEn(), para1);
//		tblInfo.getpKeyMap().put(para1.getParaNmEn(), para1);
//
//		TblPara para2 = new TblPara();
//		para2.setParaNmEn("col2");
//		para2.setParaNmJp("com2");
//		para2.setParaType("CHAR");
//		para2.setParaStrLen("20");
//		para2.setParaTypeWithlen("CHAR(20)");
//		para2.setCanNull(true);
//		para2.setParaInitVal("$");
//		tblInfo.getParaInfoMap().put(para2.getParaNmEn(), para2);

		//System.out.println(tblInfo.getSqlCreateTblForOra());
	}
	/**
	 * @return the pKeyMap
	 */
	public LinkedHashMap<String, TblPara> getpKeyMap() {
		return pKeyMap;
	}
	/**
	 * @param pKeyMap the pKeyMap to set
	 */
	public void setpKeyMap(LinkedHashMap<String, TblPara> pKeyMap) {
		this.pKeyMap = pKeyMap;
	}
	/**
	 * @return the paraInfoMap
	 */
	public LinkedHashMap<String, TblPara> getParaInfoMap() {
		return paraInfoMap;
	}
	/**
	 * @param paraInfoMap the paraInfoMap to set
	 */
	public void setParaInfoMap(LinkedHashMap<String, TblPara> paraInfoMap) {
		this.paraInfoMap = paraInfoMap;
	}

	public void setExeCreateTblList(List<String> exeCreateTblList) {
		this.exeCreateTblList = exeCreateTblList;
	}
	public String getSqlCreateTblForOra() {
		return sqlCreateTblForOra;
	}
	public void setSqlCreateTblForOra(String sqlCreateTblForOra) {
		this.sqlCreateTblForOra = sqlCreateTblForOra;
	}
	/**
	 * @return the keyPosList
	 */
	public List<Integer> getKeyPosList() {
		return keyPosList;
	}
	/**
	 * @param keyPosList the keyPosList to set
	 */
	public void setKeyPosList(List<Integer> keyPosList) {
		this.keyPosList = keyPosList;
	}


}

/**
 * 
 */
package jp.co.csj.tools.utils.db.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author 963210
 *
 */
public class XlsTblInfo extends TblInfo{

	private boolean isHaveJpNm = true;
	private List<XlsRecord> tblDataList = new ArrayList<XlsRecord> ();
	private List<Integer> keyPosList = new ArrayList<Integer> ();
	private LinkedHashMap<Integer, TblPara> paraPosInfoMap = new LinkedHashMap<Integer, TblPara>();
	private boolean isTabled = false;
	/**
	 * 
	 */
	public XlsTblInfo() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public List<XlsRecord> getTblDataList() {
		return tblDataList;
	}

	public void setTblDataList(List<XlsRecord> tblDataList) {
		this.tblDataList = tblDataList;
	}

	public List<Integer> getKeyPosList() {
		return keyPosList;
	}

	public void setKeyPosList(List<Integer> keyPosList) {
		this.keyPosList = keyPosList;
	}

	public LinkedHashMap<Integer, TblPara> getParaPosInfoMap() {
		return paraPosInfoMap;
	}

	public void setParaPosInfoMap(LinkedHashMap<Integer, TblPara> paraPosInfoMap) {
		this.paraPosInfoMap = paraPosInfoMap;
	}

	public boolean isHaveJpNm() {
		return isHaveJpNm;
	}

	public void setHaveJpNm(boolean isHaveJpNm) {
		this.isHaveJpNm = isHaveJpNm;
	}

    /**
     * @return the isTabled
     */
    public boolean isTabled() {
        return isTabled;
    }

    /**
     * @param isTabled the isTabled to set
     */
    public void setTabled(boolean isTabled) {
        this.isTabled = isTabled;
    }

}

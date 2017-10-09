package jp.co.csj.tools.utils.db.core;

import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.utils.common.constant.CsjConst;

public class TblBase {
	protected String tblNmEn = CsjConst.EMPTY;
	protected String tblNmJp = CsjConst.EMPTY;
	protected String tblNmDisplay = CsjConst.EMPTY;
	protected int colCount = 0;
	protected boolean isAllDel = false;
	public TblBase() {
	}
	/**
	 * @param tblNmEn
	 */
	public TblBase(String tblNmEn) {
		this.tblNmEn = tblNmEn;
		setTblNmDisplay();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public String getTblNmEn() {
		return tblNmEn;
	}
	public void setTblNmEn(String tblNmEn) {
		this.tblNmEn = tblNmEn;
		setTblNmDisplay();
	}
	public String getTblNmJp() {
		return tblNmJp;
	}
	public void setTblNmJp(String tblNmJp) {

		this.tblNmJp = tblNmJp;
		setTblNmDisplay();
	}
	/**
	 * @return the isAllDel
	 */
	public boolean isAllDel() {
		return isAllDel;
	}
	/*
	 * @param isAllDel the isAllDel to set
	 */
	public void setAllDel(boolean isAllDel) {
		this.isAllDel = isAllDel;
		setTblNmDisplay();
	}
	/**
	 * @return the tblNmDisplay
	 */
	public String getTblNmDisplay() {
		if (CmnStrUtils.isEmpty(tblNmDisplay)) {
			return this.tblNmEn;
		}
		return tblNmDisplay;
	}
	/**
	 * @param tblNmDisplay the tblNmDisplay to set
	 */
	public void setTblNmDisplay() {
		this.tblNmDisplay=this.tblNmEn;
		if (CmnStrUtils.isNotEmpty(this.tblNmJp)) {
			this.tblNmDisplay += CsjConst.SIGN_BRACKETS_M_L+this.tblNmJp+ CsjConst.SIGN_BRACKETS_M_R;
		}

	}
	public int getColCount() {
		return colCount;
	}
	public void setColCount(int colCount) {
		this.colCount = colCount;
	}
	@Override
	public String toString() {
		return "TblBase [tblNmEn=" + tblNmEn + ", tblNmJp=" + tblNmJp
				+ ", tblNmDisplay=" + tblNmDisplay + ", colCount=" + colCount
				+ ", isAllDel=" + isAllDel + "]";
	}
}

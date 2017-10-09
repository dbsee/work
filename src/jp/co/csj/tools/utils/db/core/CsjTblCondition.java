/**
 *
 */
package jp.co.csj.tools.utils.db.core;

import jp.co.csj.tools.utils.common.constant.CsjConst;

/**
 * @author Think
 *
 */
public class CsjTblCondition {

	private String tblNm = CsjConst.EMPTY;
	private String tblColNm = CsjConst.EMPTY;
	private String tblColSmall = CsjConst.EMPTY;
	private String tblColBig = CsjConst.EMPTY;

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
	 * @return the tblColNm
	 */
	public String getTblColNm() {
		return tblColNm;
	}
	/**
	 * @param tblColNm the tblColNm to set
	 */
	public void setTblColNm(String tblColNm) {
		this.tblColNm = tblColNm;
	}
	/**
	 * @return the tblColSmall
	 */
	public String getTblColSmall() {
		return tblColSmall;
	}
	/**
	 * @param tblColSmall the tblColSmall to set
	 */
	public void setTblColSmall(String tblColSmall) {
		this.tblColSmall = tblColSmall;
	}
	/**
	 * @return the tblColBig
	 */
	public String getTblColBig() {
		return tblColBig;
	}
	/**
	 * @param tblColBig the tblColBig to set
	 */
	public void setTblColBig(String tblColBig) {
		this.tblColBig = tblColBig;
	}

}

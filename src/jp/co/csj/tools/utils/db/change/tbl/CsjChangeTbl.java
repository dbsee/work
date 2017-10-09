/**
 *
 */
package jp.co.csj.tools.utils.db.change.tbl;

/**
 * @author Think
 *
 */
public class CsjChangeTbl {

	private String tblNmEn="";
	private String tblNmJp="";
	private long tblXlsCnt=0;
	private long tblDbCnt=0;
	private long recUptCnt=0;
	private long recInsCnt=0;
	private long recDelCnt=0;

	public CsjChangeTbl() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param tblNmEn
	 * @param tblNmJp
	 */
	public CsjChangeTbl(String tblNmEn, String tblNmJp) {
		this.tblNmEn = tblNmEn;
		this.tblNmJp = tblNmJp;
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the tblXlsCnt
	 */
	public long getTblXlsCnt() {
		return tblXlsCnt;
	}
	/**
	 * @param tblXlsCnt the tblXlsCnt to set
	 */
	public void setTblXlsCnt(long tblXlsCnt) {
		this.tblXlsCnt = tblXlsCnt;
	}
	/**
	 * @return the tblDbCnt
	 */
	public long getTblDbCnt() {
		return tblDbCnt;
	}
	/**
	 * @param tblDbCnt the tblDbCnt to set
	 */
	public void setTblDbCnt(long tblDbCnt) {
		this.tblDbCnt = tblDbCnt;
	}
	/**
	 * @return the recUptCnt
	 */
	public long getRecUptCnt() {
		return recUptCnt;
	}
	/**
	 * @param recUptCnt the recUptCnt to set
	 */
	public void setRecUptCnt(long recUptCnt) {
		this.recUptCnt = recUptCnt;
	}
	/**
	 * @return the recInsCnt
	 */
	public long getRecInsCnt() {
		return recInsCnt;
	}
	/**
	 * @param recInsCnt the recInsCnt to set
	 */
	public void setRecInsCnt(long recInsCnt) {
		this.recInsCnt = recInsCnt;
	}
	/**
	 * @return the recDelCnt
	 */
	public long getRecDelCnt() {
		return recDelCnt;
	}
	/**
	 * @param recDelCnt the recDelCnt to set
	 */
	public void setRecDelCnt(long recDelCnt) {
		this.recDelCnt = recDelCnt;
	}
	/**
	 * @return the tblNmEn
	 */
	public String getTblNmEn() {
		return tblNmEn;
	}
	/**
	 * @param tblNmEn the tblNmEn to set
	 */
	public void setTblNmEn(String tblNmEn) {
		this.tblNmEn = tblNmEn;
	}
	/**
	 * @return the tblNmJp
	 */
	public String getTblNmJp() {
		return tblNmJp;
	}
	/**
	 * @param tblNmJp the tblNmJp to set
	 */
	public void setTblNmJp(String tblNmJp) {
		this.tblNmJp = tblNmJp;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CsjChangeTbl [tblNmEn=" + tblNmEn + ", tblNmJp=" + tblNmJp
				+ ", tblXlsCnt=" + tblXlsCnt + ", tblDbCnt=" + tblDbCnt
				+ ", recUptCnt=" + recUptCnt + ", recInsCnt=" + recInsCnt
				+ ", recDelCnt=" + recDelCnt + "]";
	}

}

package jp.co.csj.tools.core;

public class CsjSqlType {

	private int uptCnt = 0;
	private boolean isUptIns = false;
	@Override
	public String toString() {
		return "CsjSqlType [uptCnt=" + uptCnt + ", isUptIns=" + isUptIns + "]";
	}
	public CsjSqlType(int uptCnt, boolean isUptIns) {
		this.uptCnt = uptCnt;
		this.isUptIns = isUptIns;
	}
	public int getUptCnt() {
		return uptCnt;
	}
	public void setUptCnt(int uptCnt) {
		this.uptCnt = uptCnt;
	}
	public boolean isUptIns() {
		return isUptIns;
	}
	public void setUptIns(boolean isUptIns) {
		this.isUptIns = isUptIns;
	}
	
}

/**
 *
 */
package jp.co.csj.tools.utils.db.core;

import org.mydbsee.common.CmnStrUtils;

/**
 * @author Think
 *
 */
public class TableDbItem {

	public static int SQL_DEL = 0;
	public static int SQL_UPT = 1;
	public static int SQL_INS = 2;
	public static int SQL_SEL = 3;
	public static int SQL_NONE = 4;
	/**
	 * @param key
	 * @param val
	 * @param type
	 */
	public TableDbItem(String key, String val, int type) {
		this.key = key;
		this.val = val;
		this.oldVal = val;
		this.type = type;
	}
	/**
	 * @param key
	 * @param val
	 * @param type
	 */
	public TableDbItem(String key, String val,int type,String valOld) {
		this.key = key;
		this.val = val;
		this.oldVal = valOld;
		this.type = type;
	}
	private String key;
	private String val;
	private int type;
	private String oldVal;
	private int status = SQL_NONE;
	
	public void checkUpdate() {
		if (status == SQL_DEL || status == SQL_INS || status == SQL_SEL ) {
			return;
		}
		status = CmnStrUtils.isEqual(oldVal, val) ? SQL_NONE : SQL_UPT;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the val
	 */
	public String getVal() {
		return val;
	}
	/**
	 * @param val the val to set
	 */
	public void setVal(String val) {
		this.val = val;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	public String getOldVal() {
		return oldVal;
	}
	public void setOldVal(String oldVal) {
		this.oldVal = oldVal;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "TableDbItem [key=" + key + ", val=" + val + ", type=" + type
				+ ", oldVal=" + oldVal + ", status=" + status + "]";
	}

}

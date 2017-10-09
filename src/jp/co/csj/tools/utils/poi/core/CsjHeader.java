/**
 * 
 */
package jp.co.csj.tools.utils.poi.core;

import jp.co.csj.tools.utils.common.constant.CsjConst;

/**
 * @author csj
 *
 */
public class CsjHeader {
    String headerNm = CsjConst.EMPTY;
    boolean isKey = false;
    /**
     * @return the headerNm
     */
    public String getHeaderNm() {
        return headerNm;
    }
    /**
     * 
     */
    public CsjHeader() {
    }
    /**
     * @param headerNm
     * @param isKey
     */
    public CsjHeader(String headerNm, boolean isKey) {
        this.headerNm = headerNm;
        this.isKey = isKey;
    }
    /**
     * @param headerNm the headerNm to set
     */
    public void setHeaderNm(String headerNm) {
        this.headerNm = headerNm;
    }
    /**
     * @return the isKey
     */
    public boolean isKey() {
        return isKey;
    }
    /**
     * @param isKey the isKey to set
     */
    public void setKey(boolean isKey) {
        this.isKey = isKey;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CsjHeader [headerNm=" + headerNm + ", isKey=" + isKey + "]";
    }
}

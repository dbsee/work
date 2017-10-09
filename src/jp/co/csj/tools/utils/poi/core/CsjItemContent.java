/**
 * 
 */
package jp.co.csj.tools.utils.poi.core;

import java.util.List;

/**
 * @author csj
 *
 */
public class CsjItemContent {
    private List<CsjItem> csjItemList = null;
    private boolean isHasRecord = false;
    
    /**
     * @param csjItemList
     * @param isHasRecord
     */
    public CsjItemContent(List<CsjItem> csjItemList, boolean isHasRecord) {
        this.csjItemList = csjItemList;
        this.isHasRecord = isHasRecord;
    }
    /**
     * @return the csjItemList
     */
    public List<CsjItem> getCsjItemList() {
        return csjItemList;
    }
    /**
     * @param csjItemList the csjItemList to set
     */
    public void setCsjItemList(List<CsjItem> csjItemList) {
        this.csjItemList = csjItemList;
    }
    /**
     * @return the isHasRecord
     */
    public boolean isHasRecord() {
        return isHasRecord;
    }
    /**
     * @param isHasRecord the isHasRecord to set
     */
    public void setHasRecord(boolean isHasRecord) {
        this.isHasRecord = isHasRecord;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CsjItemContent [csjItemList=" + csjItemList + ", isHasRecord=" + isHasRecord + "]";
    }
    
}

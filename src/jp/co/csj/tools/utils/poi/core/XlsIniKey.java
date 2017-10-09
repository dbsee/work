/**
 * 
 */
package jp.co.csj.tools.utils.poi.core;

import jp.co.csj.tools.utils.common.constant.CsjConst;

/**
 * @author Think
 *
 */
public class XlsIniKey {
    private String wbNm = CsjConst.EMPTY;
    private String sheetNm = CsjConst.EMPTY;
    private Pos pos = new Pos();
    private String val = CsjConst.EMPTY;
    /**
     * @return the sheetNm
     */
    public String getSheetNm() {
        return sheetNm;
    }
    /**
     * @param sheetNm the sheetNm to set
     */
    public void setSheetNm(String sheetNm) {
        this.sheetNm = sheetNm;
    }
    /**
     * @return the pos
     */
    public Pos getPos() {
        return pos;
    }
    /**
     * @param pos the pos to set
     */
    public void setPos(Pos pos) {
        this.pos = pos;
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
     * @return the wbNm
     */
    public String getWbNm() {
        return wbNm;
    }
    /**
     * @param wbNm the wbNm to set
     */
    public void setWbNm(String wbNm) {
        this.wbNm = wbNm;
    }
}

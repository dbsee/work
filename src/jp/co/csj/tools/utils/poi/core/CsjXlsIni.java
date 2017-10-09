/**
 * 
 */
package jp.co.csj.tools.utils.poi.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Think
 *
 */
public class CsjXlsIni {
    private int rowMin = 0;
    private int rowMax = 0;
    private LinkedHashMap<String,Pos> posMap = new LinkedHashMap<String, Pos>();
    private List<XlsIniKey> iniKeyList = new ArrayList<XlsIniKey>();
    private Set<String> sheetNmSet = new HashSet<String>();
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
 

    /**
     * @return the posMap
     */
    public LinkedHashMap<String, Pos> getPosMap() {
        return posMap;
    }

    /**
     * @param posMap the posMap to set
     */
    public void setPosMap(LinkedHashMap<String, Pos> posMap) {
        this.posMap = posMap;
    }


    /**
     * @return the iniKeyList
     */
    public List<XlsIniKey> getIniKeyList() {
        return iniKeyList;
    }


    /**
     * @param iniKeyList the iniKeyList to set
     */
    public void setIniKeyList(List<XlsIniKey> iniKeyList) {
        this.iniKeyList = iniKeyList;
    }


    /**
     * @return the sheetNmSet
     */
    public Set<String> getSheetNmSet() {
        return sheetNmSet;
    }


    /**
     * @param sheetNmSet the sheetNmSet to set
     */
    public void setSheetNmSet(Set<String> sheetNmSet) {
        this.sheetNmSet = sheetNmSet;
    }


    /**
     * @return the rowMin
     */
    public int getRowMin() {
        return rowMin;
    }


    /**
     * @param rowMin the rowMin to set
     */
    public void setRowMin(int rowMin) {
        this.rowMin = rowMin;
    }


    /**
     * @return the rowMax
     */
    public int getRowMax() {
        return rowMax;
    }


    /**
     * @param rowMax the rowMax to set
     */
    public void setRowMax(int rowMax) {
        this.rowMax = rowMax;
    }



}

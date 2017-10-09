/**
 * 
 */
package jp.co.csj.tools.utils.poi.core;

import java.util.LinkedHashMap;

/**
 * @author csj
 *
 */
public class CsjItemMap {
    private LinkedHashMap<String, CsjItemContent> keyMap = new LinkedHashMap<String, CsjItemContent>();
    private boolean isOldKeyMap = false;

    /**
     * @return the isOldKeyMap
     */
    public boolean isOldKeyMap() {
        return isOldKeyMap;
    }

    /**
     * @param isOldKeyMap the isOldKeyMap to set
     */
    public void setOldKeyMap(boolean isOldKeyMap) {
        this.isOldKeyMap = isOldKeyMap;
    }

    /**
     * @return the keyMap
     */
    public LinkedHashMap<String, CsjItemContent> getKeyMap() {
        return keyMap;
    }

    /**
     * @param keyMap the keyMap to set
     */
    public void setKeyMap(LinkedHashMap<String, CsjItemContent> keyMap) {
        this.keyMap = keyMap;
    }
   
}

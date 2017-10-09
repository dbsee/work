/**
 * 
 */
package jp.co.csj.tools.utils.common;

import java.util.ArrayList;
import java.util.List;

import org.mydbsee.common.CmnStrUtils;

/**
 * @author Think
 * 
 */
public class CsjCopyList {

    /**
     * 
     * @param list
     * @param expStr
     * @param flg 
     * @return
     */
    public static List<String> getCopyListString(List<String> list, String expStr) {
        List<String> retList = new ArrayList<String>();
        for (String str : list) {
            if (CmnStrUtils.isEmpty(expStr) || str.contains(expStr) == false) {
                retList.add(str);
            }

        }
        return retList;
    }
    
    /**
     * 
     * @param list
     * @param repStr
     * @param str 
     * @return
     */
    public static List<String> getCopyListStringFunReplace(List<String> list, String repStr, String str) {
        List<String> retList = new ArrayList<String>();
        for (String s : list) {
            retList.add(CmnStrUtils.funReplace(s, repStr, str));
        }
        return retList;
    }
    /**
     * 
     * @param list
     * @param repStr
     * @param str 
     * @return
     */
    public static List<String> getCopyListStringFromA2B(List<String> list, String A, String B) {
        List<String> retList = new ArrayList<String>();
        for (String s : list) {
            retList.add(CmnStrUtils.fromAtoBByTrim(s, A, B));
        }
        return retList;
    }
}

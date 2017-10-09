/**
 * 
 */
package jp.co.csj.tools.utils.file.core;

import java.util.ArrayList;
import java.util.List;

import jp.co.csj.tools.utils.common.constant.CsjConst;

/**
 * @author csj
 *
 */
public class CsjSameFilesInfo {

    private String fileNm = CsjConst.EMPTY;
    private List<String> filePaths = new ArrayList<String>();
    /**
     * @return the fileNm
     */
    public String getFileNm() {
        return fileNm;
    }
    /**
     * @param fileNm the fileNm to set
     */
    public void setFileNm(String fileNm) {
        this.fileNm = fileNm;
    }
    /**
     * @return the filePaths
     */
    public List<String> getFilePaths() {
        return filePaths;
    }
    /**
     * @param filePaths the filePaths to set
     */
    public void setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
    }
    
}

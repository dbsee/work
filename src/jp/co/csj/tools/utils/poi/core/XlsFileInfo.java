/**
 *
 */
package jp.co.csj.tools.utils.poi.core;

import jp.co.csj.tools.core.CsjLinkedMap;

/**
 * @author Think
 *
 */
public class XlsFileInfo {

	private String fileNm = "";
	private String filePath = "";
	CsjLinkedMap<String,CsjSheetInfo> sheetInfo = new CsjLinkedMap<String, CsjSheetInfo>();


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


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
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}


	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	/**
	 * @return the sheetInfo
	 */
	public CsjLinkedMap<String, CsjSheetInfo> getSheetInfo() {
		return sheetInfo;
	}


	/**
	 * @param sheetInfo the sheetInfo to set
	 */
	public void setSheetInfo(CsjLinkedMap<String, CsjSheetInfo> sheetInfo) {
		this.sheetInfo = sheetInfo;
	}

}

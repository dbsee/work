/**
 *
 */
package jp.co.csj.tools.utils.file;

import org.mydbsee.common.IConstFile;

/**
 * @author Think
 *
 */
public class CsjFileAttribute {

	private int fileType = IConstFile.IS_ERROR;
	private String fileDotPre = "";
	private String filePath = "";
	private String fileDotAfer = "";
	/**
	 * @return the fileType
	 */
	public int getFileType() {
		return fileType;
	}
	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(int fileType) {
		this.fileType = fileType;
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
	 * @return the fileDotAfer
	 */
	public String getFileDotAfer() {
		return fileDotAfer;
	}
	/**
	 * @param fileDotAfer the fileDotAfer to set
	 */
	public void setFileDotAfer(String fileDotAfer) {
		this.fileDotAfer = fileDotAfer;
	}
	/**
	 * @return the fileDotPre
	 */
	public String getFileDotPre() {
		return fileDotPre;
	}
	/**
	 * @param fileDotPre the fileDotPre to set
	 */
	public void setFileDotPre(String fileDotPre) {
		this.fileDotPre = fileDotPre;
	}

}

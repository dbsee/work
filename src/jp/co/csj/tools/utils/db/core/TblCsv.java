/**
 * 
 */
package jp.co.csj.tools.utils.db.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Think
 *
 */
public class TblCsv {

	private String csvFilePath = "";
	private String csvFileEncode = "";
	private List<Integer> csvColNums = new ArrayList<Integer>();
	private String csvSplitChar = "";
	
	private boolean isBeginAble = false;
	
	/**
	 * @return the csvFilePath
	 */
	public String getCsvFilePath() {
		return csvFilePath;
	}
	/**
	 * @param csvFilePath the csvFilePath to set
	 */
	public void setCsvFilePath(String csvFilePath) {
		this.csvFilePath = csvFilePath;
	}
	/**
	 * @return the csvFileEncode
	 */
	public String getCsvFileEncode() {
		return csvFileEncode;
	}
	/**
	 * @param csvFileEncode the csvFileEncode to set
	 */
	public void setCsvFileEncode(String csvFileEncode) {
		this.csvFileEncode = csvFileEncode;
	}
	/**
	 * @return the csvColNums
	 */
	public List<Integer> getCsvColNums() {
		return csvColNums;
	}
	/**
	 * @param csvColNums the csvColNums to set
	 */
	public void setCsvColNums(List<Integer> csvColNums) {
		this.csvColNums = csvColNums;
	}
	/**
	 * @return the csvSplitChar
	 */
	public String getCsvSplitChar() {
		return csvSplitChar;
	}
	/**
	 * @param csvSplitChar the csvSplitChar to set
	 */
	public void setCsvSplitChar(String csvSplitChar) {
		this.csvSplitChar = csvSplitChar;
	}
	
}

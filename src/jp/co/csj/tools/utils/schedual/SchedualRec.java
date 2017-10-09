/**
 * 
 */
package jp.co.csj.tools.utils.schedual;

/**
 * @author Think
 *
 */
public class SchedualRec {

	/**
	 * @param excelPath
	 * @param excelSheetName
	 */
	public SchedualRec(String excelPath, String excelSheetName) {
		this.excelPath = excelPath;
		this.excelSheetName = excelSheetName;
	}
	private boolean checked = true;
	private String excelPath;
	private String excelSheetName;

	/**
	 * 
	 */
	public SchedualRec() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the excelPath
	 */
	public String getExcelPath() {
		return excelPath;
	}

	/**
	 * @param excelPath the excelPath to set
	 */
	public void setExcelPath(String excelPath) {
		this.excelPath = excelPath;
	}

	/**
	 * @return the excelSheetName
	 */
	public String getExcelSheetName() {
		return excelSheetName;
	}

	/**
	 * @param excelSheetName the excelSheetName to set
	 */
	public void setExcelSheetName(String excelSheetName) {
		this.excelSheetName = excelSheetName;
	}



	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SchedualRec [checked=" + checked + ", excelPath=" + excelPath
				+ ", excelSheetName=" + excelSheetName + "]";
	}

}

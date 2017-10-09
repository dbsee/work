/**
 *
 */
package jp.co.csj.tools.utils.poi.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.mydbsee.common.CmnLog;
import org.mydbsee.common.CmnPoiUtils;

import jp.co.csj.tools.utils.common.CsjPath;
import jp.co.csj.tools.utils.common.CsjProcess;
import jp.co.csj.tools.utils.common.constant.CsjConst;

/**
 * @author csj
 *
 */
public class CsjDbCellStyle {

	public static int CS_DIFF_REC = 1;
	public static int CS_DATA_REC = 1;
	// DBから、XLSファイルに出力する。
	CellStyle csTable = null;
	CellStyle csTableBolder = null;
	CellStyle csTitleThin = null;
	CellStyle csTitleKey = null;
	CellStyle csRecordDate = null;
	CellStyle csRecordNum = null;
	CellStyle csRecordStr = null;
	CellStyle csRecordBlank = null;

	CellStyle csSignBolder = null;
	CellStyle csDiff_17_0 = null;
	CellStyle csDiff_18_0 = null;
	CellStyle csGreen = null;
	CellStyle csLightGreen = null;
	CellStyle csError = null;
	CellStyle csLink = null;


	Font ftComment = null;
	Font ftRed = null;
	Font ftBolder = null;
	CellStyle csDiff_19_0 = null;

	public static String FORMAT_DATE = "yyyy/m/d h:mm:ss";
	public static String FORMAT_NUMBER = "0.00";
	public static String FORMAT_STR = "@";

	CellStyle csDiff_10_0 = null;
	CellStyle csDiff_11_0 = null;
	CellStyle csDiff_11_1 = null;
	CellStyle csDiff_11_2 = null;
	CellStyle csDiff_11_3 = null;
	CellStyle csDiff_11_4 = null;
	CellStyle csDiff_11_5 = null;
	CellStyle csDiff_11_6 = null;

	CellStyle csDiff_12_0 = null;
	CellStyle csDiff_12_1 = null;
	CellStyle csDiff_12_2 = null;
	CellStyle csDiff_12_3 = null;
	CellStyle csDiff_12_4 = null;
	CellStyle csDiff_12_5 = null;
	CellStyle csDiff_12_6 = null;
	
	CellStyle csDiff_13_0 = null;
	CellStyle csDiff_13_2 = null;
	CellStyle csDiff_13_3 = null;
	CellStyle csDiff_13_4 = null;
	CellStyle csDiff_13_5 = null;
	CellStyle csDiff_13_6 = null;
	
	private static Map<String,Map<String,CsjCellInfo>> s_formatCellStyleMap = new HashMap<String,Map<String,CsjCellInfo>>();
	static {
		getDefaultCellStyle();
	}

	public static void getDefaultCellStyle() {
		try {
			s_formatCellStyleMap.clear();
			s_formatCellStyleMap.put(CsjConst.EXCEL_DOT_XLS_1997, CmnPoiUtils.getSheetContents(CsjProcess.s_pj_path + CsjPath.s_file_db_cellStyle + CsjConst.EXCEL_DOT_XLS_1997, "STYLE_PROPERTY", false).getCsjCellPosInfoMap());
			s_formatCellStyleMap.put(CsjConst.EXCEL_DOT_XLSX_2007, CmnPoiUtils.getSheetContents(CsjProcess.s_pj_path + CsjPath.s_file_db_cellStyle + CsjConst.EXCEL_DOT_XLSX_2007, "STYLE_PROPERTY", false).getCsjCellPosInfoMap());
		
		} catch (Throwable e) {
			CmnLog.logger.error(CsjProcess.s_pj_path + CsjPath.s_file_db_cellStyle + "  error");
		}
	}
	/**
	 * @param wb
	 * @param i
	 */
	public CsjDbCellStyle(Workbook wb, String excelType, int diffType) {
		DataFormat wbFormat = wb.createDataFormat();

		Map<String,CsjCellInfo> formatCellStyleMap = s_formatCellStyleMap.get(excelType);
		csLink = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("29_0"));

		
		csTitleKey = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("1_1"));
		csTitleThin = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("1_2"));
		csTable = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("0_0"));
		csTableBolder = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("0_0"));
		
		csRecordDate = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("5_6"));
		csRecordDate.setDataFormat(wbFormat.getFormat(FORMAT_DATE));
		csRecordNum = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("5_1"));
		csRecordNum.setDataFormat(HSSFDataFormat.getBuiltinFormat(FORMAT_NUMBER));

		csRecordStr = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("5_2"));
		csRecordStr.setDataFormat(wbFormat.getFormat(FORMAT_STR));

		csRecordBlank = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("5_3"));
		csRecordBlank.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		ftComment = wb.createFont();
		ftComment.setFontName("Arial");
		ftComment.setFontHeightInPoints((short)10);
		ftComment.setBoldweight(Font.BOLDWEIGHT_BOLD);
		ftComment.setColor(HSSFColor.RED.index);
		if (CS_DIFF_REC == diffType) {
			setDiffHeadCs(wb, wbFormat, formatCellStyleMap);
		}

		
		csSignBolder = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("1_0"));
		csSignBolder.setDataFormat(wbFormat.getFormat(FORMAT_STR));

		csError = wb.createCellStyle();
		csError.setBorderLeft(CellStyle.BORDER_THIN);
		csError.setBorderRight(CellStyle.BORDER_THIN);
		csError.setBorderTop(CellStyle.BORDER_THIN);
		csError.setBorderBottom(CellStyle.BORDER_THIN);
		csError.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
		csError.setFillPattern(CellStyle.SOLID_FOREGROUND);

		csGreen = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("17_0"));
		csLightGreen = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("18_0"));



	}
	private void setDiffHeadCs(Workbook wb, DataFormat wbFormat,
			Map<String, CsjCellInfo> formatCellStyleMap) {
		csDiff_18_0 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("18_0"));
		csDiff_17_0 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("17_0"));


		ftRed = wb.createFont();
		ftRed.setFontHeightInPoints((short)10);
		ftRed.setBoldweight(Font.BOLDWEIGHT_BOLD);
		ftRed.setColor(HSSFColor.RED.index);
		
		
		csDiff_19_0 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("19_0"));
		
		csDiff_10_0 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("10_0"));
		csDiff_10_0.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		csDiff_11_0 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("11_0"));
		csDiff_11_0.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		csDiff_11_1 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("11_1"));
		csDiff_11_1.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		csDiff_11_2 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("11_2"));
		csDiff_11_2.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		csDiff_11_3 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("11_3"));
		csDiff_11_3.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		csDiff_11_4 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("11_4"));
		csDiff_11_4.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		csDiff_11_5 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("11_5"));
		csDiff_11_5.setDataFormat(wbFormat.getFormat(FORMAT_STR));
				
		csDiff_11_6 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("11_6"));
		csDiff_11_6.setDataFormat(wbFormat.getFormat(FORMAT_STR));

		
		csDiff_12_0 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("12_0"));
		csDiff_12_0.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		csDiff_12_1 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("12_1"));
		csDiff_12_1.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		csDiff_12_2 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("12_2"));
		csDiff_12_2.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		csDiff_12_3 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("12_3"));
		csDiff_12_3.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		csDiff_12_4 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("12_4"));
		csDiff_12_4.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		csDiff_12_5 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("12_5"));
		csDiff_12_5.setDataFormat(wbFormat.getFormat(FORMAT_STR));
				
		csDiff_12_6 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("12_6"));
		csDiff_12_6.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		csDiff_13_0 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("13_0"));
		csDiff_13_0.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		csDiff_13_2 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("13_2"));
		csDiff_13_2.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		csDiff_13_3 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("13_3"));
		csDiff_13_3.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		csDiff_13_4 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("13_4"));
		csDiff_13_4.setDataFormat(wbFormat.getFormat(FORMAT_STR));
		
		csDiff_13_5 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("13_5"));
		csDiff_13_5.setDataFormat(wbFormat.getFormat(FORMAT_STR));
				
		csDiff_13_6 = CmnPoiUtils.createCellStyle(wb, formatCellStyleMap.get("13_6"));
		csDiff_13_6.setDataFormat(wbFormat.getFormat(FORMAT_STR));
	}

	/**
	 * @return the csRecordDate
	 */
	public CellStyle getCsRecordDate() {
		return csRecordDate;
	}

	/**
	 * @param csRecordDate
	 *            the csRecordDate to set
	 */
	public void setCsRecordDate(CellStyle csRecordDate) {
		this.csRecordDate = csRecordDate;
	}

	/**
	 * @return the csRecordStr
	 */
	public CellStyle getCsRecordStr() {
		return csRecordStr;
	}

	/**
	 * @param csRecordStr
	 *            the csRecordStr to set
	 */
	public void setCsRecordStr(CellStyle csRecordStr) {
		this.csRecordStr = csRecordStr;
	}

	/**
	 * @return the csRecordNum
	 */
	public CellStyle getCsRecordNum() {
		return csRecordNum;
	}

	/**
	 * @param csRecordNum
	 *            the csRecordNum to set
	 */
	public void setCsRecordNum(CellStyle csRecordNum) {
		this.csRecordNum = csRecordNum;
	}

	/**
	 * @return the csError
	 */
	public CellStyle getCsError() {
		return csError;
	}

	/**
	 * @param csError
	 *            the csError to set
	 */
	public void setCsError(CellStyle csError) {
		this.csError = csError;
	}

	/**
	 * @return the csTitleThin
	 */
	public CellStyle getCsTitleThin() {
		return csTitleThin;
	}

	/**
	 * @param csTitleThin
	 *            the csTitleThin to set
	 */
	public void setCsTitleThin(CellStyle csTitleThin) {
		this.csTitleThin = csTitleThin;
	}

	/**
	 * @return the csTable
	 */
	public CellStyle getCsTable() {
		return csTable;
	}

	/**
	 * @param csTable
	 *            the csTable to set
	 */
	public void setCsTable(CellStyle csTable) {
		this.csTable = csTable;
	}

	/**
	 * @return the csRecordBlank
	 */
	public CellStyle getCsRecordBlank() {
		return csRecordBlank;
	}

	/**
	 * @param csRecordBlank
	 *            the csRecordBlank to set
	 */
	public void setCsRecordBlank(CellStyle csRecordBlank) {
		this.csRecordBlank = csRecordBlank;
	}

	/**
	 * @return the ftComment
	 */
	public Font getFtComment() {
		return ftComment;
	}

	/**
	 * @param ftComment the ftComment to set
	 */
	public void setFtComment(Font ftComment) {
		this.ftComment = ftComment;
	}

	/**
	 * @return the ftRed
	 */
	public Font getFtRed() {
		return ftRed;
	}

	/**
	 * @param ftRed the ftRed to set
	 */
	public void setFtRed(Font ftRed) {
		this.ftRed = ftRed;
	}

	/**
	 * @return the csYellow
	 */
	public CellStyle getCsDiff_18_0() {
		return csDiff_18_0;
	}

	/**
	 * @param csDiff_18_0 the csYellow to set
	 */
	public void setCsDiff_18_0(CellStyle csDiff_18_0) {
		this.csDiff_18_0 = csDiff_18_0;
	}

	/**
	 * @return the csDelete
	 */
	public CellStyle getCsDiff_19_0() {
		return csDiff_19_0;
	}

	/**
	 * @param csDelete the csDelete to set
	 */
	public void setCsDiff_19_0(CellStyle csDiff_19_0) {
		this.csDiff_19_0 = csDiff_19_0;
	}

	/**
	 * @return the csLime
	 */
	public CellStyle getCsDiff_17_0() {
		return csDiff_17_0;
	}

	/**
	 * @param csLime the csLime to set
	 */
	public void setCsDiff_17_0(CellStyle csDiff_17_0) {
		this.csDiff_17_0 = csDiff_17_0;
	}

	/**
	 * @return the csGreen
	 */
	public CellStyle getCsGreen() {
		return csGreen;
	}

	/**
	 * @param csGreen the csGreen to set
	 */
	public void setCsGreen(CellStyle csGreen) {
		this.csGreen = csGreen;
	}

	/**
	 * @return the csLightGreen
	 */
	public CellStyle getCsLightGreen() {
		return csLightGreen;
	}

	/**
	 * @param csLightGreen the csLightGreen to set
	 */
	public void setCsLightGreen(CellStyle csLightGreen) {
		this.csLightGreen = csLightGreen;
	}

	/**
	 * @return the csTableBolder
	 */
	public CellStyle getCsTableBolder() {
		return csTableBolder;
	}

	/**
	 * @param csTableBolder the csTableBolder to set
	 */
	public void setCsTableBolder(CellStyle csTableBolder) {
		this.csTableBolder = csTableBolder;
	}

	/**
	 * @return the csSignBolder
	 */
	public CellStyle getCsSignBolder() {
		return csSignBolder;
	}

	/**
	 * @param csSignBolder the csSignBolder to set
	 */
	public void setCsSignBolder(CellStyle csSignBolder) {
		this.csSignBolder = csSignBolder;
	}

	/**
	 * @return the csTitleBoderSync
	 */
	public CellStyle getCsTitleKey() {
		return csTitleKey;
	}

	/**
	 * @param csTitleKey the csTitleKey to set
	 */
	public void setCsTitleKey(CellStyle csTitleKey) {
		this.csTitleKey = csTitleKey;
	}
	public CellStyle getCsLink() {
		return csLink;
	}

	public void setCsLink(CellStyle csLink) {
		this.csLink = csLink;
	}
	public CellStyle getCsDiff_10_0() {
		return csDiff_10_0;
	}
	public void setCsDiff_10_0(CellStyle csDiff_10_0) {
		this.csDiff_10_0 = csDiff_10_0;
	}
	public Font getFtBolder() {
		return ftBolder;
	}
	public CellStyle getCsDiff_11_0() {
		return csDiff_11_0;
	}
	public CellStyle getCsDiff_11_1() {
		return csDiff_11_1;
	}
	public CellStyle getCsDiff_11_2() {
		return csDiff_11_2;
	}
	public CellStyle getCsDiff_11_3() {
		return csDiff_11_3;
	}
	public CellStyle getCsDiff_11_4() {
		return csDiff_11_4;
	}
	public CellStyle getCsDiff_11_5() {
		return csDiff_11_5;
	}
	public CellStyle getCsDiff_11_6() {
		return csDiff_11_6;
	}
	public CellStyle getCsDiff_12_0() {
		return csDiff_12_0;
	}
	public CellStyle getCsDiff_12_1() {
		return csDiff_12_1;
	}
	public CellStyle getCsDiff_12_2() {
		return csDiff_12_2;
	}
	public CellStyle getCsDiff_12_3() {
		return csDiff_12_3;
	}
	public CellStyle getCsDiff_12_4() {
		return csDiff_12_4;
	}
	public CellStyle getCsDiff_12_5() {
		return csDiff_12_5;
	}
	public CellStyle getCsDiff_12_6() {
		return csDiff_12_6;
	}
	public CellStyle getCsDiff_13_0() {
		return csDiff_13_0;
	}
	public CellStyle getCsDiff_13_2() {
		return csDiff_13_2;
	}
	public CellStyle getCsDiff_13_3() {
		return csDiff_13_3;
	}
	public CellStyle getCsDiff_13_4() {
		return csDiff_13_4;
	}
	public CellStyle getCsDiff_13_5() {
		return csDiff_13_5;
	}
	public CellStyle getCsDiff_13_6() {
		return csDiff_13_6;
	}

}

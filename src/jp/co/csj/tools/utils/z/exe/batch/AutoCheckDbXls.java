/**
 *
 */
package jp.co.csj.tools.utils.z.exe.batch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mydbsee.common.CmnDateUtil;
import org.mydbsee.common.CmnLog;
import org.mydbsee.common.CmnLog5j;
import org.mydbsee.common.CmnPoiUtils;
import org.mydbsee.common.CmnStrUtils;
import org.mydbsee.common.IConstFile;

import jp.co.csj.tools.core.CsjLinkedMap;
import jp.co.csj.tools.utils.common.CsjPath;
import jp.co.csj.tools.utils.common.CsjProcess;
import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.db.change.tbl.CsjChangeTbl;
import jp.co.csj.tools.utils.db.change.tbl.CsjChangeTblInfo;
import jp.co.csj.tools.utils.db.core.CsjTblSelectSql;
import jp.co.csj.tools.utils.db.core.DbInfo;
import jp.co.csj.tools.utils.db.core.SheetTblsInfo;
import jp.co.csj.tools.utils.db.core.TblPara;
import jp.co.csj.tools.utils.db.core.XlsRecord;
import jp.co.csj.tools.utils.db.core.XlsTblInfo;
import jp.co.csj.tools.utils.db.core.XlsTblPara;
import jp.co.csj.tools.utils.msg.dbtools.CsjDbToolsMsg;
import jp.co.csj.tools.utils.poi.core.CsjCellInfo;
import jp.co.csj.tools.utils.poi.core.CsjDbCellStyle;
import jp.co.csj.tools.utils.poi.core.CsjHeader;
import jp.co.csj.tools.utils.poi.core.CsjItem;
import jp.co.csj.tools.utils.poi.core.CsjItemContent;
import jp.co.csj.tools.utils.xml.dbtools.XmlDb;
import jp.co.csj.tools.utils.xml.dbtools.XmlDbXlsInfoAll;
import jp.co.csj.tools.utils.xml.dbtools.XmlInfoXls;

/**
 * @author Think
 *
 */
public class AutoCheckDbXls {

	/**
	 * @param dbXlsInfo
	 * @param newPath
	 * @param oldPath
	 * @throws Throwable
	 */
	public static void run(XmlDbXlsInfoAll xmlDbXlsInfoAll, String oldPath, String newPath) throws Throwable {
		try {

			CmnLog.logger.debug("check two xls begin...");
			LinkedHashMap<String, TblPara> oldTblParaMap = exeXlsToDb(oldPath);
			LinkedHashMap<String, TblPara> newTblParaMap = exeXlsToDb(newPath);
			LinkedHashMap<String, Integer> oldParaPos = new LinkedHashMap<String, Integer>();
			LinkedHashMap<String, Integer> newParaPos = new LinkedHashMap<String, Integer>();

			setNewTblsParasForChangeType(oldTblParaMap, newTblParaMap, oldParaPos, newParaPos);

			HashMap<String,String> indexMap = new HashMap<String,String>();

			FileInputStream newInFs = null;
			FileOutputStream fileOut = null;

			
			newInFs = new FileInputStream(newPath);
			
			Workbook newWb = null;
			CsjDbCellStyle cellStyle = null;
			if (CmnStrUtils.isEndByIgnor(newPath, CsjConst.EXCEL_DOT_XLS_1997)) {
				newWb = new HSSFWorkbook(newInFs);
				cellStyle = new CsjDbCellStyle(newWb,CsjConst.EXCEL_DOT_XLS_1997,CsjDbCellStyle.CS_DATA_REC);
			} else {
				newWb = new XSSFWorkbook(newInFs);
				cellStyle = new CsjDbCellStyle(newWb,CsjConst.EXCEL_DOT_XLSX_2007,CsjDbCellStyle.CS_DATA_REC);
			}
			
			Sheet newSheet = newWb.getSheetAt(0);
			String tblNm = newSheet.getSheetName();

			File oldFile = new File(oldPath);
			FileInputStream oldInFs = new FileInputStream(oldFile);
			Workbook oldWb = null;
			if (CmnStrUtils.isEndByIgnor(oldPath, CsjConst.EXCEL_DOT_XLS_1997)) {
				oldWb = new HSSFWorkbook(oldInFs);
			} else {
				oldWb = new XSSFWorkbook(oldInFs);
			}
			
			Sheet oldSheet = oldWb.getSheetAt(0);

			boolean isBeginSetVal = false;
			for (Row row : oldSheet) {
				String cell0Str = CmnPoiUtils.getCellContent(row, 0, false);
				if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000005).equals(cell0Str)) {
					isBeginSetVal = true;
				} else if (isBeginSetVal) {

					for (Entry<String, TblPara> entry : newTblParaMap.entrySet()) {

						CmnPoiUtils.setCellValueWithCs(newSheet, row.getRowNum(), 0,String.valueOf(row.getRowNum() - 4), cellStyle.getCsRecordStr());

						String paraNm = entry.getKey();
						TblPara newTblPara = entry.getValue();

						TblPara oldTblPara = oldTblParaMap.get(paraNm);
						XlsTblPara xlsTblPara = new XlsTblPara(newTblPara);

						int newColIndex = newParaPos.get(paraNm);

						String oldContent = CsjConst.EMPTY;
						if (TblPara.CHANGED_TYPE_NO_CHANGE == newTblPara.getParaChangedType()) {
							oldContent = CmnPoiUtils.getCellContent(row, oldParaPos.get(paraNm), false);
							if (DbInfo.TABLE_COL_TYPE_DATE == newTblPara.getType()) {
								oldContent = oldContent.replaceAll("-", "/");
							}

						} else if (TblPara.CHANGED_TYPE_CAN_NOT_NULL == newTblPara.getParaChangedType()) {
							oldContent = CmnPoiUtils.getCellContent(row, oldParaPos.get(paraNm), false);
							if (CmnStrUtils.isEmpty(oldContent)) {
								oldContent = getNewContent(indexMap, newTblPara);
							}
						} else if (TblPara.CHANGED_TYPE_LENGTH_MINUS == newTblPara.getParaChangedType()) {
							oldContent = CmnPoiUtils.getCellContent(row, oldParaPos.get(paraNm), false);
							if (CmnStrUtils.isNotEmpty(oldContent) && newTblPara.getParaLen() < oldTblPara.getParaLen()) {
								oldContent = CmnStrUtils.getStrForLength(oldContent,newTblPara.getParaLen());
							}
						} else if (TblPara.CHANGED_TYPE_UPD_TYPE == newTblPara.getParaChangedType()) {

							if (newTblPara.isCanNull()) {
								oldContent = CsjConst.EMPTY;
							} else {
								oldContent = getNewContent(indexMap, newTblPara);
							}
						}
						xlsTblPara.setParaValOri(oldContent);

						CmnPoiUtils.setCellValueWithCs(newSheet, row.getRowNum(), newColIndex, xlsTblPara, cellStyle);
					}
				}
			}

			
			String filePath = CsjPath.s_path_auto_pj_AutoDB + "excel_to_excel"+ CsjPath.s_current_date_end + CsjProcess.s_f_s;
			xmlDbXlsInfoAll.getDbInfo().setCompareStructResultPath(filePath);
			File f = new File(filePath);
			f.mkdirs();
			fileOut = new FileOutputStream(filePath + tblNm + xmlDbXlsInfoAll.getXmlInfoXls().getExcelType());
			newWb.write(fileOut);
			oldWb.close();
			fileOut.close();
			newInFs.close();
			oldInFs.close();
		} catch (Throwable e) {
			throw e;
		}

	}

	/**
	 * @param indexMap
	 * @param newTblPara
	 * @return
	 * @throws Throwable
	 */
	public static String getNewContent(HashMap<String, String> indexMap, TblPara newTblPara) throws Throwable {
		String oldContent;
		if (indexMap.containsKey(newTblPara.getParaNmEn())) {

			long index = CmnStrUtils.getLongVal(indexMap.get(newTblPara.getParaNmEn()));
			if (DbInfo.TABLE_COL_TYPE_DATE == newTblPara.getType()) {

				oldContent = CmnDateUtil.getFormatDateAdd(
						CsjProcess.s_default_date,
						(int) (++index), CsjConst.YYYY_MM_DD_SLASH);
				indexMap.put(newTblPara.getParaNmEn(), String.valueOf(index));
			} else {

				oldContent = String.valueOf(++index);
				//if (DbInfo.TABLE_COL_TYPE_STR == newTblPara.getType()) {
					if (oldContent.getBytes(IConstFile.ENCODE_SHIFT_JIS).length> newTblPara.getParaLen()) {
						oldContent = String.valueOf(1);
					}
				//}
				indexMap.put(newTblPara.getParaNmEn(),oldContent);
			}

		} else {
			if (DbInfo.TABLE_COL_TYPE_DATE == newTblPara.getType()) {

				oldContent = CmnDateUtil.getFormatDateAdd(
						CsjProcess.s_default_date,
						(int) (0), CsjConst.YYYY_MM_DD_SLASH);
				indexMap.put(newTblPara.getParaNmEn(), String.valueOf(1));
			} else {
				oldContent = String.valueOf(1);
				indexMap.put(newTblPara.getParaNmEn(), String.valueOf(1));
			}
		}
		return oldContent;
	}

	/**
	 * @param oldParaMap
	 * @param newParaMap
	 * @param newParaPos
	 * @param oldParaPos
	 * @return
	 */
	public static void setNewTblsParasForChangeType(LinkedHashMap<String, TblPara> oldParaMap,
			LinkedHashMap<String, TblPara> newParaMap,
			LinkedHashMap<String, Integer> oldParaPos,
			LinkedHashMap<String, Integer> newParaPos) {

		for (Entry<String, TblPara> entryPara : newParaMap.entrySet()) {
			String paraNmEn = entryPara.getKey();
			TblPara newTblPara = entryPara.getValue();
			newParaPos.put(paraNmEn, newTblPara.getParaPos());
			if (oldParaMap.containsKey(paraNmEn)) {

				TblPara oldTblPara = oldParaMap.get(paraNmEn);
				oldParaPos.put(oldTblPara.getParaNmEn(), oldTblPara.getParaPos());
				if (oldTblPara.isPkey() == false && newTblPara.isPkey() == true) {
					newTblPara.setParaChangedType(TblPara.CHANGED_TYPE_PK);
				} else if (oldTblPara.isCanNull() == true && newTblPara.isCanNull() == false) {
					newTblPara.setParaChangedType(TblPara.CHANGED_TYPE_CAN_NOT_NULL);
				} else {
					if (oldTblPara.getType() == newTblPara.getType()) {
						if (oldTblPara.getParaLen() > newTblPara.getParaLen()
								&& DbInfo.TABLE_COL_TYPE_STR == newTblPara.getType()) {
							newTblPara.setParaChangedType(TblPara.CHANGED_TYPE_LENGTH_MINUS);
						} else {
							newTblPara.setParaChangedType(TblPara.CHANGED_TYPE_NO_CHANGE);
						}
					} else {
						newTblPara.setParaChangedType(TblPara.CHANGED_TYPE_UPD_TYPE);
					}
				}

			} else {
				newTblPara.setParaChangedType(TblPara.CHANGED_TYPE_UPD_TYPE);
			}
		}
	}

	private static LinkedHashMap<String, TblPara> exeXlsToDb(String filePath) throws Throwable {

		LinkedHashMap<Integer, TblPara> paraPosInfoMap = new LinkedHashMap<Integer, TblPara>();
		LinkedHashMap<String, TblPara> tblParaMap = new LinkedHashMap<String, TblPara>();
		File file = new File(filePath);
		Workbook wb = null;

		
		FileInputStream fs = new FileInputStream(file);
		if (CmnStrUtils.isEndByIgnor(filePath, CsjConst.EXCEL_DOT_XLS_1997)) {
			 wb = new HSSFWorkbook(fs);	}
		else if (CmnStrUtils.isEndByIgnor(filePath, CsjConst.EXCEL_DOT_XLSX_2007)) {
			wb = new XSSFWorkbook(fs);
		}
		
		Sheet sheet = wb.getSheetAt(0);
		for (Row row : sheet) {

			String cell0Str = CmnPoiUtils.getCellContent(row, 0, false);

			String sign = CmnPoiUtils.getCellContent(sheet, row.getRowNum(), 0, false);
			if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000003).equals(sign)) {
				for (Cell cell : row) {

					CsjCellInfo csjCellInfo = CmnPoiUtils.getCellInfo(cell);
					csjCellInfo.setContent(CmnStrUtils.lrTrimSpace(csjCellInfo.getContent()));
					int colPos = cell.getColumnIndex();
					if (colPos != 0) {
						TblPara para = new TblPara();
						para.setParaNmEn(csjCellInfo.getContent());
						if (csjCellInfo.getFont().getBoldweight() == Font.BOLDWEIGHT_BOLD) {
							para.setPkey(true);
						} else {
							para.setPkey(false);
						}
						para.setParaPos(colPos);
						paraPosInfoMap.put(colPos, para);
					}
				}
			} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000004).equals(sign)) {
				for (Cell cell : row) {

					CsjCellInfo csjCellInfo = CmnPoiUtils.getCellInfo( cell);
					csjCellInfo.setContent(CmnStrUtils.lrTrimSpace(csjCellInfo.getContent()));
					int colPos = cell.getColumnIndex();
					if (colPos != 0 && paraPosInfoMap.containsKey(colPos)) {
						TblPara para = paraPosInfoMap.get(colPos);
						para.setParaTypeWithlen("",csjCellInfo.getContent());

					}
				}
			} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000005).equals(sign)) {
				for (Cell cell : row) {

					CsjCellInfo csjCellInfo = CmnPoiUtils.getCellInfo( cell);
					csjCellInfo.setContent(CmnStrUtils.lrTrimSpace(csjCellInfo.getContent()));
					int colPos = cell.getColumnIndex();
					if (colPos != 0 && paraPosInfoMap.containsKey(colPos)) {
						TblPara para = paraPosInfoMap.get(colPos);
						para.setCanNull("Y".equals(CmnStrUtils.toLowOrUpStr(csjCellInfo.getContent())));
					}
				}
				for (Entry<Integer, TblPara> entry : paraPosInfoMap.entrySet()) {
					TblPara tblPara = entry.getValue();
					tblParaMap.put(tblPara.getParaNmEn(), tblPara);
				}
				break;
			}
		}
		return tblParaMap;
	}

	/**
	 * @param dataDifSheet
	 * @param oldMap
	 * @throws Throwable
	 */
	private static void setTitle(HSSFSheet st, LinkedHashMap<Integer, CsjHeader> headerMap, CsjDbCellStyle cellStyle) throws Throwable {

		st.createFreezePane(1, 1);
		for(Entry<Integer, CsjHeader> entry :headerMap.entrySet() ) {
			int key = entry.getKey();
			CsjHeader csjHeader = entry.getValue();
			if (csjHeader.isKey()) {
				CmnPoiUtils.setCellValueWithCs(st, key, 0, csjHeader.getHeaderNm(), cellStyle.getCsTitleKey());
			} else {
				CmnPoiUtils.setCellValueWithCs(st, key, 0, csjHeader.getHeaderNm(), cellStyle.getCsTitleThin());
			}

		}
	}

	/**
	 * @param headerMap
	 * @param oldSheet
	 * @param oldMaxNum
	 * @param itemOldList
	 * @param oldKeyMap
	 * @throws Throwable 
	 */
	public static void setKeyMap(LinkedHashMap<Integer, CsjHeader> headerMap,
			HSSFSheet sheet,
			LinkedHashMap<String, CsjItemContent> oldKeyMap) throws Throwable {

		long oldMaxNum = sheet.getLastRowNum();
		for (int i = 1; i <= oldMaxNum; i++) {
			List<CsjItem> itemList = new ArrayList<CsjItem>();
			StringBuffer sbKey = new StringBuffer();
			for (Entry<Integer, CsjHeader> entry : headerMap.entrySet()) {
				int colPos = entry.getKey();
				CsjHeader csjHeader = entry.getValue();
				String str = CmnPoiUtils.getCellContent(sheet.getRow(i), colPos, false);
				if (csjHeader.isKey()) {
					sbKey.append(str);
					sbKey.append(CsjConst.SIGN_SEPARATOR_3);
				}
				itemList.add(new CsjItem(str));
			}
			oldKeyMap.put(sbKey.toString(), new CsjItemContent(itemList, false));
		}
	}

	/**
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static LinkedHashMap<Integer, CsjHeader> getHeaderMap(String filePath) throws Throwable{
		FileInputStream fin = new FileInputStream(filePath);
		LinkedHashMap<Integer, CsjHeader> oldMap = new LinkedHashMap<Integer, CsjHeader>();
		HSSFWorkbook wb = new HSSFWorkbook(fin);
		HSSFSheet sheet = wb.getSheetAt(0);

		for (Row row : sheet) {
			if (row.getRowNum() == 0) {
				for (Cell cell : row) {

					CsjCellInfo cellInfo = CmnPoiUtils.getCellInfo( cell);
					oldMap.put(cell.getColumnIndex(),
							new CsjHeader(cellInfo.getContent(),
									cellInfo.getFont().getBoldweight() == Font.BOLDWEIGHT_BOLD));
				}
			}
			break;
		}

		fin.close();
		return oldMap;
	}

	/**
	 * @param isOnlyChanged 
	 * @param s_Db_Info
	 * @throws Throwable
	 */
	public static void runCheckDbAndXls(XmlDbXlsInfoAll xmlDbXlsInfoAll,boolean isOnlyChanged, CsjChangeTblInfo csjChangeTblInfo) throws Throwable {
		try {
			CmnLog5j.initLog5j(CsjPath.s_path_auto_pj_AutoDB_log,"log.txt", xmlDbXlsInfoAll.getInfoSql().getEncode());
			exeXlsToDb(xmlDbXlsInfoAll,isOnlyChanged,csjChangeTblInfo);
		} catch (Throwable e) {
			CmnLog.logger.error(e.getMessage());
			throw e;
		} finally {
			try {
				CmnLog5j.closeLog5j();
			} catch (Throwable e) {
				CmnLog.logger.error(e.getMessage());
			}
		}
	}

	private static void exeXlsToDb(XmlDbXlsInfoAll xmlDbXlsInfoAll,boolean isOnlyChanged, CsjChangeTblInfo csjChangeTblInfo) throws Throwable {

		FileInputStream fileIn =null;
		FileOutputStream fileOut = null;

		try {
			String s_sheetNm = xmlDbXlsInfoAll.getDbInfo().getSycnExcelSheetName();
			XmlDb xmlDb = xmlDbXlsInfoAll.getCurrentXmlDb();
			
			File f = new File(xmlDb.getXlsPath());
			f.mkdirs();
			
			File file = new File(CsjPath.s_path_auto_pj_AutoDB_xls_to_db);
			fileIn = new FileInputStream(file);
			
			Workbook wb = null;
			String excelType = CsjConst.EXCEL_DOT_XLS_1997;
			if (CmnStrUtils.isEndByIgnor(CsjPath.s_path_auto_pj_AutoDB_xls_to_db, CsjConst.EXCEL_DOT_XLS_1997)) {
				 wb = new HSSFWorkbook(fileIn);
			} else if (CmnStrUtils.isEndByIgnor(CsjPath.s_path_auto_pj_AutoDB_xls_to_db, CsjConst.EXCEL_DOT_XLSX_2007)) {
				 wb = new XSSFWorkbook(fileIn);
				 excelType = CsjConst.EXCEL_DOT_XLSX_2007;
			}
			CsjDbCellStyle cellStyle = new CsjDbCellStyle(wb,excelType,CsjDbCellStyle.CS_DIFF_REC);

			long sumTblXlsCnt=0;
			long sumTblDbCnt=0;
			long sumRecUptCnt=0;
			long sumRecInsCnt=0;
			long sumRecsDelCnt=0;

//			if (CsjStrUtils.isNotEmpty(s_sheetNm)) {
				Sheet sheet = CmnPoiUtils.getOneSheetByNm(wb,s_sheetNm);
				
				makeSheet(wb,sheet,cellStyle,isOnlyChanged,xmlDbXlsInfoAll,csjChangeTblInfo,file);
				sumTblXlsCnt+=csjChangeTblInfo.getSumTblXlsCnt();
				sumTblDbCnt+=csjChangeTblInfo.getSumTblDbCnt();
				sumRecInsCnt+=csjChangeTblInfo.getSumRecInsCnt();
				sumRecsDelCnt+=csjChangeTblInfo.getSumRecsDelCnt();
				sumRecUptCnt+=csjChangeTblInfo.getSumRecUptCnt();
//
//			} else {
//				for (int i = 0; i < wb.getNumberOfSheets(); i++) {
//					Sheet sheet = wb.getSheetAt(i);
//					CsjChangeTblInfo csjChangeTblInfo = new CsjChangeTblInfo();
//					csjChangeTblInfo.setWhere(isWhere);
//					makeSheet(wb,sheet,cellStyle,isWhere,isOnlyChanged,xmlDbXlsInfoAll,csjChangeTblInfo,file);
//					sumTblXlsCnt+=csjChangeTblInfo.getSumTblXlsCnt();
//					sumTblDbCnt+=csjChangeTblInfo.getSumTblDbCnt();
//					sumRecInsCnt+=csjChangeTblInfo.getSumRecInsCnt();
//					sumRecsDelCnt+=csjChangeTblInfo.getSumRecsDelCnt();
//					sumRecUptCnt+=csjChangeTblInfo.getSumRecUptCnt();
//					csjChangeTblInfoLst.add(csjChangeTblInfo);
//				}
//			}



//
//			CsjPoiUtils.setCellValueWithCs(sheet, tblRow, 3, String.valueOf(csjChangeTblInfo.getSumTblXlsCnt()),cellStyle.getCsTitleBoder());
//			CsjPoiUtils.setCellValueWithCs(sheet, tblRow, 4, String.valueOf(csjChangeTblInfo.getSumTblDbCnt()),cellStyle.getCsTitleBoder());
//			CsjPoiUtils.setCellValueWithCs(sheet, tblRow, 5, String.valueOf(csjChangeTblInfo.getSumRecUptCnt()),cellStyle.getCsTitleBoder());
//			CsjPoiUtils.setCellValueWithCs(sheet, tblRow, 6, String.valueOf(csjChangeTblInfo.getSumRecsDelCnt()),cellStyle.getCsTitleBoder());
//			CsjPoiUtils.setCellValueWithCs(sheet, tblRow, 7, String.valueOf(csjChangeTblInfo.getSumRecInsCnt()),cellStyle.getCsTitleBoder());
//
			String nm = "";
			if (isOnlyChanged) {
				nm = "_isOnlyChange";
			}
			fileOut = new FileOutputStream(xmlDb.getXlsPath() 
					+ sumRecUptCnt+"_"+sumRecsDelCnt+"_"+sumRecInsCnt+"_"+sumTblXlsCnt+"_"+sumTblDbCnt+"_"
					+CmnStrUtils.fromAtoBByTrim(file.getName(), "", ".")+nm+excelType);
			wb.write(fileOut);
			fileOut.close();
			fileIn.close();
		} catch (Throwable e) {
			throw e;
		} finally {
			if (fileOut != null) {
				fileOut.close();
			}
			if (fileIn != null) {
				fileIn.close();
			}
		}
	}

	/**
	 * @param wb
	 * @param sheet
	 * @param cellStyle
	 * @param isWhere
	 * @param isWithCmt
	 * @param isOnlyChanged 
	 * @param tblsInfo
	 * @param dbInfo
	 * @param dbXlsInfo
	 * @param csjChangeTblInfo
	 * @param excelFile 
	 * @throws Throwable
	 */
	private static void makeSheet(Workbook wb, Sheet sheet, CsjDbCellStyle cellStyle, boolean isOnlyChanged, XmlDbXlsInfoAll xmlDbXlsInfoAll, CsjChangeTblInfo csjChangeTblInfo, File excelFile) throws Throwable {
		XmlInfoXls xmlInfoXls =  xmlDbXlsInfoAll.getXmlInfoXls();
		String sheetNm = sheet.getSheetName();
		SheetTblsInfo tblsInfo = new SheetTblsInfo();

		checkConditionSame(xmlDbXlsInfoAll, sheet);

		wb.setSheetName(wb.getSheetIndex(sheetNm),sheetNm+"_cmp");
		XlsTblInfo tblInfo = null;
		String tblNmEn = CsjConst.EMPTY;
		String tblNmJp = CsjConst.EMPTY;
		int colHeigthStep = 0;
		Set<String> xlsRecKeySet = new LinkedHashSet<String>();

		int uptCnt = 0;
		int delCnt = 0;
		int xlsCnt = 0;
		CsjChangeTbl changeTbl = null;
		CmnPoiUtils.setCellValue(sheet,sheet.getLastRowNum()+2, 0, " ");
		for(int rowIndex = 0; rowIndex <sheet.getLastRowNum(); rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			if (row == null) {
				continue;
			}
			List<String> strList = CmnPoiUtils.getCellContents(row, true);
			if (strList.size() > 1) {
				if (strList.get(0).equals(xmlInfoXls.getTblSign())
						&& CmnPoiUtils.getCellContent(sheet, row.getRowNum(), 0, false).equals(
								xmlInfoXls.getTblSign())) {
					if (CmnStrUtils.isNotEmpty(tblNmEn)) {
						tblsInfo.getTblInfoMap().put(tblNmEn, tblInfo);
					}

					tblNmEn = CmnPoiUtils.getCellContent(sheet, row.getRowNum(), 1, false);
					tblNmJp = CmnPoiUtils.getCellContent(sheet, row.getRowNum(), 2, false);
					CmnPoiUtils.setCellValue(sheet, row.getRowNum(), 0, CsjDbToolsMsg.coreMsgMap
							.get(CsjDbToolsMsg.MSG_I_0000069)+xmlInfoXls.getTblSign());
					tblInfo = new XlsTblInfo();
					tblInfo.setTblNmEn(tblNmEn);
					tblInfo.setTblNmJp(tblNmJp);

					if (changeTbl != null) {
						changeTbl.setRecDelCnt(delCnt);
						changeTbl.setRecUptCnt(uptCnt);
						changeTbl.setTblXlsCnt(xlsCnt);
						if (changeTbl.getTblDbCnt()==0) {
							changeTbl.setTblDbCnt(AutoXlsToDbForMemory.getTblRecNum(xmlDbXlsInfoAll, changeTbl.getTblNmEn(),AutoDbToXls.RUN_DATA_NONE));
						}
						csjChangeTblInfo.getCsjChangeTblsMap().put(changeTbl.getTblNmEn(), changeTbl);
					}
					changeTbl = new CsjChangeTbl();
					changeTbl.setTblNmEn(tblInfo.getTblNmEn());
					changeTbl.setTblNmJp(tblInfo.getTblNmJp());

					colHeigthStep = 0;
					xlsRecKeySet.clear();
					uptCnt = 0;
					delCnt = 0;
					xlsCnt = 0;
				}
			}

			if (CmnStrUtils.isEmpty(tblNmEn)) {
			} else {
				LinkedHashMap<Integer, TblPara> paraPosInfoMap = tblInfo.getParaPosInfoMap();
				String cell0Str = CmnPoiUtils.getCellContent(row, 0, false);
				if (CmnStrUtils.isNumeric(cell0Str)) {
					xlsCnt++;

					XlsRecord xlsRec = new XlsRecord();

					for (Cell cell : row) {

						int colPos = cell.getColumnIndex();
						CsjCellInfo csjCellInfo = CmnPoiUtils.getCellInfo( cell);
						if (paraPosInfoMap.containsKey(colPos)) {
							XlsTblPara xlsTblPara = new XlsTblPara(paraPosInfoMap.get(colPos));

							xlsTblPara.setParaVal(csjCellInfo.getContent());

							xlsRec.getRecord().add(xlsTblPara);
							xlsRec.getRecmap().put(colPos, xlsTblPara);
						}
					}

					List<XlsRecord> dbRecList = getDbRec(xmlDbXlsInfoAll, tblInfo, xlsRec,xlsRecKeySet);

					XlsRecord dbRec = null;
					if (dbRecList != null && dbRecList.size() !=0 ) {
						dbRec = dbRecList.get(0);
					}

					int colStep = 1;

					
					CmnPoiUtils.setCellValue(sheet, row.getRowNum(), 0, cell0Str+CsjDbToolsMsg.coreMsgMap
							.get(CsjDbToolsMsg.MSG_I_0000070));

					if (dbRec == null) {
						if (isOnlyChanged) {
							CmnPoiUtils.setCellValueWithCs(sheet, row.getRowNum(), 0, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000093)+cell0Str+CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000070)+CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000094)+String.valueOf(++delCnt), cellStyle.getCsRecordStr());
							
							for (XlsTblPara xlsPara : xlsRec.getRecord()) {
								Cell cell = CmnPoiUtils.getOrCreateCell(sheet, row.getRowNum(), colStep++);
								if (cell != null) {
									cell.setCellStyle(cellStyle.getCsDiff_19_0());
								}
							}
							rowIndex--;
						} else {
							CmnPoiUtils.insertRow(sheet,row.getRowNum(),1,false);
							CmnPoiUtils.setCellValue(sheet, row.getRowNum(), 0, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000093)+cell0Str+CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000070));
							CmnPoiUtils.setCellValueWithCs(sheet, row.getRowNum()+1, 0, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000076)+String.valueOf(++delCnt), cellStyle.getCsRecordStr());

							for (XlsTblPara xlsPara : xlsRec.getRecord()) {
								CmnPoiUtils.setCellValueWithCs(sheet, row.getRowNum()+1, colStep++, xlsPara.getParaVal(),
										cellStyle.getCsDiff_19_0());
							}
						}
					
					} else {

						if (chkIsSame(xlsRec, dbRec, cellStyle.getFtRed())) {
							if (isOnlyChanged) {
								CmnPoiUtils.removeRow(sheet, rowIndex--);
								rowIndex--;
								
							} else {
								CmnPoiUtils.insertRow(sheet,row.getRowNum(),1,false);
								CmnPoiUtils.setCellValueWithCs(sheet, row.getRowNum()+1, 0, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000035), cellStyle.getCsRecordStr());
								for (XlsTblPara xlsPara : xlsRec.getRecord()) {
									CmnPoiUtils.setCellValueWithCs(sheet, row.getRowNum(), colStep++, xlsPara, cellStyle);
								}
								colStep = 1;
								for (XlsTblPara xlsPara : dbRec.getRecord()) {
									CmnPoiUtils.setCellValueWithCs(sheet, row.getRowNum()+1, colStep++, xlsPara, cellStyle);
								}
							}
						} else {
							CmnPoiUtils.insertRow(sheet,row.getRowNum(),1,false);
							CmnPoiUtils.setCellValue(sheet, row.getRowNum(), 0, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000093)+cell0Str+CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000070));
							CmnPoiUtils.setCellValueWithCs(sheet, row.getRowNum()+1, 0, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000075)+String.valueOf(++uptCnt), cellStyle.getCsRecordStr());
							for (int k = 0; k < xlsRec.getRecord().size(); k++) {
								XlsTblPara xlsPara = xlsRec.getRecord().get(k);
								XlsTblPara dbPara = dbRec.getRecord().get(k);

								if (xlsPara.getRichText() == null) {
									CmnPoiUtils.setCellValueWithCs(sheet, row.getRowNum(), colStep, xlsPara, cellStyle);
									CmnPoiUtils.setCellValueWithCs(sheet, row.getRowNum()+1, colStep++, dbPara, cellStyle);
								} else {
									CmnPoiUtils.setCellValueWithCs(sheet, row.getRowNum(), colStep, xlsPara.getRichText(),
											cellStyle.getCsDiff_17_0());
									CmnPoiUtils.setCellValueWithCs(sheet, row.getRowNum()+1, colStep++, dbPara.getRichText(),
											cellStyle.getCsDiff_18_0());
								}
							}
						}
					}
					rowIndex++;
				
					String cell0Str1 = CmnPoiUtils.getCellContent(sheet.getRow(rowIndex+1), 0, false);
					if (CmnStrUtils.isNumeric(cell0Str1)== false ) {
						rowIndex++;
						HashSet<String> tblNmSet = new HashSet<String>();
						tblNmSet.add(tblNmEn);
						// TODOSAI
						String maxRow = xmlDbXlsInfoAll.getXmlInfoXls().getMaxRow();
						xmlDbXlsInfoAll.getXmlInfoXls().setMaxRow("-1");
						SheetTblsInfo tblsDbInfo = AutoDbToXls.getDBInfoStruct(xmlDbXlsInfoAll, tblNmSet);
						xmlDbXlsInfoAll.getXmlInfoXls().setMaxRow(maxRow);
						if (tblsDbInfo == null) {
							return;
						}
						XlsTblInfo xlsTblInfo = tblsDbInfo.getTblInfoMap().get(tblNmEn);

						if (xlsTblInfo != null) {
							long listSize = xlsTblInfo.getTblDataList().size();
							changeTbl.setTblDbCnt(listSize);
//							if (xlsCnt<listSize ) {
//								rowIndex++;
//							}
							int cnt = 0;
							for (int index = 0; index < listSize; index++) {
								XlsRecord xlsRec1 = xlsTblInfo.getTblDataList().get(index);

								StringBuffer sb = new StringBuffer();
								for (XlsTblPara xlsPara : xlsRec1.getRecord()) {
									if (xlsPara.isPkey()) {
										sb.append(xlsPara.getParaVal());
										sb.append(CsjDbToolsMsg.coreMsgMap
												.get(CsjDbToolsMsg.MSG_I_0000071));
									}
								}
								if (xlsRecKeySet.contains(sb.toString())) {
									continue;
								}
								CmnPoiUtils.insertRow(sheet,rowIndex,1,false);
								CmnPoiUtils.setCellValue(sheet, rowIndex, 0,
										CsjDbToolsMsg.coreMsgMap
										.get(CsjDbToolsMsg.MSG_I_0000074)+
										String.valueOf(++cnt));
								int colStep1 = 1;
								for (XlsTblPara xlsPara : xlsRec1.getRecord()) {
									if (colStep1 == xmlDbXlsInfoAll.getXmlInfoXls().getMaxCol()) {
										break;
									}
									CmnPoiUtils.setCellValueWithCs(sheet, rowIndex,
											colStep1++, xlsPara, cellStyle);
								}
								rowIndex++;
							}

							changeTbl.setRecInsCnt(cnt);
						}

//						changeTbl.setRecDelCnt(delCnt);
//						changeTbl.setRecUptCnt(uptCnt);
//						changeTbl.setTblXlsCnt(xlsCnt);
//						changeTbl.setTblDbCnt(AutoXlsToDbForMemory.getTblCntByTblNm(dbInfo.getDbAccess(), tblInfo.getTblNmEn()));
//						csjChangeTblInfo.getCsjChangeTblsMap().put(changeTbl.getTblNmEn(), changeTbl);
					}
				} else if (tblInfo.getTblDataList().size() == 0) {
					String sign = CmnPoiUtils.getCellContent(sheet, row.getRowNum(), 0, false);
					if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000003).equals(sign)) {
						for (Cell cell : row) {

							CsjCellInfo csjCellInfo = CmnPoiUtils.getCellInfo( cell);
							csjCellInfo.setContent(CmnStrUtils.lrTrimSpace(csjCellInfo.getContent()));
							int colPos = cell.getColumnIndex();
							if (colPos != 0) {
								TblPara para = new TblPara();
								para.setParaNmEn(csjCellInfo.getContent());
								if (csjCellInfo.getFont().getBoldweight() == Font.BOLDWEIGHT_BOLD) {
									para.setPkey(true);
									tblInfo.getKeyPosList().add(colPos);
								} else {
									para.setPkey(false);
								}
								paraPosInfoMap.put(colPos, para);
							}
						}
					} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000004).equals(sign)) {
						for (Cell cell : row) {

							CsjCellInfo csjCellInfo = CmnPoiUtils.getCellInfo( cell);
							csjCellInfo.setContent(CmnStrUtils.lrTrimSpace(csjCellInfo.getContent()));
							int colPos = cell.getColumnIndex();
							if (colPos != 0 && paraPosInfoMap.containsKey(colPos)) {
								TblPara para = paraPosInfoMap.get(colPos);
								para.setParaTypeWithlen(xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect(),csjCellInfo.getContent());

								Map<String,String> splitMap = CmnStrUtils.getSplitMap( CmnPoiUtils.getCellComment(cell), CsjProcess.s_newLine, CsjConst.MASK_TO_RIGHT,CsjConst.trimChSet,false);

								para.setParaType(splitMap.get(CsjConst.JP_TYPE));
								para.setParaLen(CmnStrUtils.getLongVal(splitMap.get(CsjConst.JP_LENGTH)));
							}
						}
					} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000005).equals(sign)) {
						for (Cell cell : row) {

							CsjCellInfo csjCellInfo = CmnPoiUtils.getCellInfo( cell);
							csjCellInfo.setContent(CmnStrUtils.lrTrimSpace(csjCellInfo.getContent()));
							int colPos = cell.getColumnIndex();
							if (colPos != 0 && paraPosInfoMap.containsKey(colPos)) {
								TblPara para = paraPosInfoMap.get(colPos);
								para.setCanNull("Y".equals(CmnStrUtils.toLowOrUpStr(csjCellInfo.getContent())));

								Map<String,String> splitMap = CmnStrUtils.getSplitMap(CmnPoiUtils.getCellComment(cell), CsjProcess.s_newLine, CsjConst.MASK_TO_RIGHT,CsjConst.trimChSet,false);
								para.setParaInitVal(splitMap.get(CsjConst.JP_INIT_VAL));
							}
						}
						
						
						int currentRow = row.getRowNum()+1;
						
						if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000162).equals( CmnPoiUtils.getCellContent(sheet,currentRow++, 0, false))
								&& CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000163).equals( CmnPoiUtils.getCellContent(sheet,currentRow++, 0, false))
								&& CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000164).equals( CmnPoiUtils.getCellContent(sheet,currentRow++, 0, false))
								&& CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000165).equals( CmnPoiUtils.getCellContent(sheet,currentRow++, 0, false))
								&& CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000166).equals( CmnPoiUtils.getCellContent(sheet,currentRow++, 0, false))
								&& CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000167).equals( CmnPoiUtils.getCellContent(sheet,currentRow++, 0, false))
						) {
							
//							■对应列号
//							■分隔符
//							■文件位置
//							■文本编码
							currentRow = row.getRowNum()+1;
							Map<Integer,Integer> posMap = new LinkedHashMap<Integer,Integer>();
							for (Cell cell : sheet.getRow(currentRow)) {
								CsjCellInfo csjCellInfo = CmnPoiUtils.getCellInfo(cell);
								String content = CmnStrUtils.lrTrimSpace(csjCellInfo.getContent());
								if (content.contains(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000162))) {
									continue;
								}
								if (CmnStrUtils.isEmpty(content)) {
									continue;
								}
								posMap.put(cell.getColumnIndex(),CmnStrUtils.getIntVal(content));
							}
							currentRow++;
							String ch =  CmnPoiUtils.getCellContent(sheet,currentRow++, 1, false);
							String inputFilePath =  CmnPoiUtils.getCellContent(sheet,currentRow++, 1, false);
							String inputFileEncode =  CmnPoiUtils.getCellContent(sheet,currentRow++, 1, false);
							long beginRow =  CmnStrUtils.getLongVal(CmnPoiUtils.getCellContent(sheet,currentRow++, 1, false));
							long endRow= CmnStrUtils.getLongVal(CmnPoiUtils.getCellContent(sheet,currentRow++, 1, false));
							long fileIndexRow = -1;
							File fTxt = new File(inputFilePath);
							CmnLog.logger.debug(inputFilePath);

							if (!fTxt.isFile()) {
								CmnLog.logger.debug(excelFile.getParent()+CsjProcess.s_f_s+inputFilePath);
								 fTxt = new File(excelFile.getParent()+CsjProcess.s_f_s+inputFilePath);
								 if (!fTxt.isFile()) {
									throw new Exception("file not find"); 
								 }
							}
							List<Integer> posDeleteLst = new ArrayList<Integer>();
							for (Entry<Integer, TblPara> entry : paraPosInfoMap.entrySet()) {
								if (!posMap.containsKey(entry.getKey())) {
									posDeleteLst.add(entry.getKey());
								}
							}
							for (Integer pos : posDeleteLst) {
								paraPosInfoMap.remove(pos);
							}
							
							BufferedReader reader = new BufferedReader(new InputStreamReader(
									new FileInputStream(fTxt), inputFileEncode));
							File fOut = new File(xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath()+"cmp_"+xmlDbXlsInfoAll.getPicInfo().getBtnTime()+"_"+fTxt.getName());
							BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
									new FileOutputStream(fOut), inputFileEncode));
							
							while (reader.ready()) {
								String line = reader.readLine();
								fileIndexRow++;
								if (fileIndexRow<beginRow||endRow < fileIndexRow) {
									continue;
								}
								String sArr[] = line.split(ch,-1);
								if (sArr == null || sArr.length == 0|| sArr[0].length() == 0) {
									continue;
								}
								xlsCnt++;
								XlsRecord xlsRec = new XlsRecord();
								
								for (Entry<Integer, TblPara> entry : paraPosInfoMap.entrySet()) {
									
										XlsTblPara xlsTblPara = new XlsTblPara(
												paraPosInfoMap.get(entry.getKey()));
										xlsTblPara.setParaVal(sArr[posMap.get(entry.getKey())]);
	
										xlsRec.getRecord().add(xlsTblPara);
										xlsRec.getRecmap().put(entry.getKey(), xlsTblPara);
								}

								List<XlsRecord> dbRecList = getDbRec(xmlDbXlsInfoAll, tblInfo, xlsRec,xlsRecKeySet);
								XlsRecord dbRec = null;
								if (dbRecList != null && dbRecList.size() !=0 ) {
									dbRec = dbRecList.get(0);
								}

								if (dbRec == null) {
									CmnLog5j.writeLine(writer,CsjConst.CMP_DB_DEL+line);
									delCnt++;

								} else {
									if (chkIsSame(xlsRec, dbRec, null)) {
										if (isOnlyChanged) {
											
										} else {
											CmnLog5j.writeLine(writer,line);
										}
									} else {
										uptCnt++;
										CmnLog5j.writeLine(writer,CsjConst.CMP_FILE_UPT+line);
										StringBuffer sb = new StringBuffer();
										
										for (int k = 0; k < dbRec.getRecord().size(); k++) {
											XlsTblPara dbPara = dbRec.getRecord().get(k);
											sb.append(dbPara.getParaVal());
											sb.append(ch);
										}
										String dbLine = sb.toString();
										CmnLog5j.writeLine(writer,CsjConst.CMP_DB_UPT+dbLine.substring(0, dbLine.length()-1));
									}
								}
							}
							
							changeTbl.setRecDelCnt(delCnt);
							changeTbl.setRecUptCnt(uptCnt);
							reader.close();
//							if (isWhere) {
								
								List<Object> paraList = new ArrayList<Object>();
								XlsTblInfo xlsTblInfo = new XlsTblInfo();
								Set<String> keySet = new LinkedHashSet<String>();
								String paraNmEn = "";
								for (TblPara tblPara : paraPosInfoMap.values()) {
									xlsTblInfo.getParaInfoMap().put(tblPara.getParaNmEn(), tblPara);
									paraNmEn = tblPara.getParaNmEn();
									if (tblPara.isPkey()) {
										keySet.add(tblPara.getParaNmEn());
									}
								}
								xlsTblInfo.getParaInfoMap().get(paraNmEn).setLastPara(true);
								
								String sql = AutoDbToXls.getSelectTableDataSql(xmlDbXlsInfoAll, tblNmEn, xlsTblInfo, paraList, AutoDbToXls.RUN_DATA_NONE);
								
								long[] cntArr = xmlDbXlsInfoAll.getDbInfo().getDbAccess().outputInsertRecord(sql,paraList,xlsRecKeySet,keySet,CsjDbToolsMsg.coreMsgMap
										.get(CsjDbToolsMsg.MSG_I_0000071),xmlDbXlsInfoAll.getPicInfo().getCh(),writer);
								changeTbl.setTblDbCnt(cntArr[0]);
								changeTbl.setRecInsCnt(cntArr[1]);
//							}
							
							CmnLog5j.writeLine(writer,"-----     result     -----");
							CmnLog5j.writeLine(writer,"file  cnt:"+xlsCnt);
							CmnLog5j.writeLine(writer,"db    upt:"+changeTbl.getRecUptCnt());
							CmnLog5j.writeLine(writer,"db    del:"+changeTbl.getRecDelCnt());
//							if (isWhere) {
								CmnLog5j.writeLine(writer,"db    cnt:"+changeTbl.getTblDbCnt());
								CmnLog5j.writeLine(writer,"db    ins:"+changeTbl.getRecInsCnt());
//							}
				
							CmnLog5j.writeLine(writer,"--------------------------");
							CmnLog5j.close(writer);
//							tblInfo = null;
//							tblNmEn = CsjConst.EMPTY;
						} else if (CmnStrUtils.isNumeric(CmnPoiUtils.getCellContent(sheet.getRow(rowIndex+1), 0, false))== false ) {

							rowIndex++;
							HashSet<String> tblNmSet = new HashSet<String>();
							tblNmSet.add(tblNmEn);
							// TODOSAI
							String maxRow = xmlDbXlsInfoAll.getXmlInfoXls().getMaxRow();
							xmlDbXlsInfoAll.getXmlInfoXls().setMaxRow("-1");
							SheetTblsInfo tblsDbInfo = AutoDbToXls.getDBInfoStruct(xmlDbXlsInfoAll, tblNmSet);
							xmlDbXlsInfoAll.getXmlInfoXls().setMaxRow(maxRow);
							if (tblsDbInfo == null) {
								return;
							}
							XlsTblInfo xlsTblInfo = tblsDbInfo.getTblInfoMap().get(tblNmEn);

							if (xlsTblInfo != null) {
								long listSize = xlsTblInfo.getTblDataList().size();
								changeTbl.setTblDbCnt(listSize);
//								if (xlsCnt<listSize ) {
//									rowIndex++;
//								}
								int cnt = 0;
								for (int index = 0; index < listSize; index++) {
									XlsRecord xlsRec1 = xlsTblInfo.getTblDataList().get(index);

									StringBuffer sb = new StringBuffer();
									for (XlsTblPara xlsPara : xlsRec1.getRecord()) {
										if (xlsPara.isPkey()) {
											sb.append(xlsPara.getParaVal());
											sb.append(CsjDbToolsMsg.coreMsgMap
													.get(CsjDbToolsMsg.MSG_I_0000071));
										}
									}
									if (xlsRecKeySet.contains(sb.toString())) {
										continue;
									}
									CmnPoiUtils.insertRow(sheet,rowIndex,1,false);
									CmnPoiUtils.setCellValue(sheet, rowIndex, 0,
											CsjDbToolsMsg.coreMsgMap
											.get(CsjDbToolsMsg.MSG_I_0000074)+
											String.valueOf(++cnt));
									int colStep1 = 1;
									for (XlsTblPara xlsPara : xlsRec1.getRecord()) {
										if (colStep1 == xmlDbXlsInfoAll.getXmlInfoXls().getMaxCol()) {
											break;
										}
										CmnPoiUtils.setCellValueWithCs(sheet, rowIndex,
												colStep1++, xlsPara, cellStyle);
									}
									rowIndex++;
								}

								changeTbl.setRecInsCnt(cnt);
							}
						
						}
						
					}

					colHeigthStep++;
				} else if (CmnStrUtils.isNotEmpty(tblNmEn)) {
					tblsInfo.getTblInfoMap().put(tblNmEn, tblInfo);

					tblInfo = null;
					tblNmEn = CsjConst.EMPTY;
				}
			}
		}
		if (CmnStrUtils.isNotEmpty(tblNmEn) && tblsInfo.getTblInfoMap().containsKey(tblNmEn) == false) {
			tblsInfo.getTblInfoMap().put(tblNmEn, tblInfo);

			if (changeTbl != null) {
				changeTbl.setRecDelCnt(delCnt);
				changeTbl.setRecUptCnt(uptCnt);
				changeTbl.setTblXlsCnt(xlsCnt);
				if (changeTbl.getTblDbCnt()==0) {
					changeTbl.setTblDbCnt(AutoXlsToDbForMemory.getTblRecNum(xmlDbXlsInfoAll, changeTbl.getTblNmEn(),AutoDbToXls.RUN_DATA_NONE));
				}
				csjChangeTblInfo.getCsjChangeTblsMap().put(changeTbl.getTblNmEn(), changeTbl);
			}
			tblInfo = null;
			tblNmEn = CsjConst.EMPTY;
		}

		csjChangeTblInfo.resetCnt();
		CsjLinkedMap<String, CsjChangeTbl> map = csjChangeTblInfo.getCsjChangeTblsMap();

		int tblRow = 0;
		// 选择一个区域，从startRow+1直到最后一行
		sheet.shiftRows(tblRow, sheet.getLastRowNum(), map.size()+4, true, false);
		sheet.addMergedRegion(new CellRangeAddress(tblRow,tblRow,(short)1,(short)7));

		//sheet.getRow(tblRow).getCell(1)
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 1, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000085),cellStyle.getCsDiff_10_0());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 2, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000085),cellStyle.getCsDiff_10_0());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 3, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000085),cellStyle.getCsDiff_10_0());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 4, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000085),cellStyle.getCsDiff_10_0());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 5, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000085),cellStyle.getCsDiff_10_0());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 6, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000085),cellStyle.getCsDiff_10_0());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 7, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000085),cellStyle.getCsDiff_10_0());

		tblRow++;

		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 1, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000077),cellStyle.getCsDiff_11_0());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 2, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000078),cellStyle.getCsDiff_11_1());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 3, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000079),cellStyle.getCsDiff_11_2());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 4, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000080),cellStyle.getCsDiff_11_3());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 5, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000081),cellStyle.getCsDiff_11_4());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 6, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000082),cellStyle.getCsDiff_11_5());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 7, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000083),cellStyle.getCsDiff_11_6());

		tblRow++;
		for (Entry<String, CsjChangeTbl> entry : map.entrySet()) {
			CsjChangeTbl tbl = entry.getValue();
			CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 1, tbl.getTblNmEn(),cellStyle.getCsDiff_12_0());
			CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 2, tbl.getTblNmJp(),cellStyle.getCsDiff_12_1());
			CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 3, String.valueOf(tbl.getTblXlsCnt()),cellStyle.getCsDiff_12_2());
			CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 4, String.valueOf(tbl.getTblDbCnt()),cellStyle.getCsDiff_12_3());
			CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 5, String.valueOf(tbl.getRecUptCnt()),cellStyle.getCsDiff_12_4());
			CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 6, String.valueOf(tbl.getRecDelCnt()),cellStyle.getCsDiff_12_5());
			CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 7, String.valueOf(tbl.getRecInsCnt()),cellStyle.getCsDiff_12_6());
			tblRow++;
		}

		sheet.addMergedRegion(new CellRangeAddress(tblRow,tblRow,(short)1,(short)2));
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 1, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000084),cellStyle.getCsDiff_13_0());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 2, CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000084),cellStyle.getCsDiff_13_0());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 3, String.valueOf(csjChangeTblInfo.getSumTblXlsCnt()),cellStyle.getCsDiff_13_2());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 4, String.valueOf(csjChangeTblInfo.getSumTblDbCnt()),cellStyle.getCsDiff_13_3());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 5, String.valueOf(csjChangeTblInfo.getSumRecUptCnt()),cellStyle.getCsDiff_13_4());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 6, String.valueOf(csjChangeTblInfo.getSumRecsDelCnt()),cellStyle.getCsDiff_13_5());
		CmnPoiUtils.setCellValueWithCs(sheet, tblRow, 7, String.valueOf(csjChangeTblInfo.getSumRecInsCnt()),cellStyle.getCsDiff_13_6());

		CsjChangeTblInfo.s_all_sumRecInsCnt +=csjChangeTblInfo.getSumRecInsCnt();
		CsjChangeTblInfo.s_all_sumRecsDelCnt +=csjChangeTblInfo.getSumRecsDelCnt();
		CsjChangeTblInfo.s_all_sumRecUptCnt +=csjChangeTblInfo.getSumRecsDelCnt();
		CsjChangeTblInfo.s_all_sumTblDbCnt +=csjChangeTblInfo.getSumTblDbCnt();
		CsjChangeTblInfo.s_all_sumTblXlsCnt +=csjChangeTblInfo.getSumTblXlsCnt();
	}

	/**
	 * @param xmlDbXlsInfoAll
	 * @param sheet
	 * @throws Throwable
	 */
	private static void checkConditionSame(XmlDbXlsInfoAll xmlDbXlsInfoAll,
			Sheet sheet) throws Throwable {
		AutoXlsToDbForMemory.getTblsInfoBySheet(xmlDbXlsInfoAll, sheet);

		CsjLinkedMap<String, CsjTblSelectSql> importSqlMapWithNo = xmlDbXlsInfoAll.getDbInfo().getImportSqlMapWithNo();
		CsjLinkedMap<String, CsjTblSelectSql> selectSqlMapWithTblnm = xmlDbXlsInfoAll.getDbInfo().getSelectSqlMapWithTblnm();
//		for (CsjTblSelectSql importSql : importSqlMapWithNo.values()) {
//			if (selectSqlMapWithTblnm.containsKey(importSql.getTblNm())) {
//				CsjTblSelectSql syncSql = selectSqlMapWithTblnm.get(importSql.getTblNm());
//				if (!CsjStrUtils.lrTrimSpace(syncSql.getSqlWhere()).equals(CsjStrUtils.lrTrimSpace(importSql.getSqlWhere()))) {
//					throw new Exception(CsjConst.SIGN_BRACKETS_M_L + importSql.getTblNm()+CsjConst.SIGN_BRACKETS_M_R + CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000091));
//				}
//			} else {
//				throw new Exception(CsjConst.SIGN_BRACKETS_M_L + importSql.getTblNm()+CsjConst.SIGN_BRACKETS_M_R + CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000092));
//			}
//		}
	}

	/**
	 * @param xlsRec
	 * @param dbRec
	 * @param ftRed
	 * @return
	 */
	private static boolean chkIsSame(XlsRecord xlsRec, XlsRecord dbRec,
			Font ftRed) {
		boolean retVal = true;
		
		if (ftRed instanceof HSSFFont) {
			for (int i = 0; i < xlsRec.getRecord().size(); i++) {
				XlsTblPara xlsPara = xlsRec.getRecord().get(i);
				XlsTblPara dbPara = dbRec.getRecord().get(i);

				if (xlsPara.getParaVal().equals(dbPara.getParaVal())) {

				} else {
					
					xlsPara.setRichText(new HSSFRichTextString(xlsPara.getParaVal()));
					dbPara.setRichText(new HSSFRichTextString(dbPara.getParaVal()));
					CmnPoiUtils.setColorForDif(xlsPara.getRichText(),
							dbPara.getRichText(), ftRed, IConstFile.ENCODE_SHIFT_JIS);
					retVal = false;
				}
			}
		} else if (ftRed instanceof XSSFFont) {
			for (int i = 0; i < xlsRec.getRecord().size(); i++) {
				XlsTblPara xlsPara = xlsRec.getRecord().get(i);
				XlsTblPara dbPara = dbRec.getRecord().get(i);

				if (xlsPara.getParaVal().equals(dbPara.getParaVal())) {

				} else {
					
					xlsPara.setRichText(new XSSFRichTextString(xlsPara.getParaVal()));
					dbPara.setRichText(new XSSFRichTextString(dbPara.getParaVal()));
					CmnPoiUtils.setColorForDif(xlsPara.getRichText(),
							dbPara.getRichText(), ftRed, IConstFile.ENCODE_SHIFT_JIS);
					retVal = false;
				}
			}
		} else if (ftRed == null) {
			for (int i = 0; i < xlsRec.getRecord().size(); i++) {
				XlsTblPara xlsPara = xlsRec.getRecord().get(i);
				XlsTblPara dbPara = dbRec.getRecord().get(i);
				if (xlsPara.getParaVal().equals(dbPara.getParaVal())) {

				} else {
					retVal = false;
					break;
				}
			}
		}

		return retVal;
	}

	/**
	 * @param dbInfo
	 * @param tblInfo
	 * @param keyPosList
	 * @param xlsRec1
	 * @param xlsRecMap
	 * @throws Throwable
	 */
	private static List<XlsRecord> getDbRec(XmlDbXlsInfoAll xmlDbXlsInfoAll, XlsTblInfo tblInfo, XlsRecord xlsRec1,
			Set<String> xlsRecKeySet) throws Throwable {
		List<XlsRecord> dbRecList = new ArrayList<XlsRecord>();

		String tblNm = tblInfo.getTblNmEn();

		StringBuffer sbSelect = new StringBuffer();
		StringBuffer sbWhere = new StringBuffer(" WHERE 1=1 ");
		sbSelect.append("SELECT ");

		List<Object> paraList = new ArrayList<Object>();

		for (int i = 0; i < xlsRec1.getRecord().size(); i++) {

			XlsTblPara xlsTblPara = xlsRec1.getRecord().get(i);

			sbSelect.append("\"" + xlsTblPara.getParaNmEn() + "\" ");
			if (i + 1 != xlsRec1.getRecord().size()) {
				sbSelect.append(", ");
			}

			if (xlsTblPara.isPkey()) {
				sbWhere.append(" AND ");
				sbWhere.append("\"" + xlsTblPara.getParaNmEn() + "\" " + " = ? ");
				paraList.add(xlsTblPara);
			}
		}

		sbSelect.append(" FROM ");
		sbSelect.append("\"" + tblNm + "\" ");


		String selectTableDataSql = sbSelect.append(sbWhere).toString();
		List<HashMap<String, String>> dataList = null;
		dataList = xmlDbXlsInfoAll.getDbInfo().getDbAccess().getRecordList(selectTableDataSql, paraList,Integer.MAX_VALUE).getResultList();
		if (dataList ==null || dataList.size() == 0) {
		} else {
			for (HashMap<String, String> map : dataList) {
				XlsRecord dbRec =  new XlsRecord();
				StringBuffer sbKey = new StringBuffer();
				for(Entry<Integer, XlsTblPara> entryPara : xlsRec1.getRecmap().entrySet()) {

					TblPara tblPara = entryPara.getValue();
					XlsTblPara xlsTblPara = new XlsTblPara(tblPara);
					String val = CmnStrUtils.convertString(map.get(tblPara.getParaNmEn()));
					xlsTblPara.setParaVal(val);
					if (tblPara.isPkey()) {
						sbKey.append(val+ CsjDbToolsMsg.coreMsgMap
								.get(CsjDbToolsMsg.MSG_I_0000071));
					}

					dbRec.getRecord().add(xlsTblPara);

				}
				dbRecList.add(dbRec);
				xlsRecKeySet.add(sbKey.toString());
			}
		}

		return dbRecList;
	}
}



package jp.co.csj.tools.utils.z.exe.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mydbsee.common.CmnDateUtil;
import org.mydbsee.common.CmnFileUtils;
import org.mydbsee.common.CmnLog;
import org.mydbsee.common.CmnLog5j;
import org.mydbsee.common.CmnPoiUtils;
import org.mydbsee.common.CmnStrUtils;
import org.mydbsee.common.IConstFile;

import jp.co.csj.tools.core.CsjLinkedMap;
import jp.co.csj.tools.core.CsjSqlType;
import jp.co.csj.tools.utils.common.CsjPath;
import jp.co.csj.tools.utils.common.CsjProcess;
import jp.co.csj.tools.utils.common.StaticClz;
import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.db.CsjDBAccess;
import jp.co.csj.tools.utils.db.core.CsjRetWithSql;
import jp.co.csj.tools.utils.db.core.CsjTblSelectSql;
import jp.co.csj.tools.utils.db.core.DbInfo;
import jp.co.csj.tools.utils.db.core.SheetTblsInfo;
import jp.co.csj.tools.utils.db.core.TblBase;
import jp.co.csj.tools.utils.db.core.TblInfo;
import jp.co.csj.tools.utils.db.core.TblPara;
import jp.co.csj.tools.utils.db.core.XlsRecord;
import jp.co.csj.tools.utils.db.core.XlsTblInfo;
import jp.co.csj.tools.utils.db.core.XlsTblPara;
import jp.co.csj.tools.utils.db.core.para.ParaCheck;
import jp.co.csj.tools.utils.db.sqlserver.CsjSqlServerColType;
import jp.co.csj.tools.utils.msg.dbtools.CsjDbToolsMsg;
import jp.co.csj.tools.utils.poi.core.CsjDbCellStyle;
import jp.co.csj.tools.utils.poi.core.CsjPatr;
import jp.co.csj.tools.utils.xml.dbtools.XmlDbXlsInfoAll;
import jp.co.csj.tools.utils.xml.dbtools.XmlInfoXls;

/**
 * @author cuishuangjia
 *
 */
public class AutoDbToXls {

	public static final String TABLE_NAME = "TABLE_NAME";
	public static final String COMMENTS = "COMMENTS";
	public static final String TABLE_TYPE_INFO = "TABLE_TYPE_INFO";
	public static final String TBL_NM_JP = "TBL_NM_JP";
	public static final String TBL_NM_EN = "TBL_NM_EN";
	public static final String COL_ID = " ";//No.
	public static final String COL_NM_JP = "COL_NM_JP";
	public static final String COL_NM_EN = "COL_NM_EN";
	public static final String COL_TYPE_INFO = "COL_TYPE_INFO";
	public static final String COL_IS_PK = "COL_IS_PK";
	public static final String COL_CAN_NULL = "COL_CAN_NULL";
	public static final String COL_DEFAULT = "COL_DEFAULT";
	public static final String COL_TYPE = "COL_TYPE";
	public static final String COL_LENGTH = "COL_LENGTH";
	public static final String COL_EXTRA = "EXTRA";
	

	public static final int RUN_DATA_OUT = 2;
	public static final int RUN_DATA_OUT_SPLIT = 4;
	public static final int RUN_DATA_NONE = -1;

	public static int colStep = 1;
	public static int rowStep = 0;
	public static int PROCESS_MAX = 0;
	public static int PROCESS_INDEX = 0;
	public static int record_Num_Index = 0;
	public static int recordNumIndexPre = 0;
	public static boolean s_is_max_data = false;
	public static int stepRow = 1;
	public static int rowNull = 0;

	public static void resetStatus() {
		colStep = 1;
		rowStep = 0;
		record_Num_Index = 0;
		recordNumIndexPre = 0;
		stepRow = 1;
		rowNull = 0;
	}
	/**
	 *
	 */
	public AutoDbToXls() {

	}

	/**
	 * @param isToExcel 
	 * @param isFromTbl 
	 * @param dbInfo
	 * @throws Throwable
	 *
	 */
	public static void run(XmlDbXlsInfoAll xmlDbXlsInfoAll, HashSet<String> tableNmSet)
			throws Throwable {

		try {
			CmnLog.logger
					.debug("AutoDbToXls.run(DbInfo dbInfo, HashSet<String> tableNmSet) begin");

			CmnLog.logger.debug("read DB begin...");
			
				SheetTblsInfo tblsInfo = getDBInfo(xmlDbXlsInfoAll, tableNmSet);
				
				if (tblsInfo != null) {
					CmnLog.logger.debug("tables num is "
							+ tblsInfo.getTblInfoMap().size());
					CmnLog.logger.debug("read DB end");

					CmnLog.logger.debug("write xls begin...");

					writeToXlsByDataBase(tblsInfo, xmlDbXlsInfoAll);
					CmnLog.logger.debug("write xls end");
				} 
			CmnLog.logger
					.debug("AutoDbToXls.run(DbInfo dbInfo, HashSet<String> tableNmSet) end");
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * @param isToExcel 
	 * @param isFromTbl 
	 * @param dbInfo
	 * @throws Throwable
	 *
	 */
	public static void runIni(XmlDbXlsInfoAll xmlDbXlsInfoAll, String filePath, boolean isHaveMaxMinData) throws Throwable {
		CmnLog.logger.debug("AutoDbToXls.runIni(DbInfo dbInfo, String filePath) begin");
		FileInputStream fileIn = null;
		FileOutputStream fileOut = null;
		
		try {
			HashSet<String> sSet = new HashSet<String>();
			if (CmnStrUtils.isNotEmpty(filePath)) {
				sSet = CmnPoiUtils.getCellContentsMap(filePath,"", true);
			}
			SheetTblsInfo tblsInfo = getDBInfo(xmlDbXlsInfoAll, sSet);

			if (tblsInfo == null) {
				return;
			}
			String excelType = xmlDbXlsInfoAll.getXmlInfoXls().getExcelType();
			fileIn = new FileInputStream(CsjPath.s_file_pj_path_temp+excelType);
			
			Workbook wb = null;
			if (CsjConst.EXCEL_DOT_XLS_1997.equals(excelType)) {
				wb = new HSSFWorkbook(fileIn);
			} else {
				wb = new XSSFWorkbook(fileIn);
			}
			
			CsjDbCellStyle cellStyle = new CsjDbCellStyle(wb,excelType,CsjDbCellStyle.CS_DATA_REC);

			Sheet newSheet = wb.getSheet("temp");// wb.createSheet(tblNm);
			colStep = 1;
			rowStep = 0;
			stepRow = 1;
			rowNull = 0;
			writeToXlsByDataBaseWithIni(tblsInfo, xmlDbXlsInfoAll, newSheet, cellStyle,isHaveMaxMinData);

			File f = new File(xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath());
			f.mkdirs();
			String fileNm = "snap"+CsjPath.s_current_date_end+excelType;
//			if (CsjStrUtils.isNotEmpty(filePath)) {
//				fileNm = (new File(filePath)).getName();
//			}
			fileOut = new FileOutputStream(xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath()+ "data_"+ fileNm);
			wb.write(fileOut);
			fileOut.close();
			fileIn.close();
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		} finally {
			if (fileOut != null)
				fileOut.close();
			if (fileIn != null)
				fileIn.close();
		}
		CmnLog.logger.debug("AutoDbToXls.runIni(DbInfo dbInfo, String filePath) end");
	}

	/**
	 * @param tblsInfo
	 * @param dbInfo
	 */
	private static void writeToXlsByDataBaseWithIni(SheetTblsInfo tblsInfo,
			XmlDbXlsInfoAll xmlDbXlsInfoAll, Sheet newSheet, CsjDbCellStyle cellStyle, boolean isHaveMaxMinData)
			throws Throwable {
		String tblNm = "";
		try {
			CmnLog.logger
					.debug("AutoDbToXls. writeToXlsByDataBaseWithIni(SheetTblsInfo tblsInfo,DbInfo dbInfo, HSSFSheet newSheet, CsjDbCellStyle cellStyle) begin");

			XmlInfoXls xmlInfoXls = xmlDbXlsInfoAll.getXmlInfoXls();
			CsjLinkedMap<String, CsjTblSelectSql> selectSqlMapWithTblnm = xmlDbXlsInfoAll.getDbInfo().getSelectSqlMapWithTblnm();
			for (Map.Entry<String, XlsTblInfo> entry : tblsInfo.getTblInfoMap()
					.entrySet()) {

				tblNm = entry.getKey();

				CmnLog.logger.info("table:" + tblNm);

				XlsTblInfo xlsTblInfo = entry.getValue();
				CsjPatr csjPart = new CsjPatr(newSheet, 2, 10, 6, 15);
				// xlsTblInfo.setHaveJpNm(true);
				// if (xlsTblInfo.isHaveJpNm()) {
				// stepRow = 1;
				// }
				int rowType = 0;
				colStep = 1;
				CellStyle tempCs = null;
				for (Map.Entry<String, TblPara> paraEntry : xlsTblInfo
						.getParaInfoMap().entrySet()) {
					TblPara para = paraEntry.getValue();

					CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 0,
							xmlInfoXls.getTblSign(), cellStyle.getCsTableBolder());
					CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 1, tblNm,
							cellStyle.getCsTableBolder());
					CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 2,
							xlsTblInfo.getTblNmJp(), cellStyle.getCsTableBolder());
					CsjTblSelectSql selectSql = selectSqlMapWithTblnm.get(tblNm);
					if (selectSql != null) {
						CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, CmnStrUtils.getIntVal(CsjDbToolsMsg.coreMsgMap
								.get(CsjDbToolsMsg.MSG_I_0000072)),
								"WHERE "+selectSql.getSqlWhere(), cellStyle.getCsTable());
						CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, CmnStrUtils.getIntVal(CsjDbToolsMsg.coreMsgMap
								.get(CsjDbToolsMsg.MSG_I_0000073)),
								"ORDER BY "+selectSql.getSqlOrder(), cellStyle.getCsTable());
					}

					if (para.isPkey()) {
						tempCs = cellStyle.getCsTitleKey();
					} else {
						tempCs = cellStyle.getCsTitleThin();
					}

					int rowJpNm = rowStep + stepRow;
					if (xlsTblInfo.isHaveJpNm()) {
						CmnPoiUtils.setCellValueWithCs(newSheet, rowJpNm, 0,
								CsjDbToolsMsg.coreMsgMap
										.get(CsjDbToolsMsg.MSG_I_0000002),cellStyle.getCsSignBolder());

						CellStyle syncStyle = tempCs;
						if (CmnStrUtils.isNotEmpty(para.getParaNmJp()) && para.getParaNmJp().equals(para.getParaNmJpSync())) {
							if (para.isPkey()) {
								syncStyle = cellStyle.getCsTitleKey();
							} else {
								syncStyle = cellStyle.getCsTitleThin();
							}
						}

						CmnPoiUtils
								.setCellValueWithCs(newSheet, rowJpNm, colStep,
										para.getParaNmJp(), syncStyle);
					}
					int rowEnNm = rowStep + stepRow + 1;
					CmnPoiUtils.setCellValueWithCs(newSheet, rowEnNm, 0,
							CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000003),cellStyle.getCsSignBolder());
					CmnPoiUtils
							.setCellValueWithCs(newSheet, rowEnNm, colStep,
									para.getParaNmEn(), tempCs);


					ParaCheck paraCheck = para.getParaCheck();

					String cmtInfo = paraCheck.getCmtInfo();
					if (CmnStrUtils.isNotEmpty(cmtInfo)) {
						CmnPoiUtils.setCellCommentBig(newSheet.getRow(rowEnNm)
								.getCell(colStep),
								new HSSFRichTextString(cmtInfo),
								CsjProcess.s_user);
					}



					rowType = rowStep + stepRow + 2;
					CmnPoiUtils.setCellValueWithCs(newSheet, rowType, 0,
							CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000004),cellStyle.getCsSignBolder());
					CmnPoiUtils.setCellValueWithCs(newSheet, rowType, colStep,
							para.getParaTypeWithlen(),
							tempCs);
					String content = CsjConst.JP_TYPE + CsjConst.MASK_TO_RIGHT
							+ para.getParaType() + CsjProcess.s_newLine;

					String tyep = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
					if (DbInfo.STR_DB_TYPE_ORACLE.equals(tyep)
							|| DbInfo.STR_DB_TYPE_POSTGRE.equals(tyep)) {

						// if (para.getType() == DbInfo.TABLE_COL_TYPE_STR) {
						content += CsjConst.JP_LENGTH
								+ CsjConst.MASK_TO_RIGHT
								+ para.getParaLen() + CsjProcess.s_newLine;
						if (CmnStrUtils.isNotEmpty(para.getParaExtra())) {
							content += CsjConst.JP_EXTRA + CsjConst.MASK_TO_RIGHT
									+ para.getParaExtra() + CsjProcess.s_newLine;
						}
						
						// }

					} else {
						content += CsjConst.JP_LENGTH + CsjConst.MASK_TO_RIGHT
								+ para.getParaLen() + CsjProcess.s_newLine;
						if (CmnStrUtils.isNotEmpty(para.getParaExtra())) {
							content += CsjConst.JP_EXTRA + CsjConst.MASK_TO_RIGHT
									+ para.getParaExtra() + CsjProcess.s_newLine;
						}
					}

					CmnPoiUtils.setCellComment(newSheet.getRow(rowType)
							.getCell(colStep), new HSSFRichTextString(content),
							CsjProcess.s_user);
					//
					// CsjPoiUtils.setCommentByCell(
					// newSheet.getRow(rowType).getCell(colStep), content,
					// cellStyle.getFtComment(), csjPart, false,
					// CsjProcess.s_user);

					rowNull = rowStep + stepRow + 3;
					CmnPoiUtils.setCellValueWithCs(newSheet, rowNull, 0,
							CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000005),cellStyle.getCsSignBolder());
					CmnPoiUtils.setCellValueWithCs(newSheet, rowNull, colStep,
							para.isCanNull() ? "Y"
									: "N", tempCs);

					if (CmnStrUtils.isNotEmpty(para.getParaInitVal())) {
						content = CsjConst.JP_INIT_VAL + CsjConst.MASK_TO_RIGHT
								+ para.getParaInitVal() + CsjProcess.s_newLine;
						CmnPoiUtils.setCommentByCell(newSheet.getRow(rowNull)
								.getCell(colStep), content, cellStyle
								.getFtComment(), csjPart, false,
								CsjProcess.s_user);
					}

					colStep++;
				}
				rowStep = rowNull + 1;
				int tIndex = 0;


				if (isHaveMaxMinData) {

					List<XlsTblPara> list = new ArrayList<XlsTblPara>();
					for (Map.Entry<String, TblPara> paraEntry : xlsTblInfo
							.getParaInfoMap().entrySet()) {
						// 最小値
						TblPara para = paraEntry.getValue();
						XlsTblPara xlsTblPara = new XlsTblPara(para);
						if (xlsTblPara.isCanNull() == false
								|| xlsTblPara.isPkey()) {
							if (DbInfo.TABLE_COL_TYPE_NUM == xlsTblPara
									.getType()) {
								xlsTblPara.setParaVal("0");
							} else if (DbInfo.TABLE_COL_TYPE_STR == xlsTblPara
									.getType()) {
								xlsTblPara.setParaVal("x");
							} else {
								xlsTblPara.setParaVal(CmnDateUtil.getFormatDateTime(new Date(), CsjConst.YYYY_MM_DD_SLASH));
							}
						}
						list.add(xlsTblPara);
					}
					CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 0,String.format("%0"+XmlInfoXls.S_WIDTH + "d", ++tIndex)
							,cellStyle.getCsRecordStr());
					colStep = 1;
					for (XlsTblPara xlsPara : list) {
						if (colStep == xmlInfoXls.getMaxCol()) {
							break;
						}
						// CsjPoiUtils.setCellValueWithCs(newSheet, rowStep,
						// colStep++, xlsPara.getParaVal(), cs, csBlank);
						CmnPoiUtils.setCellValueWithCs(newSheet, rowStep,
								colStep++, xlsPara, cellStyle);
					}
					rowStep++;

					list.clear();
					for (Map.Entry<String, TblPara> paraEntry : xlsTblInfo
							.getParaInfoMap().entrySet()) {
						// 最大値
						TblPara para = paraEntry.getValue();
						XlsTblPara xlsTblPara = new XlsTblPara(para);
						if (DbInfo.TABLE_COL_TYPE_NUM == xlsTblPara.getType()) {
							String pre = CmnStrUtils.leftFillCharWhithByte("",
									"9", xlsTblPara.getParaLen()-xlsTblPara.getParaNumDotEndLen());
							String aft = CmnStrUtils.leftFillCharWhithByte("",
									"9", xlsTblPara.getParaNumDotEndLen());
							if (CmnStrUtils.isNotEmpty(aft)) {
								aft = "." + aft;
							}

							xlsTblPara.setParaVal(pre + aft);
						} else if (DbInfo.TABLE_COL_TYPE_STR == xlsTblPara
								.getType()) {
							xlsTblPara.setParaVal(CmnStrUtils
									.leftFillCharWhithByte("", "w",
											xlsTblPara.getParaLen()));
						} else {
							xlsTblPara.setParaVal(CmnDateUtil.getFormatDateTime(new Date(), CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24));
						}
						list.add(xlsTblPara);
					}

					CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 0,
							String.format("%0"+XmlInfoXls.S_WIDTH + "d", ++tIndex),cellStyle.getCsRecordStr());
					colStep = 1;
					for (XlsTblPara xlsPara : list) {
						if (colStep == xmlInfoXls.getMaxCol()) {
							break;
						}
						// CsjPoiUtils.setCellValueWithCs(newSheet, rowStep,
						// colStep++, xlsPara.getParaVal(), cs, csBlank);
						CmnPoiUtils.setCellValueWithCs(newSheet, rowStep,
								colStep++, xlsPara, cellStyle);
					}
					rowStep++;
				}

				for (int i = 0; i < xlsTblInfo.getTblDataList().size(); i++) {
					XlsRecord xlsRec = xlsTblInfo.getTblDataList().get(i);
					CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 0,
							String.format("%0"+XmlInfoXls.S_WIDTH + "d", ++tIndex),cellStyle.getCsRecordStr());
					colStep = 1;
					for (XlsTblPara xlsPara : xlsRec.getRecord()) {
						if (colStep == xmlInfoXls.getMaxCol()) {
							break;
						}
						// CsjPoiUtils.setCellValueWithCs(newSheet, rowStep,
						// colStep++, xlsPara.getParaVal(), cs, csBlank);
						CmnPoiUtils.setCellValueWithCs(newSheet, rowStep,
								colStep++, xlsPara, cellStyle);
					}
					rowStep++;
				}

				rowStep++;
			}
		} catch (Throwable e) {
			throw new Exception(e.getMessage()+"-->table name is "+ tblNm);
		}
		CmnLog.logger.debug("AutoDbToXls. writeToXlsByDataBaseWithIni(SheetTblsInfo tblsInfo,DbInfo dbInfo, HSSFSheet newSheet, CsjDbCellStyle cellStyle) end");
	}

	private static void writeToXlsByDataBase(SheetTblsInfo tblsInfo,
			XmlDbXlsInfoAll xmlDbXlsInfoAll) throws Throwable {
		CmnLog.logger
				.debug("AutoDbToXls.writeToXlsByDataBase(SheetTblsInfo tblsInfo,DbInfo dbInfo) begin");
		FileInputStream fileIn = null;
		FileOutputStream fileOut = null;
		XmlInfoXls xmlInfoXls = xmlDbXlsInfoAll.getXmlInfoXls();

		String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();

		try {
			for (Map.Entry<String, XlsTblInfo> entry : tblsInfo.getTblInfoMap()
					.entrySet()) {
				String excelType = xmlDbXlsInfoAll.getXmlInfoXls().getExcelType();
				fileIn = new FileInputStream(CsjPath.s_file_pj_path_temp+excelType);
				Workbook wb = null;
				if (CsjConst.EXCEL_DOT_XLS_1997.equals(excelType)) {
					wb = new HSSFWorkbook(fileIn);
				} else {
					wb = new XSSFWorkbook(fileIn);
				}

				CsjDbCellStyle cellStyle = new CsjDbCellStyle(wb,excelType,CsjDbCellStyle.CS_DATA_REC);

				String tblNm = entry.getKey();

				if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {
					tblNm = CmnStrUtils.fromAtoBByTrim(tblNm, "#", CsjPath.s_current_date_end);
				}
				Sheet newSheet = wb.getSheet("temp");// wb.createSheet(tblNm);
				wb.setSheetName(wb.getSheetIndex(newSheet.getSheetName()),
						tblNm);

				CmnLog.logger.info("table:" + tblNm);

				XlsTblInfo xlsTblInfo = entry.getValue();
				// CsjPatr csjPart = new CsjPatr(newSheet, 2, 10, 6, 15);
				int colStep = 1;
				int rowStep = 0;
				int recordNumIndex = 0;
				if (s_is_max_data) {
					recordNumIndex = record_Num_Index;
				}

				int stepRow = 0;

				xlsTblInfo.setHaveJpNm(true);
				if (xlsTblInfo.isHaveJpNm()) {
					stepRow = 1;
				}

				CellStyle tempCs = null;
				for (Map.Entry<String, TblPara> paraEntry : xlsTblInfo
						.getParaInfoMap().entrySet()) {
					TblPara para = paraEntry.getValue();

					CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 0,
							xmlInfoXls.getTblSign(), cellStyle.getCsTableBolder());
					CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 1, tblNm,
							cellStyle.getCsTableBolder());
					CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 2,
							xlsTblInfo.getTblNmJp(), cellStyle.getCsTableBolder());

					CsjLinkedMap<String, CsjTblSelectSql> selectSqlMapWithTblnm = xmlDbXlsInfoAll.getDbInfo().getSelectSqlMapWithTblnm();
					CsjTblSelectSql selectSql = selectSqlMapWithTblnm.get(tblNm);
					if (selectSql != null) {
						CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, CmnStrUtils.getIntVal(CsjDbToolsMsg.coreMsgMap
								.get(CsjDbToolsMsg.MSG_I_0000072)),
								"WHERE "+selectSql.getSqlWhere(), cellStyle.getCsTable());
						CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, CmnStrUtils.getIntVal(CsjDbToolsMsg.coreMsgMap
								.get(CsjDbToolsMsg.MSG_I_0000073)),
								"ORDER BY "+selectSql.getSqlOrder(), cellStyle.getCsTable());
					}

					if (para.isPkey()) {
						tempCs = cellStyle.getCsTitleKey();
					} else {
						tempCs = cellStyle.getCsTitleThin();
					}

					int rowJpNm = rowStep + stepRow;
					if (xlsTblInfo.isHaveJpNm()) {
						CmnPoiUtils.setCellValueWithCs(newSheet, rowJpNm, 0,
								CsjDbToolsMsg.coreMsgMap
										.get(CsjDbToolsMsg.MSG_I_0000002),cellStyle.getCsSignBolder());

						CellStyle syncStyle = tempCs;
						if (CmnStrUtils.isNotEmpty(para.getParaNmJp()) && para.getParaNmJp().equals(para.getParaNmJpSync())) {
							if (para.isPkey()) {
								syncStyle = cellStyle.getCsTitleKey();
							} else {
								syncStyle = cellStyle.getCsTitleThin();
							}
						}

						CmnPoiUtils
								.setCellValueWithCs(newSheet, rowJpNm, colStep,
										para.getParaNmJp(), syncStyle);
					}
					int rowEnNm = rowStep + stepRow + 1;
					CmnPoiUtils.setCellValueWithCs(newSheet, rowEnNm, 0,
							CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000003),cellStyle.getCsSignBolder());
					CmnPoiUtils
							.setCellValueWithCs(newSheet, rowEnNm, colStep,
									para.getParaNmEn(), tempCs);

					ParaCheck paraCheck = para.getParaCheck();

					String cmtInfo = paraCheck.getCmtInfo();
					if (CmnStrUtils.isNotEmpty(cmtInfo)) {
						CmnPoiUtils.setCellCommentBig(newSheet.getRow(rowEnNm)
								.getCell(colStep),
								new HSSFRichTextString(cmtInfo),
								CsjProcess.s_user);
					}

					int rowType = rowStep + stepRow + 2;
					CmnPoiUtils.setCellValueWithCs(newSheet, rowType, 0,
							CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000004),cellStyle.getCsSignBolder());
					CmnPoiUtils.setCellValueWithCs(newSheet, rowType, colStep,
							para.getParaTypeWithlen(),
							tempCs);
					String content = CsjConst.JP_TYPE + CsjConst.MASK_TO_RIGHT
							+ para.getParaType() + CsjProcess.s_newLine;

					//String type = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
//					if (DbInfo.STR_DB_TYPE_ORACLE.equals(type)
//							|| DbInfo.STR_DB_TYPE_POSTGRE.equals(type)) {
//
//						// if (para.getType() == DbInfo.TABLE_COL_TYPE_STR) {
//						content += CsjConst.JP_LENGTH + CsjConst.MASK_TO_RIGHT
//									+ para.getParaLen() + CsjProcess.s_newLine;
//						// }
//
//					} else {
						content += CsjConst.JP_LENGTH + CsjConst.MASK_TO_RIGHT
								+ para.getParaLen() + CsjProcess.s_newLine;
						if (CmnStrUtils.isNotEmpty(para.getParaExtra())) {
							content += CsjConst.JP_EXTRA + CsjConst.MASK_TO_RIGHT
									+ para.getParaExtra() + CsjProcess.s_newLine;
						}
//					}

					CmnPoiUtils.setCellComment(newSheet.getRow(rowType)
							.getCell(colStep), new HSSFRichTextString(content),
							CsjProcess.s_user);

					// CsjPoiUtils.setCommentByCell(newSheet.getRow(rowType)
					// .getCell(colStep), content, cellStyle
					// .getFtComment(), csjPart, false, CsjProcess.s_user);

					int rowNull = rowStep + stepRow + 3;
					CmnPoiUtils
							.setCellValueWithCs(newSheet, rowNull, 0,
									CsjDbToolsMsg.coreMsgMap
											.get(CsjDbToolsMsg.MSG_I_0000005),cellStyle.getCsSignBolder());
					CmnPoiUtils.setCellValueWithCs(newSheet, rowNull, colStep,
							para.isCanNull() ? "Y"
									: "N", tempCs);

					if (CmnStrUtils.isNotEmpty(para.getParaInitVal())) {
						content = CsjConst.JP_INIT_VAL + CsjConst.MASK_TO_RIGHT
								+ para.getParaInitVal() + CsjProcess.s_newLine;

						CmnPoiUtils.setCellComment(newSheet.getRow(rowNull)
								.getCell(colStep), new HSSFRichTextString(
								content), CsjProcess.s_user);
						// CsjPoiUtils.setCommentByCell(newSheet.getRow(rowNull)
						// .getCell(colStep), content, cellStyle
						// .getFtComment(), csjPart, false,
						// CsjProcess.s_user);
					}

					colStep++;
					if (colStep == xmlInfoXls.getMaxCol()) {
						CmnLog.logger.debug(CsjDbToolsMsg.coreMsgMap
								.get(CsjDbToolsMsg.MSG_I_0000068)
								+ tblNm);
						break;
					}
				}
				rowStep = 4;
				if (xlsTblInfo.isHaveJpNm()) {
					rowStep = 5;
				}

				long listSize = xlsTblInfo.getTblDataList().size();

				for (int i = 0; i < listSize; i++) {
					XlsRecord xlsRec = xlsTblInfo.getTblDataList().get(i);
					CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 0,String.format("%0"+XmlInfoXls.S_WIDTH + "d", ++recordNumIndex)
							,cellStyle.getCsRecordStr());
					colStep = 1;
					for (XlsTblPara xlsPara : xlsRec.getRecord()) {
						if (colStep == xmlInfoXls
								.getMaxCol()) {
							break;
						}
						// CsjPoiUtils.setCellValueWithCs(newSheet, rowStep,
						// colStep++, xlsPara.getParaVal(), cs, csBlank);
						CmnPoiUtils.setCellValueWithCs(newSheet, rowStep,
								colStep++, xlsPara, cellStyle);
					}
					rowStep++;
				}

				File f = new File(xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath());
				f.mkdirs();

				String fNm = tblNm;
				if (CmnStrUtils.isNotEmpty(xlsTblInfo.getTblNmJp())) {
					fNm += "[" + xlsTblInfo.getTblNmJp() + "]";
				}
				fNm = CmnStrUtils.getStrForFileNm(fNm);
				String strBetweenNum = "";
				if (s_is_max_data) {
					strBetweenNum = "(" + (recordNumIndexPre + 1) + "-" + recordNumIndex + ")";
					record_Num_Index = recordNumIndex;
				}
				String filePath = xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath()+ fNm + strBetweenNum;
				
				if (filePath.length()>IConstFile.FILE_MAX_LEN) {
					filePath = xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath()+strBetweenNum+ fNm ;
					filePath = CmnStrUtils.getStrForLength(filePath, IConstFile.FILE_MAX_LEN);
				}
				
				fileOut = new FileOutputStream(filePath + excelType);

				wb.write(fileOut);
				fileOut.close();
				fileIn.close();
			}

		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		} finally {
			if (fileOut != null)
				fileOut.close();
			if (fileIn != null)
				fileIn.close();
		}
		CmnLog.logger
				.debug("AutoDbToXls.writeToXlsByDataBase(SheetTblsInfo tblsInfo,DbInfo dbInfo) end");
	}

	private static String getOracleTalbeData(
			String tblNm, XlsTblInfo xlsTblInfo,
			List<Object> paraList, XmlDbXlsInfoAll xmlDbXlsInfoAll,int runDataType) throws Throwable {
		CmnLog.logger
				.debug("AutoDbToXls.getOracleTalbeData(SheetTblsInfo tblsInfo,String tblNm, long rownum, XlsTblInfo xlsTblInfo,List<Object> paraList) begin");
		StringBuffer sb = new StringBuffer();
		StringBuffer tmpSb = new StringBuffer();

		XmlInfoXls xmlInfoXls = xmlDbXlsInfoAll.getXmlInfoXls();
		long rownum = CmnStrUtils.getLongVal(xmlInfoXls.getMaxRow());
		try {
			LinkedHashMap<String, TblPara> paraInfoMap = xlsTblInfo
					.getParaInfoMap();

			tmpSb.append(" select ");
			tmpSb.append(getDistinctSql(xmlDbXlsInfoAll, tblNm));
			tmpSb.append(xlsTblInfo.getSelectStr());
			tmpSb.append(" from ");
			tmpSb.append(xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot());
			tmpSb.append("\""+tblNm + "\" t ");
			tmpSb.append(" where 1=1 ");
			
			if (RUN_DATA_OUT_SPLIT == runDataType) {

				sb.append("SELECT * FROM ( SELECT A.*, ROWNUM XXXROWNUMXXX FROM (");
				sb.append(tmpSb.toString());

				sb.append(getWhereBySelectSql(xmlDbXlsInfoAll,tblNm));
				sb.append(") A ) WHERE 1=1 ");
				sb.append(" AND XXXROWNUMXXX BETWEEN "
					+ (recordNumIndexPre + 1) + " AND "
					+ (recordNumIndexPre + rownum));
			} else {
				sb.append(tmpSb.toString());
				sb.append(getWhereBySelectSql(xmlDbXlsInfoAll,tblNm));
			}

		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger
				.debug("AutoDbToXls.getOracleTalbeData(SheetTblsInfo tblsInfo,String tblNm, long rownum, XlsTblInfo xlsTblInfo,List<Object> paraList) end");
		return sb.toString();
	}

	private static String getPostgreTalbeData(
			String tblNm, XlsTblInfo xlsTblInfo,
			List<Object> paraList, XmlDbXlsInfoAll xmlDbXlsInfoAll,int runDataType) throws Throwable {
		CmnLog.logger
				.debug("getPostgreTalbeData(SheetTblsInfo tblsInfo,String tblNm, XlsTblInfo xlsTblInfo,List<Object> paraList) begin");

		StringBuffer sb = new StringBuffer();
		XmlInfoXls xmlInfoXls = xmlDbXlsInfoAll.getXmlInfoXls();
		long rownum = CmnStrUtils.getLongVal(xmlInfoXls.getMaxRow());
		try {
			
			sb.append(" select ");
			sb.append(getDistinctSql(xmlDbXlsInfoAll, tblNm));
			sb.append(xlsTblInfo.getSelectStr());
			sb.append(" from ");
			sb.append(xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot());
			sb.append("\"" + tblNm + "\" t ");
			sb.append(" where 1=1 ");
			
			if (RUN_DATA_OUT_SPLIT == runDataType) {
				sb.append(getWhereBySelectSql(xmlDbXlsInfoAll,tblNm));
				sb.append(" LIMIT " + rownum + " OFFSET " + recordNumIndexPre);
			} else {
				sb.append(getWhereBySelectSql(xmlDbXlsInfoAll,tblNm));
			}

		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger
				.debug("getPostgreTalbeData(SheetTblsInfo tblsInfo,String tblNm, XlsTblInfo xlsTblInfo,List<Object> paraList) end");
		return sb.toString();
	}

	private static String getMysqlTalbeData(
			String tblNm, XlsTblInfo xlsTblInfo,
			List<Object> paraList, XmlDbXlsInfoAll xmlDbXlsInfoAll,int runDataType) throws Throwable {

		CmnLog.logger
				.debug("AutoDbToXls.getMysqlTalbeData(SheetTblsInfo tblsInfo,String tblNm, XlsTblInfo xlsTblInfo,List<Object> paraList) begin");
		StringBuffer sb = new StringBuffer();
		XmlInfoXls xmlInfoXls = xmlDbXlsInfoAll.getXmlInfoXls();
		long rownum = CmnStrUtils.getLongVal(xmlInfoXls.getMaxRow());
		try {

			sb.append(" select ");
			sb.append(getDistinctSql(xmlDbXlsInfoAll, tblNm));
			sb.append(xlsTblInfo.getSelectStr());
			sb.append(" from ");
			sb.append(xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot());
			sb.append("\"" + tblNm + "\" t ");
			sb.append(" where 1=1 ");

			if (RUN_DATA_OUT_SPLIT == runDataType) {
				sb.append(getWhereBySelectSql(xmlDbXlsInfoAll,tblNm));
				sb.append(" LIMIT " + (recordNumIndexPre) + ", " + rownum);
			} else {
				sb.append(getWhereBySelectSql(xmlDbXlsInfoAll,tblNm));
			}

		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger
				.debug("AutoDbToXls.getMysqlTalbeData(SheetTblsInfo tblsInfo,String tblNm, XlsTblInfo xlsTblInfo,List<Object> paraList) end");
		return sb.toString();
	}

	private static String getSqlLiteTalbeData(
			String tblNm, XlsTblInfo xlsTblInfo,
			List<Object> paraList, XmlDbXlsInfoAll xmlDbXlsInfoAll,int runDataType) throws Throwable {

		CmnLog.logger
				.debug("AutoDbToXls.getSqlLiteTalbeData(SheetTblsInfo tblsInfo,String tblNm, XlsTblInfo xlsTblInfo,List<Object> paraList) begin");
		StringBuffer sb = new StringBuffer();
		XmlInfoXls xmlInfoXls = xmlDbXlsInfoAll.getXmlInfoXls();
		long rownum = CmnStrUtils.getLongVal(xmlInfoXls.getMaxRow());
		try {

			sb.append(" select ");
			sb.append(getDistinctSql(xmlDbXlsInfoAll, tblNm));
			sb.append(xlsTblInfo.getSelectStr());
			sb.append(" from ");
			sb.append(xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot());
			sb.append("\"" + tblNm + "\" t ");
			sb.append(" where 1=1 ");

			if (RUN_DATA_OUT_SPLIT == runDataType) {
				sb.append(getWhereBySelectSql(xmlDbXlsInfoAll,tblNm));
				sb.append(" LIMIT " + (recordNumIndexPre) + ", " + rownum);
			} else {
				sb.append(getWhereBySelectSql(xmlDbXlsInfoAll,tblNm));
			}

		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger
				.debug("AutoDbToXls.getSqlLiteTalbeData(SheetTblsInfo tblsInfo,String tblNm, XlsTblInfo xlsTblInfo,List<Object> paraList) end");
		return sb.toString();
	}
	private static String getSybaseTalbeData(
			String tblNm, XlsTblInfo xlsTblInfo,
			List<Object> paraList, XmlDbXlsInfoAll xmlDbXlsInfoAll,int runDataType) throws Throwable {

		CmnLog.logger
				.debug("AutoDbToXls.getSybaseTalbeData(SheetTblsInfo tblsInfo,String tblNm, XlsTblInfo xlsTblInfo,List<Object> paraList) begin");
		StringBuffer sb = new StringBuffer();
		XmlInfoXls xmlInfoXls = xmlDbXlsInfoAll.getXmlInfoXls();
		long rownum = CmnStrUtils.getLongVal(xmlInfoXls.getMaxRow());
		try {
			if (RUN_DATA_OUT_SPLIT == runDataType) {
				sb.append(" select ");
				sb.append(xlsTblInfo.getSelectStr());
				sb.append(" from ");
				sb.append(xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot());
				if (tblNm.startsWith("#")) {
					sb.append(tblNm);
				} else {
					sb.append("\"" + tblNm + "\" t ");
				}

				sb.append(" where 1=1 ");
				sb.append(" AND XXXROWNUMXXX BETWEEN "
						+ (recordNumIndexPre + 1) + " AND "
						+ (recordNumIndexPre + rownum));
				sb.append(getWhereBySelectSql(xmlDbXlsInfoAll,tblNm));
				
			} else {
				sb.append(" select top " + xmlInfoXls.getMaxRow()+ " ");
				
				sb.append(xlsTblInfo.getSelectStr());
				sb.append(" from ");
				sb.append(xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot());
				if (tblNm.startsWith("#")) {
					sb.append(tblNm);
				} else {
					sb.append("\"" + tblNm + "\" t ");
				}

				sb.append(" where 1=1 ");
				sb.append(getWhereBySelectSql(xmlDbXlsInfoAll,tblNm));
			}

		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger
				.debug("AutoDbToXls.getSybaseTalbeData(SheetTblsInfo tblsInfo,String tblNm, XlsTblInfo xlsTblInfo,List<Object> paraList) end");
		return sb.toString();
	}
	
	private static String getDb2TalbeData(
			String tblNm,XlsTblInfo xlsTblInfo,
			List<Object> paraList, XmlDbXlsInfoAll xmlDbXlsInfoAll,int runDataType) throws Throwable {
		CmnLog.logger
				.debug("AutoDbToXls.getDb2TalbeData(SheetTblsInfo tblsInfo,String tblNm, XlsTblInfo xlsTblInfo,List<Object> paraList) begin");
		StringBuffer sb = new StringBuffer();
		CsjLinkedMap<String, CsjTblSelectSql> selectSqlMapWithTblnm = xmlDbXlsInfoAll.getDbInfo().getSelectSqlMapWithTblnm();
		XmlInfoXls xmlInfoXls = xmlDbXlsInfoAll.getXmlInfoXls();
		long rownum = CmnStrUtils.getLongVal(xmlInfoXls.getMaxRow());
		try {
			CsjTblSelectSql selectSql = selectSqlMapWithTblnm.get(tblNm);
			if (RUN_DATA_OUT_SPLIT == runDataType) {
				
				sb.append("SELECT * FROM (");
				sb.append(" SELECT ");
				sb.append(" ROW_NUMBER() OVER(  ");
				
				if (selectSql == null || CmnStrUtils.isEmpty(selectSql.getOrderBySql())) {
					sb.append(" ORDER BY 1 ");
				} else {
					sb.append(selectSql.getOrderBySql());
				}
				
				sb.append(" ) AS XXXROWNUMXXX, ");
				sb.append(xlsTblInfo.getSelectStr());
				sb.append(" from ");
				sb.append(xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot());
				sb.append("\"" + tblNm + "\" t ) ");
				sb.append(" where 1=1 ");
				if (selectSqlMapWithTblnm.containsKey(tblNm)) {
					sb.append(selectSql.getWhereSql());
				}
				sb.append(" AND XXXROWNUMXXX >= " + String.valueOf(recordNumIndexPre+1) + " AND XXXROWNUMXXX <= " + String.valueOf(recordNumIndexPre + rownum));
			} else {
				sb.append(" SELECT ");
				sb.append(xlsTblInfo.getSelectStr());
				sb.append(" from ");
				sb.append(xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot());
				sb.append("\"" + tblNm + "\" t  ");
				sb.append(" where 1=1 ");
				if (selectSqlMapWithTblnm.containsKey(tblNm)) {
					sb.append(selectSql.getWhereSql());
				}
				
				if (CmnStrUtils.isNotEmpty(selectSql.getOrderBySql())) {
					sb.append(selectSql.getOrderBySql());
				}
			}
			
			sb.append(" WITH UR ");

		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger
				.debug("AutoDbToXls.getDb2TalbeData(SheetTblsInfo tblsInfo,String tblNm,  XlsTblInfo xlsTblInfo,List<Object> paraList) end");
		return sb.toString();
	}

	private static String getSqlmicroTalbeData(
			String tblNm, XlsTblInfo xlsTblInfo,
			List<Object> paraList, XmlDbXlsInfoAll xmlDbXlsInfoAll,int runDataType) throws Throwable {
		CmnLog.logger
				.debug("AutoDbToXls.getSqlmicro2005TalbeData(SheetTblsInfo tblsInfo,String tblNm, XlsTblInfo xlsTblInfo,List<Object> paraList) begin");
		StringBuffer sb = new StringBuffer();
		XmlInfoXls xmlInfoXls = xmlDbXlsInfoAll.getXmlInfoXls();
		long rownum = CmnStrUtils.getLongVal(xmlInfoXls.getMaxRow());

		CsjLinkedMap<String, CsjTblSelectSql> selectSqlMapWithTblnm = xmlDbXlsInfoAll.getDbInfo().getSelectSqlMapWithTblnm();
		try {
			LinkedHashMap<String, TblPara> paraInfoMap = xlsTblInfo
					.getParaInfoMap();
			CsjTblSelectSql selectSql = selectSqlMapWithTblnm.get(tblNm);
			if (RUN_DATA_OUT_SPLIT == runDataType) {

				sb.append("select * from ( select ");

				int i = 0;
				String orderBy = "";

				StringBuffer sbParaEnOrder = new StringBuffer();
				for (Entry<String, TblPara> entry : paraInfoMap.entrySet()) {
					TblPara tblPara = entry.getValue();
					if (i == 0) {
						sbParaEnOrder.append(tblPara.getParaNmEn());
					}
					i++;
					String paraType = CmnStrUtils.toLowOrUpStr(tblPara.getParaType());
					if (paraType.equals(CsjSqlServerColType.TYPE_BINARY)
							// || paraType.equals(CsjSqlServerColType.TYPE_BIT)
							|| paraType
									.equals(CsjSqlServerColType.TYPE_DATETIMEOFFSET)
							|| paraType.equals(CsjSqlServerColType.TYPE_GEOGRAPHY)
							|| paraType.equals(CsjSqlServerColType.TYPE_GEOMETRY)
							|| paraType
									.equals(CsjSqlServerColType.TYPE_HIERARCHYID)
							|| paraType
									.equals(CsjSqlServerColType.TYPE_SQL_VARIANT)
							|| paraType.equals(CsjSqlServerColType.TYPE_TIMESTAMP)
							|| paraType
									.equals(CsjSqlServerColType.TYPE_UNIQUEIDENTIFIER)

					) {
						sb.append("CAST("
								+ CmnStrUtils.addLRSign(tblPara.getParaNmEn(),
										CsjConst.SIGN_DOUBLE)
								+ " AS varchar(max)) as "
								+ tblPara.getParaNmEn());
					} else {
						sb.append(CmnStrUtils.addLRSign(tblPara.getParaNmEn(),
								CsjConst.SIGN_DOUBLE));
					}
					if (tblPara.isLastPara()) {
					} else {
						sb.append(", ");
					}
				}

				if (selectSqlMapWithTblnm.containsKey(tblNm)) {
					orderBy = selectSql.getOrderBySql();
				}
				if (CmnStrUtils.isEmpty(orderBy)) {
					orderBy = " ORDER BY ["+sbParaEnOrder.toString()+"] ";
				}

				sb.append(", row_number() over( " + orderBy
						+ ") as XXXROWNUMXXX ");
				sb.append(" from ");

				sb.append(xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot());
				sb.append(" \""+tblNm+"\" ");
				sb.append(" where 1=1 ");

				if (selectSqlMapWithTblnm.containsKey(tblNm)) {
					sb.append(selectSql.getWhereSql());
				}

				sb.append(") t  where 1=1 ");
				if (runDataType == RUN_DATA_OUT_SPLIT) {
					sb.append(" AND t.XXXROWNUMXXX between "
						+ (recordNumIndexPre + 1) + " and "
						+ (recordNumIndexPre + rownum));
				}
			} else {
				sb.append(" select ");
				sb.append(getDistinctSql(xmlDbXlsInfoAll, tblNm));
				sb.append(xlsTblInfo.getSelectStr());
				sb.append(" from ");
				sb.append(xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot());
				sb.append("\"" + tblNm + "\" t ");
				sb.append(" where 1=1 ");
				if (selectSqlMapWithTblnm.containsKey(tblNm)) {
					sb.append(selectSql.getWhereSql());
				}
				if (selectSqlMapWithTblnm.containsKey(tblNm)) {
					sb.append(selectSql.getOrderBySql());
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger
				.debug("AutoDbToXls.getSqlmicro2005TalbeData(SheetTblsInfo tblsInfo,String tblNm,  XlsTblInfo xlsTblInfo,List<Object> paraList) end");
		return sb.toString();
	}

	/**
	 * @param runDataType
	 *
	 */
	public static String getDistinctSql(XmlDbXlsInfoAll xmlDbXlsInfoAll,String tblNm) {
		String retStr = "";
		// TODOSAI
		if (xmlDbXlsInfoAll.getDbInfo().getSelectSqlMapWithTblnm().containsKey(tblNm)) {
			CsjTblSelectSql selectSql = xmlDbXlsInfoAll.getDbInfo().getSelectSqlMapWithTblnm().get(tblNm);
			retStr = selectSql.getSqlDistinct();
		}
		return retStr;
	}
	
	/**
	 * @param runDataType
	 *
	 */
	public static String getWhereBySelectSql(XmlDbXlsInfoAll xmlDbXlsInfoAll,String tblNm) {
		String retStr = "";
		// TODOSAI
		if (xmlDbXlsInfoAll.getDbInfo().getSelectSqlMapWithTblnm().containsKey(tblNm)) {
			CsjTblSelectSql selectSql = xmlDbXlsInfoAll.getDbInfo().getSelectSqlMapWithTblnm().get(tblNm);
			retStr = selectSql.getSelectSql(xmlDbXlsInfoAll.getDbInfo().getTblMap().get(tblNm).getSelectStr(),xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot(), false,false);
		}
		return retStr;
	}


	// getSybaseTablesInfoSql

	private static String getSybaseTablesInfoSql(String tblNm)
			throws Throwable {
		CmnLog.logger
				.debug("AutoDbToXls.getSybaseTablesInfoSql(Object... objs) begin");
		StringBuffer sb = new StringBuffer();
		try {

			String tableNm = CmnStrUtils.fromAtoBByTrim(tblNm, "(", ")");
			String[] tableNms = tableNm.split(",");


			sb.append(" SELECT ");
			sb.append("     t.TBL_NM_EN, ");
			sb.append("     t.COL_ID, ");
			//sb.append("     t.COL_NM_JP, ");
			sb.append("     t.COL_NM_EN, ");
			sb.append("     t.COL_DEFAULT, ");
			sb.append("     t.COL_TYPE, ");
			sb.append("     t.COL_TYPE_INFO, ");


			sb.append("     t.LENGTH, ");
			sb.append("     t.PRECISION, ");
			sb.append("     t.SCALE, ");
			sb.append("     (case when t.COL_NM_EN = keyinfo.keyname then 'Y' else 'N' end) AS COL_IS_PK, ");
			sb.append("     t.COL_CAN_NULL ");
			sb.append(" FROM ");
			sb.append("     ( ");
			sb.append("         SELECT ");
			sb.append("             o.name AS TBL_NM_EN, ");
			sb.append("             c.colid AS COL_ID, ");
			sb.append("             '' AS COL_NM_JP, ");
			sb.append("             c.name AS COL_NM_EN, ");
			sb.append("             cmt.text AS COL_DEFAULT, ");
			sb.append("             t.name AS COL_TYPE, ");

			sb.append("     (case ");
			sb.append("     WHEN c.prec is not null and c.scale is not null THEN t.name||'('||convert(varchar, c.prec)||','||convert(varchar, c.scale)||')' ");
			sb.append("     WHEN c.prec is not null and c.scale is null THEN  t.name||'('||convert(varchar, c.prec)||')' ");
			sb.append("     WHEN t.name like '%time%' or t.name like '%date%' THEN  t.name ");
			sb.append("     WHEN c.length is not null THEN  t.name||'('||convert(varchar, c.length)||')' ");
			sb.append("     else t.name  ");
			sb.append("      end) AS COL_TYPE_INFO,  ");


			sb.append("             c.prec AS PRECISION, ");
			sb.append("             c.scale AS SCALE, ");
			sb.append("             c.length AS LENGTH, ");
			sb.append("             (case when c.status =8 then 'Y' else 'N' end) AS COL_CAN_NULL ");
			sb.append("         FROM ");
			sb.append("             dbo.sysobjects o, ");
			sb.append("             dbo.syscolumns c ");
			sb.append("         LEFT OUTER JOIN ");
			sb.append("             dbo.syscomments cmt ");
			sb.append("         ON ");
			sb.append("                 c.cdefault = cmt.id, ");
			sb.append("             dbo.systypes t ");
			sb.append("         WHERE ");
			sb.append("                 o.id = c.id ");
			sb.append("             AND o.type = 'U' ");
			sb.append("             AND c.usertype = t.usertype ");
			sb.append("             AND o.name in ");
			sb.append(tblNm);
			sb.append(" ");
			sb.append("     ) t ");
			sb.append(" LEFT OUTER JOIN ");
			sb.append("     ( ");


			sb.append("         SELECT * FROM (");
			for (int i = 0; i < tableNms.length; i++) {
				String key = tableNms[i];

				for (int j = 1; j <= 2; j++) {
					sb.append("         SELECT ");
					sb.append("             "+key+""+" AS tblNm, ");
					sb.append("             index_col("+key+",1,"+j+") AS keyname, ");
					sb.append("             "+j+" AS keypos ");
					if (j<2) {
						sb.append("         UNION ALL ");
					}
				}
				if (i+1<tableNms.length) {
					sb.append("         UNION ALL ");
				}
			}


			sb.append("    ) aa WHERE aa.keyname IS NOT NULL ) keyinfo ON t.COL_NM_EN = keyinfo.keyname ");
			sb.append("     ORDER BY t.TBL_NM_EN,t.COL_ID ");
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger
				.debug("AutoDbToXls.getSybaseTablesInfoSql(Object... objs) end");
		return sb.toString();
}

	private static String getOracleTablesInfoSql(Object... objs)
			throws Throwable {
		CmnLog.logger
				.debug("AutoDbToXls.getOracleTablesInfoSql(Object... objs) begin");
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(" SELECT T_CMT.TABLE_TYPE TABLE_TYPE_INFO,    ");
			sb.append("   T_CMT.COMMENTS TBL_NM_JP, ");
			sb.append("   T_CMT.TABLE_NAME TBL_NM_EN,   ");
			sb.append("   T_COL.COLUMN_ID COL_ID,   ");
			sb.append("   T_COL_CMT.COMMENTS COL_NM_JP, ");
			sb.append("   T_COL.COLUMN_NAME COL_NM_EN,  ");
			sb.append("   T_COL.DATA_TYPE COL_TYPE, ");
			sb.append("   DATA_TYPE ");
			sb.append("   ||DECODE(T_COL.DATA_TYPE,'DATE','','CLOB','','BLOB','','BFILE','','FLOAT','','LONG RAW','','LONG','','RAW','('    ");
			sb.append("   || TO_CHAR(T_COL.DATA_LENGTH) ");
			sb.append("   || ')', (DECODE(SIGN(INSTR(T_COL.DATA_TYPE, 'CHAR')),1, '('   ");
			sb.append("   || TO_CHAR(T_COL.DATA_LENGTH) ");
			sb.append("   || ')',(DECODE(SUBSTR(T_COL.DATA_TYPE, 1, 9), 'TIMESTAMP', '', (DECODE(NVL(T_COL.DATA_PRECISION, -1), -1, '',(DECODE(NVL(T_COL.DATA_SCALE, 0), 0, '(' ");
			sb.append("   || TO_CHAR(T_COL.DATA_PRECISION)  ");
			sb.append("   || ')', '('   ");
			sb.append("   || TO_CHAR(T_COL.DATA_PRECISION)  ");
			sb.append("   || ','    ");
			sb.append("   || TO_CHAR(T_COL.DATA_SCALE)  ");
			sb.append("   || ')'))))))))) COL_TYPE_INFO,    ");
			sb.append("   CASE  ");
			sb.append("     WHEN T_PK.COLUMN_NAME IS NULL   ");
			sb.append("     THEN 'N'    ");
			sb.append("     ELSE 'Y'    ");
			sb.append("   END COL_IS_PK,    ");
			sb.append("   T_COL.NULLABLE COL_CAN_NULL,  ");
			sb.append("   T_COL.DATA_DEFAULT COL_DEFAULT    ");
			sb.append(" FROM USER_TAB_COLUMNS T_COL,    ");
			sb.append("   USER_COL_COMMENTS T_COL_CMT,  ");
			sb.append("   USER_TAB_COMMENTS T_CMT,  ");
			sb.append("   (SELECT UC.TABLE_NAME,    ");
			sb.append("     UCC.COLUMN_NAME ");
			sb.append("   FROM USER_CONSTRAINTS UC, ");
			sb.append("     USER_CONS_COLUMNS UCC   ");
			sb.append("   WHERE UC.CONSTRAINT_NAME = UCC.CONSTRAINT_NAME    ");
			sb.append("   AND UC.CONSTRAINT_TYPE   = 'P'    ");
			sb.append("   ) T_PK    ");
			sb.append(" WHERE T_COL.TABLE_NAME =T_COL_CMT.TABLE_NAME    ");
			sb.append(" AND T_COL.TABLE_NAME   =T_CMT.TABLE_NAME    ");
			sb.append(" AND T_COL.TABLE_NAME   =T_PK.TABLE_NAME(+)  ");
			sb.append(" AND T_COL.COLUMN_NAME  =T_PK.COLUMN_NAME(+) ");
			sb.append(" AND T_COL.COLUMN_NAME  =T_COL_CMT.COLUMN_NAME   ");
			if (objs.length > 0) {
				String[] tbls = CmnStrUtils.fromAtoBByTrim(objs[0].toString(), "(", ")").split(",");
				if ( tbls.length == 1 && CmnStrUtils.isEmpty(tbls[0])) {

				} else {
					sb.append(" AND (");
					for (int i = 0; i < tbls.length; i++) {

						sb.append("   T_COL.TABLE_NAME = " + tbls[i]+"");
						if (i+1!=tbls.length) {
							sb.append(" OR ");
						}
					}
					sb.append(")");
				}
			}

			sb.append(" ORDER BY T_COL.COLUMN_ID   ");
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger
				.debug("AutoDbToXls.getOracleTablesInfoSql(Object... objs) end");
		return sb.toString();
	}
	private static String getSqlLiteTablesInfoSql(String tblNm)
			throws Throwable {
		CmnLog.logger
				.debug("AutoDbToXls.getSqlLiteTablesInfoSql(Object... objs) begin");
		StringBuffer sb = new StringBuffer();
		try {
//			sb.append(" select * from sqlite_master where upper(type)=\"TABLE\"    ");

			sb.append(" PRAGMA table_info ("+tblNm+")   ");
			
//			if (objs.length > 0) {
//				String[] tbls = CsjStrUtils.fromAtoBByTrim(objs[0].toString(), "(", ")").split(",");
//				if ( tbls.length == 1 && CsjStrUtils.isEmpty(tbls[0])) {
//
//				} else {
//					sb.append(" AND (");
//					for (int i = 0; i < tbls.length; i++) {
//
//						sb.append("   name = " + tbls[i]+"");
//						if (i+1!=tbls.length) {
//							sb.append(" OR ");
//						}
//					}
//					sb.append(")");
//				}
//			}

//			sb.append(" ORDER BY T_COL.COLUMN_ID   ");
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger
				.debug("AutoDbToXls.getSqlLiteTablesInfoSql(Object... objs) end");
		return sb.toString();
	}
	public static SheetTblsInfo getDBInfo(XmlDbXlsInfoAll xmlDbXlsInfoAll,
			HashSet<String> tableNmSet) throws Throwable {
		CmnLog.logger
				.debug("AutoDbToXls.getDBInfo(DbInfo dbInfo,HashSet<String> tableNmSet) begin");
		boolean isToExcel = xmlDbXlsInfoAll.getPicInfo().isToExcel();
		boolean isFromTbl = xmlDbXlsInfoAll.getPicInfo().isFromTbl();
		boolean isInOneSheet = xmlDbXlsInfoAll.getPicInfo().isInOneSheet();
		String ch = xmlDbXlsInfoAll.getPicInfo().getCh();
		String newLine = xmlDbXlsInfoAll.getPicInfo().getNewLine();
		
		SheetTblsInfo tblsInfo = new SheetTblsInfo();
		try {
			List<TblBase> tblList = getTblBaseWithSome(xmlDbXlsInfoAll, tableNmSet);

			for (TblBase base : tblList) {
				XlsTblInfo xlsTblInfo = getXlsTblInfo(xmlDbXlsInfoAll, base);
				tblsInfo.getTblInfoMap().put(base.getTblNmEn(), xlsTblInfo);
			}
			FileInputStream fileIn = null;
			FileOutputStream fileOut = null;
			XmlInfoXls xmlInfoXls = xmlDbXlsInfoAll.getXmlInfoXls();
			String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
			File f = new File(xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath());
			f.mkdirs();
			if (!isToExcel) {
				
				if (isInOneSheet) {
					String excelType = xmlDbXlsInfoAll.getXmlInfoXls().getExcelType();
					fileIn = new FileInputStream(CsjPath.s_file_pj_path_temp+excelType);
					Workbook wb = null;
					if (CsjConst.EXCEL_DOT_XLS_1997.equals(excelType)) {
						wb = new HSSFWorkbook(fileIn);
					} else {
						wb = new XSSFWorkbook(fileIn);
					}
					CsjDbCellStyle cellStyle = new CsjDbCellStyle(wb,excelType,CsjDbCellStyle.CS_DATA_REC);
					Sheet newSheet = wb.getSheet("temp");// wb.createSheet(tblNm);
					//wb.setSheetName(wb.getSheetIndex(newSheet.getSheetName()),tblNm);
					
					int rowIndex = 0;
					for (Map.Entry<String, XlsTblInfo> entry : tblsInfo.getTblInfoMap().entrySet()) {
						String tblNm = entry.getKey();
						CmnLog.logger.info("table:" + tblNm);
						if (CmnStrUtils.isNotEmpty(StaticClz.TABLE_NM)) {
							StaticClz.putTblCntMap(StaticClz.TABLE_NM, StaticClz.ONE_INDEX);
							Thread.sleep(100);
						}
						StaticClz.TABLE_NM = tblNm;
						StaticClz.ONE_INDEX = 0;
						XlsTblInfo xlsTblInfo = entry.getValue();
						
						String fNm = tblNm;
						if (CmnStrUtils.isNotEmpty(xlsTblInfo.getTblNmJp())) {
							fNm += "[" + xlsTblInfo.getTblNmJp() + "]";
						}
						fNm = CmnStrUtils.getStrForFileNm(fNm);

//						if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {
//							tblNm = CsjStrUtils.fromAtoBByTrim(tblNm, "#", CsjPath.s_current_date_end);
//						}
						int colStep = 1;

						xlsTblInfo.setHaveJpNm(true);
						CellStyle tempCs = null;
						for (Map.Entry<String, TblPara> paraEntry : xlsTblInfo
								.getParaInfoMap().entrySet()) {
							TblPara para = paraEntry.getValue();
							rowStep = rowIndex;
							CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 0,
									xmlInfoXls.getTblSign(), cellStyle.getCsTableBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 1, tblNm,
									cellStyle.getCsTableBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 2,
									xlsTblInfo.getTblNmJp(), cellStyle.getCsTableBolder());

							CsjLinkedMap<String, CsjTblSelectSql> selectSqlMapWithTblnm = xmlDbXlsInfoAll.getDbInfo().getSelectSqlMapWithTblnm();
							CsjTblSelectSql selectSql = selectSqlMapWithTblnm.get(tblNm);
							if (selectSql != null) {
								CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, CmnStrUtils.getIntVal(CsjDbToolsMsg.coreMsgMap
										.get(CsjDbToolsMsg.MSG_I_0000072)),
										"WHERE "+selectSql.getSqlWhere(), cellStyle.getCsTable());
								CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, CmnStrUtils.getIntVal(CsjDbToolsMsg.coreMsgMap
										.get(CsjDbToolsMsg.MSG_I_0000073)),
										"ORDER BY "+selectSql.getSqlOrder(), cellStyle.getCsTable());
							}

							if (para.isPkey()) {
								tempCs = cellStyle.getCsTitleKey();
							} else {
								tempCs = cellStyle.getCsTitleThin();
							}

							int rowJpNm = ++rowStep;
							if (xlsTblInfo.isHaveJpNm()) {
								CmnPoiUtils.setCellValueWithCs(newSheet, rowJpNm, 0,
										CsjDbToolsMsg.coreMsgMap
												.get(CsjDbToolsMsg.MSG_I_0000002),cellStyle.getCsSignBolder());

								CellStyle syncStyle = tempCs;
								if (CmnStrUtils.isNotEmpty(para.getParaNmJp()) && para.getParaNmJp().equals(para.getParaNmJpSync())) {
									if (para.isPkey()) {
										syncStyle = cellStyle.getCsTitleKey();
									} else {
										syncStyle = cellStyle.getCsTitleThin();
									}
								}

								CmnPoiUtils.setCellValueWithCs(newSheet, rowJpNm, colStep,para.getParaNmJp(), syncStyle);
							}
							int rowEnNm = ++rowStep;
							CmnPoiUtils.setCellValueWithCs(newSheet, rowEnNm, 0,
									CsjDbToolsMsg.coreMsgMap
											.get(CsjDbToolsMsg.MSG_I_0000003),cellStyle.getCsSignBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowEnNm, colStep,para.getParaNmEn(), tempCs);

							ParaCheck paraCheck = para.getParaCheck();

							String cmtInfo = paraCheck.getCmtInfo();
							if (CmnStrUtils.isNotEmpty(cmtInfo)) {
								CmnPoiUtils.setCellCommentBig(newSheet.getRow(rowEnNm).getCell(colStep),
										new HSSFRichTextString(cmtInfo),
										CsjProcess.s_user);
							}

							int rowType = ++rowStep;
							CmnPoiUtils.setCellValueWithCs(newSheet, rowType, 0,
									CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000004),cellStyle.getCsSignBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowType, colStep,
									para.getParaTypeWithlen(),
									tempCs);
							String content = CsjConst.JP_TYPE + CsjConst.MASK_TO_RIGHT
									+ para.getParaType() + CsjProcess.s_newLine;

							//String type = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
//							if (DbInfo.STR_DB_TYPE_ORACLE.equals(type)
//									|| DbInfo.STR_DB_TYPE_POSTGRE.equals(type)) {
		//
//								// if (para.getType() == DbInfo.TABLE_COL_TYPE_STR) {
//								content += CsjConst.JP_LENGTH + CsjConst.MASK_TO_RIGHT
//											+ para.getParaLen() + CsjProcess.s_newLine;
//								// }
		//
//							} else {
								content += CsjConst.JP_LENGTH + CsjConst.MASK_TO_RIGHT
										+ para.getParaLen() + CsjProcess.s_newLine;
								if (CmnStrUtils.isNotEmpty(para.getParaExtra())) {
									content += CsjConst.JP_EXTRA + CsjConst.MASK_TO_RIGHT
											+ para.getParaExtra() + CsjProcess.s_newLine;
								}

//							}

							CmnPoiUtils.setCellComment(newSheet.getRow(rowType)
									.getCell(colStep), new HSSFRichTextString(content),
									CsjProcess.s_user);

							// CsjPoiUtils.setCommentByCell(newSheet.getRow(rowType)
							// .getCell(colStep), content, cellStyle
							// .getFtComment(), csjPart, false, CsjProcess.s_user);

							int rowNull = ++rowStep;
							CmnPoiUtils.setCellValueWithCs(newSheet, rowNull, 0,
											CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000005),cellStyle.getCsSignBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowNull, colStep,
									para.isCanNull() ? "Y": "N", tempCs);

							if (CmnStrUtils.isNotEmpty(para.getParaInitVal())) {
								content = CsjConst.JP_INIT_VAL + CsjConst.MASK_TO_RIGHT
										+ para.getParaInitVal() + CsjProcess.s_newLine;

								CmnPoiUtils.setCellComment(newSheet.getRow(rowNull).getCell(colStep), new HSSFRichTextString(
										content), CsjProcess.s_user);
							}
							
							CmnPoiUtils.setCellValueWithCs(newSheet, ++rowStep, 0,
									CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000162),cellStyle.getCsSignBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, colStep,
									String.valueOf(colStep-1),cellStyle.getCsSignBolder());
							
							CmnPoiUtils.setCellValueWithCs(newSheet, ++rowStep, 0,
									CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000163),cellStyle.getCsSignBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 1,
									ch,cellStyle.getCsSignBolder());
							
							CmnPoiUtils.setCellValueWithCs(newSheet, ++rowStep, 0,
									CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000164),cellStyle.getCsSignBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 1, fNm + CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000171),cellStyle.getCsSignBolder());
							
							CmnPoiUtils.setCellValueWithCs(newSheet, ++rowStep, 0,
									CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000165),cellStyle.getCsSignBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 1,
									xmlDbXlsInfoAll.getInfoSql().getEncode(),cellStyle.getCsSignBolder());
							
							CmnPoiUtils.setCellValueWithCs(newSheet, ++rowStep, 0,
									CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000166),cellStyle.getCsSignBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 1,
									"0",cellStyle.getCsSignBolder());
							
							CmnPoiUtils.setCellValueWithCs(newSheet, ++rowStep, 0,
									CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000167),cellStyle.getCsSignBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 1,
									String.valueOf(Long.MAX_VALUE),cellStyle.getCsSignBolder());
							
							colStep++;
							if (colStep == xmlInfoXls.getMaxCol()) {
								CmnLog.logger.debug(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000068)+ tblNm);
								break;
							}
						}
						
						rowIndex = ++rowStep;
						rowIndex = ++rowStep;
						String filePath = CsjProcess.s_pj_path+ CsjPath.s_path_pj_path_temp+CmnDateUtil.getCurrentDateString(CsjConst.YYYYMMDDHH_MMSSMINUS_24)+ CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000171);
						writeResultSet(xmlDbXlsInfoAll, tblNm, entry.getValue(), AutoDbToXls.s_is_max_data?RUN_DATA_OUT_SPLIT:RUN_DATA_NONE, filePath, ch, newLine);
						File fileFrom = new File(filePath);
						File fileTo = new File(xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath() + fNm + CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000171));
						fileFrom.renameTo(fileTo);
					}

					String fileNm = "snap"+CsjPath.s_current_date_end+excelType;
//					if (CsjStrUtils.isNotEmpty(filePath)) {
//						fileNm = (new File(filePath)).getName();
//					}
					fileOut = new FileOutputStream(xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath()
							+ "data_"
							+ fileNm);
					
					wb.write(fileOut);
					fileOut.close();
					fileIn.close();
				} else {
					for (Map.Entry<String, XlsTblInfo> entry : tblsInfo.getTblInfoMap().entrySet()) {
						String tblNm = entry.getKey();
						
						if (CmnStrUtils.isNotEmpty(StaticClz.TABLE_NM)) {
							StaticClz.putTblCntMap(StaticClz.TABLE_NM, StaticClz.ONE_INDEX);
							Thread.sleep(100);
						}
						
						StaticClz.TABLE_NM = tblNm;
						StaticClz.ONE_INDEX = 0;
						int colStep = 1;
						int rowStep = 0;
						int recordNumIndex = 0;
						if (s_is_max_data) {
							recordNumIndex = record_Num_Index;
						}
						
						String filePath = CsjProcess.s_pj_path+ CsjPath.s_path_pj_path_temp + CmnDateUtil.getCurrentDateString(CsjConst.YYYYMMDDHH_MMSSMINUS_24)+ CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000171);
						recordNumIndex+=writeResultSet(xmlDbXlsInfoAll, tblNm, entry.getValue(), AutoDbToXls.s_is_max_data?RUN_DATA_OUT_SPLIT:RUN_DATA_OUT, filePath, ch, newLine);

						if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {
							tblNm = CmnStrUtils.fromAtoBByTrim(tblNm, "#",CsjPath.s_current_date_end);
						}
						CmnLog.logger.info("table:" + tblNm);
						XlsTblInfo xlsTblInfo = entry.getValue();

						String fNm = tblNm;
						if (CmnStrUtils.isNotEmpty(xlsTblInfo.getTblNmJp())) {
							fNm += "[" + xlsTblInfo.getTblNmJp() + "]";
						}
						fNm = CmnStrUtils.getStrForFileNm(fNm);
						String strBetweenNum = "";
						
						String excelType = xmlDbXlsInfoAll.getXmlInfoXls().getExcelType();
						fileIn = new FileInputStream(CsjPath.s_file_pj_path_temp+excelType);
						Workbook wb = null;
						if (CsjConst.EXCEL_DOT_XLS_1997.equals(excelType)) {
							wb = new HSSFWorkbook(fileIn);
						} else {
							wb = new XSSFWorkbook(fileIn);
						}

						CsjDbCellStyle cellStyle = new CsjDbCellStyle(wb,excelType,CsjDbCellStyle.CS_DATA_REC);


						Sheet newSheet = wb.getSheet("temp");// wb.createSheet(tblNm);
						wb.setSheetName(wb.getSheetIndex(newSheet.getSheetName()),tblNm);
						

						
						int stepRow = 0;
						
						xlsTblInfo.setHaveJpNm(true);
						if (xlsTblInfo.isHaveJpNm()) {
							stepRow = 1;
						}

						CellStyle tempCs = null;
						
						int rowNullIndex = 0;
						for (Map.Entry<String, TblPara> paraEntry : xlsTblInfo
								.getParaInfoMap().entrySet()) {
							TblPara para = paraEntry.getValue();

							CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 0,
									xmlInfoXls.getTblSign(), cellStyle.getCsTableBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 1, tblNm,
									cellStyle.getCsTableBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, 2,
									xlsTblInfo.getTblNmJp(), cellStyle.getCsTableBolder());

							CsjLinkedMap<String, CsjTblSelectSql> selectSqlMapWithTblnm = xmlDbXlsInfoAll.getDbInfo().getSelectSqlMapWithTblnm();
							CsjTblSelectSql selectSql = selectSqlMapWithTblnm.get(tblNm);
							if (selectSql != null) {
								CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, CmnStrUtils.getIntVal(CsjDbToolsMsg.coreMsgMap
										.get(CsjDbToolsMsg.MSG_I_0000072)),
										"WHERE "+selectSql.getSqlWhere(), cellStyle.getCsTable());
								CmnPoiUtils.setCellValueWithCs(newSheet, rowStep, CmnStrUtils.getIntVal(CsjDbToolsMsg.coreMsgMap
										.get(CsjDbToolsMsg.MSG_I_0000073)),
										"ORDER BY "+selectSql.getSqlOrder(), cellStyle.getCsTable());
							}

							if (para.isPkey()) {
								tempCs = cellStyle.getCsTitleKey();
							} else {
								tempCs = cellStyle.getCsTitleThin();
							}

							int rowJpNm = rowStep + stepRow;
							if (xlsTblInfo.isHaveJpNm()) {
								CmnPoiUtils.setCellValueWithCs(newSheet, rowJpNm, 0,
										CsjDbToolsMsg.coreMsgMap
												.get(CsjDbToolsMsg.MSG_I_0000002),cellStyle.getCsSignBolder());

								CellStyle syncStyle = tempCs;
								if (CmnStrUtils.isNotEmpty(para.getParaNmJp()) && para.getParaNmJp().equals(para.getParaNmJpSync())) {
									if (para.isPkey()) {
										syncStyle = cellStyle.getCsTitleKey();
									} else {
										syncStyle = cellStyle.getCsTitleThin();
									}
								}

								CmnPoiUtils.setCellValueWithCs(newSheet, rowJpNm, colStep,para.getParaNmJp(), syncStyle);
							}
							int rowEnNm = rowStep + stepRow + 1;
							CmnPoiUtils.setCellValueWithCs(newSheet, rowEnNm, 0,
									CsjDbToolsMsg.coreMsgMap
											.get(CsjDbToolsMsg.MSG_I_0000003),cellStyle.getCsSignBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowEnNm, colStep,para.getParaNmEn(), tempCs);

							ParaCheck paraCheck = para.getParaCheck();

							String cmtInfo = paraCheck.getCmtInfo();
							if (CmnStrUtils.isNotEmpty(cmtInfo)) {
								CmnPoiUtils.setCellCommentBig(newSheet.getRow(rowEnNm).getCell(colStep),
										new HSSFRichTextString(cmtInfo),
										CsjProcess.s_user);
							}

							int rowType = rowStep + stepRow + 2;
							CmnPoiUtils.setCellValueWithCs(newSheet, rowType, 0,
									CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000004),cellStyle.getCsSignBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowType, colStep,
									para.getParaTypeWithlen(),
									tempCs);
							String content = CsjConst.JP_TYPE + CsjConst.MASK_TO_RIGHT
									+ para.getParaType() + CsjProcess.s_newLine;

							//String type = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
//							if (DbInfo.STR_DB_TYPE_ORACLE.equals(type)
//									|| DbInfo.STR_DB_TYPE_POSTGRE.equals(type)) {
		//
//								// if (para.getType() == DbInfo.TABLE_COL_TYPE_STR) {
//								content += CsjConst.JP_LENGTH + CsjConst.MASK_TO_RIGHT
//											+ para.getParaLen() + CsjProcess.s_newLine;
//								// }
		//
//							} else {
								content += CsjConst.JP_LENGTH + CsjConst.MASK_TO_RIGHT
										+ para.getParaLen() + CsjProcess.s_newLine;
//							}

							CmnPoiUtils.setCellComment(newSheet.getRow(rowType)
									.getCell(colStep), new HSSFRichTextString(content),
									CsjProcess.s_user);

							// CsjPoiUtils.setCommentByCell(newSheet.getRow(rowType)
							// .getCell(colStep), content, cellStyle
							// .getFtComment(), csjPart, false, CsjProcess.s_user);

							rowNullIndex = rowStep + stepRow + 3;
							CmnPoiUtils.setCellValueWithCs(newSheet, rowNullIndex, 0,
											CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000005),cellStyle.getCsSignBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowNullIndex, colStep,
									para.isCanNull() ? "Y": "N", tempCs);

							if (CmnStrUtils.isNotEmpty(para.getParaInitVal())) {
								content = CsjConst.JP_INIT_VAL + CsjConst.MASK_TO_RIGHT
										+ para.getParaInitVal() + CsjProcess.s_newLine;

								CmnPoiUtils.setCellComment(newSheet.getRow(rowNullIndex).getCell(colStep), new HSSFRichTextString(
										content), CsjProcess.s_user);
								// CsjPoiUtils.setCommentByCell(newSheet.getRow(rowNull)
								// .getCell(colStep), content, cellStyle
								// .getFtComment(), csjPart, false,
								// CsjProcess.s_user);
							}
							
							CmnPoiUtils.setCellValueWithCs(newSheet, rowNullIndex+1, 0,
									CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000162),cellStyle.getCsSignBolder());
							CmnPoiUtils.setCellValueWithCs(newSheet, rowNullIndex+1, colStep,
									String.valueOf(colStep-1),cellStyle.getCsSignBolder());
							
							colStep++;
							
							if (colStep == xmlInfoXls.getMaxCol()) {
								CmnLog.logger.debug(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000068)
										+ tblNm);
								break;
							}
						}
						
						if (s_is_max_data) {
							strBetweenNum = "(" + (recordNumIndexPre + 1) + "-"+ (recordNumIndex) + ")";
							record_Num_Index = recordNumIndex;
						}

						File fileFrom = new File(filePath);
						String fileToName = fNm + strBetweenNum;
						
						if (fileToName.length()>IConstFile.FILE_MAX_LEN) {
							fileToName = strBetweenNum+ fNm ;
							fileToName = CmnStrUtils.getStrForLength(fileToName, IConstFile.FILE_MAX_LEN);
						}
						
						fileToName = fileToName + CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000171);
						File fileTo = new File(xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath() +fileToName);
						
						CmnPoiUtils.setCellValueWithCs(newSheet, rowNullIndex+2, 0,
								CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000163),cellStyle.getCsSignBolder());
						CmnPoiUtils.setCellValueWithCs(newSheet, rowNullIndex+2, 1,
								ch,cellStyle.getCsSignBolder());
						
						CmnPoiUtils.setCellValueWithCs(newSheet, rowNullIndex+3, 0,
								CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000164),cellStyle.getCsSignBolder());
						CmnPoiUtils.setCellValueWithCs(newSheet, rowNullIndex+3, 1, fileToName,cellStyle.getCsSignBolder());
						
						CmnPoiUtils.setCellValueWithCs(newSheet, rowNullIndex+4, 0,
								CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000165),cellStyle.getCsSignBolder());
						CmnPoiUtils.setCellValueWithCs(newSheet, rowNullIndex+4, 1,
								xmlDbXlsInfoAll.getInfoSql().getEncode(),cellStyle.getCsSignBolder());
						
						CmnPoiUtils.setCellValueWithCs(newSheet, rowNullIndex+5, 0,
								CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000166),cellStyle.getCsSignBolder());
						CmnPoiUtils.setCellValueWithCs(newSheet, rowNullIndex+5, 1,
								"0",cellStyle.getCsSignBolder());
						
						CmnPoiUtils.setCellValueWithCs(newSheet, rowNullIndex+6, 0,
								CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000167),cellStyle.getCsSignBolder());
						CmnPoiUtils.setCellValueWithCs(newSheet, rowNullIndex+6, 1,
								String.valueOf(Long.MAX_VALUE),cellStyle.getCsSignBolder());
						
						fileFrom.renameTo(fileTo);
						/////////////
						
						String filePathXls = xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath()+ fNm + strBetweenNum;
						
						if (filePathXls.length()>IConstFile.FILE_MAX_LEN) {
							filePathXls = xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath()+strBetweenNum+ fNm ;
							filePathXls = CmnStrUtils.getStrForLength(filePathXls, IConstFile.FILE_MAX_LEN);
						}
						
						fileOut = new FileOutputStream(filePathXls + excelType);
						
						
						wb.write(fileOut);
						fileOut.close();
						fileIn.close();
					}
				}
				
				return null;
			} else {
				tblsInfo = getDBInfoStruct(xmlDbXlsInfoAll, tableNmSet);
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger
				.debug("AutoDbToXls.getDBInfo(DbInfo dbInfo,HashSet<String> tableNmSet) end");
		return tblsInfo;
	}

	
	
	/**
	 * @param dbInfo
	 * @param dbAccess
	 * @param paraList
	 * @param tblNm
	 * @param xlsTblInfo
	 * @return
	 * @throws Throwable
	 */
	public static long writeResultSet(XmlDbXlsInfoAll xmlDbXlsInfoAll, String tblNm,
			XlsTblInfo xlsTblInfo, int runDataType,String filePath,String ch, String newLine) throws Throwable {
		CmnLog.logger
		.debug("AutoDbToXls.writeResultSet(XmlDbXlsInfoAll xmlDbXlsInfoAll,CsjDBAccess dbAccess, String tblNm,lsTblInfo xlsTblInfo) begin");

		CsjDBAccess csjDBAccess = xmlDbXlsInfoAll.getDbInfo().getDbAccess();
		List<Object> paraList = new ArrayList<Object>();
		String selectTableDataSql = getSelectTableDataSql(xmlDbXlsInfoAll, tblNm,
				xlsTblInfo, paraList, runDataType);
		long cnt = csjDBAccess.writeResultSet(selectTableDataSql, paraList, filePath, xmlDbXlsInfoAll, ch, newLine);
		CmnLog.logger
		.debug("AutoDbToXls.getCsjRetWithSql(DbInfo dbInfo,CsjDBAccess dbAccess, String tblNm,lsTblInfo xlsTblInfo) end");
		return cnt;
	}
	
	/**
	 * @param dbInfo
	 * @param dbAccess
	 * @param paraList
	 * @param tblNm
	 * @param xlsTblInfo
	 * @return
	 * @throws Throwable
	 */
	public static CsjRetWithSql getCsjRetWithSql(XmlDbXlsInfoAll xmlDbXlsInfoAll, String tblNm,
			XlsTblInfo xlsTblInfo, int runDataType) throws Throwable {
		CmnLog.logger
		.debug("AutoDbToXls.getCsjRetWithSql(DbInfo dbInfo,CsjDBAccess dbAccess, String tblNm,lsTblInfo xlsTblInfo) begin");

		CsjDBAccess csjDBAccess = xmlDbXlsInfoAll.getDbInfo().getDbAccess();
		List<Object> paraList = new ArrayList<Object>();
		CsjRetWithSql csjRetWithSql = new CsjRetWithSql();
		String selectTableDataSql = getSelectTableDataSql(xmlDbXlsInfoAll, tblNm,
				xlsTblInfo, paraList, runDataType);
		csjRetWithSql.setParaList(paraList);
		csjDBAccess.overMaxRecord = "";
		csjRetWithSql.setCsjDbDataLst(csjDBAccess.getRecordList(
				selectTableDataSql, paraList,CmnStrUtils.getIntVal(xmlDbXlsInfoAll.getXmlInfoXls().getMaxRow())));
		csjRetWithSql.setSelectTableDataSql(selectTableDataSql);
		csjRetWithSql.setRowInfo(CmnLog5j.addlrBracketsS(CsjDBAccess.overMaxRecord + String.valueOf(csjRetWithSql.getDataList().size()) + "rows", true) );
		csjRetWithSql.setTitle(tblNm);
		csjDBAccess.overMaxRecord = "";

		CmnLog.logger
		.debug("AutoDbToXls.getCsjRetWithSql(DbInfo dbInfo,CsjDBAccess dbAccess, String tblNm,lsTblInfo xlsTblInfo) end");
		return csjRetWithSql;
	}

	/**
	 * @param xmlDbXlsInfoAll
	 * @param tblNm
	 * @param xlsTblInfo
	 * @param selectTableDataSql
	 * @param dbType
	 * @param paraList
	 * @return
	 * @throws Throwable
	 */
	public static String getSelectTableDataSql(XmlDbXlsInfoAll xmlDbXlsInfoAll,
			String tblNm, XlsTblInfo xlsTblInfo,List<Object> paraList,int runDataType) throws Throwable {
		String selectTableDataSql = CsjConst.EMPTY;
		String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
		if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
			selectTableDataSql = getOracleTalbeData(tblNm,
					xlsTblInfo, paraList,xmlDbXlsInfoAll,runDataType);
		} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
			selectTableDataSql = getPostgreTalbeData(tblNm,
					xlsTblInfo, paraList,xmlDbXlsInfoAll,runDataType);
		} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
			selectTableDataSql = getSqlmicroTalbeData(
					tblNm,  xlsTblInfo, paraList,xmlDbXlsInfoAll,runDataType);
		} else if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
			selectTableDataSql = getMysqlTalbeData(tblNm,
					xlsTblInfo, paraList,xmlDbXlsInfoAll,runDataType);
		} else if (DbInfo.STR_DB_TYPE_DB2.equals(dbType)) {
			selectTableDataSql = getDb2TalbeData(tblNm,
					xlsTblInfo, paraList,xmlDbXlsInfoAll,runDataType);
		} else if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {
			selectTableDataSql = getSybaseTalbeData(tblNm,
					xlsTblInfo, paraList,xmlDbXlsInfoAll,runDataType);
		}else if (DbInfo.STR_DB_TYPE_SQLITE.equals(dbType)) {
			selectTableDataSql = getSqlLiteTalbeData(tblNm,
					xlsTblInfo, paraList,xmlDbXlsInfoAll,runDataType);
		}
		return selectTableDataSql;
	}
	/**
	 * @param dbInfo
	 * @param tableNmSet
	 * @return
	 * @throws Throwable
	 */
	public static List<TblBase> getTblBaseWithSome(XmlDbXlsInfoAll xmlDbXlsInfoAll,
			HashSet<String> tableNmSet) throws Throwable {
		List<TblBase> tblList = new ArrayList<TblBase>();
		CmnLog.logger
				.debug("AutoDbToXls.getTblBaseWithSome(DbInfo dbInfo,HashSet<String> tableNmSet) begin");
		try {

			List<TblBase> tblsFromInitList = xmlDbXlsInfoAll.getDbInfo().getTblInfoList();
			if (CmnStrUtils.isEmpty(tableNmSet)) {
				tblList = tblsFromInitList;
			} else {
				for (TblBase tblBase : tblsFromInitList) {
					for (String str : tableNmSet) {
						if (CmnStrUtils.toLowOrUpStr(str).equals(
								CmnStrUtils.toLowOrUpStr(tblBase.getTblNmEn()))) {
							tblList.add(tblBase);
						}
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger
				.debug("AutoDbToXls.getTblBaseWithSome(DbInfo dbInfo,HashSet<String> tableNmSet) end");
		return tblList;
	}

	/**
	 * @param dbInfo
	 * @param paraList
	 * @param base
	 * @return
	 * @throws Throwable
	 */
	public static XlsTblInfo getXlsTblInfo(XmlDbXlsInfoAll xmlDbXlsInfoAll, TblBase base)
			throws Throwable {
		try {
			CmnLog.logger
					.debug("AutoDbToXls.getXlsTblInfo(DbInfo dbInfo, TblBase base) begin");
			XlsTblInfo xlsTblInfo = new XlsTblInfo();
			xlsTblInfo.setTblNmEn(base.getTblNmEn());
			List<Integer> keyPosList = null;
			try {
				keyPosList = xmlDbXlsInfoAll.getDbInfo().getTblMap().get(base.getTblNmEn()).getKeyPosList();
			} catch (Throwable e) {
				CmnLog.logger.debug(e.getMessage());
			}
			if (CmnStrUtils.isNotEmpty(keyPosList)) {
				xlsTblInfo.getKeyPosList().addAll(keyPosList);
			}

			TblInfo tblInfo = xmlDbXlsInfoAll.getDbInfo().getTblMap().get(base.getTblNmEn());
			xlsTblInfo.getParaInfoMap().putAll(tblInfo.getParaInfoMap());
			int pos = 0;
			for (TblPara para : tblInfo.getParaInfoMap().values()) {
				xlsTblInfo.getParaPosInfoMap().put(Integer.valueOf(pos++),para);
			}
			
			xlsTblInfo.setTblNmJp(tblInfo.getTblNmJp());
			CmnLog.logger
					.debug("AutoDbToXls.getXlsTblInfo(DbInfo dbInfo, TblBase base) end");
			return xlsTblInfo;
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

	}

	/**
	 * @param dbXlsInfo
	 * @param xlsTblInfo
	 * @param keyPosList
	 * @param dataList
	 * @throws Throwable
	 */
	public static void getParaInfoMap(XmlDbXlsInfoAll xmlDbXlsInfoAll,
			XlsTblInfo xlsTblInfo, List<Integer> keyPosList,
			List<HashMap<String, String>> dataList) throws Throwable {
		CmnLog.logger
				.debug("AutoDbToXls.getParaInfoMap(CsjDBAccess dbAccess,XlsTblInfo xlsTblInfo, List<Integer> keyPosList,List<HashMap<String, String>> dataList) begin");
		String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
		try {
			for (int i = 0; i < dataList.size(); i++) {
				HashMap<String, String> map = dataList.get(i);
				TblPara tblPara = new TblPara();

				if (i + 1 == dataList.size()) {
					tblPara.setLastPara(true);
				}
				tblPara.setSelfValFromDb(map, dbType);
				if (CmnStrUtils.isNotEmpty(tblPara.getParaNmJp())) {
					xlsTblInfo.setHaveJpNm(true);
				}
				if (tblPara.isPkey()) {
					keyPosList.add(tblPara.getParaPos());
				}

				xlsTblInfo.getParaInfoMap().put(tblPara.getParaNmEn(), tblPara);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger
				.debug("AutoDbToXls.getParaInfoMap(CsjDBAccess dbAccess,XlsTblInfo xlsTblInfo, List<Integer> keyPosList,List<HashMap<String, String>> dataList) end");
	}

	/**
	 * @param dbInfo
	 * @param paraList
	 * @param dbXlsInfo
	 * @param base
	 * @return
	 * @throws Throwable
	 */
	public static List<HashMap<String, String>> getTableColInfo(XmlDbXlsInfoAll xmlDbXlsInfoAll,
			String tableNm) throws Throwable {
		CmnLog.logger
				.debug("AutoDbToXls.getTableColInfo(DbInfo dbInfo,String tableNm) begin");
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String,String>>();
		try {
			CsjDBAccess dbAccess = xmlDbXlsInfoAll.getDbInfo().getDbAccess();
			String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
			String schema = xmlDbXlsInfoAll.getCurrentXmlDb().getSchema();
			List<Object> paraList = new ArrayList<Object>();
			if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
				dataList = dbAccess.getRecordList(
						getOracleTablesInfoSql(tableNm), paraList,Integer.MAX_VALUE).getResultList();
			} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
				dataList = dbAccess.getRecordList(
						getPostgreTablesInfoSql(tableNm, schema), paraList,Integer.MAX_VALUE).getResultList();
//				if (dataList.size() == 0) {
//					dataList = dbInfo.getDbAccess().getRecordList(
//							getPostgreTablesInfoSql(tableNm, ""), paraList);
//				}
			} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
				dataList = dbAccess.getRecordList(
						getSqlMicroTablesInfoSql(tableNm), paraList,Integer.MAX_VALUE).getResultList();
			} else if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {

				if (CmnStrUtils.isEmpty(schema)) {
					schema = xmlDbXlsInfoAll.getCurrentXmlDb()
					.getDatabase();
				}
				dataList = dbAccess.getRecordList(
						getMysqlTablesInfoSql(schema, tableNm), paraList,Integer.MAX_VALUE).getResultList();
			}  else if (DbInfo.STR_DB_TYPE_DB2.equals(dbType)) {
				dataList = dbAccess.getRecordList(
						getDb2TablesInfoSql(xmlDbXlsInfoAll.getCurrentXmlDb().getSchema(), tableNm), paraList,Integer.MAX_VALUE).getResultList();
			}  else if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {

//				tableNm = CsjStrUtils.fromAtoBByTrim(tableNm, "(", ")");
//				String[] tableNms = tableNm.split(",");
//				dataList = new ArrayList<HashMap<String,String>>();
//				for (String tblNm : tableNms) {
//					dataList.addAll(dbAccess.getRecordList(
//							getSybaseTablesInfoSql(tblNm), paraList));
//				}
				if (CmnStrUtils.isNotEmpty(tableNm)) {
					dataList = dbAccess.getRecordList(
							getSybaseTablesInfoSql(tableNm), paraList,Integer.MAX_VALUE).getResultList();
				}
			} else if (DbInfo.STR_DB_TYPE_SQLITE.equals(dbType)) {
				
				String[] tbls = CmnStrUtils.fromAtoBByTrim(tableNm, "(", ")").split(",");
				for (String tblNm : tbls) {
					tblNm = tblNm.replace("'", "");
					if (CmnStrUtils.isEmpty(tblNm)) {
						continue;
					}
					
					List<HashMap<String, String>> lst = dbAccess.getRecordList(
							getSqlLiteTablesInfoSql(tblNm), paraList,Integer.MAX_VALUE).getResultList();
					for (HashMap<String, String> map : lst) {
						HashMap<String,String> m = new HashMap<String,String>();
						m.put(AutoDbToXls.TBL_NM_EN, tblNm);
						m.put(AutoDbToXls.COL_ID, map.get("cid"));
						m.put(AutoDbToXls.COL_NM_EN, map.get("name"));
						m.put(AutoDbToXls.COL_TYPE_INFO, map.get("type"));
						m.put(AutoDbToXls.COL_IS_PK, "0".equals(map.get("pk"))?"N":"Y");
						m.put(AutoDbToXls.COL_CAN_NULL, "0".equals(map.get("notnull"))?"N":"Y");
						m.put(AutoDbToXls.COL_DEFAULT, map.get("dflt_value"));
						dataList.add(m);
					}
				}

			} else {
				dataList = dbAccess.getRecordList(
						getPostgreTablesInfoSql(tableNm), paraList,Integer.MAX_VALUE).getResultList();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger
				.debug("AutoDbToXls.getTableColInfo(DbInfo dbInfo,String tableNm) end");
		return dataList;
	}

	/**
	 * @param tblInfoMap
	 * @param linkedHashMap
	 * @param tableNmSet
	 * @throws Throwable
	 */
	public static LinkedHashMap<String, TblInfo>
			putMapBySetKey(
					LinkedHashMap<String, TblInfo> linkedHashMap,
					HashSet<String> tableNmSet) throws Throwable {
		LinkedHashMap<String, TblInfo> tblInfoMap = new LinkedHashMap<String, TblInfo>();
		CmnLog.logger
				.debug("AutoDbToXls.putMapBySetKey(LinkedHashMap<String, TblInfo> linkedHashMap,HashSet<String> tableNmSet) begin");

		try {
			if (tableNmSet == null || tableNmSet.isEmpty()) {
				return linkedHashMap;
			}
			for (String key : tableNmSet) {
				if (linkedHashMap.containsKey(key)) {
					tblInfoMap.put(key, linkedHashMap.get(key));
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger
				.debug("AutoDbToXls.putMapBySetKey(LinkedHashMap<String, TblInfo> linkedHashMap,HashSet<String> tableNmSet) end");
		return tblInfoMap;
	}

	/**
	 * @param linkedHashMap
	 * @param dbAccess
	 * @param paraList
	 * @throws Throwable
	 */
	public static List<TblBase> getAllTablesNm(XmlDbXlsInfoAll xmlDbXlsInfoAll,
			Set<String> tableNmSet, LinkedHashMap<String, TblBase> tblBaseMap,boolean isReload)
			throws Throwable {
		CmnLog.logger
				.debug("AutoDbToXls.getAllTablesNm(DbInfo dbInfo,HashSet<String> tableNmSet) begin");
		
		Map<String ,TblBase> tblMap = new HashMap<String ,TblBase>();
		List<TblBase> tblList = new ArrayList<TblBase>();
		List<Object> paraList = new ArrayList<Object>();
		try {
			CsjDBAccess dbAccess = xmlDbXlsInfoAll.getDbInfo().getDbAccess();
			String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
			String schema = xmlDbXlsInfoAll.getCurrentXmlDb().getSchema();
			List<HashMap<String, String>> tableList = null;
			StringBuffer sqlBuf = new StringBuffer();
			if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {

				sqlBuf.append("SELECT UTC.TABLE_NAME as TABLE_NAME,UTC.COMMENTS FROM SYS.USER_TAB_COMMENTS UTC, USER_TABLES UTS WHERE UTC.TABLE_NAME = UTS.TABLE_NAME AND UTC.TABLE_NAME NOT LIKE '%$%' ");
				if (tblBaseMap.size() > 0) {

					sqlBuf.append(" AND (");
					int index = 0;
					for (TblBase tblBase : tblBaseMap.values()) {
						index++;
						sqlBuf.append("   UTC.TABLE_NAME = '"+ tblBase.getTblNmEn() + "' ");
						if (index != tblBaseMap.size()) {
							sqlBuf.append(" OR ");
						}
					}
					sqlBuf.append(")");
				}

				sqlBuf.append(" ORDER BY TABLE_NAME");
				tableList = dbAccess
						.getRecordList(
								sqlBuf.toString(),
								paraList,Integer.MAX_VALUE).getResultList();

			} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {

				String str = "PUBLIC";
				if (CmnStrUtils.isNotEmpty(schema)) {
					str =  CmnStrUtils.toLowOrUpStr(schema);
				}

				sqlBuf.append("SELECT t.relname as \"TABLE_NAME\",t.*,d.*, d.description as \"COMMENTS\"  FROM \"pg_statio_all_tables\" t left join \"pg_description\" d on t.\"relid\" = d.\"objoid\" and d.\"objsubid\" = 0  WHERE  1=1 ");
				sqlBuf.append("AND upper(t.\"schemaname\")  = '");
				sqlBuf.append(str+"' ");

//						+//and d.\"objsubid\" = '0'
//						+ );
				if (tblBaseMap.size() > 0) {

					sqlBuf.append(" AND (");
					int index = 0;
					for (TblBase tblBase : tblBaseMap.values()) {
						index++;
						sqlBuf.append("   t.relname = '"+ tblBase.getTblNmEn() + "' ");
						if (index != tblBaseMap.size()) {
							sqlBuf.append(" OR ");
						}
					}
					sqlBuf.append(")");
				}
				sqlBuf.append(" ORDER BY t.relname ");
				tableList = dbAccess
						.getRecordList(
								sqlBuf.toString(), paraList,Integer.MAX_VALUE).getResultList();
//				if (tableList.size() == 0) {
//					sqlBuf = new StringBuffer();
//					sqlBuf.append("SELECT DISTINCT TABLENAME as \"TABLE_NAME\" FROM \"PG_TABLES\" WHERE TABLENAME NOT LIKE 'pg%' AND TABLENAME NOT LIKE 'sql_%' ");
//					if (tblBaseMap.size() > 0) {
//						sqlBuf.append(" AND (");
//						int index = 0;
//						for (TblBase tblBase : tblBaseMap.values()) {
//							index++;
//							sqlBuf.append("   TABLENAME = '"+ tblBase.getTblNmEn() + "' ");
//							if (index != tblBaseMap.size()) {
//								sqlBuf.append(" OR ");
//							}
//						}
//						sqlBuf.append(")");
//					}
//					sqlBuf.append("AND upper(SCHEMANAME) = upper('");
//					sqlBuf.append(schema);
//					sqlBuf.append("')  ORDER BY TABLENAME");
//					tableList = dbAccess.getRecordList(sqlBuf.toString(),paraList);
//				}

			} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
//				tableList = dbAccess
//						.getRecordList(
//								"SELECT name    as TABLE_NAME from   sysobjects   where   type='U' and name<>'dtproperties' ORDER BY TABLE_NAME",
//								paraList);

				sqlBuf.append("SELECT CAST(tables.name as varchar(1000)) as TABLE_NAME,CAST(extended_properties.value as varchar(1000)) as COMMENTS FROM sys.tables  left outer join sys.extended_properties on   tables.object_id = extended_properties.major_id and extended_properties.minor_id = 0");
				sqlBuf.append(" WHERE 1=1 ");
				if (tblBaseMap.size() > 0) {

					sqlBuf.append(" AND (");
					int index = 0;
					for (TblBase tblBase : tblBaseMap.values()) {
						index++;
						sqlBuf.append("   tables.name = '"+ tblBase.getTblNmEn() + "' ");
						if (index != tblBaseMap.size()) {
							sqlBuf.append(" OR ");
						}
					}
					sqlBuf.append(")");
				}

				sqlBuf.append(" ORDER BY TABLE_NAME");
				tableList = dbAccess.getRecordList(sqlBuf.toString(),paraList,Integer.MAX_VALUE).getResultList();
			} else if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {

				String sql = "select table_name as TABLE_NAME,table_comment as COMMENTS from information_schema.\"TABLES\" where UPPER(TABLE_SCHEMA) = UPPER('";

				if (CmnStrUtils.isNotEmpty(schema)) {
					sql+=schema;
				} else {
					sql+=xmlDbXlsInfoAll.getCurrentXmlDb().getDatabase();
				}
				sql+="')";
				sqlBuf.append(sql);
				if (tblBaseMap.size() > 0) {

					sqlBuf.append(" AND (");
					int index = 0;
					for (TblBase tblBase : tblBaseMap.values()) {
						index++;
						sqlBuf.append("   table_name = '"+ tblBase.getTblNmEn() + "' ");
						if (index != tblBaseMap.size()) {
							sqlBuf.append(" OR ");
						}
					}
					sqlBuf.append(")");
				}
				tableList = dbAccess
						.getRecordList(sqlBuf.toString(),
								paraList,Integer.MAX_VALUE).getResultList();
			} else if (DbInfo.STR_DB_TYPE_DB2.equals(dbType)) {

				String sql = "select   tabname  as TABLE_NAME ,remarks as COMMENTS from   syscat.tables   where   UPPER(tabschema)   =   ";

				if (CmnStrUtils.isNotEmpty(schema)) {
					sql+="UPPER(\'"+schema+"\')";
				} else {
					sql+="UPPER(current   schema) ";
				}
				sqlBuf.append(sql);
				if (tblBaseMap.size() > 0) {

					sqlBuf.append(" AND (");
					int index = 0;
					for (TblBase tblBase : tblBaseMap.values()) {
						index++;
						sqlBuf.append("   tabname = '"+ tblBase.getTblNmEn() + "' ");
						if (index != tblBaseMap.size()) {
							sqlBuf.append(" OR ");
						}
					}
					sqlBuf.append(")");
				}
				sqlBuf.append(" AND UPPER(type) like 'T' order by tabname");
				tableList = dbAccess.getRecordList(sqlBuf.toString(), paraList,Integer.MAX_VALUE).getResultList();
			} else if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {



				String sql = "SELECT o.name as TABLE_NAME FROM  dbo.sysobjects o, dbo.sysusers u WHERE o.uid = u.uid  AND o.type = 'U' and o.name not like '#%' ";
				if (CmnStrUtils.isNotEmpty(schema)) {
					sql+=" AND UPPER(u.name) = "+"UPPER(\'"+schema+"\')";
				}
				sqlBuf.append(sql);
				if (tblBaseMap.size() > 0) {

					sqlBuf.append(" AND (");
					int index = 0;
					for (TblBase tblBase : tblBaseMap.values()) {
						index++;
						sqlBuf.append("   o.name = '"+ tblBase.getTblNmEn() + "' ");
						if (index != tblBaseMap.size()) {
							sqlBuf.append(" OR ");
						}
					}
					sqlBuf.append(")");
				}
				sqlBuf.append(" ORDER BY o.name");
				tableList = dbAccess.getRecordList(sqlBuf.toString(), paraList,Integer.MAX_VALUE).getResultList();
			} else if (DbInfo.STR_DB_TYPE_SQLITE.equals(dbType)) {

				sqlBuf.append("SELECT name as TABLE_NAME, tbl_name as COMMENTS FROM SQLITE_MASTER where  upper(type) = upper(\"table\")");
				if (tblBaseMap.size() > 0) {

					sqlBuf.append(" AND (");
					int index = 0;
					for (TblBase tblBase : tblBaseMap.values()) {
						index++;
						sqlBuf.append("   name = '"+ tblBase.getTblNmEn() + "' ");
						if (index != tblBaseMap.size()) {
							sqlBuf.append(" OR ");
						}
					}
					sqlBuf.append(")");
				}

				sqlBuf.append(" ORDER BY TABLE_NAME");
				tableList = dbAccess
						.getRecordList(
								sqlBuf.toString(),
								paraList,Integer.MAX_VALUE).getResultList();

			}
//			tableList = CsjStrUtils.toUpperKeyList(tableList,new HashSet<String>());
			for (HashMap<String, String> map : tableList) {
				TblBase base = new TblBase();
				String tblNm = map.get(TABLE_NAME);
				if (CmnStrUtils.isEmpty(tblNm)) {
					continue;
				}
				base.setTblNmEn(tblNm);
				
				if (isReload) {
					xmlDbXlsInfoAll.getDbInfo().getTblMap().put(tblNm, null);
				}

				String tblNmJp = CmnStrUtils.convertString(map.get(COMMENTS));
				if (CmnStrUtils.isEmpty(tblNmJp)&& CmnStrUtils.isNotEmpty(tblBaseMap.get(tblNm))) {
					base.setTblNmJp(CmnStrUtils.convertString(tblBaseMap.get(tblNm).getTblNmJp()));
				} else {
					if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
						if (tblNmJp.contains("; InnoDB free:")) {
							tblNmJp = tblNmJp.substring(0, tblNmJp.indexOf("; InnoDB free:"));
						}else if (tblNmJp.contains("InnoDB free:")) {
							tblNmJp = tblNmJp.substring(0, tblNmJp.indexOf("InnoDB free:"));
						}
						
					}
					
					base.setTblNmJp(tblNmJp);
				}

				if (CmnStrUtils.isEmpty(tableNmSet)) {
					tblList.add(base);
				} else {

					if (tableNmSet.contains(tblNm)) {
						tblMap.put(tblNm, base);
					}
				}
			}
			
			if (CmnStrUtils.isNotEmpty(tableNmSet)) {
				for (String key : tableNmSet) {
					tblList.add(tblMap.get(key));
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger
				.debug("AutoDbToXls.getAllTablesNm(DbInfo dbInfo,HashSet<String> tableNmSet) end");
		return tblList;
	}

	private static String getSqlMicroTablesInfoSql(Object... objs)
			throws Throwable {
		StringBuffer sb = new StringBuffer();
		CmnLog.logger
				.debug("AutoDbToXls.getSqlMicroTablesInfoSql(Object... objs) begin");
		try {
			sb.append(" SELECT  ");

			// sb.append("        case ");
			// sb.append("          when a.colorder = 1 then case when f.value is null then  ''  else f.value  end   ");
			//
			// sb.append("          else   ");
			// sb.append("           ''    ");
			// sb.append("        end as TBL_NM_JP    ");
			sb.append("       CAST(isnull(g.value,'') as varchar(1000))  as COL_NM_JP,    ");

			sb.append("        a.colorder as COL_ID,    ");
			sb.append("        a.name as COL_NM_EN, ");
			// sb.append("        case ");
			// sb.append("          when COLUMNPROPERTY(a.id, a.name, 'IsIdentity') = 1 then   ");
			// sb.append("           'Y'   ");
			// sb.append("          else   ");
			// sb.append("           ''    ");
			// sb.append("        end as 标识,   ");

			// sb.append("       case   when a.colorder = 1 then   ");
			// sb.append("           d.name    ");
			// sb.append("          else   ");
			// sb.append("           ''    ");
			sb.append("        d.name as TBL_NM_EN ,    ");
			sb.append("        case ");
			sb.append("          when exists    ");
			sb.append("           (SELECT 1 ");
			sb.append("                  FROM sysobjects    ");
			sb.append("                 where xtype = 'PK'  ");
			sb.append("                   and parent_obj = a.id ");
			sb.append("                   and name in   ");
			sb.append("                       (SELECT name  ");
			sb.append("                          FROM sysindexes    ");
			sb.append("                         WHERE indid in (SELECT indid    ");
			sb.append("                                           FROM sysindexkeys ");
			sb.append("                                          WHERE id = a.id    ");
			sb.append("                                            AND colid = a.colid))) then  ");
			sb.append("           'Y'   ");
			sb.append("          else   ");
			sb.append("           'N'   ");
			sb.append("        end as COL_IS_PK,    ");
			sb.append("        (b.name + '(') + ");
			sb.append("        CAST(COLUMNPROPERTY(a.id, a.name, 'PRECISION') as varchar(50)) +  case CAST(isnull(COLUMNPROPERTY(a.id, a.name, 'Scale'), '0') as varchar(50)) when '0' then '' else ','+ CAST(isnull(COLUMNPROPERTY(a.id, a.name, 'Scale'), '0') as varchar(50)) end + ')'  as COL_TYPE_INFO,   ");
			sb.append("        b.name as COL_TYPE,    ");
			sb.append("        a.length as 占用字节数,   ");
			sb.append("        COLUMNPROPERTY(a.id, a.name, 'PRECISION') as COL_LENGTH,   ");
			sb.append("        isnull(COLUMNPROPERTY(a.id, a.name, 'Scale'), 0) as AFTER_DOT_NUM,    ");
			sb.append("        case ");
			sb.append("          when a.isnullable = 1 then ");
			sb.append("           'Y'   ");
			sb.append("          else   ");
			sb.append("           'N'   ");
			sb.append("        end as COL_CAN_NULL, ");
			sb.append("        isnull(e.text, '') as COL_DEFAULT   ");
			// sb.append("        isnull(g.value, '') as COL_NM_JP    ");
			sb.append("   FROM syscolumns a ");
			sb.append("   left join systypes b  ");
			sb.append("     on a.xusertype = b.xusertype    ");
			sb.append("  inner join sysobjects d    ");
			sb.append("     on a.id = d.id  ");
			sb.append("    and d.xtype = 'U'    ");
			sb.append("    and d.name <> 'dtproperties' ");
			sb.append("   left join syscomments e   ");
			sb.append("     on a.cdefault = e.id    ");
			sb.append("   left join sys.extended_properties g   ");
			sb.append("     on a.id = g.major_id    ");
			sb.append("    and a.colid = g.minor_id ");
			sb.append("   left join sys.extended_properties f   ");
			sb.append("     on d.id = f.major_id    ");
			sb.append("    and f.minor_id = 0   ");
			sb.append("  where 1=1     ");

			if (objs.length > 0 && CmnStrUtils.isNotEmpty( (String)objs[0])) {
				sb.append(" and d.name in " + objs[0]);
			}
			sb.append("  order by a.id, a.colorder  ");
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger
				.debug("AutoDbToXls.getSqlMicroTablesInfoSql(Object... objs) end");
		return sb.toString();
	}

	private static String getPostgreTablesInfoSql(Object... objs)
			throws Throwable {
		StringBuffer sb = new StringBuffer();
		CmnLog.logger
				.debug("AutoDbToXls.getPostgreTablesInfoSql(Object... objs) begin");
		try {
			sb.append(" select f.oid, ");
			sb.append("        f.relname as \"TBL_NM_EN\", ");
			sb.append("        f.col_nm_jp as \"COL_NM_JP\", ");
			sb.append("        f.col_type_info as \"COL_TYPE_INFO\", ");
			sb.append("        f.col_nm_en as \"COL_NM_EN\", ");
			sb.append("        (case f.col_can_null ");
			sb.append("          when 't' then ");
			sb.append("           'N' ");
			sb.append("          else ");
			sb.append("           'Y' ");
			sb.append("        end) as \"COL_CAN_NULL\", ");
			sb.append("        (case f.pk_name ");
			sb.append("          when '$$$' then ");
			sb.append("           'N' ");
			sb.append("          else ");
			sb.append("           'Y' ");
			sb.append("        end) as \"COL_IS_PK\", ");
			sb.append("        f.adsrc as \"COL_DEFAULT\" ");
			sb.append("   from (select a.oid, ");
			sb.append("                a.relname, ");
			sb.append("                a.col_nm_jp, ");
			sb.append("                a.col_type_info, ");
			sb.append("                a.col_nm_en, ");
			sb.append("                a.col_can_null, ");
			sb.append("                COALESCE(b.pk_name, '$$$') as pk_name, ");
			sb.append("                pg_attrdef.adsrc ");
			sb.append("           from (select c.oid, ");
			sb.append("                        c.relname, ");
			sb.append("                        col_description(a.attrelid, a.attnum) as col_nm_jp, ");
			sb.append("                        format_type(a.atttypid, a.atttypmod) as col_type_info, ");
			sb.append("                        a.attname as col_nm_en, ");
			sb.append("                        a.attnotnull as col_can_null, ");
			sb.append("                        a.attrelid, ");
			sb.append("                        a.attnum ");
			sb.append("                   from pg_class as c, pg_attribute as a");

			if (CmnStrUtils.isEmpty(objs[1])) {

				sb.append("                   , pg_statio_all_tables as tbs ");
				sb.append("                  where a.attrelid = c.oid ");
				sb.append("                    and a.attrelid = tbs.relid ");
				sb.append("                    and upper(tbs.schemaname) = 'PUBLIC"
						+ "'");
			} else {
				sb.append("                   , pg_statio_all_tables as tbs ");
				sb.append("                  where a.attrelid = c.oid ");
				sb.append("                    and a.attrelid = tbs.relid ");
				sb.append("                    and upper(tbs.schemaname) = '"
						+ CmnStrUtils.toLowOrUpStr(objs[1])
						+ "'");
			}

			sb.append("                    and a.attnum > 0 ");
			sb.append("                    and a.attname not like '...%' ");

			if (CmnStrUtils.isNotEmpty(objs[0])) {
				sb.append("   AND c.relname in " + objs[0]);
			}

			sb.append("                    ) a ");
			sb.append("           left outer join pg_attrdef ");
			sb.append("             on a.attrelid = pg_attrdef.adrelid ");
			sb.append("            and a.attnum = pg_attrdef.adnum ");
			sb.append("           left outer join (select pg_class.oid, ");
			sb.append("                                  pg_constraint.conname as pk_name, ");
			sb.append("                                  pg_attribute.attname  as colname, ");
			sb.append("                                  pg_type.typname       as typename ");
			sb.append("                             from pg_constraint ");
			sb.append("                            inner join pg_class ");
			sb.append("                               on pg_constraint.conrelid = pg_class.oid ");
			sb.append("                            inner join pg_attribute ");
			sb.append("                               on pg_attribute.attrelid = pg_class.oid ");
			sb.append("                              and (pg_attribute.attnum = pg_constraint.conkey [ 0 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 1 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 2 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 3 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 4 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 5 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 6 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 7 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 8 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 9 ]) ");
			sb.append("                            inner join pg_type ");
			sb.append("                               on pg_type.oid = pg_attribute.atttypid  where pg_constraint.contype='p') b ");
			sb.append("             on a.oid = b.oid ");
			sb.append("            and a.col_nm_en = b.colname) f ");
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		return sb.toString();
	}

	private static String getMysqlTablesInfoSql(Object... objs)
			throws Throwable {
		StringBuffer sb = new StringBuffer();
		CmnLog.logger
				.debug("AutoDbToXls.getMysqlTablesInfoSql(Object... objs) begin");
		try {

			sb.append(" SELECT TABLE_NAME       TBL_NM_EN, ");
			sb.append("        ORDINAL_POSITION COL_ID, ");
			sb.append("        COLUMN_COMMENT   COL_NM_JP, ");
			sb.append("        COLUMN_NAME      COL_NM_EN, ");
			sb.append("         ");
			sb.append("        COLUMN_DEFAULT COL_DEFAULT, ");
			sb.append("         ");
			sb.append("        DATA_TYPE COL_TYPE, ");
			sb.append("        COLUMN_TYPE COL_TYPE_INFO, ");
			sb.append("        CASE ");
			sb.append("          WHEN COLUMN_KEY = 'PRI' THEN ");
			sb.append("           'Y' ");
			sb.append("          ELSE ");
			sb.append("           'N' ");
			sb.append("        END COL_IS_PK, ");
			sb.append("        CASE ");
			sb.append("          WHEN IS_NULLABLE = 'YES' THEN ");
			sb.append("           'Y' ");
			sb.append("          ELSE ");
			sb.append("           'N' ");
			sb.append("        END COL_CAN_NULL, ");
			sb.append("        CHARACTER_MAXIMUM_LENGTH, ");
			sb.append("        EXTRA, ");
			sb.append("        NUMERIC_PRECISION ");
			sb.append("   FROM \"information_schema\".\"COLUMNS\" ");

			if (CmnStrUtils.isNotEmpty(objs)) {

				sb.append("  WHERE UPPER(TABLE_SCHEMA) = UPPER('" + objs[0]
						+ "') ");
				if (CmnStrUtils.isNotEmpty(objs[1])) {
					sb.append(" AND  TABLE_NAME in ");
					sb.append(objs[1]);
				}

			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger
				.debug("AutoDbToXls.getMysqlTablesInfoSql(Object... objs) end");
		return sb.toString();
	}

	private static String getDb2TablesInfoSql(Object... objs)
			throws Throwable {
		StringBuffer sb = new StringBuffer();
		CmnLog.logger
				.debug("AutoDbToXls.getDb2TablesInfoSql(Object... objs) begin");
		try {
			sb.append("SELECT TABNAME TBL_NM_EN, ");
			sb.append("       COLNO COL_ID, ");
			sb.append("       REMARKS COL_NM_JP, ");
			sb.append("       COLNAME COL_NM_EN, ");
			sb.append("       COL_DEFAULT, ");
			sb.append("       TYPENAME COL_TYPE, ");
			sb.append("       CASE ");
			sb.append("         WHEN TYPENAME = 'CHARACTER' THEN ");
			sb.append("          TYPENAME || '(' || TRIM(CHAR(COL_LEN)) || ')' ");
			sb.append("         WHEN TYPENAME = 'VARCHAR' THEN ");
			sb.append("          TYPENAME || '(' || TRIM(CHAR(COL_LEN)) || ')' ");
			sb.append("         WHEN TYPENAME = 'DECIMAL' THEN ");
			sb.append("          TYPENAME || '(' || TRIM(CHAR(COL_LEN)) || ',' || TRIM(CHAR(SCALE)) || ')' ");
			sb.append("         ELSE ");
			sb.append("          TYPENAME ");
			sb.append("       END COL_TYPE_INFO, ");
			sb.append("       CASE ");
			sb.append("         WHEN KEYSEQ IS NOT NULL THEN ");
			sb.append("          'Y' ");
			sb.append("         ELSE ");
			sb.append("          'N' ");
			sb.append("       END COL_IS_PK, ");
			sb.append("       NULLS COL_CAN_NULL, ");
			sb.append("       SCALE NUMERIC_PRECISION, ");
			sb.append("       TABSCHEMA ");
			sb.append("  FROM (SELECT TABSCHEMA, ");
			sb.append("               TABNAME, ");
			sb.append("               COLNAME, ");
			sb.append("               COLNO, ");
//			sb.append("               TYPESCHEMA, ");
			sb.append("               TYPENAME, ");
			sb.append("               LENGTH             COL_LEN, ");
			sb.append("               SCALE, ");
			sb.append("               DEFAULT            COL_DEFAULT, ");
			sb.append("               NULLS, ");
//			sb.append("               CODEPAGE, ");
			sb.append("               COLLATIONSCHEMA, ");
//			sb.append("               COLLATIONNAME, ");
//			sb.append("               LOGGED, ");
//			sb.append("               COMPACT, ");
//			sb.append("               COLCARD, ");
//			sb.append("               HIGH2KEY, ");
//			sb.append("               LOW2KEY, ");
//			sb.append("               AVGCOLLEN, ");
			sb.append("               KEYSEQ, ");
//			sb.append("               PARTKEYSEQ, ");
//			sb.append("               NQUANTILES, ");
//			sb.append("               NMOSTFREQ, ");
//			sb.append("               NUMNULLS, ");
//			sb.append("               TARGET_TYPESCHEMA, ");
//			sb.append("               TARGET_TYPENAME, ");
//			sb.append("               SCOPE_TABSCHEMA, ");
//			sb.append("               SCOPE_TABNAME, ");
//			sb.append("               SOURCE_TABSCHEMA, ");
//			sb.append("               SOURCE_TABNAME, ");
//			sb.append("               DL_FEATURES, ");
//			sb.append("               SPECIAL_PROPS, ");
//			sb.append("               HIDDEN, ");
//			sb.append("               INLINE_LENGTH, ");
//			sb.append("               PCTINLINED, ");
//			sb.append("               IDENTITY, ");
//			sb.append("               ROWCHANGETIMESTAMP, ");
//			sb.append("               GENERATED, ");
//			sb.append("               TEXT, ");
//			sb.append("               COMPRESS, ");
//			sb.append("               AVGDISTINCTPERPAGE, ");
//			sb.append("               PAGEVARIANCERATIO, ");
//			sb.append("               SUB_COUNT, ");
//			sb.append("               SUB_DELIM_LENGTH, ");
//			sb.append("               AVGCOLLENCHAR, ");
//			sb.append("               IMPLICITVALUE, ");
//			sb.append("               SECLABELNAME, ");
//			sb.append("               ROWBEGIN, ");
//			sb.append("               ROWEND, ");
//			sb.append("               TRANSACTIONSTARTID, ");
//			sb.append("               QUALIFIER, ");
//			sb.append("               FUNC_PATH, ");
			sb.append("               REMARKS ");
			sb.append("          FROM SYSCAT.COLUMNS ");

			if (CmnStrUtils.isNotEmpty(objs)) {

				sb.append("  WHERE 1=1 ");
				if (CmnStrUtils.isNotEmpty(objs[0])) {
					sb.append(" AND UPPER(TABSCHEMA) = UPPER('");
					sb.append(objs[0]);
					sb.append("')");
				}
				if (CmnStrUtils.isNotEmpty(objs[1])) {
					sb.append(" AND  TABNAME in ");
					sb.append(objs[1]);
				}

			}

			sb.append("           ) AAA ");
			sb.append(" ORDER BY COL_ID ");


		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger
				.debug("AutoDbToXls.getDb2TablesInfoSql(Object... objs) end");
		return sb.toString();
	}
	/**
	 * @param m_colTypeMap
	 * @param tableNm
	 * @return
	 * @throws Throwable
	 */
	public static List<String> getComboList(XmlDbXlsInfoAll xmlDbXlsInfoAll, TblBase base,
			HashMap<String, Integer> m_colTypeMap) throws Throwable {
		List<String> retList = new ArrayList<String>();
		CmnLog.logger
				.debug("AutoDbToXls.getComboList(DbInfo dbInfo, TblBase base,HashMap<String, Integer> m_colTypeMap) begin");
		try {
			m_colTypeMap.clear();

			retList.add(CsjConst.EMPTY);
			XlsTblInfo xlsTblInfo = getXlsTblInfo(xmlDbXlsInfoAll, base);

			LinkedHashMap<String, TblPara> paraInfoMap = xlsTblInfo
					.getParaInfoMap();

			for (Entry<String, TblPara> entry : paraInfoMap.entrySet()) {
				TblPara para = entry.getValue();
				m_colTypeMap.put(para.getParaNmEn(), para.getType());
				retList.add(para.getColDisplayNm());
			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger
				.debug("AutoDbToXls.getComboList(DbInfo dbInfo, TblBase base,HashMap<String, Integer> m_colTypeMap) end");
		return retList;
	}

	/**
	 * @param isToExcel 
	 * @param s_Db_Info
	 * @param text
	 * @throws Throwable
	 */
	public static void runSql(XmlDbXlsInfoAll xmlDbXlsInfoAll, String sqlPath) throws Throwable {
		CmnLog.logger.debug("AutoDbToXls.runSql(DbInfo dbInfo, String sqlPath) begin");
		FileOutputStream fileOut = null;
		FileInputStream fileIn = null;
		try {
			boolean isToExcel = xmlDbXlsInfoAll.getPicInfo().isToExcel();
			boolean isFromTbl = xmlDbXlsInfoAll.getPicInfo().isFromTbl();
			String ch = xmlDbXlsInfoAll.getPicInfo().getCh();
			String newLine = xmlDbXlsInfoAll.getPicInfo().getNewLine();
			
			xmlDbXlsInfoAll.getCurrentXmlDb().setXlsPath(CmnStrUtils.fromAtoBByTrim(
					xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath(), "", "_at_")
					+ CsjPath.s_current_date_end + CsjProcess.s_f_s);
			File f = new File(xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath());
			f.mkdirs();
			
			CsjDBAccess dbAccess = xmlDbXlsInfoAll.getDbInfo().getDbAccess();
			List<Object> paraList = new ArrayList<Object>();
			String sql = CmnFileUtils.getTxtFileInfo(sqlPath,
					xmlDbXlsInfoAll.getInfoSql().getEncode(), CsjConst.SIGN_SPACE_1);
			
			if (!isToExcel) {
				dbAccess.writeResultSet(sql, paraList, xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath() + "result.txt", xmlDbXlsInfoAll, ch, newLine);
				return;
			}
			List<HashMap<String, String>> dataList = xmlDbXlsInfoAll.getDbInfo().getDbAccess()
					.getRecordList(sql, paraList,CmnStrUtils.getIntVal(xmlDbXlsInfoAll.getXmlInfoXls().getMaxRow())).getResultList();

			String excelType = xmlDbXlsInfoAll.getXmlInfoXls().getExcelType();
			fileIn = new FileInputStream(
					CsjPath.s_file_pj_path_temp+excelType);
			
			Workbook wb = null;
			if (CsjConst.EXCEL_DOT_XLS_1997.equals(excelType)) {
				wb = new HSSFWorkbook(fileIn);
			} else {
				wb = new XSSFWorkbook(fileIn);
			}
			Sheet newSheet = wb.getSheet("temp");// wb.createSheet(tblNm);
			wb.setSheetName(wb.getSheetIndex(newSheet.getSheetName()), "result");

			CsjDbCellStyle cellStyle = new CsjDbCellStyle(wb,excelType,CsjDbCellStyle.CS_DATA_REC);

			for (int i = 0; i < dataList.size(); i++) {
				int colIndex = 0;
				for (Map.Entry<String, String> entry : dataList.get(i)
						.entrySet()) {

					String key = entry.getKey();
					String val = entry.getValue();
					if (i == 0) {
						CmnPoiUtils.setCellValueWithCs(newSheet, 0, colIndex,
								key,
								cellStyle.getCsTitleThin());
					}

					int type = DbInfo.getDbTypeByStr(dbAccess.getColTypeMap()
							.get(String.valueOf(colIndex)));
					if (DbInfo.TABLE_COL_TYPE_DATE == type) {
						CmnPoiUtils.setCellValueWithCs(newSheet, i + 1,
								colIndex,
								val, cellStyle.getCsRecordDate());
					} else if (DbInfo.TABLE_COL_TYPE_NUM == type) {
						CmnPoiUtils.setCellValueWithCs(newSheet, i + 1,
								colIndex,
								val, cellStyle.getCsRecordNum());
					} else {
						CmnPoiUtils.setCellValueWithCs(newSheet, i + 1,
								colIndex,
								val, cellStyle.getCsRecordStr());
					}
					colIndex++;
				}
			}

			fileOut = new FileOutputStream(
					xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath() + "result"+excelType);
			wb.write(fileOut);
			fileOut.close();
			fileIn.close();

		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		} finally {
			if (fileOut != null) {
				fileOut.close();
			}
			if (fileIn != null) {
				fileIn.close();
			}

		}

		CmnLog.logger
				.debug("AutoDbToXls.runSql(DbInfo dbInfo, String sqlPath) begin");

	}

	/**
	 * @param s_Db_Info
	 * @param file
	 * @throws Throwable
	 */
	public static CsjSqlType runUptSql(XmlDbXlsInfoAll xmlDbXlsInfoAll, String filePath)
			throws Throwable {
		CmnLog.logger
				.debug("AutoDbToXls.runUptSql(DbInfo dbInfo, String filePath) begin");
		int sum = 0;
		boolean retIsUptIns = false;
		try {
			String encode = xmlDbXlsInfoAll.getXmlInfoSql().getEncode();

			CsjDBAccess dbAccess = xmlDbXlsInfoAll.getDbInfo().getDbAccess();
			List<String> fileInfoList = CmnFileUtils.getFileContent(new File(
					filePath), encode);

			StringBuffer sb = new StringBuffer();

			for (String str : fileInfoList) {
				sb.append(str);
				sb.append(CsjProcess.s_newLine);
			}

			List<Object> paraList = new ArrayList<Object>();
			List<String> codeList = CmnStrUtils.getListStrSplit(sb.toString(),
					CsjDbToolsMsg.coreMsgMap
					.get(CsjDbToolsMsg.MSG_I_0000071),
					false, true);

			for (String strSQL : codeList) {
				boolean isUptIns = CmnStrUtils.isUptOrIns(strSQL);
				int cnt = dbAccess.executeSQL(strSQL, paraList);
				if (isUptIns) {
					sum += cnt;
					retIsUptIns = isUptIns;
				}
				
				AutoXlsToDbForMemory.exeInserLog("file command", xmlDbXlsInfoAll, strSQL);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger.debug("AutoDbToXls.runUptSql(DbInfo dbInfo, String filePath) end");
		return new CsjSqlType(sum, retIsUptIns);
	}
	public static SheetTblsInfo getDBInfoStruct(
			XmlDbXlsInfoAll xmlDbXlsInfoAll, HashSet<String> tblNmSet) throws Throwable {
		
		SheetTblsInfo tblsInfo = new SheetTblsInfo();
		try {
			List<TblBase> tblList = getTblBaseWithSome(xmlDbXlsInfoAll, tblNmSet);

			for (TblBase base : tblList) {
				XlsTblInfo xlsTblInfo = getXlsTblInfo(xmlDbXlsInfoAll, base);
				tblsInfo.getTblInfoMap().put(base.getTblNmEn(), xlsTblInfo);
			}
			
		for (Map.Entry<String, XlsTblInfo> entry : tblsInfo.getTblInfoMap()
				.entrySet()) {
			String tblNm = entry.getKey();

			CmnLog.logger.info("table:" + tblNm);
			
			XlsTblInfo xlsTblInfo = entry.getValue();

			for (Map.Entry<String, TblPara> entryPara : xlsTblInfo
					.getParaInfoMap().entrySet()) {
				TblPara tblPara = entryPara.getValue();

				HashMap<String, List<String>> dbTypeMap = tblsInfo
						.getDbTypeMap();

				String str = tblPara.getParaNmJp();
				if (CmnStrUtils.isNotEmpty(str)) {
					str = "(" + str + ")";
				} else {
					str = "";
				}
				String content = tblNm + CsjConst.MASK_COLON
						+ tblPara.getParaNmEn() + str;
				if (dbTypeMap.containsKey(tblPara.getParaTypeWithlen())) {
					dbTypeMap.get(tblPara.getParaTypeWithlen())
							.add(content);
				} else {
					List<String> list = new ArrayList<String>();
					list.add(content);
					dbTypeMap.put(tblPara.getParaTypeWithlen(), list);
				}
			}

			List<XlsRecord> tblDataList = xlsTblInfo.getTblDataList();
			if (CmnStrUtils.isNotEmpty(StaticClz.TABLE_NM)) {
				StaticClz.putTblCntMap(StaticClz.TABLE_NM, StaticClz.ONE_INDEX);
				Thread.sleep(100);
			}
			StaticClz.TABLE_NM = tblNm;
			StaticClz.ONE_INDEX = 0;
			CsjRetWithSql csjRetWithSql = getCsjRetWithSql(xmlDbXlsInfoAll,tblNm, xlsTblInfo,AutoDbToXls.s_is_max_data?RUN_DATA_OUT_SPLIT:RUN_DATA_OUT);

			List<HashMap<String, String>> dataList = csjRetWithSql.getDataList();

			for (int i = 0; i < dataList.size(); i++) {
				XlsRecord xlsRec = new XlsRecord();
				for (Map.Entry<String, TblPara> entryPara : xlsTblInfo
						.getParaInfoMap().entrySet()) {
					TblPara tblPara = entryPara.getValue();

					String colNm = entryPara.getKey();
					XlsTblPara xlsTblPara = new XlsTblPara(tblPara);
					HashMap<String, String> map = dataList.get(i);
					xlsTblPara.setParaVal(CmnStrUtils.convertString(map
							.get(tblPara.getParaNmEn())));
					xlsRec.getRecord().add(xlsTblPara);
				}
				tblDataList.add(xlsRec);
			}
		} } catch (Throwable e) {
			throw e;
		}
			
		return tblsInfo;
	}
		


}

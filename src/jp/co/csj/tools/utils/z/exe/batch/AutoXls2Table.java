/**
 *
 */
package jp.co.csj.tools.utils.z.exe.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mydbsee.common.CmnDateUtil;
import org.mydbsee.common.CmnFileUtils;
import org.mydbsee.common.CmnLog;
import org.mydbsee.common.CmnPoiUtils;
import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.utils.common.CsjPath;
import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.db.core.DbInfo;
import jp.co.csj.tools.utils.db.core.SheetTblsInfo;
import jp.co.csj.tools.utils.db.core.TableMakerIni;
import jp.co.csj.tools.utils.db.core.TblInfo;
import jp.co.csj.tools.utils.db.core.TblPara;
import jp.co.csj.tools.utils.db.core.XlsRecord;
import jp.co.csj.tools.utils.db.core.XlsTblInfo;
import jp.co.csj.tools.utils.db.core.XlsTblPara;
import jp.co.csj.tools.utils.db.core.ctbl.CTableInfo;
import jp.co.csj.tools.utils.msg.dbtools.CsjDbToolsMsg;
import jp.co.csj.tools.utils.poi.core.CsjCellInfo;
import jp.co.csj.tools.utils.poi.core.CsjRowInfo;
import jp.co.csj.tools.utils.poi.core.CsjSheetInfo;
import jp.co.csj.tools.utils.poi.core.CsjXlsIni;
import jp.co.csj.tools.utils.poi.core.Pos;
import jp.co.csj.tools.utils.poi.core.XlsIniKey;
import jp.co.csj.tools.utils.xml.dbtools.XmlDbXlsInfoAll;

/**
 * @author csj
 *
 */
public class AutoXls2Table {

	public static final String FILE_ABS_PATH = "FILE_ABS_PATH";
	public static final String FILE_PATH = "FILE_PATH";
	public static final String FILE_NM = "FILE_NM";
	public static final String FILE_UPDATE_TIME = "FILE_UPDATE_TIME";
	public static final String FILE_SIZE = "FILE_SIZE";
	public static final String SHEET_NM = "SHEET_NM";

	public static final String SHEET_NM_SIGN ="SHEET_NM_SIGN";
	public static final String OTHER_COL_EN_NM = "OTHER_COL_EN_NM";

	public static final String EXCEL_FILE_ABS_PATH = "EXCEL_FILE_ABS_PATH";
	public static final String EXCEL_SHEET_NM = "EXCEL_SHEET_NM";
	public static final String EXCEL_START_ROW = "EXCEL_START_ROW";
	public static final String EXCEL_END_ROW = "EXCEL_END_ROW";
	public static final String N_COL_EN_NM = "N_COL_EN_NM";

	public static final String EXCEL_ROW_NUM = "EXCEL_ROW_NUM";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//run(s_iniPath,s_bugPath,"xx",1);
		try {
			//getTableMakerIni("標準①");
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.info(e.getMessage());
		}
	}

	/**
	 * @param s_Db_Info
	 * @param str_make_table_name
	 * @param tableMakerIni
	 * @throws Throwable
	 *
	 */
	public static void runRecords(XmlDbXlsInfoAll xmlDbXlsInfoAll, List<File> fileList, TableMakerIni tableMakerIni) throws Throwable {
		String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
		createTblByRec(xmlDbXlsInfoAll,getRecTblInfo(xmlDbXlsInfoAll, tableMakerIni));
		Map<String ,TblPara> tblParaMap = tableMakerIni.getTblParaMap();

		for (File f : fileList) {

			HashMap<String, CsjSheetInfo> sheetInfoMap = CmnPoiUtils.getSheetContentsToMap(f.getAbsolutePath(),true);

			for (Entry<String,List<CTableInfo>> entry : tableMakerIni.getMapTblInfo().entrySet()) {
				String sheetNm  = entry.getKey();
				XlsRecord xlsRec = getOtherColVal(f,tblParaMap);
				if(tblParaMap.containsKey(SHEET_NM)) {
					XlsTblPara tXlsTblPara = new XlsTblPara(tblParaMap.get(SHEET_NM),sheetNm);
					xlsRec.getRecord().add(tXlsTblPara);
				}

				CsjSheetInfo sheetInfo = sheetInfoMap.get(sheetNm);

				for (CTableInfo tblInfo : entry.getValue()) {
					String val ="";
					if (sheetInfo!=null && sheetInfo.getCsjCellPosInfoMap()!= null && CmnStrUtils.isNotEmpty(sheetInfo.getCsjCellPosInfoMap().get(tblInfo.getPosStr()))) {
						val=sheetInfo.getCsjCellPosInfoMap().get(tblInfo.getPosStr()).getContent();
					}
					XlsTblPara tXlsTblPara = new XlsTblPara();
					tXlsTblPara.setParaNmEn(tblInfo.getTblColEnNm());
					tXlsTblPara.setParaStrLen(tblInfo.getTypeLength());
					tXlsTblPara.setParaTypeWithlen(dbType,getType(xmlDbXlsInfoAll, tblInfo.getColType(),tXlsTblPara.getParaStrLen()));
					tXlsTblPara.setParaVal(val);

					xlsRec.getRecord().add(tXlsTblPara);
				}
				executeSql(xmlDbXlsInfoAll,xlsRec,tableMakerIni.getTblNm());
			}
		}
	}

	/**
	 * @param dbInfo
	 * @param tableMakerIni
	 * @return
	 */
	public static TblInfo getRecTblInfo(XmlDbXlsInfoAll xmlDbXlsInfoAll,
			TableMakerIni tableMakerIni) {
		String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();

		TblInfo tblInf = new TblInfo();
		tblInf.getParaInfoMap().putAll(tableMakerIni.getTblParaMap());
		LinkedHashMap<String, TblPara> paraInfoMap = tblInf.getParaInfoMap();
		for (Entry<String, List<CTableInfo>> entry: tableMakerIni.getMapTblInfo().entrySet() ) {
			List<CTableInfo> list = entry.getValue();
			for(CTableInfo cTableInfo:list) {
				TblPara para = new TblPara();
				para.setParaNmEn(cTableInfo.getTblColEnNm());
				para.setParaNmJp(cTableInfo.getTblColJpNm());
				para.setParaStrLen(cTableInfo.getTypeLength());
				para.setParaTypeWithlen(dbType,getType(xmlDbXlsInfoAll,cTableInfo.getColType(),para.getParaStrLen()));
				paraInfoMap.put(para.getParaNmEn(), para);
			}
		}
		tblInf.setTblNmEn(tableMakerIni.getTblNm());
		return tblInf;
	}
	/**
	 * @param dbInfo
	 * @param xlsRec
	 * @param tblNm
	 * @throws Throwable
	 * @throws SQLException
	 */
	private static void executeSql(XmlDbXlsInfoAll xmlDbXlsInfoAll, XlsRecord xlsRec, String tblNm) throws Throwable {
		List<Object> paraList = new ArrayList<Object>();
		String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
		String sql = AutoXlsToDbForMemory.getInsertExeSql(dbType,tblNm, xlsRec,paraList,xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot());
//		MyLog.logger.debug(AutoXlsToDbForMemory.getInsertSql(tblNm, xlsRec,dbType,xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()));
		xmlDbXlsInfoAll.getDbInfo().getDbAccess().executeSQL(sql,paraList);
	}

	/**
	 * @param f
	 * @param xlsRec
	 * @param tblParaMap
	 * @throws Throwable
	 */
	private static XlsRecord getOtherColVal(File f, Map<String, TblPara> tblParaMap) throws Throwable {
		XlsRecord xlsRec = new XlsRecord();
		List<XlsTblPara> recList = xlsRec.getRecord();


		if(tblParaMap.containsKey(FILE_ABS_PATH)) {
			XlsTblPara tXlsTblPara = new XlsTblPara(tblParaMap.get(FILE_ABS_PATH),f.getAbsolutePath());
			recList.add(tXlsTblPara);
		}
		if(tblParaMap.containsKey(FILE_PATH)) {
			XlsTblPara tXlsTblPara = new XlsTblPara(tblParaMap.get(FILE_PATH),f.getParent());
			recList.add(tXlsTblPara);
		}
		if(tblParaMap.containsKey(FILE_NM)) {
			XlsTblPara tXlsTblPara = new XlsTblPara(tblParaMap.get(FILE_NM),f.getName());
			recList.add(tXlsTblPara);
		}
		if(tblParaMap.containsKey(FILE_UPDATE_TIME)) {
			XlsTblPara tXlsTblPara = new XlsTblPara(tblParaMap.get(FILE_UPDATE_TIME),CmnDateUtil.getDateTime(f.lastModified()));
			recList.add(tXlsTblPara);
		}
		if(tblParaMap.containsKey(FILE_SIZE)) {
			XlsTblPara tXlsTblPara = new XlsTblPara(tblParaMap.get(FILE_SIZE),CmnFileUtils.getFileSize(f));
			recList.add(tXlsTblPara);
		}


		return xlsRec;
	}

	/**
	 * @param dbInfo
	 * @param mapTblInfo
	 * @throws Throwable
	 */
	private static void
			createTblByRec(XmlDbXlsInfoAll xmlDbXlsInfoAll, TblInfo tblInf) throws Throwable {
		List<String> createTblSqlList = null;
		String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();

		String schemaDot = xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot();
		if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
			createTblSqlList = AutoXlsToCreateTblSql.getCreateOracle(tblInf,schemaDot);
		} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
			createTblSqlList = AutoXlsToCreateTblSql.getCreateServer(tblInf);
		} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
			createTblSqlList = AutoXlsToCreateTblSql.getCreatePostgre(tblInf,schemaDot);
		} else {
			createTblSqlList = AutoXlsToCreateTblSql.getCreateMySql(xmlDbXlsInfoAll.getDbInfo().getDbAccess(), tblInf,schemaDot);
		}

		String sql = CmnStrUtils.getStrByList(createTblSqlList);
		for (String str : sql.split(CsjDbToolsMsg.coreMsgMap
				.get(CsjDbToolsMsg.MSG_I_0000071))) {
			CmnLog.logger.debug(str+CsjDbToolsMsg.coreMsgMap
					.get(CsjDbToolsMsg.MSG_I_0000071));
			xmlDbXlsInfoAll.getDbInfo().getDbAccess().executeSQL(str, null);
		}

	}


	/**
	 * @param dbInfo
	 * @param colType
	 * @param strLength
	 * @return
	 */
	private static String getType(XmlDbXlsInfoAll xmlDbXlsInfoAll, int colType, String strLength) {
		String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();

		String retStr = "";
		if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
			if (colType == DbInfo.TABLE_COL_TYPE_DATE) {
				retStr="DATE";
			} else if (colType == DbInfo.TABLE_COL_TYPE_NUM) {
				retStr="NUMBER(16,2)";
			} else {
				if (CmnStrUtils.isEmpty(strLength)) {
					retStr="VARCHAR2(5000)";
				} else {
					retStr="VARCHAR2("+strLength+")";
				}
			}
		} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
			if (colType == DbInfo.TABLE_COL_TYPE_DATE) {
				retStr="DATE";
			} else if (colType == DbInfo.TABLE_COL_TYPE_NUM) {
				retStr="DECIMAL(16,2)";
			} else {
				if (CmnStrUtils.isEmpty(strLength)) {
					retStr="text";
				} else {
					retStr="VARCHAR("+strLength+")";
				}

			}
		} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
			if (colType == DbInfo.TABLE_COL_TYPE_DATE) {
				retStr="DATE";
			} else if (colType == DbInfo.TABLE_COL_TYPE_NUM) {
				retStr="NUMERIC(16,2)";
			} else {
				if (CmnStrUtils.isEmpty(strLength)) {
					retStr = "text";
				} else {
					retStr = "CHARACTER VARYING(" + strLength + ")";
				}
			}
		} else {
			if (colType == DbInfo.TABLE_COL_TYPE_DATE) {
				retStr="DATETIME";
			} else if (colType == DbInfo.TABLE_COL_TYPE_NUM) {
				retStr="DECIMAL(12,2)";
			} else {
				if (CmnStrUtils.isEmpty(strLength)) {
					retStr = "text";
				} else {
					retStr = "VARCHAR(" + strLength + ")";
				}
			}
		}
		return retStr;
	}

	/**
	 * @throws Throwable
	 *
	 */
	public static void runTable(XmlDbXlsInfoAll xmlDbXlsInfoAll, TableMakerIni tableMakerIni) throws Throwable {
		String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
			createTblByRec(xmlDbXlsInfoAll,get1TblInfo(xmlDbXlsInfoAll, tableMakerIni));
			HashMap<String, CsjSheetInfo> sheetInfoMap = CmnPoiUtils.getSheetContentsToMap(tableMakerIni.getFileAbsPath(),true);
			CsjSheetInfo sheetInfo = sheetInfoMap.get(tableMakerIni.getSheetNm());

			Map<Integer,CsjRowInfo> csjRowInfoMap = sheetInfo.getCsjRowInfoMap();
			for (Entry<Integer,CsjRowInfo> e : csjRowInfoMap.entrySet()) {
				if (tableMakerIni.getStartRow() <= e.getKey() && e.getKey() <= tableMakerIni.getEndRow()) {
					boolean isExe = true;
					XlsRecord xlsRec = new XlsRecord();
					for (Entry<String,CTableInfo> entry : tableMakerIni.getTblMap().entrySet()) {

						CTableInfo tblInfo =  entry.getValue();
						tblInfo.setRowNum(e.getKey());
						String val ="";
						if (EXCEL_ROW_NUM.equals(entry.getKey())) {
							val = String.valueOf(e.getKey()+1);
						} else {
							if (sheetInfo!=null && sheetInfo.getCsjCellPosInfoMap()!= null && CmnStrUtils.isNotEmpty(sheetInfo.getCsjCellPosInfoMap().get(tblInfo.getPosStr()))) {
								val=sheetInfo.getCsjCellPosInfoMap().get(tblInfo.getPosStr()).getContent();
							}
						}

						if (tblInfo.isPk() && CmnStrUtils.isEmpty(val)) {
							isExe = false;
							break;
						}
						XlsTblPara tXlsTblPara = new XlsTblPara();
						tXlsTblPara.setParaNmEn(tblInfo.getTblColEnNm());
						tXlsTblPara.setParaStrLen(tblInfo.getTypeLength());
						tXlsTblPara.setParaTypeWithlen(dbType,getType(xmlDbXlsInfoAll, tblInfo.getColType(),tXlsTblPara.getParaStrLen()));
						tXlsTblPara.setParaVal(val);
						xlsRec.getRecord().add(tXlsTblPara);
					}
					if (isExe) {
						executeSql(xmlDbXlsInfoAll,xlsRec,tableMakerIni.getTblNm());
					}
				}
			}

	}
	/**
	 * @param dbInfo
	 * @param tableMakerIni
	 * @return
	 */
	private static TblInfo get1TblInfo(XmlDbXlsInfoAll xmlDbXlsInfoAll,
			TableMakerIni tableMakerIni) {
		String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
		TblInfo tblInf = new TblInfo();
		LinkedHashMap<String, TblPara> paraInfoMap = tblInf.getParaInfoMap();
		for (Entry<String, CTableInfo> entry: tableMakerIni.getTblMap().entrySet() ) {
			CTableInfo cTableInfo = entry.getValue();
			TblPara para = new TblPara();
			para.setParaNmEn(cTableInfo.getTblColEnNm());
			para.setParaNmJp(cTableInfo.getTblColJpNm());
			para.setPkey(cTableInfo.isPk());
			para.setParaStrLen(cTableInfo.getTypeLength());
			para.setParaTypeWithlen(dbType,getType(xmlDbXlsInfoAll,cTableInfo.getColType(),para.getParaStrLen()));
			paraInfoMap.put(para.getParaNmEn(), para);
		}
		tblInf.setTblNmEn(tableMakerIni.getTblNm());
		return tblInf;
	}

	/**
	 * @param iniPath
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static CsjXlsIni getXlsIniByTable(String iniPath) throws Throwable {
		CsjXlsIni xlsIni = new CsjXlsIni();
		File file = new File(iniPath);
		FileInputStream fs = new FileInputStream(file);
		Workbook wb = null;
		if (CmnStrUtils.isEndByIgnor(iniPath, CsjConst.EXCEL_DOT_XLS_1997)) {
			wb = new HSSFWorkbook(fs);
		} else {
			wb = new XSSFWorkbook(fs);
		}
		

		getPosInfoTable(wb, xlsIni);
		getKeyInfoRecords(wb, xlsIni.getIniKeyList());
		List<String> strList= CmnPoiUtils.getCellContents(wb.getSheet("CsjGetSheet"), 0, 1,99999, false);
		Set<String> sheetNmSet = xlsIni.getSheetNmSet();

		for (String str :strList ) {
			sheetNmSet.add(str);
		}
		fs.close();
		return xlsIni;
	}


	/**
	 * @param wb
	 * @param xlsIni
	 * @throws Throwable 
	 */
	private static void getPosInfoTable(Workbook wb, CsjXlsIni xlsIni) throws Throwable {
		Sheet sheet = wb.getSheet("PosInfo");
		int rowY = 0;
		for (Row row : sheet) {
			List<String> strList =  CmnPoiUtils.getCellContents(row, true);
			if (strList.size()>0) {
				String sign = CmnStrUtils.toLowOrUpStr(strList.get(0));
				if (CmnStrUtils.isNotEmpty(sign)) {
					if (sign.contains("rowmin")) {
						xlsIni.setRowMin(Integer.parseInt(strList.get(1)));
						continue;
					} else if (sign.contains("rowmax")) {
						xlsIni.setRowMax(Integer.parseInt(strList.get(1)));
						continue;
					} else if (sign.contains("col")) {
						rowY = row.getRowNum();
						break;
					}
				}
			}
		}

		for (Row row : sheet) {
			List<String> strList =  CmnPoiUtils.getCellContents(row, true);
			if (strList.size()>0) {
				String sign = strList.get(0);
				if (CmnStrUtils.isNotEmpty(sign)) {
					if (sign.contains(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000003))) {
						for (Cell cell : row) {
							String key = CmnPoiUtils.getCellContent(cell, true);
							int cellIndex = cell.getColumnIndex();
							int y = (int)CmnStrUtils.getLongVal(CmnPoiUtils.getCellContent(sheet,rowY,cellIndex, true));
							Pos pos =  new Pos(0, y, cell.getColumnIndex());
							xlsIni.getPosMap().put(key, pos);
						}
					}
				}
			}
		}
	}

	public static CsjXlsIni getRecordsXlsIni(String iniPath) throws Throwable {
		CsjXlsIni xlsIni = new CsjXlsIni();
		File file = new File(iniPath);
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));

		getPosInfoRecords(wb, xlsIni.getPosMap());
		getKeyInfoRecords(wb, xlsIni.getIniKeyList());
		List<String> strList= CmnPoiUtils.getCellContents(wb.getSheet("CsjGetSheet"), 0, 1,99999, false);
		Set<String> sheetNmSet = xlsIni.getSheetNmSet();

		for (String str :strList ) {
			sheetNmSet.add(str);
		}

		return xlsIni;
	}

	/**
	 * @param wb
	 * @param list
	 * @throws Throwable 
	 */
	private static void getKeyInfoRecords(Workbook wb, List<XlsIniKey> keyList) throws Throwable {
		Sheet sheet = wb.getSheet("CsjTempKey");
		for (Row row : sheet) {
			if (row.getRowNum() < 1) {
				continue;
			}
			XlsIniKey iniKey = new XlsIniKey();
			iniKey.setWbNm(CmnPoiUtils.getCellContent(row, 0, false));
			iniKey.setSheetNm(CmnPoiUtils.getCellContent(row, 1, false));
			iniKey.getPos().setX((int)CmnStrUtils.getLongVal(CmnPoiUtils.getCellContent(row, 2, false)));
			iniKey.getPos().setY((int)CmnStrUtils.getLongVal(CmnPoiUtils.getCellContent(row, 3, false)));
			iniKey.setVal(CmnPoiUtils.getCellContent(row, 4, false));
			keyList.add(iniKey);
		}
	}

	/**
	 * @param wb
	 * @param iniMap
	 * @throws Throwable 
	 */
	private static void getPosInfoRecords(HSSFWorkbook wb, LinkedHashMap<String, Pos> iniMap) throws Throwable {
		HSSFSheet sheet = wb.getSheet("PosInfo");
		int rowY = 0;
		int rowX = 0;
		for (Row row : sheet) {
			List<String> strList =  CmnPoiUtils.getCellContents(row, true);
			if (strList.size()>0) {
				String sign = CmnStrUtils.toLowOrUpStr(strList.get(0));
				if (CmnStrUtils.isNotEmpty(sign)) {
					if (sign.contains("row")) {
						rowX = row.getRowNum();
						continue;
					} else if (sign.contains("col")) {
						rowY = row.getRowNum();
						break;
					}
				}
			}
		}

		for (Row row : sheet) {
			List<String> strList =  CmnPoiUtils.getCellContents(row, true);
			if (strList.size()>0) {
				String sign = strList.get(0);
				if (CmnStrUtils.isNotEmpty(sign)) {
					if (sign.contains(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000003))) {
						for (Cell cell : row) {
							String key = CmnPoiUtils.getCellContent(cell, true);
							int cellIndex = cell.getColumnIndex();
							int x = (int)CmnStrUtils.getLongVal(CmnPoiUtils.getCellContent(sheet,rowX,cellIndex, true));
							int y = (int)CmnStrUtils.getLongVal(CmnPoiUtils.getCellContent(sheet,rowY,cellIndex, true));
							iniMap.put(key, new Pos(x, y,cell.getColumnIndex()));
						}
					}
				}

			}
		}
	}

	/**
	 * @param dbInfo
	 * @return
	 * @throws Throwable
	 */
	public static TableMakerIni getTableMakerIni(XmlDbXlsInfoAll xmlDbXlsInfoAll, String sheetNm) throws Throwable {
		TableMakerIni tableMakerIni = new TableMakerIni();
		CsjSheetInfo csjSheetInfo = CmnPoiUtils.getSheetContents(CsjPath.s_file_db_table_maker, sheetNm, true);

		Map<String,CsjCellInfo> csjCellInfoMap = csjSheetInfo.getCsjCellInfoMap();
		Map<String,CsjCellInfo> csjCellPosInfoMap = csjSheetInfo.getCsjCellPosInfoMap();

		CsjCellInfo cellInfoSheet = csjCellInfoMap.get(SHEET_NM_SIGN);
		List<CsjCellInfo> cellInfoColList = csjSheetInfo.getCsjCellPosInfoColList().get(cellInfoSheet.getCellNum());

		Map<String,List<CTableInfo>> mapTblInfo = tableMakerIni.getMapTblInfo();
		for (CsjCellInfo cellInfo : cellInfoColList) {

			if(cellInfoSheet.getRowNum()<cellInfo.getRowNum()) {
				String stNm = cellInfo.getContent();
				String tblColEnNm = csjCellPosInfoMap.get(cellInfo.getRowNum() + "_"+(cellInfo.getCellNum()+1)).getContent();
				String tblColJpNm =csjCellPosInfoMap.get(cellInfo.getRowNum() + "_"+(cellInfo.getCellNum()+2)).getContent();
				String dbType = csjCellPosInfoMap.get(cellInfo.getRowNum() + "_"+(cellInfo.getCellNum()+3)).getContent();
				CsjCellInfo tInfo = csjCellPosInfoMap.get(cellInfo.getRowNum() + "_"+(cellInfo.getCellNum()+4));
				String typeLength = tInfo == null?"":tInfo.getContent();
				String rowNum = csjCellPosInfoMap.get(cellInfo.getRowNum() + "_"+(cellInfo.getCellNum()+5)).getContent();
				String colNum = csjCellPosInfoMap.get(cellInfo.getRowNum() + "_"+(cellInfo.getCellNum()+6)).getContent();
				if(CmnStrUtils.isNotEmpty(dbType)&&CmnStrUtils.isNotEmpty(tblColEnNm)&&CmnStrUtils.isNotEmpty(rowNum)&&CmnStrUtils.isNotEmpty(colNum)) {
					CTableInfo cTableInfo = new CTableInfo(rowNum,colNum, tblColEnNm,tblColJpNm, typeLength,getTypeByString(dbType),false);
					if (mapTblInfo.containsKey(stNm)) {
						mapTblInfo.get(stNm).add(cTableInfo);
					} else {
						List<CTableInfo> cTableInfos = new ArrayList<CTableInfo>();
						cTableInfos.add(cTableInfo);
						mapTblInfo.put(stNm, cTableInfos);
					}
				}
			}
		}

		CsjCellInfo cellInfoOtherCol = csjCellInfoMap.get(OTHER_COL_EN_NM);
		List<CsjCellInfo> cellInfoOtherColList = csjSheetInfo.getCsjCellPosInfoColList().get(cellInfoOtherCol.getCellNum());

		for (CsjCellInfo cellInfo : cellInfoOtherColList) {
			if(cellInfoOtherCol.getRowNum()<cellInfo.getRowNum()) {
				String tblColJpNm = csjCellPosInfoMap.get(cellInfo.getRowNum() + "_"+(cellInfo.getCellNum()+1)).getContent();
				String dbType =csjCellPosInfoMap.get(cellInfo.getRowNum() + "_"+(cellInfo.getCellNum()+2)).getContent();
				CsjCellInfo tInfo =csjCellPosInfoMap.get(cellInfo.getRowNum() + "_"+(cellInfo.getCellNum()+3));
				String typeLength = tInfo == null?"":tInfo.getContent();
				String isKey = csjCellPosInfoMap.get(cellInfo.getRowNum() + "_"+(cellInfo.getCellNum()+4)).getContent();

				TblPara tblPara = new TblPara();
				tblPara.setParaNmEn(CmnStrUtils.toLowOrUpStr(cellInfo.getContent()));
				tblPara.setParaNmJp(tblColJpNm);
				tblPara.setParaStrLen(typeLength);
				tblPara.setParaTypeWithlen(dbType,getType(xmlDbXlsInfoAll,getTypeByString(dbType),tblPara.getParaStrLen()));
				tblPara.setPkey(isKey.equals("1"));
				tableMakerIni.getTblParaMap().put(tblPara.getParaNmEn(),tblPara);
			}
		}

		CsjCellInfo startCell = csjCellPosInfoMap.get(csjCellInfoMap.get(EXCEL_START_ROW).getRowNum()+"_"+(csjCellInfoMap.get(EXCEL_START_ROW).getCellNum()+1));
		tableMakerIni.setStartRow(CmnStrUtils.getIntVal(startCell.getContent()));
		CsjCellInfo endCell = csjCellPosInfoMap.get(csjCellInfoMap.get(EXCEL_END_ROW).getRowNum()+"_"+(csjCellInfoMap.get(EXCEL_END_ROW).getCellNum()+1));
		tableMakerIni.setEndRow(CmnStrUtils.getIntVal(endCell.getContent()));

		CsjCellInfo absCell = csjCellPosInfoMap.get(csjCellInfoMap.get(EXCEL_FILE_ABS_PATH).getRowNum()+"_"+(csjCellInfoMap.get(EXCEL_FILE_ABS_PATH).getCellNum()+1));
		tableMakerIni.setFileAbsPath(absCell.getContent());
		CsjCellInfo sheetCell = csjCellPosInfoMap.get(csjCellInfoMap.get(EXCEL_SHEET_NM).getRowNum()+"_"+(csjCellInfoMap.get(EXCEL_SHEET_NM).getCellNum()+1));
		tableMakerIni.setSheetNm(sheetCell.getContent());

		CsjCellInfo cellNInfo=csjCellInfoMap.get(N_COL_EN_NM);
		List<CsjCellInfo> colNList = csjSheetInfo.getCsjCellPosInfoColList().get(cellNInfo.getCellNum());
		for (CsjCellInfo cellInfo : colNList) {

			if(cellNInfo.getRowNum()<cellInfo.getRowNum()) {
				String tblColEnNm = cellInfo.getContent();
				String tblColJpNm = csjCellPosInfoMap.get(cellInfo.getRowNum() + "_"+(cellInfo.getCellNum()+1)).getContent();
				String dbType =CmnStrUtils.toLowOrUpStr(csjCellPosInfoMap.get(cellInfo.getRowNum() + "_"+(cellInfo.getCellNum()+2)).getContent());
				CsjCellInfo tInfo = csjCellPosInfoMap.get(cellInfo.getRowNum() + "_"+(cellInfo.getCellNum()+3));
				String typeLength = tInfo == null?"":tInfo.getContent();
				String colNum = csjCellPosInfoMap.get(cellInfo.getRowNum() + "_"+(cellInfo.getCellNum()+4)).getContent();
				String isKey = csjCellPosInfoMap.get(cellInfo.getRowNum() + "_"+(cellInfo.getCellNum()+5)).getContent();
				if(CmnStrUtils.isNotEmpty(dbType)&&CmnStrUtils.isNotEmpty(tblColEnNm)&&CmnStrUtils.isNotEmpty(colNum)) {
					CTableInfo cTableInfo = new CTableInfo("",colNum, tblColEnNm,tblColJpNm, typeLength,getTypeByString(dbType),"1".equals(isKey));
					tableMakerIni.getTblMap().put(cTableInfo.getTblColEnNm(),cTableInfo);
				}
			}
		}
		return tableMakerIni;
	}

	/**
	 * @param val
	 * @return
	 */
	public static int getTypeByString(String val) {
		int type = DbInfo.TABLE_COL_TYPE_STR;
		if (CmnStrUtils.toLowOrUpStr(val).equals(CmnStrUtils.toLowOrUpStr("number"))) {
			type =  DbInfo.TABLE_COL_TYPE_NUM;
		} else if (CmnStrUtils.toLowOrUpStr(val).equals(CmnStrUtils.toLowOrUpStr("date"))) {
			type =  DbInfo.TABLE_COL_TYPE_DATE;
		}
		return type;
	}

	/**
	 * @param s_Db_Info
	 * @param str_make_format_path
	 * @param fileList
	 * @param tableMakerIni
	 * @param isFromTbl 
	 */
	public static void runRewriteRecords(XmlDbXlsInfoAll xmlDbXlsInfoAll, String formatAbsPath, TableMakerIni tableMakerIni,boolean isToExcel,String ch, String newLine, boolean isFromTbl) {
		try {

			HashSet<String> tableNmSet = new HashSet<String>();
			tableNmSet.add(tableMakerIni.getTblNm());
			SheetTblsInfo sheetTblsInfo = AutoDbToXls.getDBInfo(xmlDbXlsInfoAll, tableNmSet);
			Map<String,List<CTableInfo>> mapTblInfo = tableMakerIni.getMapTblInfo();

			XlsTblInfo xlsTblInfo =  sheetTblsInfo.getTblInfoMap().get(tableMakerIni.getTblNm());
			for (XlsRecord rec : xlsTblInfo.getTblDataList()) {
				List<XlsTblPara> paraList = rec.getRecord();
				Map<String ,XlsTblPara> map = new LinkedHashMap<String, XlsTblPara>();

				for (XlsTblPara para : paraList) {
					map.put(para.getParaNmEn(), para);
				}

				String fileAbsPath = map.get(FILE_ABS_PATH).getParaVal();
				String sheetNm = map.get(SHEET_NM).getParaVal();
				List<CTableInfo> cTableInfoList = mapTblInfo.get(sheetNm);
				for (CTableInfo cTableInfo:cTableInfoList) {
					CmnPoiUtils.setCellValue(fileAbsPath,sheetNm,cTableInfo.getRowNum(),cTableInfo.getColNum(),map.get(cTableInfo.getTblColEnNm()).getParaVal());
				}

			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param s_Db_Info
	 * @param tableMakerIni
	 * @param isFromTbl 
	 * @throws Throwable
	 * @throws FileNotFoundException
	 */
	public static void runRewriteTable(XmlDbXlsInfoAll xmlDbXlsInfoAll,
			TableMakerIni tableMakerIni,boolean isToExcel,String ch, String newLine, boolean isFromTbl) throws Throwable {
		File file = new File(tableMakerIni.getFileAbsPath());
		FileInputStream fileIn = new FileInputStream(file);
		POIFSFileSystem fs = new POIFSFileSystem(fileIn);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheet(tableMakerIni.getSheetNm());

		HashSet<String> tableNmSet = new HashSet<String>();
		tableNmSet.add(tableMakerIni.getTblNm());
		SheetTblsInfo sheetTblsInfo = AutoDbToXls.getDBInfo(xmlDbXlsInfoAll, tableNmSet);
		Map<String,CTableInfo> mapTblInfo = tableMakerIni.getTblMap();

		XlsTblInfo xlsTblInfo =  sheetTblsInfo.getTblInfoMap().get(tableMakerIni.getTblNm());

		for (XlsRecord rec : xlsTblInfo.getTblDataList()) {
			List<XlsTblPara> paraList = rec.getRecord();
			Map<String ,XlsTblPara> paraMap = new LinkedHashMap<String, XlsTblPara>();

			for (XlsTblPara xlsTblPara : paraList) {
				paraMap.put(xlsTblPara.getParaNmEn(), xlsTblPara);
			}

			for (Map.Entry<String, CTableInfo> entry : mapTblInfo
					.entrySet()) {

				CTableInfo cTableInfo = entry.getValue();
				XlsTblPara xlsTblPara = paraMap.get(cTableInfo.getTblColEnNm());
				CmnPoiUtils.setCellValue(sheet, CmnStrUtils.getIntVal(paraMap.get(EXCEL_ROW_NUM).getParaVal())-1, cTableInfo.getColNum(), xlsTblPara.getParaVal());
			}
		}
		FileOutputStream fileOut = new FileOutputStream(tableMakerIni.getFileAbsPath());
		wb.write(fileOut);
		fileOut.close();
		fileIn.close();
	}
}

package jp.co.csj.tools.utils.db.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.mydbsee.common.CmnLog;
import org.mydbsee.common.CmnPoiUtils;
import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.core.CsjLinkedMap;
import jp.co.csj.tools.utils.common.CsjPath;
import jp.co.csj.tools.utils.common.StaticClz;
import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.db.CsjDBAccess;
import jp.co.csj.tools.utils.db.convert.ConvertColumn;
import jp.co.csj.tools.utils.db.convert.ConvertDb;
import jp.co.csj.tools.utils.db.convert.ConvertTbl;
import jp.co.csj.tools.utils.db.convert.DbConvertInfo;
import jp.co.csj.tools.utils.msg.dbtools.CsjDbToolsMsg;
import jp.co.csj.tools.utils.poi.core.CsjCellInfo;
import jp.co.csj.tools.utils.poi.core.CsjColInfo;
import jp.co.csj.tools.utils.poi.core.CsjRowInfo;
import jp.co.csj.tools.utils.poi.core.CsjSheetInfo;
import jp.co.csj.tools.utils.reg.RegConstStr;
import jp.co.csj.tools.utils.xml.dbtools.XmlDbXlsInfoAll;
import jp.co.csj.tools.utils.z.exe.batch.AutoDbToXls;

public class DbInfo {

	public static final String STR_DB_TYPE_ORACLE =  "ORACLE";
	public static final String STR_DB_TYPE_POSTGRE =  "POSTGRE";
	public static final String STR_DB_TYPE_SQLSERVER =  "SQLSERVER";
	public static final String STR_DB_TYPE_MYSQL =  "MYSQL";
	public static final String STR_DB_TYPE_DB2 =  "DB2";
	public static final String STR_DB_TYPE_SYBASE =  "SYBASE";
	public static final String STR_DB_TYPE_SQLITE =  "SQLITE";
	public static final int TABLE_COL_TYPE_NULL =  0;
	public static final int TABLE_COL_TYPE_STR =  1;
	public static final int TABLE_COL_TYPE_NUM =  2;
	public static final int TABLE_COL_TYPE_DATE =  3;
	public static final String TABLE_COL_TYPE_NULL_INFO = CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_P_0000133);
	public static final String TABLE_COL_TYPE_STR_INFO =   CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_P_0000026);
	public static final String TABLE_COL_TYPE_NUM_INFO =  CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_P_0000025);
	public static final String TABLE_COL_TYPE_DATE_INFO = CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_P_0000021);

	public static final String S_LOG_TABLE = "ZZZ_TOOLS_LOG";
	public static final int DB_MEMORY_CHANGED = 1;
	public static final int DB_MEMORY_CHANGING = 2;
	public static final int DB_MEMORY_RESET = 3;
	public static final int DB_MEMORY_DO_NOTHING = 0;

	private int dbMemoryChangeType = DB_MEMORY_DO_NOTHING;

	private XmlDbXlsInfoAll xmlDbXlsInfoAll = null;
	protected String checkDate = "";

	protected CsjDBAccess dbAccess = null;
	protected LinkedHashMap<String, TblInfo> tblMap = new LinkedHashMap<String, TblInfo>();
	protected DbConvertInfo dbConvertInfo;
	protected List<TblBase> tblInfoList = new ArrayList<TblBase>();


	private CsjLinkedMap<String, TblInfo> importChkTblMap = new CsjLinkedMap<String, TblInfo>();
	private CsjLinkedMap<String, CsjTblSelectSql> importSqlMapWithNo = new CsjLinkedMap<String, CsjTblSelectSql>();
	private CsjLinkedMap<String, CsjTblSelectSql> importSqlMapWithTblnm = new CsjLinkedMap<String, CsjTblSelectSql>();
	private CsjLinkedMap<String, CsjTblSelectSql> selectSqlMapWithNo = new CsjLinkedMap<String, CsjTblSelectSql>();
	private CsjLinkedMap<String, CsjTblSelectSql> selectSqlMapWithTblnm = new CsjLinkedMap<String, CsjTblSelectSql>();
	private String sycnExcelPath;
	private String sycnExcelSheetName;
	private String excelSheetName;
	private String schedualDbInPath;
	private String schedualDbOutPath;
	private String compareStructResultPath;
	private String createSqlPath;
	private boolean isHaveLogTbl = true;
	
	public void resetDbAll() throws Throwable {
		try {
			getDbAccess().closeConnection();
			getDbAccess().openConnection();
			getDbTablsesAllInfo();
			setDbMemoryChangeType(DbInfo.DB_MEMORY_RESET);
			getSelectSqlMapWithNo().clear();
		} catch (Throwable e) {
			throw e;
		}

	}

	public void clearSheetInfo() {
		importChkTblMap.clear();
		importSqlMapWithNo.clear();
		importSqlMapWithTblnm.clear();
//		selectSqlMapWithNo.clear();
//		selectSqlMapWithTblnm.clear();
	}
	public void resetTblInfoList() throws Throwable {
		CmnLog.logger.info("resetTblInfoList() begin");
		try {
			tblInfoList.clear();
			StaticClz.tblNmSet.clear();
			StaticClz.colNmSet.clear();
			
			Map<Integer,LinkedHashSet<String>> tblNmMap = new TreeMap<Integer, LinkedHashSet<String>>();  
			Map<Integer,LinkedHashSet<String>> colNmMap = new TreeMap<Integer, LinkedHashSet<String>>();
			
			List<String> tblLst = new ArrayList<String>();
			List<String> colLst = new ArrayList<String>();
			
			for (TblInfo tblInfo : tblMap.values()) {

				if (CmnStrUtils.isNotEmpty(tblInfo)) {
					LinkedHashMap<String, TblPara> paraInfoMap = tblInfo.getParaInfoMap();
					if (CmnStrUtils.isNotEmpty(paraInfoMap)) {
						Object[] objArr = paraInfoMap.values().toArray();
						if (objArr.length>0) {
							TblPara para = (TblPara)objArr[objArr.length-1];
							para.setLastPara(true);
						}
						tblInfoList.add(tblInfo);
						
						String tblLow = tblInfo.getTblNmEn().toLowerCase();
						LinkedHashSet<String> tSet = tblNmMap.get(tblLow.length());
						if (CmnStrUtils.isEmpty(tSet)) {
							tSet = new LinkedHashSet<String>();
							tSet.add(tblLow);
							tblNmMap.put(tblLow.length(), tSet);
						} else {
							tSet.add(tblLow);
						}
						int index = 1;
						for (TblPara para : paraInfoMap.values()) {
							para.setParaPos(index++);
							String colLow = para.getParaNmEn().toLowerCase();
							LinkedHashSet<String> cSet = colNmMap.get(colLow.length());
							if (CmnStrUtils.isEmpty(cSet)) {
								cSet = new LinkedHashSet<String>();
								cSet.add(colLow);
								colNmMap.put(colLow.length(), cSet);
							} else {
								cSet.add(colLow);
							}
						}
					}
				}
			}
			
			
			for (LinkedHashSet<String> set : tblNmMap.values()) {
				for (String s:set) {
					tblLst.add(s);
				}
			}
			for (LinkedHashSet<String> set : colNmMap.values()) {
				for (String s:set) {
					colLst.add(s);
				}
			}
			for (int i = tblLst.size()-1; i >=0 ; i--) {
				StaticClz.tblNmSet.add(CsjConst.SIGN_SPACE_1 + tblLst.get(i) + CsjConst.SIGN_SPACE_1);
				StaticClz.tblNmSet.add(CsjConst.SIGN_SPACE_1 + tblLst.get(i) + CsjConst.SIGN_SEMICOLON);
				StaticClz.tblNmSet.add(tblLst.get(i) + CsjConst.SIGN_DOT);
				StaticClz.tblNmSet.add(CsjConst.SIGN_DOUBLE + tblLst.get(i) + CsjConst.SIGN_DOUBLE);
			}
			for (int i = colLst.size()-1; i >=0 ; i--) {
				StaticClz.colNmSet.add(colLst.get(i));
			}
		} catch (Throwable e) {
			throw e;
		}
		CmnLog.logger.info("resetTblInfoList() end");
	}
	/**
	 * @throws Throwable
	 */
	public void getDbTablsesAllInfo() throws Throwable {
		CmnLog.logger.debug("getDbTablsesAllInfo begin");
		try {
			this.tblInfoList.clear();
			this.tblInfoList = AutoDbToXls.getAllTablesNm(this.xmlDbXlsInfoAll, new HashSet<String>(),getConfigTblNms(CsjPath.s_file_db_read_tables, this.xmlDbXlsInfoAll.getTableSheet()),true);

			this.tblMap.clear();
			
			CmnLog.logger.info(this.dbAccess.getConnection().getMetaData().getURL());
			CmnLog.logger.info(this.dbAccess.getConnection().getMetaData().getUserName());

			setTblMap(getTblMapForReSearch(this.xmlDbXlsInfoAll,this.tblInfoList));
			
			resetTblInfoList();
		} catch (Throwable e) {
			throw e;
		}

		CmnLog.logger.debug("getDbTablsesAllInfo end");
	}

	public static LinkedHashMap<String, TblInfo> getTblMapForReSearch(XmlDbXlsInfoAll xmlDbXlsInfoAll, List<TblBase> tblInfoList) throws Throwable {
		
		LinkedHashMap<String, TblInfo> tblMap = new LinkedHashMap<String, TblInfo>();
		
		int len =  tblInfoList.size();
		String tblNms = "";
		if (len > 0) {
			StringBuffer sb = new StringBuffer("(");

			for (int i=0 ; i< len; i++) {
				TblBase tblBase = tblInfoList.get(i);
				if (CmnStrUtils.isNotEmpty(tblBase.getTblNmEn())) {
					sb.append("'");
					sb.append(tblBase.getTblNmEn());
					sb.append("'");
					if (i+1!=len) {
						sb.append(",");
					}
				}
			}
			sb.append(")");
			tblNms = sb.toString();
		}

		List<HashMap<String, String>> allDataList = AutoDbToXls.getTableColInfo(xmlDbXlsInfoAll,tblNms);

		HashSet<String> setCol = new HashSet<String>();
		setCol.add("COL_NM_EN");
//			allDataList = CsjStrUtils.toUpperKeyList(allDataList,setCol);
		Map<String,List<HashMap<String, String>>> dataInfo = new HashMap<String, List<HashMap<String,String>>>();

		for (HashMap<String, String> map : allDataList) {

			String tblNm = map.get(AutoDbToXls.TBL_NM_EN);

			if (dataInfo.containsKey(tblNm)) {
				map.put("No.", String.valueOf(dataInfo.get(tblNm).size()+1));//
				dataInfo.get(tblNm).add(map);
			} else {
				List<HashMap<String, String>> newVal = new ArrayList<HashMap<String,String>>();
				newVal.add(map);
				map.put("No.", "1");//"No."
				dataInfo.put(tblNm, newVal);
			}
		}
		for (TblBase tblBase : tblInfoList) {
			// String delSql = "delete from "+String.format("%-30s", tblBase.getTblNmEn()) + ";";
			CmnLog.logger.info(tblBase.getTblNmEn());

			List<HashMap<String, String>> dataList = dataInfo.get(tblBase.getTblNmEn());
			if (dataList == null) {
				continue;
			}
			TblInfo tblInfo = new TblInfo();
			tblInfo.setTblNmEn(tblBase.getTblNmEn());
			tblInfo.setTblNmJp(tblBase.getTblNmJp());
			for (int i = 0; i < dataList.size(); i++) {
				HashMap<String, String> map = dataList.get(i);
				TblPara tblPara = new TblPara();
				if (i + 1 == dataList.size()) {
					tblPara.setLastPara(true);
				}
				tblPara.setSelfValFromDb(map, xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect());
				if (tblPara.isPkey()) {
					tblInfo.getpKeyMap().put(tblPara.getParaNmEn(), tblPara);
					tblInfo.getKeyPosList().add(i);
				}
				tblInfo.getParaInfoMap().put(tblPara.getParaNmEn(), tblPara);
			}
			tblInfo.setColCount(dataList.size());
			tblMap.put(tblBase.getTblNmEn(), tblInfo);
		}
		return tblMap;
	}
	/**
	 * @throws Throwable
	 */
	public static LinkedHashMap<String,TblBase> getConfigTblNms(String filePath, String sheetNm) throws Throwable {

		LinkedHashMap<String,TblBase> tblBaseMap = new LinkedHashMap<String, TblBase>();
		if (CmnStrUtils.isEmpty(sheetNm)) {
			return tblBaseMap;
		}
		CsjSheetInfo csjSheetInfo = CmnPoiUtils.getSheetContents(filePath, sheetNm, true);
		Map<Integer,List<CsjCellInfo>> csjCellPosInfoColList = csjSheetInfo.getCsjCellPosInfoColList();
		Map<String,CsjCellInfo> csjCellPosInfoMap = csjSheetInfo.getCsjCellPosInfoMap();
		for (CsjCellInfo cellInfo:csjCellPosInfoColList.get(0)) {
			if (cellInfo.getRowNum() == 0) {
				continue;
			}
			TblBase base = new TblBase(cellInfo.getContent());
			CsjCellInfo jpInfo = csjCellPosInfoMap.get(cellInfo.getRowNum()+"_1");

			if (CmnStrUtils.isNotEmpty(jpInfo)) {
				base.setTblNmJp(CmnStrUtils.convertString(jpInfo.getContent()));
			}

			tblBaseMap.put(base.getTblNmEn(), base);

		}
		return tblBaseMap;
	}
	/**
	 * @throws Throwable
	 */
	public String convertDb(String key) throws Throwable {

		CmnLog.logger.debug("convertDb(String key) begin");
//		if (key.matches(RegConstStr.DB_CONVERT_TABLE)) {
//			for (TblBase tblBase : tblInfoList) {
//				TblInfo tblInfo = tblMap.get(tblBase.getTblNmEn());
//				for (Entry<String, TblPara> entry : tblInfo.getParaInfoMap().entrySet()) {
//					TblPara tblPara = entry.getValue();
//					tblPara.getParaTypeWithlen();
//				}
//			}
//		} else if (key.matches(RegConstStr.DB_CONVERT_COL)) {
//			HashMap<String, String> typeMap = this.dbConvertInfo.getColumnMap().get(key).getTypeMap();
//			for (TblBase tblBase : tblInfoList) {
//				TblInfo tblInfo = tblMap.get(tblBase.getTblNmEn());
//				for (Entry<String, TblPara> entry : tblInfo.getParaInfoMap().entrySet()) {
//					TblPara tblPara = entry.getValue();
//					tblPara.getParaTypeWithlen();
//				}
//			}
//		} else {
		try {
			HashMap<String, String> typeMap = this.dbConvertInfo.getDbMap().get(key).getTypeMap();
			
			String dbType = CmnStrUtils.fromAtoBByTrim(key.toUpperCase(), "TO_", "");
			
			
			for (TblBase tblBase : tblInfoList) {
				TblInfo tblInfo = tblMap.get(tblBase.getTblNmEn());
				for (Entry<String, TblPara> entry : tblInfo.getParaInfoMap().entrySet()) {
					TblPara tblPara = entry.getValue();
					String paraTypeWIthLen = tblPara.getParaTypeWithlen();
					if (CmnStrUtils.isEmpty(typeMap)||typeMap.containsKey(paraTypeWIthLen) == false) {
						return paraTypeWIthLen;
					}
				}
			}
			
			for (TblBase tblBase : tblInfoList) {
				TblInfo tblInfo = tblMap.get(tblBase.getTblNmEn());
				for (Entry<String, TblPara> entry : tblInfo.getParaInfoMap().entrySet()) {
					TblPara tblPara = entry.getValue();
					String paraTypeWIthLen = tblPara.getParaTypeWithlen();
					tblPara.setParaTypeWithlen(dbType,"");
					tblPara.setParaType("");
					
					tblPara.setParaLen(0);

					tblPara.setParaTypeWithlen(dbType,CmnStrUtils.getMapValForConvert(typeMap,paraTypeWIthLen));
					
				}
			}
		} catch (Throwable e) {
			throw e;
		}
		CmnLog.logger.debug("convertDb(String key) end");
		return CsjConst.EMPTY;
//		}
	}
	/**
	 * @throws Throwable
	 *
	 */
	public void readDbConvertInfo(String dbType) throws Throwable {
		CmnLog.logger.debug("readDbConvertInfo() begin");
		try {
			this.dbConvertInfo = new DbConvertInfo();
			HashMap<String, CsjSheetInfo> csjSheetMap = CmnPoiUtils.getSheetContentsToMap(CsjPath.s_file_db_convert,false);

			HashMap<String,ConvertDb> dbMap = dbConvertInfo.getDbMap();
			HashMap<String,ConvertTbl> tblMap = dbConvertInfo.getTblMap();
			HashMap<String,ConvertColumn> colMap = dbConvertInfo.getColumnMap();

			for (Entry<String, CsjSheetInfo> entry : csjSheetMap.entrySet()) {
				String key = entry.getKey();
				
				CsjSheetInfo csjSheetInfo = entry.getValue();

				Map<String,CsjCellInfo> csjCellInfoMap =csjSheetInfo.getCsjCellInfoMap();
				Map<String,CsjCellInfo> csjCellPosInfoMap = csjSheetInfo.getCsjCellPosInfoMap();
				Map<Integer,CsjRowInfo> csjRowInfoMap = csjSheetInfo.getCsjRowInfoMap();
				Map<Integer,CsjColInfo> csjColInfoMap = csjSheetInfo.getCsjColInfoMap();

				if (key.toUpperCase().startsWith(("DB_"+ dbType+"_TO_").toUpperCase())) {
					CsjCellInfo cellInfoFrom = csjCellInfoMap.get("FROM");
					CsjCellInfo cellInfoTo = csjCellInfoMap.get("TO");
					CsjColInfo csjColInfoFrom = csjColInfoMap.get(cellInfoFrom.getCellNum());

					CsjLinkedMap<String, CsjCellInfo> colCellMapFrom = csjColInfoFrom.getColMap();
					ConvertDb convertDb = new ConvertDb();
					for (Entry<String, CsjCellInfo> entryCellInfoFrom : colCellMapFrom.entrySet()) {

						CsjCellInfo cellFrom = entryCellInfoFrom.getValue();
						if (cellFrom.getRowNum()>cellInfoFrom.getRowNum()) {
							String toVal = csjCellPosInfoMap.get(cellFrom.getRowNum()+"_"+(cellFrom.getCellNum()+1)).getContent();
							convertDb.getTypeMap().put(cellFrom.getContent(), toVal);
						}
					}
					dbMap.put(key, convertDb);
				} else if (key.matches(RegConstStr.DB_CONVERT_COL)) {

				} else if (key.matches(RegConstStr.DB_CONVERT_TABLE)) {

				}
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CmnLog.logger.info(e.getMessage());
			throw e;

		}
		CmnLog.logger.debug("readDbConvertInfo() end");
	}
	/**
	 * @throws Throwable
	 *
	 */
	public DbInfo(XmlDbXlsInfoAll xmlDbXlsInfoAll) throws Throwable {
		this.xmlDbXlsInfoAll = xmlDbXlsInfoAll;
		this.dbAccess = new CsjDBAccess(xmlDbXlsInfoAll);
	}
	public void connectDb() throws Throwable {
		CmnLog.logger.debug("connectDb() begin");
		try {
			this.dbAccess.openConnection();
			java.sql.DatabaseMetaData dbmd = this.dbAccess.getConnection().getMetaData();
			this.dbAccess.setDataBaseVer(dbmd.getDatabaseProductVersion());
			getDbTablsesAllInfo();
		} catch (Throwable e) {
			throw e;
		}

		CmnLog.logger.debug("connectDb() end");
	}
	/**
	 * @return the dbAccess
	 */
	public CsjDBAccess getDbAccess() {
		return dbAccess;
	}

	/**
	 * @param dbAccess the dbAccess to set
	 */
	public void setDbAccess(CsjDBAccess dbAccess) {
		this.dbAccess = dbAccess;
	}

	/**
	 * @return the tblMap
	 */
	public LinkedHashMap<String, TblInfo> getTblMap() {
		return tblMap;
	}

	/**
	 * @param tblMap the tblMap to set
	 */
	public void setTblMap(LinkedHashMap<String, TblInfo> tblMap) {
		this.tblMap = tblMap;
	}

	public static Integer getDbTypeByStr(String str) {
		int ret = DbInfo.TABLE_COL_TYPE_STR;

		str = CmnStrUtils.toLowOrUpStr(str);
		if (str.contains("DATE") || str.contains("TIME")) {
			ret = DbInfo.TABLE_COL_TYPE_DATE;
		} else if (str.contains("NUM") || str.contains("INT") || str.contains("DECI") || str.contains("FLOAT")) {
			ret = DbInfo.TABLE_COL_TYPE_NUM;
		}
		return ret;
	}
	/**
	 * @return the checkDate
	 */
	public String getCheckDate() {
		return checkDate;
	}
	/**
	 * @param checkDate the checkDate to set
	 */
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	/**
	 * @return the tblInfoList
	 */
	public List<TblBase> getTblInfoList() {
		return tblInfoList;
	}
	/**
	 * @param tblInfoList the tblInfoList to set
	 */
	public void setTblInfoList(List<TblBase> tblInfoList) {
		this.tblInfoList = tblInfoList;
	}
	/**
	 * @return the importSqlMapWithNo
	 */
	public CsjLinkedMap<String, CsjTblSelectSql> getImportSqlMapWithNo() {
		return importSqlMapWithNo;
	}
	/**
	 * @param importSqlMapWithNo the importSqlMapWithNo to set
	 */
	public void setImportSqlMapWithNo(
			CsjLinkedMap<String, CsjTblSelectSql> importSqlMapWithNo) {
		this.importSqlMapWithNo = importSqlMapWithNo;
	}
	/**
	 * @return the selectSqlMapWithNo
	 */
	public CsjLinkedMap<String, CsjTblSelectSql> getSelectSqlMapWithNo() {
		return selectSqlMapWithNo;
	}
	/**
	 * @param selectSqlMapWithNo the selectSqlMapWithNo to set
	 */
	public void setSelectSqlMapWithNo(
			CsjLinkedMap<String, CsjTblSelectSql> selectSqlMapWithNo) {
		this.selectSqlMapWithNo = selectSqlMapWithNo;
	}
	/**
	 * @return the selectSqlMapWithTblnm
	 */
	public CsjLinkedMap<String, CsjTblSelectSql> getSelectSqlMapWithTblnm() {
		return selectSqlMapWithTblnm;
	}
	/**
	 * @param selectSqlMapWithTblnm the selectSqlMapWithTblnm to set
	 */
	public void setSelectSqlMapWithTblnm(
			CsjLinkedMap<String, CsjTblSelectSql> selectSqlMapWithTblnm) {
		this.selectSqlMapWithTblnm = selectSqlMapWithTblnm;
	}
	/**
	 * @return the dbMemoryChangeType
	 */
	public int getDbMemoryChangeType() {
		return dbMemoryChangeType;
	}
	/**
	 * @param dbMemoryChangeType the dbMemoryChangeType to set
	 */
	public void setDbMemoryChangeType(int dbMemoryChangeType) {
		this.dbMemoryChangeType = dbMemoryChangeType;
	}
	/**
	 * @return the importChkTblMap
	 */
	public CsjLinkedMap<String, TblInfo> getImportChkTblMap() {
		return importChkTblMap;
	}
	/**
	 * @param importChkTblMap the importChkTblMap to set
	 */
	public void setImportChkTblMap(CsjLinkedMap<String, TblInfo> importChkTblMap) {
		this.importChkTblMap = importChkTblMap;
	}

	/**
	 * @return the sycnExcelPath
	 */
	public String getSycnExcelPath() {
		return sycnExcelPath;
	}
	/**
	 * @param sycnExcelPath the sycnExcelPath to set
	 */
	public void setSycnExcelPath(String sycnExcelPath) {
		this.sycnExcelPath = sycnExcelPath;
	}
	/**
	 * @return the sycnExcelSheetName
	 */
	public String getSycnExcelSheetName() {
		return sycnExcelSheetName;
	}
	/**
	 * @param sycnExcelSheetName the sycnExcelSheetName to set
	 */
	public void setSycnExcelSheetName(String sycnExcelSheetName) {
		this.sycnExcelSheetName = sycnExcelSheetName;
	}

	/**
	 * @return the schedualDbInPath
	 */
	public String getSchedualDbInPath() {
		return schedualDbInPath;
	}
	/**
	 * @param schedualDbInPath the schedualDbInPath to set
	 */
	public void setSchedualDbInPath(String schedualDbInPath) {
		this.schedualDbInPath = schedualDbInPath;
	}
	/**
	 * @return the schedualDbOutPath
	 */
	public String getSchedualDbOutPath() {
		return schedualDbOutPath;
	}
	/**
	 * @param schedualDbOutPath the schedualDbOutPath to set
	 */
	public void setSchedualDbOutPath(String schedualDbOutPath) {
		this.schedualDbOutPath = schedualDbOutPath;
	}
	/**
	 * @return the compareStructResultPath
	 */
	public String getCompareStructResultPath() {
		return compareStructResultPath;
	}
	/**
	 * @param compareStructResultPath the compareStructResultPath to set
	 */
	public void setCompareStructResultPath(String compareStructResultPath) {
		this.compareStructResultPath = compareStructResultPath;
	}

	/**
	 * @return the createSqlPath
	 */
	public String getCreateSqlPath() {
		return createSqlPath;
	}

	/**
	 * @param createSqlPath the createSqlPath to set
	 */
	public void setCreateSqlPath(String createSqlPath) {
		this.createSqlPath = createSqlPath;
	}

	public CsjLinkedMap<String, CsjTblSelectSql> getImportSqlMapWithTblnm() {
		return importSqlMapWithTblnm;
	}

	public void setImportSqlMapWithTblnm(
			CsjLinkedMap<String, CsjTblSelectSql> importSqlMapWithTblnm) {
		this.importSqlMapWithTblnm = importSqlMapWithTblnm;
	}

	public boolean isHaveLogTbl() {
		return isHaveLogTbl;
	}

	public void setHaveLogTbl(boolean isHaveLogTbl) {
		this.isHaveLogTbl = isHaveLogTbl;
	}

	public String getExcelSheetName() {
		return excelSheetName;
	}

	public void setExcelSheetName(String excelSheetName) {
		this.excelSheetName = excelSheetName;
	}
}

// sqlserver 2008
//这几天，调项目的时候发现到主机 �?TCP/IP 连接失败 java.net.ConnectException: Connection refused: connect
//
//用了如下的方法都没有解决
//
//sqlserver2005默认情况下是禁用了tcp/ip连接。启动tcp/ip连接的方�?
//打开 \Microsoft SQL Server 2005\配置工具\目录下的SQL Server Configuration Manager，选择mssqlserver协议,
//然后右边窗口有个tcp/ip协议，然后启动它，把sqlserver服务停了，然后在启动。问题就解决�?
//
//
//如果还不行就检�?433端口是否被占�?
//
//打开命令提示符窗口：用telnet localhost 1433 命令检查，如果窗口显示为黑屏，即为正常�?
//如果报错，则还应做相应修改。经常的报错是：
//正在连接到localhost...不能打开到主机的连接，在端口 1433: 连接失败
//报错其实很明显了，在端口�?433处连接失败的，修改端口号即可~~�?
//
//
//这两种修改以后都要重新启动sqlserver服务，否则不好使
//
//后来通过在命令行下输入netstat -ano发现1433端口不存�?
//
//进入SQL Server Configuration Manager，查看配�?
//SQL Server 配置管理�?本地)
//->SQL Server 2005 网络配置
//->HLHEBSQL 的协�?
//    -> TCP/IP
//
//右键->属�?
//
//
//
//发现服务器地址IP1,未启用，端口使用的是动态端口�?
//按如下更改后，重新启用SQL Server服务�?
//
//
//注意：一定要IPALL的TCP动态端口删除，TCP端口改为1433,只改IP1不好用，在这个问题很多网上的解决办法都没有明确，走了很多的弯路！
//
//
//3.     重新查看端口，可以看�?433已经打开
//C:\Documents and Settings\Administrator>netstat -ano
//
//Active Connections
//
//Proto Local Address          Foreign Address        State           PID
//TCP    0.0.0.0:21             0.0.0.0:0              LISTENING       2900
//TCP    0.0.0.0:80             0.0.0.0:0              LISTENING       3672
//TCP    0.0.0.0:135            0.0.0.0:0              LISTENING       936
//TCP    0.0.0.0:445            0.0.0.0:0              LISTENING       4
//TCP    0.0.0.0:1025           0.0.0.0:0              LISTENING       656
//TCP    0.0.0.0:1030           0.0.0.0:0              LISTENING       2900
//TCP    0.0.0.0:1433           0.0.0.0:0              LISTENING       1532
//TCP    0.0.0.0:3389           0.0.0.0:0              LISTENING       620
//TCP    1x.xx.xx.18:139        0.0.0.0:0              LISTENING       4
//TCP    1x.xx.xx.18:3389       1x.xx.xx.44:1116       ESTABLISHED     620
//TCP    127.0.0.1:1038         0.0.0.0:0              LISTENING       1532
//UDP    0.0.0.0:445            *:*                                    4
//UDP    0.0.0.0:500            *:*                                    656
//UDP    0.0.0.0:1434           *:*                                    2380
//UDP    0.0.0.0:2967           *:*                                    1772
//UDP    0.0.0.0:3456           *:*                                    2900
//UDP    0.0.0.0:4500           *:*                                    656
//UDP    1x.xx.xx.18:123        *:*                                    1096
//UDP    1x.xx.xx.18:137        *:*                                    4
//UDP    1x.xx.xx.18:138        *:*                                    4
//UDP    127.0.0.1:123          *:*                                    1096
//UDP    127.0.0.1:1027         *:*                                    1096
//UDP    127.0.0.1:3456         *:*                                    2900


//
//1、Oracle8/8i/9i数据库（thin模式�?
//Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
//String url="jdbc:oracle:thin:@localhost:1521:orcl";
////orcl为数据库的SID
//String user="test";
//String password="test";
//Connection conn= DriverManager.getConnection(url,user,password);
//
//2、DB2数据�?
//Class.forName("com.ibm.db2.jdbc.app.DB2Driver ").newInstance();
//String url="jdbc:db2://localhost:5000/sample";
////sample为你的数据库�?
//String user="admin";
//String password="";
//Connection conn= DriverManager.getConnection(url,user,password);
//
//3、Sql Server7.0/2000数据�?
//Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver").newInstance();
//String url="jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=mydb";
////mydb为数据库
//String user="sa";
//String password="";
//Connection conn= DriverManager.getConnection(url,user,password);
//
//4、Sybase数据�?
//Class.forName("com.sybase.jdbc.SybDriver").newInstance();
//String url =" jdbc:sybase:Tds:localhost:5007/myDB";
////myDB为你的数据库�?
//Properties sysProps = System.getProperties();
//SysProps.put("user","userid");
//SysProps.put("password","user_password");
//Connection conn= DriverManager.getConnection(url, SysProps);
//
//5、Informix数据�?
//Class.forName("com.informix.jdbc.IfxDriver").newInstance();
//String url =
//"jdbc:informix-sqli://123.45.67.89:1533/myDB:INFORMIXSERVER=myserver;
//user=testuser;password=testpassword";
////myDB为数据库�?
//Connection conn= DriverManager.getConnection(url);
//
//6、MySQL数据�?
//Class.forName("org.gjt.mm.mysql.Driver").newInstance();
//String url ="jdbc:mysql://localhost/myDB?user=soft&password=soft1234&useUnicode=true&characterEncoding=8859_1"
////myDB为数据库�?
//Connection conn= DriverManager.getConnection(url);
//
//7、PostgreSQL数据�?
//Class.forName("org.postgresql.Driver").newInstance();
//String url ="jdbc:postgresql://localhost/myDB"
////myDB为数据库�?
//String user="myuser";
//String password="mypassword";
//Connection conn= DriverManager.getConnection(url,user,password);
//
//8、JDBC-ODBC�?
//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
//Connection con=DriverManager.getConnection("jdbc:odbc:jsp");
//jsp为建立的odbc数据源名，事先要先将SQL server的表设置为数据源。在“管理工具�?“数据源odbc”里用系统DNS添加�?
//
//
//
//8.Oracle8/8i/9i数据库（thin模式�?
////import java.sql.*;
//Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
//String url="jdbc:oracle:thin:@localhost:1521:orcl"; //orcl为数据库的SID
//String user="test";
//String password="test";
//Connection conn= DriverManager.getConnection(url,user,password);
//Statement stmtNew=conn.createStatement();
//
//9.DB2数据�?
////import java.sql.*;
//Class.forName("com.ibm.db2.jdbc.app.DB2Driver ").newInstance();
//String url="jdbc:db2://localhost:5000/sample"; //sample为你的数据库�?
//String user="admin";
//String password="";
//Connection conn= DriverManager.getConnection(url,user,password);
//Statement stmtNew=conn.createStatement();
//
//10.Sql Server7.0/2000数据�?
////import java.sql.*;
//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
////String url="jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=db2"; //7.0�?000
//String url="jdbc:sqlserver://localhost:1433;DatabaseName=db2"; //2005
////db2为数据库�?
//String user="sa";
//String password="";
//Connection conn= DriverManager.getConnection(url,user,password);
//Statement stmtNew=conn.createStatement();
//
//11.Sybase数据�?
////import java.sql.*;
//Class.forName("com.sybase.jdbc.SybDriver").newInstance();
//String url =" jdbc:sybase:Tds:localhost:5007/myDB";//myDB为你的数据库�?
//Properties sysProps = System.getProperties();
//SysProps.put("user","userid");
//SysProps.put("password","user_password");
//Connection conn= DriverManager.getConnection(url, SysProps);
//Statement stmtNew=conn.createStatement();
//
//12.Informix数据�?
////import java.sql.*;
//Class.forName("com.informix.jdbc.IfxDriver").newInstance();
//String url = "jdbc:informix-sqli://123.45.67.89:1533/myDB:INFORMIXSERVER=myserver;
//user=testuser;password=testpassword"; //myDB为数据库�?
//Connection conn= DriverManager.getConnection(url);
//Statement stmtNew=conn.createStatement();
//
//13.MySQL数据�?
////import java.sql.*;
////Class.forName("org.gjt.mm.mysql.Driver").newInstance();
//Class.forName("com.mysql.jdbc.Driver");
////String url ="jdbc:mysql://localhost/myDB?user=soft&password=soft1234&useUnicode=true&characterEncoding=8859_1";
//String url ="jdbc:mysql://localhost:3306/myDB";
//
////myDB为数据库�?
//Connection conn= DriverManager.getConnection(url,"root","root");
//Statement stmtNew=conn.createStatement();
//
//14.PostgreSQL数据�?
////import java.sql.*;
//Class.forName("org.postgresql.Driver").newInstance();
//String url ="jdbc:postgresql://localhost/myDB" //myDB为数据库�?
//String user="myuser";
//String password="mypassword";
//Connection conn= DriverManager.getConnection(url,user,password);
//Statement stmtNew=conn.createStatement();
//
//15.access数据库直连用ODBC�?
////import java.sql.*;
//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver") ;
//String url="jdbc:odbc:Driver={MicroSoft Access Driver (*.mdb)};DBQ="+application.getRealPath("/Data/ReportDemo.mdb");
//Connection conn = DriverManager.getConnection(url,"sa","");
//Statement stmtNew=conn.createStatement();
//
//16.程序计时
//long time1=System.currentTimeMillis();
//long time2=System.currentTimeMillis();
//long interval=time2-time1;
//
//17.延时
//try {
//Thread.sleep(Integer.Parse(%%1));
//} catch(InterruptedException e) {
//e.printStackTrace();
//}
//
//18.连接Excel文件
////import java.sql.*;
//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
//String url = "jdbc:odbc:driver={Microsoft Excel Driver (*.xls)};DBQ=D:\\myDB.xls"; // 不设置数据源
//String user="myuser";
//String password="mypassword";
//Connection conn= DriverManager.getConnection(url,user,password);
//Statement stmtNew=conn.createStatement();


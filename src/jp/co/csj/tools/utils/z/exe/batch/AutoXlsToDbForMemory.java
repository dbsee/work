package jp.co.csj.tools.utils.z.exe.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
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
import jp.co.csj.tools.utils.common.CsjPath;
import jp.co.csj.tools.utils.common.CsjProcess;
import jp.co.csj.tools.utils.common.StaticClz;
import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.db.CsjDBAccess;
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
import jp.co.csj.tools.utils.poi.core.CsjAutoXlsData;
import jp.co.csj.tools.utils.poi.core.CsjCellInfo;
import jp.co.csj.tools.utils.poi.core.CsjColCellInfos;
import jp.co.csj.tools.utils.poi.core.CsjDbCellStyle;
import jp.co.csj.tools.utils.reg.RegConstNum;
import jp.co.csj.tools.utils.xml.dbtools.XmlDbXlsInfoAll;
import jp.co.csj.tools.utils.xml.dbtools.XmlInfoSql;
import jp.co.csj.tools.utils.xml.dbtools.XmlInfoXls;

/**
 * @author 963210
 *
 */
public class AutoXlsToDbForMemory {
	public static final String S_COMMIT = "commit";
	public static String s_tableNm = "";
	public static boolean s_is_tableNm_in = true;
	public static List<String> errorList = new ArrayList<String>();
	private static CsjDbCellStyle cellStyle = null;
	public static boolean s_isPicCheckParaError = false;
	public static String s_check_status = XmlInfoSql.CHECK_TYPE_CHECK;

	public static String s_tmp_now = "";
	public static int s_tmp_now_index = 0;
	/**
	 *
	 */
	public AutoXlsToDbForMemory() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// DbInfo dbInfo = new
			// DbInfo(CsjPath.s_file_auto_pj_ibats_ini_db_oracle);
			// run(dbInfo);
			// dbInfo.getDbAccess().closeConnection();
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
		}
	}

	public static void createLogTbl(XmlDbXlsInfoAll xmlDbXlsInfoAll) throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.createLogTbl() begin");
		try {
			DbInfo dbInfo = xmlDbXlsInfoAll.getDbInfo();
			XmlInfoSql xmlInfoSql = xmlDbXlsInfoAll.getXmlInfoSql();
			String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();

			if ("1".equals(xmlInfoSql.getIsLogAbled()) == false) {
				return;
			}else {
				LinkedHashMap<String, TblBase> tblBaseMap = new LinkedHashMap<String, TblBase>();
				TblBase tblBase = new TblBase(DbInfo.S_LOG_TABLE);
				tblBaseMap.put(tblBase.getTblNmEn(), tblBase);
				List<TblBase> lst = AutoDbToXls.getAllTablesNm(xmlDbXlsInfoAll, null, tblBaseMap,false);
				if (lst.size() > 0) {
					return;
				}
			}

			if (dbInfo.getTblMap().containsKey(DbInfo.S_LOG_TABLE) == false) {
				CsjDBAccess dbAccess = dbInfo.getDbAccess();
				List<Object> sqlParaList = new ArrayList<Object>();

				if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {

					dbAccess.executeSQL(
							"create table "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+""+DbInfo.S_LOG_TABLE+"(  ID VARCHAR2(25) not null,  MAC VARCHAR2(12), CMPUTER_INFO VARCHAR2(200), EXE_TABLE_NM VARCHAR2(100),  EXE_SQL      VARCHAR2(4000),  EXE_TIME     DATE default sysdate)",
							sqlParaList);
					dbAccess.executeSQL(
							"alter table "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+""+DbInfo.S_LOG_TABLE+" add constraint PK_"+DbInfo.S_LOG_TABLE+" primary key (ID,MAC)",
							sqlParaList);
				} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {

					dbAccess.executeSQL(
							"CREATE TABLE "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+"\""+DbInfo.S_LOG_TABLE+"\"(  \"ID\" character(25) NOT NULL,  \"MAC\" character(12),  \"CMPUTER_INFO\" character(200),  \"EXE_TABLE_NM\" character(100),  \"EXE_SQL\" character(4000),  \"EXE_TIME\" timestamp with time zone DEFAULT now(),  CONSTRAINT \"PK_"+DbInfo.S_LOG_TABLE+"\" PRIMARY KEY (\"ID\",\"MAC\" ))",
							sqlParaList);
				} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {

					dbAccess.executeSQL(
							"CREATE TABLE "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+"["+DbInfo.S_LOG_TABLE+"]( [ID] [varchar](25) NOT NULL, [MAC] [varchar](12)  , [CMPUTER_INFO] [varchar](200) , [EXE_TABLE_NM] [varchar](100)  , [EXE_SQL] [varchar](4000) , [EXE_TIME] [datetime]    DEFAULT getdate(), CONSTRAINT [PK_"+DbInfo.S_LOG_TABLE+"] PRIMARY KEY CLUSTERED ( [ID] ASC,[MAC] ASC)WITH (PAD_INDEX  = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]) ON [PRIMARY]",
							sqlParaList);
					return;
				} else if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {

					dbAccess.executeSQL(
							"CREATE TABLE "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+"`"+DbInfo.S_LOG_TABLE+"` (  `ID` VARCHAR(25) NOT NULL COMMENT '"
							+ CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000087)
							+ "',  `MAC` VARCHAR(12) COMMENT '"
							+ CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000038)
							+ "',  `CMPUTER_INFO` VARCHAR(200) COMMENT '"
							+ CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000039)
							+ "',  `EXE_TABLE_NM` VARCHAR(100) COMMENT '"
							+ CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000040)
							+ "',  `EXE_SQL` VARCHAR(4000) COMMENT '"
							+ CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000041)
							+ "',  `EXE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '"
							+ CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000042)
							+ "',  PRIMARY KEY (`ID`,`MAC`)) COMMENT='"
							+ CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000037)
							+ "';", sqlParaList);
					return;
				}  else if (DbInfo.STR_DB_TYPE_SQLITE.equals(dbType)) {

					dbAccess.executeSQL(
							"CREATE TABLE "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+"`"+DbInfo.S_LOG_TABLE+"` (  `ID` VARCHAR(25) NOT NULL "
							+ ",  `MAC` VARCHAR(12) "
							+ ",  `CMPUTER_INFO` VARCHAR(200) "
							+ ",  `EXE_TABLE_NM` VARCHAR(100) "
							+ ",  `EXE_SQL` VARCHAR(4000) "
							+ ",  `EXE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP "
							+ ",  PRIMARY KEY (`ID`,`MAC`)) "
							+ "", sqlParaList);
					return;
				} else if (DbInfo.STR_DB_TYPE_DB2.equals(dbType)) {

					dbAccess.executeSQL(
							"create table "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+"\""+DbInfo.S_LOG_TABLE+"\"(  \"ID\"           VARCHAR(25) not null,  \"MAC\" VARCHAR(12) not null,  \"CMPUTER_INFO\"   VARCHAR(200),  \"EXE_TABLE_NM\" VARCHAR(100),  \"EXE_SQL\"      VARCHAR(4000),  \"EXE_TIME\"     TIMESTAMP DEFAULT CURRENT TIMESTAMP)",
							sqlParaList);
					dbAccess.executeSQL(
							"alter table "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+""+DbInfo.S_LOG_TABLE+" add constraint PK_"+DbInfo.S_LOG_TABLE+" primary key (\"ID\",\"MAC\")",
							sqlParaList);
				} else if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {

					dbAccess.executeSQL(
							"create table "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+"\""+DbInfo.S_LOG_TABLE+"\"(  \"ID\"           VARCHAR(25) not null,  \"MAC\" VARCHAR(12) not null,  \"CMPUTER_INFO\"   VARCHAR(200) null,  \"EXE_TABLE_NM\" VARCHAR(100) null,  \"EXE_SQL\"    text null,  \"EXE_TIME\"     datetime DEFAULT getdate())",
							sqlParaList);
					dbAccess.executeSQL(
							"alter table "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+""+DbInfo.S_LOG_TABLE+" add constraint PK_"+DbInfo.S_LOG_TABLE+" primary key (ID,MAC)",
							sqlParaList);
					return;
				} else {
					dbAccess.executeSQL(
							"create table "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+""+DbInfo.S_LOG_TABLE+"(  ID VARCHAR2(25) not null,  MAC VARCHAR2(12), CMPUTER_INFO VARCHAR2(200), EXE_TABLE_NM VARCHAR2(100),  EXE_SQL      VARCHAR2(4000),  EXE_TIME     DATE default sysdate)",
							sqlParaList);
					dbAccess.executeSQL(
							"alter table "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+""+DbInfo.S_LOG_TABLE+" add constraint PK_"+DbInfo.S_LOG_TABLE+" primary key (ID,MAC)",
							sqlParaList);
				}
				dbAccess.executeSQL("COMMENT ON TABLE "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+"\""+DbInfo.S_LOG_TABLE+"\"  IS '"
						+ CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000037)
						+ "'", sqlParaList);
				dbAccess.executeSQL("COMMENT ON COLUMN "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+"\""+DbInfo.S_LOG_TABLE+"\".\"ID\" IS '"
						+ CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000087)
						+ "'", sqlParaList);
				dbAccess.executeSQL(
						"COMMENT ON COLUMN "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+"\""+DbInfo.S_LOG_TABLE+"\".\"MAC\" IS '"
								+ CsjDbToolsMsg.coreMsgMap
										.get(CsjDbToolsMsg.MSG_I_0000038) + "'",
						sqlParaList);
				dbAccess.executeSQL(
						"COMMENT ON COLUMN "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+"\""+DbInfo.S_LOG_TABLE+"\".\"CMPUTER_INFO\" IS '"
								+ CsjDbToolsMsg.coreMsgMap
										.get(CsjDbToolsMsg.MSG_I_0000039) + "'",
						sqlParaList);
				dbAccess.executeSQL(
						"COMMENT ON COLUMN "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+"\""+DbInfo.S_LOG_TABLE+"\".\"EXE_TABLE_NM\" IS '"
								+ CsjDbToolsMsg.coreMsgMap
										.get(CsjDbToolsMsg.MSG_I_0000040) + "'",
						sqlParaList);
				dbAccess.executeSQL("COMMENT ON COLUMN "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+"\""+DbInfo.S_LOG_TABLE+"\".\"EXE_SQL\" IS '"
						+ CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000041)
						+ "'", sqlParaList);
				dbAccess.executeSQL("COMMENT ON COLUMN "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+"\""+DbInfo.S_LOG_TABLE+"\".\"EXE_TIME\" IS '"
						+ CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000042)
						+ "'", sqlParaList);
				dbInfo.getTblMap().put(DbInfo.S_LOG_TABLE, null);
			}
		} catch (Throwable e) {
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger.debug("AutoXlsToDbForMemory.createLogTbl() end");
	}

	/**
	 * @param dbInfo
	 * @throws Throwable
	 *
	 */
	public static String run(XmlDbXlsInfoAll xmlDbXlsInfoAll) throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.run() begin");

		String retVal = CsjConst.EMPTY;
		DbInfo dbInfo = xmlDbXlsInfoAll.getDbInfo();
		try {

			createLogTbl(xmlDbXlsInfoAll);

			SheetTblsInfo tblsInfo = exeXlsToDb(xmlDbXlsInfoAll);

			if (!dbInfo.getDbAccess().getConnection().getAutoCommit()) {
				for (Entry<String, PreparedStatement> entry : dbInfo.getDbAccess().getPreStatemetMap().entrySet() ) {
					PreparedStatement statement = entry.getValue();
					statement.executeBatch();
		            statement.clearBatch();
				}
				dbInfo.getDbAccess().getConnection().commit();
			}
			
			retVal= getTblRecNum(xmlDbXlsInfoAll, tblsInfo);
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		} finally {
			dbInfo.getDbAccess().getPreStatemetMap().clear();
		}
		CmnLog.logger.debug("AutoXlsToDbForMemory.run() end");
		return retVal;
	}

	/**
	 * @param dbInfo
	 * @param dbXlsInfo
	 * @param tblsInfo
	 * @throws Throwable
	 */
	public static String getTblRecNum(XmlDbXlsInfoAll xmlDbXlsInfoAll,
			SheetTblsInfo tblsInfo) throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.getTblRecNum(DbInfo dbInfo,SheetTblsInfo tblsInfo) begin");
		StringBuffer sb = new StringBuffer();
		try {
			XmlInfoSql xmlInfoSql = xmlDbXlsInfoAll.getXmlInfoSql();
			if ("1".equals(xmlInfoSql.getIsGetTableCnt())) {
				for (Map.Entry<String, XlsTblInfo> entry : tblsInfo.getTblInfoMap()
						.entrySet()) {


					long cnt = getTblRecNum(xmlDbXlsInfoAll, entry.getKey(),AutoDbToXls.RUN_DATA_NONE);
					String line = "--table NM : "
							+ CmnStrUtils
									.rPadWhithByte(entry.getKey(), "", " ", 30)
							+ " table record number is -> " + cnt;
					sb.append(line);
					sb.append(CsjProcess.s_newLine);
				}
			}
		} catch (Throwable e) {
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		
		// CsjLog5j.closeLog5j();
		CmnLog.logger.debug("AutoXlsToDbForMemory.getTblRecNum(DbInfo dbInfo,SheetTblsInfo tblsInfo) end");
		return sb.toString();
	}

	/**
	 * @param dbInfo
	 * @param dbXlsInfo
	 * @param tblsInfo
	 * @throws Throwable
	 */
	public static int getTblRecNum(XmlDbXlsInfoAll xmlDbXlsInfoAll ,
			String tableNm,int runDataType) throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.getTblRecNum(DbInfo dbInfo,String tableNm) begin");
		int cnt = 0;
		String sql = CsjConst.EMPTY;
		List<Object> paraList = new ArrayList<Object>();
		try {

			HashSet<String> tableNmSet = new HashSet<String>();
			tableNmSet.add(tableNm);
			List<TblBase> tblList = AutoDbToXls.getTblBaseWithSome(xmlDbXlsInfoAll, tableNmSet);

			if (CmnStrUtils.isNotEmpty(tblList)) {
				TblBase base = tblList.get(0);
				XlsTblInfo xlsTblInfo = AutoDbToXls.getXlsTblInfo(xmlDbXlsInfoAll, base);


				sql = "SELECT COUNT(1) as  \"CNT\" FROM ( ";
				sql+=AutoDbToXls.getSelectTableDataSql(xmlDbXlsInfoAll, tableNm, xlsTblInfo, paraList,runDataType);
				sql+=") t";
			} else {
				sql = "SELECT COUNT(1) as  \"CNT\" FROM \"" + tableNm + "\" t";
			}

			cnt = commitCntSql(xmlDbXlsInfoAll.getDbInfo().getDbAccess(), sql, paraList);
		} catch (Throwable e) {
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger.debug("AutoXlsToDbForMemory.getTblRecNum(DbInfo dbInfo,String tableNm) end");
		return cnt;
	}

	public static int getTblRecNumSimple(XmlDbXlsInfoAll xmlDbXlsInfoAll ,String tableNm, String whereContent) throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.getTblRecNum(DbInfo dbInfo,String tableNm) begin");
		int cnt = 0;
		String sql = CsjConst.EMPTY;
		List<Object> paraList = new ArrayList<Object>();
		try {
			sql = "SELECT COUNT(1) as  \"CNT\" FROM \"" + tableNm + "\" t";
			if (CmnStrUtils.isNotEmpty(whereContent)) {
				sql=sql+" WHERE " + whereContent;
			}
		
			cnt = commitCntSql(xmlDbXlsInfoAll.getDbInfo().getDbAccess(), sql, paraList);
		} catch (Throwable e) {
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger.debug("AutoXlsToDbForMemory.getTblRecNum(DbInfo dbInfo,String tableNm) end");
		return cnt;
	}
	
	public static String getInsertSql(XmlDbXlsInfoAll xmlDbXlsInfoAll, String tblNm, XlsRecord rec)
			throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.getInsertSql(String tblNm, XlsRecord rec, int dbType) begin");
		StringBuffer sb = new StringBuffer();

		String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
		String schema = xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot();
		try {
			sb.append("insert into "+schema+"\""
					+ tblNm + "\" (");

		List<XlsTblPara> tblPara = rec.getRecord();
		for (int i = 0; i < tblPara.size(); i++) {

			TblPara para = tblPara.get(i);
			if (i + 1 == tblPara.size()) {
				sb.append("\"" + para.getParaNmEn() + "\"");
				sb.append(") values (");
			} else {
				sb.append("\"" + para.getParaNmEn() + "\"");
				sb.append(", ");
			}
		}
		for (int i = 0; i < tblPara.size(); i++) {
			XlsTblPara para = tblPara.get(i);
			if (i + 1 == tblPara.size()) {
				sb.append(getInsUptParaVal(para, dbType));
				sb.append(")");
			} else {
				sb.append(getInsUptParaVal(para, dbType));
				sb.append(",");
			}
		}
		} catch (Throwable e) {
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger.debug("AutoXlsToDbForMemory.getInsertSql(String tblNm, XlsRecord rec, int dbType) end");
		return sb.toString();
	}

	public static String getInsertExeSql(String dbType, String tblNm,
			XlsRecord rec,
			List<Object> sqlParaList, String schema) throws Throwable {
		StringBuffer sb = new StringBuffer();
		CmnLog.logger
		.debug("AutoXlsToDbForMemory.getInsertExeSql(int dbType, String tblNm,XlsRecord rec,List<Object> sqlParaList) begin");
		try {
			sb.append("insert into "+schema+"\"" + tblNm + "\" (");

			List<XlsTblPara> tblPara = rec.getRecord();
			for (int i = 0; i < tblPara.size(); i++) {

				TblPara para = tblPara.get(i);
				String paraType = CmnStrUtils.toLowOrUpStr(para.getParaType());

				if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)
						&& paraType.equals(CsjSqlServerColType.TYPE_TIMESTAMP)) {
					// com.microsoft.sqlserver.jdbc.SQLServerException:
					// 不能将显式值插入时间戳列。请对列列表使用 INSERT 来排除时间戳列，或将 DEFAULT 插入时间戳列。
					continue;
				}
				if (i + 1 == tblPara.size()) {
					sb.append("\"" + para.getParaNmEn() + "\"");
					sb.append(") values (");
				} else {
					sb.append("\"" + para.getParaNmEn() + "\"");
					sb.append(", ");
				}
			}
			for (int i = 0; i < tblPara.size(); i++) {
				XlsTblPara para = tblPara.get(i);

				String paraType = CmnStrUtils.toLowOrUpStr(para.getParaType());
				if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)
						&& paraType.equals(CsjSqlServerColType.TYPE_TIMESTAMP)) {
					// com.microsoft.sqlserver.jdbc.SQLServerException:
					// 不能将显式值插入时间戳列。请对列列表使用 INSERT 来排除时间戳列，或将 DEFAULT 插入时间戳列。
					continue;
				}
				if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)
						&& (paraType.equals(CsjSqlServerColType.TYPE_BINARY)
								// ||
								// paraType.equals(CsjSqlServerColType.TYPE_BIT)
								|| paraType
										.equals(CsjSqlServerColType.TYPE_DATETIMEOFFSET)
								|| paraType
										.equals(CsjSqlServerColType.TYPE_GEOGRAPHY)
								|| paraType
										.equals(CsjSqlServerColType.TYPE_GEOMETRY)
								|| paraType
										.equals(CsjSqlServerColType.TYPE_HIERARCHYID)
								// ||
								// paraType.equals(CsjSqlServerColType.TYPE_IMAGE)
								|| paraType
										.equals(CsjSqlServerColType.TYPE_SQL_VARIANT)
						// ||
						// paraType.equals(CsjSqlServerColType.TYPE_TIMESTAMP)
						|| paraType
								.equals(CsjSqlServerColType.TYPE_UNIQUEIDENTIFIER))

				) {
					sb.append("CAST(? AS " + paraType + ")");
				} else {
					if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType) &&CmnStrUtils.toLowOrUpStr(para.getParaTypeWithlen()).contains("TIMESTAMP") ) {
						String retVal = "to_timestamp('" + para.getParaVal() + "'" + ",'yyyy/mm/dd hh24:mi:ssxff"+para.getParaLen()+"')";
						sb.append(retVal);
						if (i + 1 == tblPara.size()) {
							sb.append(") ");
						} else {
							sb.append(",");
						}
						continue;
					} else {
						sb.append("?");
					}
				}

				if (i + 1 == tblPara.size()) {
					sb.append(") ");
				} else {
					sb.append(",");
				}
				sqlParaList.add(para);
			}

		} catch (Throwable e) {
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger.debug("AutoXlsToDbForMemory.getInsertExeSql(int dbType, String tblNm,XlsRecord rec,List<Object> sqlParaList) begin");
		return sb.toString();
	}

	
	public static List<String>
			getDeleteSql(XmlDbXlsInfoAll xmlDbXlsInfoAll,String tblNm,
					List<Integer> keyPosList, XlsRecord xlsRec,
					List<Object> sqlParaList) throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.getDeleteSql(DbInfo dbInfo, XlsTblInfo tblInfo,List<Integer> keyPosList, XlsRecord xlsRec,List<Object> sqlParaList) begin");
		List<String> retList = new ArrayList<String>();
		try {
			StringBuffer sbTxt = new StringBuffer();
			StringBuffer sbExe = new StringBuffer();

			String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();

			String schema = xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot();
			String sqlHead = "delete from " + schema +"\""+tblNm
					+ "\" where 1=1";
			sbTxt.append(sqlHead);
			sbExe.append(sqlHead);

			for (Integer posIndex : keyPosList) {

				XlsTblPara para = xlsRec.getRecmap().get(posIndex);

				try {
					List<String> whereList = getWhereCond(para, false,
							sqlParaList, dbType);
					sbTxt.append(whereList.get(0));
					sbExe.append(whereList.get(1));
				} catch (Throwable e) {
					e.printStackTrace();
					CmnLog.logger.error(e.getMessage());
				}
			}
			retList.add(sbTxt.toString());
			retList.add(sbExe.toString());
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger.debug("AutoXlsToDbForMemory.getDeleteSql(DbInfo dbInfo, XlsTblInfo tblInfo,List<Integer> keyPosList, XlsRecord xlsRec,List<Object> sqlParaList) end");
		return retList;
	}

	/**
	 * @param para
	 * @param sqlParaList
	 * @param dbType
	 * @param sb
	 * @throws Throwable
	 */

	public static List<String> getWhereCond(XlsTblPara para, boolean isLike,
			List<Object> sqlParaList, String dbType) throws Throwable {
		List<String> retList = new ArrayList<String>();
		CmnLog.logger.debug("AutoXlsToDbForMemory.getWhereCond((XlsTblPara para, boolean isLike,List<Object> sqlParaList, int dbType) begin");
		try {
			XlsTblPara tmpPara = para;
			if (CmnStrUtils.isNotEqual(para.getParaVal(), para.getParaValOld())) {
				tmpPara = new XlsTblPara(para);
				tmpPara.setParaVal(tmpPara.getParaValOld());
			}
			
			StringBuffer whereTxt = new StringBuffer();
			StringBuffer whereExe = new StringBuffer();
			whereTxt.append(" AND \"" + tmpPara.getParaNmEn() + "\"");
			whereExe.append(" AND \"" + tmpPara.getParaNmEn() + "\"");
			
			if (CmnStrUtils.isEmpty(tmpPara.getParaVal())) {
				String nullStr = " is NULL ";//'" + para.getParaVal() + "'";
				whereTxt.append(nullStr);
				whereExe.append(nullStr);
			} else {

				if (DbInfo.TABLE_COL_TYPE_DATE == tmpPara.getType()) {

					if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
						if (tmpPara.getParaVal().length() > 10) {
							whereTxt.append(" STR_TO_DATE('"
									+ tmpPara.getParaVal()
									+ "','%Y/%m/%d %k:%i:%s')");
						} else {
							whereTxt.append(" STR_TO_DATE('"
									+ tmpPara.getParaVal() + "','%Y/%m/%d')");
						}
					} else if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
						if (tmpPara.getParaVal().length() > 10) {
							whereTxt.append(" = to_date('"
									+ tmpPara.getParaVal()
									+ "','YYYY/MM/DD HH24:MI:SS')");
						} else {
							whereTxt.append(" = to_date('"
									+ tmpPara.getParaVal() + "','YYYY/MM/DD')");
						}
					} else {
						whereTxt.append(" = '" + tmpPara.getParaVal() + "'");
					}

					whereExe.append(" = " + "?");
					sqlParaList.add(tmpPara);
				

				} else if (DbInfo.TABLE_COL_TYPE_NUM == tmpPara.getType()) {

					whereTxt.append(" = " + tmpPara.getParaVal());
					whereExe.append(" = " + "?");
					sqlParaList.add(tmpPara);
				

				} else {
					whereTxt.append(" = '" + tmpPara.getParaVal() + "'");
					whereExe.append(" = " + "?");
					sqlParaList.add(tmpPara);
				}

			}
			retList.add(whereTxt.toString());
			retList.add(whereExe.toString());
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger.debug("AutoXlsToDbForMemory.getWhereCond((XlsTblPara para, boolean isLike,List<Object> sqlParaList, int dbType) end");
		return retList;
	}

	private static String getInsUptParaVal(XlsTblPara para, String dbType)
			throws Throwable {
		String retVal = CsjConst.EMPTY;
		CmnLog.logger.debug("AutoXlsToDbForMemory.getInsUptParaVal(XlsTblPara para, int dbType) begin");
		try {

			if (CmnStrUtils.isEmpty(para.getParaVal())) {
				retVal = "NULL";
				return retVal;
			}

			String paraTypeWithlenUpper = CmnStrUtils.toLowOrUpStr(para.getParaTypeWithlen());
			
			if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType) &&CmnStrUtils.toLowOrUpStr(para.getParaTypeWithlen()).contains("TIMESTAMP") ) {
				retVal = "to_timestamp('" + para.getParaVal() + "'" + ",'yyyy/mm/dd hh24:mi:ssxff"+para.getParaLen()+"')";
				return retVal;
			}
			
			if (paraTypeWithlenUpper.contains(CmnStrUtils.toLowOrUpStr("XML"))
					|| paraTypeWithlenUpper.contains(CmnStrUtils.toLowOrUpStr("CHAR"))
					|| paraTypeWithlenUpper.contains(CmnStrUtils.toLowOrUpStr("TEXT"))
					|| paraTypeWithlenUpper.contains(CmnStrUtils.toLowOrUpStr("TIME"))) {
				retVal = "'" + para.getParaVal() + "'";
			} else if (CmnStrUtils.toLowOrUpStr(para.getParaTypeWithlen()).contains(CmnStrUtils.toLowOrUpStr("DATE"))) {

				String val = CmnStrUtils.fromAtoBByTrim(para.getParaVal(), "",
						".");
				if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
					if (para.getParaVal().length() > 10) {
						retVal = " STR_TO_DATE('" + val
								+ "','%Y/%m/%d %k:%i:%s')";
					} else {
						retVal = " STR_TO_DATE('" + val + "','%Y/%m/%d')";
					}
				} else if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)||DbInfo.STR_DB_TYPE_DB2.equals(dbType)) {
					if (para.getParaVal().length() > 10) {
						retVal = " to_date('" + val
								+ "','YYYY/MM/DD HH24:MI:SS')";
					} else {
						retVal = " to_date('" + val + "','YYYY/MM/DD')";
					}
				} else {
					retVal = "'" + val + "'";
				}

				// retVal = "to_date('" + para.getParaVal() + "')";
			} else {// if (para.getParaTypeWithlen().contains("NUMBER")) {
				if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType) &&CmnStrUtils.toLowOrUpStr(para.getParaTypeWithlen()).contains("LOB") ) {
					retVal = "'" + para.getParaVal() + "'";
				} else {
					retVal = para.getParaVal();
				}

			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger.debug("AutoXlsToDbForMemory.getInsUptParaVal(XlsTblPara para, int dbType) end");
		return retVal;
	}

	public static List<String> getSheetTables(XmlDbXlsInfoAll xmlDbXlsInfoAll)
			throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.getSheetTables(DbInfo dbInfo) begin");
		List<String> retList = new ArrayList<String>();
		String path = xmlDbXlsInfoAll.getDbInfo().getSycnExcelPath();
		try {

			File file = new File(path);
			Workbook wb =  null;
			FileInputStream fs = new FileInputStream(file);
			if (CmnStrUtils.isEndByIgnor(path, CsjConst.EXCEL_DOT_XLS_1997)) {
				wb = new HSSFWorkbook(fs);
			} else {
				wb = new XSSFWorkbook(fs);
			}
			Sheet sheet = wb.getSheet(xmlDbXlsInfoAll.getDbInfo().getExcelSheetName());
			retList = getTblsInfoBySheet(xmlDbXlsInfoAll, sheet);
			wb.close();
			fs.close();
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger.debug("AutoXlsToDbForMemory.getSheetTables(DbInfo dbInfo) end");
		return retList;
	}

	/**
	 * @param xmlDbXlsInfoAll
	 * @param retList
	 * @param sheet
	 * @throws Throwable
	 */
	public static List<String> getTblsInfoBySheet(XmlDbXlsInfoAll xmlDbXlsInfoAll, Sheet sheet) throws Throwable {
		List<String> retList = new ArrayList<String>();
		int index = 1;
		String tblNmEn = CsjConst.EMPTY;

		String tblNmJp = CsjConst.EMPTY;
		String sqlWhere = CsjConst.EMPTY;
		String sqlOrder = CsjConst.EMPTY;

		xmlDbXlsInfoAll.getDbInfo().clearSheetInfo();
		CsjLinkedMap<String, TblInfo> importChkTblMap = xmlDbXlsInfoAll.getDbInfo().getImportChkTblMap();
		for (Row row : sheet) {
			String sign = CmnPoiUtils
					.getCellContent(sheet, row.getRowNum(), 0, false);
			if (sign.equals(xmlDbXlsInfoAll.getXmlInfoXls().getTblSign())) {
				tblNmEn = CmnPoiUtils
						.getCellContent(sheet, row.getRowNum(), 1, false);
				tblNmJp = CmnPoiUtils
						.getCellContent(sheet, row.getRowNum(), 2, false);

				importChkTblMap.put(tblNmEn, new TblInfo());
				List<String> strList = CmnPoiUtils.getCellContents(row, 3,
						true);
				for (int i = 0; i < strList.size(); i++) {

					String str = CmnStrUtils.lrTrimSpace(strList.get(i));

					if (CmnStrUtils.toLowOrUpStr(str).startsWith("WHERE")) {
						sqlWhere = str.substring(5);
					} else {

						if (CmnStrUtils.toLowOrUpStr(str).startsWith("ORDER BY")) {
							sqlOrder = str.substring(8);
						}
					}
				}
			} else if (CsjDbToolsMsg.coreMsgMap.get(
					CsjDbToolsMsg.MSG_I_0000003).equals(sign) && CmnStrUtils.isNotEmpty(tblNmEn)) {

				LinkedHashSet<String> colSet = new LinkedHashSet<String>();
				for (Cell cell : row) {
					if (cell.getColumnIndex()>0) {

						String paraEnNm = CmnPoiUtils.getCellContent(cell, false);
						if (CmnStrUtils.isNotEmpty(paraEnNm)) {
							colSet.add(paraEnNm);
						}

						TblPara para = new TblPara();
						para.setParaNmEn(paraEnNm);
						setParaCheck(para,cell,sheet);

						TblInfo tblInfo = importChkTblMap.get(tblNmEn);
						tblInfo.getParaInfoMap().put(paraEnNm, para);
					}
				}
				if (CmnStrUtils.isNotEmpty(tblNmEn)) {
					retList.add(tblNmEn
							+ CmnLog5j.addlrBracket_M_L_JP(tblNmJp, true));
					CsjTblSelectSql sq = new CsjTblSelectSql(tblNmEn, sqlWhere, sqlOrder);
					sq.setColSet(colSet);
					xmlDbXlsInfoAll.getDbInfo().getImportSqlMapWithNo().put(String.valueOf(index++),sq);
					xmlDbXlsInfoAll.getDbInfo().getImportSqlMapWithTblnm().put(tblNmEn,sq);
					tblNmEn= CsjConst.EMPTY;;
					tblNmJp = CsjConst.EMPTY;
					sqlWhere = CsjConst.EMPTY;
					sqlOrder = CsjConst.EMPTY;
				}
			}

		}
		return retList;
	}

	/**
	 * @param para
	 * @param cell
	 * @param sheet
	 * @throws Throwable
	 */
	private static void setParaCheck(TblPara para, Cell cell, Sheet sheet) throws Throwable {
		Map<String, String> splitMap = CmnStrUtils
		.getSplitMap(
				CmnPoiUtils
						.getCellComment(cell),
						CsjConst.SIGN_CIRCLE_HOLLOW_2,
				CsjConst.MASK_TO_RIGHT,CsjConst.trimChSet,false);

		if (CmnStrUtils.isNotEmpty(CmnPoiUtils
				.getCellComment(cell))) {
			ParaCheck paraCheck = para
					.getParaCheck();
			paraCheck.setReg(CmnStrUtils.convertString(splitMap
					.get(CsjConst.JP_REG)));
			paraCheck.setEqualStr(CmnStrUtils.convertString(splitMap
					.get(CsjConst.JP_EQUAL)));
			paraCheck
					.setEqualSet(CmnStrUtils.getSet(
							splitMap.get(CsjConst.JP_EQUAL),
							CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000071),
							false));
			paraCheck.setNoEqualStr(CmnStrUtils.convertString(splitMap
					.get(CsjConst.JP_NO_EQUAL)));
			paraCheck
					.setNoEqualSet(CmnStrUtils.getSet(
							splitMap.get(CsjConst.JP_NO_EQUAL),
							CsjDbToolsMsg.coreMsgMap
									.get(CsjDbToolsMsg.MSG_I_0000071),
							false));
			paraCheck.setMaxLen(CmnStrUtils.convertString(splitMap
					.get(CsjConst.JP_MAX_LEN)));
			paraCheck.setMaxVal(CmnStrUtils.convertString(splitMap
					.get(CsjConst.JP_MAX_VAL)));
			paraCheck.setMinLen(CmnStrUtils.convertString(splitMap
					.get(CsjConst.JP_MIN_LEN)));
			paraCheck.setMinVal(CmnStrUtils.convertString(splitMap
					.get(CsjConst.JP_MIN_VAL)));
			paraCheck.setParaSummary(CmnStrUtils.convertString(splitMap
					.get(CsjConst.JP_SUMMARY)));
		}

		Cell hssfCell = CmnPoiUtils.getOrCreateCell(sheet, cell.getRow().getRowNum()-1, cell.getColumnIndex());
		CsjCellInfo csjCellInfo = CmnPoiUtils.getCellInfo(hssfCell);
		if (csjCellInfo.getFont().getItalic()) {
			para.setParaNmJpSync(CmnPoiUtils.getCellContent(hssfCell, false));
		}
	}

	private static void writeCommit(XmlInfoSql xmlInfoSql, String dbType) throws Throwable {

		if ("0".equals(xmlInfoSql.getIsLogTxtAbled())) {
			return;
		}
		if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
			CmnFileUtils.writeWithbBlank(CmnLog5j.s_log5j, S_COMMIT+CsjDbToolsMsg.coreMsgMap
					.get(CsjDbToolsMsg.MSG_I_0000071), 0);
		}

	}
	private static SheetTblsInfo exeXlsToDb(XmlDbXlsInfoAll xmlDbXlsInfoAll) throws Throwable {
		SheetTblsInfo tblsInfo = new SheetTblsInfo();
		
		CmnLog.logger.debug("AutoXlsToDbForMemory.exeXlsToDb(DbInfo dbInfo) begin");
		FileInputStream fileInputStream = null;
		FileOutputStream fileOut = null;
		XmlInfoXls xmlInfoXls = xmlDbXlsInfoAll.getXmlInfoXls();
		XmlInfoSql xmlInfoSql = xmlDbXlsInfoAll.getXmlInfoSql();
		String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
		String s_sheetNm = xmlDbXlsInfoAll.getDbInfo().getExcelSheetName();
		Workbook wb = null;
		Sheet sheet = null;
		File excelFile = null;
		Row tRow = null;
		try {
			List<Object> sqlParaList = new ArrayList<Object>();

			excelFile = new File(CsjPath.s_path_auto_pj_AutoDB_xls_to_db);
			
			fileInputStream = new FileInputStream(excelFile);

			if (CmnStrUtils.isEndByIgnor(excelFile.getName(), CsjConst.EXCEL_DOT_XLS_1997)) {
				wb = new HSSFWorkbook(fileInputStream);
				cellStyle = new CsjDbCellStyle(wb,CsjConst.EXCEL_DOT_XLS_1997,CsjDbCellStyle.CS_DATA_REC);
			} else {
				wb = new XSSFWorkbook(fileInputStream);
				cellStyle = new CsjDbCellStyle(wb,CsjConst.EXCEL_DOT_XLSX_2007,CsjDbCellStyle.CS_DATA_REC);
			}
			
			HashMap<String, String> delMap = new HashMap<String, String>();
			CmnLog.logger.info(excelFile.getAbsolutePath());
			HashSet<String> sheetNmErrorSet = new HashSet<String>();
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				sheet = wb.getSheetAt(i);
				String sheetNm = sheet.getSheetName();
				CmnLog.logger.info(sheetNm);
				if (CmnStrUtils.isNotEmpty(s_sheetNm)&& sheetNm.equals(s_sheetNm) == false) {
					continue;
				}
				XlsTblInfo tblInfo = null;
				String tblNmEn = CsjConst.EMPTY;
				String tblNmJp = CsjConst.EMPTY;
				int colHeigthStep = 0;
				for (Row row : sheet) {
					tRow = row;
					List<String> strList = CmnPoiUtils.getCellContents(row,true);
					if (strList.size() > 1) {
						if (strList.get(0).equals(xmlInfoXls.getTblSign())
									&& CmnPoiUtils.getCellContent(sheet,row.getRowNum(), 0, false).equals(xmlInfoXls.getTblSign())) {

							String tmpTblNmEn = strList.get(1);
							if (CmnStrUtils.isNotEmpty(s_tableNm)) {
								if (s_is_tableNm_in) {
									if (tmpTblNmEn.equals(s_tableNm)) {
									} else {
										tblNmEn = "";
										continue;
									}
								} else {
									if (tmpTblNmEn.equals(s_tableNm)) {
										tblNmEn = "";
										continue;
									} else {
									}
								}
							}

							if (CmnStrUtils.isNotEmpty(tblNmEn)
									&& tblInfo != null) {
								tblsInfo.getTblInfoMap().put(tblNmEn, tblInfo);
							}

							tblNmEn = strList.get(1);
							if (strList.size() >= 3) {
								tblNmJp = strList.get(2);
							}

							tblInfo = new XlsTblInfo();
							tblInfo.setTblNmEn(tblNmEn);

							tblInfo.setTblNmJp(tblNmJp);
							CsjCellInfo cellInfo = CmnPoiUtils
									.getCellFontByContent(row, tblNmEn, true);
							if ("1".equals(xmlInfoSql.getIsDeleteTable())
									|| cellInfo.getFont().getStrikeout()) {
								tblInfo.setAllDel(true);
								String delSql = "delete from \""
										+ tblInfo.getTblNmEn()
										+ "\"";
								if (xmlDbXlsInfoAll.getDelSqlSet().contains(delSql.toUpperCase()) == false) {
									xmlDbXlsInfoAll.getDbInfo().getDbAccess().executeSQL(delSql,
											new ArrayList<Object>());
									exeInserLog(tblInfo.getTblNmEn(), xmlDbXlsInfoAll,delSql);
									if ("1".equals(xmlInfoSql.getIsLogTxtAbled())) {
										CmnFileUtils.writeWithbBlank(CmnLog5j.s_log5j, delSql+CsjDbToolsMsg.coreMsgMap
												.get(CsjDbToolsMsg.MSG_I_0000071), 0);
									}
									writeCommit(xmlInfoSql,dbType);
									xmlDbXlsInfoAll.getDelSqlSet().add(delSql.toUpperCase());
								}
							}
							colHeigthStep = 0;
						}
					}

					if (CmnStrUtils.isEmpty(tblNmEn)) {
						delMap.clear();
					} else if (tblInfo != null) {

						LinkedHashMap<Integer, TblPara> paraPosInfoMap = tblInfo.getParaPosInfoMap();
						String cell0Str = CmnPoiUtils.getCellContent(row, 0,false);
						List<Integer> keyPosList = tblInfo.getKeyPosList();
						if (CmnStrUtils.isNumeric(cell0Str)) {
							XlsRecord xlsRec = new XlsRecord();

							for (Cell cell : row) {

								int colPos = cell.getColumnIndex();
								CsjCellInfo csjCellInfo = CmnPoiUtils
										.getCellInfo( cell);

								if (paraPosInfoMap.containsKey(colPos)) {
									XlsTblPara xlsTblPara = new XlsTblPara(
											paraPosInfoMap.get(colPos));
									xlsTblPara.setXlsRowNum(row.getRowNum());
									xlsTblPara.setXlsColNum(cell
											.getColumnIndex());
									xlsTblPara.setParaVal(csjCellInfo
											.getContent());

									xlsRec.getRecord().add(xlsTblPara);
									xlsRec.getRecmap().put(colPos, xlsTblPara);
								}
							}

							if ("1".equals(xmlInfoSql.getIsErrorContinue())) {
								try {
									checkCommit(xmlDbXlsInfoAll, sqlParaList,
												delMap, tblInfo.getTblNmEn(), keyPosList,
												xlsRec, sheet,sheetNmErrorSet);
								} catch (Throwable e) {
									errorList.add(e.getMessage());
									errorList.add("FILE:"+CmnLog5j.addlrBracketsM(excelFile.getAbsolutePath(), true));
									errorList.add("SHEET:"+CmnLog5j.addlrBracketsM(sheet.getSheetName(),true));
									errorList.add("row:"+CmnLog5j.addlrBracketsM(String.valueOf(tRow.getRowNum()+1),true));
									
									StringBuffer sb = new StringBuffer();
									for (Object obj : sqlParaList) {
										if (obj instanceof XlsTblPara) {
											XlsTblPara xPara  = (XlsTblPara)obj;
											sb.append(xPara.getParaVal());
											sb.append(CsjConst.SIGN_COMMA);
										}
									}
									String output = sb.toString();
									if (output.length()!=0) {
										output = output.substring(0, output.length()-1);
									}
									CmnLog.logger.error(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000172)+output);
									CmnLog.logger.error(e.getMessage());
									sqlParaList.clear();
									e.printStackTrace();
								}

							} else {
								checkCommit(xmlDbXlsInfoAll, sqlParaList, delMap,
										tblInfo.getTblNmEn(), keyPosList, xlsRec, sheet,sheetNmErrorSet);
							}
						} else if (cell0Str.contains("■")) {

							Map<String,CsjAutoXlsData> autoContentMap = new HashMap<String, CsjAutoXlsData>();
							
							long startNum = CmnStrUtils.getLongVal(CmnStrUtils
									.fromAtoBByTrim(cell0Str, "■", "-"));
							long endNum = CmnStrUtils.getLongVal(CmnStrUtils
									.fromAtoBByTrim(cell0Str, "-", ""));

							long numByte = CmnStrUtils.getNumByte(endNum);
							
							XlsRecord xlsRec = new XlsRecord();
							
							for (long index = startNum; index <= endNum; index++) {
								
								for (Cell cell : row) {

									int colPos = cell.getColumnIndex();
									CsjCellInfo csjCellInfo = CmnPoiUtils.getCellInfo( cell);

									if (paraPosInfoMap.containsKey(colPos)) {
										
										XlsTblPara xlsTblPara = null;
										if (index==startNum) {
											xlsTblPara = new XlsTblPara(paraPosInfoMap.get(colPos));
											xlsTblPara.setParaVal(null);
											xlsTblPara.setXlsRowNum(row.getRowNum());
											xlsTblPara.setXlsColNum(cell.getColumnIndex());
										} else {
											xlsTblPara = xlsRec.getRecmap().get(colPos);
										}
										
										String content = csjCellInfo.getContent();
										CmnLog.logger.info(content);

										if (csjCellInfo.getFont().getItalic()) {
											
											CsjColCellInfos colCellInfos = new CsjColCellInfos(csjCellInfo.getContent());
											CmnLog.logger.info(colCellInfos.toString());
											if (CsjColCellInfos.STATUS_NOTHING != colCellInfos.getStatus()) {
												CsjAutoXlsData csjAutoXlsData = autoContentMap.get(colCellInfos.getKey());
												if (csjAutoXlsData == null) {
													csjAutoXlsData = new CsjAutoXlsData(colCellInfos.isRandom(),colCellInfos.isRepeat());
													Map<Integer, String> tMap = csjAutoXlsData.getSheetInfoMap().get(colCellInfos.getColnum());
													if (tMap == null) {
														if (CsjColCellInfos.STATUS_EXCEL == colCellInfos.getStatus()) {
															tMap = CmnPoiUtils.getSheetContents(colCellInfos.getFilename(), colCellInfos.getSheetname(), colCellInfos.getColnum(), colCellInfos.getRownumstart(), colCellInfos.getRownumend());
														} else {
															tMap = CmnFileUtils.getFileContents(colCellInfos.getFilename(), colCellInfos.getSplitch(), colCellInfos.getColnum(), colCellInfos.getRownumstart(), colCellInfos.getRownumend(), colCellInfos.getEncode());
														}
														csjAutoXlsData.getSheetInfoMap().put(colCellInfos.getColnum(), tMap);
														csjAutoXlsData.resetRowIndexList(tMap.size(),colCellInfos.isRandom());
													}
													autoContentMap.put(colCellInfos.getKey(), csjAutoXlsData);
												}
												Map<Integer, String> tMap = csjAutoXlsData.getSheetInfoMap().get(colCellInfos.getColnum());
												if (tMap == null) {
													if (CsjColCellInfos.STATUS_EXCEL == colCellInfos.getStatus()) {
														tMap = CmnPoiUtils.getSheetContents(colCellInfos.getFilename(), colCellInfos.getSheetname(), colCellInfos.getColnum(), colCellInfos.getRownumstart(), colCellInfos.getRownumend());
													} else {
														tMap = CmnFileUtils.getFileContents(colCellInfos.getFilename(), colCellInfos.getSplitch(), colCellInfos.getColnum(), colCellInfos.getRownumstart(), colCellInfos.getRownumend(), colCellInfos.getEncode());
													}
													csjAutoXlsData.getSheetInfoMap().put(colCellInfos.getColnum(), tMap);
												}
												int rowIndex = csjAutoXlsData.getRowIndex();
												if (rowIndex<0) {
													content = "";
												} else {
													content = colCellInfos.getBeginstr()+CmnStrUtils.convertString(tMap.get(csjAutoXlsData.getRowIndexList().get(rowIndex)))+colCellInfos.getEndstr();
												}
												
											} else {
												if (DbInfo.TABLE_COL_TYPE_DATE == xlsTblPara.getType()) {

													if (content
															.getBytes(IConstFile.ENCODE_SHIFT_JIS).length > 10) {
														content = CmnDateUtil.getFormatDateAdd(CmnDateUtil.getDate(content,CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24),
																		(int) index,
													
																		CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24);
													} else {
														content = CmnDateUtil.getFormatDateAdd(CmnDateUtil.getDate(content,CsjConst.YYYY_MM_DD_SLASH),
																		(int) index,
																		CsjConst.YYYY_MM_DD_SLASH);
													}

												} else if (DbInfo.TABLE_COL_TYPE_NUM == xlsTblPara.getType()) {
													content = String.valueOf(CmnStrUtils.getLongVal(CmnStrUtils.fromAtoBByTrim(content,"","."))+ index);
												} else {
													content = CmnStrUtils.lPadWhithByte(content,String.valueOf(index),"0",content.getBytes("shift-jis").length+ numByte);
												}
											}
											
										}
										xlsTblPara.setParaVal(content);
										if (index==startNum) {
											xlsRec.getRecord().add(xlsTblPara);
											xlsRec.getRecmap().put(colPos,xlsTblPara);
										}
									}
								}
								checkCommit(xmlDbXlsInfoAll, sqlParaList, delMap,
										tblInfo.getTblNmEn(), keyPosList, xlsRec, sheet,sheetNmErrorSet);
								for (CsjAutoXlsData csjAutoXlsData : autoContentMap.values()) {
									csjAutoXlsData.reset(csjAutoXlsData);
								}
								
							}
						} else if (tblInfo.getTblDataList().size() == 0) {
							String sign = CmnPoiUtils.getCellContent(sheet,
									row.getRowNum(), 0, false);
							if (CsjDbToolsMsg.coreMsgMap.get(
									CsjDbToolsMsg.MSG_I_0000003).equals(sign)) {
								for (Cell cell : row) {

									CsjCellInfo csjCellInfo = CmnPoiUtils
											.getCellInfo( cell);
									csjCellInfo.setContent(CmnStrUtils
											.lrTrimSpace(csjCellInfo
													.getContent()));
									int colPos = cell.getColumnIndex();
									if (colPos != 0) {
										TblPara para = new TblPara();
										para.setParaNmEn(csjCellInfo
												.getContent());

										setParaCheck(para,cell,sheet);

										if (csjCellInfo.getFont()
												.getBoldweight() == Font.BOLDWEIGHT_BOLD) {
											para.setPkey(true);
											tblInfo.getKeyPosList().add(colPos);
										} else {
											para.setPkey(false);
										}
										paraPosInfoMap.put(colPos, para);
									}
								}
							} else if (CsjDbToolsMsg.coreMsgMap.get(
									CsjDbToolsMsg.MSG_I_0000004).equals(sign)) {
								for (Cell cell : row) {

									CsjCellInfo csjCellInfo = CmnPoiUtils
											.getCellInfo( cell);
									csjCellInfo.setContent(CmnStrUtils
											.lrTrimSpace(csjCellInfo
													.getContent()));
									int colPos = cell.getColumnIndex();
									if (colPos != 0
											&& paraPosInfoMap
													.containsKey(colPos)) {
										TblPara para = paraPosInfoMap
												.get(colPos);
										para.setParaTypeWithlen(dbType,
												csjCellInfo.getContent());
										// xxxxxx
										Map<String, String> splitMap = CmnStrUtils
												.getSplitMap(
														CmnPoiUtils.getCellComment(cell),
														CsjProcess.s_newLine,
														CsjConst.MASK_TO_RIGHT,CsjConst.trimChSet,false);

										para.setParaType(splitMap.get(CsjConst.JP_TYPE));
										para.setParaLen(CmnStrUtils.getLongVal(splitMap.get(CsjConst.JP_LENGTH)));
										para.setParaExtra(splitMap.get(CsjConst.JP_EXTRA));
									}
								}
							} else if (CsjDbToolsMsg.coreMsgMap.get(
									CsjDbToolsMsg.MSG_I_0000005).equals(sign)) {
								for (Cell cell : row) {

									CsjCellInfo csjCellInfo = CmnPoiUtils.getCellInfo(cell);
									csjCellInfo.setContent(CmnStrUtils.lrTrimSpace(csjCellInfo.getContent()));
									int colPos = cell.getColumnIndex();
									if (colPos != 0
											&& paraPosInfoMap
													.containsKey(colPos)) {
										TblPara para = paraPosInfoMap
												.get(colPos);
										para.setCanNull(CmnStrUtils.toLowOrUpStr(csjCellInfo
												.getContent())
												.equals("Y"));

										// para.getParaCheck()
										Map<String, String> splitMap = CmnStrUtils
												.getSplitMap(CmnPoiUtils
														.getCellComment(cell),
														CsjProcess.s_newLine,
														CsjConst.MASK_TO_RIGHT,CsjConst.trimChSet,false);
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
									
//									■对应列号
//									■分隔符
//									■文件位置
//									■文本编码
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
									
									XlsRecord xlsRec = new XlsRecord();
									
									while (reader.ready()) {
										String str = reader.readLine();
										fileIndexRow++;
										if (fileIndexRow<beginRow||endRow < fileIndexRow) {
											continue;
										}
										
										String sArr[] = str.split(ch,-1);
										
										for (Entry<Integer, TblPara> entry : paraPosInfoMap.entrySet()) {
											
											XlsTblPara xlsTblPara = null;
											if (fileIndexRow==beginRow) {
												xlsTblPara = new XlsTblPara(entry.getValue());
												xlsTblPara.setParaVal(null);
												xlsRec.getRecord().add(xlsTblPara);
												xlsRec.getRecmap().put(entry.getKey(), xlsTblPara);

											} else {
												xlsTblPara = xlsRec.getRecmap().get(entry.getKey());
											}
											xlsTblPara.setParaVal(sArr[posMap.get(entry.getKey())]);
										}

										if ("1".equals(xmlInfoSql.getIsErrorContinue())) {
											try {
												checkCommit(xmlDbXlsInfoAll, sqlParaList,
															delMap, tblInfo.getTblNmEn(), keyPosList,
															xlsRec, sheet,sheetNmErrorSet);
											} catch (Throwable e) {
												errorList.add(e.getMessage());
												sqlParaList.clear();
												e.printStackTrace();
												CmnLog.logger.error(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000172)+str);
												CmnLog.logger.error(e.getMessage());
											}

										} else {
											checkCommit(xmlDbXlsInfoAll, sqlParaList, delMap,
													tblInfo.getTblNmEn(), keyPosList, xlsRec, sheet,sheetNmErrorSet);
										}
										
									}
									reader.close();
									
									tblInfo = null;
									tblNmEn = CsjConst.EMPTY;
								}
							}

							colHeigthStep++;
						} else if (CmnStrUtils.isNotEmpty(tblNmEn)) {
							tblsInfo.getTblInfoMap().put(tblNmEn, tblInfo);
							tblInfo = null;
							tblNmEn = CsjConst.EMPTY;
						}
					}
					// if (row.getRowNum() == sheet.getLastRowNum() &&
					// CsjStrUtils.isNotEmpty(tblNmEn)) {
					// //if (CsjStrUtils.isNotEmpty(tblNmEn) &&
					// tblsInfo.getTblInfoMap().containsKey(tblNmEn) == false) {
					// tblsInfo.getTblInfoMap().put(tblNmEn, tblInfo);
					// tblInfo = null;
					// tblNmEn = CsjConst.EMPTY;
					// }
				}
				if (CmnStrUtils.isNotEmpty(tblNmEn)
						&& tblsInfo.getTblInfoMap().containsKey(tblNmEn) == false) {
					tblsInfo.getTblInfoMap().put(tblNmEn, tblInfo);
					tblInfo = null;
					tblNmEn = CsjConst.EMPTY;
				}
			}


			if (sheetNmErrorSet.size()>0) {
				s_isPicCheckParaError = true;
				String xlsPath = xmlDbXlsInfoAll.getCurrentXmlDb().getXlsPath();
				new File(xlsPath).mkdirs();
				String filePath = xlsPath + "error"+CsjPath.s_current_date_mid
						+ excelFile.getName();
				fileOut = new FileOutputStream(filePath);
				CmnPoiUtils.deleteWithOutSheetNm(wb, sheetNmErrorSet);
				wb.write(fileOut);
				fileOut.close();
			}

			fileInputStream.close();
		} catch (Throwable e) {
			errorList.add("FILE:"+CmnLog5j.addlrBracketsM(excelFile.getAbsolutePath(), true));
			errorList.add("SHEET:"+CmnLog5j.addlrBracketsM(sheet.getSheetName(),true));
			errorList.add("row:"+CmnLog5j.addlrBracketsM(String.valueOf(tRow.getRowNum()+1),true));
			CmnLog.logger.error(e.getMessage());
			throw new Exception(e.getMessage() + CmnStrUtils.getStrByListWithCr(errorList,true));
		} finally {
			if (fileOut != null) {
				fileOut.close();
			}
			if (fileInputStream != null) {
				fileInputStream.close();
			}

		}

		CmnLog.logger.debug("AutoXlsToDbForMemory.exeXlsToDb(DbInfo dbInfo) end");
		return tblsInfo;
	}

	/**
	 * @param dbInfo
	 * @param sqlParaList
	 * @param delMap
	 * @param tblInfo
	 * @param keyPosList
	 * @param xlsRec
	 * @param sheet
	 * @throws Throwable
	 */
	public static void checkCommit(XmlDbXlsInfoAll xmlDbXlsInfoAll, List<Object> sqlParaList,
			HashMap<String, String> delMap, String tblNm,
			List<Integer> keyPosList, XlsRecord xlsRec, Sheet sheet,HashSet<String> sheetNmErrorSet)
			throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.checkCommit(DbInfo dbInfo, List<Object> sqlParaList,HashMap<String, String> delMap, XlsTblInfo tblInfo,List<Integer> keyPosList, XlsRecord xlsRec, HSSFSheet sheet) begin");
		try {
			if (s_check_status.equals(XmlInfoSql.CHECK_TYPE_CHECK)) {
				isRecordCheckOk(xmlDbXlsInfoAll, xlsRec, sheet,sheetNmErrorSet);
			} else if (s_check_status.equals(XmlInfoSql.CHECK_TYPE_COMMIT)) {
				commitSql(xmlDbXlsInfoAll, sqlParaList, tblNm, keyPosList, xlsRec, delMap);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger.debug("AutoXlsToDbForMemory.checkCommit(DbInfo dbInfo, List<Object> sqlParaList,HashMap<String, String> delMap, XlsTblInfo tblInfo,List<Integer> keyPosList, XlsRecord xlsRec, HSSFSheet sheet) end");
	}

	/**
	 * @param dbInfo
	 * @param xlsRec
	 * @param sheet
	 * @return
	 * @throws Throwable
	 */
	private static void isRecordCheckOk(XmlDbXlsInfoAll xmlDbXlsInfoAll, XlsRecord xlsRec,
			Sheet sheet,HashSet<String> sheetNmErrorSet) throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.isRecordCheckOk(DbInfo dbInfo, XlsRecord xlsRec,HSSFSheet sheet) begin");

		try {
			boolean isDbCheckError = false;
			for (XlsTblPara xlsTblPara : xlsRec.getRecord()) {
				
				Cell cell = sheet.getRow(xlsTblPara.getXlsRowNum())
						.getCell(xlsTblPara.getXlsColNum());

				CmnLog.logger.debug("check val is :"+xlsTblPara.getParaVal());
				StringBuffer sb = getCheckDbErrorMsg(xlsTblPara);

				if (sb.length() != 0) {
					sheetNmErrorSet.add(sheet.getSheetName());
					isDbCheckError = true;
					// CsjPoiUtils.setCellComment(cell, new
					// HSSFRichTextString(sb.toString()), CsjProcess.s_user);
					cell.setCellStyle(cellStyle.getCsError());
					cellStyle.getCsError().setFont(cellStyle.getFtRed());
				}
			}

			XmlInfoSql xmlInfoSql = xmlDbXlsInfoAll.getXmlInfoSql();
			boolean isCheckWithLogic = "1".equals(xmlInfoSql
					.getIsCheckWithLogic());
			if (isCheckWithLogic && isDbCheckError == false) {
				for (XlsTblPara xlsTblPara : xlsRec.getRecord()) {
					
					if (CmnStrUtils.isEmpty(xlsTblPara.getParaVal())) {
						continue;
					}
					Cell cell = sheet.getRow(xlsTblPara.getXlsRowNum())
							.getCell(xlsTblPara.getXlsColNum());
					CmnLog.logger.debug("check logic val is :"+xlsTblPara.getParaVal());
					StringBuffer sb = getCheckLogicErrorMsg(xlsTblPara);
					
					if (sb.length() != 0) {
						sheetNmErrorSet.add(sheet.getSheetName());
						CmnPoiUtils.setCellCommentBig(cell,  new
								HSSFRichTextString(sb.toString()), CsjProcess.s_user);
						cell.setCellStyle(cellStyle.getCsDiff_18_0());
						cellStyle.getCsDiff_18_0().setFont(cellStyle.getFtRed());
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger.debug("AutoXlsToDbForMemory.isRecordCheckOk(DbInfo dbInfo, XlsRecord xlsRec,HSSFSheet sheet) end");
	}

	public static StringBuffer getCheckLogicErrorMsg(XlsTblPara xlsTblPara) throws Throwable {
		StringBuffer sb = new StringBuffer();
		String val = xlsTblPara.getParaVal();
		ParaCheck paraCheck = xlsTblPara.getParaCheck();
		if (xlsTblPara.getType() == DbInfo.TABLE_COL_TYPE_NUM) {

			if (CmnStrUtils.isNotEmpty(paraCheck.getReg())) {
				if (!val.matches(paraCheck.getReg())) {
					sb.append(CsjConst.JP_REG_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}
			if (CmnStrUtils.isNotEmpty(paraCheck.getEqualSet())) {
				if (!CmnStrUtils.isNumInSet(
						paraCheck.getEqualSet(), val)) {
					sb.append(CsjConst.JP_EQUAL_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}

			if (CmnStrUtils.isNotEmpty(paraCheck.getNoEqualSet())) {
				if (paraCheck.getNoEqualSet().contains(val)) {
					sb.append(CsjConst.JP_NO_EQUAL_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}

			BigDecimal bVal = new BigDecimal(val);
			if (CmnStrUtils.isNotEmpty(paraCheck.getMaxVal())) {
				if (new BigDecimal(paraCheck.getMaxVal())
						.compareTo(bVal) < 0) {
					sb.append(CsjConst.JP_MAX_VAL_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}
			if (CmnStrUtils.isNotEmpty(paraCheck.getMinVal())) {
				if (bVal.compareTo(new BigDecimal(paraCheck
						.getMinVal())) < 0) {
					sb.append(CsjConst.JP_MIN_VAL_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}

		} else if (xlsTblPara.getType() == DbInfo.TABLE_COL_TYPE_DATE) {

			if (CmnStrUtils.isNotEmpty(paraCheck.getReg())) {
				if (!val.matches(paraCheck.getReg())) {
					sb.append(CsjConst.JP_REG_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}
			if (CmnStrUtils.isNotEmpty(paraCheck.getEqualSet())) {
				if (!paraCheck.getEqualSet().contains(val)) {
					sb.append(CsjConst.JP_EQUAL_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}
			if (CmnStrUtils.isNotEmpty(paraCheck.getNoEqualSet())) {
				if (!paraCheck.getNoEqualSet().contains(val)) {
					sb.append(CsjConst.JP_NO_EQUAL_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}
			if (CmnStrUtils.isNotEmpty(paraCheck.getMaxVal())) {
				if (paraCheck.getMaxVal().compareTo(val) < 0) {
					sb.append(CsjConst.JP_MAX_VAL_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}
			if (CmnStrUtils.isNotEmpty(paraCheck.getMinVal())) {
				if (val.compareTo(paraCheck.getMinVal()) < 0) {
					sb.append(CsjConst.JP_MIN_VAL_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}
			if (CmnStrUtils.isNotEmpty(paraCheck.getMaxLen())) {
				if (CmnStrUtils.getIntVal(paraCheck.getMaxLen()) < CmnStrUtils
						.getBitesLength(val)) {
					sb.append(CsjConst.JP_MAX_LEN_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}
			if (CmnStrUtils.isNotEmpty(paraCheck.getMinLen())) {
				if (CmnStrUtils.getBitesLength(val) < CmnStrUtils
						.getIntVal(paraCheck.getMinLen())) {
					sb.append(CsjConst.JP_MIN_LEN_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}

		} else {

			if (CmnStrUtils.isNotEmpty(paraCheck.getReg())) {
				if (!val.matches(paraCheck.getReg())) {
					sb.append(CsjConst.JP_REG_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}
			if (CmnStrUtils.isNotEmpty(paraCheck.getEqualSet())) {
				if (!paraCheck.getEqualSet().contains(val)) {
					sb.append(CsjConst.JP_EQUAL_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}
			if (CmnStrUtils.isNotEmpty(paraCheck.getNoEqualSet())) {
				if (paraCheck.getNoEqualSet().contains(val)) {
					sb.append(CsjConst.JP_NO_EQUAL_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}

			if (CmnStrUtils.isNotEmpty(paraCheck.getMaxVal())) {
				if (paraCheck.getMaxVal().compareTo(val) < 0) {
					sb.append(CsjConst.JP_MAX_VAL_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}
			if (CmnStrUtils.isNotEmpty(paraCheck.getMinVal())) {
				if (val.compareTo(paraCheck.getMinVal()) < 0) {
					sb.append(CsjConst.JP_MIN_VAL_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}
			if (CmnStrUtils.isNotEmpty(paraCheck.getMaxLen())) {
				if (CmnStrUtils.getIntVal(paraCheck.getMaxLen()) < CmnStrUtils
						.getBitesLength(val)) {
					sb.append(CsjConst.JP_MAX_LEN_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}
			if (CmnStrUtils.isNotEmpty(paraCheck.getMinLen())) {
				if (CmnStrUtils.getBitesLength(val) < CmnStrUtils
						.getIntVal(paraCheck.getMinLen())) {
					sb.append(CsjConst.JP_MIN_LEN_ERROR);
					sb.append(CsjProcess.s_newLine);
				}
			}
		}
		return sb;
	}

	public static StringBuffer getCheckDbErrorMsg(XlsTblPara xlsTblPara) {
		StringBuffer sb = new StringBuffer();
		String val = xlsTblPara.getParaVal();
		if ((xlsTblPara.isCanNull() == false || xlsTblPara.isPkey() == true)
				&& CmnStrUtils.isEmpty(val)) {
			sb.append(CsjConst.JP_BLANK_ERROR);
			sb.append(CsjProcess.s_newLine);
		} else {
			if (CmnStrUtils.isNotEmpty(val)) {
				if (xlsTblPara.getType() == DbInfo.TABLE_COL_TYPE_NUM) {

					if (!val.matches(RegConstNum.NUM_10)) {
						sb.append(CsjConst.JP_NUM_ERROR);
						sb.append(CsjProcess.s_newLine);
					} else  {
						BigDecimal bigNum = new BigDecimal(val);
						if (xlsTblPara.getParaNumDotEndLen()<bigNum.scale()) {
							sb.append(CsjConst.JP_NUM_ERROR_DOT_END);
							sb.append(CsjProcess.s_newLine);
						}
						if (xlsTblPara.getParaLen()!=0&&xlsTblPara.getParaLen()<String.valueOf(bigNum.longValue()).length()) {
							sb.append(CsjConst.JP_NUM_ERROR_DOT_HEAD);
							sb.append(CsjProcess.s_newLine);
						}
					}

				} else if (xlsTblPara.getType() == DbInfo.TABLE_COL_TYPE_DATE) {
					if (!CmnStrUtils.isXlsDateStr(val)) {
						sb.append(CsjConst.JP_DATE_ERROR);
						sb.append(CsjProcess.s_newLine);
					}

				} else {
					if (xlsTblPara.getParaLen()>0) {
						if (xlsTblPara.getParaLen() < CmnStrUtils.getBitesLength(val)) {
							sb.append(CsjConst.JP_MAX_DB_LEN_ERROR);
							sb.append(CsjProcess.s_newLine);
						}
					}
				}
			}

		}
		return sb;
	}

	public static void commitSql(XmlDbXlsInfoAll xmlDbXlsInfoAll, List<Object> sqlParaList,
			String tblNm, List<Integer> keyPosList, XlsRecord xlsRec,
			HashMap<String, String> delMap)
			throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.commitSql(DbInfo dbInfo, List<Object> sqlParaList,XlsTblInfo tblInfo, List<Integer> keyPosList, XlsRecord xlsRec,HashMap<String, String> delMap) begin");

		try {
			CsjDBAccess dbAccess = xmlDbXlsInfoAll.getDbInfo().getDbAccess();
			XmlInfoSql xmlInfoSql = xmlDbXlsInfoAll.getXmlInfoSql();
			String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
			int uptCnt = 0;
			List<Object> sqlUptParaList = new ArrayList<Object>();
			if (keyPosList.size() == 0) {
				CmnFileUtils.writeWithbBlank(
						CmnLog5j.s_log5j,
						tblNm
								+ CsjDbToolsMsg.coreMsgMap
										.get(CsjDbToolsMsg.MSG_I_0000036), 0);
				// delSqlList.add("truncate " + tblInfo.getTblNmEn());

			} else {
				List<String> sqlList = null;
				String sqlStr = "";
				if (XmlInfoSql.EXE_TYPE_DEL_INS.equals(xmlInfoSql.getExeType())) {
					sqlList = getDeleteSql(xmlDbXlsInfoAll, tblNm, keyPosList, xlsRec,
							sqlParaList);
					sqlStr = sqlList.get(0);
				} else {
					sqlList = getUpdateSql(xmlDbXlsInfoAll, tblNm, keyPosList, xlsRec,
							sqlParaList, 
							sqlUptParaList);
					sqlStr = sqlList.get(0);
				}

				if (delMap.containsKey(sqlStr)) {
					sqlParaList.clear();
				} else {
					delMap.put(sqlStr, "");
					if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
						sqlStr = sqlStr.replaceAll("\"", "`");
					}
					if ("0".equals(xmlInfoSql.getIsDeleteTable())) {
						CmnFileUtils.writeWithbBlank(CmnLog5j.s_log5j, sqlStr + CsjDbToolsMsg.coreMsgMap
								.get(CsjDbToolsMsg.MSG_I_0000071), 0);
					}
					if (xmlInfoSql.getExeAbled().contains("1")) {

						if (XmlInfoSql.EXE_TYPE_DEL_INS.equals(xmlInfoSql.getExeType())) {
							String delExeSql = sqlList.get(1);
							if ("0".equals(xmlInfoSql.getIsDeleteTable())) {
								if (xmlInfoSql.getIsBatch().contains("1")) {
									dbAccess.executeBatchSQL(delExeSql, sqlParaList);
								} else {
									dbAccess.executeSQL(delExeSql, sqlParaList);
								}
								exeInserLog(tblNm, xmlDbXlsInfoAll, sqlStr);
							}
						} else {
								String uptExeSql = sqlList.get(1);
								if (xmlInfoSql.getIsBatch().contains("1")) {
									dbAccess.executeBatchSQL(uptExeSql, sqlUptParaList);
								} else {
									uptCnt = dbAccess.executeSQL(uptExeSql, sqlUptParaList);
								}
								
								exeInserLog(tblNm, xmlDbXlsInfoAll, sqlStr);
						}
					}
					sqlParaList.clear();
					//sqlCnt += 1;
				}
			}

			if (("1".equals(xmlInfoSql.getHaveInsert()) && XmlInfoSql.EXE_TYPE_DEL_INS.equals(xmlInfoSql.getExeType()))
					|| (uptCnt == 0 && XmlInfoSql.EXE_TYPE_UPT_INS.equals(xmlInfoSql.getExeType()) )) {
				String insStr = getInsertSql(xmlDbXlsInfoAll, tblNm, xlsRec);
				if (DbInfo.STR_DB_TYPE_MYSQL.equals(xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect())) {
					insStr = insStr.replaceAll("\"", "`");
				}
				CmnLog.logger.debug(insStr);
				if ("1".equals(xmlInfoSql.getIsLogTxtAbled())) {
					CmnFileUtils.writeWithbBlank(CmnLog5j.s_log5j, insStr + CsjDbToolsMsg.coreMsgMap
							.get(CsjDbToolsMsg.MSG_I_0000071), 0);
				}
		

				if ("1".equals(xmlInfoSql.getIsDeleteTable())) {
					writeCommit(xmlInfoSql,dbType);
				}

				if (xmlInfoSql.getExeAbled().contains("1")) {

					String insExeStr = getInsertExeSql(dbType,
							tblNm, xlsRec, sqlParaList,xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot());
					if (xmlInfoSql.getIsBatch().contains("1")) {
						dbAccess.executeBatchSQL(insExeStr, sqlParaList);
						exeInserLog(tblNm, xmlDbXlsInfoAll, insStr);
					} else {
						dbAccess.executeSQL(insExeStr, sqlParaList);
						exeInserLog(tblNm, xmlDbXlsInfoAll, insStr);
					}
					sqlParaList.clear();
				}

			}

		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger.debug("AutoXlsToDbForMemory.commitSql(DbInfo dbInfo, List<Object> sqlParaList,XlsTblInfo tblInfo, List<Integer> keyPosList, XlsRecord xlsRec,HashMap<String, String> delMap) end");
	}

	
	/**
	 * @param dbXlsInfo
	 * @param tblInfo
	 * @param keyPosList
	 * @param xlsRec
	 * @param sqlParaList
	 * @param dbType
	 * @param sqlUptParaList
	 * @return
	 */
	public static String getSelectSql(XmlDbXlsInfoAll xmlDbXlsInfoAll,String tblNmEn,Map<Integer, TblPara> paraPosInfoMap)
			throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.getSelectSql(XmlDbXlsInfoAll xmlDbXlsInfoAll,String tblNmEn, XlsRecord xlsRec) begin");
		StringBuffer sbExe = new StringBuffer("select ");
		try {
			int i = 0;
			for (TblPara para : paraPosInfoMap.values()) {
				if (i + 1 == paraPosInfoMap.size()) {
					sbExe.append(" \"" + para.getParaNmEn() + "\"");
				} else {
					sbExe.append(" \"" + para.getParaNmEn() + "\"");
					sbExe.append(",");
				}
				i++;
			}
			sbExe.append(" from " + xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+ "\""+tblNmEn + "\"");
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger.debug("AutoXlsToDbForMemory.getUpdateSql(XmlDbXlsInfoAll xmlDbXlsInfoAll,String tblNmEn, XlsRecord xlsRec) end");
		return sbExe.toString();
	}
	
	/**
	 * @param dbXlsInfo
	 * @param tblInfo
	 * @param keyPosList
	 * @param xlsRec
	 * @param sqlParaList
	 * @param dbType
	 * @param sqlUptParaList
	 * @return
	 */
	public static List<String> getUpdateSql(XmlDbXlsInfoAll xmlDbXlsInfoAll,
			String tblNmEn, List<Integer> keyPosList, XlsRecord xlsRec,
			List<Object> sqlParaList, List<Object> sqlUptParaList)
			throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.getUpdateSql(DbXlsInfo dbXlsInfo,XlsTblInfo tblInfo, List<Integer> keyPosList, XlsRecord xlsRec,List<Object> sqlParaList, int dbType, List<Object> sqlUptParaList) begin");
		List<String> retList = new ArrayList<String>();
		try {
			String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
			String isUpdateWithOutNull = xmlDbXlsInfoAll.getXmlInfoSql().getIsUpdateWithOutNull();
			StringBuffer sbTxt = new StringBuffer();
			StringBuffer sbExe = new StringBuffer();

			String sqlHead = "update  " +xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+ "\""+tblNmEn + "\" set ";
			sbTxt.append(sqlHead);
			sbExe.append(sqlHead);

			Set<String> keySet = new HashSet<String>();
			for (Integer posIndex : keyPosList) {

				XlsTblPara para = xlsRec.getRecmap().get(posIndex);
				keySet.add(para.getParaNmEn());
			}

			List<XlsTblPara> tblPara = xlsRec.getRecord();
			for (int i = 0; i < tblPara.size(); i++) {
				XlsTblPara para = tblPara.get(i);
//				if (keySet.contains(para.getParaNmEn())) {
//					continue;
//				}
				if ("1".equals(isUpdateWithOutNull)) {
					if (CmnStrUtils.isEmpty(para.getParaVal())) {
						continue;
					}
				}
//				if (i + 1 == tblPara.size()) {
//					sbTxt.append(" \"" + para.getParaNmEn() + "\" = "
//							+ getInsUptParaVal(para, dbType));
//					sbExe.append(" \"" + para.getParaNmEn() + "\" = ?");
//					sbTxt.append(" ");
//					sbExe.append(" ");
//				} else {
					sbTxt.append(" \"" + para.getParaNmEn() + "\" = "
							+ getInsUptParaVal(para, dbType));
					sbExe.append(" \"" + para.getParaNmEn() + "\" = ?");
					sbTxt.append(",");
					sbExe.append(",");
//				}
				sqlUptParaList.add(para);
			}
			sbTxt = new StringBuffer(sbTxt.substring(0, sbTxt.lastIndexOf(",")));
			sbExe = new StringBuffer(sbExe.substring(0, sbExe.lastIndexOf(",")));
			sbTxt.append(" where 1=1");
			sbExe.append(" where 1=1");
			for (Integer posIndex : keyPosList) {

				XlsTblPara para = xlsRec.getRecmap().get(posIndex);
//				keySet.add(para.getParaNmEn());
				try {
					List<String> whereList = getWhereCond(para, false,
							sqlUptParaList, dbType);
					sbTxt.append(whereList.get(0));
					sbExe.append(whereList.get(1));
				} catch (Throwable e) {
					e.printStackTrace();
					CmnLog.logger.error(e.getMessage());
				}
			}

//			if (xlsRec.getRecord().size() == keySet.size()) {
//				MyLog.logger.info("the record is not have noKeyCol");
//				retList.add("");
//				retList.add("");
//			} else if (keySet.size() == 0) {
//				MyLog.logger.info("the record is not have keyCol");
//				retList.add("");
//				retList.add("");
//			} else {
				retList.add(sbTxt.toString());
				retList.add(sbExe.toString());
//			}

		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger.debug("AutoXlsToDbForMemory.getUpdateSql(DbXlsInfo dbXlsInfo,XlsTblInfo tblInfo, List<Integer> keyPosList, XlsRecord xlsRec,List<Object> sqlParaList, int dbType, List<Object> sqlUptParaList) end");
		return retList;
	}

	/**
	 * @param dbXlsInfo
	 * @param tblInfo
	 * @param keyPosList
	 * @param xlsRecLst
	 * @param sqlParaList
	 * @param dbType
	 * @param sqlUptParaList
	 * @return
	 */
	public static List<String> getSelectSql(XmlDbXlsInfoAll xmlDbXlsInfoAll,
			String tblNmEn, List<Integer> keyPosList, List<XlsRecord> xlsRecLst,
			List<Object> sqlParaList, List<Object> sqlUptParaList)
			throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.getUpdateSql(DbXlsInfo dbXlsInfo,XlsTblInfo tblInfo, List<Integer> keyPosList, XlsRecord xlsRec,List<Object> sqlParaList, int dbType, List<Object> sqlUptParaList) begin");
		List<String> retList = new ArrayList<String>();
		try {
			String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
			String isUpdateWithOutNull = xmlDbXlsInfoAll.getXmlInfoSql().getIsUpdateWithOutNull();
			StringBuffer sbTxt = new StringBuffer();
			StringBuffer sbExe = new StringBuffer();

			String sqlHead = "select  ";
			sbTxt.append(sqlHead);
			sbExe.append(sqlHead);

			Set<String> keySet = new HashSet<String>();
			
				for (Integer posIndex : keyPosList) {

					XlsTblPara para = xlsRecLst.get(0).getRecmap().get(posIndex);
					keySet.add(para.getParaNmEn());
				}
				
				List<XlsTblPara> tblPara = xlsRecLst.get(0).getRecord();
				for (int i = 0; i < tblPara.size(); i++) {
					XlsTblPara para = tblPara.get(i);
//					if (keySet.contains(para.getParaNmEn())) {
//						continue;
//					}
					if ("1".equals(isUpdateWithOutNull)) {
						if (CmnStrUtils.isEmpty(para.getParaVal())) {
							continue;
						}
					}
//					if (i + 1 == tblPara.size()) {
//						sbTxt.append(" \"" + para.getParaNmEn() + "\" = "
//								+ getInsUptParaVal(para, dbType));
//						sbExe.append(" \"" + para.getParaNmEn() + "\" = ?");
//						sbTxt.append(" ");
//						sbExe.append(" ");
//					} else {
						sbTxt.append(" \"" + para.getParaNmEn() + "\"");
						sbExe.append(" \"" + para.getParaNmEn() + "\"");
						sbTxt.append(",");
						sbExe.append(",");
//					}
					sqlUptParaList.add(para);
				}


			sbTxt = new StringBuffer(sbTxt.substring(0, sbTxt.lastIndexOf(",")));
			sbExe = new StringBuffer(sbExe.substring(0, sbExe.lastIndexOf(",")));
			sbTxt.append(" from " + xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+ "\""+tblNmEn + "\" ");
			sbExe.append(" from " +  xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+ "\""+tblNmEn + "\" ");
			
			if (CmnStrUtils.isNotEmpty(keySet)) {
				sbTxt.append(" where 1=2 ");
				sbExe.append(" where 1=2 ");
			}
			for (XlsRecord xlsRecord : xlsRecLst) {
				String txt = CsjConst.EMPTY;
				String exe = CsjConst.EMPTY;
				for (Integer posIndex : keyPosList) {

					XlsTblPara para = xlsRecord.getRecmap().get(posIndex);
//					keySet.add(para.getParaNmEn());
					try {
						List<String> whereList = getWhereCond(para, false,sqlUptParaList, dbType);
						txt+=whereList.get(0);
						exe+=whereList.get(1);
					} catch (Throwable e) {
						e.printStackTrace();
						CmnLog.logger.error(e.getMessage());
					}
				}
				if (CmnStrUtils.isNotEmpty(txt)) {
					sbTxt.append(" OR (" + txt.replaceFirst("AND", "") + ")");
				}
				if (CmnStrUtils.isNotEmpty(exe)) {
					sbExe.append(" OR (" + exe.replaceFirst("AND", "") + ")");
				}
				
				//.replaceFirst("AND", "OR")
			}


//			if (xlsRec.getRecord().size() == keySet.size()) {
//				MyLog.logger.info("the record is not have noKeyCol");
//				retList.add("");
//				retList.add("");
//			} else if (keySet.size() == 0) {
//				MyLog.logger.info("the record is not have keyCol");
//				retList.add("");
//				retList.add("");
//			} else {
				retList.add(sbTxt.toString());
				retList.add(sbExe.toString());
//			}

		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger.debug("AutoXlsToDbForMemory.getUpdateSql(DbXlsInfo dbXlsInfo,XlsTblInfo tblInfo, List<Integer> keyPosList, XlsRecord xlsRec,List<Object> sqlParaList, int dbType, List<Object> sqlUptParaList) end");
		return retList;
	}
	/**
	 * @param sqlParaList
	 * @param tableNm
	 * @param dbInfo
	 * @param delStr
	 * @throws Throwable
	 */
	public static void exeInserLog(String tableNm, XmlDbXlsInfoAll xmlDbXlsInfoAll, String sql)
			throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.exeInserLog(String tableNm, DbInfo dbInfo, String sql) begin");
		try {
			CsjDBAccess dbAccess = xmlDbXlsInfoAll.getDbInfo().getDbAccess();
			if ("1".equals(xmlDbXlsInfoAll.getXmlInfoSql().getIsLogAbled()) == false) {
				return;
			}

			List<Object> sqlParaList = new ArrayList<Object>();
			
			XlsTblPara xlsTblPara = new XlsTblPara();
			xlsTblPara.setType(DbInfo.TABLE_COL_TYPE_STR);
			xlsTblPara.setParaType("CHAR");
//			if (isBatch) {
//				if (dbAccess.getInsertLogCnt() == -1) {
//					cnt = getLogMaxId(xmlDbXlsInfoAll);
//				} else {
//					cnt = dbAccess.getInsertLogCnt()+1;
//				}
//				dbAccess.setInsertLogCnt(cnt);
//			}
			String now = CmnDateUtil.getCurrentDateString(CsjConst.YYYYMMDDHHMMSSSSS);
			if (now.equals(s_tmp_now)) {
				s_tmp_now_index++;
			} else {
				s_tmp_now = now;
				s_tmp_now_index=0;
			}
			xlsTblPara.setParaVal(now+"_"+String.format("%07d", s_tmp_now_index));
			sqlParaList.add(xlsTblPara);
			
			xlsTblPara = new XlsTblPara();
			xlsTblPara.setType(DbInfo.TABLE_COL_TYPE_STR);
			xlsTblPara.setParaType("CHAR");
			xlsTblPara.setParaVal(tableNm);
			sqlParaList.add(xlsTblPara);
			
			xlsTblPara = new XlsTblPara();
			xlsTblPara.setType(DbInfo.TABLE_COL_TYPE_STR);
			xlsTblPara.setParaType("CHAR");
			xlsTblPara.setParaVal(sql);

			sqlParaList.add(xlsTblPara);

			if (CsjConst.USER_COMMAND.equals(tableNm)) {
				dbAccess.executeSQL(xmlDbXlsInfoAll.getDbInfo().getDbAccess().getSqlInsertLog(), sqlParaList);
			} else {
				if (xmlDbXlsInfoAll.getXmlInfoSql().getIsBatch().contains("1")) {
					dbAccess.executeBatchSQL(xmlDbXlsInfoAll.getDbInfo().getDbAccess().getSqlInsertLog(), sqlParaList);
				} else {
					dbAccess.executeSQL(xmlDbXlsInfoAll.getDbInfo().getDbAccess().getSqlInsertLog(), sqlParaList);
				}
			}
					

		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger.debug("AutoXlsToDbForMemory.exeInserLog(String tableNm, DbInfo dbInfo, String sql) end");
	}

	public static long getLogMaxId(XmlDbXlsInfoAll xmlDbXlsInfoAll)  throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.getLogMaxId(XmlDbXlsInfoAll xmlDbXlsInfoAll) begin");
		long maxCnt = 0;
		try {
			String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();

			CsjDBAccess dbAccess = xmlDbXlsInfoAll.getDbInfo().getDbAccess();
			List<Object> sqlParaList = new ArrayList<Object>();
			String maxSql = "select nvl(count(\"id\"),0) + 1 as CNT from \""+DbInfo.S_LOG_TABLE+"\"";
			if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
				maxSql = "select nvl(count(\"ID\"),0) + 1 as CNT from \""+DbInfo.S_LOG_TABLE+"\"";
			} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
				maxSql = "select coalesce(count(\"ID\"),0) + 1 as CNT from \""+DbInfo.S_LOG_TABLE+"\"";
			} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
				maxSql = "select (isnull(count(\"ID\"),0) + 1) as CNT from \""+DbInfo.S_LOG_TABLE+"\"";
			} else if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
				maxSql = "select (IFNULL(count(\"id\"), 0) + 1) as CNT from \""+DbInfo.S_LOG_TABLE+"\"";
			} else if (DbInfo.STR_DB_TYPE_DB2.equals(dbType)) {
				maxSql = "select nvl(count(\"ID\"),0) + 1 as CNT from \""+DbInfo.S_LOG_TABLE+"\"";

			} else if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {
				maxSql = "(select (isnull(count(\"ID\"),0) + 1) as CNT from \""+DbInfo.S_LOG_TABLE+"\")";
			}
			maxCnt = commitCntSql(dbAccess, maxSql, sqlParaList);
		
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger.debug("AutoXlsToDbForMemory.getLogMaxId(XmlDbXlsInfoAll xmlDbXlsInfoAll) end");
		return maxCnt;
	}
	
	public static int commitCntSql(CsjDBAccess dbAccess, String sql,
			List<Object> paraList)
			throws Throwable {
		CmnLog.logger.debug("AutoXlsToDbForMemory.commitCntSql(CsjDBAccess dbAccess, String sql,List<Object> paraList) begin");
		int retVal = 0;
		try {
			StringBuffer sbSql = new StringBuffer();

			sbSql.append(sql);

			List<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();

			HashMap<String, String> dataMap = new HashMap<String, String>();
			resultList = dbAccess.getRecordList(sbSql.toString(), paraList,1).getResultList();

			if (resultList != null && resultList.size() != 0) {
				dataMap = resultList.get(0);
				retVal = CmnStrUtils.getIntVal(String.valueOf(dataMap.get("CNT")));
				StaticClz.ONE_INDEX--;
				StaticClz.ALL_INDEX--;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger.debug("AutoXlsToDbForMemory.commitCntSql(CsjDBAccess dbAccess, String sql,List<Object> paraList) end");
		return retVal;
	}
}

/*
 * select
 *
 * t3.name as 表名,t1.name as 字段名,t2.text as 默认值 ,t4.name
 * from syscolumns t1,syscomments t2,sysobjects t3 ,sysobjects t4
 * where t1.cdefault=t2.id and t3.xtype='u' and t3.id=t1.id
 * and t4.xtype='d' and t4.id=t2.id;
 *
 *
 * SQLlSERVER
 * exec sp_helpindex 'zzz_test'
 *
 *
 * ORACLE
 * select t.*, i.*--i.index_type
 * from user_ind_columns t, user_indexes i
 * where t.index_name = i.index_name
 * and t.table_name = i.table_name
 * and t.table_name = 'CSJTEST'
 */

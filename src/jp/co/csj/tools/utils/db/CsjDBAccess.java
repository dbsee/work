package jp.co.csj.tools.utils.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.mydbsee.common.CmnDateUtil;
import org.mydbsee.common.CmnLog;
import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.core.CsjDbDataLst;
import jp.co.csj.tools.core.CsjLinkedMap;
import jp.co.csj.tools.utils.common.CsjCheck;
import jp.co.csj.tools.utils.common.CsjProcess;
import jp.co.csj.tools.utils.common.StaticClz;
import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.db.core.DbInfo;
import jp.co.csj.tools.utils.db.core.TableDbItem;
import jp.co.csj.tools.utils.db.core.TblPara;
import jp.co.csj.tools.utils.db.core.XlsTblPara;
import jp.co.csj.tools.utils.db.mysql.CsjMySqlColType;
import jp.co.csj.tools.utils.db.oracle.CsjOracleColType;
import jp.co.csj.tools.utils.db.postgre.CsjPostgreColType;
import jp.co.csj.tools.utils.db.sqlserver.CsjSqlServerColType;
import jp.co.csj.tools.utils.xml.dbtools.XmlDbXlsInfoAll;
import jp.co.csj.tools.utils.xml.dbtools.XmlInfoXls;
import jp.co.csj.tools.utils.z.exe.batch.AutoDbToXls;

/**
 *
 * DB処理作成
 *
 */
public class CsjDBAccess {

	public static final String ORACLE_DRIVER_CLASS = "oracle.jdbc.driver.OracleDriver";
	public static final String POSTGRE_DRIVER_CLASS = "org.postgresql.Driver";
	public static final String MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";
	public static final String SQLSERVER_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public static final String SQLSERVER_DRIVER_CLASS_2000 = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	public static final String DB2_DRIVER_CLASS = "com.ibm.db2.jcc.DB2Driver";
	public static final String SYBASE_DRIVER_CLASS = "com.sybase.jdbc4.jdbc.SybDriver";
	public static final String SQLITE_DRIVER_CLASS = "org.sqlite.JDBC";

	public static final String SQL_DATE_ORALCE="select to_char(sysdate,'YYYYMMDD') STR_DATE from dual";
	public static final String SQL_DATE_POSTGRE="select to_char( CURRENT_DATE, 'YYYYMMDD') as STR_DATE";
	public static final String SQL_DATE_MYSQL="select date_format(now(),'%Y%m%d') STR_DATE";
	public static final String SQL_DATE_SQLSERVER="select CONVERT(varchar(12) , getdate(), 112 ) STR_DATE";
	public static final String SQL_DATE_DB2="SELECT int(date(current date)) as STR_DATE FROM sysibm.sysdummy1";
	public static final String SQL_DATE_SYBASE="select CONVERT(varchar(12) , getdate(), 112 ) STR_DATE";
	
	public static final String SQL_DATE_SQLITE="select strftime('%Y/%m/%d','now')";
	private LinkedHashMap<String, String> colTypeMap = new LinkedHashMap<String, String>();

	private XmlDbXlsInfoAll xmlDbXlsInfoAll = null;
	private String dataBaseVer="";
	public static String costTime = "";
	public static String overMaxRecord = "";
	private String sqlInsertLog = "";
	/** コンネクション */
	private Connection connection = null;
	private String dbType = "";
	private String strDate ="";
	private String dbStr = "";
	private Map<String,PreparedStatement> preStatemetMap = new LinkedHashMap<String, PreparedStatement>();

	public static final int FETCH_SIZE = 100;
	private long sqlCnt = 0;

	/**
	 * @throws Throwable 
	 *
	 */
	public CsjDBAccess(XmlDbXlsInfoAll xmlDbXlsInfoAll) throws Throwable {
		this.xmlDbXlsInfoAll = xmlDbXlsInfoAll;
		this.dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
		this.sqlInsertLog = getInsertLogSql(xmlDbXlsInfoAll);
	}

	/**
	 * DB接続を取得
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * DB接続
	 *
	 * @throws Throwable
	 *             例外
	 */
	public void openConnection() throws Throwable {
		String strDBUrl = xmlDbXlsInfoAll.getCurrentXmlDb().getDbUrl();
		String ip = xmlDbXlsInfoAll.getCurrentXmlDb().getIp();
		String database = xmlDbXlsInfoAll.getCurrentXmlDb().getDatabase();
		String port = xmlDbXlsInfoAll.getCurrentXmlDb().getPort();
		String strUserId =  xmlDbXlsInfoAll.getCurrentXmlDb().getDbUserId();
		String strPassword =  xmlDbXlsInfoAll.getCurrentXmlDb().getDbPassword();
		String serverUrl = CsjConst.EMPTY;
		try {

			// DB接続用URL、ユーザ名とパスワードを取得する

			// DB接続用オブジェクット指定
			Class.forName(ORACLE_DRIVER_CLASS);
			Class.forName(POSTGRE_DRIVER_CLASS);
			Class.forName(SQLSERVER_DRIVER_CLASS);
//			Class.forName(SQLSERVER_DRIVER_CLASS_2000);
			Class.forName(MYSQL_DRIVER_CLASS);
			Class.forName(DB2_DRIVER_CLASS);
			Class.forName(SYBASE_DRIVER_CLASS);
			Class.forName(SQLITE_DRIVER_CLASS);
			
			CmnLog.logger.info("DB connecting ...");

			// DB接続

			//jdbc:oracle:thin:@localhost:1521:orcl","system","ok"
	

			String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
			if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
				serverUrl = strDBUrl+ip+":"+port+"/"+database;
			} else if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
				serverUrl = strDBUrl+ip+":"+port+"/"+database+"?useUnicode=true&characterEncoding="+xmlDbXlsInfoAll.getXmlInfoSql().getEncode();
			} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
				serverUrl = strDBUrl+ip+":"+port+"/"+database;
			} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
				serverUrl = strDBUrl+ip+":"+port+";DatabaseName="+database;
			} else if (DbInfo.STR_DB_TYPE_DB2.equals(dbType)) {
				serverUrl = strDBUrl+ip+":"+port+"/"+database;
			}  else if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {
				serverUrl = strDBUrl+ip+":"+port+"/"+database;
			}  else if (DbInfo.STR_DB_TYPE_SQLITE.equals(dbType)) {
				if (database.contains(":")) {
					serverUrl = strDBUrl+database;
				} else {
					serverUrl = strDBUrl+CsjProcess.s_pj_path + database;
				}
			}  else {
				serverUrl = strDBUrl+ip+":"+port+":"+database;
			}
			
			CmnLog.logger.info("serverUrl:" + serverUrl);
			CmnLog.logger.info("strUserId:" + strUserId);
			CmnLog.logger.info("strPassword:" + strPassword);
			try {
				connection = DriverManager.getConnection(serverUrl, strUserId, strPassword);
			} catch (Throwable e) {
				
				if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
					CmnLog.logger.error("update before ServerURL:" +serverUrl);
					serverUrl = strDBUrl+ip+":"+port+":"+database;
					CmnLog.logger.error("update after ServerURL:" +serverUrl);
					connection = DriverManager.getConnection(serverUrl, strUserId, strPassword);
				} else {
					throw e;
				}
			}
			
			List<Object> sqlParaList= new ArrayList<Object>();
			String retVal="";

			if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
				retVal=getContent(SQL_DATE_ORALCE,sqlParaList);
			} else if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
				retVal=getContent(SQL_DATE_MYSQL,sqlParaList);
			} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
				retVal=getContent(SQL_DATE_POSTGRE,sqlParaList);
			} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
				retVal=getContent(SQL_DATE_SQLSERVER,sqlParaList);
			} else if (DbInfo.STR_DB_TYPE_DB2.equals(dbType)) {
				retVal=getContent(SQL_DATE_DB2,sqlParaList);
			} else if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {
				retVal=getContent(SQL_DATE_SYBASE,sqlParaList);
			} else if (DbInfo.STR_DB_TYPE_SQLITE.equals(dbType)) {
				retVal=getContent(SQL_DATE_SQLITE,sqlParaList);
				
			}
			
			
			if (CsjCheck.checkDate.compareTo(retVal)<=0) {
				CsjCheck.isCheckError = true;
			}
			CmnLog.logger.info("DB connected");
			CmnLog.logger.info("DB connected success");
		} catch (Throwable e) {
			throw new Exception(e.getMessage()
					+ CsjProcess.s_newLine + "server_url:" + serverUrl
					+ CsjProcess.s_newLine + "user_id:" + strUserId
					+ CsjProcess.s_newLine + "password:" + strPassword+ CsjProcess.s_newLine+ CsjProcess.s_newLine);
		}
	

	}

	/**
	 * DB接続閉じる
	 *
	 * @throws SQLException
	 *             SQL例外
	 */
	public void closeConnection() throws SQLException {

		if (connection != null) {
			connection.close();
			CmnLog.logger.info("DB disconnected");
		} else {
			CmnLog.logger.info("DB connection is null ");
		}
	}

	/**
	 * コミット
	 *
	 * @throws SQLException
	 *             SQL例外
	 */
	public void commit() throws SQLException {

		connection.commit();

		CmnLog.logger.info("DB commit");
	}

	/**
	 * ロールバック
	 *
	 * @throws SQLException
	 *             SQL例外
	 */
	public void rollback() throws SQLException {

		connection.rollback();

		CmnLog.logger.info("DB rollback");
	}

	/**
	 * autocommitの解除
	 *
	 * @throws SQLException
	 *             SQL例外
	 */
	public void setAutoCommitOff() throws SQLException {

		// autocommitの解除
		connection.setAutoCommit(false);

	}

	/**
	 * autocommitの設定
	 *
	 * @throws SQLException
	 *             SQL例外
	 */
	public void setAutoCommitOn() throws SQLException {

		// autocommitの設定
		connection.setAutoCommit(true);

	}

	/**
	 * 挿入、更新、削除などのSQL文を実行する
	 *
	 * @param strSQL
	 *            SQL文
	 * @param paraList
	 *            SQL文パラメーター
	 * @throws SQLException
	 *             SQL例外
	 * @return 実行件数
	 */
	public int executeSQL(String strSQL, List<Object> paraList) throws Throwable {

		if (CmnStrUtils.isEmpty(paraList)) {
			paraList = new ArrayList<Object>();
		}
		//strSQL = CsjStrUtils.fromAtoBByTrim(strSQL, "", "--");
		if (CmnStrUtils.isEmpty(strSQL)||strSQL.startsWith("--")) {
			return 0;
		}
		if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
			strSQL = strSQL.replaceAll("\"", "`");
		}
		CmnLog.logger.info("SQL = " + strSQL);

		for (int i = 0; i < paraList.size(); i++) {
			XlsTblPara xlsTblPara = (XlsTblPara)paraList.get(i);
			CmnLog.logger.info("  PARAM" + i+":"+xlsTblPara.getParaVal());
		}

		// 実行件数を初期化する
		int iExeCnt = 0;

		// コンネクションがNULLではない場合
		if (connection != null) {

			// ステートメントを作成する
			PreparedStatement statement = createPreStatement(strSQL);

			if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
				setPsByMysql(paraList, statement);
			} else if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
				setPsByOracle(paraList, statement);
			} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
				setPsByPostgre(paraList, statement);
			} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
				setPsBySqlServer(paraList, statement);
			} else if (DbInfo.STR_DB_TYPE_DB2.equals(dbType)) {
				setPsByDb2(paraList, statement);
			} else if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {
				setPsBySybase(paraList, statement);
			} else if (DbInfo.STR_DB_TYPE_SQLITE.equals(dbType)) {
				setPsBySqlLite(paraList, statement);
			} else {
				setPsByOracle(paraList, statement);
			}

			paraList.clear();

			if (!connection.getAutoCommit()) {
				sqlCnt++;
				if (sqlCnt % CsjCheck.s_commit_count == 0) {
					CmnLog.logger.info("commit_count = " + sqlCnt);
					commit();
				}
			}

			// SQL文を実行する
			iExeCnt = statement.executeUpdate();

			// ステートメントを閉じる
			statement.close();

		} else {
			throw new SQLException("connection is unavailable");
		}

		// 実行件数を返す
		return iExeCnt;
	}
	/**
	 * 挿入、更新、削除などのSQL文を実行する
	 *
	 * @param strSQL
	 *            SQL文
	 * @param paraList
	 *            SQL文パラメーター
	 * @throws SQLException
	 *             SQL例外
	 * @return 実行件数
	 */
	public int executeBatchSQL(String strSQL, List<Object> paraList) throws Throwable {

		int retNum = 0;
		if (CmnStrUtils.isEmpty(paraList)) {
			paraList = new ArrayList<Object>();
		}
//		strSQL = CsjStrUtils.fromAtoBByTrim(strSQL, "", "--");
		if (CmnStrUtils.isEmpty(strSQL)||strSQL.startsWith("--")) {
			return 0;
		}
		if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
			strSQL = strSQL.replaceAll("\"", "`");
		}
		CmnLog.logger.info("SQL = " + strSQL);

		for (int i = 0; i < paraList.size(); i++) {
			XlsTblPara xlsTblPara = (XlsTblPara)paraList.get(i);
			CmnLog.logger.info("  PARAM" + i+":"+xlsTblPara.getParaVal());
		}



		// コンネクションがNULLではない場合
		if (connection != null) {

			// ステートメントを作成する
			//PreparedStatement statement = createPreStatement(strSQL);

			PreparedStatement statement = preStatemetMap.get(strSQL);
			
			if (statement == null) {
				// ステートメントを作成する
				statement = createPreStatement(strSQL);
				preStatemetMap.put(strSQL, statement);
			} 
			
			if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
				setPsByMysql(paraList, statement);
			} else if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
				setPsByOracle(paraList, statement);
			} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
				setPsByPostgre(paraList, statement);
			} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
				setPsBySqlServer(paraList, statement);
			} else if (DbInfo.STR_DB_TYPE_DB2.equals(dbType)) {
				setPsByDb2(paraList, statement);
			} else if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {
				setPsBySybase(paraList, statement);
			} else if (DbInfo.STR_DB_TYPE_SQLITE.equals(dbType)) {
				setPsBySqlLite(paraList, statement);
			} else {
				setPsByOracle(paraList, statement);
			}

			paraList.clear();
			statement.addBatch();
			if (!connection.getAutoCommit()) {
				sqlCnt++;
				if (sqlCnt % CsjCheck.s_commit_count == 0) {
					CmnLog.logger.info("commit_count = " + sqlCnt);
					for (PreparedStatement st : preStatemetMap.values()) {
						int[] arrCnt = st.executeBatch();
						for (Integer in : arrCnt) {
							retNum += in;
						}
						st.clearBatch();
					}
					
//					int[] arrCnt = statement.executeBatch();
//					for (Integer in : arrCnt) {
//						retNum+=in;
//					}
					commit();
//		            statement.clearBatch();
				}
			}
//			// 実行件数を初期化する
//			int iExeCnt = 0;
//			// SQL文を実行する
//			iExeCnt = statement.executeUpdate();

//			// ステートメントを閉じる
//			statement.close();

		} else {
			throw new SQLException("connection is unavailable");
		}

		// 実行件数を返す
		return retNum;
	}
//    public Clob oracleStr2Clob(String str,Clob lob) throws Throwable {
//
//        Method methodToInvoke = lob.getClass().getMethod(
//
//        "getCharacterOutputStream",(Class[]) null);
//
//        Writer writer = (Writer) methodToInvoke.invoke(lob,(Object[]) null);
//
//        writer.write(str);
//
//        writer.close();
//
//        return lob;
//
//        }
	/**
	 * @param paraList
	 * @param statement
	 * @throws SQLException
	 */
		// SQL文パラメーターのセート
	public void setPsByOracle(List<Object> paraList, PreparedStatement statement) throws Throwable {
		if (paraList != null && paraList.size() > 0) {
			for (int i = 0; i < paraList.size(); i++) {
				XlsTblPara xlsTblPara = (XlsTblPara) paraList.get(i);
				String strColumnType = xlsTblPara.getParaType();
				String strVal = xlsTblPara.getParaVal();
				int index = i +1;
				if (CmnStrUtils.isEmpty(xlsTblPara.getParaVal())) {
					statement.setObject(index, null);
					continue;
				}

				// タイプ転換
				if (CsjOracleColType.TYPE_BINARY_DOUBLE.equalsIgnoreCase(strColumnType)) {
					statement.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(strVal)));
				} else if (CsjOracleColType.TYPE_BLOB.equalsIgnoreCase(strColumnType)) {
					statement.setBinaryStream(index,CmnStrUtils.String2ByteArrayInputStream(strVal));
				} else if (CsjOracleColType.TYPE_CLOB.equalsIgnoreCase(strColumnType)) {
					statement.setString(index, strVal);
				} else if (CsjOracleColType.TYPE_CHAR.equalsIgnoreCase(strColumnType)) {
					statement.setString(index,strVal);
				} else if (CsjOracleColType.TYPE_DATE.equalsIgnoreCase(strColumnType)) {

					strVal = CmnStrUtils.funReplace(strVal, "-", "/");
					String style = CsjConst.YYYY_MM_DD_SLASH;
					if (strVal.length() > 10) {
						style = CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24;
					}

						java.sql.Timestamp dateTime = new java.sql.Timestamp(
								(CmnDateUtil.getDate(strVal, style).getTime()));// Timestamp类

						statement.setTimestamp(index, dateTime);


				} else if (CsjOracleColType.TYPE_INTERVALDS.equalsIgnoreCase(strColumnType)) {
					statement.setString(index, strVal);
				} else if (CsjOracleColType.TYPE_INTERVALYM.equalsIgnoreCase(strColumnType)) {
					statement.setString(index, strVal);
				} else if (CsjOracleColType.TYPE_LONG.equalsIgnoreCase(strColumnType)) {

					//廃棄類型
					statement.setString(index,strVal);;
				} else if (CsjOracleColType.TYPE_NCLOB.equalsIgnoreCase(strColumnType)) {
					statement.setString(index, strVal);
				} else if (CsjOracleColType.TYPE_NVARCHAR2.equalsIgnoreCase(strColumnType)) {
					statement.setString(index,strVal);
				} else if (CsjOracleColType.TYPE_NUMBER.equalsIgnoreCase(strColumnType)) {
					statement.setBigDecimal(index,BigDecimal.valueOf(CmnStrUtils.getDoubleVal(strVal)));
				} else if (CsjOracleColType.TYPE_RAW.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjOracleColType.TYPE_TIMESTAMP.equalsIgnoreCase(strColumnType)) {
					String style = CsjConst.YYYY_MM_DD_SLASH;
					if (strVal.length()>10) {
						style = CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24;
					}
					java.sql.Timestamp dateTime = new java.sql.Timestamp((CmnDateUtil.getDate(strVal,style).getTime()));//Timestamp类

					statement.setTimestamp(index, dateTime);
				} else if (CsjOracleColType.TYPE_TIMESTAMP_6.equalsIgnoreCase(strColumnType)) {
					String style = CsjConst.YYYY_MM_DD_SLASH;
					if (strVal.length()>10) {
						style = CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24;
					}
					java.sql.Timestamp dateTime = new java.sql.Timestamp((CmnDateUtil.getDate(strVal,style).getTime()));//Timestamp类

					statement.setTimestamp(index, dateTime);
				} else if (CsjOracleColType.TYPE_TIMESTAMP_WITH_LOCAL_TIME_ZONE.equalsIgnoreCase(strColumnType)) {
					String style = CsjConst.YYYY_MM_DD_SLASH;
					if (strVal.length()>10) {
						style = CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24;
					}
					java.sql.Timestamp dateTime = new java.sql.Timestamp((CmnDateUtil.getDate(strVal,style).getTime()));//Timestamp类

					statement.setTimestamp(index, dateTime);

				} else if (CsjOracleColType.TYPE_TIMESTAMP_WITH_LOCAL_TIME_ZONE_6.equalsIgnoreCase(strColumnType)) {
					String style = CsjConst.YYYY_MM_DD_SLASH;
					if (strVal.length()>10) {
						style = CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24;
					}
					java.sql.Timestamp dateTime = new java.sql.Timestamp((CmnDateUtil.getDate(strVal,style).getTime()));//Timestamp类

					statement.setTimestamp(index, dateTime);

				} else if (CsjOracleColType.TYPE_TIMESTAMP_WITH_TIME_ZONE.equalsIgnoreCase(strColumnType)) {
					String style = CsjConst.YYYY_MM_DD_SLASH;
					if (strVal.length()>10) {
						style = CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24;
					}
					java.sql.Timestamp dateTime = new java.sql.Timestamp((CmnDateUtil.getDate(strVal,style).getTime()));//Timestamp类

					statement.setTimestamp(index, dateTime);
				} else if (CsjOracleColType.TYPE_TIMESTAMP_WITH_TIME_ZONE_6.equalsIgnoreCase(strColumnType)) {
					String style = CsjConst.YYYY_MM_DD_SLASH;
					if (strVal.length()>10) {
						style = CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24;
					}
					java.sql.Timestamp dateTime = new java.sql.Timestamp((CmnDateUtil.getDate(strVal,style).getTime()));//Timestamp类

					statement.setTimestamp(index, dateTime);
				} else if (CsjOracleColType.TYPE_VARCHAR2.equalsIgnoreCase(strColumnType)) {
					statement.setString(index,strVal);
				} else {
					statement.setString(index,strVal);
				}
			}
		}
	}
	/**
	 * @param paraList
	 * @param statement
	 * @throws SQLException
	 */
	public void setPsByPostgre(List<Object> paraList, PreparedStatement statement) throws Throwable {
		// SQL文パラメーターのセート
		if (paraList != null && paraList.size() > 0) {
			for (int i = 0; i < paraList.size(); i++) {
				int index = i+1;

				XlsTblPara xlsTblPara = (XlsTblPara) paraList.get(i);

				String strVal = xlsTblPara.getParaVal();

				if (CmnStrUtils.isEmpty(strVal))
				{
					statement.setObject(index, null);
					continue;
				}

				if (DbInfo.TABLE_COL_TYPE_NUM == xlsTblPara.getType()) {
					statement.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(strVal)));

				} else if (DbInfo.TABLE_COL_TYPE_DATE == xlsTblPara.getType()) {
					String style = CsjConst.YYYY_MM_DD_SLASH;
					java.sql.Timestamp dateTime = null;
						if (strVal.length()>10) {
							style = CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24;
						}
						dateTime = new java.sql.Timestamp((CmnDateUtil.getDate(strVal.replaceAll("-", "/"),style).getTime()));//Timestamp类
					statement.setTimestamp(index, dateTime);
				} else {
					statement.setObject(index, strVal);
				}
			}
		}
	}
	/**
	 * @param paraList
	 * @param statement
	 * @throws SQLException
	 */
	public void setPsByMysql(List<Object> paraList, PreparedStatement statement) throws Throwable {
		// SQL文パラメーターのセート
		if (paraList != null && paraList.size() > 0) {
			for (int i = 0; i < paraList.size(); i++) {
				int index = i+1;

				XlsTblPara xlsTblPara = (XlsTblPara) paraList.get(i);

				String strVal = xlsTblPara.getParaVal();

				//String type = TblPara.getColTypeInfo(xlsTblPara);
				if (CmnStrUtils.isEmpty(strVal))
				{
					statement.setObject(index, null);
					continue;
				}

				if (DbInfo.TABLE_COL_TYPE_NUM == xlsTblPara.getType()) {
					statement.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(strVal)));

				} else if (DbInfo.TABLE_COL_TYPE_DATE == xlsTblPara.getType()) {
					String style = CsjConst.YYYY_MM_DD_SLASH;
					java.sql.Timestamp dateTime = null;
						if (strVal.length()>10) {
							style = CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24;
						}
						dateTime = new java.sql.Timestamp((CmnDateUtil.getDate(strVal.replaceAll("-", "/"),style).getTime()));//Timestamp类
					statement.setTimestamp(index, dateTime);

				} else {
					statement.setObject(index, strVal);
				}
			}
		}
	}
	/**
	 * @param paraList
	 * @param statement
	 * @throws SQLException
	 */
	public void setPsBySqlLite(List<Object> paraList, PreparedStatement statement) throws Throwable {
		// SQL文パラメーターのセート
		if (paraList != null && paraList.size() > 0) {
			for (int i = 0; i < paraList.size(); i++) {
				int index = i+1;

				XlsTblPara xlsTblPara = (XlsTblPara) paraList.get(i);

				String strVal = xlsTblPara.getParaVal();

				//String type = TblPara.getColTypeInfo(xlsTblPara);
				if (CmnStrUtils.isEmpty(strVal))
				{
					statement.setObject(index, null);
					continue;
				}

				if (DbInfo.TABLE_COL_TYPE_NUM == xlsTblPara.getType()) {
					statement.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(strVal)));

				} else if (DbInfo.TABLE_COL_TYPE_DATE == xlsTblPara.getType()) {
					String style = CsjConst.YYYY_MM_DD_MINUS;
					java.sql.Timestamp dateTime = null;
						if (strVal.length()>10) {
							style = CsjConst.YYYY_MM_DD_HH_MM_SS_MINUS_24_SSS;
						}
//						dateTime = new java.sql.Timestamp((CsjDateUtil.getDate(strVal.replaceAll("/", "-"),style).getTime()));//Timestamp类
						Date d = CmnDateUtil.getDate(strVal.replaceAll("/", "-"),style);
						String strDate = CmnDateUtil.getFormatDateTime(d, style);
					statement.setObject(index, strDate);

				} else {
					statement.setObject(index, strVal);
				}
			}
		}
	}
	/**
	 * @param paraList
	 * @param statement
	 * @throws SQLException
	 */
	public void setPsByDb2(List<Object> paraList, PreparedStatement statement) throws Throwable {
		// SQL文パラメーターのセート
		if (paraList != null && paraList.size() > 0) {
			for (int i = 0; i < paraList.size(); i++) {
				int index = i+1;

				XlsTblPara xlsTblPara = (XlsTblPara) paraList.get(i);

				String strVal = xlsTblPara.getParaVal();

			//	String type = TblPara.getColTypeInfo(xlsTblPara);

				if (CmnStrUtils.isEmpty(strVal))
				{
					statement.setObject(index, null);
					continue;
				}

				if (DbInfo.TABLE_COL_TYPE_NUM == xlsTblPara.getType()) {
					statement.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(strVal)));

				} else if (DbInfo.TABLE_COL_TYPE_DATE == xlsTblPara.getType()) {
					String style = CsjConst.YYYY_MM_DD_SLASH;
					java.sql.Timestamp dateTime = null;
						if (strVal.length()>10) {
							style = CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24;
						}
						dateTime = new java.sql.Timestamp((CmnDateUtil.getDate(strVal.replaceAll("-", "/"),style).getTime()));//Timestamp类

					statement.setTimestamp(index, dateTime);

				} else {
					statement.setObject(index, strVal);
				}
			}
		}
	}
	/**
	 * @param paraList
	 * @param statement
	 * @throws SQLException
	 */
	public void setPsBySybase(List<Object> paraList, PreparedStatement statement) throws Throwable {
		// SQL文パラメーターのセート
		if (paraList != null && paraList.size() > 0) {
			for (int i = 0; i < paraList.size(); i++) {
				int index = i+1;

				XlsTblPara xlsTblPara = (XlsTblPara) paraList.get(i);

				String strVal = xlsTblPara.getParaVal();

				//String type = TblPara.getColTypeInfo(xlsTblPara);

				if (CmnStrUtils.isEmpty(strVal))
				{
					String type = TblPara.getColTypeInfo(xlsTblPara);
					if (type == DbInfo.TABLE_COL_TYPE_STR_INFO) {
						statement.setNull(index, java.sql.Types.CHAR);
					} else if (type == DbInfo.TABLE_COL_TYPE_DATE_INFO){
						statement.setNull(index, java.sql.Types.DATE);
					} else if (type == DbInfo.TABLE_COL_TYPE_NUM_INFO) {
						statement.setNull(index, java.sql.Types.NUMERIC);
					}

					continue;
				}

				if (DbInfo.TABLE_COL_TYPE_NUM == xlsTblPara.getType()) {
					statement.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(strVal)));

				} else if (DbInfo.TABLE_COL_TYPE_DATE == xlsTblPara.getType()) {
					String style = CsjConst.YYYY_MM_DD_SLASH;
					java.sql.Timestamp dateTime = null;
						if (strVal.length()>10) {
							style = CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24;
						}
						dateTime = new java.sql.Timestamp((CmnDateUtil.getDate(strVal.replaceAll("-", "/"),style).getTime()));//Timestamp类
					statement.setTimestamp(index, dateTime);

				} else {
					statement.setObject(index, strVal);
				}
			}
		}
	}
	
	/**
	 * @param paraList
	 * @param statement
	 * @throws SQLException
	 */
	public void setPsBySqlServer(List<Object> paraList, PreparedStatement statement) throws SQLException {
		// SQL文パラメーターのセート
		if (paraList != null && paraList.size() > 0) {
			for (int i = 0; i < paraList.size(); i++) {

				int index = i+1;
				XlsTblPara xlsTblPara = (XlsTblPara) paraList.get(i);

				String strColumnType = xlsTblPara.getParaType();
				String strVal = xlsTblPara.getParaVal();

				if (CmnStrUtils.isEmpty(strVal)
						|| CsjSqlServerColType.TYPE_GEOGRAPHY.equalsIgnoreCase(strColumnType)
						|| CsjSqlServerColType.TYPE_GEOMETRY.equalsIgnoreCase(strColumnType)
						|| CsjSqlServerColType.TYPE_HIERARCHYID.equalsIgnoreCase(strColumnType)
						//|| CsjSqlServerColType.TYPE_TINYINT.equalsIgnoreCase(strColumnType)
				) {
					statement.setObject(index, null);
					continue;
				}

				// タイプ転換
				if (CsjSqlServerColType.TYPE_BIGINT.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_BINARY.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_BIT.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, (CmnStrUtils.toLowOrUpStr(strVal).equals("TRUE") ? 1:0));
				} else if (CsjSqlServerColType.TYPE_CHAR.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_DATE.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_DATETIME.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_DATETIME2.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_DATETIMEOFFSET.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_DECIMAL.equalsIgnoreCase(strColumnType)) {
					statement.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(strVal)));
				} else if (CsjSqlServerColType.TYPE_FLOAT.equalsIgnoreCase(strColumnType)) {
					statement.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(strVal)));
				} else if (CsjSqlServerColType.TYPE_GEOGRAPHY.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_GEOMETRY.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_HIERARCHYID.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_IMAGE.equalsIgnoreCase(strColumnType)) {

					statement.setBinaryStream(index,CmnStrUtils.String2ByteArrayInputStream(strVal));
				} else if (CsjSqlServerColType.TYPE_INT.equalsIgnoreCase(strColumnType)) {
					statement.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(strVal)));
				} else if (CsjSqlServerColType.TYPE_MONEY.equalsIgnoreCase(strColumnType)) {
					statement.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(strVal)));
				} else if (CsjSqlServerColType.TYPE_NCHAR.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_NTEXT.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_NUMERIC.equalsIgnoreCase(strColumnType)) {
					statement.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(strVal)));
				} else if (CsjSqlServerColType.TYPE_NVARCHAR.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_REAL.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_SMALLDATETIME.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_SMALLINT.equalsIgnoreCase(strColumnType)) {
					statement.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(strVal)));
				} else if (CsjSqlServerColType.TYPE_SMALLMONEY.equalsIgnoreCase(strColumnType)) {
					statement.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(strVal)));
				} else if (CsjSqlServerColType.TYPE_TEXT.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_TIME.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_TIMESTAMP.equalsIgnoreCase(strColumnType)) {
					statement.setBinaryStream(index,CmnStrUtils.String2ByteArrayInputStream(strVal));
				} else if (CsjSqlServerColType.TYPE_TINYINT.equalsIgnoreCase(strColumnType)) {
					statement.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(strVal)));
				} else if (CsjSqlServerColType.TYPE_UNIQUEIDENTIFIER.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_VARBINARY.equalsIgnoreCase(strColumnType)) {
					statement.setBinaryStream(index,CmnStrUtils.String2ByteArrayInputStream(strVal));
				} else if (CsjSqlServerColType.TYPE_VARCHAR.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else if (CsjSqlServerColType.TYPE_XML.equalsIgnoreCase(strColumnType)) {
					statement.setObject(index, strVal);
				} else {
					statement.setObject(index, strVal);
				}
			}
		}
	}

	/**
	 * コンネクションを設定する
	 *
	 * @param conn
	 *            コンネクション
	 */
	public void setConnection(Connection conn) {
		connection = conn;
	}
	/**
	 * 検索SQL文を実行し、結果レコードを返却する（1件のみ）
	 *
	 * @param strSQL
	 *            SQL文
	 * @param paraList
	 *            SQL文パラメーター
	 * @return 検索結果リスト
	 * @throws Throwable
	 */
	public String getContent(String strSQL, List<Object> paraList) throws Throwable {

		String retVal = "";
		// 結果レコードを初期化する
		HashMap<String, String> record = null;

		// 結果リストを取得する
		List<HashMap<String, String>> resultList = getRecordList(strSQL, paraList,1).getResultList();
		if ((resultList != null) && (resultList.size() > 0)) {
			for (java.util.Map.Entry<String, String> entry :resultList.get(0).entrySet() ) {
				CmnStrUtils.isNotEmpty(entry.getValue()) ;
				retVal=entry.getValue();
			}
		}

		// 実行結果を返す
		return retVal;
	}
	/**
	 * 検索SQL文を実行し、結果レコードを返却する（1件のみ）
	 *
	 * @param strSQL
	 *            SQL文
	 * @param paraList
	 *            SQL文パラメーター
	 * @return 検索結果リスト
	 * @throws Throwable
	 */
	public HashMap<String, String> getRecord(String strSQL, List<Object> paraList) throws Throwable {

		// 結果レコードを初期化する
		HashMap<String, String> record = null;

		// 結果リストを取得する
		List<HashMap<String, String>> resultList = getRecordList(strSQL, paraList,1).getResultList();
		if ((resultList != null) && (resultList.size() > 0)) {
			record = resultList.get(0);
		} else {
			record = new HashMap<String, String>();
		}

		// 実行結果を返す
		return record;
	}

	/**
	 * 検索SQL文を実行し、結果リストを返却する（複数件）
	 *
	 * @param strSQL
	 *            SQL文
	 * @param paraList
	 *            SQL文パラメーター
	 * @param dbXlsInfo
	 * @return 検索結果リスト
	 * @throws Throwable
	 */
	public CsjDbDataLst getRecordList(String strSQL, List<Object> paraList,int maxRowNum) throws Throwable {

		PreparedStatement statement= null;
		ResultSet rs = null;
		try {
			
			if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
				strSQL = strSQL.replaceAll("\"", "`");
			}
			//strSQL = strSQL.replaceAll(", \"BINARY_DOUBLE\", \"BINARY_FLOAT\", \"CBLOB\", \"CCLOB\" ", " ");
			//strSQL = "select CNUMBER from ALL_TYPE t  where 1=1  and rownum <= 60000";
			CmnLog.logger.debug("SQL = " + strSQL);
			for (int i = 0; i < paraList.size(); i++) {
				XlsTblPara xlsTblPara = (XlsTblPara) paraList.get(i);
				CmnLog.logger.debug("PARAM"+ i + ":"+ xlsTblPara.getParaVal());
			}

			
			// 結果リストを初期化する
			List<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
			Map<String, TableDbItem> csjLinkedMap = null;
			ResultSetMetaData meta = null;
			int iColumnCount =0; 
			if (connection != null) {

				// ステートメントを作成する
				statement = createPreStatement(strSQL);

				if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
					setPsByMysql(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
					setPsByOracle(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
					setPsByPostgre(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
					setPsBySqlServer(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_DB2.equals(dbType)) {
					setPsByDb2(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {
					setPsBySybase(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_SQLITE.equals(dbType)) {
					setPsBySqlLite(paraList, statement);
				} else {
					setPsByOracle(paraList, statement);
				}

//				paraList.clear();

				long begin = System.currentTimeMillis();

				// 問合せの実行
				rs = statement.executeQuery();

				long lasting = System.currentTimeMillis();

				costTime = "("+ CmnDateUtil.getMsHour(lasting - begin) + ")";

				// 最大列番号を取得する
				meta = rs.getMetaData();
				iColumnCount = meta.getColumnCount();
				int iHasMapLength = 16;
				
			
				// HashMap長さの設定
				if (iColumnCount > 16) {
					iHasMapLength = iColumnCount;
				}
				HashMap<String, String> map = null;

				long rowNum = 0;

				if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {

						if (maxRowNum >= 0 && maxRowNum <= rowNum) {
							overMaxRecord = CsjConst.SIGN_BIGER;
							break;
						}
						
						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						map.put(AutoDbToXls.COL_ID, String.format("%0"+XmlInfoXls.S_WIDTH + "d", ++rowNum));

						getDataMapForMysql(rs, meta, iColumnCount, map);

						// 当該行の内容を結果リストに格納する
						resultList.add(map);
						StaticClz.increseIndex();
						
					}
				} else if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {

						if (maxRowNum >= 0 && maxRowNum <= rowNum) {
							overMaxRecord = CsjConst.SIGN_BIGER;
							break;
						}
						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						map.put(AutoDbToXls.COL_ID, String.format("%0"+XmlInfoXls.S_WIDTH + "d", ++rowNum));

						getDataMapForOracle(rs, meta, iColumnCount, map);

						// 当該行の内容を結果リストに格納する
						resultList.add(map);
						StaticClz.increseIndex();
					}
				} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {

						if (maxRowNum >= 0 && maxRowNum <= rowNum) {
							overMaxRecord = CsjConst.SIGN_BIGER;
							break;
						}
						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						map.put(AutoDbToXls.COL_ID, String.format("%0"+XmlInfoXls.S_WIDTH + "d", ++rowNum));

						getDataMapForPostGre(rs, meta, iColumnCount, map);

						// 当該行の内容を結果リストに格納する
						resultList.add(map);
						StaticClz.increseIndex();
					}
				} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {

						if (maxRowNum >= 0 && maxRowNum <= rowNum) {
							overMaxRecord = CsjConst.SIGN_BIGER;
							break;
						}
						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						map.put(AutoDbToXls.COL_ID, String.format("%0"+XmlInfoXls.S_WIDTH + "d", ++rowNum));

						getDataMapForSqlServer(rs, meta, iColumnCount, map);

						// 当該行の内容を結果リストに格納する
						resultList.add(map);
						StaticClz.increseIndex();
					}
				} else if (DbInfo.STR_DB_TYPE_DB2.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {

						if (maxRowNum >= 0 && maxRowNum <= rowNum) {
							overMaxRecord = CsjConst.SIGN_BIGER;
							break;
						}
						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						map.put(AutoDbToXls.COL_ID, String.format("%0"+XmlInfoXls.S_WIDTH + "d", ++rowNum));

						getDataMapForDb2(rs, meta, iColumnCount, map);

						// 当該行の内容を結果リストに格納する
						resultList.add(map);
						StaticClz.increseIndex();
					}
				} else if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {

						if (maxRowNum >= 0 && maxRowNum <= rowNum) {
							overMaxRecord = CsjConst.SIGN_BIGER;
							break;
						}
						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						map.put(AutoDbToXls.COL_ID, String.format("%0"+XmlInfoXls.S_WIDTH + "d", ++rowNum));

						// TODOSAI
						getDataMapForDb2(rs, meta, iColumnCount, map);

						// 当該行の内容を結果リストに格納する
						resultList.add(map);
						StaticClz.increseIndex();
					}
				} else if (DbInfo.STR_DB_TYPE_SQLITE.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {

						if (maxRowNum >= 0 && maxRowNum <= rowNum) {
							overMaxRecord = CsjConst.SIGN_BIGER;
							break;
						}
						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						map.put(AutoDbToXls.COL_ID, String.format("%0"+XmlInfoXls.S_WIDTH + "d", ++rowNum));

						getDataMapForOracle(rs, meta, iColumnCount, map);

						// 当該行の内容を結果リストに格納する
						resultList.add(map);
						StaticClz.increseIndex();
					}
				} else {
					// 問合せ結果の表示
					while (rs.next()) {

						if (maxRowNum >= 0 && maxRowNum <= rowNum) {
							overMaxRecord = CsjConst.SIGN_BIGER;
							break;
						}
						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						map.put(AutoDbToXls.COL_ID, String.format("%0"+XmlInfoXls.S_WIDTH + "d", ++rowNum));

						getDataMapForOracle(rs, meta, iColumnCount, map);

						// 当該行の内容を結果リストに格納する
						resultList.add(map);
						StaticClz.increseIndex();
					}
				}
				if (CmnStrUtils.isEmpty(resultList)) {
					csjLinkedMap = new CsjLinkedMap<String, TableDbItem>();

					csjLinkedMap.put("", new TableDbItem("", "", DbInfo.TABLE_COL_TYPE_STR));
					for (int i = 1; i <= iColumnCount; i++) {
						// 項目キーを初期化する
						String strColumnKey = null;

						// 項目名を取得する
						String strColumnName = meta.getColumnName(i);
						// 項目別名を取得する
						String strColumnLabel = meta.getColumnLabel(i);
						// カラムのタイプを取得する
//						String strColumnType = meta.getColumnTypeName(i);

						// 項目キーを指定する
						if (CmnStrUtils.isNotEmpty(strColumnLabel)) {
							// 項目別名が存在する場合

							// 項目キーは項目別名を指定する
							strColumnKey = strColumnLabel;
						} else {
							// 項目別名が存在しない場合

							// 項目キーは項目名を指定する
							strColumnKey = strColumnName;
						}
						csjLinkedMap.put(strColumnKey, new TableDbItem(strColumnKey, "", DbInfo.TABLE_COL_TYPE_STR));
					}
				}
				// 結果セットをクローズ
				rs.close();
				// ステートメントをクローズ
				statement.close();

			} else {
				throw new SQLException("connection is unavailable");
			}
			
			// 実行結果を返す
			return new CsjDbDataLst(resultList,csjLinkedMap,costTime);
		} catch (Throwable e) {

			throw e;
		} finally {
			if (rs != null) {
				// 結果セットをクローズ
				rs.close();
			}
			if (statement != null) {
				// ステートメントをクローズ
				statement.close();
			}
		}
		

	}

	

	/**
	 * 検索SQL文を実行し、結果リストを返却する（複数件）
	 *
	 * @param strSQL
	 *            SQL文
	 * @param paraList
	 *            SQL文パラメーター
	 * @param dbXlsInfo
	 * @return 検索結果リスト
	 * @throws Throwable
	 */
	public long writeResultSet(String strSQL, List<Object> paraList,String filePath,XmlDbXlsInfoAll xmlDbXlsInfoAll, String ch,String newLine) throws Throwable {
		
		BufferedWriter bw = null;
		int rowNum = 0;
		int maxRowNum = CmnStrUtils.getIntVal(xmlDbXlsInfoAll.getXmlInfoXls().getMaxRow());
		PreparedStatement statement =null;
		ResultSet rs =null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filePath), xmlDbXlsInfoAll.getXmlInfoSql().getEncode()));
			if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
				strSQL = strSQL.replaceAll("\"", "`");
			}
			CmnLog.logger.debug("SQL = " + strSQL);
			for (int i = 0; i < paraList.size(); i++) {
				XlsTblPara xlsTblPara = (XlsTblPara) paraList.get(i);
				CmnLog.logger.debug("PARAM"+ i + ":"+ xlsTblPara.getParaVal());
			}

			if (connection != null) {

				// ステートメントを作成する
				statement = createPreStatement(strSQL);

				if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
					setPsByMysql(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
					setPsByOracle(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
					setPsByPostgre(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
					setPsBySqlServer(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_DB2.equals(dbType)) {
					setPsByDb2(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {
					setPsBySqlServer(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_SQLITE.equals(dbType)) {
					setPsBySqlLite(paraList, statement);
				} else {
					setPsByOracle(paraList, statement);
				}
				paraList.clear();

				// 問合せの実行
				rs = statement.executeQuery();

				// 最大列番号を取得する
				ResultSetMetaData meta = rs.getMetaData();
				int iColumnCount = meta.getColumnCount();
				int iHasMapLength = 16;

				// HashMap長さの設定
				if (iColumnCount > 16) {
					iHasMapLength = iColumnCount;
				}
				HashMap<String, String> map = null;

				if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {
						if (maxRowNum >= 0 && maxRowNum <= rowNum) {
							break;
						}
						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						getDataMapForMysql(rs, meta, iColumnCount, map);
						writeContent(bw, ch, newLine, map,CsjConst.EMPTY);
						rowNum++;
						StaticClz.increseIndex();
					}
				} else if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {
						if (maxRowNum >= 0 && maxRowNum <= rowNum) {
							break;
						}
						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						getDataMapForOracle(rs, meta, iColumnCount, map);
	
						writeContent(bw, ch, newLine, map,CsjConst.EMPTY);
						rowNum++;
						StaticClz.increseIndex();
					}
				} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {
						if (maxRowNum >= 0 && maxRowNum <= rowNum) {
							break;
						}
						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						getDataMapForPostGre(rs, meta, iColumnCount, map);
						writeContent(bw, ch, newLine, map,CsjConst.EMPTY);
						rowNum++;
						StaticClz.increseIndex();
					}
				} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {
						if (maxRowNum >= 0 && maxRowNum <= rowNum) {
							break;
						}
						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						getDataMapForSqlServer(rs, meta, iColumnCount, map);
						writeContent(bw, ch,newLine, map,CsjConst.EMPTY);
						rowNum++;
						StaticClz.increseIndex();
					}
				} else if (DbInfo.STR_DB_TYPE_DB2.equals(dbType)) {
					// 問合せ結果の表示
					while (maxRowNum >= 0 && maxRowNum <= rowNum) {
						if (maxRowNum <= rowNum) {
							break;
						}
						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						getDataMapForDb2(rs, meta, iColumnCount, map);
						writeContent(bw, ch,newLine, map,CsjConst.EMPTY);
						rowNum++;
						StaticClz.increseIndex();
					}
				} else if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {
						if (maxRowNum >= 0 && maxRowNum <= rowNum) {
							break;
						}
						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						// TODOSAI
						getDataMapForDb2(rs, meta, iColumnCount, map);
						writeContent(bw, ch,newLine, map,CsjConst.EMPTY);
						rowNum++;
						StaticClz.increseIndex();
					}
				} else if (DbInfo.STR_DB_TYPE_SQLITE.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {
						if (maxRowNum >= 0 && maxRowNum <= rowNum) {
							break;
						}
						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);
						// TODOSAI
						getDataMapForDb2(rs, meta, iColumnCount, map);
	
						writeContent(bw, ch, newLine, map,CsjConst.EMPTY);
						rowNum++;
						StaticClz.increseIndex();
					}
				} else {
					// 問合せ結果の表示
					while (rs.next()) {
						if (maxRowNum >= 0 && maxRowNum <= rowNum) {
							break;
						}
						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						getDataMapForOracle(rs, meta, iColumnCount, map);
						writeContent(bw, ch,newLine, map,CsjConst.EMPTY);
						rowNum++;
						StaticClz.increseIndex();
					}
				}

				bw.close();
				// 結果セットをクローズ
				rs.close();
				// ステートメントをクローズ
				statement.close();

			} else {
				throw new SQLException("connection is unavailable");
			}
		} catch (Throwable e) {

			throw e;
		} finally {
			if (bw != null) {
				bw.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (statement != null) {
				statement.close();
			}
		}
		return rowNum;
	}

	/**
	 * @param bw
	 * @param ch
	 * @param iColumnCount
	 * @param map
	 * @throws IOException
	 */
	private void writeContent(BufferedWriter bw, String ch,String newLine,
			HashMap<String, String> map, String preStr) throws IOException {
		int index = 0;
		StringBuffer sb = new StringBuffer(preStr);
		map.remove("XXXROWNUMXXX");
		
		int size = map.size();
		for (Entry<String, String> entry : map.entrySet()) {
			String val = entry.getValue();
			if (null == val) {
				// do nothing
			} else {
				sb.append(entry.getValue());
			}
			

			if (index+1 < size) {
				sb.append(ch);
			} else {
				sb.append(newLine);
				break;
			}
			index++;
		}
		bw.write(sb.toString());
		bw.flush();
	}
	
	/**
	 * @param rs
	 * @param meta
	 * @param iColumnCount
	 * @param map
	 * @throws Throwable
	 */
	public void getDataMapForPostGre(ResultSet rs, ResultSetMetaData meta, int iColumnCount, HashMap<String, String> map)
			throws Throwable {

		colTypeMap.clear();
		colTypeMap.put("0", "NUM");

		// 列番号毎に繰り返す
		for (int i = 1; i <= iColumnCount; i++) {

			// 項目キーを初期化する
			String strColumnKey = null;

			// 項目名を取得する
			String strColumnName = meta.getColumnName(i);
			// 項目別名を取得する
			String strColumnLabel = meta.getColumnLabel(i);
			// カラムのタイプを取得する
			String strColumnType = meta.getColumnTypeName(i);

			colTypeMap.put(String.valueOf(i), strColumnType);

			// 項目キーを指定する
			if (CmnStrUtils.isNotEmpty(strColumnLabel)) {
				// 項目別名が存在する場合

				// 項目キーは項目別名を指定する
				strColumnKey = strColumnLabel;
			} else {
				// 項目別名が存在しない場合

				// 項目キーは項目名を指定する
				strColumnKey = strColumnName;
			}

			String content = rs.getString(i);
			if (CmnStrUtils.isNotEmpty(content) && strColumnType.equalsIgnoreCase(CsjPostgreColType.TYPE_TIMESTAMP)) {
				content = CmnStrUtils.funReplace(content, "-", "/");
			}
			map.put(strColumnKey, content);

		}
	}

	/**
	 * @param rs
	 * @param meta
	 * @param iColumnCount
	 * @param map
	 * @throws Throwable
	 */
	public void getDataMapForDb2(ResultSet rs, ResultSetMetaData meta, int iColumnCount, HashMap<String, String> map)
			throws Throwable {

		colTypeMap.clear();
		colTypeMap.put("0", "NUM");

		// 列番号毎に繰り返す
		for (int i = 1; i <= iColumnCount; i++) {

			// 項目キーを初期化する
			String strColumnKey = null;

			// 項目名を取得する
			String strColumnName = meta.getColumnName(i);
			// 項目別名を取得する
			String strColumnLabel = meta.getColumnLabel(i);
			// カラムのタイプを取得する
			String strColumnType = meta.getColumnTypeName(i);

			colTypeMap.put(String.valueOf(i), strColumnType);

			// 項目キーを指定する
			if (CmnStrUtils.isNotEmpty(strColumnLabel)) {
				// 項目別名が存在する場合

				// 項目キーは項目別名を指定する
				strColumnKey = strColumnLabel;
			} else {
				// 項目別名が存在しない場合

				// 項目キーは項目名を指定する
				strColumnKey = strColumnName;
			}

			String content = rs.getString(i);
			if (CmnStrUtils.isNotEmpty(content) && strColumnType.equalsIgnoreCase(CsjPostgreColType.TYPE_TIMESTAMP)) {
				content = CmnStrUtils.funReplace(content, "-", "/");
				content = CmnStrUtils.getExcelDate(content);
			}
			map.put(strColumnKey, content);

		}
	}

	/**
		* 将CLOB转换成字串
		* @param clob
		* @return
		*/
	public static String convertClobToString(Clob clob) {
		String reString = "";
		try {
			Reader is = clob.getCharacterStream();// 得到流
			BufferedReader br = new BufferedReader(is);
			String s = br.readLine();
			StringBuffer sb = new StringBuffer();
			while (s != null) {
				sb.append(s);
				sb.append(CsjProcess.s_newLine);
				s = br.readLine();
			}
			reString = sb.toString().trim();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return reString;
	}

	/**
	 * @param rs
	 * @param meta
	 * @param iColumnCount
	 * @param map
	 * @throws Throwable
	 */
	public void getDataMapForOracle(ResultSet rs, ResultSetMetaData meta, int iColumnCount, HashMap<String, String> map)
			throws Throwable {

		colTypeMap.clear();
		colTypeMap.put("0", "NUM");

		// 列番号毎に繰り返す
		for (int i = 1; i <= iColumnCount; i++) {

			// 項目キーを初期化する
			String strColumnKey = null;

			// 項目名を取得する
			String strColumnName = meta.getColumnName(i);
			// 項目別名を取得する
			String strColumnLabel = meta.getColumnLabel(i);
			// カラムのタイプを取得する
			String strColumnType = meta.getColumnTypeName(i);

			colTypeMap.put(String.valueOf(i), strColumnType);

			// 項目キーを指定する
			if (CmnStrUtils.isNotEmpty(strColumnLabel)) {
				// 項目別名が存在する場合

				// 項目キーは項目別名を指定する
				strColumnKey = strColumnLabel;
			} else {
				// 項目別名が存在しない場合

				// 項目キーは項目名を指定する
				strColumnKey = strColumnName;
			}

			try {
				if (rs.getObject(i) == null) {

					map.put(strColumnKey, null);
					continue;
				} else if ((CsjOracleColType.TYPE_LONG.equalsIgnoreCase(strColumnType)
						|| CsjOracleColType.TYPE_LONG_RAW.equalsIgnoreCase(strColumnType))) {
					map.put(strColumnKey, null);
					continue;
//					throw new Exception(CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_I_0000086));
				}
			} catch (Exception e) {
				map.put(strColumnKey, null);
				continue;
			}

			// タイプ転換
			if (CsjOracleColType.TYPE_BINARY_DOUBLE.equalsIgnoreCase(strColumnType)) {

				map.put(strColumnKey, rs.getString(i));
			} else if (CsjOracleColType.TYPE_BLOB.equalsIgnoreCase(strColumnType)) {
//
//              InputStreamReader read = new InputStreamReader(blob.getBinaryStream()) ;
//
//              StringBuffer sb = new StringBuffer();
//              char[] charbuf = new char[4096];
//              for (int index = read.read(charbuf); index > 0; index = read.read(charbuf)) {
//                  sb.append(charbuf, 0, index);
//              }
//              read.close();

				java.sql.Blob blob = rs.getBlob(i);
				long nLen = blob.length();

				int nSize = (int) nLen;

				byte[] data = new byte[nSize];
				InputStream inStream = blob.getBinaryStream();

				inStream.read(data);

				inStream.close();
				StringBuffer sb = new StringBuffer();
				for (byte b : data) {
					sb.append((char)(b));
				}

				map.put(strColumnKey, sb.toString());
			} else if (CsjOracleColType.TYPE_CLOB.equalsIgnoreCase(strColumnType)) {
				java.sql.Clob clob = rs.getClob(i);

				map.put(strColumnKey, convertClobToString(clob));
//				long nLen = clob.length();
//
//				int nSize = (int) nLen;
//
//				byte[] data = new byte[nSize];
//				InputStream inStream = clob.getAsciiStream();
//
//				inStream.read(data);
//
//				inStream.close();
//				StringBuffer sb = new StringBuffer();
//				for (byte b : data) {
//					sb.append((char)(b));
//				}
//
//				map.put(strColumnKey, sb.toString());
			} else if (CsjOracleColType.TYPE_CHAR.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjOracleColType.TYPE_DATE.equalsIgnoreCase(strColumnType)) {
				String content = rs.getObject(i).toString().replaceAll("-", "/");
				content = CmnStrUtils.getExcelDate(content);
				map.put(strColumnKey, content);

			} else if (CsjOracleColType.TYPE_INTERVALDS.equalsIgnoreCase(strColumnType)) {

				map.put(strColumnKey, rs.getString(i));
			} else if (CsjOracleColType.TYPE_INTERVALYM.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjOracleColType.TYPE_LONG.equalsIgnoreCase(strColumnType)
					|| CsjOracleColType.TYPE_LONG_RAW.equalsIgnoreCase(strColumnType)) {

				//廃棄類型
				java.io.Reader reader = (java.io.Reader)rs.getCharacterStream(i);
				java.io.BufferedReader bufReader = new java.io.BufferedReader(reader);
				StringBuffer strBuf = new StringBuffer();
				String line;
				while ((line = bufReader.readLine()) != null) {
				strBuf.append(line);
				strBuf.append("\r\n");
				}
				bufReader.close();

				rs.getObject(i);
				//map.put(strColumnKey, rs.getByte(i));

			} else if (CsjOracleColType.TYPE_NCLOB.equalsIgnoreCase(strColumnType)) {
				java.sql.NClob nclob = rs.getNClob(i);
				long nLen = nclob.length();

				int nSize = (int) nLen;

				byte[] data = new byte[nSize];
				InputStream inStream = nclob.getAsciiStream();

				inStream.read(data);

				inStream.close();
				StringBuffer sb = new StringBuffer();
				for (byte b : data) {
					sb.append((char)(b));
				}

				map.put(strColumnKey, sb.toString());
			} else if (CsjOracleColType.TYPE_NVARCHAR2.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjOracleColType.TYPE_NUMBER.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getBigDecimal(i).toPlainString());
			} else if (CsjOracleColType.TYPE_RAW.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjOracleColType.TYPE_TIMESTAMP.equalsIgnoreCase(strColumnType)) {
				String content = rs.getObject(i).toString().replaceAll("-", "/");
				content = CmnStrUtils.getExcelDate(content);
				map.put(strColumnKey, content);
			} else if (CsjOracleColType.TYPE_TIMESTAMP_WITH_LOCAL_TIME_ZONE.equalsIgnoreCase(strColumnType)) {
				String content = rs.getObject(i).toString().replaceAll("-", "/");
				content = CmnStrUtils.getExcelDate(content);
				map.put(strColumnKey, content);

			} else if (CsjOracleColType.TYPE_TIMESTAMP_WITH_TIME_ZONE.equalsIgnoreCase(strColumnType)) {
				String content = rs.getObject(i).toString().replaceAll("-", "/");
				content = CmnStrUtils.getExcelDate(content);
				map.put(strColumnKey,  content);
			} else if (CsjOracleColType.TYPE_VARCHAR2.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else {
				map.put(strColumnKey, rs.getString(i));
			}
		}
	}

	/**
	 * @param rs
	 * @param meta
	 * @param iColumnCount
	 * @param map
	 * @throws Throwable
	 */
	public void getDataMapForMysql(ResultSet rs, ResultSetMetaData meta, int iColumnCount, HashMap<String, String> map)
			throws Throwable {

		colTypeMap.clear();
		colTypeMap.put("0", "NUM");

		// 列番号毎に繰り返す
		for (int i = 1; i <= iColumnCount; i++) {

			// 項目キーを初期化する
			String strColumnKey = null;

			// 項目名を取得する
			String strColumnName = meta.getColumnName(i);
			// 項目別名を取得する
			String strColumnLabel = meta.getColumnLabel(i);
			// カラムのタイプを取得する
			String strColumnType = meta.getColumnTypeName(i);

			colTypeMap.put(String.valueOf(i), strColumnType);

			// 項目キーを指定する
			if (CmnStrUtils.isNotEmpty(strColumnLabel)) {
				// 項目別名が存在する場合

				// 項目キーは項目別名を指定する
				strColumnKey = strColumnLabel;
			} else {
				// 項目別名が存在しない場合

				// 項目キーは項目名を指定する
				strColumnKey = strColumnName;
			}

			try {
				if (rs.getObject(i) == null) {

					map.put(strColumnKey, null);
					continue;
				} else if ((CsjMySqlColType.TYPE_LONG.equalsIgnoreCase(strColumnType)
						|| CsjMySqlColType.TYPE_LONG_RAW.equalsIgnoreCase(strColumnType))) {
					map.put(strColumnKey, null);
					continue;
//					throw new Exception(CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_I_0000086));
				}
			} catch (Exception e) {
				map.put(strColumnKey, null);
				continue;
			}

			// タイプ転換
			if (CsjMySqlColType.TYPE_BINARY_DOUBLE.equalsIgnoreCase(strColumnType)) {

				map.put(strColumnKey, rs.getString(i));
			} else if (CsjMySqlColType.TYPE_BLOB.equalsIgnoreCase(strColumnType)) {
//
//              InputStreamReader read = new InputStreamReader(blob.getBinaryStream()) ;
//
//              StringBuffer sb = new StringBuffer();
//              char[] charbuf = new char[4096];
//              for (int index = read.read(charbuf); index > 0; index = read.read(charbuf)) {
//                  sb.append(charbuf, 0, index);
//              }
//              read.close();

				java.sql.Blob blob = rs.getBlob(i);
				long nLen = blob.length();

				int nSize = (int) nLen;

				byte[] data = new byte[nSize];
				InputStream inStream = blob.getBinaryStream();

				inStream.read(data);

				inStream.close();
				StringBuffer sb = new StringBuffer();
				for (byte b : data) {
					sb.append((char)(b));
				}

				map.put(strColumnKey, sb.toString());
			} else if (CsjMySqlColType.TYPE_CLOB.equalsIgnoreCase(strColumnType)) {
				java.sql.Clob clob = rs.getClob(i);

				map.put(strColumnKey, convertClobToString(clob));
//				long nLen = clob.length();
//
//				int nSize = (int) nLen;
//
//				byte[] data = new byte[nSize];
//				InputStream inStream = clob.getAsciiStream();
//
//				inStream.read(data);
//
//				inStream.close();
//				StringBuffer sb = new StringBuffer();
//				for (byte b : data) {
//					sb.append((char)(b));
//				}
//
//				map.put(strColumnKey, sb.toString());
			} else if (CsjMySqlColType.TYPE_CHAR.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjMySqlColType.TYPE_DATE.equalsIgnoreCase(strColumnType)) {
				String content = rs.getObject(i).toString().replaceAll("-", "/");
				CmnStrUtils.getExcelDate(content);
				map.put(strColumnKey, content);

			} else if (CsjMySqlColType.TYPE_INTERVALDS.equalsIgnoreCase(strColumnType)) {

				map.put(strColumnKey, rs.getString(i));
			} else if (CsjMySqlColType.TYPE_INTERVALYM.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjMySqlColType.TYPE_LONG.equalsIgnoreCase(strColumnType)
					|| CsjMySqlColType.TYPE_LONG_RAW.equalsIgnoreCase(strColumnType)) {

				//廃棄類型
				java.io.Reader reader = (java.io.Reader)rs.getCharacterStream(i);
				java.io.BufferedReader bufReader = new java.io.BufferedReader(reader);
				StringBuffer strBuf = new StringBuffer();
				String line;
				while ((line = bufReader.readLine()) != null) {
				strBuf.append(line);
				strBuf.append("\r\n");
				}
				bufReader.close();

				rs.getObject(i);
				//map.put(strColumnKey, rs.getByte(i));

			} else if (CsjMySqlColType.TYPE_NCLOB.equalsIgnoreCase(strColumnType)) {
				java.sql.NClob nclob = rs.getNClob(i);
				long nLen = nclob.length();

				int nSize = (int) nLen;

				byte[] data = new byte[nSize];
				InputStream inStream = nclob.getAsciiStream();

				inStream.read(data);

				inStream.close();
				StringBuffer sb = new StringBuffer();
				for (byte b : data) {
					sb.append((char)(b));
				}

				map.put(strColumnKey, sb.toString());
			} else if (CsjMySqlColType.TYPE_NVARCHAR2.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjMySqlColType.TYPE_NUMBER.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getBigDecimal(i).toPlainString());
			} else if (CsjMySqlColType.TYPE_RAW.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjMySqlColType.TYPE_TIMESTAMP.equalsIgnoreCase(strColumnType)) {
				String content = rs.getObject(i).toString().replaceAll("-", "/");
				content = CmnStrUtils.getExcelDate(content);
				map.put(strColumnKey, content);
			} else if (CsjMySqlColType.TYPE_TIMESTAMP_WITH_LOCAL_TIME_ZONE.equalsIgnoreCase(strColumnType)) {
				String content = rs.getObject(i).toString().replaceAll("-", "/");
				content = CmnStrUtils.getExcelDate(content);
				map.put(strColumnKey, content);

			} else if (CsjMySqlColType.TYPE_TIMESTAMP_WITH_TIME_ZONE.equalsIgnoreCase(strColumnType)) {
				String content = rs.getObject(i).toString().replaceAll("-", "/");
				content = CmnStrUtils.getExcelDate(content);
				map.put(strColumnKey,  content);
			} else if (CsjMySqlColType.TYPE_VARCHAR2.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			}  else if (CsjOracleColType.TYPE_TIME.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getObject(i).toString());
			}  else if (CsjOracleColType.TYPE_YEAR.equalsIgnoreCase(strColumnType)) {
				
				Date date = (Date)rs.getObject(i);
				map.put(strColumnKey, CmnDateUtil.getYearByDate(date));
			} else {
				map.put(strColumnKey, rs.getString(i));
			}
		}
	}
	/**
	 * @param rs
	 * @param meta
	 * @param iColumnCount
	 * @param map
	 * @throws Throwable
	 */
	public void getDataMapForSqlServer(ResultSet rs, ResultSetMetaData meta, int iColumnCount, HashMap<String, String> map)
			throws Throwable {

		colTypeMap.clear();
		colTypeMap.put("0", "NUM");
		// 列番号毎に繰り返す
		for (int i = 1; i <= iColumnCount; i++) {

			// 項目キーを初期化する
			String strColumnKey = null;

			// 項目名を取得する
			String strColumnName = meta.getColumnName(i);
			// 項目別名を取得する
			String strColumnLabel = meta.getColumnLabel(i);
			// カラムのタイプを取得する
			String strColumnType = meta.getColumnTypeName(i);

			colTypeMap.put(String.valueOf(i), strColumnType);

			// 項目キーを指定する
			if (CmnStrUtils.isNotEmpty(strColumnLabel)) {
				// 項目別名が存在する場合

				// 項目キーは項目別名を指定する
				strColumnKey = strColumnLabel;
			} else {
				// 項目別名が存在しない場合

				// 項目キーは項目名を指定する
				strColumnKey = strColumnName;
			}


			try {

				if (rs.getObject(i) == null) {
					map.put(strColumnKey, null);
					continue;
				}
			} catch (Exception e) {
				map.put(strColumnKey, null);
				continue;
			}
			// タイプ転換
			if (CsjSqlServerColType.TYPE_BIGINT.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, String.valueOf(rs.getLong(i)));
			} else if (CsjSqlServerColType.TYPE_BINARY.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_BIT.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getObject(i).toString());
			} else if (CsjSqlServerColType.TYPE_CHAR.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_DATE.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_DATETIME.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_DATETIME2.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_DATETIMEOFFSET.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_DECIMAL.equalsIgnoreCase(strColumnType)) {
				if (rs.getBigDecimal(i).compareTo(new BigDecimal(0))==0) {
					map.put(strColumnKey, "0");
				} else {
					map.put(strColumnKey, rs.getString(i));
				}
			}  else if (CsjSqlServerColType.TYPE_NUMERIC.equalsIgnoreCase(strColumnType)) {
				if (rs.getBigDecimal(i).compareTo(new BigDecimal(0))==0) {
					map.put(strColumnKey, "0");
				} else {
					map.put(strColumnKey, rs.getString(i));
				}

			} else if (CsjSqlServerColType.TYPE_FLOAT.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_GEOGRAPHY.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_GEOMETRY.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_HIERARCHYID.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_IMAGE.equalsIgnoreCase(strColumnType)) {
//                InputStream in=rs.getBinaryStream("pic");
//                in.read(b);
//                File f=new File("D:/3.jpg");
//                FileOutputStream out=new FileOutputStream(f);
//                out.write(b, 0, b.length);
//                out.close();

//
//                InputStream stream = rs.getBinaryStream(i);
//                byte[] in = new byte[1];
//                StringBuffer sb = new StringBuffer();
//                while (stream.read(in) != -1) {
//                    sun.io.ByteToCharConverter converter = ByteToCharConverter.getConverter(CsjFileConst.ENCODE_UTF_8);
//                    char c[] = converter.convertAll(in);
//                    sb.append(c);
//                }
				map.put(strColumnKey, CmnStrUtils.inputStream2String(rs.getBinaryStream(i)));
			} else if (CsjSqlServerColType.TYPE_INT.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_MONEY.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_NCHAR.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_NTEXT.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_NVARCHAR.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_REAL.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_SMALLDATETIME.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_SMALLINT.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_SMALLMONEY.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_TEXT.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_TIME.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_TIMESTAMP.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, CmnStrUtils.inputStream2String(rs.getBinaryStream(i)));
			} else if (CsjSqlServerColType.TYPE_TINYINT.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_UNIQUEIDENTIFIER.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_VARBINARY.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, CmnStrUtils.inputStream2String(rs.getBinaryStream(i)));
			} else if (CsjSqlServerColType.TYPE_VARCHAR.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else if (CsjSqlServerColType.TYPE_XML.equalsIgnoreCase(strColumnType)) {
				map.put(strColumnKey, rs.getString(i));
			} else {
				map.put(strColumnKey, rs.getString(i));
			}
		}
	}
	/**
	 * 検索SQL文を実行し、結果リストを返却する（複数件）
	 *
	 * @param strSQL
	 *            SQL文
	 * @param paraList
	 *            SQL文パラメーター
	 * @return 検索結果リスト
	 * @throws SQLException
	 *             SQL例外
	 */
	public List<List<String>> getPageCountRecordList(String strSQL, List<String> paraList, int iPageCount)
			throws SQLException {

		CmnLog.logger.info( "SQL = " + strSQL);

		CmnLog.logger.info("PARAM = " + paraList);

		// 結果リストを初期化する
		List<List<String>> resultList = new ArrayList<List<String>>();

		if (connection != null) {

			// ステートメントを作成する
			PreparedStatement statement = createPreStatement(strSQL);

			// SQL文パラメーターのセート
			if (paraList != null && paraList.size() > 0) {
				for (int i = 0; i < paraList.size(); i++) {
					statement.setString(
							i + 1,
							paraList.get(i).toString());

				}
			}
			// 問合せの実行
			ResultSet rs = statement.executeQuery();

			// 最大列番号を取得する
			ResultSetMetaData meta = rs.getMetaData();
			int iColumnCount = meta.getColumnCount();
			List<String> dataList = null;
			// 問合せ結果の表示
			while (rs.next()) {

				// 各行の内容を格納するために、マップを初期化する
				dataList = new ArrayList<String>();

				// 列番号毎に繰り返す
				for (int i = 1; i <= iColumnCount; i++) {

					// カラムのタイプを取得する
					String strColumnType = meta.getColumnTypeName(i);

					// タイプ転換
//                    if (CsjOracleColType.TYPE_CHAR.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(rs.getString(i));
//                    } else if (CsjOracleColType.TYPE_VARCHAR.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(rs.getString(i));
//                    } else if (CsjOracleColType.TYPE_LONGVARCHAR.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(rs.getString(i));
//                    } else if (CsjOracleColType.TYPE_NUMERIC.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(CsjStrUtils.convertString(rs.getBigDecimal(i)));
//                    } else if (CsjOracleColType.TYPE_DECIMAL.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(CsjStrUtils.convertString(rs.getBigDecimal(i)));
//                    } else if (CsjOracleColType.TYPE_BIT.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(CsjStrUtils.convertString(rs.getBoolean(i)));
//                    } else if (CsjOracleColType.TYPE_TINYINT.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(CsjStrUtils.convertString(rs.getByte(i)));
//                    } else if (CsjOracleColType.TYPE_SMALLINT.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(CsjStrUtils.convertString(rs.getShort(i)));
//                    } else if (CsjOracleColType.TYPE_INTEGER.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(CsjStrUtils.convertString(rs.getInt(i)));
//                    } else if (CsjOracleColType.TYPE_BIGINT.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(CsjStrUtils.convertString(rs.getLong(i)));
//                    } else if (CsjOracleColType.TYPE_REAL.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(CsjStrUtils.convertString(rs.getFloat(i)));
//                    } else if (CsjOracleColType.TYPE_FLOAT.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(CsjStrUtils.convertString(rs.getDouble(i)));
//                    } else if (CsjOracleColType.TYPE_DOUBLE.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(CsjStrUtils.convertString(rs.getDouble(i)));
//                    } else if (CsjOracleColType.TYPE_BINARY.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(CsjStrUtils.convertString(rs.getBytes(i)));
//                    } else if (CsjOracleColType.TYPE_VARBINARY.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(CsjStrUtils.convertString(rs.getBytes(i)));
//                    } else if (CsjOracleColType.TYPE_LONGVARBINARY.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(CsjStrUtils.convertString(rs.getBytes(i)));
//                    } else if (CsjOracleColType.TYPE_DATE.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(CsjStrUtils.convertString(rs.getDate(i)));
//                    } else if (CsjOracleColType.TYPE_TIME.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(CsjStrUtils.convertString(rs.getTime(i)));
//                    } else if (CsjOracleColType.TYPE_TIMESTAMP.equalsIgnoreCase(strColumnType)) {
//                        dataList.add(CsjStrUtils.convertString(rs.getTimestamp(i)));
//                    } else {
//                        dataList.add(CsjStrUtils.convertString(rs.getObject(i)));
//                    }
				}

				// 当該行の内容を結果リストに格納する
				resultList.add(dataList);
			}

			// 結果セットをクローズ
			rs.close();
			// ステートメントをクローズ
			statement.close();

		} else {
			throw new SQLException("connection is unavailable");
		}

		// 実行結果を返す
		return resultList;
	}

	/**
	 * SQL文により、PreparedStatementを作成する
	 *
	 * @param strSQL
	 *            SQL文
	 * @return PreparedStatement
	 * @throws SQLException
	 *             SQL例外
	 */
	public PreparedStatement createPreStatement(String strSQL) throws SQLException {

		// ステートメントを初期化する
		PreparedStatement preStatement = null;

		// コンネクションがNULLではない場合
		if (connection != null) {

			// ステートメント作成
			preStatement = connection.prepareStatement(strSQL,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
				preStatement.setFetchSize(Integer.MIN_VALUE);
			} else {
				preStatement.setFetchSize(FETCH_SIZE);
			}
			

		} else {
			throw new SQLException("connection is unavailable");
		}

		// ステートメントを返却する
		return preStatement;
	}

	/**
	 * SQL文により、PreparedStatementを作成する
	 *
	 * @param strSQL
	 *            SQL文
	 * @return PreparedStatement
	 * @throws SQLException
	 *             SQL例外
	 */
	public PreparedStatement createPreStatementForwardReadOnly(String strSQL) throws SQLException {

		// ステートメントを初期化する
		PreparedStatement preStatement = null;

		// コンネクションがNULLではない場合
		if (connection != null) {

			// ステートメント作成
			preStatement = connection.prepareStatement(strSQL,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);

		} else {
			throw new SQLException("connection is unavailable");
		}

		// ステートメントを返却する
		return preStatement;
	}
	/**
	 * SQL文により、Statementを作成する
	 *
	 * @throws SQLException
	 *             SQL例外
	 * @return Statement
	 */
	public Statement createStatementForward() throws SQLException {

		// ステートメントを初期化する
		Statement statement = null;

		// コンネクションがNULLではない場合
		if (connection != null) {

			// ステートメント作成
			statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
		} else {

			throw new SQLException("connection is unavailable");
		}

		// ステートメントを返却する
		return statement;
	}
	
	/**
	 * SQL文により、Statementを作成する
	 *
	 * @throws SQLException
	 *             SQL例外
	 * @return Statement
	 */
	public Statement createStatement() throws SQLException {

		// ステートメントを初期化する
		Statement statement = null;

		// コンネクションがNULLではない場合
		if (connection != null) {

			// ステートメント作成
			statement = connection.createStatement();
		} else {

			throw new SQLException("connection is unavailable");
		}

		// ステートメントを返却する
		return statement;
	}

	public void executeBatch(String strSQL) throws Throwable {
		Statement pstmt = this.createStatement();
		pstmt.addBatch(strSQL);
		pstmt.executeBatch();
		this.commit();
	}

	/**
	 * @return the colTypeMap
	 */
	public LinkedHashMap<String, String> getColTypeMap() {
		return colTypeMap;
	}

	/**
	 * @param colTypeMap the colTypeMap to set
	 */
	public void setColTypeMap(LinkedHashMap<String, String> colTypeMap) {
		this.colTypeMap = colTypeMap;
	}

	/**
	 * @return the strDate
	 */
	public String getStrDate() {
		return strDate;
	}

	/**
	 * @param strDate the strDate to set
	 */
	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	/**
	 * @return the dbStr
	 */
	public String getDbStr() {
		return dbStr;
	}

	/**
	 * @param dbStr the dbStr to set
	 */
	public void setDbStr(String dbStr) {
		this.dbStr = dbStr;
	}

	/**
	 * @return the dataBaseVer
	 */
	public String getDataBaseVer() {
		return dataBaseVer;
	}

	/**
	 * @param dataBaseVer the dataBaseVer to set
	 */
	public void setDataBaseVer(String dataBaseVer) {
		this.dataBaseVer = dataBaseVer;
	}

	/**
	 * @return the sqlInsertLog
	 */
	public String getSqlInsertLog() {
		return sqlInsertLog;
	}

	/**
	 * @param sqlInsertLog the sqlInsertLog to set
	 */
	public void setSqlInsertLog(String sqlInsertLog) {
		this.sqlInsertLog = sqlInsertLog;
	}

	/**
	 * @return the preStatemetMap
	 */
	public Map<String, PreparedStatement> getPreStatemetMap() {
		return preStatemetMap;
	}

	/**
	 * @param preStatemetMap the preStatemetMap to set
	 */
	public void setPreStatemetMap(Map<String, PreparedStatement> preStatemetMap) {
		this.preStatemetMap = preStatemetMap;
	}

	/**
	 * @param dbXlsInfo
	 * @throws Throwable
	 */
	public String getInsertLogSql(XmlDbXlsInfoAll xmlDbXlsInfoAll) throws Throwable {
		CmnLog.logger.debug("CsjDBAccess.getInsertLogSql(String tableNm, String delStr,DbInfo dbInfo) begin");
		StringBuffer logSql = new StringBuffer();

		try {
			String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
			if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
				logSql.append("insert into "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+"`"+DbInfo.S_LOG_TABLE+"` (`ID`, `MAC`, `CMPUTER_INFO`, `EXE_TABLE_NM`, `EXE_SQL`) values (");
			} else {
				logSql.append("insert into "+xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot()+"\""+DbInfo.S_LOG_TABLE+"\" (\"ID\", \"MAC\", \"CMPUTER_INFO\", \"EXE_TABLE_NM\", \"EXE_SQL\") values (");
			}

			logSql.append("?");
			logSql.append(", '");
			logSql.append(CsjProcess.s_local_inet_addr);
			logSql.append("', '");
			logSql.append(CsjProcess.s_db_log_pc_info);
			
			logSql.append("', ");
			logSql.append("?");
			logSql.append(", ");
			logSql.append("?");
			logSql.append(") ");
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}

		CmnLog.logger.debug("CsjDBAccess.getInsertLogSql(String tableNm, String delStr,DbInfo dbInfo) end");
		return logSql.toString();
	}

	/**
	 * @param paraList 
	 * @param selectSql
	 * @param xlsRecKeySet
	 * @param keySet 
	 * @param s_log5j
	 * @throws Throwable 
	 */
	public long[] outputInsertRecord(String strSQL,
			List<Object> paraList, Set<String> xlsRecKeySet, Set<String> keySet, String keych,String ch,BufferedWriter bw) throws Throwable {
		long retVal[] = {0,0};
		
//		sb.append(CsjDbToolsMsg.coreMsgMap
//				.get(CsjDbToolsMsg.MSG_I_0000071));
		
		long cnt = 0;
		long insCnt = 0;
		PreparedStatement statement = null;
		ResultSet rs= null;
		try {
			if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
				strSQL = strSQL.replaceAll("\"", "`");
			}
			CmnLog.logger.debug("SQL = " + strSQL);
			for (int i = 0; i < paraList.size(); i++) {
				XlsTblPara xlsTblPara = (XlsTblPara) paraList.get(i);
				CmnLog.logger.debug("PARAM"+ i + ":"+ xlsTblPara.getParaVal());
			}

			if (connection != null) {

				// ステートメントを作成する
				statement = createPreStatement(strSQL);

				if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
					setPsByMysql(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
					setPsByOracle(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
					setPsByPostgre(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
					setPsBySqlServer(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_DB2.equals(dbType)) {
					setPsByDb2(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {
					setPsBySqlServer(paraList, statement);
				} else if (DbInfo.STR_DB_TYPE_SQLITE.equals(dbType)) {
					setPsBySqlLite(paraList, statement);
				} else {
					setPsByOracle(paraList, statement);
				}
				paraList.clear();

				// 問合せの実行
				rs = statement.executeQuery();

				// 最大列番号を取得する
				ResultSetMetaData meta = rs.getMetaData();
				int iColumnCount = meta.getColumnCount();
				int iHasMapLength = 16;

				// HashMap長さの設定
				if (iColumnCount > 16) {
					iHasMapLength = iColumnCount;
				}
				HashMap<String, String> map = null;

				if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {

						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						getDataMapForMysql(rs, meta, iColumnCount, map);
						
						if (!checkIsHave(map,keySet,xlsRecKeySet,keych)) {
							writeContent(bw, ch, CsjProcess.s_newLine, map,CsjConst.CMP_DB_INS);
							insCnt++;
						}
						cnt++;
					}
				} else if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {

						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						getDataMapForOracle(rs, meta, iColumnCount, map);
	
						if (!checkIsHave(map,keySet,xlsRecKeySet,keych)) {
							writeContent(bw, ch, CsjProcess.s_newLine, map,CsjConst.CMP_DB_INS);
							insCnt++;
						}
						cnt++;
					}
				} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {

						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						getDataMapForPostGre(rs, meta, iColumnCount, map);
						if (!checkIsHave(map,keySet,xlsRecKeySet,keych)) {
							writeContent(bw, ch, CsjProcess.s_newLine, map,CsjConst.CMP_DB_INS);
							insCnt++;
						}
						cnt++;
					}
				} else if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {

						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						getDataMapForSqlServer(rs, meta, iColumnCount, map);
						if (!checkIsHave(map,keySet,xlsRecKeySet,keych)) {
							writeContent(bw, ch, CsjProcess.s_newLine, map,CsjConst.CMP_DB_INS);
							insCnt++;
						}
						cnt++;
					}
				} else if (DbInfo.STR_DB_TYPE_DB2.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {

						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						getDataMapForDb2(rs, meta, iColumnCount, map);
						if (!checkIsHave(map,keySet,xlsRecKeySet,keych)) {
							writeContent(bw, ch, CsjProcess.s_newLine, map,CsjConst.CMP_DB_INS);
							insCnt++;
						}
						cnt++;
					}
				} else if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {

						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						// TODOSAI
						getDataMapForDb2(rs, meta, iColumnCount, map);
						if (!checkIsHave(map,keySet,xlsRecKeySet,keych)) {
							writeContent(bw, ch, CsjProcess.s_newLine, map,CsjConst.CMP_DB_INS);
							insCnt++;
						}
						cnt++;
					}
				}  else if (DbInfo.STR_DB_TYPE_SQLITE.equals(dbType)) {
					// 問合せ結果の表示
					while (rs.next()) {

						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						// TODOSAI
						getDataMapForDb2(rs, meta, iColumnCount, map);
						if (!checkIsHave(map,keySet,xlsRecKeySet,keych)) {
							writeContent(bw, ch, CsjProcess.s_newLine, map,CsjConst.CMP_DB_INS);
							insCnt++;
						}
						cnt++;
					}
				} else {
					// 問合せ結果の表示
					while (rs.next()) {

						// 各行の内容を格納するために、マップを初期化する
						map = new LinkedHashMap<String, String>(iHasMapLength);

						getDataMapForOracle(rs, meta, iColumnCount, map);
						if (!checkIsHave(map,keySet,xlsRecKeySet,keych)) {
							writeContent(bw, ch, CsjProcess.s_newLine, map,CsjConst.CMP_DB_INS);
							insCnt++;
						}
						cnt++;
					}
				}

				// 結果セットをクローズ
				rs.close();
				// ステートメントをクローズ
				statement.close();

			} else {
				throw new SQLException("connection is unavailable");
			}
		} catch (Throwable e) {
			throw e;
		} finally {
			if (rs != null) {
				// 結果セットをクローズ
				rs.close();
			}
			if (statement != null) {
				// ステートメントをクローズ
				statement.close();
			}
		}
	
		retVal[0]=cnt;
		retVal[1]=insCnt;
		return retVal;
	}

	/**
	 * @param map
	 * @param xlsRecKeySet
	 * @param xlsRecKeySet2 
	 */
	private boolean checkIsHave(HashMap<String, String> map,
			Set<String> keySet, Set<String> xlsRecKeySet, String ch) {
		boolean retVal = false;
		
		StringBuffer sb = new StringBuffer();
		for (String key : keySet) {
			sb.append(map.get(key));
			sb.append(ch);
		}
		if (xlsRecKeySet.contains(sb.toString())) {
			retVal = true;
		}
		return retVal;
	}
	public void setSqlCntZero() {
		sqlCnt = 0;
	}

}
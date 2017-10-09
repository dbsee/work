/**
 *
 */
package jp.co.csj.tools.utils.z.exe.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
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
import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.db.CsjDBAccess;
import jp.co.csj.tools.utils.db.core.CsjTableStructXls;
import jp.co.csj.tools.utils.db.core.DbInfo;
import jp.co.csj.tools.utils.db.core.SheetTblsInfo;
import jp.co.csj.tools.utils.db.core.TblBase;
import jp.co.csj.tools.utils.db.core.TblInfo;
import jp.co.csj.tools.utils.db.core.TblPara;
import jp.co.csj.tools.utils.db.core.XlsTblInfo;
import jp.co.csj.tools.utils.msg.dbtools.CsjDbToolsMsg;
import jp.co.csj.tools.utils.poi.core.CsjCellInfo;
import jp.co.csj.tools.utils.poi.core.CsjColInfo;
import jp.co.csj.tools.utils.poi.core.CsjDbCellStyle;
import jp.co.csj.tools.utils.poi.core.CsjRowInfo;
import jp.co.csj.tools.utils.poi.core.CsjSheetInfo;
import jp.co.csj.tools.utils.xml.dbtools.XmlDbXlsInfoAll;

/**
 * @author 963210
 *
 */
public class AutoXlsToCreateTblSql {
	public static String s_info = "";
	public static long sqlCnt = 0;
	public static List<String> errorList = new ArrayList<String>();
	//public static String s_sheetNm = "";

	private static final String INI_LOOP_ROWNUM="LOOP_ROWNUM";
	private static final String INI_LOOP_COLNUM="LOOP_COLNUM";
	private static final String INI_NO="NO";
	private static final String INI_COLJP="COLJP";
	private static final String INI_COLEN="COLEN";
	private static final String INI_COLTYPEALL="COLTYPEALL";
	private static final String INI_COLTYPE="COLTYPE";
	private static final String INI_COLISNULL="COLISNULL";
	private static final String INI_COLISKEY="COLISKEY";
	private static final String INI_NUMBERDOTPRELENG="NUMBERDOTPRELENG";
	private static final String INI_NUMBERDOTAFTERLENG="NUMBERDOTAFTERLENG";
	private static final String INI_INITVAL="INITVAL";
	private static final String INI_TBLEN="TBLEN";
	private static final String INI_TBLJP="TBLJP";
	private static final String INI_TBLRECORDNUM="TBLRECORDNUM";
	private static final String INI_SERVERID="SERVERID";
	private static final String INI_USER="USER";
	private static final String INI_PASSWORD="PASSWORD";
	private static final String INI_DBTYPE="DBTYPE";

	/**
	 *
	 */
	public AutoXlsToCreateTblSql() {
	}

	/**
	 * @param sheetNm 
	 * @param dbInfo
	 * @throws Throwable
	 *
	 */
	public static void run(XmlDbXlsInfoAll xmlDbXlsInfoAll, String xlsPath, String sheetNm) throws Throwable {
		long lasting = System.currentTimeMillis();
		errorList.clear();
		try {

			SheetTblsInfo tblsInfo = exeXlsToDb(xmlDbXlsInfoAll, xlsPath,sheetNm);

			for (Map.Entry<String, XlsTblInfo> entry : tblsInfo.getTblInfoMap().entrySet()) {
				writeCreateTblSql(xmlDbXlsInfoAll,entry.getValue());
			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		} finally {
			try {
				CmnLog5j.closeLog5j();
			} catch (Throwable e) {
				e.printStackTrace();
				CmnLog.logger.error(e.getMessage());
			}
		}
		s_info = "run time：" + CmnDateUtil.getMsHour(System.currentTimeMillis() - lasting);
		CmnLog.logger.debug(s_info);
	}
	public static void runByDb(XmlDbXlsInfoAll xmlDbXlsInfoAll, String tblEn) throws Throwable {
		long lasting = System.currentTimeMillis();
		errorList.clear();
		try {

			if (CmnStrUtils.isNotEmpty(tblEn)) {
				for (TblBase tblBase : xmlDbXlsInfoAll.getDbInfo().getTblInfoList()) {
					if(tblBase.getTblNmEn().equalsIgnoreCase(tblEn)) {
						writeCreateTblSql(xmlDbXlsInfoAll,AutoDbToXls.getXlsTblInfo(xmlDbXlsInfoAll, tblBase));
					}
				}
			} else {
				for (TblBase tblBase : xmlDbXlsInfoAll.getDbInfo().getTblInfoList()) {
					writeCreateTblSql(xmlDbXlsInfoAll,AutoDbToXls.getXlsTblInfo(xmlDbXlsInfoAll, tblBase));
				}
			}
			
			String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
			if (DbInfo.STR_DB_TYPE_ORACLE.equals(dbType)) {
				outPutOracleSeq(xmlDbXlsInfoAll);
			}

		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		} finally {
			try {
				CmnLog5j.closeLog5j();
			} catch (Throwable e) {
				e.printStackTrace();
				CmnLog.logger.error(e.getMessage());
			}
		}
		s_info = "run time：" + CmnDateUtil.getMsHour(System.currentTimeMillis() - lasting);
		CmnLog.logger.debug(s_info);
	}
	public static void outPutOracleSeq(XmlDbXlsInfoAll xmlDbXlsInfoAll) throws Throwable {
		String seqSql = "select  *  from  SYS.user_sequences";
		
		CsjDBAccess dbAccess = xmlDbXlsInfoAll.getDbInfo().getDbAccess();
		List<HashMap<String, String>> seqDataList = 
				dbAccess.getRecordList(seqSql, new ArrayList<Object>(),Integer.MAX_VALUE).getResultList();

		for (HashMap<String, String> map : seqDataList) {

			String SEQUENCE_NAME = map.get("SEQUENCE_NAME");
			String MIN_VALUE = map.get("MIN_VALUE");
			String MAX_VALUE = map.get("MAX_VALUE");
			String INCREMENT_BY = map.get("INCREMENT_BY");
			String CYCLE_FLAG = map.get("CYCLE_FLAG");
			String ORDER_FLAG = map.get("ORDER_FLAG");
			
			String currentSeq = "select "+SEQUENCE_NAME+".nextval AAA from dual";
			
			//HashMap<String, String> nextValMap = dbAccess.getRecord(currentSeq, new ArrayList<>());
			String currentVal = map.get("LAST_NUMBER");//nextValMap.get("AAA");
			
			
			int currentIntVal = CmnStrUtils.getIntVal(currentVal);
			
			List<String> sqlLst = new ArrayList<>();
			sqlLst.add("--drop sequence "+CmnStrUtils.addLRSign(SEQUENCE_NAME, CsjConst.SIGN_DOUBLE) + CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000071));
//			create sequence SEQ1
//			minvalue 1
//			maxvalue 2000
//			start with 1
//			increment by 1
//			cache 20
//			cycle
//			order
			sqlLst.add("create sequence " + SEQUENCE_NAME);
			sqlLst.add("minvalue " + MIN_VALUE);
			sqlLst.add("maxvalue " + MAX_VALUE);
			sqlLst.add("start with " + currentIntVal);
			sqlLst.add("increment by " + INCREMENT_BY);
			if ("Y".equalsIgnoreCase(CYCLE_FLAG)) {
				sqlLst.add("cycle");
			}
			if ("Y".equalsIgnoreCase(ORDER_FLAG)) {
				sqlLst.add("order");
			}
			writeSql("SEQUENCE_"+SEQUENCE_NAME, sqlLst, xmlDbXlsInfoAll);
		}
		
	}

	public static List<String> writeCreateTblSql(XmlDbXlsInfoAll xmlDbXlsInfoAll, XlsTblInfo tblInfo) throws Throwable {

		List<String> retList = null;
		String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
		if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType)) {
			writeSqlServer(tblInfo,xmlDbXlsInfoAll);
		} else if (DbInfo.STR_DB_TYPE_POSTGRE.equals(dbType)) {
			writePostgres(tblInfo,xmlDbXlsInfoAll);
		} else if (DbInfo.STR_DB_TYPE_MYSQL.equals(dbType)) {
			writeMysql(tblInfo,xmlDbXlsInfoAll);
		} else if (DbInfo.STR_DB_TYPE_SQLITE.equals(dbType)) {
			writeSqlLite(tblInfo,xmlDbXlsInfoAll);
		} else {
			writeOracle(tblInfo,xmlDbXlsInfoAll);
		}
		return retList;
	}


	/**
	 * @param tblInfo
	 * @param dbXlsInfo
	 * @throws Throwable
	 * @throws UnsupportedEncodingException
	 */
	private static void writeMysql(XlsTblInfo tblInfo, XmlDbXlsInfoAll xmlDbXlsInfoAll) throws Throwable {
		String encode = xmlDbXlsInfoAll.getXmlInfoSql().getEncode();
		String filePath = xmlDbXlsInfoAll.getDbInfo().getCreateSqlPath();
		File f = new File(filePath);
		f.mkdirs();

		String schemaDot = xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot();

		String fileAbPath = filePath + tblInfo.getTblNmEn() + ".sql";
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileAbPath), encode));

		CmnFileUtils.writeWithbBlank(writer, "--drop table "+ schemaDot+ CmnStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_SINGLE_1) + CsjDbToolsMsg.coreMsgMap
				.get(CsjDbToolsMsg.MSG_I_0000071), 0);

		List<String> createList = getCreateMySql(xmlDbXlsInfoAll.getDbInfo().getDbAccess(),tblInfo,schemaDot);
		for (String str : createList) {

//			strSQL = strSQL.replaceAll("\"", "`");
			CmnFileUtils.writeWithbBlank(writer, str, 0);
		}

		writer.close();
	}


	/**
	 * @param tblInfo
	 * @param dbXlsInfo
	 * @throws Throwable
	 * @throws UnsupportedEncodingException
	 */
	private static void writeSqlLite(XlsTblInfo tblInfo, XmlDbXlsInfoAll xmlDbXlsInfoAll) throws Throwable {
		String encode = xmlDbXlsInfoAll.getXmlInfoSql().getEncode();
		String filePath = xmlDbXlsInfoAll.getDbInfo().getCreateSqlPath();
		File f = new File(filePath);
		f.mkdirs();

		String schemaDot = xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot();

		String fileAbPath = filePath + tblInfo.getTblNmEn() + ".sql";
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileAbPath), encode));

		CmnFileUtils.writeWithbBlank(writer, "--drop table "+ schemaDot+ CmnStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_SINGLE_1) + CsjDbToolsMsg.coreMsgMap
				.get(CsjDbToolsMsg.MSG_I_0000071), 0);

		List<String> createList = getCreateSqlLite(xmlDbXlsInfoAll.getDbInfo().getDbAccess(),tblInfo,schemaDot);
		for (String str : createList) {

//			strSQL = strSQL.replaceAll("\"", "`");
			CmnFileUtils.writeWithbBlank(writer, str, 0);
		}

		writer.close();
	}
	/**
	 * @param csjDBAccess 
	 * @param tblInfo
	 * @param schema
	 * @return
	 * @throws Throwable 
	 */
	public static List<String> getCreateMySql(CsjDBAccess csjDBAccess, TblInfo tblInfo, String schema) throws Throwable {
		List<String> tmpCreateList = new ArrayList<String>();
		String keyStr = "";

		List<String> keyList = new ArrayList<String>();
		List<TblPara> paraList = new ArrayList<TblPara>();

		if (tblInfo instanceof XlsTblInfo && ((XlsTblInfo)tblInfo).getParaPosInfoMap().isEmpty() == false) {
			for (Entry<Integer, TblPara> entry : ((XlsTblInfo)tblInfo).getParaPosInfoMap().entrySet()) {

				TblPara para = entry.getValue();
				paraList.add(para);
			}
		} else {
			for (Entry<String, TblPara> entry : tblInfo.getParaInfoMap().entrySet()) {

				TblPara para = entry.getValue();

				paraList.add(para);
			}
		}

		String autoIncrementCol = "";
		
		for (int i = 0; i < paraList.size(); i++) {
			TblPara para = paraList.get(i);
			StringBuffer sb = new StringBuffer();
			if (para.isPkey()) {
				keyList.add(para.getParaNmEn());
			}
			sb.append(CmnStrUtils.addLRSign(para.getParaNmEn(), CsjConst.SIGN_SINGLE_1) + CsjConst.SIGN_SPACE_1);

			sb.append(CmnStrUtils.toLowOrUpStr(para.getParaTypeWithlen()) + CsjConst.SIGN_SPACE_1);

			if (para.isCanNull() == false) {
				sb.append(CsjConst.SIGN_NOT + CsjConst.SIGN_SPACE_1);
			}

			sb.append(CsjConst.SIGN_NULL + CsjConst.SIGN_SPACE_1);

			if ("auto_increment".equalsIgnoreCase(para.getParaExtra())) {
				sb.append("AUTO_INCREMENT" + CsjConst.SIGN_SPACE_1);
				autoIncrementCol = para.getParaNmEn();
			}
			
			String initVal = para.getParaInitVal();
			if (CmnStrUtils.isNotEmpty(initVal)) {
				sb.append("DEFAULT " +initVal + CsjConst.SIGN_SPACE_1);
			}
			if (CmnStrUtils.isNotEmpty(para.getParaNmJp())) {
				sb.append(CsjConst.SIGN_COMMENT  + CsjConst.SIGN_SPACE_1+ CmnStrUtils.addLRSign(para.getParaNmJp(), CsjConst.SIGN_SINGLE_2));
			}

			tmpCreateList.add(sb.toString());
		}
		if (CmnStrUtils.isNotEmpty(autoIncrementCol)) {
			String sql = "SELECT MAX("+autoIncrementCol+") as  CNT FROM \"" + tblInfo.getTblNmEn() + "\" t";
			int cnt = AutoXlsToDbForMemory.commitCntSql(csjDBAccess, sql, new ArrayList<Object>()) + 1;
			autoIncrementCol = "AUTO_INCREMENT=" +  cnt;
		}
		
		StringBuffer sbKey = new StringBuffer();
		for (int i = 0; i < keyList.size(); i++) {
			sbKey.append(CmnStrUtils.addLRSign(keyList.get(i), CsjConst.SIGN_SINGLE_1));
			if (i + 1 != keyList.size()) {
				sbKey.append(CsjConst.SIGN_COMMA);
			}
		}

		if (CmnStrUtils.isNotEmpty(sbKey.toString())) {
			keyStr = "PRIMARY KEY (" + sbKey.toString() + ")";
		}
		List<String> createList = new ArrayList<String>();

		String sqpTblJpNm ="";
		if (CmnStrUtils.isNotEmpty(tblInfo.getTblNmJp())) {
			sqpTblJpNm = "COMMENT="+CmnStrUtils.addLRSign(tblInfo.getTblNmJp(), CsjConst.SIGN_SINGLE_2);
		}

		createList.add( "create table " +schema+ CmnStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_SINGLE_1) + " (");
		for (int i = 0; i < tmpCreateList.size(); i++) {

			String str = tmpCreateList.get(i);
			if (i + 1 == tmpCreateList.size()) {
				if (CmnStrUtils.isEmpty(keyStr)) {
					str += CsjConst.SIGN_BRACKETS_S_R +autoIncrementCol+CsjConst.SIGN_SPACE_1+ sqpTblJpNm + CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000071);
					createList.add(str);
				} else {
					str += CsjConst.SIGN_COMMA;
					createList.add(str);
					createList.add(keyStr + CsjConst.SIGN_BRACKETS_S_R +autoIncrementCol+CsjConst.SIGN_SPACE_1+ sqpTblJpNm + CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000071));

				}
				continue;

			} else {
				str += CsjConst.SIGN_COMMA;
			}

			createList.add(str);
		}

		return createList;
	}

	/**
	 * @param csjDBAccess 
	 * @param tblInfo
	 * @param schema
	 * @return
	 * @throws Throwable 
	 */
	public static List<String> getCreateSqlLite(CsjDBAccess csjDBAccess, TblInfo tblInfo, String schema) throws Throwable {
		List<String> tmpCreateList = new ArrayList<String>();
		String keyStr = "";

		List<String> keyList = new ArrayList<String>();
		List<TblPara> paraList = new ArrayList<TblPara>();

		if (tblInfo instanceof XlsTblInfo && ((XlsTblInfo)tblInfo).getParaPosInfoMap().isEmpty() == false) {
			for (Entry<Integer, TblPara> entry : ((XlsTblInfo)tblInfo).getParaPosInfoMap().entrySet()) {

				TblPara para = entry.getValue();
				paraList.add(para);
			}
		} else {
			for (Entry<String, TblPara> entry : tblInfo.getParaInfoMap().entrySet()) {

				TblPara para = entry.getValue();

				paraList.add(para);
			}
		}

		String autoIncrementCol = "";
		
		for (int i = 0; i < paraList.size(); i++) {
			TblPara para = paraList.get(i);
			StringBuffer sb = new StringBuffer();
			if (para.isPkey()) {
				keyList.add(para.getParaNmEn());
			}
			sb.append(CmnStrUtils.addLRSign(para.getParaNmEn(), CsjConst.SIGN_SINGLE_1) + CsjConst.SIGN_SPACE_1);

			sb.append(CmnStrUtils.toLowOrUpStr(para.getParaTypeWithlen()) + CsjConst.SIGN_SPACE_1);

			if (para.isCanNull() == false) {
				sb.append(CsjConst.SIGN_NOT + CsjConst.SIGN_SPACE_1);
			}

			sb.append(CsjConst.SIGN_NULL + CsjConst.SIGN_SPACE_1);

			if ("auto_increment".equalsIgnoreCase(para.getParaExtra())) {
				sb.append("AUTO_INCREMENT" + CsjConst.SIGN_SPACE_1);
				autoIncrementCol = para.getParaNmEn();
			}
			
			String initVal = para.getParaInitVal();
			if (CmnStrUtils.isNotEmpty(initVal)) {
				sb.append("DEFAULT " +initVal + CsjConst.SIGN_SPACE_1);
			}
//			if (CsjStrUtils.isNotEmpty(para.getParaNmJp())) {
//				sb.append(CsjConst.SIGN_COMMENT  + CsjConst.SIGN_SPACE_1+ CsjStrUtils.addLRSign(para.getParaNmJp(), CsjConst.SIGN_SINGLE_2));
//			}

			tmpCreateList.add(sb.toString());
		}
		if (CmnStrUtils.isNotEmpty(autoIncrementCol)) {
			String sql = "SELECT MAX("+autoIncrementCol+") as  CNT FROM \"" + tblInfo.getTblNmEn() + "\" t";
			int cnt = AutoXlsToDbForMemory.commitCntSql(csjDBAccess, sql, new ArrayList<Object>()) + 1;
			autoIncrementCol = "AUTO_INCREMENT=" +  cnt;
		}
		
		StringBuffer sbKey = new StringBuffer();
		for (int i = 0; i < keyList.size(); i++) {
			sbKey.append(CmnStrUtils.addLRSign(keyList.get(i), CsjConst.SIGN_SINGLE_1));
			if (i + 1 != keyList.size()) {
				sbKey.append(CsjConst.SIGN_COMMA);
			}
		}

		if (CmnStrUtils.isNotEmpty(sbKey.toString())) {
			keyStr = "PRIMARY KEY (" + sbKey.toString() + ")";
		}
		List<String> createList = new ArrayList<String>();

		String sqpTblJpNm ="";
//		if (CsjStrUtils.isNotEmpty(tblInfo.getTblNmJp())) {
//			sqpTblJpNm = "COMMENT="+CsjStrUtils.addLRSign(tblInfo.getTblNmJp(), CsjConst.SIGN_SINGLE_2);
//		}

		createList.add( "create table " +schema+ CmnStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_SINGLE_1) + " (");
		for (int i = 0; i < tmpCreateList.size(); i++) {

			String str = tmpCreateList.get(i);
			if (i + 1 == tmpCreateList.size()) {
				if (CmnStrUtils.isEmpty(keyStr)) {
					str += CsjConst.SIGN_BRACKETS_S_R +autoIncrementCol+CsjConst.SIGN_SPACE_1+ sqpTblJpNm + CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000071);
					createList.add(str);
				} else {
					str += CsjConst.SIGN_COMMA;
					createList.add(str);
					createList.add(keyStr + CsjConst.SIGN_BRACKETS_S_R +autoIncrementCol+CsjConst.SIGN_SPACE_1+ sqpTblJpNm + CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000071));

				}
				continue;

			} else {
				str += CsjConst.SIGN_COMMA;
			}

			createList.add(str);
		}

		return createList;
	}
	/**
	 * @param tblInfo
	 * @param dbXlsInfo
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private static void  writePostgres(XlsTblInfo tblInfo,XmlDbXlsInfoAll xmlDbXlsInfoAll) throws Throwable {
		String encode = xmlDbXlsInfoAll.getXmlInfoSql().getEncode();
		List<String> postgreSqlList = getCreatePostgre(tblInfo,xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot());

		String filePath = xmlDbXlsInfoAll.getDbInfo().getCreateSqlPath();
		File f = new File(filePath);
		f.mkdirs();

		String fileAbPath = filePath + tblInfo.getTblNmEn() + ".sql";
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileAbPath), encode));

		CmnFileUtils.writeWithbBlank(writer, "--drop table " + CmnStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_DOUBLE) + CsjDbToolsMsg.coreMsgMap
				.get(CsjDbToolsMsg.MSG_I_0000071), 0);
		for (String str: postgreSqlList) {
			CmnFileUtils.writeWithbBlank(writer,str,0);
		}
		writer.close();
	}

	/**
	 * @param tblInfo
	 * @param schema
	 * @return
	 */
	public static List<String> getCreatePostgre(TblInfo tblInfo, String schema) {
		List<String> retList = new ArrayList<String>();

		List<String> tmpCreateList = new ArrayList<String>();
		String keyStr = "";

		List<String> keyList = new ArrayList<String>();
		List<TblPara> paraList = new ArrayList<TblPara>();
		List<String> defList = new ArrayList<String>();
		List<String> paraJpList = new ArrayList<String>();

		paraJpList.add("comment on table "+ schema+"\""+tblInfo.getTblNmEn()  + "\" is '" + tblInfo.getTblNmJp() + "';");

		if (tblInfo instanceof XlsTblInfo && ((XlsTblInfo)tblInfo).getParaPosInfoMap().isEmpty() == false) {
			for (Entry<Integer, TblPara> entry : ((XlsTblInfo)tblInfo).getParaPosInfoMap().entrySet()) {

				TblPara para = entry.getValue();

				if (CmnStrUtils.isNotEmpty(para.getParaNmJp())) {
					paraJpList.add("comment on column " + schema+tblInfo.getTblNmEn() + CsjConst.SIGN_DOT+"\"" + para.getParaNmEn()+"\""
							+ " is '" + para.getParaNmJp() + "';");
				}
				paraList.add(para);
			}
		} else {
			for (Entry<String, TblPara> entry : tblInfo.getParaInfoMap().entrySet()) {

				TblPara para = entry.getValue();

				if (CmnStrUtils.isNotEmpty(para.getParaNmJp())) {
					paraJpList.add("comment on column " + schema+tblInfo.getTblNmEn() + CsjConst.SIGN_DOT+"\"" + para.getParaNmEn()+"\""
							+ " is '" + para.getParaNmJp() + "';");
				}
				paraList.add(para);
			}
		}

		for (int i = 0; i < paraList.size(); i++) {
			TblPara para = paraList.get(i);
			StringBuffer sb = new StringBuffer();
			if (para.isPkey()) {
				keyList.add(para.getParaNmEn());
			}
			sb.append(CmnStrUtils.addLRSign(para.getParaNmEn(), CsjConst.SIGN_DOUBLE) + CsjConst.SIGN_SPACE_1);

			sb.append(CmnStrUtils.toLowOrUpStr(para.getParaTypeWithlen()) + CsjConst.SIGN_SPACE_1);

			if (para.isCanNull() == false) {
				sb.append(CsjConst.SIGN_NOT + CsjConst.SIGN_SPACE_1);
			}

			sb.append(CsjConst.SIGN_NULL + CsjConst.SIGN_SPACE_1);

			String initVal = para.getParaInitVal();
			if (CmnStrUtils.isNotEmpty(initVal)) {
				sb.append("DEFAULT " +initVal);
			}

			tmpCreateList.add(sb.toString());
		}
		StringBuffer sbKey = new StringBuffer();
		for (int i = 0; i < keyList.size(); i++) {
			sbKey.append(CmnStrUtils.addLRSign(keyList.get(i), CsjConst.SIGN_DOUBLE));
			if (i + 1 != keyList.size()) {
				sbKey.append(CsjConst.SIGN_COMMA);
			}
		}

		if (CmnStrUtils.isNotEmpty(sbKey.toString())) {
			keyStr = "CONSTRAINT " + CmnStrUtils.addLRSign(tblInfo.getTblNmEn()+schema.replace(".", "")+"_pkey", CsjConst.SIGN_DOUBLE)
					+ " primary key (" + sbKey.toString() + ")";
		}
		List<String> createList = new ArrayList<String>();

		createList.add("create table " + schema+CmnStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_DOUBLE) + " (");
		for (int i = 0; i < tmpCreateList.size(); i++) {

			String str = tmpCreateList.get(i);
			if (i + 1 == tmpCreateList.size()) {
				if (CmnStrUtils.isEmpty(keyStr)) {
					str += CsjConst.SIGN_BRACKETS_S_R + CsjDbToolsMsg.coreMsgMap
					.get(CsjDbToolsMsg.MSG_I_0000071);
				} else {
					str += CsjConst.SIGN_COMMA;
					createList.add(str);
					createList.add(keyStr + CsjConst.SIGN_BRACKETS_S_R + CsjDbToolsMsg.coreMsgMap
							.get(CsjDbToolsMsg.MSG_I_0000071));
					continue;
				}

			} else {
				str += CsjConst.SIGN_COMMA;

			}

			createList.add(str);
		}
		for (String str : createList) {
			retList.add(str);
		}

		for (String str : defList) {
			retList.add(str);
		}
		for (String str : paraJpList) {
			retList.add(str);
		}
		return retList;
	}

	/**
	 * @param tblInfo
	 * @param dbXlsInfo
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private static void writeSqlServer(XlsTblInfo tblInfo, XmlDbXlsInfoAll xmlDbXlsInfoAll) throws Throwable {
		String encode = xmlDbXlsInfoAll.getXmlInfoSql().getEncode();
		List<String> sqlServer = getCreateServer(tblInfo);

		String filePath = xmlDbXlsInfoAll.getDbInfo().getCreateSqlPath();
		File f = new File(filePath);
		f.mkdirs();

		String fileAbPath = filePath + tblInfo.getTblNmEn() + ".sql";
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileAbPath), encode));
		CmnFileUtils.writeWithbBlank(writer, "--drop table " + CmnStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_DOUBLE) + CsjDbToolsMsg.coreMsgMap
				.get(CsjDbToolsMsg.MSG_I_0000071), 0);
		for (String str: sqlServer) {
			CmnFileUtils.writeWithbBlank(writer,str,0);
		}

		writer.close();
	}

	/**
	 * @param tblInfo
	 * @return
	 */
	public static List<String> getCreateServer(TblInfo tblInfo) {

		List<String> retList = new ArrayList<String>();
		List<String> createList = new ArrayList<String>();
		List<String> constraintList = new ArrayList<String>();

		createList.add("create table " + CmnStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_DOUBLE)
				+ CsjConst.SIGN_SPACE_1 + CsjConst.SIGN_BRACKETS_S_L);
		List<String> keyList = new ArrayList<String>();
		List<TblPara> paraList = new ArrayList<TblPara>();
		List<String> defList = new ArrayList<String>();
		List<String> paraJpList = new ArrayList<String>();

		//EXECUTE sp_addextendedproperty N'MS_Description', N'表注释', N'user', N'dbo', N'table', N'empyyy', NULL, NULL
		//paraJpList.add("comment on table " + CsjStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_DOUBLE) + " is '" + tblInfo.getTblNmJp() + "';");
		paraJpList.add("EXECUTE sp_addextendedproperty N'MS_Description', N'" +tblInfo.getTblNmJp()+ "', N'user', N'dbo', N'table', N'"+tblInfo.getTblNmEn() + "', NULL, NULL;");
		if (tblInfo instanceof XlsTblInfo && ((XlsTblInfo)tblInfo).getParaPosInfoMap().isEmpty() == false) {
			for (Entry<Integer, TblPara> entry : ((XlsTblInfo)tblInfo).getParaPosInfoMap().entrySet()) {

				TblPara para = entry.getValue();
				String initVal = para.getParaInitVal();
				if (CmnStrUtils.isNotEmpty(initVal)) {

					defList.add("ALTER TABLE " + CmnStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_DOUBLE)
							+ " ADD  CONSTRAINT [DF_" + tblInfo.getTblNmEn() + para.getParaNmEn() + "]  DEFAULT " + initVal
							+ " FOR [" + para.getParaNmEn() + "];");
					// ALTER TABLE [dbo].[test_csj] ADD CONSTRAINT [DF_test_csj_cmt]
					// DEFAULT ('deff') FOR [cmt]
				}
				if (CmnStrUtils.isNotEmpty(para.getParaNmJp())) {
					//EXECUTE sp_addextendedproperty N'MS_Description', N'字段注释', N'user', N'dbo', N'table', N'empyyy', N'column', N'empyyyid'
//					paraJpList.add("comment on column " +CsjStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_DOUBLE) + CsjConst.SIGN_DOT + CsjStrUtils.addLRSign(para.getParaNmEn(), CsjConst.SIGN_DOUBLE)
//							+ " is '" + para.getParaNmJp() + "';");
					paraJpList.add("EXECUTE sp_addextendedproperty N'MS_Description', N'"+para.getParaNmJp() +"', N'user', N'dbo', N'table', N'"+tblInfo.getTblNmEn()+"', N'column', N'" + para.getParaNmEn()+"';");
				}
				paraList.add(para);
			}
		} else {
			for (Entry<String, TblPara> entry : tblInfo.getParaInfoMap().entrySet()) {

				TblPara para = entry.getValue();
				String initVal = para.getParaInitVal();
				if (CmnStrUtils.isNotEmpty(initVal)) {

					defList.add("ALTER TABLE " + CmnStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_DOUBLE)
							+ " ADD  CONSTRAINT [DF_" + tblInfo.getTblNmEn() + para.getParaNmEn() + "]  DEFAULT " + initVal
							+ " FOR [" + para.getParaNmEn() + "];");
					// ALTER TABLE [dbo].[test_csj] ADD CONSTRAINT [DF_test_csj_cmt]
					// DEFAULT ('deff') FOR [cmt]
				}
				if (CmnStrUtils.isNotEmpty(para.getParaNmJp())) {
					//EXECUTE sp_addextendedproperty N'MS_Description', N'字段注释', N'user', N'dbo', N'table', N'empyyy', N'column', N'empyyyid'
//					paraJpList.add("comment on column " +CsjStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_DOUBLE) + CsjConst.SIGN_DOT + CsjStrUtils.addLRSign(para.getParaNmEn(), CsjConst.SIGN_DOUBLE)
//							+ " is '" + para.getParaNmJp() + "';");
					paraJpList.add("EXECUTE sp_addextendedproperty N'MS_Description', N'"+para.getParaNmJp() +"', N'user', N'dbo', N'table', N'"+tblInfo.getTblNmEn()+"', N'column', N'" + para.getParaNmEn()+"';");
				}
				paraList.add(para);
			}
		}

		for (int i = 0; i < paraList.size(); i++) {
			TblPara para = paraList.get(i);
			StringBuffer sb = new StringBuffer();
			if (para.isPkey()) {
				keyList.add(para.getParaNmEn());
			}
			sb.append(CmnStrUtils.addLRSign(para.getParaNmEn(), CsjConst.SIGN_DOUBLE) + CsjConst.SIGN_SPACE_1);
			sb.append(para.getParaTypeWithlen() + CsjConst.SIGN_SPACE_1);
			if (para.isCanNull() == false) {
				sb.append(CsjConst.SIGN_NOT + CsjConst.SIGN_SPACE_1);
			}

			sb.append(CsjConst.SIGN_NULL + CsjConst.SIGN_SPACE_1);

			if (i + 1 == paraList.size()) {
				sb.append(CsjConst.SIGN_BRACKETS_S_R + CsjDbToolsMsg.coreMsgMap
						.get(CsjDbToolsMsg.MSG_I_0000071));
			} else {
				sb.append(CsjConst.SIGN_COMMA);
			}
			createList.add(sb.toString());
		}
		StringBuffer sbKey = new StringBuffer();
		for (int i = 0; i < keyList.size(); i++) {
			sbKey.append(CmnStrUtils.addLRSign(keyList.get(i), CsjConst.SIGN_DOUBLE));
			if (i + 1 != keyList.size()) {
				sbKey.append(CsjConst.SIGN_COMMA);
			}
		}
		if (CmnStrUtils.isNotEmpty(sbKey.toString())) {
			constraintList.add("alter table " + CmnStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_DOUBLE)
					+ " add constraint pk_" + tblInfo.getTblNmEn() + " primary key (" + sbKey.toString() + ");");
		}
		for (String str : createList) {
			retList.add(str);
		}
		for (String str : constraintList) {
			retList.add(str);
		}
		for (String str : defList) {
			retList.add(str);
		}
		for (String str : paraJpList) {
			retList.add(str);
		}
		return retList;
	}

	/**
	 * @param tblInfo
	 * @param dbXlsInfo
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private static void writeOracle(XlsTblInfo tblInfo, XmlDbXlsInfoAll xmlDbXlsInfoAll) throws Throwable {
		String encode = xmlDbXlsInfoAll.getXmlInfoSql().getEncode();
		String schemaDot = xmlDbXlsInfoAll.getCurrentXmlDb().getSchemaDot();
		List<String> oracleSql = getCreateOracle(tblInfo,schemaDot);

		String filePath = xmlDbXlsInfoAll.getDbInfo().getCreateSqlPath();
		File f = new File(filePath);
		f.mkdirs();

		String fileAbPath = filePath + tblInfo.getTblNmEn() +CsjDbToolsMsg.coreMsgMap
		.get(CsjDbToolsMsg.MSG_I_0000089)+  CsjDbToolsMsg.coreMsgMap
		.get(CsjDbToolsMsg.MSG_I_0000090);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fileAbPath), encode));

		CmnFileUtils.writeWithbBlank(writer, "--drop table "+schemaDot + CmnStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_DOUBLE) + CsjDbToolsMsg.coreMsgMap
				.get(CsjDbToolsMsg.MSG_I_0000071), 0);

		for (String str : oracleSql) {
			CmnFileUtils.writeWithbBlank(writer, str, 0);
		}
		writer.close();
	}
	
	/**
	 * @param tblInfo
	 * @param dbXlsInfo
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private static void writeSql(String fileNm,List<String> sqlLst, XmlDbXlsInfoAll xmlDbXlsInfoAll) throws Throwable {
		String encode = xmlDbXlsInfoAll.getXmlInfoSql().getEncode();


		String filePath = xmlDbXlsInfoAll.getDbInfo().getCreateSqlPath();
		File f = new File(filePath);
		f.mkdirs();

		String fileAbPath = filePath + fileNm +CsjDbToolsMsg.coreMsgMap
		.get(CsjDbToolsMsg.MSG_I_0000089)+  CsjDbToolsMsg.coreMsgMap
		.get(CsjDbToolsMsg.MSG_I_0000090);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fileAbPath), encode));

		for (String str : sqlLst) {
			CmnFileUtils.writeWithbBlank(writer, str, 0);
		}
		writer.close();
	}


	/**
	 * @param tblInfo
	 * @param schema
	 * @return
	 */
	public static List<String> getCreateOracle(TblInfo tblInfo, String schema) {
		List<String> retList = new ArrayList<String>();
		List<String> createList = new ArrayList<String>();
		List<String> constraintList = new ArrayList<String>();

		createList.add("create table " + schema+CmnStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_DOUBLE) + CsjConst.SIGN_SPACE_1 + CsjConst.SIGN_BRACKETS_S_L);
		List<String> keyList = new ArrayList<String>();
		List<TblPara> paraList = new ArrayList<TblPara>();
		List<String> defList = new ArrayList<String>();
		List<String> paraJpList = new ArrayList<String>();

		if ( CmnStrUtils.isNotEmpty(tblInfo.getTblNmJp())) {
			paraJpList.add("comment on table "+schema+CmnStrUtils.addLRSign(tblInfo.getTblNmEn(),CsjConst.SIGN_DOUBLE)  + " is " + CmnStrUtils.addLRSign(tblInfo.getTblNmJp(),CsjConst.SIGN_SINGLE_2) + ";");
		}
		if (tblInfo instanceof XlsTblInfo && ((XlsTblInfo)tblInfo).getParaPosInfoMap().isEmpty() == false) {
			for (Entry<Integer, TblPara> entry : ((XlsTblInfo)tblInfo).getParaPosInfoMap().entrySet()) {

				TblPara para = entry.getValue();

				if (CmnStrUtils.isNotEmpty(para.getParaNmJp())) {
					paraJpList.add("comment on column "+ schema+CmnStrUtils.addLRSign(tblInfo.getTblNmEn(),CsjConst.SIGN_DOUBLE) + CsjConst.SIGN_DOT + CmnStrUtils.addLRSign(para.getParaNmEn(),CsjConst.SIGN_DOUBLE) + " is " + CmnStrUtils.addLRSign(para.getParaNmJp(),CsjConst.SIGN_SINGLE_2) + ";");
				}
				paraList.add( para);

			}
		} else {
			for (Entry<String, TblPara> entry : tblInfo.getParaInfoMap().entrySet()) {

				TblPara para = entry.getValue();
	
				if (CmnStrUtils.isNotEmpty(para.getParaNmJp())) {
					paraJpList.add("comment on column "+ schema+CmnStrUtils.addLRSign(tblInfo.getTblNmEn(),CsjConst.SIGN_DOUBLE) + CsjConst.SIGN_DOT + CmnStrUtils.addLRSign(para.getParaNmEn(),CsjConst.SIGN_DOUBLE) + " is " + CmnStrUtils.addLRSign(para.getParaNmJp(),CsjConst.SIGN_SINGLE_2) + ";");
				}
				paraList.add( para);

			}
		}

		for (int i = 0; i < paraList.size();i++) {
			TblPara para = paraList.get(i);
			StringBuffer sb = new StringBuffer();
			if (para.isPkey()) {
				keyList.add(para.getParaNmEn());
			}
			sb.append(CmnStrUtils.addLRSign(para.getParaNmEn(), CsjConst.SIGN_DOUBLE) + CsjConst.SIGN_SPACE_1);

				String paraTypeWithLen = para.getParaTypeWithlen();
				if (paraTypeWithLen.contains("INTERVAL DAY") || paraTypeWithLen.contains("INTERVAL YEAR")) {
					paraTypeWithLen = paraTypeWithLen.substring(0, paraTypeWithLen.lastIndexOf("("));
				}
				sb.append(paraTypeWithLen + CsjConst.SIGN_SPACE_1);

			if (para.isCanNull() == false) {
				sb.append(CsjConst.SIGN_NOT + CsjConst.SIGN_SPACE_1);
					sb.append(CsjConst.SIGN_NULL + CsjConst.SIGN_SPACE_1);
			} else {
				String initVal = para.getParaInitVal();
					
					if (CmnStrUtils.isNotEmpty(initVal)) {
						sb.append(CsjConst.SIGN_DEFAULT + CsjConst.SIGN_SPACE_1+ initVal+CsjConst.SIGN_SPACE_1);

					} else {
						sb.append(CsjConst.SIGN_NULL + CsjConst.SIGN_SPACE_1);
					}
				
			}

			if (i+1==paraList.size()) {
				sb.append(CsjConst.SIGN_BRACKETS_S_R + CsjDbToolsMsg.coreMsgMap
						.get(CsjDbToolsMsg.MSG_I_0000071));
			} else {
				sb.append(CsjConst.SIGN_COMMA);
			}
			createList.add(sb.toString());
		}
		StringBuffer sbKey = new StringBuffer();
		for (int i = 0; i < keyList.size(); i++) {
			sbKey.append(CmnStrUtils.addLRSign(keyList.get(i), CsjConst.SIGN_DOUBLE));
			if (i +1 != keyList.size()) {
				sbKey.append(CsjConst.SIGN_COMMA);
			}
		}
		if (CmnStrUtils.isNotEmpty(sbKey.toString())) {
			constraintList.add("alter table "
					+ schema+CmnStrUtils.addLRSign(tblInfo.getTblNmEn(), CsjConst.SIGN_DOUBLE)
					+ " add constraint pk_"+tblInfo.getTblNmEn() +  " primary key (" +sbKey.toString()+ ");");
		}
		for (String str : createList) {
			retList.add(str);
		}

		for (String str : defList) {
			retList.add(str);
		}
		for (String str : paraJpList) {
			retList.add(str);
		}
		for (String str : constraintList) {
			retList.add(str);
		}
		return retList;
	}

	private static SheetTblsInfo exeXlsToDb(XmlDbXlsInfoAll xmlDbXlsInfoAll, String bookXls, String sheetName) throws Throwable {

		SheetTblsInfo tblsInfo = new SheetTblsInfo();
		String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();
		File file = new File(bookXls);
		FileInputStream fs = new FileInputStream(file);
		Workbook wb = null;
		if (CmnStrUtils.isEndByIgnor(bookXls, CsjConst.EXCEL_DOT_XLS_1997)) {
			wb = new HSSFWorkbook(fs);
		} else {
			wb = new XSSFWorkbook(fs);
		}
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			Sheet sheet = wb.getSheetAt(i);
			String sheetNm = sheet.getSheetName();
			if (CmnStrUtils.isNotEmpty(sheetName) && sheetNm.equals(sheetName) == false) {
				continue;
			}

			XlsTblInfo tblInfo = null;
			String tblNmEn = CsjConst.EMPTY;
			String tblNmJp = CsjConst.EMPTY;
			int colHeigthStep = 0;
			for (Row row : sheet) {

				String sign = CmnPoiUtils
				.getCellContent(sheet, row.getRowNum(), 0, false);
				if (sign.equals(xmlDbXlsInfoAll.getXmlInfoXls().getTblSign())) {
					tblNmEn = CmnPoiUtils
							.getCellContent(sheet, row.getRowNum(), 1, false);
					tblNmJp = CmnPoiUtils
							.getCellContent(sheet, row.getRowNum(), 2, false);


					tblInfo = new XlsTblInfo();
					tblsInfo.getTblInfoMap().put(tblNmEn, tblInfo);

					colHeigthStep = 0;
					tblInfo.setTblNmEn(tblNmEn);
					tblInfo.setTblNmJp(tblNmJp);
				}

//				List<String> strList = CsjPoiUtils.getCellContents(row, true);
//				if (strList.size() > 1) {
//					if (strList.get(0).equals(xmlInfoXls.getTblSign())
//							&& CsjPoiUtils.getCellContent(sheet, row.getRowNum(), 0, false)
//									.equals(xmlInfoXls.getTblSign())) {
//						if (CsjStrUtils.isNotEmpty(tblNmEn)) {
//							tblsInfo.getTblInfoMap().put(tblNmEn, tblInfo);
//						}
//
//						tblNmEn = strList.get(1);
//
//						if (strList.size()>=3) {
//							tblNmJp = strList.get(2);
//						}
//
//						tblInfo = new XlsTblInfo();
//						colHeigthStep = 0;
//						tblInfo.setTblNmEn(tblNmEn);
//						tblInfo.setTblNmJp(tblNmJp);
//						continue;
//					}
//				}

				if (CmnStrUtils.isNotEmpty(tblNmEn)) {
					LinkedHashMap<Integer, TblPara> paraPosInfoMap = tblInfo.getParaPosInfoMap();
					if (CmnStrUtils.isNotEmpty(tblNmEn)) {

						if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000002).equals(sign)) {
							for (Cell cell : row) {

								CsjCellInfo csjCellInfo = CmnPoiUtils.getCellInfo( cell);
								csjCellInfo.setContent(CmnStrUtils.lrTrimSpace(csjCellInfo.getContent()));
								int colPos = cell.getColumnIndex();
								if (colPos != 0) {
									TblPara para = new TblPara();
									if (csjCellInfo.getFont().getItalic()) {
										para.setParaNmJp(CsjConst.EMPTY);
									} else {
										para.setParaNmJp(csjCellInfo.getContent());
									}

									paraPosInfoMap.put(colPos, para);
								}
							}
						} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000003).equals(sign)) {
							for (Cell cell : row) {

								CsjCellInfo csjCellInfo = CmnPoiUtils.getCellInfo( cell);
								csjCellInfo.setContent(CmnStrUtils.lrTrimSpace(csjCellInfo.getContent()));
								int colPos = cell.getColumnIndex();
								if (colPos != 0) {
									if (paraPosInfoMap.containsKey(colPos)) {
										TblPara para = paraPosInfoMap.get(colPos);
										para.setParaNmEn(csjCellInfo.getContent());
										if (csjCellInfo.getFont().getBoldweight() == Font.BOLDWEIGHT_BOLD) {
											para.setPkey(true);
											tblInfo.getKeyPosList().add(colPos);
										} else {
											para.setPkey(false);
										}
									} else {
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
							}
						} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000004).equals(sign)) {
							for (Cell cell : row) {

								CsjCellInfo csjCellInfo = CmnPoiUtils.getCellInfo( cell);
								csjCellInfo.setContent(CmnStrUtils.lrTrimSpace(csjCellInfo.getContent()));
								int colPos = cell.getColumnIndex();
								if (colPos != 0 && paraPosInfoMap.containsKey(colPos)) {
									TblPara para = paraPosInfoMap.get(colPos);
									para.setParaTypeWithlen(dbType,csjCellInfo.getContent());
									Map<String, String> splitMap = CmnStrUtils.getSplitMap(CmnPoiUtils.getCellComment(cell), CsjProcess.s_newLine, CsjConst.MASK_TO_RIGHT,CsjConst.trimChSet,false);

									para.setParaType(splitMap.get(CsjConst.JP_TYPE));
									para.setParaLen(CmnStrUtils.getLongVal(splitMap.get(CsjConst.JP_LENGTH)));
									para.setParaExtra(splitMap.get(CsjConst.JP_EXTRA));
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

									Map<String, String> splitMap = CmnStrUtils.getSplitMap(
											CmnPoiUtils.getCellComment(cell), CsjProcess.s_newLine, CsjConst.MASK_TO_RIGHT,CsjConst.trimChSet,false);
									para.setParaInitVal(splitMap.get(CsjConst.JP_INIT_VAL));
								}
							}
							tblsInfo.getTblInfoMap().put(tblNmEn, tblInfo);
							tblInfo = null;
							tblNmEn = CsjConst.EMPTY;
						}

						colHeigthStep++;

					}
				}
			}
		}
		fs.close();
		return tblsInfo;
	}

	/**
	 * @param s_Db_Info
	 * @param absolutePath
	 * @param iniSheetNm
	 * @param sheetNm 
	 * @throws Throwable
	 */
	public static void runWithTblLayout(XmlDbXlsInfoAll xmlDbXlsInfoAll, String filePath,
			String iniSheetNm, String sheetNm) throws Throwable {

		CmnLog.logger.debug("AutoXlsToCreateTblSql.runWithTblLayout() begin");
		// xlsTblInfo.setParaInfoMap(paraInfoMap)
		String dbType = xmlDbXlsInfoAll.getCurrentXmlDbConfig().getDefualtSelect();

		SheetTblsInfo tblsInfo = new SheetTblsInfo();
		LinkedHashMap<String, XlsTblInfo> tblInfoMap = tblsInfo.getTblInfoMap();
		CsjTableStructXls tableStructXls = getTblStructXlsInfo(iniSheetNm, xmlDbXlsInfoAll);

		File inFile = new File(filePath);
		FileInputStream fileInputStream = new FileInputStream(inFile);

		Workbook wb;
		if (CmnStrUtils.isEndByIgnor(inFile.getName(),CsjConst.EXCEL_DOT_XLS_1997)) {
			wb = new HSSFWorkbook(fileInputStream);
		} else {
			wb = new XSSFWorkbook(fileInputStream);
		}
		
		
		CsjLinkedMap<Integer, String> colMap = tableStructXls.getColMap();
		CsjLinkedMap<String, Integer> colContentMap = tableStructXls
				.getColContentMap();

		int beginRow = tableStructXls.getLoopRow();
		int colNum = colContentMap.get(INI_NO) == null ? -1:colContentMap.get(INI_NO);
		int colEn = colContentMap.get(INI_COLEN) == null ? -1:colContentMap.get(INI_COLEN);
		int colJp = colContentMap.get(INI_COLJP) == null ? -1:colContentMap.get(INI_COLJP);
		int colIsKey = colContentMap.get(INI_COLISKEY) == null ? -1:colContentMap.get(INI_COLISKEY);
		int colIsNull = colContentMap.get(INI_COLISNULL) == null ? -1:colContentMap.get(INI_COLISNULL);
		int colType = colContentMap.get(INI_COLTYPE) == null ? -1:colContentMap.get(INI_COLTYPE);
		int colTypeAll = colContentMap.get(INI_COLTYPEALL) == null ? -1:colContentMap.get(INI_COLTYPEALL);
		int colDotPre = colContentMap.get(INI_NUMBERDOTPRELENG) == null ? -1:colContentMap.get(INI_NUMBERDOTPRELENG);
		int colDotAfter = colContentMap.get(INI_NUMBERDOTAFTERLENG) == null ? -1:colContentMap.get(INI_NUMBERDOTAFTERLENG);
		int colInit = colContentMap.get(INI_INITVAL) == null ? -1:colContentMap.get(INI_INITVAL);

		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			Sheet sheet = wb.getSheetAt(i);
			if (CmnStrUtils.isNotEmpty(sheetNm) && sheet.getSheetName().equals(sheetNm) == false) {
				continue;
			}
			
			CmnLog.logger.debug(sheet.getSheetName());
			CsjSheetInfo csjSheetInfo = CmnPoiUtils.getSheetContents(sheet,true);
			Map<String, CsjCellInfo> cellInfoMap = csjSheetInfo
					.getCsjCellInfoMap();
			Map<String, CsjCellInfo> cellPosInfoMap = csjSheetInfo
					.getCsjCellPosInfoMap();
			Map<Integer, CsjRowInfo> csjRowInfoMap = csjSheetInfo
					.getCsjRowInfoMap();
			Map<Integer, CsjColInfo> csjColInfoMap = csjSheetInfo
					.getCsjColInfoMap();
			XlsTblInfo xlsTblInfo = new XlsTblInfo();
			xlsTblInfo.setTblNmEn(cellPosInfoMap.get(
					tableStructXls.getTblEn().getRowNum() + "_"
							+ tableStructXls.getTblEn().getCellNum())
					.getContent());
			try {
				xlsTblInfo.setTblNmJp(cellPosInfoMap.get(
						tableStructXls.getTblJp().getRowNum() + "_"
								+ tableStructXls.getTblJp().getCellNum())
						.getContent());
			} catch (Throwable e) {
				e.printStackTrace();
				CmnLog.logger.error(e.getMessage());
			}

			LinkedHashMap<Integer, TblPara> paraPosInfoMap = xlsTblInfo.getParaPosInfoMap();
			CsjColInfo colInfo = csjColInfoMap.get(colNum);
			for (Entry<String, CsjCellInfo> entry : colInfo.getColMap().entrySet()) {
				CsjCellInfo cellInfo = entry.getValue();
				if (cellInfo == null || CmnStrUtils.isNumeric(entry.getKey())==false) {
					continue;
				}
				int rowIndex = cellInfo.getRowNum();
				if (rowIndex < beginRow) {
					continue;
				}
				TblPara tblPara = new TblPara();
				CsjCellInfo tCellInfo = cellPosInfoMap.get(rowIndex + "_"
						+ colEn);
				tblPara.setParaNmEn(tCellInfo == null ? "" : tCellInfo
						.getContent());
				tCellInfo = cellPosInfoMap.get(rowIndex + "_" + colJp);
				tblPara.setParaNmJp(tCellInfo == null ? "" : tCellInfo
						.getContent());
				
				
				tCellInfo = cellPosInfoMap.get(rowIndex + "_" + colTypeAll);
				tblPara.setParaTypeWithlen(dbType,tCellInfo == null ? "" : tCellInfo
						.getContent());
				
				

				if (tCellInfo == null) {
					tCellInfo = cellPosInfoMap.get(rowIndex + "_" + colType);
					tblPara.setParaType(tCellInfo == null ? "" : tCellInfo.getContent());
					tCellInfo = cellPosInfoMap.get(rowIndex + "_" + colDotPre);
					tblPara.setParaLen(tCellInfo == null ? 0 : CmnStrUtils.getIntVal(tCellInfo.getContent()));
					tblPara.setParaStrLen(tCellInfo.getContent());
					tCellInfo = cellPosInfoMap.get(rowIndex + "_" + colDotAfter);
					tblPara.setParaNumDotEndLen(tCellInfo == null ? 0 : CmnStrUtils.getIntVal(tCellInfo.getContent()));
					tblPara.setParaTypeWithlen(tblPara.getParaType(),tblPara.getParaLen(),tblPara.getParaNumDotEndLen());
				}
				
				tCellInfo = cellPosInfoMap.get(rowIndex + "_" + colInit);
				tblPara.setParaInitVal(tCellInfo == null ? "" : tCellInfo
						.getContent());

				tCellInfo = cellPosInfoMap.get(rowIndex + "_" + colIsKey);
				String tStr = tCellInfo == null ? "" : tCellInfo.getContent();
				tblPara.setPkey(tStr.equals(tableStructXls.getTruePk()));

				tCellInfo = cellPosInfoMap.get(rowIndex + "_" + colIsNull);
				tStr = tCellInfo == null ? "" : tCellInfo.getContent();
				tblPara.setCanNull(tStr.equals(tableStructXls.getTrueNull()));
				paraPosInfoMap.put(Integer.valueOf(entry.getKey()), tblPara);
			}
			writeCreateTblSql(xmlDbXlsInfoAll, xlsTblInfo);
		}
		fileInputStream.close();
		CmnLog.logger.debug("AutoXlsToCreateTblSql.runWithTblLayout()終了");
	}

	/**
	 * @param xlsTblInfo
	 * @param sheet
	 * @param tableStructXls
	 * @param dbInfo
	 * @param sheetMap
	 * @throws Throwable
	 */
	private static boolean
			writeToXlsTables(XlsTblInfo xlsTblInfo, Sheet sheet,
					CsjTableStructXls tableStructXls, XmlDbXlsInfoAll xmlDbXlsInfoAll,
					HashMap<String, Sheet> sheetMap)
					throws Throwable {
		CmnLog.logger
				.debug("AutoXlsToCreateTblSql.writeToXlsTables(XlsTblInfo xlsTblInfo, HSSFSheet sheet,CsjTableStructXls tableStructXls, DbInfo dbInfo, HashMap<String, HSSFSheet> sheetMap)  begin");
		boolean isSame = true;
		
		try {
			Map<String, String> map = new HashMap<String, String>();

			CsjCellInfo cellInfo = tableStructXls.getTblEn();
			
			if (cellInfo != null) {
				cellInfo.setContent(xlsTblInfo.getTblNmEn());
				CmnPoiUtils.setCellValue(sheet, cellInfo);
			}
			CmnPoiUtils.getCellContent(sheet, 0, 0, false);
			cellInfo = tableStructXls.getTblJp();
			if (cellInfo != null) {
				cellInfo.setContent(xlsTblInfo.getTblNmJp());
				CmnPoiUtils.setCellValue(sheet, cellInfo);
			}

			cellInfo = tableStructXls.getTblRecNum();
			if (cellInfo != null) {
				cellInfo.setContent(String.valueOf(xlsTblInfo.getTblDataList()
						.size()));
				CmnPoiUtils.setCellValue(sheet, cellInfo);
			}
			cellInfo = tableStructXls.getSubId();
			if (cellInfo != null) {
				cellInfo.setContent(xmlDbXlsInfoAll.getCurrentXmlDb().getDbUrl());
				CmnPoiUtils.setCellValue(sheet, cellInfo);
			}
			cellInfo = tableStructXls.getUser();
			if (cellInfo != null) {
				cellInfo.setContent(xmlDbXlsInfoAll.getCurrentXmlDb().getDbUserId());
				CmnPoiUtils.setCellValue(sheet, cellInfo);
			}
			cellInfo = tableStructXls.getPsw();
			if (cellInfo != null) {
				cellInfo.setContent(xmlDbXlsInfoAll.getCurrentXmlDb().getDbPassword());
				CmnPoiUtils.setCellValue(sheet, cellInfo);
			}
			cellInfo = tableStructXls.getDbType();
			if (cellInfo != null) {
				cellInfo.setContent(xmlDbXlsInfoAll.getDbInfo().getDbAccess().getConnection()
						.getClientInfo().toString());
				CmnPoiUtils.setCellValue(sheet, cellInfo);
			}
			
			CsjDbCellStyle cellStyle = new CsjDbCellStyle(sheet.getWorkbook(),xmlDbXlsInfoAll.getXmlInfoXls().getExcelType(),CsjDbCellStyle.CS_DATA_REC);

			CsjLinkedMap<Integer, String> colMap = tableStructXls.getColMap();
			LinkedHashMap<String, TblPara> paraInfoMap = xlsTblInfo
					.getParaInfoMap();
			String sheetName = sheet.getSheetName();
			
			CmnPoiUtils.insertRow(sheet, tableStructXls.getLoopRow(),
					paraInfoMap.size() - 1, true);
			int rowPos = tableStructXls.getLoopRow();
			int index = 0;
			for (Entry<String, TblPara> entry : paraInfoMap.entrySet()) {
				TblPara tblPara = entry.getValue();
				Map<String, String> infoMap = new HashMap<String, String>();
				infoMap.put(INI_NO, String.valueOf(++index));
				infoMap.put(INI_COLJP, tblPara.getParaNmJp());
				infoMap.put(INI_COLEN, tblPara.getParaNmEn());
				infoMap.put(INI_COLTYPE, tblPara.getParaType());
				infoMap.put(INI_COLTYPEALL, tblPara.getParaTypeWithlen());
				infoMap.put(INI_COLISNULL,tblPara.isCanNull() ? 
						tableStructXls.getTrueNull(): tableStructXls.getFalseNull());
//				infoMap.put(CsjDbToolsMsg.coreMsgMap
//						.get(CsjDbToolsMsg.MSG_I_0000009), tblPara
//						.getParaComment());
				infoMap.put(INI_INITVAL, tblPara.getParaInitVal());
				infoMap.put(INI_COLISKEY,tblPara.isPkey() ? 
						tableStructXls.getTruePk(): tableStructXls.getFalsePk());
				if (DbInfo.TABLE_COL_TYPE_NUM == tblPara.getType()) {
					infoMap.put(INI_NUMBERDOTPRELENG, String.valueOf(tblPara.getParaLen()));
					infoMap.put(INI_NUMBERDOTAFTERLENG, String.valueOf(tblPara.getParaNumDotEndLen()));
				} else if (DbInfo.TABLE_COL_TYPE_STR == tblPara.getType()) {
					if (tblPara.getParaLen() <= 0) {
						infoMap.put(INI_NUMBERDOTPRELENG, String.valueOf(tableStructXls.getIntNum()));
					} else {
						infoMap.put(INI_NUMBERDOTPRELENG, String.valueOf(tblPara.getParaLen()));
					}
					infoMap.put(INI_NUMBERDOTAFTERLENG, String.valueOf(tableStructXls.getDotNum()));
				} else {
					infoMap.put(INI_NUMBERDOTPRELENG, String.valueOf(tableStructXls.getIntNum()));
					infoMap.put(INI_NUMBERDOTAFTERLENG, String.valueOf(tableStructXls.getDotNum()));
				}

				infoMap.put(CsjConst.JP_TYPE, tblPara.getParaType());

				for (Entry<Integer, String> entryPara : colMap.entrySet()) {

					String val = infoMap.get(entryPara.getValue());

					Cell newCell =  CmnPoiUtils.setCellValueWithCs(sheet, rowPos,
							entryPara.getKey(), val,cellStyle.getCsRecordStr());
					if (sheetMap.size() != 0) {
						if (sheetMap.containsKey(CmnStrUtils.toLowOrUpStr(sheetName))) {
							Sheet oldSt = sheetMap.get(CmnStrUtils.toLowOrUpStr(sheetName));
							String oldVal = CmnPoiUtils.getCellContent(
									oldSt.getRow(rowPos), entryPara.getKey(),
									false);
							if (!CmnStrUtils.isEqualIgnorBigSmall(oldVal, val,
									true)) {
								CmnPoiUtils.setCellComment(newCell,
										new HSSFRichTextString(oldVal),
										CsjProcess.s_user);
								newCell.setCellStyle(cellStyle.getCsDiff_18_0());
								isSame = false;
							}
						}
					}
				}
				rowPos++;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger
				.debug("AutoXlsToCreateTblSql.writeToXlsTables(XlsTblInfo xlsTblInfo, HSSFSheet sheet,CsjTableStructXls tableStructXls, DbInfo dbInfo, HashMap<String, HSSFSheet> sheetMap)  end");
		return isSame;
	}

	/**
	 * @param s_file_table_temp
	 * @param sheetNm
	 * @param xmlDbXlsInfoAll 
	 * @return
	 * @throws Throwable
	 */
	public static CsjTableStructXls getTblStructXlsInfo(String sheetNm, XmlDbXlsInfoAll xmlDbXlsInfoAll)
			throws Throwable {
		CmnLog.logger
				.debug("AutoXlsToCreateTblSql.getTblStructXlsInfo(String sheetNm)  begin");
		CsjTableStructXls csjTableStructXls = new CsjTableStructXls();

		CsjSheetInfo csjSheetIno = CmnPoiUtils.getSheetContents(
				CsjPath.s_file_table_temp+xmlDbXlsInfoAll.getXmlInfoXls().getExcelType(), sheetNm + "ini", false);
		Map<String, CsjCellInfo> cellInfoMap = csjSheetIno.getCsjCellInfoMap();
		Map<String, CsjCellInfo> cellPosInfoMap = csjSheetIno
				.getCsjCellPosInfoMap();
		Map<Integer, CsjRowInfo> csjRowInfoMap = csjSheetIno.getCsjRowInfoMap();
		CsjCellInfo cellInfo = cellInfoMap.get(INI_LOOP_ROWNUM);
		csjTableStructXls.setLoopRow(CmnStrUtils.getIntVal(cellPosInfoMap.get(
				(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 1)))
				.getContent()));

		int rowPos = 0;
		int colPos = 0;

		try {
			cellInfo = cellInfoMap.get(INI_TBLEN);
			rowPos = CmnStrUtils.getIntVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 1)))
					.getContent());
			colPos = CmnStrUtils.getIntVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 2)))
					.getContent());
			csjTableStructXls.setTblEn(new CsjCellInfo("", rowPos, colPos));
		} catch (Throwable e) {
			CmnLog.logger.info(CsjDbToolsMsg.coreMsgMap
					.get(CsjDbToolsMsg.MSG_I_0000015));
		}

		try {
			cellInfo = cellInfoMap.get(INI_TBLJP);
			rowPos = CmnStrUtils.getIntVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 1)))
					.getContent());
			colPos = CmnStrUtils.getIntVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 2)))
					.getContent());
			csjTableStructXls.setTblJp(new CsjCellInfo("", rowPos, colPos));
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			CmnLog.logger.info(CsjDbToolsMsg.coreMsgMap
					.get(CsjDbToolsMsg.MSG_I_0000017));
		}

		try {
			cellInfo = cellInfoMap.get(INI_TBLRECORDNUM);
			rowPos = CmnStrUtils.getIntVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 1)))
					.getContent());
			colPos = CmnStrUtils.getIntVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 2)))
					.getContent());
			csjTableStructXls.setTblRecNum(new CsjCellInfo("", rowPos, colPos));
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			CmnLog.logger.info(CsjDbToolsMsg.coreMsgMap
					.get(CsjDbToolsMsg.MSG_I_0000019));
		}

		try {
			cellInfo = cellInfoMap.get(INI_SERVERID);
			rowPos = CmnStrUtils.getIntVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 1)))
					.getContent());
			colPos = CmnStrUtils.getIntVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 2)))
					.getContent());
			csjTableStructXls.setSubId(new CsjCellInfo("", rowPos, colPos));
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			CmnLog.logger.info(CsjDbToolsMsg.coreMsgMap
					.get(CsjDbToolsMsg.MSG_I_0000021));
		}
		try {
			cellInfo = cellInfoMap.get(INI_USER);
			rowPos = CmnStrUtils.getIntVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 1)))
					.getContent());
			colPos = CmnStrUtils.getIntVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 2)))
					.getContent());
			csjTableStructXls.setUser(new CsjCellInfo("", rowPos, colPos));
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			CmnLog.logger.info(CsjDbToolsMsg.coreMsgMap
					.get(CsjDbToolsMsg.MSG_I_0000023));
		}
		try {
			cellInfo = cellInfoMap.get(INI_PASSWORD);
			rowPos = CmnStrUtils.getIntVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 1)))
					.getContent());
			colPos = CmnStrUtils.getIntVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 2)))
					.getContent());
			csjTableStructXls.setPsw(new CsjCellInfo("", rowPos, colPos));
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			CmnLog.logger.info(CsjDbToolsMsg.coreMsgMap
					.get(CsjDbToolsMsg.MSG_I_0000025));
		}
		try {
			cellInfo = cellInfoMap.get(INI_DBTYPE);
			rowPos = CmnStrUtils.getIntVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 1)))
					.getContent());
			colPos = CmnStrUtils.getIntVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 2)))
					.getContent());
			csjTableStructXls.setDbType(new CsjCellInfo("", rowPos, colPos));
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			CmnLog.logger.info(CsjDbToolsMsg.coreMsgMap
					.get(CsjDbToolsMsg.MSG_I_0000027));
		}
		try {
			cellInfo = cellInfoMap.get(INI_DBTYPE);
			rowPos = CmnStrUtils.getIntVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 1)))
					.getContent());
			colPos = CmnStrUtils.getIntVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 2)))
					.getContent());
			csjTableStructXls.setDbType(new CsjCellInfo("", rowPos, colPos));
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			CmnLog.logger.info(CsjDbToolsMsg.coreMsgMap
					.get(CsjDbToolsMsg.MSG_I_0000027));
		}

		try {
			cellInfo = cellInfoMap.get(INI_COLISKEY);
			csjTableStructXls.setTruePk(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 1)))
					.getContent());
			csjTableStructXls.setFalsePk(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 2)))
					.getContent());
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			CmnLog.logger.info(CsjDbToolsMsg.coreMsgMap
					.get(CsjDbToolsMsg.MSG_I_0000044));
		}
		try {
			cellInfo = cellInfoMap.get(INI_COLISNULL);
			csjTableStructXls.setTrueNull(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 1)))
					.getContent());
			csjTableStructXls.setFalseNull(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 2)))
					.getContent());
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			CmnLog.logger.info(CsjDbToolsMsg.coreMsgMap
					.get(CsjDbToolsMsg.MSG_I_0000028));
		}
//		try {
//			cellInfo = cellInfoMap.get(CsjDbToolsMsg.coreMsgMap
//					.get(CsjDbToolsMsg.MSG_I_0000029));
//			csjTableStructXls.setIntNum(cellPosInfoMap.get(
//					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 1)))
//					.getContent());
//		} catch (Throwable e) {
//			e.printStackTrace();
//			MyLog.logger.error(e.getMessage());
//			MyLog.logger.info(CsjDbToolsMsg.coreMsgMap
//					.get(CsjDbToolsMsg.MSG_I_0000030));
//		}
//		try {
//			cellInfo = cellInfoMap.get(CsjDbToolsMsg.coreMsgMap
//					.get(CsjDbToolsMsg.MSG_I_0000031));
//			csjTableStructXls.setDotNum(cellPosInfoMap.get(
//					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 1)))
//					.getContent());
//		} catch (Throwable e) {
//			e.printStackTrace();
//			MyLog.logger.error(e.getMessage());
//			MyLog.logger.info(CsjDbToolsMsg.coreMsgMap
//					.get(CsjDbToolsMsg.MSG_I_0000032));
//		}
		try {
			cellInfo = cellInfoMap.get(INI_INITVAL);
			csjTableStructXls.setIniVal(cellPosInfoMap.get(
					(cellInfo.getRowNum() + "_" + (cellInfo.getCellNum() + 1)))
					.getContent());
		} catch (Throwable e) {

			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			CmnLog.logger.info(CsjDbToolsMsg.coreMsgMap
					.get(CsjDbToolsMsg.MSG_I_0000033));
		}
			cellInfo = cellInfoMap.get(INI_LOOP_COLNUM);
			CsjRowInfo csjRowInfoKey = csjRowInfoMap.get(cellInfo.getRowNum());
			CsjRowInfo csjRowInfoVal = csjRowInfoMap.get(cellInfo.getRowNum() + 1);
			CsjLinkedMap<Integer, String> colMap = csjTableStructXls.getColMap();
			CsjLinkedMap<String, Integer> colContentMap = csjTableStructXls
					.getColContentMap();
			for (int i = 0; i < csjRowInfoKey.getRowMap().size(); i++) {
				String strKey = csjRowInfoKey.getRowMap().getValByPos(i)
						.getContent();
				if (INI_LOOP_COLNUM.equals(strKey)) {
					continue;
				}
				Integer key = CmnStrUtils.getIntVal(strKey);
				String val = csjRowInfoVal.getRowMap().getValByPos(i).getContent();
				colMap.put(key, val);
				colContentMap.put(val, key);
			}


		CmnLog.logger
				.debug("AutoXlsToCreateTblSql.getTblStructXlsInfo(String sheetNm)  end");
		return csjTableStructXls;
	}

	/**
	 * @param tblsInfo
	 * @param dbInfo
	 * @param sheetMap
	 * @param isOneFile 
	 * @param compareFilePath
	 * @param isCompare
	 * @throws Throwable
	 */
	public static void writeToXlsForTables(SheetTblsInfo tblsInfo,
			XmlDbXlsInfoAll xmlDbXlsInfoAll, String sheetNm, HashMap<String, Sheet> sheetMap, boolean isOneFile)
			throws Throwable {
		CmnLog.logger
				.debug("AutoXlsToCreateTblSql.writeToXlsForTables(SheetTblsInfo tblsInfo,DbInfo dbInfo,String sheetNm, HashMap<String, HSSFSheet> sheetMap)  begin");
		FileInputStream fileIn = null;
		FileOutputStream fileOut = null;
		CsjTableStructXls tableStructXls = getTblStructXlsInfo(sheetNm,xmlDbXlsInfoAll);

		String excelType = xmlDbXlsInfoAll.getXmlInfoXls().getExcelType();
		try {

			if (isOneFile) {
				
				fileIn = new FileInputStream(CsjPath.s_file_table_temp+excelType);
				
				Workbook wb = null;
				if (CsjConst.EXCEL_DOT_XLS_1997.equals(excelType)) {
					 wb = new HSSFWorkbook(fileIn);
				} else {
					 wb = new XSSFWorkbook(fileIn);
				}
				CsjDbCellStyle csjDbCellStyle = new CsjDbCellStyle(wb,excelType,CsjDbCellStyle.CS_DIFF_REC);
				Set<String> tblNmSet = new HashSet<String>();
				
				Sheet headSheet = wb.createSheet("all_tbls");// wb.createSheet(tblNm);
				
				for (Map.Entry<String, XlsTblInfo> entry : tblsInfo.getTblInfoMap()
						.entrySet()) {

					String tblNm = entry.getKey();
					tblNmSet.add(tblNm);
					XlsTblInfo xlsTblInfo = entry.getValue();

					Sheet newSheet = wb.getSheet(sheetNm);// wb.createSheet(tblNm);
					newSheet = wb.cloneSheet(wb.getSheetIndex(newSheet.getSheetName()));
					wb.setSheetName(wb.getSheetIndex(newSheet.getSheetName()),
							tblNm);

					CmnLog.logger.info("table:" + tblNm);

					boolean isSame = writeToXlsTables(xlsTblInfo,
							wb.getSheet(tblNm), tableStructXls, xmlDbXlsInfoAll, sheetMap);

				}
				
				CreationHelper createHelper = wb.getCreationHelper();  
				int row = 0;
				for (Map.Entry<String, XlsTblInfo> entry : tblsInfo.getTblInfoMap().entrySet()) {
					Cell cell0 = CmnPoiUtils.getOrCreateCell(headSheet, row, 0);
					cell0.setCellValue(row + 1);
					Cell cell1 = CmnPoiUtils.getOrCreateCell(headSheet, row, 1);
					cell1.setCellValue(entry.getKey());
					// 最后把style应用到cell上去就大功告成了。
					cell1.setCellStyle(csjDbCellStyle.getCsLink());
					Cell cell2 = CmnPoiUtils.getOrCreateCell(headSheet, row, 2);
					cell2.setCellValue(entry.getValue().getTblNmJp());

					Hyperlink link2 = createHelper
							.createHyperlink(Hyperlink.LINK_DOCUMENT);
					link2.setAddress("'" + entry.getKey() + "'!A1");
					cell1.setHyperlink(link2);
//					cell1.setCellStyle(csjDbCellStyle.getCsLink());
					row++;
				}

				tblNmSet.add("all_tbls");

//				newSheet = wb.cloneSheet(wb.getSheetIndex(newSheet.getSheetName()));
//				wb.setSheetName(wb.getSheetIndex(newSheet.getSheetName()),
//						tblNm);
				
				String path = CsjPath.s_path_db_2_layout;
				CmnPoiUtils.deleteSheetWithOutNms(wb, tblNmSet);
				File f = new File(path);
				f.mkdirs();

				String filePath = path +"all_in_one" + excelType;
				fileOut = new FileOutputStream(filePath);
				wb.write(fileOut);
				fileOut.close();
				fileIn.close();
			} else {
				for (Map.Entry<String, XlsTblInfo> entry : tblsInfo.getTblInfoMap()
						.entrySet()) {

					fileIn = new FileInputStream(CsjPath.s_file_table_temp+excelType);
					
					Workbook wb = null;
					if (CsjConst.EXCEL_DOT_XLS_1997.equals(excelType)) {
						 wb = new HSSFWorkbook(fileIn);
					} else {
						 wb = new XSSFWorkbook(fileIn);
					}

					String tblNm = entry.getKey();
					XlsTblInfo xlsTblInfo = entry.getValue();

					Sheet newSheet = wb.getSheet(sheetNm);// wb.createSheet(tblNm);
					
					wb.setSheetName(wb.getSheetIndex(newSheet.getSheetName()),
							tblNm);

					CmnLog.logger.info("table:" + tblNm);
					
					String fNm = tblNm;
					if (CmnStrUtils.isNotEmpty(xlsTblInfo.getTblNmJp())) {
						fNm += "[" + xlsTblInfo.getTblNmJp() + "]";
					}
					fNm = CmnStrUtils.getStrForFileNm(fNm);
					String preNm = "";
					boolean isSame = writeToXlsTables(xlsTblInfo,
							wb.getSheet(tblNm), tableStructXls, xmlDbXlsInfoAll, sheetMap);
					if (sheetMap.size() != 0) {
						if (isSame) {
							preNm = "OK_";
						} else {
							preNm = "ERROR_";
						}
					}
					String path = CsjPath.s_path_db_2_layout;
					CmnPoiUtils.deleteSheetWithOutNm(wb, tblNm);
					File f = new File(path);
					f.mkdirs();

					String filePath = path + preNm + fNm;
					
					if (filePath.length()>IConstFile.FILE_MAX_LEN) {
						filePath = CmnStrUtils.getStrForLength(filePath, IConstFile.FILE_MAX_LEN);
					}
					
					
					fileOut = new FileOutputStream(filePath+ excelType);
					wb.write(fileOut);
					fileOut.close();
					fileIn.close();
				}
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
				.debug("AutoXlsToCreateTblSql.writeToXlsForTables(SheetTblsInfo tblsInfo,DbInfo dbInfo,String sheetNm, HashMap<String, HSSFSheet> sheetMap)  end");

	}

	/**
	 * @param s_Db_Info
	 * @param tableNmSet
	 * @param isOneFile 
	 * @param fileList
	 * @throws Throwable
	 */
	public static void runGenerateTableStructToXLs(XmlDbXlsInfoAll xmlDbXlsInfoAll,
			HashSet<String> tableNmSet, String sheetNm,
			boolean isOneFile, HashMap<String, Sheet> sheetMap) throws Throwable {

		long lasting = System.currentTimeMillis();
		CmnLog.logger
				.debug("AutoXlsToCreateTblSql.runGenerateTableStructToXLs(DbInfo dbInfo,HashSet<String> tableNmSet,String sheetNm,HashMap<String,HSSFSheet> sheetMap )  begin");
		try {
			CmnLog.logger.debug("read DB begin...");
			// if (CsjStrUtils.isEmpty(tableNmSet)) {
			// MyLog.logger.debug("tableNmSet is empty!!");
			// return ;
			// }
			// CsjPoiUtils.getSheetContents(sheet, checkStrikeOut)
			
			
			//SheetTblsInfo tblsInfo = AutoDbToXls.getDBInfo(xmlDbXlsInfoAll, tableNmSet);
			SheetTblsInfo tblsInfo = new SheetTblsInfo();
			try {
				List<TblBase> tblList = AutoDbToXls.getTblBaseWithSome(xmlDbXlsInfoAll, tableNmSet);

				for (TblBase base : tblList) {
					XlsTblInfo xlsTblInfo = AutoDbToXls.getXlsTblInfo(xmlDbXlsInfoAll, base);
					tblsInfo.getTblInfoMap().put(base.getTblNmEn(), xlsTblInfo);
				}
			} catch (Throwable e) {
				// TODO: handle exception
			}
				
			if (CmnStrUtils.isEmpty(sheetMap)) {
				CmnLog.logger.info("↓↓↓ tbls column infos ↓↓↓");
				for (Entry<String, List<String>> entry : tblsInfo
						.getDbTypeMap().entrySet()) {

					CmnLog.logger.info(String.format("%-20s",
							entry.getKey() + entry.getValue().toString()));
					// CsjStrUtils.toStr(entry.getKey())
					// + CsjStrUtils.toStr(entry.getValue());
					// MyLog.logger.info(String.format("%-20s",
					// ());

				}
				CmnLog.logger.info("↑↑↑ tbls column infos ↑↑↑");
			}

			CmnLog.logger.debug("tables num is "
					+ tblsInfo.getTblInfoMap().size());
			CmnLog.logger.debug("read DB end");

			CmnLog.logger.debug("write xls begin...");

			writeToXlsForTables(tblsInfo, xmlDbXlsInfoAll, sheetNm, sheetMap,isOneFile);
			CmnLog.logger.debug("write xls end");

			s_info = "run time："
					+ CmnDateUtil.getMsHour(System.currentTimeMillis()
							- lasting);
			CmnLog.logger.debug(s_info);
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
			throw e;
		}
		CmnLog.logger
				.debug("AutoXlsToCreateTblSql.runGenerateTableStructToXLs(DbInfo dbInfo,HashSet<String> tableNmSet,String sheetNm,HashMap<String,HSSFSheet> sheetMap )  end");

	}

}


/*
select

t3.name   as   表名,t1.name   as   字段名,t2.text   as   默认值   ,t4.name
from   syscolumns   t1,syscomments   t2,sysobjects   t3   ,sysobjects   t4
where     t1.cdefault=t2.id   and   t3.xtype='u'   and   t3.id=t1.id
and   t4.xtype='d'   and   t4.id=t2.id;


SQLlSERVER
exec   sp_helpindex   'zzz_test'


ORACLE
select t.*, i.*--i.index_type
from user_ind_columns t, user_indexes i
where t.index_name = i.index_name
and t.table_name = i.table_name
and t.table_name = 'CSJTEST'

 * */

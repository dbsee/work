/**
 * 
 */
package jp.co.csj.tools.utils.poi.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import jp.co.csj.tools.utils.msg.dbtools.CsjDbToolsMsg;

/**
 * @author Think
 *
 */
public class CsjSchedual {


	public static final int RUN_TYPE_WAIT_TIME = 0;
	public static final int RUN_TYPE_RUN_FILE = 1;
	public static final int RUN_TYPE_COPY_FILE = 2;
	public static final int RUN_TYPE_DB_IN = 3;
	public static final int RUN_TYPE_TBL_SYNS = 4;
	public static final int RUN_TYPE_DB_OUT_ALL_TABLE = 5;
	public static final int RUN_TYPE_DB_OUT_ONE_TABLE = 6;
	public static final int RUN_TYPE_DB_OUT_SQL = 7;
	public static final int RUN_TYPE_DB_OUT_CONFIG = 8;
	public static final int RUN_TYPE_TBL_STRUCT_DATA_MERGE = 9;
	public static final int RUN_TYPE_TBL_COMPARE = 10;
	public static final int RUN_TYPE_TBL_STRUCT_OUT = 11;
	public static final int RUN_TYPE_DB_MOVE = 12;
	public static final int RUN_TYPE_CREATE_TBLSQL = 13;
	public static final int RUN_TYPE_MAKE_TBL = 14;
	public static final int RUN_TYPE_WAIT_FILE = 15;
	public static final int RUN_TYPE_DELETE_FILE = 16;
	public static final int RUN_TYPE_CONFIG_FILE = 17;
	public static final int RUN_TYPE_SEND_MAIL = 18;
	/**
	 * @return the no
	 */
	public String getNo() {
		return no;
	}
	/**
	 * @param no the no to set
	 */
	public void setNo(String no) {
		this.no = no;
	}
	private String no="";
	
	private int runType = -1;
	private String runTypeStr="";
	
	// -------------
	private String waitSecond="";
	
	private String runFile="";
	
	// -------------
	private String fromFolder="";
	private String fromFile="";
	private String toFolder="";
	private String toFile="";
	
	// -------------
	private String dbInExcelPath="";
	private String dbInSheet="";
	private String dbInTblNm="";
	private String dbInIsContain="";
	private String dbInResultPath="";
	// -------------
	private String syncExcelPath="";
	private String syncExcelSheet="";
	// -------------
	private String dbOutAllTableOneSheet="";
	private String dbOutAllTableResultPath="";
	// -------------
	private String dbOutOneTableNm="";
	private String dbOutOneTableColNm="";
	private String dbOutOneTableColCharLike="";
	private String dbOutOneTableColNumMax="";
	private String dbOutOneTableColNumMin="";
	private String dbOutOneTableColDateMax="";
	private String dbOutOneTableColDateMin="";
	private String dbOutOneTableResultPath="";
	// -------------
	private String dbOutSqlType="";
	private String dbOutSqlPath="";
	private String dbOutSqlResultPath="";
	
	// -------------
	private String dbOutConfigPath="";
	private String dbOutConfigResultPath="";
	
	// -------------
	private String compareStructOldPath="";
	private String compareStructNewPath="";
	private String compareStructResultPath="";
	
	// -------------
	private String compareDataType="";
	private String compareDataPath="";
	private String compareDbOut="";
	private String compareOnlyChange="";
	private String compareResultPath="";

	// -------------
	private String tblStructOutIniPath="";
	private String tblStructOutIsCompare="";
	private String tblStructOutComparePath="";
	private String tblStructOutResultPath="";
	// -------------
	private String dbChangeType="";
	private String dbChangeResultPath="";
	// -------------
	private String tblScriptOutType="";
	private String tblScriptFromPath="";
	private String tblScriptResultPath="";
	// -------------
	private String createTblTemplateNm="";
	private String createTblFromFile="";
	private String createTblNm="";
	private String createTblType="";
	private String createResultPath="";
	// -------------
	private String waitFile="";
	private String waitFileSecond="";
	// -------------
	private String outputTxtAble="";
	private String outputTxtSplit="";
	private String outputTxtCr="";
	// -------------
	private String delFolderPath="";
	private String delFileNmReg="";
	// -------------
	private String configFile="";
	private String dbType="";
	private String tableSheet="";
	private String splitData="";
	private String recordNum="";
	private String excelType="";
	private String inDb="";
	private String inLog="";
	private String genrateSql="";
	private String insertAble="";
	private String tblNumDisplay="";
	private String errorSkip="";
	private String tblClear="";
	private String checkExeType="";
	private String logicCheck="";
	private String encode="";
	private String exeType="";
	private String batch="";
	
	///-------------
	private String mailFolderPath="";
	private String mailTo="";
	private String mailTitle="";
	private String mailText="";
	private String mailZipNm="";
	private String mailZipPwd="";
	
	
	/**
	 * @return the splitData
	 */
	public String getSplitData() {
		return splitData;
	}
	/**
	 * @param splitData the splitData to set
	 */
	public void setSplitData(String splitData) {
		this.splitData = splitData;
	}
	/**
	 * @return the inDb
	 */
	public String getInDb() {
		return inDb;
	}
	/**
	 * @param inDb the inDb to set
	 */
	public void setInDb(String inDb) {
		this.inDb = inDb;
	}
	/**
	 * @return the inLog
	 */
	public String getInLog() {
		return inLog;
	}
	/**
	 * @param inLog the inLog to set
	 */
	public void setInLog(String inLog) {
		this.inLog = inLog;
	}
	/**
	 * @return the genrateSql
	 */
	public String getGenrateSql() {
		return genrateSql;
	}
	/**
	 * @param genrateSql the genrateSql to set
	 */
	public void setGenrateSql(String genrateSql) {
		this.genrateSql = genrateSql;
	}
	/**
	 * @return the insertAble
	 */
	public String getInsertAble() {
		return insertAble;
	}
	/**
	 * @param insertAble the insertAble to set
	 */
	public void setInsertAble(String insertAble) {
		this.insertAble = insertAble;
	}
	/**
	 * @return the tblNumDisplay
	 */
	public String getTblNumDisplay() {
		return tblNumDisplay;
	}
	/**
	 * @param tblNumDisplay the tblNumDisplay to set
	 */
	public void setTblNumDisplay(String tblNumDisplay) {
		this.tblNumDisplay = tblNumDisplay;
	}
	/**
	 * @return the errorSkip
	 */
	public String getErrorSkip() {
		return errorSkip;
	}
	/**
	 * @param errorSkip the errorSkip to set
	 */
	public void setErrorSkip(String errorSkip) {
		this.errorSkip = errorSkip;
	}
	/**
	 * @return the tblClear
	 */
	public String getTblClear() {
		return tblClear;
	}
	/**
	 * @param tblClear the tblClear to set
	 */
	public void setTblClear(String tblClear) {
		this.tblClear = tblClear;
	}
	/**
	 * @return the logicCheck
	 */
	public String getLogicCheck() {
		return logicCheck;
	}
	/**
	 * @param logicCheck the logicCheck to set
	 */
	public void setLogicCheck(String logicCheck) {
		this.logicCheck = logicCheck;
	}
	/**
	 * @return the batch
	 */
	public String getBatch() {
		return batch;
	}
	/**
	 * @param batch the batch to set
	 */
	public void setBatch(String batch) {
		this.batch = batch;
	}
	/**
	 * @return the tableSheet
	 */
	public String getTableSheet() {
		return tableSheet;
	}
	/**
	 * @param tableSheet the tableSheet to set
	 */
	public void setTableSheet(String tableSheet) {
		this.tableSheet = tableSheet;
	}
	/**
	 * @return the recordNum
	 */
	public String getRecordNum() {
		return recordNum;
	}
	/**
	 * @param recordNum the recordNum to set
	 */
	public void setRecordNum(String recordNum) {
		this.recordNum = recordNum;
	}
	/**
	 * @return the excelType
	 */
	public String getExcelType() {
		return excelType;
	}
	/**
	 * @param excelType the excelType to set
	 */
	public void setExcelType(String excelType) {
		this.excelType = excelType;
	}
	/**
	 * @return the checkExeType
	 */
	public String getCheckExeType() {
		return checkExeType;
	}
	/**
	 * @param checkExeType the checkExeType to set
	 */
	public void setCheckExeType(String checkExeType) {
		this.checkExeType = checkExeType;
	}
	/**
	 * @return the encode
	 */
	public String getEncode() {
		return encode;
	}
	/**
	 * @param encode the encode to set
	 */
	public void setEncode(String encode) {
		this.encode = encode;
	}
	/**
	 * @return the exeType
	 */
	public String getExeType() {
		return exeType;
	}
	/**
	 * @param exeType the exeType to set
	 */
	public void setExeType(String exeType) {
		this.exeType = exeType;
	}
	/**
	 * @return the mailFolderPath
	 */
	public String getMailFolderPath() {
		return mailFolderPath;
	}
	/**
	 * @param mailFolderPath the mailFolderPath to set
	 */
	public void setMailFolderPath(String mailFolderPath) {
		this.mailFolderPath = mailFolderPath;
	}
	/**
	 * @return the mailTo
	 */
	public String getMailTo() {
		return mailTo;
	}
	/**
	 * @param mailTo the mailTo to set
	 */
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
	/**
	 * @return the mailTitle
	 */
	public String getMailTitle() {
		return mailTitle;
	}
	/**
	 * @param mailTitle the mailTitle to set
	 */
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}
	/**
	 * @return the mailText
	 */
	public String getMailText() {
		return mailText;
	}
	/**
	 * @param mailText the mailText to set
	 */
	public void setMailText(String mailText) {
		this.mailText = mailText;
	}
	/**
	 * @return the mailZipNm
	 */
	public String getMailZipNm() {
		return mailZipNm;
	}
	/**
	 * @param mailZipNm the mailZipNm to set
	 */
	public void setMailZipNm(String mailZipNm) {
		this.mailZipNm = mailZipNm;
	}
	/**
	 * @return the mailZipPwd
	 */
	public String getMailZipPwd() {
		return mailZipPwd;
	}
	/**
	 * @param mailZipPwd the mailZipPwd to set
	 */
	public void setMailZipPwd(String mailZipPwd) {
		this.mailZipPwd = mailZipPwd;
	}
	/**
	 * @return the delFolderPath
	 */
	public String getDelFolderPath() {
		return delFolderPath;
	}
	/**
	 * @param delFolderPath the delFolderPath to set
	 */
	public void setDelFolderPath(String delFolderPath) {
		this.delFolderPath = delFolderPath;
	}
	/**
	 * @return the delFileNmReg
	 */
	public String getDelFileNmReg() {
		return delFileNmReg;
	}
	/**
	 * @param delFileNmReg the delFileNmReg to set
	 */
	public void setDelFileNmReg(String delFileNmReg) {
		this.delFileNmReg = delFileNmReg;
	}
	/**
	 * @return the configFile
	 */
	public String getConfigFile() {
		return configFile;
	}
	/**
	 * @param configFile the configFile to set
	 */
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	/**
	 * @return the dbType
	 */
	public String getDbType() {
		return dbType;
	}
	/**
	 * @param dbType the dbType to set
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	/**
	 * @return the waitSecond
	 */
	public String getWaitSecond() {
		return waitSecond;
	}
	/**
	 * @return the runType
	 */
	public int getRunType() {
		return runType;
	}
	/**
	 * @param runType the runType to set
	 */
	public void setRunType(int runType) {
		this.runType = runType;
	}
	/**
	 * @param runType the runType to set
	 */
	public void setRunType(String runTypeStr) {
		
		if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000095).equals(runTypeStr)) {
			this.runType = RUN_TYPE_WAIT_TIME;
		} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000096).equals(runTypeStr)) {
			this.runType = RUN_TYPE_RUN_FILE;
		} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000097).equals(runTypeStr)) {
			this.runType = RUN_TYPE_COPY_FILE;
		} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000098).equals(runTypeStr)) {
			this.runType = RUN_TYPE_DB_IN;
		} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000099).equals(runTypeStr)) {
			this.runType = RUN_TYPE_TBL_SYNS;
		} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000100).equals(runTypeStr)) {
			this.runType = RUN_TYPE_DB_OUT_ALL_TABLE;
		} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000101).equals(runTypeStr)) {
			this.runType = RUN_TYPE_DB_OUT_ONE_TABLE;
		} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000102).equals(runTypeStr)) {
			this.runType = RUN_TYPE_DB_OUT_SQL;
		} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000103).equals(runTypeStr)) {
			this.runType = RUN_TYPE_DB_OUT_CONFIG;
		} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000104).equals(runTypeStr)) {
			this.runType = RUN_TYPE_TBL_STRUCT_DATA_MERGE;
		} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000105).equals(runTypeStr)) {
			this.runType = RUN_TYPE_TBL_COMPARE;
		} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000106).equals(runTypeStr)) {
			this.runType = RUN_TYPE_TBL_STRUCT_OUT;
		} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000107).equals(runTypeStr)) {
			this.runType = RUN_TYPE_DB_MOVE;
		} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000108).equals(runTypeStr)) {
			this.runType = RUN_TYPE_CREATE_TBLSQL;
		} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000109).equals(runTypeStr)) {
			this.runType = RUN_TYPE_MAKE_TBL;
		} else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000160).equals(runTypeStr)) {
			this.runType = RUN_TYPE_WAIT_FILE;
		}else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000173).equals(runTypeStr)) {
			this.runType = RUN_TYPE_DELETE_FILE;
		}else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000176).equals(runTypeStr)) {
			this.runType = RUN_TYPE_CONFIG_FILE;
		}else if (CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000179).equals(runTypeStr)) {
			this.runType = RUN_TYPE_SEND_MAIL;
		}
		this.runTypeStr = runTypeStr;
	}

	public void setMsgInfo(LinkedHashMap<String,LinkedHashMap<String,String>> map) {
		LinkedHashMap<String,String> valMap = new LinkedHashMap<String, String>();
		switch (runType) {
		case RUN_TYPE_WAIT_TIME:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000121), waitSecond);
			break;
		case RUN_TYPE_RUN_FILE:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000122), runFile);
			break;
		case RUN_TYPE_COPY_FILE:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000123), fromFolder);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000124), fromFile);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000125), toFolder);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000126), toFile);
			break;
		case RUN_TYPE_DB_IN:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000127), dbInExcelPath);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000128), dbInSheet);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000129), dbInTblNm);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000130), dbInIsContain);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000120), dbInResultPath);
			break;
		case RUN_TYPE_TBL_SYNS:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000131), syncExcelPath);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000132), syncExcelSheet);
			break;
		case RUN_TYPE_DB_OUT_ALL_TABLE:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000133), dbOutAllTableOneSheet);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000120), dbOutAllTableResultPath);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000168), outputTxtAble);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000169), outputTxtSplit);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000170), outputTxtCr);
			break;
		case RUN_TYPE_DB_OUT_ONE_TABLE:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000134), dbOutOneTableNm);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000135), dbOutOneTableColNm);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000136), dbOutOneTableColCharLike);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000137), dbOutOneTableColNumMax);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000138), dbOutOneTableColNumMin);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000139), dbOutOneTableColDateMax);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000140), dbOutOneTableColDateMin);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000120), dbOutOneTableResultPath);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000168), outputTxtAble);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000169), outputTxtSplit);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000170), outputTxtCr);
			break;
		case RUN_TYPE_DB_OUT_SQL:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000141), dbOutSqlType);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000142), dbOutSqlPath);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000120), dbOutSqlResultPath);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000168), outputTxtAble);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000169), outputTxtSplit);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000170), outputTxtCr);
			break;
		case RUN_TYPE_DB_OUT_CONFIG:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000143), dbOutConfigPath);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000120), dbOutConfigResultPath);
			break;
		case RUN_TYPE_TBL_STRUCT_DATA_MERGE:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000144), compareStructOldPath);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000145), compareStructNewPath);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000120), compareStructResultPath);
			break;
		case RUN_TYPE_TBL_COMPARE:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000127), compareDataPath);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000146), compareDataType);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000147), compareDbOut);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000148), compareOnlyChange);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000120), compareResultPath);
			break;
		case RUN_TYPE_TBL_STRUCT_OUT:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000149), tblStructOutIniPath);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000150), tblStructOutIsCompare);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000151), tblStructOutComparePath);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000120), tblStructOutResultPath);
			break;
		case RUN_TYPE_DB_MOVE:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000152), dbChangeType);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000120), dbChangeResultPath);
			break;
		case RUN_TYPE_CREATE_TBLSQL:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000153), tblScriptOutType);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000154), tblScriptFromPath);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000120), tblScriptResultPath);
			break;
		case RUN_TYPE_MAKE_TBL:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000155), createTblTemplateNm);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000156), createTblFromFile);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000157), createTblNm);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000158), createTblType);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000120), createResultPath);
			break;
		case RUN_TYPE_WAIT_FILE:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000160), waitFile);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000121), waitFileSecond);
			break;
		case RUN_TYPE_DELETE_FILE:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000174), delFolderPath);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000175), delFileNmReg);
			break;
		case RUN_TYPE_CONFIG_FILE:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000177), configFile);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000178), dbType);
			valMap.put(CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_P_0000306), tableSheet);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000180), splitData);
			valMap.put(CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_P_0000106), recordNum);
			valMap.put(CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_P_0000295), excelType);
			valMap.put(CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_P_0000099), inDb);
			valMap.put(CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_P_0000093), inLog);
			valMap.put(CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_P_0000268), genrateSql);
			valMap.put(CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_P_0000097), insertAble);
			valMap.put(CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_P_0000094), tblNumDisplay);
			valMap.put(CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_P_0000110), errorSkip);
			valMap.put(CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_P_0000111), tblClear);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000181), checkExeType);
			valMap.put(CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_P_0000204), logicCheck);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000182), encode);
			valMap.put(CsjDbToolsMsg.picMsgMap.get(CsjDbToolsMsg.MSG_P_0000194), exeType);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000183), batch);
			
//			private String configFile="";
//			private String dbType="";
//			private String tableSheet="";
//			private String splitData="";
//			private String recordNum="";
//			private String excelType="";
//			private String inDb="";
//			private String inLog="";
//			private String genrateSql="";
//			private String insertAble="";
//			private String tblNumDisplay="";
//			private String errorSkip="";
//			private String tblClear="";
//			private String checkExeType="";
//			private String logicCheck="";
//			private String encode="";
//			private String exeType="";
//			private String batch="";
//			private String logLevel="";
			break;
		case RUN_TYPE_SEND_MAIL:
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000185), mailFolderPath);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000186), mailTo);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000187), mailTitle);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000188), mailText);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000189), mailZipNm);
			valMap.put(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000190), mailZipPwd);
			
//			private String mailFolderPath="";
//			private String mailTo="";
//			private String mailTitle="";
//			private String mailText="";
//			private String mailZipNm="";
//			private String mailZipPwd="";
			break;
		default:
			break;
		}
		map.put(no+":"+runTypeStr, valMap);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		switch (runType) {
		case RUN_TYPE_WAIT_TIME:
			return "["+no+":"+runTypeStr
			+ ", waitSecond=" + waitSecond+"]";
		case RUN_TYPE_RUN_FILE:
			return "["+no+":"+runTypeStr
			+ ", runFile=" + runFile+"]";
		case RUN_TYPE_COPY_FILE:
			return "["+no+":"+runTypeStr
			+ ", fromFolder=" + fromFolder + ", fromFile=" + fromFile
			+ ", toFolder=" + toFolder + ", toFile=" + toFile+"]";
		case RUN_TYPE_DB_IN:
			return "["+no+":"+runTypeStr
			+ ", dbInExcelPath=" + dbInExcelPath + ", dbInSheet="
			+ dbInSheet + ", dbInTblNm=" + dbInTblNm + ", dbInIsContain="
			+ dbInIsContain + ", dbInResultPath=" + dbInResultPath+"]";
		case RUN_TYPE_TBL_SYNS:
			return "["+no+":"+runTypeStr
			+ ", syncExcelPath=" + syncExcelPath + ", syncExcelSheet="
			+ syncExcelSheet+"]";
		case RUN_TYPE_DB_OUT_ALL_TABLE:
			return "["+no+":"+runTypeStr+ ", dbOutAllTableOneSheet="
			+ dbOutAllTableOneSheet + ", dbOutAllTableResultPath="
			+ dbOutAllTableResultPath+"]";
		case RUN_TYPE_DB_OUT_ONE_TABLE:
			return "["+no+":"+runTypeStr
			+ ", dbOutOneTableNm="
			+ dbOutOneTableNm + ", dbOutOneTableColNm="
			+ dbOutOneTableColNm + ", dbOutOneTableColCharLike="
			+ dbOutOneTableColCharLike + ", dbOutOneTableColNumMax="
			+ dbOutOneTableColNumMax + ", dbOutOneTableColNumMin="
			+ dbOutOneTableColNumMin + ", dbOutOneTableColDateMax="
			+ dbOutOneTableColDateMax + ", dbOutOneTableColDateMin="
			+ dbOutOneTableColDateMin + ", dbOutOneTableResultPath="
			+ dbOutOneTableResultPath+"]";
		case RUN_TYPE_DB_OUT_SQL:
			return "["+no+":"+runTypeStr
			 + ", dbOutSqlType=" + dbOutSqlType
				+ ", dbOutSqlPath=" + dbOutSqlPath + ", dbOutSqlResultPath="
				+ dbOutSqlResultPath+"]";
		case RUN_TYPE_DB_OUT_CONFIG:
			return "["+no+":"+runTypeStr
			+ ", dbOutConfigPath=" + dbOutConfigPath
			+ ", dbOutConfigResultPath=" + dbOutConfigResultPath+"]";
		case RUN_TYPE_TBL_STRUCT_DATA_MERGE:
			return "["+no+":"+runTypeStr
			+ ", compareStructOldPath=" + compareStructOldPath
			+ ", compareStructNewPath=" + compareStructNewPath
			+ ", compareStructResultPath=" + compareStructResultPath+"]";
		case RUN_TYPE_TBL_COMPARE:
			return "["+no+":"+runTypeStr
			+ ", compareDataType=" + compareDataType + ", compareDbOut="
			+ compareDbOut + ", compareOnlyChange=" + compareOnlyChange
			+ ", compareResultPath=" + compareResultPath+"]";
		case RUN_TYPE_TBL_STRUCT_OUT:
			return "["+no+":"+runTypeStr
			+ ", tblStructOutIniPath="+ tblStructOutIniPath + ", tblStructOutIsCompare="
			+ tblStructOutIsCompare + ", tblStructOutComparePath="
			+ tblStructOutComparePath + ", tblStructOutResultPath="
			+ tblStructOutResultPath +"]";
		case RUN_TYPE_DB_MOVE:
			return "["+no+":"+runTypeStr
			 + ", dbChangeType=" + dbChangeType
			+ ", dbChangeResultPath=" + dbChangeResultPath+"]";
		case RUN_TYPE_CREATE_TBLSQL:
			return "["+no+":"+runTypeStr
			+ ", tblScriptOutType=" + tblScriptOutType
			+ ", tblScriptFromPath=" + tblScriptFromPath
			+ ", tblScriptResultPath=" + tblScriptResultPath+"]";
		case RUN_TYPE_MAKE_TBL:
			return "["+no+":"+runTypeStr
			+ ", createTblTemplateNm=" + createTblTemplateNm
			+ ", createTblFromFile=" + createTblFromFile + ", createTblNm="
			+ createTblNm + ", createTblType=" + createTblType
			+ ", createResultPath=" + createResultPath+"]";
		case RUN_TYPE_WAIT_FILE:
			return "["+no+":"+runTypeStr
			+ ", waitFileSecond=" + waitFileSecond
			+ ", waitFile=" + waitFile+"]";
			
		case RUN_TYPE_DELETE_FILE:
			return "["+no+":"+runTypeStr
			+ ", delFolderPath=" + delFolderPath
			+ ", delFileNmReg=" + delFileNmReg+"]";
		case RUN_TYPE_CONFIG_FILE:
			return "["+no+":"+runTypeStr
			+ ", configFile=" + configFile
			+ ", dbType=" + dbType
			+ ", tableSheet=" + tableSheet
			+ ", splitData=" + splitData
			+ ", recordNum=" + recordNum
			+ ", excelType=" + excelType
			+ ", inDb=" + inDb
			+ ", inLog=" + inLog
			+ ", genrateSql=" + genrateSql
			+ ", insertAble=" + insertAble
			+ ", tblNumDisplay=" + tblNumDisplay
			+ ", tblClear=" + tblClear
			+ ", checkExeType=" + checkExeType
			+ ", logicCheck=" + logicCheck
			+ ", encode=" + encode
			+ ", exeType=" + exeType
			+ ", batch=" + batch+"]";

		case RUN_TYPE_SEND_MAIL:
			return "["+no+":"+runTypeStr
			+ ", mailFolderPath=" + mailFolderPath
			+ ", mailTo=" + mailTo
			+ ", mailTitle=" + mailTitle
			+ ", mailText=" + mailText
			+ ", mailZipNm=" + mailZipNm
			+ ", mailZipPwd=" + mailZipPwd+"]";
		default:
			return "";
		}
	}
	
	/**
	 * @param wbMap
	 * @return
	 */
	public static List<List<String>>getStrLst(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>> wbMap) {
		List<List<String>> strLst = new ArrayList<List<String>>();
		for (Entry<String, LinkedHashMap<String, LinkedHashMap<String, String>>> entrySt : wbMap.entrySet()) {
			String sheetNm = entrySt.getKey();
			List<String> tsLst = new ArrayList<String>();
			tsLst.add(sheetNm);
			tsLst.add("");
			tsLst.add("");
			tsLst.add("");
			strLst.add(tsLst);
			for (Entry<String, LinkedHashMap<String, String>> entryRun: entrySt.getValue().entrySet()) {
				String runType = entryRun.getKey();
				
				List<String> trLst = new ArrayList<String>();
				trLst.add("");
				trLst.add(runType);
				trLst.add("");
				trLst.add("");
				strLst.add(trLst);
				
				for (Entry<String,String> paraEntry : entryRun.getValue().entrySet()) {
					List<String> tpLst = new ArrayList<String>();
					tpLst.add("");
					tpLst.add("");
					tpLst.add(paraEntry.getKey());
					tpLst.add(paraEntry.getValue());
					strLst.add(tpLst);
				}
			}
		}
		return strLst;
	}
	/**
	 * @return the runTypeStr
	 */
	public String getRunTypeStr() {
		return runTypeStr;
	}
	/**
	 * @param runTypeStr the runTypeStr to set
	 */
	public void setRunTypeStr(String runTypeStr) {
		this.runTypeStr = runTypeStr;
	}
	/**
	 * @return the runFile
	 */
	public String getRunFile() {
		return runFile;
	}
	/**
	 * @param runFile the runFile to set
	 */
	public void setRunFile(String runFile) {
		this.runFile = runFile;
	}
	/**
	 * @return the fromFolder
	 */
	public String getFromFolder() {
		return fromFolder;
	}
	/**
	 * @param fromFolder the fromFolder to set
	 */
	public void setFromFolder(String fromFolder) {
		this.fromFolder = fromFolder;
	}
	/**
	 * @return the fromFile
	 */
	public String getFromFile() {
		return fromFile;
	}
	/**
	 * @param fromFile the fromFile to set
	 */
	public void setFromFile(String fromFile) {
		this.fromFile = fromFile;
	}
	/**
	 * @return the toFolder
	 */
	public String getToFolder() {
		return toFolder;
	}
	/**
	 * @param toFolder the toFolder to set
	 */
	public void setToFolder(String toFolder) {
		this.toFolder = toFolder;
	}
	/**
	 * @return the toFile
	 */
	public String getToFile() {
		return toFile;
	}
	/**
	 * @param toFile the toFile to set
	 */
	public void setToFile(String toFile) {
		this.toFile = toFile;
	}
	/**
	 * @return the dbInExcelPath
	 */
	public String getDbInExcelPath() {
		return dbInExcelPath;
	}
	/**
	 * @param dbInExcelPath the dbInExcelPath to set
	 */
	public void setDbInExcelPath(String dbInExcelPath) {
		this.dbInExcelPath = dbInExcelPath;
	}
	/**
	 * @return the dbInSheet
	 */
	public String getDbInSheet() {
		return dbInSheet;
	}
	/**
	 * @param dbInSheet the dbInSheet to set
	 */
	public void setDbInSheet(String dbInSheet) {
		this.dbInSheet = dbInSheet;
	}
	/**
	 * @return the dbInTblNm
	 */
	public String getDbInTblNm() {
		return dbInTblNm;
	}
	/**
	 * @param dbInTblNm the dbInTblNm to set
	 */
	public void setDbInTblNm(String dbInTblNm) {
		this.dbInTblNm = dbInTblNm;
	}
	/**
	 * @return the dbInIsContain
	 */
	public String getDbInIsContain() {
		return dbInIsContain;
	}
	/**
	 * @param dbInIsContain the dbInIsContain to set
	 */
	public void setDbInIsContain(String dbInIsContain) {
		this.dbInIsContain = dbInIsContain;
	}
	/**
	 * @return the dbInResultPath
	 */
	public String getDbInResultPath() {
		return dbInResultPath;
	}
	/**
	 * @param dbInResultPath the dbInResultPath to set
	 */
	public void setDbInResultPath(String dbInResultPath) {
		this.dbInResultPath = dbInResultPath;
	}
	/**
	 * @return the syncExcelPath
	 */
	public String getSyncExcelPath() {
		return syncExcelPath;
	}
	/**
	 * @param syncExcelPath the syncExcelPath to set
	 */
	public void setSyncExcelPath(String syncExcelPath) {
		this.syncExcelPath = syncExcelPath;
	}
	/**
	 * @return the syncExcelSheet
	 */
	public String getSyncExcelSheet() {
		return syncExcelSheet;
	}
	/**
	 * @param syncExcelSheet the syncExcelSheet to set
	 */
	public void setSyncExcelSheet(String syncExcelSheet) {
		this.syncExcelSheet = syncExcelSheet;
	}
	/**
	 * @return the dbOutAllTableOneSheet
	 */
	public String getDbOutAllTableOneSheet() {
		return dbOutAllTableOneSheet;
	}
	/**
	 * @param dbOutAllTableOneSheet the dbOutAllTableOneSheet to set
	 */
	public void setDbOutAllTableOneSheet(String dbOutAllTableOneSheet) {
		this.dbOutAllTableOneSheet = dbOutAllTableOneSheet;
	}
	/**
	 * @return the dbOutAllTableResultPath
	 */
	public String getDbOutAllTableResultPath() {
		return dbOutAllTableResultPath;
	}
	/**
	 * @param dbOutAllTableResultPath the dbOutAllTableResultPath to set
	 */
	public void setDbOutAllTableResultPath(String dbOutAllTableResultPath) {
		this.dbOutAllTableResultPath = dbOutAllTableResultPath;
	}
	/**
	 * @return the dbOutOneTableNm
	 */
	public String getDbOutOneTableNm() {
		return dbOutOneTableNm;
	}
	/**
	 * @param dbOutOneTableNm the dbOutOneTableNm to set
	 */
	public void setDbOutOneTableNm(String dbOutOneTableNm) {
		this.dbOutOneTableNm = dbOutOneTableNm;
	}
	/**
	 * @return the dbOutOneTableColNm
	 */
	public String getDbOutOneTableColNm() {
		return dbOutOneTableColNm;
	}
	/**
	 * @param dbOutOneTableColNm the dbOutOneTableColNm to set
	 */
	public void setDbOutOneTableColNm(String dbOutOneTableColNm) {
		this.dbOutOneTableColNm = dbOutOneTableColNm;
	}
	/**
	 * @return the dbOutOneTableColCharLike
	 */
	public String getDbOutOneTableColCharLike() {
		return dbOutOneTableColCharLike;
	}
	/**
	 * @param dbOutOneTableColCharLike the dbOutOneTableColCharLike to set
	 */
	public void setDbOutOneTableColCharLike(String dbOutOneTableColCharLike) {
		this.dbOutOneTableColCharLike = dbOutOneTableColCharLike;
	}
	/**
	 * @return the dbOutOneTableColNumMax
	 */
	public String getDbOutOneTableColNumMax() {
		return dbOutOneTableColNumMax;
	}
	/**
	 * @param dbOutOneTableColNumMax the dbOutOneTableColNumMax to set
	 */
	public void setDbOutOneTableColNumMax(String dbOutOneTableColNumMax) {
		this.dbOutOneTableColNumMax = dbOutOneTableColNumMax;
	}
	/**
	 * @return the dbOutOneTableColNumMin
	 */
	public String getDbOutOneTableColNumMin() {
		return dbOutOneTableColNumMin;
	}
	/**
	 * @param dbOutOneTableColNumMin the dbOutOneTableColNumMin to set
	 */
	public void setDbOutOneTableColNumMin(String dbOutOneTableColNumMin) {
		this.dbOutOneTableColNumMin = dbOutOneTableColNumMin;
	}
	/**
	 * @return the dbOutOneTableColDateMax
	 */
	public String getDbOutOneTableColDateMax() {
		return dbOutOneTableColDateMax;
	}
	/**
	 * @param dbOutOneTableColDateMax the dbOutOneTableColDateMax to set
	 */
	public void setDbOutOneTableColDateMax(String dbOutOneTableColDateMax) {
		this.dbOutOneTableColDateMax = dbOutOneTableColDateMax;
	}
	/**
	 * @return the dbOutOneTableColDateMin
	 */
	public String getDbOutOneTableColDateMin() {
		return dbOutOneTableColDateMin;
	}
	/**
	 * @param dbOutOneTableColDateMin the dbOutOneTableColDateMin to set
	 */
	public void setDbOutOneTableColDateMin(String dbOutOneTableColDateMin) {
		this.dbOutOneTableColDateMin = dbOutOneTableColDateMin;
	}
	/**
	 * @return the dbOutOneTableResultPath
	 */
	public String getDbOutOneTableResultPath() {
		return dbOutOneTableResultPath;
	}
	/**
	 * @param dbOutOneTableResultPath the dbOutOneTableResultPath to set
	 */
	public void setDbOutOneTableResultPath(String dbOutOneTableResultPath) {
		this.dbOutOneTableResultPath = dbOutOneTableResultPath;
	}
	/**
	 * @return the dbOutSqlType
	 */
	public String getDbOutSqlType() {
		return dbOutSqlType;
	}
	/**
	 * @param dbOutSqlType the dbOutSqlType to set
	 */
	public void setDbOutSqlType(String dbOutSqlType) {
		this.dbOutSqlType = dbOutSqlType;
	}
	/**
	 * @return the dbOutSqlPath
	 */
	public String getDbOutSqlPath() {
		return dbOutSqlPath;
	}
	/**
	 * @param dbOutSqlPath the dbOutSqlPath to set
	 */
	public void setDbOutSqlPath(String dbOutSqlPath) {
		this.dbOutSqlPath = dbOutSqlPath;
	}
	/**
	 * @return the dbOutSqlResultPath
	 */
	public String getDbOutSqlResultPath() {
		return dbOutSqlResultPath;
	}
	/**
	 * @param dbOutSqlResultPath the dbOutSqlResultPath to set
	 */
	public void setDbOutSqlResultPath(String dbOutSqlResultPath) {
		this.dbOutSqlResultPath = dbOutSqlResultPath;
	}
	/**
	 * @return the dbOutConfigPath
	 */
	public String getDbOutConfigPath() {
		return dbOutConfigPath;
	}
	/**
	 * @param dbOutConfigPath the dbOutConfigPath to set
	 */
	public void setDbOutConfigPath(String dbOutConfigPath) {
		this.dbOutConfigPath = dbOutConfigPath;
	}
	/**
	 * @return the dbOutConfigResultPath
	 */
	public String getDbOutConfigResultPath() {
		return dbOutConfigResultPath;
	}
	/**
	 * @param dbOutConfigResultPath the dbOutConfigResultPath to set
	 */
	public void setDbOutConfigResultPath(String dbOutConfigResultPath) {
		this.dbOutConfigResultPath = dbOutConfigResultPath;
	}
	/**
	 * @return the compareStructOldPath
	 */
	public String getCompareStructOldPath() {
		return compareStructOldPath;
	}
	/**
	 * @param compareStructOldPath the compareStructOldPath to set
	 */
	public void setCompareStructOldPath(String compareStructOldPath) {
		this.compareStructOldPath = compareStructOldPath;
	}
	/**
	 * @return the compareStructNewPath
	 */
	public String getCompareStructNewPath() {
		return compareStructNewPath;
	}
	/**
	 * @param compareStructNewPath the compareStructNewPath to set
	 */
	public void setCompareStructNewPath(String compareStructNewPath) {
		this.compareStructNewPath = compareStructNewPath;
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
	 * @return the compareDataType
	 */
	public String getCompareDataType() {
		return compareDataType;
	}
	/**
	 * @param compareDataType the compareDataType to set
	 */
	public void setCompareDataType(String compareDataType) {
		this.compareDataType = compareDataType;
	}
	/**
	 * @return the compareDbOut
	 */
	public String getCompareDbOut() {
		return compareDbOut;
	}
	/**
	 * @param compareDbOut the compareDbOut to set
	 */
	public void setCompareDbOut(String compareDbOut) {
		this.compareDbOut = compareDbOut;
	}
	/**
	 * @return the compareOnlyChange
	 */
	public String getCompareOnlyChange() {
		return compareOnlyChange;
	}
	/**
	 * @param compareOnlyChange the compareOnlyChange to set
	 */
	public void setCompareOnlyChange(String compareOnlyChange) {
		this.compareOnlyChange = compareOnlyChange;
	}
	/**
	 * @return the compareResultPath
	 */
	public String getCompareResultPath() {
		return compareResultPath;
	}
	/**
	 * @param compareResultPath the compareResultPath to set
	 */
	public void setCompareResultPath(String compareResultPath) {
		this.compareResultPath = compareResultPath;
	}
	/**
	 * @return the tblStructOutIniPath
	 */
	public String getTblStructOutIniPath() {
		return tblStructOutIniPath;
	}
	/**
	 * @param tblStructOutIniPath the tblStructOutIniPath to set
	 */
	public void setTblStructOutIniPath(String tblStructOutIniPath) {
		this.tblStructOutIniPath = tblStructOutIniPath;
	}
	/**
	 * @return the tblStructOutIsCompare
	 */
	public String getTblStructOutIsCompare() {
		return tblStructOutIsCompare;
	}
	/**
	 * @param tblStructOutIsCompare the tblStructOutIsCompare to set
	 */
	public void setTblStructOutIsCompare(String tblStructOutIsCompare) {
		this.tblStructOutIsCompare = tblStructOutIsCompare;
	}
	/**
	 * @return the tblStructOutComparePath
	 */
	public String getTblStructOutComparePath() {
		return tblStructOutComparePath;
	}
	/**
	 * @param tblStructOutComparePath the tblStructOutComparePath to set
	 */
	public void setTblStructOutComparePath(String tblStructOutComparePath) {
		this.tblStructOutComparePath = tblStructOutComparePath;
	}
	/**
	 * @return the tblStructOutResultPath
	 */
	public String getTblStructOutResultPath() {
		return tblStructOutResultPath;
	}
	/**
	 * @param tblStructOutResultPath the tblStructOutResultPath to set
	 */
	public void setTblStructOutResultPath(String tblStructOutResultPath) {
		this.tblStructOutResultPath = tblStructOutResultPath;
	}
	/**
	 * @return the dbChangeType
	 */
	public String getDbChangeType() {
		return dbChangeType;
	}
	/**
	 * @param dbChangeType the dbChangeType to set
	 */
	public void setDbChangeType(String dbChangeType) {
		this.dbChangeType = dbChangeType;
	}
	/**
	 * @return the dbChangeResultPath
	 */
	public String getDbChangeResultPath() {
		return dbChangeResultPath;
	}
	/**
	 * @param dbChangeResultPath the dbChangeResultPath to set
	 */
	public void setDbChangeResultPath(String dbChangeResultPath) {
		this.dbChangeResultPath = dbChangeResultPath;
	}
	/**
	 * @return the tblScriptOutType
	 */
	public String getTblScriptOutType() {
		return tblScriptOutType;
	}
	/**
	 * @param tblScriptOutType the tblScriptOutType to set
	 */
	public void setTblScriptOutType(String tblScriptOutType) {
		this.tblScriptOutType = tblScriptOutType;
	}
	/**
	 * @return the tblScriptFromPath
	 */
	public String getTblScriptFromPath() {
		return tblScriptFromPath;
	}
	/**
	 * @param tblScriptFromPath the tblScriptFromPath to set
	 */
	public void setTblScriptFromPath(String tblScriptFromPath) {
		this.tblScriptFromPath = tblScriptFromPath;
	}
	/**
	 * @return the tblScriptResultPath
	 */
	public String getTblScriptResultPath() {
		return tblScriptResultPath;
	}
	/**
	 * @param tblScriptResultPath the tblScriptResultPath to set
	 */
	public void setTblScriptResultPath(String tblScriptResultPath) {
		this.tblScriptResultPath = tblScriptResultPath;
	}
	/**
	 * @return the createTblTemplateNm
	 */
	public String getCreateTblTemplateNm() {
		return createTblTemplateNm;
	}
	/**
	 * @param createTblTemplateNm the createTblTemplateNm to set
	 */
	public void setCreateTblTemplateNm(String createTblTemplateNm) {
		this.createTblTemplateNm = createTblTemplateNm;
	}
	/**
	 * @return the createTblFromFile
	 */
	public String getCreateTblFromFile() {
		return createTblFromFile;
	}
	/**
	 * @param createTblFromFile the createTblFromFile to set
	 */
	public void setCreateTblFromFile(String createTblFromFile) {
		this.createTblFromFile = createTblFromFile;
	}
	/**
	 * @return the createTblNm
	 */
	public String getCreateTblNm() {
		return createTblNm;
	}
	/**
	 * @param createTblNm the createTblNm to set
	 */
	public void setCreateTblNm(String createTblNm) {
		this.createTblNm = createTblNm;
	}
	/**
	 * @return the createTblType
	 */
	public String getCreateTblType() {
		return createTblType;
	}
	/**
	 * @param createTblType the createTblType to set
	 */
	public void setCreateTblType(String createTblType) {
		this.createTblType = createTblType;
	}
	/**
	 * @return the createResultPath
	 */
	public String getCreateResultPath() {
		return createResultPath;
	}
	/**
	 * @param createResultPath the createResultPath to set
	 */
	public void setCreateResultPath(String createResultPath) {
		this.createResultPath = createResultPath;
	}
	/**
	 * @param waitSecond the waitSecond to set
	 */
	public void setWaitSecond(String waitSecond) {
		this.waitSecond = waitSecond;
	}
	/**
	 * @return the compareDataPath
	 */
	public String getCompareDataPath() {
		return compareDataPath;
	}
	/**
	 * @param compareDataPath the compareDataPath to set
	 */
	public void setCompareDataPath(String compareDataPath) {
		this.compareDataPath = compareDataPath;
	}
	/**
	 * @return the waitFile
	 */
	public String getWaitFile() {
		return waitFile;
	}
	/**
	 * @param waitFile the waitFile to set
	 */
	public void setWaitFile(String waitFile) {
		this.waitFile = waitFile;
	}
	/**
	 * @return the waitFileSecond
	 */
	public String getWaitFileSecond() {
		return waitFileSecond;
	}
	/**
	 * @param waitFileSecond the waitFileSecond to set
	 */
	public void setWaitFileSecond(String waitFileSecond) {
		this.waitFileSecond = waitFileSecond;
	}
	/**
	 * @return the outputTxtAble
	 */
	public String getOutputTxtAble() {
		return outputTxtAble;
	}
	/**
	 * @param outputTxtAble the outputTxtAble to set
	 */
	public void setOutputTxtAble(String outputTxtAble) {
		this.outputTxtAble = outputTxtAble;
	}
	/**
	 * @return the outputTxtSplit
	 */
	public String getOutputTxtSplit() {
		return outputTxtSplit;
	}
	/**
	 * @param outputTxtSplit the outputTxtSplit to set
	 */
	public void setOutputTxtSplit(String outputTxtSplit) {
		this.outputTxtSplit = outputTxtSplit;
	}
	/**
	 * @return the outputTxtCr
	 */
	public String getOutputTxtCr() {
		return outputTxtCr;
	}
	/**
	 * @param outputTxtCr the outputTxtCr to set
	 */
	public void setOutputTxtCr(String outputTxtCr) {
		this.outputTxtCr = outputTxtCr;
	}
	
	
}

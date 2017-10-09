/**
 *
 */
package jp.co.csj.tools.utils.common.constant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jp.co.csj.tools.utils.common.CsjProcess;
import jp.co.csj.tools.utils.msg.dbtools.CsjDbToolsMsg;
import jp.co.csj.tools.utils.reg.RegConstStr;


/**
 * @author Administrator
 *
 */
public class CsjConst {

	public static final String s_version = " -- ver 17.09.09 -- ";
	public static final String COMPANY_SITE = "http://www.sowinfo.co.jp//";
	public static final String AUTHOR_MAIL = "cuishuangjia@gmail.com";
	public static final String DOWNSITE = "https://sourceforge.net/projects/penguinsdbtools/";
	public static final String COPY_RIGHT ="Copyright (C) 2016-2019 sowinfo All Rights Reserved.        ";
	public static void main(String[] args) {
//		for (int i = 0; i < 100; i++) {
//			System.out.println(CsjRandom.createRadomNum(CsjConst.SIGN_RANDOM.length, true));
//
//		}
	}
	
	public static final String CMP_DB_INS   = CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000191);//"I   db->";
	public static final String CMP_DB_DEL   = CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000192);//"D   db->";
	public static final String CMP_DB_UPT   = CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000193);//"U   db->";
	public static final String CMP_FILE_UPT = CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000194);//"U file->";
	

	public static final String DB_SIGN_UPDATE = "UPDATE";
	public static final String DB_SIGN_SELECT = "SELECT";
	public static List<String> signLstLeft = new ArrayList<String>();
	public static List<String> signLstRight = new ArrayList<String>();
	public static final String PENGUINS_DBTOOLS_NEW_USER = "NEW USER";
	public static final String PENGUINS_DBTOOLS_NM = "Penguins DbTools";
	
	public static final String SCHEDUAL_SHEET_NM = "SCHEDUAL";
	public static final String EMPTY = "";
	public static final String MAIL_CR = "<br>";
	public static final String EXCEL_DOT_XLS_1997 = ".xls";
	public static final String EXCEL_DOT_XLSX_2007 = ".xlsx";
	public static final int EXCEL_DOT_XLSX_2007_MAX_COL = 16384-1;
	public static final int EXCEL_DOT_XLSX_1997_MAX_COL = 256-1;
	public static final String SCHEDUAL_DOT_CNC = ".cnc";
	public static final String SCHEDUAL_DOT_LIKE_CNC = "*.cnc";
	public static final String FILE_DOT_ZIP = ".zip";
	
	public static final String EXCEL_LIKE_DOT_XLS = "*.*";
	public static final String USER_COMMAND = "user command";
	public static final String NOTHING = "NOTHING$$$";

	public static final String[] SIGN_RANDOM ={"{","}","[","]","(",")","!","+","/","`"};
	public static final String MAIL_ADRRESS_AUTHOR = "cuishuangjia@gmail.com";//"82059821@qq.com";//
	public static final String MAIL_ADRRESS_SERVER02 = "dbtoolsserver02@gmail.com";//"82059821@qq.com";//
	public static final String SIGN_CIRCLE_HOLLOW_1 = "○";
	public static final String SIGN_BATU_1 = "×";
	public static final String SIGN_DB = "DB";
	public static final String SIGN_TBL_DATE_EXCEL = "TBL_DATE_EXCEL";
	public static final String SIGN_TBL_STRUCT_EXCEL = "TBL_STRUCT_EXCEL";
	
	public static final String SIGN_CIRCLE_HOLLOW_2 = "◎";
	public static final String SIGN_INS = "INS";
	public static final String SIGN_HOSI = "*";
	public static final String SIGN_CIRCLE_SOLID = "●";
	public static final String SIGN_SEMICOLON = ";";
	public static final String SIGN_RECTANGLE_SOLID = "■";
	public static final String SIGN_HOSI_SOLID = "★";
	public static final String SIGN_SPACE_1 = " ";
	public static final char CHAR_SIGN_SPACE_1 = ' ';
	public static final String SIGN_NUMBER_0 = "0";
	public static final String SIGN_SPACE_4 = "    ";
	public static final String SIGN_BRACKETS_B_L = "{";
	public static final String SIGN_BRACKETS_B_R = "}";
	public static final String SIGN_BRACKETS_M_L = "[";
	public static final String SIGN_BRACKETS_M_R = "]";
	public static final String SIGN_BRACKETS_S_L = "(";
	public static final String SIGN_BRACKETS_S_R = ")";
	public static final String SIGN_ENTER_N = "\n";
	public static final String SIGN_ENTER_RN = "\r\n";
	public static final String SIGN_ENTER_R = "\r";
	public static final String SIGN_COMMA = ",";
	public static final String SIGN_RIGHT = "⇒";
	public static final String SIGN_AT = "@";
	public static final String SIGN_SLASH = "/";
	public static final String SIGN_MINUS = "-";
	public static final String SIGN_DOWN_LINE = "_";
	public static final String SIGN_COLON = ":";
	public static final String SIGN_DOT = ".";
	public static final String SIGN_DOUBLE = "\"";
	public static final String SIGN_SINGLE_1 = "`";
	public static final String SIGN_SINGLE_2 = "'";
	public static final String SIGN_TAB = "\t";
	public static final String SIGN_SEPARATOR_3 = "$$$";
	public static final String SIGN_SEP_LINE_3 = "|||";
	public static final String SIGN_SEPARATOR_SET = "$$$";
	public static final String SIGN_SLASH_2 = "//";
	public static final String SIGN_SLASH_3 = "///";
	public static final String SIGN_WAVE = "~";
	public static final String SIGN_ARR_N = "「n」";
	public static final String SIGN_Arr = "Arr";
	public static final String SIGN_L_LINE_START = "/*";
	public static final String SIGN_R_LINE_START = "*/";
	public static final String SIGN_BIGER = ">";
	public static final String SIGN_SMALLER = "<";


	public static final String SIGN_HEAD_01 = "";
	public static final String SIGN_PRIVATE = "private";
	public static final String SIGN_PUBLIC = "private";
	public static final String SIGN_PROTECTED = "protected";
	public static final String SIGN_CLASS = "class";
	public static final String SIGN_IMPLEMENTS = "implements";
	public static final String SIGN_INTERFACE = "interface";
	public static final String SIGN_PACKAGE = "package";
	public static final String SIGN_IMPORT = "import";
	public static final String SIGN_SRC = "src";
	public static final String SIGN_AUTOCODE = "AutoCode";
	public static final String SIGN_LIKE = "like";
	public static final String SIGN_NOT = "not";
	public static final String SIGN_NULL = "null";
	public static final String SIGN_DEFAULT = "DEFAULT";
	public static final String SIGN_KEY_SPLIT = "$$$$$$$$$$";
	public static final String SIGN_COMMENT = "COMMENT";
	public static final String SIGN_KEY_SPLIT_2 = "@#";


	/** 符号:":" */
	public final static String MASK_COLON = ":";
	/** 符号:"⇒" */
	public final static String MASK_TO_RIGHT = "⇒";

	/**
	 * 全角標識について
	 * */
	public static final String Z_SIGN_SPACE_1 = "　";
	public static final String Z_SIGN_BRACKETS_B_L = "｛";
	public static final String Z_SIGN_BRACKETS_B_R = "｝";
	public static final String Z_SIGN_BRACKETS_M_L_J = "「";
	public static final String Z_SIGN_BRACKETS_M_R_J = "」";
	public static final String Z_SIGN_BRACKETS_M_L_JP = "【";
	public static final String Z_SIGN_BRACKETS_M_R_JP = "】";
	public static final String Z_SIGN_BRACKETS_M_L_ZH = "［";
	public static final String Z_SIGN_BRACKETS_M_R_ZH = "］";
	public static final String Z_SIGN_BRACKETS_S_L = "（";
	public static final String Z_SIGN_BRACKETS_S_R = "）";
	public static final String Z_SIGN_WAVE = "～";

	/**
	 * 時間について
	 * */
	public static final String YYYY_MM_DD_HH_MM_SS_SLASH_24 = "yyyy/MM/dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM_SS_SLASH_24_SSS = "yyyy/MM/dd HH:mm:ss:SSS";
	public static final String YYYY_MM_DD_HH_MM_SS_SLASH_12 = "yyyy/MM/dd KK:mm:ss a";
	public static final String YYYY_MM_DD_SLASH = "yyyy/MM/dd";
	public static final String YYYY_MM_DD = "yyyyMMdd";
	public static final String YYYY_MM_DD_HH_MM_SS_MINUS_24 = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM_SS_MINUS_24_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
	public static final String HH_MM_SS_MINUS_24 = "HHmmss";
	public static final String YYYY_MM_DD_HH_MM_SS_MINUS_12 = "yyyy-MM-dd KK:mm:ss a";
	public static final String YY_MM_DD_HH_MM_SS_MINUS_24 = "yy/MM/dd";
	public static final String YY_MM_DD_HH_MM_SS_SLASH_24 = "yy/MM/dd HH:mm:ss:SSS";
	public static final String YYYY_MM_DD_MINUS = "yyyy-MM-dd";
	public static final String YYYY_MM_DD_HH_MM_SS_ALL_MINUS_24 = "yyyy-MM-dd HH-mm-ss";
	public static final String YYYYMMDDHHMMSSMINUS_24 = "yyyyMMddHHmmss";
	public static final String YYYYMMDDHH_MMSSMINUS_24 = "yyyyMMdd_HHmmss_SSS";
	public static final String YYYYMMDDHH_HHMMSS_24 = "yyyyMMdd_HHmmss";
	public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
	public static final String SIGN_HTML_SUMMARY_BEGIN = "<summary>";
	public static final String SIGN_HTML_SUMMARY_END = "</summary>";
	public static final String SIGN_HTML_BR = "<br/>";

	public static final String JP_TYPE = CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000012);
	public static final String JP_LENGTH = CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000045);
	public static final String JP_EXTRA = "EXTRA";
	public static final String JP_INIT_VAL = CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000008);

	public static final String JP_REG = CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000046);
	public static final String JP_EQUAL =CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000047);
	public static final String JP_NO_EQUAL =CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000060);
	public static final String JP_MAX_VAL =CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000048);
	public static final String JP_MIN_VAL =CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000049);
	public static final String JP_MAX_LEN = CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000050);
	public static final String JP_MIN_LEN =CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000051);
	public static final String JP_SUMMARY =CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000052);

	public static final String JP_REG_ERROR =CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000053);
	public static final String JP_EQUAL_ERROR = CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000054);
	public static final String JP_NO_EQUAL_ERROR = CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000061);

	public static final String JP_MAX_VAL_ERROR = CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000055);
	public static final String JP_MIN_VAL_ERROR =CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000056);
	public static final String JP_MAX_LEN_ERROR = CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000057);
	public static final String JP_MIN_LEN_ERROR =CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000058);
	public static final String JP_NUM_ERROR =CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000059);
	public static final String JP_DATE_ERROR =CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000062);
	public static final String JP_BLANK_ERROR =CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000063);
	public static final String JP_NUM_ERROR_DOT_HEAD =CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000064);
	public static final String JP_NUM_ERROR_DOT_END =CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000065);
	public static final String JP_MAX_DB_LEN_ERROR =CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000066);
	public static final String JP_MAX_DB_NULL_ERROR =CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000067);
	public static final String EXCEL_FILE_EDITTING = "~$";


	public static HashSet<String> trimChSet = new HashSet<String>();
	static {
		signLstLeft.add(SIGN_BRACKETS_B_L);
		signLstLeft.add(SIGN_BRACKETS_M_L);
		signLstLeft.add(SIGN_BRACKETS_S_L);
		signLstLeft.add(Z_SIGN_BRACKETS_B_L);
		signLstLeft.add(Z_SIGN_BRACKETS_M_L_J);
		signLstLeft.add(Z_SIGN_BRACKETS_M_L_JP);
		signLstLeft.add(Z_SIGN_BRACKETS_M_L_ZH);
		signLstLeft.add(Z_SIGN_BRACKETS_S_L);

		signLstRight.add(SIGN_BRACKETS_B_R);
		signLstRight.add(SIGN_BRACKETS_M_R);
		signLstRight.add(SIGN_BRACKETS_S_R);
		signLstRight.add(Z_SIGN_BRACKETS_B_R);
		signLstRight.add(Z_SIGN_BRACKETS_M_R_J);
		signLstRight.add(Z_SIGN_BRACKETS_M_R_JP);
		signLstRight.add(Z_SIGN_BRACKETS_M_R_ZH);
		signLstRight.add(Z_SIGN_BRACKETS_S_R);

		trimChSet.add(RegConstStr.SIGN_N);
		trimChSet.add(RegConstStr.SIGN_R);
		trimChSet.add(RegConstStr.SIGN_T);
		trimChSet.add(CsjProcess.s_newLine);
	}
}


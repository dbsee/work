/**
 *
 */
package jp.co.csj.tools.utils.common;

import org.mydbsee.common.CmnDateUtil;

import jp.co.csj.tools.utils.common.constant.CsjConst;

/**
 * @author Think
 *
 */
public class CsjPath {

	public static String DB_TYPE  = "";
	public static void resetTime() {
		s_current_date = CmnDateUtil.getCurrentDateString(CsjConst.YYYYMMDDHH_MMSSMINUS_24);
		s_current_date_end = "_at_" + s_current_date;
		s_current_date_pre = s_current_date + "_";
		s_current_date_mid = "_"+s_current_date + "_";
		s_path_pj_autocode =CsjProcess.s_pj_path + CsjConst.SIGN_AUTOCODE   +s_current_date_end + CsjProcess.s_f_s;
		s_path_auto_pj_AutoDB_db_to_xls = s_path_auto_pj_AutoDB + "db_to_excel" +s_current_date_end+ CsjProcess.s_f_s;
		s_path_auto_pj_AutoDB_log = s_path_auto_pj_AutoDB +DB_TYPE + CsjProcess.s_f_s+ "sqlLog" + s_current_date_end + CsjProcess.s_f_s;
//		s_path_check_table_path =  s_path_auto_pj_AutoDB + "excel_to_excel"+ s_current_date_end + CsjProcess.s_f_s;
		s_path_xls_2_table_path =  s_path_auto_pj_AutoDB  +DB_TYPE + CsjProcess.s_f_s+ "excel_to_table" + s_current_date_end+CsjProcess.s_f_s;
		s_path_db_2_layout = s_path_auto_pj_AutoDB +DB_TYPE + CsjProcess.s_f_s+ "layout" + s_current_date_end + CsjProcess.s_f_s;

	}
	public static String log4j_file_path="";
	public static String s_path_auto_pj = CsjProcess.s_pj_path + "pj_set_info" + CsjProcess.s_f_s;

	public static String s_file_db_info_path = "dbInfo"+ CsjProcess.s_f_s;
	public static String s_file_db_info = "dbInfo"+ CsjProcess.s_f_s + "dbInfo.xml";
	public static String s_file_db_info_config = "dbInfo"+ CsjProcess.s_f_s + "config.xml";

	public static String s_path_db_info = "dbInfo"+ CsjProcess.s_f_s +"template"+ CsjProcess.s_f_s;
	public static String s_file_auto_pj_read_me = "ReadMe"+CsjConst.EXCEL_DOT_XLSX_2007;
	public static String s_file_msg_path = "msg"+ CsjProcess.s_f_s + "msg"+CsjConst.EXCEL_DOT_XLS_1997;

	public static String s_file_auto_pj_pic_wall = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "wall.jpg";
	public static String s_file_auto_pj_pic_runpenguin = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "runpenguin.gif";

	
	public static String s_file_auto_pj_pic_db_00 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db00_32.png";
	public static String s_file_auto_pj_pic_db_01 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db01_16.png";
	public static String s_file_auto_pj_pic_db_02 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db02_16.png";
	public static String s_file_auto_pj_pic_db_03 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db03_16.png";
	public static String s_file_auto_pj_pic_db_04 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db04_16.png";
	public static String s_file_auto_pj_pic_db_05 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db05_16.png";
	public static String s_file_auto_pj_pic_db_06 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db06_16.png";
	public static String s_file_auto_pj_pic_db_07 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db07_16.png";
	public static String s_file_auto_pj_pic_db_08 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db08_16.png";
	public static String s_file_auto_pj_pic_db_09 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db09_16.png";
	public static String s_file_auto_pj_pic_db_10 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db10_16.png";
	public static String s_file_auto_pj_pic_db_11 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db11_16.png";
	public static String s_file_auto_pj_pic_db_12 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db12_16.png";
	public static String s_file_auto_pj_pic_db_12_110 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db12_110.png";
	public static String s_file_auto_pj_pic_db_13 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db13_16.png";
	public static String s_file_auto_pj_pic_db_14 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db14_16.png";
	public static String s_file_auto_pj_pic_db_15 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db15_16.png";
	public static String s_file_auto_pj_pic_db_16 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db16_16.png";
	public static String s_file_auto_pj_pic_db_17 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db17_16.png";
	public static String s_file_auto_pj_pic_db_18 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db18_16.png";
	public static String s_file_auto_pj_pic_db_19 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db19_16.png";
	public static String s_file_auto_pj_pic_db_20 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db20_16.png";
	public static String s_file_auto_pj_pic_db_21 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db21_16.png";
	public static String s_file_auto_pj_pic_db_22 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db22_16.png";
	public static String s_file_auto_pj_pic_db_23 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db23_16.png";
	public static String s_file_auto_pj_pic_db_24 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db24_16.png";
	public static String s_file_auto_pj_pic_db_25 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db25_16.png";
	public static String s_file_auto_pj_pic_db_26 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db26_16.png";
	public static String s_file_auto_pj_pic_db_27 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db27_16.png";
	public static String s_file_auto_pj_pic_db_28 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db28_16.png";
	public static String s_file_auto_pj_pic_db_29 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db29_16.png";
	public static String s_file_auto_pj_pic_db_30 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db30_16.png";
	public static String s_file_auto_pj_pic_db_31 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db31_16.png";
	public static String s_file_auto_pj_pic_db_32 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db32_16.png";
	public static String s_file_auto_pj_pic_db_33 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db33_16.png";
	public static String s_file_auto_pj_pic_db_34 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db34_16.png";
	public static String s_file_auto_pj_pic_db_35 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db35_16.png";
	public static String s_file_auto_pj_pic_db_36 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db36_16.png";
	public static String s_file_auto_pj_pic_db_37 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db37_16.png";
	public static String s_file_auto_pj_pic_db_38 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db38_16.png";
	public static String s_file_auto_pj_pic_db_39 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db39_16.png";
	public static String s_file_auto_pj_pic_db_40 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db40_16.png";
	public static String s_file_auto_pj_pic_db_41 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db41_16.png";
	public static String s_file_auto_pj_pic_db_42 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db42.jpg";
	public static String s_file_auto_pj_pic_db_43 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db43.jpg";
	public static String s_file_auto_pj_pic_db_44 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db44_96.png";
	public static String s_file_auto_pj_pic_db_45 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db45_16.png";
	public static String s_file_auto_pj_pic_db_46 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db46_16.png";
	public static String s_file_auto_pj_pic_db_47 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db47_16.png";
	public static String s_file_auto_pj_pic_db_48 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db48_16.png";
	public static String s_file_auto_pj_pic_db_49 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db49_16.png";
	public static String s_file_auto_pj_pic_db_50 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db50_16.png";
	public static String s_file_auto_pj_pic_db_51 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db51_16.png";
	public static String s_file_auto_pj_pic_db_52 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db52_16.png";
	public static String s_file_auto_pj_pic_db_53 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db53_16.png";
	public static String s_file_auto_pj_pic_db_54 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db54_16.png";
	public static String s_file_auto_pj_pic_db_55 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db55_16.png";
	public static String s_file_auto_pj_pic_db_56 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db56_16.png";
	public static String s_file_auto_pj_pic_db_57 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db57_16.png";
	public static String s_file_auto_pj_pic_db_58 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db58_16.png";
	public static String s_file_auto_pj_pic_db_59 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db59_16.png";
	public static String s_file_auto_pj_pic_db_60 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db60_16.png";
	public static String s_file_auto_pj_pic_db_61 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db61_16.png";
	public static String s_file_auto_pj_pic_db_62 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db62_16.png";
	public static String s_file_auto_pj_pic_db_63 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db63_16.png";
	public static String s_file_auto_pj_pic_db_64 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db64_16.png";
	public static String s_file_auto_pj_pic_db_65 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db65_16.png";
	public static String s_file_auto_pj_pic_db_66 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db66_16.png";
	public static String s_file_auto_pj_pic_db_67 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db67_16.png";
	public static String s_file_auto_pj_pic_db_68 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db68_16.png";
	public static String s_file_auto_pj_pic_db_69 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db69_16.png";
	public static String s_file_auto_pj_pic_db_70 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db70_16.png";
	public static String s_file_auto_pj_pic_db_71 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db71_16.png";
	public static String s_file_auto_pj_pic_db_72 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db72_16.png";
	public static String s_file_auto_pj_pic_db_73 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db73_16.png";
	public static String s_file_auto_pj_pic_db_74 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db74_16.png";
	public static String s_file_auto_pj_pic_db_75 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db75_16.png";
	public static String s_file_auto_pj_pic_db_76 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db76_16.png";
	public static String s_file_auto_pj_pic_db_77 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db77_16.png";
	public static String s_file_auto_pj_pic_db_78 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db78_16.png";
	public static String s_file_auto_pj_pic_db_79 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db79_16.png";
	public static String s_file_auto_pj_pic_db_80 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db80_16.png";
	public static String s_file_auto_pj_pic_db_81 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db81_16.png";
	public static String s_file_auto_pj_pic_db_82 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db82_16.png";
	public static String s_file_auto_pj_pic_db_83 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db83_16.png";
	public static String s_file_auto_pj_pic_db_84 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db84_16.png";
	public static String s_file_auto_pj_pic_db_85 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db85_16.png";
	public static String s_file_auto_pj_pic_db_86 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db86_16.png";
	public static String s_file_auto_pj_pic_db_87 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db87_16.png";
	public static String s_file_auto_pj_pic_db_88 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db88_16.png";
	public static String s_file_auto_pj_pic_db_89 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db89_16.png";
	public static String s_file_auto_pj_pic_db_90 = s_path_auto_pj + "pic" + CsjProcess.s_f_s + "db90_16.png";
	
	

	public static String s_path_pj_path_temp = "tmp" + CsjProcess.s_f_s ;
	
	
	public static String s_file_sql_history = "history"+ CsjProcess.s_f_s + "SqlHistory"+CsjConst.EXCEL_DOT_XLS_1997;
	public static String s_file_db_keywork = s_path_db_info+ "KeyWord"+CsjConst.EXCEL_DOT_XLS_1997;
	public static String s_file_db_read_tables = s_path_db_info+ "ReadTables"+CsjConst.EXCEL_DOT_XLS_1997;
	public static String s_file_db_convert = s_path_db_info+ "DbConvert"+CsjConst.EXCEL_DOT_XLS_1997;
	public static String s_file_db_table_maker = s_path_db_info+ "TableMaker"+CsjConst.EXCEL_DOT_XLS_1997;
	//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
	public static String s_file_table_temp =s_path_db_info + "TableStructTemp";
	public static String s_file_db_cellStyle = s_path_db_info+ "CellStyle";
	public static String s_file_pj_path_temp = s_path_db_info  + "Temp";
	public static String s_file_schdual = s_path_db_info+ "schdual.txt";
	//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
	
	public static String s_path_auto_pj_AutoDB = "AutoDb" + CsjProcess.s_f_s;//CsjProcess.s_pj_path +
	public static String s_path_auto_pj_AutoDB_db_to_xls = s_path_auto_pj_AutoDB + "db_to_excel" + CsjProcess.s_f_s;
	public static String s_path_auto_pj_AutoDB_xls_to_db = s_path_auto_pj_AutoDB + "excel_to_db" + CsjProcess.s_f_s;
	public static String s_path_auto_pj_AutoDB_tlbs_cmp = "";
	public static String s_current_date = "";
	public static String s_current_date_end = "_at_" + s_current_date;
	public static String s_current_date_pre = s_current_date + "_";
	public static String s_current_date_mid = "_"+s_current_date + "_";

	public static String s_path_auto_pj_AutoDB_log = s_path_auto_pj_AutoDB + "sqlLog" + s_current_date_end + CsjProcess.s_f_s;
	public static String s_path_pj_autocode = CsjProcess.s_pj_path + CsjConst.SIGN_AUTOCODE   +s_current_date_end + CsjProcess.s_f_s;
//	public static String s_path_check_table_path =  s_path_auto_pj_AutoDB + "excel_to_excel" + CsjProcess.s_f_s;
	public static String s_path_xls_2_table_path =  s_path_auto_pj_AutoDB + "excel_to_table" + CsjProcess.s_f_s;
	public static String s_path_db_2_layout =  s_path_auto_pj_AutoDB + "layout" + CsjProcess.s_f_s;
	// D:\javaTools\eclipse\workspace\CsjToolsCore\pj_set_info\Ibats\ini\db\oracle
}

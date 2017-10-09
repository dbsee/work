package org.mydbsee.common;

import jp.co.csj.tools.utils.common.CsjProcess;

public interface IConstFile {

	// 文件后缀
	public static final String DOT_INI =".ini";
	public static final String DOT_INI_BAK = ".ini_bk";

	//public static final String DOT_XMLBK =".xmlbk";
	public static final int FILE_MAX_LEN = 200-CsjProcess.s_pj_path_length;
	// 文件编码形式
	public static final String ENCODE_UTF_8 = "utf-8";
	public static final String ENCODE_SHIFT_JIS = "shift_jis";
	public static final String ENCODE_GBK = "GBK";
	public static final String ENCODE_GB2312 = "GB2312";

	// ファイルパースまたはファイル
	public static final int IS_PATH = 0;
	public static final int IS_FILE = 1;
	public static final int IS_FILES = 2;
	public static final int IS_ERROR = -1;


}

/**
 *
 */
package org.mydbsee.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.log.constant.CsjLogConst;

/**
 * @author Think
 *
 */
public class CmnLog5j {
	
	public static BufferedWriter s_log5j = null;
	
	public static void main(String[] args) {
		try {
			initLog5j("c:\\1\\","a.txt",IConstFile.ENCODE_UTF_8);
			s_log5j.write("1111");
			s_log5j.write("\r\n");
			s_log5j.write("2222");
			closeLog5j();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}

	public  static void initLog5j(String logPath,String fileNm, String enCode) throws Throwable {
		File file = new File(logPath);
		file.mkdirs();
		s_log5j = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(logPath + fileNm), enCode));
	}
	public  static void initLog5j(String filePath, String enCode) throws Throwable {
		File file = new File(filePath);
		s_log5j = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(filePath), enCode));
	}
	public static void closeLog5j() {
		try {
			if (s_log5j != null) {
				s_log5j.close();
				s_log5j=null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void writeLine(String str) {
		CmnFileUtils.writeWithbBlank(s_log5j, str, 0);
	}
	public static void writeWithConsole(String str) {
		CmnFileUtils.writeWithbBlank(s_log5j, str, 0);
		System.out.println(str);
	}
	public static String getXlsPosLog(String filePath, String sheetNm, String row,String col, String content) {
		StringBuffer sb = new StringBuffer();
		if (CmnStrUtils.isNotEmpty(filePath)) {
			sb.append(CsjLogConst.SIGN_FILE_PATH + addlrBracketsM(filePath,true) );
		}
		if (CmnStrUtils.isNotEmpty(sheetNm)) {
			sb.append(CsjLogConst.SIGN_SHEET_NM + addlrBracketsM(sheetNm,true) );
		}
		if (CmnStrUtils.isNotEmpty(row)) {
			sb.append(CsjLogConst.SIGN_ROW + addlrBracketsM(row,true) );
		}
		if (CmnStrUtils.isNotEmpty(col)) {
			sb.append(CsjLogConst.SIGN_COL + addlrBracketsM(col,true) );
		}
		if (CmnStrUtils.isNotEmpty(content)) {
			sb.append(CsjLogConst.SIGN_CONTENT + addlrBracketsM(content,true) );
		}
		return addlrBracket_M_L_JP(sb.toString(),true);
	}

	public static String addlrBracketsM(String str,boolean isBlankNotPrint) {

		if (CmnStrUtils.isEmpty(str)) {
			if (isBlankNotPrint) {
				return "";
			}
		}
		return CsjConst.SIGN_BRACKETS_M_L + str + CsjConst.SIGN_BRACKETS_M_R;
	}
	public static String addlrBracket_M_L_JP(String str, boolean isBlankNotPrint) {
		if (CmnStrUtils.isEmpty(str)) {
			if (isBlankNotPrint) {
				return "";
			}
		}
		return CsjConst.Z_SIGN_BRACKETS_M_L_JP + str + CsjConst.Z_SIGN_BRACKETS_M_R_JP;
	}

	public static String addlrBracketsS(String str, boolean isBlankNotPrint) {
		if (CmnStrUtils.isEmpty(str)) {
			if (isBlankNotPrint) {
				return "";
			}
		}
		return CsjConst.SIGN_BRACKETS_S_L + str + CsjConst.SIGN_BRACKETS_S_R;
	}
	/**
	 * @param writer
	 * @param line
	 */
	public static void writeLine(BufferedWriter writer, String line) throws Throwable {
		try {
			writer.write(line);
			writer.newLine();
			writer.flush();
		} catch (Throwable e) {
			throw e;
		}
		
	}
	/**
	 * @param writer
	 * @param line
	 */
	public static void writeLineWithConsole(BufferedWriter writer, String line) throws Throwable {
		try {
			writer.write(line);
			writer.newLine();
			writer.flush();
			System.out.println(line);
		} catch (Throwable e) {
			throw e;
		}
		
	}
	/**
	 * @param writer
	 * @throws IOException 
	 */
	public static void close(BufferedWriter writer) throws Throwable {
		// TODO Auto-generated method stub
		try {
			if (writer!=null) {
				writer.close();
				writer=null;
			}
		} catch (Throwable e) {
			throw e;
		}

	}
}

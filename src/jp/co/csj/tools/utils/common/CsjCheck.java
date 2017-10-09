/**
 *
 */
package jp.co.csj.tools.utils.common;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.mydbsee.common.CmnDateUtil;
import org.mydbsee.common.CmnFileUtils;
import org.mydbsee.common.CmnLanIPUtils;
import org.mydbsee.common.CmnLog;
import org.mydbsee.common.IConstFile;

import jp.co.csj.tools.utils.common.constant.CsjConst;

/**
 * @author Think
 *
 */
public class CsjCheck {

	public static String errorInfoRegedit = "regedit error!\nif you want to use,please mail to ["+CsjConst.MAIL_ADRRESS_AUTHOR+"]";
	public static String nowDate = CmnDateUtil
			.getCurrentDateString(CsjConst.YYYYMMDDHHMMSSMINUS_24);
	public static String successInfo = "Congraduation To You!";
//	public static String limitDate = "20171031";
	public static String limitDate = "20181031";
	public static String checkDate = "";
	public static boolean isCheckError = true;
	public static boolean isPayed = false;
	public static long s_commit_count = 1000;
	public static String local_key = "";
	public static boolean isTimeOver(String paraNowDate) throws Throwable {

		boolean isOver = true;
		if (paraNowDate.compareTo(limitDate) < 0) {// nowDate.compareTo("20120101")
													// > 0 &&
			isOver = false;
		}
		return isOver;
	}

	public static void deleteKey() {
		File f = new File(CmnLanIPUtils.s_file_key_file);
		f.delete();
		
	}

	public static void getNetDate() {
		try {
			URLConnection conn = new URL("http://www.baidu.com").openConnection();
			String dateStr = conn.getHeaderField("Date");
			//System.out.println("获取到的服务器时间：" + dateStr);

			// 解析为北京时间：GMT+8
			DateFormat httpDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.US);
			httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			Date date = httpDateFormat.parse(dateStr);
			nowDate = CmnDateUtil.getFormatDateTime(date, CsjConst.YYYYMMDDHHMMSSMINUS_24);
		} catch (Throwable e) {
		}

	}
	public static String getKeyDateVal() throws Throwable {
		isCheckError = true;
		String retVal = "";//"unregediter";
		try {

			LinkedHashMap<String, List<String>> fileMap = CmnFileUtils.getKeyFileMap(
					CmnLanIPUtils.s_file_key_file, IConstFile.ENCODE_UTF_8);
			if (fileMap.containsKey(CsjProcess.s_local_inet_addr)) {
				List<String> list=fileMap.get(CsjProcess.s_local_inet_addr);

				String strDate =list.get(1);
				String strPay =list.get(0);

					CmnDateUtil.getDate(strDate, CsjConst.YYYY_MM_DD);
					isPayed = "payed".equals(strPay.substring(6));
					if (nowDate.substring(0, 8).compareTo(strDate.substring(6)) <= 0) {

						retVal = strDate.substring(6);
						checkDate = retVal;
						isCheckError = false;
					}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.info(e.getMessage());
		}
		return retVal;
	}
}

package jp.co.csj.utils.exe.net;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mydbsee.common.CmnLog;

/**
 *
 * @author Administrator
 */
public class SyncTime {

	private static int sleepMinutes = 0;
	private static final long EPOCH_OFFSET_MILLIS;
	private static final String[] hostName = {"time-a.nist.gov", "time-nw.nist.gov", "time.nist.gov"};


	static {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		// Java使用的参照标准是1970年，而时间服务器返回的秒是相当1900年的，算一下偏移
		calendar.set(1900, Calendar.JANUARY, 1, 0, 0, 0);
		EPOCH_OFFSET_MILLIS = Math.abs(calendar.getTime().getTime());
	}

	public static void main(String[] args) {
		// 检测电脑是否在线
		while (offLine() && sleepMinutes < 30) {
			try {
				Thread.sleep(10);
				sleepMinutes += 2;
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				CmnLog.logger.info(ex.getMessage());
				Logger.getLogger(SyncTime.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		// 30分钟还没有联线，表示就不上网了，退出吧
		if (sleepMinutes >= 30)
		{
			System.exit(0);
		}

		// 从网络上获取时间
		Date date = null;
		for (int i = 0; i < hostName.length; i++) {
			date = getNetDate(hostName[i]);
			if (date != null) {
				break;
			}
		}

		// 修改本机时间
		if (date != null) {
			setComputeDate(date);
		}
	}

	private static Date getNetDate(String hostName) {
		Date date = null;
		long result = 0;
		try {
			Socket socket = new Socket(hostName, 37);
			BufferedInputStream bis = new BufferedInputStream(socket.getInputStream(),
					socket.getReceiveBufferSize());
			int b1 = bis.read();
			int b2 = bis.read();
			int b3 = bis.read();
			int b4 = bis.read();
			if ((b1 | b2 | b3 | b3) < 0) {
				return null;
			}
			result = (((long) b1) << 24) + (b2 << 16) + (b3 << 8) + b4;
			date = new Date(result * 1000 - EPOCH_OFFSET_MILLIS);
			socket.close();
		} catch (UnknownHostException ex) {
			Logger.getLogger(SyncTime.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(SyncTime.class.getName()).log(Level.SEVERE, null, ex);
		}
		return date;
	}

	/**
	 * 通过ping命令判断是否离线
	 * @return
	 */
	private static boolean offLine() {
		Runtime run = Runtime.getRuntime();
		try {
			Process process = run.exec("ping www.hao123.com");
			InputStream s = process.getInputStream();
			BufferedReader bis = new BufferedReader(new InputStreamReader(s));
			String str = bis.readLine();
			while (str != null) {
				if (str.startsWith("Reply from")) {
					return false;
				}
				str = bis.readLine();
			}
			process.waitFor();
		} catch (IOException ex) {
			Logger.getLogger(SyncTime.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
			Logger.getLogger(SyncTime.class.getName()).log(Level.SEVERE, null, ex);
		}
		return true;
	}

	/**
	 * 通过调用本地命令date和time修改计算机时间
	 * @param date
	 */
	private static void setComputeDate(Date date) {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);

		c.setTime(new Date());
		int year_c = c.get(Calendar.YEAR);
		int month_c = c.get(Calendar.MONTH) + 1;
		int day_c = c.get(Calendar.DAY_OF_MONTH);
		int hour_c = c.get(Calendar.HOUR_OF_DAY);
		int minute_c = c.get(Calendar.MINUTE);

		String ymd = year + "-" + month + "-" + day;
		String time = hour + ":" + minute + ":" + second;
		try {
			// 日期不一致就修改一下日期
			if (year != year_c || month != month_c || day != day_c) {
				String cmd = "cmd /c date " + ymd;
				Process process = Runtime.getRuntime().exec(cmd);
				process.waitFor();
			}

			// 时间不一致就修改一下时间
			if (hour != hour_c || minute != minute_c) {
				String cmd = "cmd  /c  time " + time;
				Process process = Runtime.getRuntime().exec(cmd);
				process.waitFor();
			}
		} catch (IOException ex) {
			Logger.getLogger(SyncTime.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
			Logger.getLogger(SyncTime.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}

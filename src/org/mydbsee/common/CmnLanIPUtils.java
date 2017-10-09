package org.mydbsee.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import jp.co.csj.tools.utils.common.CsjCheck;
import jp.co.csj.tools.utils.common.CsjProcess;
import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.key.CsjDesEncrypt;
import jp.co.csj.tools.utils.reg.RegConstStr;
import jp.co.csj.utils.exe.net.CsjMacAddress;

/**
 * 使用java线程扫描局域网ip简单方案
 *
 * @author Administrator
 *
 */
public class CmnLanIPUtils {

	public static long step = 0;
	public static String proty = CsjProcess.s_pj_path + "properties"
			+ CsjProcess.s_f_s;
	public static String keyNm = "key.dll";
	public static String keyTNm = "keyT.dll";
	public static String s_file_key_T_file = "properties" + CsjProcess.s_f_s
			+ keyTNm;
	public static String s_file_key_file = "properties" + CsjProcess.s_f_s
			+ "key.dll";
	public static boolean isAccessNet=false;

	/**
	 * @throws Throwable
	 */
	public static void writeLimitDate() throws Throwable {

		List<String> strList = new ArrayList<String>();
		strList.add(CsjDesEncrypt.encode(enLongStrKey(CsjCheck.limitDate, 10)));
		String folder = CsjProcess.s_pj_path + "properties"
				+ CsjProcess.s_f_s;
		System.out.println(folder);
		CmnFileUtils.writeFile(folder, keyTNm, IConstFile.ENCODE_UTF_8,
				strList);
	}

	/**
	 * @throws Throwable
	 */
	public static void writeLocalMacKey(String key) throws Throwable {
		List<String> strList = new ArrayList<String>();
		strList.add(key);
		CmnFileUtils
				.writeFile(proty, keyNm, IConstFile.ENCODE_UTF_8, strList);
	}

	/**
	 * @param isPayMoney
	 * @param code
	 * @return
	 * @throws Throwable
	 */
	public static String getOneKey(boolean isPayMoney, String strMac,
			String strDate) throws Throwable {
		// System.out.println(CsjProcess.s_local_inet_addr + "<--");
		String stepStr = CmnRandomUtils.createRadomStrs(6, true, false);
		String strPre = "unpay";
		if (isPayMoney) {
			strPre = "payed";
		}

		strMac = strMac.replaceAll("-", "");
		String strMac1 = strMac.substring(0, 6);
		String strMac2 = strMac.substring(6, 12);
		String str = strMac + CsjConst.SIGN_AT + strMac1 + strPre
				+ CsjConst.SIGN_AT + strMac2 + strDate + CsjConst.SIGN_AT
				+ stepStr;
		System.out.println(strMac);
		System.out.println(strPre);
		System.out.println(strDate);
		System.out.println(stepStr);
		System.out.println(str);


		str = enLongStrKey(str,10);
		str = CsjDesEncrypt.encode(str);
		System.out.println(str);
		return str;
	}

	/**
	 * @param str
	 * @param level
	 * @return
	 */
	public static String enLongStrKey(String str, int level) {
		for (int i = 0; i < CsjConst.SIGN_RANDOM.length; i++) {
			for (int index = 0; index < level; index++) {
				int num = CmnRandomUtils.createRadomNum(CsjConst.SIGN_RANDOM.length, true);
				str = CmnStrUtils.insertChar(str,CsjConst.SIGN_RANDOM[num],CmnRandomUtils.createRadomNum(str.length(), true)%str.length());
			}
		}
		return str;
	}

	/**
	 * @param strLine
	 * @return
	 */
	public static String unLongStrKey(String strLine) {
		for (int i = 0; i < CsjConst.SIGN_RANDOM.length; i++) {
			strLine=CmnStrUtils.funReplace(strLine, CsjConst.SIGN_RANDOM[i], "");
		}
		return strLine;
	}

	public static void main(String[] args) {
		String strMac = "00-26-C7-72-DC-D4";

		strMac = strMac.replaceAll("-", "");
		String strMac1 = strMac.substring(0, 6);
		String strMac2 = strMac.substring(6, 12);
		System.out.println(strMac1);
		System.out.println(strMac2);
		System.out.println("abcdefghi".substring(3));

		// if ("192.168.20.123s".matches(RegConstStr.IP)) {
		// System.out.println("ok");
		// }

		try {
			writeLimitDate();
			// writeLocalMacKey(getOneKey());

			System.out.println("REGEDIT SUCCESS");
			// writeLanMacKey("c:\\", "key.dll",CsjFileConst.ENCODE_UTF_8);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CmnLog.logger.info(e.getMessage());
			System.out.println("REGEDIT ERROR");
		}
	}

	/**
	 * @param isPayMoney
	 * @param code
	 * @return
	 * @throws Throwable
	 */
	public static String getOneKeyUncode(boolean isPayMoney) throws Throwable {
		// System.out.println(CsjProcess.s_local_inet_addr + "<--");
		String code = CsjDesEncrypt.encode(CsjProcess.s_local_inet_addr_with_line);
		String limitDate = getKeyTValLine0();
		String strDate = limitDate;
		String strPre = "unpay";
		if (isPayMoney) {
			strPre = "payed";
		}
		String strMac = CsjProcess.s_local_inet_addr_with_line.replaceAll("-", "");
		String strMac1 = strMac.substring(0, 6);
		String strMac2 = strMac.substring(6, 12);

		String str = CsjDesEncrypt.encode(strMac + CsjConst.SIGN_AT + strMac1
				+ strPre + CsjConst.SIGN_AT + strMac2 + strDate
				+ CsjConst.SIGN_AT + CmnRandomUtils.createRadomStrs(6, true, false));

		return str;
	}

	/**
	 * @param isPayMoney
	 * @param code
	 * @return
	 * @throws Throwable
	 */
	public static String regeditKey(String mac, String strDate)
			throws Throwable {
		// System.out.println(CsjProcess.s_local_inet_addr + "<--");
		String str = mac + "payed" + strDate + 000000;
		str = CsjDesEncrypt.encode(str);
		return str;
	}

	public static String getKeyTValLine0() throws Throwable {


		String key = CmnFileUtils.getFileContent(
				new File(s_file_key_T_file), IConstFile.ENCODE_UTF_8).get(0);
		String unkey = CsjDesEncrypt.uncode(key);
		return unLongStrKey(unkey);
	}
	public static void isAccessNet() throws Throwable {

		if (isAccessNet) {
			List<String> lst = CmnFileUtils.getFileContent(
					new File(s_file_key_T_file), IConstFile.ENCODE_UTF_8);
			if (lst.size()>1) {
				isAccessNet = !lst.get(1).equals("NN");
			}
		}
	}

	public static String getKeyDateVal() throws Throwable {
		return CsjDesEncrypt.uncode(CmnFileUtils.getFileContent(
				new File(s_file_key_file), IConstFile.ENCODE_UTF_8).get(0));
	}

	/**
	 * @throws Throwable
	 *
	 */
	public static void writeLanMacKey() throws Throwable {
		System.out.println("begin......");
		CmnLanIPUtils ip = new CmnLanIPUtils();
		List<String> list = ip.getLanIPArrayList();
		List<String> macList = ip.getLanIPArrayList();

		System.out.println("----------------    list    ----------------");

		Thread.sleep(5000);
		for (String str : list) {
			String mac = CsjMacAddress.run(str);
			if (CmnStrUtils.isNotEmpty(mac) && mac.matches(RegConstStr.MAC)) {
				String stepStr = CmnStrUtils.leftFillChar(
						String.valueOf(step++), '0', 6);
				String strDate = CsjCheck.limitDate + stepStr;
				macList.add(CsjDesEncrypt.encode(mac)
						+ CsjDesEncrypt.encode(strDate));
			}
		}
		CmnFileUtils
				.writeFile(proty, keyNm, IConstFile.ENCODE_UTF_8, macList);
		System.out.println("最后有多少个===>" + list.size());

	}

	public ArrayList<String> getLanIPArrayList() {
		ArrayList<String> arrayIP = null;
		try {
			InitSystem initSystem = null;
			initSystem = new InitSystem();
			Thread thread = new Thread(initSystem);
			thread.start();
			thread.join();
			arrayIP = initSystem.getArrayIPUsed();
		} catch (UnknownHostException e) {
			CmnLog.logger.info(e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			CmnLog.logger.info(e.getMessage());
			e.printStackTrace();
		}
		return arrayIP;
	}

	private class InitSystem implements Runnable {
		private int firstIP = 2;// 查询的 IP 地址的最后一位起始点

		private int lastIP = 255;// 查询的 IP 地址的最后一位结束点

		private volatile ArrayList<Thread> arrayThread;// 子线程段

		private final int MAXTHREADNUM = 30; // 最大同时进行的子线程数量

		private int threadNumNow;// 当前正在进行的子线程数量

		private volatile ArrayList<String> arrayIP;// 局域网查询所有可能的 IP 地址的结果集

		private volatile ArrayList<String> arrayIPUsed;// 局域网查询已经使用的 IP 地址的结果集

		private InitSystem(String ip) {
			System.out.println("IP===>" + ip);
			arrayIP = new ArrayList<String>();
			arrayIPUsed = new ArrayList<String>();
			arrayThread = new ArrayList<Thread>();
			setIPAddressList(ip);
		}

		private InitSystem() throws UnknownHostException {
			this(InetAddress.getLocalHost().getHostAddress());
		}

		private synchronized ArrayList<String> getArrayIPUsed() {
			try {
				System.out.println("getArrayIPUsed:  arrayIP.size===>"
						+ arrayIP.size());
				while (arrayIP.size() > 0) {
					Thread.sleep(300);
				}
			} catch (InterruptedException e) {
				CmnLog.logger.info(e.getMessage());
				e.printStackTrace();
			}
			return arrayIPUsed;
		}

		private void setIPAddressList(String ip) {
			// 根据这个 ip 地址查询它所在的局域网的所有可能 IP 地址的集合
			int lastPointIndex = ip.lastIndexOf('.');
			String stringIPHead = ip.substring(0, ++lastPointIndex);
			System.out.println("stringIPHead===>" + stringIPHead);
			String stringIP = null;
			for (int i = firstIP; i <= lastIP; i++) {
				stringIP = stringIPHead + i;
				arrayIP.add(stringIP);
			}
			System.out.println("进放到这里...arrayIP的总个数：" + arrayIP.size());
		}

		public void run() {
			synchronized (this) {
				try {
					System.out.println("run()  arrayIP.size()===>"
							+ arrayIP.size());
					System.out
							.println("run()  threadNumNow===>" + threadNumNow);
					System.out.println("arrayThread.size()"
							+ arrayThread.size());
					while (arrayIP.size() > 0) {
						while (threadNumNow >= MAXTHREADNUM) {
							System.out.println("线程超出30，中止后面的...");
							for (Thread thread : arrayThread) {
								if (!thread.getState().equals(
										Thread.State.TERMINATED)) {
									thread.join(5);
								}
								--threadNumNow;
							}
							arrayThread = new ArrayList<Thread>();
						}
						Thread thread = new Thread(new InnerClass(arrayIP
								.remove(0)));
						thread.start();
						threadNumNow++;
						arrayThread.add(thread);
					}
				} catch (Throwable e) {
					CmnLog.logger.info(e.getMessage());
					e.printStackTrace();
				}
			}
		}

		private class InnerClass implements Runnable {
			// 线程查询一个 IP 是否是可以连接的 是则加入到相应的 IP 数组
			private String ip;

			private InnerClass(String ip) {
				System.out.println("InnerClass ip===>" + ip);
				this.ip = ip;
			}

			private boolean isUsedIPAddress(String ip) {
				System.out.println("isUsedIPAddress===>" + ip);
				synchronized (this) {
					System.out.println("进入此地.....");
					// 判断这个 IP 地址在当前局域网中是否是可连接的 IP
					Process process = null;
					BufferedReader bufReader = null;
					String bufReadLineString = null;
					try {
						process = Runtime.getRuntime().exec(
								"ping " + ip + " -w 100 -n 1");
						bufReader = new BufferedReader(new InputStreamReader(
								process.getInputStream()));
						for (int i = 0; i < 6 && bufReader != null; i++) {
							bufReader.readLine();
						}
						bufReadLineString = bufReader.readLine();
						System.out.println("bufReadLineString===>"
								+ bufReadLineString);
						if (bufReadLineString == null) {
							process.destroy();
							return false;
						}
						if (bufReadLineString.indexOf("timed out") > 0
								|| bufReadLineString.length() < 17
								|| bufReadLineString.indexOf("invalid") > 0) {
							process.destroy();
							return false;
						}
					} catch (IOException e) {
						CmnLog.logger.info(e.getMessage());
						e.printStackTrace();
					}
					process.destroy();
					return true;
				}
			}

			public void run() {
				synchronized (this) {
					if (isUsedIPAddress(ip)) {
						arrayIPUsed.add(ip);
					}
				}
			}
		}
	}
}
//
// 利用IP地址获得局域网计算机的名字、mac地址、工作组
//
// System.out.println("192.168.1.187对应网卡的MAC是:");
//
// NetworkInterface
// ne=NetworkInterface.getByInetAddress(InetAddress.getByName("192.168.1.187"));
//
// byte[]mac=ne.getHardwareAddress();
//
// String mac_s=hexByte(mac[0])+":"+hexByte(mac[1])+":"+
// hexByte(mac[2])+":"+hexByte(mac[3])+":"+
// hexByte(mac[4])+":"+hexByte(mac[5]);System.out.println(mac_s);
//
// 程序运行结果: 192.168.1.187对应网卡的MAC是: 00:0c:f1:20:75:58

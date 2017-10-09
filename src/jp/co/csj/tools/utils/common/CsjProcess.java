/**
 *
 */
package jp.co.csj.tools.utils.common;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;

import org.mydbsee.common.CmnDateUtil;
import org.mydbsee.common.CmnLanIPUtils;
import org.mydbsee.common.CmnLog;
import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.utils.exe.net.ExternalIpAddressFetcher;

/**
 * @author Think
 *
 */
public class CsjProcess {

	public static String s_pj_path = CsjConst.EMPTY;
	public static int s_pj_path_length = 0;
	public static String s_f_s = CsjConst.EMPTY;
	public static String s_user = CsjConst.EMPTY;
	public static String s_local_host = CsjConst.EMPTY;
	public static String s_local_userdomain = CsjConst.EMPTY;
	public static String s_local_usercountry = CsjConst.EMPTY;
	
	public static String s_local_os = CsjConst.EMPTY;
	public static String s_local_os_arch = CsjConst.EMPTY;
	
	
	public static String s_out_ip = CsjConst.EMPTY;
	public static String s_local_inet_addr_with_line = CsjConst.EMPTY;
	public static String s_local_inet_addr = CsjConst.EMPTY;
	public static Date s_default_date;
	
	public static final String s_log4j_file_path = "";
	public static String s_pc_info = CsjConst.EMPTY;
	public static String s_db_log_pc_info = CsjConst.EMPTY;
	public static String   s_newLine   =   CsjConst.EMPTY;

	static {

		try {
			CmnDateUtil.getDate("1901/01/01", CsjConst.YYYY_MM_DD_SLASH);
			Properties   pp   =   System.getProperties();
			s_newLine = pp.getProperty( "line.separator");
			s_f_s = System.getProperty("file.separator");

			// 取得当前路径
			s_pj_path = System.getProperty("user.dir") + s_f_s;
			s_pj_path_length = s_pj_path.length();
			
			s_local_usercountry = System.getProperty("user.country");

			s_local_os = System.getProperty("os.name");
			s_local_os_arch = System.getProperty("os.arch");
			s_local_host =InetAddress.getLocalHost().toString();
			CmnLanIPUtils.isAccessNet();
			// 以下はlinux システムで使用する。
//            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
//            InetAddress ip = null;
//            while (allNetInterfaces.hasMoreElements())
//            {
//            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
//            System.out.println(netInterface.getName());
//            Enumeration addresses = netInterface.getInetAddresses();
//            while (addresses.hasMoreElements())
//            {
//            ip = (InetAddress) addresses.nextElement();
//            if (ip != null && ip instanceof Inet4Address)
//            {
//            System.out.println("本机的IP = " + ip.getHostAddress());
//            }
//            }
//            }
			s_user = System.getenv().get("USERNAME");// 获取用户名
			
			try {
				run();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CmnLog.logger.info(e.getMessage());
			}
			if (CmnLanIPUtils.isAccessNet) {
				s_out_ip = ExternalIpAddressFetcher.getOutIp();//UserIp.getWebIp();//
			}

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CmnLog.logger.info(e.getMessage());
		}
		s_db_log_pc_info = CsjProcess.s_user + "/"+ CsjProcess.s_local_host + "/" + CsjProcess.s_local_os+"/"+CsjProcess.s_local_os_arch+"/"+CsjProcess.s_local_usercountry+"/mac:"+s_local_inet_addr_with_line+"/"+CsjConst.s_version;
	}
	public static void setPcInfo() {
		s_pc_info = s_db_log_pc_info +"/"+CsjProcess.s_out_ip;
	}
	public static void run() {
		try {
			InetAddress address = InetAddress.getLocalHost();
			NetworkInterface netCard = NetworkInterface
					.getByInetAddress(address);
			byte[] addr = netCard.getHardwareAddress();

			StringBuffer sb = new StringBuffer();
			
			if (addr != null) {
				for (int i = 0; i < addr.length; i++) {
					if (i != 0) {
						sb.append("-");
					}

					String string = Integer.toHexString(addr[i] & 0xff);
					sb.append(string.length() == 1 ? "0" + string : string);
				}
			}

			s_local_inet_addr_with_line = sb.toString().toUpperCase();
			if (CmnStrUtils.isEmpty(s_local_inet_addr_with_line)) {
				s_local_inet_addr_with_line= "00-00-00-00-00-00";
			}
			s_local_inet_addr = s_local_inet_addr_with_line.replaceAll("-", "");
			
		} catch (SocketException e) {
			e.printStackTrace();
			CmnLog.logger.info(e.getMessage());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			CmnLog.logger.info(e.getMessage());
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		run();

	}

	public static void openFile(String filePath)  {
		try {
			Runtime.getRuntime().exec("cmd /c start "+ filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void delFile(String filePath) {

		Process process = null;
		File f = new File(filePath);
		try {

			if (f.isDirectory()) {
				process = Runtime.getRuntime().exec("cmd /c  rd /q /s " + f.getAbsolutePath());
				process.waitFor();
			} else if (f.isFile()) {
				process = Runtime.getRuntime().exec("cmd /c del " + f.getAbsolutePath());
				process.waitFor();
			} else {
				System.out.println("error");
				return;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.info(e.getMessage());
			// TODO: handle exception
		}
	}

	public static void copyFile(String fromFilePath, String toFilePath) {
		try {
			Process process = null;
			File fromFile = new File(fromFilePath);
			File toFile = new File(toFilePath);
			toFile.mkdirs();
			String batStr = "xcopy " + fromFile.getAbsolutePath() + "\\*.* " + toFile.getAbsolutePath()
					+ "\\ /y /e /k /c /R";
			process = Runtime.getRuntime().exec(batStr);
			process.waitFor();
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.info(e.getMessage());
			// TODO: handle exception
		}
	}
}
//1.  request.getRealPath("/");//不推荐使用获取工程的根路径
//2.  request.getRealPath(request.getRequestURI());//获取jsp的路径，这个方法比较好用，可以直接在servlet和jsp中使用
//3.  request.getSession().getServletContext().getRealPath("/");//获取工程的根路径，这个方法比较好用，可以直接在servlet和jsp中使用
//4.  this.getClass().getClassLoader().getResource("").getPath();//获取工程classes 下的路径，这个方法可以在任意jsp，servlet，java文件中使用，因为不管是jsp，servlet其实都是java程序，都是一个 class。所以它应该是一个通用的方法。
//5. System.getProperty("user.dir"); 取得当前路径

package jp.co.csj.tools.utils.z.exe.batch;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.mydbsee.common.CmnLog;
import org.mydbsee.common.CmnLog5j;
import org.mydbsee.common.CmnStrUtils;
import org.mydbsee.common.IConstFile;

import jp.co.csj.tools.utils.common.CsjProcess;

/**
 * @author Think
 *
 */
public class AutoConvertFileByEncode {

	public static String oldFilePath = "D:\\xxxx - 副本\\SwtUtils\\src\\com";
	public static String oldEncode = "gb2312";//CsjFileConst.ENCODE_SHIFT_JIS;
	public static String newFilePath = "C:\\tools\\A111\\";
	public static String newEncode = IConstFile.ENCODE_UTF_8;
	public static boolean isChange = true;

	public static List<String> fExpEndList = new ArrayList<String>();
	public static List<String> fExpContainList = new ArrayList<String>();
	public static List<String> fExpBeginList = new ArrayList<String>();

	public static List<String> fEndList = new ArrayList<String>();
	public static List<String> fContainList = new ArrayList<String>();
	public static List<String> fBeginList = new ArrayList<String>();


	/**
	 *
	 */
	public AutoConvertFileByEncode() {
		// TODO Auto-generated constructor stub
	}

	public static void reEncodeFiles(List<File> fileList,String oldPath,String oldEncode,String newPath,String newEncode) throws Throwable {

		try {
			for (File f : fileList) {
				String oldFilePath = CmnStrUtils.fromAtoBByTrim(f.getParent() + CsjProcess.s_f_s, oldPath, "");
				toChange(oldFilePath,f,oldEncode,newPath,newEncode);
			}
		} catch (Throwable e) {
			throw e;
		}

	}
	/**
	 * @param oldPath
	 * @param f
	 * @param newEncode
	 * @param newPath
	 * @param regex
	 * @param oldEncode
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void toChange(String oldFilePath, File f, String oldEncode, String newPath, String newEncode) throws Throwable {
		
		try {
			BufferedReader bufferReader  = new BufferedReader(new InputStreamReader(new FileInputStream(f), oldEncode));

			String newFilePath = newPath + oldFilePath;
			new File(newFilePath).mkdirs();

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFilePath+CsjProcess.s_f_s
					+ f.getName()), newEncode));

			while (bufferReader.ready()) {
				String line = bufferReader.readLine();
				writer.write(line);
				writer.newLine();
			}

			bufferReader.close();
			writer.close();
		} catch (Throwable e) {
			throw e;
		}

	}

	public static void run() throws Throwable {

		//reEncodeFiles();
	}
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		try {
			CmnLog5j.initLog5j(newFilePath, "1.sql", IConstFile.ENCODE_UTF_8);
			//reEncodeFiles();
			CmnLog5j.closeLog5j();
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.error(e.getMessage());
		}

	}



}

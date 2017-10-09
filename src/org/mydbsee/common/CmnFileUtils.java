/**
 * Copyright (c) 2012, Java CNC/CSJ for tools. All rights reserved.
 */
package org.mydbsee.common;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import jp.co.csj.tools.utils.common.CsjPath;
import jp.co.csj.tools.utils.common.CsjProcess;
import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.file.core.CsjSameFilesInfo;
import jp.co.csj.tools.utils.key.CsjDesEncrypt;
import jp.co.csj.tools.utils.msg.dbtools.CsjDbToolsMsg;
import jp.co.csj.tools.utils.reg.RegConstStr;
/**
 * CsjDirectory
 * @author cuishuangjia@163.com
 * @version 1.0
 * @since 1.6
 */
public class CmnFileUtils {

	public static void main(String[] args) {
		
		try {
			CmnLog5j.initLog5j("c:\\b\\","log.txt", IConstFile.ENCODE_UTF_8);
			List<File> fileLst = getFilesList("C:\\aa", true);
			for (File f: fileLst) {
				CmnLog5j.writeLine("-------"+f.getAbsolutePath());
				try {
					List<String> strlst = getFileContent(f, IConstFile.ENCODE_UTF_8);
					for(String s : strlst) {
						CmnLog5j.writeLine(s);
					}
					
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			CmnLog5j.closeLog5j();
		}
		
	}
	public static LinkedList<File> getFilesListWithOutStr(String filePath,
			boolean isHaveSubFile, String startStr, String endStr) {

		LinkedList<File> tList = getFilesList(filePath, isHaveSubFile);
		LinkedList<File> retList = new LinkedList<File>();
		for (File f : tList) {
			String fNm = f.getName().toLowerCase();
			if (CmnStrUtils.isNotEmpty(startStr) && fNm.startsWith(startStr)) {
				continue;
			} else if (CmnStrUtils.isNotEmpty(endStr) && fNm.endsWith(endStr)) {
				continue;
			}
		}
		return retList;
	}

	public static boolean isFiles(String text, String splitStr) {

		boolean retVal = true;
		String[] strArr = text.split(splitStr);
		for (String str : strArr) {
			if (CmnStrUtils.isEmpty(str)) {
				continue;
			}
			File f = new File(str);
			if (f.isFile() == false) {
				retVal = false;
				break;
			}
		}
		return retVal;
	}
	/**
	 * * @param
	 */
	public static int checkFilePath(String filePath) {
		int retVal = IConstFile.IS_ERROR;
		File f = new File(filePath);
		if (f.isDirectory()) {
			retVal = IConstFile.IS_PATH;
		} else if (f.isFile()) {
			retVal = IConstFile.IS_FILE;
		} else if (filePath.contains(";") && isFiles(filePath, ";")) {
			retVal = IConstFile.IS_FILES;
		}
		return retVal;
	}
	public static LinkedList<File> getExcelFileByAbsPath(String filePath,boolean isHaveSubFile) {
		LinkedList<File> retList = new LinkedList<File>();
		int retVal = checkFilePath(filePath);
		if (retVal == IConstFile.IS_PATH) {
			retList= getFilesListReg(filePath, isHaveSubFile,RegConstStr.EXCEL_REG_DOT);
		} else if (retVal == IConstFile.IS_FILE) {
			retList.add(new File(filePath));
		} else if (retVal == IConstFile.IS_FILES) {
			String[] strArr = filePath.split(",");
			for (String str : strArr) {
				if (CmnStrUtils.isNotEmpty(str)) {
					retList.add(new File(str));
				}
			}
		}
		return retList;
	}
	public static LinkedList<File> getFilesListWithStr(String filePath,
			boolean isHaveSubFile,String startStr,String endStr) {

		LinkedList<File> tList =getFilesList(filePath,isHaveSubFile);
		LinkedList<File> retList = new LinkedList<File>();
		for (File f : tList) {
			String fNm = f.getName().toLowerCase();
			if (CmnStrUtils.isNotEmpty(startStr)&& fNm.startsWith(startStr)) {
					retList.add(f);
			} else if (CmnStrUtils.isNotEmpty(endStr)&& fNm.endsWith(endStr)) {
				retList.add(f);
			}
		}
		return retList;
	}
	public static TreeMap<Long,File> getFilesModifyTimeMapReg(String filePath,
			boolean isHaveSubFile,String regex) {

		LinkedList<File> tList =getFilesList(filePath,isHaveSubFile);
		TreeMap<Long,File> retMap = new TreeMap<Long, File>();
		for (File f : tList) {
			String fNm = f.getName().toLowerCase();
			
			if (CmnStrUtils.isEmpty(regex)||fNm.matches(regex)) {
				retMap.put(f.lastModified(), f);
			}
		}
		return retMap;
	}
	public static LinkedList<File> getFilesListReg(String filePath,
			boolean isHaveSubFile,String regex) {

		LinkedList<File> fList = getFilesList(filePath,isHaveSubFile);
		LinkedList<File> retList = new LinkedList<File>();

		for (File f : fList) {
			if (CmnStrUtils.isEmpty(regex)||f.getName().matches(regex)) {
				retList.add(f);
			}
		}
		return retList;
	}
	/**
	 * @param String filePath
	 * @param boolean isHaveSubFile
	 * @return
	 */
	public static LinkedList<File> getFilesList(String filePath,
			boolean isHaveSubFile) {

		LinkedList<File> retList = new LinkedList<File>();
		LinkedList<File> list = new LinkedList<File>();
		File dir = new File(filePath);
		File file[] = dir.listFiles();
		if (null == file) {
		return retList;
		}

		for (int i = 0; i < file.length; i++) {

//			if (file[i].getName().endsWith(".scc")) {
//				continue;
//			}

			if (file[i].isDirectory())
				list.add(file[i]);
			else
				retList.add(file[i]);
		}

		if (isHaveSubFile) {
			File tmp;
			while (!list.isEmpty()) {
				tmp = list.removeFirst();

				if (tmp.isDirectory()) {
					file = tmp.listFiles();
					if (file == null)
						continue;
					for (int i = 0; i < file.length; i++) {
						if (file[i].isDirectory())
							list.add(file[i]);
						else {
//							if (file[i].getName().endsWith(".scc")) {
//								continue;
//							}
							retList.add(file[i]);
						}
					}
				} else {
//					if (tmp.getName().endsWith(".scc")) {
//						continue;
//					}
					retList.add(tmp);
				}
			}
		}

		return retList;
	}

	public static boolean reNameFile(File f, String newNm) {
		File mm = new File(f.getParent() + CsjProcess.s_f_s + newNm);
		return f.renameTo(mm);
	}
//    /**
//     *
//     * @param filePathList
//     * @return
//     */
//    public static LinkedList<File> getFilesList(List<String> filePathList, boolean isHaveSubFile) {
//	LinkedList<File> retList = new LinkedList<File>();
//	for (String filePath : filePathList) {
//	    LinkedList<File> fileList = getFilesList(filePath, isHaveSubFile);
//	    for (File file : fileList) {
//		retList.add(file);
//	    }
//	}
//	return retList;
//    }
//
//    /**
//     *
//     * @param filePath
//     * @return
//     */
//	public static LinkedList<File> getFilesList(String filePath,
//			boolean isHaveSubFile) {
//
//		LinkedList<File> retList = new LinkedList<File>();
//		LinkedList<File> list = new LinkedList<File>();
//		File dir = new File(filePath);
//		File file[] = dir.listFiles();
//		if (null == file) {
//			System.out.println("a");
//		}
//
//		for (int i = 0; i < file.length; i++) {
//
//			if (file[i].getName().endsWith(".scc")) {
//				continue;
//			}
//
//			if (file[i].isDirectory())
//				list.add(file[i]);
//			else
//				retList.add(file[i]);
//		}
//
//		if (isHaveSubFile) {
//			File tmp;
//			while (!list.isEmpty()) {
//				tmp = list.removeFirst();
//
//				if (tmp.isDirectory()) {
//					file = tmp.listFiles();
//					if (file == null)
//						continue;
//					for (int i = 0; i < file.length; i++) {
//						if (file[i].isDirectory())
//							list.add(file[i]);
//						else {
//							if (file[i].getName().endsWith(".scc")) {
//								continue;
//							}
//							retList.add(file[i]);
//						}
//					}
//				} else {
//					if (tmp.getName().endsWith(".scc")) {
//						continue;
//					}
//					retList.add(tmp);
//				}
//			}
//		}
//
//		return retList;
//	}
	public static LinkedHashMap<String, File> getFilesMap(String filePath, boolean isHaveSubFile) {
		LinkedHashMap<String, File> retMap = new LinkedHashMap<String, File>();
		List<File> fileList = getFilesList(filePath, isHaveSubFile);
		for (File f : fileList) {
			retMap.put(f.getName(), f);
		}
		return retMap;
	}
	public static Map<String, File> getFilesTreeMap(String filePath, boolean isHaveSubFile) {
		Map<String, File> retMap = new TreeMap<String, File>();
		List<File> fileList = getFilesList(filePath, isHaveSubFile);
		for (File f : fileList) {
			retMap.put(f.getName(), f);
		}
		return retMap;
	}
	public static long getFileSizes(File f) {// 取得文件大小
		long s = 0;
		FileInputStream fis = null;
		try {
			if (f.exists()) {
				
				fis = new FileInputStream(f);
				s = fis.available();
			} else {
				f.createNewFile();
				System.out.println("file not exist!");
			}
		} catch (Throwable e) {
			CmnLog.logger.info(e.getMessage());
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				CmnLog.logger.info(e.getMessage());
			}
		}
		return s;
	}

	public static String formatFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
	public static String getFileSize(File f) {
		return formatFileSize(getFileSizes(f));

	}
	public static long getFileMaxLine(File f) {

		long retVal = 0;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(f), IConstFile.ENCODE_SHIFT_JIS));

			while (reader.ready()) {
				reader.readLine();
				retVal++;
			}
			reader.close();
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.info(e.getMessage());
		}
		return retVal;
	}
	public static void removeFiles(String filePath) throws Throwable {
		try {
			File f = new File(filePath);
			if (f!=null) {
				delFile(f);
			}
		} catch (Throwable e) {
			throw e;
		}

	}

	public static void delFile(String path,String fileNmReg,boolean haveSub) throws Throwable {
		try {
			File f = new File(path);
			if (CmnStrUtils.isEmpty(fileNmReg)) {
				delFile(f);
			}else if (f.isDirectory()) {
				List<File> fLst = getFilesList(f.getAbsolutePath(), haveSub);
				for (File ft : fLst) {
					if (CmnStrUtils.isEmpty(fileNmReg)) {
						ft.delete();
					}else if (ft.getName().matches(fileNmReg)) {
						ft.delete();
					}
				}
			}
		} catch (Throwable e) {
			throw e;
		}

	}
	public static void delFile(File f)throws Throwable {
		try {
			if (f == null) {
				return;
			}
			if (f.isDirectory()) {
				File[] list = f.listFiles();
				for (int i = 0; i < list.length; i++) {
					if (list[i].isDirectory()) {
						delFile(list[i]);
					}else{
						if(list[i].isFile())
							list[i].delete();
					}
				}
				f.delete();
			} else {
				if (f.isFile())
					f.delete();
			}
		} catch (Throwable e) {
			throw e;
		}
	}

	/**
	 * @param text
	 * @param string
	 * @throws Throwable
	 */
	public static void copyDbtoolsInfo(String newIniNm, String iniText) throws Throwable {
		if (CmnStrUtils.isNotEmpty(iniText)) {
			copyFile(CsjPath.s_file_db_info_path, iniText + ".xml", CsjPath.s_file_db_info_path, newIniNm+ ".xml", IConstFile.ENCODE_UTF_8);
		} else {
			LinkedList<File> fileList = CmnFileUtils.getFilesListWithStr(CsjPath.s_file_db_info_path, false, "", IConstFile.DOT_INI);
			if (fileList.size()!=0) {
				File f = fileList.get(0);
				copyFile(CsjPath.s_file_db_info_path, f.getName(), CsjPath.s_file_db_info_path, newIniNm+ ".xml", IConstFile.ENCODE_UTF_8);
			} else {
				fileList = CmnFileUtils.getFilesListWithStr(CsjPath.s_file_db_info_path, false, "", IConstFile.DOT_INI_BAK);
				File f = fileList.get(0);
				copyFile(CsjPath.s_file_db_info_path, f.getName(), CsjPath.s_file_db_info_path, newIniNm+ ".xml", IConstFile.ENCODE_UTF_8);

			}

		}

	}

	private static String formatToFolder(String folder) {
		if (folder.endsWith(CsjProcess.s_f_s)) {
			return folder;
		} else {
			return folder + CsjProcess.s_f_s;
		}
	}
	/**
	 * @param fromFolder
	 * @param fromFile
	 * @param toFolder
	 * @param toFile
	 * @throws Throwable 
	 */
	public static void copyFile(String fromFolder, String fromFile,
			String toFolder, String toFile,boolean toFolderAutoCreate) throws Throwable {
		
		try {
			if (toFolderAutoCreate) {
				File makeFolder = new File(toFolder);
				makeFolder.mkdirs();
			}
			boolean isHaveSubFile = false;
			if (fromFolder.toLowerCase().startsWith("havesub:")) {
				isHaveSubFile = true;
			}
			fromFolder = CmnStrUtils.fromAtoBByTrim(fromFolder,"havesub:", "");
			
			boolean isReg = false;
			if (fromFile.startsWith("reg:")) {
				isReg = true;
			}
			fromFile = CmnStrUtils.fromAtoBByTrim(fromFile,"reg:", "");
			
			String fromStr = formatToFolder(fromFolder)+fromFile;
			if (isReg) {
				fromStr = formatToFolder(fromFolder);
			}
			String toStr = formatToFolder(toFolder)+toFile;
			File from = new File(fromStr);
			File to = new File(toStr);
			
			if (from.isDirectory()) {
				if (to.isDirectory()) {
					if (isReg) {
						LinkedList<File> fileLst = getFilesListReg(fromFolder, isHaveSubFile,fromFile);
						for (File f : fileLst) {
							String toPath = toStr+CmnStrUtils.fromAtoBByTrim(f.getParent()+CsjProcess.s_f_s, fromStr, "");
							new File(toPath).mkdirs();
							String batStr = "xcopy " + f.getAbsolutePath() + " " + toPath + " /y /k /c /R";
							Process process  = Runtime.getRuntime().exec(batStr);
							process.waitFor();
						}
					} else {
						LinkedList<File> fileLst = getFilesList(fromFolder, isHaveSubFile);
						for (File f : fileLst) {
							String toPath = toStr+CmnStrUtils.fromAtoBByTrim(f.getParent()+CsjProcess.s_f_s, fromStr, "");
							new File(toPath).mkdirs();
							String batStr = "xcopy " + f.getAbsolutePath() + " " + toPath + " /y /k /c /R";
							Process process  = Runtime.getRuntime().exec(batStr);
							process.waitFor();
						}
					}
				} else {
					throw new Exception(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000111) + CmnLog5j.addlrBracketsM(toStr, false));
				}
			} else if (from.isFile()) {
				if (to.isDirectory()) {
					String batStr = "xcopy " + from.getAbsolutePath() + " " + to + " /y  /k /c /R";
					Process process  = Runtime.getRuntime().exec(batStr);
					process.waitFor();
				} else {
					File tmpToFolder = new File(toFolder);
					if (tmpToFolder.isDirectory()) {
						String batStr = "xcopy " + from.getAbsolutePath() + " " + toFolder + " /y  /k /c /R";
						Process process  = Runtime.getRuntime().exec(batStr);
						process.waitFor();
						
						File f = new File(toFolder+toFile);
						if (f.isFile()) {
							f.delete();
						}
						File fr = new File(toFolder+fromFile);
						if (fr.isFile()) {
							fr.renameTo(f);
						}
					} else {
						throw new Exception(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000113) + CmnLog5j.addlrBracketsM(toFolder, false));
					}
				}
			} else {
				throw new Exception(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000112) + CmnLog5j.addlrBracketsM(fromStr, false));
			}
		} catch (Throwable e) {
			throw e;
		}
	}

	////////////////////////////

	public static void writeWithbBlank(BufferedWriter writer, String str,
			int blankCount) {

		if (writer == null) {
			return;
		}
		try {
			writer.write(getBlank(str, blankCount, true));
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			CmnLog.logger.info(e.getMessage());
		}

	}

	public static String getTxtFileInfo(String fileNm, String encode,
			String splitLine) throws Throwable {
		StringBuffer buffer = new StringBuffer();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileNm), encode));

		while (reader.ready()) {
			buffer.append(reader.readLine() + splitLine);
		}

		return buffer.toString();

	}

	public static String getBlank(String str, int blankCount, boolean isBefore) {
		String strBlank = CsjConst.EMPTY;
		for (int i = 0; i < blankCount; i++) {
			strBlank += CsjConst.SIGN_SPACE_4;
		}
		if (isBefore) {
			return strBlank + str;
		} else {
			return str + strBlank;
		}

	}

	/**
	 * @param writer
	 * @param copyRightList
	 * @param blankCount
	 */
	public static void writeWithbBlank(BufferedWriter writer,
			List<String> strList, int blankCount) {
		for (String str : strList) {
			writeWithbBlank(writer, str, blankCount);
		}
	}

	/**
	 * @param writer
	 * @param copyRightList
	 * @param blankCount
	 */
	public static void writeWithbBlank(BufferedWriter writer, String paraStr,
			List<String> strList, int blankCount,
			boolean isLeft) {
		for (String str : strList) {
			if (isLeft) {
				writeWithbBlank(writer, paraStr + str, blankCount);
			} else {
				writeWithbBlank(writer, str + paraStr, blankCount);
			}

		}
	}

	/**
	 * @param writer
	 * @param importMap
	 * @param blankCount
	 */
	public static void writeWithbBlank(BufferedWriter writer, String paraStr,
			Map<String, String> map, int blankCount,
			boolean isLeft) {
		for (Entry<String, String> entry : map.entrySet()) {

			if (isLeft) {
				writeWithbBlank(writer, paraStr + entry.getKey(), blankCount);
			} else {
				writeWithbBlank(writer, entry.getKey() + paraStr, blankCount);
			}
		}
	}

	/**
	 * @param writer
	 * @param importMap
	 * @param blankCount
	 */
	public static void writeWithbBlank(BufferedWriter writer,
			Map<String, String> map, int blankCount) {
		for (Entry<String, String> entry : map.entrySet()) {
			writeWithbBlank(writer, entry.getKey(), blankCount);
		}
	}

	public static LinkedHashMap<String, String> getFileMap(String filePath,
			String encode, boolean isKeyLineNo) {

		LinkedHashMap<String, String> retMap = new LinkedHashMap<String, String>();

		long retVal = 0;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(filePath)),
					encode));

			while (reader.ready()) {
				if (isKeyLineNo) {
					retMap.put(String.valueOf(retVal++), reader.readLine());
				} else {
					retMap.put(reader.readLine(), String.valueOf(retVal++));
				}
			}
			reader.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return retMap;
	}

	public static LinkedHashMap<String, List<String>> getKeyFileMap(
			String filePath, String encode) {
		LinkedHashMap<String, List<String>> retMap = new LinkedHashMap<String, List<String>>();
		CmnLog.logger.info(new File(filePath).getAbsolutePath());

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(filePath)),
					encode));

			while (reader.ready()) {
				String line = reader.readLine();
				CmnLog.logger.info(line);
				if (CmnStrUtils.isNotEmpty(line)) {
					String strLine = CsjDesEncrypt.uncode(line);
					strLine = CmnLanIPUtils.unLongStrKey(strLine);
					String str[] = strLine.split(CsjConst.SIGN_AT);
					if (str.length == 4) {
						// CsjLog4j.info(CsjDesEncrypt.uncode(line).toString());
						List<String> list = new ArrayList<String>();
						list.add(str[1]);
						list.add(str[2]);
						retMap.put(str[0], list);
					}
				}

				// if (line != null && line.length() == 80) {
				// String key = line.substring(0, 48);
				// System.out.println(CsjDesEncrypt.uncode(key));
				// String pay = line.substring(48, 64);
				// System.out.println(CsjDesEncrypt.uncode(key));
				// String date = line.substring(64,96);
				// System.out.println(CsjDesEncrypt.uncode(key));
				// retMap.put(key,pay+"|||"+date);
				// }
			}
			reader.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return retMap;
	}



	public static void writeFile(String logPath, String fileNm, String enCode,
			List<String> strList) throws Throwable {
		File file = new File(logPath);
		file.mkdirs();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(logPath + fileNm), enCode));
		for (String str : strList) {
			writeWithbBlank(writer, str, 0);
		}
		writer.close();
	}
	public static boolean isFileExist(String filePath) {
		return new File(filePath).isFile();
	}
	public static boolean isFolderExist(String folderPath) {
		return new File(folderPath).isDirectory();
	}
	public static void reWriteFile(String filePath, String enCode,
			Map<String,String> regexMap,String cr) throws Throwable {
		List<String> strLst = getFileContent(filePath, enCode);
		List<String> strLstTmp = new ArrayList<String>();
		for (String s:strLst) {
			for (String key : regexMap.keySet()) {
				if (s.matches(key)) {
					s = regexMap.get(key);
					break;
				}
			}
			strLstTmp.add(s);
		}

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(filePath), enCode));
		for (String str : strLstTmp) {
			writer.write(str);
			writer.write(cr);
		}
		writer.close();
	}
	
	public static String getFileNm(String filePath) {
		String retVal = CsjConst.EMPTY;
		File file = new File(filePath);
		retVal = file.getName();
		return retVal;
	}

	public static boolean
			isTwoFileNmSame(String fileOldPath, String filNewPath) {
		return getFileNm(fileOldPath).equals(getFileNm(filNewPath));
	}

	/**
	 * @param oldFilesMap
	 * @param newFilesMap
	 * @return
	 */
	public static List<CsjSameFilesInfo> getSameFile(
			LinkedHashMap<String, File> oldFilesMap,
			LinkedHashMap<String, File> newFilesMap) {
		List<CsjSameFilesInfo> retList = new ArrayList<CsjSameFilesInfo>();

		for (Entry<String, File> entry : newFilesMap.entrySet()) {
			String key = entry.getKey();
			if (oldFilesMap.containsKey(key)) {
				CsjSameFilesInfo csjSameFilesInfo = new CsjSameFilesInfo();
				csjSameFilesInfo.setFileNm(key);
				csjSameFilesInfo.getFilePaths().add(
						oldFilesMap.get(key).getAbsolutePath());
				csjSameFilesInfo.getFilePaths().add(
						newFilesMap.get(key).getAbsolutePath());
				retList.add(csjSameFilesInfo);
			}
		}
		return retList;
	}

	/**
	 * @param oldFilesMap
	 * @param newFilesMap
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static List<String> getFileContent(File f, String encode)
			throws Throwable {
		List<String> retList = new ArrayList<String>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(f),
				encode));

		while (reader.ready()) {
			retList.add(reader.readLine());
		}
		reader.close();
		return retList;
	}
	/**
	 * @param oldFilesMap
	 * @param newFilesMap
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static List<String> getFileContent(String path, String encode)
			throws Throwable {
		List<String> retList = new ArrayList<String>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(path)),
				encode));

		while (reader.ready()) {
			retList.add(reader.readLine());
		}
		reader.close();
		return retList;
	}
	public static Map<Integer,String> getFileContents(String filePath,String splitCh,int col,int rowStart,int rowEnd,String encode) throws Throwable {
		Map<Integer,String> retMap = new HashMap<Integer, String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(filePath)),
				encode));

		int i = 0;
		while (reader.ready()) {
			String line = reader.readLine();
			if (i >= rowStart && i <= rowEnd) {
				String[] sa = line.split(splitCh);
				if (col < sa.length ) {
					retMap.put(i-rowStart, sa[col]);
				}
			}
			i++;
		}
		reader.close();

		return 	retMap;
	}
	/**
	 * @param oldFilesMap
	 * @param newFilesMap
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static List<String> getFilesContent(String path, String encode)
			throws Throwable {

		List<File> fileList = CmnFileUtils.getFilesList(path, false);
		List<String> retList = new ArrayList<String>();
		for (File f : fileList) {
			List<String> strList = getFileContent(f, encode);
			for (String str : strList) {
				retList.add(str);
			}
		}

		return retList;
	}

	/**
	 * @param oldFilesMap
	 * @param newFilesMap
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void copyFile(String path, String nm, String newPath,
			String newNm, String encode) throws Throwable {

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(path + nm),
				encode));
		File file = new File(newPath);
		file.mkdirs();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(newPath + newNm), encode));

		while (reader.ready()) {
			writeWithbBlank(writer, reader.readLine(), 0);
		}

		writer.close();
		reader.close();
	}

	/**
	 * @param fileMap
	 * @param string
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void createFile(TreeMap<Long, File> fileMap, String outputFileName, String encode) throws Throwable {
		
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputFileName), encode));
			for (File f : fileMap.values()) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(f.getAbsolutePath()),encode));
				while (reader.ready()) {
					String line = reader.readLine();
					CmnLog5j.writeLine(writer, line);
				}
				reader.close();
			}
			CmnLog5j.close(writer);
		} catch (Throwable e) {
			throw e;
		}
	}

}

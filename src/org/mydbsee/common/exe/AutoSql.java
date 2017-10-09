package org.mydbsee.common.exe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mydbsee.common.CmnDateUtil;
import org.mydbsee.common.CmnFileUtils;
import org.mydbsee.common.CmnLog5j;
import org.mydbsee.common.CmnStrUtils;
import org.mydbsee.common.IConstDate;
import org.mydbsee.common.IConstFile;

public class AutoSql {

	public static Map<String,String> commentMap = new HashMap<String,String>();
	public static Map<String,String> optypeMap = new HashMap<String,String>();

	public static void run() {

		try {
			
			String autoTime = CmnDateUtil.getCurrentDateString(IConstDate.YYYYMMDDHH_MMSSMINUS_24);
			CmnLog5j.initLog5j(autoTime + "_log.txt",IConstFile.ENCODE_UTF_8);
			readFileProperty(commentMap,"C:\\Users\\Think\\git\\work\\src\\org\\mydbsee\\common\\exe\\comment.txt");
			readFileProperty(optypeMap,"C:\\Users\\Think\\git\\work\\src\\org\\mydbsee\\common\\exe\\optype.txt");
			List<File> flst  = CmnFileUtils.getFilesList("C:\\Users\\Think\\Documents\\Tencent Files\\2796523176\\FileRecv\\src_1103\\src\\jp\\co\\zc\\card\\ssm", true);
			for (File f : flst) {
				if (!f.getName().toLowerCase().endsWith(".xml")) {
					continue;
				}
				String directory= "C:\\"+autoTime+ "\\"+ CmnStrUtils.fromAtoNearB(f.getAbsolutePath(), "C:\\", f.getName());
				String pjName = CmnStrUtils.fromAtoNearB(f.getName(), "C:\\Users\\Think\\Documents\\Tencent Files\\2796523176\\", "\\");
				String fileNm = CmnStrUtils.fromAtoNearB(f.getName(), "", ".xml");
				
				List<String> strLst = CmnFileUtils.getFileContent(f, IConstFile.ENCODE_UTF_8);
				List<OperateInfo> operateInfoLst = new ArrayList<OperateInfo>();
				for (int i=0; i < strLst.size(); i++) {
					for (Entry<String,String> entryOpType : optypeMap.entrySet()) {
						if (strLst.get(i).toLowerCase().contains(entryOpType.getKey().toLowerCase())) {
							OperateInfo operateInfo = new OperateInfo();
							operateInfoLst.add(operateInfo);
							operateInfo.operateType = entryOpType.getKey().toLowerCase();
							operateInfo.id = CmnStrUtils.fromAtoNearB(CmnStrUtils.fromAtoFarB(strLst.get(i), "id", ""), "\"", "\"");
							while(!strLst.get(++i).toLowerCase().contains(entryOpType.getValue().toLowerCase())) {
								operateInfo.strList.add(strLst.get(i));
							}
							for (int j = 0; j < operateInfo.strList.size(); j++) {
								String line = operateInfo.strList.get(j);
								line = getParaLine(line,operateInfo);
								line = getCommentLine(line);
								operateInfo.strList.set(j, line);
							}
							writeFile(directory,pjName,fileNm,operateInfo);
						}
					}
				}
			}
			
		} catch (Throwable e) {
			CmnLog5j.writeLine(e.getStackTrace().toString());
			System.out.println(e.getMessage());
		} finally {
			try {
				CmnLog5j.closeLog5j();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	
	}
	public static void main(String[] args) {
		run();
	}

	private static void writeFile(String path,String pjName, String fileNm, OperateInfo operateInfo) throws Throwable {
		File file = new File(path);
		file.mkdirs();
		BufferedWriter bwOracle = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(path + pjName+"_"+fileNm+ "_"+operateInfo.id + "_O.txt"), IConstFile.ENCODE_UTF_8));
		BufferedWriter bwMysql = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(path + pjName+"_"+fileNm+ "_"+operateInfo.id + "_M.txt"), IConstFile.ENCODE_UTF_8));
		
		writeSql(operateInfo, bwOracle,true);
		writeSql(operateInfo, bwMysql,false);
		
	}

	private static void writeSql(OperateInfo operateInfo, BufferedWriter bw, boolean isOracle) throws Throwable {
		if (!operateInfo.operateType.toLowerCase().contains("select")) {
			CmnLog5j.writeLineWithConsole(bw, "trasaction");
		}
		for(Entry<String,String> entry : operateInfo.defineMap.entrySet()) {
			String param = entry.getKey();
			String type = entry.getValue();
			if (isOracle) {
				CmnLog5j.writeLineWithConsole(bw, "oaaa" + param + "obbb" + "/*" + type + "*/");
			} else {
				CmnLog5j.writeLineWithConsole(bw, "maaa" + param + "mbbb" + "/*" + type + "*/");
			}
		}
		
		for(String str : operateInfo.strList) {
			if (isOracle) {
				str = str.replace("$$$", "OOOO");
			}else {
				str = str.replace("$$$", "MMMM");
			}
			CmnLog5j.writeLineWithConsole(bw, str);
		}
		CmnLog5j.writeLineWithConsole(bw, ";");
		if (!operateInfo.operateType.toLowerCase().contains("select")) {
			CmnLog5j.writeLineWithConsole(bw, "rollback;");
		}
		CmnLog5j.close(bw);
	}

	private static String getCommentLine(String line) {
		String str = line;
		System.out.println(str);
		for (Entry<String, String> entry : commentMap.entrySet()) {
			String key = entry.getKey();
			String val = entry.getValue();
			List<String> list = new ArrayList<String>();
			while (str.indexOf(key) >= 0) {
				System.out.println(str);
				String str1 = str.substring(0, str.indexOf(key));
				list.add(str1 + "/*");
				if (CmnStrUtils.isEmpty(val)) {
					list.add(str.substring(str.indexOf(key)) + "*/");
					str = str.substring(str.indexOf(key)+key.length());
				} else {
					list.add(str.substring(str.indexOf(key), str.indexOf(val) + 1) + "*/");
					str = str.substring(str.indexOf(val) + 1);
				}
			}
			list.add(str);

			StringBuffer sb = new StringBuffer();
			for (String s : list) {
				sb.append(s);
			}
			str = sb.toString();
		}
		
		return str;
	}

	private static String getParaLine(String line, OperateInfo operateInfo) {
		if (!line.contains("#{")) {
			return line;
		}
		String str = line;
		List<String> list = new ArrayList<String>();
		while(str.indexOf("#{")>=0) {
			String str1 = str.substring(0, str.indexOf("#{"));
			list.add(str1);
			String paraName = CmnStrUtils.fromAtoNearB(str, "#{", ",");
			list.add("$$$"+paraName);
			String paraType = "XXXXXX";
			if (CmnStrUtils.fromAtoNearB(str, "#{", "}").contains("jdbcType=")) {
				paraType = CmnStrUtils.fromAtoNearB(str, "jdbcType=", "}");
			}
			operateInfo.defineMap.put(paraName, paraType);
			System.out.println(str);
			list.add(str.substring(str.indexOf("#{"), str.indexOf("}")+1));
			str = str.substring(str.indexOf("}")+1);
		}
		list.add(str);
		
		StringBuffer sb = new StringBuffer();
		for (String s : list) {
			sb.append(s);	
		}
		
		return sb.toString();
	}

	private static void readFileProperty(Map<String,String> map,String filePath) throws Throwable {
		List<String> strLst = CmnFileUtils.getFileContent(filePath, IConstFile.ENCODE_UTF_8);
		for (String s: strLst) {
			String sArr[] = s.split(",");
			if (sArr.length==1) {
				map.put(sArr[0], null);
			} else if (sArr.length==2) {
				map.put(sArr[0], sArr[1]);
			}
		}
	}

}
class OperateInfo {
	public Map<String,String> defineMap = new LinkedHashMap<String,String>();
	public String operateType;
	public String id;
	public List<String> strList = new ArrayList<>();
}
//<if,>
//#{,}
//<when,>
//<foreach,>
//</foreach>
//<trim,>
//</trim>
//</if>
//<otherwise,>
//</otherwise>
//</when>
//<choose,>
//</choose>
//<set,>
//</set>
//<where,>
//</where>


//<update,</update>
//<select,</select>
//<insert,</insert>
//<delete,</delete>
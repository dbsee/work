/**
 *
 */
package jp.co.csj.tools;

import java.util.List;

import org.mydbsee.common.CmnFileUtils;
import org.mydbsee.common.CmnLog5j;
import org.mydbsee.common.CmnStrUtils;
import org.mydbsee.common.IConstFile;

import jp.co.csj.tools.utils.common.CsjProcess;

/**
 * @author Think
 *
 */
public class ToolCreateTbl {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			List<String> lst = CmnFileUtils.getFileContent("D:\\IDE\\eclipse-rcp-luna-SR2-win32\\eclipse\\workspace\\CsjToolsPic\\AutoDb\\excel_to_createSql_at_20160530_194336_055\\EMP_sql.txt",
					IConstFile.ENCODE_UTF_8);
			for (int i = 0; i < 3000; i++) {
				CmnLog5j.initLog5j(CsjProcess.s_pj_path+"\\ddl\\auto\\", "EMP_TEST_"+(i+1)+".txt", IConstFile.ENCODE_UTF_8);
				for(String s:lst) {
					CmnLog5j.writeLine(CmnStrUtils.funReplace(s, "EMP", "EMP"+(i+1)));
				}
				CmnLog5j.closeLog5j();
			}

			System.out.println(CsjProcess.s_pj_path+"\\ddl\\auto\\");
			
		} catch (Throwable e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}

}

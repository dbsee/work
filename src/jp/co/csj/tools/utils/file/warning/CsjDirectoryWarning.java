/**
 *
 */
package jp.co.csj.tools.utils.file.warning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.mydbsee.common.CmnLog;

/**
 * @author Think
 *
 */
public class CsjDirectoryWarning {

	// 文件夾拷貝核心函數

	public static boolean copyDirectory(String SrcDirectoryPath, String DesDirectoryPath) {

		FileInputStream FIS;
		FileOutputStream FOS;

		try {
			// 創建不存在的目錄
			File F0 = new File(DesDirectoryPath);
			if (!F0.exists()) {
				if (!F0.mkdir()) {
					System.out.println("目標文件夾不存，創建失敗!");
				}
			}
			File F = new File(SrcDirectoryPath);
			File[] allFile = F.listFiles(); // 取得當前目錄下面的所有文件，將其放在文件數組中
			int totalNum = allFile.length; // 取得當前文件夾中有多少文件（包括文件夾）
			String srcName = "";
			String desName = "";
			int currentFile = 0;
			// 一個一個的拷貝文件
			for (currentFile = 0; currentFile < totalNum; currentFile++) {
				if (!allFile[currentFile].isDirectory()) {
					// 如果是文件是采用處理文件的方式
					srcName = allFile[currentFile].toString();
					desName = DesDirectoryPath + "\\" + allFile[currentFile].getName();
					FileCopy FC = new FileCopy();
					FC.copyFile(srcName, desName);
				}
				// 如果是文件夾就采用遞歸處理
				else {
					// 利用遞歸讀取文件夾中的子文件下的內容，再讀子文件夾下面的子文件夾下面的內容...
					if (copyDirectory(allFile[currentFile].getPath().toString(), DesDirectoryPath + "\\"
							+ allFile[currentFile].getName().toString())) {
						// System.out.println("D Copy Successfully!");
					} else {
						System.out.println("SubDirectory Copy Error!");
					}
				}
			}
			return true;
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.info(e.getMessage());
			return false;
		}
	}

}

class FileCopy {
	FileInputStream FIS;
	FileOutputStream FOS;

	public boolean copyFile(String src, String des) {
		try {
			FIS = new FileInputStream(src);
			FOS = new FileOutputStream(des);
			byte[] bt = new byte[1024];
			int readNum = 0;
			while ((readNum = FIS.read(bt)) != -1) {
				FOS.write(bt, 0, bt.length);
			}
			FIS.close();
			FOS.close();
			return true;
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.info(e.getMessage());
			try {

				FIS.close();
				FOS.close();
			} catch (IOException f) {

				CmnLog.logger.info(f.getMessage());
				f.printStackTrace();
				// TODO
			}
			return false;
		} finally {
		}
	}
}

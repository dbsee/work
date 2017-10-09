package jp.co.csj.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mydbsee.common.CmnDateUtil;
import org.mydbsee.common.CmnFileUtils;
import org.mydbsee.common.CmnPoiUtils;
import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.poi.core.CsjCellInfo;
import jp.co.csj.tools.utils.poi.core.CsjSheetInfo;


public class VVV {

	public static String FOLDER = "all1001";
	public static void main(String[] args) {
			
		TreeMap<String,int[]> numMap = new TreeMap<String,int[]>();
		
			FileInputStream fileIn = null;
			FileOutputStream fileOut = null;
			try {
					fileIn = new FileInputStream("C:\\Users\\Think\\Documents\\bk\\bug_list.xlsx");
					Workbook wb = new XSSFWorkbook(fileIn);
					Sheet newSheet = wb.getSheet("tmp");
					Sheet picSheet = wb.getSheet("pic");
					
					CellStyle cs = wb.createCellStyle();
					cs.setBorderLeft(CellStyle.BORDER_THIN);
					cs.setBorderRight(CellStyle.BORDER_THIN);
					cs.setBorderTop(CellStyle.BORDER_THIN);
					cs.setBorderBottom(CellStyle.BORDER_THIN);

					//CsjPoiUtils.printSheet("E:\\3\\DBTOOLS_BUG_140516_01.xlsx");
					Map<String, File> fileMap = CmnFileUtils.getFilesTreeMap("C:\\Users\\Think\\Desktop\\work\\"+FOLDER+"\\", true);
					
					Map<String, File> map = new TreeMap<String, File>();
					
					for (File f : fileMap.values()) {
						map.put(CmnDateUtil.getDateTime(f.lastModified(), CsjConst.YYYYMMDDHH_HHMMSS_24), f);
					}
					int rowNum = 1;
					for (Entry<String,File> entry : map.entrySet()) {

					
						
						File f = entry.getValue();
						int col = 0;
						CsjSheetInfo sheetInfo = CmnPoiUtils.getSheetContents(f.getAbsolutePath(), "问题说明", true);
						String dbType = getVal(sheetInfo.getCsjCellPosInfoMap(),"1_1");
						String dbVersion = getVal(sheetInfo.getCsjCellPosInfoMap(),"2_1");
						String testUser = getVal(sheetInfo.getCsjCellPosInfoMap(),"4_1");
						String testBegin = getVal(sheetInfo.getCsjCellPosInfoMap(),"4_4");
						
						try {
							testBegin = CmnDateUtil.getFormatDateTime(CmnDateUtil.getDate(testBegin, CsjConst.YYYY_MM_DD_SLASH), CsjConst.YYYY_MM_DD_SLASH);
						} catch (Exception e) {
							// TODO: handle exception
						}
						String testEnd = getVal(sheetInfo.getCsjCellPosInfoMap(),"4_7");
						try {
							testEnd = CmnDateUtil.getFormatDateTime(CmnDateUtil.getDate(testEnd, CsjConst.YYYY_MM_DD_SLASH), CsjConst.YYYY_MM_DD_SLASH);
						} catch (Exception e) {
						}
						
						String testState = getVal(sheetInfo.getCsjCellPosInfoMap(),"4_10");
						String testInfo = getVal(sheetInfo.getCsjCellPosInfoMap(),"6_1");
						
						
						String updateUser = getVal(sheetInfo.getCsjCellPosInfoMap(),sheetInfo.getCsjCellInfoMap().get("处置者").getRowNum() + "_"+(sheetInfo.getCsjCellInfoMap().get("处置者").getCellNum()+1));
						String updateBegin = getVal(sheetInfo.getCsjCellPosInfoMap(),sheetInfo.getCsjCellInfoMap().get("预定完了日").getRowNum() + "_"+(sheetInfo.getCsjCellInfoMap().get("预定完了日").getCellNum()+1));
						try {
							updateBegin = CmnDateUtil.getFormatDateTime(CmnDateUtil.getDate(updateBegin, CsjConst.YYYY_MM_DD_SLASH), CsjConst.YYYY_MM_DD_SLASH);
						} catch (Exception e) {
						}
						
						String updateEnd = getVal(sheetInfo.getCsjCellPosInfoMap(),sheetInfo.getCsjCellInfoMap().get("实际完了日").getRowNum() + "_"+(sheetInfo.getCsjCellInfoMap().get("实际完了日").getCellNum()+1));
						try {
							updateEnd = CmnDateUtil.getFormatDateTime(CmnDateUtil.getDate(updateEnd, CsjConst.YYYY_MM_DD_SLASH), CsjConst.YYYY_MM_DD_SLASH);
						} catch (Exception e) {
						}
						String updateState = getVal(sheetInfo.getCsjCellPosInfoMap(),sheetInfo.getCsjCellInfoMap().get("对应状态").getRowNum() + "_"+(sheetInfo.getCsjCellInfoMap().get("对应状态").getCellNum()+1));
						String updateInfo = getVal(sheetInfo.getCsjCellPosInfoMap(),(sheetInfo.getCsjCellInfoMap().get("处置者用").getRowNum()+1) + "_"+(sheetInfo.getCsjCellInfoMap().get("处置者用").getCellNum()+1));
						String bugFlg = getVal(sheetInfo.getCsjCellPosInfoMap(),sheetInfo.getCsjCellInfoMap().get("问题分类").getRowNum() + "_"+(sheetInfo.getCsjCellInfoMap().get("问题分类").getCellNum()+1));
						
						
						int[] numArr ={0,0,0,0,0,0,0,0};
						if (numMap.containsKey(testBegin)) {
							numArr = numMap.get(testBegin);
						} else {
							numMap.put(testBegin, numArr);
						}
						if ("UI错误".equals(bugFlg)) {
							numArr[0] = numArr[0] + 1;
						} else if("逻辑错误".equals(bugFlg)) {
							numArr[1] = numArr[1] + 1;
						} else if("显示不正".equals(bugFlg)) {
							numArr[2] = numArr[2] + 1;
						} else if("健壮性".equals(bugFlg)) {
							numArr[3] = numArr[3] + 1;
						} else if("建议".equals(bugFlg)) {
							numArr[4] = numArr[4] + 1;
						} else if("环境问题".equals(bugFlg)) {
							numArr[5] = numArr[5] + 1;
						} else if("操作不当".equals(bugFlg)) {
							numArr[6] = numArr[6] + 1;
						} else if("工具说明".equals(bugFlg)) {
							numArr[7] = numArr[7] + 1;
						}
						
						CmnPoiUtils.setCellValueWithCs(newSheet,rowNum,col++,String.valueOf(rowNum),cs);
						
						CmnPoiUtils.setCellValueWithCs(newSheet,rowNum,col++,CmnStrUtils.fromAtoBByTrim(CmnStrUtils.fromAtoBByTrim(f.getAbsolutePath(),FOLDER,""), "", ".xlsx").replaceAll("DBTOOLS_BUG_", ""),cs);
						CmnPoiUtils.setCellValueWithCs(newSheet,rowNum,col++,entry.getKey(),cs);
						CmnPoiUtils.setCellValueWithCs(newSheet,rowNum,col++,dbType,cs);
						CmnPoiUtils.setCellValueWithCs(newSheet,rowNum,col++,dbVersion,cs);
						CmnPoiUtils.setCellValueWithCs(newSheet,rowNum,col++,testUser,cs);
						CmnPoiUtils.setCellValueWithCs(newSheet,rowNum,col++,testBegin,cs);
						CmnPoiUtils.setCellValueWithCs(newSheet,rowNum,col++,testInfo,cs);
						CmnPoiUtils.setCellValueWithCs(newSheet,rowNum,col++,testEnd,cs);
						CmnPoiUtils.setCellValueWithCs(newSheet,rowNum,col++,testState,cs);
						CmnPoiUtils.setCellValueWithCs(newSheet,rowNum,col++,updateInfo,cs);
						CmnPoiUtils.setCellValueWithCs(newSheet,rowNum,col++,updateUser,cs);
						CmnPoiUtils.setCellValueWithCs(newSheet,rowNum,col++,updateBegin,cs);
						CmnPoiUtils.setCellValueWithCs(newSheet,rowNum,col++,updateEnd,cs);
						CmnPoiUtils.setCellValueWithCs(newSheet,rowNum,col++,updateState,cs);

						CmnPoiUtils.setCellValueWithCs(newSheet,rowNum,col++,bugFlg,cs);
						rowNum++;
						
					}
					
					int picRow = 1;
					for (Entry<String,int[]> entry : numMap.entrySet()) {
						CmnPoiUtils.setCellValueWithCs(picSheet,picRow,0,entry.getKey(),cs);
						CmnPoiUtils.setCellValueWithCs(picSheet,picRow,1,String.valueOf(entry.getValue()[0]),cs);
						CmnPoiUtils.setCellValueWithCs(picSheet,picRow,2,String.valueOf(entry.getValue()[1]),cs);
						CmnPoiUtils.setCellValueWithCs(picSheet,picRow,3,String.valueOf(entry.getValue()[2]),cs);
						CmnPoiUtils.setCellValueWithCs(picSheet,picRow,4,String.valueOf(entry.getValue()[3]),cs);
						CmnPoiUtils.setCellValueWithCs(picSheet,picRow,5,String.valueOf(entry.getValue()[4]),cs);
						CmnPoiUtils.setCellValueWithCs(picSheet,picRow,6,String.valueOf(entry.getValue()[5]),cs);
						CmnPoiUtils.setCellValueWithCs(picSheet,picRow,7,String.valueOf(entry.getValue()[6]),cs);
						CmnPoiUtils.setCellValueWithCs(picSheet,picRow,8,String.valueOf(entry.getValue()[7]),cs);
						picRow++;
					}
					
					wb.setSheetName(wb.getSheetIndex(newSheet.getSheetName()),"bug_list");
					fileOut = new FileOutputStream("C:\\Users\\Think\\Documents\\bk\\bug_list_" + CmnDateUtil.getFormatDate(CsjConst.YYYYMMDDHHMMSSMINUS_24)+".xlsx");
					wb.write(fileOut);
					fileOut.close();
					fileIn.close();
					wb.close();

			} catch (Throwable e) {
				e.printStackTrace();
		} finally {
			try {
				if (fileOut != null)
					fileOut.close();
				if (fileIn != null)
					fileIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public static String getVal(Map<String,CsjCellInfo> map,String key) {
		String retVal = "";
		if (map.containsKey(key)) {
			if (CmnStrUtils.isNotEmpty(map.get(key))) {
				retVal = map.get(key).getContent();
			}
		}
		return retVal;
	}
}

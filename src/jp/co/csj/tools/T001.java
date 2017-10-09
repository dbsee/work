package jp.co.csj.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.mydbsee.common.CmnDateUtil;
import org.mydbsee.common.CmnPoiUtils;

import jp.co.csj.tools.utils.common.constant.CsjConst;

public class T001 {

	public static void main(String[] args) {
		
		try {
			Workbook wb = CmnPoiUtils.getWorkBook("D:\\123.xlsx");
			Sheet st = wb.getSheetAt(0);
			
			Map<String,List<Bean>> map = new TreeMap<String,List<Bean>>();
			
			for(Row row : st) {
				System.out.println(row.getRowNum() + "--"+CmnPoiUtils.getCellContent(row, 0, false));
				System.out.println(row.getRowNum() + "--"+CmnPoiUtils.getCellContent(row, 1, false));
				System.out.println(row.getRowNum() + "--"+CmnPoiUtils.getCellContent(row, 2, false));
				if (row.getRowNum()<1) {
					continue;
				}
				String barcode = CmnPoiUtils.getCellContent(row, 0, false);
				String name = CmnPoiUtils.getCellContent(row, 1, false);
				String orderNum = CmnPoiUtils.getCellContent(row, 2, false);
				if (barcode.length()<5) {
					System.err.println("----" + barcode);
					continue;
				}
				String barcodeKey = barcode.substring(0,5);
				if (map.containsKey(barcodeKey)) {
					map.get(barcodeKey).add(new Bean(barcode, name, orderNum));
				} else {
					List<Bean> lst = new ArrayList<Bean>();
					lst.add(new Bean(barcode, name, orderNum));
					map.put(barcodeKey, lst);
				}
			}
			
			int sum =0;
			for(Entry<String,List<Bean>> entry : map.entrySet()) {
				Sheet stNew = wb.createSheet(entry.getKey());
				int i = 0;
				for(Bean b : entry.getValue()) {
					CmnPoiUtils.setCellValue(stNew, i, 0, b.barcode);
					CmnPoiUtils.setCellValue(stNew, i, 1, b.name);
					CmnPoiUtils.setCellValue(stNew, i, 2, b.orderNum);
					i++;
				}
				sum+=i;
			}
			
			String s_current_date = CmnDateUtil.getCurrentDateString(CsjConst.YYYYMMDDHH_MMSSMINUS_24);
			s_current_date = "_at_" + s_current_date;
			CmnPoiUtils.writeXls(wb, "D:\\auto\\", "123"+s_current_date);
	System.out.println("--->"+sum);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
}
class Bean {
	String barcode ;
	String name;
	String orderNum;
	public Bean(String barcode, String name, String orderNum) {
		this.barcode = barcode;
		this.name = name;
		this.orderNum = orderNum;
	}
	
}

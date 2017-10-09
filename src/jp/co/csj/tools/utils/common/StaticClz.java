/**
 *
 */
package jp.co.csj.tools.utils.common;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mydbsee.common.CmnPoiUtils;
import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.poi.core.CsjCellInfo;
import jp.co.csj.tools.utils.poi.core.CsjSheetInfo;


/**
 * @author Think
 *
 */
public class StaticClz {
	public static int ALL_INDEX = 0;
	public static int ONE_INDEX = 0;
	public static String TABLE_NM = CsjConst.EMPTY;
	public static Map<String,Integer> TBL_CNT_MAP = new HashMap<String,Integer>();
	public static void putTblCntMap(String tblNm, int num){
		if (TBL_CNT_MAP.containsKey(tblNm)) {
			TBL_CNT_MAP.put(tblNm, TBL_CNT_MAP.get(tblNm).intValue()+num);
		} else {
			TBL_CNT_MAP.put(tblNm, num);
		}
	}
	public static int getCurrentIndex(String tbl,boolean isSplit) {
		if (isSplit) {
			if (CmnStrUtils.isNotEmpty(TBL_CNT_MAP.get(tbl))) {
				return TBL_CNT_MAP.get(tbl).intValue();
			} else {
				return 0;
			}
		} else {
			return ONE_INDEX;
		}
	}
	public static void increseIndex() {
		ALL_INDEX++;
		ONE_INDEX++;
	}
	public static void getIndex(){
		for (int i = 0; i < 10000; i++) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ALL_INDEX++;
		}
	}
	public static String[] encodeArr = null;
	public static Set<String> sqlSet = new LinkedHashSet<String>();
	public static Set<String> funSet = new LinkedHashSet<String>();
	public static Set<String> tblNmSet = new LinkedHashSet<String>();
	public static Set<String> colNmSet = new LinkedHashSet<String>();
	public static List<Object> paraList = new ArrayList<Object>();
	public static void init() {

		Map<String, Charset> map = Charset.availableCharsets();
		encodeArr = new String[map.size()];
		int index = 0;
		for (Map.Entry<String, Charset> element : map.entrySet()) {
			encodeArr[index++] = element.getKey();
		}
		
	}
	public static void initKeyWord(String sheetNm) {

		try {
			HashMap<String, CsjSheetInfo> csjSheetInfoMap = CmnPoiUtils.getSheetContentsToMap(CsjPath.s_file_db_keywork, false);

			CsjSheetInfo sheetInfo = csjSheetInfoMap.get(sheetNm.toUpperCase());
			Map<Integer,List<CsjCellInfo>> csjCellPosInfoColList = sheetInfo.getCsjCellPosInfoColList();
			List<CsjCellInfo> sqlLst = csjCellPosInfoColList.get(0);
			if (CmnStrUtils.isNotEmpty(sqlLst)) {
				for (CsjCellInfo info : sqlLst) {
					String content = info.getContent().toLowerCase();
					if (CmnStrUtils.isNotEmpty(content)) {
						sqlSet.add(content);
					}
//					sqlSet.add(content+CsjConst.SIGN_SPACE_1);
//					sqlSet.add(CsjConst.SIGN_SPACE_1 + content+CsjConst.SIGN_SPACE_1);
//					sqlSet.add(CsjConst.SIGN_ENTER_N + content+CsjConst.SIGN_SPACE_1);
//					sqlSet.add(CsjConst.SIGN_TAB + content+CsjConst.SIGN_SPACE_1);
				}
			}
			List<CsjCellInfo> funLst = csjCellPosInfoColList.get(1);
			if (CmnStrUtils.isNotEmpty(funLst)) {
				for (CsjCellInfo info : funLst) {
					String content = info.getContent().toLowerCase();
					if (CmnStrUtils.isNotEmpty(content)) {
						funSet.add(content);//+CsjConst.SIGN_BRACKETS_S_L
					}
					
				}
			}
			sqlSet.add("select ");
			sqlSet.add("from ");
			sqlSet.add("where ");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

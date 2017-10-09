/**
 *
 */
package jp.co.csj.tools.utils.msg.dbtools;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.mydbsee.common.CmnLog;
import org.mydbsee.common.CmnLog5j;
import org.mydbsee.common.CmnPoiUtils;
import org.mydbsee.common.IConstFile;

import jp.co.csj.tools.utils.common.CsjPath;
import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.poi.core.CsjCellInfo;
import jp.co.csj.tools.utils.poi.core.CsjColInfo;
import jp.co.csj.tools.utils.poi.core.CsjSheetInfo;

/**
 * @author Think
 *
 */
public class CsjDbToolsMsg {

	public static String type_jp = "jp";
	public static String type_en = "en";
	public static String type_zh = "zh";

	public static CsjSheetInfo coreInfo = new CsjSheetInfo();
	public static CsjSheetInfo picInfo = new CsjSheetInfo();
	public static HashMap<String, String> coreMsgMap = new HashMap<String, String>();
	public static HashMap<String, String> picMsgMap = new HashMap<String, String>();

	public static String getMailMsg(String str) {
		StringBuffer sb = new StringBuffer();
		sb.append(picMsgMap.get(MSG_P_0000315));
		sb.append(CsjConst.MAIL_CR);
		sb.append(CsjConst.MAIL_CR);
		sb.append(str);
		sb.append(CsjConst.MAIL_CR);
		sb.append(CsjConst.MAIL_CR);
		sb.append(picMsgMap.get(MSG_P_0000320));
		sb.append(CsjConst.MAIL_CR);
		return sb.toString();
	}
	
	public static void getMsg(String language) throws Throwable {
		HashMap<String, CsjSheetInfo> map = CmnPoiUtils.getSheetContentsToMap(
				CsjPath.s_file_msg_path, true);
		picInfo = map.get("PIC");
		coreInfo = map.get("MSG");

		int coreValCol = coreInfo.getCsjCellInfoMap().get(language).getCellNum();
		CsjColInfo keyCoreInfo = coreInfo.getCsjColInfoMap().get(0);

		int picValCol = picInfo.getCsjCellInfoMap().get(language).getCellNum();
		CsjColInfo keyPicInfo = picInfo.getCsjColInfoMap().get(0);

		try {

			for (Entry<String, CsjCellInfo> entry : keyCoreInfo.getColMap()
					.entrySet()) {
				String key = entry.getKey();
				CsjCellInfo cellInfo = entry.getValue();
				String val = coreInfo.getCsjCellPosInfoMap()
						.get(cellInfo.getRowNum() + "_" + coreValCol).getContent();
				coreMsgMap.put(key, val);
			}
			for (Entry<String, CsjCellInfo> entry : keyPicInfo.getColMap()
					.entrySet()) {
				
				String key = entry.getKey();
				CsjCellInfo cellInfo = entry.getValue();
				String val = picInfo.getCsjCellPosInfoMap()
						.get(cellInfo.getRowNum() + "_" + picValCol).getContent();
				picMsgMap.put(key, val);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			CmnLog.logger.info(e.getMessage());
		}

	}
	public static void main(String[] args) {
		try {
			CmnLog5j.initLog5j("D:\\IDE\\eclipse-rcp-luna-SR2-win32\\eclipse\\workspace\\CsjToolsPic\\", "msg.txt", IConstFile.ENCODE_UTF_8);
			HashMap<String, CsjSheetInfo> map = CmnPoiUtils.getSheetContentsToMap(
						"D:\\IDE\\eclipse-rcp-luna-SR2-win32\\eclipse\\workspace\\CsjToolsPic\\msg\\msg.xls", true);
			coreInfo = map.get("MSG");
			picInfo = map.get("PIC");

			outputMsg(coreInfo);
			outputMsg(picInfo);
			CmnLog5j.closeLog5j();
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
	/**
	 * @param picInfo2
	 *
	 */
	public static void outputMsg(CsjSheetInfo info) {
		for (Entry<Integer, List<CsjCellInfo>> entry : info.getCsjCellPosInfoRowList().entrySet()) {
			List<CsjCellInfo> lst = entry.getValue();
			String id =lst.get(0).getContent();
			String msgzh=lst.get(1).getContent();
			String msgjp=lst.get(2).getContent();
			String msgen = lst.get(3).getContent();

			if ("No.".equals(id)) {
				continue;
			}
			String cmt = "/** msgId:"+id+" - msgZh:"+ msgzh+" - msgJp:"+msgjp+" - msgEn:"+msgen+" */";
			String code = "public static final String "+id+" = \""+ id+"\";";
			System.out.println(cmt);
			System.out.println(code);
			CmnLog5j.writeLine(cmt);
			CmnLog5j.writeLine(code);
		}
	}
	/** msgId:MSG_I_0000001 - msgZh:TABLE: - msgJp:TABLE: - msgEn:TODO1: */
	public static final String MSG_I_0000001 = "MSG_I_0000001";
	/** msgId:MSG_I_0000002 - msgZh:COL_COMENT: - msgJp:COL_COMENT: - msgEn:TODO2: */
	public static final String MSG_I_0000002 = "MSG_I_0000002";
	/** msgId:MSG_I_0000003 - msgZh:COL_EN: - msgJp:COL_EN: - msgEn:TODO3: */
	public static final String MSG_I_0000003 = "MSG_I_0000003";
	/** msgId:MSG_I_0000004 - msgZh:COL_TYPE: - msgJp:COL_TYPE: - msgEn:TODO4: */
	public static final String MSG_I_0000004 = "MSG_I_0000004";
	/** msgId:MSG_I_0000005 - msgZh:COL_NULL_OK: - msgJp:COL_NULL_OK: - msgEn:TODO5: */
	public static final String MSG_I_0000005 = "MSG_I_0000005";
	/** msgId:MSG_I_0000006 - msgZh:编号 - msgJp:番号 - msgEn:TODO6: */
	public static final String MSG_I_0000006 = "MSG_I_0000006";
	/** msgId:MSG_I_0000007 - msgZh:主键 - msgJp:主キー - msgEn:TODO7: */
	public static final String MSG_I_0000007 = "MSG_I_0000007";
	/** msgId:MSG_I_0000008 - msgZh:初期值 - msgJp:初期値 - msgEn:TODO8: */
	public static final String MSG_I_0000008 = "MSG_I_0000008";
	/** msgId:MSG_I_0000009 - msgZh:备注 - msgJp:コメント - msgEn:TODO9: */
	public static final String MSG_I_0000009 = "MSG_I_0000009";
	/** msgId:MSG_I_0000010 - msgZh:整数长度 - msgJp:整数長さ - msgEn:TODO10: */
	public static final String MSG_I_0000010 = "MSG_I_0000010";
	/** msgId:MSG_I_0000011 - msgZh:小数长度 - msgJp:小数長さ - msgEn:TODO11: */
	public static final String MSG_I_0000011 = "MSG_I_0000011";
	/** msgId:MSG_I_0000012 - msgZh:TYPE - msgJp:TYPE - msgEn:TODO12: */
	public static final String MSG_I_0000012 = "MSG_I_0000012";
	/** msgId:MSG_I_0000013 - msgZh:从第几行开始循环 - msgJp:繰り返す行 - msgEn:TODO13: */
	public static final String MSG_I_0000013 = "MSG_I_0000013";
	/** msgId:MSG_I_0000014 - msgZh:表的英文名称 - msgJp:テーブル英語名称 - msgEn:TODO14: */
	public static final String MSG_I_0000014 = "MSG_I_0000014";
	/** msgId:MSG_I_0000015 - msgZh:表的英文名称没有设定 - msgJp:テーブル英語名称を設定しない - msgEn:TODO15: */
	public static final String MSG_I_0000015 = "MSG_I_0000015";
	/** msgId:MSG_I_0000016 - msgZh:表的中文名称 - msgJp:テーブル和語名称 - msgEn:TODO16: */
	public static final String MSG_I_0000016 = "MSG_I_0000016";
	/** msgId:MSG_I_0000017 - msgZh:表的中文名称没有设定 - msgJp:テーブル和語名称を設定しない - msgEn:TODO17: */
	public static final String MSG_I_0000017 = "MSG_I_0000017";
	/** msgId:MSG_I_0000018 - msgZh:表的记录数 - msgJp:テーブル件数 - msgEn:TODO18: */
	public static final String MSG_I_0000018 = "MSG_I_0000018";
	/** msgId:MSG_I_0000019 - msgZh:表的记录数没有设定 - msgJp:テーブル件数を設定しない - msgEn:TODO19: */
	public static final String MSG_I_0000019 = "MSG_I_0000019";
	/** msgId:MSG_I_0000020 - msgZh:服务器的ID - msgJp:サーバID - msgEn:TODO20: */
	public static final String MSG_I_0000020 = "MSG_I_0000020";
	/** msgId:MSG_I_0000021 - msgZh:服务器的ID没有设定 - msgJp:サーバIDを設定しない - msgEn:TODO21: */
	public static final String MSG_I_0000021 = "MSG_I_0000021";
	/** msgId:MSG_I_0000022 - msgZh:用户名 - msgJp:ユーザ - msgEn:TODO22: */
	public static final String MSG_I_0000022 = "MSG_I_0000022";
	/** msgId:MSG_I_0000023 - msgZh:用户名没有设定 - msgJp:ユーザを設定しない - msgEn:TODO23: */
	public static final String MSG_I_0000023 = "MSG_I_0000023";
	/** msgId:MSG_I_0000024 - msgZh:密码 - msgJp:パスワード - msgEn:TODO24: */
	public static final String MSG_I_0000024 = "MSG_I_0000024";
	/** msgId:MSG_I_0000025 - msgZh:密码没有设定 - msgJp:パスワードを設定しない - msgEn:TODO25: */
	public static final String MSG_I_0000025 = "MSG_I_0000025";
	/** msgId:MSG_I_0000026 - msgZh:数据库的类型 - msgJp:データベース類型 - msgEn:TODO26: */
	public static final String MSG_I_0000026 = "MSG_I_0000026";
	/** msgId:MSG_I_0000027 - msgZh:数据库的类型没有设定 - msgJp:データベース類型を設定しない - msgEn:TODO27: */
	public static final String MSG_I_0000027 = "MSG_I_0000027";
	/** msgId:MSG_I_0000028 - msgZh:字段可否为空没有设定 - msgJp:項目NULLできるかを設定しない - msgEn:TODO28: */
	public static final String MSG_I_0000028 = "MSG_I_0000028";
	/** msgId:MSG_I_0000029 - msgZh:整数 - msgJp:整数 - msgEn:TODO29: */
	public static final String MSG_I_0000029 = "MSG_I_0000029";
	/** msgId:MSG_I_0000030 - msgZh:整数没有设定 - msgJp:整数を設定しない - msgEn:TODO30: */
	public static final String MSG_I_0000030 = "MSG_I_0000030";
	/** msgId:MSG_I_0000031 - msgZh:小数 - msgJp:小数 - msgEn:TODO31: */
	public static final String MSG_I_0000031 = "MSG_I_0000031";
	/** msgId:MSG_I_0000032 - msgZh:小数没有设定 - msgJp:小数を設定しない - msgEn:TODO32: */
	public static final String MSG_I_0000032 = "MSG_I_0000032";
	/** msgId:MSG_I_0000033 - msgZh:初期值没有设定 - msgJp:初期値を設定しない - msgEn:TODO33: */
	public static final String MSG_I_0000033 = "MSG_I_0000033";
	/** msgId:MSG_I_0000034 - msgZh:单元格的列 - msgJp:セールの列 - msgEn:TODO34: */
	public static final String MSG_I_0000034 = "MSG_I_0000034";
	/** msgId:MSG_I_0000035 - msgZh:(执行后) - msgJp:(実行後) - msgEn:TODO35: */
	public static final String MSG_I_0000035 = "MSG_I_0000035";
	/** msgId:MSG_I_0000036 - msgZh:这个表没有发现主键 - msgJp:キーは見つかれませんでした、仮キーを作成し、太字を設定してください！ - msgEn:TODO36: */
	public static final String MSG_I_0000036 = "MSG_I_0000036";
	/** msgId:MSG_I_0000037 - msgZh:DBTOOLS输出日志 - msgJp:ツールログ - msgEn:TODO37: */
	public static final String MSG_I_0000037 = "MSG_I_0000037";
	/** msgId:MSG_I_0000038 - msgZh:计算机MAC - msgJp:コンピュータのMAC - msgEn:TODO38: */
	public static final String MSG_I_0000038 = "MSG_I_0000038";
	/** msgId:MSG_I_0000039 - msgZh:计算机的信息 - msgJp:コンピュータの情報 - msgEn:TODO39: */
	public static final String MSG_I_0000039 = "MSG_I_0000039";
	/** msgId:MSG_I_0000040 - msgZh:表的名称 - msgJp:テーブル名称 - msgEn:TODO40: */
	public static final String MSG_I_0000040 = "MSG_I_0000040";
	/** msgId:MSG_I_0000041 - msgZh:执行的SQL语句 - msgJp:実行SQL文 - msgEn:TODO41: */
	public static final String MSG_I_0000041 = "MSG_I_0000041";
	/** msgId:MSG_I_0000042 - msgZh:数据库执行时间 - msgJp:DB実行タイミング - msgEn:TODO42: */
	public static final String MSG_I_0000042 = "MSG_I_0000042";
	/** msgId:MSG_I_0000043 - msgZh:类型： - msgJp:類型： - msgEn:TODO43: */
	public static final String MSG_I_0000043 = "MSG_I_0000043";
	/** msgId:MSG_I_0000044 - msgZh:主键没有设定 - msgJp:主キーを設定しない - msgEn:TODO44: */
	public static final String MSG_I_0000044 = "MSG_I_0000044";
	/** msgId:MSG_I_0000045 - msgZh:LENGTH - msgJp:LENGTH - msgEn:TODO45: */
	public static final String MSG_I_0000045 = "MSG_I_0000045";
	/** msgId:MSG_I_0000046 - msgZh:正则表达式 - msgJp:正則表現 - msgEn:regular */
	public static final String MSG_I_0000046 = "MSG_I_0000046";
	/** msgId:MSG_I_0000047 - msgZh:等于值 - msgJp:イコール - msgEn:TODO47: */
	public static final String MSG_I_0000047 = "MSG_I_0000047";
	/** msgId:MSG_I_0000048 - msgZh:最大值 - msgJp:最大値 - msgEn:TODO48: */
	public static final String MSG_I_0000048 = "MSG_I_0000048";
	/** msgId:MSG_I_0000049 - msgZh:最小值 - msgJp:最小値 - msgEn:TODO49: */
	public static final String MSG_I_0000049 = "MSG_I_0000049";
	/** msgId:MSG_I_0000050 - msgZh:最大长度 - msgJp:最大長さ - msgEn:TODO50: */
	public static final String MSG_I_0000050 = "MSG_I_0000050";
	/** msgId:MSG_I_0000051 - msgZh:最小长度 - msgJp:最小長さ - msgEn:TODO51: */
	public static final String MSG_I_0000051 = "MSG_I_0000051";
	/** msgId:MSG_I_0000052 - msgZh:备考 - msgJp:備考 - msgEn:TODO52: */
	public static final String MSG_I_0000052 = "MSG_I_0000052";
	/** msgId:MSG_I_0000053 - msgZh:正则表达式不正确 - msgJp:REGに合わない - msgEn:TODO53: */
	public static final String MSG_I_0000053 = "MSG_I_0000053";
	/** msgId:MSG_I_0000054 - msgZh:等于值不正确 - msgJp:イコール不正 - msgEn:TODO54: */
	public static final String MSG_I_0000054 = "MSG_I_0000054";
	/** msgId:MSG_I_0000055 - msgZh:大于最大值不正确 - msgJp:最大値不正 - msgEn:TODO55: */
	public static final String MSG_I_0000055 = "MSG_I_0000055";
	/** msgId:MSG_I_0000056 - msgZh:小于最小值不正确 - msgJp:最小値不正 - msgEn:TODO56: */
	public static final String MSG_I_0000056 = "MSG_I_0000056";
	/** msgId:MSG_I_0000057 - msgZh:长度大于最大长度不正确 - msgJp:最大長さ不正 - msgEn:TODO57: */
	public static final String MSG_I_0000057 = "MSG_I_0000057";
	/** msgId:MSG_I_0000058 - msgZh:长度小于最小长度不正确 - msgJp:最小長さ不正 - msgEn:TODO58: */
	public static final String MSG_I_0000058 = "MSG_I_0000058";
	/** msgId:MSG_I_0000059 - msgZh:不是数值类型 - msgJp:数値ではなりません - msgEn:TODO59: */
	public static final String MSG_I_0000059 = "MSG_I_0000059";
	/** msgId:MSG_I_0000060 - msgZh:不等于值 - msgJp:イコールではない - msgEn:TODO60: */
	public static final String MSG_I_0000060 = "MSG_I_0000060";
	/** msgId:MSG_I_0000061 - msgZh:不等于值不正确 - msgJp:イコールではないことは不正 - msgEn:TODO61: */
	public static final String MSG_I_0000061 = "MSG_I_0000061";
	/** msgId:MSG_I_0000062 - msgZh:不是正确的日期格式①．<年/月/日>(yyyy/MM/dd)②．<年/月/日 时:分:秒>(yyyy/MM/dd HH:mm:ss) - msgJp:日時ではなりません①．<年/月/日>(yyyy/MM/dd)②．<年/月/日 時:分:秒>(yyyy/MM/dd HH:mm:ss) - msgEn:TODO62: */
	public static final String MSG_I_0000062 = "MSG_I_0000062";
	/** msgId:MSG_I_0000063 - msgZh:不可为空项 - msgJp:空値できない項目 - msgEn:TODO63: */
	public static final String MSG_I_0000063 = "MSG_I_0000063";
	/** msgId:MSG_I_0000064 - msgZh:超过了字段最大整数位 - msgJp:項目に、最大整数位を超える - msgEn:TODO64: */
	public static final String MSG_I_0000064 = "MSG_I_0000064";
	/** msgId:MSG_I_0000065 - msgZh:超过了字段最大小数位 - msgJp:項目に、最大小数位を超える - msgEn:TODO65: */
	public static final String MSG_I_0000065 = "MSG_I_0000065";
	/** msgId:MSG_I_0000066 - msgZh:超过了字段最大字符串长度 - msgJp:項目に、最大文字数を超える - msgEn:TODO66: */
	public static final String MSG_I_0000066 = "MSG_I_0000066";
	/** msgId:MSG_I_0000067 - msgZh:因为是必须入力项，所以不可以是空值 - msgJp:NULL値できません - msgEn:TODO67: */
	public static final String MSG_I_0000067 = "MSG_I_0000067";
	/** msgId:MSG_I_0000068 - msgZh:由于表的字段超过了EXCEL的最大列数，在EXCEL里没有全部被保存 - msgJp:テーブルの列が多すぎるので、EXCELに、保存できないです。テーブルの名前は - msgEn:tables col is too max that excel can't save it -->table name is  */
	public static final String MSG_I_0000068 = "MSG_I_0000068";
	/** msgId:MSG_I_0000069 - msgZh:★ - msgJp:★ - msgEn:★ */
	public static final String MSG_I_0000069 = "MSG_I_0000069";
	/** msgId:MSG_I_0000070 - msgZh:) - msgJp:) - msgEn:) */
	public static final String MSG_I_0000070 = "MSG_I_0000070";
	/** msgId:MSG_I_0000071 - msgZh:; - msgJp:; - msgEn:; */
	public static final String MSG_I_0000071 = "MSG_I_0000071";
	/** msgId:MSG_I_0000072 - msgZh:4 - msgJp:4 - msgEn:4 */
	public static final String MSG_I_0000072 = "MSG_I_0000072";
	/** msgId:MSG_I_0000073 - msgZh:7 - msgJp:7 - msgEn:7 */
	public static final String MSG_I_0000073 = "MSG_I_0000073";
	/** msgId:MSG_I_0000074 - msgZh:●新建 - msgJp:●新規 - msgEn:●insert */
	public static final String MSG_I_0000074 = "MSG_I_0000074";
	/** msgId:MSG_I_0000075 - msgZh:●修改 - msgJp:●修正 - msgEn:●update */
	public static final String MSG_I_0000075 = "MSG_I_0000075";
	/** msgId:MSG_I_0000076 - msgZh:●删除 - msgJp:●削除 - msgEn:●delete */
	public static final String MSG_I_0000076 = "MSG_I_0000076";
	/** msgId:MSG_I_0000077 - msgZh:TBL_NM - msgJp:TBL_NM - msgEn:TBL_NM */
	public static final String MSG_I_0000077 = "MSG_I_0000077";
	/** msgId:MSG_I_0000078 - msgZh:TBL_CMT - msgJp:TBL_CMT - msgEn:TBL_CMT */
	public static final String MSG_I_0000078 = "MSG_I_0000078";
	/** msgId:MSG_I_0000079 - msgZh:FILE_REC数 - msgJp:FILE_REC数 - msgEn:TODO */
	public static final String MSG_I_0000079 = "MSG_I_0000079";
	/** msgId:MSG_I_0000080 - msgZh:DB数 - msgJp:DB数 - msgEn:TODO */
	public static final String MSG_I_0000080 = "MSG_I_0000080";
	/** msgId:MSG_I_0000081 - msgZh:修改数 - msgJp:修正数 - msgEn:TODO */
	public static final String MSG_I_0000081 = "MSG_I_0000081";
	/** msgId:MSG_I_0000082 - msgZh:删除数 - msgJp:削除数 - msgEn:TODO */
	public static final String MSG_I_0000082 = "MSG_I_0000082";
	/** msgId:MSG_I_0000083 - msgZh:新建数 - msgJp:新規数 - msgEn:TODO */
	public static final String MSG_I_0000083 = "MSG_I_0000083";
	/** msgId:MSG_I_0000084 - msgZh:总合计数 - msgJp:合計数 - msgEn:SUM */
	public static final String MSG_I_0000084 = "MSG_I_0000084";
	/** msgId:MSG_I_0000085 - msgZh:数据更新状态一览 - msgJp:レコード変更状態一覧 - msgEn:TODO */
	public static final String MSG_I_0000085 = "MSG_I_0000085";
	/** msgId:MSG_I_0000086 - msgZh:JDBC在ORACLE数据库中[LONG]和[LONG RAW]是读取不出来的，建议在表设计中，将含有这两个种类型的字段删除，之后再执行导入或导出 - msgJp:ORACLEに、LONGとLONG RAW類型は読まないです。テーブル設計に、この二つ類型のコラムを削除してから、導入導出進めます。 - msgEn:TODO */
	public static final String MSG_I_0000086 = "MSG_I_0000086";
	/** msgId:MSG_I_0000087 - msgZh:客户端时间 - msgJp:クライアント時間 - msgEn:CLIENT TIME */
	public static final String MSG_I_0000087 = "MSG_I_0000087";
	/** msgId:MSG_I_0000088 - msgZh:超过了单元格内容（文本）的最大长度 32767 个字符 - msgJp:セールの文字列最大長さ(32767)を超えてしまった。 - msgEn:TODO */
	public static final String MSG_I_0000088 = "MSG_I_0000088";
	/** msgId:MSG_I_0000089 - msgZh:_sql - msgJp:_sql - msgEn:_sql */
	public static final String MSG_I_0000089 = "MSG_I_0000089";
	/** msgId:MSG_I_0000090 - msgZh:.txt - msgJp:.txt - msgEn:.txt */
	public static final String MSG_I_0000090 = "MSG_I_0000090";
	/** msgId:MSG_I_0000091 - msgZh:表的检索条件不一致，请在[表设计]画面中执行与选中工作薄的同期化处理后，再进行对比操作。 - msgJp:テーブルの検索条件は不一致ので、「テーブル設計」画面に、選択されたシートとの同期化処理を実行しないと、比較できません。 - msgEn:TODO */
	public static final String MSG_I_0000091 = "MSG_I_0000091";
	/** msgId:MSG_I_0000092 - msgZh:表没有同期化，请在[表设计]画面中执行与选中工作薄的同期化处理后，再进行对比操作。 - msgJp:テーブル同期化しないので、「テーブル設計」画面に、選択されたシートとの同期化処理を実行しないと、比較できません。 - msgEn:TODO */
	public static final String MSG_I_0000092 = "MSG_I_0000092";
	/** msgId:MSG_I_0000093 - msgZh:● - msgJp:● - msgEn:● */
	public static final String MSG_I_0000093 = "MSG_I_0000093";
	/** msgId:MSG_I_0000094 - msgZh:删除 - msgJp:削除 - msgEn:DELETE */
	public static final String MSG_I_0000094 = "MSG_I_0000094";
	/** msgId:MSG_I_0000095 - msgZh:等待时间 - msgJp:待ち時間 - msgEn:wait time */
	public static final String MSG_I_0000095 = "MSG_I_0000095";
	/** msgId:MSG_I_0000096 - msgZh:执行程序 - msgJp:APを行う - msgEn:run application */
	public static final String MSG_I_0000096 = "MSG_I_0000096";
	/** msgId:MSG_I_0000097 - msgZh:文件复制 - msgJp:ファイルコピー - msgEn:TODO */
	public static final String MSG_I_0000097 = "MSG_I_0000097";
	/** msgId:MSG_I_0000098 - msgZh:数据库导入 - msgJp:DB導入 - msgEn:TODO */
	public static final String MSG_I_0000098 = "MSG_I_0000098";
	/** msgId:MSG_I_0000099 - msgZh:同期化处理 - msgJp:同期化処理 - msgEn:TODO */
	public static final String MSG_I_0000099 = "MSG_I_0000099";
	/** msgId:MSG_I_0000100 - msgZh:数据库导出全表 - msgJp:DB導出すべてテーブル - msgEn:TODO */
	public static final String MSG_I_0000100 = "MSG_I_0000100";
	/** msgId:MSG_I_0000101 - msgZh:数据库导出单表 - msgJp:DB導出一つテーブル - msgEn:TODO */
	public static final String MSG_I_0000101 = "MSG_I_0000101";
	/** msgId:MSG_I_0000102 - msgZh:数据库导出SQL - msgJp:DB導出のSQLを発行 - msgEn:TODO */
	public static final String MSG_I_0000102 = "MSG_I_0000102";
	/** msgId:MSG_I_0000103 - msgZh:数据库导出配置 - msgJp:DB導出配置ファイル - msgEn:TODO */
	public static final String MSG_I_0000103 = "MSG_I_0000103";
	/** msgId:MSG_I_0000104 - msgZh:表结构纠错 - msgJp:データ整形 - msgEn:TODO */
	public static final String MSG_I_0000104 = "MSG_I_0000104";
	/** msgId:MSG_I_0000105 - msgZh:表数据比较 - msgJp:データ比較 - msgEn:TODO */
	public static final String MSG_I_0000105 = "MSG_I_0000105";
	/** msgId:MSG_I_0000106 - msgZh:表结构导出 - msgJp:テーブル定義導出 - msgEn:TODO */
	public static final String MSG_I_0000106 = "MSG_I_0000106";
	/** msgId:MSG_I_0000107 - msgZh:数据库迁移 - msgJp:データ移行 - msgEn:TODO */
	public static final String MSG_I_0000107 = "MSG_I_0000107";
	/** msgId:MSG_I_0000108 - msgZh:建表SQL导出 - msgJp:SQL定義導出 - msgEn:TODO */
	public static final String MSG_I_0000108 = "MSG_I_0000108";
	/** msgId:MSG_I_0000109 - msgZh:制作表 - msgJp:テーブルを作成 - msgEn:TODO */
	public static final String MSG_I_0000109 = "MSG_I_0000109";
	/** msgId:MSG_I_0000110 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000110 = "MSG_I_0000110";
	/** msgId:MSG_I_0000111 - msgZh:目的文件不存在！ - msgJp:先ファイルがない - msgEn:TODO */
	public static final String MSG_I_0000111 = "MSG_I_0000111";
	/** msgId:MSG_I_0000112 - msgZh:来自文件或文件夹不存在！ - msgJp:元ファイルまたはフォルダがない - msgEn:TODO */
	public static final String MSG_I_0000112 = "MSG_I_0000112";
	/** msgId:MSG_I_0000113 - msgZh:目的文件夹不存在！ - msgJp:先フォルダがない - msgEn:TODO */
	public static final String MSG_I_0000113 = "MSG_I_0000113";
	/** msgId:MSG_I_0000114 - msgZh:工作薄名称 - msgJp:SHEET名前 - msgEn:TODO */
	public static final String MSG_I_0000114 = "MSG_I_0000114";
	/** msgId:MSG_I_0000115 - msgZh:启动次序 - msgJp:起動順次 - msgEn:TODO */
	public static final String MSG_I_0000115 = "MSG_I_0000115";
	/** msgId:MSG_I_0000116 - msgZh:参数KEY - msgJp:引数キー - msgEn:TODO */
	public static final String MSG_I_0000116 = "MSG_I_0000116";
	/** msgId:MSG_I_0000117 - msgZh:参数VAL - msgJp:引数VAL - msgEn:TODO */
	public static final String MSG_I_0000117 = "MSG_I_0000117";
	/** msgId:MSG_I_0000118 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000118 = "MSG_I_0000118";
	/** msgId:MSG_I_0000119 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000119 = "MSG_I_0000119";
	/** msgId:MSG_I_0000120 - msgZh:成果物路径 - msgJp:成果物場所 - msgEn:TODO */
	public static final String MSG_I_0000120 = "MSG_I_0000120";
	/** msgId:MSG_I_0000121 - msgZh:等待时间(微秒) - msgJp:待ち時間(ミリン秒) - msgEn:TODO */
	public static final String MSG_I_0000121 = "MSG_I_0000121";
	/** msgId:MSG_I_0000122 - msgZh:执行文件 - msgJp:執行ファイル - msgEn:TODO */
	public static final String MSG_I_0000122 = "MSG_I_0000122";
	/** msgId:MSG_I_0000123 - msgZh:来自文件夹 - msgJp:元フォルダー - msgEn:TODO */
	public static final String MSG_I_0000123 = "MSG_I_0000123";
	/** msgId:MSG_I_0000124 - msgZh:来自文件 - msgJp:元ファイル - msgEn:TODO */
	public static final String MSG_I_0000124 = "MSG_I_0000124";
	/** msgId:MSG_I_0000125 - msgZh:目的文件夹 - msgJp:先フォルダー - msgEn:TODO */
	public static final String MSG_I_0000125 = "MSG_I_0000125";
	/** msgId:MSG_I_0000126 - msgZh:目的文件 - msgJp:先ファイル - msgEn:TODO */
	public static final String MSG_I_0000126 = "MSG_I_0000126";
	/** msgId:MSG_I_0000127 - msgZh:数据位置 - msgJp:データ場所 - msgEn:TODO */
	public static final String MSG_I_0000127 = "MSG_I_0000127";
	/** msgId:MSG_I_0000128 - msgZh:工作薄 - msgJp:SHEET名前 - msgEn:TODO */
	public static final String MSG_I_0000128 = "MSG_I_0000128";
	/** msgId:MSG_I_0000129 - msgZh:表名 - msgJp:テーブル名 - msgEn:TODO */
	public static final String MSG_I_0000129 = "MSG_I_0000129";
	/** msgId:MSG_I_0000130 - msgZh:包含 - msgJp:含む - msgEn:TODO */
	public static final String MSG_I_0000130 = "MSG_I_0000130";
	/** msgId:MSG_I_0000131 - msgZh:同期化EXCEL文件 - msgJp:EXCEL同期化用 - msgEn:TODO */
	public static final String MSG_I_0000131 = "MSG_I_0000131";
	/** msgId:MSG_I_0000132 - msgZh:同期化工作薄 - msgJp:SHEET同期化用 - msgEn:TODO */
	public static final String MSG_I_0000132 = "MSG_I_0000132";
	/** msgId:MSG_I_0000133 - msgZh:同一工作薄 - msgJp:同じSHEET - msgEn:TODO */
	public static final String MSG_I_0000133 = "MSG_I_0000133";
	/** msgId:MSG_I_0000134 - msgZh:单表表名 - msgJp:テーブル名 - msgEn:TODO */
	public static final String MSG_I_0000134 = "MSG_I_0000134";
	/** msgId:MSG_I_0000135 - msgZh:表字段名 - msgJp:テーブル項目 - msgEn:TODO */
	public static final String MSG_I_0000135 = "MSG_I_0000135";
	/** msgId:MSG_I_0000136 - msgZh:文字类型模糊 - msgJp:文字の曖昧検索 - msgEn:TODO */
	public static final String MSG_I_0000136 = "MSG_I_0000136";
	/** msgId:MSG_I_0000137 - msgZh:数值最大 - msgJp:数字の最大値 - msgEn:TODO */
	public static final String MSG_I_0000137 = "MSG_I_0000137";
	/** msgId:MSG_I_0000138 - msgZh:数值最小 - msgJp:数字の最小値 - msgEn:TODO */
	public static final String MSG_I_0000138 = "MSG_I_0000138";
	/** msgId:MSG_I_0000139 - msgZh:日期最大 - msgJp:日付の最大値 - msgEn:TODO */
	public static final String MSG_I_0000139 = "MSG_I_0000139";
	/** msgId:MSG_I_0000140 - msgZh:日期最小 - msgJp:日付の最小値 - msgEn:TODO */
	public static final String MSG_I_0000140 = "MSG_I_0000140";
	/** msgId:MSG_I_0000141 - msgZh:SQL种类 - msgJp:SQL種別 - msgEn:TODO */
	public static final String MSG_I_0000141 = "MSG_I_0000141";
	/** msgId:MSG_I_0000142 - msgZh:SQL位置 - msgJp:SQL場所 - msgEn:TODO */
	public static final String MSG_I_0000142 = "MSG_I_0000142";
	/** msgId:MSG_I_0000143 - msgZh:配置位置 - msgJp:配置ファイル場所 - msgEn:TODO */
	public static final String MSG_I_0000143 = "MSG_I_0000143";
	/** msgId:MSG_I_0000144 - msgZh:旧文件位置 - msgJp:古いファイル場所 - msgEn:TODO */
	public static final String MSG_I_0000144 = "MSG_I_0000144";
	/** msgId:MSG_I_0000145 - msgZh:新文件位置 - msgJp:新しいファイル場所 - msgEn:TODO */
	public static final String MSG_I_0000145 = "MSG_I_0000145";
	/** msgId:MSG_I_0000146 - msgZh:数据差分方式（包含新建数据） - msgJp:データ差分方式(新規データ含む) - msgEn:TODO */
	public static final String MSG_I_0000146 = "MSG_I_0000146";
	/** msgId:MSG_I_0000147 - msgZh:表数据再导出 - msgJp:データもう一回導出 - msgEn:TODO */
	public static final String MSG_I_0000147 = "MSG_I_0000147";
	/** msgId:MSG_I_0000148 - msgZh:只输出变更记录 - msgJp:変更だけレコード導出 - msgEn:TODO */
	public static final String MSG_I_0000148 = "MSG_I_0000148";
	/** msgId:MSG_I_0000149 - msgZh:配置位置 - msgJp:配置ファイル場所 - msgEn:TODO */
	public static final String MSG_I_0000149 = "MSG_I_0000149";
	/** msgId:MSG_I_0000150 - msgZh:比较可否 - msgJp:比較可否 - msgEn:TODO */
	public static final String MSG_I_0000150 = "MSG_I_0000150";
	/** msgId:MSG_I_0000151 - msgZh:比较路径 - msgJp:比較場所 - msgEn:TODO */
	public static final String MSG_I_0000151 = "MSG_I_0000151";
	/** msgId:MSG_I_0000152 - msgZh:转换模式 - msgJp:転換モード - msgEn:TODO */
	public static final String MSG_I_0000152 = "MSG_I_0000152";
	/** msgId:MSG_I_0000153 - msgZh:选择建表根据方式 - msgJp:新規テーブル方式 - msgEn:TODO */
	public static final String MSG_I_0000153 = "MSG_I_0000153";
	/** msgId:MSG_I_0000154 - msgZh:来源路径 - msgJp:先場所 - msgEn:TODO */
	public static final String MSG_I_0000154 = "MSG_I_0000154";
	/** msgId:MSG_I_0000155 - msgZh:模板名称 - msgJp:テンプレート名前 - msgEn:TODO */
	public static final String MSG_I_0000155 = "MSG_I_0000155";
	/** msgId:MSG_I_0000156 - msgZh:文件来源 - msgJp:先ファイル - msgEn:TODO */
	public static final String MSG_I_0000156 = "MSG_I_0000156";
	/** msgId:MSG_I_0000157 - msgZh:表名称 - msgJp:テーブル名 - msgEn:TODO */
	public static final String MSG_I_0000157 = "MSG_I_0000157";
	/** msgId:MSG_I_0000158 - msgZh:制作方式 - msgJp:制作方式 - msgEn:TODO */
	public static final String MSG_I_0000158 = "MSG_I_0000158";
	/** msgId:MSG_I_0000159 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000159 = "MSG_I_0000159";
	/** msgId:MSG_I_0000160 - msgZh:等待文件 - msgJp:待ちファイル - msgEn:TODO */
	public static final String MSG_I_0000160 = "MSG_I_0000160";
	/** msgId:MSG_I_0000161 - msgZh:等待文件路径 - msgJp:待ちファイル場所 - msgEn:TODO */
	public static final String MSG_I_0000161 = "MSG_I_0000161";
	/** msgId:MSG_I_0000162 - msgZh:■对应列号 - msgJp:■列番号 - msgEn:TODO */
	public static final String MSG_I_0000162 = "MSG_I_0000162";
	/** msgId:MSG_I_0000163 - msgZh:■分隔符 - msgJp:■分割記号 - msgEn:TODO */
	public static final String MSG_I_0000163 = "MSG_I_0000163";
	/** msgId:MSG_I_0000164 - msgZh:■文件位置 - msgJp:■ファイル場所 - msgEn:TODO */
	public static final String MSG_I_0000164 = "MSG_I_0000164";
	/** msgId:MSG_I_0000165 - msgZh:■文本编码 - msgJp:■ファイルエンコード - msgEn:TODO */
	public static final String MSG_I_0000165 = "MSG_I_0000165";
	/** msgId:MSG_I_0000166 - msgZh:■文本起始行 - msgJp:■ファイル行目(から) - msgEn:TODO */
	public static final String MSG_I_0000166 = "MSG_I_0000166";
	/** msgId:MSG_I_0000167 - msgZh:■文本终了行 - msgJp:■ファイル行目(まで) - msgEn:TODO */
	public static final String MSG_I_0000167 = "MSG_I_0000167";
	/** msgId:MSG_I_0000168 - msgZh:导出文本数据 - msgJp:出力csv - msgEn:TODO */
	public static final String MSG_I_0000168 = "MSG_I_0000168";
	/** msgId:MSG_I_0000169 - msgZh:分隔符 - msgJp:分割記号 - msgEn:TODO */
	public static final String MSG_I_0000169 = "MSG_I_0000169";
	/** msgId:MSG_I_0000170 - msgZh:换行符 - msgJp:改行記号 - msgEn:TODO */
	public static final String MSG_I_0000170 = "MSG_I_0000170";
	/** msgId:MSG_I_0000171 - msgZh:.txt - msgJp:.txt - msgEn:.txt */
	public static final String MSG_I_0000171 = "MSG_I_0000171";
	/** msgId:MSG_I_0000172 - msgZh:文本导入出错 - msgJp:txtファイル導入エラー - msgEn:TODO */
	public static final String MSG_I_0000172 = "MSG_I_0000172";
	/** msgId:MSG_I_0000173 - msgZh:删除文件 - msgJp:ファイルを削除 - msgEn:TODO */
	public static final String MSG_I_0000173 = "MSG_I_0000173";
	/** msgId:MSG_I_0000174 - msgZh:删除文件夹路径 - msgJp:フォルダーを削除 - msgEn:TODO */
	public static final String MSG_I_0000174 = "MSG_I_0000174";
	/** msgId:MSG_I_0000175 - msgZh:删除文件名匹配 - msgJp:曖昧ファイル名 - msgEn:TODO */
	public static final String MSG_I_0000175 = "MSG_I_0000175";
	/** msgId:MSG_I_0000176 - msgZh:配置文件 - msgJp:配置ファイル - msgEn:TODO */
	public static final String MSG_I_0000176 = "MSG_I_0000176";
	/** msgId:MSG_I_0000177 - msgZh:配置文件名 - msgJp:配置ファイル名 - msgEn:TODO */
	public static final String MSG_I_0000177 = "MSG_I_0000177";
	/** msgId:MSG_I_0000178 - msgZh:数据库种类 - msgJp:DB種別 - msgEn:TODO */
	public static final String MSG_I_0000178 = "MSG_I_0000178";
	/** msgId:MSG_I_0000179 - msgZh:发送邮件 - msgJp:送信メール - msgEn:TODO */
	public static final String MSG_I_0000179 = "MSG_I_0000179";
	/** msgId:MSG_I_0000180 - msgZh:分割导出 - msgJp:分割導出 - msgEn:TODO */
	public static final String MSG_I_0000180 = "MSG_I_0000180";
	/** msgId:MSG_I_0000181 - msgZh:校验/执行 - msgJp:チェック/執行 - msgEn:TODO */
	public static final String MSG_I_0000181 = "MSG_I_0000181";
	/** msgId:MSG_I_0000182 - msgZh:编码格式 - msgJp:エンコード - msgEn:encode */
	public static final String MSG_I_0000182 = "MSG_I_0000182";
	/** msgId:MSG_I_0000183 - msgZh:批处理 - msgJp:バッチ - msgEn:TODO */
	public static final String MSG_I_0000183 = "MSG_I_0000183";
	/** msgId:MSG_I_0000184 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000184 = "MSG_I_0000184";
	/** msgId:MSG_I_0000185 - msgZh:文件夹路径 - msgJp:フォルダー - msgEn:TODO */
	public static final String MSG_I_0000185 = "MSG_I_0000185";
	/** msgId:MSG_I_0000186 - msgZh:邮件地址 - msgJp:メール先 - msgEn:TODO */
	public static final String MSG_I_0000186 = "MSG_I_0000186";
	/** msgId:MSG_I_0000187 - msgZh:标题 - msgJp:件名 - msgEn:TODO */
	public static final String MSG_I_0000187 = "MSG_I_0000187";
	/** msgId:MSG_I_0000188 - msgZh:正文 - msgJp:本文 - msgEn:TODO */
	public static final String MSG_I_0000188 = "MSG_I_0000188";
	/** msgId:MSG_I_0000189 - msgZh:附件名 - msgJp:添付名 - msgEn:TODO */
	public static final String MSG_I_0000189 = "MSG_I_0000189";
	/** msgId:MSG_I_0000190 - msgZh:附件密码 - msgJp:添付パスワード - msgEn:TODO */
	public static final String MSG_I_0000190 = "MSG_I_0000190";
	/** msgId:MSG_I_0000191 - msgZh:I   db-> - msgJp:I   db-> - msgEn:I   db-> */
	public static final String MSG_I_0000191 = "MSG_I_0000191";
	/** msgId:MSG_I_0000192 - msgZh:D   db-> - msgJp:D   db-> - msgEn:D   db-> */
	public static final String MSG_I_0000192 = "MSG_I_0000192";
	/** msgId:MSG_I_0000193 - msgZh:U   db-> - msgJp:U   db-> - msgEn:U   db-> */
	public static final String MSG_I_0000193 = "MSG_I_0000193";
	/** msgId:MSG_I_0000194 - msgZh:U file-> - msgJp:U file-> - msgEn:U file-> */
	public static final String MSG_I_0000194 = "MSG_I_0000194";
	/** msgId:MSG_I_0000195 - msgZh:发送邮件报告结果 - msgJp:処理結果をメールで送信 - msgEn:TODO */
	public static final String MSG_I_0000195 = "MSG_I_0000195";
	/** msgId:MSG_I_0000196 - msgZh:1000 - msgJp:1000 - msgEn:1000 */
	public static final String MSG_I_0000196 = "MSG_I_0000196";
	/** msgId:MSG_I_0000197 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000197 = "MSG_I_0000197";
	/** msgId:MSG_I_0000198 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000198 = "MSG_I_0000198";
	/** msgId:MSG_I_0000199 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000199 = "MSG_I_0000199";
	/** msgId:MSG_I_0000200 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000200 = "MSG_I_0000200";
	/** msgId:MSG_I_0000201 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000201 = "MSG_I_0000201";
	/** msgId:MSG_I_0000202 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000202 = "MSG_I_0000202";
	/** msgId:MSG_I_0000203 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000203 = "MSG_I_0000203";
	/** msgId:MSG_I_0000204 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000204 = "MSG_I_0000204";
	/** msgId:MSG_I_0000205 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000205 = "MSG_I_0000205";
	/** msgId:MSG_I_0000206 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000206 = "MSG_I_0000206";
	/** msgId:MSG_I_0000207 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000207 = "MSG_I_0000207";
	/** msgId:MSG_I_0000208 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000208 = "MSG_I_0000208";
	/** msgId:MSG_I_0000209 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000209 = "MSG_I_0000209";
	/** msgId:MSG_I_0000210 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000210 = "MSG_I_0000210";
	/** msgId:MSG_I_0000211 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000211 = "MSG_I_0000211";
	/** msgId:MSG_I_0000212 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000212 = "MSG_I_0000212";
	/** msgId:MSG_I_0000213 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000213 = "MSG_I_0000213";
	/** msgId:MSG_I_0000214 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000214 = "MSG_I_0000214";
	/** msgId:MSG_I_0000215 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000215 = "MSG_I_0000215";
	/** msgId:MSG_I_0000216 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000216 = "MSG_I_0000216";
	/** msgId:MSG_I_0000217 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000217 = "MSG_I_0000217";
	/** msgId:MSG_I_0000218 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000218 = "MSG_I_0000218";
	/** msgId:MSG_I_0000219 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000219 = "MSG_I_0000219";
	/** msgId:MSG_I_0000220 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000220 = "MSG_I_0000220";
	/** msgId:MSG_I_0000221 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000221 = "MSG_I_0000221";
	/** msgId:MSG_I_0000222 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000222 = "MSG_I_0000222";
	/** msgId:MSG_I_0000223 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000223 = "MSG_I_0000223";
	/** msgId:MSG_I_0000224 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000224 = "MSG_I_0000224";
	/** msgId:MSG_I_0000225 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000225 = "MSG_I_0000225";
	/** msgId:MSG_I_0000226 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000226 = "MSG_I_0000226";
	/** msgId:MSG_I_0000227 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000227 = "MSG_I_0000227";
	/** msgId:MSG_I_0000228 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000228 = "MSG_I_0000228";
	/** msgId:MSG_I_0000229 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000229 = "MSG_I_0000229";
	/** msgId:MSG_I_0000230 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000230 = "MSG_I_0000230";
	/** msgId:MSG_I_0000231 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000231 = "MSG_I_0000231";
	/** msgId:MSG_I_0000232 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000232 = "MSG_I_0000232";
	/** msgId:MSG_I_0000233 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000233 = "MSG_I_0000233";
	/** msgId:MSG_I_0000234 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000234 = "MSG_I_0000234";
	/** msgId:MSG_I_0000235 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000235 = "MSG_I_0000235";
	/** msgId:MSG_I_0000236 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000236 = "MSG_I_0000236";
	/** msgId:MSG_I_0000237 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000237 = "MSG_I_0000237";
	/** msgId:MSG_I_0000238 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000238 = "MSG_I_0000238";
	/** msgId:MSG_I_0000239 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000239 = "MSG_I_0000239";
	/** msgId:MSG_I_0000240 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000240 = "MSG_I_0000240";
	/** msgId:MSG_I_0000241 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000241 = "MSG_I_0000241";
	/** msgId:MSG_I_0000242 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000242 = "MSG_I_0000242";
	/** msgId:MSG_I_0000243 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000243 = "MSG_I_0000243";
	/** msgId:MSG_I_0000244 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000244 = "MSG_I_0000244";
	/** msgId:MSG_I_0000245 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000245 = "MSG_I_0000245";
	/** msgId:MSG_I_0000246 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000246 = "MSG_I_0000246";
	/** msgId:MSG_I_0000247 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000247 = "MSG_I_0000247";
	/** msgId:MSG_I_0000248 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000248 = "MSG_I_0000248";
	/** msgId:MSG_I_0000249 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000249 = "MSG_I_0000249";
	/** msgId:MSG_I_0000250 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000250 = "MSG_I_0000250";
	/** msgId:MSG_I_0000251 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000251 = "MSG_I_0000251";
	/** msgId:MSG_I_0000252 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000252 = "MSG_I_0000252";
	/** msgId:MSG_I_0000253 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000253 = "MSG_I_0000253";
	/** msgId:MSG_I_0000254 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000254 = "MSG_I_0000254";
	/** msgId:MSG_I_0000255 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000255 = "MSG_I_0000255";
	/** msgId:MSG_I_0000256 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000256 = "MSG_I_0000256";
	/** msgId:MSG_I_0000257 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000257 = "MSG_I_0000257";
	/** msgId:MSG_I_0000258 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000258 = "MSG_I_0000258";
	/** msgId:MSG_I_0000259 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000259 = "MSG_I_0000259";
	/** msgId:MSG_I_0000260 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000260 = "MSG_I_0000260";
	/** msgId:MSG_I_0000261 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000261 = "MSG_I_0000261";
	/** msgId:MSG_I_0000262 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000262 = "MSG_I_0000262";
	/** msgId:MSG_I_0000263 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000263 = "MSG_I_0000263";
	/** msgId:MSG_I_0000264 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000264 = "MSG_I_0000264";
	/** msgId:MSG_I_0000265 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000265 = "MSG_I_0000265";
	/** msgId:MSG_I_0000266 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000266 = "MSG_I_0000266";
	/** msgId:MSG_I_0000267 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000267 = "MSG_I_0000267";
	/** msgId:MSG_I_0000268 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000268 = "MSG_I_0000268";
	/** msgId:MSG_I_0000269 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000269 = "MSG_I_0000269";
	/** msgId:MSG_I_0000270 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000270 = "MSG_I_0000270";
	/** msgId:MSG_I_0000271 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000271 = "MSG_I_0000271";
	/** msgId:MSG_I_0000272 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000272 = "MSG_I_0000272";
	/** msgId:MSG_I_0000273 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000273 = "MSG_I_0000273";
	/** msgId:MSG_I_0000274 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000274 = "MSG_I_0000274";
	/** msgId:MSG_I_0000275 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000275 = "MSG_I_0000275";
	/** msgId:MSG_I_0000276 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000276 = "MSG_I_0000276";
	/** msgId:MSG_I_0000277 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000277 = "MSG_I_0000277";
	/** msgId:MSG_I_0000278 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000278 = "MSG_I_0000278";
	/** msgId:MSG_I_0000279 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000279 = "MSG_I_0000279";
	/** msgId:MSG_I_0000280 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000280 = "MSG_I_0000280";
	/** msgId:MSG_I_0000281 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000281 = "MSG_I_0000281";
	/** msgId:MSG_I_0000282 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000282 = "MSG_I_0000282";
	/** msgId:MSG_I_0000283 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000283 = "MSG_I_0000283";
	/** msgId:MSG_I_0000284 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000284 = "MSG_I_0000284";
	/** msgId:MSG_I_0000285 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000285 = "MSG_I_0000285";
	/** msgId:MSG_I_0000286 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000286 = "MSG_I_0000286";
	/** msgId:MSG_I_0000287 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000287 = "MSG_I_0000287";
	/** msgId:MSG_I_0000288 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000288 = "MSG_I_0000288";
	/** msgId:MSG_I_0000289 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000289 = "MSG_I_0000289";
	/** msgId:MSG_I_0000290 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000290 = "MSG_I_0000290";
	/** msgId:MSG_I_0000291 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000291 = "MSG_I_0000291";
	/** msgId:MSG_I_0000292 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000292 = "MSG_I_0000292";
	/** msgId:MSG_I_0000293 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000293 = "MSG_I_0000293";
	/** msgId:MSG_I_0000294 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000294 = "MSG_I_0000294";
	/** msgId:MSG_I_0000295 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000295 = "MSG_I_0000295";
	/** msgId:MSG_I_0000296 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000296 = "MSG_I_0000296";
	/** msgId:MSG_I_0000297 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000297 = "MSG_I_0000297";
	/** msgId:MSG_I_0000298 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000298 = "MSG_I_0000298";
	/** msgId:MSG_I_0000299 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000299 = "MSG_I_0000299";
	/** msgId:MSG_I_0000300 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_I_0000300 = "MSG_I_0000300";
	/** msgId:MSG_P_0000001 - msgZh:数据库相关 - msgJp:DBに関する - msgEn:TODO */
	public static final String MSG_P_0000001 = "MSG_P_0000001";
	/** msgId:MSG_P_0000002 - msgZh:数据库导入 - msgJp:DB導入 - msgEn:TODO */
	public static final String MSG_P_0000002 = "MSG_P_0000002";
	/** msgId:MSG_P_0000003 - msgZh:数据位置 - msgJp:データ位置 - msgEn:TODO */
	public static final String MSG_P_0000003 = "MSG_P_0000003";
	/** msgId:MSG_P_0000004 - msgZh:请选择文件 - msgJp:データファイルを選んでください - msgEn:TODO */
	public static final String MSG_P_0000004 = "MSG_P_0000004";
	/** msgId:MSG_P_0000005 - msgZh:导入处理 - msgJp:導入する - msgEn:TODO */
	public static final String MSG_P_0000005 = "MSG_P_0000005";
	/** msgId:MSG_P_0000006 - msgZh:工作薄 - msgJp:シート - msgEn:TODO */
	public static final String MSG_P_0000006 = "MSG_P_0000006";
	/** msgId:MSG_P_0000007 - msgZh:错误信息 - msgJp:エラーメッセージ - msgEn:TODO */
	public static final String MSG_P_0000007 = "MSG_P_0000007";
	/** msgId:MSG_P_0000008 - msgZh:表 - msgJp:テーブル - msgEn:TODO */
	public static final String MSG_P_0000008 = "MSG_P_0000008";
	/** msgId:MSG_P_0000009 - msgZh:如果选择了的话，只是导入左面的表。如果不选择的话，只有左面的表不进行导入 - msgJp:選択したら、左側のテーブルは導入のみです。選択しないと、左側のテーブルだけ導入しません。 - msgEn:TODO */
	public static final String MSG_P_0000009 = "MSG_P_0000009";
	/** msgId:MSG_P_0000010 - msgZh:包含 - msgJp:含む - msgEn:TODO */
	public static final String MSG_P_0000010 = "MSG_P_0000010";
	/** msgId:MSG_P_0000011 - msgZh:数据库导出 - msgJp:DB導出 - msgEn:TODO */
	public static final String MSG_P_0000011 = "MSG_P_0000011";
	/** msgId:MSG_P_0000012 - msgZh:(%s)件记录受了到影响。 - msgJp:(%s)件レコードを影響しました。 - msgEn:TODO */
	public static final String MSG_P_0000012 = "MSG_P_0000012";
	/** msgId:MSG_P_0000013 - msgZh:确认 - msgJp:確認 - msgEn:TODO */
	public static final String MSG_P_0000013 = "MSG_P_0000013";
	/** msgId:MSG_P_0000014 - msgZh:SQL文件不可以没有 - msgJp:SQL ファイル が必要です - msgEn:TODO */
	public static final String MSG_P_0000014 = "MSG_P_0000014";
	/** msgId:MSG_P_0000015 - msgZh:已经处理成功 - msgJp:成功処理しました - msgEn:TODO */
	public static final String MSG_P_0000015 = "MSG_P_0000015";
	/** msgId:MSG_P_0000016 - msgZh:EXCEL数据文件不可以没有 - msgJp:EXCELデータファイル が必要です - msgEn:TODO */
	public static final String MSG_P_0000016 = "MSG_P_0000016";
	/** msgId:MSG_P_0000017 - msgZh:所有表都进行导出处理吗？ - msgJp:すべてのテーブルを導出しますか？ - msgEn:TODO */
	public static final String MSG_P_0000017 = "MSG_P_0000017";
	/** msgId:MSG_P_0000018 - msgZh:导出 - msgJp:導出 - msgEn:TODO */
	public static final String MSG_P_0000018 = "MSG_P_0000018";
	/** msgId:MSG_P_0000019 - msgZh:根据表设计导出 - msgJp:テーブル設計で出力 - msgEn:TODO */
	public static final String MSG_P_0000019 = "MSG_P_0000019";
	/** msgId:MSG_P_0000020 - msgZh:表 - msgJp:テーブル - msgEn:TODO */
	public static final String MSG_P_0000020 = "MSG_P_0000020";
	/** msgId:MSG_P_0000021 - msgZh:日期类型 - msgJp:日付類型 - msgEn:TODO */
	public static final String MSG_P_0000021 = "MSG_P_0000021";
	/** msgId:MSG_P_0000022 - msgZh:SQL的位置 - msgJp:SQL位置 - msgEn:TODO */
	public static final String MSG_P_0000022 = "MSG_P_0000022";
	/** msgId:MSG_P_0000023 - msgZh:SQL文件(*.*) - msgJp:SQLファイル(*.*) - msgEn:TODO */
	public static final String MSG_P_0000023 = "MSG_P_0000023";
	/** msgId:MSG_P_0000024 - msgZh:请选择SQL文件 - msgJp:SQLファイルを選択してください - msgEn:TODO */
	public static final String MSG_P_0000024 = "MSG_P_0000024";
	/** msgId:MSG_P_0000025 - msgZh:数字类型 - msgJp:数字類型 - msgEn:TODO */
	public static final String MSG_P_0000025 = "MSG_P_0000025";
	/** msgId:MSG_P_0000026 - msgZh:文字类型 - msgJp:文字類型 - msgEn:TODO */
	public static final String MSG_P_0000026 = "MSG_P_0000026";
	/** msgId:MSG_P_0000027 - msgZh:全字段补值 - msgJp:項目補足 - msgEn:TODO */
	public static final String MSG_P_0000027 = "MSG_P_0000027";
	/** msgId:MSG_P_0000028 - msgZh:EXCEL数据文件 - msgJp:EXCELデータ - msgEn:TODO */
	public static final String MSG_P_0000028 = "MSG_P_0000028";
	/** msgId:MSG_P_0000029 - msgZh:配置文件(*.xls|*.xlsx) - msgJp:配置ファイル(*.xls|*.xlsx) - msgEn:TODO */
	public static final String MSG_P_0000029 = "MSG_P_0000029";
	/** msgId:MSG_P_0000030 - msgZh:请选择配置文件 - msgJp:配置ファイルを選択してください - msgEn:TODO */
	public static final String MSG_P_0000030 = "MSG_P_0000030";
	/** msgId:MSG_P_0000031 - msgZh:请选择数据库种类 - msgJp:DB類型を選択 - msgEn:TODO */
	public static final String MSG_P_0000031 = "MSG_P_0000031";
	/** msgId:MSG_P_0000032 - msgZh:请选择一个数据库 - msgJp:一つデーターベースを選んでください！ - msgEn:TODO */
	public static final String MSG_P_0000032 = "MSG_P_0000032";
	/** msgId:MSG_P_0000033 - msgZh:配置 - msgJp:配置 - msgEn:TODO */
	public static final String MSG_P_0000033 = "MSG_P_0000033";
	/** msgId:MSG_P_0000034 - msgZh:退出 - msgJp:閉じる - msgEn:TODO */
	public static final String MSG_P_0000034 = "MSG_P_0000034";
	/** msgId:MSG_P_0000035 - msgZh:帮助 - msgJp:ヘルプ - msgEn:TODO */
	public static final String MSG_P_0000035 = "MSG_P_0000035";
	/** msgId:MSG_P_0000036 - msgZh:执行 - msgJp:実行 - msgEn:TODO */
	public static final String MSG_P_0000036 = "MSG_P_0000036";
	/** msgId:MSG_P_0000037 - msgZh:数据建表SQL导出 - msgJp:テーブル定義SQL導出 - msgEn:TODO */
	public static final String MSG_P_0000037 = "MSG_P_0000037";
	/** msgId:MSG_P_0000038 - msgZh:EXCEL位置 - msgJp:EXCEL位置 - msgEn:TODO */
	public static final String MSG_P_0000038 = "MSG_P_0000038";
	/** msgId:MSG_P_0000039 - msgZh:SQL导出 - msgJp:SQL導出 - msgEn:TODO */
	public static final String MSG_P_0000039 = "MSG_P_0000039";
	/** msgId:MSG_P_0000040 - msgZh:根据DB - msgJp:DBにより - msgEn:TODO */
	public static final String MSG_P_0000040 = "MSG_P_0000040";
	/** msgId:MSG_P_0000041 - msgZh:根据表数据 - msgJp:データにより - msgEn:TODO */
	public static final String MSG_P_0000041 = "MSG_P_0000041";
	/** msgId:MSG_P_0000042 - msgZh:表数据比较 - msgJp:レコード差分 - msgEn:TODO */
	public static final String MSG_P_0000042 = "MSG_P_0000042";
	/** msgId:MSG_P_0000043 - msgZh:旧文件位置 - msgJp:パス(古) - msgEn:TODO */
	public static final String MSG_P_0000043 = "MSG_P_0000043";
	/** msgId:MSG_P_0000044 - msgZh:数据文件(*.xls) - msgJp:データファイル(*.xls) - msgEn:TODO */
	public static final String MSG_P_0000044 = "MSG_P_0000044";
	/** msgId:MSG_P_0000045 - msgZh:选择数据文件 - msgJp:データファイルを選んでください - msgEn:TODO */
	public static final String MSG_P_0000045 = "MSG_P_0000045";
	/** msgId:MSG_P_0000046 - msgZh:比较处理 - msgJp:差分する - msgEn:TODO */
	public static final String MSG_P_0000046 = "MSG_P_0000046";
	/** msgId:MSG_P_0000047 - msgZh:新文件位置 - msgJp:パス(新) - msgEn:TODO */
	public static final String MSG_P_0000047 = "MSG_P_0000047";
	/** msgId:MSG_P_0000048 - msgZh:表结构变更，数据保留处理 - msgJp:データマジ - msgEn:TODO */
	public static final String MSG_P_0000048 = "MSG_P_0000048";
	/** msgId:MSG_P_0000049 - msgZh:再表设计里面可以更改检索条件范围，差分数据 - msgJp:テーブル設計ボタン押したら、テーブルを検索条件を修正できます。 - msgEn:TODO */
	public static final String MSG_P_0000049 = "MSG_P_0000049";
	/** msgId:MSG_P_0000050 - msgZh:数据差分(包含插入记录) - msgJp:データ差分(新規含む) - msgEn:TODO */
	public static final String MSG_P_0000050 = "MSG_P_0000050";
	/** msgId:MSG_P_0000051 - msgZh:XLS和DB比较(KEY) - msgJp:XLSとDB差分(KEY) - msgEn:TODO */
	public static final String MSG_P_0000051 = "MSG_P_0000051";
	/** msgId:MSG_P_0000052 - msgZh:数据差分 - msgJp:データ差分 - msgEn:TODO */
	public static final String MSG_P_0000052 = "MSG_P_0000052";
	/** msgId:MSG_P_0000053 - msgZh:执行处理 - msgJp:実行する - msgEn:TODO */
	public static final String MSG_P_0000053 = "MSG_P_0000053";
	/** msgId:MSG_P_0000054 - msgZh:格式化 - msgJp:フォーマット - msgEn:TODO */
	public static final String MSG_P_0000054 = "MSG_P_0000054";
	/** msgId:MSG_P_0000055 - msgZh:表名称 - msgJp:テーブル名称 - msgEn:TODO */
	public static final String MSG_P_0000055 = "MSG_P_0000055";
	/** msgId:MSG_P_0000056 - msgZh:生成 - msgJp:生成 - msgEn:TODO */
	public static final String MSG_P_0000056 = "MSG_P_0000056";
	/** msgId:MSG_P_0000057 - msgZh:复写 - msgJp:複写 - msgEn:TODO */
	public static final String MSG_P_0000057 = "MSG_P_0000057";
	/** msgId:MSG_P_0000058 - msgZh:数据情报导出 - msgJp:DB情報導出 - msgEn:TODO */
	public static final String MSG_P_0000058 = "MSG_P_0000058";
	/** msgId:MSG_P_0000059 - msgZh:关于表结构 - msgJp:テーブル定義 - msgEn:TODO */
	public static final String MSG_P_0000059 = "MSG_P_0000059";
	/** msgId:MSG_P_0000060 - msgZh:配置位置 - msgJp:配置位置 - msgEn:TODO */
	public static final String MSG_P_0000060 = "MSG_P_0000060";
	/** msgId:MSG_P_0000061 - msgZh:构造导出 - msgJp:構造導出 - msgEn:TODO */
	public static final String MSG_P_0000061 = "MSG_P_0000061";
	/** msgId:MSG_P_0000062 - msgZh:更改配置文件 - msgJp:配置ファイルを編集します。 - msgEn:TODO */
	public static final String MSG_P_0000062 = "MSG_P_0000062";
	/** msgId:MSG_P_0000063 - msgZh:更改 - msgJp:編集 - msgEn:TODO */
	public static final String MSG_P_0000063 = "MSG_P_0000063";
	/** msgId:MSG_P_0000064 - msgZh:数据文件(*.xls|*.xlsx) - msgJp:データファイル(*.xls|*.xlsx) - msgEn:TODO */
	public static final String MSG_P_0000064 = "MSG_P_0000064";
	/** msgId:MSG_P_0000065 - msgZh:可比较 - msgJp:差分可 - msgEn:TODO */
	public static final String MSG_P_0000065 = "MSG_P_0000065";
	/** msgId:MSG_P_0000066 - msgZh:表以外 - msgJp:tables以外 - msgEn:TODO */
	public static final String MSG_P_0000066 = "MSG_P_0000066";
	/** msgId:MSG_P_0000067 - msgZh:数据库变换 - msgJp:データベース変換 - msgEn:TODO */
	public static final String MSG_P_0000067 = "MSG_P_0000067";
	/** msgId:MSG_P_0000068 - msgZh:赠送 - msgJp:おまけ - msgEn:TODO */
	public static final String MSG_P_0000068 = "MSG_P_0000068";
	/** msgId:MSG_P_0000069 - msgZh:注册序列号 - msgJp:ログイン - msgEn:TODO */
	public static final String MSG_P_0000069 = "MSG_P_0000069";
	/** msgId:MSG_P_0000070 - msgZh:数据库综合管理 - msgJp:統合データベース - msgEn:TODO */
	public static final String MSG_P_0000070 = "MSG_P_0000070";
	/** msgId:MSG_P_0000071 - msgZh:取得临时的序列号 - msgJp:一時SNを取得します。 - msgEn:TODO */
	public static final String MSG_P_0000071 = "MSG_P_0000071";
	/** msgId:MSG_P_0000072 - msgZh:注册码： - msgJp:SN: - msgEn:TODO */
	public static final String MSG_P_0000072 = "MSG_P_0000072";
	/** msgId:MSG_P_0000073 - msgZh:取得临时的序列号 - msgJp:ライセンスを取得 - msgEn:TODO */
	public static final String MSG_P_0000073 = "MSG_P_0000073";
	/** msgId:MSG_P_0000074 - msgZh:Registration - msgJp:Registration - msgEn:Registration */
	public static final String MSG_P_0000074 = "MSG_P_0000074";
	/** msgId:MSG_P_0000075 - msgZh:★　取得注册信息发送给作者 - msgJp:★　Get Regedit Info and Mail to Author - msgEn:★　Get Regedit Info and Mail to Author */
	public static final String MSG_P_0000075 = "MSG_P_0000075";
	/** msgId:MSG_P_0000076 - msgZh:离开 - msgJp:EXIT - msgEn:TODO */
	public static final String MSG_P_0000076 = "MSG_P_0000076";
	/** msgId:MSG_P_0000077 - msgZh:信息 - msgJp:Info Dialog - msgEn:TODO */
	public static final String MSG_P_0000077 = "MSG_P_0000077";
	/** msgId:MSG_P_0000078 - msgZh:退出 - msgJp:退出 - msgEn:TODO */
	public static final String MSG_P_0000078 = "MSG_P_0000078";
	/** msgId:MSG_P_0000079 - msgZh:行列转换 - msgJp:行列転換 - msgEn:TODO */
	public static final String MSG_P_0000079 = "MSG_P_0000079";
	/** msgId:MSG_P_0000080 - msgZh:国际化 - msgJp:国際化 - msgEn:TODO */
	public static final String MSG_P_0000080 = "MSG_P_0000080";
	/** msgId:MSG_P_0000081 - msgZh:重新注册 - msgJp:再登録 - msgEn:TODO */
	public static final String MSG_P_0000081 = "MSG_P_0000081";
	/** msgId:MSG_P_0000082 - msgZh:重新注册序列号吗？[确认]按钮按下后，原有注册信息将被删除！ - msgJp:ライセンスを登録しましょうか？もし、「確認」ボタンを押したら、もとライセンスがなくなります！ - msgEn:TODO */
	public static final String MSG_P_0000082 = "MSG_P_0000082";
	/** msgId:MSG_P_0000083 - msgZh:更改数据库配置 - msgJp:配置の設定 - msgEn:TODO */
	public static final String MSG_P_0000083 = "MSG_P_0000083";
	/** msgId:MSG_P_0000084 - msgZh:数据库连接 - msgJp:DB接続 - msgEn:TODO */
	public static final String MSG_P_0000084 = "MSG_P_0000084";
	/** msgId:MSG_P_0000085 - msgZh:端口号 - msgJp:PORT - msgEn:TODO */
	public static final String MSG_P_0000085 = "MSG_P_0000085";
	/** msgId:MSG_P_0000086 - msgZh:选中时导出文本文件，非选中时导出EXCEL数据文件 - msgJp:選択された場合、テキストに出力。選択しないと、EXCELに出力。 - msgEn:TODO */
	public static final String MSG_P_0000086 = "MSG_P_0000086";
	/** msgId:MSG_P_0000087 - msgZh:概要 - msgJp:概要 - msgEn:TODO */
	public static final String MSG_P_0000087 = "MSG_P_0000087";
	/** msgId:MSG_P_0000088 - msgZh:日语 - msgJp:和語 - msgEn:TODO */
	public static final String MSG_P_0000088 = "MSG_P_0000088";
	/** msgId:MSG_P_0000089 - msgZh:英语 - msgJp:英語 - msgEn:TODO */
	public static final String MSG_P_0000089 = "MSG_P_0000089";
	/** msgId:MSG_P_0000090 - msgZh:类型 - msgJp:類型 - msgEn:TODO */
	public static final String MSG_P_0000090 = "MSG_P_0000090";
	/** msgId:MSG_P_0000091 - msgZh:主键 - msgJp:KEY - msgEn:TODO */
	public static final String MSG_P_0000091 = "MSG_P_0000091";
	/** msgId:MSG_P_0000092 - msgZh:zzz_csj_log表中插入数据 - msgJp:zzz_csj_logに挿入 - msgEn:TODO */
	public static final String MSG_P_0000092 = "MSG_P_0000092";
	/** msgId:MSG_P_0000093 - msgZh:可输出日志 - msgJp:ログできる - msgEn:TODO */
	public static final String MSG_P_0000093 = "MSG_P_0000093";
	/** msgId:MSG_P_0000094 - msgZh:表中数据件数显示 - msgJp:テーブルのデータを現す - msgEn:TODO */
	public static final String MSG_P_0000094 = "MSG_P_0000094";
	/** msgId:MSG_P_0000095 - msgZh:表中数据件数 - msgJp:データ件数 - msgEn:TODO */
	public static final String MSG_P_0000095 = "MSG_P_0000095";
	/** msgId:MSG_P_0000096 - msgZh:从XLS到数据库操作时，插入数据 - msgJp:XLSからDBへ挿入可否（選択しない場合、削除のみです） - msgEn:TODO */
	public static final String MSG_P_0000096 = "MSG_P_0000096";
	/** msgId:MSG_P_0000097 - msgZh:可插入 - msgJp:挿入可 - msgEn:TODO */
	public static final String MSG_P_0000097 = "MSG_P_0000097";
	/** msgId:MSG_P_0000098 - msgZh:要将XLS文件导入数据库 - msgJp:XLSからDBへ導入 - msgEn:TODO */
	public static final String MSG_P_0000098 = "MSG_P_0000098";
	/** msgId:MSG_P_0000099 - msgZh:导入数据库 - msgJp:DBに導入 - msgEn:TODO */
	public static final String MSG_P_0000099 = "MSG_P_0000099";
	/** msgId:MSG_P_0000100 - msgZh:单元格设定 - msgJp:セル類型 - msgEn:TODO */
	public static final String MSG_P_0000100 = "MSG_P_0000100";
	/** msgId:MSG_P_0000101 - msgZh:表标志 - msgJp:TABLE_SIGN - msgEn:TODO */
	public static final String MSG_P_0000101 = "MSG_P_0000101";
	/** msgId:MSG_P_0000102 - msgZh:表英文名 - msgJp:EN_NAME_COL - msgEn:TODO */
	public static final String MSG_P_0000102 = "MSG_P_0000102";
	/** msgId:MSG_P_0000103 - msgZh:表日文名 - msgJp:JP_NAME_COL - msgEn:TODO */
	public static final String MSG_P_0000103 = "MSG_P_0000103";
	/** msgId:MSG_P_0000104 - msgZh:表列信息 - msgJp:COL_INFO - msgEn:TODO */
	public static final String MSG_P_0000104 = "MSG_P_0000104";
	/** msgId:MSG_P_0000105 - msgZh:最大字段数 - msgJp:maxCol - msgEn:TODO */
	public static final String MSG_P_0000105 = "MSG_P_0000105";
	/** msgId:MSG_P_0000106 - msgZh:MAX行数 - msgJp:MAX行数 - msgEn:TODO */
	public static final String MSG_P_0000106 = "MSG_P_0000106";
	/** msgId:MSG_P_0000107 - msgZh:日期格式 - msgJp:日付格式 - msgEn:TODO */
	public static final String MSG_P_0000107 = "MSG_P_0000107";
	/** msgId:MSG_P_0000108 - msgZh:数字格式 - msgJp:数字格式 - msgEn:TODO */
	public static final String MSG_P_0000108 = "MSG_P_0000108";
	/** msgId:MSG_P_0000109 - msgZh:错误的场合，继续处理 - msgJp:エラー場合、処理続け - msgEn:TODO */
	public static final String MSG_P_0000109 = "MSG_P_0000109";
	/** msgId:MSG_P_0000110 - msgZh:忽略错误 - msgJp:エラー続け - msgEn:TODO */
	public static final String MSG_P_0000110 = "MSG_P_0000110";
	/** msgId:MSG_P_0000111 - msgZh:表数据清除 - msgJp:テーブルクリア - msgEn:TODO */
	public static final String MSG_P_0000111 = "MSG_P_0000111";
	/** msgId:MSG_P_0000112 - msgZh:数据库 - msgJp:Database - msgEn:TODO */
	public static final String MSG_P_0000112 = "MSG_P_0000112";
	/** msgId:MSG_P_0000113 - msgZh:模式 - msgJp:SCHEMA - msgEn:TODO */
	public static final String MSG_P_0000113 = "MSG_P_0000113";
	/** msgId:MSG_P_0000114 - msgZh:表数据全件分割导出 - msgJp:分割でテーブルデータを導出 - msgEn:TODO */
	public static final String MSG_P_0000114 = "MSG_P_0000114";
	/** msgId:MSG_P_0000115 - msgZh:欢迎使用 - msgJp:ようこそ - msgEn:TODO */
	public static final String MSG_P_0000115 = "MSG_P_0000115";
	/** msgId:MSG_P_0000116 - msgZh:退出 - msgJp:退出 - msgEn:TODO */
	public static final String MSG_P_0000116 = "MSG_P_0000116";
	/** msgId:MSG_P_0000117 - msgZh:退出吗？ - msgJp:退出？ - msgEn:TODO */
	public static final String MSG_P_0000117 = "MSG_P_0000117";
	/** msgId:MSG_P_0000118 - msgZh:关于PenguinsDbTools - msgJp:PenguinsDbToolsについて - msgEn:TODO */
	public static final String MSG_P_0000118 = "MSG_P_0000118";
	/** msgId:MSG_P_0000119 - msgZh:如果有问题的话，请联系@# - msgJp:問題あれば、@#に連絡ください - msgEn:TODO */
	public static final String MSG_P_0000119 = "MSG_P_0000119";
	/** msgId:MSG_P_0000120 - msgZh:显示画面 - msgJp:画面を表す - msgEn:TODO */
	public static final String MSG_P_0000120 = "MSG_P_0000120";
	/** msgId:MSG_P_0000121 - msgZh:图标最小化 - msgJp:アイコン化する - msgEn:TODO */
	public static final String MSG_P_0000121 = "MSG_P_0000121";
	/** msgId:MSG_P_0000122 - msgZh:其他 - msgJp:其の他 - msgEn:TODO */
	public static final String MSG_P_0000122 = "MSG_P_0000122";
	/** msgId:MSG_P_0000123 - msgZh:文件格式 - msgJp:ファイル形式 - msgEn:TODO */
	public static final String MSG_P_0000123 = "MSG_P_0000123";
	/** msgId:MSG_P_0000124 - msgZh:来自文件夹 - msgJp:フォルダから - msgEn:TODO */
	public static final String MSG_P_0000124 = "MSG_P_0000124";
	/** msgId:MSG_P_0000125 - msgZh:包含子文件夹 - msgJp:子フォルダを含む - msgEn:TODO */
	public static final String MSG_P_0000125 = "MSG_P_0000125";
	/** msgId:MSG_P_0000126 - msgZh:转至文件夹 - msgJp:フォルダまで - msgEn:TODO */
	public static final String MSG_P_0000126 = "MSG_P_0000126";
	/** msgId:MSG_P_0000127 - msgZh:文件编码格式：%-20s - msgJp:ファイルエンコード：%-20s - msgEn:TODO */
	public static final String MSG_P_0000127 = "MSG_P_0000127";
	/** msgId:MSG_P_0000128 - msgZh:检查编码格式 - msgJp:チェックエンコード - msgEn:TODO */
	public static final String MSG_P_0000128 = "MSG_P_0000128";
	/** msgId:MSG_P_0000129 - msgZh:开始转换 - msgJp:転換する - msgEn:TODO */
	public static final String MSG_P_0000129 = "MSG_P_0000129";
	/** msgId:MSG_P_0000130 - msgZh:正则表达式 - msgJp:正则公式 - msgEn:TODO */
	public static final String MSG_P_0000130 = "MSG_P_0000130";
	/** msgId:MSG_P_0000131 - msgZh:编码格式 - msgJp:エンコード - msgEn:TODO */
	public static final String MSG_P_0000131 = "MSG_P_0000131";
	/** msgId:MSG_P_0000132 - msgZh:关于数据库转换 - msgJp:DB転換について - msgEn:TODO */
	public static final String MSG_P_0000132 = "MSG_P_0000132";
	/** msgId:MSG_P_0000133 - msgZh:无类型 - msgJp:類型なし - msgEn:TODO */
	public static final String MSG_P_0000133 = "MSG_P_0000133";
	/** msgId:MSG_P_0000134 - msgZh:删除选中的配置文件吗？ - msgJp:選択した配置ファイルを削除しますか？ - msgEn:TODO */
	public static final String MSG_P_0000134 = "MSG_P_0000134";
	/** msgId:MSG_P_0000135 - msgZh:删除 - msgJp:削除 - msgEn:TODO */
	public static final String MSG_P_0000135 = "MSG_P_0000135";
	/** msgId:MSG_P_0000136 - msgZh:设置新的配置文件 - msgJp:配置ファイルを新規 - msgEn:TODO */
	public static final String MSG_P_0000136 = "MSG_P_0000136";
	/** msgId:MSG_P_0000137 - msgZh:新建 - msgJp:新規 - msgEn:TODO */
	public static final String MSG_P_0000137 = "MSG_P_0000137";
	/** msgId:MSG_P_0000138 - msgZh:根据表结构 - msgJp:テーブル構造により - msgEn:TODO */
	public static final String MSG_P_0000138 = "MSG_P_0000138";
	/** msgId:MSG_P_0000139 - msgZh:制作表 - msgJp:テーブル作成 - msgEn:TableMaker */
	public static final String MSG_P_0000139 = "MSG_P_0000139";
	/** msgId:MSG_P_0000140 - msgZh:日志 - msgJp:ログ - msgEn:view */
	public static final String MSG_P_0000140 = "MSG_P_0000140";
	/** msgId:MSG_P_0000141 - msgZh:表结构信息 - msgJp:テーブル定義 - msgEn:table info */
	public static final String MSG_P_0000141 = "MSG_P_0000141";
	/** msgId:MSG_P_0000142 - msgZh:程序重启动后有效 - msgJp:本AP再起動場合、有効できます - msgEn:TODO */
	public static final String MSG_P_0000142 = "MSG_P_0000142";
	/** msgId:MSG_P_0000143 - msgZh:记录 - msgJp:レコード - msgEn:TODO */
	public static final String MSG_P_0000143 = "MSG_P_0000143";
	/** msgId:MSG_P_0000144 - msgZh:OK - msgJp:OK - msgEn:OK */
	public static final String MSG_P_0000144 = "MSG_P_0000144";
	/** msgId:MSG_P_0000145 - msgZh:表 - msgJp:テーブル - msgEn:TODO */
	public static final String MSG_P_0000145 = "MSG_P_0000145";
	/** msgId:MSG_P_0000146 - msgZh:IP地址 - msgJp:IP - msgEn:IP */
	public static final String MSG_P_0000146 = "MSG_P_0000146";
	/** msgId:MSG_P_0000147 - msgZh:端口号 - msgJp:PORT - msgEn:PORT */
	public static final String MSG_P_0000147 = "MSG_P_0000147";
	/** msgId:MSG_P_0000148 - msgZh:用户名 - msgJp:USER - msgEn:USER */
	public static final String MSG_P_0000148 = "MSG_P_0000148";
	/** msgId:MSG_P_0000149 - msgZh:密码 - msgJp:PASSWORD - msgEn:PASSWORD */
	public static final String MSG_P_0000149 = "MSG_P_0000149";
	/** msgId:MSG_P_0000150 - msgZh:对不起，您还没有注册该软件。 - msgJp:すみません、このソフトは購入しません。 - msgEn:sorry! the soft is unregister。 */
	public static final String MSG_P_0000150 = "MSG_P_0000150";
	/** msgId:MSG_P_0000151 - msgZh:TODO - msgJp:TODO - msgEn:use time over */
	public static final String MSG_P_0000151 = "MSG_P_0000151";
	/** msgId:MSG_P_0000152 - msgZh:---------------------- - msgJp:---------------------- - msgEn:---------------------- */
	public static final String MSG_P_0000152 = "MSG_P_0000152";
	/** msgId:MSG_P_0000153 - msgZh:・一个月    人民币(10元) - msgJp:・一ヶ月間   日円(10元) - msgEn:・1 month     RMB(10yuan) */
	public static final String MSG_P_0000153 = "MSG_P_0000153";
	/** msgId:MSG_P_0000154 - msgZh:・三个月    人民币(20元) - msgJp:・三ヶ月間   日円(20元) - msgEn:・3 months    RMB(20yuan) */
	public static final String MSG_P_0000154 = "MSG_P_0000154";
	/** msgId:MSG_P_0000155 - msgZh:・十二个月  人民币(60元) - msgJp:・一年間     日円(60元) - msgEn:・1 year      RMB(60yuan) */
	public static final String MSG_P_0000155 = "MSG_P_0000155";
	/** msgId:MSG_P_0000156 - msgZh:・永久使用  人民币(100元) - msgJp:・永久利用   日円(100元) - msgEn:・100 years   RMB(100yuan) */
	public static final String MSG_P_0000156 = "MSG_P_0000156";
	/** msgId:MSG_P_0000157 - msgZh:・发现错误  人民币(0元) - msgJp:・バグを見る 日円(0元) - msgEn:・if find bug RMB(0元) */
	public static final String MSG_P_0000157 = "MSG_P_0000157";
	/** msgId:MSG_P_0000158 - msgZh:・共达社员  人民币(0元) - msgJp:・共達社員   日円(0元) - msgEn:・cnc members RMB(0元) */
	public static final String MSG_P_0000158 = "MSG_P_0000158";
	/** msgId:MSG_P_0000159 - msgZh:如果想使用,请发MAIL到 - msgJp:利用したいお客様、メールを連絡ください - msgEn:if you want to use,please mail to */
	public static final String MSG_P_0000159 = "MSG_P_0000159";
	/** msgId:MSG_P_0000160 - msgZh:更新SQL文 - msgJp:is cmd sql - msgEn:is cmd sql */
	public static final String MSG_P_0000160 = "MSG_P_0000160";
	/** msgId:MSG_P_0000161 - msgZh:SQL文窗口 - msgJp:SQL文 - msgEn:SQL DATA VIEW */
	public static final String MSG_P_0000161 = "MSG_P_0000161";
	/** msgId:MSG_P_0000162 - msgZh:正则 - msgJp:regex - msgEn:regex */
	public static final String MSG_P_0000162 = "MSG_P_0000162";
	/** msgId:MSG_P_0000163 - msgZh:非法的EXCEL文件路径或文件路径不存在。 - msgJp:ファイルパスを確認ください。 - msgEn:TODO */
	public static final String MSG_P_0000163 = "MSG_P_0000163";
	/** msgId:MSG_P_0000164 - msgZh:新旧文件夹下面，没有发现相同的文件名。 - msgJp:新旧ファイルパスに、同じファイルを見つかれません。 - msgEn:TODO */
	public static final String MSG_P_0000164 = "MSG_P_0000164";
	/** msgId:MSG_P_0000165 - msgZh:临时注册码 - msgJp:一時のライセンス - msgEn:TODO */
	public static final String MSG_P_0000165 = "MSG_P_0000165";
	/** msgId:MSG_P_0000166 - msgZh:导出处理 - msgJp:導出処理 - msgEn:TODO */
	public static final String MSG_P_0000166 = "MSG_P_0000166";
	/** msgId:MSG_P_0000167 - msgZh:最大列数 - msgJp:最大列 - msgEn:maxCol */
	public static final String MSG_P_0000167 = "MSG_P_0000167";
	/** msgId:MSG_P_0000168 - msgZh:行列转换 - msgJp:行列転換 - msgEn:TODO */
	public static final String MSG_P_0000168 = "MSG_P_0000168";
	/** msgId:MSG_P_0000169 - msgZh:注册 - msgJp:Regedit - msgEn:Regedit */
	public static final String MSG_P_0000169 = "MSG_P_0000169";
	/** msgId:MSG_P_0000170 - msgZh:发MAIL给作者 - msgJp:Mail to Author - msgEn:Mail to Author */
	public static final String MSG_P_0000170 = "MSG_P_0000170";
	/** msgId:MSG_P_0000171 - msgZh:应用 - msgJp:適用 - msgEn:update */
	public static final String MSG_P_0000171 = "MSG_P_0000171";
	/** msgId:MSG_P_0000172 - msgZh:关闭 - msgJp:閉じる - msgEn:close */
	public static final String MSG_P_0000172 = "MSG_P_0000172";
	/** msgId:MSG_P_0000173 - msgZh:修正您要更改的表结构 - msgJp:修正したいテーブルの設計 - msgEn:TODO */
	public static final String MSG_P_0000173 = "MSG_P_0000173";
	/** msgId:MSG_P_0000174 - msgZh:表的导出设计 - msgJp:テーブル導出設計 - msgEn:TODO */
	public static final String MSG_P_0000174 = "MSG_P_0000174";
	/** msgId:MSG_P_0000175 - msgZh:表的设计 - msgJp:TBL設計 - msgEn:TODO */
	public static final String MSG_P_0000175 = "MSG_P_0000175";
	/** msgId:MSG_P_0000176 - msgZh:连接 - msgJp:接続 - msgEn:TODO */
	public static final String MSG_P_0000176 = "MSG_P_0000176";
	/** msgId:MSG_P_0000177 - msgZh:连接数据库 - msgJp:接続データベース - msgEn:TODO */
	public static final String MSG_P_0000177 = "MSG_P_0000177";
	/** msgId:MSG_P_0000178 - msgZh:表的结构已经发生了变化 - msgJp:テーブルの構造は変更された。 - msgEn:TODO */
	public static final String MSG_P_0000178 = "MSG_P_0000178";
	/** msgId:MSG_P_0000179 - msgZh:欢迎使用 - msgJp:ようこそ - msgEn:TODO */
	public static final String MSG_P_0000179 = "MSG_P_0000179";
	/** msgId:MSG_P_0000180 - msgZh:当前系统不支持系统托盘 - msgJp:該当システムのアイコン化は支持できません - msgEn:TODO */
	public static final String MSG_P_0000180 = "MSG_P_0000180";
	/** msgId:MSG_P_0000181 - msgZh:企鹅:2796523176 - msgJp:QQ:2796523176 - msgEn:TODO */
	public static final String MSG_P_0000181 = "MSG_P_0000181";
	/** msgId:MSG_P_0000182 - msgZh:企鹅群:184715368 - msgJp:QQ群:184715368 - msgEn:TODO */
	public static final String MSG_P_0000182 = "MSG_P_0000182";
	/** msgId:MSG_P_0000183 - msgZh:关于工具 - msgJp:DbToolsについて - msgEn:About DbTools */
	public static final String MSG_P_0000183 = "MSG_P_0000183";
	/** msgId:MSG_P_0000184 - msgZh:执行结果 - msgJp:完了しました - msgEn:TODO */
	public static final String MSG_P_0000184 = "MSG_P_0000184";
	/** msgId:MSG_P_0000185 - msgZh:信息一览 - msgJp:情報一覧 - msgEn:TODO */
	public static final String MSG_P_0000185 = "MSG_P_0000185";
	/** msgId:MSG_P_0000186 - msgZh:信息无 - msgJp:情報なし - msgEn:TODO */
	public static final String MSG_P_0000186 = "MSG_P_0000186";
	/** msgId:MSG_P_0000187 - msgZh:错误信息 - msgJp:エラーメッセージ - msgEn:TODO */
	public static final String MSG_P_0000187 = "MSG_P_0000187";
	/** msgId:MSG_P_0000188 - msgZh:生成的内容一览 - msgJp:生成した内容を一覧する - msgEn:TODO */
	public static final String MSG_P_0000188 = "MSG_P_0000188";
	/** msgId:MSG_P_0000189 - msgZh:亲~ - msgJp:すみませんですが - msgEn:TODO */
	public static final String MSG_P_0000189 = "MSG_P_0000189";
	/** msgId:MSG_P_0000190 - msgZh:由于您尚未对该软件注册,使用该软件时,将有部分功能限制! - msgJp:ソフトウェアを登録していないので、ソフトウェアを使用して、いくつかの機能的な制限がある！ - msgEn:TODO */
	public static final String MSG_P_0000190 = "MSG_P_0000190";
	/** msgId:MSG_P_0000191 - msgZh:建议您正式注册该软件,来取得最佳的数据移行体验! - msgJp:最高のデータ移行の経験を得るために、ソフトウェアを登録することをお勧めします！ - msgEn:TODO */
	public static final String MSG_P_0000191 = "MSG_P_0000191";
	/** msgId:MSG_P_0000192 - msgZh:数据效验出错 - msgJp:データチェックエラーが出てしまう - msgEn:TODO */
	public static final String MSG_P_0000192 = "MSG_P_0000192";
	/** msgId:MSG_P_0000193 - msgZh:数据库导入 - msgJp:データベースに導入方式 - msgEn:TODO */
	public static final String MSG_P_0000193 = "MSG_P_0000193";
	/** msgId:MSG_P_0000194 - msgZh:执行方式 - msgJp:執行方式 - msgEn:TODO */
	public static final String MSG_P_0000194 = "MSG_P_0000194";
	/** msgId:MSG_P_0000195 - msgZh:校验即将导入数据库文件的正确性,不执行数据库操作 - msgJp:チェックだけで、データベースに導入しません。 - msgEn:TODO */
	public static final String MSG_P_0000195 = "MSG_P_0000195";
	/** msgId:MSG_P_0000196 - msgZh:校验即将导入数据库文件的正确性,如果校验正确，执行数据库操作 - msgJp:正確にチェックした後で、データベースに導入します。 - msgEn:TODO */
	public static final String MSG_P_0000196 = "MSG_P_0000196";
	/** msgId:MSG_P_0000197 - msgZh:不通过校验直接执行数据库操作 - msgJp:チェックしないで、直接にデータベースに導入します。 - msgEn:TODO */
	public static final String MSG_P_0000197 = "MSG_P_0000197";
	/** msgId:MSG_P_0000198 - msgZh:先删除再插入 - msgJp:削除後で、挿入します - msgEn:TODO */
	public static final String MSG_P_0000198 = "MSG_P_0000198";
	/** msgId:MSG_P_0000199 - msgZh:先更新再插入 - msgJp:更新データゼロ件場合、挿入します - msgEn:TODO */
	public static final String MSG_P_0000199 = "MSG_P_0000199";
	/** msgId:MSG_P_0000200 - msgZh:校验 - msgJp:チェック - msgEn:check */
	public static final String MSG_P_0000200 = "MSG_P_0000200";
	/** msgId:MSG_P_0000201 - msgZh:校验执行 - msgJp:チェック執行 - msgEn:checkcommit */
	public static final String MSG_P_0000201 = "MSG_P_0000201";
	/** msgId:MSG_P_0000202 - msgZh:执行 - msgJp:執行 - msgEn:commit */
	public static final String MSG_P_0000202 = "MSG_P_0000202";
	/** msgId:MSG_P_0000203 - msgZh:进行业务逻辑验证 - msgJp:業務ロジックチェック執行 - msgEn:TODO */
	public static final String MSG_P_0000203 = "MSG_P_0000203";
	/** msgId:MSG_P_0000204 - msgZh:包含逻辑验证 - msgJp:ロジックチェック付き - msgEn:TODO */
	public static final String MSG_P_0000204 = "MSG_P_0000204";
	/** msgId:MSG_P_0000205 - msgZh:如果本地的OUTLOOK没有配置的话，请直接将 - msgJp:outlookを利用できない場合、 - msgEn:if outlook is not useable,copy the  */
	public static final String MSG_P_0000205 = "MSG_P_0000205";
	/** msgId:MSG_P_0000206 - msgZh:我的银行帐户 - msgJp:私の銀行番号は - msgEn:my BankCard number is : */
	public static final String MSG_P_0000206 = "MSG_P_0000206";
	/** msgId:MSG_P_0000207 - msgZh:  钱已经汇到 - msgJp:   使用料を支払いしました - msgEn:   the money is payed to */
	public static final String MSG_P_0000207 = "MSG_P_0000207";
	/** msgId:MSG_P_0000208 - msgZh:机器码 - msgJp:パソコン情報 - msgEn:sn info */
	public static final String MSG_P_0000208 = "MSG_P_0000208";
	/** msgId:MSG_P_0000209 - msgZh:请发送邮件给 - msgJp:メール - msgEn:mailto: */
	public static final String MSG_P_0000209 = "MSG_P_0000209";
	/** msgId:MSG_P_0000210 - msgZh:是您的银行帐号 - msgJp:はお客様の銀行番号 - msgEn: is your BankCard number */
	public static final String MSG_P_0000210 = "MSG_P_0000210";
	/** msgId:MSG_P_0000211 - msgZh:我想买 - msgJp:このツールを利用したいので、 - msgEn:I want to buy  */
	public static final String MSG_P_0000211 = "MSG_P_0000211";
	/** msgId:MSG_P_0000212 - msgZh:发送给作者。 - msgJp:をコピーし、作者にメールします。 - msgEn: and mail to author please~ */
	public static final String MSG_P_0000212 = "MSG_P_0000212";
	/** msgId:MSG_P_0000213 - msgZh:中国内地银行 - msgJp:中国の銀行 - msgEn:chinese bank */
	public static final String MSG_P_0000213 = "MSG_P_0000213";
	/** msgId:MSG_P_0000214 - msgZh:日本内地银行 - msgJp:日本の銀行 - msgEn:japan bank */
	public static final String MSG_P_0000214 = "MSG_P_0000214";
	/** msgId:MSG_P_0000215 - msgZh:一个月 (10元) - msgJp:一ヶ月 (10RMB) - msgEn:1 month (10RMB) */
	public static final String MSG_P_0000215 = "MSG_P_0000215";
	/** msgId:MSG_P_0000216 - msgZh:三个月 (20元) - msgJp:三ヶ月 (20RMB) - msgEn:3 months (20RMB) */
	public static final String MSG_P_0000216 = "MSG_P_0000216";
	/** msgId:MSG_P_0000217 - msgZh:一年 (60元) - msgJp:一年 (60RMB) - msgEn:1 year (60RMB) */
	public static final String MSG_P_0000217 = "MSG_P_0000217";
	/** msgId:MSG_P_0000218 - msgZh:永久 (100元) - msgJp:永遠(100RMB) - msgEn:100 years(100RMB) */
	public static final String MSG_P_0000218 = "MSG_P_0000218";
	/** msgId:MSG_P_0000219 - msgZh:一个月 (200日元) - msgJp:一ヶ月 (200円) - msgEn:1 month (200円) */
	public static final String MSG_P_0000219 = "MSG_P_0000219";
	/** msgId:MSG_P_0000220 - msgZh:三个月 (400日元) - msgJp:三ヶ月 (400円) - msgEn:3 months (400円) */
	public static final String MSG_P_0000220 = "MSG_P_0000220";
	/** msgId:MSG_P_0000221 - msgZh:一年 1200日元) - msgJp:一年 (1200円) - msgEn:1 year (1200円) */
	public static final String MSG_P_0000221 = "MSG_P_0000221";
	/** msgId:MSG_P_0000222 - msgZh:永久 (2000日元) - msgJp:永遠 (2000円) - msgEn:100 years(2000円) */
	public static final String MSG_P_0000222 = "MSG_P_0000222";
	/** msgId:MSG_P_0000223 - msgZh:please enter the registration key below. - msgJp:please enter the registration key below. - msgEn:please enter the registration key below. */
	public static final String MSG_P_0000223 = "MSG_P_0000223";
	/** msgId:MSG_P_0000224 - msgZh:CHK - msgJp:CHK - msgEn:CHK */
	public static final String MSG_P_0000224 = "MSG_P_0000224";
	/** msgId:MSG_P_0000225 - msgZh:由于是根据数据库进行表导出，所以要选择导出所有表，或是选择单表进行导出处理。 - msgJp:すべてテーブルを導出またはシングルテーブルを選択しないので、導出できませんでした。 - msgEn:TODO */
	public static final String MSG_P_0000225 = "MSG_P_0000225";
	/** msgId:MSG_P_0000226 - msgZh:拷贝记录(含头字段信息)(CTRL + SHIFT + C) - msgJp:レコードをコピー(ヘッダー付き)(CTRL + SHIFT + C) - msgEn:TODO */
	public static final String MSG_P_0000226 = "MSG_P_0000226";
	/** msgId:MSG_P_0000227 - msgZh:拷贝记录(CTRL + C) - msgJp:レコードをコピー(CTRL + C) - msgEn:TODO */
	public static final String MSG_P_0000227 = "MSG_P_0000227";
	/** msgId:MSG_P_0000228 - msgZh:逻辑删除选中记录(CTRL + D) - msgJp:レコードを仮削除(CTRL + D) - msgEn:TODO */
	public static final String MSG_P_0000228 = "MSG_P_0000228";
	/** msgId:MSG_P_0000229 - msgZh:历史 - msgJp:履歴 - msgEn:HISTORY */
	public static final String MSG_P_0000229 = "MSG_P_0000229";
	/** msgId:MSG_P_0000230 - msgZh:顺位 - msgJp:順位 - msgEn:TODO */
	public static final String MSG_P_0000230 = "MSG_P_0000230";
	/** msgId:MSG_P_0000231 - msgZh:字段英文名 - msgJp:項目英語 - msgEn:TODO */
	public static final String MSG_P_0000231 = "MSG_P_0000231";
	/** msgId:MSG_P_0000232 - msgZh:字段注释 - msgJp:項目コメント - msgEn:TODO */
	public static final String MSG_P_0000232 = "MSG_P_0000232";
	/** msgId:MSG_P_0000233 - msgZh:字段类型 - msgJp:項目類型 - msgEn:TODO */
	public static final String MSG_P_0000233 = "MSG_P_0000233";
	/** msgId:MSG_P_0000234 - msgZh:最后执行时刻 - msgJp:最後執行時刻 - msgEn:TODO */
	public static final String MSG_P_0000234 = "MSG_P_0000234";
	/** msgId:MSG_P_0000235 - msgZh:SQL类别 - msgJp:SQL類別 - msgEn:TODO */
	public static final String MSG_P_0000235 = "MSG_P_0000235";
	/** msgId:MSG_P_0000236 - msgZh:SQL内容 - msgJp:SQL内容 - msgEn:TODO */
	public static final String MSG_P_0000236 = "MSG_P_0000236";
	/** msgId:MSG_P_0000237 - msgZh:    保    存     - msgJp:    保    存     - msgEn:    SAVE     */
	public static final String MSG_P_0000237 = "MSG_P_0000237";
	/** msgId:MSG_P_0000238 - msgZh:选择 - msgJp:選択 - msgEn:SELECT */
	public static final String MSG_P_0000238 = "MSG_P_0000238";
	/** msgId:MSG_P_0000239 - msgZh:设计模板中的表名是唯一的，仅能设计一次。 - msgJp:テンプレートは一回使えるしかないです。 - msgEn:same table name */
	public static final String MSG_P_0000239 = "MSG_P_0000239";
	/** msgId:MSG_P_0000240 - msgZh:追加设计 - msgJp:設計追加 - msgEn:add design */
	public static final String MSG_P_0000240 = "MSG_P_0000240";
	/** msgId:MSG_P_0000241 - msgZh:同期化(表名、字段、校验) - msgJp:同期化(テーブル名、項目、チェック) - msgEn:sync tree(item、check) */
	public static final String MSG_P_0000241 = "MSG_P_0000241";
	/** msgId:MSG_P_0000242 - msgZh:与左侧表的树结构同期化，删除无关表信息 - msgJp:左のツリーと同期化、メモリの中に、関係ないテーブル情報を削除します。 - msgEn:TODO */
	public static final String MSG_P_0000242 = "MSG_P_0000242";
	/** msgId:MSG_P_0000243 - msgZh:导入 - msgJp:導入 - msgEn:TODO */
	public static final String MSG_P_0000243 = "MSG_P_0000243";
	/** msgId:MSG_P_0000244 - msgZh:将主画面入力SHEET的数据进行导入处理，将会在内存中自动为表加上条件。 - msgJp:テンプレートを導入、自動にテーブルに、条件を作ります。 - msgEn:TODO */
	public static final String MSG_P_0000244 = "MSG_P_0000244";
	/** msgId:MSG_P_0000245 - msgZh:单体测试时专用 - msgJp:単体テスト用 - msgEn:unit test */
	public static final String MSG_P_0000245 = "MSG_P_0000245";
	/** msgId:MSG_P_0000246 - msgZh:追加一条表出力数据时的限制条件 - msgJp:一つレコードを追加します。 - msgEn:one record */
	public static final String MSG_P_0000246 = "MSG_P_0000246";
	/** msgId:MSG_P_0000247 - msgZh:设计表条件 - msgJp:テーブル条件の設計 - msgEn:TODO */
	public static final String MSG_P_0000247 = "MSG_P_0000247";
	/** msgId:MSG_P_0000248 - msgZh:交替差分 - msgJp:注释差分 - msgEn:DIFF */
	public static final String MSG_P_0000248 = "MSG_P_0000248";
	/** msgId:MSG_P_0000249 - msgZh:表数据再导出 - msgJp:データ現状を出力 - msgEn:after data */
	public static final String MSG_P_0000249 = "MSG_P_0000249";
	/** msgId:MSG_P_0000250 - msgZh:生成差分文件后，数据库附合条件的数据也会检索出来，供下一个环境来使用。 - msgJp:差分ファイルを作成した時、データの現状でも作成します。 - msgEn:after data */
	public static final String MSG_P_0000250 = "MSG_P_0000250";
	/** msgId:MSG_P_0000251 - msgZh:记录数 - msgJp:レコード件数 - msgEn:TODO */
	public static final String MSG_P_0000251 = "MSG_P_0000251";
	/** msgId:MSG_P_0000252 - msgZh:入力文件的SHEET没有选择，是否继续执行？ - msgJp:選択したファイルのシートを選択しないので、大丈夫ですか？ - msgEn:TODO */
	public static final String MSG_P_0000252 = "MSG_P_0000252";
	/** msgId:MSG_P_0000253 - msgZh:合计(TBL) - msgJp:合計(TBL) - msgEn:SUM */
	public static final String MSG_P_0000253 = "MSG_P_0000253";
	/** msgId:MSG_P_0000254 - msgZh:合计(FILE) - msgJp:合計(FILE) - msgEn:SUM */
	public static final String MSG_P_0000254 = "MSG_P_0000254";
	/** msgId:MSG_P_0000255 - msgZh:合计(DB) - msgJp:合計(DB) - msgEn:SUM */
	public static final String MSG_P_0000255 = "MSG_P_0000255";
	/** msgId:MSG_P_0000256 - msgZh:合计(修改) - msgJp:合計(修正) - msgEn:SUM */
	public static final String MSG_P_0000256 = "MSG_P_0000256";
	/** msgId:MSG_P_0000257 - msgZh:合计(删除) - msgJp:合計(削除) - msgEn:SUM */
	public static final String MSG_P_0000257 = "MSG_P_0000257";
	/** msgId:MSG_P_0000258 - msgZh:合计(新建) - msgJp:合計(新規) - msgEn:SUM */
	public static final String MSG_P_0000258 = "MSG_P_0000258";
	/** msgId:MSG_P_0000259 - msgZh:确认表数据的变化状况 - msgJp:テーブルの状況を統計します - msgEn:TODO */
	public static final String MSG_P_0000259 = "MSG_P_0000259";
	/** msgId:MSG_P_0000260 - msgZh:作者QQ - msgJp:ペンギンQQ - msgEn:QQ */
	public static final String MSG_P_0000260 = "MSG_P_0000260";
	/** msgId:MSG_P_0000261 - msgZh:数据库交流QQ群 - msgJp:ペンギンQQたち - msgEn:QQS */
	public static final String MSG_P_0000261 = "MSG_P_0000261";
	/** msgId:MSG_P_0000262 - msgZh:下载地址 - msgJp:ダウンロード - msgEn:download */
	public static final String MSG_P_0000262 = "MSG_P_0000262";
	/** msgId:MSG_P_0000263 - msgZh:联系作者 - msgJp:作者メール - msgEn:MAIL */
	public static final String MSG_P_0000263 = "MSG_P_0000263";
	/** msgId:MSG_P_0000264 - msgZh:如果是程序的错误，请将下面的错误信息发给作者，谢谢您的支持！ - msgJp:APバグの場合、下記のエラー内容を開発者に送付お願い致します。 - msgEn:TODO */
	public static final String MSG_P_0000264 = "MSG_P_0000264";
	/** msgId:MSG_P_0000265 - msgZh:生成到同一张工作薄 - msgJp:一つSHEETへ生成する。 - msgEn:TODO */
	public static final String MSG_P_0000265 = "MSG_P_0000265";
	/** msgId:MSG_P_0000266 - msgZh:SQL执行历史 - msgJp:SQL実行履歴 - msgEn:TODO */
	public static final String MSG_P_0000266 = "MSG_P_0000266";
	/** msgId:MSG_P_0000267 - msgZh:SQL执行时间 - msgJp:SQL実行時間 - msgEn:TODO */
	public static final String MSG_P_0000267 = "MSG_P_0000267";
	/** msgId:MSG_P_0000268 - msgZh:生成SQL文件 - msgJp:SQLスクリプト生成 - msgEn:TODO */
	public static final String MSG_P_0000268 = "MSG_P_0000268";
	/** msgId:MSG_P_0000269 - msgZh:激活快捷键 - msgJp:アクティブキー - msgEn:active key */
	public static final String MSG_P_0000269 = "MSG_P_0000269";
	/** msgId:MSG_P_0000270 - msgZh:全选 - msgJp:全選択 - msgEn:select all */
	public static final String MSG_P_0000270 = "MSG_P_0000270";
	/** msgId:MSG_P_0000271 - msgZh:查看 - msgJp:検索 - msgEn:view */
	public static final String MSG_P_0000271 = "MSG_P_0000271";
	/** msgId:MSG_P_0000272 - msgZh:查看表中的内容 - msgJp:テーブルのレコードを取得 - msgEn:view record */
	public static final String MSG_P_0000272 = "MSG_P_0000272";
	/** msgId:MSG_P_0000273 - msgZh:全选择 - msgJp:全選択 - msgEn:sel all */
	public static final String MSG_P_0000273 = "MSG_P_0000273";
	/** msgId:MSG_P_0000274 - msgZh:整形 - msgJp:整形 - msgEn:beauti */
	public static final String MSG_P_0000274 = "MSG_P_0000274";
	/** msgId:MSG_P_0000275 - msgZh:全展开 - msgJp:全展開 - msgEn:expand */
	public static final String MSG_P_0000275 = "MSG_P_0000275";
	/** msgId:MSG_P_0000276 - msgZh:仅显差异 - msgJp:差異のみ - msgEn:only diff */
	public static final String MSG_P_0000276 = "MSG_P_0000276";
	/** msgId:MSG_P_0000277 - msgZh:表结构 - msgJp:layout - msgEn:layout */
	public static final String MSG_P_0000277 = "MSG_P_0000277";
	/** msgId:MSG_P_0000278 - msgZh:当前数据库的表结构信息 - msgJp:接続DBのテーブルレイアウト - msgEn:TODO */
	public static final String MSG_P_0000278 = "MSG_P_0000278";
	/** msgId:MSG_P_0000279 - msgZh:表与文件同期 - msgJp:テーブルとファイル同期化 - msgEn:TODO */
	public static final String MSG_P_0000279 = "MSG_P_0000279";
	/** msgId:MSG_P_0000280 - msgZh:根据文件生成表和数据 - msgJp:ファイルにより、テーブルとデータを自動生成 - msgEn:TODO */
	public static final String MSG_P_0000280 = "MSG_P_0000280";
	/** msgId:MSG_P_0000281 - msgZh:字段英文名 - msgJp:項目英語名 - msgEn:TODO */
	public static final String MSG_P_0000281 = "MSG_P_0000281";
	/** msgId:MSG_P_0000282 - msgZh:行数 - msgJp:行数 - msgEn:TODO */
	public static final String MSG_P_0000282 = "MSG_P_0000282";
	/** msgId:MSG_P_0000283 - msgZh:列数起始位置 - msgJp:列数開始位置 - msgEn:TODO */
	public static final String MSG_P_0000283 = "MSG_P_0000283";
	/** msgId:MSG_P_0000284 - msgZh:列数终了位置 - msgJp:列数終了位置 - msgEn:TODO */
	public static final String MSG_P_0000284 = "MSG_P_0000284";
	/** msgId:MSG_P_0000285 - msgZh:列数集合 - msgJp:列数集合 - msgEn:TODO */
	public static final String MSG_P_0000285 = "MSG_P_0000285";
	/** msgId:MSG_P_0000286 - msgZh:分割符 - msgJp:分割文字列 - msgEn:TODO */
	public static final String MSG_P_0000286 = "MSG_P_0000286";
	/** msgId:MSG_P_0000287 - msgZh:天后，不可使用。 - msgJp:日後、利用できません。 - msgEn:TODO */
	public static final String MSG_P_0000287 = "MSG_P_0000287";
	/** msgId:MSG_P_0000288 - msgZh:根据选中表数据XLS，自动生成每一字段的最大值和最小值 - msgJp:データXLSファイルにより、項目の最大値と最小値を自動生成します。 - msgEn:TODO */
	public static final String MSG_P_0000288 = "MSG_P_0000288";
	/** msgId:MSG_P_0000289 - msgZh:符合该正则表达式的文件作为操作对象 - msgJp:この公式に合うのファイルを対象として処理します。 - msgEn:TODO */
	public static final String MSG_P_0000289 = "MSG_P_0000289";
	/** msgId:MSG_P_0000290 - msgZh:重置表结构 - msgJp:リセットDB - msgEn:TODO */
	public static final String MSG_P_0000290 = "MSG_P_0000290";
	/** msgId:MSG_P_0000291 - msgZh:重新读取数据库表结构。 - msgJp:DB初期状態に戻る。 - msgEn:TODO */
	public static final String MSG_P_0000291 = "MSG_P_0000291";
	/** msgId:MSG_P_0000292 - msgZh:颜色 - msgJp:着色 - msgEn:color */
	public static final String MSG_P_0000292 = "MSG_P_0000292";
	/** msgId:MSG_P_0000293 - msgZh:根据个人喜好，可以修改单元格的颜色，方便打印等操作 - msgJp:セールの色を修正します、印刷など。 - msgEn:TODO */
	public static final String MSG_P_0000293 = "MSG_P_0000293";
	/** msgId:MSG_P_0000294 - msgZh:该字段为数字类型，禁止非数字输入 - msgJp:この項目は数字類型だから、数字以外文字を入力禁止 - msgEn:TODO */
	public static final String MSG_P_0000294 = "MSG_P_0000294";
	/** msgId:MSG_P_0000295 - msgZh:EXCEL导出方式 - msgJp:EXCEL拡張子 - msgEn:TODO */
	public static final String MSG_P_0000295 = "MSG_P_0000295";
	/** msgId:MSG_P_0000296 - msgZh:xls(1997～2003) - msgJp:xls(1997～2003) - msgEn:xls(1997～2003) */
	public static final String MSG_P_0000296 = "MSG_P_0000296";
	/** msgId:MSG_P_0000297 - msgZh:xlsx(2007～2013) - msgJp:xlsx(2007～2013) - msgEn:xlsx(2007～2013) */
	public static final String MSG_P_0000297 = "MSG_P_0000297";
	/** msgId:MSG_P_0000298 - msgZh:数据库导入位置的文本框不是合法表数据文件，或者没有选中工作薄 - msgJp:DB導入ファイルは存在しません。或いはシートを選択しません。 - msgEn:TODO */
	public static final String MSG_P_0000298 = "MSG_P_0000298";
	/** msgId:MSG_P_0000299 - msgZh:文本文件编码格式转换 - msgJp:テキストのエンコードを転換する - msgEn:TODO */
	public static final String MSG_P_0000299 = "MSG_P_0000299";
	/** msgId:MSG_P_0000300 - msgZh:EXCEL格式转换 - msgJp:EXCELの格式を転換する - msgEn:TODO */
	public static final String MSG_P_0000300 = "MSG_P_0000300";
	/** msgId:MSG_P_0000301 - msgZh:定时任务 - msgJp:定時機能 - msgEn:TODO */
	public static final String MSG_P_0000301 = "MSG_P_0000301";
	/** msgId:MSG_P_0000302 - msgZh:数据库导入时间 - msgJp:DB導入時間 - msgEn:TODO */
	public static final String MSG_P_0000302 = "MSG_P_0000302";
	/** msgId:MSG_P_0000303 - msgZh:启动 - msgJp:起動 - msgEn:start */
	public static final String MSG_P_0000303 = "MSG_P_0000303";
	/** msgId:MSG_P_0000304 - msgZh:停止 - msgJp:停止 - msgEn:stop */
	public static final String MSG_P_0000304 = "MSG_P_0000304";
	/** msgId:MSG_P_0000305 - msgZh:数据库导出时间 - msgJp:DB導出時間 - msgEn:TODO */
	public static final String MSG_P_0000305 = "MSG_P_0000305";
	/** msgId:MSG_P_0000306 - msgZh:读入表 - msgJp:TBLS読込 - msgEn:TODO */
	public static final String MSG_P_0000306 = "MSG_P_0000306";
	/** msgId:MSG_P_0000307 - msgZh:[来自文件夹]的文件 ※如果下面的文本框中有文件内容的话，将会把指定的文件转换到输出文件路径中。 - msgJp:[フォルダから]のファイル ※もし内容があったら、指定されたファイルを変換し、出力します。 - msgEn:TODO */
	public static final String MSG_P_0000307 = "MSG_P_0000307";
	/** msgId:MSG_P_0000308 - msgZh:导出到同一个文件中 - msgJp:一つファイルに導入する。 - msgEn:TODO */
	public static final String MSG_P_0000308 = "MSG_P_0000308";
	/** msgId:MSG_P_0000309 - msgZh:只输出变更记录 - msgJp:変更したレコードのみ - msgEn:TODO */
	public static final String MSG_P_0000309 = "MSG_P_0000309";
	/** msgId:MSG_P_0000310 - msgZh:是否采用JDBC批处理模式 - msgJp:JDBC バッチ処理 - msgEn:is jdbc batch */
	public static final String MSG_P_0000310 = "MSG_P_0000310";
	/** msgId:MSG_P_0000311 - msgZh:放大 - msgJp:拡大 - msgEn:TODO */
	public static final String MSG_P_0000311 = "MSG_P_0000311";
	/** msgId:MSG_P_0000312 - msgZh:打印 - msgJp:印刷 - msgEn:print */
	public static final String MSG_P_0000312 = "MSG_P_0000312";
	/** msgId:MSG_P_0000313 - msgZh:表数据定时任务计划 - msgJp:表数据定時スケジュール - msgEn:TODO */
	public static final String MSG_P_0000313 = "MSG_P_0000313";
	/** msgId:MSG_P_0000314 - msgZh:高级任务计划 - msgJp:高級スケジュール - msgEn:TODO */
	public static final String MSG_P_0000314 = "MSG_P_0000314";
	/** msgId:MSG_P_0000315 - msgZh:尊敬的客户： - msgJp:お客様へ - msgEn:TODO */
	public static final String MSG_P_0000315 = "MSG_P_0000315";
	/** msgId:MSG_P_0000316 - msgZh:您好 - msgJp:いつもお世話になっております。 - msgEn:TODO */
	public static final String MSG_P_0000316 = "MSG_P_0000316";
	/** msgId:MSG_P_0000317 - msgZh:已经开始了，正在处理中，请等待处理成功通知邮件。 - msgJp:処理始めます、しばらくお待ちください。 - msgEn:TODO */
	public static final String MSG_P_0000317 = "MSG_P_0000317";
	/** msgId:MSG_P_0000318 - msgZh:已经处理成功。 - msgJp:処理成功しました。 - msgEn:TODO */
	public static final String MSG_P_0000318 = "MSG_P_0000318";
	/** msgId:MSG_P_0000319 - msgZh:处理失败。 - msgJp:処理失敗しました。 - msgEn:TODO */
	public static final String MSG_P_0000319 = "MSG_P_0000319";
	/** msgId:MSG_P_0000320 - msgZh:以上。 - msgJp:以上です、宜しくお願い致します。 - msgEn:TODO */
	public static final String MSG_P_0000320 = "MSG_P_0000320";
	/** msgId:MSG_P_0000321 - msgZh:成功 - msgJp:成功 - msgEn:TODO */
	public static final String MSG_P_0000321 = "MSG_P_0000321";
	/** msgId:MSG_P_0000322 - msgZh:失败 - msgJp:失敗 - msgEn:TODO */
	public static final String MSG_P_0000322 = "MSG_P_0000322";
	/** msgId:MSG_P_0000323 - msgZh:开始通知 - msgJp:開始通知 - msgEn:TODO */
	public static final String MSG_P_0000323 = "MSG_P_0000323";
	/** msgId:MSG_P_0000324 - msgZh:邮件 - msgJp:メール - msgEn:email */
	public static final String MSG_P_0000324 = "MSG_P_0000324";
	/** msgId:MSG_P_0000325 - msgZh:附件 - msgJp:添付 - msgEn:attach */
	public static final String MSG_P_0000325 = "MSG_P_0000325";
	/** msgId:MSG_P_0000326 - msgZh:zip压缩密码 - msgJp:zipパスワード - msgEn:zip password */
	public static final String MSG_P_0000326 = "MSG_P_0000326";
	/** msgId:MSG_P_0000327 - msgZh:如果有多个邮件地址，请多个邮件地址用逗号[,]分隔 - msgJp:複数メールに郵送したい場合、コンマ「,」で切り分けください。 - msgEn:TODO */
	public static final String MSG_P_0000327 = "MSG_P_0000327";
	/** msgId:MSG_P_0000328 - msgZh:选择所有记录(CTRL + A) - msgJp:全レコード選択(CTRL + A) - msgEn:TODO */
	public static final String MSG_P_0000328 = "MSG_P_0000328";
	/** msgId:MSG_P_0000329 - msgZh:界面最大化(CTRL + M) - msgJp:画面最大化(CTRL + M) - msgEn:TODO */
	public static final String MSG_P_0000329 = "MSG_P_0000329";
	/** msgId:MSG_P_0000330 - msgZh:行列转换(CTRL + T) - msgJp:行列変換(CTRL + T) - msgEn:TODO */
	public static final String MSG_P_0000330 = "MSG_P_0000330";
	/** msgId:MSG_P_0000331 - msgZh:全选择(CTRL + A) - msgJp:全選択(CTRL + A) - msgEn:TODO */
	public static final String MSG_P_0000331 = "MSG_P_0000331";
	/** msgId:MSG_P_0000332 - msgZh:整形(CTRL + B) - msgJp:整形(CTRL + B) - msgEn:TODO */
	public static final String MSG_P_0000332 = "MSG_P_0000332";
	/** msgId:MSG_P_0000333 - msgZh:复制(CTRL + C) - msgJp:コピー(CTRL + C) - msgEn:TODO */
	public static final String MSG_P_0000333 = "MSG_P_0000333";
	/** msgId:MSG_P_0000334 - msgZh:粘贴(CTRL + V) - msgJp:貼り付け(CTRL + V) - msgEn:TODO */
	public static final String MSG_P_0000334 = "MSG_P_0000334";
	/** msgId:MSG_P_0000335 - msgZh:剪切(CTRL + X) - msgJp:切り取り(CTRL + X) - msgEn:TODO */
	public static final String MSG_P_0000335 = "MSG_P_0000335";
	/** msgId:MSG_P_0000336 - msgZh:删除(DELETE) - msgJp:删除(DELETE) - msgEn:TODO */
	public static final String MSG_P_0000336 = "MSG_P_0000336";
	/** msgId:MSG_P_0000337 - msgZh:联想表名(CTRL + T) - msgJp:テーブル名の一覧(CTRL + T) - msgEn:TODO */
	public static final String MSG_P_0000337 = "MSG_P_0000337";
	/** msgId:MSG_P_0000338 - msgZh:根据表名联想列名(CTRL + L) - msgJp:テーブル名により項目を表示(CTRL + L) - msgEn:TODO */
	public static final String MSG_P_0000338 = "MSG_P_0000338";
	/** msgId:MSG_P_0000339 - msgZh:根据列名联想表名(CTRL + SHIFT + T) - msgJp:項目によりテーブル名を表示(CTRL + SHIFT + T) - msgEn:TODO */
	public static final String MSG_P_0000339 = "MSG_P_0000339";
	/** msgId:MSG_P_0000340 - msgZh:联想SQL关键字(CTRL + SHIFT + K) - msgJp:SQLキー(CTRL + SHIFT + K) - msgEn:TODO */
	public static final String MSG_P_0000340 = "MSG_P_0000340";
	/** msgId:MSG_P_0000341 - msgZh:联想SQL函数(CTRL + SHIFT + F) - msgJp:SQL関数(CTRL + SHIFT + F) - msgEn:TODO */
	public static final String MSG_P_0000341 = "MSG_P_0000341";
	/** msgId:MSG_P_0000342 - msgZh:执行SQL(CTRL + R/F8) - msgJp:SQLを行う(CTRL + R/F8) - msgEn:TODO */
	public static final String MSG_P_0000342 = "MSG_P_0000342";
	/** msgId:MSG_P_0000343 - msgZh:界面最大化(CTRL + N) - msgJp:画面最大化(CTRL + N) - msgEn:TODO */
	public static final String MSG_P_0000343 = "MSG_P_0000343";
	/** msgId:MSG_P_0000344 - msgZh:SQL加注释(CTRL + SHIFT + C) - msgJp:SQLをコメントに(CTRL + SHIFT + C) - msgEn:TODO */
	public static final String MSG_P_0000344 = "MSG_P_0000344";
	/** msgId:MSG_P_0000345 - msgZh:字母统一成小写(CTRL + SHIFT + Y) - msgJp:小文字列へ(CTRL + SHIFT + Y) - msgEn:TODO */
	public static final String MSG_P_0000345 = "MSG_P_0000345";
	/** msgId:MSG_P_0000346 - msgZh:字母统一成大写(CTRL + SHIFT + X) - msgJp:大文字列へ(CTRL + SHIFT + X) - msgEn:TODO */
	public static final String MSG_P_0000346 = "MSG_P_0000346";
	/** msgId:MSG_P_0000347 - msgZh:查看SQL历史(CTRL + H) - msgJp:SQLの履歴(CTRL + H) - msgEn:TODO */
	public static final String MSG_P_0000347 = "MSG_P_0000347";
	/** msgId:MSG_P_0000348 - msgZh:打印SQL(CTRL + P) - msgJp:SQLを印刷(CTRL + P) - msgEn:TODO */
	public static final String MSG_P_0000348 = "MSG_P_0000348";
	/** msgId:MSG_P_0000349 - msgZh:新建 - msgJp:新規 - msgEn:TODO */
	public static final String MSG_P_0000349 = "MSG_P_0000349";
	/** msgId:MSG_P_0000350 - msgZh:打开 - msgJp:開く - msgEn:TODO */
	public static final String MSG_P_0000350 = "MSG_P_0000350";
	/** msgId:MSG_P_0000351 - msgZh:保存 - msgJp:保存 - msgEn:TODO */
	public static final String MSG_P_0000351 = "MSG_P_0000351";
	/** msgId:MSG_P_0000352 - msgZh:另存为 - msgJp:名前付 - msgEn:TODO */
	public static final String MSG_P_0000352 = "MSG_P_0000352";
	/** msgId:MSG_P_0000353 - msgZh:设置配置文件名 - msgJp:配置ファイルの名称を設定します。 - msgEn:TODO */
	public static final String MSG_P_0000353 = "MSG_P_0000353";
	/** msgId:MSG_P_0000354 - msgZh:修正 - msgJp:修正 - msgEn:EDIT */
	public static final String MSG_P_0000354 = "MSG_P_0000354";
	/** msgId:MSG_P_0000355 - msgZh:保存当前配置文件吗？ - msgJp:画面設定された内容を保存しますか？ - msgEn:TODO */
	public static final String MSG_P_0000355 = "MSG_P_0000355";
	/** msgId:MSG_P_0000356 - msgZh:打开配置文件 - msgJp:配置ファイルを開く - msgEn:TODO */
	public static final String MSG_P_0000356 = "MSG_P_0000356";
	/** msgId:MSG_P_0000357 - msgZh:配置文件 - msgJp:配置ファイル - msgEn:TODO */
	public static final String MSG_P_0000357 = "MSG_P_0000357";
	/** msgId:MSG_P_0000358 - msgZh:文件复制 - msgJp:ファイルコピー - msgEn:TODO */
	public static final String MSG_P_0000358 = "MSG_P_0000358";
	/** msgId:MSG_P_0000359 - msgZh:数据库导入 - msgJp:DB導入 - msgEn:TODO */
	public static final String MSG_P_0000359 = "MSG_P_0000359";
	/** msgId:MSG_P_0000360 - msgZh:数据库导出 - msgJp:DB導出 - msgEn:TODO */
	public static final String MSG_P_0000360 = "MSG_P_0000360";
	/** msgId:MSG_P_0000361 - msgZh:表数据比较 - msgJp:テーブルのデータを比較 - msgEn:TODO */
	public static final String MSG_P_0000361 = "MSG_P_0000361";
	/** msgId:MSG_P_0000362 - msgZh:表信息（表结构导出） - msgJp:テーブル情報（結構導出） - msgEn:TODO */
	public static final String MSG_P_0000362 = "MSG_P_0000362";
	/** msgId:MSG_P_0000363 - msgZh:表信息（数据库迁移） - msgJp:テーブル情報（DB移行） - msgEn:TODO */
	public static final String MSG_P_0000363 = "MSG_P_0000363";
	/** msgId:MSG_P_0000364 - msgZh:表信息（建表SQL导出） - msgJp:テーブル情報（TBLのSQL導出） - msgEn:TODO */
	public static final String MSG_P_0000364 = "MSG_P_0000364";
	/** msgId:MSG_P_0000365 - msgZh:制作表 - msgJp:テーブル設計 - msgEn:TODO */
	public static final String MSG_P_0000365 = "MSG_P_0000365";
	/** msgId:MSG_P_0000366 - msgZh:执行方式 - msgJp:実行方式 - msgEn:TODO */
	public static final String MSG_P_0000366 = "MSG_P_0000366";
	/** msgId:MSG_P_0000367 - msgZh:成果物路径 - msgJp:成果物のパス - msgEn:TODO */
	public static final String MSG_P_0000367 = "MSG_P_0000367";
	/** msgId:MSG_P_0000368 - msgZh:来自文件夹 - msgJp:フォルダー元 - msgEn:TODO */
	public static final String MSG_P_0000368 = "MSG_P_0000368";
	/** msgId:MSG_P_0000369 - msgZh:来自文件 - msgJp:ファイル元 - msgEn:TODO */
	public static final String MSG_P_0000369 = "MSG_P_0000369";
	/** msgId:MSG_P_0000370 - msgZh:目的文件夹 - msgJp:フォルダー先 - msgEn:TODO */
	public static final String MSG_P_0000370 = "MSG_P_0000370";
	/** msgId:MSG_P_0000371 - msgZh:目的文件 - msgJp:ファイル先 - msgEn:TODO */
	public static final String MSG_P_0000371 = "MSG_P_0000371";
	/** msgId:MSG_P_0000372 - msgZh:数据位置 - msgJp:データ場所 - msgEn:TODO */
	public static final String MSG_P_0000372 = "MSG_P_0000372";
	/** msgId:MSG_P_0000373 - msgZh:工作薄 - msgJp:シート - msgEn:TODO */
	public static final String MSG_P_0000373 = "MSG_P_0000373";
	/** msgId:MSG_P_0000374 - msgZh:表名 - msgJp:テーブル名 - msgEn:TODO */
	public static final String MSG_P_0000374 = "MSG_P_0000374";
	/** msgId:MSG_P_0000375 - msgZh:包含 - msgJp:含む - msgEn:TODO */
	public static final String MSG_P_0000375 = "MSG_P_0000375";
	/** msgId:MSG_P_0000376 - msgZh:导出方式 - msgJp:導出方式 - msgEn:TODO */
	public static final String MSG_P_0000376 = "MSG_P_0000376";
	/** msgId:MSG_P_0000377 - msgZh:配置路径 - msgJp:配置パス - msgEn:TODO */
	public static final String MSG_P_0000377 = "MSG_P_0000377";
	/** msgId:MSG_P_0000378 - msgZh:数据差分方式 - msgJp:データ差分方式 - msgEn:TODO */
	public static final String MSG_P_0000378 = "MSG_P_0000378";
	/** msgId:MSG_P_0000379 - msgZh:同期化 - msgJp:同期化 - msgEn:TODO */
	public static final String MSG_P_0000379 = "MSG_P_0000379";
	/** msgId:MSG_P_0000380 - msgZh:表数据再导出 - msgJp:データ再導出 - msgEn:TODO */
	public static final String MSG_P_0000380 = "MSG_P_0000380";
	/** msgId:MSG_P_0000381 - msgZh:只输出变更记录 - msgJp:変更データだけ - msgEn:TODO */
	public static final String MSG_P_0000381 = "MSG_P_0000381";
	/** msgId:MSG_P_0000382 - msgZh:配置位置（表结构导出） - msgJp:配置場所（テーブル導出） - msgEn:TODO */
	public static final String MSG_P_0000382 = "MSG_P_0000382";
	/** msgId:MSG_P_0000383 - msgZh:比较可否（表结构导出） - msgJp:比較可否（テーブル導出） - msgEn:TODO */
	public static final String MSG_P_0000383 = "MSG_P_0000383";
	/** msgId:MSG_P_0000384 - msgZh:比较路径（表结构导出） - msgJp:比較パス（テーブル導出） - msgEn:TODO */
	public static final String MSG_P_0000384 = "MSG_P_0000384";
	/** msgId:MSG_P_0000385 - msgZh:转换模式（数据库迁移） - msgJp:転換モード（テーブル導出） - msgEn:TODO */
	public static final String MSG_P_0000385 = "MSG_P_0000385";
	/** msgId:MSG_P_0000386 - msgZh:选择建表根据方式 - msgJp:テーブル作成方式 - msgEn:TODO */
	public static final String MSG_P_0000386 = "MSG_P_0000386";
	/** msgId:MSG_P_0000387 - msgZh:来源路径 - msgJp:場所元 - msgEn:TODO */
	public static final String MSG_P_0000387 = "MSG_P_0000387";
	/** msgId:MSG_P_0000388 - msgZh:模板名称 - msgJp:テンプレート名 - msgEn:TODO */
	public static final String MSG_P_0000388 = "MSG_P_0000388";
	/** msgId:MSG_P_0000389 - msgZh:文件来源 - msgJp:ファイル元 - msgEn:TODO */
	public static final String MSG_P_0000389 = "MSG_P_0000389";
	/** msgId:MSG_P_0000390 - msgZh:表名称 - msgJp:テーブル名 - msgEn:TODO */
	public static final String MSG_P_0000390 = "MSG_P_0000390";
	/** msgId:MSG_P_0000391 - msgZh:制作方式 - msgJp:制作方式 - msgEn:TODO */
	public static final String MSG_P_0000391 = "MSG_P_0000391";
	/** msgId:MSG_P_0000392 - msgZh:执行文件 - msgJp:執行ファイル - msgEn:TODO */
	public static final String MSG_P_0000392 = "MSG_P_0000392";
	/** msgId:MSG_P_0000393 - msgZh:VIEW - msgJp:VIEW - msgEn:VIEW */
	public static final String MSG_P_0000393 = "MSG_P_0000393";
	/** msgId:MSG_P_0000394 - msgZh:请确认所要执行的任务计划 - msgJp:TODO - msgEn:TODO */
	public static final String MSG_P_0000394 = "MSG_P_0000394";
	/** msgId:MSG_P_0000395 - msgZh:重新执行检索(CTRL + R/F8) - msgJp:検索処理を行う(CTRL + R/F8) - msgEn:TODO */
	public static final String MSG_P_0000395 = "MSG_P_0000395";
	/** msgId:MSG_P_0000396 - msgZh:显示字段备考(CTRL + J) - msgJp:論理名付きで表示(CTRL + J) - msgEn:TODO */
	public static final String MSG_P_0000396 = "MSG_P_0000396";
	/** msgId:MSG_P_0000397 - msgZh:还没有进行表设计操作，确定要将所有表中的数据导出吗？ - msgJp:テーブル設計しないで、すべてテーブルを導出しますか？ - msgEn:TODO */
	public static final String MSG_P_0000397 = "MSG_P_0000397";
	/** msgId:MSG_P_0000398 - msgZh:继续导出的话，可能会花很长时间 - msgJp:長い時間をかかる可能性があります。 - msgEn:TODO */
	public static final String MSG_P_0000398 = "MSG_P_0000398";
	/** msgId:MSG_P_0000399 - msgZh:非检索SQL语句执行时，SQL文依次执行，中间不提示 - msgJp:検索SQL文ではない場合、SQL文を実行続けて行く - msgEn:TODO */
	public static final String MSG_P_0000399 = "MSG_P_0000399";
	/** msgId:MSG_P_0000400 - msgZh:执行方式 - msgJp:実行方式 - msgEn:TODO */
	public static final String MSG_P_0000400 = "MSG_P_0000400";
	/** msgId:MSG_P_0000401 - msgZh:指定间隔时间 - msgJp:間隔時間を指定 - msgEn:TODO */
	public static final String MSG_P_0000401 = "MSG_P_0000401";
	/** msgId:MSG_P_0000402 - msgZh:间隔(秒) - msgJp:間隔(秒) - msgEn:TODO */
	public static final String MSG_P_0000402 = "MSG_P_0000402";
	/** msgId:MSG_P_0000403 - msgZh:执行次数 - msgJp:執行回数 - msgEn:TODO */
	public static final String MSG_P_0000403 = "MSG_P_0000403";
	/** msgId:MSG_P_0000404 - msgZh:指定开始时间 - msgJp:開始時間 - msgEn:TODO */
	public static final String MSG_P_0000404 = "MSG_P_0000404";
	/** msgId:MSG_P_0000405 - msgZh:自定义多个开始时间 - msgJp:開始時間を設定 - msgEn:TODO */
	public static final String MSG_P_0000405 = "MSG_P_0000405";
	/** msgId:MSG_P_0000406 - msgZh:注重间隔(秒) - msgJp:間隔(秒)優先 - msgEn:TODO */
	public static final String MSG_P_0000406 = "MSG_P_0000406";
	/** msgId:MSG_P_0000407 - msgZh:注重执行次数 - msgJp:回数優先 - msgEn:TODO */
	public static final String MSG_P_0000407 = "MSG_P_0000407";
	/** msgId:MSG_P_0000408 - msgZh:没有自定义的任务计划 - msgJp:スケジュールなし - msgEn:TODO */
	public static final String MSG_P_0000408 = "MSG_P_0000408";
	/** msgId:MSG_P_0000409 - msgZh:单表检索 - msgJp:ひとつテーブル検索 - msgEn:TODO */
	public static final String MSG_P_0000409 = "MSG_P_0000409";
	/** msgId:MSG_P_0000410 - msgZh:定时高级任务计划 - msgJp:定時高級スケジュール - msgEn:TODO */
	public static final String MSG_P_0000410 = "MSG_P_0000410";
	/** msgId:MSG_P_0000411 - msgZh:导出txt - msgJp:導出txt - msgEn:TODO */
	public static final String MSG_P_0000411 = "MSG_P_0000411";
	/** msgId:MSG_P_0000412 - msgZh:读入 - msgJp:読込 - msgEn:TODO */
	public static final String MSG_P_0000412 = "MSG_P_0000412";
	/** msgId:MSG_P_0000413 - msgZh:重新读入EXCEL样式信息 - msgJp:EXCEL様式を読込 - msgEn:TODO */
	public static final String MSG_P_0000413 = "MSG_P_0000413";
	/** msgId:MSG_P_0000414 - msgZh:EXCEL样式信息读入完成 - msgJp:EXCEL様式を読込した - msgEn:TODO */
	public static final String MSG_P_0000414 = "MSG_P_0000414";
	/** msgId:MSG_P_0000415 - msgZh:公司官网 - msgJp:会社サイト - msgEn:company */
	public static final String MSG_P_0000415 = "MSG_P_0000415";
	/** msgId:MSG_P_0000416 - msgZh:■ver 20160716
	1.在SQL编辑画面中，增加表的树结构和SQL文窗口的拖拽功能。
	2.在SQL编辑画面中，可以生成单表的增删改查SQL语句。

	■ver 20160711 
	修正导出记录为0件的记录进度条显示问题 

	■ver 20160704 
	POSTGRE中，更改了检索主键的SQL语句 

	■ver 20160701 
	1.为了解决辽宁省电梯数据库中心的脏数据对应问题，增加了下面的功能。 
	如果入力文件的字段内容是空，则跳过，不组成UPDATE的SET语句。(在配置画面内可设置) 
	2.POSTGRE数据库导出文本时的错误修正。

	■ver 20160630 
	修改POSTGRE连接数据库取得时间不正确问题。 
	增加导出DISTICT功能（MYSQL，ORACLE,POSTGRE测试完了） 

	■ver 20160629 
	工具生成的文件路径修正。统一放到数据库类型文件夹下面。 

	■ver 20160625 
	SQL检索结果0件时，行转列BUG对应 

	■ver 20160624 
	在单表检索时，可以生成增删改查SQL文。 

	■ver 20160621
	生成表结构时，增加了对表注释里面所包含特殊字符的过滤处理。

	■ver 20160615 
	修正SQL着色功能 

	■ver 20160613 
	在SQL窗口，增加了是否询问继续执行选项。

	■ver 20160608 
	删除鼠标略过特效 

	■ver 20160607 
	主画面也可以直接激活SQL历史画面。 
	SQL历史画面增加左上角全选功能。

	■ver 20160606
	右键菜单图标对应 

	■ver 20160530 
	图标对应 

	■ver 20160527 
	tab顺序对应 

	■ver 20160519
	根据沈阳CNC公司的建议，画面进行简单化对应，画面部分30%发生变更。删除了不必要的控件，同时增强了画面制御。

	■ver 20160423
	1. 表设计画面增加SQL预览功能。 

	■ver 20160419
	1.增加通知图标右键弹出版本历史说明对话框。 

	■ver 20160418
	1.重新读取表结构时，表的树结构不能重新显示问题。 

	■ver 20160417 
	1.增强导出Excel文件样式的自定义功能。 

	■ver 20160414 
	1.增加检索结果画面记录的行数提示。 

	■ver 20160410 
	1.监视多用户操作的INSERT失败BUG修正。 

	■ver 20160404 
	1.默认配置了网络数据库 (oracle,postgre) 

	■ver 20160013 
	1.数据迁移画面修正 

	■ver 20160308 
	1.更改简单任务时画面显示问题 

	■ver 20160220 
	1.画面显示问题 
	2.改进自带提供的任务计划 

	■ver 20160121 
	1.解决导出EXCEL文件时，文件名过长BUG 

	■ver 20151216 
	1.增加MYSQL的自增序列的导出SQL功能 
	2.修正对应ORACLE自增序列时的BUG。 

	■ver 20151212 
	1.增加导出自增SEQ序列功能,对应ORACLE。※下个版本将实现MYSQL的自增序列 
	2.更改监视LOG的字段长度。 

	■ver 20151127 
	1.解决数据库连接失败时，就不可以再次执行SQL语句的BUG。 
	2.增加连接数据库时，访问数据库编码设置（仅更新对应了MYSQL数据库） 
	感谢MarLon提出的BUG。 

	■ver 20151124 
	1.文本差分BUG修正。 
	2. 搬家到 https://sourceforge.net/projects/penguinsdbtools/

	■ver 20151104 
	1.文本差分BUG修正。 

	■ver 20151020 
	1.性能提高。 

	■ver 201508015 
	1.将表结构生成到一个EXCEL里 

	■ver 20150801 
	1.更新最新的POI 
	2.修正个别xls,xlsx兼容的BUG 

	■ver 20150524 
	1.更新最新的POI 
	2.更新到JAVA8 

	■ver 20150517 
	1.修正JVM溢出时的画面BUG。 

	■ver 20150513 
	1.修正文本文件字段为空的数据导入数据库时异常。 

	■ver 20150510 
	1.增加文本文件和数据库的差分功能。 

	■ver 20150415 
	1.增加从数据库和文本文件同期化功能。 


	■ver 20150323 
	1.增强定时任务功能 可自定义多个时间。 

	■ver 20150313 
	1.增强定时任务功能 
	2.增加当前SQL结果窗口再检索功能 

	■ver 20150223 
	1.全面支持OFFICE系列EXCEL版本 
	2.增加SQL窗口右键菜单 

	■ver 20150216 
	1.增加MAIL送信功能（简易定时计划功能） 

	■ver 20150201 
	1.程序优化 

	■ver 20150126 
	1.增加SQL着色功能 
	2.SQL对话框的UI、最大化
	3.文本编辑器的制作

	■ver 20150115
	1.之前版本数据插入很慢，10万条记录1分多插入。
	该版本增强数据插入效率，10万条记录10秒插入。

	■ver 20150112
	1.增加数据差分功能，可以在EXCEL中仅显示变更记录。
	2.更改显示EXCEL边线样式。
	3.修正LOG4J级别时，不用重启应用。

	■ver 20141227
	增加定时任务

	■ver 20140703
	修正数值高精度类型，当为0值时，科学计数法表示问题。

	■ver 20140606
	增加了可以指定表进行读入工具内存操作。

	■ver14.05.25
	1.增大JVM到1G
	2.完美支持SYBASE
	3.当选中SHEET对比时，增加是否已经同期化的校验。

	■ver 14.05.05
	修正点：
	1.配置文件优化
	2.解决DB2分页问题
	3.更新名字为【Penguins DbTools】

	■ver 20130825
	修正内容： 性能优化。

	■ver 20130621
	修正内容：
	1.更正■ver 20130619版本中，EXCEL表不能导入到数据库的问题。
	2.错误画面显示问题
	3.UI修正

	■ver 20130619
	修正内容：
	1.SQL条件追加
	2..程序执行前后，数据库表中的状态变化，新增记录可以看到
	3.UI修正

	■ver 20130610
	修正内容：
	1.SQL历史信息
	2.增加复制功能
	3.支持DB2

	■ver 20130604
	修正内容：
	1.选择详细设计书时，没有生成模板。
	2.其它BUG修正。

	■ver 20130522
	1.SQLSERVERBUG修正
	2.连接假死现象修正
	测试环境：
	1.2000多张表，总共5万多列,3秒左右连接上。

	■ver 20130518
	1.表名为关建字时，检索出错。
	2.SQLSERVER导出时，字段备考没有导出

	■ver20130506
	1.根据银行需求，对EXCEL中的数据，进行校验后，导入数据库。
	2.支持del->inser,update->insert

	■ver20130406
	1.实现不同种别的数据库之间的数据移行

	■ver20130120
	1.实现了表结构差分

	■ver20120711
	1.对应MYSQL

	■ver20120401
	1.实现表数据对比

	■ver20120216
	1.对应SQLSERVER2012以后版本

	■ver20120202
	1.对应postgre

	■ver20120119（初版）
	1.实现ORACLE数据库备份，表数据的导入与导出。 - msgJp:■ver 20160716
	1.SQL編集画面で、drag対応した。
	2.在SQL编辑画面中，テーブルにより、insert,delete,update ,selectのSQL文を生成出来。

	■ver 20160711 
	出力進度状態を表す出来ること

	■ver 20160704 
	POSTGREに、テーブルキーの検索SQLを修正した

	■ver 20160701 
	1.中国の遼寧省エレベーターDBセンターのdirtyデータを対応するため、下記機能を追加した。
	入力項目内容がNULL場合、スキップして、UPDATE文のSET項目を対象外にする。(オプション設定可能) 
	2.POSTGREからTXTに導出バッグを修正した。

	■ver 20160630 
	POSTGREで、DBから時間を取得できない件を修正した。 
	導出時、DISTICT機能を追加した。（MYSQL，ORACLE,POSTGRE测试完了） 

	■ver 20160629 
	ツールで出力したファイルのフォルダーを整理した。

	■ver 20160625 
	SQL检索结果0件时，行转列BUG对应。

	"■ver 20160624 
	一つテーブルを検索する時、データ一覧画面で、挿入/削除/更新/検索SQL文を自由に生成出来る。

	■ver 20160621
	テーブルレイアウトを生成する時、ファイル名前に、特別な文字をチェックする。

	■ver 20160615 
	ＳＱＬ着色機能を修正する。 

	■ver 20160613 
	ＳＱＬ画面にて、実行続けるかどうかの問い合わせ用のチェックボックスを追加した。

	■ver 20160608 
	文字特効機能を削除 

	■ver 20160607 
	メイン画面にて、
	「CTRL+S」はSQL画面を呼び出す。
	「CTRL+Ｈ」はSQL履歴画面を呼び出す。
	「CTRL+Ｄ」はテーブルの設計画面を呼び出す。
	SQL歴史画面の左上処に、全選択機能を追加した。

	■ver 20160606
	右ボタンで、メニューアイコンの対応 

	■ver 20160530 
	イメージ対応 

	■ver 20160527 
	tab対応

	■ver 20160519
	瀋陽CNCのアドバイスにより、画面UIはシンプル化に対応する。画面は30%以上変更が発生した。必要ないコントロールを削除し、UI制御機能を追加する。

	■ver 20160423
	1. テーブル設計画面に、SQL検索機能(preview)を追加。

	■ver 20160419
	1.バージョン履歴画面を追加。 

	■ver 20160418
	1.DBにより、TBL定義情報を読んで、TBLSツリーに表示できるように。

	■ver 20160417 
	1.EXCEL様式設定可能

	■ver 20160414 
	1.検索結果画面に、レコード行数を表示できるように

	■ver 20160410 
	1.ツールログ挿入失敗問題の対応。 

	■ver 20160404 
	1.デフォルトウエブデータベースの対応 

	■ver 20160013 
	1.データ移行画面の対応

	■ver 20160308 
	1.簡単スケジュール画面の対応

	■ver 20160220 
	1.画面表示修正
	2.スケジュールを実現する

	■ver 20160121 
	1.EXCELを導出する場合、ファイル名長すぎの対応


	■ver 20151216 
	1.MYSQLのシーケンス対応
	2.ORACLEのシーケンス対応

	■ver 20151212 
	1.シーケンス定義SQLを出力対応
	2.ツールログ項目サイズを修正

	■ver 20151127 
	1.DBに接続失敗場合、SQL実行不可BUG対応。
	2.DBを接続場合、エンコードの設定を追加。
	MarLonからのBUG。ありがとう 

	■ver 20151124 
	1.CVS差分対応。 
	2.DOWNLOAD場所の変更　https://sourceforge.net/projects/penguinsdbtools/

	■ver 20151104 
	1.CVS差分対応。 

	■ver 20151020 
	1.性能アップ。 

	■ver 201508015 
	1.テーブルLAYOUTを一つファイルに出力できる。

	■ver 20150801 
	1.POIバージョンアップ 
	2.XLS,XLSX対応

	■ver 20150524 
	1.POIバージョンアップ 
	2.JAVA7⇒JAVA8 

	■ver 20150517 
	1.JVM OUT MEMORY対応。 

	■ver 20150513 
	1.CSVファイルにて、項目はNULL場合、導入異常対応。 

	■ver 20150510 
	1.CVSファイルとテーブルデータの差分機能を追加。 

	■ver 20150415 
	1.同期化機能の追加。 


	■ver 20150323 
	1.スケジュールの対応。 

	■ver 20150313 
	1.スケジュールを追加
	2.SQL画面で、再検索機能を追加
	￥
	■ver 20150223 
	1.EXCEL1997～2013対応
	2.SQL画面で、右キーを追加

	■ver 20150216 
	1.メール送信を追加

	■ver 20150201 
	1.バーグ修正

	■ver 20150126 
	1.SQL画面に、色付きの対応 
	2.SQL画面UI対応、画面の最大化対応
	3.SQLテキスト対応

	■ver 20150115
	1.修正前,10萬件レコードを挿入すると、１分かかる。
	　修正後、10萬件レコード、10秒かかる。

	■ver 20150112
	1.データ差分の対応、変更だけのレコードを表示する。
	2.EXCELの样式対応。
	3.LOG4Jのレーベルを修正すると、APP再起動することが必要ない。

	■ver 20141227
	スケジュールを追加

	■ver 20140703
	数字の場合、表示エラー対応

	■ver 20140606
	指定テーブルをツールメモリに読込

	■ver14.05.25
	2.SYBASE対応
	3.データ差分する字、同期化チェックを追加

	■ver 14.05.05
	修正点：
	1.コンフィグファイルの対応
	2.DB2導出問題対応
	3.正式名前の変更【Penguins DbTools】

	■ver 20130825
	修正内容： 性能アップ。

	■ver 20130621
	修正内容：
	1.ver 20130619にて，EXCELファイルをDBに導入できない障害対応。
	2.エラー画面表示対応
	3.UI修正

	■ver 20130619
	修正内容：
	1.SQL条件追加
	2.データ差分、新規レコードも表示できるように
	3.UI修正

	■ver 20130610
	修正内容：
	1.SQL履歴追加
	2.コピペを追加
	3.DB2対応

	■ver 20130604
	修正内容：
	1.DB設計の出力
	2.BUG修正。

	■ver 20130522
	1.SQLSERVERBUG修正
	1.ORACLEで、2000テーブル，5萬列,接続時間3秒。

	■ver 20130518
	1.表名为关建字时，检索出错。
	2.SQLSERVER导出时，字段备考没有导出

	■ver20130506
	1.チェック、del->inser,update->insert。

	■ver20130406
	1.ORACLE⇒MYSQL

	■ver20130120
	1.テーブルLAYOUT差分

	■ver20120711
	1.MYSQL対応

	■ver20120401
	1.テーブルデータ差分

	■ver20120216
	1.SQLSERVER2012対応

	■ver20120202
	1.postgre対応

	■ver20120119（初版）
	1.ORACLEデータバックアップ，データ導出と導入。 - msgEn:TODO */
	public static final String MSG_P_0000416 = "MSG_P_0000416";
	/** msgId:MSG_P_0000417 - msgZh:表名或表字段不在左侧一览中。 - msgJp:テーブル名または項目名は左側のツリーに存在しない。 - msgEn:TODO */
	public static final String MSG_P_0000417 = "MSG_P_0000417";
	/** msgId:MSG_P_0000418 - msgZh:工作薄导入 - msgJp:SHEET導入 - msgEn:TODO */
	public static final String MSG_P_0000418 = "MSG_P_0000418";
	/** msgId:MSG_P_0000419 - msgZh:字段 - msgJp:項目 - msgEn:COL */
	public static final String MSG_P_0000419 = "MSG_P_0000419";
	/** msgId:MSG_P_0000420 - msgZh:同一个工作薄 - msgJp:一つSHEET - msgEn:ONE SHEET */
	public static final String MSG_P_0000420 = "MSG_P_0000420";
	/** msgId:MSG_P_0000421 - msgZh:数据分割导出 - msgJp:データ分割 - msgEn:TODO */
	public static final String MSG_P_0000421 = "MSG_P_0000421";
	/** msgId:MSG_P_0000422 - msgZh:根据SQL文件执行或导出 - msgJp:SQLファイルで実行／出力 - msgEn:TODO */
	public static final String MSG_P_0000422 = "MSG_P_0000422";
	/** msgId:MSG_P_0000423 - msgZh:按照指定行数，进行数据分割导出。 - msgJp:指定行数分割し、データを導出する。 - msgEn:TODO */
	public static final String MSG_P_0000423 = "MSG_P_0000423";
	/** msgId:MSG_P_0000424 - msgZh:信息 - msgJp:情報 - msgEn:INFO */
	public static final String MSG_P_0000424 = "MSG_P_0000424";
	/** msgId:MSG_P_0000425 - msgZh:column: - msgJp:column: - msgEn:column: */
	public static final String MSG_P_0000425 = "MSG_P_0000425";
	/** msgId:MSG_P_0000426 - msgZh:record: - msgJp:record: - msgEn:record: */
	public static final String MSG_P_0000426 = "MSG_P_0000426";
	/** msgId:MSG_P_0000427 - msgZh:已经应用完成 - msgJp:適用されました - msgEn:TODO */
	public static final String MSG_P_0000427 = "MSG_P_0000427";
	/** msgId:MSG_P_0000428 - msgZh:工作薄导入完成 - msgJp:シード導入完了した - msgEn:TODO */
	public static final String MSG_P_0000428 = "MSG_P_0000428";
	/** msgId:MSG_P_0000429 - msgZh:表结构重置完成 - msgJp:テーブル定義を取得完了した - msgEn:TODO */
	public static final String MSG_P_0000429 = "MSG_P_0000429";
	/** msgId:MSG_P_0000430 - msgZh:表设计信息将全部消失，请重新进行表设计操作。 - msgJp:テーブル設計をなくなるので、TBL設計をやり直しください。 - msgEn:TODO */
	public static final String MSG_P_0000430 = "MSG_P_0000430";
	/** msgId:MSG_P_0000431 - msgZh:警告 - msgJp:警告 - msgEn:WARNING */
	public static final String MSG_P_0000431 = "MSG_P_0000431";
	/** msgId:MSG_P_0000432 - msgZh:错误 - msgJp:エラー - msgEn:ERROR */
	public static final String MSG_P_0000432 = "MSG_P_0000432";
	/** msgId:MSG_P_0000433 - msgZh:没有需要导出的表，请确认表设计画面 - msgJp:出力できるテーブルがありません。TBL設計を確認ください。 - msgEn:TODO */
	public static final String MSG_P_0000433 = "MSG_P_0000433";
	/** msgId:MSG_P_0000434 - msgZh:取消 - msgJp:キャンセル - msgEn:CANCEL */
	public static final String MSG_P_0000434 = "MSG_P_0000434";
	/** msgId:MSG_P_0000435 - msgZh:(无) - msgJp:(無) - msgEn:(NONE) */
	public static final String MSG_P_0000435 = "MSG_P_0000435";
	/** msgId:MSG_P_0000436 - msgZh:(无) - msgJp:NULL可 - msgEn:NULL */
	public static final String MSG_P_0000436 = "MSG_P_0000436";
	/** msgId:MSG_P_0000437 - msgZh:NULL不可 - msgJp:NULL不可 - msgEn:NOT NULL */
	public static final String MSG_P_0000437 = "MSG_P_0000437";
	/** msgId:MSG_P_0000438 - msgZh:数据校验的设定 - msgJp:データのチェックを設定 - msgEn:check */
	public static final String MSG_P_0000438 = "MSG_P_0000438";
	/** msgId:MSG_P_0000439 - msgZh:检索表数据(双击) - msgJp:データの検索(ダブルクリック) - msgEn:SEARCH DATA */
	public static final String MSG_P_0000439 = "MSG_P_0000439";
	/** msgId:MSG_P_0000440 - msgZh:复制(CTRL + C) - msgJp:コピー(CTRL + C) - msgEn:COPY */
	public static final String MSG_P_0000440 = "MSG_P_0000440";
	/** msgId:MSG_P_0000441 - msgZh:校验表中数据合法性 - msgJp:データチェック - msgEn:TODO */
	public static final String MSG_P_0000441 = "MSG_P_0000441";
	/** msgId:MSG_P_0000442 - msgZh:拷贝单元格内容 - msgJp:セールの内容をコピー - msgEn:TODO */
	public static final String MSG_P_0000442 = "MSG_P_0000442";
	/** msgId:MSG_P_0000443 - msgZh:(NULL) - msgJp:(NULL) - msgEn:(NULL) */
	public static final String MSG_P_0000443 = "MSG_P_0000443";
	/** msgId:MSG_P_0000444 - msgZh:生成文件一览： - msgJp:生成ファイル一覧： - msgEn:TODO */
	public static final String MSG_P_0000444 = "MSG_P_0000444";
	/** msgId:MSG_P_0000445 - msgZh:请确认以下错误信息。 - msgJp:下記エラー情報お確認ください。 - msgEn:TODO */
	public static final String MSG_P_0000445 = "MSG_P_0000445";
	/** msgId:MSG_P_0000446 - msgZh:需要关闭吗？ - msgJp:を閉じますか。 - msgEn:TODO */
	public static final String MSG_P_0000446 = "MSG_P_0000446";
	/** msgId:MSG_P_0000447 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_P_0000447 = "MSG_P_0000447";
	/** msgId:MSG_P_0000448 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_P_0000448 = "MSG_P_0000448";
	/** msgId:MSG_P_0000449 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_P_0000449 = "MSG_P_0000449";
	/** msgId:MSG_P_0000450 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_P_0000450 = "MSG_P_0000450";
	/** msgId:MSG_P_0000451 - msgZh:应用 - msgJp:適用 - msgEn:apply */
	public static final String MSG_P_0000451 = "MSG_P_0000451";
	/** msgId:MSG_P_0000452 - msgZh:更改映射配置 - msgJp:項目マッピング - msgEn:cloumn match */
	public static final String MSG_P_0000452 = "MSG_P_0000452";
	/** msgId:MSG_P_0000453 - msgZh:表数据BI操作 - msgJp:データBI操作 - msgEn:DATA BI */
	public static final String MSG_P_0000453 = "MSG_P_0000453";
	/** msgId:MSG_P_0000454 - msgZh:纠错处理 - msgJp:マージ処理 - msgEn:TODO */
	public static final String MSG_P_0000454 = "MSG_P_0000454";
	/** msgId:MSG_P_0000455 - msgZh:发送邮件报告结果 - msgJp:処理結果をメールで送信 - msgEn:TODO */
	public static final String MSG_P_0000455 = "MSG_P_0000455";
	/** msgId:MSG_P_0000456 - msgZh:表名 - msgJp:テーブル - msgEn:TABLE */
	public static final String MSG_P_0000456 = "MSG_P_0000456";
	/** msgId:MSG_P_0000457 - msgZh:检索条件 - msgJp:検索条件 - msgEn:WHERE */
	public static final String MSG_P_0000457 = "MSG_P_0000457";
	/** msgId:MSG_P_0000458 - msgZh:排序 - msgJp:ソート順 - msgEn:ORDER BY */
	public static final String MSG_P_0000458 = "MSG_P_0000458";
	/** msgId:MSG_P_0000459 - msgZh:SQL - msgJp:SQL - msgEn:SQL */
	public static final String MSG_P_0000459 = "MSG_P_0000459";
	/** msgId:MSG_P_0000460 - msgZh:确定 - msgJp:確定 - msgEn:TODO */
	public static final String MSG_P_0000460 = "MSG_P_0000460";
	/** msgId:MSG_P_0000461 - msgZh:生成删除SQL语句(CTRL + SHIFT + D) - msgJp:削除SQL文を生成する(CTRL + SHIFT + D) - msgEn:TODO */
	public static final String MSG_P_0000461 = "MSG_P_0000461";
	/** msgId:MSG_P_0000462 - msgZh:生成插入SQL语句(CTRL + SHIFT + I) - msgJp:挿入SQL文を生成する(CTRL + SHIFT + I) - msgEn:TODO */
	public static final String MSG_P_0000462 = "MSG_P_0000462";
	/** msgId:MSG_P_0000463 - msgZh:生成更新SQL语句(CTRL + SHIFT + U) - msgJp:更新SQL文を生成する(CTRL + SHIFT + U) - msgEn:TODO */
	public static final String MSG_P_0000463 = "MSG_P_0000463";
	/** msgId:MSG_P_0000464 - msgZh:查看每个表的记录总数(CTRL + R) - msgJp:全テーブルのレコード数(CTRL + R) - msgEn:TODO */
	public static final String MSG_P_0000464 = "MSG_P_0000464";
	/** msgId:MSG_P_0000465 - msgZh:查看每个表的字段总数(CTRL + SHIFT + R) - msgJp:全テーブルの項目数(CTRL + SHIFT + R) - msgEn:TODO */
	public static final String MSG_P_0000465 = "MSG_P_0000465";
	/** msgId:MSG_P_0000466 - msgZh:生成检索SQL语句(CTRL + SHIFT + S) - msgJp:検索SQL文を生成する(CTRL + SHIFT + S) - msgEn:TODO */
	public static final String MSG_P_0000466 = "MSG_P_0000466";
	/** msgId:MSG_P_0000467 - msgZh:拷贝头字段内容 - msgJp:ヘッダーをコピー - msgEn:TODO */
	public static final String MSG_P_0000467 = "MSG_P_0000467";
	/** msgId:MSG_P_0000468 - msgZh:不显示字段备考(CTRL + J) - msgJp:論理名を付かないで表示(CTRL + J) - msgEn:TODO */
	public static final String MSG_P_0000468 = "MSG_P_0000468";
	/** msgId:MSG_P_0000469 - msgZh:处理成功 - msgJp:成功に処理完了しました - msgEn:TODO */
	public static final String MSG_P_0000469 = "MSG_P_0000469";
	/** msgId:MSG_P_0000470 - msgZh:如果入力文件字段内容为空，该字段将忽略 - msgJp:入力ファイルに項目内容がNULL場合、スキップする。 - msgEn:TODO */
	public static final String MSG_P_0000470 = "MSG_P_0000470";
	/** msgId:MSG_P_0000471 - msgZh:是否继续执行打印操作 - msgJp:印刷操作を続きますか？ - msgEn:TODO */
	public static final String MSG_P_0000471 = "MSG_P_0000471";
	/** msgId:MSG_P_0000472 - msgZh:激活表数据BI操作面板(ctrl+1) - msgJp:テーブルBI操作画面(ctrl+1) - msgEn:TODO */
	public static final String MSG_P_0000472 = "MSG_P_0000472";
	/** msgId:MSG_P_0000473 - msgZh:激活表结构信息面板(ctrl+2) - msgJp:テーブル定義操作画面(ctrl+2) - msgEn:TODO */
	public static final String MSG_P_0000473 = "MSG_P_0000473";
	/** msgId:MSG_P_0000474 - msgZh:激活SQL文窗口(ctrl+q) - msgJp:SQL文画面(ctrl+q) - msgEn:TODO */
	public static final String MSG_P_0000474 = "MSG_P_0000474";
	/** msgId:MSG_P_0000475 - msgZh:激活SQL执行历史窗口(ctrl+h) - msgJp:SQL実行履歴画面(ctrl+h) - msgEn:TODO */
	public static final String MSG_P_0000475 = "MSG_P_0000475";
	/** msgId:MSG_P_0000476 - msgZh:激活表的设计窗口(ctrl+d) - msgJp:テーブル設計画面(ctrl+d) - msgEn:TODO */
	public static final String MSG_P_0000476 = "MSG_P_0000476";
	/** msgId:MSG_P_0000477 - msgZh:选择应用映射： - msgJp:マッピングタイプ： - msgEn:TODO */
	public static final String MSG_P_0000477 = "MSG_P_0000477";
	/** msgId:MSG_P_0000478 - msgZh:总进度    - msgJp:processing    - msgEn:TODO */
	public static final String MSG_P_0000478 = "MSG_P_0000478";
	/** msgId:MSG_P_0000479 - msgZh:成功导出 - msgJp:成功に導出しました。 - msgEn:TODO */
	public static final String MSG_P_0000479 = "MSG_P_0000479";
	/** msgId:MSG_P_0000480 - msgZh:请耐心等待处理结果… - msgJp:少々お待ちください… - msgEn:TODO */
	public static final String MSG_P_0000480 = "MSG_P_0000480";
	/** msgId:MSG_P_0000481 - msgZh:确定要终止当前操作吗？ - msgJp:処理を停止しますか？ - msgEn:TODO */
	public static final String MSG_P_0000481 = "MSG_P_0000481";
	/** msgId:MSG_P_0000482 - msgZh:没有找到对应的映射类型。 - msgJp:マッピングタイプを見つかれません。 - msgEn:TODO */
	public static final String MSG_P_0000482 = "MSG_P_0000482";
	/** msgId:MSG_P_0000483 - msgZh:物理删除选中记录(CTRL + ALT + D) - msgJp:DBから削除する(CTRL + ALT + D) - msgEn:TODO */
	public static final String MSG_P_0000483 = "MSG_P_0000483";
	/** msgId:MSG_P_0000484 - msgZh:物理插入记录(空记录)(CTRL + ALT + I) - msgJp:DBに挿入する(空白行)(CTRL + ALT + I) - msgEn:TODO */
	public static final String MSG_P_0000484 = "MSG_P_0000484";
	/** msgId:MSG_P_0000485 - msgZh:物理更新数据库(CTRL + ALT + S) - msgJp:DBに反映する(CTRL + ALT + S) - msgEn:TODO */
	public static final String MSG_P_0000485 = "MSG_P_0000485";
	/** msgId:MSG_P_0000486 - msgZh:修改前 - msgJp:修正前 - msgEn:TODO */
	public static final String MSG_P_0000486 = "MSG_P_0000486";
	/** msgId:MSG_P_0000487 - msgZh:数据库类型验证没有通过 - msgJp:DBチェックエラー - msgEn:TODO */
	public static final String MSG_P_0000487 = "MSG_P_0000487";
	/** msgId:MSG_P_0000488 - msgZh:业务逻辑验证没有通过 - msgJp:業務ロジックチェックエラー - msgEn:TODO */
	public static final String MSG_P_0000488 = "MSG_P_0000488";
	/** msgId:MSG_P_0000489 - msgZh:物理剪切板粘贴插入记录(CTRL + ALT + V) - msgJp:DBに挿入する(Clipboardから)(CTRL + ALT + V) - msgEn:TODO */
	public static final String MSG_P_0000489 = "MSG_P_0000489";
	/** msgId:MSG_P_0000490 - msgZh:数据没有被更改。 - msgJp:データを修正されない。 - msgEn:TODO */
	public static final String MSG_P_0000490 = "MSG_P_0000490";
	/** msgId:MSG_P_0000491 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_P_0000491 = "MSG_P_0000491";
	/** msgId:MSG_P_0000492 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_P_0000492 = "MSG_P_0000492";
	/** msgId:MSG_P_0000493 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_P_0000493 = "MSG_P_0000493";
	/** msgId:MSG_P_0000494 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_P_0000494 = "MSG_P_0000494";
	/** msgId:MSG_P_0000495 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_P_0000495 = "MSG_P_0000495";
	/** msgId:MSG_P_0000496 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_P_0000496 = "MSG_P_0000496";
	/** msgId:MSG_P_0000497 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_P_0000497 = "MSG_P_0000497";
	/** msgId:MSG_P_0000498 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_P_0000498 = "MSG_P_0000498";
	/** msgId:MSG_P_0000499 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_P_0000499 = "MSG_P_0000499";
	/** msgId:MSG_P_0000500 - msgZh:TODO - msgJp:TODO - msgEn:TODO */
	public static final String MSG_P_0000500 = "MSG_P_0000500";
	}

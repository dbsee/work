package jp.co.csj.tools.utils.db.core;

import org.apache.poi.ss.usermodel.RichTextString;
import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.utils.common.constant.CsjConst;

public class XlsTblPara extends TblPara {
	protected String paraVal = CsjConst.EMPTY;
	protected String paraValOld = CsjConst.EMPTY;
	private RichTextString richText = null;
	private int xlsRowNum = 0;
	private int xlsColNum = 0;
	public XlsTblPara() {
		super();
	}

	public XlsTblPara(TblPara para) {
		this.canNull = para.isCanNull();
		this.isLastPara = para.isLastPara();
		this.isParaHalf = para.isParaHalf();
		this.isPkey = para.isPkey();
		this.paraComment = para.getParaComment();
		this.paraForCreate = para.getParaForCreate();
		this.paraInitVal = para.getParaInitVal();
		this.paraLen = para.getParaLen();
		this.paraNmEn = para.getParaNmEn();
		this.paraNmJp = para.getParaNmJp();
		this.paraNmJpSync = para.getParaNmJpSync();
		this.paraStrLen = para.getParaStrLen();
		this.paraType = para.getParaType();
		this.paraTypeWithlen = para.getParaTypeWithlen();
		this.type = para.getType();
		this.paraNumDotEndLen = para.getParaNumDotEndLen();
		this.paraExtra = para.getParaExtra();
		this.colDisplayNm = para.getColDisplayNm();
		this.paraCheck = para.getParaCheck();

	}
	
	public XlsTblPara(XlsTblPara para) {
		this.canNull = para.isCanNull();
		this.isLastPara = para.isLastPara();
		this.isParaHalf = para.isParaHalf();
		this.isPkey = para.isPkey();
		this.paraComment = para.getParaComment();
		this.paraForCreate = para.getParaForCreate();
		this.paraInitVal = para.getParaInitVal();
		this.paraLen = para.getParaLen();
		this.paraNmEn = para.getParaNmEn();
		this.paraNmJp = para.getParaNmJp();
		this.paraNmJpSync = para.getParaNmJpSync();
		this.paraStrLen = para.getParaStrLen();
		this.paraType = para.getParaType();
		this.paraTypeWithlen = para.getParaTypeWithlen();
		this.type = para.getType();
		this.paraNumDotEndLen = para.getParaNumDotEndLen();
		this.paraExtra = para.getParaExtra();
		this.colDisplayNm = para.getColDisplayNm();
		this.paraCheck = para.getParaCheck();
		this.paraVal = para.getParaVal();
		this.paraValOld = para.getParaValOld();
	}

	public XlsTblPara(TblPara para, String val) {
		this.canNull = para.isCanNull();
		this.isLastPara = para.isLastPara();
		this.isParaHalf = para.isParaHalf();
		this.isPkey = para.isPkey();
		this.paraComment = para.getParaComment();
		this.paraForCreate = para.getParaForCreate();
		this.paraInitVal = para.getParaInitVal();
		this.paraLen = para.getParaLen();
		this.paraNmEn = para.getParaNmEn();
		this.paraNmJp = para.getParaNmJp();
		this.paraNmJpSync = para.getParaNmJpSync();
		this.paraStrLen = para.getParaStrLen();
		this.paraType = para.getParaType();
		this.paraTypeWithlen = para.getParaTypeWithlen();
		this.type = para.getType();
		this.paraNumDotEndLen = para.getParaNumDotEndLen();
		this.paraExtra = para.getParaExtra();
		this.colDisplayNm = para.getColDisplayNm();
		this.paraCheck = para.getParaCheck();
		this.paraVal = val;
		this.paraValOld = val;
	}

	public void setParaValOri(String paraVal) {
		this.paraVal = paraVal;
		this.paraValOld = paraVal;

	}

	public String getParaVal() {
		return paraVal;
	}

	public void setParaVal(String paraVal) {
		if (type==DbInfo.TABLE_COL_TYPE_DATE) {

			this.paraVal = CmnStrUtils.funReplace(paraVal, "-", "/");
//			this.paraVal = CsjStrUtils.funReplace(this.paraVal, "_", "/");
//			this.paraVal = CsjStrUtils.funReplace(this.paraVal, ".", "/");
		} else {
			this.paraVal = paraVal;
		}
		this.paraValOld = this.paraVal;
	}

	/**
	 * @return the richText
	 */
	public RichTextString getRichText() {
		return richText;
	}

	/**
	 * @param richText the richText to set
	 */
	public void setRichText(RichTextString richText) {
		this.richText = richText;
	}

	/**
	 * @return the xlsRowNum
	 */
	public int getXlsRowNum() {
		return xlsRowNum;
	}

	/**
	 * @param xlsRowNum the xlsRowNum to set
	 */
	public void setXlsRowNum(int xlsRowNum) {
		this.xlsRowNum = xlsRowNum;
	}

	/**
	 * @return the xlsColNum
	 */
	public int getXlsColNum() {
		return xlsColNum;
	}

	/**
	 * @param xlsColNum the xlsColNum to set
	 */
	public void setXlsColNum(int xlsColNum) {
		this.xlsColNum = xlsColNum;
	}


	public String getParaValOld() {
		return paraValOld;
	}

	public void setParaValOld(String paraValOld) {
		this.paraValOld = paraValOld;
	}

	@Override
	public String toString() {
		return "XlsTblPara [paraVal=" + paraVal + ", paraValOld=" + paraValOld
				+ ", richText=" + richText + ", xlsRowNum=" + xlsRowNum
				+ ", xlsColNum=" + xlsColNum + ", paraNmEn=" + paraNmEn
				+ ", paraNmJp=" + paraNmJp + ", paraNmJpSync=" + paraNmJpSync
				+ ", paraType=" + paraType + ", paraTypeWithlen="
				+ paraTypeWithlen + ", paraExtra=" + paraExtra + ", paraLen="
				+ paraLen + ", paraNumDotEndLen=" + paraNumDotEndLen
				+ ", paraStrLen=" + paraStrLen + ", isParaHalf=" + isParaHalf
				+ ", isPkey=" + isPkey + ", canNull=" + canNull
				+ ", paraComment=" + paraComment + ", paraInitVal="
				+ paraInitVal + ", paraForCreate=" + paraForCreate
				+ ", isLastPara=" + isLastPara + ", paraPos=" + paraPos
				+ ", paraChangedType=" + paraChangedType + ", type=" + type
				+ ", colDisplayNm=" + colDisplayNm + ", paraCheck=" + paraCheck
				+ "]";
	}

}

package jp.co.csj.tools.utils.db.core;

import java.util.HashMap;
import java.util.List;

import org.mydbsee.common.CmnLog5j;
import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.db.core.para.ParaCheck;
import jp.co.csj.tools.utils.msg.dbtools.CsjDbToolsMsg;
import jp.co.csj.tools.utils.reg.RegConstStr;
import jp.co.csj.tools.utils.z.exe.batch.AutoDbToXls;

public class TblPara {

	public static final int CHANGED_TYPE_NO_CHANGE = 0;
	public static final int CHANGED_TYPE_UPD_TYPE = 1;
	public static final int CHANGED_TYPE_LENGTH_MINUS = 2;
	public static final int CHANGED_TYPE_PK = 3;
	public static final int CHANGED_TYPE_CAN_NOT_NULL = 4;
//	public static HashSet<String> paraTypeSet = new HashSet<String>();

	/**
	 *
	 */
	public TblPara() {

	}

	protected String paraNmEn = CsjConst.EMPTY;
	protected String paraNmJp = CsjConst.EMPTY;
	protected String paraNmJpForPic = CsjConst.EMPTY;
	protected String paraNmJpSync = CsjConst.EMPTY;
	protected String paraType = CsjConst.EMPTY;
	protected String paraTypeWithlen = CsjConst.EMPTY;
	protected String paraExtra = CsjConst.EMPTY;
	protected long paraLen = 0;
	protected long paraNumDotEndLen = 0;
	protected String paraStrLen = CsjConst.EMPTY;
	protected boolean isParaHalf = true;
	protected boolean isPkey = false;
	protected boolean canNull = true;
	protected String paraComment = CsjConst.EMPTY;
	protected String paraInitVal = CsjConst.EMPTY;
	protected String paraForCreate = CsjConst.EMPTY;
	protected boolean isLastPara = false;
	protected int paraPos = 0;
	protected int paraChangedType = CHANGED_TYPE_NO_CHANGE;
	protected int type = -1;
	protected String colDisplayNm = CsjConst.EMPTY;
	protected ParaCheck paraCheck = new ParaCheck(this);

	public static String getColTypeInfo(TblPara para ) {
		String type = DbInfo.TABLE_COL_TYPE_STR_INFO;
		if (para.getType() == DbInfo.TABLE_COL_TYPE_DATE) {
			type = DbInfo.TABLE_COL_TYPE_DATE_INFO;
		} else if (para.getType() == DbInfo.TABLE_COL_TYPE_NUM) {
			type = DbInfo.TABLE_COL_TYPE_NUM_INFO;
		} else if (para.getType() == DbInfo.TABLE_COL_TYPE_NULL) {
			type = DbInfo.TABLE_COL_TYPE_NULL_INFO;
		}
		return type;
	}
	/**
	 * @return the isParaHalf
	 */
	public boolean isParaHalf() {
		return isParaHalf;
	}
	/**
	 * @param isParaHalf the isParaHalf to set
	 */
	public void setParaHalf(boolean isParaHalf) {
		this.isParaHalf = isParaHalf;
	}
	/**
	 * @return the paraStrLen
	 */
	public String getParaStrLen() {
		return paraStrLen;
	}
	/**
	 * @param paraStrLen the paraStrLen to set
	 */
	public void setParaStrLen(String paraStrLen) {
		this.paraStrLen = paraStrLen;
	}

	public String getParaExtra() {
		return paraExtra;
	}
	public void setParaExtra(String paraExtra) {
		this.paraExtra = paraExtra;
	}
	/**
	 * @return the isLastPara
	 */
	public boolean isLastPara() {
		return isLastPara;
	}
	/**
	 * @param isLastPara the isLastPara to set
	 */
	public void setLastPara(boolean isLastPara) {
		this.isLastPara = isLastPara;
	}
	public String getParaNmEn() {
		return paraNmEn;
	}
	/**
	 * @return the paraForCreate
	 */
	public String getParaForCreate() {
		return paraForCreate;
	}
	/**
	 * @param paraForCreate the paraForCreate to set
	 */
	public void setParaForCreate(String paraForCreate) {
		this.paraForCreate = paraForCreate;
	}
	public void setParaNmEn(String paraNmEn) {
		this.paraNmEn = paraNmEn;
	}
	public String getParaNmJp() {
		return paraNmJp;
	}
	public void setParaNmJp(String paraNmJp) {
		this.paraNmJp = paraNmJp;
	}
	public String getParaType() {
		return paraType;
	}
	public void setParaType(String paraType) {
		if (CmnStrUtils.isEmpty(paraType)) {

			this.paraType = paraTypeWithlen.replaceAll(RegConstStr.DB_TYPE_REPLACE, "");
		} else {
			this.paraType = paraType;
		}

	}
	public long getParaLen() {
		return paraLen;
	}
	public void setParaLen(long paraLen) {
		this.paraLen = paraLen;
	}
	public boolean isPkey() {
		return isPkey;
	}
	public void setPkey(boolean isPkey) {
		this.isPkey = isPkey;
		if (isPkey) {
			this.setCanNull(false);
		}
	}
	public boolean isCanNull() {
		return canNull;
	}
	public void setCanNull(boolean canNull) {
		this.canNull = canNull;
	}
	public String getParaComment() {
		return paraComment;
	}
	public void setParaComment(String paraComment) {
		this.paraComment = paraComment;
	}

	public String getParaInitVal() {
		return paraInitVal;
	}
	public void setParaInitVal(String paraInitVal) {
		this.paraInitVal = paraInitVal;
	}
//	/* (non-Javadoc)
//	 * @see java.lang.Object#toString()
//	 */
//	@Override
//	public String toString() {
//
//		System.out.println("paraNmEn ->" + paraNmEn);
//		System.out.println("paraNmJp ->" + paraNmJp);
//		System.out.println("paraType ->" + paraType);
//		System.out.println("paraLen ->" + paraLen);
//		System.out.println("isPkey ->" + isPkey);
//		System.out.println("canNull ->" + canNull);
//		System.out.println("paraComment ->" + paraComment);
//		System.out.println("paraInitVal ->" + paraInitVal);
//
//		return super.toString();
//	}
	public String getParaTypeWithlen() {
		return paraTypeWithlen;
	}

	public void setParaTypeWithlen(String dbType, String paraTypeWithlen) {
		this.paraTypeWithlen = paraTypeWithlen;

		setType(DbInfo.getDbTypeByStr(paraTypeWithlen));

		if (CmnStrUtils.isEmpty(this.paraType)) {
			if (DbInfo.TABLE_COL_TYPE_STR == this.type) {
				this.paraType = CmnStrUtils.fromAtoBByTrim(this.paraTypeWithlen, "", "(");
			} else {
				this.paraType = this.paraTypeWithlen.replaceAll(RegConstStr.DB_TYPE_REPLACE, "");
			}
			
		}
		if (DbInfo.STR_DB_TYPE_SQLSERVER.equals(dbType) && this.type == DbInfo.TABLE_COL_TYPE_DATE) {
			this.paraTypeWithlen =this.paraType;
		} else if (DbInfo.STR_DB_TYPE_SYBASE.equals(dbType) && this.paraType.toLowerCase().contains("int")) {
			this.paraTypeWithlen =this.paraType;
		}

			if (this.type == DbInfo.TABLE_COL_TYPE_NUM) {
					List<String> l = CmnStrUtils.getListStrSplit(paraTypeWithlen, ",", false,true);
					if (l.size()!=0) {
						this.paraLen = CmnStrUtils.getLongVal(CmnStrUtils.getNumericByStr(l.get(0),true));
						if (l.size() == 2) {
							this.paraNumDotEndLen =  CmnStrUtils.getLongVal(CmnStrUtils.getNumericByStr(l.get(1),true));
							this.paraLen = this.paraLen -this.paraNumDotEndLen;
						}
					}
					if (this.paraLen == 0&&this.paraNumDotEndLen==0) {
						this.paraLen =0;
					}
			} else {
				// TODOSAI
				this.paraLen = CmnStrUtils.getLongVal(CmnStrUtils.getNumericByStr(CmnStrUtils.fromAtoBByTrim(paraTypeWithlen, CsjConst.SIGN_BRACKETS_S_L, ""),false));
//				if (this.paraLen == 0) {
//					this.paraLen = -1;
//				}
			}
//		if (this.type == ) {
//			this.setParaNumDotEndLen(CsjStrUtils.getAllNumericByStr(CsjStrUtils.fromAtoBByTrim(this.paraTypeWithlen,",", "")));
//		}
		setDisplayNm();
	}
	/**
	 *
	 */
	public void setDisplayNm() {
		StringBuffer sb = new StringBuffer();
		if (CmnStrUtils.isNotEmpty(this.getParaNmJp())) {
			sb.append(CmnLog5j.addlrBracketsM(this.getParaNmJp(), true));
		}
		
		sb.append(CsjDbToolsMsg.coreMsgMap.get(CsjDbToolsMsg.MSG_I_0000043)
				+ CmnLog5j.addlrBracketsM(this.getParaTypeWithlen(), true)+CmnStrUtils.toStr(getParaExtra()));

		colDisplayNm = this.getParaNmEn()
		+ CmnLog5j.addlrBracket_M_L_JP(sb.toString(), false);
	}
	public int getParaPos() {
		return paraPos;
	}
	public void setParaPos(int paraPos) {
		this.paraPos = paraPos;
	}

	public void setSelfValFromDb(HashMap<String, String> map, String dbType) {
		this.setParaExtra(CmnStrUtils.convertString(map.get(AutoDbToXls.COL_EXTRA)));
		this.setCanNull("Y".equals(CmnStrUtils.convertString(map.get(AutoDbToXls.COL_CAN_NULL))));
		this.setPkey("Y".equals(CmnStrUtils.convertString(map.get(AutoDbToXls.COL_IS_PK))));
		this.setParaInitVal(CmnStrUtils.convertString(map.get(AutoDbToXls.COL_DEFAULT)));
		this.setParaPos(Integer.parseInt(CmnStrUtils.convertString(map.get(AutoDbToXls.COL_ID))));
		this.setParaNmEn(CmnStrUtils.convertString(map.get(AutoDbToXls.COL_NM_EN)));
		this.setParaNmJp(CmnStrUtils.convertString(map.get(AutoDbToXls.COL_NM_JP)));
		this.setParaType(CmnStrUtils.convertString(map.get(AutoDbToXls.COL_TYPE)));
		this.setParaLen(CmnStrUtils.getLongVal(CmnStrUtils.convertString(map.get(AutoDbToXls.COL_LENGTH))));
		this.setParaTypeWithlen(dbType,CmnStrUtils.convertString(map.get(AutoDbToXls.COL_TYPE_INFO)));


		
	}

	/**
	 * @return the paraChangedType
	 */
	public int getParaChangedType() {
		return paraChangedType;
	}

	/**
	 * @param paraChangedType the paraChangedType to set
	 */
	public void setParaChangedType(int paraChangedType) {
		this.paraChangedType = paraChangedType;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the paraNumDotEndLen
	 */
	public long getParaNumDotEndLen() {
		return paraNumDotEndLen;
	}
	/**
	 * @param paraNumDotEndLen the paraNumDotEndLen to set
	 */
	public void setParaNumDotEndLen(long paraNumDotEndLen) {
		this.paraNumDotEndLen = paraNumDotEndLen;
	}

	
	@Override
	public String toString() {
		return "TblPara [paraNmEn=" + paraNmEn + ", paraNmJp=" + paraNmJp
				+ ", paraNmJpSync=" + paraNmJpSync + ", paraType=" + paraType
				+ ", paraTypeWithlen=" + paraTypeWithlen + ", paraExtra="
				+ paraExtra + ", paraLen=" + paraLen + ", paraNumDotEndLen="
				+ paraNumDotEndLen + ", paraStrLen=" + paraStrLen
				+ ", isParaHalf=" + isParaHalf + ", isPkey=" + isPkey
				+ ", canNull=" + canNull + ", paraComment=" + paraComment
				+ ", paraInitVal=" + paraInitVal + ", paraForCreate="
				+ paraForCreate + ", isLastPara=" + isLastPara + ", paraPos="
				+ paraPos + ", paraChangedType=" + paraChangedType + ", type="
				+ type + ", colDisplayNm=" + colDisplayNm + ", paraCheck="
				+ paraCheck + ", isParaHalf()=" + isParaHalf()
				+ ", getParaStrLen()=" + getParaStrLen() + ", getParaExtra()="
				+ getParaExtra() + ", isLastPara()=" + isLastPara()
				+ ", getParaNmEn()=" + getParaNmEn() + ", getParaForCreate()="
				+ getParaForCreate() + ", getParaNmJp()=" + getParaNmJp()
				+ ", getParaType()=" + getParaType() + ", getParaLen()="
				+ getParaLen() + ", isPkey()=" + isPkey() + ", isCanNull()="
				+ isCanNull() + ", getParaComment()=" + getParaComment()
				+ ", getParaInitVal()=" + getParaInitVal()
				+ ", getParaTypeWithlen()=" + getParaTypeWithlen()
				+ ", getParaPos()=" + getParaPos() + ", getParaChangedType()="
				+ getParaChangedType() + ", getType()=" + getType()
				+ ", getParaNumDotEndLen()=" + getParaNumDotEndLen()
				+ ", getColDisplayNm()=" + getColDisplayNm()
				+ ", getParaCheck()=" + getParaCheck() + ", getParaNmJpSync()="
				+ getParaNmJpSync() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	/**
	 * @return the colDisplayNm
	 */
	public String getColDisplayNm() {
		return colDisplayNm;
	}
	/**
	 * @param colDisplayNm the colDisplayNm to set
	 */
	public void setColDisplayNm(String colDisplayNm) {
		this.colDisplayNm = colDisplayNm;
	}
	/**
	 * @return the paraCheck
	 */
	public ParaCheck getParaCheck() {
		return paraCheck;
	}
	/**
	 * @param paraCheck the paraCheck to set
	 */
	public void setParaCheck(ParaCheck paraCheck) {
		paraCheck.setTblPara(this);
		this.paraCheck = paraCheck;
	}
	/**
	 * @return the paraNmJpSync
	 */
	public String getParaNmJpSync() {
		return paraNmJpSync;
	}
	/**
	 * @param paraNmJpSync the paraNmJpSync to set
	 */
	public void setParaNmJpSync(String paraNmJpSync) {
		this.paraNmJpSync = paraNmJpSync;
	}
	/**
	 * @param dbType
	 * @param paraType2
	 * @param paraLen2
	 * @param paraNumDotEndLen2
	 */
	public void setParaTypeWithlen(String paraType2,
			long paraLen2, long paraNumDotEndLen2) {

		if (CmnStrUtils.isEmpty(this.paraType)) {
			this.paraTypeWithlen = "";
		}

		if (this.paraLen == 0) {
			this.paraTypeWithlen = paraType2;
		} else if (paraNumDotEndLen2 == 0) {
			this.paraTypeWithlen = paraType2+"("+paraLen+")";
		} else {
			this.paraTypeWithlen = paraType2+"("+paraLen+","+paraNumDotEndLen2+")";
		}
	}
}

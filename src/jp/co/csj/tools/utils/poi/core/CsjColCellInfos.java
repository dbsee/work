/**
 * 
 */
package jp.co.csj.tools.utils.poi.core;

import java.io.File;
import java.util.HashMap;

import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.utils.common.CsjProcess;
import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.reg.RegConstStr;

/**
 * @author Think
 *
 */
public class CsjColCellInfos {
	public static int STATUS_NOTHING = 0;
	public static int STATUS_EXCEL = 1;
	public static int STATUS_NOT_EXCEL = 2;
	
	private String filename;
	private String sheetname;
	private int colnum = 0;
	private int rownumstart= 0;
	private int rownumend = 0;
	private boolean isRepeat = false;
	private boolean isRandom = false;
	private int status = STATUS_NOTHING;
	private String encode;
	private String splitch;
	private String beginstr;
	private String endstr;
	
	public String getKey () {
		return filename+CsjConst.SIGN_SEPARATOR_3+sheetname;
	}
	
	/**
	 * 
	 */
	public CsjColCellInfos(String content) {
		boolean checkOk = content.contains("filename") && content.contains("sheetname") 
		&& content.contains("colnum") && content.contains("rownumstart")
		&& content.contains("rownumend")&& content.contains("isrepeat")
		&& content.contains("israndom") && content.contains("encode") && content.contains("splitch")&& content.contains("beginstr") && content.contains("endstr");
		if (checkOk) {
			HashMap<String, String> map = CmnStrUtils.getSplitMap(content, CsjConst.SIGN_ENTER_N, CsjConst.MASK_TO_RIGHT, null, false);
			filename = CmnStrUtils.getNoNullStr(map.get("filename"));
			sheetname = CmnStrUtils.getNoNullStr(map.get("sheetname"));
			encode = CmnStrUtils.getNoNullStr(map.get("encode"));
			splitch= CmnStrUtils.getNoNullStr(map.get("splitch"));
			beginstr= CmnStrUtils.getNoNullStr(map.get("beginstr"));
			endstr= CmnStrUtils.getNoNullStr(map.get("endstr"));
			colnum = CmnStrUtils.getIntVal(map.get("colnum"));
			rownumstart = CmnStrUtils.getIntVal(map.get("rownumstart"));
			rownumend = CmnStrUtils.getIntVal(map.get("rownumend"));
			isRepeat = "1".equals(map.get("isrepeat"));
			isRandom = "1".equals(map.get("israndom"));
			if (CmnStrUtils.isMatchIgnorReg(filename, RegConstStr.EXCEL_REG_DOT)) {
				status = STATUS_EXCEL;
			} else  {
				status = STATUS_NOT_EXCEL;
			} 
			File f = new File(filename);
			if (f.isFile()) {
				filename = f.getAbsolutePath();
			} else {
				filename = CsjProcess.s_pj_path+filename;
			}
		}
	}
	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the sheetname
	 */
	public String getSheetname() {
		return sheetname;
	}

	/**
	 * @param sheetname the sheetname to set
	 */
	public void setSheetname(String sheetname) {
		this.sheetname = sheetname;
	}

	/**
	 * @return the colnum
	 */
	public int getColnum() {
		return colnum;
	}

	/**
	 * @param colnum the colnum to set
	 */
	public void setColnum(int colnum) {
		this.colnum = colnum;
	}

	/**
	 * @return the rownumstart
	 */
	public int getRownumstart() {
		return rownumstart;
	}

	/**
	 * @param rownumstart the rownumstart to set
	 */
	public void setRownumstart(int rownumstart) {
		this.rownumstart = rownumstart;
	}

	/**
	 * @return the rownumend
	 */
	public int getRownumend() {
		return rownumend;
	}

	/**
	 * @param rownumend the rownumend to set
	 */
	public void setRownumend(int rownumend) {
		this.rownumend = rownumend;
	}

	/**
	 * @return the isRepeat
	 */
	public boolean isRepeat() {
		return isRepeat;
	}

	/**
	 * @param isRepeat the isRepeat to set
	 */
	public void setRepeat(boolean isRepeat) {
		this.isRepeat = isRepeat;
	}

	/**
	 * @return the isRandom
	 */
	public boolean isRandom() {
		return isRandom;
	}

	/**
	 * @param isRandom the isRandom to set
	 */
	public void setRandom(boolean isRandom) {
		this.isRandom = isRandom;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the encode
	 */
	public String getEncode() {
		return encode;
	}

	/**
	 * @param encode the encode to set
	 */
	public void setEncode(String encode) {
		this.encode = encode;
	}

	/**
	 * @return the splitch
	 */
	public String getSplitch() {
		return splitch;
	}

	/**
	 * @param splitch the splitch to set
	 */
	public void setSplitch(String splitch) {
		this.splitch = splitch;
	}

	/**
	 * @return the beginstr
	 */
	public String getBeginstr() {
		return beginstr;
	}

	/**
	 * @param beginstr the beginstr to set
	 */
	public void setBeginstr(String beginstr) {
		this.beginstr = beginstr;
	}

	/**
	 * @return the endstr
	 */
	public String getEndstr() {
		return endstr;
	}

	/**
	 * @param endstr the endstr to set
	 */
	public void setEndstr(String endstr) {
		this.endstr = endstr;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CsjColCellInfos [filename=" + filename + ", sheetname="
				+ sheetname + ", colnum=" + colnum + ", rownumstart="
				+ rownumstart + ", rownumend=" + rownumend + ", isRepeat="
				+ isRepeat + ", isRandom=" + isRandom + ", status=" + status
				+ ", encode=" + encode + ", splitch=" + splitch + ", beginstr="
				+ beginstr + ", endstr=" + endstr + "]";
	}



}

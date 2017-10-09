/**
 *
 */
package jp.co.csj.tools.utils.db.core.para;

import java.util.LinkedHashSet;
import java.util.Set;

import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.utils.common.CsjProcess;
import jp.co.csj.tools.utils.common.constant.CsjConst;
import jp.co.csj.tools.utils.db.core.DbInfo;
import jp.co.csj.tools.utils.db.core.TblPara;



/**
 * @author Think
 *
 */
public class ParaCheck {
	private Set<String> equalSet = new LinkedHashSet<String>();
	private String equalStr = "";
	private Set<String> noEqualSet = new LinkedHashSet<String>();
	private String noEqualStr = "";
	private String reg = "";
	private String maxVal = "";
	private String minVal = "";
	private String maxLen = "";
	private String minLen = "";
	private String paraSummary = CsjConst.EMPTY;
	private TblPara tblPara = null;

	public ParaCheck(TblPara tblPara) {
		this.tblPara = tblPara;
	}

	public String getCmtInfo () {
		StringBuffer sb = new StringBuffer();

		if (CmnStrUtils.isNotEmpty(this.getParaSummary())) {
			sb.append(CsjConst.SIGN_CIRCLE_HOLLOW_2);
			sb.append(CsjConst.JP_SUMMARY);
			sb.append(CsjConst.MASK_TO_RIGHT);
			sb.append(this.getParaSummary());
			sb.append(CsjProcess.s_newLine);
		}

		if (CmnStrUtils.isNotEmpty(this.getReg())) {
			sb.append(CsjConst.SIGN_CIRCLE_HOLLOW_2);
			sb.append(CsjConst.JP_REG);
			sb.append(CsjConst.MASK_TO_RIGHT);
			sb.append(this.getReg());
			sb.append(CsjProcess.s_newLine);
		}

		if (CmnStrUtils.isNotEmpty(this.getEqualStr())) {
			sb.append(CsjConst.SIGN_CIRCLE_HOLLOW_2);
			sb.append(CsjConst.JP_EQUAL);
			sb.append(CsjConst.MASK_TO_RIGHT);
			sb.append(this.getEqualStr());
			sb.append(CsjProcess.s_newLine);
		}

		if (CmnStrUtils.isNotEmpty(this.getNoEqualStr())) {
			sb.append(CsjConst.SIGN_CIRCLE_HOLLOW_2);
			sb.append(CsjConst.JP_NO_EQUAL);
			sb.append(CsjConst.MASK_TO_RIGHT);
			sb.append(this.getNoEqualStr());
			sb.append(CsjProcess.s_newLine);
		}
		if (CmnStrUtils.isNotEmpty(this.getMaxVal())) {
			sb.append(CsjConst.SIGN_CIRCLE_HOLLOW_2);
			sb.append(CsjConst.JP_MAX_VAL);
			sb.append(CsjConst.MASK_TO_RIGHT);
			sb.append(this.getMaxVal());
			sb.append(CsjProcess.s_newLine);
		}

		if (CmnStrUtils.isNotEmpty(this.getMinVal())) {
			sb.append(CsjConst.SIGN_CIRCLE_HOLLOW_2);
			sb.append(CsjConst.JP_MIN_VAL);
			sb.append(CsjConst.MASK_TO_RIGHT);
			sb.append(this.getMinVal());
			sb.append(CsjProcess.s_newLine);
		}
		if (CmnStrUtils.isNotEmpty(this.getMaxLen())) {
			sb.append(CsjConst.SIGN_CIRCLE_HOLLOW_2);
			sb.append(CsjConst.JP_MAX_LEN);
			sb.append(CsjConst.MASK_TO_RIGHT);
			sb.append(this.getMaxLen());
			sb.append(CsjProcess.s_newLine);
		}

		if (CmnStrUtils.isNotEmpty(this.getMinLen())) {
			sb.append(CsjConst.SIGN_CIRCLE_HOLLOW_2);
			sb.append(CsjConst.JP_MIN_LEN);
			sb.append(CsjConst.MASK_TO_RIGHT);
			sb.append(this.getMinLen());
			sb.append(CsjProcess.s_newLine);
		}
		return sb.toString();
	}
	public ParaCheck(ParaCheck paraCheck) {
		for (String str : paraCheck.getEqualSet()) {
			this.equalSet.add(str);
		}
		for (String str : paraCheck.getNoEqualSet()) {
			this.noEqualSet.add(str);
		}
		reg = paraCheck.getReg();
		maxVal = paraCheck.getMaxVal();
		minVal = paraCheck.getMinVal();
		maxLen = paraCheck.getMaxLen();
		minLen = paraCheck.getMinLen();
		paraSummary = paraCheck.getParaSummary();
		equalStr = paraCheck.getEqualStr();
		noEqualStr = paraCheck.getNoEqualStr();
		this.tblPara = paraCheck.getTblPara();
	}
	/**
	 * @return the reg
	 */
	public String getReg() {
		return reg;
	}
	/**
	 * @param reg the reg to set
	 */
	public void setReg(String reg) {
		this.reg = reg;
	}
	/**
	 * @return the maxVal
	 */
	public String getMaxVal() {
		return maxVal;
	}
	/**
	 * @param maxVal the maxVal to set
	 */
	public void setMaxVal(String maxVal) {
		if (this.tblPara.getType()==DbInfo.TABLE_COL_TYPE_NUM) {
			this.maxVal = CmnStrUtils.getStringAllNumericByStr(maxVal);
		} else {
			this.maxVal = maxVal;
		}

	}
	/**
	 * @return the minVal
	 */
	public String getMinVal() {
		return minVal;
	}
	/**
	 * @param minVal the minVal to set
	 */
	public void setMinVal(String minVal) {
		if (this.tblPara.getType()==DbInfo.TABLE_COL_TYPE_NUM) {
			this.minVal = CmnStrUtils.getStringAllNumericByStr(minVal);
		} else {
			this.minVal = minVal;
		}

	}
	/**
	 * @return the maxLen
	 */
	public String getMaxLen() {
		return maxLen;
	}
	/**
	 * @param maxLen the maxLen to set
	 */
	public void setMaxLen(String maxLen) {
		this.maxLen = CmnStrUtils.getStringAllNumericByStr(maxLen);
	}
	/**
	 * @return the minLen
	 */
	public String getMinLen() {
		return minLen;
	}
	/**
	 * @param minLen the minLen to set
	 */
	public void setMinLen(String minLen) {

		this.minLen = CmnStrUtils.getStringAllNumericByStr(minLen);
	}
	/**
	 * @return the equalSet
	 */
	public Set<String> getEqualSet() {
		return equalSet;
	}
	/**
	 * @param equalSet the equalSet to set
	 */
	public void setEqualSet(Set<String> equalSet) {
		this.equalSet = equalSet;
	}
	/**
	 * @return the paraSummary
	 */
	public String getParaSummary() {
		return paraSummary;
	}
	/**
	 * @param paraSummary the paraSummary to set
	 */
	public void setParaSummary(String paraSummary) {
		this.paraSummary = paraSummary;
	}
	/**
	 * @return the equalStr
	 */
	public String getEqualStr() {
		return equalStr;
	}
	/**
	 * @param equalStr the equalStr to set
	 */
	public void setEqualStr(String equalStr) {
		this.equalStr = equalStr;
	}
	/**
	 * @return the noEqualSet
	 */
	public Set<String> getNoEqualSet() {
		return noEqualSet;
	}
	/**
	 * @param noEqualSet the noEqualSet to set
	 */
	public void setNoEqualSet(Set<String> noEqualSet) {
		this.noEqualSet = noEqualSet;
	}
	/**
	 * @return the noEqualStr
	 */
	public String getNoEqualStr() {
		return noEqualStr;
	}
	/**
	 * @param noEqualStr the noEqualStr to set
	 */
	public void setNoEqualStr(String noEqualStr) {
		this.noEqualStr = noEqualStr;
	}

	/**
	 * @return the tblPara
	 */
	public TblPara getTblPara() {
		return tblPara;
	}

	/**
	 * @param tblPara the tblPara to set
	 */
	public void setTblPara(TblPara tblPara) {
		this.tblPara = tblPara;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ParaCheck [equalSet=" + equalSet + ", equalStr=" + equalStr
				+ ", noEqualSet=" + noEqualSet + ", noEqualStr=" + noEqualStr
				+ ", reg=" + reg + ", maxVal=" + maxVal + ", minVal=" + minVal
				+ ", maxLen=" + maxLen + ", minLen=" + minLen
				+ ", paraSummary=" + paraSummary + ", tblPara=" + tblPara + "]";
	}
}

/**
 *
 */
package jp.co.csj.tools.utils.db.core.para;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Think
 *
 */
public class ParaStrCheck {
	private Set<String> equalValMap = new LinkedHashSet<String>();
	private ParaRegCheck paraRegCheck = new ParaRegCheck();
	private String paraMaxValCheck = "";
	private String paraMinValCheck = "";
	private int paraMaxLenVal = 0;
	private int paraMinLenVal = 0;
}

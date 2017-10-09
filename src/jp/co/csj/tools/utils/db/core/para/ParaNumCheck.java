/**
 *
 */
package jp.co.csj.tools.utils.db.core.para;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Think
 *
 */
public class ParaNumCheck {
	private Set<String> equalValMap = new LinkedHashSet<String>();
	private ParaRegCheck paraRegCheck = new ParaRegCheck();
	private BigDecimal paraMaxValCheck = new BigDecimal(0);
	private BigDecimal paraMinValCheck = new BigDecimal(0);
}

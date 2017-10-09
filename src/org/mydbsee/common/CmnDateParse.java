/**
 * 
 */
package org.mydbsee.common;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;

/**
 * @author Think
 *
 */
public class CmnDateParse {
	private SimpleDateFormat fmt = null;
	private ParsePosition pos = null;
	/**
	 * @return the fmt
	 */
	public SimpleDateFormat getFmt() {
		return fmt;
	}
	/**
	 * @param fmt the fmt to set
	 */
	public void setFmt(SimpleDateFormat fmt) {
		this.fmt = fmt;
	}
	/**
	 * @return the pos
	 */
	public ParsePosition getPos() {
		return pos;
	}
	/**
	 * @param pos the pos to set
	 */
	public void setPos(ParsePosition pos) {
		this.pos = pos;
	}
	
}

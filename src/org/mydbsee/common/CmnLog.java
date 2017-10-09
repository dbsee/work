package org.mydbsee.common;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class CmnLog {
	public static Logger logger = LogManager.getLogger(CmnLog.class.getName());
	public static void main(String[] args) {
		logger.debug("aaaaaaaaaaa");
	}

}

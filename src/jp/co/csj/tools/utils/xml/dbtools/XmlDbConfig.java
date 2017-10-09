/**
 *
 */
package jp.co.csj.tools.utils.xml.dbtools;

import java.util.TreeMap;

/**
 * @author Think
 *
 */
public class XmlDbConfig {
	private String name = "";
	private String defualtSelect = "";
	private String maxRecord = "";
	private TreeMap<String,XmlDb> xmlDbMap = new TreeMap<String, XmlDb>();
	private String[] viewDbType;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**

	/**
	 * @return the xmlDbMap
	 */
	public TreeMap<String, XmlDb> getXmlDbMap() {
		return xmlDbMap;
	}
	/**
	 * @param xmlDbMap the xmlDbMap to set
	 */
	public void setXmlDbMap(TreeMap<String, XmlDb> xmlDbMap) {
		this.xmlDbMap = xmlDbMap;
	}

	/**
	 * @return the viewDbType
	 */
	public String[] getViewDbType() {
		return viewDbType;
	}
	/**
	 * @param viewDbType the viewDbType to set
	 */
	public void setViewDbType(String[] viewDbType) {
		this.viewDbType = viewDbType;
	}
	/**
	 * @return the defualtSelect
	 */
	public String getDefualtSelect() {
		return defualtSelect;
	}
	/**
	 * @param defualtSelect the defualtSelect to set
	 */
	public void setDefualtSelect(String defualtSelect) {
		this.defualtSelect = defualtSelect;
	}
	public String getMaxRecord() {
		return maxRecord;
	}
	public void setMaxRecord(String maxRecord) {
		this.maxRecord = maxRecord;
	}

}

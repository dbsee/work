/**
 *
 */
package jp.co.csj.tools.utils.db.convert;

import java.util.HashMap;

/**
 * @author Think
 *
 */
public class ConvertDb {
	private HashMap<String, String> typeMap = new HashMap<String, String>();

	/**
	 * @return the typeMap
	 */
	public HashMap<String, String> getTypeMap() {
		return typeMap;
	}

	/**
	 * @param typeMap the typeMap to set
	 */
	public void setTypeMap(HashMap<String, String> typeMap) {
		this.typeMap = typeMap;
	}

}

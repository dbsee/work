/**
 *
 */
package jp.co.csj.tools.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Think
 *
 */
public class CsjLinkedMap<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = 1L;
	public static final int MAP_BEGIN = 0;
	public static final int MAP_KEY_LEFT = -2;
	public static final int MAP_KEY_RIGHT = -1;

	public static void main(String[] args) {
		CsjLinkedMap<String, String> map = new CsjLinkedMap<String, String>();
		map.put("a6", "aaa");
		map.put("a2", "bbb");
		map.put("a5", "ccc");
		map.put("a4", "ddd");
//		sortByKey(map, true);
//		map.removeByPos(1);
	}

	public List<K> toKeyList() {
		LinkedHashMap<K, V> tMap = new LinkedHashMap<K, V>();
		List<K> keyList = new ArrayList<K>();
		for (java.util.Map.Entry<K, V> entry : this.entrySet()) {
			keyList.add(entry.getKey());
		}
		return keyList;
	}

	public List<V> toValList(boolean isHaveNull) {
		LinkedHashMap<K, V> tMap = new LinkedHashMap<K, V>();
		List<V> valList = new ArrayList<V>();
		if (isHaveNull) {
			for (java.util.Map.Entry<K, V> entry : this.entrySet()) {
				valList.add(entry.getValue());
			}
		} else {
			for (java.util.Map.Entry<K, V> entry : this.entrySet()) {
				if (entry.getValue() != null) {
					valList.add(entry.getValue());
				}
			}
		}
		return valList;
	}

	public void subMap(int startPos, int endPos) {
		LinkedHashMap<K, V> tMap = new LinkedHashMap<K, V>();
		int index = 0;
		for (java.util.Map.Entry<K, V> entry : this.entrySet()) {
			if (startPos <= index && index < endPos) {
				tMap.put(entry.getKey(), entry.getValue());
			}
			index++;
		}
		this.clear();
		this.putAll(tMap);
	}

	public void converMap() {
		LinkedHashMap<K, V> tMap = new LinkedHashMap<K, V>();
		List<K> keyList = new ArrayList<K>();
		List<V> valList = new ArrayList<V>();
		for (java.util.Map.Entry<K, V> entry : this.entrySet()) {
			keyList.add(entry.getKey());
			valList.add(entry.getValue());
		}
		for (int i = keyList.size() - 1; i >= 0; i--) {
			tMap.put(keyList.get(i), valList.get(i));
		}
		this.clear();
		this.putAll(tMap);
	}

	public V getValByPos(int pos) {
		int index = 0;
		V obj = null;
		for (java.util.Map.Entry<K, V> entry : this.entrySet()) {
			if (index == pos) {
				obj = entry.getValue();
				break;
			}
			index++;
		}
		return obj;
	}

	public K getKeyByPos(int pos) {
		int index = 0;
		K obj = null;
		for (java.util.Map.Entry<K, V> entry : this.entrySet()) {
			if (index == pos) {
				obj = entry.getKey();
				break;
			}
			index++;
		}
		return obj;
	}
//	public boolean removeAllExceptByKey(String key) {
//		boolean retVal = true;
//		if (key == null) {
//			retVal = false;
//
//		} else {
//
//		}
//		return retVal;
//	}
	public boolean removeByPos(int pos) {
		boolean retVal = true;
		K obj = getKeyByPos(pos);
		if (obj == null) {
			retVal = false;

		} else {
			remove (obj);
		}
		return retVal;
	}
	public void insVal(K newKey, V newVal, K oldKey, int pos) {
		LinkedHashMap<K, V> tMap = new LinkedHashMap<K, V>();
		if (pos == MAP_BEGIN) {
			tMap.put(newKey, newVal);
			tMap.putAll(this);
		} else if (pos == MAP_KEY_RIGHT) {
			if (this.containsKey(oldKey)) {
				for (java.util.Map.Entry<K, V> entry : this.entrySet()) {
					tMap.put(entry.getKey(), entry.getValue());
					if (oldKey.equals(entry.getKey())) {
						tMap.put(newKey, newVal);
					}
				}
			} else {
				tMap.putAll(this);
				tMap.put(newKey, newVal);
			}
		} else if (pos == MAP_KEY_LEFT) {
			if (this.containsKey(oldKey)) {
				for (java.util.Map.Entry<K, V> entry : this.entrySet()) {
					tMap.put(entry.getKey(), entry.getValue());
					if (oldKey.equals(entry.getKey())) {
						tMap.put(newKey, newVal);
					}
				}
			} else {
				tMap.put(newKey, newVal);
				tMap.putAll(this);
			}
		} else if (0 < pos && pos <= this.size()) {
			int index = 0;

				for (java.util.Map.Entry<K, V> entry : this.entrySet()) {
					tMap.put(entry.getKey(), entry.getValue());
					if (index == pos) {
						tMap.put(newKey, newVal);
					}
					index++;
				}
				if (index == pos) {
					tMap.put(newKey, newVal);
				}

		} else {
			System.err.println("map erro");
		}
		this.clear();
		this.putAll(tMap);
	}

	public static void sortByKey(CsjLinkedMap map, boolean isAsc) {
		List<Object> tLst = (List<Object>) map.toKeyList();
		if (tLst == null || tLst.size() == 0) {
			return;
		} else {
			Object obj = tLst.get(0);
			if (isAsc) {
				if (obj instanceof String) {
					for (int i = 0; i < tLst.size(); i++) {
						for (int j = i + 1; j < tLst.size(); j++) {
							String iVal = (String) tLst.get(i);
							String tiVal = iVal;
							String jVal = (String) tLst.get(j);
							if (iVal.compareTo(jVal) > 0) {
								tLst.set(i, jVal);
								tLst.set(j, tiVal);
							}
						}
					}
				} else if (obj instanceof Integer) {
					if (obj instanceof String) {
						for (int i = 0; i < tLst.size(); i++) {
							for (int j = i + 1; j < tLst.size(); j++) {
								int iVal = (Integer) tLst.get(i);
								int tiVal = iVal;
								int jVal = (Integer) tLst.get(j);
								if (iVal > jVal) {
									tLst.set(i, jVal);
									tLst.set(j, tiVal);
								}
							}
						}
					}
				}
			} else {

				if (obj instanceof String) {
					for (int i = 0; i < tLst.size(); i++) {
						for (int j = i + 1; j < tLst.size(); j++) {
							String iVal = (String) tLst.get(i);
							String tiVal = iVal;
							String jVal = (String) tLst.get(j);
							if (iVal.compareTo(jVal) < 0) {
								tLst.set(i, jVal);
								tLst.set(j, tiVal);
							}
						}
					}
				} else if (obj instanceof Integer) {
					if (obj instanceof String) {
						for (int i = 0; i < tLst.size(); i++) {
							for (int j = i + 1; j < tLst.size(); j++) {
								int iVal = (Integer) tLst.get(i);
								int tiVal = iVal;
								int jVal = (Integer) tLst.get(j);
								if (iVal < jVal) {
									tLst.set(i, jVal);
									tLst.set(j, tiVal);
								}
							}
						}
					}
				}

			}
			LinkedHashMap<Object, Object> tMap = new LinkedHashMap<Object, Object>();
			for (Iterator<Object> iterator = tLst.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				tMap.put(object,map.get(object));
			}
			map.clear();
			map.putAll(tMap);
		}
	}
}

/**
 *
 */
package jp.co.csj.tools.core;

import java.util.Comparator;
import java.util.Map;

import org.mydbsee.common.CmnStrUtils;

import jp.co.csj.tools.utils.db.core.DbInfo;
import jp.co.csj.tools.utils.db.core.TableDbItem;

/**
 * @author Think
 *
 */
public class CsjLinkSort implements Comparator<Map<String, Object>> {

	public static final int SORT_ASC = 1;
	public static final int SORT_DESC = -1;

	private String sortKey1 = null;
	private int sortAsc1 = SORT_ASC;
	private String sortKey2 = null;
	private int sortAsc2 = SORT_ASC;
	private String sortKey3 = null;
	private int sortAsc3 = SORT_ASC;
	private String sortKey4 = null;
	private int sortAsc4 = SORT_ASC;

	/**
	 *
	 */
	public CsjLinkSort(String key1, int sort1) {
		this.sortKey1 = key1;
		this.sortAsc1 = sort1;
	}

	public CsjLinkSort(String key1, int sort1, String key2, int sort2) {
		this.sortKey1 = key1;
		this.sortAsc1 = sort1;
		this.sortKey2 = key2;
		this.sortAsc2 = sort2;
	}

	public CsjLinkSort(String key1, int sort1, String key2, int sort2,
			String key3, int sort3) {
		this.sortKey1 = key1;
		this.sortAsc1 = sort1;
		this.sortKey2 = key2;
		this.sortAsc2 = sort2;
		this.sortKey3 = key3;
		this.sortAsc3 = sort3;
	}

	public CsjLinkSort(String key1, int sort1, String key2, int sort2,
			String key3, int sort3, String key4, int sort4) {
		this.sortKey1 = key1;
		this.sortAsc1 = sort1;
		this.sortKey2 = key2;
		this.sortAsc2 = sort2;
		this.sortKey3 = key3;
		this.sortAsc3 = sort3;
		this.sortKey4 = key4;
		this.sortAsc4 = sort4;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Map<String, Object> t1, Map<String, Object> t2) {
		// TODO Auto-generated method stub

		try {
			String obj11= "";
			String obj12= "";
			if (t1.get(sortKey1) instanceof String) {

				if (obj11 == null || obj12 == null) {
					return -1;
				}
				if (obj11.equals(obj12)) {
					if (sortKey2 == null) {
						return 0;
					}
					String obj21 = (String) t1.get(sortKey2);
					String obj22 = (String) t2.get(sortKey2);
					if (obj21 == null || obj22 == null) {
						return -1;
					}
					if (obj21.equals(obj22)) {
						if (sortKey3 == null) {
							return 0;
						}
						String obj31 = (String) t1.get(sortKey3);
						String obj32 = (String) t2.get(sortKey3);
						if (obj31 == null || obj32 == null) {
							return -1;
						}
						if (obj31.equals(obj32)) {
							if (sortKey4 == null) {
								return 0;
							}
							String obj41 = (String) t1.get(sortKey4);
							String obj42 = (String) t2.get(sortKey4);
							if (obj41 == null || obj42 == null) {
								return -1;
							}
							if (obj41.equals(obj42)) {
								return 0;
							}
							return obj41.compareTo(obj42) * sortAsc4;
						}
						return obj31.compareTo(obj32) * sortAsc3;
					}
					return obj21.compareTo(obj22) * sortAsc2;
				}
				return obj11.compareTo(obj12) * sortAsc1;
			} else {

				TableDbItem item1 = (TableDbItem) (t1.get(sortKey1));
				TableDbItem item2 = (TableDbItem) (t2.get(sortKey1));
				obj11 = item1.getVal();
				obj12 = item2.getVal();

				if (obj11 == null || obj12 == null) {
					return -1;
				}
				if (item1.getType() == DbInfo.TABLE_COL_TYPE_NUM) {
					double dobj11 = CmnStrUtils.getDoubleVal(obj11);
					double dobj12 = CmnStrUtils.getDoubleVal(obj12);
					if (dobj11==dobj12) {
						if (sortKey2 == null) {
							return 0;
						}
						String obj21 =  ((TableDbItem) (t1.get(sortKey2))).getVal();
						String obj22 = ((TableDbItem) (t2.get(sortKey2))).getVal();
						if (obj21 == null || obj22 == null) {
							return -1;
						}
						double dobj21 = CmnStrUtils.getDoubleVal(obj21);
						double dobj22 = CmnStrUtils.getDoubleVal(obj22);
						if (dobj21==dobj22) {
							if (sortKey3 == null) {
								return 0;
							}
							String obj31 = ((TableDbItem) (t1.get(sortKey3))).getVal();
							String obj32 = ((TableDbItem) (t2.get(sortKey3))).getVal();
							if (obj31 == null || obj32 == null) {
								return -1;
							}
							double dobj31 = CmnStrUtils.getDoubleVal(obj31);
							double dobj32 = CmnStrUtils.getDoubleVal(obj32);
							if (dobj31==dobj32) {
								if (sortKey4 == null) {
									return 0;
								}
								String obj41 = ((TableDbItem) (t1.get(sortKey4))).getVal();
								String obj42 = ((TableDbItem) (t2.get(sortKey4))).getVal();
								if (obj41 == null || obj42 == null) {
									return -1;
								}
								double dobj41 = CmnStrUtils.getDoubleVal(obj41);
								double dobj42 = CmnStrUtils.getDoubleVal(obj42);
								if (dobj41==dobj42) {
									return 0;
								}
								return (dobj41<dobj42 ? -1 : 1) * sortAsc4;
							}
							return (dobj31<dobj32 ? -1 : 1) * sortAsc3;
						}
						return (dobj21<dobj22 ? -1 : 1) * sortAsc2;
					}
					return (dobj11<dobj12 ? -1 : 1) * sortAsc1;
				} else {
					if (obj11.equals(obj12)) {
						if (sortKey2 == null) {
							return 0;
						}
						String obj21 =  ((TableDbItem) (t1.get(sortKey2))).getVal();
						String obj22 = ((TableDbItem) (t2.get(sortKey2))).getVal();
						if (obj21 == null || obj22 == null) {
							return -1;
						}
						if (obj21.equals(obj22)) {
							if (sortKey3 == null) {
								return 0;
							}
							String obj31 = ((TableDbItem) (t1.get(sortKey3))).getVal();
							String obj32 = ((TableDbItem) (t2.get(sortKey3))).getVal();
							if (obj31 == null || obj32 == null) {
								return -1;
							}
							if (obj31.equals(obj32)) {
								if (sortKey4 == null) {
									return 0;
								}
								String obj41 = ((TableDbItem) (t1.get(sortKey4))).getVal();
								String obj42 = ((TableDbItem) (t2.get(sortKey4))).getVal();
								if (obj41 == null || obj42 == null) {
									return -1;
								}
								if (obj41.equals(obj42)) {
									return 0;
								}
								return obj41.compareTo(obj42) * sortAsc4;
							}
							return obj31.compareTo(obj32) * sortAsc3;
						}
						return obj21.compareTo(obj22) * sortAsc2;
					}
				}
				return obj11.compareTo(obj12) * sortAsc1;
			}
		} catch (Throwable e) {
			// TODO: handle exception
		}
return 0;
	}

}

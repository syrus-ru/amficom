/*
 * $Id: ObjectResourceSorter.java,v 1.8 2004/08/19 08:20:48 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.Client.Resource;

import java.util.*;

/**
 * @version $Revision: 1.8 $, $Date: 2004/08/19 08:20:48 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public abstract class ObjectResourceSorter {

	public static final int		SORT_ASCENDING			= 0;
	public static final int		SORT_DESCENDING			= 1;

	List						list;

	public ObjectResourceSorter() {
		// empty constuctor
	}

	public ObjectResourceSorter(Collection collection) {
		setDataSet(collection);
	}

	public ObjectResourceSorter(List list) {
		this.list = list;
	}

	public ObjectResourceSorter(Map map) {
		setDataSet(map);
	}

	public List default_sort() {
		return sort(getSortedColumns()[0][0], SORT_ASCENDING);
	}

	public abstract long getLong(ObjectResource or, String column);

	public abstract String[][] getSortedColumns();

	public abstract String getString(ObjectResource or, String column);

	public void setDataSet(Collection collection) {
		this.list = new LinkedList();
		for (Iterator it = collection.iterator(); it.hasNext();) {
			this.list.add(it.next());
		}
	}

	public void setDataSet(List list) {
		this.list = list;
	}

	public void setDataSet(Map map) {
		this.list = new LinkedList();
		for (Iterator it = map.values().iterator(); it.hasNext();) {
			this.list.add(it.next());
		}
	}

	public List sort(String column, int sortdir) {
		List sorted_dataSet = new LinkedList();

		int size = list.size();
		String sort_type = "";
		ObjectResource temp_or = null;
		long long_obj_sorter = 0;
		String string_obj_sorter = "";

		long curr_long = -9223372036854775808L;
		String curr_string = "";

		String[][] sorted_columns = getSortedColumns();

		for (int i = 0; i < sorted_columns.length; i++) {
			if (column.equals(sorted_columns[i][0])) {
				sort_type = sorted_columns[i][1];
				break;
			}
			if (i == sorted_columns.length - 1) {
				sorted_dataSet = list;
			}
		}

		for (int i = 0; i < size; i++) {
			curr_long = -9223372036854775808L;
			curr_string = "";
			for (Iterator it = list.iterator(); it.hasNext();) {
				ObjectResource or = (ObjectResource) it.next();
				if (sort_type.equals("long")) {
					long_obj_sorter = getLong(or, column);
					if (sortdir == SORT_ASCENDING) {
						if (long_obj_sorter < curr_long || curr_long == -9223372036854775808L) {
							curr_long = long_obj_sorter;
							temp_or = or;
						}
					}
					if (sortdir == SORT_DESCENDING) {
						if (long_obj_sorter > curr_long) {
							curr_long = long_obj_sorter;
							temp_or = or;
						}
					}
				} else if (sort_type.equals("string")) {
					string_obj_sorter = getString(or, column);
					if (sortdir == SORT_ASCENDING) {
						if (string_obj_sorter.compareTo(curr_string) < 0 || curr_string.equals("")) {
							curr_string = string_obj_sorter;
							temp_or = or;
						}
					}
					if (sortdir == SORT_DESCENDING) {
						if (string_obj_sorter.compareTo(curr_string) > 0 || curr_string.equals("")) {
							curr_string = string_obj_sorter;
							temp_or = or;
						}
					}
				}

			}
			sorted_dataSet.add(temp_or);
			list.remove(temp_or);
		}
		return sorted_dataSet;
	}

}
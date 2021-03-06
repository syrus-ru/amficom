package com.syrus.AMFICOM.client_.general.ui_;

import java.util.Comparator;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * ColumnSorter used for sorting elements with aid of Collections i.g.
 * <code>Collections.sort(list, new ColumnSorter(controller, key, ascending))</code>
 *
 * @version $Revision: 1.6 $, $Date: 2005/05/18 14:01:19 $
 * @author $Author: bass $
 * @module generalclient_v1
 */
public class ColumnSorter implements Comparator {

	/**
	 * if true, sort results will be presented in ascending order, and in
	 * descending order otherwise.
	 */
	private boolean				ascending;

	/**
	 * key of the field at ObjectResourceController.
	 * see {@link ObjectResourceController#getKeys()}
	 */
	private String				key;

	/**
	 * ObjectResourceController of Model (ObjectResource) will be used for sorting.
	 * see {@link ObjectResourceController}
	 */
	private ObjectResourceController	controller;

	/**
	 * @param controller see {@link #controller}
	 * @param key see {@link #key}
	 * @param ascending see {@link #ascending}
	 */
	public ColumnSorter(ObjectResourceController controller, String key, boolean ascending) {
		this.controller = controller;
		this.key = key;
		this.ascending = ascending;
	}

	public int compare(Object a, Object b) {
		int result = 0;
		Object o1 = this.controller.getValue(a, this.key);
		Object o2 = this.controller.getValue(b, this.key);

		// Treat empty strains like nulls
		if (o1 instanceof String && ((String) o1).length() == 0) {
			o1 = null;
		}
		if (o2 instanceof String && ((String) o2).length() == 0) {
			o2 = null;
		}

		// Sort nulls so they appear last, regardless
		// of sort order
		if (o1 == null && o2 == null) {
			result = 0;
		} else if (o1 == null) {
			result = 1;
		} else if (o2 == null) {
			result = -1;
		} else if (o1 instanceof Comparable) {
			if (this.ascending) {
				result = ((Comparable) o1).compareTo(o2);
			} else
				result = ((Comparable) o2).compareTo(o1);

		} else {
			if (this.ascending) {
				result = o1.toString().compareTo(o2.toString());
			} else
				result = o2.toString().compareTo(o1.toString());

		}
		return result;
	}
}

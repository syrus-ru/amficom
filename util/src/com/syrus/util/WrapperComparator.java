/*-
* $Id: WrapperComparator.java,v 1.8 2006/06/21 08:14:16 saa Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.util;

import java.util.Comparator;


/**
 * Wrapper comparator
 * @version $Revision: 1.8 $, $Date: 2006/06/21 08:14:16 $
 * @author $Author: saa $
 * @author Vladimir Dolzhenko
 * @module util
 */
public final class WrapperComparator<T> implements Comparator<T> {
	
	private final Wrapper<? super T> wrapper;
	private final String key;
	private final boolean ascend;
	
	/**
	 * @param wrapper object wrapper
	 * @param key key for wrapper
	 * @param ascend ascend compare
	 */
	public WrapperComparator(final Wrapper<? super T> wrapper, final String key, final boolean ascend) {
		if (!wrapper.getKeys().contains(key)) {
			throw new IllegalArgumentException("Key '" + key + "' is not supported by wrapper");
		}
		this.wrapper = wrapper;
		this.key = key;
		this.ascend = ascend;
	}	

	/**
	 * WrapperComparator with ascend
	 * 
	 * @param wrapper
	 * @param key
	 */
	public WrapperComparator(final Wrapper<T> wrapper, final String key) {
		this(wrapper, key, true);
	}

	public int compare(final T object1, final T object2) {
		final Object value1 = this.wrapper.getValue(object1, this.key);
		final Object value2 = this.wrapper.getValue(object2, this.key);

		Comparable comparable1;
		if (value1 instanceof Comparable) {
			comparable1 = (Comparable) value1;
		} else {
			comparable1 = value1 != null ? value1.toString() : "";
		}

		Comparable comparable2;
		if (value2 instanceof Comparable) {
			comparable2 = (Comparable) value2;
		} else {
			comparable2 = value2 != null ? value2.toString() : "";
		}

		return (this.ascend ? 1 : -1) * comparable1.compareTo(comparable2);
	}

}


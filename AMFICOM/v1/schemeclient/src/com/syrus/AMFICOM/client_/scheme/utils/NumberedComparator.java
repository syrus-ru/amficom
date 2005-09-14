/*-
 * $Id: NumberedComparator.java,v 1.1 2005/09/14 10:19:06 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.utils;

import java.util.Comparator;

import com.syrus.util.Wrapper;

public class NumberedComparator<T> implements Comparator<T> {
	
	private Wrapper<T> wrapper;	
	private String key;
	private boolean ascend;
	
	public NumberedComparator(final Wrapper<T> wrapper,
      final String key, boolean ascend) {
		this.wrapper = wrapper;
		this.key = key;
		this.ascend = ascend;
	}
	
	public NumberedComparator(final Wrapper<T> wrapper,
      final String key) {
		this(wrapper, key, true);
	}

	
	public int compare(T object1, T object2) {
		final Object value1 = this.wrapper.getValue(object1, this.key);
		final Object value2 = this.wrapper.getValue(object2, this.key);
		
		if (value1 instanceof String && value2 instanceof String) {
			try {
				double d1 = Double.parseDouble(ClientUtils.parseNumberedName((String)value1));
				double d2 = Double.parseDouble(ClientUtils.parseNumberedName((String)value2));
				return (this.ascend ? 1 : -1) * (int)(d1 - d2);
			} catch (NumberFormatException e) {
				// ignore
			}	
		}
		
		Comparable comparable1;
		if (value1 instanceof Comparable) {
			comparable1 = (Comparable)value1;
		} else {
			comparable1 = value1 != null ? value1.toString() : "";
		}		

		Comparable comparable2;
		if (value2 instanceof Comparable) {
			comparable2 = (Comparable)value2;
		} else {
			comparable2 = value2 != null ? value2.toString() : "";
		}		

		return (this.ascend ? 1 : -1) * comparable1.compareTo(comparable2);
	}
}

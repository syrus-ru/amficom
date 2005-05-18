/*-
* $Id: WrapperComparator.java,v 1.2 2005/05/18 10:49:17 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.util;

import java.util.Comparator;


/**
 * Wrapper comparator
 * @version $Revision: 1.2 $, $Date: 2005/05/18 10:49:17 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module util
 */
public final class WrapperComparator implements Comparator {

	
	private Wrapper wrapper;	
	private String key;
	private boolean ascend;
	
	/**
	 * @param wrapper object wrapper
	 * @param key key for wrapper
	 * @param ascend ascend compare
	 */
	public WrapperComparator(final Wrapper wrapper, final String key, final boolean ascend) {
		this.wrapper = wrapper;
		this.key = key;
		this.ascend = ascend;
	}	

	/**
	 * WrapperComparator with ascend
	 * @param wrapper
	 * @param key
	 */
	public WrapperComparator(final Wrapper wrapper, final String key) {
		this(wrapper, key, true);
	}

	public int compare(	final Object object1,
	                   	final Object object2) {
		final Object value1 = this.wrapper.getValue(object1, this.key);
		final Object value2 = this.wrapper.getValue(object2, this.key);
		
		Comparable comparable1;
		if (value1 instanceof Comparable) {
			comparable1 = (Comparable)value1;
		} else
			comparable1 = value1.toString();		
		
		return (this.ascend ? 1 : -1) * comparable1.compareTo(value2);
	}

}


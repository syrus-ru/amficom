/*-
 * $Id: TestLRUMap.java,v 1.1 2005/09/19 11:02:49 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @version $Revision: 1.1 $, $Date: 2005/09/19 11:02:49 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class TestLRUMap extends TestCase {

	public TestLRUMap(final String name) {
		super(name);
	}

	public void testAdd() {
		final int size = 10;
		final LRUMap<String, Integer> lruMap = new LRUMap<String, Integer>(size);
		for (int i = 0; i < size - 5; i++) {
			final String key = "key_" + i;
			final Integer value = new Integer(i);
			lruMap.put(key, value);
		}
		System.out.println("LRUMap: " + lruMap);

		for (final Iterator<Integer> it = lruMap.iterator(); it.hasNext();) {
			final Integer value = it.next();
			it.remove();
			System.out.println("Value: " + value);
		}

		System.out.println("LRUMap: " + lruMap);
		for (final Iterator<Integer> it = lruMap.iterator(); it.hasNext();) {
			final Integer value = it.next();
			System.out.println("Remained: " + value);
		}
	}
}

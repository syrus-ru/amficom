/*
 * $Id: LRUMapTest.java,v 1.1 2004/11/10 16:20:04 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util;

import java.util.Iterator;

/**
 * @version $Revision: 1.1 $, $Date: 2004/11/10 16:20:04 $
 * @author $Author: bob $
 * @module util/test
 */
public class LRUMapTest {

	public static void main(String[] args) {
		LRUMap map = new LRUMap();
		map.put("1", "first");
		map.put("2", "second");
		map.put("1", "1st");

		for (Iterator it = map.iterator(); it.hasNext();) {
			String value = (String) it.next();
			System.out.println("value:'" + value + "'");
		}
	}

}

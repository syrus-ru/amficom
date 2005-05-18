/*
 * $Id: LRUMapTest.java,v 1.3 2005/05/18 10:49:17 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.util.Iterator;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/18 10:49:17 $
 * @author $Author: bass $
 * @module util
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

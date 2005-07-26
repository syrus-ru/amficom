/*
 * $Id: LRUMapTest.java,v 1.4 2005/07/26 19:39:52 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.util.Iterator;

/**
 * @version $Revision: 1.4 $, $Date: 2005/07/26 19:39:52 $
 * @author $Author: bass $
 * @module util
 */
public class LRUMapTest {

	public static void main(String[] args) {
		LRUMap<String, String> map = new LRUMap<String, String>();
		map.put("1", "first");
		map.put("2", "second");
		map.put("1", "1st");

		for (Iterator it = map.iterator(); it.hasNext();) {
			String value = (String) it.next();
			System.out.println("value:'" + value + "'");
		}
	}

}

/*
 * $Id: LRUMapTest.java,v 1.2 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.util.Iterator;

/**
 * @version $Revision: 1.2 $, $Date: 2005/03/04 08:05:49 $
 * @author $Author: bass $
 * @module util
 */
public class LRUMapTest {

	public static void main(String[] args) {
		LRUMap map = new LRUMap();
		map.put("1", "first");  //$NON-NLS-1$//$NON-NLS-2$
		map.put("2", "second");  //$NON-NLS-1$//$NON-NLS-2$
		map.put("1", "1st"); //$NON-NLS-1$ //$NON-NLS-2$

		for (Iterator it = map.iterator(); it.hasNext();) {
			String value = (String) it.next();
			System.out.println("value:'" + value + "'");  //$NON-NLS-1$//$NON-NLS-2$
		}
	}

}

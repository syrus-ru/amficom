/*
 * $Id: DatabaseStringTestCase.java,v 1.2 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.database;

import junit.framework.TestCase;


/**
 * @version $Revision: 1.2 $, $Date: 2005/03/04 08:05:49 $
 * @author $Author: bass $
 * @module util
 */
public class DatabaseStringTestCase extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(DatabaseStringTestCase.class);
	}
	
	public void testConversions(){
		String s = "test 'by me'"; //$NON-NLS-1$
		System.out.println(s);
		String s1 = DatabaseString.toQuerySubString(s);
		System.out.println(s1);
		String s2 = DatabaseString.fromQuerySubString(s1);
		System.out.println(s2);
		assertEquals(s, s2);
	}

}

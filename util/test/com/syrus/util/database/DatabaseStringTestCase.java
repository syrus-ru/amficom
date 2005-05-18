/*
 * $Id: DatabaseStringTestCase.java,v 1.3 2005/05/18 10:49:18 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.database;

import junit.framework.TestCase;


/**
 * @version $Revision: 1.3 $, $Date: 2005/05/18 10:49:18 $
 * @author $Author: bass $
 * @module util
 */
public class DatabaseStringTestCase extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(DatabaseStringTestCase.class);
	}
	
	public void testConversions(){
		String s = "test 'by me'";
		System.out.println(s);
		String s1 = DatabaseString.toQuerySubString(s);
		System.out.println(s1);
		String s2 = DatabaseString.fromQuerySubString(s1);
		System.out.println(s2);
		assertEquals(s, s2);
	}

}

/*
 * $Id: DatabaseStringTestCase.java,v 1.1 2004/10/27 12:20:17 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.util.database;

import junit.framework.TestCase;


/**
 * @version $Revision: 1.1 $, $Date: 2004/10/27 12:20:17 $
 * @author $Author: bob $
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

/*
 * $Id: UniCommonTest.java,v 1.1 2006/01/20 17:08:24 saa Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;



/**
 * @version $Revision: 1.1 $, $Date: 2006/01/20 17:08:24 $
 * @author $Author: saa $
 * @module test
 */
public class UniCommonTest extends CommonTest {
	private static TestContext test = null;

	@Override
	void oneTimeSetUp() {
		super.oneTimeSetUp();
	}

	@Override
	void oneTimeTearDown() {
		super.oneTimeTearDown();
	}

	public static final void use(TestContext env) {
		notUse();
		test = env;
		test.setUp();
	}

	public static final void notUse() {
		if (test != null) {
			test.tearDown();
			test = null;
		}
	}
}

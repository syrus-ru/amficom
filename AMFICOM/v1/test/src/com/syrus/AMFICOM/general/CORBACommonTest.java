/*
 * $Id: CORBACommonTest.java,v 1.2 2005/06/22 14:45:47 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.2 $, $Date: 2005/06/22 14:45:47 $
 * @author $Author: arseniy $
 * @module test
 */
public abstract class CORBACommonTest extends CommonTest {
	public static final String KEY_SERVER_HOST_NAME = "ServerHostName";

	public static final String SERVER_HOST_NAME = "localhost";

	void oneTimeSetUp() {
		super.oneTimeSetUp();
		
	}

	void oneTimeTearDown() {
		
		super.oneTimeTearDown();
	}
}

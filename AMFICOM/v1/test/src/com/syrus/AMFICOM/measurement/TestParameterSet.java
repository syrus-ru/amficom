/*
 * $Id: TestParameterSet.java,v 1.1 2005/06/19 18:41:53 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import junit.framework.Test;

import com.syrus.AMFICOM.general.DatabaseCommonTest;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/19 18:41:53 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestParameterSet extends DatabaseCommonTest {

	public TestParameterSet(String name) {
		super(name);
	}

	public static Test suite() {
		addTestSuite(TestParameterSet.class);
		return createTestSetup();
	}
}

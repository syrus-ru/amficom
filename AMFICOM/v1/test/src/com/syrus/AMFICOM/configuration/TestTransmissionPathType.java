/*
 * $Id: TestTransmissionPathType.java,v 1.8 2006/02/17 12:04:55 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.8 $, $Date: 2006/02/17 12:04:55 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestTransmissionPathType extends TestCase {

	public TestTransmissionPathType(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestTransmissionPathType.class);
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		TransmissionPathType transmissionPathType = TransmissionPathType.createInstance(userId,
				"reflectometry",
				"For tests",
				"tptyp");

		StorableObjectPool.flush(transmissionPathType, userId, true);
	}

}

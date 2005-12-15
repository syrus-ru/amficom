/*
 * $Id: TestTransmissionPathType.java,v 1.7 2005/12/15 14:16:36 arseniy Exp $
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
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.7 $, $Date: 2005/12/15 14:16:36 $
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
		TransmissionPathType transmissionPathType = TransmissionPathType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				"reflectometry",
				"For tests",
				"tptyp");

		StorableObjectPool.flush(transmissionPathType, DatabaseCommonTest.getSysUser().getId(), true);
	}

}

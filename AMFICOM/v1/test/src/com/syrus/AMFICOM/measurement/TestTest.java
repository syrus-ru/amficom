/*-
 * $Id: TestTest.java,v 1.4 2006/02/17 12:04:55 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.4 $, $Date: 2006/02/17 12:04:55 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class TestTest extends TestCase {

	public TestTest(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestTest.class);
		return commonTest.createTestSetup();
	}

	public void testAddStops() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final Identifier testId = new Identifier("Test_321");//146085512912830785
		final com.syrus.AMFICOM.measurement.Test test = StorableObjectPool.getStorableObject(testId, true);
		System.out.println("Loaded: " + test.toString() + ", status: " + test.getStatus().value());
		System.out.println("Stops: " + test.getStoppingMap());

		test.addStopping("Просто надо");

		System.out.println("Stops: " + test.getStoppingMap());

		StorableObjectPool.flush(test, userId, false);
	}

}

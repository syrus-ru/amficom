/*
 * $Id: TestLoadAnalysis.java,v 1.1 2006/01/16 16:04:35 saa Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.1 $, $Date: 2006/01/16 16:04:35 $
 * @author $Author: saa $
 * @module test
 */
public final class TestLoadAnalysis extends TestCase {

	public TestLoadAnalysis(final String name) {
		super(name);
	}

	public static Test suite() {
		return suiteCorba();
	}

	public static Test suiteDb() {
		final CommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestLoadAnalysis.class);
		return commonTest.createTestSetup();
	}

	public static Test suiteCorba() {
		final CommonTest commonTest = new CORBACommonTest();
		commonTest.addTestSuite(TestLoadAnalysis.class);
		return commonTest.createTestSetup();
	}

	public void testLoadAnalysis() throws ApplicationException {
		Identifier mId = new Identifier("Measurement_8686");
		LinkedIdsCondition lic =
			new LinkedIdsCondition(mId, ObjectEntities.ANALYSIS_CODE);
		boolean tooLong = false;
		for (int i = 0; i < 20; i++) {
			StorableObjectPool.clean();
			long t1 = System.nanoTime();
			StorableObjectPool.getStorableObjectsByCondition(lic, true);
			long t2 = System.nanoTime();
			final double timeMs = (t2 - t1) / 1e6;
			System.out.println("test " + i + ": SO received in " + timeMs + " ms"); // сюда не доходит
			tooLong |= timeMs > 1000.0; // require every result loaded within 1000 ms
		}
		assertFalse(tooLong);
	}
}

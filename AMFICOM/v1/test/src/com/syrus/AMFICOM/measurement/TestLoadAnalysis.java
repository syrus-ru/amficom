/*
 * $Id: TestLoadAnalysis.java,v 1.5 2006/06/06 15:52:38 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.5 $, $Date: 2006/06/06 15:52:38 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestLoadAnalysis extends TestCase {

	public TestLoadAnalysis(final String name) {
		super(name);
	}

	public static Test suite() {
		return suiteCorba();
//		return suiteDb();
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
		Identifier mId = Identifier.valueOf("Measurement_8686");
		LinkedIdsCondition lic =
			new LinkedIdsCondition(mId, ObjectEntities.ANALYSIS_CODE);
		boolean tooLong = false;
		double dtSum = 0.0;
		final int steps = 20;
		for (int i = 0; i < steps; i++) {
			StorableObjectPool.clean();
			long t1 = System.nanoTime();
			final Set<StorableObject> objects =
				StorableObjectPool.getStorableObjectsByCondition(lic, true);
			long t2 = System.nanoTime();
			final double timeMs = (t2 - t1) / 1e6;
			System.out.println("test " + i + ": " + objects.size()
					+ " Storable Objects received in " + timeMs + " ms");
			tooLong |= timeMs > 1000.0; // require every result loaded within 1000 ms
			assertTrue(timeMs < (i == 0 ? 7000.0 : 2500.0));
			dtSum += timeMs;
		}
		System.out.println("average dt = " + dtSum / steps + " ms");
		assertFalse(tooLong);
	}
}

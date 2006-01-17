/*
 * $Id: TestLoadAnalysisResult.java,v 1.3 2006/01/17 10:43:56 bob Exp $
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
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.reflectometry.MeasurementReflectometryAnalysisResult;
import com.syrus.io.DataFormatException;

/**
 * @version $Revision: 1.3 $, $Date: 2006/01/17 10:43:56 $
 * @author $Author: bob $
 * @module test
 */
public final class TestLoadAnalysisResult extends TestCase {

	public TestLoadAnalysisResult(final String name) {
		super(name);
	}

	public static Test suite() {
		return suiteCorba();
	}

	public static Test suiteDb() {
		final CommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestLoadAnalysisResult.class);
		return commonTest.createTestSetup();
	}

	public static Test suiteCorba() {
		final CommonTest commonTest = new CORBACommonTest();
		commonTest.addTestSuite(TestLoadAnalysisResult.class);
		return commonTest.createTestSetup();
	}

	public void testLoadAnalysis() throws ApplicationException, DataFormatException {
		StorableObjectPool.clean();
		
		final double expectedTime = 1000.0;
		
		
		final Identifier measurementId = new Identifier("Measurement_8686");
		
		boolean tooLong = false;
		for (int i = 0; i < 20; i++) {
			long t1 = System.nanoTime();
			StorableObjectPool.clean();
		
			final Measurement measurement = 
				StorableObjectPool.getStorableObject(measurementId, true);
			
			final MeasurementReflectometryAnalysisResult result =
				new MeasurementReflectometryAnalysisResult(measurement);
			
			long t2 = System.nanoTime();
			
			final double timeMs = (t2 - t1) / 1e6;
			System.out.println("test " + i + ": SO received in " + timeMs + " ms");
			tooLong |= timeMs > expectedTime;
		}
		assertFalse(tooLong);
	}
}

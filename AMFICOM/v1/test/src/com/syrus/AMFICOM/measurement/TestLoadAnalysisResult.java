/*
 * $Id: TestLoadAnalysisResult.java,v 1.4.2.1 2006/06/06 15:47:28 arseniy Exp $
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
 * @version $Revision: 1.4.2.1 $, $Date: 2006/06/06 15:47:28 $
 * @author $Author: arseniy $
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
		
		
		final Identifier measurementId = Identifier.valueOf("Measurement_8686");
		
		boolean tooLongLoadMeasurement = false;
		boolean tooLongCreateMRAR = false;
		for (int i = 0; i < 20; i++) {
			StorableObjectPool.clean();
		
			long t0 = System.nanoTime();

			// load Measurement (by id)
			final Measurement measurement = 
				StorableObjectPool.getStorableObject(measurementId, true);
			long t1 = System.nanoTime();

			// create MRAR by given Measurement and loaded Analysis (via SOP)
			final MeasurementReflectometryAnalysisResult result =
				new MeasurementReflectometryAnalysisResult(measurement);
			long t2 = System.nanoTime();

			final double dt1 = (t1 - t0) / 1e6;
			final double dt2 = (t2 - t1) / 1e6;
			System.out.println("test " + i + ": "
					+ "SO received in " + dt1 + " ms, "
					+ "MRAR created in " + dt2 + " ms");
			tooLongLoadMeasurement |= dt1 > expectedTime;
			tooLongCreateMRAR |= dt2 > expectedTime;
		}
		assertFalse("MRAR max creation time is too high", tooLongCreateMRAR);
		assertFalse("SO Measurement load time is too high", tooLongLoadMeasurement);
	}
}

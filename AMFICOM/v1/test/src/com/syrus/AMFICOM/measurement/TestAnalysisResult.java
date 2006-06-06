/*
 * $Id: TestAnalysisResult.java,v 1.5.2.2 2006/06/06 15:47:28 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSISRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_ALARMS;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.io.DataFormatException;

/**
 * @version $Revision: 1.5.2.2 $, $Date: 2006/06/06 15:47:28 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestAnalysisResult extends TestCase {

	public TestAnalysisResult(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestAnalysisResult.class);
		return commonTest.createTestSetup();
	}

//	public void testLoadAnalysisResult() {
//		final Identifier
//	}

	public void testLoadResult() throws ApplicationException, DataFormatException {
		final long t0 = System.nanoTime();
		final Identifier analysisId = Identifier.valueOf("Analysis_63");
		//Analysis analysis = StorableObjectPool.getStorableObject(analysisId, true);
		final LinkedIdsCondition lic = new LinkedIdsCondition(analysisId, ANALYSISRESULTPARAMETER_CODE);
		final long t1 = System.nanoTime();
		final Set<AnalysisResultParameter> analysisResultParameters = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		final long t2 = System.nanoTime();

		System.out.println("Result has " + analysisResultParameters.size() + " parameters");
		for (final AnalysisResultParameter analysisResultParameter : analysisResultParameters) {
			final String codename = analysisResultParameter.getTypeCodename();
			System.out.println("Parameter codename: " + codename);
			if (codename.equals(DADARA_ALARMS.stringValue())) {
				ReflectogramMismatchImpl[] alarms = ReflectogramMismatchImpl.alarmsFromByteArray(analysisResultParameter.getValue());
				System.out.println("\t" + "Number of alarms: " + alarms.length);
				for (int j = 0; j < alarms.length; j++) {
					System.out.println("\t\t" + "alarm [" + j + "] == " + alarms[j]);
				}
			}
		}
		System.out.println("id & lic: " + (t1 - t0) / 1e6 + " ms");
		System.out.println("getSOByC: " + (t2 - t1) / 1e6 + " ms");
	}
}
